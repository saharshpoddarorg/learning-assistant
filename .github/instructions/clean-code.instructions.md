```instructions
---
applyTo: "**/*.java"
name: 'Clean Code'
description: 'Clean code practices, code smells to avoid, and quality heuristics for Java files'
---

# Clean Code Practices

## Method Quality
- Methods do ONE thing — if you need the word "and" to describe it, it does too much
- Max 30 lines per method — extract helpers when longer
- Max 3 parameters — use a parameter object or builder if more
- No side effects — a method named `checkPassword` should NOT initialize a session
- Prefer early returns (guard clauses) over deep nesting

## Naming Heuristics
- Names should reveal intent: `elapsedTimeInDays` not `d`
- Avoid disinformation: don't use `list` suffix for things that aren't Lists
- Make meaningful distinctions: `source` and `destination`, not `a1` and `a2`
- Use pronounceable names: `generationTimestamp` not `genymdhms`
- Boolean names: `isValid`, `hasPermission`, `canExecute`, `shouldRetry`

## Refactoring — Naming & Renaming
When suggesting or applying names during any refactoring (extract variable, extract method, extract parameter, extract field, extract constant, rename, introduce parameter object, etc.), always follow these principles:

### General Principles
- **Readable first:** names should read like natural language — `remainingAttempts` not `remAttempt`
- **Concise but complete:** remove filler words, but never sacrifice clarity — `fetchUserProfile` not `doGetTheUserProfileData`
- **Context-aware:** consider the surrounding class, method, and domain — inside `OrderService`, prefer `total` over `orderTotal`; inside a generic utility, prefer `orderTotal`
- **Reveal intent, not implementation:** `activeCustomers` not `filteredList`; `isEligibleForDiscount` not `checkFlag`

### Extract Variable
- Name captures **what the value represents**, not how it's computed — `discountedPrice` not `priceMinusDiscount`
- For boolean expressions, phrase as a readable condition — `final var hasExceededLimit = attempts > MAX_ATTEMPTS;`
- For complex sub-expressions, use a name that summarizes the business meaning — `isPremiumMember` not `levelGreaterThan3`

### Extract Method
- Start with a **verb** describing the single action — `calculateShippingCost`, `validateEmailFormat`, `buildConfirmationMessage`
- Name should make the call site read like a sentence — `if (isEligibleForRefund(order))` not `if (checkOrder(order))`
- Avoid vague verbs: `process`, `handle`, `manage`, `do`, `perform` — be specific about **what** is processed

### Extract / Rename Parameter
- Name should describe the **role** of the value in that method — `retryDelayMillis` not `delay`, `maxResults` not `n`
- Use domain language matching the method's Javadoc and context
- For boolean parameters, phrase as a question or condition — `includeArchived`, `forceRefresh`

### Extract Field
- Name reflects the **state or dependency** the field represents — `emailNotifier` not `notifier` (if multiple notifiers exist), `connectionPool` not `pool`
- Prefix or suffix with the role when a class holds multiple instances of the same type — `sourceDatabase`, `targetDatabase`

### Extract Constant
- Name captures the **business meaning**, not the literal value — `MAX_LOGIN_ATTEMPTS` not `THREE`, `DEFAULT_PAGE_SIZE` not `TEN`
- Group related constants with a shared prefix — `TIMEOUT_CONNECTION_MILLIS`, `TIMEOUT_READ_MILLIS`

### Rename (General)
- When renaming, consider all usages and how the new name reads at each call site
- Align the name with the **current purpose** — if a method's responsibility evolved, update its name to match
- Keep names consistent with nearby code — if sibling methods use `find`, don't introduce `locate` or `fetch` for the same pattern

## Code Smells to Watch For
- **Long Method** → Extract smaller methods with descriptive names
- **Large Class** → Split by responsibility (SRP)
- **Feature Envy** → Method uses another class's data more than its own → move it
- **Data Clumps** → Same group of fields/params appear together → extract a class
- **Primitive Obsession** → Using `String email` everywhere → create an `Email` value object
- **Shotgun Surgery** → One change requires edits in many classes → consolidate
- **Divergent Change** → One class changes for multiple unrelated reasons → split it

## Error Handling
- Throw exceptions for exceptional conditions, not for flow control
- Catch specific exceptions: `FileNotFoundException`, not `Exception`
- Include context in error messages: what failed, what was expected, what was actual
- Never swallow exceptions with empty catch blocks
- Use try-with-resources for ALL AutoCloseable resources

## Comments
- Good: explain WHY a non-obvious decision was made
- Good: warn about consequences ("this is O(n²), don't use for large datasets")
- Bad: restating what the code does (`// increment i` before `i++`)
- Bad: commented-out code — delete it, version control remembers
```
