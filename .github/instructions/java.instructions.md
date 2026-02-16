---
applyTo: "**/*.java"
---

# Java Coding Standards

## Naming Conventions
- **Classes:** `UpperCamelCase` — e.g., `CustomerService`, `OrderValidator`
- **Methods:** `lowerCamelCase` — e.g., `calculateTotal()`, `getCustomerName()`
- **Variables:** `lowerCamelCase`, descriptive — e.g., `totalPrice`, not `tp`
- **Constants:** `UPPER_SNAKE_CASE` — e.g., `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT`
- **Booleans:** prefix with `is`, `has`, `can`, or `should` — e.g., `isValid`, `hasPermission`

## Refactoring — Naming Guidance
When refactoring (extract variable / method / parameter / field / constant, rename, etc.):
- **Readable & concise:** names read naturally and carry no filler — `remainingAttempts` not `remAttempt` or `theRemainingNumberOfAttempts`
- **Context-aware:** leverage the enclosing class/method to avoid redundancy — inside `OrderService`, use `total` not `orderTotal`
- **Intent over implementation:** `activeUsers` not `filteredList`, `isExpired` not `checkDateFlag`
- **Extract Variable:** name the meaning, not the computation — `hasExceededLimit` not `attemptsGtMax`
- **Extract Method:** start with a specific verb — `calculateTax`, `sendWelcomeEmail`; avoid `process`, `handle`, `do`
- **Extract Parameter:** name the role — `retryDelayMillis`, `includeArchived`; never single-letter unless a loop counter
- **Extract Field:** name the state or dependency — `connectionPool`, `emailValidator`
- **Extract Constant:** name the business meaning — `MAX_LOGIN_ATTEMPTS` not `THREE`
- **Rename:** check all usage sites, keep names consistent with sibling methods/fields, and align with current purpose

## Code Structure
- One public class per file, class name matches filename
- Use `final` for variables that don't change
- Keep methods under 30 lines — extract helpers when longer
- Order: fields → constructors → public methods → private methods → toString/equals/hashCode

## Error Handling
- Catch specific exceptions, never `Exception` or `Throwable`
- Never leave catch blocks empty — at minimum, log the error
- Use try-with-resources for `Closeable`/`AutoCloseable` objects

## Java 21+ Features (Use When Appropriate)
- `var` for local variables when type is obvious
- Text blocks (`"""`) for multi-line strings
- Pattern matching for `instanceof`
- Records for immutable data classes
- Sealed classes/interfaces for restricted hierarchies
- Switch expressions with arrow syntax

## Documentation
- Add Javadoc on all public methods with `@param` and `@return`
- Use `@throws` to document checked exceptions
- Comments should explain WHY, not WHAT
