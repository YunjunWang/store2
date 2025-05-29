package com.yunjun.store2.payments;

import com.stripe.exception.StripeException;
import com.yunjun.store2.entities.Order;

import java.util.Optional;

/**
 * name it with the suffix 'Gateway' as it is not a public service,
 * this service only for internal purpose.
 */
public interface PaymentGateway {
    CheckoutSession checkout(Order order) throws StripeException;

    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
