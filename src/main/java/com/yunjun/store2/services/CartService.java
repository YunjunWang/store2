package com.yunjun.store2.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yunjun.store2.dtos.AddItemToCartRequest;
import com.yunjun.store2.dtos.CartDto;
import com.yunjun.store2.dtos.CartItemDto;
import com.yunjun.store2.dtos.UpdateCartItemRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CartService {
    CartDto addCart(CartDto cartDto);
    List<CartDto> getAllCarts();
    CartDto getCart(UUID id);
    void deleteCart(UUID id);
    CartItemDto addCartItem(AddItemToCartRequest request, UUID id);
    CartItemDto updateCartItem(UpdateCartItemRequest request, UUID id, Long productId) throws JsonProcessingException;
    void removeCartItem(UUID productId, Long cartId);

    void clearCart(UUID id);
}
