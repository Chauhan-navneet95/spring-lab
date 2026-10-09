package com.example.springlab.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class FriendlyGreetingService implements GreetingService {
    @Override
    public String sayHello() {
        System.out.println("Friendly GreetingService");
        return "Hello Friend";
    }
}
