package com.yunjun.store2.services.impls;

import com.yunjun.store2.dtos.JwtResponse;
import com.yunjun.store2.services.JwtTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    @Value("${spring.jwt.secret}")
    private String secret;

    private static final long tokenExpiration = 84600;
    public JwtResponse generateToken(String email) {
        var token = Jwts.builder()
                .setSubject(email)
                .issuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 1000 * tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
        return new JwtResponse(token);
    }

    /**
     * @param token
     * @return
     */
    @Override
    public boolean validate(String token) {
        try {
            var claim = getClaims(token);
            return (claim.getExpiration().after(new Date()));
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SecurityException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    /**
     * @return
     */
    @Override
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }
}
