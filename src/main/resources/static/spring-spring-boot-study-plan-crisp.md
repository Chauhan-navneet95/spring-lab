# Spring & Spring Boot Study Roadmap

> **Goal:** Build strong Spring intuition first, then progress to advanced Spring, security, microservices, Kubernetes and Spring AI.
>
> **Starting point:** Strong Java knowledge; familiar with Controllers/DTOs and production systems, but limited hands-on Spring Boot development from scratch.
>
> **Learning philosophy:** **Build → Observe → Understand → Break → Fix → Generalize**

## Table of Contents

- [Phase 1 — First Spring Boot Application](#phase-1--first-spring-boot-application)
- [Phase 2 — REST API Fundamentals](#phase-2--rest-api-fundamentals)
- [Phase 3 — Controller → Service → Dependency Injection](#phase-3--controller--service--dependency-injection)
- [Phase 4 — Spring Core: IoC, DI & Beans](#phase-4--spring-core-ioc-di--beans)
- [Phase 5 — Configuration](#phase-5--configuration)
- [Phase 6 — Spring Boot Internals](#phase-6--spring-boot-internals)
- [Phase 7 — DTOs, Validation & Error Handling](#phase-7--dtos-validation--error-handling)
- [Phase 8 — Testing From Here Onward](#phase-8--testing-from-here-onward)
- [Phase 9 — Database: JPA → Hibernate → Spring Data](#phase-9--database-jpa--hibernate--spring-data)
- [Phase 10 — JPA/Hibernate Deep Dive](#phase-10--jpahibernate-deep-dive)
- [Phase 11 — Transactions](#phase-11--transactions)
- [Phase 12 — AOP & Proxies](#phase-12--aop--proxies)
- [Phase 13 — Custom Annotations](#phase-13--custom-annotations)
- [Phase 14 — Spring Security](#phase-14--spring-security)
- [Phase 15 — JWT → OAuth 2.0 → OIDC](#phase-15--jwt--oauth-20--oidc)
- [Phase 16 — HTTP Clients, Async & Scheduling](#phase-16--http-clients-async--scheduling)
- [Phase 17 — Production Spring Boot](#phase-17--production-spring-boot)
- [Phase 18 — Docker](#phase-18--docker)
- [Phase 19 — Monolith → Microservices](#phase-19--monolith--microservices)
- [Phase 20 — Microservices & Resilience](#phase-20--microservices--resilience)
- [Phase 21 — Kafka & Transactional Outbox](#phase-21--kafka--transactional-outbox)
- [Phase 22 — Kubernetes Basics](#phase-22--kubernetes-basics)
- [Phase 23 — Spring AI](#phase-23--spring-ai)
- [Phase 24 — Final Capstone](#phase-24--final-capstone)

## 1. The Big Picture

```text
Java
 │
 ▼
Why Spring?
 │
 ├── IoC / DI
 ├── Beans
 └── ApplicationContext
 │
 ▼
Spring Boot
 │
 ├── Auto Configuration
 ├── Configuration
 └── Application Startup
 │
 ▼
Spring Web
 │
 ├── Controllers
 ├── REST
 ├── DTOs
 └── Validation
 │
 ▼
Data
 │
 ├── JPA
 ├── Hibernate
 └── Transactions
 │
 ▼
Security
 │
 ├── Security Filter Chain
 ├── JWT
 └── OAuth2 / OIDC
 │
 ▼
Production
 │
 ├── Testing
 ├── Actuator
 ├── Observability
 ├── Redis
 └── Docker
 │
 ▼
Distributed Systems
 │
 ├── HTTP Clients
 ├── Async / Scheduling
 ├── Microservices
 ├── Resilience
 ├── Kafka
 ├── Outbox
 └── Kubernetes
 │
 ▼
Spring AI
 │
 ├── ChatClient
 ├── Structured Output
 ├── Tool Calling
 ├── RAG
 └── MCP
```

# Phase 1 — First Spring Boot Application

**Goal:** Become comfortable creating and running a Spring Boot project from scratch.

### Learn

- What Spring is
- Why Spring exists
- Spring vs Spring Boot
- Spring Initializr
- Maven project structure
- `pom.xml`
- JDK and dependencies
- `src/main`, `src/test`, `resources`
- Main application class
- `@SpringBootApplication`
- Embedded server
- `application.properties` / `application.yml`

### Build

```text
spring-lab
    └── Hello World API
```

```java
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
```

### Questions

- What happens when the application starts?
- Who starts the web server?
- How is `HelloController` discovered?
- How does `/hello` reach the Java method?
- How does the Java return value become an HTTP response?

# Phase 2 — REST API Fundamentals

**Goal:** Build a small API while understanding the request flow.

```text
HTTP Request
     ↓
Spring Boot
     ↓
Controller
     ↓
Java Method
     ↓
HTTP Response
```

### Learn

```java
@RestController
@RequestMapping
@GetMapping
@PostMapping
@PutMapping
@PatchMapping
@DeleteMapping
@PathVariable
@RequestParam
@RequestBody
@RequestHeader
```

### Build

```text
GET    /products
GET    /products/{id}
POST   /products
PUT    /products/{id}
DELETE /products/{id}
```

Keep it in-memory initially. **Do not introduce database complexity yet.**

# Phase 3 — Controller → Service → Dependency Injection

Introduce:

```text
Controller
    ↓
Service
```

```java
@RestController
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }
}
```

Important question:

> **Who created `ProductService`?**

This leads into Spring's core.

# Phase 4 — Spring Core: IoC, DI & Beans

**First major conceptual deep dive.**

```text
Java Object
    ↓
Spring Bean
    ↓
Spring Container
    ↓
ApplicationContext
```

### Learn

- IoC
- Dependency Injection
- Constructor injection
- Spring Container
- `ApplicationContext`
- Bean / BeanDefinition
- Component scanning
- Bean creation
- Bean lifecycle
- Singleton / prototype
- `@Component`
- `@Service`
- `@Repository`
- `@Configuration`
- `@Bean`
- `@Primary`
- `@Qualifier`

### Mental model

```text
@Component
     ↓
Component Scan
     ↓
Bean Definition
     ↓
Object Creation
     ↓
Bean
     ↓
ApplicationContext
```

### Experiments

- Two beans of the same type
- `@Primary`
- `@Qualifier`
- Missing dependency
- Prototype vs singleton
- Constructor injection vs `new`

# Phase 5 — Configuration

Understand:

```text
application.properties
application.yml
       ↓
Spring Environment
       ↓
Application configuration
```

### Learn

- Configuration properties
- `@Value`
- `@ConfigurationProperties`
- Profiles
- Environment variables
- `application-dev.yml`
- `application-test.yml`
- `application-prod.yml`

### Questions

- Where does configuration come from?
- Which value wins when multiple sources exist?
- How should secrets be handled?

# Phase 6 — Spring Boot Internals

Now that we've used Boot, look underneath it.

```text
@SpringBootApplication
        │
        ├── Configuration
        ├── Component Scan
        └── Auto Configuration
```

### Learn

- `SpringApplication`
- Application startup
- Auto-configuration
- Conditional configuration
- `@ConditionalOnClass`
- `@ConditionalOnMissingBean`
- `@ConditionalOnProperty`
- BeanPostProcessor
- ApplicationContext startup

### Goal

> **Why did Spring automatically create/configure this component?**

# Phase 7 — DTOs, Validation & Error Handling

```text
HTTP JSON
   ↓
Request DTO
   ↓
Validation
   ↓
Controller
   ↓
Service
   ↓
Response DTO
```

### Learn

- Request / response DTOs
- Java records as DTOs
- Mapping
- `@Valid`
- `@Validated`
- `@NotNull`
- `@NotBlank`
- `@Size`
- `@ControllerAdvice`
- `@ExceptionHandler`
- Error response design

# Phase 8 — Testing From Here Onward

Testing is **not a final phase**. Every new feature gets tests.

### Unit

```text
Service
   ↓
JUnit + Mockito
```

### Web

```text
Controller
   ↓
MockMvc
```

### Integration

```text
Spring Boot
     ↓
PostgreSQL
     ↓
Testcontainers
```

### Learn

- JUnit 5
- Mockito
- Mock vs stub
- Verification
- `@SpringBootTest`
- `@WebMvcTest`
- MockMvc
- Integration tests
- Testcontainers

# Phase 9 — Database: JPA → Hibernate → Spring Data

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Spring Data
    ↓
JPA
    ↓
Hibernate
    ↓
JDBC
    ↓
PostgreSQL
```

### Learn

- JDBC at a high level
- JPA
- Hibernate
- Spring Data JPA
- `@Entity`
- `@Id`
- Repository
- Entity lifecycle
- Persistence Context
- Managed / detached / transient
- Dirty checking
- Lazy loading
- Relationships
- JPQL
- Query methods

> **JPA = specification; Hibernate = implementation; Spring Data = repository abstraction.**

# Phase 10 — JPA/Hibernate Deep Dive

**Major senior-level intuition phase.**

### Learn

- Persistence context
- First-level cache
- Dirty checking
- Flush
- Lazy vs eager loading
- Proxies
- N+1 problem
- Fetch joins
- Entity graphs
- Pagination
- Batch operations
- Query performance

### Lab experiments

1. Trigger N+1.
2. Inspect generated SQL.
3. Fix N+1.
4. Trigger lazy loading failure.
5. Compare lazy/eager behaviour.
6. Compare entity vs DTO queries.

# Phase 11 — Transactions

Understand `@Transactional` before memorizing its options.

### Learn

- ACID
- Transaction boundaries
- Commit
- Rollback
- Propagation
- Isolation
- Read-only transactions
- Rollback rules
- Transaction manager
- Proxy mechanics
- Self-invocation problem

### Experiment

```text
Order creation
     ↓
Save Order
     ↓
Save Payment
     ↓
Failure
```

Observe what gets committed and rolled back.

# Phase 12 — AOP & Proxies

```text
Your object
    ↓
Spring Proxy
    ↓
Your method
```

### Learn

- Cross-cutting concerns
- Proxy
- JDK dynamic proxy
- CGLIB/class-based proxy
- Advice
- Pointcut
- Join point
- `@Aspect`
- `@Around`
- `@Before`
- `@After`

Connect this to:

```text
@Transactional
@Cacheable
@Async
@PreAuthorize
```

# Phase 13 — Custom Annotations

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {
}
```

Then:

```text
@Audit
   ↓
AOP
   ↓
Proxy
   ↓
Audit logic
   ↓
Target method
```

# Phase 14 — Spring Security

Start with:

```text
HTTP Request
     ↓
Security Filter Chain
     ↓
Authentication
     ↓
Authorization
     ↓
Controller
```

### Learn

- Authentication
- Authorization
- SecurityFilterChain
- Filters
- Principal
- Roles
- Authorities
- Password hashing
- Method security
- CORS
- CSRF
- Stateless APIs

# Phase 15 — JWT → OAuth 2.0 → OIDC

Do these in order.

### JWT

```text
Header.Payload.Signature
```

Understand claims, expiration, signing, verification, access tokens and refresh tokens.

### OAuth 2.0

```text
Resource Owner
      ↓
Client
      ↓
Authorization Server
      ↓
Access Token
      ↓
Resource Server
```

Learn Authorization Code, PKCE, scopes, access tokens and refresh tokens.

### OIDC

```text
OAuth 2.0 = Authorization
OIDC      = Authentication / identity layer
```

### Lab

Build a secured Spring Boot Resource Server.

# Phase 16 — HTTP Clients, Async & Scheduling

## HTTP clients

```text
RestClient
    ↓
Synchronous

WebClient
    ↓
Reactive / non-blocking

HTTP Service Client
    ↓
Interface-based client
```

Learn timeouts, connection pools, error handling, retries, interceptors and observability.

## Async

```java
@Async
```

Understand TaskExecutor, thread pools, context propagation and exception handling. Connect this to the Java concurrency study.

## Scheduling

```java
@Scheduled
```

Learn fixed rate, fixed delay, cron and multi-instance scheduling problems.

# Phase 17 — Production Spring Boot

## Observability

```text
Logs
Metrics
Traces
    ↓
Observability
```

Learn:

- Actuator
- Health
- Readiness / liveness
- Micrometer
- Prometheus concepts
- OpenTelemetry concepts
- Structured logging
- Correlation IDs

## Caching

```text
Request
  ↓
Cache?
 ├── HIT → Response
 └── MISS
       ↓
    Database
       ↓
     Cache
```

Learn Redis, cache-aside, `@Cacheable`, `@CacheEvict`, TTL and cache invalidation.

# Phase 18 — Docker

```text
Spring Boot
    ↓
JAR
    ↓
Docker Image
    ↓
Container
```

Focus on Spring-specific concerns:

- JVM/container behaviour
- Environment configuration
- Health checks
- Graceful shutdown
- Image layers
- Docker Compose

Lab:

```text
Spring Boot
PostgreSQL
Redis
```

# Phase 19 — Monolith → Microservices

Start:

```text
Product
Order
User
Payment
     ↓
One Spring Boot application
```

Then ask:

> **Why would I split this?**

Understand the trade-offs between deployment simplicity, coupling, independent scaling, network failures, distributed data and eventual consistency.

# Phase 20 — Microservices & Resilience

```text
API Gateway
     │
 ┌───┼────┐
 ▼   ▼    ▼
Product Order User
```

Learn:

- Service boundaries
- Database per service
- Service-to-service REST
- Timeouts
- Retries
- Circuit breakers
- Bulkheads
- Rate limiting
- Idempotency
- Eventual consistency
- Saga concepts

Focus on distributed-systems concepts first, Spring implementation second.

# Phase 21 — Kafka & Transactional Outbox

Problem:

```text
DB COMMIT succeeds
Kafka PUBLISH fails
```

Outbox:

```text
             DB Transaction
                  │
          ┌───────┴────────┐
          ▼                ▼
      Business           Outbox
       Tables             Table
                             │
                             ▼
                         Publisher
                             │
                             ▼
                           Kafka
```

Learn Kafka basics, producers, consumers, topics, partitions, consumer groups, offsets, delivery semantics, at-least-once delivery, idempotent consumers, retry, dead-letter topics, transactional outbox, eventual consistency and CDC concepts.

# Phase 22 — Kubernetes Basics

```text
Docker Image
     ↓
Pod
     ↓
Deployment
     ↓
Service
     ↓
Ingress
```

Learn Pod, Deployment, Service, ConfigMap, Secret, readiness/liveness probes, resource limits, rolling deployment, scaling and service discovery.

Connect:

```text
Spring Actuator
      ↓
Kubernetes probes
```

# Phase 23 — Spring AI

Introduce AI as a backend capability.

```text
Spring Boot
    ↓
ChatClient
    ↓
LLM
```

### Level 1 — Chat

- ChatClient
- Models
- Prompts
- System/user messages
- Streaming

### Level 2 — Structured output

```text
LLM
 ↓
Structured response
 ↓
Java record
 ↓
Business logic
```

### Level 3 — Tool calling

```text
User
 ↓
LLM
 ↓
Tool decision
 ↓
Spring Service
 ↓
Database / API
 ↓
Tool result
 ↓
LLM
```

### Level 4 — RAG

```text
Documents
 ↓
Embeddings
 ↓
Vector Store
 ↓
Retriever
 ↓
Context
 ↓
LLM
```

### Level 5 — Production AI

- Token cost
- Latency
- Rate limits
- Retries
- Model fallback
- Structured output validation
- Prompt versioning
- Security
- Data/PII boundaries
- Observability
- Evaluation

### Level 6 — MCP

```text
LLM
 ↓
MCP
 ↓
Tools / Resources
 ↓
Enterprise systems
```

# Phase 24 — Final Capstone

Evolve the original lab into:

```text
                         Client
                           │
                           ▼
                      API Gateway
                           │
          ┌────────────────┼────────────────┐
          ▼                ▼                ▼
      Product           Order             User
      Service           Service           Service
          │                │                │
          ▼                ▼                ▼
       Product DB       Order DB          User DB
                           │
                           ▼
                         Outbox
                           │
                           ▼
                         Kafka
                      /    │    \
                     ▼     ▼     ▼
                Inventory Email Analytics

Supporting:
────────────────────────────────────
Redis
Security / OAuth2
Actuator
Metrics / Tracing
Docker
Kubernetes
CI/CD
Spring AI
```

# 25. Study Rules

## Build before deep-diving

Don't read 50 pages about IoC before writing a Spring application. Build first, then understand what happened.

## One project, progressively upgraded

```text
Hello World
    ↓
REST API
    ↓
DI
    ↓
Database
    ↓
Validation
    ↓
Testing
    ↓
Security
    ↓
Docker
    ↓
Redis
    ↓
Microservices
    ↓
Kafka
    ↓
Kubernetes
    ↓
Spring AI
```

## Break things deliberately

```text
Make it fail
    ↓
Observe failure
    ↓
Understand why
    ↓
Fix it
```

Examples: two beans of the same type, missing dependency, broken configuration, N+1 query, transaction rollback, lazy loading failure, invalid JWT, downstream timeout, duplicate Kafka event, failed outbox publication.

## Always ask "what is Spring doing?"

Instead of:

> `@Service` means service.

Ask:

> How did Spring discover this class? When was the object created? Where is it stored? Who injected it? Is it proxied? What happens when the method executes?

# 26. Core Mental Map

```text
                 SPRING BOOT
                      │
          ┌───────────┴───────────┐
          ▼                       ▼
       Container                Web
          │                       │
      IoC / DI              DispatcherServlet
      Beans                      │
      Lifecycle              Controller
      Context                     │
          │                     DTO
          │                  Validation
          │                       │
          └──────────┬────────────┘
                     ▼
                   Data
                     │
               JPA / Hibernate
                     │
                Transactions
                     │
          ┌──────────┴──────────┐
          ▼                     ▼
      Security              Integration
          │                     │
     JWT/OAuth2             HTTP Clients
       /OIDC                Async/Schedule
          │                     │
          └──────────┬──────────┘
                     ▼
                 Production
                     │
        Testing / Observability
              Redis / Docker
                     │
                     ▼
             Distributed Systems
                     │
        Microservices / Kafka
         Outbox / Kubernetes
                     │
                     ▼
                 Spring AI
```

# 27. Success Criteria

You are progressing when you can explain:

- **Spring Boot:** What happens when I run `main()`?
- **IoC:** Who creates and manages my objects?
- **DI:** How does `ProductService` appear inside my controller?
- **Web:** How does an HTTP request reach my Java method?
- **Data:** What happens between `repository.save()` and PostgreSQL?
- **Transactions:** Where is the transaction boundary and what happens on failure?
- **Security:** Where does authentication happen before my controller runs?
- **HTTP:** What happens when another service is slow or unavailable?
- **Kafka:** What happens if my database commits but publishing an event fails?
- **Kubernetes:** How does Kubernetes know whether my Spring application is healthy?
- **Spring AI:** How does an LLM safely interact with my application and its data?

# 28. Recommended Sequence

```text
FOUNDATION
[ ] 01. Create first Spring Boot project
[ ] 02. Project structure
[ ] 03. @SpringBootApplication
[ ] 04. First REST endpoint
[ ] 05. Request → Controller → Response
[ ] 06. Controller → Service
[ ] 07. Dependency Injection
[ ] 08. Beans / ApplicationContext
[ ] 09. Configuration / Profiles
[ ] 10. Boot Auto-configuration

APPLICATION
[ ] 11. DTOs
[ ] 12. Validation
[ ] 13. Exception handling
[ ] 14. Testing
[ ] 15. PostgreSQL
[ ] 16. JPA / Hibernate
[ ] 17. Persistence Context
[ ] 18. N+1 / Fetching
[ ] 19. Transactions

INTERNALS
[ ] 20. Bean lifecycle
[ ] 21. BeanPostProcessor
[ ] 22. AOP
[ ] 23. Proxies
[ ] 24. Custom annotations

SECURITY
[ ] 25. SecurityFilterChain
[ ] 26. Authentication / Authorization
[ ] 27. JWT
[ ] 28. OAuth 2.0
[ ] 29. OIDC

INTEGRATION / PRODUCTION
[ ] 30. RestClient
[ ] 31. WebClient
[ ] 32. HTTP Service Client
[ ] 33. @Async
[ ] 34. @Scheduled
[ ] 35. Actuator
[ ] 36. Observability
[ ] 37. Redis / Caching
[ ] 38. Docker

DISTRIBUTED SYSTEMS
[ ] 39. Monolith vs Microservices
[ ] 40. Service boundaries
[ ] 41. Resilience
[ ] 42. Idempotency
[ ] 43. Kafka
[ ] 44. Transactional Outbox
[ ] 45. Eventual consistency
[ ] 46. Kubernetes

AI
[ ] 47. Spring AI / ChatClient
[ ] 48. Structured output
[ ] 49. Tool calling
[ ] 50. RAG
[ ] 51. MCP
[ ] 52. Production AI concerns

FINAL
[ ] 53. Capstone architecture
[ ] 54. Deploy
[ ] 55. Architecture review
[ ] 56. Interview / senior-level review
```

# Guiding Principle

> **Don't learn Spring as a collection of annotations.**
>
> Learn the chain:

```text
Problem
   ↓
Why do we need this?
   ↓
Spring abstraction
   ↓
What happens underneath?
   ↓
Code
   ↓
Failure mode
   ↓
Production use
```

That is the path from **"I have used Spring"** to **"I understand Spring."**
