package com.yunjun.store2.controllers;

import com.yunjun.store2.dtos.CheckoutRequest;
import com.yunjun.store2.dtos.CheckoutResponse;
import com.yunjun.store2.services.AuthService;
import com.yunjun.store2.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final AuthService authService;

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.OK)
    public CheckoutResponse checkoutOrder(@Valid @RequestBody CheckoutRequest request) throws IllegalArgumentException {
        var orderDto = checkoutService.checkout(request.getCartId(), authService.getUserId());
        return new CheckoutResponse(orderDto.getId());
    }
}
