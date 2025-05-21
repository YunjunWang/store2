package com.yunjun.store2.services.impls;

import com.yunjun.store2.dtos.OrderDto;
import com.yunjun.store2.entities.Order;
import com.yunjun.store2.exceptions.CartIsEmptyException;
import com.yunjun.store2.exceptions.CartNotFoundException;
import com.yunjun.store2.mappers.OrderMapper;
import com.yunjun.store2.repositories.CartRepository;
import com.yunjun.store2.repositories.OrderRepository;
import com.yunjun.store2.repositories.UserRepository;
import com.yunjun.store2.services.CartService;
import com.yunjun.store2.services.CheckoutService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CartService cartService;

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
        var cart = cartRepository.findCartByIdWithCartItems(cartId)
                .orElseThrow(CartNotFoundException::new);
        if (cart.isEmpty()) {
            throw new CartIsEmptyException();
        }
        var order = Order.fromCart(cart, userRepository.findUserById(userId));
        orderRepository.save(order);
        cartService.clearCart(cartId);

        return orderMapper.toDto(order);
    }
}
