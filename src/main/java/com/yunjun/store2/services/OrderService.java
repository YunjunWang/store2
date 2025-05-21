package com.yunjun.store2.services;

import com.yunjun.store2.dtos.OrderDto;
import com.yunjun.store2.entities.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDto checkout(UUID cartId, Long principle);
    OrderDto updateOrderStatus(Long orderId, OrderStatus status);

    List<OrderDto> getAllOrders(Long customerId);

    OrderDto getOrder(Long orderId, Long customerId);
}
