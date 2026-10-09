package com.example.springlab.service;

import org.springframework.stereotype.Service;

@Service
public class FriendlyGreetingService implements GreetingService {
    @Override
    public void sayHello() {
        System.out.println("Friendly GreetingService");
    }
}
