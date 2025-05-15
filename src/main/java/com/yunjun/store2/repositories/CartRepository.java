package com.yunjun.store2.repositories;

import com.yunjun.store2.entities.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    @EntityGraph(attributePaths = {"items.product"})
    @Query("select c from Cart c where c.id = :cardId")
    Optional<Cart> findCartByIdWithCartItems(@Param("cardId") UUID cardId);
}
