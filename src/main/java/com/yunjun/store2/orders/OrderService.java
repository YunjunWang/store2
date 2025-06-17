package com.yunjun.store2.orders;

import com.yunjun.store2.users.User;

import java.util.List;

public interface OrderService {
    OrderDto updateOrderStatus(Long orderId, PaymentStatus status);
//    List<OrderDto> getAllOrders(Long customerId);
    List<OrderDto> getAllOrders(User customer);
    List<OrderDto> getAllOrders();
    OrderDto getOrder(Long orderId, Long customerId);
    OrderDto getOrder(Long orderId);
}
