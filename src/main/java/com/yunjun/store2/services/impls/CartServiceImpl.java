package com.yunjun.store2.services.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunjun.store2.dtos.AddItemToCartRequest;
import com.yunjun.store2.dtos.CartDto;
import com.yunjun.store2.dtos.CartItemDto;
import com.yunjun.store2.dtos.UpdateCartItemRequest;
import com.yunjun.store2.entities.CartItem;
import com.yunjun.store2.mappers.CartItemMapper;
import com.yunjun.store2.mappers.CartMapper;
import com.yunjun.store2.repositories.CartRepository;
import com.yunjun.store2.repositories.ProductRepository;
import com.yunjun.store2.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Domain Driven Design - Aggregation Root rule
 * Cart is an aggregation root of the domain model which contains CartItems.
 * And CartItems are not an aggregation root of the domain model.
 * Also, CartItems don't have their own lifecycle; without Cart, there's no CartItem.
 * Therefore, we don't have CartItemService, CartItemRepository.
 * Instead, CartService contains all the methods related to CartItems and Cart entity.
 *
 * @author yunjun.wangirl@gmail.com
 */
@AllArgsConstructor
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    /**
     * @param cartDto
     * @return
     */
    @Override
    public CartDto addCart(CartDto cartDto) {
        var cart = cartRepository.save(cartMapper.toEntity(cartDto));
        return cartMapper.toDto(cart);
    }

    /**
     * @return
     */
    @Override
    public List<CartDto> getAllCarts() {
        var carts = cartRepository.findAll();
        return carts.stream()
                .map(cartMapper::toDto)
                .toList();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public CartDto getCart(UUID id) throws NoSuchElementException {
        var cart = cartRepository
                .findCartByIdWithCartItems(id)
                .orElseThrow(() -> new NoSuchElementException("Cart not found with id: " + id));
        return cartMapper.toDto(cart);
    }

    /**
     * @param id
     */
    @Override
    public void deleteCart(UUID id) throws NoSuchElementException {
        if (!cartRepository.existsById(id)) {
            throw new NoSuchElementException("Cart not found");
        }

        cartRepository.deleteById(id);
    }

    /**
     * @param request
     * @param cartId
     * @return
     */
    @Override
    public CartItemDto addCartItem(AddItemToCartRequest request, UUID cartId) {
        var productId = request.getProductId();
        // the two queries are combined into one db query with findCartByIdWithCartItems in the repository
        var product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product is not in the cart!")); // should set it as bad_request, not the no such item exception here
        var cart = cartRepository.findCartByIdWithCartItems(cartId).orElseThrow(() -> new IllegalArgumentException("Cart not found with id: " + cartId));
        var cartItem = cart.getItems().stream().filter(i -> i.getProduct().getId().equals(productId)).findFirst().orElse(null);
        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .product(product)
                    .cart(cart)
                    .quantity(1)
                    .build();
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }
        cart.addItem(cartItem);
        cartRepository.save(cart);
        return cartItemMapper.toDto(cartItem);
    }

    /**
     * @param request
     * @param id
     * @return
     */
    @Override
    public CartItemDto updateCartItem(UpdateCartItemRequest request, UUID id, Long productId) throws JsonProcessingException {
//        var productId = request.getProductId();
        var cart = cartRepository.findCartByIdWithCartItems(id).orElse(null);
        if (cart == null) {
            var error = Map.of("Error", "Cart not found with id: " + id);
            var objectMapper = new ObjectMapper().writerWithDefaultPrettyPrinter();
            String json = objectMapper.writeValueAsString(error);
            throw new NoSuchElementException(json);
        }
        var cartItem = cart
                .getItems()
                .stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
        if (cartItem == null) {
            var error = Map.of("Error", "Product not found in the cart with cart id: " + id);
            var objectMapper = new ObjectMapper().writerWithDefaultPrettyPrinter();
            String json = objectMapper.writeValueAsString(error);
            throw new NoSuchElementException(json);
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        return cartItemMapper.toDto(cartItem);
    }

    /**
     * @param productId
     * @param cartId
     */
    @Override
    public void removeCartItem(Long productId, Long cartId) {

    }
}
