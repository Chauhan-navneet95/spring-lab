package com.example.springlab.service;

import com.example.springlab.config.GreetingProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//commenting annotation to test Configuration class in config package
@Service
public class HelloService {

    private final GreetingProperties properties;

    public HelloService(GreetingProperties properties) {
        this.properties = properties;
    }

    public String getMessage() {
        return properties.getPrefix()
                + ", "
                + properties.getAudience()
                + "!";
    }
}
