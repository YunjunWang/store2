package com.yunjun.store2.orders;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class OrderItemDto {
    private Long id;
    private ProductDto product;
    private BigDecimal unitPrice = BigDecimal.ZERO;
    private Integer quantity;
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
