package com.yunjun.store2.dtos;

import com.yunjun.store2.entities.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class OrderDto {
    private Long id;
    private String checkoutUrl;

    @NotNull
    private UserDto customer;

    @NotNull
    private OrderStatus status;

    private LocalDateTime createdAt;

    private List<OrderItemDto> items = new ArrayList<>();

    @NotNull
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public OrderDto(Long id, String checkoutUrl) {
        this.id = id;
        this.checkoutUrl = checkoutUrl;
    }
}
