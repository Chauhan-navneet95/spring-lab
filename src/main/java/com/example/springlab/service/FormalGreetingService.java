package com.example.springlab.service;

import org.springframework.stereotype.Service;

@Service
public class FormalGreetingService implements GreetingService {
    @Override
    public void sayHello() {
        System.out.println("Formal GreetingService");
    }
}
