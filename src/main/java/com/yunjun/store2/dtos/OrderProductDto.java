package com.yunjun.store2.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDto {
    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal unitPrice = BigDecimal.ZERO;
}
