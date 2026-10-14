package com.example.springlab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // Temporary setting for our Postman lab
                .csrf(csrf -> csrf.disable())

                // Enable HTTP Basic authentication
                .httpBasic(Customizer.withDefaults())

                // Define request authorization rules
                .authorizeHttpRequests(authorize -> authorize

                        // Public read operations
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/products",
                                "/api/products/**"
                        ).permitAll()
                        //post filter authorization
                        .requestMatchers(HttpMethod.POST, "/api/products")
                                .hasRole("USER")
                        // Everything else requires authentication
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}