package com.yunjun.store2.controllers;

import com.yunjun.store2.config.JwtConfig;
import com.yunjun.store2.dtos.*;
import com.yunjun.store2.services.JwtService;
import com.yunjun.store2.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final JwtService jwtService;

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
        var userDto = userService.getUserByEmail(request.getEmail());
        var refreshToken = jwtService.generateRefreshToken(userDto);
        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new LoginResponse(jwtService.generateAccessToken(userDto).toString());
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the current user")
    public UserDto me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var principle = authentication.getPrincipal();
        return userService.getUserById((Long) principle);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Refresh the access token with refresh token")
    public LoginResponse refreshToken(@CookieValue(value = "refreshToken") String refreshToken) throws BadCredentialsException {
        var jwt = jwtService.parse(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        var userDto = userService.getUserById(jwt.getUserId());
        return new LoginResponse(jwtService.generateAccessToken(userDto).toString());
    }

    /**
     * Client-Side Logout
     * - Delete access token from the memory / storage
     * - Delete refresh token by cleaning the cookie
     * - Simple to implement, but not secure,
     * - Tokens are still valid until they expire.
     * - Redirect to the login page
     * - Better suites for small applications
     *
     * Server-Side Logout
     * - Store a list of active / removed tokens in the database
     * - When a user logs out, mark the token as invalid in the database
     * - During each request, check if the token is blacklisted or not
     * - Provides a true logout experience for large applications
     * - Adds complexity and requires additional token db lookup for each request
     * - Best for high-security applications
     *
     * TODO: complete this API after course finished.
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Logout the current user")
    public void logout() {

    }

    /**
     * Manual features for security to build a secure application
     * - Email verification
     * - Password reset
     * - Account lockout
     * - Social login
     * - Multi-factor authentication
     * - Multi-device sessions
     * - Brute-force protection
     * ...
     *
     * Instead, use of the options below to improve security and reduce complexity.
     * Auth Providers to automate authentication
     * - Auth0
     * - Amazon Cognito
     * - Firebase Authentication
     * - Okta
     * Can build and ship apps faster and with fewer security vulnerabilities.
     * But is expensive.
     * Use auth provider when:
     * - Building a startup
     * - Building an MVP
     * - If speed matters more
     *
     * Roll your own when:
     * - If operating at scale
     * - Have specific requirements
     * - Want full control over the authentication process
     */
}
