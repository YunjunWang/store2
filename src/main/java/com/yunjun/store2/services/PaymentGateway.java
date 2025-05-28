package com.yunjun.store2.services;

import com.stripe.exception.StripeException;
import com.yunjun.store2.entities.Order;

public interface PaymentGateway {
    CheckoutSession checkout(Order order) throws StripeException;
}
