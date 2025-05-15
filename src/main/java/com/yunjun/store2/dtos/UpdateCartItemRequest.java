package com.yunjun.store2.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
//    @NotNull
//    private Long productId;

    @NotNull(message = "Quantity must be provided")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
}
