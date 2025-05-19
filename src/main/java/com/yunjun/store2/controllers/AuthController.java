package com.yunjun.store2.controllers;

import com.yunjun.store2.config.JwtConfig;
import com.yunjun.store2.dtos.*;
import com.yunjun.store2.entities.Role;
import com.yunjun.store2.mappers.UserMapper;
import com.yunjun.store2.services.JwtTokenService;
import com.yunjun.store2.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final JwtConfig jwtConfig;
    private final JwtTokenService jwtTokenService;
    private final UserMapper userMapper;

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
    @Operation(summary = "Login a user with credentials")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse loginUser(
            @Valid @RequestBody LoginUserRequest request,
            HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail() ,
                        request.getPassword()
                )
        );
        // after authenticated, we get the user from the db
        var userDto = userService.getUserByEmail(request.getEmail());

        var refreshToken = jwtTokenService.generateRefreshToken(userDto);
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return jwtTokenService.generateAccessToken(userDto);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the current user")
    public UserDto me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principle = authentication.getPrincipal();
        return (UserDto) principle;
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Refresh the access token with refresh token")
    public LoginResponse refreshToken(@CookieValue(value = "refreshToken") String refreshToken) throws IllegalAccessException {
        if (!jwtTokenService.validate(refreshToken)) {
            throw new IllegalAccessException("Invalid refresh token! Please login again to get a new refresh token.");
        }
        var userId = jwtTokenService.getUserIdFromToken(refreshToken);
        var userDto = userService.getUserById(userId);
        return jwtTokenService.generateAccessToken(userDto);
    }
}
