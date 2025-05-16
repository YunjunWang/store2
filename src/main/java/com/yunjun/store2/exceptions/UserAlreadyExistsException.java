package com.yunjun.store2.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String email;
}
