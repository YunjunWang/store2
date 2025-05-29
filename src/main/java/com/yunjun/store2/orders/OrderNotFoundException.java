package com.yunjun.store2.orders;

import java.io.Serial;

public class OrderNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public OrderNotFoundException() {
        super("Order not found");
    }
}
