package com.yunjun.store2.services;

import com.yunjun.store2.dtos.JwtResponse;

public interface JwtTokenService {
    JwtResponse generateToken(String email);
}
