package com.yunjun.store2.products;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all products")
    public List<ProductDto> getAllProducts(
//            @RequestHeader(name = "x-auth-token", required = false) String token,
            @RequestParam(name = "categoryId", required = false) Byte categoryId) {
//        System.out.println(token);
        return productService.getAllProducts(categoryId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a product")
    public ProductDto getProduct(@NotNull @Min(value = 0) @PathVariable("id") Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody ProductDto request,
            UriComponentsBuilder uriBuilder) {
        var productDto = productService.createProduct(request);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a product")
    public ProductDto updateProduct(
            @NotNull @Min(value = 0) @PathVariable("id") Long id,
            @Valid @RequestBody ProductDto request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a product")
    public void deleteProduct(@NotNull @Min(value = 0) @PathVariable("id") Long id) {
        productService.deleteProductById(id);
    }
}
