# Copilot Customization — Deep Dive Reference

> **The complete guide to all GitHub Copilot customization primitives — differences, similarities,
> when to use each, and how to compose them into powerful layered workflows.**

| Audience | Start Here |
|---|---|
| 🟢 **Newbie** | [Part 1 — The 6 Primitives](#part-1-the-6-primitives) → [Decision Guide](#part-3-decision-guide) |
| 🟡 **Amateur** | [Comparison Table](#part-2-head-to-head-comparison) → [Composition Basics](#same-type-composition) |
| 🔴 **Pro** | [Full Stack Compositions](#cross-type-composition) → [Anti-Patterns](#part-5-anti-patterns) |

---

## Table of Contents

1. [The 6 Primitives](#part-1-the-6-primitives)
2. [Head-to-Head Comparison](#part-2-head-to-head-comparison)
3. [Decision Guide — Which to Use When](#part-3-decision-guide)
4. [Composition Patterns](#part-4-composition-patterns)
5. [Anti-Patterns](#part-5-anti-patterns)
6. [Quick Reference Card](#part-6-quick-reference-card)

---

## Part 1: The 6 Primitives

### 1 — `copilot-instructions.md` — The Foundation

**Location:** `.github/copilot-instructions.md` (exactly one file)
**Analogy:** *Employee handbook* — every new hire (every Copilot session) reads it first.

The single root file that defines who you are as a team and what your standards are.
Loaded **automatically on every request**, regardless of what file you're editing or which agent
you've selected. It is always the lowest-priority layer — everything else stacks on top.

**Best for:**
- Project-wide naming conventions (classes, methods, variables, constants)
- Default language and style (Java 21, `var`, `final`, Javadoc)
- Commit message format, branching strategy, team vocabulary
- High-level "always/never" rules that apply everywhere

**Format:**

```markdown
# Project Instructions

## Naming
- Classes: UpperCamelCase
- Methods: lowerCamelCase
...
```

No frontmatter needed — VS Code recognizes the filename.

**What it does NOT do:** It does not activate specific tools, it is not a persona, and it cannot
be selectively active for some files only. Size matters — keep it <2 000 tokens since it's
injected into every request.

---

### 2 — `.instructions.md` — The Specialists

**Location:** `.github/instructions/*.instructions.md` (can be many files)
**Analogy:** *Department-specific HR policy supplement* — Marketing follows the handbook AND the
Marketing policy; Engineering follows the handbook AND the Engineering policy.

Path-scoped rule files that stack on top of `copilot-instructions.md`. They activate automatically
when the file you're editing matches their `applyTo` glob. Multiple instruction files can match the
same file — they ALL stack.

**Key frontmatter field:**

```yaml
---
applyTo: "**/*.java"     # Glob pattern — required
---
```

**Best for:**
- Java-specific naming, Javadoc style, modifier order
- React/TypeScript component patterns
- Test file conventions (`@Test`, naming, structure)
- Build-file-specific rules (pom.xml, Dockerfile)
- Steering mode files (e.g., `change-completeness.instructions.md` with `applyTo: "**"`)

**Stacking example:** For a `*.java` file, all of these apply simultaneously:

```
copilot-instructions.md     ← always
    + java.instructions.md      ← applyTo: **/*.java
    + clean-code.instructions.md ← applyTo: **/*.java
    = Combined Java coding standards
```

---

### 3 — `.prompt.md` — The Shortcuts

**Location:** `.github/prompts/*.prompt.md` (can be many files)
**Analogy:** *Standard Operating Procedure (SOP)* — instead of re-explaining the full review
process every time, you say "run the review SOP" and everyone knows what to do.

Slash-command templates that define **pre-built workflows**. Users trigger them by typing
`/command-name` in Copilot Chat. Prompts are **not loaded automatically** — they're only
active when explicitly invoked.

**Key frontmatter fields:**

```yaml
---
name: design-review
description: 'Run a SOLID/GRASP architecture review on the current codebase'
agent: designer
tools: ['codebase', 'search', 'usages']
---
```

**Powerful features:**
- `${input:varName:Prompt for the user?}` — collects user input at invocation time
- `#file:path/to/file` — attaches a file to the prompt context
- `tools:` list — restricts which tools are available during this prompt's session
- `agent:` — can auto-select the right agent persona for the task

**Best for:**
- Multi-step repeatable workflows (design review, debugging session, code explanation)
- Onboarding workflows ("run `/explore-project` to understand the codebase")
- Session management (`/context`, `/scope`)
- Standardizing team review processes

---

### 4 — `.agent.md` — The Personas

**Location:** `.github/agents/*.agent.md` (can be many files)
**Analogy:** *Job title and role description* — when you hire a security auditor vs. a UX
designer, each brings a completely different mindset, vocabulary, and approach to the same problem.

Agent files define AI **personas** — specialized roles that Copilot can adopt. The user selects
from a dropdown in VS Code Chat. When active, the agent's system prompt, tool restrictions,
and handoff chain all come into effect.

**Key frontmatter fields:**

```yaml
---
description: >
  A security reviewer. Use when asked to audit for OWASP, review auth,
  or check for injection vulnerabilities.
tools:
  - codebase
  - search
  - usages
model: gpt-4o          # Optional — pin to specific model (new in 2026)
---
```

**Powerful features (2026):**
- `model:` field — pin the agent to a specific model (Claude Sonnet, GPT-4o, o3-mini)
- `handoffs:` — define which agents this one can pass work to
- Tool restriction via `tools:` list — security-critical agents can be made read-only
- The system prompt (the Markdown body) is the agent's persona instruction

**Best for:**
- Specialist review sessions (security, performance, accessibility, architecture)
- Restricting tools for safety (read-only code reviewer, no terminal access)
- Consistent persona for recurring complex workflows
- Teaching and mentoring roles (Socratic method, progressive disclosure)

---

### 5 — `SKILL.md` — The Knowledge Base

**Location:** `.github/skills/<domain>/SKILL.md` (one per domain)
**Analogy:** *Domain reference manual* — not rules about HOW to write code, but deep knowledge
ABOUT a domain that Copilot can draw on when answering questions.

Skills are VS Code's "intelligent context injection" mechanism. Unlike instructions (behavioral
directives), skills are **informational reference** — cheatsheets, architecture notes, curated
resources, command references, recipes. Copilot infers when to activate a skill by semantically
matching your question to the skill's `description` field.

**Key frontmatter fields:**

```yaml
---
name: mcp-development
description: >
  Comprehensive guide to MCP (Model Context Protocol)... Use when asked about MCP,
  building MCP servers, configuring AI agents with tools...
---
```

**Critical:** The `description` field is the **activation trigger**. If it's too vague,
the skill never activates. If it's too specific, it misses relevant questions.
Write it as: *"Use when asked about [TOPIC 1], [TOPIC 2], [USE CASE]..."*

**Best for:**
- Domain cheatsheets (MCP, Git, Java, build tools)
- Architecture reference (how your MCP servers work)
- Curated learning resources that Copilot should know about
- "Second brain" content — things you'd otherwise have to explain every time

**What it is NOT:** Skills are not behavioral (they don't say "always do X"). They are
informational. Use instructions for behavioral rules, skills for knowledge.

---

### 6 — MCP Servers — The External Hands

**Location:** `.vscode/mcp.json` (config) + external server code
**Analogy:** *Physical workbench tools* — Copilot can know everything about Git from a skill file,
but MCP servers are what let it actually **do things** with Git (create PRs, push commits, etc.).

MCP (Model Context Protocol) servers are **external processes** that Copilot's agent mode can
call as tools. They run as separate processes, communicate via JSON-RPC, and can do anything
an external API or database can do — read files, query databases, search GitHub, update Jira.

**Config (`.vscode/mcp.json`):**

```jsonc
{
  "inputs": [
    { "id": "githubToken", "type": "promptString", "password": true, "description": "GitHub PAT" }
  ],
  "servers": {
    "github": {
      "type": "stdio",
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": { "GITHUB_TOKEN": "${input:githubToken}" }
    }
  }
}
```

**What is unique about MCP servers compared to the other 5 primitives:**
- They run as **separate processes** (not just text files loaded into context)
- They can perform **real actions** (write to DB, call APIs, push to Git)
- They are **always registered** (Copilot agent sees all tools from all servers)
- They are **protocol-based** — any language (TypeScript, Python, Java, Go) can implement one
- Open Preview (March 2026): Config key = `servers` (not `mcpServers`), Streamable HTTP replaces SSE

**Best for:**
- Reading/writing real data (GitHub issues, Jira tickets, databases)
- Any operation that needs network access or file writes
- Wrapping existing internal APIs as AI-callable tools
- Workflow automation at the OS/system level

---

## Part 2: Head-to-Head Comparison

### Mega Comparison Table

| Dimension | `copilot-instructions.md` | `.instructions.md` | `.prompt.md` | `.agent.md` | `SKILL.md` | MCP Server |
|---|---|---|---|---|---|---|
| **Count in project** | Exactly 1 | Many | Many | Many | 1 per domain | Many |
| **Activation** | Always-on (automatic) | Automatic (glob match) | Manual (`/cmd`) | Manual (dropdown) | Automatic (semantic) | Always-on (registered) |
| **Who triggers it** | Nobody — always on | VS Code (file match) | User (types `/`) | User (picks dropdown) | Copilot (AI inference) | Copilot agent (calls tool) |
| **Scope** | Entire project, always | Specific file types | Per invocation | Per session | Per domain topic | Per registered server |
| **Purpose** | Project-wide ground rules | File-type-specific rules | Reusable task workflow | AI persona/role | Domain knowledge cache | External tool/data access |
| **Content nature** | Behavioral directives | Behavioral directives | Workflow instructions + variables | Persona instructions | Informational reference | Tool schemas + external APIs |
| **What it changes** | How Copilot always writes | How Copilot writes matching files | What Copilot does for this task | How Copilot thinks (persona) | What Copilot knows | What Copilot can DO |
| **Stacks with others** | Yes — always base layer | Yes — multiple files can apply | No — one prompt at a time | No — one agent at a time | Yes — multiple activate | Yes — all servers active |
| **YAML frontmatter** | None (filename is config) | `applyTo: "glob"` | `name`, `description`, `agent`, `tools` | `description`, `tools`, `model` | `name`, `description` | N/A (JSON config) |
| **Has `model:` field** | No | No | No | ✅ Yes (2026) | No | N/A |
| **Variable injection** | No | No | ✅ `${input:var}` | No | No | Via `${input:}` in mcp.json |
| **File references** | No | No | ✅ `#file:` | No | No | No |
| **Personality/persona** | No | No | Partial (via `agent:` field) | ✅ Yes (core purpose) | No | No |
| **Size limit guidance** | <2 000 tokens | <500 tokens | No hard limit | No hard limit | No hard limit | N/A |
| **Affects Chat modes** | Ask, Agent, Edit, Inline | Ask, Agent, Edit, Inline | Ask, Agent | Ask, Agent | Ask, Agent | Agent mode only |
| **Skills read by Copilot** | ✅ Always | ✅ When active | ✅ When invoked | ✅ When active | ✅ When relevant | ✅ When in agent mode |
| **Needs server setup** | No | No | No | No | No | ✅ Yes (external process) |
| **Real-world analogy** | Employee handbook | Dept. policy supplement | SOP / playbook | Job title & role card | Reference manual | Physical tools at workbench |

### Similarities

All 6 primitives share these properties:

1. **They all live in the repo** (except MCP server code which is optional) — customizations travel with your project
2. **They all affect Copilot's quality for the domain they cover** — all improve relevance
3. **They all use Markdown** (partially or fully) for the content layer
4. **They all are invisible at runtime** — users get better Copilot responses without explicitly invoking context
5. **They can all coexist simultaneously** — there is NO "choose one" — all 6 can be active at once
6. **Changes take effect immediately** — no restart required, no build step
7. **They support 3-tier content** — you can write different depth sections for different audiences

### Key Differences

| | Behavioral vs Informational | Automatic vs Manual | Scope: Project vs File vs Topic vs Session |
|---|---|---|---|
| `copilot-instructions.md` | **Behavioral** | Automatic | Project-wide |
| `.instructions.md` | **Behavioral** | Automatic | File-type-scoped |
| `.prompt.md` | **Workflow** | Manual | Per-invocation |
| `.agent.md` | **Behavioral + Persona** | Manual | Session-scoped |
| `SKILL.md` | **Informational** | Automatic | Topic-scoped |
| MCP Server | **Tool/Action** | Automatic (in agent mode) | Service-scoped |

---

## Part 3: Decision Guide — Which to Use When

### "I want to..." Decision Tree

```
I want to...
│
├── ...enforce rules consistently → BEHAVIORAL type
│   │
│   ├── ...for the entire project, always
│   │   └── ✅ copilot-instructions.md
│   │
│   ├── ...only for specific file types (Java, TS, tests...)
│   │   └── ✅ .instructions.md (with applyTo glob)
│   │
│   └── ...give Copilot a specific expert persona/mindset
│       └── ✅ .agent.md
│
├── ...provide knowledge/reference material → INFORMATIONAL type
│   │
│   ├── ...that Copilot should use answer domain questions
│   │   └── ✅ SKILL.md
│   │
│   └── ...that Copilot should use to access external data
│       └── ✅ MCP Server (+ SKILL.md for context about it)
│
└── ...create a reusable workflow → WORKFLOW type
    └── ✅ .prompt.md
```

### Quick Selection Matrix

| Use Case | Best Choice | Why |
|---|---|---|
| Define Java naming conventions | `.instructions.md` `applyTo: **/*.java` | File-scoped — won't pollute Python or JS sessions |
| Project-wide commit message format | `copilot-instructions.md` | Applies everywhere, non-negotiable |
| "Run a security audit on this class" | `.prompt.md` + `.agent.md` | Prompt = workflow, Agent = security mindset |
| Deep knowledge about our architecture | `SKILL.md` | Informational, auto-loads on relevant questions |
| Let Copilot create GitHub PRs | MCP Server (`@modelcontextprotocol/server-github`) | Only MCP can call external APIs |
| Standardize new-hire onboarding queries | `.prompt.md` | `/onboarding` slash command = repeatable workflow |
| Force "read-only" mode for some Copilot sessions | `.agent.md` (tools list without `editFiles`) | Agents control tool access |
| Add terminology / project vocabulary | `copilot-instructions.md` | Needs to be always available |
| Domain cheatsheet (Git commands, MCP patterns) | `SKILL.md` | Large reference docs belong here, not in instructions |
| Multi-step design review → impact → code-review | `.prompt.md` + agent handoffs | Workflow + specialist chain |
| Give Copilot access to our Jira board | MCP Server (Atlassian MCP) | Only MCP can read external services |
| Restrict Copilot from running terminal commands | `.agent.md` (no `runCommands` in tools) | Tool list in agent frontmatter |

### Common Confusions

**"Should this go in `copilot-instructions.md` or a `SKILL.md`?"**
→ Ask: *Is it a RULE (behavioral) or KNOWLEDGE (informational)?*
- "Always use `var` for local variables" → **instruction** (rule)
- "Here's a reference of all Maven lifecycle phases" → **skill** (knowledge)

**"Should this be an `.instructions.md` or a `SKILL.md`?"**
→ Ask: *Should Copilot DO something differently, or KNOW something extra?*
- "Never use raw SQL in service classes" → **instruction** (changes behavior)
- "Here's how our SQL schema is organized" → **skill** (adds knowledge)

**"Should this be an `.agent.md` or a `.prompt.md`?"**
→ Ask: *Is this a ROLE or a TASK?*
- "Be a security auditor" → **agent** (persona, sustained across conversation)
- "Do a security audit of this class" → **prompt** (one-shot workflow)

**"Should this be a `.prompt.md` or part of `copilot-instructions.md`?"**
→ Ask: *Is this always needed, or only sometimes?*
- "Always format responses with these sections" → **instructions** (always)
- "Format a design review report as..." → **prompt** (only during /design-review)

**"Should this be a `SKILL.md` or an MCP Server?"**
→ Ask: *Is this static reference data or live/writable data?*
- "Here's how JavaDoc works" → **skill** (static knowledge)
- "Read the latest JavaDoc for project X from our Confluence" → **MCP** (live, external)

---

## Part 4: Composition Patterns

Primitives are designed to **compose**. The most powerful Copilot setups combine multiple types
simultaneously. Here are the patterns — from simple to advanced.

---

### Same-Type Composition

#### Recipe 1 — Instruction Stacking (Multiple `.instructions.md`)

Apply multiple rule layers to the same files. All matching files stack in order.

```
copilot-instructions.md          ← Layer 0: project-wide (always)
    + java.instructions.md           ← Layer 1: Java language (applyTo: **/*.java)
    + clean-code.instructions.md     ← Layer 2: code quality (applyTo: **/*.java)
    + test.instructions.md           ← Layer 3: test-specific (applyTo: **/*Test.java)
```

For a `UserServiceTest.java` file, **all 4 layers** are active simultaneously.
For `pom.xml`, only Layer 0 applies.

**When to use:** Separate concerns cleanly — one file for language rules, one for quality, one for test conventions. Don't put everything in `copilot-instructions.md`.

**Steering mode variant:** Use `applyTo: "**"` to make an instruction file project-wide
(same behavior as `copilot-instructions.md`, but it stacks on top instead of being the base):

```yaml
---
applyTo: "**"
---
# Change Completeness Mode
Every change must follow the completeness checklist...
```

---

#### Recipe 2 — Multi-Skill Activation (Multiple `SKILL.md` files)

Multiple skills activate simultaneously when your question spans domains.

```
User asks: "How do I build an MCP server in Java using Spring Boot?"
│
├── mcp-development/SKILL.md           ← matches "MCP server", "protocol"
└── java-learning-resources/SKILL.md  ← matches "Java", "Spring Boot"
```

Both skills activate and Copilot synthesizes knowledge from both.

**When to use:** Design skills around DOMAINS (MCP, Git, DevOps), not specific questions.
Domain boundaries determine which skills activate together naturally.

---

#### Recipe 3 — Prompt Chain (Super-Prompt via `#file:` References)

One prompt includes other prompts by file reference — creating a composite workflow.

```markdown
<!-- .github/prompts/composite.prompt.md -->
---
name: composite
description: 'Full-stack analysis: design + impact + code quality'
---
Run a complete analysis in three phases:

Phase 1 — Architecture Review:
#file:.github/prompts/design-review.prompt.md

Phase 2 — Impact Analysis:
#file:.github/prompts/impact.prompt.md

Phase 3 — Code Quality:
#file:.github/prompts/refactor.prompt.md

Synthesize all three into a prioritized action plan.
```

**When to use:** Multi-phase workflows where each phase has its own structured template.
Avoids duplicating content across prompts.

---

#### Recipe 4 — Agent Handoff Chain

Agents hand work to specialist agents in sequence.

```
User: "Review this architecture and tell me what to change"
  │
  ▼
Designer (agent)
  Finds: "Observer pattern missing, God class Anti-pattern"
  Hands off to →
                Impact-Analyzer (agent)
                  Finds: "Removing God class affects 12 callsites"
                  Hands off to →
                                Code-Reviewer (agent)
                                  Reviews: actual refactoring plan quality
```

**Agent file configuration:**

```yaml
---
description: Architecture reviewer. Hands off to Impact-Analyzer when design decision made.
tools:
  - codebase
  - search
  - usages
---
...after completing the design review, hand off to the Impact-Analyzer
for a full assessment of the ripple effects of each recommendation.
```

**When to use:** Sequential specialist analysis where each expert needs the previous expert's output.

---

### Cross-Type Composition

#### Recipe 5 — Instructions + Skill (Always-On Rules + Always-On Knowledge)

The "enriched Copilot" pattern: automatic rules AND automatic domain knowledge, both active constantly.

```
.github/instructions/java.instructions.md    ← "Use final, var, @Override, try-with-resources"
.github/skills/java-learning-resources/      ← "Here's the full Java API reference, common patterns"
```

When a user asks a Java question:
- Instructions enforce HOW Copilot writes (behavioral)
- Skill enables WHAT Copilot knows (informational)
- Both activate without any user action

**When to use:** For domains where you want both standardized behavior AND deep reference material.

---

#### Recipe 6 — Agent + Prompt (Persona + Workflow)

The most common cross-type composition: "WHO you are" + "WHAT you do".

```
User selects:  Debugger agent (systematic, hypothesis-driven, root-cause focus)
User types:    /debug

Result: The Debugger PERSONA runs the /debug WORKFLOW structure
```

Each element contributes differently:
- **Agent:** Copilot's mindset, communication style, tool access
- **Prompt:** Structure, steps, required outputs, input variables

```yaml
# .github/prompts/debug.prompt.md
---
agent: debugger          ← auto-activates the Debugger agent
tools: ['codebase', 'search', 'problems']
---
${input:symptom:What is the exact error or unexpected behavior?}

Follow the 5-phase debugging methodology:
1. Observe (reproduce the symptom)
2. Hypothesize (list possible causes)
...
```

**When to use:** Any time you have a defined expert role AND a structured way that expert should approach a task.

---

#### Recipe 7 — Instructions + Agent (Rules Always Constrain Persona)

Instructions stack UNDER and ALWAYS apply, even when an agent is active.

```
java.instructions.md (applyTo: **/*.java):
  "Methods must be <30 lines. Use @Override. Use Objects.requireNonNull."
                          +
Debugger agent:
  "Think systematically. List hypotheses. Focus on root cause."

Combined: The debugger thinks systematically AND writes any code suggestions following Java rules.
```

The agent CANNOT override instruction-level rules. Instructions are the contract; agents are the style.

**When to use:** Always — this happens automatically. The insight is that you can write agents
that ASSUME instructions enforce the baseline, so the agent doesn't need to repeat coding standards.

---

#### Recipe 8 — Prompt + Skill (Workflow Template + Deep Reference)

When a prompt is invoked, the relevant skill auto-activates alongside it.

```
User types: /mcp
                │
                ├── .github/prompts/mcp.prompt.md loads     ← workflow structure + response shape
                └── mcp-development/SKILL.md auto-activates ← deep MCP protocol reference
```

Write the prompt body to be the FRAMEWORK (what to address, in what order, at what depth).
Let the skill provide the DEPTH (specific commands, code examples, API specs).

**When to use:** Any domain slash command that has a corresponding skill file. The prompt controls
structure; the skill controls depth.

---

#### Recipe 9 — Agent + MCP (Persona + External Tools)

The "specialist with real tools" pattern: an expert persona that can actually DO things.

```
Code-Reviewer agent:
  - Read-only persona
  - Tools: [codebase, search, usages]  ← no editFiles, no runCommands

                +

GitHub MCP Server:
  - Tools: search_code, list_pull_requests, get_file_contents, list_issues

Result: Code-Reviewer reviews ACTUAL open PRs, reads REAL files from the repo,
        comments on specific line numbers — all without needing editFiles access
        (MCP call is different from VS Code tool access).
```

**When to use:** Specialist agents that need live external data but should not have write access.
The agent's tool list restricts VS Code tools; MCP server tools are separate.

---

#### Recipe 10 — Full Stack (All 6 Primitives Together)

The maximum-power composition for high-stakes or complex workflows.

```
┌──────────────────────────────────────────────────────┐
│              ACTIVE CUSTOMIZATION STACK               │
│                                                       │
│  1. copilot-instructions.md  ← project rules (always) │
│  2. java.instructions.md     ← Java rules (*.java)    │
│  3. clean-code.instructions.md ← quality rules        │
│  4. Designer agent           ← pattern-focused persona │
│  5. /design-review prompt    ← structured workflow    │
│  6. design-patterns/SKILL.md ← GoF patterns knowledge │
│  7. GitHub MCP server        ← actual PR/issue data   │
└──────────────────────────────────────────────────────┘
```

**Practical Example: "Review PR #47 for architecture issues"**

1. Instructions enforce that any code suggestions follow Java conventions
2. Agent = Designer: thinks in terms of patterns, SOLID, coupling
3. Prompt = /design-review: structures the output (issues, recommendations, examples)
4. Skill = design-patterns: provides GoF pattern knowledge as reference
5. MCP = GitHub: reads PR diff, comments, changed files

**When to use:** Architecture reviews, security audits, release readiness checks — any
workflow where correctness, depth, and consistency all matter.

---

#### Recipe 11 — Meta-Pattern: Steering Modes (Instructions as Global Mode Switches)

Use `applyTo: "**"` instruction files as "always-on behavioral modes" that activate globally.

```
.github/instructions/change-completeness.instructions.md:
  applyTo: "**"
  Content: "Every change has a ripple. Follow the completeness checklist..."
```

This creates a **mode** that is always active, regardless of file type or agent. Stack multiple
mode files by adding multiple `applyTo: "**"` instruction files. The steering-modes file itself
explains and documents all available modes.

**Pattern:**

```
copilot-instructions.md       ← base project rules
    + steering-modes.inst.md  (applyTo: **) ← meta: documents modes
    + change-completeness.inst.md (applyTo: **) ← completeness mode: always on
    + [any-feature-mode].inst.md (applyTo: **) ← add/remove modes as needed
```

**When to use:** Repo-wide workflow enforcement that must apply even when editing `pom.xml`.

---

#### Recipe 12 — Dynamic Composition (Input-Driven Prompt Controls Which Knowledge Loads)

Use `${input:topic}` variables to make one prompt cover multiple domains — and the skill
for that domain auto-activates based on the content of the conversation.

```
/learn-concept ${input:topic}

User: /learn-concept → "mcp-servers"

Copilot resolves:
  - /learn-concept prompt structure (response format)
  - mcp-development/SKILL.md (auto-activates on "mcp-servers" topic)
  - java-learning-resources/SKILL.md (if user adds "in Java")
```

The `topic` input dynamically determines which skills become relevant, without hardcoding
a fixed set of skill →prompt mappings.

**When to use:** Generalist slash commands (`/learn-concept`, `/explain`, `/debug`) that need
to cover many domains but can delegate depth to the relevant skill.

---

## Part 5: Anti-Patterns

### 🚫 Anti-Pattern 1: Putting Instructions in the Wrong Place

| Mistake | Symptom | Fix |
|---|---|---|
| Coding rules in `SKILL.md` | Rules are treated as suggestions, not followed consistently | Move rules to `.instructions.md` |
| Reference data in `copilot-instructions.md` | File becomes huge; ALL requests include irrelevant context | Move large reference blocks to `SKILL.md` |
| Persona in `.prompt.md` body | Persona only active during that prompt | Extract to `.agent.md`, reference via `agent:` field |
| File-type rules in `copilot-instructions.md` | Even Python sessions get Java rules | Use `.instructions.md` with `applyTo` |

---

### 🚫 Anti-Pattern 2: Bloated `copilot-instructions.md`

```
❌ copilot-instructions.md: 5000 tokens of Maven lifecycle, Git branching diagrams,
   full Java API cheatsheet, 3 different response format templates...

✅ copilot-instructions.md: 500 tokens of project conventions
✅ java.instructions.md: 300 tokens of Java-specific rules
✅ java-learning-resources/SKILL.md: Maven, API cheatsheets (auto-loads when relevant)
```

Every token in `copilot-instructions.md` is sent on EVERY request, forever. Keep it lean.

---

### 🚫 Anti-Pattern 3: Vague Skill Descriptions

```
❌  description: "Java stuff"
    → Never activates (too vague)

❌  description: "How to create a singleton pattern in Java"
    → Too specific, misses many relevant questions

✅  description: >
      Comprehensive Java & IDE learning resources. Use when asked about Java 21+,
      JVM, concurrency, streams, JDK API, Maven, Gradle, IntelliJ IDEA setup,
      or Java learning resources.
    → Activates for all Java-related questions
```

The `description` field IS the skill's activation mechanism (semantic matching). It is the
single most important field in a `SKILL.md` file.

---

### 🚫 Anti-Pattern 4: Agents Without Tool Restrictions

```
❌  .agent.md (no tools: field)
    → Agent gets ALL tools by default (editFiles, runCommands, browser, terminal...)
    → Security risk: a "read-only reviewer" that can run rm -rf

✅  .agent.md:
    tools:
      - codebase
      - search
      - usages
    (read-only tools only — agent CANNOT edit files or run commands)
```

Always be explicit about `tools:`. Default = everything. Explicit list = least privilege.

---

### 🚫 Anti-Pattern 5: Prompt vs. Instructions Confusion

```
❌ copilot-instructions.md:
   "When asked to review code, first check SOLID principles,
    then check naming, then check error handling, then check tests..."
   → This is a WORKFLOW — it belongs in a prompt

✅ copilot-instructions.md:
   "Methods must be <30 lines. Use @Override. Never catch generic Exception."
   → These are ALWAYS TRUE RULES → belong in instructions

✅ .github/prompts/review.prompt.md:
   "Step 1: Check SOLID. Step 2: Check naming. Step 3: Check error handling..."
   → Workflow → invoked with /review
```

**Rule of thumb:** If it starts with "When I ask you to...", it's a prompt workflow,
not a project-wide instruction.

---

### 🚫 Anti-Pattern 6: Using MCP When a Skill Would Do

```
❌ Build an MCP server to serve your team's Git branching guide document to Copilot
   → Unnecessary complexity, latency, server infra

✅ Put the Git branching guide in git-vcs/SKILL.md
   → Auto-activates on Git questions, zero infra

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

✅ Build an MCP server for: reading LIVE GitHub PR data, creating issues,
   pushing commits — things that require real API calls
```

Use MCP when you need LIVE data or WRITE operations. Use Skills for STATIC knowledge.

---

### 🚫 Anti-Pattern 7: Duplicate Instruction Content Across Files

```
❌  java.instructions.md:      "Always add Javadoc"
    clean-code.instructions.md: "Always add Javadoc"
    copilot-instructions.md:    "All public methods need Javadoc"
    → Conflict potential, maintenance nightmare

✅  copilot-instructions.md:    "All public Java methods need Javadoc"
    (only one place — no duplication)
```

If a rule applies to all Java files, put it in `java.instructions.md` once. Don't scatter.

---

## Part 6: Quick Reference Card

### Activation Summary

```
────────────────────────────────────────────────────────────
PRIMITIVE               ACTIVATION          STACKS
────────────────────────────────────────────────────────────
copilot-instructions    ALWAYS              Base layer
.instructions.md        auto (file match)   Yes (many can apply)
SKILL.md                auto (semantic)     Yes (many can activate)
MCP servers             auto (agent mode)   Yes (all servers)
.agent.md               manual (dropdown)   No (one at a time)
.prompt.md              manual (/cmd)       No (one at a time)
────────────────────────────────────────────────────────────
```

### Frontmatter Field Reference

**`.instructions.md`:**

```yaml
---
applyTo: "**/*.java"          # Required: glob pattern(s) for file matching
---
```

**`.prompt.md`:**

```yaml
---
name: my-command              # Required: the /command-name users type
description: 'What it does'  # Required: shown in autocomplete
agent: designer               # Optional: auto-select this agent
tools: ['codebase', 'search'] # Optional: restrict available tools
mode: ask                     # Optional: 'ask' | 'agent' | 'edit'
---
```

**`.agent.md`:**

```yaml
---
description: >               # Required: WHEN to use (shown in dropdown)
  Use this agent when asked to...
tools:                       # Optional (defaults to ALL tools)
  - codebase
  - search
  - usages
model: gpt-4o                # Optional (new 2026): pin to model
---
```

**`SKILL.md`:**

```yaml
---
name: my-skill               # Required: skill identifier
description: >               # Required: THIS IS THE ACTIVATION TRIGGER
  Use when asked about [X], [Y], or [Z]...
---
```

### Composition Decision Matrix

```
NEED                                         → COMBINE
─────────────────────────────────────────────────────────
Rules + Knowledge (always on)               → instructions + skill
Workflow + Expert mindset                   → prompt + agent
Expert + Live external data                 → agent + MCP
Rules + Workflow + Expert                   → instructions + prompt + agent
Everything for high-stakes sessions         → all 6
Domain-specific always-on enrichment        → .instructions.md (applyTo: **) + SKILL.md
Multi-step sequential analysis              → agent handoff chain
Repeatable compound workflow                → prompt with #file: references
```

### Further Reading

| Topic | File |
|---|---|
| Architecture overview | [customization-guide.md](customization-guide.md) |
| All slash commands | [slash-commands.md](slash-commands.md) |
| MCP open preview changelog | [copilot-mcp-preview.md](copilot-mcp-preview.md) |
| MCP server development | [mcp-ecosystem.md](mcp-ecosystem.md) |
| Agent file format | [../agents/README.md](../agents/README.md) |
| Instruction file format | [../instructions/README.md](../instructions/README.md) |
| Skill file format | [../skills/README.md](../skills/README.md) |
| Prompt file format | [../prompts/README.md](../prompts/README.md) |
