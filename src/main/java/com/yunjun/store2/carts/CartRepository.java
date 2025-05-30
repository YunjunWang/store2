package com.yunjun.store2.carts;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    @EntityGraph(attributePaths = {"items.product"})
    @Query("select c from Cart c where c.id = :cardId")
    Optional<Cart> findCartByIdWithCartItems(@Param("cardId") UUID cardId);

    @EntityGraph(attributePaths = {"items.product"})
    @Query("select c from Cart c")
    List<Cart> getAllCartsWithCartItems();
}
