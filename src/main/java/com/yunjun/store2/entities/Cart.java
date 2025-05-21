package com.yunjun.store2.entities;

import com.yunjun.store2.exceptions.ProductNotFoundException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * OOP - Information Expert Principle
 * Should assign the responsibility to the class has the necessary data to do the job.
 *
 * Domain Model
 * Anemia Model: Classes that only contain data and getter / setters
 * Rich Model: Classes that contain data and behaviors
 * Should use Rich Model in our classes where it applies.
 */
@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    /*
     * UUID is a type of unique identifier,
     * it's a 128-bit number, which is a better alternative to the auto-generated primary key.
     * It's more secure than an auto-generated primary key, and it's easy to generate.
     * It also saves space compared to an auto-generated primary key because it's 16 bytes instead of 8 bytes.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    /*for field except the id, we can use "insertable = false, updatable = false"
      to tell Hibernate to ignore this column when we insert or update the entity.
    */
    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate dateCreated;

    /*
     * orphanRemoval = true means when the current entity's child item becomes an orphan,
     * meaning the current entity is removed, it's children should be removed as well
     *
     * CascadeType.MERGE means to update the current data
     * CascadeType.PERSIST means to create new data
     */
    @OneToMany(mappedBy = "cart", orphanRemoval = true, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<CartItem> items = new HashSet<>();

    public Optional<CartItem> getItem(Long productId) {
        return items.stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();
    }

    public CartItem addItem(Product product) {
        var cartItem = getItem(product.getId()).orElse(null);
        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .product(product)
                    .cart(this)
                    .quantity(1)
                    .build();
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }
        items.add(cartItem);
        return cartItem;
    }

    public CartItem updateItem(Long productId, Integer quantity) throws ProductNotFoundException {
        var cartItem = getItem(productId).orElseThrow(ProductNotFoundException::new);
        cartItem.setQuantity(quantity);
        return cartItem;
    }

    public void removeItem(Long productId) {
        var cartItem = getItem(productId).orElse(null);
        if (cartItem != null) {
            items.remove(cartItem);
            cartItem.setCart(null); // need to set this association in the cartItem too!
        }
    }

    public void clear() {
        items.clear();
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)// example of OOP Information Expert Principle
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}