package com.yunjun.store2.controllers;

import com.yunjun.store2.dtos.LoginUserRequest;
import com.yunjun.store2.dtos.RegisterUserRequest;
import com.yunjun.store2.dtos.UserDto;
import com.yunjun.store2.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder) throws IllegalArgumentException{
        // We can never decode it when the user login, we'll hash it again to compare with the database
        // password should be encoded in the API layer to avoid inherited security vulnerabilities.
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        var userDto = userService.registerUser(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user")
    @ResponseStatus(HttpStatus.OK)
    public void loginUser(@Valid @RequestBody LoginUserRequest request) throws IllegalAccessException {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.loginUser(request);
    }
}
