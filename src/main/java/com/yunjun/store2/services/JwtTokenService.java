package com.yunjun.store2.services;

import com.yunjun.store2.dtos.LoginResponse;
import com.yunjun.store2.dtos.CurrentUserResponse;
import com.yunjun.store2.dtos.UserDto;

public interface JwtTokenService {
    LoginResponse generateAccessToken(String email);
    LoginResponse generateAccessToken(UserDto userDto);

    String generateRefreshToken(UserDto userDto);

    boolean validate(String token);

    String getEmailFromToken(String token);

    Long getUserIdFromToken(String token);

    CurrentUserResponse getUserFromToken(String token);
}
