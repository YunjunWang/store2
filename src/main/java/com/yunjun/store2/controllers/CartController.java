package com.yunjun.store2.controllers;

import com.yunjun.store2.dtos.AddItemToCartRequest;
import com.yunjun.store2.dtos.CartDto;
import com.yunjun.store2.dtos.CartItemDto;
import com.yunjun.store2.dtos.UpdateCartItemRequest;
import com.yunjun.store2.exceptions.CartNotFoundException;
import com.yunjun.store2.exceptions.ProductNotFoundException;
import com.yunjun.store2.services.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartDto>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(
            @NotNull @PathVariable UUID cartId) throws NoSuchElementException {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @PostMapping
    public ResponseEntity<CartDto> addCart(UriComponentsBuilder uriBuilder) {
        var cartDto = cartService.addCart(new CartDto());
        var uri = uriBuilder.path("/carts/{cartId}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addCartItem(
            @Valid @RequestBody AddItemToCartRequest request,
            @NotNull @PathVariable(name = "cartId") UUID cartId) {
        var cartItemDto = cartService.addCartItem(request.getProductId(), cartId);
        return new ResponseEntity<>(cartItemDto, HttpStatus.CREATED);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateCartItem(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) throws ProductNotFoundException, CartNotFoundException {
        var cartItemDto = cartService.updateCartItem(request.getQuantity(), cartId, productId);
        return new ResponseEntity<>(cartItemDto, HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> deleteCartItem(
            @NotNull @PathVariable("cartId") UUID cartId,
            @NotNull @PathVariable("productId") Long productId) throws CartNotFoundException {
        cartService.removeCartItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(
            @PathVariable("cartId") UUID cartId) throws CartNotFoundException {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(
            @NotNull @PathVariable UUID cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
