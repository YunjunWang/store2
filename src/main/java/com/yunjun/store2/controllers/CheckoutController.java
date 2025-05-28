package com.yunjun.store2.controllers;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.yunjun.store2.dtos.CheckoutRequest;
import com.yunjun.store2.dtos.CheckoutResponse;
import com.yunjun.store2.entities.OrderStatus;
import com.yunjun.store2.exceptions.OrderNotFoundException;
import com.yunjun.store2.exceptions.PaymentException;
import com.yunjun.store2.repositories.OrderRepository;
import com.yunjun.store2.services.AuthService;
import com.yunjun.store2.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final AuthService authService;
    private final OrderRepository orderRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CheckoutResponse checkoutOrder(@Valid @RequestBody CheckoutRequest request) throws IllegalArgumentException, PaymentException {
        var orderDto = checkoutService.checkout(request.getCartId(), authService.getUserId());
        return new CheckoutResponse(orderDto.getId());
    }

    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.OK)
    public void handleWebhook(@RequestHeader("Stripe-Signature") String signature,
                              @RequestBody String payload) throws PaymentException, IllegalArgumentException{
        try {
            var event = Webhook.constructEvent(payload, signature, webhookSecret);
            /* event.getType()
             * subscription renew
             * charge -> (Charge) stripeObject
             * payment_intent.* -> (PaymentIntent) stripeObject
             * ...
             */
            System.out.println(event.getType());
            // can get the event id and check it is new.

            // if the version of Stripe API used is not compatible with the stripe-java library,
            // event.getDataObjectDeserializer().getObject() can return null,
            // to make sure it won't happen, make sure they are compatible by check on the stripe-java GitHub repo,
            // and the API is latest in your Stripe account/Developers(Workbench) overview page
            var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);

            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    // update order status (PAID)
                    var paymentIntent = (PaymentIntent) stripeObject;
                    if (paymentIntent != null) {
                        var orderId =  paymentIntent.getMetadata().get("order_id");
                        // only look for the order without the items for performance optimization
                        var order = orderRepository.findById(Long.parseLong(orderId)).orElseThrow(OrderNotFoundException::new);
                        order.setStatus(OrderStatus.PAID);
                        orderRepository.save(order);
                    }
                }
                case "payment_intent.payment_failed" -> {
                    // update order status (FAILED)
                }
            }
        } catch (SignatureVerificationException e) {
            throw new PaymentException(e.getMessage());
        }
    }
}
