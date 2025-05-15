package com.yunjun.store2.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Long productId;
}
