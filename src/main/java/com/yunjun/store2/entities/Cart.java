package com.yunjun.store2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    @OneToMany(mappedBy = "cart", orphanRemoval = true, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<CartItem> items = new HashSet<>();


    public void addItem(CartItem cartItem) {
        items.add(cartItem);
    }

    public void removeItem(CartItem cartItem) {
        items.remove(cartItem);
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}