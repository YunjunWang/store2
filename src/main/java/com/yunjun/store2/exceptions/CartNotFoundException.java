package com.yunjun.store2.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CartNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID cartId;
}
