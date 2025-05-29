package com.yunjun.store2.payments;

import com.stripe.exception.StripeException;
import com.yunjun.store2.dtos.OrderDto;
import com.yunjun.store2.entities.Order;
import com.yunjun.store2.exceptions.*;
import com.yunjun.store2.repositories.CartRepository;
import com.yunjun.store2.repositories.OrderRepository;
import com.yunjun.store2.users.UserRepository;
import com.yunjun.store2.services.CartService;
import com.yunjun.store2.users.UserNotFoundException;
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
    public OrderDto checkout(UUID cartId, Long userId) throws PaymentException, CartNotFoundException, CartIsEmptyException, UserNotFoundException {
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

    /**
     * It's always better to pass in fewer parameters in the method signature.
     * Using a class for it is much more readable.
     * By using classes, e.g., WebhookRequest and PaymentResult,
     * we can avoid passing in any Stripe dependent objects to the service layer.
     * These are now all payment gateway agnostics.
     *
     * The code here is self-explanatory.
     *
     * @param request
     */
    @Override
    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    // only look for the order without the items for performance optimization
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow(() -> new PaymentException("Cannot find order with id " + paymentResult.getOrderId()));
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });
    }
}
