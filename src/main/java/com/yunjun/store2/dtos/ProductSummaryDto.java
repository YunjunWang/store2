package com.yunjun.store2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductSummaryDto {
    private final Long id;
    private final String name;
}
