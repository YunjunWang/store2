package com.yunjun.store2.carts;

import java.util.List;

public interface CartItemService {
    List<CartItemDto> addItemToCart(Long productId, Long cartId);
    List<CartItemDto> removeItemFromCart(Long productId, Long cartId);
}
