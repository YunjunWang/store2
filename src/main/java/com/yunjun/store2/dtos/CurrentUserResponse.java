package com.yunjun.store2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CurrentUserResponse {
    private Long userId;
    private String name;
    private String email;
}
