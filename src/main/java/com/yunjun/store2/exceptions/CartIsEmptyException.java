package com.yunjun.store2.exceptions;


import java.io.Serial;

public class CartIsEmptyException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public CartIsEmptyException() {
        super("Cart is empty");
    }
}
