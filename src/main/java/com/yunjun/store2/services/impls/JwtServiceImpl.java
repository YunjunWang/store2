package com.yunjun.store2.services.impls;

import com.yunjun.store2.config.JwtConfig;
import com.yunjun.store2.dtos.UserDto;
import com.yunjun.store2.services.Jwt;
import com.yunjun.store2.services.JwtService;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    private final JwtConfig jwtConfig;
    /**
     * @param token
     * @return
     */
    @Override
    public Jwt parse(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * @return
     */
    @Override
    public Jwt generateAccessToken(UserDto userDto) {
        return generateToken(userDto, jwtConfig.getAccessTokenExpiration());
    }

    /**
     * @return
     */
    @Override
    public Jwt generateRefreshToken(UserDto userDto) {
        return generateToken(userDto, jwtConfig.getRefreshTokenExpiration());
    }

    private Jwt generateToken(UserDto userDto, long tokenExpiration) {
        var token = Jwts.claims()
                .subject(userDto.getId().toString())
                .add("name", userDto.getName())
                .add("email", userDto.getEmail())
                .add("role", userDto.getRole())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + 1000 * tokenExpiration))
                .build();
        return new Jwt(token, jwtConfig.getSecretKey());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

}
