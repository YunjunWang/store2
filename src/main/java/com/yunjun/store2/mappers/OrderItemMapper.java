package com.yunjun.store2.mappers;

import com.yunjun.store2.dtos.CartItemDto;
import com.yunjun.store2.dtos.OrderItemDto;
import com.yunjun.store2.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "unitPrice", source = "product.price")
    OrderItemDto toDto(CartItemDto cartItemDto);

    OrderItemDto toDto(OrderItem item);
    OrderItem toEntity(OrderItemDto itemDto);

    @Mapping(target = "unitPrice", source = "product.price")
    OrderItem toEntity(CartItemDto cartItemDto);
}
