package com.yunjun.store2.services;

import com.yunjun.store2.users.Role;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;


@AllArgsConstructor
public class Jwt {
    private final Claims claims;
    private final SecretKey key;

    public boolean isExpired() {
        return (claims.getExpiration().before(new Date()));
    }

    public Long getUserId() {
        return Long.parseLong(claims.getSubject());
    }

    public Role getRole() {
        return Role.valueOf(claims.get("role").toString());
    }

    public String toString() {
        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }
}
