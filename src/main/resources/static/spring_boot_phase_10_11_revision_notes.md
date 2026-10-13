# Spring Boot Revision Notes — Phase 10 & Phase 11

> **Purpose:** Crisp revision notes for JPA/Hibernate deep dive and Spring transactions. Examples use Spring Boot 4.x, Java 21, Jakarta Persistence, Spring Data JPA, and PostgreSQL.

## Table of Contents

- [Phase 10 — JPA/Hibernate Deep Dive](#phase-10--jpahibernate-deep-dive)
  - [1. Persistence Context and Entity States](#1-persistence-context-and-entity-states)
  - [2. First-Level Cache and Dirty Checking](#2-first-level-cache-and-dirty-checking)
  - [3. Flush vs. Commit](#3-flush-vs-commit)
  - [4. Lazy vs. Eager Loading and Proxies](#4-lazy-vs-eager-loading-and-proxies)
  - [5. N+1 Problem](#5-n1-problem)
  - [6. Fetch Join and EntityGraph](#6-fetch-join-and-entitygraph)
  - [7. Entity Queries vs. DTO Projections](#7-entity-queries-vs-dto-projections)
  - [8. Pagination](#8-pagination)
  - [9. Batch Operations and Performance](#9-batch-operations-and-performance)
- [Phase 11 — Transactions](#phase-11--transactions)
  - [10. ACID](#10-acid)
  - [11. Transaction Boundaries and `@Transactional`](#11-transaction-boundaries-and-transactional)
  - [12. Propagation: `REQUIRED` vs. `REQUIRES_NEW`](#12-propagation-required-vs-requires_new)
  - [13. Isolation](#13-isolation)
  - [14. Read-Only Transactions](#14-read-only-transactions)
  - [15. Rollback Rules](#15-rollback-rules)
  - [16. Transaction Manager, Proxies, and Self-Invocation](#16-transaction-manager-proxies-and-self-invocation)
  - [17. Order/Payment Rollback Experiment](#17-orderpayment-rollback-experiment)
- [Quick Revision Checklist](#quick-revision-checklist)

---

# Phase 10 — JPA/Hibernate Deep Dive

## 1. Persistence Context and Entity States

A **persistence context** tracks managed entities and their persistent state. It is commonly associated with a transaction in a Spring service.

### Four entity states

| State | Meaning | Typical transition/example |
|---|---|---|
| **Transient** | New Java object, not managed | `new Product(...)` |
| **Managed** | Associated with a persistence context | `persist()` or load within a context |
| **Detached** | Previously managed, no longer tracked by that context | `detach()`, `clear()`, context ends |
| **Removed** | Managed entity marked for deletion | `remove(managedEntity)` |

```java
Product p = new Product(null, "Keyboard", new BigDecimal("3499.00"));
// Transient

entityManager.persist(p);
// Managed

entityManager.detach(p);
// Detached
```

Key points:
- A generated ID alone does **not** mean an entity is managed.
- A detached object still exists in Java; its changes are not automatically tracked by the old context.
- `merge(detached)` copies state into a managed instance and returns that instance. The original object remains detached.
- `entityManager.contains(entity)` checks whether that instance is managed by the current persistence context.
- A removed entity is marked for deletion; SQL may execute at flush.

## 2. First-Level Cache and Dirty Checking

The persistence context is Hibernate's **first-level cache**. It is scoped to one persistence context, not shared globally across the application.

```java
@Transactional
public void example(Long id) {
    Product first = entityManager.find(Product.class, id);
    Product second = entityManager.find(Product.class, id);

    System.out.println(first == second); // normally true
}
```

Within one persistence context, Hibernate normally maintains one managed instance for a given entity type and primary key. Repeated `find()` calls may therefore avoid another SELECT.

**Dirty checking** detects changes to managed entities and synchronizes them during flush:

```java
@Transactional
public void changePrice(Long id) {
    Product p = productRepository.findById(id).orElseThrow();
    p.setPrice(new BigDecimal("5000.00"));
    // No save() required for an already-managed entity.
    // Hibernate can detect the change at flush.
}
```

Conditions and caveats:
- Use a writable transaction for this experiment.
- Detached entities are not automatically dirty-checked by the old persistence context.
- A JPQL query can still execute SQL even if matching entities are already in the first-level cache; entity identity and query-result caching are different concepts.

## 3. Flush vs. Commit

- **Flush:** synchronizes pending persistence-context changes with the database by issuing SQL as needed.
- **Commit:** completes the transaction successfully, making its changes durable under the database's transaction guarantees.
- **Rollback:** undoes uncommitted transactional changes.

```java
@Transactional
public void updatePrice(Long id) {
    Product p = productRepository.findById(id).orElseThrow();
    p.setPrice(new BigDecimal("5000.00"));

    entityManager.flush(); // SQL UPDATE can execute here

    // If the transaction later rolls back, the UPDATE is rolled back too.
}
```

Remember:
- `flush()` is **not** `commit()`.
- Hibernate may flush automatically before certain queries or when a transaction commits.
- SQL can execute before commit, while the transaction is still uncommitted.
- `GenerationType.IDENTITY` may cause an INSERT to execute early so Hibernate can obtain the generated ID.

## 4. Lazy vs. Eager Loading and Proxies

- **Lazy loading:** associated data is loaded when accessed, if needed.
- **Eager loading:** the association is required to be fetched as part of the entity's loading contract; the provider may choose the SQL strategy.
- **Proxy:** Hibernate may use a proxy to represent an association before its data is initialized.

Example:

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "customer_id", nullable = false)
private Customer customer;
```

Accessing `order.getCustomer().getName()` may trigger another SELECT.

### `LazyInitializationException`

It can occur when code accesses an uninitialized lazy association after the relevant persistence context has ended.

**Preferred API pattern:** fetch required data within the service transaction and return a DTO, rather than returning entities whose lazy associations the controller may access later.

Do not switch all relationships to `EAGER` as a blanket fix; that can load unnecessary data and still lead to inefficient queries.

## 5. N+1 Problem

N+1 occurs when:
1. One query loads a set of parent entities.
2. Accessing an association triggers additional queries for those entities.

Example: five orders belong to three distinct customers:
- One SELECT for orders.
- Three SELECTs for customers (assuming no cache hits and one load per distinct customer).
- Total: four SELECTs.

The actual count depends on distinct associations, persistence-context contents, caching and fetch strategy.

A typical trigger:

```java
List<PurchaseOrder> orders = orderRepository.findAll();

return orders.stream()
    .map(order -> order.getCustomer().getName())
    .toList();
```

Inspect generated SQL rather than guessing. N+1 is about query patterns and scaling, not merely a fixed number of queries.

## 6. Fetch Join and EntityGraph

### Fetch join

```java
@Query("""
    select o
    from PurchaseOrder o
    join fetch o.customer
    """)
List<PurchaseOrder> findAllWithCustomer();
```

A fetch join requests that the association be fetched as part of that query. For the lab, this reduced the SQL from one order query plus three customer queries to one joined query.

### EntityGraph

```java
@EntityGraph(attributePaths = "customer")
@Query("select o from PurchaseOrder o")
List<PurchaseOrder> findAllWithCustomerGraph();
```

Import:

```java
import org.springframework.data.jpa.repository.EntityGraph;
```

An EntityGraph declares the associations to fetch for an operation, separate from the query text. It is a fetch-plan instruction, **not a guarantee of exactly one SQL statement**; inspect SQL to verify behavior.

| Fetch join | EntityGraph |
|---|---|
| Specifies association in JPQL | Declares fetch requirements separately |
| Direct control over query join | Provider chooses how to satisfy fetch plan |
| Good for query-specific fetch logic | Useful for reusable/variable fetch plans |

## 7. Entity Queries vs. DTO Projections

Use **entity queries** when you need managed domain objects or intend to change them. Use **DTO projections** when a read use case needs only selected fields.

DTO record:

```java
public record OrderSummaryDto(
    Long orderId,
    BigDecimal total,
    String customerName
) {}
```

JPQL constructor projection:

```java
@Query("""
    select new com.example.springlab.dto.OrderSummaryDto(
        o.id, o.total, c.name
    )
    from PurchaseOrder o
    join o.customer c
    """)
List<OrderSummaryDto> findAllSummaries();
```

This selects only the required fields and constructs DTOs. It avoids hydrating full `PurchaseOrder` and `Customer` entities for this result.

| Entity result | DTO projection |
|---|---|
| Managed entity state | Data-only result |
| Useful for domain operations and updates | Useful for read-only APIs and reports |
| Associations can lazy-load | No entity association to lazy-load on the DTO itself |
| May load unnecessary columns | Can select only needed fields |

## 8. Pagination

Spring Data repositories support `Pageable` through `JpaRepository`:

```java
Page<Product> page = productRepository.findAll(
    PageRequest.of(0, 20, Sort.by("name").ascending())
);
```

Imports:

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
```

- Page indexes start at `0`.
- `Page` provides content and page metadata, including total-count information; obtaining the total can require a count query.
- `Slice` is useful when you need to know whether another slice exists but don't need a total count.
- Deep offset pagination can become expensive; consider keyset/scrolling approaches for suitable large datasets.
- Pagination with collection fetch joins needs care because joins multiply rows and can interfere with limiting parent entities.

## 9. Batch Operations and Performance

### JDBC batching

Batching groups JDBC statements to reduce round-trip overhead. It is not guaranteed merely because application code calls `save()` repeatedly.

Example configuration:

```properties
spring.jpa.properties.hibernate.jdbc.batch_size=50
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

Important:
- `GenerationType.IDENTITY` generally prevents Hibernate from batching inserts for entities using that identity strategy because each generated ID is needed from the insert.
- Actual batching depends on ID strategy, provider/driver behavior, and execution patterns.
- For large persistence operations, periodic `flush()` and `clear()` can limit the number of managed objects retained in memory. `clear()` detaches all entities in the current persistence context.

### Query-performance checklist

1. How many SQL statements execute?
2. How many rows and columns are returned?
3. Are appropriate indexes present?
4. Is the query loading unnecessary associations or columns?
5. Is N+1 occurring?
6. Would a fetch join, EntityGraph, DTO projection or batching help?
7. Inspect SQL and query plans; do not optimize query count alone.

---

# Phase 11 — Transactions

## 10. ACID

| Property | Meaning | Example |
|---|---|---|
| **Atomicity** | All-or-nothing | Order and Payment writes both commit or both roll back |
| **Consistency** | Preserve defined integrity rules | Foreign keys and constraints remain valid |
| **Isolation** | Controls interactions/visibility between concurrent transactions | Concurrent updates don't produce an invalid result |
| **Durability** | Committed data persists under database guarantees | Successfully committed order remains stored |

Spring coordinates transactions; PostgreSQL enforces database transaction behavior.

## 11. Transaction Boundaries and `@Transactional`

A transaction should generally cover one unit of business work.

```java
@Service
public class CheckoutService {

    @Transactional
    public void checkout() {
        // Save Order
        // Save Payment
        // Any failure should roll back both, if both writes
        // participate in this same transaction.
    }
}
```

By default, Spring Boot's JPA setup provides a transaction manager integrating with JPA and the database.

Important:
- Both writes must participate in the same transaction for atomic rollback.
- An external API call is not rolled back by a database transaction.
- Flush can send SQL before the transaction commits.
- `@Transactional` behavior in Spring's common proxy mode is applied when calls pass through the proxy.

## 12. Propagation: `REQUIRED` vs. `REQUIRES_NEW`

Propagation determines how a method participates when a transaction already exists.

| Mode | Meaning |
|---|---|
| `REQUIRED` | Join existing transaction, or create one if none exists. Default. |
| `REQUIRES_NEW` | Suspend existing transaction and run a separate transaction. |
| `MANDATORY` | Require an existing transaction. |
| `SUPPORTS` | Join one if present; otherwise run without one. |
| `NOT_SUPPORTED` | Suspend an existing transaction and run without one. |
| `NEVER` | Fail if a transaction exists. |
| `NESTED` | Use a nested transaction/savepoint if supported by the transaction manager. |

### Lab result: Order and Payment

Outer service:

```java
@Transactional
public void updateOrderAndFail(Long orderId) {
    PurchaseOrder order = orderRepository.findById(orderId)
        .orElseThrow();

    order.setTotal(new BigDecimal("888888.88"));
    entityManager.flush();

    paymentService.createPayment(
        orderId, new BigDecimal("888888.88")
    );

    throw new IllegalStateException("Simulated failure");
}
```

Inner service initially:

```java
@Transactional(propagation = Propagation.REQUIRED)
public void createPayment(Long orderId, BigDecimal amount) {
    PurchaseOrder order = orderRepository.findById(orderId)
        .orElseThrow();

    paymentRepository.saveAndFlush(new Payment(amount, order));
}
```

**With `REQUIRED`:**
- Inner method joins the outer transaction.
- The outer unchecked exception causes both the order update and payment insert to roll back.

Change the inner annotation to:

```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
```

**With `REQUIRES_NEW`:**
- Outer transaction is suspended while the inner transaction runs.
- Inner payment transaction commits independently if successful.
- Outer transaction resumes and rolls back its order update.
- The payment can remain committed while the order update is rolled back.

Lab prerequisites:
- Use an already committed order so the inner transaction can reference it through the foreign key.
- Call the inner method through a separate injected Spring bean so proxy interception applies.
- Verify rows in PostgreSQL after the request fails.
- Clean up test data after the experiment.

## 13. Isolation

Isolation controls what concurrent transactions can observe and which anomalies are possible.

Common levels:
- `READ UNCOMMITTED`
- `READ COMMITTED`
- `REPEATABLE READ`
- `SERIALIZABLE`

PostgreSQL's default is **READ COMMITTED**: each statement sees a snapshot of committed data as of that statement's start. A later query in the same transaction can see newer committed changes than an earlier query.

Higher isolation can prevent more anomalies but can reduce concurrency or require transaction retries. Exact behavior depends on the database implementation.

## 14. Read-Only Transactions

```java
@Transactional(readOnly = true)
public List<Product> findAll() {
    return productRepository.findAll();
}
```

`readOnly = true` communicates intent and may enable optimizations. It is **not a universal guarantee that writes are impossible**.

Use it for genuinely read-only service operations, and use a writable transaction when deliberately testing entity modifications and dirty checking.

## 15. Rollback Rules

Default Spring behavior:
- Roll back for unchecked exceptions (`RuntimeException`) and `Error`.
- Checked exceptions do not generally trigger rollback by default.

To roll back for checked exceptions too:

```java
@Transactional(rollbackFor = Exception.class)
public void performOperation() throws Exception {
    // Work
}
```

Caveats:
- If an exception is caught and swallowed, automatic rollback may not occur.
- A transaction may already be marked rollback-only.
- Rolling back a database transaction cannot undo an external side effect such as an already-completed payment-provider call.

## 16. Transaction Manager, Proxies, and Self-Invocation

### Transaction manager

Spring's transaction manager coordinates begin/join, commit and rollback. In this project, the JPA transaction infrastructure integrates Hibernate with PostgreSQL.

### Proxy flow

```text
Caller
  ↓
Spring transactional proxy
  ↓
Begin/join transaction
  ↓
Target service method
  ↓
Commit or rollback
```

### Self-invocation problem

```java
@Service
public class ExampleService {

    public void outer() {
        inner(); // Calls this.inner() directly
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void inner() {
        // Transactional advice is not independently applied
        // to this self-invocation in the usual proxy mode.
    }
}
```

In Spring's usual proxy-based transaction mode, the call from `outer()` to `inner()` does not pass through the proxy. Therefore, the `REQUIRES_NEW` annotation is not independently intercepted for that call.

**Practical fix:** move the transactional operation to a separate Spring-managed bean and call it through the injected bean reference, or establish the required transaction at the externally invoked method.

## 17. Order/Payment Rollback Experiment

The core experiment:
1. Begin one transaction.
2. Insert Order.
3. Insert Payment.
4. Flush both inserts.
5. Throw an unchecked exception.
6. Verify both rows are absent after rollback.

Example:

```java
@Transactional
public void createOrderAndPaymentThenFail(Long customerId) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow();

    PurchaseOrder order = new PurchaseOrder(
        new BigDecimal("777777.77"), customer
    );
    entityManager.persist(order);
    entityManager.flush();

    entityManager.persist(new Payment(
        new BigDecimal("777777.77"), order
    ));
    entityManager.flush();

    throw new IllegalStateException("Simulated failure");
}
```

Verification:

```sql
SELECT *
FROM purchase_orders
WHERE total = 777777.77;

SELECT *
FROM payments
WHERE amount = 777777.77;
```

Both should return zero rows after the transaction rolls back.

**What it proves:** SQL may execute during flush, but uncommitted changes remain subject to rollback until the transaction commits.

---

# Quick Revision Checklist

## Phase 10
- [ ] Explain transient, managed, detached and removed.
- [ ] Explain the persistence context and first-level cache.
- [ ] Explain dirty checking without another `save()`.
- [ ] Distinguish flush from commit.
- [ ] Explain lazy loading, proxies and `LazyInitializationException`.
- [ ] Identify N+1 from SQL logs.
- [ ] Choose between fetch join and EntityGraph.
- [ ] Choose entity queries vs. DTO projections.
- [ ] Explain pagination and count-query trade-offs.
- [ ] Explain JDBC batching and why `IDENTITY` can limit insert batching.
- [ ] Inspect SQL and query plans before optimizing.

## Phase 11
- [ ] Explain ACID.
- [ ] Define a transaction boundary around a business operation.
- [ ] Explain `REQUIRED` vs. `REQUIRES_NEW`.
- [ ] Describe PostgreSQL `READ COMMITTED`.
- [ ] Explain read-only transactions and rollback rules.
- [ ] Explain the transaction manager and Spring proxy.
- [ ] Identify the self-invocation problem.
- [ ] Demonstrate Order/Payment rollback and verify database state.

## One-minute mental model

- **Persistence context:** tracks managed entities.
- **Dirty checking:** detects changes to managed entities.
- **Flush:** sends pending SQL to the database.
- **Commit:** successfully completes the transaction.
- **Fetch plan:** determines which associated data is loaded.
- **DTO projection:** fetches only the fields a read use case needs.
- **Transaction propagation:** determines whether a method joins or creates a transaction.
- **Isolation:** controls concurrent transaction visibility and anomalies.
- **Spring proxy:** applies transactional behavior around intercepted method calls.
