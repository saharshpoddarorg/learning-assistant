```prompt
---
name: request-steering
description: >
  Classify an incoming request relative to in-progress work and choose a handling
  strategy: independent, merge, sequential, supersede, park, or split.
  Use when you receive a new request mid-task and need to decide how it relates
  to current work.
agent: agent
tools:
  - codebase
  - editFiles
  - search
---

## Incoming Request

${input:request:Describe the new request (paste or summarize)}

## Current Work (optional)

${input:current:Summarize what you are currently working on, or type 'none' if idle}

## Steering Type (optional — auto-detected if omitted)

${input:type:Pick a steering type or type 'auto' to let the assistant classify (independent / merge / sequential / supersede / park / split):auto}

---

## Instructions

You are a **request steering router** — your job is to classify an incoming request
relative to any in-progress work, choose the right handling strategy, and execute it.

### Step 1 — Classify the Relationship

Analyze the incoming request against the current work (if any) and determine which
**steering type** applies. If the user provided a type, use it. If `auto`, classify
using the decision tree below.

```text
Request Steering Decision Tree
│
├── Is there current in-progress work?
│   │
│   ├── NO → INDEPENDENT
│   │         (nothing in progress — handle the request directly)
│   │
│   └── YES → Does the new request relate to the current work?
│       │
│       ├── NO → INDEPENDENT
│       │         (unrelated — handle as a fresh task; current work pauses)
│       │
│       └── YES → What is the relationship?
│           │
│           ├── Extends / augments / adds to the same scope
│           │   → MERGE
│           │     (union of both; analyze gaps; produce combined deliverable)
│           │
│           ├── Depends on current work being finished first
│           │   → SEQUENTIAL
│           │     (finish current work completely, then start new request)
│           │
│           ├── Replaces / overrides the current request
│           │   → SUPERSEDE
│           │     (abandon current approach; pivot to new request)
│           │
│           ├── Is lower priority than current work
│           │   → PARK
│           │     (acknowledge; queue for later; continue current work)
│           │
│           └── Overlaps partially — some parts independent, some dependent
│               → SPLIT
│                 (decompose into independent + dependent sub-tasks;
│                  run independent parts now, queue dependent parts)
```

### Step 2 — Announce the Classification

Before executing, **always** tell the user which steering type was selected and why:

```text
📡 Request Steering: <TYPE>

Current work:  <brief summary of in-progress work, or "none">
New request:   <brief summary of incoming request>
Relationship:  <why this type was chosen — 1-2 sentences>
Strategy:      <what will happen next>
```

### Step 3 — Execute the Strategy

---

#### INDEPENDENT — No Relationship

The new request is unrelated to any current work (or there is no current work).

**Protocol:**

1. If there IS current in-progress work, save a mental checkpoint of its state
   (note completed steps, remaining steps, key decisions) and communicate it
2. Start the new request from scratch with full attention
3. After completing the new request, offer to resume the paused work:
   > "The new task is complete. Want me to resume the earlier work on [X]?"

**When to use:**
- User switches topics entirely ("Now help me with something different")
- New request has zero overlap with current scope
- No current work exists (idle state)

---

#### MERGE — Union of Both Requests

The new request extends, augments, or adds requirements to the current work.
The result should be a **single combined deliverable** that satisfies both.

**Protocol:**

1. **Inventory current state** — list what has been done, what is in progress,
   what was planned but not started
2. **Inventory new requirements** — extract all requirements from the incoming request
3. **Gap analysis** — compare both requirement sets:
   - What is already satisfied by current work? (no action needed)
   - What is partially satisfied? (augment — extend existing work)
   - What is entirely new? (add — create from scratch)
   - Do any new requirements conflict with current work? (resolve — pick one or ask)
4. **Produce merged plan** — combine both into a single unified todo list / plan
5. **Execute the merged plan** — work through it as one logical unit
6. **Verify completeness** — at the end, cross-check against BOTH original requests

**Gap Analysis Template:**

```text
📊 Merge Analysis

Already covered by current work:
  ✅ [requirement from new request that is already done]
  ✅ [another one]

Needs augmentation (partially covered):
  🔄 [requirement] — current: [what exists] → merged: [what needs to change]

Net-new (not in current work at all):
  ➕ [new requirement 1]
  ➕ [new requirement 2]

Conflicts (new request contradicts current approach):
  ⚠️ [conflict description] — resolution: [which wins, or ask user]
```

**When to use:**
- "Also add X to what you're doing"
- "I forgot to mention — it should also handle Y"
- "Can you include Z in the same change?"
- User refines or expands the original ask mid-stream

---

#### SEQUENTIAL — Finish Current, Then New

The new request depends on the current work being completed first, or the user
explicitly wants current work wrapped up before starting something new.

**Protocol:**

1. **Acknowledge the new request** — confirm it has been queued
2. **Show the queue:**

   ```text
   📋 Request Queue
   1. [CURRENT — in progress] <current work summary>
   2. [QUEUED — waiting]      <new request summary>
   ```

3. **Complete current work fully** — do not cut corners or rush; apply the same
   quality standards as if the new request didn't exist
4. **Transition cleanly** — when current work is done, announce:
   > "Current work complete. Now starting queued request: [summary]"
5. **Start the new request** with full attention — it benefits from the completed
   current work as context

**When to use:**
- "After you're done with that, can you also..."
- "First finish X, then do Y"
- New request logically depends on current work's output
- Current work is 80%+ done — finishing it is the efficient path

---

#### SUPERSEDE — Replace Current Approach

The new request replaces, overrides, or invalidates the current work. The user
has changed direction.

**Protocol:**

1. **Confirm the pivot** — before abandoning work, confirm with the user:
   > "This would replace the current approach. Should I abandon [current work]
   > and pivot to [new request]? Or merge them instead?"
2. If confirmed:
   - **Save a record** of what was done (in case the user wants to revisit)
   - **Discard the current approach** cleanly (revert files if needed, or note
     what was changed)
   - **Start fresh** with the new request's requirements
3. If NOT confirmed:
   - Re-classify (likely MERGE or SEQUENTIAL)

**When to use:**
- "Actually, forget that — do this instead"
- "I changed my mind — let's go with approach B"
- User fundamentally redefines the scope or direction
- New request contradicts a core assumption of current work

---

#### PARK — Queue for Later

The new request is valid but lower priority than the current work. Acknowledge
it, record it, and continue with current work.

**Protocol:**

1. **Acknowledge immediately:**
   > "Noted. I'll queue this for after the current task."
2. **Record the parked request** (add to todo list or mention in response)
3. **Continue current work** without distraction
4. **Remind when current work completes:**
   > "Current task done. You also had a parked request: [summary]. Want me to
   > start on that?"

**When to use:**
- User mentions something tangential ("Oh, also remind me to fix X later")
- New request is a nice-to-have, not urgent
- Current work is complex and context-switching would be costly
- User says "when you get a chance" or "not urgent but..."

---

#### SPLIT — Decompose into Sub-Tasks

The new request partially overlaps with current work. Some parts can be done
independently; other parts depend on current work finishing.

**Protocol:**

1. **Decompose** the new request into sub-tasks
2. **Classify each sub-task:**
   - Independent of current work? → Execute now (in parallel or after a clean break)
   - Dependent on current work? → Queue as SEQUENTIAL
   - Overlapping with current work? → Handle as MERGE for that portion
3. **Show the decomposition:**

   ```text
   🔀 Split Analysis

   Independent (can start now):
     • [sub-task A] — no dependency on current work
     • [sub-task B] — standalone

   Dependent (queue after current work):
     • [sub-task C] — needs [current work output] first
     • [sub-task D] — overlaps with [current work step 3]

   Merged (folded into current work):
     • [sub-task E] — naturally fits into what's already in progress
   ```

4. **Execute** in the optimal order — independent parts first (or merged into
   current work), then dependent parts after current work completes

**When to use:**
- Large incoming request with mixed dependencies
- "Do X, Y, and Z" where X is independent, Y depends on current work, Z extends it
- Complex refactoring where some files can be changed independently

---

### Edge Cases & Advanced Patterns

#### Chained Steering

A single incoming request might trigger multiple steering types sequentially:

```text
Example: "Also add validation (MERGE), and after that, write tests (SEQUENTIAL)"
→ MERGE the validation into current work
→ SEQUENTIAL the test-writing after the merged work completes
```

#### Re-Classification Mid-Execution

If the steering type was wrong (e.g., classified as INDEPENDENT but turns out to
be related), re-classify and announce:

```text
📡 Re-steering: INDEPENDENT → MERGE
I initially treated this as independent, but it shares context with [current work].
Switching to MERGE strategy — running gap analysis now.
```

#### Multiple Parked Requests

When multiple requests are parked, maintain a priority queue:

```text
📋 Parked Requests (will process after current task)
1. [HIGH]   Fix the null check in OrderService
2. [MEDIUM] Add logging to PaymentGateway
3. [LOW]    Update the README with new endpoints
```

#### Implicit Steering Detection

Watch for implicit steering signals in user language:

| User says | Likely type |
|---|---|
| "also", "and also", "include" | MERGE |
| "after that", "once done", "then" | SEQUENTIAL |
| "actually", "forget that", "instead" | SUPERSEDE |
| "when you get a chance", "not urgent" | PARK |
| "oh and one more thing" (unrelated) | INDEPENDENT or PARK |
| "part of it needs X, rest needs Y" | SPLIT |

---

### Integration with Todo Tracking

When steering involves task management:

- **MERGE:** Update existing todos + add new ones; mark overlapping ones as augmented
- **SEQUENTIAL:** Add new todos after current ones; mark them as `not-started`
- **PARK:** Add parked todos with a `[PARKED]` prefix
- **SPLIT:** Create sub-todos grouped by dependency category
- **SUPERSEDE:** Mark current todos as cancelled; create new ones
- **INDEPENDENT:** Pause current todos; create fresh set

---

### Integration with Session Capture

When a steering decision changes the session's direction significantly:

- Log the steering event in the session's `scope-transitions` (if capturing)
- If SUPERSEDE or SPLIT creates a fundamentally different task, consider forking
  the session capture file
- MERGE events should be noted as scope-widening transitions

---

### Quick Reference Card

```text
INDEPENDENT .... Unrelated or no current work — handle directly
MERGE .......... Extends current work — union + gap analysis + combined deliverable
SEQUENTIAL ..... Finish current first — queue new request
SUPERSEDE ...... Replace current approach — confirm before pivoting
PARK ........... Low priority — acknowledge, queue, continue current work
SPLIT .......... Mixed deps — decompose, run independent now, queue dependent
```

```text
