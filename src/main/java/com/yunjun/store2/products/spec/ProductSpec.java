package com.yunjun.store2.repositories.spec;

import com.yunjun.store2.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Reusable code using Specification for Dynamic Queries written in Java code
 */
public class ProductSpec {
    public static Specification<Product> containsName(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> greaterThanOrEqualTo(BigDecimal minPrice) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> lessThanOrEqualTo(BigDecimal maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> belongsToCategory(Long categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }
}
