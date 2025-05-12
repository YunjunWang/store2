package com.yunjun.store2.mappers;

import com.yunjun.store2.dtos.UserDto;
import com.yunjun.store2.entities.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
