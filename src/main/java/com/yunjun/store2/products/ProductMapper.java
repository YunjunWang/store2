package com.yunjun.store2.products;

import com.yunjun.store2.dtos.CartProductDto;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);

    void toEntity(ProductDto productDto, @MappingTarget Product product);

    Product toEntity(@NotNull CartProductDto product);
}
