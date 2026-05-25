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

When a Java error is reported, check the exception type first:

| Exception | First Thing to Check |
|---|---|
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

## JVM Flags for Debugging

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

## VS Code Java Debugger Shortcuts

| Action | Shortcut |
|---|---|
| Toggle breakpoint | `F9` |
| Start debugging | `F5` |
| Step over | `F10` |
| Step into | `F11` |
| Step out | `Shift+F11` |
| Continue | `F5` |
| Stop | `Shift+F5` |
| Evaluate expression | Select code → right-click → Evaluate |

````
