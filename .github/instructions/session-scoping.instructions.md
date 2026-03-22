---
applyTo: "**"
---

# Session Scoping — Scope Management for Chat Sessions

> **Purpose:** Define a scoping mechanism for chat sessions that enables switching,
> widening, narrowing, and cross-referencing scopes within and across captured sessions.
>
> **When active:** Always — this instruction applies to every file via `applyTo: "**"`.

---

## Why Session Scoping Exists

Real conversations don't stay in a single box. A requirements gathering session for a
personal project might naturally evolve into general tech stack research. That research
might then narrow back down to project-specific implementation decisions. Without a scoping
mechanism, this evolution is **invisible** — the session metadata says "requirements" but
half the content is reusable general knowledge.

Session scoping solves three problems:

1. **Findability** — know which parts of a session are project-specific vs. reusable
2. **Reusability** — general learning captured during project work can be discovered later
3. **Traceability** — understand how one scope led to another (requirements → research → design)

---

## Scope Levels — The Hierarchy

Every chat session (or segment within a session) operates at one of three scope levels:

```text
global          Applies to any project, any context
   ▲               Examples: "Learn Java Streams", "Compare REST vs gRPC"
   │ widen
   │
project         Tied to a specific project bucket
   ▲               Examples: "ABSDevelopment tech stack", "learning-assistant vault design"
   │ widen
   │
feature         Tied to a specific feature within a project
                    Examples: "ABSDevelopment user-auth flow", "learning-assistant MCP scoping"
```

| Level | Scope | Reusability | Example |
|---|---|---|---|
| `global` | Not tied to any project | High — applies anywhere | "How does OAuth 2.0 work?" |
| `project` | Tied to a project | Medium — applies within that project | "ABSDevelopment OAuth integration options" |
| `feature` | Tied to a feature in a project | Low — specific to that feature | "ABSDevelopment login-page OAuth button placement" |

---

## Scope Properties in Frontmatter

Every captured session includes these scope fields in its YAML frontmatter:

```yaml
scope: global | project | feature
scope-project: null | project-name          # required when scope = project or feature
scope-feature: null | feature-slug          # required when scope = feature
scope-transitions: []                       # log of scope changes during session
scope-refs: []                              # cross-references to related sessions
```

### Field Reference

| Field | Required | Values | Purpose |
|---|---|---|---|
| `scope` | Yes | `global`, `project`, `feature` | Current (or dominant) scope level |
| `scope-project` | Conditional | `null` or kebab-case | Project bucket — required when scope is `project` or `feature` |
| `scope-feature` | Conditional | `null` or kebab-case | Feature slug — required when scope is `feature` |
| `scope-transitions` | Yes | List (can be empty) | Chronological log of scope changes during the session |
| `scope-refs` | Yes | List (can be empty) | Cross-references to sessions at different scopes |

### Scope Transition Entry Format

```yaml
scope-transitions:
  - at: "Exchange 3"
    from: "feature:abs-development/mvp-requirements"
    to: "project:abs-development"
    reason: "shifted from feature scoping to tech stack research for the whole project"
  - at: "Exchange 6"
    from: "project:abs-development"
    to: "global"
    reason: "tech stack comparison is general knowledge, not project-specific"
```

### Scope Reference Entry Format

```yaml
scope-refs:
  - file: "personal/software-dev/requirements/2026-03-20_02-15pm_requirements_abs-mvp-scope.md"
    relationship: origin
    note: "requirements session that spawned this research"
  - file: "personal/learning/2026-03-20_04-00pm_learning_spring-boot-deep-dive.md"
    relationship: spawned
    note: "general learning session born from project research"
```

---

## Scope Operations

### Widen (Increase Scope)

**Direction:** feature → project → global

**When to widen:**

- The conversation shifts from a specific feature to broader project concerns
- Research that started as project-specific becomes generally applicable
- You realise the knowledge gained is reusable beyond the current project

**Protocol:**

1. Note the exchange number where the shift occurs
2. Log a transition entry in `scope-transitions`
3. Update the `scope` field to the new (wider) level
4. If the session is being captured as a single file, annotate the exchange boundary
5. If the widened portion is substantial (3+ exchanges), consider splitting (see Fork/Split)

**Example narrative:**

> "I started researching auth flows for ABSDevelopment (feature scope: login-page),
> but the OAuth 2.0 comparison is useful for any project. Widening to global."

### Narrow (Decrease Scope)

**Direction:** global → project → feature

**When to narrow:**

- General research needs to be applied to a specific project
- A project-level discussion drills into a specific feature
- You want to constrain the conversation to a more focused context

**Protocol:**

1. Note the exchange number where the shift occurs
2. Log a transition entry in `scope-transitions`
3. Update `scope`, `scope-project`, and/or `scope-feature`
4. If narrowing creates a new project/feature context, populate those fields

**Example narrative:**

> "The general Spring Boot research is great, but now I need to decide specifically
> how ABSDevelopment will use it. Narrowing to project scope."

### Switch (Jump to Different Scope)

**Direction:** Any → any (different project or unrelated scope)

**When to switch:**

- Moving from one project's context to a completely different one
- Shifting between unrelated topics within the same session
- Context switch between work and personal domains

**Protocol:**

1. Log the transition with both the `from` and `to` scope identifiers
2. Consider whether a session split is appropriate (different projects usually warrant it)
3. If staying in the same file, use an H2 heading to clearly delineate the switch

**Example narrative:**

> "Done with ABSDevelopment requirements. Now switching to research on MCP transport
> for the learning-assistant project."

### Fork / Split (Branch Off a Separate Session)

**Direction:** Current session → new session file at a different scope

**When to fork:**

- A scope segment becomes substantial (3+ exchanges with analytical depth)
- The widened/narrowed content deserves its own discoverable session file
- Different scopes would belong in different category folders

**Protocol:**

1. Create a new session file for the forked scope segment
2. Add `scope-refs` entries in **both** files pointing to each other
3. In the original file, note where the fork happened
4. The new file's `scope-refs` should list the original as `origin`
5. The original file's `scope-refs` should list the new file as `spawned`

**Example:**

```text
Original:  personal/software-dev/requirements/2026-03-20_02-15pm_requirements_abs-mvp-scope.md
           scope: feature, scope-project: abs-development, scope-feature: mvp-scope

Forked:    personal/software-dev/research/2026-03-20_03-00pm_research_spring-vs-quarkus.md
           scope: global (widened from project research)
           scope-refs: [{ file: "../requirements/2026-03-20_...", relationship: origin }]
```

---

## Cross-Reference Relationships

Sessions at different scopes can reference each other using these relationship types:

| Relationship | Meaning | Direction |
|---|---|---|
| `origin` | This session was born from the referenced session | Child → Parent |
| `spawned` | The referenced session was born from this one | Parent → Child |
| `narrows` | This session narrows the scope of the referenced session | Narrow → Wide |
| `widens` | This session widens the scope of the referenced session | Wide → Narrow |
| `related` | Topically related, different scope — no parent/child relationship | Sibling |
| `continuation` | Direct continuation of the same scope (prefer `parent` field instead) | Sequential |
| `implements` | This session implements decisions/requirements from the referenced session | Dev → Req |

### Bidirectional Rule

Cross-references should be **bidirectional**. When you add a `scope-refs` entry in file A
pointing to file B, add a corresponding entry in file B pointing back to file A.

| File A says | File B says |
|---|---|
| `relationship: origin` | `relationship: spawned` |
| `relationship: narrows` | `relationship: widens` |
| `relationship: spawned` | `relationship: origin` |
| `relationship: widens` | `relationship: narrows` |
| `relationship: related` | `relationship: related` |
| `relationship: implements` | `relationship: related` (or add `implemented-by`) |

---

## Scope in Session Content Structure

When a session contains scope transitions, annotate them in the session body:

### Single-Scope Session (no transitions)

Standard session structure — no special annotations needed.

### Multi-Scope Session (has transitions)

Use H2 scope boundary markers between exchanges:

```markdown
## Exchange 1 — Requirements for ABSDevelopment Login (feature scope)

### Request

...

### Response

...

---

> **Scope transition:** feature → project
> Shifted from login-page specifics to ABSDevelopment-wide tech stack research.

## Exchange 2 — ABSDevelopment Tech Stack Options (project scope)

### Request

...

### Response

...

---

> **Scope transition:** project → global
> The Spring Boot vs Quarkus comparison is general knowledge — not ABSDevelopment-specific.

## Exchange 3 — Spring Boot vs Quarkus Comparison (global scope)

### Request

...

### Response

...
```

---

## Determining the Dominant Scope

A session's top-level `scope` field should reflect the **dominant** scope — the level
where the majority of substantive content operates:

| Scenario | Dominant scope |
|---|---|
| 5 exchanges at feature, 1 at project | `feature` |
| 2 exchanges at project, 4 at global | `global` |
| Equal split between project and global | Use the **final** scope |
| Single exchange at each level | Use the **most specific** (narrowest) scope |

When the dominant scope is ambiguous, prefer the scope that best describes how you would
search for and reuse this session later.

---

## Scope and Category Interaction

Scope is **orthogonal** to category. Any category can operate at any scope level:

| Category | Global example | Project example | Feature example |
|---|---|---|---|
| `requirements` | "How to write good user stories" | "ABSDevelopment MVP requirements" | "ABSDevelopment login-page stories" |
| `research` | "Compare REST vs gRPC" | "ABSDevelopment API protocol choice" | "ABSDevelopment auth-endpoint protocol" |
| `learning` | "Java virtual threads deep-dive" | "Concurrency patterns for ABSDevelopment" | "Thread pool for ABSDevelopment batch-import" |

Scope does NOT change the category folder. A global research session and a project-scoped
research session both go in `research/`. Scope is metadata, not a folder dimension.

---

## Practical Workflow — The User's Scenario

Here is how the scoping mechanism handles a real-world flow:

### Step 1 — Requirements Gathering (feature scope)

```text
User: "Let's scope the MVP for ABSDevelopment — focus on the user management feature."
```

Session created:

```yaml
scope: feature
scope-project: abs-development
scope-feature: user-management-mvp
category: requirements
```

### Step 2 — Widening to Tech Stack Research (project scope)

```text
User: "Now let's research what tech stack to use for ABSDevelopment."
```

Two options:

**Option A — Stay in same session:**

```yaml
scope: project              # updated from feature
scope-project: abs-development
scope-feature: null         # cleared
scope-transitions:
  - at: "Exchange 4"
    from: "feature:abs-development/user-management-mvp"
    to: "project:abs-development"
    reason: "shifted to tech stack research for the whole project"
```

**Option B — Fork a new session:**

New file at `personal/software-dev/research/2026-03-20_..._research_abs-tech-stack.md`
with `scope-refs` linking back to the requirements session.

### Step 3 — Widening to General Learning (global scope)

```text
User: "This Spring Boot vs Quarkus comparison is useful beyond ABSDevelopment."
```

```yaml
scope: global               # updated from project
scope-project: null          # cleared (or kept as context)
scope-feature: null
scope-transitions:
  - at: "Exchange 4"
    from: "feature:abs-development/user-management-mvp"
    to: "project:abs-development"
    reason: "shifted to tech stack research"
  - at: "Exchange 7"
    from: "project:abs-development"
    to: "global"
    reason: "framework comparison is general knowledge"
```

### Step 4 — Cross-Referencing

The requirements session and the global learning session each get `scope-refs`:

Requirements file:

```yaml
scope-refs:
  - file: "personal/software-dev/research/2026-03-20_research_spring-vs-quarkus.md"
    relationship: spawned
    note: "tech stack research that grew out of this requirements session"
```

Research file:

```yaml
scope-refs:
  - file: "personal/software-dev/requirements/2026-03-20_requirements_abs-mvp-scope.md"
    relationship: origin
    note: "started as ABSDevelopment requirements, widened to general research"
```

---

## User Commands for Scope Management

| Command | Effect |
|---|---|
| `/session-scope status` | Show current scope level, project, feature, and any transitions |
| `/session-scope widen` | Increase scope: feature → project → global |
| `/session-scope narrow <project> [feature]` | Decrease scope to a project or feature |
| `/session-scope switch <project> [feature]` | Jump to a different project/feature context |
| `/session-scope split` | Fork the current scope segment into its own session file |
| `/session-scope link <file>` | Add a manual cross-reference to another session |
| `/session-scope history` | Show scope transition history for the current session |

These commands can be used mid-conversation to explicitly signal scope changes.
The AI assistant should also **detect implicit scope changes** and suggest transitions
when the conversation naturally shifts scope (e.g., from project-specific to general).

---

## Implicit Scope Detection

The AI assistant should watch for signals that the scope is shifting:

### Widening Signals

- User says "this applies to any project" or "this is general knowledge"
- Discussion moves from specific implementation to abstract concepts
- Comparisons become framework-level rather than project-level
- The word "in general" or "regardless of project" appears

### Narrowing Signals

- User says "for ABSDevelopment specifically" or "in this project"
- Discussion moves from abstract concepts to specific implementation
- Concrete project names, file paths, or feature names are mentioned
- The word "specifically" or "in our case" appears

### Switch Signals

- User says "now let's talk about X" where X is a different project/topic
- A clear topic break with no connection to the prior scope
- Domain switch (work → personal or vice versa)

### Protocol for Implicit Detection

When a scope shift is detected:

1. **Acknowledge** — "It looks like we've shifted from [old scope] to [new scope]."
2. **Confirm** — "Should I update the session scope?"
3. **Act** — If confirmed, log the transition and update the scope fields.

Do NOT silently change scope. Always surface the change so the user can confirm or correct.

---

## Activity-Phase Transitions (software-dev sessions)

Beyond scope level (global/project/feature), `software-dev` sessions also track
**activity phase** — the kind of work being done. Activity transitions happen
frequently within project-scoped sessions.

### Activity Transition Logging

Activity transitions are logged alongside scope transitions in `scope-transitions`:

```yaml
scope-transitions:
  - at: "Exchange 3"
    from: "feature:task-manager/mvp-requirements"
    to: "feature:task-manager/mvp-requirements"
    activity-from: "requirements"
    activity-to: "design"
    reason: "shifted from scoping to API design within the same feature"
```

### Activity Detection Signals

| Signal | Activity detected |
|---|---|
| "what should we build", "user stories", "acceptance criteria" | requirements |
| "what does the competitor do", "is there demand", "what tech to use" | research |
| "how should we design", "architecture", "API endpoints", "schema" | design |
| "let's code this", "implement", "build the feature" | implementation |
| "write tests", "test strategy", "TDD" | testing |
| "review the code", "refactor" | code-review |
| "deploy", "CI/CD", "Docker", "infrastructure" | devops |

### When to Fork vs. Continue

| Situation | Action |
|---|---|
| Brief tangent (1-2 exchanges) then back | Continue in same file, log transition |
| Extended work in new activity (3+ exchanges) | Fork to new file, cross-reference both |
| Different project entirely | Always fork — different scope |
| Same project, recurring back-and-forth | Continue in same file, frequent transitions expected |

---

## Scope Defaults

| Situation | Default scope |
|---|---|
| No scope specified by user | Infer from context (project name → project, feature name → feature, abstract topic → global) |
| Ambiguous context | Default to `project` if a project is mentioned, otherwise `global` |
| New session started without context | `global` |
| Continuing from a previous session | Inherit the previous session's scope |
