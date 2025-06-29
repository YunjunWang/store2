package com.yunjun.store2.orders;

import com.yunjun.store2.carts.Cart;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.yunjun.store2.users.User;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    /*
     * Use enum OrderStatus here instead of String.
     * Tell Spring to save it as string / varchar in the db
     * Avoid using ORDINAL as the order of the enum items could change,
     * and the number saved in the db loses its meaning.
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    /**
     * Better use LocalDateTime than the Instant as suggested
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<OrderItem> items = new LinkedHashSet<>();

    public static Order fromCart(Cart cart, User customer) {
        var order = Order.builder()
                .status(PaymentStatus.PENDING)
                .totalPrice(cart.getTotalPrice())
                .customer(customer)
                .build();

        /**
         * an OrderItem cannot live without
         * an Order
         * a Product
         * and Quantity.
         * Therefore, we need to pass them into the OrderItem constructor
         */
        var orderItems = cart.getItems().stream().map(item ->
                new OrderItem(order, item.getProduct(), item.getQuantity())).collect(Collectors.toSet());

        order.setItems(orderItems);
        return order;
    }

    public boolean isPlacedBy(User customer) {
        return this.customer.equals(customer);
    }
}