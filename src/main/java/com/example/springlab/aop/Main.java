package com.example.springlab.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {

        GreetingService target = new GreetingServiceImpl();

        InvocationHandler handler = (proxy, method, argument) -> {
            System.out.println("Before Method Call");

            Object result = method.invoke(target, argument);

            System.out.println("After Method Call");

            return result;
        };

        GreetingService proxy = (GreetingService) Proxy.newProxyInstance(
                GreetingService.class.getClassLoader(),
                new Class[]{GreetingService.class},
                handler
        );
        String greeting = proxy.greet("Navneet");
        System.out.println(greeting);
    }
}
