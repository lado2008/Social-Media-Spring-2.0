package com.example.social_media.security;

import com.example.social_media.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtBasedAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;

    public JwtBasedAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authenticationHeader = request.getHeader("Authorization");
        if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            String token = authenticationHeader.replace("Bearer ", "");
            SecurityContextHolder.getContext().setAuthentication(userService.authentication(token));
        }
        filterChain.doFilter(request, response);
    }
}
