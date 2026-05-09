```prompt
---
name: explore-project
description: 'Learn by exploring an open-source project — understand its architecture, patterns, and coding practices (any language)'
agent: learning-mentor
tools: ['codebase', 'fetch', 'search']
---

## Project or Library
${input:project:Which open-source project or library? (e.g., Redis, Guava, Flask, JUnit 5, Nginx, Tokio, Spring Boot, Linux kernel, Kubernetes, SQLite)}

## What to Focus On
${input:focus:What aspect interests you? (e.g., architecture, design patterns, testing, API design, concurrency, data structures, protocol handling, a specific feature)}

## Instructions

Guide me through learning from this open-source project. Follow this structure:

### 1. Project Overview
- **What it does** — purpose in 2-3 sentences
- **Why it's worth studying** — what makes this project's code exemplary
- **Size & scope** — is this a small focused library or a large framework?
- **GitHub URL** and documentation links

### 2. Architecture Walkthrough
- **Package structure** — how is the code organized and why?
- **Key abstractions** — what are the main interfaces/classes and their roles?
- **Entry points** — where does execution start? How does a user interact with the API?
- **Dependency graph** — how do the major components relate to each other?

### 3. Design Patterns in Action
- Identify **specific design patterns** used in this project
- For each pattern:
  - Where exactly it's used (package, class)
  - Why they chose this pattern (what problem it solves here)
  - How it's implemented (simplified code walkthrough)
  - What would the code look like without this pattern (the pain)

### 4. Coding Practices to Learn From
- **Naming conventions** — how do they name classes, methods, variables, modules?
- **API design choices** — how do they make the library easy to use?
- **Error handling approach** — how do they handle failures?
- **Testing strategy** — how do they test? What testing patterns do they use?
- **Documentation style** — how do they write docs, comments, and README?
- **Build & CI** — how is the project built, tested, and released?

### 5. Code Reading Exercise
- Pick **one feature** and trace it end-to-end
- Walk through the execution path step by step
- Explain each class/method encountered and its role
- Highlight the concepts being applied at each step

### 6. What to Explore Next
- **Files to read first** — 3-5 specific files that teach the most
- **Tests to study** — which test classes best demonstrate usage?
- **PRs to review** — suggest looking at recent merged PRs to see evolution
- **Issues to explore** — any `good first issue` labels for hands-on learning?

### 7. Apply It
- **Concept extraction** — what general principles can you take from this project to your own code?
- **Mini-exercise** — try implementing a tiny version of one pattern you saw
- **Compare** — how does this project's approach compare to alternatives?

### Rules
- Always reference specific files/packages/classes by name so the learner can find them
- Explain the practical "why" behind architectural decisions, not just the "what"
- Keep explanations grounded — avoid abstract theory without tying it to the actual project code
- If the project has evolved its patterns over time, note the evolution and why
- Suggest starting with the smallest, most self-contained module first
```
