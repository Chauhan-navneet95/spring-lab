# Spring Boot Phase 12 --- AOP & Proxies

**Revision notes \| Spring Boot 4.1.1 · Java 21**

## 1. Why AOP?

**Aspect-Oriented Programming (AOP)** helps separate *cross-cutting
concerns*---behavior needed across many methods---from business logic.
Examples: logging, execution timing, transactions, caching, and
method-level authorization.

Without AOP, infrastructure code can become duplicated throughout
services. With proxy-based Spring AOP, Spring can intercept eligible
method calls and apply additional behavior without changing the target
method.

## 2. Proxy: the core idea

A **proxy** is an intermediary object through which a caller invokes a
target. It can run behavior before, after, or around the target
invocation.

``` text
Caller → Spring proxy/advice → target method
                     ← result/exception ←
```

-   **Target:** object containing business logic.
-   **Proxy:** object that intercepts calls and applies extra behavior.
-   **Interception:** the proxy runs additional logic around a matched
    method call.

This helps explain `@Transactional`: a call through Spring's proxy can
apply transaction handling around target execution.

## 3. Plain Java: JDK dynamic proxy

JDK dynamic proxies work through **interfaces** and an
`InvocationHandler`.

``` java
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
        new Class<?>[]{GreetingService.class},
        handler
);

String message = proxy.greet("Navneet");
System.out.println(message);
```

-   `Proxy.newProxyInstance(...)` creates the proxy.
-   `InvocationHandler.invoke(...)` defines the intercepted behavior.
-   `method.invoke(target, arguments)` calls the real implementation
    through reflection.
-   `finally` runs whether the target returns or throws.
-   Reflection wraps a target exception in `InvocationTargetException`.
    Unwrapping its cause avoids leaking that checked wrapper through an
    interface method that does not declare it.

If the target throws `IllegalStateException("Greeting failed")`, the
`finally` message still prints, and the original unchecked exception
propagates. The process exits with an error if nobody catches it.

## 4. JDK proxy vs. CGLIB proxy

  -----------------------------------------------------------------------
  JDK dynamic proxy                   CGLIB/class-based proxy
  ----------------------------------- -----------------------------------
  Implements one or more interfaces   Creates a subclass of the target
                                      class

  Caller uses an interface reference  Can proxy a class without an
                                      interface

  Does not expose target-only methods Can intercept eligible overridable
  absent from its interfaces          methods

  Cannot intercept private methods    Cannot subclass a `final` class or
  through interface dispatch          override `final` methods
  -----------------------------------------------------------------------

Spring Boot commonly uses class-based proxies by default for Spring AOP.
In SpringLab, `AopUtils` reported:

``` text
Runtime class: com.example.springlab.service.ProductService$$SpringCGLIB$$0
Is JDK proxy: false
Is CGLIB proxy: true
```

This confirmed the injected `ProductService` reference was a CGLIB
proxy. `AopUtils.isJdkDynamicProxy(bean)` and
`AopUtils.isCglibProxy(bean)` are useful diagnostics.

## 5. Spring AOP: our working aspect

We created `ServiceLoggingAspect`. The service's business logic did not
need to change.

``` java
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
            System.out.println("[AOP] Failed: " + methodName
                    + " -> " + exception.getClass().getSimpleName());
            throw exception;
        }
    }
}
```

Imports:

``` java
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
```

The pointcut expression:

``` java
execution(* com.example.springlab.service.ProductService.*(..))
```

matches executions of any method, with any return type and any
arguments, in `ProductService`.

For `GET /api/products`, we observed:

``` text
[AOP] Before: ProductService.findAll()
Hibernate: select ... from products p1_0
[AOP] After: ProductService.findAll()
```

SQL and the JSON response remained normal. `joinPoint.proceed()` invokes
the underlying operation and returns its result. The aspect should
return that result to preserve method behavior. If an exception occurs,
our advice logs and rethrows it rather than swallowing it.

## 6. Three core AOP terms

-   **Advice --- what happens?** The additional code to run.
    `logServiceCalls()` is our advice.
-   **Pointcut --- where should it happen?** The expression selecting
    which method executions match.
-   **Join point --- which execution?** In Spring AOP, an interceptable
    method execution. `ProductService.findAll()` was one of ours.

**Memory aid:** Advice = *what*; pointcut = *where*; join point = *the
matched execution*.

We deliberately misspelled `service` as `services` in the pointcut. The
application and endpoint still worked, but the AOP logs disappeared
because the method no longer matched. A registered aspect can have no
effect if its pointcut matches nothing.

## 7. Other advice types

  -----------------------------------------------------------------------
  Annotation                          Purpose
  ----------------------------------- -----------------------------------
  `@Before`                           Runs before the matched method

  `@AfterReturning`                   Runs after successful return

  `@AfterThrowing`                    Runs when the method throws

  `@After`                            Runs after completion, including
                                      exceptional completion

  `@Around`                           Wraps the invocation; can proceed,
                                      inspect the result, or
                                      handle/rethrow exceptions
  -----------------------------------------------------------------------

`@Around` is the most flexible. `ProceedingJoinPoint.proceed()`
continues the intercepted invocation. Consider both success and
exception paths.

## 8. Self-invocation: a critical limitation

``` java
public void outerMethod() {
    innerMethod(); // equivalent to this.innerMethod()
}

public void innerMethod() {
    System.out.println("Inside innerMethod");
}
```

When an external caller invokes `outerMethod()` through the proxy, that
external call can be intercepted. But the internal call from
`outerMethod()` to `innerMethod()` uses `this` on the target object and
does **not** pass through the proxy again. Advice on `innerMethod()`
therefore does not trigger for that internal call.

This limitation also matters for proxy-based uses of `@Transactional`,
`@Async`, and method security. A common design solution is to move the
separately intercepted operation into another Spring bean and call it
through that bean's Spring-managed proxy.

## Key takeaways

1.  A proxy adds behavior around calls; the target still contains
    business logic.
2.  JDK proxies are interface-based; CGLIB proxies are subclass-based.
3.  Advice defines *what*, pointcuts define *where*, and join points are
    matched method executions.
4.  `@Around` uses `proceed()` to continue to the target and should
    preserve return values and exception behavior.
5.  A wrong pointcut can stop advice from matching; self-invocation
    bypasses proxy interception.
6.  This proxy model helps explain Spring transaction handling and
    method-level security. Spring Security's HTTP filter chain is a
    separate mechanism.
