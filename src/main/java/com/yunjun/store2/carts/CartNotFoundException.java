package com.yunjun.store2.carts;

import java.io.Serial;

public class CartNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public CartNotFoundException() {
        super("Cart not found");
    }
}
