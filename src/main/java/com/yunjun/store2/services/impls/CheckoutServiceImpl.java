package com.yunjun.store2.services.impls;

import com.stripe.exception.StripeException;
import com.yunjun.store2.dtos.OrderDto;
import com.yunjun.store2.entities.Order;
import com.yunjun.store2.exceptions.CartIsEmptyException;
import com.yunjun.store2.exceptions.CartNotFoundException;
import com.yunjun.store2.exceptions.PaymentException;
import com.yunjun.store2.exceptions.UserNotFoundException;
import com.yunjun.store2.repositories.CartRepository;
import com.yunjun.store2.repositories.OrderRepository;
import com.yunjun.store2.repositories.UserRepository;
import com.yunjun.store2.services.CartService;
import com.yunjun.store2.services.CheckoutService;
import com.yunjun.store2.services.CheckoutSession;
import com.yunjun.store2.services.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    /**
     * @param cartId
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public OrderDto checkout(UUID cartId, Long userId) throws PaymentException, CartNotFoundException, CartIsEmptyException, UserNotFoundException{
        if (cartId == null) {
            throw new IllegalArgumentException("Invalid card ID");
        }
        var cart = cartRepository.findCartByIdWithCartItems(cartId)
                .orElseThrow(CartNotFoundException::new);
        if (cart.isEmpty()) {
            throw new CartIsEmptyException();
        }
        var order = Order.fromCart(
                cart,
                userRepository
                        .findUserById(userId)
                        .orElseThrow(UserNotFoundException::new));
        orderRepository.save(order);

        // Create a checkout session
        try {

            CheckoutSession session = paymentGateway.checkout(order);

            cartService.clearCart(cartId);

            return new OrderDto(order.getId(), session.getCheckoutUrl());
        } catch (StripeException e) {
            // need to the secret api key
            System.out.println("StripeException: " + e.getMessage());
            // remove this order if failed to avoid having bad order data in the db
            orderRepository.delete(order);
            throw new PaymentException(e.getMessage());
        }
    }
}
