package com.yunjun.store2.exceptions;

import lombok.NoArgsConstructor;

import java.io.Serial;

@NoArgsConstructor
public class PaymentException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    public PaymentException(String message) {
        super(message);
    }
}
