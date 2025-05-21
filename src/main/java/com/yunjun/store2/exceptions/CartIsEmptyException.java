package com.yunjun.store2.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CartIsEmptyException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message;
}
