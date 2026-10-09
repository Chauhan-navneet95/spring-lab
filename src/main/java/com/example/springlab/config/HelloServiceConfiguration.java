package com.example.springlab.config;

import com.example.springlab.service.HelloService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloServiceConfiguration {

    @Bean
    @ConditionalOnMissingBean(HelloService.class)
    public HelloService defaultHelloService() {
        System.out.println("Creating default HelloService");
        return new HelloService();
    }
}