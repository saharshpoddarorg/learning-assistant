---
name: code-analysis-deep-dive
description: 'Deep-dive into code internals — trace data flow, call stack, code blocks, line-by-line understanding of any class, method, or feature'
agent: ''
tools: ['codebase', 'fetch', 'editFiles', 'runCommands']
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

Perform a **code analysis deep-dive** on the target code. The output is a **virtual
refactoring** — you decompose the code into extracted methods on paper, creating a
method tree that a developer reads like refactored code. No actual code changes are made.

The approach: **think like a developer doing extract-method refactoring**, but stop
at the thinking stage. The extracted methods exist only in the analysis document. Each
extracted method has a real Java signature, the actual source code verbatim, and inline
annotations for anything non-obvious. A developer reads the analysis top-down:

1. **Quick Scan** — what does this code do? (30 seconds)
2. **Refactored View** — the method rewritten as virtual extracted calls (the structure)
3. **Method Extraction Tree** — each extracted method in detail (the substance)
4. **Context & Cheat Sheet** — dependencies, coupling, error map, debugging quick-start

> **This is a developer's annotated walkthrough, not an academic report.** The reader
> is a developer who reads Java fluently. The virtual method signatures and code are the
> documentation — prose is only for gotchas and things you cannot see in the source.

### How to Read This Analysis — Navigation Guide

The output is designed for **non-linear reading**. A developer should NOT read start
to finish like a document. Instead, use the phase that matches their current need:

| I need to... | Start here | Then drill into |
|---|---|---|
| **Understand what this code does** (30 sec) | Quick Scan | — (done) |
| **See the overall structure** | Refactored View | Any B-ref that's unclear |
| **Understand a specific section** | Method Tree → B*n* | Nested B*n.a*, B*n.b* if complex |
| **Trace data through the code** | Refactored View variable chain | `← Receives` / `→ Produces` in each B*n* |
| **Find all failure modes** | Error & Exception Map (§4a) | E-refs in Method Tree for details |
| **Debug a specific issue** | Cheat Sheet → Debugging Quick-Start | 🛑 Breakpoint lines in Method Tree |
| **Assess change impact** | Dependencies (§4) + State annotations | `mutated` tags in Behaviour blocks |
| **Review before code review** | Quick Scan → Refactored View → Error Map | Method Tree only for flagged blocks |
| **Onboard to unfamiliar code** | Quick Scan → Refactored View → read every B*n* | Design Rationale for the "why" |

**Visual navigation:**

```text
Quick Scan ─────────────────────────────────────────── 30 seconds
     │        (what, why, where, error summary)
     ▼
Refactored View ─── structure + pipeline ──────────── 2 minutes
     │              (B-refs are links to method details)
     ▼
Method Tree ──────── each Bn in depth ─────────────── 5-20 minutes
     │              (read only the B-refs you need)
     ▼
Error Map ────────── all failure modes consolidated ── 2 minutes
     │              (every E-ref from Method Tree in one table)
     ▼
Design Rationale ── why this pattern was chosen ───── 1 minute
     │              (trade-offs, constraints, evolution risk)
     ▼
Dependencies ─────── coupling + testability ────────── 2 minutes
     │
     ▼
Cheat Sheet ──────── debugging + change impact ─────── 1 minute
```

### ID System

Tag items for cross-referencing across sections:

| Tag | Section | Example | Purpose |
|---|---|---|---|
| **B*n*** | Method Tree | B1, B2, B3 | Extracted method / code block |
| **L*n*** | Method Tree | L42, L47 | Source file line number |
| **E*n*** | Method Tree | E1, E2, E3 | Edge case / error scenario |

### 1 — Quick Scan (30-Second Understanding)

Give a developer the complete picture in under 30 seconds. A senior engineer would
explain this at the whiteboard before ever looking at code:

```text
Purpose:      <one sentence — what does this code do in business terms?>
Why it exists: <what business problem does this solve? what breaks without it?>
Responsibility: <what it IS responsible for — and what it is NOT>
Architecture:  <which layer: controller → service → repository → infra>
Neighbourhood: <upstream callers → THIS → downstream dependencies>
Pattern:      <Service, Strategy, Factory, Template Method, etc.>
Entry:        <who calls it, what triggers it>
Happy path:   <input → step → step → ... → output (one line)>
Key state:    <which fields/variables change during execution>
Error summary: <how many error paths, handled vs. unhandled — see §4a for full map>
Side-effects: <what changes outside this code — DB / queue / cache>
Danger:       <biggest unhandled edge case — E-ref>
```

**`Why it exists`** — A developer reading code for the first time needs to understand
the business motivation BEFORE the mechanics. This is what a senior explains first:
"This code exists because customers can have mixed premium/standard items in one order,
and pricing rules differ per item type." Without the WHY, the developer sees WHAT the
code does but can't judge whether it does the right thing.

**`Neighbourhood`** — Show the architectural context around this code. What calls it,
what it calls, and how it fits in the request/response flow:

```text
Neighbourhood: OrderController.handlePost()
                   → THIS: OrderService.processOrder()
                       → OrderValidator.validate()    (injected)
                       → PriceCalculator.compute()    (injected)
                       → OrderRepository.save()       (injected)
                       → EventBus.publish()           (injected)
```

For multi-caller methods (called from 3+ distinct contexts), add a caller table:

| Caller | Context | Expected Behaviour | Error Handling |
|---|---|---|---|
| `OrderController` | HTTP — user-facing | Fast, throw on invalid | 400/500 mapped |
| `BatchProcessor` | Nightly batch — system | Tolerant, log & skip | Logged, continues |
| `EventHandler` | Async event — internal | Fire-and-forget | Silently retried |

#### Class-Scope Quick Scan Additions

When scope is `class`, add these fields to the Quick Scan:

```text
Methods:      <public method count / total method count — e.g., "6 public / 12 total">
Responsibilities: <1-3 named responsibilities this class handles>
Cohesion:     <high (one responsibility) / medium (2-3 related) / low (God class — see §3)>
```

**State model** — for class scope, show every field, its type, lifecycle, and which
methods touch it:

```text
State Model:
  OrderValidator validator  (injected, immutable after construction)
  OrderRepository repo      (injected, immutable after construction)
  String lastOrderId        (field, mutable — written by processOrder(), read by getLastOrder())
  Map<String, PriceTier> cache (field, mutable — populated lazily, never cleared ← memory leak risk)
```

**Method inventory** — for class scope, list every method with its role:

| Method | Visibility | Lines | Role | Complexity |
|---|---|---|---|---|
| `processOrder(Order)` | public | L30-110 | Orchestrator — main business flow | High — decompose |
| `calculateSubtotal(List)` | private | L111-130 | Pure computation — stream reduce | Low |
| `applyDiscount(double)` | private | L131-155 | Business rules — branching | Medium |
| `getLastOrderId()` | public | L156-158 | Getter — trivial | Skip |

**Complexity legend:** `High — decompose` = gets full Method Extraction Tree.
`Medium` = gets Behaviour block but no nested extraction. `Low` / `Skip` = mentioned
in inventory, not analysed further (developer can read these directly).

#### Feature-Scope Quick Scan Additions

When scope is `feature`, add these fields to the Quick Scan:

```text
Boundary:     <where the feature starts and ends — entry point → final side-effect>
Classes:      <N classes involved — list each with its role>
Handoff points: <where data passes from one class to another>
```

**Class inventory for feature scope:**

| Class | Role in Feature | Entry Method | Key Data Passed |
|---|---|---|---|
| `OrderController` | Entry point — HTTP → domain | `handlePost(OrderRequest)` | `OrderRequest → Order` |
| `OrderService` | Orchestrator — validation + pricing + persistence | `processOrder(Order)` | `Order → Receipt` |
| `PriceCalculator` | Pure computation — pricing rules | `compute(List<LineItem>)` | `List<LineItem> → double` |
| `OrderRepository` | Persistence — DB write | `save(Receipt)` | `Receipt → void (side-effect)` |

### 2 — Refactored View (The Method as Virtual Extracted Calls)

This is the **single most important artefact** in the deep-dive. Show the original
method body rewritten as if every logical section had been extracted into a well-named
method. The developer reads this and understands the entire structure in under a minute.

**Rules:**

- Rewrite the method body using the virtual method names — one line per extracted call
- Each line has the B-ref, virtual call, and the source line range in a comment
- Data flows visibly through variable names between calls
- The refactored view MUST be compilable-looking Java (not pseudocode)
- Keep the original method's signature and control flow structure intact

**Example:**

```java
public Receipt processOrder(Order order) {
    Order validated   = validateAndGuardInputs(order);                  // B1: L30-45
    double subtotal   = calculateSubtotal(validated.getItems());        // B2: L46-58
    double discounted = applyDiscountRules(subtotal, order.getDiscount()); // B3: L59-68
    double taxed      = calculateTax(discounted, getTaxRate());         // B4: L69-78
    Receipt receipt   = buildReceipt(validated, subtotal, discounted, taxed); // B5: L79-95
    persistAndNotify(receipt);                                          // B6: L96-110
    return receipt;                                                     // L111
}
```

**What this gives the developer:**

- The **pipeline** is visible — data flows left to right through variable names
- Each virtual method name is an **intent-revealing summary** of what that code section does
- The **B-refs and line ranges** let them jump to the detailed breakdown
- The **structure** is immediately clear — validate → calculate → discount → tax → persist
- They know exactly which "method" to drill into for more detail

**Handling branches and loops** — when the method has if-else or loops, keep them in
the refactored view but extract the branch bodies:

```java
public double calculatePrice(Order order) {
    validateOrder(order);                                               // B1: L30-38
    double subtotal = calculateSubtotal(order.getItems());              // B2: L39-52
    double discount;
    if (order.isPremium()) {
        discount = applyPremiumDiscount(subtotal);                      // B3: L54-62
    } else if (order.getTotal() > 1000) {
        discount = applyBulkDiscount(subtotal);                         // B4: L63-70
    } else {
        discount = applyStandardDiscount(subtotal);                     // B5: L71-76
    }
    return applyTaxAndFinalize(discount, order.getTaxRate());           // B6: L77-85
}
```

**Handling nested complexity** — when an extracted method is itself complex (15+ lines,
nested logic), show it as its own refactored view in the Method Extraction Tree:

```java
// B3 is itself complex — its refactored view:
private double applyPremiumDiscount(double subtotal) {
    double loyaltyBonus = lookupLoyaltyBonus(customer);                 // B3a: L54-56
    double baseDiscount = calculateBaseDiscount(subtotal);              // B3b: L57-59
    return combineDiscounts(baseDiscount, loyaltyBonus);                // B3c: L60-62
}
```

This recursive extraction is key: the developer drills into B3 and sees it decomposed
into B3a, B3b, B3c — just like a real codebase where methods call other methods.

#### Class-Scope Refactored View

For **class scope**, show a refactored view per public method. Additionally, show
a **class-level orchestration view** — how the public methods relate to each other
and to the shared state:

```java
// === CLASS-LEVEL ORCHESTRATION VIEW ===
// OrderService — 6 public, 12 total — orchestrates order processing

// Primary flow — called per HTTP request:
public Receipt processOrder(Order order) {                              // orchestrator
    Order validated   = validateAndGuardInputs(order);                  // B1: uses validator (injected)
    double subtotal   = calculateSubtotal(validated.getItems());        // B2: pure computation
    double discounted = applyDiscountRules(subtotal, validated);        // B3: uses discountService (injected)
    return buildAndPersist(validated, subtotal, discounted);            // B4: uses repo (injected), mutates lastOrderId
}

// Secondary flows — called independently:
public Order getOrder(String id) { ... }                                // read-only, uses repo
public void cancelOrder(String id) { ... }                              // mutates order status, uses repo + eventBus
public String getLastOrderId() { return this.lastOrderId; }             // field read — depends on processOrder having run

// Shared state: lastOrderId written by processOrder(), read by getLastOrderId()
// Shared dependency: repo used by processOrder(), getOrder(), cancelOrder()
```

**Why this matters:** At class scope, the developer needs to see the **relationships
between methods** — which share state, which share dependencies, which can be called
independently vs. which assume prior method calls. A method-scope view hides this.

#### Feature-Scope Refactored View

For **feature scope**, show the **cross-class flow** as a sequence diagram in code form.
Data handoff points between classes are the critical insight:

```java
// === FEATURE FLOW: Order Checkout (4 classes, 7 handoff points) ===

// 1. Entry — HTTP boundary
OrderController.handlePost(HttpRequest request) {
    OrderRequest dto = parseAndValidate(request);                       // C1-B1: HTTP → domain
    Order order = OrderMapper.toDomain(dto);                            // C1-B2: DTO → entity
    Receipt receipt = orderService.processOrder(order);                 // ──handoff──→ C2
    return ResponseEntity.ok(ReceiptMapper.toDto(receipt));             // C1-B3: entity → HTTP
}

// 2. Orchestration — business logic
OrderService.processOrder(Order order) {
    validate(order);                                                    // C2-B1: self
    double subtotal = priceCalculator.compute(order.getItems());        // ──handoff──→ C3
    double discounted = discountEngine.apply(subtotal, order);          // ──handoff──→ (not shown)
    Receipt receipt = Receipt.build(order, subtotal, discounted);       // C2-B2: assembly
    orderRepository.save(receipt);                                      // ──handoff──→ C4
    eventBus.publish(new OrderCompleted(receipt.getId()));               // ──handoff──→ (async)
    return receipt;                                                     // ──handoff──→ C1
}

// 3. Pure computation — no side-effects
PriceCalculator.compute(List<LineItem> items) {
    return items.stream().mapToDouble(i -> i.getPrice() * i.getQty()).sum(); // C3-B1: pure
}

// 4. Persistence — side-effect boundary
OrderRepository.save(Receipt receipt) {
    jdbcTemplate.update(INSERT_SQL, receipt.toParams());                // C4-B1: DB write
}
```

**Key feature-scope additions:**

- `──handoff──→` markers show where data crosses class boundaries
- Each class is labelled with its role (entry, orchestration, computation, persistence)
- C*n*-B*n* IDs uniquely identify blocks across classes (C1-B1 = Controller block 1)
- The developer can trace an HTTP request from entry to DB write in one view

### 3 — Method Extraction Tree (The Core of the Deep-Dive)

This is the **bulk of the analysis** and the section the developer will use side-by-side
with the source code. For each virtual method from the Refactored View, show the real
code with inline annotations. Then recurse into sub-methods when a block is complex.

> **Think like you're building a call tree of extracted methods.** The Refactored View
> (Section 2) is the root. Each extracted method in Section 3 is a node. When a node is
> itself complex, it gets its own child nodes (sub-methods). The developer navigates this
> tree the same way they'd navigate a well-structured codebase: start at the top-level
> method, then drill into the methods it calls.

#### Scope-Adaptive Method Tree Strategy

The Method Tree depth depends on the analysis scope:

**Method scope** (default): Full extraction tree for the target method. Every line
appears in at least one B-ref block.

**Class scope**: Not every method gets a full extraction tree. Use the method inventory
from Quick Scan to decide:

| Method Complexity | Treatment |
|---|---|
| High — decompose | Full Method Extraction Tree (same as method scope) |
| Medium | Behaviour block only — Data structures, Operations, Algorithm summary, no sub-method extraction |
| Low / Skip | One-line purpose annotation — e.g., "getter for `lastOrderId` (L156)" |

**Reading order for class scope:**

1. Start with the class-level Refactored View (§2) — the big picture
2. Read each `High — decompose` method's full tree (§3) in call order
3. Reference `Medium` methods' Behaviour blocks when called from a High method
4. Skip trivial methods — their purpose is clear from naming

**Inter-method state flow for class scope** — after the per-method trees, add a summary
showing how state flows between methods:

```text
State flow across methods:
  processOrder() writes → this.lastOrderId (field, mutated)
  getLastOrderId() reads → this.lastOrderId (field, read-only)
  cancelOrder() reads → repo.findById() then writes → order.status (entity, mutated)

Shared dependencies:
  repo: used by processOrder(), getOrder(), cancelOrder()
  discountService: used by processOrder() only
  eventBus: used by cancelOrder() only
```

**Feature scope**: The Method Tree focuses on **orchestrator methods** (the methods that
coordinate cross-class flow). Pure computation and simple delegation methods get
Behaviour blocks only. The cross-class handoff annotations from the feature-scope
Refactored View (§2) become the primary navigation structure.

#### How to Split Code into Extracted Methods

Every block boundary should be a place where a senior developer would genuinely consider
extracting a method. Apply these tests:

**Extract-Method Fitness Test** — every proposed block must pass ALL THREE:

1. **Nameable** — can you give it a verb-noun method name? (`validateInputs`,
   `calculateSubtotal`, `persistResult`). If you can only name it "part of X" or
   "continuation of Y", the boundary is wrong — merge it with the adjacent block
2. **Self-contained** — can you define clear typed inputs (parameters) and a typed
   output (return value)? If the block needs 5+ parameters or its "output" is just
   "side-effect on ambient state", reconsider the boundary
3. **One reason to change** — would this code change for exactly one business reason?
   If half the block is validation and the other half is calculation, split them

**Where to look for natural split points** — the source code tells you where blocks
begin and end:

| Signal in source code | Block boundary type |
|---|---|
| **Blank line** left by the original developer | Intent separator — developer already thought in blocks |
| **Comment** describing "what comes next" | Phase marker — explicit block start |
| **New variable declaration** starting a new phase | Data-phase boundary — new data context begins |
| **Try-catch boundary** | Error-handling boundary — different concern |
| **If-else / switch branch** | Decision boundary — each branch is a candidate block |
| **Loop boundary** | Iteration boundary — setup / body / post-loop are separate blocks |
| **Return statement** | Exit boundary — everything before return is the "compute" block |
| **Method call on an injected dependency** | Delegation boundary — external call is its own concern |

#### Per-Method Template

For each extracted method (B*n*), provide:

```text
#### B1 — `Order validateAndGuardInputs(Order order)` (L30-45)
```

````java
// paste the ACTUAL source code verbatim — do not reformat
if (order == null) {                              // ← [B1.1] no null-check exists — NPE risk (E1)
    throw new IllegalArgumentException("order");
}
if (order.getItems().isEmpty()) {                 // ← [B1.2] empty ≠ null — returns valid but useless
    return order;                                 // ← early return: 0 items → 0.0 total (E2)
}
validator.validate(order);                        // ← [B1.3] delegates to Validator — 🔍 step-into
````

```text
📋 Behaviour:
  Data structures: Order order (arg, shared → B2, NOT mutated — may be null),
                   Order.items: List<LineItem> (field-access via order, checked for empty, NOT mutated),
                   Validator validator (injected — delegates business rule checks)
  Operations:      null-check order → empty-check items → delegate to validator.validate()
  Algorithm:       Guard-clause chain with early exits — three sequential gates:
                     1. order == null  → throw (not handled — E1)
                     2. items.isEmpty  → early return (valid but 0 items — E2)
                     3. validator.validate(order) → throws on business rule failure
  Data flow:       Order (raw, unchecked) ──guard gates──→ Order (same object, now proven valid)
  Side-effects:    none — pure gate, no mutations (order object passed through as-is)
  Contract:        post: order ≠ null ∧ order.items.size > 0 ∧ all business rules pass

← Receives: Order order (may be null, unchecked) from caller
→ Produces: Order order (validated, non-null, items present) → B2
⚠ E1: null order → NPE at L30 (unhandled) | E2: empty items → 0.0 total (handled, but confusing)
State: none mutated
🔍 Step-into: validator.validate() at L38 — worth tracing validation rules
🛑 Breakpoint: L30 — verify order is non-null on entry
```

**Template rules:**

| Element | Required? | Purpose |
|---|---|---|
| **Header** `#### Bn — virtual signature (lines)` | Always | Intent + contract in one line |
| **Code fence** with actual source + inline annotations | Always | The code — developer reads THIS |
| **📋 Behaviour** | Always | Abstraction: what's happening, data structures, algorithm, data flow |
| **← Receives / → Produces** | Always | Data flow between methods — typed contract |
| **⚠ Edge cases** | Only if present | E-refs with trigger + impact |
| **State** | Only if mutates | Variables/fields changed, lifecycle, thread-safety |
| **🔍 Step-into / 🛑 Breakpoint / 👁 Watch** | method scope | Debugging markers for key lines |

#### 📋 Behaviour — What to Write

The `📋 Behaviour` block is the **abstraction layer** between the raw code and the
data flow arrows. It uses a structured format with labelled fields so the developer
can scan for exactly the dimension they care about. Every field answers a specific
question about the extracted method:

```text
📋 Behaviour:
  Data structures: <key data — args, locals, fields, shared objects — with concrete types and roles>
  Operations:      <what operations are performed on those data structures>
  Algorithm:       <pseudo-code or named pattern — how the code processes data>
  Data flow:       <input ──transformation──→ output — the pipeline in one line>
  Side-effects:    <what changes outside this method — DB / fields / MQ / cache>
  Contract:        <pre/post conditions — what must be true before and after>
```

**Field-by-field guide:**

**`Data structures`** — Name every key data structure the method touches:
arguments, local variables, instance fields, and objects shared across methods.
Use concrete Java types and annotate each with its **role** so the developer knows
where it came from and how far it travels:

| Role tag | Meaning | Why it matters |
|---|---|---|
| `arg` | Method parameter | Caller controls it — check what's passed in |
| `local` | Declared inside this method | Lives and dies here — safe to reason about locally |
| `field` | Instance field (`this.x`) | Shared across methods — mutation here affects elsewhere |
| `injected` | Dependency injected via constructor/setter | External collaborator — worth tracing into |
| `shared` | Passed to/from multiple methods (B*n* → B*n+1*) | The connective tissue between extracted methods |
| `return` | The value this method produces | What downstream methods will consume |
| `mutated` | Changed by this method (value or internal state) | The most dangerous annotation — mutation = ripple risk |

A single variable can have multiple roles — e.g., `order (arg, shared → B2)` is
both an argument AND data that flows to the next extracted method. Combine freely:
`cache (field, mutated)`, `items (arg, mutated — sorted in place)`,
`subtotal (local, mutated in loop, return)`.

**Concrete types are mandatory.** The developer needs to know what's actually in
memory — `List<LineItem>`, not "a collection"; `HashMap<String, PriceTier>`, not
"a map".

- ✅ `Order order (arg, shared → B2, NOT mutated), Order.items: List<LineItem> (field-access, iterated), double subtotal (local, return)`
- ✅ `HashMap<String, PriceTier> cache (field, mutated — new entry added on miss), Validator validator (injected)`
- ✅ `List<LineItem> items (arg from B1, mutated — sorted in place by price), double subtotal (local, mutated in loop: 0.0 → total, return → B3)`
- ❌ "the order object" — no type, no role, no specifics
- ❌ "a collection of items" — which collection? List? Set? Map? arg or local?
- ❌ `Order (input)` — "input" is not a role tag; use `arg` or `shared`
- ❌ `HashMap<String, PriceTier> cache (field, read + written)` — "written" is vague; say what mutation: `mutated — new entry on miss`

**Cross-method tracking is critical.** When a variable is passed between extracted
methods (B1 → B2 → B3), annotate it with `shared → Bn` so the developer can trace
the data's journey through the method chain. This is the key connective tissue that
makes the extraction readable — without it, each method is an island.

**Mutation tracking is mandatory.** When a method mutates any variable — whether it's
an argument, a local, a field, or a shared object — annotate it with `mutated` and
describe the before → after state change. Mutation is the #1 source of bugs in
multi-method code, so the Behaviour block must make every mutation visible:

- **Mutated args** — the most dangerous: caller passed data in, and this method changed
  it. Annotate: `List<LineItem> items (arg, mutated — sorted in place by price)`
- **Mutated fields** — affects every other method in the class. Annotate:
  `String lastReceiptId (field, mutated: null → receipt.id after persist)`
- **Mutated locals** — safe if they stay local, dangerous if returned or shared.
  Annotate: `double subtotal (local, mutated in loop: 0.0 → accumulated total, return)`
- **Mutated shared objects** — the data structure was received from a prior method and
  this method changes its internal state. Annotate:
  `Order order (shared from B1, mutated — status set to VALIDATED)`
- **Immutable / not mutated** — explicitly say so when it matters: `Order order (arg,
  NOT mutated — passed through as-is)`. This reassures the reader that the method is
  safe to skip when debugging a mutation bug.

The rule: **if you can't tell from the Behaviour block alone which variables are
mutated by this method, the Behaviour block is incomplete.**

**`Operations`** — What is done TO those data structures. Use operation verbs that
describe structural interaction with the data:

- **Read ops:** iterate, lookup, get, contains, size, isEmpty, stream, filter
- **Write ops:** add, put, remove, set, clear, sort, replace, merge, compute
- **Transform ops:** map, reduce, collect, flatMap, groupBy, partition
- **Check ops:** null-check, bounds-check, type-check, isEmpty, contains

Example: `iterate List<LineItem> → map to double (price × qty) → reduce via sum()`

**`Algorithm`** — The processing logic expressed as pseudo-code or a named pattern.
This is NOT a restatement of the Java code — it's one level of abstraction higher.
Write it so a developer can understand the approach without reading the implementation.

##### When to Use One-Liner vs. Pseudo-Code

| Method complexity | Algorithm format | Example |
|---|---|---|
| **Simple** — single operation, guard clause, delegation, one-line stream | One-liner naming the pattern | `Guard-clause chain: null → empty → domain rules` |
| **Medium** — 2-3 sequential steps, single loop, pipeline with filter | 2-3 line summary with step numbers | `1. filter nulls 2. map to price 3. sum` |
| **Complex** — nested loops, branching within loops, multi-step transformation, recursion | Indented pseudo-code block (3-12 lines) | See examples below |

**Decision rule:** If the developer can understand the algorithm from one sentence +
the data flow line, use a one-liner. If they'd need to read the actual code to
understand the logic structure, write pseudo-code.

##### One-Liner Algorithm Examples

For simple methods, name the **pattern** and its key steps on one line:

```text
Algorithm: Guard-clause chain with early exits: null → empty → domain rules
Algorithm: Stream reduce: items.stream().mapToDouble(price × qty).sum()
Algorithm: Builder pattern: Receipt.builder().subtotal(x).tax(y).build()
Algorithm: Delegation: forwards to repository.save(order) — no logic here
Algorithm: Lookup + default: cache.getOrDefault(key, computeDefault())
```

##### Pseudo-Code Conventions

For complex methods, write **indented pseudo-code** that shows the logic structure
without Java syntax noise. The pseudo-code is a separate abstraction from the actual
code fence — it strips away boilerplate and reveals the algorithm's skeleton.

**Pseudo-code style rules:**

| Rule | Do | Don't |
|---|---|---|
| **Language** | Plain English verbs + data names | Java syntax (`for (var item : items)`) |
| **Indentation** | 2-space indent per nesting level | Flat list of steps |
| **Branching** | `if condition:` / `else:` | `if (condition) {` |
| **Loops** | `for each item in items:` | `items.forEach(item -> {` |
| **Annotations** | `// comment` for costs, risks, notes | No annotations |
| **Data references** | Use variable names from Data structures field | Introduce new unnamed data |
| **Method calls** | Name the concept: `fetch price from DB` | Restate the call: `priceRepo.findById(sku)` |
| **Return** | `return result` or `→ result` | Omit the output |
| **Step length** | 3-12 lines for the pseudo-code body | 1-2 lines (too compressed) or 15+ (too detailed) |

##### Pseudo-Code Examples by Pattern

**Sequential steps with branching (common in business logic):**

```text
Algorithm:
  for each item in order.items:                    // O(n) — one pass
    if item.isSpecial:
      price = lookupSpecialPrice(item.sku)         // DB hit per special item
      if price not found: fallback to item.defaultPrice
    else:
      price = item.defaultPrice
    subtotal += price × item.qty                   // subtotal mutated each iteration
  apply volume discount if subtotal > threshold    // threshold from config (field)
  return subtotal
```

**Accumulator pattern (building a result object step by step):**

```text
Algorithm:
  receipt = new Receipt.Builder()
  receipt.subtotal = subtotal (from B2)
  receipt.discount = discount (from B3)
  receipt.tax     = taxRate × (subtotal - discount)   // tax on discounted amount
  receipt.total   = subtotal - discount + receipt.tax
  persist receipt to DB via repository.save()          // DB write — within transaction
  publish OrderCompletedEvent(receipt.id) to MQ        // fire-and-forget — NOT in tx
  this.lastReceiptId = receipt.id                      // field mutation — not thread-safe
  return receipt
```

**Filter-map-collect pipeline (common in stream operations):**

```text
Algorithm:
  start with order.items (List<LineItem>)
  filter: remove items where qty ≤ 0               // defensive — shouldn't happen
  map:    each item → (item.price × item.qty)       // produces DoubleStream
  sum:    reduce with identity 0.0                   // empty list → 0.0, not null
  assign to subtotal (local)
```

**Two-pass algorithm (when one iteration isn't enough):**

```text
Algorithm:
  Pass 1 — collect:
    for each item in items:
      group items by item.category into Map<Category, List<LineItem>>
  Pass 2 — compute:
    for each (category, categoryItems) in grouped map:
      categoryTotal = sum(categoryItems.price × qty)
      apply category-specific discount rule           // from discountRules (field)
      totals.put(category, discountedTotal)
  return totals (Map<Category, Double>)
```

**Recursive algorithm:**

```text
Algorithm:
  base case: if node is leaf → return node.value
  recursive: left  = compute(node.left)              // recurse left subtree
             right = compute(node.right)             // recurse right subtree
  combine:   return merge(left, right)               // merge strategy depends on node type
  depth:     max recursion = tree height (O(log n) balanced, O(n) worst)
```

**State machine / multi-phase processing:**

```text
Algorithm:
  phase 1 — validate:  run guard checks (B1 already did this)
  phase 2 — compute:   subtotal = sum(items × prices)          // B2
  phase 3 — discount:  discounted = apply rules to subtotal    // B3
  phase 4 — tax:       tax = rate × discounted                 // B4
  phase 5 — assemble:  receipt = build from phases 2-4         // B5
  phase 6 — persist:   save receipt, publish event             // B5 (cont.)
```

##### How Pseudo-Code Maps to the Code Block

The algorithm pseudo-code and the inline-annotated code block serve **different purposes**
and operate at **different abstraction levels**. They must be consistent but not redundant:

| Aspect | Code block (````java`) | Algorithm pseudo-code |
|---|---|---|
| **Abstraction** | Actual Java — every line visible | One level above Java — logic skeleton only |
| **Purpose** | Developer reads this to understand the exact implementation | Developer reads this to understand the approach before diving in |
| **Annotations** | `// ← [B1.3] why this matters` — point-level notes on specific lines | `// DB hit` — cost/risk annotations on algorithm steps |
| **Boilerplate** | Present (variable declarations, type casts, try-catch) | Stripped — only the logic that matters |
| **Variable names** | Exact Java names as they appear in source | Same names — must match Data structures field |

**Consistency rule:** Every step in the pseudo-code must correspond to a visible section
in the code block. If the pseudo-code says "filter items where qty ≤ 0", the code block
must have that filter with an inline annotation cross-referencing why it's there. If the
code block has a significant operation that doesn't appear in the pseudo-code, the
pseudo-code is incomplete.

**Variable name rule:** The pseudo-code must use the **same variable names** as the
Data structures field and the code block. If Data structures says `double subtotal
(local, mutated in loop)`, the pseudo-code says `subtotal += price × item.qty`, and
the code block shows `subtotal += price * item.getQuantity();` — all three refer to
the same `subtotal`. Never introduce a name in pseudo-code that doesn't appear in
Data structures.

**`Data flow`** — A single-line pipeline showing how data transforms from input to
output. Use `──verb──→` arrows between stages:

```text
Data flow: Order (raw) ──validate──→ Order (valid) ──extract items──→ List<LineItem>
           ──map(price×qty)──→ DoubleStream ──sum──→ double subtotal
```

For methods with branching, show the main path and note branches:

```text
Data flow: subtotal ──[premium? 15% cap : bulk? tiered : standard 0%]──→ discounted double
```

**`Side-effects`** — What changes in the world outside this method's return value.
Be specific about WHAT is changed, WHERE, and whether it's reversible:

- ✅ `Writes Order row to orders table via repository.save() — within transaction, reversible`
- ✅ `Mutates this.lastProcessedId field (String) — not thread-safe`
- ✅ `Publishes OrderCompletedEvent to MQ — fire-and-forget, NOT reversible`
- ✅ `none — pure computation, no side-effects`
- ❌ "has side-effects" — says nothing

**`Contract`** — Pre-conditions (what must be true on entry) and post-conditions
(what is guaranteed on exit). Use `pre:` and `post:` prefixes:

```text
Contract: pre: order ≠ null, items non-empty | post: subtotal ≥ 0.0 (identity for empty)
Contract: post: receipt persisted to DB OR exception thrown — never silent failure
Contract: pre: cache warmed | post: cache updated with new entry for this SKU
```

**Field presence rules:**

| Field | When to include | When to omit |
|---|---|---|
| **Data structures** | Always | Never — every method touches data |
| **Operations** | Always | Never — every method does something to data |
| **Algorithm** | Always (1 line for simple, multi-line pseudo-code for complex) | Never |
| **Data flow** | Always | Never — even "passthrough" is a valid flow |
| **Side-effects** | Always (write `none` explicitly when pure) | Never |
| **Contract** | When pre/post conditions are non-obvious | Trivial getters/setters |

**Length:** 4-8 lines for simple methods (all fields, one line each). 8-15 lines for
complex methods (multi-line Algorithm pseudo-code). The structured format replaces the
old prose paragraph — no unstructured text in the Behaviour block.

**The skip test:** A developer who reads ONLY the `📋 Behaviour` block should:

1. Know what data structures are in play and what types they are
2. Understand the algorithm well enough to predict the output for a given input
3. Know what side-effects to expect (or that there are none)
4. Decide whether they need to read the actual code or can move to the next method

**Anti-patterns (don't write these):**

- ❌ `Data structures: the order` — no type, no role, no specifics
- ❌ `Data structures: Order (input)` — "input" is not a role; use `arg`, `local`, `field`, etc.
- ❌ `Data structures: List<LineItem>` — missing variable name, role, and what happens to it
- ❌ `Data structures: cache (field, read + written)` — "written" is vague; what mutation? new entry? cleared? replaced?
- ❌ `Data structures: subtotal (local)` — is it mutated? what's its lifecycle (0.0 → total)? is it returned?
- ❌ `Algorithm: validates the order` — just restates the method name
- ❌ `Operations: calls validator.validate()` — restates the code, says nothing about WHAT it does to the data
- ❌ A 10-line prose paragraph instead of the structured fields

**Good examples (complete Behaviour blocks):**

✅ Simple guard method (args + injected dependency, no locals, no mutations):

```text
📋 Behaviour:
  Data structures: Order order (arg, shared → B2, NOT mutated — may be null),
                   Order.items: List<LineItem> (field-access via order, checked for empty, NOT mutated),
                   Validator validator (injected — delegates business rule checks)
  Operations:      null-check order → empty-check items → delegate to validator.validate()
  Algorithm:       Guard-clause chain with early exits: null → empty → domain rules
  Data flow:       Order (raw, unchecked) ──guard gates──→ Order (same object, proven valid)
  Side-effects:    none — pure gate, no mutations (order passed through as-is)
  Contract:        post: order ≠ null ∧ items.size > 0 ∧ all business rules pass
```

✅ Stream computation (arg received from prior method, local accumulator built by stream):

```text
📋 Behaviour:
  Data structures: List<LineItem> items (arg from B1, iterated, NOT mutated),
                   double subtotal (local, built by stream reduce, return → B3)
  Operations:      stream items → mapToDouble (price × qty per item) → sum into subtotal
  Algorithm:       Stream reduce: items.stream().mapToDouble(item → price × qty).sum()
                   Identity is 0.0, so empty list → 0.0 (not null, not error)
  Data flow:       List<LineItem> ──map(price × qty)──→ DoubleStream ──sum()──→ double subtotal
  Side-effects:    none — pure computation
  Contract:        post: subtotal ≥ 0.0 (assumes prices and quantities are non-negative)
```

✅ Complex method (args from prior methods + fields mutated + local built + side-effects):

```text
📋 Behaviour:
  Data structures: double subtotal (arg — from B2), double discount (arg — from B3),
                   double tax (arg — from B4),
                   Receipt receipt (local, built via Builder, return),
                   HashMap<String, PriceTier> cache (field, mutated — new entry on miss via computeIfAbsent),
                   String lastReceiptId (field, mutated: null → receipt.id after persist),
                   MessageQueue mq (injected, published to — NOT mutated)
  Operations:      build Receipt from args → cache.computeIfAbsent → mq.publish → field write
  Algorithm:
    build receipt from subtotal + discount + tax (received from B2-B4)
    if cache miss for order.customerId:
      fetch PriceTier from DB, store in cache    // cache-aside pattern
    publish OrderCompletedEvent to MQ            // fire-and-forget
    write receiptId to this.lastReceiptId        // field mutation
  Data flow:       (subtotal, discount, tax) ──Builder──→ Receipt ──persist──→ DB row
                   Receipt.id ──publish──→ MQ event
  Side-effects:    DB write (orders table, within tx), MQ publish (irreversible),
                   field mutation: this.lastReceiptId (not thread-safe)
  Contract:        post: receipt persisted OR exception thrown — never silent failure
```

**Inline annotation rules:**

- Format: `// ← [B1.3] why this line matters`
- Only annotate lines where understanding would break if the developer skipped them
- Never annotate what the code literally does (`// adds 1 to count`) — only WHY it
  matters, WHAT it connects to, or WHAT goes wrong when it's wrong
- Reference E-tags for edge cases, other B-tags for cross-block dependencies
- Mark external calls: `🔌 DB read`, `⚡ async`, `✦ pure logic`

**Block continuity rule** — the output of B*n* must be consumable as input to B*n+1*.
If you can't describe what data passes between two adjacent methods, the split is wrong
or a method is missing. The `← Receives` / `→ Produces` arrows must form a complete
chain matching the Refactored View.

#### Nested Extraction (Methods Within Methods)

When an extracted method is itself complex (15+ lines, or contains nested loops/branches
with distinct concerns), decompose it further into sub-methods. Use nested B-IDs:

```text
#### B3 — `double applyPremiumDiscount(double subtotal)` (L54-62)
```

**Refactored view of B3:**

```java
private double applyPremiumDiscount(double subtotal) {
    double loyaltyBonus = lookupLoyaltyBonus(customer);                 // B3a: L54-56
    double baseDiscount = calculateBaseDiscount(subtotal);              // B3b: L57-59
    return combineDiscounts(baseDiscount, loyaltyBonus);                // B3c: L60-62
}
```

Then provide per-method detail for B3a, B3b, B3c at a deeper heading level (`#####`).
This creates a readable tree structure:

```text
### 3 — Method Extraction Tree
  #### B1 — validateAndGuardInputs (L30-45)
  #### B2 — calculateSubtotal (L46-58)
  #### B3 — applyPremiumDiscount (L54-62)         ← complex, decomposed further
    ##### B3a — lookupLoyaltyBonus (L54-56)
    ##### B3b — calculateBaseDiscount (L57-59)
    ##### B3c — combineDiscounts (L60-62)
  #### B4 — calculateTax (L69-78)
  #### B5 — buildReceipt (L79-95)
  #### B6 — persistAndNotify (L96-110)
```

**Nest when:**

- A block has 15+ lines with distinct inner phases
- A block contains a loop body with branching (the loop body is a sub-method)
- A block delegates to 2+ meaningful private method calls (each is a sub-method)
- A block has try-catch where the try body and catch body are separate concerns

**Don't nest when:**

- A block is a straight-line sequence of 10-15 lines doing one thing
- The inner logic is a single expression (even if complex, like a stream pipeline)
- Nesting would create sub-methods with only 2-3 lines each

#### Completeness Rules

- **Every line of the target code must appear in at least one method's code fence.**
  The methods together reconstruct the full source. No gaps
- **Don't skip code** — a developer scrolling through the source file must find every
  single line explained somewhere in the tree
- Aim for **3-8 methods per level** — fewer for simple targets, more for complex ones

#### Handling Complex Code Patterns

##### God Classes (10+ responsibilities, 500+ lines)

1. **Responsibility inventory first** — before the method tree, group methods by
   responsibility and show the virtual class each group WOULD belong to:

   ```text
   ## Responsibility Inventory — OrderService (847 lines, 23 methods)

   | # | Responsibility | Virtual Class | Methods | Lines |
   |---|---|---|---|---|
   | R1 | Input validation | `OrderValidator` | validateOrder, checkStock | L30-120 |
   | R2 | Price calculation | `PriceCalculator` | calculateTotal, applyDiscount | L121-280 |
   | R3 | Persistence | `OrderRepository` | saveOrder, updateStatus | L281-390 |
   | R4 | Notification | `OrderNotifier` | notifyCustomer, publishEvent | L391-450 |
   ```

2. **Deep-dive one responsibility at a time** — each responsibility group gets its own
   Refactored View and Method Extraction Tree
3. **Cross-responsibility data flow** — show how data passes between groups (R1 → R2 → R3)

##### Very Long Methods (100+ lines)

1. **Two-pass extraction:**
   - **Pass 1 — Coarse methods** (5-8 methods covering the full method at region level)
   - **Pass 2 — Fine methods** (each coarse method decomposed into 2-4 sub-methods)
2. **Branch maps for deep nesting** — if the method has 3+ levels of nesting, draw
   an ASCII map showing which block handles each branch:

   ```text
   L42  if (isValid)
   L43  ├─ for (item : items)           → B3 (processItems)
   L44  │  ├─ if (item.isSpecial())     → B3a (handleSpecialItem)
   L50  │  │  └─ try { ... }            → B3b (lookupSpecialPrice)
   L55  │  └─ else                      → B3c (calculateStandardPrice)
   L60  └─ else                         → B2 (handleValidationFailure)
   ```

3. **Decision tree for complex conditionals** — when there are 3+ branches:

   ```text
   L42: if (order.type == PREMIUM)
        ├─ YES → B3 (applyPremiumDiscount) — 15% discount cap
        └─ NO → L48: if (order.total > 1000)
                 ├─ YES → B4 (applyBulkDiscount) — tiered rates
                 └─ NO → L52: if (customer.isLoyal())
                          ├─ YES → B5 (applyLoyaltyDiscount) — 5% flat
                          └─ NO → B6 (noDiscount)
   ```

##### Deeply Nested / Tangled Logic

Flatten-and-label the blocks even though the code is nested:

```text
B1 — validateInputs (L30-35)              [nesting: 0]
B2 — setupItemLoop (L36-38)               [nesting: 1]
B3 — handleSpecialItem (L39-52)           [nesting: 2, inside B2 loop]
B4 — calculateStandardPrice (L53-58)      [nesting: 2, inside B2 loop]
B5 — accumulateLoopResult (L59-62)        [nesting: 1, end of B2 loop]
B6 — aggregateAndReturn (L63-70)          [nesting: 0]
```

Each block's code snippet should include 1-2 lines of surrounding context (the enclosing
`if`/`for`/`try`) so the developer can see where in the nesting this block lives.

### 3b — Error & Exception Map

**This section consolidates every error path in the code.** Individual E-refs are
scattered throughout the Method Tree — this map brings them together so a developer
can see ALL failure modes at once, like a senior drawing "here's every way this breaks"
on the whiteboard.

#### Error Inventory Table

List every E-ref with its origin, propagation path, and handling status:

| E-ref | Trigger | Origin (Block + Line) | Exception Type | Propagation | Handled? | Impact |
|---|---|---|---|---|---|---|
| E1 | `order == null` | B1, L30 | `NullPointerException` | Uncaught → caller | ❌ No | 500 error to user |
| E2 | `items.isEmpty()` | B1, L34 | — (early return) | Returns `order` | ✅ Yes | 0.0 total — confusing but not crash |
| E3 | `discount > subtotal` | B3, L62 | — (negative result) | Silent — negative total returned | ❌ No | Charging negative amount |
| E4 | `repository.save()` fails | B6, L98 | `DataAccessException` | Caught by B6 try-catch | ✅ Yes | Logged, receipt NOT persisted |
| E5 | Concurrent access | B6, L105 | — (race condition) | `lastReceiptId` overwritten | ❌ No | Wrong receipt ID returned to some callers |

#### Error Propagation Flow

Show how exceptions flow through the method chain — where they originate, where they're
caught, and where they escape to the caller:

```text
B1 (validate)
  ├─ E1: NPE ──────────────────────────────────────── uncaught → caller gets 500
  └─ E2: early return ─────────────────────────────── handled (0.0 total)

B2 (calculate)
  └─ (no errors — pure computation)

B3 (discount)
  └─ E3: negative result ──────────────────────────── silent — flows to B4 as negative

B4-B5 (tax + build)
  └─ (no errors — pure computation, inherits E3 silently)

B6 (persist)
  ├─ E4: DataAccessException ──┬── caught ──── logged
  │                            └── receipt NOT persisted ← DATA LOSS
  └─ E5: race on lastReceiptId ───────────────────── silent, no exception
```

**Unhandled error summary** — the "danger" section for quick scanning:

```text
UNHANDLED (require attention):
  E1 — null order: NPE propagates to caller with no context → add guard or @NonNull
  E3 — negative total: passes silently through tax + build → add floor(0.0) or throw
  E5 — thread-safety: concurrent writes to lastReceiptId → volatile or AtomicReference

HANDLED (confirmed safe):
  E2 — empty items: early return, produces 0.0 total (intentional but confusing)
  E4 — DB failure: caught, logged, but receipt is lost — acceptable? check business rules
```

**When to include this section:**

| Scope | Error Map | Error Propagation Flow |
|---|---|---|
| Method with 0-1 E-refs | Skip — edge cases are visible in the Method Tree | Skip |
| Method with 2+ E-refs | Include error inventory table | Include if errors cross methods |
| Class scope | **Mandatory** — consolidate all E-refs across all methods | **Mandatory** |
| Feature scope | **Mandatory** — show errors crossing class boundaries | **Mandatory** |

### 3c — Design Rationale (Why This Code Exists)

A senior architect doesn't just explain WHAT the code does — they explain **WHY** it
was designed this way and what alternatives were considered. This section captures the
design reasoning that would otherwise exist only in the original developer's head.

**Include when:** The code has non-obvious design decisions, pattern choices, or
architectural constraints. Skip for simple CRUD or straightforward utility methods.

```text
Design Rationale:
  Why this pattern:  <why Strategy/Template/Service/etc. instead of alternatives>
  Key trade-off:     <what was sacrificed for what — e.g., "simplicity over performance">
  Constraint:        <what forced this design — e.g., "must be backward-compatible with v1 API">
  Alternative rejected: <what was NOT done and why — e.g., "event sourcing was too complex for MVP">
  Evolution risk:    <what will break this design — e.g., "adding a 4th discount type requires modifying 3 methods">
```

**Example:**

```text
Design Rationale:
  Why this pattern:  Orchestrator pattern (processOrder coordinates 6 steps) — simple
                     sequential flow was chosen over an event-driven pipeline because all
                     steps must complete in a single transaction
  Key trade-off:     All steps tightly coupled to OrderService — any step failure rolls
                     back everything. Simpler but less resilient than saga pattern
  Constraint:        Legacy API contract requires synchronous response with receipt —
                     cannot go fully async
  Alternative rejected: Event sourcing (too complex for current volume), CQRS (read/write
                     patterns are identical — no benefit)
  Evolution risk:    Adding a new pricing rule requires modifying applyDiscountRules()
                     (B3) — no extension point. 3+ discount types → consider Strategy
```

### 4 — Dependencies & Coupling

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

### 4b — Key Takeaways & Developer Cheat Sheet

Summarise everything for quick future reference:

**In 5 bullet points:**

- What this code does (one sentence)
- The most important design decision and why
- The biggest risk/edge case to watch for
- The key dependency to understand
- What to deep-dive next if you want to go deeper

**Developer cheat sheet** (copy-pasteable quick-reference):

```text
Purpose:     <one-liner from Quick Scan>
Entry:       <who calls it, when — from Quick Scan entry points>
Happy path:  <B1 → B2 → B3 → ... → output — from Refactored View>
Error path:  <E1, E3 unhandled — from Method Tree edge cases>
Key blocks:  <B2 (calculateSubtotal), B3 (applyDiscount) — from Method Tree>
Thread-safe: yes / no / partially — <reference E4 if applicable>
Testable:    easy / moderate / hard — because <reference Dependencies verdict>
```

**Debugging quick-start** (when opening the debugger for this code):

```text
Breakpoints:
  L42  — entry guard (verify order is non-null and valid)
  L51  — after discount applied (verify total is positive)
  L60  — field mutation (watch lastProcessedId)

Watch expressions:
  order, order.getItems().size(), total, discount, this.lastProcessedId

Conditional breakpoints:
  L51: discount > 1.0        — catches E3 (negative total)
  L42: order == null          — catches E1 (null input)

Step-into targets (worth tracing):
  validator.validate() — verify validation rules
  this.transform()    — core logic, worth tracing line-by-line

Step-over (trust these):
  Controller.handle()  — thin wrapper
  EventBus.publish()   — async fire-and-forget
```

The cheat sheet references B/L/E IDs so a developer can drill into any detail.
The debugging quick-start is derived from the Method Extraction Tree (step-into/over
markers, breakpoint markers, watch expressions, edge case triggers).

**Debugging quick-start derivation flow:**

```mermaid
flowchart LR
    MT["Method Tree\n🔍 Step-into / ⏩ Step-over"] -->|step targets| QS["Cheat Sheet\nDebugging Quick-Start"]
    BP["Method Tree\n🛑 Breakpoint markers"] -->|BP lines| QS
    WE["Method Tree\nState: watch expressions"] -->|watch vars| QS
    EC["Method Tree\n⚠ Edge cases (E-refs)"] -->|conditional BPs\nfrom triggers| QS
```

### Output Rules

- **Scope-adaptive:** For `method` scope, all phases apply (Quick Scan → Refactored
  View → Method Extraction Tree → Error & Exception Map → Dependencies → Cheat Sheet).
  For `class`, show Quick Scan (with state model + method inventory), class-level
  Refactored View, Method Tree for complex methods, Error Map (mandatory), Design
  Rationale, and Dependencies. For `feature`, show Quick Scan (with class inventory),
  cross-class Refactored View with handoff markers, Method Tree for key orchestrator
  methods, Error Map (mandatory — show cross-class error propagation), Design Rationale,
  and Dependencies
- **Error Map placement:** The Error & Exception Map (§3b) comes after the Method
  Extraction Tree and before Dependencies. For methods with 0-1 E-refs, skip it —
  the edge cases are visible inline. For class/feature scope, the Error Map is mandatory
- **Design Rationale:** Include for any code with non-obvious design decisions, pattern
  choices, or architectural constraints. Skip for trivial utility methods or simple CRUD
- **Code-first:** Always show actual source code in fenced blocks — never describe code
  without showing it. A developer should be able to read ONLY this document and
  reconstruct the mental model of the code completely
- **Side-by-side design:** Line ranges (L*n*) in every method MUST match the actual source
  file. A developer with the source open on the left and this doc on the right should be
  able to locate any extracted method's code instantly by line number
- **Type-precise:** Always include types in virtual method signatures and data flow arrows
- **Honest:** If something is unclear, surprising, or looks like a bug, say so directly
- **No refactoring in the analysis** — the virtual method signatures and extraction tree
  are for understanding, NOT a refactoring proposal. The code stays exactly as-is. If you
  see a genuine extract-method opportunity, note it in the Cheat Sheet takeaways, but
  do NOT reorganise or rewrite the actual code in the analysis
- **Completeness over brevity** — every line of the target code must appear in at least
  one method's code fence in the Method Extraction Tree. No gaps. The methods together
  reconstruct the full source
- **Cross-reference coherence** — every B-ref in the Refactored View must have a
  corresponding section in the Method Tree. Every E-ref in an edge case annotation must
  name its method and line. The `← Receives` / `→ Produces` arrows must chain correctly
  through the entire Refactored View
- **No analysis dump** — every annotation must tell the developer something they cannot
  see by reading the source code alone. If an annotation just restates what the code
  literally does (`"this line adds the price to the total"` for `total += price`),
  remove it. Focus on: hidden contracts, implicit assumptions, data flow between methods,
  edge cases, and the "why" behind non-obvious choices. A developer who reads Java
  fluently does not need a prose translation of their code
- End with one "what to deep-dive next" recommendation

#### Complexity-Adaptive Thresholds

The depth of analysis scales with the complexity of the target code:

| Target | Method Tree | Nested Extraction | Error Map | Design Rationale | Multi-Caller Table | Responsibility Inventory |
|---|---|---|---|---|---|---|
| Method ≤ 30 lines | 3-5 methods | No | If 2+ E-refs | If non-obvious design | If 3+ callers | N/A |
| Method 30-100 lines | 5-8 methods | If nested 3+ levels | If 2+ E-refs | Recommended | If 3+ callers | N/A |
| Method 100+ lines | 8-15 methods (two-pass) | **Mandatory** | **Mandatory** | **Mandatory** | If 3+ callers | N/A |
| Method 200+ lines | 12-20 methods (two-pass) | **Mandatory** | **Mandatory** | **Mandatory** | If 3+ callers | N/A |
| Class ≤ 5 methods | Per-method extraction | No | **Mandatory** | Recommended | Per method if applicable | No |
| Class 5-10 methods | Per-method extraction | For complex methods | **Mandatory** | **Mandatory** | Per method if applicable | Recommended |
| God class (10+/500+) | Per-responsibility then per-method | **Mandatory** | **Mandatory** | **Mandatory** | **Mandatory** | **Mandatory** |

**Scaling rules:**

- Method > 50 lines → Method Extraction Tree with full per-method detail is mandatory
- Method > 100 lines → two-pass extraction (coarse + fine) is mandatory
- Method > 200 lines → inline annotations must cover 30+ key lines across all methods
- Class > 5 public methods → Refactored View + Method Tree for EACH significant method
  (skip trivial getters/setters/toString)
- Class > 10 public methods or > 500 lines → responsibility inventory is mandatory
  before method-level analysis
- Method called from 3+ distinct callers → caller context table in Quick Scan is mandatory
- Nesting depth 3+ levels → branch map / decision tree is mandatory in Method Tree

### Session Capture — Auto-Save to Brain

After completing the deep-dive analysis, you **MUST** capture the full output as
a session file by **actually creating the file** using the `create_file` tool. This is
not optional — every deep-dive produces a permanent reference document. Do NOT just
show the analysis in chat and skip the file creation.

#### Workspace Resolution

The session file must be written to the `brain/ai-brain/sessions/` directory in the
**workspace where the analysed code lives** — NOT necessarily this (learning-assistant)
repository. Resolve the brain path as follows:

1. **Identify the workspace root** — the root of the VS Code workspace or git repo
   containing the target code (check `git rev-parse --show-toplevel` if unsure)
2. **Find the brain directory** — look for `brain/ai-brain/sessions/` under that
   workspace root. If it does not exist, create the required directory structure:
   `brain/ai-brain/sessions/work/code-analysis/deep-dive/`
3. **Use absolute paths** — when calling `create_file`, always use the full absolute
   path (e.g., `E:\mgcnoscan\iesd-26\brain\ai-brain\sessions\work\code-analysis\deep-dive\<filename>.md`)
4. **Environment variable override** — if `BRAIN_PATH` is set, use that instead of
   the default `brain/ai-brain` relative path

#### Capture Workflow

```mermaid
flowchart TD
    A[Deep-dive analysis complete] --> B[1. Query system clock]
    B --> C[2. Classify domain]
    C --> D{Work or Personal?}
    D -->|Work| E[sessions/work/code-analysis/deep-dive/]
    D -->|Personal| F[sessions/personal/personal-work/software-dev/code-review/deep-dive/]
    E --> G[3. Build filename]
    F --> G
    G --> H[4. Read template]
    I --> J[5. Populate frontmatter + all 4 phases]
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

1. **Get the actual current timestamp** — run this command in the terminal (do NOT guess):

   ```powershell
   Get-Date -Format "yyyy-MM-dd_hh-mmtt_hh:mm tt"
   ```

   Parse the output to extract:
   - `yyyy-MM-dd` → frontmatter `date` field (e.g., `2026-04-20`)
   - `hh-mmtt` → filename timestamp segment, lowercase (e.g., `09-21pm`)
   - `hh:mm tt` → frontmatter `time` field, quoted (e.g., `"09:21 PM"`)

   **You MUST run this command.** Never guess, round, or use a placeholder.

2. **Resolve the workspace root** — identify where the analysed code lives:

   ```powershell
   git rev-parse --show-toplevel
   ```

   This gives you the workspace root (e.g., `E:/mgcnoscan/iesd-26`). The brain
   session path is `<workspace-root>/brain/ai-brain/sessions/`.

3. **Determine the domain** from the code being analysed:
   - Code in a work project → `work`
   - Code in a personal/side project → `personal`

4. **Build the absolute file path** — deep-dive sessions go to a **permanent
   `deep-dive/` sub-folder** (not subject to de-escalation):
   - Work: `<workspace-root>/brain/ai-brain/sessions/work/code-analysis/deep-dive/`
   - Personal: `<workspace-root>/brain/ai-brain/sessions/personal/personal-work/software-dev/code-review/deep-dive/`
   - If a class sub-package already exists (e.g., `deep-dive/order-service/`), place
     the file inside it
   - **If the directory does not exist, create it** (the `create_file` tool creates
     parent directories automatically)

5. **Build the filename** following the naming convention. Files inside `deep-dive/`
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

6. **Check for existing versions** — list the target directory to check if a file
   with the same class+method subject already exists:
   - If found → create a versioned continuation: append `_v2`, `_v3`, etc.
   - Set `version: 2` and `parent: <original-filename>` in frontmatter

7. **Build the file content** using the template structure from
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

   **Body** — populate all 4 phases (Quick Scan, Refactored View, Method Extraction
   Tree, Context & Cheat Sheet) from the deep-dive analysis output above.
   Every section must contain real, substantive content — not placeholder text.
   The Method Extraction Tree must reconstruct the full method across all extracted methods.

8. **WRITE THE FILE** — use the `create_file` tool with the **absolute path** from
   step 4 + filename from step 5. The file content is the frontmatter + all 4 phases.
   This step is **mandatory** — do NOT skip it or defer it.

   Example path: `E:\mgcnoscan\iesd-26\brain\ai-brain\sessions\work\code-analysis\deep-dive\2026-04-20_09-21pm_order-service-calculate-total.md`

9. **Check escalation** — count session files in the target folder:
   - If **3+ files** share the same class prefix (e.g., `order-service-*`), create a
     class sub-package per Pattern 3a in chat-capture instructions
   - Move matching files into `<class-kebab>/` and truncate their names
     (drop class prefix — implied by folder)
   - If **2 files** and a multi-part deep-dive is planned, apply early escalation

10. **Append to SESSION-LOG.md** — use `replace_string_in_file` or `editFiles` to
    append a row to `<workspace-root>/brain/ai-brain/sessions/SESSION-LOG.md`
    (create the file with headers if it doesn't exist):

   ```markdown
   | 2026-04-20 | 09:21 PM | work | code-analysis | order-service-calculate-total | v1 | high | draft | [View](work/code-analysis/deep-dive/2026-04-20_09-21pm_order-service-calculate-total.md) |
   ```

11. **Append to CAPTURE-LOG.md** — log the capture operation in
    `<workspace-root>/brain/ai-brain/sessions/CAPTURE-LOG.md`
    (create the file with headers if it doesn't exist):

    ```markdown
    | 2026-04-20 | 09:21 PM | capture | Deep-dive: OrderService.calculateTotal (method, all) → work/code-analysis/deep-dive/ | 1 file created |
    ```

    If escalation was triggered, log that as a separate row:

    ```markdown
    | 2026-04-20 | 09:22 PM | escalation:pattern-3a | Created order-service/ sub-package in deep-dive/ (3+ class files) | N files moved |
    ```

12. **Report** — tell the user: "Deep-dive captured to `<absolute-path>`"
    Include the full path so the user can open the file directly.

#### Content Quality Rules

- **Method Extraction Tree** must be thorough — split every non-trivial method
  into 3-8 extracted methods with actual code snippets and inline annotations. This is
  the most valuable section for a developer reading the file later.
- **Quick Scan** must be immediately understandable — a developer should get the full
  picture in 30 seconds by reading just this section.
- **Inline annotations** should cover key decision lines, not boilerplate.
- The file must be **self-contained** — a developer who has never seen this code should
  be able to understand it fully by reading only this file.
- Include actual code blocks (not just descriptions) in the Refactored View and Method Tree.
