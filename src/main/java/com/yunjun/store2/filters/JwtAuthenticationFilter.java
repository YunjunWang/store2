package com.yunjun.store2.filters;

import com.yunjun.store2.services.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;
    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // validate token
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        var token = authHeader.replace("Bearer ", "");
        if (!jwtTokenService.validate(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        /* new UsernamePasswordAuthenticationToken(Object principle) for unauthorized users
         * new UsernamePasswordAuthenticationToken(Object principle, Object credentials, Collection<?> authorities) for authorized users
         *
         * set authentication with user email from a token
         * we don't get the user email from the db here to avoid performance issue as it'll be a db query / request
         */
        var authentication = new UsernamePasswordAuthenticationToken(
                jwtTokenService.getEmailFromToken(token), // user object, either user, username, email etc.
                null, // password, here we don't need
                null // roles and permissions, for authenticated users, no need here
        );
        // add request metadata into the authentication details
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        // set it for further usage, e.g., to access the current user
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
