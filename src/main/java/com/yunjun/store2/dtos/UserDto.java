package com.yunjun.store2.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserDto {
    private final Long id;
    private final String name;
    private final String email;
}
