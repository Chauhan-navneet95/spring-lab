package com.example.springlab.aop;

public class GreetingServiceImpl implements GreetingService {
    @Override
    public String greet(String name){

        System.out.println("Inside Greet Method");
        return "Hello " + name;
    }
}
