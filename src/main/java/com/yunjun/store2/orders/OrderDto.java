package com.yunjun.store2.orders;

import com.yunjun.store2.users.UserDto;
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
    private PaymentStatus status;

    private LocalDateTime createdAt;

    private List<OrderItemDto> items = new ArrayList<>();

    @NotNull
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public OrderDto(Long id, String checkoutUrl) {
        this.id = id;
        this.checkoutUrl = checkoutUrl;
    }
}
