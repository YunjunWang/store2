package com.yunjun.store2.auth;

import com.yunjun.store2.users.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    @Operation(summary = "Login a user with credentials")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse loginUser(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        var loginResult = authService.loginUser(request, response);
        var cookie = new Cookie("refreshToken", loginResult.getRefreshToken().toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new JwtResponse(loginResult.getAccessToken().toString());
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the current user")
    public UserDto me() {
        return authService.getCurrentUser();
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Refresh the access token with refresh token")
    public JwtResponse refreshToken(@CookieValue(value = "refreshToken") String refreshToken) throws BadCredentialsException {
        var accessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponse(accessToken.toString());
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
