package com.yunjun.store2.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private BigDecimal price = BigDecimal.ZERO;
}
