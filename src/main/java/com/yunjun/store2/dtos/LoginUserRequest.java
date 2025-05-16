package com.yunjun.store2.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginUserRequest {
    @NotNull(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Password is required")
    private String password;
}
