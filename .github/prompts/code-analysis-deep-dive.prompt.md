---
name: code-analysis-deep-dive
description: 'Deep-dive into code internals — trace data flow, call stack, code blocks, line-by-line understanding of any class, method, or feature'
agent: ''
tools: ['codebase', 'fetch']
---

## Target

${input:target:What code do you want to deep-dive into? (e.g., OrderService.calculateTotal, PaymentGateway class, checkout flow)}

## Scope

${input:scope:What scope? (method — single method internals / class — full class analysis / feature — cross-class flow)}

## Focus (optional)

${input:focus:What to emphasize? (all — complete analysis / internals — how it works / flow — call chain and data flow / state — how state evolves / leave blank for all)}

## Context (optional)

${input:context:Why are you deep-diving? (onboarding / pre-refactoring / code-review-prep / learning / debugging-prep / leave blank)}

## Instructions

Perform a **code analysis deep-dive** on the target code. The goal is complete
understanding — not finding bugs or proposing refactoring (though note them if obvious).

Work through these 9 layers systematically. Skip layers that don't apply to the scope.

### Layer 1 — High-Level Overview

- **Purpose:** What does this code do? One-sentence responsibility statement
- **Architecture role:** Where it sits (layer, module, domain boundary)
- **Design pattern:** What pattern it implements (Service, Repository, Strategy, etc.)
- **Entry points:** Who calls this code and when?
- **One-paragraph summary** for someone who has never seen this code

### Layer 2 — Data Flow

Trace data from **inputs → transformations → outputs**:

- List all inputs (parameters, injected dependencies, global state read)
- Show each transformation step with types
- List all outputs (return values, side-effects, state mutations, events emitted)
- Draw a simple flow: `Input A → transform → intermediate → transform → Output B`

### Layer 3 — Call Stack / Method Flow

Map the method call chain:

- Show the complete call tree with indentation (who calls what)
- For each call: caller, callee, purpose, return type
- Highlight recursive calls, callbacks, or async boundaries
- Note any external service calls (DB, HTTP, file I/O)

### Layer 4 — Code Block Breakdown

Split the code into **functional blocks by cohesion**:

- Each block: name, line range, purpose, why it exists
- Show the actual code for each block
- Explain how blocks connect — what data flows between them
- Aim for 3-8 blocks per method (break on logical boundaries, not arbitrary line counts)

### Layer 5 — Line-by-Line Walkthrough

For **key logic lines** (skip boilerplate):

- What the line does
- Why it does it that way
- What would change if this line were different
- Flag any subtle behaviour (implicit conversions, short-circuit evaluation, side-effects)

### Layer 6 — State Changes

Track how state evolves through execution:

- Local variable mutations (before → after → why)
- Field/instance state changes
- External state changes (DB writes, cache invalidation, event publication)
- Thread-safety implications of state changes

### Layer 7 — Edge Cases & Error Paths

Enumerate what can go wrong:

- Null/empty inputs
- Boundary conditions (zero, max, overflow)
- Exception paths (what's caught, what propagates)
- Concurrency issues (race conditions, deadlocks)
- Missing validation

### Layer 8 — Dependencies & Coupling

Map the dependency graph:

- **Outgoing:** what this code depends on (interfaces vs concrete, loose vs tight)
- **Incoming:** what depends on this code (callers, consumers)
- Coupling assessment: is this code easy to change in isolation?

### Layer 9 — Key Takeaways

Summarise for future reference:

- 3-5 bullet points capturing the essential understanding
- Any surprises or non-obvious behaviour
- Suggestions for further investigation (related classes, upstream/downstream)

### Output Rules

- **Scope-adaptive:** For `method` scope, all 9 layers apply. For `class`, emphasize
  Layers 1-3 and 8. For `feature`, emphasize Layers 1-3 and show cross-class flow
- **Code-first:** Show actual code blocks, not just descriptions
- **Type-precise:** Always include types in data flow and call stack tables
- **Honest:** If something is unclear or looks like a bug, say so
- Use the `code-analysis-deep-dive-capture.md` session template for capture
- If the target method is > 50 lines, the Code Block Breakdown (Layer 4) is mandatory
- End with one "what to deep-dive next" recommendation
