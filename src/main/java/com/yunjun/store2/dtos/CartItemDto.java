package com.yunjun.store2.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    @NotNull
    private CartProductDto product;

    @NotNull
    private Integer quantity;

    private BigDecimal totalPrice = BigDecimal.ZERO;
}
