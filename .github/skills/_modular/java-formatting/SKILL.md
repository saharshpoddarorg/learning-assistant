---
name: java-formatting
description: >
  Java code formatting and style rules specific to this project, aligned with the
  committed IntelliJ IDEA configs (`.idea/codeStyles/Project.xml` and
  `.idea/inspectionProfiles/Project_Default.xml`). Covers Allman braces, forced
  braces, 120-char margin, method-chain wrapping, stream/builder formatting,
  operator placement, Allman lambda bodies, Javadoc blank-line rules, member
  arrangement, and the project's enabled inspections. Activates ONLY when
  explicitly asked about Java formatting, code style, formatting review, or a
  formatting pass on Java code.
---

# Java Formatting & Style — Project Rules

> **Source of truth:** `.idea/codeStyles/Project.xml` and
> `.idea/inspectionProfiles/Project_Default.xml`. When those configs change,
> re-read them and update this skill.

---

## 1. Braces & Control Flow

### 1.1 Allman (Next-Line) Braces

Opening braces go on the **next line** for classes and methods (`BRACE_STYLE = 2`).

```java
public class OrderService
{
    public void processOrder(Order order)
    {
        // body
    }
}
```

### 1.2 `else`, `catch`, `finally` on Their Own Line

Not cuddled with the closing brace.

```java
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

### 1.3 Forced Braces

Braces are mandatory for `if`, `for`, `while`, and `do-while` — even single-line bodies (`*_BRACE_FORCE = 3`). Never write `if (x) return;`.

---

## 2. Line Length & Wrapping

### 2.1 Soft Margin: 120 Characters

### 2.2 Method Call Chain Wrapping

When chaining (streams, builders, fluent APIs, Optional, function composition),
**each chained `.method()` goes on its own line**, indented one level from the source
(`METHOD_CALL_CHAIN_WRAP = 2`).

**Short-chain exception:** ≤ 3 calls fitting in 120 chars may stay on one line.

```java
// Long chain — every call on its own line
var result = orders.stream()
    .filter(Order::isActive)
    .map(Order::getTotal)
    .reduce(BigDecimal.ZERO, BigDecimal::add);

// Short chain (≤ 3 calls, fits in margin) — one line OK
var first = items.stream().filter(Item::isActive).findFirst();
```

```java
// Wrong — partial wrap (group of 2 on first line)
var result = orders.stream().filter(Order::isActive)
    .map(Order::getTotal)
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```

The same rule applies to non-stream chains (HTTP clients, AssertJ, builders, function composition):

```java
var response = client.target(baseUrl)
    .path("api")
    .path("users")
    .queryParam("active", true)
    .request(MediaType.APPLICATION_JSON)
    .get();

assertThat(result)
    .isNotNull()
    .hasSize(3)
    .contains("alpha");
```

> **Rule of thumb:** 4+ calls or > 120 chars → every `.` on its own line.

### 2.3 Builder & Stream Formatting

Same as §2.2 — each operation on its own line, one indent from the source.

---

## 3. Operator Placement

### 3.1 `&&` / `||` at Start of Continuation Line

```java
// Correct
if (isValidUser
    && hasPermission
    && !isBlocked)
{
    grantAccess();
}
```

### 3.2 Ternary: Break Before `?` and `:`

```java
var status = isApproved
    ? "Confirmed"
    : "Pending";
```

---

## 4. Lambdas

Simple (single-expression) lambdas stay on one line. Multi-statement bodies use Allman braces:

```java
list.forEach(item -> process(item));

list.forEach(item ->
{
    validate(item);
    process(item);
});
```

---

## 5. Switch Expressions

No forced wrapping (`SWITCH_EXPRESSIONS_WRAP = 0`). Use Allman braces on the switch block:

```java
var label = switch (status)
{
    case ACTIVE -> "Active";
    case INACTIVE -> "Inactive";
    case PENDING -> "Pending Review";
};
```

---

## 6. Javadoc

Project-specific (from `JavaCodeStyleSettings`):

- **Blank line after the `@param` block**
- **Blank line after `@return`**
- Preserve manual line feeds
- Use Javadoc tags (`{@code}`, `{@link}`) instead of HTML (`<code>`, `<a>`)

```java
/**
 * Calculates the total price including tax.
 *
 * @param basePrice the pre-tax price
 * @param taxRate   the tax rate as a decimal (e.g., 0.10 for 10%)
 *
 * @return the final price after tax
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

No wildcard imports — `OnDemandImport` inspection is enabled. Always import each type explicitly.

---

## 8. `instanceof`

Use pattern matching: `if (obj instanceof String s)` — enforced by `REPLACE_INSTANCEOF_AND_CAST`.

---

## 9. Member Arrangement

Order within a class (from IntelliJ arrangement settings):

1. Static fields (constants first)
2. Instance fields
3. Constructors
4. Getters/setters — kept together as pairs
5. Public methods
6. Overridden methods — kept in declaration order
7. Dependent methods — breadth-first (caller before callee)
8. Private/helper methods
9. `toString()`, `equals()`, `hashCode()`

---

## 10. Project Inspections — Non-Obvious Rules

These are enabled in `.idea/inspectionProfiles/Project_Default.xml` and represent
choices Copilot cannot infer from generic Java knowledge.

### Complexity Thresholds

| Inspection | Threshold |
|---|---|
| `ClassComplexity` (cyclomatic per class) | ≤ 80 |
| `ClassCoupling` (coupled classes, excl. JDK & libs) | ≤ 15 |

### Project Conventions

| Inspection | Rule |
|---|---|
| `QuestionableName` | No `foo`, `bar`, `dummy`, `blah`, etc. |
| `ExceptionNameDoesntEndWithException` | Exception class names must end with `Exception` |
| `NonExceptionNameEndsWithException` | Non-exceptions must NOT end with `Exception` |
| `MagicNumber` | Extract numeric literals to named constants |
| `ConstantOnWrongSideOfComparison` | `"value".equals(var)` — constant on the left |
| `EqualsCalledOnEnumConstant` | Use `==` for enums, not `.equals()` |
| `FieldMayBeStatic` | Constant-valued fields must be `static` |
| `MapReplaceableByEnumMap` / `SetReplaceableByEnumSet` | Use `EnumMap`/`EnumSet` for enum keys/elements |
| `OptionalContainsCollection` | Don't wrap collections in `Optional` — return empty collection |
| `UseOfObsoleteDateTimeApi` | `java.time` only — no `Date`/`Calendar`/`SimpleDateFormat` |
| `Guava` | Prefer JDK equivalents over Guava (Java 8+) |
| `InterfaceMayBeAnnotatedFunctional` | Add `@FunctionalInterface` to SAM interfaces |
| `JUnit5Converter` | Migrate JUnit 4 patterns to JUnit 5 |
| `HtmlTagCanBeJavadocTag` | Javadoc tags over HTML in Javadoc |
| `MissingDeprecatedAnnotation` | `@Deprecated` annotation must accompany `@deprecated` Javadoc |
| `PackageDotHtmlMayBePackageInfo` | Use `package-info.java`, not `package.html` |

---

## Quick Reference

```text
BRACES        next-line (Allman) for classes, methods, control flow, switch, lambda bodies
              always required — no brace-less if/for/while
ELSE/CATCH    own line, not cuddled
LINE LENGTH   120 chars
CHAINS        each .method() on its own line; short chains (≤3, fits 120) may inline
OPERATORS     && || at START of continuation line
LAMBDAS       simple → one line; multi-statement → Allman braces
JAVADOC       blank line after @param block and after @return; {@code} not <code>
IMPORTS       no wildcards
INSTANCEOF    pattern matching (Java 16+)
ENUMS         EnumMap/EnumSet; compare with ==
DATE/TIME     java.time only
ARRANGEMENT   fields → ctor → getters/setters → public → overridden → dependent → private → toString/equals/hashCode
COMPLEXITY    class cyclomatic ≤ 80; coupling ≤ 15
```
