package com.yunjun.store2.config;

import com.yunjun.store2.entities.Role;
import com.yunjun.store2.filters.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Authentication Methods
 * Session-based:
 * server needs to allocate large memory for all users' sessions
 * as each user needs one sessionId saved
 * on the server's memory;
 *                            POST/login
 *    Client --------------------------------------------> Server
 *                                                         checks credential,
 *                                                         creates and stores a session: SID34 = user1
 *    Client <-------------------------------------------- Server
 * stores(cookie: SID34)        SID34
 *
 *
 *                             GET/order
 *    Client --------------------------------------------> Server
 *     SID34                 Cookie:{SID34}                getSessionId(Cookie:SID34), getsBySessionId(user1)
 *                                                         ...
 *
 *
 * Token-based:
 * Servers no longer need to save the user's sessionId in their memory.
 * Servers will issue a JWT token which is self-contained and saved on the client side.
 * Token Structure:
 *  - Header: Type, Algorithm, a seal to show the token hasn't been touched.
 *  - Payload: sub - userId, iat - issued at
 *  - Signature: secrete to decode the token,
 *               generated with the algorithm based on the payload content
 * It is:
 * Stateless,
 * Scalable,
 * Works well with front-end apps
 *
 *
 *                            POST/login
 *    Client --------------------------------------------> Server
 *                                                         checks credential,
 *                                                         creates a token, stores the token secret
 *    Client <-------------------------------------------- Server
 * stores(token)                 token
 *
 *
 *
 *                            GET/order
 *     Client -------------------------------------------> Server
 * Memory(token)             Auth:{token}                  verifies(token),decode(secrete, token)
 *                                                         extracts user
 *                                                         checks credential
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configure Spring Security to use the AuthenticationProvider -> AuthenticationManager
     * for login operations.
     *
     * DaoAuthenticationProvider is a default implementation of AuthenticationProvider.
     * There are authentications using LDAP, oAuth, Active Directory, etc.
     *
     * @param configuration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder( passwordEncoder() );
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    /**
     * PasswordEncoder is used to encode the password before storing it in the database.
     * It is an interface.
     * It has two implementations, BCryptPasswordEncoder and Pbkdf2PasswordEncoder.
     * BCryptPasswordEncoder is the default implementation and the most secure option.
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Step 1. Declare it is a stateless session
     * Step 2. Disable csrf
     * Step 3. Only allow access to the following paths: POST: /api/carts, POST: /api/auth/register, POST: /api/auth/login, /api/carts/{cartId}/**
     *         so that the other paths are not accessible by anyone unless they are authenticated.
     *         This means a user can create a cart, view/add/update/delete cart/cartItems or log in to the system without being authenticated.
     * Step 4. Build the filter chain
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c
                        .requestMatchers(HttpMethod.POST, "/api/carts").permitAll()
                        .requestMatchers( "/api/carts/{cartId}/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/refresh").permitAll()
                        .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
                        // .requestMatchers(HttpMethod.POST, "/api/auth/validate").permitAll()
                        .anyRequest().authenticated())
                // make sure this is the first filter once gets a request
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c -> {
                    // make sure all auth entry points get 401 - unauthorized, which means the user is not authenticated or credentials are invalid.
                    c.authenticationEntryPoint(
                            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    // when it is for role-based authenticated, but the user has no permission to access the resource.
                    c.accessDeniedHandler(((request, response, accessDeniedException) ->
                            response.setStatus(HttpStatus.FORBIDDEN.value())));
                });
        return http.build();
    }
}
