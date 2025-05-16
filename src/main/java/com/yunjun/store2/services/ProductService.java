package com.yunjun.store2.services;

import com.yunjun.store2.dtos.ProductDto;
import com.yunjun.store2.entities.Product;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts(Byte categoryId);
    ProductDto getProductById(Long id);
    ProductDto createProduct(ProductDto request);
    void updateProduct(Long id, ProductDto request);
    void deleteProductById(Long id);
}
