package com.revature.security;

import com.revature.security.jwt.JwtAuthenticationFilter;
import com.revature.security.jwt.JwtAuthorizationFilter;
import com.revature.security.jwt.JwtUtil;
import com.revature.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    // use constructor injection for immutability
    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // ensures spring security uses CustomUserDetailsService and PasswordEncoder for its authentication process
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return auth.build();
    }

    // configures HTTP request security declaring how requests are secured
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager, JwtUtil jwtUtil) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authManager, jwtUtil); // jwtAuthenticationFilter
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtUtil); // jwtAuthorizationFilter

        jwtAuthenticationFilter.setFilterProcessesUrl("/login"); // configure authentication filter trigger path

        http
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests // specify which URL patterns are secured and which are permitted
                        .requestMatchers("/", "/login").permitAll() // "/", "/login" is accessible to everyone
                        .anyRequest().authenticated() // all other requests require authentication
                )
                .logout((logout) -> logout.permitAll()); // all users can logout
        return http.build();
    }

    // use BCrypt to hash passwords
    // Make a bean to use in service layer (dependency injection)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
