package com.revature.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collections;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws java.io.IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String username = jwtUtil.getUsernameFromToken(token);
                if (jwtUtil.validateToken(token, username)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()); // use username to create principals
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTVerificationException exception) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);// Ensure no invalid authentication data remains
                response.getWriter().write("Invalid JWT token: " + exception.getMessage());
                // TODO: log or handle exception
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // TODO: revoke tokens and refresh tokens
}
