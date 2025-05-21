package com.yunjun.store2.services.impls;

import com.yunjun.store2.dtos.OrderDto;
import com.yunjun.store2.entities.OrderItem;
import com.yunjun.store2.entities.OrderStatus;
import com.yunjun.store2.exceptions.CartIsEmptyException;
import com.yunjun.store2.mappers.OrderItemMapper;
import com.yunjun.store2.mappers.OrderMapper;
import com.yunjun.store2.mappers.ProductMapper;
import com.yunjun.store2.repositories.OrderRepository;
import com.yunjun.store2.services.CartService;
import com.yunjun.store2.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final UserServiceImpl userService;
    private final CartService cartService;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;

    /**
     * @param cartId
     * @param userId
     * @return
     */
    @Override
    public OrderDto checkout(UUID cartId, Long userId) {
        if (cartId == null) {
            throw new IllegalArgumentException("Invalid card ID");
        }
        var cartDto = cartService.getCart(cartId);
        var items = cartDto.getItems();
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        var orderDto = OrderDto.builder()
                .status(OrderStatus.PENDING)
                .totalPrice(cartDto.getTotalPrice())
                .customer(userService.getUserById(userId))
                .build();

        var order = orderMapper.toEntity(orderDto);

        var orderItems = items.stream().map(item ->
                new OrderItem(order, productMapper.toEntity(item.getProduct()), item.getQuantity())).collect(Collectors.toSet());

        order.setItems(orderItems);
        orderRepository.save(order);
        cartService.clearCart(cartId);
        orderDto.setId(order.getId());

        return orderDto;
    }

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
