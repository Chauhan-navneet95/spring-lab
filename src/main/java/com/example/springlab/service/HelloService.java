package com.example.springlab.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//commenting annotation to test Configuration class in config package
@Service
public class HelloService {

    @Value("${app.greeting.prefix}")
    private String prefix;

    @Value("${app.greeting.audience}")
    private String audience;

    public String getMessage(){
       return prefix + " "+ audience;
    }

}
