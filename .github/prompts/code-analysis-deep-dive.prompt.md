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
The output must be a **developer reference document** — someone reading it should
fully understand the code without ever opening the source file.

Work through these 9 layers systematically. Skip layers that don't apply to the scope.

### Cross-Layer Coherence — How the Layers Interlink

The 9 layers are **not independent sections** — they form a single connected narrative.
A developer must be able to trace any piece of data from Layer 2 (where it enters)
through Layer 4 (which block processes it) through Layer 5 (which line transforms it)
through Layer 6 (how it mutates state) and into Layer 7 (what happens when it's wrong).

**Consistent ID system** — tag items across layers so cross-references are unambiguous:

| Tag | Layer | Example | Purpose |
|---|---|---|---|
| **T*n*** | Layer 2 | T1, T2, T3 | Transformation step in the data pipeline |
| **C*n*** | Layer 3 | C1, C2, C3 | Call in the call stack / method flow |
| **B*n*** | Layer 4 | B1, B2, B3 | Code block in the block breakdown |
| **L*n*** | Layer 5 | L42, L47 | Source file line number |
| **E*n*** | Layer 7 | E1, E2, E3 | Edge case / error scenario |

**Every layer must cross-reference related items in other layers:**

- Layer 3 calls → which Layer 4 block contains them
- Layer 4 blocks → which Layer 2 transformation steps and Layer 3 calls they implement
- Layer 5 lines → which Layer 4 block they belong to
- Layer 6 state changes → which Layer 4 block causes each mutation
- Layer 7 edge cases → which Layer 4 block and source line they occur at
- Layer 8 dependencies → which Layer 4 blocks use them

This interlinking makes the document a **navigable reference** — a developer can jump
from a block to its data flow, from an edge case to the exact line that causes it,
from a dependency to every block that relies on it.

### Layer 1 — High-Level Overview (30-Second Understanding)

This section must give a developer the complete picture in under 30 seconds.

- **One-sentence purpose:** What does this code do? (answer in business terms, not code)
- **Responsibility boundary:** What this code is responsible for — and what it is NOT
- **Architecture role:** Which layer/module it belongs to (controller → service → repository → infrastructure)
- **Design pattern:** What pattern it implements (Service, Repository, Strategy, Factory, Template Method, Observer, etc.)
- **Entry points and triggers:** Who calls this code? What event/request/schedule triggers it?
- **Collaborators:** What other classes/services does it work with? (just names and roles)
- **One-paragraph narrative:** Write a natural-language paragraph explaining what happens when this code runs,
  as if describing it to a developer who just joined the team. Include the "why" — why does this code exist
  in the system, what business problem does it solve?

### Layer 2 — Data Flow (Input → Transformation → Output)

Trace data through the entire execution path:

**Inputs — What enters this code:**

| Parameter / Source | Type | Origin | Description |
|---|---|---|---|
| `paramName` | `Type` | caller / DI / config / DB | What this data represents in business terms |

**Transformation pipeline — What happens to the data:**

Number each transformation step. For each step, show the before-type, transformation
logic, and after-type. Use an ASCII flow diagram to make it visual:

```text
Input: Order(items, customer, discount)
  │
  ├─ T1: Validate → throws InvalidOrderException if items empty
  ├─ T2: Calculate subtotal → sum(item.price × item.qty) → BigDecimal
  ├─ T3: Apply discount → subtotal × (1 - discount.rate) → BigDecimal
  ├─ T4: Calculate tax → discounted × taxRate → BigDecimal
  ├─ T5: Build receipt → Receipt(subtotal, discount, tax, total)
  │
  Output: Receipt with all line items and totals
```

Tag each step **T1, T2, T3...** — these IDs are referenced by Layer 4 blocks to show
which transformation steps each block implements.

**Outputs — What leaves this code:**

| Return / Side-Effect | Type | Consumer | Description |
|---|---|---|---|
| Return value | `Type` | Caller class | What the caller uses this for |
| Side-effect | DB write / event / cache | Downstream system | What changes in the world |

### Layer 3 — Call Stack / Method Flow

Map the complete execution flow as an indented call tree:

```text
→ EntryPoint.publicMethod(args)
    C1 → this.validateInput(args)
         C2 → Validator.check(args)           // returns boolean
    C3 → this.processCore(validatedArgs)
         C4 → dependency.fetchData(id)        // 🔌 DB read — latency point
         C5 → this.transform(raw)             // ✦ pure logic — no side-effects
         C6 → dependency.save(result)         // 🔌 DB write
    C7 → this.notifyListeners(result)
         C8 → EventBus.publish(event)         // ⚡ async — fire-and-forget
```

Tag each call **C1, C2, C3...** — these IDs are referenced by Layer 4 blocks.

For each call in the tree, provide a detail row including which Layer 4 block contains it:

| Call | Caller → Callee | Purpose | Returns | Side-Effects | Block(s) | Notes |
|---|---|---|---|---|---|---|
| C1 | `Controller.handle` → `Service.process` | Entry from HTTP layer | `ResponseDTO` | None | B1 | Wraps in try-catch for HTTP errors |
| C4 | `Service.process` → `Repo.findById` | Fetch entity from DB | `Optional<Entity>` | DB read | B3 | Can return empty → 404 |

Annotate in the call tree: recursive calls (⟳), async boundaries (⚡), external I/O (🔌), pure logic (✦).

### Layer 4 — Code Block Breakdown (The Core of the Deep-Dive)

This is the **most valuable layer**. Split the code into **cohesive functional blocks**
based on what each section of code is doing logically. This is NOT about extracting
methods or proposing refactoring — it's about grouping lines that work together to
accomplish one logical step, so a developer can understand the code piece by piece.

**For each block:**

1. **Block name** — a descriptive, intention-revealing label (e.g., "Input Validation Guard",
   "Price Calculation Pipeline", "Error Recovery and Cleanup")
2. **Line range** — exact lines in the source file (e.g., L42-58)
3. **The actual code** — paste the real code in a fenced block (with language tag)
4. **What it does** — plain-English explanation of the block's purpose and mechanics.
   Explain the "what" and the "why" — not just restating the code in words
5. **How it connects** — what data this block receives from the previous block, and what
   it produces/passes to the next block. Show the data bridge between blocks
6. **Key decisions** — if the block contains conditionals, loops, or branching, explain
   the decision logic and what each branch means in business terms
7. **Gotchas** — any subtle behaviour, implicit assumptions, or non-obvious side-effects

**Block splitting rules:**

- Split on **logical boundaries**, not arbitrary line counts — each block should do
  exactly one conceptual thing (validate, transform, persist, notify, etc.)
- Aim for **3-8 blocks per method** — fewer for simple methods, more for complex ones
- A block can be 1 line (if it's a critical decision) or 20 lines (if they're cohesive)
- **Overlap is allowed** — if a line serves as both the end of one block and the
  beginning of another (e.g., a return value that's also a state change), include it
  in both blocks and annotate the dual role
- **Don't skip code** — every line of the method must appear in at least one block.
  The blocks together should reconstruct the full method
- **Name blocks by intent**, not by implementation — "Customer Eligibility Check" not
  "If-statement on line 42"

**Block template:**

```text
### Block N (BN) — <Intent-Revealing Name> (L42-58)

Implements: T2-T3 (Layer 2) · Contains: C4, C5 (Layer 3)
```

````java
// paste the actual source code for this block
````

```text
**What it does:** [plain-English explanation — what business/technical step this accomplishes]

**Data bridge:**
  ← Receives from BN-1 (<Previous Block Name>): [what data/state this block gets]
  → Produces for BN+1 (<Next Block Name>): [what data/state this block passes on]

**Key decisions:** [explain any branching, conditionals, or early returns]

**Gotchas:** [subtle behaviour, edge cases — reference E-numbers from Layer 7 if known]

**State impact:** [which Layer 6 variables are mutated in this block, if any]
```

The cross-references (**T***n*, **C***n*, **B***n*) create a web of links between layers.
A developer reading Block 3 can jump to T3 in Layer 2 to see where in the pipeline
this block sits, or to C4 in Layer 3 to see the call detail, or to E2 in Layer 7 to
see what edge case lurks here.

### Layer 5 — Line-by-Line Walkthrough (Key Logic Only)

For **decision-making lines, algorithm steps, and non-obvious behaviour** — skip
boilerplate (imports, standard getters/setters, simple assignments, logging statements).

For each key line:

| Line | Block | Code | What It Does | Why This Way | What If Different |
|---|---|---|---|---|---|
| L42 | B1 | `if (order.isValid())` | Guards against invalid orders | Delegates validation to Order — SRP | If removed: NPE on null items at L47 → E1 |
| L47 | B2 | `var total = items.stream().mapToDouble(...)` | Calculates subtotal via stream | Functional style — immutable intermediate | Could use for-loop but less readable |
| L51 | B3 | `total = applyDiscount(total, discount)` | Applies percentage discount | Mutates local — discount is multiplicative | If additive: different rounding behaviour → E3 |

The **Block** column links each line back to its Layer 4 block. The **What If Different**
column references Layer 7 edge case IDs (E*n*) where removing or changing the line
would trigger a failure.

Focus on lines where **the developer's understanding would break** if they skipped it.

### Layer 6 — State Changes

Track every mutation through execution:

**Local variable lifecycle:**

| Variable | Declared At | Mutated At | Block(s) | Before → After | Why |
|---|---|---|---|---|---|
| `total` | L45 | L47, L51, L55 | B2, B3, B4 | `0.0` → `100.0` → `90.0` → `99.0` | Accumulates: subtotal → discounted → taxed |

The **Block(s)** column shows which Layer 4 blocks are responsible for each mutation —
a developer can jump directly to the block to see the code that changes this variable.

**Instance/field state changes:**

| Field | Changed At | Block | Before → After | Scope of Impact |
|---|---|---|---|---|
| `this.lastProcessedId` | L60 | B5 | `null` → `"ORD-123"` | Affects subsequent calls — not thread-safe (see E4) |

**External state changes (side-effects leaving this code):**

| What Changes | Where | When | Block | Reversible? |
|---|---|---|---|---|
| DB row updated | `orders` table | L62 | B5 | Yes (within transaction) |
| Event published | Message queue | L65 | B6 | No — consumers may have already processed |

### Layer 7 — Edge Cases & Error Paths

Enumerate every way this code can fail or behave unexpectedly:

| Edge | Scenario | Location | Input / Condition | What Happens | Handled? | Impact |
|---|---|---|---|---|---|---|
| E1 | Null input | B1, L42 | `order == null` | NPE at L42 | ❌ No null-check | Caller gets 500 |
| E2 | Empty items list | B2, L47 | `order.getItems().isEmpty()` | Returns `0.0` total | ✅ Stream returns identity | Technically correct but may confuse caller |
| E3 | Negative discount | B3, L51 | `discount > 1.0` | Negative total | ❌ Not validated | Incorrect billing |
| E4 | Concurrent modification | B5, L60 | Two threads, same order | Race condition on `lastProcessedId` | ❌ Not synchronised | Data corruption |
| E5 | DB connection failure | B5, L62 | Network issue at L62 | `SQLException` propagates | ✅ Caught in caller | Transaction rolls back |

The **Edge** column (E*n*) is referenced from Layers 4, 5, and 6 — so a developer
spotting a gotcha in a block can jump here for the full scenario, and vice versa.
The **Location** column pinpoints the exact block and line where the edge case manifests.

### Layer 8 — Dependencies & Coupling

**Outgoing dependencies (what this code needs):**

| Dependency | Type | Interface or Concrete? | Coupling | Used in Blocks | Testability Impact |
|---|---|---|---|---|---|
| `OrderRepository` | Injected | Interface | Loose | B4, B5 | Easy to mock |
| `DiscountService` | Injected | Concrete class | Tight | B3 | Must mock concrete — fragile |
| `TaxCalculator` | Static call | Static method | Very tight | B4 | Cannot mock without PowerMock |

**Incoming dependencies (what needs this code):**

| Dependent | How It Uses This Code | Frequency | Breakage Risk |
|---|---|---|---|
| `OrderController` | Calls `processOrder()` | Per HTTP request | High — controller has no fallback |
| `BatchProcessor` | Calls in loop | Scheduled nightly | Medium — has retry logic |

**Coupling verdict:** How easy is it to change this code without breaking callers?
Rate as: isolated / manageable / tangled / dangerous.

### Layer 9 — Key Takeaways & Developer Cheat Sheet

Summarise everything for quick future reference:

**In 5 bullet points:**

- What this code does (one sentence)
- The most important design decision and why
- The biggest risk/edge case to watch for
- The key dependency to understand
- What to deep-dive next if you want to go deeper

**Developer cheat sheet** (copy-pasteable quick-reference):

```text
Purpose:     <one-liner from Layer 1>
Entry:       <who calls it, when — from Layer 1 entry points>
Happy path:  <T1 → T2 → T3 → ... → output — from Layer 2>
Error path:  <E1, E3 unhandled — from Layer 7>
Key blocks:  <B2 (Price Calc), B3 (Discount) — from Layer 4>
Thread-safe: yes / no / partially — <reference E4 if applicable>
Testable:    easy / moderate / hard — because <reference Layer 8 verdict>
```

The cheat sheet references Layer IDs so a developer can drill into any detail.

### Output Rules

- **Scope-adaptive:** For `method` scope, all 9 layers apply. For `class`, emphasize
  Layers 1-4 and 8 (show blocks per method, skip line-by-line). For `feature`,
  emphasize Layers 1-3 and show cross-class flow with a feature-level block breakdown
- **Code-first:** Always show actual source code in fenced blocks — never describe code
  without showing it. A developer should be able to read ONLY this document and
  reconstruct the mental model of the code completely
- **Type-precise:** Always include types in data flow and call stack tables
- **Honest:** If something is unclear, surprising, or looks like a bug, say so directly
- **No refactoring in the analysis** — the block breakdown shows logical groupings
  for understanding. If you see an extract-method opportunity, note it in Layer 9
  takeaways, but do NOT reorganise the code in the analysis itself
- **Completeness over brevity** — every line of the target code must appear in at least
  one block in Layer 4. No gaps. The blocks together reconstruct the full method
- If the target method is > 50 lines, Layer 4 (Code Block Breakdown) is mandatory
- If the target class has > 5 public methods, provide a Layer 4 breakdown for EACH
  significant method (skip trivial getters/setters/toString)
- **Cross-layer coherence is mandatory** — every block (B*n*) must reference the
  transformation steps (T*n*) it implements and the calls (C*n*) it contains. Every
  edge case (E*n*) must name its block and line. Every state change must name its block.
  A developer reading any single layer must be able to navigate to every related layer
  via the ID tags. If an ID appears in one layer, it must be defined in its home layer.
- End with one "what to deep-dive next" recommendation

### Session Capture — Auto-Save to Brain

After completing the deep-dive analysis, **automatically capture** the full output as
a session file. This is mandatory — every deep-dive produces a permanent reference doc.

#### Capture Workflow

```mermaid
flowchart TD
    A[Deep-dive analysis complete] --> B[1. Query system clock]
    B --> C[2. Classify domain]
    C --> D{Work or Personal?}
    D -->|Work| E[sessions/work/code-analysis/deep-dive/]
    D -->|Personal| F[sessions/personal/software-dev/code-review/deep-dive/]
    E --> G[3. Build filename]
    F --> G
    G --> H[4. Read template]
    H --> I[5. Populate frontmatter + all 9 layers]
    I --> J[6. Write file]
    J --> K{7. Escalation check}
    K -->|3+ same class prefix| L[Create class sub-package]
    K -->|< 3| M[Keep flat]
    L --> N[8. Append SESSION-LOG.md]
    M --> N
    N --> O[9. Append CAPTURE-LOG.md]
    O --> P[10. Report to user]
```

#### Step-by-Step Protocol

1. **Get the actual current timestamp** — always query the system clock first:

   ```powershell
   Get-Date -Format "yyyy-MM-dd"          # → 2026-04-20  (frontmatter date)
   Get-Date -Format "hh-mmtt"             # → 09-21pm     (filename time, lowercase am/pm)
   Get-Date -Format "hh:mm tt"            # → 09:21 PM    (frontmatter time, uppercase)
   ```

   Never guess or round — use the exact values returned.

2. **Determine the domain** from the code being analysed:
   - Code in this repo or any work project → `work`
   - Code in a personal/side project → `personal`

3. **Build the file path** — deep-dive sessions go to a **permanent `deep-dive/` sub-folder**
   (not subject to de-escalation):
   - Work: `brain/ai-brain/sessions/work/code-analysis/deep-dive/`
   - Personal: `brain/ai-brain/sessions/personal/software-dev/code-review/deep-dive/`
   - If a class sub-package already exists (e.g., `deep-dive/order-service/`), place the
     file inside it

4. **Build the filename** following the naming convention. Files inside `deep-dive/`
   carry rich descriptive metadata because the category is implied by the folder path:

   ```text
   # Naming formula for deep-dive/ (no category prefix — implied by folder)
   <date>_<time>_<subject-slug>.md

   Subject slug composition (order matters — most identifying first):
     <class-kebab>-<method-kebab>[-<focus>][-<context>]

   Segment reference:
     <class-kebab>   — mandatory: kebab-case class name (OrderService → order-service)
     <method-kebab>  — optional: kebab-case method name (calculateTotal → calculate-total)
                       omit for class-level, use "overview" instead
     <focus>         — optional: what aspect was emphasised (internals / flow / state)
                       omit when focus = all (the default)
     <context>       — optional: why the deep-dive was done (onboarding / pre-refactoring)
                       omit when context is general learning
   ```

   **Filename examples by scope:**

   | Scope | Target | Focus | Context | Filename |
   |---|---|---|---|---|
   | method | `OrderService.calculateTotal` | all | — | `2026-04-20_09-21pm_order-service-calculate-total.md` |
   | method | `PaymentGateway.charge` | flow | debugging | `2026-04-20_03-45pm_payment-gateway-charge-flow-debugging.md` |
   | class | `OrderService` | all | onboarding | `2026-04-20_11-00am_order-service-overview-onboarding.md` |
   | class | `ConfigLoader` | internals | — | `2026-04-20_02-30pm_config-loader-overview-internals.md` |
   | feature | checkout flow | flow | — | `2026-04-20_04-00pm_checkout-flow.md` |

   **Inside a class sub-package** (`deep-dive/order-service/`):

   | Target | Filename (no class prefix — implied by folder) |
   |---|---|
   | `OrderService.calculateTotal` | `2026-04-20_09-21pm_calculate-total.md` |
   | `OrderService.validateOrder` | `2026-04-20_03-45pm_validate-order-flow.md` |
   | `OrderService` class overview | `2026-04-20_11-00am_overview-onboarding.md` |

5. **Check for existing versions** — before writing, check if a file with the same
   class+method subject already exists in the target folder:
   - If found → create a versioned continuation: append `_v2`, `_v3`, etc.
   - Set `version: 2` and `parent: <original-filename>` in frontmatter

6. **Read and populate the template** from
   `brain/ai-brain/sessions/_templates/code-analysis-deep-dive-capture.md`:

   **Frontmatter** — fill every field:

   ```yaml
   date: 2026-04-20
   time: "09:21 PM"
   kind: session-capture
   domain: work
   category: code-analysis
   project: learning-assistant
   subject: order-service-calculate-total
   tags: [project:learning-assistant, deep-dive, code-analysis, java, order-service]
   status: draft
   version: 1
   parent: null
   complexity: high
   outcomes:
     - "Mapped data flow: price × quantity → discount → tax → total"
     - "Identified missing null-check on discount parameter"
   source: copilot
   scope: project
   scope-project: learning-assistant
   scope-feature: null
   scope-transitions: []
   scope-refs: []
   code-target:
     class: OrderService
     method: calculateTotal
     package: com.example.order
     file: src/order/OrderService.java
   deep-dive:
     level: method
     focus: all
   ```

   **Body** — populate ALL 9 layers from the deep-dive analysis output above.
   Every layer must contain real, substantive content — not placeholder text.
   Layer 4 (Code Block Breakdown) must reconstruct the full method across all blocks.

7. **Write the file** to the path from step 3.

8. **Check escalation** — count session files in the target folder:
   - If **3+ files** share the same class prefix (e.g., `order-service-*`), create a
     class sub-package per Pattern 3a in chat-capture instructions
   - Move matching files into `<class-kebab>/` and truncate their names
     (drop class prefix — implied by folder)
   - If **2 files** and a multi-part deep-dive is planned, apply early escalation

9. **Append to SESSION-LOG.md** — add a row to `brain/ai-brain/sessions/SESSION-LOG.md`:

   ```markdown
   | 2026-04-20 | 09:21 PM | work | code-analysis | order-service-calculate-total | v1 | high | draft | [View](work/code-analysis/deep-dive/2026-04-20_09-21pm_order-service-calculate-total.md) |
   ```

10. **Append to CAPTURE-LOG.md** — log the capture operation in
    `brain/ai-brain/sessions/CAPTURE-LOG.md` (create the file if it doesn't exist):

    ```markdown
    | 2026-04-20 | 09:21 PM | capture | Deep-dive: OrderService.calculateTotal (method, all) → work/code-analysis/deep-dive/ | 1 file created |
    ```

    If escalation was triggered, log that as a separate row:

    ```markdown
    | 2026-04-20 | 09:22 PM | escalation:pattern-3a | Created order-service/ sub-package in deep-dive/ (3+ class files) | N files moved |
    ```

11. **Report** — tell the user: "Deep-dive captured to `sessions/<path>`"

#### Content Quality Rules

- **Layer 4 (Code Block Breakdown)** must be thorough — split every non-trivial method
  into 3-8 functional blocks with actual code snippets and explanations. This is the
  most valuable section for a developer reading the file later.
- **Layer 1 (High-Level Overview)** must be immediately understandable — a developer
  should get the full picture in 30 seconds by reading just this section.
- **Layer 5 (Line-by-Line)** should cover key decision lines, not boilerplate.
- The file must be **self-contained** — a developer who has never seen this code should
  be able to understand it fully by reading only this file.
- Include actual code blocks (not just descriptions) in Layers 4 and 5.
