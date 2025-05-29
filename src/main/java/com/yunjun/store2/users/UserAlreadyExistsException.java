package com.yunjun.store2.users;


import java.io.Serial;

public class UserAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException() {
        super("User already exists");
    }
}
