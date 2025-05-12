package com.yunjun.store2.services;

import com.yunjun.store2.dtos.ChangePasswordRequest;
import com.yunjun.store2.dtos.RegisterUserRequest;
import com.yunjun.store2.dtos.UpdateUserRequest;
import com.yunjun.store2.dtos.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(String sortBy);

    UserDto getUserById(Long id);

    UserDto createUser(RegisterUserRequest request);

    UserDto updateUser(Long id, UpdateUserRequest userDto);

    boolean deleteUser(Long id);

    UserDto changePassword(ChangePasswordRequest request, Long id) throws IllegalAccessException, IllegalArgumentException;
}
