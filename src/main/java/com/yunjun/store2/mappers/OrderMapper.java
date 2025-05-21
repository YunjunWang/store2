package com.yunjun.store2.mappers;

import com.yunjun.store2.dtos.OrderDto;
import com.yunjun.store2.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);
}
