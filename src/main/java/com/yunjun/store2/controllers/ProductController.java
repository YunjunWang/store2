package com.yunjun.store2.controllers;

import com.yunjun.store2.dtos.ProductDto;
import com.yunjun.store2.entities.Product;
import com.yunjun.store2.mappers.ProductMapper;
import com.yunjun.store2.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
@Tag(name = "products", description = "Operations about products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<List<ProductDto>> getAllProducts(
//            @RequestHeader(name = "x-auth-token", required = false) String token,
            @RequestParam(name = "categoryId", required = false) Byte categoryId) {
//        System.out.println(token);
        return ResponseEntity.ok(productService.getAllProducts(categoryId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") Long id) {
        var product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto request, UriComponentsBuilder uriBuilder) {
        try {
            var product = productService.createProduct(request);
            var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
            return ResponseEntity.created(uri).body(request);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a product")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDto request) {
        try {
            productService.updateProduct(id, request);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
