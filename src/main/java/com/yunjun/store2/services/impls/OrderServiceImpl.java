package com.yunjun.store2.services.impls;

import com.yunjun.store2.dtos.OrderDto;
import com.yunjun.store2.entities.OrderStatus;
import com.yunjun.store2.mappers.OrderMapper;
import com.yunjun.store2.repositories.OrderRepository;
import com.yunjun.store2.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    /**
     * @param orderId
     * @param status
     * @return
     */
    @Override
    public OrderDto updateOrderStatus(Long orderId, OrderStatus status) {
       var order = orderRepository.findById(orderId).orElseThrow();
       order.setStatus(status);
       orderRepository.save(order);
       return orderMapper.toDto(order);
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public List<OrderDto> getAllOrders(Long customerId) {
        var orders =  orderRepository.getCustomerOrders(customerId);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    /**
     * @param orderId
     * @param customerId
     * @return
     */
    @Override
    public OrderDto getOrder(Long orderId, Long customerId) {
        var order = orderRepository.getCustomerOrder(orderId, customerId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return orderMapper.toDto(order);
    }
}
