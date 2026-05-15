````skill
---
name: java-debugging
description: >
  Java debugging techniques, stack trace analysis, common exception patterns, and JVM troubleshooting.
  Use when debugging Java code, analyzing exceptions, reading stack traces, investigating
  NullPointerException, ConcurrentModificationException, ClassCastException, or performance issues.
---

# Java Debugging Skill

## Quick Diagnosis Checklist

When a Java error is reported, check these in order:

### 1. Read the Exception Type
| Exception | First Thing to Check |
|-----------|---------------------|
| `NullPointerException` | What variable is null? Trace where it should have been initialized |
| `ArrayIndexOutOfBoundsException` | Loop bounds — off-by-one? Array length vs. index? |
| `ClassCastException` | Raw types? Unchecked casts? Deserialization? |
| `ConcurrentModificationException` | Modifying collection during iteration? Multi-threaded access? |
| `StackOverflowError` | Infinite recursion — check base case of recursive method |
| `OutOfMemoryError` | Memory leak? Collection growing unbounded? Large file in memory? |
| `IllegalArgumentException` | Caller passing invalid value — check preconditions |
| `IllegalStateException` | Method called at wrong time — check object lifecycle |
| `UnsupportedOperationException` | Calling modify on `Collections.unmodifiableList()` or `List.of()`? |
| `NumberFormatException` | Parsing non-numeric string — validate input before parsing |

### 2. Read the Stack Trace (Top to Bottom)
```

Top frame    → WHERE the error happened (your code)
Middle frames → HOW execution got there (call chain)
"Caused by"  → WHY it happened (root cause — start here!)

```

### 3. Helpful JVM Flags for Debugging
```sh

# Show detailed NullPointerException messages (Java 14+)

java -XX:+ShowCodeDetailsInExceptionMessages -cp src Main

# Enable assertions

java -ea -cp src Main

# Verbose garbage collection (memory issues)

java -verbose:gc -cp src Main

# Remote debugging

java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -cp src Main

```

## Common Fix Patterns

### Null Safety
```java

// BAD — crashes if getCustomer() returns null
String name = getCustomer().getName();

// GOOD — use Optional
String name = getCustomer()
    .map(Customer::getName)
    .orElse("Unknown");

// GOOD — null check with early return
var customer = getCustomer();
if (customer == null) {
    throw new IllegalStateException("Customer not found for order: " + orderId);
}

// GOOD — Objects.requireNonNull in constructors
public OrderService(CustomerRepository repository) {
    this.repository = Objects.requireNonNull(repository, "repository must not be null");
}

```

### Collection Safety
```java

// BAD — ConcurrentModificationException
for (String item : items) {
    if (item.startsWith("X")) {
        items.remove(item);  // modifying during iteration!
    }
}

// GOOD — removeIf
items.removeIf(item -> item.startsWith("X"));

// GOOD — iterator
var iterator = items.iterator();
while (iterator.hasNext()) {
    if (iterator.next().startsWith("X")) {
        iterator.remove();
    }
}

```

### String Comparison
```java

// BAD — compares references, not values
if (status == "ACTIVE") { ... }

// GOOD — compares content
if ("ACTIVE".equals(status)) { ... }  // null-safe: constant on left

```

### Resource Management
```java

// BAD — resource leak if exception thrown
FileReader reader = new FileReader("data.txt");
String content = readAll(reader);
reader.close();

// GOOD — try-with-resources
try (var reader = new FileReader("data.txt")) {
    String content = readAll(reader);
}

```

## Debugging with Print Statements (When No Debugger Available)

```java

// Strategic placement — trace data flow
System.err.println("[DEBUG] Method entry: processOrder(" + orderId + ")");
System.err.println("[DEBUG] customer = " + customer);
System.err.println("[DEBUG] items.size() = " + items.size());
System.err.println("[DEBUG] Returning: " + result);

```

> Use `System.err` not `System.out` — err is unbuffered and prints immediately.
> Remove all debug prints before committing.

## VS Code Java Debugger Shortcuts

| Action | Shortcut |
|--------|----------|
| Toggle breakpoint | `F9` |
| Start debugging | `F5` |
| Step over | `F10` |
| Step into | `F11` |
| Step out | `Shift+F11` |
| Continue | `F5` |
| Stop | `Shift+F5` |
| Evaluate expression | Select code → right-click → Evaluate |

````
