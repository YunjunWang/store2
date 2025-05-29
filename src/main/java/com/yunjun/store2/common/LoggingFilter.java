package com.yunjun.store2.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Filter start...");
        System.out.println("Request URL: " + request.getRequestURL());
        filterChain.doFilter(request, response);
        System.out.println("Response Status: " + response.getStatus());
        System.out.println("Filter end...");

    }
}
