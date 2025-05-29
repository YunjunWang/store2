package com.yunjun.store2.orders;

import java.util.List;

public interface OrderService {
    OrderDto updateOrderStatus(Long orderId, PaymentStatus status);
    List<OrderDto> getAllOrders(Long customerId);
    List<OrderDto> getAllOrders();
    OrderDto getOrder(Long orderId, Long customerId);
    OrderDto getOrder(Long orderId);
}
