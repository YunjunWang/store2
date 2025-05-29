package com.yunjun.store2.auth;

import com.yunjun.store2.users.UserDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    Long getUserId();
    LoginResponse loginUser(LoginRequest request, HttpServletResponse response);
    UserDto getCurrentUser();
    Jwt refreshAccessToken(String refreshToken);
}
