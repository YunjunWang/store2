package com.yunjun.store2.carts;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private BigDecimal price = BigDecimal.ZERO;
}
