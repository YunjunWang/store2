package com.yunjun.store2.services;

import com.yunjun.store2.dtos.OrderDto;

import java.util.UUID;

public interface CheckoutService {

    OrderDto checkout(UUID cartId, Long principle);
}
