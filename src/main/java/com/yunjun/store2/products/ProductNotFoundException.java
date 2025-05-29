package com.yunjun.store2.exceptions;

import java.io.Serial;

public class ProductNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ProductNotFoundException() {
        super("Product not found");
    }
}
