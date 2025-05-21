package com.yunjun.store2.dtos;

import com.yunjun.store2.entities.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Builder
@Data
public class OrderDto {
    private Long id;

    @NotNull
    private UserDto customer;

    @NotNull
    private OrderStatus status;

    private LocalDateTime createdAt;

    private List<OrderItemDto> items = new ArrayList<>();

    @NotNull
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
