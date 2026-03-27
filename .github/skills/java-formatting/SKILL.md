---
name: java-formatting
description: >
  Java code formatting, style, and inspection rules aligned with this project's IntelliJ
  IDEA configuration (.idea/codeStyles/ and .idea/inspectionProfiles/). Covers brace
  placement, wrapping, indentation, stream pipeline formatting, operator placement,
  method chaining, control flow style, Javadoc formatting, and project-level inspections.
  Use ONLY when explicitly asked about Java formatting, code style, formatting review,
  inspection rules, or when the user requests a formatting pass on Java code.
---

# Java Formatting & Style — Project Rules

> **Source of truth:** `.idea/codeStyles/Project.xml` and
> `.idea/inspectionProfiles/Project_Default.xml` define the IDE-enforced rules.
> This skill documents the same rules for Copilot to follow when generating or
> reviewing Java code formatting.

> **Maintenance:** This skill is derived from the IntelliJ configs committed in
> `.idea/codeStyles/` and `.idea/inspectionProfiles/`. When those configs change,
> update this skill to match. Read the XML files to detect drift when invoked.

---

## 1. Braces & Control Flow

### 1.1 Brace Placement — Next-Line (Allman) Style

Opening braces go on the **next line** for classes and methods (IntelliJ `BRACE_STYLE = 2`).

```java
// Correct — next-line braces for class
public class OrderService
{
    // Correct — next-line braces for method
    public void processOrder(Order order)
    {
        // method body
    }
}
```

```java
// Wrong — K&R / same-line braces
public class OrderService {
    public void processOrder(Order order) {
    }
}
```

### 1.2 `else`, `catch`, `finally` on Their Own Line

Each keyword starts on a new line, not cuddled with the closing brace.

```java
// Correct
if (isValid)
{
    process();
}
else
{
    reject();
}

try
{
    connect();
}
catch (IOException e)
{
    log(e);
}
finally
{
    cleanup();
}
```

```java
// Wrong — cuddled else/catch/finally
if (isValid) {
    process();
} else {
    reject();
}
```

### 1.3 Forced Braces — Always Use Braces

Braces are mandatory for `if`, `for`, `while`, and `do-while` — even single-line bodies
(IntelliJ `*_BRACE_FORCE = 3`).

```java
// Correct
if (count > 0)
{
    return count;
}

// Wrong — no braces
if (count > 0)
    return count;
```

---

## 2. Line Length & Wrapping

### 2.1 Soft Margin

**120 characters** soft margin. Wrap lines that exceed this.

### 2.2 Wrap Long Lines

Long lines are wrapped automatically. Prefer breaking at logical boundaries
(after operators, before `.`, after commas).

### 2.3 Method Call Chain Wrapping

Method chains wrap with each call on its own line (IntelliJ `METHOD_CALL_CHAIN_WRAP = 2`).

```java
// Correct — each chained call on its own line
var result = orders.stream()
    .filter(order -> order.isActive())
    .map(Order::getTotal)
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```

```java
// Wrong — chained calls on same line
var result = orders.stream().filter(order -> order.isActive()).map(Order::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
```

### 2.4 Stream Pipeline Formatting

Each stream operation (`.filter()`, `.map()`, `.flatMap()`, `.collect()`, `.reduce()`,
`.forEach()`, `.sorted()`, `.distinct()`, `.peek()`) goes on its own line, indented
one level from the stream source.

```java
// Correct
var activeNames = users.stream()
    .filter(User::isActive)
    .map(User::getDisplayName)
    .sorted()
    .collect(Collectors.toList());

// Correct — Optional chains
var name = repository.findById(id)
    .map(User::getProfile)
    .map(Profile::getDisplayName)
    .orElse("Unknown");
```

### 2.5 Builder / Fluent API Chaining

Same rule as stream pipelines — each builder call on its own line.

```java
var config = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(10))
    .followRedirects(HttpClient.Redirect.NORMAL)
    .build();
```

---

## 3. Operator Placement

### 3.1 Logical Operators on Next Line

When a boolean expression wraps, `&&` and `||` go at the **start** of the next line,
not at the end of the previous line.

```java
// Correct — operator at start of continuation line
if (isValidUser
    && hasPermission
    && !isBlocked)
{
    grantAccess();
}

// Wrong — operator at end of line
if (isValidUser &&
    hasPermission &&
    !isBlocked)
{
    grantAccess();
}
```

### 3.2 Ternary Operator Wrapping

For long ternary expressions, break before `?` and `:`.

```java
var status = isApproved
    ? "Confirmed"
    : "Pending";
```

### 3.3 String Concatenation

When concatenating with `+`, place `+` at the start of the continuation line.
Prefer text blocks or `String.format()` / `formatted()` over concatenation.

---

## 4. Lambdas

### 4.1 Simple Lambdas on One Line

Keep simple lambdas (single expression) on one line (IntelliJ `KEEP_SIMPLE_LAMBDAS_IN_ONE_LINE`).

```java
// Correct — simple lambda, one line
list.forEach(item -> process(item));
names.sort((a, b) -> a.compareToIgnoreCase(b));

// Correct — multi-line lambda body gets braces
list.forEach(item ->
{
    validate(item);
    process(item);
});
```

---

## 5. Switch Expressions

### 5.1 No Wrapping for Switch Expressions

Switch expressions do not force wrapping (IntelliJ `SWITCH_EXPRESSIONS_WRAP = 0`).

```java
var label = switch (status)
{
    case ACTIVE -> "Active";
    case INACTIVE -> "Inactive";
    case PENDING -> "Pending Review";
};
```

---

## 6. Comments & Javadoc

### 6.1 Comment Formatting

- Comments are **not** kept in the first column (IntelliJ `KEEP_FIRST_COLUMN_COMMENT = false`)
- Comments follow the indentation of the surrounding code
- Long comments wrap at the soft margin

### 6.2 Javadoc Style

From IntelliJ `JavaCodeStyleSettings`:

- Blank line after `@param` block
- Blank line after `@return`
- Preserve manual line feeds in Javadoc
- Use `@pattern` Javadoc tags instead of bare `<code>` HTML where possible

```java
/**
 * Calculates the total price including tax and discounts.
 *
 * @param basePrice the pre-tax price
 * @param taxRate   the tax rate as a decimal (e.g., 0.10 for 10%)
 *
 * @return the final price after tax and discounts
 *
 * @throws IllegalArgumentException if basePrice is negative
 */
public BigDecimal calculateTotal(BigDecimal basePrice, BigDecimal taxRate)
{
    // ...
}
```

---

## 7. Imports

### 7.1 No Wildcard Imports

Never use on-demand (wildcard) imports (IntelliJ `OnDemandImport` inspection enabled).

```java
// Correct
import java.util.List;
import java.util.Map;

// Wrong
import java.util.*;
```

### 7.2 Delete Unused Module Imports

Unused module imports in `module-info.java` are auto-removed.

---

## 8. `instanceof` and Casting

Use pattern matching for `instanceof` (IntelliJ `REPLACE_INSTANCEOF_AND_CAST`).

```java
// Correct — pattern matching (Java 16+)
if (obj instanceof String text)
{
    return text.length();
}

// Wrong — separate cast
if (obj instanceof String)
{
    String text = (String) obj;
    return text.length();
}
```

---

## 9. Member Arrangement

From IntelliJ arrangement settings, member ordering within a class:

1. **Static fields** (constants first)
2. **Instance fields**
3. **Constructors**
4. **Getters/Setters** — kept together as pairs
5. **Public methods**
6. **Overridden methods** — kept in declaration order
7. **Dependent methods** — ordered breadth-first (caller before callee)
8. **Private/helper methods**
9. `toString()`, `equals()`, `hashCode()`

---

## 10. Project Inspections Reference

These inspections are enabled in `.idea/inspectionProfiles/Project_Default.xml`.
When writing or reviewing Java code, watch for these:

### Complexity & Coupling

| Inspection | Rule | Threshold |
|---|---|---|
| `ClassComplexity` | Total cyclomatic complexity per class | ≤ 80 |
| `ClassCoupling` | Number of coupled classes (excl. JDK & libs) | ≤ 15 |

### Naming

| Inspection | Rule |
|---|---|
| `QuestionableName` | Flag dummy/placeholder variable names (`foo`, `bar`, `dummy`, `blah`, etc.) |
| `ExceptionNameDoesntEndWithException` | Exception class names must end with `Exception` |
| `NonExceptionNameEndsWithException` | Non-exception class names must NOT end with `Exception` |

### Collections & Data Structures

| Inspection | Rule |
|---|---|
| `MapReplaceableByEnumMap` | Use `EnumMap` when key type is an enum |
| `SetReplaceableByEnumSet` | Use `EnumSet` when element type is an enum |
| `CollectionsFieldAccessReplaceableByMethodCall` | Use `Collections.unmodifiableList()` etc. instead of field access |
| `OptionalContainsCollection` | Don't wrap collections in `Optional` — return empty collection instead |

### Code Quality

| Inspection | Rule |
|---|---|
| `MagicNumber` | Extract numeric literals to named constants |
| `ConstantOnWrongSideOfComparison` | Put constant on the left: `"value".equals(var)` |
| `LiteralAsArgToStringEquals` | Same — literal should be the receiver of `.equals()` |
| `SimplifiableEqualsExpression` | Simplify redundant equals patterns |
| `NegatedConditionalExpression` | Avoid `!condition ? a : b` — flip the ternary |
| `NegatedEqualityExpression` | Avoid `!(a == b)` — use `a != b` |
| `EqualsCalledOnEnumConstant` | Use `==` for enum comparison, not `.equals()` |
| `UnnecessaryThis` | Remove `this.` when unambiguous |
| `UnnecessaryConstructor` | Remove no-arg constructor that does nothing |
| `FieldMayBeStatic` | Fields with constant values should be `static` |
| `ExtendsUtilityClass` | Don't extend utility classes |
| `InstanceofThis` | Avoid `instanceof` check against `this` |

### Modernization

| Inspection | Rule |
|---|---|
| `UseOfObsoleteDateTimeApi` | Use `java.time` instead of `Date`, `Calendar`, `SimpleDateFormat` |
| `Guava` | Prefer JDK equivalents over Guava when available (Java 8+) |
| `InterfaceMayBeAnnotatedFunctional` | Add `@FunctionalInterface` to single-abstract-method interfaces |
| `JUnit5Converter` | Migrate JUnit 4 patterns to JUnit 5 |
| `StaticPseudoFunctionalStyleMethod` | Prefer instance method references over static wrappers |

### Javadoc & Documentation

| Inspection | Rule |
|---|---|
| `HtmlTagCanBeJavadocTag` | Use Javadoc tags (`{@code}`, `{@link}`) instead of HTML (`<code>`, `<a>`) |
| `UnnecessaryInheritDoc` | Remove `{@inheritDoc}` when it adds no value |
| `UnnecessaryJavaDocLink` | Remove redundant `{@link}` to the containing class/method |
| `PackageDotHtmlMayBePackageInfo` | Use `package-info.java` instead of `package.html` |
| `MissingDeprecatedAnnotation` | `@Deprecated` annotation must accompany `@deprecated` Javadoc tag |

---

## Quick Reference Card

```text
BRACES        .  next-line (Allman) for classes and methods
              .  always required — even for single-line if/for/while
ELSE/CATCH    .  own line, not cuddled with }
LINE LENGTH   .  120 chars soft margin
CHAINS        .  each .method() on its own line (streams, builders, Optional)
OPERATORS     .  && || on START of next line, not end of previous
LAMBDAS       .  simple → one line; multi-statement → braces
SWITCH EXPR   .  no forced wrapping
IMPORTS       .  no wildcards; delete unused
INSTANCEOF    .  use pattern matching (Java 16+)
MAGIC NUMBERS .  extract to named constants
ENUMS         .  EnumMap/EnumSet instead of HashMap/HashSet; compare with ==
DATE/TIME     .  java.time only — no Date/Calendar/SimpleDateFormat
JAVADOC       .  {@code} not <code>; blank after @param/@return blocks
ARRANGEMENT   .  fields → constructors → public → overridden → dependent → private → toString/equals/hashCode
```
