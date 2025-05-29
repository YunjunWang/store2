package com.yunjun.store2.products;

import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    com.yunjun.store2.products.ProductDto toDto(Product product);

    Product toEntity(com.yunjun.store2.products.ProductDto productDto);

    void toEntity(com.yunjun.store2.products.ProductDto productDto, @MappingTarget Product product);

    Product toEntity(@NotNull com.yunjun.store2.carts.ProductDto product);
}
