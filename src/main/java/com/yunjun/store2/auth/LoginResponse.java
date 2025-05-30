package com.yunjun.store2.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private final Jwt accessToken;
    private final Jwt refreshToken;
}
