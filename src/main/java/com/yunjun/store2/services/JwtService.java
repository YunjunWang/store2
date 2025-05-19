package com.yunjun.store2.services;

import com.yunjun.store2.dtos.UserDto;

public interface JwtService {
    Jwt generateAccessToken(UserDto userDto);

    Jwt generateRefreshToken(UserDto userDto);

    Jwt parse(String token);
}
