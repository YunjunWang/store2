package com.yunjun.store2.services;

import com.stripe.exception.StripeException;
import com.yunjun.store2.entities.Order;

/**
 * name it with the suffix 'Gateway' as it is not a public service,
 * this service only for internal purpose.
 */
public interface PaymentGateway {
    CheckoutSession checkout(Order order) throws StripeException;
}
