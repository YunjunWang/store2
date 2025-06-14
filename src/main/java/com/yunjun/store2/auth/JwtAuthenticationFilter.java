package com.yunjun.store2.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // extract the header authorization value
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            /* Pass to the next filter in the chain,
             * It will eventually hit the API endpoint
             * Spring Security will kick in,
             * If the endpoint is protected, Spring will return 403
             */
            filterChain.doFilter(request, response);
            return;
        }
        // validate token
        var token = authHeader.replace("Bearer ", "");
        var jwt = jwtService.parse(token);
        if (jwt == null || jwt.isExpired()) {
            // Pass to the next filter in the chain
            filterChain.doFilter(request, response);
            return;
        }

        /* When the token is valid, we need to tell Spring to allow user to
         * access the requested resource
         * To do this, we need to first create an authentication object.
         *
         * UsernamePasswordAuthenticationToken(Object principle) constructor
         * represents unauthenticated / anonymous users.
         * It is used for registering users
         * UsernamePasswordAuthenticationToken(Object principle, Object credentials, Collection<?> authorities) constructor
         * represents authenticated users
         *
         * Set authentication with user email from a token.
         * We don't get the user email from the db here to
         * avoid performance issue as it'll be a db query / request,
         * and we don't want to make this db query for each Http request.
         * Instead, we get it from the token.
         */
        var authentication = new UsernamePasswordAuthenticationToken(
                jwt.getUserId(),
                //jwtTokenService.getEmailFromToken(token), // user object, either user, username, email etc.
                null, // password, here we don't need for authenticated users, so we set it as null
                // null // roles and permissions, for authenticated users. No need here before implementing the role-based access
                /*
                 * authorities:
                 * 1. Roles (ADMIN, USER, etc.), roles have to start with "ROLE_" + role_name as the example below, which permission no need
                 * 2. Permissions(e.g. ISSUE_REFUND)
                 */
                List.of(new SimpleGrantedAuthority("ROLE_" + jwt.getRole().name()))
        );
        /* add request metadata into the authentication details,
         * e.g., IP address
         */
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        /* set it for further usage, e.g., to access the current user
         * SecurityContextHolder holds the authenticated user
         */
        SecurityContextHolder.getContext().setAuthentication(authentication);

        /* JwtAuthenticationFilter finishes,
         * Pass the control to the next filter in the filter chain
         * It will eventually hit the API endpoint
         * to allow user to access the resource that is requested
         *
         * Now this filter is ready.
         * Next is to make sure
         * this filter is the first filter to be hit
         * inside the securityFilterChain of SecurityConfig class,
         * because filter order matters for Http Security.
         */
        filterChain.doFilter(request, response);
    }
}
