package com.yunjun.store2.repositories;

import com.yunjun.store2.entities.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductCriteriaRepository {
    List<Product> fetchProductsWithCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice);

}
