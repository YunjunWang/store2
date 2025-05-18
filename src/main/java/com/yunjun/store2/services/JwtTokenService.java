package com.yunjun.store2.services;

import com.yunjun.store2.dtos.JwtResponse;
import com.yunjun.store2.dtos.LoginResponse;
import com.yunjun.store2.dtos.UserDto;

public interface JwtTokenService {
    JwtResponse generateToken(String email);
    JwtResponse generateToken(UserDto userDto);

    boolean validate(String token);

    String getEmailFromToken(String token);

    Long getUserIdFromToken(String token);

    LoginResponse getUserFromToken(String token);
}
