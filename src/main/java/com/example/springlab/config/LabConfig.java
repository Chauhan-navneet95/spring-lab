package com.example.springlab.config;

import com.example.springlab.service.LabMessageFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LabConfig {

    @Bean
    public LabMessageFormatter configuredMessageFormatter() {
        return new LabMessageFormatter();
    }
}


//The bean's default name is configuredMessageFormatter, derived from the method name.