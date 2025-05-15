package com.yunjun.store2.services;

import com.yunjun.store2.dtos.CartItemDto;

import java.util.List;

public interface CartItemService {
    List<CartItemDto> addItemToCart(Long productId, Long cartId);
    List<CartItemDto> removeItemFromCart(Long productId, Long cartId);
}
