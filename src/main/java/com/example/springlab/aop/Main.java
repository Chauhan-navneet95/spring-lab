package com.example.springlab.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {

        GreetingService target = new GreetingServiceImpl();

        InvocationHandler handler = (proxy, method, arguments) -> {
            System.out.println("Before Method Call");

            try {
                return method.invoke(target, arguments);
            } catch (InvocationTargetException exception) {
                throw exception.getCause();
            } finally {
                System.out.println("After Method Call");
            }
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
