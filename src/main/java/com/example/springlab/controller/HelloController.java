package com.example.springlab.controller;

import com.example.springlab.service.GreetingService;
import com.example.springlab.service.HelloService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
            public String getMessage(){
        return helloService.getMessage();
    }


}