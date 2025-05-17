package com.yunjun.store2.services.impls;

import com.yunjun.store2.dtos.JwtResponse;
import com.yunjun.store2.services.JwtTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    private static final long tokenExpiration = 84600;
    public JwtResponse generateToken(String email) {
        var token = Jwts.builder()
                .setSubject(email)
                .issuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 1000 * tokenExpiration))
                .signWith(Keys.hmacShaKeyFor("secrete".getBytes()))
                .compact();
        return new JwtResponse(token);
    }
}
