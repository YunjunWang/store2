package com.yunjun.store2.products;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts(Byte categoryId);
    ProductDto getProductById(Long id);
    ProductDto createProduct(ProductDto request);
    ProductDto updateProduct(Long id, ProductDto request);
    void deleteProductById(Long id);
}
