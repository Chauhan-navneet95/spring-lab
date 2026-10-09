package com.example.springlab.controller;

import com.example.springlab.service.GreetingService;
import com.example.springlab.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    //private final HelloService helloService;

    //constructor injection
    /*public HelloController(HelloService helloService) {
        System.out.println("Inside Hello Controller Constructor");
        this.helloService = helloService;
    }
    @GetMapping("/hello")
    public String hello() {
        return helloService.getMessage();
    }

    */
    //constructor injection

    private final GreetingService greetingService;
    public HelloController(GreetingService greetingService) {
        this.greetingService = greetingService;
        System.out.println("Inside Hello Controller Constructor");
    }


    @GetMapping("/greet")
    public void sayhello() {
        greetingService.sayHello();
    }
}