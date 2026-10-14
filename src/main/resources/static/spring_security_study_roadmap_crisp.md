# Spring Security Study Roadmap

**Spring Boot 4.1.1 · Java 21 · SpringLab**

## Phase 14 --- Spring Security Fundamentals

### Session 1: Security Architecture

-   Servlet filters and `SecurityFilterChain`
-   `DelegatingFilterProxy` and `FilterChainProxy`
-   Authentication vs. authorization
-   Default Spring Security behavior
-   HTTP 401, 403 and redirects

### Session 2: Request Security

-   `SecurityFilterChain` and `HttpSecurity`
-   `requestMatchers`
-   `permitAll`, `authenticated`, `denyAll`
-   Rule ordering and least privilege
-   HTTP Basic and form login

### Session 3: Authentication Internals

-   `Authentication` and `SecurityContext`
-   `SecurityContextHolder` and principal
-   `AuthenticationManager` and `ProviderManager`
-   `AuthenticationProvider`
-   `UserDetailsService`

### Session 4: Users and Passwords

-   Database-backed users with PostgreSQL
-   User and role model
-   `UserDetailsService` implementation
-   `PasswordEncoder` and `DelegatingPasswordEncoder`
-   BCrypt and password storage
-   Account lifecycle and credential errors

### Session 5: Authorization and Web Security

-   Roles vs. authorities
-   Request-level authorization
-   Method security: `@EnableMethodSecurity`, `@PreAuthorize`
-   CSRF and CORS
-   Session-based vs. stateless security
-   Security headers and access-denied handling

## Phase 15 --- JWT, OAuth 2.0 and OIDC

### Session 6: JWT Fundamentals

-   JWT structure and claims
-   Signature, expiry, issuer and audience
-   Access tokens and refresh tokens
-   Decoding vs. validating tokens

### Session 7: OAuth 2.0 Resource Server

-   Bearer-token authentication
-   JWT signature and claim validation
-   Issuer and audience validation
-   Scopes and authority mapping
-   Invalid, malformed and expired tokens

### Session 8: OAuth 2.0

-   Client, authorization server and resource server
-   Scopes and access tokens
-   Authorization Code flow
-   PKCE
-   Token lifecycle

### Session 9: OpenID Connect

-   OAuth 2.0 vs. OIDC
-   ID token vs. access token
-   Discovery metadata and signing keys
-   Issuer validation
-   External identity-provider integration

## Phase 16 --- Security Hardening and Industry Practices

### Session 10: Common Security Failures

-   Broken object-level authorization
-   Excessive permissions and role design
-   CSRF and CORS misconfiguration
-   Token leakage and credential handling
-   Secrets management and sensitive logging
-   Rate limiting and brute-force protection

### Session 11: Verification and Production Readiness

-   Security integration tests
-   Anonymous, authenticated and unauthorized paths
-   Security headers and HTTP responses
-   Dependency vulnerability scanning
-   HTTPS and deployment configuration
-   Audit logging and security review checklist

## Tools and Libraries

-   Spring Security
-   Spring Security OAuth2 Resource Server
-   Spring Security Test
-   Keycloak
-   PostgreSQL
-   Postman
-   OWASP ZAP
-   Testcontainers
-   Snyk
-   OWASP Cheat Sheet Series
-   Managed identity providers: Microsoft Entra ID, Auth0, Okta, Amazon
    Cognito

## Working Principles

-   Build → Observe → Understand → Break → Fix → Generalize
-   Least privilege and fail-closed authorization
-   Established protocols and maintained libraries
-   Validate tokens and protect individual resources
-   Choose CSRF protection based on credential model
-   Keep secrets out of source code and logs
-   Test both allowed and denied access
