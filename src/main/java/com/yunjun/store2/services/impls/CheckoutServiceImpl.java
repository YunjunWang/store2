package com.yunjun.store2.services.impls;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.yunjun.store2.dtos.OrderDto;
import com.yunjun.store2.entities.Order;
import com.yunjun.store2.exceptions.CartIsEmptyException;
import com.yunjun.store2.exceptions.CartNotFoundException;
import com.yunjun.store2.exceptions.UserNotFoundException;
import com.yunjun.store2.mappers.OrderMapper;
import com.yunjun.store2.repositories.CartRepository;
import com.yunjun.store2.repositories.OrderRepository;
import com.yunjun.store2.repositories.UserRepository;
import com.yunjun.store2.services.CartService;
import com.yunjun.store2.services.CheckoutService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CartService cartService;

    @Value("${websiteUrl}")
    private String websiteUrl;

    /**
     * @param cartId
     * @param userId
     * @return
     */
    @Override
    public OrderDto checkout(UUID cartId, Long userId) throws StripeException {
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
        var builder = SessionCreateParams.builder()
            .setMode((SessionCreateParams.Mode.PAYMENT))
            // .setMode((SessionCreateParams.Mode.SUBSCRIPTION))
            .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
            .setCancelUrl(websiteUrl + "/checkout-cancel");

        order.getItems().forEach(item -> {
            var lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(Long.valueOf(item.getQuantity()))
                    // dynamic price data, if setPrice(price: String), it is expecting an ID from Stripe that has been registered
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmountDecimal(item.getUnitPrice())
                            // dynamic product data, if setProduct(product: String), it is expecting an ID from Stripe that has been registered
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(item.getProduct().getName())
                                // .setDescription(item.getProduct().getDescription())
                                .build())
                            .build())
                    .build();
            builder.addLineItem(lineItem);
        });

        Session session = Session.create(builder.build());

        cartService.clearCart(cartId);

        return new OrderDto(order.getId(), session.getUrl());
    }
}
