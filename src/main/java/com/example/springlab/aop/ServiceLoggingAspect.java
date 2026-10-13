
package com.example.springlab.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {

    @Around(
            "execution(* com.example.springlab.service.ProductService.*(..))"
    )
    public Object logServiceCalls(ProceedingJoinPoint joinPoint)
            throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();

        System.out.println("[AOP] Before: " + methodName);

        try {
            Object result = joinPoint.proceed();

            System.out.println("[AOP] After: " + methodName);

            return result;
        } catch (Throwable exception) {
            System.out.println(
                    "[AOP] Failed: " + methodName
                            + " -> " + exception.getClass().getSimpleName()
            );

            throw exception;
        }
    }
}
