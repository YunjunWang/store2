package com.yunjun.store2.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class OrderItemDto {
    private Long id;
    private OrderProductDto product;
    private BigDecimal unitPrice = BigDecimal.ZERO;
    private Integer quantity;
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
