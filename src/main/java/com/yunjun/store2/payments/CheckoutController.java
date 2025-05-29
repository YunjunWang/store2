package com.yunjun.store2.controllers;

import com.yunjun.store2.dtos.CheckoutRequest;
import com.yunjun.store2.dtos.CheckoutResponse;
import com.yunjun.store2.dtos.WebhookRequest;
import com.yunjun.store2.exceptions.PaymentException;
import com.yunjun.store2.repositories.OrderRepository;
import com.yunjun.store2.services.AuthService;
import com.yunjun.store2.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final AuthService authService;
    private final OrderRepository orderRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CheckoutResponse checkoutOrder(@Valid @RequestBody CheckoutRequest request) throws IllegalArgumentException, PaymentException {
        var orderDto = checkoutService.checkout(request.getCartId(), authService.getUserId());
        return new CheckoutResponse(orderDto.getId());
    }

    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.OK)
    public void handleWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload) throws PaymentException, IllegalArgumentException{
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }
}
