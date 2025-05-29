package com.yunjun.store2.services;

import com.yunjun.store2.dtos.OrderDto;
import com.yunjun.store2.dtos.WebhookRequest;
import com.yunjun.store2.exceptions.PaymentException;

import java.util.UUID;

public interface CheckoutService {

    OrderDto checkout(UUID cartId, Long principle) throws PaymentException;

    void handleWebhookEvent(WebhookRequest request);
}
