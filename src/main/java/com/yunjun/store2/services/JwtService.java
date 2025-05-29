package com.yunjun.store2.services;

import com.yunjun.store2.users.UserDto;

public interface JwtService {
    Jwt generateAccessToken(UserDto userDto);

    Jwt generateRefreshToken(UserDto userDto);

    Jwt parse(String token);
}
