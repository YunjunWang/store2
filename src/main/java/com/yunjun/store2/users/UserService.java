package com.yunjun.store2.users;

import com.yunjun.store2.auth.LoginRequest;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(String sortBy);

    UserDto getUserById(Long id);

    UserDto registerUser(RegisterUserRequest request) throws IllegalArgumentException;

    UserDto updateUser(Long id, UpdateUserRequest userDto);

    void deleteUser(Long id);

    UserDto changePassword(ChangePasswordRequest request, Long id) throws IllegalAccessException, IllegalArgumentException;

    UserDto getUserByEmail(String email);
}
