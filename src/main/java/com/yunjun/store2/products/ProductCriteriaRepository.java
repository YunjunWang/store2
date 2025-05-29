package com.yunjun.store2.products;

import java.math.BigDecimal;
import java.util.List;

public interface ProductCriteriaRepository {
    List<Product> getProductsWithCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> getProductsWithCriteriaByCategory(Category category);

}
