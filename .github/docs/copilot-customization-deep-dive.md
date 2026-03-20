# Copilot Customization — Deep Dive Reference

> **The complete guide to all GitHub Copilot customization primitives — differences, similarities,
> when to use each, and how to compose them into powerful layered workflows.**

| Audience | Start Here |
|---|---|
| 🟢 **Newbie** | [Part 9 — Why Not Just Skills?](#part-9-why-not-just-skills--the-faq) → [Part 1 — The 6 Primitives](#part-1-the-6-primitives) → [Decision Guide](#part-3-decision-guide) → [Side-by-Side Examples](#part-12-side-by-side--same-task-every-primitive) |
| 🟡 **Amateur** | [Comparison Table](#part-2-head-to-head-comparison) → [Composition Basics](#same-type-composition) → [Migration Guide](#part-7-migration--interchange-guide) → [Testing](#part-14-testing-your-customizations) → [Cross-Repo Portability](#part-15-cross-repo-portability) |
| 🔴 **Pro** | [Latest Features](#part-11-latest-features--api-updates-2026) → [Full Stack Compositions](#cross-type-composition) → [Anti-Patterns](#part-5-anti-patterns) → [Token Economics](#part-13-token-economics--performance) → [API Surface](#part-16-complete-api-surface-reference) → [Security](#part-17-security-considerations) → [Official Resources](#part-10-official-resources--standards) |

---

## Table of Contents

1. [The 6 Primitives](#part-1-the-6-primitives)
2. [Head-to-Head Comparison](#part-2-head-to-head-comparison)
3. [Decision Guide — Which to Use When](#part-3-decision-guide)
4. [Composition Patterns](#part-4-composition-patterns)
5. [Anti-Patterns](#part-5-anti-patterns)
6. [Quick Reference Card](#part-6-quick-reference-card)
7. [Migration & Interchange Guide](#part-7-migration--interchange-guide)
8. [Step-by-Step Creation Walkthroughs](#part-8-step-by-step-creation-walkthroughs)
9. [Why Not Just Skills? — The FAQ](#part-9-why-not-just-skills--the-faq)
10. [Official Resources & Standards](#part-10-official-resources--standards)
11. [Latest Features & API Updates (2026)](#part-11-latest-features--api-updates-2026)
12. [Side-by-Side — Same Task, Every Primitive](#part-12-side-by-side--same-task-every-primitive)
13. [Token Economics & Performance](#part-13-token-economics--performance)
14. [Testing Your Customizations](#part-14-testing-your-customizations)
15. [Cross-Repo Portability](#part-15-cross-repo-portability)
16. [Complete API Surface Reference](#part-16-complete-api-surface-reference)
17. [Security Considerations](#part-17-security-considerations)
18. [Versioning & Evolving Customizations](#part-18-versioning--evolving-customizations)

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

```text
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

```text
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

```text
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

```text
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

```yaml
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

```text
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

```text
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

```text
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

```text
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

```text
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

```text
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

```text
.github/instructions/change-completeness.instructions.md:
  applyTo: "**"
  Content: "Every change has a ripple. Follow the completeness checklist..."
```

This creates a **mode** that is always active, regardless of file type or agent. Stack multiple
mode files by adding multiple `applyTo: "**"` instruction files. The steering-modes file itself
explains and documents all available modes.

**Pattern:**

```text
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

```text
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

```text
❌ copilot-instructions.md: 5000 tokens of Maven lifecycle, Git branching diagrams,
   full Java API cheatsheet, 3 different response format templates...

✅ copilot-instructions.md: 500 tokens of project conventions
✅ java.instructions.md: 300 tokens of Java-specific rules
✅ java-learning-resources/SKILL.md: Maven, API cheatsheets (auto-loads when relevant)
```

Every token in `copilot-instructions.md` is sent on EVERY request, forever. Keep it lean.

---

### 🚫 Anti-Pattern 3: Vague Skill Descriptions

```text
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

```text
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

```text
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

```text
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

```text
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

```text
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

```text
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
| Migration & interchange | [Part 7 below](#part-7-migration--interchange-guide) |
| Step-by-step creation | [Part 8 below](#part-8-step-by-step-creation-walkthroughs) |

---

## Part 7: Migration & Interchange Guide

> **When you already have customizations but they're in the wrong primitive type —
> or you're not sure whether two types are interchangeable.**

This section covers the 8 most common migration paths, with before/after examples,
a 3-tier decision framework, and an interchange matrix.

---

### 🟢 Newbie — 3 Simple Rules

If you're just getting started, these three rules prevent 90% of misplacements:

```text
RULE 1:  If it's a RULE that should ALWAYS apply → instruction file
RULE 2:  If it's KNOWLEDGE that answers questions → skill file
RULE 3:  If it's a WORKFLOW you trigger manually  → prompt file
```

**Don't overthink it.** Pick one, try it, and if it doesn't activate when expected,
read the Amateur section below for migration patterns.

---

### 🟡 Amateur — The 8 Migration Paths

#### Migration 1: Bloated `copilot-instructions.md` → Split `.instructions.md` Files

**When to migrate:** Your `copilot-instructions.md` exceeds ~200 lines, or contains
rules that only apply to specific file types.

**Before (everything in one file):**

```markdown
<!-- copilot-instructions.md — 400 lines, unmaintainable -->
# Project Instructions
## Java Rules
- Use final for variables that don't change
- Prefer var for local variables...
- Add Javadoc to all public methods...
(80 lines of Java rules)

## Markdown Rules
- Every heading must have blank lines before and after...
- Use --- for horizontal rules...
(60 lines of Markdown rules)

## Clean Code Rules
- Methods under 30 lines...
- Single responsibility...
(40 lines of clean code rules)
```

**After (domain-specific instruction files):**

```text
.github/
├── copilot-instructions.md          ← 50 lines: project overview + universal rules ONLY
├── instructions/
│   ├── java.instructions.md         ← applyTo: "**/*.java" — Java-specific rules
│   ├── md-formatting.instructions.md ← applyTo: "**" — Markdown formatting rules
│   └── clean-code.instructions.md   ← applyTo: "**/*.java" — Clean code principles
```

**Migration steps:**

1. Identify rule clusters in `copilot-instructions.md` (Java, Markdown, testing, etc.)
2. For each cluster, create `.github/instructions/<domain>.instructions.md`
3. Set `applyTo` to the narrowest glob that matches (`**/*.java`, `**/*.md`, `**/test/**`)
4. Move the rules — copy-paste, then delete from `copilot-instructions.md`
5. Keep only project-wide conventions in `copilot-instructions.md` (naming, commit style, structure)

**Key insight:** `copilot-instructions.md` is your **project overview**. Instruction files
are your **domain-specific rule books**. If a rule only matters when editing `.java` files,
it belongs in an instruction file with `applyTo: "**/*.java"`.

---

#### Migration 2: Instructions → Skill (knowledge extraction)

**When to migrate:** Your instruction file contains large blocks of **reference knowledge**
(lists of resources, concept explanations, "here's how X works" sections) rather than
**rules** ("always do X", "never do Y").

**The test:** Read each line and ask: "Is this a rule Copilot must follow, or knowledge
Copilot should know?" Rules stay as instructions. Knowledge becomes a skill.

**Before (instruction file with embedded knowledge):**

```markdown
---
applyTo: "**/*.java"
---
# Java Instructions

## Rules
- Use `final` for variables that don't change
- Prefer `var` for local variables

## Java Concurrency Reference
Virtual threads were introduced in Java 21...
Use `Executors.newVirtualThreadPerTaskExecutor()` for I/O-bound work...
Structured concurrency via `StructuredTaskScope`...
(100 lines of concurrency knowledge)
```

**After (rules in instruction, knowledge in skill):**

```markdown
<!-- .github/instructions/java.instructions.md -->
---
applyTo: "**/*.java"
---
# Java Instructions
- Use `final` for variables that don't change
- Prefer `var` for local variables
```

```markdown
<!-- .github/skills/java-concurrency/SKILL.md -->
---
name: java-concurrency
description: >
  Use when asked about Java concurrency, virtual threads,
  structured concurrency, or parallel processing...
---
# Java Concurrency Knowledge
Virtual threads were introduced in Java 21...
(Full reference material here)
```

**Why this matters:** Instructions are injected **every time** the file pattern matches.
Skills activate **only when relevant**. Moving 100 lines of concurrency knowledge to a
skill means Copilot only loads it when you ask about concurrency — not when you're
writing a simple getter method.

---

#### Migration 3: Skill → `.instructions.md` (enforcement extraction)

**When to migrate:** Your skill file contains rules that Copilot should **always enforce**
when editing certain files, not just reference when asked.

**The test:** If you want Copilot to follow these rules even when the user doesn't ask
about the topic, they need to be instructions (auto-applied by file pattern), not skills
(auto-applied by semantic relevance).

**Before (skill with enforcement rules):**

```markdown
<!-- SKILL.md — mixing knowledge with rules -->
---
description: Use when asked about API design...
---
# API Design Skill
## Rules (should always apply to controller files)
- All endpoints return ResponseEntity
- Use @Valid on request bodies
- Return 404 for missing resources, never null

## Knowledge (reference material)
REST maturity model: Level 0 through Level 3...
HATEOAS explained: ...
```

**After:**

```markdown
<!-- .github/instructions/api-design.instructions.md -->
---
applyTo: "**/controller/**/*.java"
---
- All endpoints return ResponseEntity
- Use @Valid on request bodies
- Return 404 for missing resources, never null
```

```markdown
<!-- .github/skills/api-design/SKILL.md (knowledge only) -->
---
description: Use when asked about API design, REST maturity, HATEOAS...
---
# API Design Knowledge
REST maturity model: Level 0 through Level 3...
HATEOAS explained: ...
```

---

#### Migration 4: Prompt → Agent (workflow to persona)

**When to migrate:** Your prompt file defines a detailed **persona** and **behavioral style**
that you want to reuse across multiple conversations — not just a one-shot workflow.

**The test:** Do you select this by typing `/command` once, or do you want to stay in this
"mode" for an entire conversation? If the latter, it should be an agent.

**Before (prompt acting as a persona):**

```markdown
---
name: code-reviewer
description: 'Review code like a senior engineer'
mode: ask
---
You are a senior software engineer performing a code review.
Be thorough, critical, and constructive...
Always check: SOLID, naming, error handling, test coverage...
Review the selected code and provide feedback.
```

**After (agent for persistent persona):**

```markdown
<!-- .github/agents/code-reviewer.agent.md -->
---
description: >
  Use when you want thorough, senior-engineer-level code review.
  Checks SOLID, naming, error handling, and test coverage.
tools:
  - codebase
  - search
  - usages
---
You are a senior software engineer performing a code review.
Be thorough, critical, and constructive...
Always check: SOLID, naming, error handling, test coverage...
```

**Keep the prompt too (as a quick-trigger):**

```markdown
---
name: review
description: 'Quick code review of the selected code'
agent: code-reviewer
mode: ask
---
Review the selected code. Focus on: {{focus}}
```

**Pattern:** Agent = persistent persona. Prompt = quick-trigger that optionally selects
an agent. They complement each other — don't delete the prompt, just make it delegate.

---

#### Migration 5: Agent → Prompt (persona to workflow)

**When to migrate:** Your agent file is rarely used as a persistent mode. Users just
want to trigger a specific task, not switch their entire conversation persona.

**The test:** Do you ever "stay in" this agent for 5+ exchanges? If not, it's a prompt.

**Before (agent used as a one-shot):**

```markdown
<!-- .github/agents/generate-tests.agent.md -->
---
description: Use when you need to generate unit tests for Java code.
---
You are a test generation expert. Generate JUnit 5 tests with...
```

**After (prompt — more natural invocation):**

```markdown
---
name: test-gen
description: 'Generate JUnit 5 tests for the current file'
mode: agent
---
Generate comprehensive JUnit 5 tests for the current file.
Include: happy path, edge cases, error scenarios.
Use: @BeforeEach setup, assertThrows for exceptions...
```

**Rule of thumb:** If the user's mental model is "I want to **do** X" → prompt.
If it's "I want Copilot to **be** X" → agent.

---

#### Migration 6: Skill → MCP Server (static to live)

**When to migrate:** Your skill file references data that changes frequently or requires
real-time access (API responses, database state, live documentation, issue trackers).

**The test:** Is the knowledge **static** (rarely changes, can be committed to git)?
Keep it as a skill. Is the knowledge **dynamic** (changes daily, requires network access)?
Migrate to MCP.

**Before (skill with stale data):**

```markdown
---
description: Use when asked about our Jira project status...
---
# Project Status
## Current Sprint (Sprint 47)
- PROJ-101: User auth — In Progress
- PROJ-102: Dashboard — Done
- PROJ-103: Reports — To Do
(outdated the moment you commit it)
```

**After (MCP server with live data):**

```java
// MCP tool: jira_get_sprint_status
// Returns live sprint data from Jira REST API
```

```markdown
<!-- Keep the skill for STATIC context that complements live data -->
---
description: Use when asked about project conventions, not live status...
---
# Project Conventions
- Sprint length: 2 weeks
- Story point scale: 1, 2, 3, 5, 8, 13
- Definition of Done: code reviewed + tests passing + deployed to staging
```

**Pattern:** MCP replaces the **dynamic** part of a skill. Keep the **static** context
(conventions, reference guides) as a skill alongside the MCP server.

---

#### Migration 7: MCP Server → Skill (simplification)

**When to migrate:** You built an MCP server but it only serves static content that
never changes. The server infrastructure is overkill.

**The test:** Does the server make any network calls, read any databases, or access any
APIs? If not — if it's just returning hardcoded content — a skill file does the same
job with zero infrastructure.

**Before (MCP server serving static content):**

```java
case "get_git_branching_guide":
    return """
        ## Git Branching Strategies
        ### GitFlow: main, develop, feature/*, release/*, hotfix/*
        ### GitHub Flow: main + feature branches only...
        """;
```

**After (skill file — same content, zero infra):**

```markdown
---
description: Use when asked about Git branching strategies...
---
# Git Branching Strategies
## GitFlow: main, develop, feature/*, release/*, hotfix/*
## GitHub Flow: main + feature branches only...
```

---

#### Migration 8: Multiple Prompts → Prompt + Agent (consolidation)

**When to migrate:** You have 5+ prompt files that all set the same persona/context
before doing slightly different tasks.

**Before (repeated persona in every prompt):**

```markdown
<!-- debug.prompt.md -->
You are an expert debugger. You use hypothesis-driven debugging...
Investigate: {{issue}}
```

```markdown
<!-- rca.prompt.md -->
You are an expert debugger. You use hypothesis-driven debugging...
Perform root cause analysis on: {{issue}}
```

```markdown
<!-- trace.prompt.md -->
You are an expert debugger. You use hypothesis-driven debugging...
Trace the execution path of: {{function}}
```

**After (one agent + lightweight prompts):**

```markdown
<!-- .github/agents/debugger.agent.md -->
---
description: Expert debugger using hypothesis-driven analysis...
---
You are an expert debugger. You use hypothesis-driven debugging...
```

```markdown
<!-- debug.prompt.md -->
---
agent: debugger
---
Investigate: {{issue}}
```

```markdown
<!-- rca.prompt.md -->
---
agent: debugger
---
Perform root cause analysis on: {{issue}}
```

**Benefit:** Persona defined once. Prompts become thin workflow triggers.

---

### 🔴 Pro — Interchange Matrix & Advanced Patterns

#### The Interchange Matrix

Not all primitives are interchangeable. This matrix shows which migrations are
**valid**, **possible but not recommended**, or **impossible**:

```text
FROM ↓ / TO →    instructions  skill   prompt  agent   MCP    copilot-inst
────────────────────────────────────────────────────────────────────────────
copilot-inst     ✅ split     ⚠️ rare  ❌      ❌      ❌     —
instructions     —            ✅ know  ⚠️ rare ❌      ❌     ✅ merge
skill            ✅ enforce   —        ❌      ❌      ✅ live ⚠️ rare
prompt           ❌           ❌       —       ✅ mode  ❌     ❌
agent            ❌           ❌       ✅ thin —        ❌     ❌
MCP              ❌           ✅ static ❌     ❌      —      ❌

✅ = natural migration path    ⚠️ = possible but usually wrong    ❌ = not applicable
```

**Reading the matrix:** Row = source type, Column = target type. For example,
"instructions → skill" is ✅ (natural path when extracting knowledge from rules).

#### When Content Could Live in Multiple Primitives

Some content genuinely fits more than one type. Use this tiebreaker:

| Criterion | Winner |
|---|---|
| Must apply even when the user doesn't ask about this topic | `.instructions.md` |
| Should only load when semantically relevant | `SKILL.md` |
| Needs user to explicitly trigger it | `.prompt.md` |
| Content changes weekly or faster | MCP server |
| Content changes monthly or slower | `SKILL.md` or `.instructions.md` |
| Content is < 20 lines of rules | `.instructions.md` |
| Content is > 50 lines of reference | `SKILL.md` |

#### Refactoring at Scale — Multi-File Migration

When reorganizing an entire `.github/` directory:

1. **Audit current state** — list every file, its size, and primary purpose (rules/knowledge/workflow/persona)
2. **Classify each file** — tag as `rule`, `knowledge`, `workflow`, `persona`, or `mixed`
3. **Split mixed files first** — extract rules → instructions, knowledge → skills
4. **Consolidate similar agents** — if 3 agents share 80% of their persona, create one base agent
5. **Wire prompts to agents** — prompts become thin triggers (`agent: <name>` in frontmatter)
6. **Verify activation** — test each primitive by asking Copilot questions that should trigger it
7. **Delete orphans** — remove files that nothing references and nothing activates

#### The "Right Size" Heuristic

```text
copilot-instructions.md  →  30–80 lines (project overview + universal rules)
.instructions.md         →  20–60 lines per file (focused domain rules)
SKILL.md                 →  100–500 lines (deep domain knowledge)
.prompt.md               →  10–40 lines (workflow trigger + template)
.agent.md                →  20–80 lines (persona definition + behavioral rules)
MCP server               →  As complex as needed (but each TOOL should do one thing)
```

If a file drastically exceeds these ranges, it's likely trying to do too much and
should be split or migrated.

---

## Part 8: Step-by-Step Creation Walkthroughs

> **Quick reference for creating each primitive type from scratch.**
> For full architectural context, see [Customization Guide](customization-guide.md).

### Create an Instruction File

```text
1. Create:  .github/instructions/<domain>.instructions.md
2. Add frontmatter:
   ---
   applyTo: "**/*.java"    ← glob pattern: when does this activate?
   ---
3. Write rules as bullet points — imperative ("Use X", "Never Y")
4. Test: open a file matching the glob → ask Copilot a question → verify the rule is followed
```

### Create a Skill

```text
1. Create:  .github/skills/<domain>/SKILL.md
2. Add frontmatter:
   ---
   name: my-domain
   description: >
     Use when asked about [topic A], [topic B], or [topic C]...
   ---
3. Write reference content — explanations, tables, cheatsheets, resource links
4. Test: ask Copilot about [topic A] → verify the skill content appears in the response
5. KEY: the description field IS the activation trigger — make it specific and keyword-rich
```

### Create a Prompt (Slash Command)

```text
1. Create:  .github/prompts/<command-name>.prompt.md
2. Add frontmatter:
   ---
   name: my-command
   description: 'One-line description shown in autocomplete'
   mode: ask           ← 'ask' | 'agent' | 'edit'
   agent: designer     ← optional: auto-select an agent
   tools: ['codebase'] ← optional: restrict tools
   ---
3. Write the prompt template with {{variables}} for user input
4. Test: type /my-command in Chat → verify it triggers correctly
```

### Create an Agent

```text
1. Create:  .github/agents/<name>.agent.md
2. Add frontmatter:
   ---
   description: >
     Use when you want [persona] behavior. Expert in [X], [Y], [Z].
   ---
3. Write the persona: who the agent IS, how it THINKS, what it DOES and DOESN'T do
4. Test: select the agent from the Chat dropdown → ask domain questions → verify persona
5. KEY: description tells VS Code WHEN to suggest this agent — be specific
```

### Create an MCP Server

```text
1. Higher complexity — see the full guide: mcp-development SKILL.md
2. Quick summary:
   a. Define tools (name, description, input schema)
   b. Implement handlers (API calls, data processing)
   c. Register in mcp.json (command, args, env)
   d. Build and test with MCP Inspector or VS Code
3. Use MCP ONLY when you need: live data, write operations, or external API access
```

### Quick Decision Flowchart

```text
I want to add something to my .github/ setup. What do I create?

Is it a RULE?
├── Yes → Does it apply to specific file types?
│   ├── Yes → .instructions.md (with applyTo glob)
│   └── No → copilot-instructions.md (universal rule)
└── No → Is it KNOWLEDGE?
    ├── Yes → Is the knowledge STATIC?
    │   ├── Yes → SKILL.md
    │   └── No (live/dynamic) → MCP server
    └── No → Is it a WORKFLOW?
        ├── Yes → Is it a one-shot task?
        │   ├── Yes → .prompt.md
        │   └── No (persistent mode) → .agent.md
        └── No → Probably doesn't need a customization file
```

---

## Part 9: Why Not Just Skills? — The FAQ

> **"My colleague says skills are the best primitive and we should just use skills for
> everything. Is that true? If skills can do it all, why do the other 5 primitives exist?"**

This is one of the most common questions. The short answer: **No — skills are great for
one specific job (domain knowledge), but they fundamentally cannot do what the other
primitives do.** Here's why.

---

### The Core Misunderstanding

Skills are **knowledge packs**. They answer the question "What does Copilot know?"
But a good Copilot setup also needs to answer:

| Question | Primitive | Skills can't do this because... |
|---|---|---|
| What rules must Copilot **always** follow? | Instructions | Skills only activate when semantically relevant — they can't enforce rules on every request |
| What **persona** should Copilot adopt? | Agent | Skills have no persona — they can't change Copilot's communication style, tool access, or behavioral constraints |
| What **workflow** should Copilot execute? | Prompt | Skills are passive reference — they can't define a multi-step task template with user inputs |
| What **live data** should Copilot access? | MCP Server | Skills are static text files — they can't make API calls, read databases, or fetch real-time data |

---

### FAQ: One Primitive at a Time

#### "Why not put rules in a skill instead of an instruction file?"

**Because skills don't guarantee activation.** Skills activate when Copilot judges
the conversation is semantically relevant to the skill's description. If you put
"always use `final` for variables" in a skill, Copilot will only load it when it
thinks the conversation is about `final` keywords — not when you ask it to write
a simple getter method.

Instructions with `applyTo: "**/*.java"` activate **every time you edit a Java file**.
No semantic matching needed. No missed rules.

```text
Instruction (applyTo: "**/*.java"):  "Use final for variables" → ALWAYS enforced
Skill (description: "Java best practices"):  "Use final for variables" → only when Copilot loads it
```

#### "Why not put a persona in a skill instead of an agent?"

**Because skills can't change who Copilot IS.** An agent redefines Copilot's identity:
its communication style, its tool access, its constraints. A skill just gives Copilot
more information.

```text
Agent:  "You are a security auditor. You ONLY look for vulnerabilities. You are
         skeptical by default. You do NOT write new code — only review."
         → Changes behavior, tool access, personality

Skill:  "OWASP Top 10: 1. Broken Access Control..."
         → Adds knowledge. Copilot is still Copilot.
```

When you select the "Security Auditor" agent in the dropdown, Copilot **becomes** that
auditor for the entire conversation. A skill can't do that.

#### "Why not put a workflow in a skill instead of a prompt?"

**Because skills can't be invoked.** You can't type `/my-skill` in the chat. Skills
activate silently in the background. Prompts are explicit triggers — you type `/review`
and a predefined workflow starts with template variables and structured steps.

```text
Prompt (/review):   "Review {{file}} focusing on {{focus}}"
                    → User types /review → fills in variables → structured output

Skill:              (no way to trigger it manually — waits for Copilot to decide it's relevant)
```

#### "Why not just keep everything in `copilot-instructions.md`?"

**Because context windows are finite.** `copilot-instructions.md` is injected into
**every single request**. If you put 5000 lines of Java knowledge, Git conventions,
MCP documentation, and security checklists in there, you'll consume most of Copilot's
context window before the conversation even starts.

```text
copilot-instructions.md with 5000 lines → Every request is bloated, slow, and confused
vs.
copilot-instructions.md (50 lines)  +  5 instruction files  +  8 skills
→ Only the relevant content loads per request
```

#### "If I already have comprehensive skills, do I still need instructions?"

**Yes.** Think of it this way:

- **Skills** = the textbooks on your shelf (you look them up when you need them)
- **Instructions** = the rules on the wall (you follow them whether you looked them up or not)

You need rules on the wall AND textbooks on the shelf. They serve different purposes.

---

### The "Just Use Skills" Approach — What Goes Wrong

Here's what happens when teams try to put everything in skills:

| What they wanted | What actually happened |
|---|---|
| Java rules enforced on every file | Rules only activated when Copilot deemed the conversation "about Java rules" |
| Consistent commit message format | Skill about commits didn't load when answering "fix this bug" |
| Security persona for code reviews | Copilot gave knowledge about security but didn't change its behavior |
| Quick /debug workflow trigger | No way to invoke the skill — users had to describe the debugging workflow every time |
| Live Jira ticket data in answers | Skill contained stale data from 3 weeks ago |

---

### When Skills ARE the Best Choice

Skills genuinely are the best choice when:

- You have **domain reference material** (API docs, framework guides, cheatsheets)
- You want Copilot to **answer questions** about a topic (not enforce rules about it)
- The content is **static** (doesn't change daily)
- The content is **long** (100-500 lines) — too long for instructions
- You want content to load **only when relevant** (not on every request)

**The right mental model:**

```text
Skills are ONE of 6 tools. Each tool has a specific job.
Using only skills is like putting on a play with only the script.
You also need actors (agents), stage directions (instructions),
cues (prompts), and live effects (MCP servers).
```

---

### The 1-Minute Decision Chart (Newbie-Friendly)

**Memorize these 5 sentences and you'll always pick the right primitive:**

```text
1. "Copilot must ALWAYS do this"                → instruction
2. "Copilot should KNOW this when relevant"      → skill
3. "I want to TRIGGER a specific task"           → prompt
4. "I want Copilot to BECOME someone different"  → agent
5. "I need LIVE data from an external system"    → MCP server
```

---

## Part 10: Official Resources & Standards

> **Authoritative sources for GitHub Copilot customization — official documentation,
> specifications, and standards referenced throughout this guide.**

### VS Code Copilot Documentation

| Resource | URL | What It Covers |
|---|---|---|
| Copilot Customization Overview | [code.visualstudio.com/docs/copilot/customization](https://code.visualstudio.com/docs/copilot/customization) | Entry point for all customization features |
| Custom Instructions | [code.visualstudio.com/.../custom-instructions](https://code.visualstudio.com/docs/copilot/customization/custom-instructions) | `.instructions.md` format, `applyTo` globs, stacking |
| Prompt Files | [code.visualstudio.com/.../prompt-files](https://code.visualstudio.com/docs/copilot/customization/prompt-files) | `.prompt.md` format, variables, modes, frontmatter |
| Custom Agents | [code.visualstudio.com/.../custom-agents](https://code.visualstudio.com/docs/copilot/customization/custom-agents) | `.agent.md` format, tool restrictions, personas |
| Agent Skills | [code.visualstudio.com/.../agent-skills](https://code.visualstudio.com/docs/copilot/customization/agent-skills) | `SKILL.md` format, description-based activation |
| Copilot Chat Guide | [code.visualstudio.com/docs/copilot/chat](https://code.visualstudio.com/docs/copilot/chat) | Chat modes (ask/agent/edit), tool usage |
| VS Code Release Notes | [code.visualstudio.com/updates](https://code.visualstudio.com/updates/) | Track when new Copilot features ship |

### GitHub Documentation

| Resource | URL | What It Covers |
|---|---|---|
| GitHub Copilot Docs | [docs.github.com/en/copilot](https://docs.github.com/en/copilot) | Product overview, plans, features, billing |
| Repository Custom Instructions | [docs.github.com/.../adding-repository-custom-instructions](https://docs.github.com/en/copilot/customizing-copilot/adding-repository-custom-instructions-for-github-copilot) | `copilot-instructions.md` on GitHub.com (not VS Code) |
| GitHub Copilot Features | [github.com/features/copilot](https://github.com/features/copilot) | Product page, feature comparison across plans |
| Awesome Copilot | [github.com/github/awesome-copilot](https://github.com/github/awesome-copilot) | Community examples, custom instructions, integrations |

### Model Context Protocol (MCP)

| Resource | URL | What It Covers |
|---|---|---|
| MCP Specification | [spec.modelcontextprotocol.io](https://spec.modelcontextprotocol.io/) | Full protocol spec — transports, capabilities, lifecycle |
| MCP Official Servers | [github.com/modelcontextprotocol/servers](https://github.com/modelcontextprotocol/servers) | Reference server implementations |
| MCP TypeScript SDK | [github.com/modelcontextprotocol/typescript-sdk](https://github.com/modelcontextprotocol/typescript-sdk) | Build MCP servers in TypeScript/Node.js |
| MCP Python SDK | [github.com/modelcontextprotocol/python-sdk](https://github.com/modelcontextprotocol/python-sdk) | Build MCP servers in Python |
| MCP Java SDK | [github.com/modelcontextprotocol/java-sdk](https://github.com/modelcontextprotocol/java-sdk) | Build MCP servers in Java (Spring AI integration) |
| MCP Inspector | [github.com/modelcontextprotocol/inspector](https://github.com/modelcontextprotocol/inspector) | Debug and test MCP servers interactively |

### LLM Model Documentation

| Resource | URL | What It Covers |
|---|---|---|
| OpenAI Platform Docs | [platform.openai.com/docs](https://platform.openai.com/docs/) | GPT-4o, o3-mini capabilities, context limits |
| Anthropic Claude Docs | [docs.anthropic.com](https://docs.anthropic.com/) | Claude Sonnet/Opus capabilities, context limits |
| GitHub Copilot Model Selection | [docs.github.com/.../copilot-chat](https://docs.github.com/en/copilot/using-github-copilot/ai-models/changing-the-ai-model-for-github-copilot-chat) | How to switch models in Copilot Chat |

### Related Standards

| Standard | URL | Relevance |
|---|---|---|
| Conventional Commits | [conventionalcommits.org](https://www.conventionalcommits.org/) | Commit message format (used in prompt templates) |
| Semantic Versioning | [semver.org](https://semver.org/) | Version numbering (used in prompt templates) |
| JSON Schema | [json-schema.org](https://json-schema.org/) | MCP tool input schemas |
| JSON-RPC 2.0 | [jsonrpc.org/specification](https://www.jsonrpc.org/specification) | MCP transport protocol |
| Agent Skills Standard | [agentskills.io](https://agentskills.io/) | Open standard for agent skills (adopted by Claude Code, Gemini CLI, Goose, Roo Code, OpenHands, and more) |

### Curated GitHub Repositories

> **Famous, well-maintained, open-source repositories useful for learning and reference.**

#### GHCP & AI Agent Customization

| Repository | Stars | What It Offers |
|---|---|---|
| [github/awesome-copilot](https://github.com/github/awesome-copilot) | 26k+ | Community collection of Copilot agents, instructions, skills, plugins, hooks, workflows, cookbook. Learning Hub at [awesome-copilot.github.com](https://awesome-copilot.github.com) |
| [anthropics/skills](https://github.com/anthropics/skills) | 98k+ | Reference Agent Skills implementation. Creative, Development, Enterprise, and Document skill categories. Powers Claude Code document capabilities |
| [agentskills/agentskills](https://github.com/agentskills/agentskills) | — | The open specification for Agent Skills (originally by Anthropic, now adopted across many AI coding assistants) |
| [modelcontextprotocol/servers](https://github.com/modelcontextprotocol/servers) | 35k+ | Official MCP reference server implementations (GitHub, filesystem, Slack, Google Drive, PostgreSQL, etc.) |

#### Free Ebooks & Learning Resources

| Repository | Stars | What It Offers |
|---|---|---|
| [EbookFoundation/free-programming-books](https://github.com/EbookFoundation/free-programming-books) | 384k+ | Most starred repo for free programming books. Books by language and subject, cheat sheets, free courses, interactive tutorials, podcasts, and playgrounds in 40+ languages |
| [codecrafters-io/build-your-own-x](https://github.com/codecrafters-io/build-your-own-x) | 482k+ | Step-by-step guides to recreate 30+ technologies from scratch (databases, Docker, Git, OS, compilers, neural networks, search engines, shells, etc.) |
| [practical-tutorials/project-based-learning](https://github.com/practical-tutorials/project-based-learning) | 261k+ | Project-based tutorials by programming language. Build real applications (web, mobile, games, ML, DevOps) |
| [donnemartin/system-design-primer](https://github.com/donnemartin/system-design-primer) | 290k+ | Learn large-scale system design. System design interview prep with real-world architecture examples |
| [papers-we-love/papers-we-love](https://github.com/papers-we-love/papers-we-love) | 91k+ | Classic and influential CS papers organized by topic with community reading groups |

#### Software Engineering & AI

| Repository | Stars | What It Offers |
|---|---|---|
| [iluwatar/java-design-patterns](https://github.com/iluwatar/java-design-patterns) | 91k+ | Design patterns implemented in Java — GoF + application patterns with real-world examples |
| [TheAlgorithms/Java](https://github.com/TheAlgorithms/Java) | 60k+ | All algorithms implemented in Java — sorting, searching, graphs, dynamic programming |
| [trekhleb/javascript-algorithms](https://github.com/trekhleb/javascript-algorithms) | 190k+ | Algorithms and data structures in JavaScript with explanations and further reading links |
| [f/awesome-chatgpt-prompts](https://github.com/f/awesome-chatgpt-prompts) | 120k+ | Curated prompt engineering techniques and examples for ChatGPT and other LLMs |
| [rasbt/LLMs-from-scratch](https://github.com/rasbt/LLMs-from-scratch) | 45k+ | Build a Large Language Model from scratch — step-by-step code and explanations |

---

### Key Takeaways for Each Primitive — Official Source

| Primitive | Official Doc | Key Design Intent (from Microsoft) |
|---|---|---|
| `copilot-instructions.md` | [Repository instructions](https://docs.github.com/en/copilot/customizing-copilot/adding-repository-custom-instructions-for-github-copilot) | Project-wide rules, always injected, shared across team |
| `.instructions.md` | [Custom instructions](https://code.visualstudio.com/docs/copilot/customization/custom-instructions) | File-type-scoped rules, automatic via `applyTo` glob |
| `.prompt.md` | [Prompt files](https://code.visualstudio.com/docs/copilot/customization/prompt-files) | Reusable workflows, slash commands, team-shared tasks |
| `.agent.md` | [Custom agents](https://code.visualstudio.com/docs/copilot/customization/custom-agents) | Specialist personas with constrained tool access |
| `SKILL.md` | [Agent skills](https://code.visualstudio.com/docs/copilot/customization/agent-skills) | Domain knowledge activated by semantic relevance |
| MCP servers | [MCP spec](https://spec.modelcontextprotocol.io/) | Live external data and write operations via tools |

---

## Part 11: Latest Features & API Updates (2026)

> **New features in GitHub Copilot customization as of March 2026.**
> These are sourced from the official VS Code documentation at
> [code.visualstudio.com/docs/copilot/customization](https://code.visualstudio.com/docs/copilot/customization).

---

### Always-On Instruction Files — `AGENTS.md` and `CLAUDE.md`

VS Code now recognizes **three** always-on instruction files (not just `copilot-instructions.md`):

| File | Purpose | Audience |
|---|---|---|
| `.github/copilot-instructions.md` | Copilot-specific project instructions | GitHub Copilot in VS Code |
| `AGENTS.md` | Multi-agent compatible instructions (workspace root) | Any AI coding agent that supports the convention |
| `CLAUDE.md` | Claude Code compatible instructions (workspace root) | Claude Code CLI + VS Code Claude extension |

**All three stack** — if you have all three files, Copilot combines their instructions.
Priority order: personal settings > repository files > organization files.

**When to use `AGENTS.md`:** When your project is used with multiple AI agents (Copilot,
Claude Code, Cursor, etc.) and you want a single instruction file all of them respect.

**When to use `CLAUDE.md`:** When team members use both Copilot and Claude Code. Copilot
recognizes `CLAUDE.md` and `.claude/rules` for cross-tool compatibility.

**Cross-compatibility files Copilot also reads:**

```text
.claude/rules       ← Claude Code rules (Copilot reads these too)
.claude/agents      ← Claude Code agent configs (Copilot reads these too)
```

---

### Organization-Level Sharing

Instructions and agents can now be **shared across all repos in a GitHub organization**:

| Level | Location | Priority |
|---|---|---|
| Personal | User settings or `~/.vscode/` | **Highest** — overrides everything |
| Repository | `.github/` in the repo | **Medium** — project-specific |
| Organization | `.github` repo in the GitHub org | **Lowest** — org-wide defaults |

**How it works:** Create a `.github` repository in your GitHub organization. Place
`copilot-instructions.md`, `.instructions.md`, agents, and skills there. Every repo
in the organization inherits them automatically — unless overridden at the repo level.

**Use case:** Enforce org-wide coding standards, security rules, and approved agent
personas across 100+ repositories without copying files into each one.

---

### Agent Handoffs

Agents can now **hand off** to other agents mid-conversation using the `handoffs:` frontmatter:

```yaml
---
description: Triages incoming requests and routes to specialists.
tools: ['codebase', 'search']
handoffs:
  - agent: security-reviewer
    label: Security Review
    prompt: "Please review this code for security vulnerabilities"
  - agent: debugger
    label: Debug Issue
    prompt: "Please investigate this issue"
---
You are a triage agent. Analyze the user's request and decide which
specialist agent should handle it.
```

**Handoff fields:**

| Field | Required | Purpose |
|---|---|---|
| `agent` | Yes | The agent file to hand off to (without `.agent.md` suffix) |
| `label` | No | Button label shown to the user |
| `prompt` | No | Initial prompt sent to the target agent |
| `send` | No | What context to send (`history`, `selected-context`, `nothing`) |
| `model` | No | Override the model for the handoff target |

**Use case:** Create a "router" agent that triages requests to specialists:

```text
User → Triage Agent → Security Reviewer → (returns findings)
                    → Debugger → (returns RCA)
                    → Designer → (returns architecture review)
```

---

### Subagents (Experimental)

Custom agents can now invoke other agents as **subagents** — programmatic delegation:

```yaml
---
description: Orchestrator agent that delegates subtasks.
tools: ['codebase', 'search', 'runSubagent']
---
```

**Difference from handoffs:** Handoffs transfer control to another agent (user stays
with the new agent). Subagents run in the background and return results to the calling
agent, which keeps control.

---

### New Frontmatter Fields

#### `user-invocable` (Skills and Agents)

Controls whether users can directly trigger the skill or agent:

```yaml
---
name: internal-helper
description: Internal tool used by other agents only
user-invocable: false   # Won't appear in dropdowns or / commands
---
```

Default: `true`. Set to `false` for skills/agents designed to be called only by
other agents (via handoffs or subagents).

#### `disable-model-invocation` (Skills and Agents)

Prevents the AI model from automatically invoking this skill/agent:

```yaml
---
name: dangerous-operation
description: Performs destructive database operations
disable-model-invocation: true   # Only humans can trigger this
---
```

Default: `false`. Set to `true` for high-risk operations that should require explicit
human invocation.

#### `argument-hint` (Agents, Skills, and Prompts)

Provides a hint for what the user should type after selecting the agent/skill/prompt:

```yaml
---
name: debugger
description: Expert debugger using hypothesis-driven analysis
argument-hint: Describe the bug or paste the error message
---
```

This hint text appears in the VS Code input field after the user selects the item.

---

### Skills as Slash Commands

Skills now appear in the `/` slash command menu alongside prompts. When a user types
`/my-skill`, it invokes the skill and sends its content as context for the current query.

**Before (2025):** Skills only activated via semantic matching (invisible to users).
**Now (2026):** Skills are also directly invocable via `/skill-name` in chat.

This means skills have **two activation paths:**

1. **Automatic** — Copilot loads the skill when the conversation matches the description
2. **Manual** — User types `/skill-name` to explicitly load it

---

### Three-Level Skill Loading

VS Code loads skills in three progressive levels:

| Level | What Loads | When |
|---|---|---|
| **Discovery** | `name` + `description` (frontmatter only) | Always — used for semantic matching |
| **Instructions** | Full SKILL.md body text | When Copilot decides the skill is relevant |
| **Resource access** | Files referenced by `#file:` in the skill | When the skill needs specific file context |

**Why this matters:** Only the frontmatter is loaded upfront. The full skill body is loaded
on-demand when relevant. This keeps the context window small when skills aren't needed.

---

### Generation Commands

New VS Code commands for AI-assisted creation of customization files:

| Command | What It Creates | How to Use |
|---|---|---|
| `/init` | Generates `copilot-instructions.md` from workspace analysis | Type `/init` in chat — Copilot analyzes your project and writes initial instructions |
| `/create-instruction` | Creates a new `.instructions.md` file | Type `/create-instruction` — Copilot asks you questions and generates the file |
| `/create-agent` | Creates a new `.agent.md` file | Type `/create-agent` — Copilot interviews you about the persona |
| `/create-skill` | Creates a new `SKILL.md` file | Type `/create-skill` — Copilot helps you structure domain knowledge |
| `/create-prompt` | Creates a new `.prompt.md` file | Type `/create-prompt` — Copilot helps you define the workflow |

**`/init` is particularly useful** for bootstrapping a new project — it reads your code,
package files, and config to generate tailored instructions automatically.

---

### Chat Customizations Editor (Preview)

A new VS Code command for managing all customizations in one place:

```text
Ctrl+Shift+P → "Chat: Open Chat Customizations"
```

This opens a dashboard showing:

- All instruction files and their `applyTo` patterns
- All prompt files and their descriptions
- All agent files and their tool lists
- All skills and their activation keywords

**Use case:** Quickly audit your `.github/` setup without navigating the file tree.

---

### Settings Sync for Customizations

Prompt files and instruction files now sync across devices via VS Code **Settings Sync**.
This means your personal customizations follow you to any machine.

**What syncs:**

- `.github/prompts/*.prompt.md` files
- `.github/instructions/*.instructions.md` files
- VS Code settings related to customization paths

**What does NOT sync:**

- Agent files (`.agent.md`) — these are workspace-specific
- Skills (`SKILL.md`) — these are workspace-specific
- MCP server configs (`.vscode/mcp.json`) — contain local paths
- Secrets and credentials — never synced

---

### Configurable File Locations

VS Code now supports custom directories for customization files:

| Setting | Default | What It Controls |
|---|---|---|
| `chat.instructionsFilesLocations` | `[".github/instructions"]` | Where VS Code looks for `.instructions.md` files |
| `chat.promptFilesLocations` | `[".github/prompts"]` | Where VS Code looks for `.prompt.md` files |
| `chat.agentFilesLocations` | `[".github/agents"]` | Where VS Code looks for `.agent.md` files |
| `chat.agentSkillsLocations` | `[".github/skills"]` | Where VS Code looks for `SKILL.md` files |
| `chat.useCustomizationsInParentRepositories` | `false` | Look for customizations in parent directories |

**Use case for parent repository discovery:** In monorepos, place shared customizations
in the repository root's `.github/` — child packages inherit them automatically.

---

### Agent Hooks (Preview)

Agents can now define **hook commands** that run at specific points in the agent lifecycle:

- **Pre-response hooks** — run before the agent generates a response
- **Post-response hooks** — run after the agent generates a response

Use case: Auto-run linters, formatters, or tests after an agent makes code changes.

---

### Agent Plugins (Extensions)

VS Code extensions can contribute skills to agents via `chatSkills` in their `package.json`:

```json
{
  "contributes": {
    "chatSkills": [
      {
        "name": "my-extension-skill",
        "description": "Provides context from my extension"
      }
    ]
  }
}
```

This allows extension authors to enrich Copilot with domain-specific knowledge without
requiring users to create `SKILL.md` files manually.

---

### `.chatmode.md` → `.agent.md` Rename

The previous `.chatmode.md` format has been **renamed** to `.agent.md`. VS Code still
recognizes the old format for backward compatibility, but new files should use `.agent.md`.

If you have existing `.chatmode.md` files:

```powershell
# Rename all .chatmode.md files to .agent.md
Get-ChildItem -Recurse -Filter "*.chatmode.md" | ForEach-Object {
    $newName = $_.FullName -replace '\.chatmode\.md$', '.agent.md'
    Rename-Item $_.FullName $newName
}
```

---

### Instruction Priority Hierarchy

When instructions come from multiple sources, VS Code applies them in this priority order:

```text
1. Personal (highest priority)
   └── User settings, local machine overwrites

2. Repository
   └── .github/copilot-instructions.md
   └── .github/instructions/*.instructions.md
   └── AGENTS.md, CLAUDE.md, .claude/rules

3. Organization (lowest priority)
   └── .github repo in the GitHub org
```

**Higher-priority instructions override lower ones.** This means your personal overrides beat
repository rules, and repository rules beat organization defaults.

---

### Agent Skills Open Standard

Skills follow the **Agent Skills** open standard at
[agentskills.io](https://agentskills.io/) — meaning skills you write for VS Code Copilot
are portable to other tools that support the standard (Copilot CLI, coding agent, etc.).

**Community repositories:**

- [github.com/github/awesome-copilot](https://github.com/github/awesome-copilot) —
  Curated collection of Copilot customizations and examples
- [github.com/anthropics/skills](https://github.com/anthropics/skills) —
  Anthropic's reference skills collection

---

### `description` Field Semantic Matching

The `description` field in `.instructions.md` files is now used for **semantic matching**
(not just `applyTo` glob matching). This means VS Code can activate instruction files based on
the conversation context, even if the current file doesn't match the `applyTo` pattern.

```yaml
---
applyTo: "**/*.java"
description: >
  Java best practices including SOLID principles, clean code, and modern
  Java features like records, sealed classes, and pattern matching.
---
```

Both `applyTo` (file glob) and `description` (semantic match) contribute to activation.

---

### Summary: What Changed Per Primitive

| Primitive | New in 2026 |
|---|---|
| `copilot-instructions.md` | `AGENTS.md` + `CLAUDE.md` alternatives; org-level sharing; instruction priority hierarchy; `/init` generation; Settings Sync |
| `.instructions.md` | `description` for semantic matching; configurable locations; Settings Sync |
| `.prompt.md` | `argument-hint` field; `/create-prompt` generation; configurable locations; Settings Sync |
| `.agent.md` | `handoffs:` for chaining; subagents; `user-invocable`; `disable-model-invocation`; `argument-hint`; agent hooks; `target: github-copilot`; `.chatmode.md` → `.agent.md` rename; org-level agents; `/create-agent` generation |
| `SKILL.md` | Skills as slash commands; three-level loading; `user-invocable`; `disable-model-invocation`; `argument-hint`; agent plugins (extensions); Agent Skills open standard; `/create-skill` generation |
| MCP servers | `mcp-servers` in agent frontmatter (for cloud agents); `servers` key (not `mcpServers`) in Open Preview |

---

## Part 12: Side-by-Side — Same Task, Every Primitive

> **See exactly how each primitive handles the same requirement — and why the right
> choice matters.** These examples make the abstract comparison concrete.

---

### Task A: "Enforce `final` on local variables in Java"

#### Using `copilot-instructions.md` (works but wasteful)

```markdown
# Project Instructions
- Always use `final` for local variables that don't change
```

**Result:** Rule injected on EVERY request — even Markdown edits, YAML edits, terminal
commands. Wastes tokens when you're not editing Java.

#### Using `.instructions.md` (correct choice)

```yaml
---
applyTo: "**/*.java"
---
- Always use `final` for local variables that don't change
```

**Result:** Rule activates only when editing `.java` files. Zero overhead elsewhere.

#### Using `SKILL.md` (wrong — unreliable enforcement)

```yaml
---
name: java-final
description: Use when asked about Java final keyword...
---
# Use final for local variables that don't change
```

**Result:** Only loads when Copilot judges the topic relevant. If you ask "write a getter,"
the skill may not activate — the `final` rule is silently skipped.

#### Using `.prompt.md` (wrong — requires manual trigger)

```yaml
---
name: java-final-check
description: 'Check code for missing final keywords'
---
Review the code and add final to all variables that don't change.
```

**Result:** Only runs when you type `/java-final-check`. Won't help during normal coding.

#### Using `.agent.md` (overkill)

```markdown
---
description: Java purist who insists on final everywhere.
---
You are a strict Java developer. Every local variable MUST be final...
```

**Result:** Works, but you'd need to select this agent for every session. Agents are
for sustained personas, not single rules.

#### Verdict

| Primitive | Verdict | Why |
|---|---|---|
| `copilot-instructions.md` | ⚠️ Works but wasteful | Injected everywhere, even non-Java |
| **`.instructions.md`** | **✅ Best choice** | **Scoped to Java files, automatic, zero overhead** |
| `SKILL.md` | ❌ Wrong tool | Activation is unreliable for behavioral rules |
| `.prompt.md` | ❌ Wrong tool | Requires manual trigger; rules should be automatic |
| `.agent.md` | ❌ Overkill | Persona for a single rule? No. |
| MCP server | ❌ Absurd | Building a server to say "use final"? Never. |

---

### Task B: "Explain our microservice architecture to new devs"

#### Using `copilot-instructions.md` (wrong — too much for always-on)

```markdown
# Architecture
Our system has 12 microservices: OrderService, UserService, PaymentService...
(200 lines of architecture knowledge)
```

**Result:** 200 lines injected into EVERY request. Most requests don't need architecture
context. Wastes ~400 tokens per request.

#### Using `SKILL.md` (correct choice)

```yaml
---
name: our-architecture
description: >
  Use when asked about our microservice architecture, service boundaries,
  inter-service communication, or system design decisions.
---
# Microservice Architecture Reference
## Services
| Service | Responsibility | API | Dependencies |
|---|---|---|---|
| OrderService | Order lifecycle | REST | UserService, PaymentService |
...
```

**Result:** Loads only when someone asks about architecture. Zero cost otherwise.
Semantic matching works perfectly for informational reference content.

#### Using `.instructions.md` (wrong — knowledge, not rules)

Instructions are behavioral directives. "Our system has 12 microservices" isn't a rule
Copilot must follow — it's knowledge Copilot should know.

#### Verdict

| Primitive | Verdict | Why |
|---|---|---|
| `copilot-instructions.md` | ❌ Token waste | 200 lines on every request |
| `.instructions.md` | ❌ Wrong type | Knowledge, not rules |
| **`SKILL.md`** | **✅ Best choice** | **Loads on-demand, semantic activation** |
| `.prompt.md` | ⚠️ If structured walkthrough needed | `/architecture` could work as guided tour |
| `.agent.md` | ❌ Overkill | No need for persona change |
| MCP server | ❌ Static content | No live data needed |

---

### Task C: "Run a structured security audit on this class"

#### Using `.prompt.md` + `.agent.md` (correct — workflow + persona)

```yaml
# .github/agents/security-auditor.agent.md
---
description: >
  Security-focused code reviewer. OWASP Top 10, injection checks,
  auth/authz analysis. Read-only — never modifies code.
tools:
  - codebase
  - search
  - usages
---
You are a security auditor. Be skeptical. Assume everything is vulnerable
until proven otherwise. Check OWASP Top 10 categories systematically.
```

```yaml
# .github/prompts/security-audit.prompt.md
---
name: security-audit
description: 'Run OWASP Top 10 audit on the current file'
agent: security-auditor
---
${input:scope:What to audit? (current file / selected code / entire module)}

## Audit Steps
1. Injection (SQL, XSS, command)
2. Broken auth
3. Sensitive data exposure
...
Output a findings table: severity | category | location | recommendation
```

**Result:** Type `/security-audit` → structured, reproducible audit with expert mindset.

#### Using `SKILL.md` only (insufficient — no workflow, no persona)

A skill could provide OWASP reference knowledge, but can't structure the audit workflow
or change Copilot's skeptical mindset. Best used alongside the agent+prompt combo.

#### Verdict

| Primitive | Verdict | Why |
|---|---|---|
| `copilot-instructions.md` | ❌ Not a universal rule | Security audits are on-demand, not every-request |
| `.instructions.md` | ❌ Too narrow | Rules can't define multi-step workflows |
| `SKILL.md` | ⚠️ Complementary | OWASP reference = skill; audit process = prompt |
| **`.prompt.md` + `.agent.md`** | **✅ Best choice** | **Workflow structure + expert persona** |
| MCP server | ⚠️ If scanning tool needed | Could wrap a SAST tool as MCP |

---

### Task D: "Let Copilot create PRs and read Jira tickets"

Only one primitive can do this: **MCP server**.

No `.md` file can make API calls. The other 5 primitives inject text into Copilot's
context — only MCP servers give Copilot real hands to interact with external systems.

```jsonc
// .vscode/mcp.json
{
  "servers": {
    "github": { "type": "stdio", "command": "npx", "args": ["-y", "@modelcontextprotocol/server-github"] },
    "jira": { "type": "stdio", "command": "java", "args": ["-jar", "mcp-servers/out/atlassian-server.jar"] }
  }
}
```

**Complementary skill:** Add a skill with Jira project conventions (ticket naming,
story point scale, workflow states) so Copilot knows HOW to write good tickets,
while MCP gives it the ability to CREATE them.

---

## Part 13: Token Economics & Performance

> **Every customization costs tokens. Understanding the budget helps you build
> efficient stacks that don't waste context window space.**

---

### How Tokens Are Consumed

```text
┌─────────────────────────────────────────────────────────┐
│                    CONTEXT WINDOW                        │
│                                                          │
│  System prompt (Copilot built-in)          ~1 500 tokens │
│  copilot-instructions.md (always)          ~200–800      │
│  Active .instructions.md files             ~100–500 each │
│  Active SKILL.md files                     ~200–2 000    │
│  .agent.md persona (if selected)           ~100–400      │
│  .prompt.md template (if invoked)          ~50–200       │
│  MCP tool schemas (all registered)         ~50–200 each  │
│  MCP tool results (when called)            ~100–2 000    │
│  Conversation history                      variable      │
│  User's current message                    variable      │
│  ──────────────────────────────────────────────────────  │
│  REMAINING: available for Copilot's response             │
└─────────────────────────────────────────────────────────┘
```

### Token Budget by Primitive

| Primitive | Injected When | Typical Tokens | Budget Guidance |
|---|---|---|---|
| `copilot-instructions.md` | Every request | 200–800 | Keep under 500 — this is your "tax" on every interaction |
| `.instructions.md` | File pattern matches | 100–300 each | Lean rules; 20-60 lines typical |
| `SKILL.md` | Semantic match | 200–2 000 | Larger is OK — only loads when relevant |
| `.agent.md` | Session start | 100–400 | Moderate — persona context for whole session |
| `.prompt.md` | Per invocation | 50–200 | Lean templates; depth comes from skills/agents |
| MCP schemas | Agent mode active | 50–200 per server | Descriptive schemas help Copilot pick the right tool |
| MCP results | Per tool call | 100–2 000 | Trim large results — return summaries, not raw dumps |

### Optimization Strategies

**🟢 Newbie:** Keep `copilot-instructions.md` under 80 lines. That's it.

**🟡 Amateur:**

- Move reference blocks from instructions to skills (load-on-demand vs. always-on)
- Use `applyTo` globs to scope instructions narrowly — don't pollute non-matching files
- Write concise skill sections — tables > paragraphs for token efficiency
- Use thin prompts that delegate depth to the matching skill

**🔴 Pro:**

- Audit your full stack: open a file, estimate total tokens loaded
- Use the 3-level skill loading (discovery → instructions → resources) — VS Code already
  does this, but awareness helps you structure skills with the most important content first
- Split large skills (>500 lines) into focused sub-skills — only the relevant one loads
- For agents, avoid duplicating instructions content in the persona — agents STACK on
  instructions, so the agent body only needs unique persona context
- MCP tool schemas should use concise `description` fields — verbose descriptions waste tokens
- Return structured JSON from MCP tools, not prose — smaller payload, easier for LLM to parse

### Cost of Stacking (Real-World Estimate)

A typical "full stack" session editing a Java file:

```text
Component                            Tokens    Cumulative
─────────────────────────────────────────────────────────
Copilot system prompt                ~1 500    1 500
copilot-instructions.md              ~400      1 900
java.instructions.md                 ~200      2 100
clean-code.instructions.md           ~150      2 250
Designer agent persona               ~300      2 550
design-review prompt                 ~100      2 650
design-patterns SKILL.md             ~800      3 450
GitHub MCP schemas                   ~150      3 600
─────────────────────────────────────────────────────────
Total context overhead               ~3 600    (~2 100 from your files)
```

With a 128K context window, 3 600 tokens is ~3% overhead — well within budget even
for long conversations. The cost is bearable because each component earns its keep.

---

## Part 14: Testing Your Customizations

> **How do you know your instructions, skills, agents, and prompts actually work?
> Here's a systematic testing methodology.**

---

### Testing by Primitive Type

#### Testing Instructions

```text
Method: Open a matching file → ask Copilot to write code → verify the rule is followed

Test script for java.instructions.md (applyTo: **/*.java):
  1. Open any .java file
  2. Ask: "Write a method that parses a CSV line"
  3. Check: Does the generated code use `final` for local variables? (if that's your rule)
  4. Check: Does it include Javadoc? (if that's your rule)
  5. Check: Is the method under 30 lines? (if that's your rule)

Negative test:
  6. Open a .py file
  7. Ask the same question
  8. Check: Java rules should NOT apply to Python output
```

#### Testing Skills

```text
Method: Ask a question that should activate the skill → verify skill content appears

Test script for mcp-development/SKILL.md:
  1. Ask: "How do I create an MCP server in TypeScript?"
  2. Check: Response includes MCP-specific details (tool registration, JSON-RPC, stdio)
  3. Check: Response references the SDK (modelcontextprotocol/typescript-sdk)

Activation test:
  4. Ask: "What's the weather today?" (unrelated question)
  5. Check: MCP skill should NOT activate for unrelated queries

Edge case:
  6. Ask: "How do I add a tool to my Copilot setup?"
  7. Check: Should the MCP skill activate? (Yes — "tool" + "Copilot" is semantically close)
```

#### Testing Agents

```text
Method: Select agent → ask domain questions → verify persona is active

Test script for debugger.agent.md:
  1. Select "Debugger" from Chat dropdown
  2. Ask: "This method returns null sometimes"
  3. Check: Does the agent use hypothesis-driven approach?
  4. Check: Does it list possible causes systematically?
  5. Check: Does it ask clarifying questions before jumping to solutions?

Tool restriction test:
  6. If agent has tools: [codebase, search] (no editFiles)
  7. Ask: "Fix this bug by editing the file"
  8. Check: Agent should explain what to change but NOT edit files directly
```

#### Testing Prompts

```text
Method: Type the slash command → verify the workflow structure is followed

Test script for /design-review:
  1. Type: /design-review
  2. Fill in any variables (topic, scope, etc.)
  3. Check: Does the response follow the prompt's template structure?
  4. Check: Is the correct agent auto-selected (if agent: field is set)?
  5. Check: Are the output sections in the expected order?
```

#### Testing MCP Servers

```text
Method: In agent mode, ask Copilot to use the tool → verify real data returns

Test script for GitHub MCP:
  1. Ensure agent mode is active
  2. Ask: "List open PRs in this repo"
  3. Check: Does Copilot call the MCP tool (visible in response metadata)?
  4. Check: Does the result contain REAL, current data (not hallucinated)?
  5. Check: Error handling — what happens if the server is down?
```

### Systematic Test Matrix

| What To Test | How To Test | Pass Criteria |
|---|---|---|
| Instruction activation (positive) | Open matching file, ask for code | Rule is followed |
| Instruction activation (negative) | Open non-matching file, same question | Rule is NOT applied |
| Instruction stacking | Open file matching multiple globs | ALL matching rules applied |
| Skill activation (positive) | Ask topic-relevant question | Skill content in response |
| Skill activation (negative) | Ask unrelated question | Skill NOT in response |
| Multi-skill activation | Ask cross-domain question | Multiple skills contribute |
| Agent persona | Select agent, ask domain question | Persona is evident in style |
| Agent tool restriction | Request action agent can't do | Agent refuses or explains limitation |
| Prompt structure | Invoke slash command | Response matches template |
| Prompt + agent | Invoke prompt with `agent:` field | Correct agent auto-selected |
| MCP tool call | Request external data in agent mode | Real data returned |
| MCP error handling | Request data with server down | Graceful error, no crash |
| Full stack | All 6 active simultaneously | No conflicts, each contributes |

### Debugging Activation Issues

```text
Symptom: "My skill isn't activating"
→ Check description field — does it contain the keywords you're asking about?
→ Try: /skill-name to manually invoke it (2026 feature)
→ Rephrase your question to include exact words from the description

Symptom: "My instruction is being ignored"
→ Open VS Code Output → select "GitHub Copilot" → look for loaded files
→ Check: is applyTo glob correct? Test at globtester.com
→ Check: is a higher-priority source (agent, prompt) overriding?

Symptom: "My agent isn't in the dropdown"
→ Check: file is in .github/agents/ with .agent.md extension
→ Check: description field exists in frontmatter (required)
→ Reload window: Ctrl+Shift+P → "Developer: Reload Window"

Symptom: "MCP tools not available"
→ Check: must be in Agent mode (not Ask or Edit mode)
→ Check: VS Code Output → "MCP" → look for connection errors
→ Check: did the server process actually start?
```

---

## Part 15: Cross-Repo Portability

> **How to design customizations that travel between repositories — personal projects,
> team repos, org-wide standards, and open-source contributions.**

---

### Portability Tiers

| Tier | What Travels | How |
|---|---|---|
| **Copy-paste portable** | `.md` files with no project-specific references | Copy `.github/` folder wholesale |
| **Adapt-and-go** | `.md` files with project-specific examples | Copy, then search-replace project names |
| **Template-based** | Parameterized files with `${input:}` variables | Copy as-is; variables prompt at runtime |
| **Org-level** | Files in the `.github` org repo | Automatic inheritance to all repos |
| **Agent Skills standard** | `SKILL.md` files following agentskills.io | Portable across VS Code, Claude Code, Gemini CLI |

### What Ports Easily vs. What Doesn't

| Primitive | Portability | Friction Points |
|---|---|---|
| `copilot-instructions.md` | ⚠️ Medium | Project-specific naming, conventions, structure references |
| `.instructions.md` | ✅ High | Generic rules port well; `applyTo` globs usually work across repos |
| `SKILL.md` | ✅ High | Domain knowledge is inherently portable (Java skills work in any Java project) |
| `.agent.md` | ✅ High | Personas are project-agnostic (a "debugger" works anywhere) |
| `.prompt.md` | ✅ High | Workflows port well; `${input:}` variables handle project differences |
| MCP servers | ❌ Low | Require build setup, dependencies, server processes, credentials |

### Designing for Portability

**🟢 Newbie — Just Copy the Files**

```text
1. Copy the .github/ folder from this repo to your new project
2. Delete files you don't need
3. Edit copilot-instructions.md to match your new project's conventions
4. Done — instructions, skills, agents, and prompts all work immediately
```

**🟡 Amateur — Adapt with a Checklist**

When porting `.github/` to a new repo:

```text
☐ copilot-instructions.md — update project name, language, conventions
☐ .instructions.md files — keep generic ones, remove project-specific ones
☐ SKILL.md files — keep domain skills (Java, Git, MCP), remove project-specific skills
☐ .agent.md files — keep all (personas are universal)
☐ .prompt.md files — keep all (workflows are universal)
☐ MCP servers — don't copy; configure from scratch per project
☐ .vscode/mcp.json — don't copy; configure per environment
```

**🔴 Pro — Organization-Level Architecture**

```text
.github org repo (github.com/<org>/.github):
  copilot-instructions.md    ← org-wide standards (priority: lowest)
  instructions/
    security.instructions.md ← org-wide security rules
    testing.instructions.md  ← org-wide test standards
  agents/
    security-auditor.agent.md ← shared security persona
  skills/
    company-architecture/SKILL.md ← company conventions

Each project repo:
  .github/
    copilot-instructions.md  ← project-specific overrides (priority: medium)
    skills/
      project-domain/SKILL.md ← project-specific knowledge

Developer's machine:
  VS Code settings → personal instruction overrides (priority: highest)
```

### Cross-Tool Portability (Agent Skills Standard)

Skills written for VS Code Copilot are portable to any tool supporting the
[Agent Skills](https://agentskills.io/) standard:

| Tool | Reads `SKILL.md`? | Reads `.instructions.md`? | Reads `.agent.md`? |
|---|---|---|---|
| VS Code Copilot | ✅ | ✅ | ✅ |
| Claude Code | ✅ (via AGENTS.md) | ❌ (uses CLAUDE.md) | ❌ (uses .claude/agents) |
| Gemini CLI | ✅ | ❌ | ❌ |
| Goose | ✅ | ❌ | ❌ |
| Roo Code | ✅ | ❌ | ❌ |
| OpenHands | ✅ | ❌ | ❌ |
| Qodo | ✅ | ❌ | ❌ |

**Key insight:** If you want maximum cross-tool portability, invest most heavily in
`SKILL.md` files and `AGENTS.md` — these are the most widely supported formats.
Instructions and prompts are VS Code-specific (for now).

### Multi-Tool Instruction Strategy

For teams using both VS Code Copilot and Claude Code:

```text
.github/
  copilot-instructions.md   ← VS Code Copilot reads this
  AGENTS.md                  ← Both Copilot and Claude Code read this
  CLAUDE.md                  ← Claude Code reads this; Copilot also reads for compat
  .claude/rules              ← Claude Code rules; Copilot also reads
  skills/
    <domain>/SKILL.md        ← Agent Skills standard — all tools read this
```

**Recommendation:** Put the most critical, universal rules in `AGENTS.md` — it has the
broadest compatibility across AI coding tools.

---

## Part 16: Complete API Surface Reference

> **Every frontmatter field, every tool name, every glob pattern — in one place.**

---

### Frontmatter Fields — Universal Reference

#### `copilot-instructions.md`

```yaml
# No frontmatter — VS Code recognizes the filename
```

#### `.instructions.md`

```yaml
---
applyTo: "**/*.java"          # Required: glob pattern for file matching
description: >                # Optional (2026): semantic matching trigger
  Java best practices including SOLID, clean code, modern features.
---
```

#### `.prompt.md`

```yaml
---
name: my-command                # Required: slash command name (/my-command)
description: 'Brief text'      # Required: autocomplete description
agent: agent-name               # Optional: auto-select agent on invocation
tools:                          # Optional: restrict tools (default: all)
  - codebase
  - search
  - editFiles
mode: ask                       # Optional: 'ask' | 'agent' | 'edit'
argument-hint: 'Enter topic'    # Optional (2026): hint text after selection
---
```

#### `.agent.md`

```yaml
---
description: >                  # Required: when to use (shown in dropdown)
  Use when you need X. Expert in Y, Z.
tools:                          # Optional: restrict tools (default: ALL)
  - codebase
  - search
  - usages
  - editFiles
  - runCommands
  - problems
  - fetch
  - findTestFiles
  - terminalLastCommand
  - terminalSelection
  - testFailure
model: gpt-4o                   # Optional (2026): pin model
handoffs:                        # Optional (2026): agent handoff chain
  - agent: target-agent
    label: 'Button Label'
    prompt: 'Initial message'
    send: history                # 'history' | 'selected-context' | 'nothing'
    model: claude-opus-4-5       # Optional: override model for handoff
user-invocable: true             # Optional (2026): show in UI (default: true)
disable-model-invocation: false  # Optional (2026): prevent auto-invocation
argument-hint: 'Describe bug'   # Optional (2026): input hint text
target: github-copilot          # Optional (2026): for cloud/coding agents
mcp-servers:                     # Optional (2026): MCP servers for cloud agents
  - server-name
---
```

#### `SKILL.md`

```yaml
---
name: skill-identifier          # Required: kebab-case identifier
description: >                  # Required: activation trigger (semantic matching)
  Use when asked about [topic A], [topic B], or [use case C].
  Covers: [subtopic X], [subtopic Y].
user-invocable: true             # Optional (2026): show as /skill-name
disable-model-invocation: false  # Optional (2026): prevent auto-activation
argument-hint: 'Enter topic'     # Optional (2026): hint text
---
```

### Tool Names Reference

Tools available in `tools:` frontmatter arrays:

| Tool Name | What It Does | Category |
|---|---|---|
| `codebase` | Read files in the workspace (not editable) | Read |
| `search` | Search text in workspace files | Read |
| `usages` | Find references/usages of a symbol | Read |
| `problems` | Read VS Code Problems panel | Read |
| `findTestFiles` | Find test files associated with a source file | Read |
| `terminalLastCommand` | Read the last terminal command output | Read |
| `terminalSelection` | Read selected text in the terminal | Read |
| `testFailure` | Read failing test details | Read |
| `editFiles` | Create and edit workspace files | Write |
| `runCommands` | Execute terminal commands | Write |
| `fetch` | Fetch URLs (web pages, APIs) | Network |
| `runSubagent` | Invoke another agent as a subagent | Agent |

**Security categories:**

- **Read** tools: safe for read-only agents
- **Write** tools: restrict for review/audit agents
- **Network** tools: restrict for sandboxed agents
- **Agent** tools: for orchestrator agents only

### Glob Pattern Reference

Common `applyTo` patterns for `.instructions.md`:

| Pattern | Matches | Use Case |
|---|---|---|
| `**` | Everything | Steering modes (completeness, formatting) |
| `**/*.java` | All Java files | Java coding rules |
| `**/*.{ts,tsx}` | TypeScript files | React/TS conventions |
| `**/*.{js,jsx}` | JavaScript files | JS conventions |
| `**/*.py` | Python files | Python conventions |
| `**/*.md` | Markdown files | Documentation rules |
| `**/*Test.java` | Java test files | Test-specific conventions |
| `**/*Spec.ts` | TypeScript spec files | Test-specific conventions |
| `**/controller/**/*.java` | Controller-layer Java | API design rules |
| `**/service/**/*.java` | Service-layer Java | Business logic rules |
| `**/pom.xml` | Maven POM files | Maven-specific conventions |
| `**/Dockerfile` | Dockerfiles | Container build rules |
| `**/*.gradle` | Gradle files | Build script rules |
| `**/.github/**` | All .github content | Meta-customization rules |

### Chat Mode Reference

The `mode:` field in `.prompt.md`:

| Mode | Behavior | Best For |
|---|---|---|
| `ask` | Copilot responds conversationally, no file edits | Explanations, analysis, reviews |
| `agent` | Copilot can use tools (edit, search, terminal) | Implementation, refactoring |
| `edit` | Copilot directly edits the current file | Inline code changes |

---

## Part 17: Security Considerations

> **Customization files control what Copilot can do. Security matters.**

---

### Threat Model

| Threat | Surface | Mitigation |
|---|---|---|
| **Prompt injection via skill content** | Malicious content in `SKILL.md` could override instructions | Review skill content; don't auto-generate from untrusted sources |
| **Overprivileged agents** | Agent without `tools:` restriction gets all tools including `runCommands` | Always specify explicit `tools:` list; default to read-only |
| **MCP credential exposure** | API tokens in `.vscode/mcp.json` committed to git | Use `${input:}` variables with `password: true`; add `mcp.json` to `.gitignore` |
| **MCP server code injection** | MCP server constructs commands from user input | Validate/sanitize all inputs; use parameterized queries/commands |
| **Org-level instruction override** | Org-wide rules could be overridden by repo-level files | Audit priority hierarchy; use org policies for critical security rules |
| **Instruction manipulation** | PR adds malicious rules to `.instructions.md` | Code review `.github/` changes carefully; treat them as security-sensitive |

### Agent Security Best Practices

```text
PRINCIPLE: Least Privilege — agents should have the minimum tools needed.

Read-Only Agent (reviews, analysis):
  tools: [codebase, search, usages, problems]

Implementation Agent (writes code):
  tools: [codebase, search, usages, editFiles]

Full-Power Agent (admin tasks):
  tools: [codebase, search, usages, editFiles, runCommands, fetch]
  ← Only for trusted, well-tested agents
```

**Never omit the `tools:` field** on agents that should be restricted. The default is
ALL tools — a "read-only reviewer" without `tools:` can run `rm -rf`.

### MCP Server Security

```text
✅ DO:
- Use ${input:} variables for all secrets (they prompt at runtime, don't persist)
- Add .vscode/mcp.json to .gitignore if it contains local credentials
- Validate all tool inputs before executing commands
- Return minimal data (don't dump entire DB tables)
- Log tool invocations for audit

❌ DON'T:
- Hardcode API tokens in mcp.json
- Build MCP tools that execute arbitrary shell commands from user input
- Return sensitive data (passwords, tokens, PII) in tool results
- Trust tool input without validation — it comes from the LLM, not the user
```

### Secure `.github/` Code Review Checklist

When reviewing PRs that modify `.github/` customization files:

```text
☐ No new always-on instructions that could override security rules
☐ Agent tool lists are appropriately restricted
☐ Skill descriptions don't contain prompt injection payloads
☐ Prompt templates don't bypass safety guardrails
☐ MCP configuration doesn't expose credentials
☐ No instructions that disable safety features or bypass verification steps
```

---

## Part 18: Versioning & Evolving Customizations

> **Customizations aren't "set and forget." They evolve with your team, codebase, and
> the Copilot platform itself.**

---

### Evolution Patterns

#### Phase 1: Bootstrap (Week 1)

```text
.github/
├── copilot-instructions.md   ← 30 lines: naming, language, commit format
└── instructions/
    └── java.instructions.md  ← 15 lines: Java-specific rules
```

#### Phase 2: Knowledge (Week 2-4)

```text
.github/
├── copilot-instructions.md
├── instructions/
│   ├── java.instructions.md
│   └── clean-code.instructions.md
└── skills/
    ├── project-architecture/SKILL.md  ← your domain knowledge
    └── java-patterns/SKILL.md         ← team patterns
```

#### Phase 3: Workflows (Month 2)

```text
.github/
├── copilot-instructions.md
├── instructions/ (3-4 files)
├── skills/ (4-6 domains)
├── prompts/
│   ├── review.prompt.md       ← team code review workflow
│   ├── test-gen.prompt.md     ← test generation
│   └── debug.prompt.md        ← debugging workflow
└── agents/
    └── code-reviewer.agent.md ← team reviewer persona
```

#### Phase 4: Mature (Month 3+)

```text
.github/
├── copilot-instructions.md
├── instructions/ (5-8 files, including steering modes)
├── skills/ (8-12 domains)
├── prompts/ (10-20 commands)
├── agents/ (3-5 specialists)
└── docs/ (guides, references, onboarding)
[Plus]
.vscode/
└── mcp.json (2-3 MCP servers)
```

### Version Control for Customizations

**Treat `.github/` as first-class code:** review PRs, write commit messages, test changes.

```text
feat(skills): Add kubernetes deployment patterns skill

New SKILL.md with 3-tier reference (beginner/intermediate/advanced)
covering kubectl commands, deployment strategies, and common
troubleshooting patterns.

— created by gpt
```

### Breaking Changes

When a customization change could surprise team members:

| Change | Impact | Communication |
|---|---|---|
| New always-on instruction | Changes Copilot behavior for everyone | Team notification + PR review |
| Renamed slash command | Existing muscle memory broken | Deprecation period with alias |
| Removed agent | Users lose a persona they rely on | Announce replacement or merge |
| New MCP server | New tool buttons appear in agent mode | Demo in team meeting |
| Description field change in skill | Skill may activate for different queries | Test activation before/after |

### Deprecation Pattern

```text
1. Add "DEPRECATED" to the old file's description
2. Create the replacement file
3. Update cross-references
4. Wait 2 sprints
5. Delete the old file
```

---

**Navigation:** [← START-HERE](START-HERE.md) · [Newbie Guide →](copilot-customization-newbie.md)
· [Customization Guide →](customization-guide.md) · [Primitives Cheatsheet →](primitives-at-a-glance.md)
· [MCP vs Skills →](mcp-vs-skills.md) · [Export Guide →](export-guide.md)
