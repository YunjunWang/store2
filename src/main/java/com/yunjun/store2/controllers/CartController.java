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
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("api/carts")
@Tag(name = "carts", description = "Operations about carts")
public class CartController {
    private final CartService cartService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all carts")
    public List<CartDto> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/{cartId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a cart")
    public CartDto getCart(@PathVariable UUID cartId) throws CartNotFoundException {
        return cartService.getCart(cartId);
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
            @PathVariable(name = "cartId") @Parameter(description = "The ID of the cart.") UUID cartId) {
        var cartItemDto = cartService.addCartItem(request.getProductId(), cartId);
        return new ResponseEntity<>(cartItemDto, HttpStatus.CREATED);
    }

    @PutMapping("/{cartId}/items/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update the quantity of a product in the cart")
    public CartItemDto updateCartItem(
            @PathVariable(name = "cartId") UUID cartId,
            @PathVariable(name = "productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) throws ProductNotFoundException, CartNotFoundException {
        return cartService.updateCartItem(request.getQuantity(), cartId, productId);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a product from the cart")
    public void deleteCartItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId) throws CartNotFoundException {
        cartService.removeCartItem(cartId, productId);
    }

    @DeleteMapping("/{cartId}/items")
    @Operation(summary = "Remove all products from the cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable("cartId") UUID cartId) throws CartNotFoundException {
        cartService.clearCart(cartId);
    }

    @DeleteMapping("/{cartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a cart")
    public void deleteCart(@PathVariable UUID cartId) {
        cartService.deleteCart(cartId);
    }
}
