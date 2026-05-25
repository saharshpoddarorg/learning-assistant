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

## IDE Debugger Shortcuts

### VS Code

| Action | Windows/Linux | macOS |
|---|---|---|
| Toggle breakpoint | `F9` | `F9` |
| Start debugging | `F5` | `F5` |
| Step over | `F10` | `F10` |
| Step into | `F11` | `F11` |
| Step out | `Shift+F11` | `Shift+F11` |
| Continue | `F5` | `F5` |
| Stop | `Shift+F5` | `Shift+F5` |
| Evaluate expression | `Ctrl+Shift+D` → Watch | `Cmd+Shift+D` → Watch |
| Debug console | `Ctrl+Shift+Y` | `Cmd+Shift+Y` |
| Conditional breakpoint | Right-click breakpoint | Right-click breakpoint |

### IntelliJ IDEA / JetBrains

| Action | Windows/Linux | macOS |
|---|---|---|
| Toggle breakpoint | `Ctrl+F8` | `Cmd+F8` |
| Start debugging | `Shift+F9` | `Ctrl+D` |
| Step over | `F8` | `F8` |
| Step into | `F7` | `F7` |
| Step out | `Shift+F8` | `Shift+F8` |
| Resume (continue) | `F9` | `Cmd+Opt+R` |
| Stop | `Ctrl+F2` | `Cmd+F2` |
| Evaluate expression | `Alt+F8` | `Opt+F8` |
| View breakpoints | `Ctrl+Shift+F8` | `Cmd+Shift+F8` |
| Conditional breakpoint | Right-click breakpoint | Right-click breakpoint |
| Run to cursor | `Alt+F9` | `Opt+F9` |
| Mute all breakpoints | Actions menu | Actions menu |

````
