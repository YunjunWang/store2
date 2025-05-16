package com.yunjun.store2.services.impls;

import com.yunjun.store2.dtos.CartDto;
import com.yunjun.store2.dtos.CartItemDto;
import com.yunjun.store2.entities.CartItem;
import com.yunjun.store2.exceptions.CartNotFoundException;
import com.yunjun.store2.exceptions.ProductNotFoundException;
import com.yunjun.store2.mappers.CartItemMapper;
import com.yunjun.store2.mappers.CartMapper;
import com.yunjun.store2.repositories.CartRepository;
import com.yunjun.store2.repositories.ProductRepository;
import com.yunjun.store2.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        var carts = cartRepository.findAllCartsWithCartItems();
        return carts.stream()
                .map(cartMapper::toDto)
                .toList();
    }

    /**
     * @param cartId
     * @return
     */
    @Override
    public CartDto getCart(UUID cartId) throws CartNotFoundException {
        var cart = cartRepository
                .findCartByIdWithCartItems(cartId)
                .orElseThrow(() -> new CartNotFoundException());
        return cartMapper.toDto(cart);
    }

    /**
     * @param productId
     * @param cartId
     * @return
     */
    @Override
    public CartItemDto addCartItem(Long productId, UUID cartId) throws ProductNotFoundException, CartNotFoundException{
        // the two queries are combined into one db query with findCartByIdWithCartItems in the repository
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new); // should set it as bad_request, not the no such item exception here
        var cart = cartRepository.findCartByIdWithCartItems(cartId).orElseThrow(CartNotFoundException::new);
        CartItem cartItem = cart.addItem(product);
        cartRepository.save(cart);
        return cartItemMapper.toDto(cartItem);
    }

    /**
     * @param quantity
     * @param cartId
     * @param productId
     * @return
     */
    @Override
    public CartItemDto updateCartItem(Integer quantity, UUID cartId, Long productId) throws CartNotFoundException, ProductNotFoundException{
        var cart = cartRepository.findCartByIdWithCartItems(cartId).orElseThrow(CartNotFoundException::new);

        try {
            var cartItem = cart.updateItem(productId, quantity);
            cartRepository.save(cart);

            return cartItemMapper.toDto(cartItem);
        } catch (ProductNotFoundException e) {
            throw e;
        }
    }

    /**
     * @param cartId
     * @param productId
     */
    @Override
    public void removeCartItem(UUID cartId, Long productId) {
        var cart = cartRepository.findCartByIdWithCartItems(cartId).orElseThrow(CartNotFoundException::new);
        // when not find the item by productId, we don't care, let it complete silently
        // to avoid too aggressive programming.
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    /**
     * @param cartId
     */
    @Override
    public void clearCart(UUID cartId) {
        var cart = cartRepository.findCartByIdWithCartItems(cartId).orElseThrow(CartNotFoundException::new);
        cart.clear();
        cartRepository.save(cart);
    }

    /**
     * @param cartId
     */
    @Override
    public void deleteCart(UUID cartId) throws CartNotFoundException {
        // silent deletion
        cartRepository.deleteById(cartId);
    }
}
