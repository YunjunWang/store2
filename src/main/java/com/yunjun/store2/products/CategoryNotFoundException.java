package com.yunjun.store2.products;

import java.io.Serial;

public class CategoryNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public CategoryNotFoundException() {
        super("Category not found");
    }
}
