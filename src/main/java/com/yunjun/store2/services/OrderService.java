package com.yunjun.store2.services;

import com.yunjun.store2.dtos.OrderDto;
import com.yunjun.store2.entities.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderDto updateOrderStatus(Long orderId, OrderStatus status);
    List<OrderDto> getAllOrders(Long customerId);
    OrderDto getOrder(Long orderId, Long customerId);
}
