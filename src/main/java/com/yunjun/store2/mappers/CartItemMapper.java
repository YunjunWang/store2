package com.yunjun.store2.mappers;

import com.yunjun.store2.dtos.CartItemDto;
import com.yunjun.store2.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
    CartItem toEntity(CartItemDto cartItemDto);
}
