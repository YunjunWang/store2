package com.yunjun.store2.auth;

import com.yunjun.store2.users.UserDto;

public interface JwtService {
    Jwt generateAccessToken(UserDto userDto);

    Jwt generateRefreshToken(UserDto userDto);

    Jwt parseToken(String token);
}
