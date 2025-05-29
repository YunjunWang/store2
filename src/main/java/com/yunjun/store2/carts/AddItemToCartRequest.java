package com.yunjun.store2.carts;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull(message = "Product ID must be provided")
    @Min(value = 0, message = "Product ID must be greater than 0")
    private Long productId;
}
