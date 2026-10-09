package com.example.springlab.service;

import org.springframework.stereotype.Component;

@Component
public class LabMessageFormatter {


    public String format(String message) {
        return "[SPRING-LAB] " + message;
    }
}

//default name for bean created by spring application context is classname with first letter lower cased i.e labMessageFormater