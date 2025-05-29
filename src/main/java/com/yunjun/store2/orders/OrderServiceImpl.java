package com.yunjun.store2.orders;

import com.yunjun.store2.users.UserNotFoundException;
import com.yunjun.store2.users.UserRepository;
import com.yunjun.store2.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    /**
     * @param orderId
     * @param status
     * @return
     */
    @Override
    public OrderDto updateOrderStatus(Long orderId, PaymentStatus status) {
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
        var orders =  orderRepository.getOrdersByCustomerId(customerId);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    /**
     * @return
     */
    @Override
    public List<OrderDto> getAllOrders() {
        var orders =  orderRepository.getAllOrdersWithItems();
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
        var order = orderRepository
                .findOrderWithItemsByCustomerId(orderId, customerId)
                .orElseThrow(OrderNotFoundException::new);
        return orderMapper.toDto(order);
    }

    /**
     * @param orderId
     * @return
     */
    @Override
    public OrderDto getOrder(Long orderId) {
        var order = orderRepository
                .findOrderWithItemsById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        var userId = authService.getUserId();
        var user = userRepository.findUserById(userId).orElseThrow(UserNotFoundException::new);
        if (!order.isPlacedBy(user)) {
            throw new AccessDeniedException("You don't have the access to this order.");
        }
        return orderMapper.toDto(order);
    }
}
