package com.yunjun.store2.controllers;

import com.yunjun.store2.dtos.CheckoutRequest;
import com.yunjun.store2.dtos.CheckoutResponse;
import com.yunjun.store2.services.AuthService;
import com.yunjun.store2.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CheckoutController {
    private final CartService cartService;
    private final AuthService authService;

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.OK)
    public CheckoutResponse checkoutOrder(@Valid @RequestBody CheckoutRequest request) throws IllegalArgumentException {
        var orderDto = cartService.checkout(request.getCartId(), authService.getUserId());
        return new CheckoutResponse(orderDto.getId());
    }
}
