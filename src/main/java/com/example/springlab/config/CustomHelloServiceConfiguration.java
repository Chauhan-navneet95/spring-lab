package com.example.springlab.config;

import com.example.springlab.service.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomHelloServiceConfiguration {

    @Bean
    public HelloService customHelloService() {
        return new HelloService() {
            @Override
            public String getMessage() {
                return "Custom HelloService!";
            }
        };
    }
}