package com.yunjun.store2.mappers;

import com.yunjun.store2.dtos.CartDto;
import com.yunjun.store2.entities.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart toEntity(CartDto cartDto);

    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);
}
