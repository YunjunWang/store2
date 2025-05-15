package com.yunjun.store2.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull
    private Long productId;
}
