package com.booking.booking_api.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for testing
            .authorizeHttpRequests()
            .requestMatchers("/api/auth/**").permitAll() // Allow register/login\
            .requestMatchers("/api/category/**").authenticated()
            .anyRequest().authenticated() // All other endpoints need auth
            .and()
            .httpBasic(); // For testing, simple basic auth

        return http.build();
    }
}
