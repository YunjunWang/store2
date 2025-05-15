package com.yunjun.store2.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
    private UUID id;
    private LocalDate dateCreated;
    private List<CartItemDto> items = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
