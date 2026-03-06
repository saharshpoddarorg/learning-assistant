````skill
---
name: design-patterns
description: >
  Reference for object-oriented design patterns, SOLID principles, GRASP, and clean architecture.
  Use when asked about design patterns, architecture decisions, class design, refactoring for better design,
  coupling, cohesion, abstraction, or when reviewing code structure.
---

# Design Patterns & Principles Reference

## When to Use Which Pattern — Decision Guide

### "I need to create objects without specifying the exact class"
→ **Factory Method** or **Abstract Factory**
```java

// Factory Method — let subclasses decide which class to instantiate
public abstract class NotificationFactory {
    public abstract Notification createNotification(String type);
}

// Abstract Factory — family of related objects
public interface UIFactory {
    Button createButton();
    TextField createTextField();
}

```

### "I need to construct a complex object step by step"
→ **Builder**
```java

var order = Order.builder()
    .customerId(customerId)
    .addItem(item1)
    .addItem(item2)
    .shippingAddress(address)
    .build();

```

### "I need to add behavior to an object without changing its class"
→ **Decorator**
```java

InputStream input = new BufferedInputStream(
    new GZIPInputStream(
        new FileInputStream("data.gz")));

```

### "I need to switch algorithms at runtime"
→ **Strategy**
```java

public interface PricingStrategy {
    BigDecimal calculatePrice(Order order);
}
// Swap strategies without changing the client

```

### "I need to notify multiple objects when something changes"
→ **Observer**
```java

public interface OrderEventListener {
    void onOrderPlaced(OrderEvent event);
}

```

### "I need to simplify a complex subsystem"
→ **Facade**
```java

// Instead of clients calling 5 different services...
public class OrderFacade {
    public OrderResult placeOrder(OrderRequest request) {
        // orchestrates inventory, payment, shipping, notification
    }
}

```

### "I need to handle a chain of operations"
→ **Chain of Responsibility**
```java

public abstract class ValidationHandler {
    private ValidationHandler next;
    public abstract ValidationResult validate(Request request);
}

```

### "I need to represent an operation to be performed later"
→ **Command**
```java

public interface Command {
    void execute();
    void undo();
}

```

## SOLID Quick Reference

| Principle | One-Line Rule | Violation Smell |
|-----------|--------------|-----------------|
| **SRP** | One reason to change | Class has methods serving different actors |
| **OCP** | Extend, don't modify | Adding a feature requires changing existing code |
| **LSP** | Subtypes are substitutable | Subclass throws `UnsupportedOperationException` |
| **ISP** | Small, focused interfaces | Client forced to implement unused methods |
| **DIP** | Depend on abstractions | `new ConcreteService()` inside business logic |

## Clean Architecture Layers (Dependency Direction: Inward)

```

┌───────────────────────────────────────────┐
│              Frameworks & Drivers          │  ← Outermost: DB, Web, UI
│  ┌───────────────────────────────────┐    │
│  │       Interface Adapters          │    │  ← Controllers, Gateways, Presenters
│  │  ┌───────────────────────────┐    │    │
│  │  │     Application Logic     │    │    │  ← Use Cases / Services
│  │  │  ┌───────────────────┐    │    │    │
│  │  │  │  Domain Entities   │    │    │    │  ← Innermost: Business rules
│  │  │  └───────────────────┘    │    │    │
│  │  └───────────────────────────┘    │    │
│  └───────────────────────────────────┘    │
└───────────────────────────────────────────┘

Rule: Dependencies ALWAYS point inward.
Inner layers NEVER know about outer layers.

```

## Java-Specific Design Guidance

### Records for Value Objects (Java 16+)
```java

// Immutable data — use records instead of mutable POJOs
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
    }
}

```

### Sealed Classes for Restricted Hierarchies (Java 17+)
```java

// When you want a closed set of subtypes
public sealed interface Shape permits Circle, Rectangle, Triangle {
    double area();
}

```

### Pattern Matching for Cleaner Code (Java 21+)
```java

// Instead of instanceof + cast chains
if (shape instanceof Circle c) {
    return Math.PI * c.radius() * c.radius();
}

```

## Common Design Smells → Pattern Solutions

| Smell | Symptom | Pattern Solution |
|-------|---------|-----------------|
| Long `if/else` or `switch` | Growing conditional chains | **Strategy** or **Polymorphism** |
| Constructor with 5+ params | Hard to read, easy to misorder | **Builder** |
| God class (500+ lines) | Does everything, changes constantly | Extract classes per **SRP** |
| Copy-paste code | Same logic in multiple places | **Template Method** or extract shared method |
| Tight coupling to framework | Business logic imports `javax.servlet` | **Dependency Inversion** + interfaces |
| Primitive obsession | `String email`, `int age` everywhere | Value Objects (**Records**) |

````
