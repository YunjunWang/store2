package com.yunjun.store2.controllers;

import com.yunjun.store2.dtos.AddItemToCartRequest;
import com.yunjun.store2.dtos.CartDto;
import com.yunjun.store2.dtos.CartItemDto;
import com.yunjun.store2.dtos.UpdateCartItemRequest;
import com.yunjun.store2.exceptions.CartNotFoundException;
import com.yunjun.store2.exceptions.ProductNotFoundException;
import com.yunjun.store2.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("api/carts")
@Tag(name = "carts", description = "Operations about carts")
public class CartController {
    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Get all carts")
    public ResponseEntity<List<CartDto>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @GetMapping("/{cartId}")
    @Operation(summary = "Get a cart")
    public ResponseEntity<CartDto> getCart(
            @NotNull @PathVariable UUID cartId) throws NoSuchElementException {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @PostMapping
    @Operation(summary = "Create a new cart")
    public ResponseEntity<CartDto> addCart(UriComponentsBuilder uriBuilder) {
        var cartDto = cartService.addCart(new CartDto());
        var uri = uriBuilder.path("/carts/{cartId}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add a product to the cart")
    public ResponseEntity<CartItemDto> addCartItem(
            @Valid @RequestBody AddItemToCartRequest request,
            @NotNull @PathVariable(name = "cartId") @Parameter(description = "The ID of the cart.") UUID cartId) {
        var cartItemDto = cartService.addCartItem(request.getProductId(), cartId);
        return new ResponseEntity<>(cartItemDto, HttpStatus.CREATED);
    }

    @PutMapping("/{cartId}/items/{productId}")
    @Operation(summary = "Update the quantity of a product in the cart")
    public ResponseEntity<CartItemDto> updateCartItem(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) throws ProductNotFoundException, CartNotFoundException {
        var cartItemDto = cartService.updateCartItem(request.getQuantity(), cartId, productId);
        return new ResponseEntity<>(cartItemDto, HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    @Operation(summary = "Remove a product from the cart")
    public ResponseEntity<Void> deleteCartItem(
            @NotNull @PathVariable("cartId") UUID cartId,
            @NotNull @PathVariable("productId") Long productId) throws CartNotFoundException {
        cartService.removeCartItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    @Operation(summary = "Remove all products from the cart")
    public ResponseEntity<Void> clearCart(
            @PathVariable("cartId") UUID cartId) throws CartNotFoundException {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}")
    @Operation(summary = "Delete a cart")
    public ResponseEntity<Void> deleteCart(
            @NotNull @PathVariable UUID cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
