package com.yunjun.store2.services.impls;

import com.yunjun.store2.dtos.JwtResponse;
import com.yunjun.store2.dtos.LoginResponse;
import com.yunjun.store2.dtos.UserDto;
import com.yunjun.store2.services.JwtTokenService;
import com.yunjun.store2.services.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 *  Access Token                                            Refresh Token
 *  - To get access to protected endpoints                  - To get a new access token
 *  - Short lived, <= 15 mins                               - Long lived, >= 7 days
 *  - Returned in the response body                         - Returned as an HttpOnly Cookie
 *  - Saved in the memory / localStorage                    - No access via JavaScript
 *  - Web browser localStorage is less safe                 - Much harder to steal
 */
@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final UserService userService;
    @Value("${spring.jwt.secret}")
    private String secret;

    public JwtTokenServiceImpl(UserService userService) {
        this.userService = userService;
    }

    public JwtResponse generateAccessToken(String email) {
        final long tokenExpiration = 84600;
        var token = Jwts.builder()
                .setSubject(email)
                .issuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 1000 * tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
        return new JwtResponse(token);
    }

    /**
     * @param userDto
     * @return
     */
    @Override
    public JwtResponse generateAccessToken(UserDto userDto) {
        final long tokenExpiration = 84600;
        var token = generateToken(userDto, tokenExpiration);
        return new JwtResponse(token);
    }

    /**
     * @param userDto
     * @return
     */
    @Override
    public String generateRefreshToken(UserDto userDto) {
        final long tokenExpiration = 604800;
        return generateToken(userDto, tokenExpiration);
    }

    private String generateToken(UserDto userDto, long tokenExpiration) {
        var token = Jwts.builder()
                .setSubject(userDto.getId().toString())
                .issuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 1000 * tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .claim("name", userDto.getName())
                .claim("email", userDto.getEmail())
                .compact();
        return token;
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

    /**
     * @param token
     * @return
     */
    @Override
    public Long getUserIdFromToken(String token) {
        var sub = getClaims(token).getSubject();
        return Long.parseLong(sub);
    }

    /**
     * @param token
     * @return
     */
    @Override
    public LoginResponse getUserFromToken(String token) {
        var claims = getClaims(token);
        var userId = claims.getSubject();
        var userName = claims.get("name", String.class);
        var email = claims.get("email", String.class);
        return new LoginResponse(Long.parseLong(userId),userName, email);
    }
}
