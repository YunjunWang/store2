package com.yunjun.store2.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductDto {
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must be less than 255 characters")
    private String name;

    private String description;

    @NotNull
    @Min(value = 0, message = "Price must be greater than or equal to zero")
    private BigDecimal price;

    @NotNull
    @Min(value = 0, message = "Quantity must be greater than 0")
    private Byte categoryId;
}
