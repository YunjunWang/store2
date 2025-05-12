package com.yunjun.store2.services;

import com.yunjun.store2.dtos.UserDto;
import com.yunjun.store2.entities.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(String sortBy);

    UserDto getUserById(Long id);
}
