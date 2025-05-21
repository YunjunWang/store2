package com.yunjun.store2.repositories;

import com.yunjun.store2.entities.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"items.product"})
    @Query("select o from Order o where o.customer.id = :customerId")
    List<Order> getOrdersByCustomerId(@Param("customerId") Long customerId);

    @EntityGraph(attributePaths = {"items.product"})
    @Query("select o from Order o where o.id = :orderId and o.customer.id = :customerId")
    Optional<Order> findOrderWithItemsByCustomerId(@Param("orderId") Long orderId, @Param("customerId") Long customerId);

    @EntityGraph(attributePaths = {"items.product"})
    @Query("select o from Order o where o.id = :orderId")
    Optional<Order> findOrderWithItemsById(@Param("orderId") Long orderId);

    @EntityGraph(attributePaths = {"items.product"})
    @Query("select o from Order o")
    List<Order> getAllOrdersWithItems();
}
