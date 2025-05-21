package com.yunjun.store2.controllers;

import com.yunjun.store2.dtos.OrderDto;
import com.yunjun.store2.services.AuthService;
import com.yunjun.store2.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final AuthService authService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getOrders() {
        return orderService.getAllOrders(authService.getUserId());
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrder(@PathVariable("orderId") Long orderId) {
        return orderService.getOrder(orderId, authService.getUserId());
    }
}
