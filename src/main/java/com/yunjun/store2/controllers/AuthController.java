package com.yunjun.store2.controllers;

import com.yunjun.store2.dtos.JwtResponse;
import com.yunjun.store2.dtos.LoginUserRequest;
import com.yunjun.store2.dtos.RegisterUserRequest;
import com.yunjun.store2.dtos.UserDto;
import com.yunjun.store2.services.JwtTokenService;
import com.yunjun.store2.services.UserService;
import com.yunjun.store2.services.impls.JwtTokenServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

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
    public JwtResponse loginUser(@Valid @RequestBody LoginUserRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        return jwtTokenService.generateToken(request.getEmail());
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate a token")
    @ResponseStatus(HttpStatus.OK)
    public boolean validateToken(@RequestHeader("Authorization") String authHeader) {
//        var token = authHeader.replace("Bearer ", "");
        return jwtTokenService.validate(authHeader);
    }
}
