package com.yunjun.store2.dtos;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmedPassword;
    private String email;
    private String name;
}
