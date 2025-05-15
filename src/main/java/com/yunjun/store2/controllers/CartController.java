package com.yunjun.store2.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yunjun.store2.dtos.AddItemToCartRequest;
import com.yunjun.store2.dtos.CartDto;
import com.yunjun.store2.dtos.CartItemDto;
import com.yunjun.store2.dtos.UpdateCartItemRequest;
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

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(
            @NotNull @PathVariable UUID id) throws NoSuchElementException {
        return ResponseEntity.ok(cartService.getCart(id));
    }

    @PostMapping
    public ResponseEntity<CartDto> addCart(UriComponentsBuilder uriBuilder) {
        var cartDto = cartService.addCart(new CartDto());
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CartItemDto> addCartItem(
            @Valid @RequestBody AddItemToCartRequest request,
            @NotNull @PathVariable(name = "id") UUID id) {
        var cartItemDto = cartService.addCartItem(request, id);
        return new ResponseEntity<>(cartItemDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/items/{productId}")
    public ResponseEntity<CartItemDto> updateCartItem(
            @PathVariable(name = "id") UUID id,
            @PathVariable(name = "productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) throws JsonProcessingException {
        var cartItemDto = cartService.updateCartItem(request, id, productId);
        return new ResponseEntity<>(cartItemDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/items/{productId}")
    public ResponseEntity<Void> deleteCartItem(
            @NotNull @PathVariable("id") UUID id,
            @NotNull @PathVariable("productId") Long productId) throws NoSuchElementException {
        cartService.removeCartItem(id, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/items")
    public ResponseEntity<Void> clearCart(
            @PathVariable("id") UUID id) throws NoSuchElementException {
        cartService.clearCart(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(
            @NotNull @PathVariable UUID id) throws NoSuchElementException {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
