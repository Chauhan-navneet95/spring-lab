package com.example.springlab.controller;

import com.example.springlab.service.GreetingService;
import com.example.springlab.service.HelloService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    //Qualifier   - notice the naming convention, spring created bean with first letter of class small.

    private final GreetingService greetingService;
    //constructor injection
    public HelloController( @Qualifier("formalGreetingService") GreetingService greetingService) {
        this.greetingService = greetingService;
        System.out.println("Inside Hello Controller Constructor");
    }


    @GetMapping("/greet")
    public String sayhello() {
        return greetingService.sayHello();
    }
}