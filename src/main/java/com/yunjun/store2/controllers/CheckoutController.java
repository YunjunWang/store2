package com.yunjun.store2.controllers;

import com.yunjun.store2.dtos.CheckoutRequest;
import com.yunjun.store2.dtos.CheckoutResponse;
import com.yunjun.store2.dtos.OrderDto;
import com.yunjun.store2.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CheckoutController {
    private final OrderService orderService;

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.OK)
    public CheckoutResponse checkoutOrder(@RequestBody CheckoutRequest request) throws IllegalArgumentException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principle = authentication.getPrincipal();
        var orderDto = orderService.checkout(request.getCartId(), (Long) principle);
        return new CheckoutResponse(orderDto.getId());
    }

    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getOrders() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principle = authentication.getPrincipal();
        return orderService.getAllOrders((Long) principle);
    }

    @GetMapping("/orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrder(@PathVariable("orderId") Long orderId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principle = authentication.getPrincipal();
        return orderService.getOrder(orderId, (Long) principle);
    }
}
