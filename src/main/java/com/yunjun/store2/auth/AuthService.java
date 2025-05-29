package com.yunjun.store2.auth;

public interface AuthService {
    Long getUserId();
    void loginUser(LoginRequest request);
}
