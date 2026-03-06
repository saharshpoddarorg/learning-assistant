# Prompt Composition — Combining Copilot Commands for Complex Workflows

> **Purpose:** A guide to composing, chaining, and layering Copilot prompts for tasks
> that require more than one command. Covers sequential chains, parallel prompts,
> meta-prompts, and real workflow recipes from this repo.

---

## Tier 1 — Newbie: What is Prompt Composition?

A **single prompt** handles one well-defined task. **Prompt composition** is chaining
or layering multiple prompts so the output of one feeds the input of another — or so
multiple prompts work together to cover a complex workflow.

Think of prompts like LEGO bricks. Each brick is useful alone. Snapping them together
builds something bigger.

### Simple vs Composed

| Simple (single prompt) | Composed (chained prompts) |
|---|---|
| `/brain-new` — create a note | `/brain-capture-session` → `/brain-new` → `/brain-search` |
| `/debug` — investigate a bug | `/debug` → `/impact` → `/refactor` |
| `/resources` — search resources | `/deep-dive` → `/resources` → `/brain-new` |
| `/explain` — explain a file | `/explain` → `/design-review` → `/write-docs` |

### The Simplest Composition — Sequential Chain

Type one command. After it finishes, type the next:

```
Step 1: /deep-dive → Java streams → advanced
Step 2: /resources → search → "Java streams" (add the best resource to your notes)
Step 3: /brain-new → "Java streams cheatsheet" → notes → java
```

---

## Tier 2 — Amateur: Types of Composition

### Type 1 — Sequential Chain (most common)

Output of prompt A feeds input of prompt B. You type them one after another:

```
A — /learn-concept binary-tree → explanation
B — /dsa → binary-tree → implement → java → medium   (deepens the learning)
C — /brain-new → "binary tree patterns" → notes       (saves the learning)
```

**When to use:** When each step depends on the previous one completing.

### Type 2 — Parallel Prompts (run together, compare outputs)

Run two commands in the same message (or session) for comparison:

```
"Using /design-review principles, review [FileA] and [FileB] and compare which is more SOLID-compliant"
```

Or: open two Copilot chat tabs and run different prompts on the same file to compare approaches.

**When to use:** When you want two different analyses of the same input.

### Type 3 — Layered / Nested (meta-prompts that call sub-prompts)

A meta-prompt orchestrates other prompts implicitly. Examples from this repo:

- `/hub` — returns the navigation tree; you then invoke sub-commands from it
- `/composite` — explicitly chains multiple task-prompts in one request
- `/write-docs all-of-above` — internally creates brain-note + dev-doc + cheatsheet + skill + prompt in one command
- `/copilot-customization plan-composition` — recommends which customization types to combine

**When to use:** When a single workflow produces multiple artifacts.

### Type 4 — Conditional (if/then composition)

Start with a diagnostic prompt, then branch:

```
/check-standards → brain/ai-brain/notes/
        │
        ├── Issues found → fix files → /check-standards again (iteration)
        └── All passing  → /brain-new to document the standards audit result
```

Another example:
```
/design-review → FileA
        │
        ├── SOLID violations found → /refactor FileA
        └── Clean                 → move on
```

### Type 5 — Meta-Prompt Override (context + task)

Use `/context` or `/scope` to set constraints, then run a task prompt:

```
/scope specific FileA.java    ← constrains context to one file
/debug                        ← now runs with that scope active
```

Or:
```
/context → "We are in the middle of refactoring the MCP server registry. 
  All edits should leave existing tools working. Do not rename public methods."
/refactor src/server/McpServerRegistry.java    ← runs with that context active
```

---

## Tier 3 — Pro: Composition Recipes & Patterns

### Recipe 1 — Full Learning Workflow

For any new concept you want to deeply learn and retain:

```
Step 1: /deep-dive → <concept> → advanced
        (3-tier conceptual explanation: what, why, how)

Step 2: /dsa (if algorithm) or /learn-concept (if broader concept)
        (deeper exploration with examples)

Step 3: /resources → search → <concept>
        (find curated learning resources for further reading)

Step 4: /brain-new → "<concept> notes" → notes → <project>
        (save your distilled learning as a notes/ file)

Step 5 (optional): /brain-publish → inbox version → library/<project>
        (archive any external references you imported)
```

**Total prompts:** 3–5 | **Output:** A permanent brain note + curated resources

---

### Recipe 2 — Bug Investigation & Fix Workflow

```
Step 1: /debug → <file or description>
        (systematic hypothesis-driven investigation)

Step 2: /impact → <the line/method you're about to change>
        (understand ripple effects before editing)

Step 3: Make the fix (direct agent editing or /refactor)

Step 4: /design-review → <changed file>
        (verify the fix didn't introduce design issues)

Step 5: /brain-new → "session-<bug-slug>" → notes → kind: session
        (record what you found and fixed)
```

**Total prompts:** 4–5 | **Output:** Fix + impact analysis + session note

---

### Recipe 3 — Architecture Decision Workflow

```
Step 1: /system-design → <problem> → HLD
        (explore high-level options)

Step 2: /design-review → <any existing related code>
        (understand current design before deciding)

Step 3: Discuss trade-offs in chat (no prompt needed)

Step 4: /brain-new → "decision-<topic>" → notes → kind: decision
        (write the ADR: Context / Decision / Consequences)

Step 5: /write-docs dev-doc → <your decision note>
        (turn it into a formal 3-tier developer doc if it's important)
```

**Total prompts:** 3–4 | **Output:** ADR note + optional dev doc

---

### Recipe 4 — Knowledge Sharing Session Processing

Given a set of slides/notes from a KS session:

```
Step 1: brain publish <inbox-file> --project <session-name>
        (archive the raw source to library/)
        [Repeat for each imported file]

Step 2: /deep-dive → <key concept from the session>
        (deepen your understanding of the most important topic)

Step 3: /brain-new → "<session-name>-distilled" → notes
        (write your own synthesis: what you learned, key takeaways, action items)

Step 4: /check-standards → brain/ai-brain/notes/ → brain-naming
        (verify all new files follow naming conventions)
```

**Total prompts:** 2–3 | **Output:** Archived sources + your personal distillation note

---

### Recipe 5 — MCP-to-Skill Migration

```
Step 1: /mcp-to-skill → <server-name> → analyse
        (feasibility report: which tools can migrate)

Step 2: /mcp-to-skill → <server-name> → generate → .github/skills/<name>/SKILL.md
        (produce the SKILL.md draft)

Step 3: /check-standards → .github/skills/<name>/SKILL.md → skill-file
        (verify the generated skill file meets standards)

Step 4: /write-docs prompt → <skill content>
        (create a companion slash command if useful)

Step 5: /hub (verify new skill appears in navigation)
```

**Total prompts:** 4–5 | **Output:** New skill file + optional prompt + verification

---

### Recipe 6 — New Feature Full-Stack Documentation

When adding a new feature to the repo (MCP tool, skill, prompt, etc.):

```
/write-docs all-of-above → <feature description>
```

This single command (with `all-of-above` mode) generates:
1. Brain session note (records what you did)
2. Dev doc (3-tier reference)
3. Cheatsheet (fast-reference tables)
4. SKILL.md (if domain knowledge)
5. Slash command prompt (if workflow command)
6. README update

Then manually:
- Update `slash-commands.md` count and entry
- Update `hub.prompt.md` navigation tree
- Update `copilot-instructions.md` skills block if new skill added
- Rebuild: `.\mcp-servers\build.ps1` if any Java changed

---

## Meta-Prompts Reference

Meta-prompts modify HOW Copilot works rather than WHAT it produces:

| Meta-prompt | Effect | Best combined with |
|---|---|---|
| `/scope specific <file>` | Constrains edits to one file | Any editing prompt |
| `/scope targeted <list>` | Constrains edits to a set of files | Multi-file refactors |
| `/context <statement>` | Adds persistent context for this session | Any task prompt |
| `/composite <prompt list>` | Chains multiple task prompts explicitly | Complex workflows |
| `/hub` | Navigation — shows what's available | Before any domain prompt |
| `/steer completeness` | Activates completeness mode | Before any change |
| `/check-standards` | Audit before/after any change | After editing files |

### The /composite Pattern

```
/composite
→ prompts: ["/deep-dive java-streams advanced", "/brain-new java-streams-cheatsheet notes"]
→ execute: sequential
→ confirm-between: yes
```

Or in plain language: "After explaining Java streams, save my key takeaways as a brain note."

---

## Composition Anti-Patterns

| Anti-pattern | Problem | Fix |
|---|---|---|
| Too many steps in one message | Copilot loses track / times out | Max 3 numbered items per message |
| Chaining without saving context | Results lost when session ends | Use /brain-new or /brain-capture-session |
| Running /debug + /refactor simultaneously | Investigating ≠ fixing — do in order | Debug first, then fix |
| Using /write-docs before /design-review | Writing docs for a bad design | Review design first |
| Skipping /check-standards after bulk edits | Inconsistencies creep in | Always end a bulk edit session with standards check |
| Composing without a goal | Prompting for its own sake | Define the end artifact before starting |

---

## Quick Reference — Composition Patterns by Task

| Task | Recommended composition |
|---|---|
| Learn a new concept deeply | `/deep-dive` → `/resources` → `/brain-new` |
| Fix a bug safely | `/debug` → `/impact` → fix → `/design-review` |
| Process KS session materials | `brain publish` × N → `/brain-new` (your distillation) |
| Create new feature with full docs | Change code → `/write-docs all-of-above` |
| Refactor a file | `/design-review` → `/refactor` → `/check-standards java-code` |
| Migrate MCP tool to skill | `/mcp-to-skill analyse` → `/mcp-to-skill generate` → `/check-standards skill-file` |
| Standards audit | `/check-standards all` → fix issues → `/check-standards all` (verify) |
| New architecture decision | `/system-design` → `/design-review` → `/brain-new decision-...` |
