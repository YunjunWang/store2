package com.yunjun.store2.users;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super("User not found");
    }
}
