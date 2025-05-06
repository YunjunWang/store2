package com.yunjun.store2.services;

import com.yunjun.store2.entities.Category;
import com.yunjun.store2.entities.Product;
import com.yunjun.store2.repositories.CategoryRepository;
import com.yunjun.store2.repositories.ProductRepository;
import com.yunjun.store2.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void showRelatedChildEntities() {
        Category category  = new Category("Mobile");
        Product product = Product
                .builder()
                .category(category)
                .name("iPhone")
                .price(BigDecimal.valueOf(1200L))
                .description("The latest iPhone")
                .build();

        productRepository.save(product);

        product = Product
                .builder()
                .category(category)
                .name("iPhone")
                .price(BigDecimal.valueOf(1200L))
                .description("The latest iPhone")
                .build();
        productRepository.save(product);

        var user = userRepository.findById(6L).orElseThrow();
        user.addWishlist(product);
        userRepository.save(user);

        productRepository.delete(productRepository.findAll().iterator().next());
    }

    @Transactional
    public void showRelatedParentEntities() {
        Category category  = new Category("Mobile");
        Product product = Product
                .builder()
                .category(category)
                .name("iPhone")
                .price(BigDecimal.valueOf(1200L))
                .description("The latest iPhone")
                .build();

        category.addProduct(product);
        categoryRepository.save(category);
        categoryRepository.delete(category);
    }
}
