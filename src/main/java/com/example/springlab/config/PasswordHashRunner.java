package com.example.springlab.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordHashRunner {

   /* @Bean
    CommandLineRunner printPasswordHashes(
            PasswordEncoder passwordEncoder) {

        return args -> {
            System.out.println(
                    "labuser hash: " +
                            passwordEncoder.encode("LabPass123!")
            );

            System.out.println(
                    "viewer hash: " +
                            passwordEncoder.encode("ViewerPass123!")
            );
        };
    }*/
}