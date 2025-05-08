package com.yunjun.store2.services;

import com.yunjun.store2.entities.Category;
import com.yunjun.store2.entities.Product;
import com.yunjun.store2.repositories.CategoryRepository;
import com.yunjun.store2.repositories.ProductRepository;
import com.yunjun.store2.repositories.UserRepository;
import com.yunjun.store2.repositories.spec.ProductSpec;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    @Transactional
    public void updateProductPrices() {
        productRepository.updatePriceByCategory((byte) 1, BigDecimal.valueOf(1001L));
    }

    public void fetchProducts() {
        var products = productRepository.findProducts(new Category((byte) 1));
        products.forEach(System.out::println);

        var products2 = productRepository.findProductsP(new Category((byte) 2));
        products2.forEach(System.out::println);

        var products3 = productRepository.findProductsD(new Category((byte) 3));
        products3.forEach(System.out::println);
    }

    /**
     * Need to use @Transactional for any method calling a query
     * that uses a stored procedure
     */
    @Transactional
    public void fetchProductsWithPrices() {
        var products = productRepository.findProductsByPrices(BigDecimal.valueOf(1000L), BigDecimal.valueOf(1001L));
        products.forEach(System.out::println);
    }

    /**
     * Limitations of Dynamic Queries with Example by Java code:
     * 1. No support for nested properties
     * 2. No support for matching collections/maps
     * 3. Database-specific support for matching strings
     * 4. Exact matching is other types (e.g., numbers/dates)
     */
    public void fetchProductsWithDynamicQuery() {
        Product product = new Product();
        product.setName("iPhone");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnorePaths("id", "category")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Product> example = Example.of(product, matcher);

        var products = productRepository.findAll(example);
        products.forEach(System.out::println);
    }

    public void fetchProductsByCriteria() {
        var products = productRepository.fetchProductsWithCriteria("iPhone", BigDecimal.valueOf(1L), BigDecimal.valueOf(2000L));
        products.forEach(System.out::println);
    }

    public void fetchProductsBySpecifications(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        Specification<Product> spec = Specification.where(null);
        if (name != null) {
            spec = spec.and(ProductSpec.containsName(name));
        }

        if (minPrice != null) {
            spec = spec.and(ProductSpec.greaterThanOrEqualTo(minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and(ProductSpec.lessThanOrEqualTo(maxPrice));
        }

        productRepository.findAll(spec).forEach(System.out::println);

    }
 }
