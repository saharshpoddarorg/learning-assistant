```prompt
---
name: refactor
description: 'Suggest and apply refactorings to improve design, readability, and maintainability'
agent: designer
tools: ['codebase', 'usages', 'search', 'editFiles']
---

Analyze this file for refactoring opportunities:
${file}

## Instructions
For each refactoring:
1. **Name the refactoring** — use standard refactoring catalog names (Extract Method, Introduce Parameter Object, Replace Conditional with Polymorphism, etc.)
2. **What smells does it address?** — name the code smell or design smell
3. **Why does it matter?** — what principle does the current code violate?
4. **Before & After** — show the exact code transformation
5. **Risk** — what could go wrong? What needs retesting?

Prioritize refactorings by impact: highest value, lowest risk first.

## Naming Rules for Refactored Code
When suggesting or applying names for extracted variables, methods, parameters, fields, or constants:
- **Readable & concise** — names should read naturally without filler words
- **Context-aware** — consider the enclosing class, method, and domain to avoid redundancy
- **Intent over implementation** — name what a thing *represents*, not how it's computed
- **Extract Variable:** capture the business meaning — `hasExceededLimit` not `attemptsGtMax`
- **Extract Method:** lead with a specific verb — `calculateShippingCost`, not `doProcessing`
- **Extract Parameter:** name the role — `retryDelayMillis`, `includeArchived`
- **Extract Field:** name the state or dependency — `connectionPool`, `emailValidator`
- **Extract Constant:** name the business meaning — `MAX_LOGIN_ATTEMPTS`, never `THREE`
- **Rename:** verify readability at every call site, keep names consistent with neighboring code, and match the current purpose
```
