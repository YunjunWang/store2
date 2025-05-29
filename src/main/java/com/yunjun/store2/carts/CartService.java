package com.yunjun.store2.carts;

import com.yunjun.store2.products.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CartService {
    CartDto addCart(CartDto cartDto);
    List<CartDto> getAllCarts();
    CartDto getCart(UUID cartId);
    CartItemDto addCartItem(Long productId, UUID cartId) throws ProductNotFoundException, CartNotFoundException;
    CartItemDto updateCartItem(Integer quantity, UUID id, Long productId) throws ProductNotFoundException, CartNotFoundException;
    void removeCartItem(UUID productId, Long cartId);
    void clearCart(UUID cartId);
    void deleteCart(UUID cartId);
}
