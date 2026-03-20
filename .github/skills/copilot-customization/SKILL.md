---
name: copilot-customization
description: >
  GitHub Copilot customization — all 6 primitives (copilot-instructions.md, .instructions.md,
  .prompt.md, .agent.md, SKILL.md, MCP servers). Use when asked about how to customize Copilot,
  which customization type to use, how to create instructions or prompts or agents or skills,
  differences between customization types, composition of multiple customizations, when to use
  skills vs instructions vs prompts vs agents vs MCP servers, frontmatter syntax, activation rules,
  stacking order, building a .github/ customization strategy, why not just use skills for everything,
  migrating between primitive types, converting instructions to skills, or choosing the right
  customization approach for a repository.
---

# GitHub Copilot Customization — Skill Reference

---

## 🟢 Quick Decision Card

| I want to... | Use |
|---|---|
| Enforce rules for ALL files, ALWAYS | `copilot-instructions.md` |
| Enforce rules for specific file types only | `.instructions.md` (`applyTo: "**/*.java"`) |
| Make Copilot deeply know a domain | `SKILL.md` |
| Create a repeatable workflow (slash command) | `.prompt.md` |
| Give Copilot a specialized expert persona | `.agent.md` |
| Let Copilot read/write external systems (GitHub, Jira, DB) | MCP Server |

---

## The 6 Primitives — At a Glance

```text
Primitive               File(s)                         Activation      Content Type
──────────────────────────────────────────────────────────────────────────────────────
copilot-instructions    .github/copilot-instructions.md  Always on       Behavioral rules
.instructions.md        .github/instructions/*.md         Auto (glob)     Behavioral rules
SKILL.md                .github/skills/<domain>/SKILL.md  Auto (semantic) Domain knowledge
.agent.md               .github/agents/*.agent.md         Manual (drop)   Persona + tools
.prompt.md              .github/prompts/*.prompt.md        Manual (/cmd)   Workflow template
MCP Server              .vscode/mcp.json + server code    Auto (agent)    External tools/APIs
```

---

## Detailed Reference by Type

### 1. `copilot-instructions.md` — Project Foundation

**Location:** `.github/copilot-instructions.md` (exactly ONE file)
**Activation:** Every single request — no conditions
**Role:** Project-wide non-negotiable ground rules
**Stacks:** Always the base layer; everything else adds on top
**Size:** Keep under 2 000 tokens — it's sent on EVERY request

**Frontmatter:** None required (VS Code recognizes the filename)

**What belongs here:**
- Project naming conventions (classes, methods, constants, packages)
- Coding language version (Java 21+, ES2022, Python 3.12)
- Project vocabulary / glossary
- Commit message format
- The 5-10 most critical "never do this" rules
- Links to other resources Copilot should know about

**What does NOT belong here:**
- Large reference tables (use SKILL.md)
- File-type-specific rules (use .instructions.md with applyTo)
- Persona instructions (use .agent.md)
- Detailed workflows (use .prompt.md)

---

### 2. `.instructions.md` — File-Type Specialists

**Location:** `.github/instructions/*.instructions.md` (many files allowed)
**Activation:** Automatic when `applyTo` glob matches the open file
**Role:** File-type-specific coding standards that stack on base rules
**Stacks:** Yes — multiple files matching the same file all activate together

**Frontmatter (required):**

```yaml
---
applyTo: "**/*.java"           # Required — one glob string
---
```

**Multi-glob (stack multiple patterns):**

```yaml
---
applyTo: "**/*.java"
---
```

> To apply one file to multiple globs, create multiple instruction files or use `"**"` for global.

**Glob pattern guide:**
| Pattern | Matches |
|---|---|
| `**/*.java` | All Java files anywhere |
| `src/**/*.java` | Java files under src/ only |
| `**/*Test.java` | Java test files |
| `**/*.{ts,tsx}` | TypeScript and TSX files |
| `**/pom.xml` | Maven POM files |
| `**` | Every file (steering mode pattern) |

**Stacking example:**

```text
Open file: UserServiceTest.java

Active instructions:
  1. copilot-instructions.md           (always)
  2. java.instructions.md              (applyTo: **/*.java ✓)
  3. clean-code.instructions.md        (applyTo: **/*.java ✓)
  4. test.instructions.md              (applyTo: **/*Test.java ✓)

All 4 are merged and active simultaneously.
```

---

### 3. `SKILL.md` — Domain Knowledge Base

**Location:** `.github/skills/<domain-name>/SKILL.md`
**Activation:** Automatic — VS Code semantically matches your question to the `description` field
**Role:** Deep informational reference (NOT behavioral rules)
**Stacks:** Yes — multiple skills can activate simultaneously

**Frontmatter (required):**

```yaml
---
name: my-skill-name
description: >
  THIS IS THE ACTIVATION TRIGGER — write it carefully.
  Use when asked about [TOPIC 1], [TOPIC 2], [USE CASE 1], [USE CASE 2].
  Covers: [subtopic A], [subtopic B], [subtopic C].
---
```

**Description field writing guide:**

```text
Too vague:   "Java stuff"                    → Never activates
Too specific: "How to create a singleton"    → Misses related questions
Just right:  "Use when asked about Java 21+, JVM, concurrency, streams,
              Spring, Maven, Gradle, or Java learning resources."
```

**Content structure best practices (3-tier):**

```markdown
## 🟢 Quick Reference (newbie)
Simple cheatsheet — works for 80% of questions

## 🟡 Detailed Guide (amateur)
Full explanations, examples, patterns

## 🔴 Advanced / Pro Tips
Edge cases, performance, architecture, deep dives
```

**What belongs in a SKILL.md:**
- Command cheatsheets (Git, Maven, kubectl, etc.)
- Architecture reference for your domain
- Pattern recipes and code templates
- Curated links to external resources
- Project-specific context (how YOUR services are structured)

---

### 4. `.agent.md` — AI Personas

**Location:** `.github/agents/*.agent.md`
**Activation:** Manual — user selects from VS Code Chat dropdown
**Role:** AI persona with specific mindset, tool access, and communication style
**Stacks:** No — one agent active at a time

**Frontmatter fields (all optional except description):**

```yaml
---
description: >                 # Required: shown in dropdown + when to use
  Use this agent when you need a security-focused review. Thinks in terms
  of OWASP Top 10, injection vectors, auth flows, and threat modeling.
tools:                         # Optional. Omit = ALL tools (security risk)
  - codebase                   # Read code (non-editable)
  - search                     # Search workspace
  - usages                     # Find symbol usages
  - fetch                      # Fetch URLs
  - editFiles                  # Can edit files  ← remove for read-only agents
  - runCommands                # Can run terminal ← remove for safe agents
  - problems                   # Read VS Code problems
  - findTestFiles              # Find associated tests (2025+)
  - terminalLastCommand        # Read last terminal output (2025+)
  - terminalSelection          # Read terminal selection (2025+)
  - testFailure                # Read test failure details (2025+)
model: gpt-4o                  # Optional (2026): pin to specific model
---
```

**Available model values (2026):**
- `gpt-4o`
- `claude-sonnet-4-5 (copilot)` / `Claude Sonnet 4.5 (copilot)`
- `o3-mini`
- `gemini-2.0-flash`

**Agent body (Markdown):**
The Markdown body is the system prompt — write it as direct instructions to the AI:

```markdown
You are a [ROLE]. Your expertise is [DOMAIN].

## How You Think
- Always [APPROACH 1]
- Focus on [FOCUS AREA]
- Ask [QUESTIONS] before [ACTIONS]

## What You Do
When the user asks for [TASK]:
1. [STEP 1]
2. [STEP 2]

## What You Never Do
- Never edit production files without explicit approval
- Never assume — always verify with codebase search
```

---

### 5. `.prompt.md` — Slash Command Workflows

**Location:** `.github/prompts/*.prompt.md`
**Activation:** Manual — user types `/command-name` in Copilot Chat
**Role:** Pre-built workflow template with inputs, steps, and output structure
**Stacks:** No — one prompt active per invocation

**Frontmatter fields:**

```yaml
---
name: design-review             # Required: /design-review in chat
description: 'Short description' # Required: shown in autocomplete
agent: designer                 # Optional: auto-select this agent
tools: ['codebase', 'search']   # Optional: restrict tools for this prompt
mode: ask                       # Optional: 'ask' | 'agent' | 'edit'
---
```

**Variable injection (powerful feature):**

```markdown
${input:varName:Prompt shown to user when they invoke the command?}

Examples:
${input:level:Your experience level? (beginner / intermediate / advanced)}
${input:topic:What topic? (overview / deep-dive / examples)}
${input:symptom:Describe the exact error or behavior}
```

**File attachment:**

```markdown
Reference the current file:
#file:${file}

Reference a specific file:
#file:.github/instructions/java.instructions.md

Reference another prompt (prompt chaining):
#file:.github/prompts/design-review.prompt.md
```

**Prompt body structure (best practices):**

```markdown
## Topic
${input:topic:...}

## Context
${input:goal:...}

## Instructions

[Main sections for Copilot to respond to]

### When topic is X:
[X-specific instructions]

### When topic is Y:
[Y-specific instructions]

### Rules
- Always [rule 1]
- Never [rule 2]
```

---

### 6. MCP Servers — External Tools

**Location:** Config at `.vscode/mcp.json`; server code anywhere (TypeScript, Python, Java, Go)
**Activation:** Always registered; Copilot agent calls tools as needed
**Role:** External process that gives Copilot real-world tool access
**Stacks:** Yes — all registered servers active simultaneously

**Workspace config (`.vscode/mcp.json`):**

```jsonc
{
  "inputs": [
    {
      "id": "githubToken",
      "type": "promptString",
      "description": "GitHub Personal Access Token",
      "password": true          // stored securely — not written to disk
    }
  ],
  "servers": {
    "github": {
      "type": "stdio",           // or "http" for Streamable HTTP
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": { "GITHUB_TOKEN": "${input:githubToken}" }
    },
    "filesystem": {
      "type": "stdio",
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-filesystem",
               "${workspaceFolder}"]
    },
    "remote": {
      "type": "http",            // Streamable HTTP (March 2026) — replaces SSE
      "url": "https://mcp.example.com/mcp",
      "headers": { "Authorization": "Bearer ${input:token}" }
    }
  }
}
```

**When to use MCP vs. SKILL.md:**
| Situation | Use |
|---|---|
| Static reference content (docs, patterns, cheatsheets) | SKILL.md |
| Live data (current GitHub issues, live DB query) | MCP Server |
| Read-only content that doesn't change | SKILL.md |
| Write operations (create PR, update Jira, push code) | MCP Server |
| Content you own and can embed | SKILL.md |
| Content owned by external services | MCP Server |

---

## Composition Recipes

### Recipe 1: Always-On Enriched Domain (Instructions + Skill)

```text
java.instructions.md (applyTo: **/*.java)   → behavioral: HOW to write Java
java-learning-resources/SKILL.md           → informational: WHAT to know about Java
```

Combined: Java code is written to standards AND Copilot knows deep Java context.

### Recipe 2: Structured Expert Workflow (Agent + Prompt)

```text
.agent.md (designer)       → WHO: architecture mindset, pattern vocabulary
.prompt.md (design-review) → WHAT: structured review workflow
```

User selects Designer, types /design-review → Expert runs structured workflow.

### Recipe 3: Compound Workflow (Prompt via `#file:` references)

```text
composite.prompt.md includes:
  #file:.github/prompts/design-review.prompt.md
  #file:.github/prompts/impact.prompt.md
  #file:.github/prompts/refactor.prompt.md
```

One `/composite` command runs three full workflows in sequence.

### Recipe 4: Agent Handoff Chain

```text
Designer → Impact-Analyzer → Code-Reviewer
(each agent hands work to the next specialist)
```

Configure via `handoffs:` in agent frontmatter and body instructions.

### Recipe 5: Specialist + Live Data (Agent + MCP)

```text
Code-Reviewer agent (read-only tools)   → WHO: review mindset
GitHub MCP server                       → WHAT data: actual PR diffs
```

Agent reviews real PRs without needing VS Code editFiles access.

### Recipe 6: Steering Mode (Global Instruction File)

```text
change-completeness.instructions.md (applyTo: "**")
```

Acts as a project-wide behavioral mode — always on, for every file.
Stack multiple `applyTo: "**"` files for multiple global modes.

### Recipe 7: Full Stack

```text
copilot-instructions.md          → always: project rules
java.instructions.md             → when *.java: Java rules
designer.agent.md                → selected: architecture persona
design-review.prompt.md          → invoked: review workflow
design-patterns/SKILL.md         → auto: pattern knowledge
github-mcp-server                → auto: live PR data
```

Maximum customization for architecture review sessions.

---

## Stacking Order (Priority)

When multiple customizations are active, conflicts are resolved in this order:

| Priority | Source | Wins when... |
|---|---|---|
| 1 (highest) | User's message | Always wins — you can override everything |
| 2 | `.prompt.md` template | When `/cmd` is invoked |
| 3 | `.agent.md` persona | When agent is selected |
| 4 | `SKILL.md` knowledge | When topic matches |
| 5 | Path `.instructions.md` | When file matches `applyTo` |
| 6 (lowest) | `copilot-instructions.md` | Always present, least specific |

MCP servers don't fit the priority model — they're tools the agent calls, not context injection.

---

## Troubleshooting Activation

**"My skill isn't activating"**
→ Check: Is the `description` field specific enough? Does it include the exact terms the user asks?
→ Test: Ask a question that uses words from the description field

**"My instructions aren't being followed"**
→ Check: Is `applyTo` glob correct? Test the glob at `globtester.com`
→ Check: Is the file too large? (>500 tokens for instructions — Copilot may truncate)
→ Check: Is the rule in conflict with a higher-priority source?

**"My agent isn't in the dropdown"**
→ Check: Is the file in `.github/agents/` with `.agent.md` extension?
→ Check: Is `description:` present in frontmatter (required)?

**"My prompt /command isn't showing"**
→ Check: Is `name:` in frontmatter matching the /command you type?
→ Check: File must be in `.github/prompts/` with `.prompt.md` extension

**"MCP server tools aren't available"**
→ Check: Are you in Agent mode? (MCP tools only available in agent mode)
→ Check: Did the server start successfully? (VS Code → Output → MCP)

---

## Quick Templates

### New `.instructions.md`

```markdown
---
applyTo: "**/*.java"
---
## [Domain] Rules

- [Specific rule 1 — actionable, brief]
- [Specific rule 2]
- [Specific rule 3]

### [Sub-category Rules]
- [Rule]
```

### New `.agent.md`

```markdown
---
description: >
  [One sentence: what this agent is GOOD AT].
  Use when: [use case 1], [use case 2], [use case 3].
tools:
  - codebase
  - search
  - usages
---

You are a [ROLE]. Your expertise is [DOMAIN].

When the user asks you to [TASK]:
1. [STEP 1]
2. [STEP 2]

## Rules
- Always [rule]
- Never [rule]
```

### New `.prompt.md`

```markdown
---
name: my-command
description: 'Brief description shown in autocomplete'
agent: optional-agent
tools: ['codebase', 'search']
---

## Input
${input:topic:What topic?}
${input:goal:What is your goal?}

## Instructions

[Main instructions for Copilot organized by topic/goal]

### When topic is X:
[Instructions for X]

### Rules
- Always [rule 1]
- Provide working examples
```

### New `SKILL.md`

```markdown
---
name: domain-name
description: >
  [Write this carefully — it's the activation trigger.]
  Use when asked about [TOPIC 1], [TOPIC 2], or [USE CASE].
  Covers: [subtopic A], [subtopic B].
---

# [Domain] — Skill Reference

## 🟢 Quick Reference

[Newbie-friendly cheatsheet — short commands, quick answers]

## 🟡 Detailed Guide

[Full explanations, patterns, examples]

## 🔴 Advanced Topics

[Edge cases, performance, architecture]
```

---

## Mermaid Diagrams as LLM Context

> **The technique:** Use Mermaid text diagrams to give Copilot structured architecture knowledge
> instead of pasting raw code. 20 lines of Mermaid = same knowledge as 500 lines of code.
> **10x token compression, higher first-attempt accuracy.**

**The 3-step workflow:**

```text
Step 1: GENERATE  → "Generate a Mermaid class diagram for [InterfaceName] and all consumers"
Step 2: FEED      → Paste the diagram into your next prompt as context
Step 3: MODIFY    → Agent reasons over structure, not code text — gets it right first time
```

**Why Mermaid specifically:**
- Plain text → fits naturally in Markdown, instructions, prompt files
- GitHub, VS Code, Confluence all render it natively
- LLMs can both generate AND consume Mermaid (bidirectional — unlike images)
- Version-controllable (diffs are meaningful)
- LLMs cannot reason about PNG/SVG architecture diagrams — only text

**Best diagram type by task:**

| Task | Diagram Type |
|---|---|
| Add/modify a property or method | `classDiagram` (shows hierarchy + consumers) |
| Understand a call flow | `sequenceDiagram` (call order across classes) |
| Cross-module impact analysis | `graph TD` (dependency ripple effects) |
| Debug event-driven code | `stateDiagram-v2` (transitions + edge cases) |
| Understand lifecycle decisions | `flowchart TD` (decision points + order) |
| API design planning | `classDiagram` (contracts + hierarchies) |

**Always-on architecture context — embed in instruction files:**

```markdown
---
applyTo: "**/*.java"
---
# Architecture Reference

## Module Dependency Map
[paste Mermaid graph here]

## Core Entity Hierarchy
[paste classDiagram here]
```

This loads the diagram into context automatically for every Java file edit.

**Advanced techniques:**
1. **Generate → Modify → Re-generate** — Modify the diagram, then "implement the diff"
2. **Diagram as specification** — Draw the design instead of writing English prose
3. **Sequence diagram as test spec** — Each arrow becomes a test assertion
4. **State diagram as FSM** — Each state becomes an enum, each transition a method
5. **Reverse engineering** — "Generate a sequence for this user action" to understand unfamiliar code

Full guide: `.github/docs/mermaid-as-context.md`

---

## Model Selection Guide

When choosing which model to use in VS Code Copilot Chat:

| Task | Best Model | Why |
|---|---|---|
| Quick inline edit / Ask mode | **GPT-4o** | Fastest, good enough for simple tasks |
| Code generation, unit tests | **Claude Sonnet 4** | Good quality, follows patterns well |
| Multi-file agent, deep reasoning | **Claude Opus 4** | Best reasoning, handles complex instructions |
| Large file / entire codebase context | **Gemini 2.5 Pro** | 1M token context window |
| Debug complex logic / algorithms | **Claude Opus 4 / o3-mini** | Deep reasoning |
| Build, git, simple commands | **GPT-4o** | Speed matters, task is straightforward |
| Design review, multi-source analysis | **Claude Opus 4** | Complex template + cross-file research |

**Rule of thumb:**
> Start with **Claude Sonnet 4** for most tasks.
> Upgrade to **Claude Opus 4** when Agent mode needs deep multi-step reasoning.
> Use **GPT-4o** for speed-critical Ask or Edit tasks.
> Use **Gemini 2.5 Pro** when you need to reference a very large file or whole codebase.

**Pin model in `.agent.md` frontmatter (2026+):**

```yaml
model: gpt-4o            # Fast agents
model: claude-opus-4-5   # Deep reasoning agents
model: gemini-2.0-flash  # Large context agents
```

---

## Team Adoption Patterns

**ROI from real-world adoption (~60-80% reduction in prompt re-engineering):**

| What changes | Before | After |
|---|---|---|
| Convention-compliant output | ~40% first-attempt | >90% |
| Repeated context in prompts | Multiple times/session | Near zero |
| Code review convention comments | Baseline | Down 70-80% |
| New dev onboarding to first PR | Weeks | Days |

**The minimum viable team setup (start here):**
1. `copilot-instructions.md` — 5-10 universal rules for the team
2. One `.instructions.md` for the main coding language/area
3. One `SKILL.md` for the top domain knowledge

**Highest-ROI additions after the starter kit:**
- `/test-gen` prompt — consistent test generation across team
- `/review` prompt — pre-PR self-review checklist
- `code-review-checklist/SKILL.md` — auto-activates for review questions
- `build-workflow/SKILL.md` — auto-activates for build questions

**Team knowledge sharing session pattern:**
- Live demos > slides (show before/after code diffs)
- Opening hook: "How many times have you told Copilot the same thing twice today?"
- Prove ROI with actual project code, not toy examples
- Presenter notes kept on second screen for natural delivery

Full guide: `.github/docs/team-copilot-adoption.md`

---

## Migration & Interchange Patterns

> When content is in the wrong primitive type, or you need to refactor your `.github/` setup.
> Full guide with before/after examples: `.github/docs/copilot-customization-deep-dive.md` Part 7.

### 3 Simple Rules (Newbie)

```text
RULE (always enforce)       → .instructions.md
KNOWLEDGE (answer questions) → SKILL.md
WORKFLOW (trigger manually)  → .prompt.md
```

### The 8 Common Migration Paths

| # | From | To | When |
|---|---|---|---|
| 1 | Bloated `copilot-instructions.md` | Split `.instructions.md` files | File exceeds ~200 lines or has file-type-specific rules |
| 2 | `.instructions.md` | `SKILL.md` | File contains reference knowledge, not rules |
| 3 | `SKILL.md` | `.instructions.md` | Skill has rules that must always apply (not just when relevant) |
| 4 | `.prompt.md` | `.agent.md` | Prompt defines a persistent persona, not a one-shot task |
| 5 | `.agent.md` | `.prompt.md` | Agent is only ever used for one-shot tasks |
| 6 | `SKILL.md` | MCP server | Knowledge is dynamic (changes daily, needs live API access) |
| 7 | MCP server | `SKILL.md` | Server only returns static/hardcoded content |
| 8 | Multiple prompts | Agent + thin prompts | 5+ prompts share the same persona/context |

### Quick Decision Flowchart

```text
Is it a RULE?
├── Yes → Specific file types? → .instructions.md (applyTo glob)
│         All files?           → copilot-instructions.md
└── No → Is it KNOWLEDGE?
    ├── Yes → Static?  → SKILL.md
    │         Dynamic? → MCP server
    └── No → One-shot task?     → .prompt.md
             Persistent persona? → .agent.md
```

### Interchange Matrix (Pro)

```text
FROM ↓ / TO →    instructions  skill   prompt  agent   MCP
──────────────────────────────────────────────────────────
instructions     —            ✅ know  ⚠️ rare ❌      ❌
skill            ✅ enforce   —        ❌      ❌      ✅ live
prompt           ❌           ❌       —       ✅ mode  ❌
agent            ❌           ❌       ✅ thin —        ❌
MCP              ❌           ✅ static ❌     ❌      —

✅ = natural path    ⚠️ = possible but usually wrong    ❌ = not applicable
```

### The "Right Size" Heuristic

```text
copilot-instructions.md  →  30–80 lines
.instructions.md         →  20–60 lines per file
SKILL.md                 →  100–500 lines
.prompt.md               →  10–40 lines
.agent.md                →  20–80 lines
```

If a file drastically exceeds these ranges, it likely needs splitting or migration.

---

## Why Not Just Skills? — Quick Reference

> When users ask "why not just use skills for everything?" refer to this.

**Skills are knowledge packs. They do ONE job well: giving Copilot domain reference
material when semantically relevant.** They cannot:

| Need | Why skills fail | Correct primitive |
|---|---|---|
| Enforce rules on every Java file | Skills only activate when Copilot judges the conversation relevant | `.instructions.md` (auto by file pattern) |
| Change Copilot's persona | Skills add knowledge, not personality | `.agent.md` |
| Trigger a workflow with `/command` | Skills have no manual trigger | `.prompt.md` |
| Access live API data | Skills are static text files | MCP server |

**The 5 sentences to memorize:**

```text
1. "Copilot must ALWAYS do this"                → instruction
2. "Copilot should KNOW this when relevant"      → skill
3. "I want to TRIGGER a specific task"           → prompt
4. "I want Copilot to BECOME someone different"  → agent
5. "I need LIVE data from an external system"    → MCP server
```

Full explanation with examples: `.github/docs/copilot-customization-deep-dive.md` Part 9.

---

## Official Resources

| Resource | URL |
|---|---|
| Copilot Customization Overview | [code.visualstudio.com/docs/copilot/customization](https://code.visualstudio.com/docs/copilot/customization) |
| Custom Instructions | [code.visualstudio.com/.../custom-instructions](https://code.visualstudio.com/docs/copilot/customization/custom-instructions) |
| Prompt Files | [code.visualstudio.com/.../prompt-files](https://code.visualstudio.com/docs/copilot/customization/prompt-files) |
| Custom Agents | [code.visualstudio.com/.../custom-agents](https://code.visualstudio.com/docs/copilot/customization/custom-agents) |
| Agent Skills | [code.visualstudio.com/.../agent-skills](https://code.visualstudio.com/docs/copilot/customization/agent-skills) |
| Agent Skills Standard | [agentskills.io](https://agentskills.io/) |
| MCP Specification | [spec.modelcontextprotocol.io](https://spec.modelcontextprotocol.io/) |
| GitHub Copilot Docs | [docs.github.com/en/copilot](https://docs.github.com/en/copilot) |
| Awesome Copilot | [github.com/github/awesome-copilot](https://github.com/github/awesome-copilot) |
| Anthropic Skills | [github.com/anthropics/skills](https://github.com/anthropics/skills) |
| 5-Minute Newbie Guide | `.github/docs/copilot-customization-newbie.md` |
| Full Deep Dive | `.github/docs/copilot-customization-deep-dive.md` |
| Export to Your Project | `.github/docs/export-guide.md` |
| Newbie Export Guide | `.github/docs/export-newbie-guide.md` |

---

## Curated GitHub Repositories

> **Famous, well-maintained, open-source repositories for learning, reference, and resources.**
> These are some of the most starred and community-trusted repositories on GitHub.

### GHCP & AI Agent Customization

| Repository | Stars | Description |
|---|---|---|
| [github/awesome-copilot](https://github.com/github/awesome-copilot) | 26k+ | Official community collection of Copilot agents, instructions, skills, plugins, hooks, workflows, and cookbook examples. Includes Learning Hub at [awesome-copilot.github.com](https://awesome-copilot.github.com) |
| [anthropics/skills](https://github.com/anthropics/skills) | 98k+ | Reference implementation of Agent Skills standard. Creative, Development, Enterprise, and Document skill categories. Powers Claude Code document capabilities |
| [agentskills/agentskills](https://github.com/agentskills/agentskills) | — | The open specification for Agent Skills adopted by Claude Code, Gemini CLI, Goose, Roo Code, OpenHands, Qodo, and many more |
| [modelcontextprotocol/servers](https://github.com/modelcontextprotocol/servers) | 35k+ | Official MCP reference server implementations (GitHub, filesystem, Slack, Google Drive, etc.) |

### Free Ebooks & Learning Resources

| Repository | Stars | Description |
|---|---|---|
| [EbookFoundation/free-programming-books](https://github.com/EbookFoundation/free-programming-books) | 384k+ | The most starred GitHub repo for free programming books. Books by language and subject, cheat sheets, free courses, interactive tutorials, podcasts, and playgrounds. 40+ languages |
| [codecrafters-io/build-your-own-x](https://github.com/codecrafters-io/build-your-own-x) | 482k+ | "What I cannot create, I do not understand." Step-by-step guides for recreating 30+ technologies from scratch (databases, Docker, Git, OS, compilers, neural networks, etc.) |
| [practical-tutorials/project-based-learning](https://github.com/practical-tutorials/project-based-learning) | 261k+ | Project-based tutorials organized by programming language. Build real applications (web, mobile, games, ML, DevOps) to learn |
| [donnemartin/system-design-primer](https://github.com/donnemartin/system-design-primer) | 290k+ | Learn how to design large-scale systems. System design interview prep with real-world architecture examples |
| [papers-we-love/papers-we-love](https://github.com/papers-we-love/papers-we-love) | 91k+ | Classic and influential CS papers organized by topic. Community reading groups worldwide |

### Software Engineering & Algorithms

| Repository | Stars | Description |
|---|---|---|
| [trekhleb/javascript-algorithms](https://github.com/trekhleb/javascript-algorithms) | 190k+ | Algorithms and data structures implemented in JavaScript with explanations and links to further readings |
| [TheAlgorithms/Java](https://github.com/TheAlgorithms/Java) | 60k+ | All algorithms implemented in Java — sorting, searching, graphs, dynamic programming, etc. |
| [kdn251/interviews](https://github.com/kdn251/interviews) | 63k+ | Everything you need to prepare for a technical interview — DSA, system design, behavioral |
| [iluwatar/java-design-patterns](https://github.com/iluwatar/java-design-patterns) | 91k+ | Design patterns implemented in Java. Gang of Four + application patterns with real-world examples |

### AI / LLM Resources

| Repository | Stars | Description |
|---|---|---|
| [f/awesome-chatgpt-prompts](https://github.com/f/awesome-chatgpt-prompts) | 120k+ | Curated list of prompt engineering techniques and examples for ChatGPT and other LLMs |
| [Hannibal046/Awesome-LLM](https://github.com/Hannibal046/Awesome-LLM) | 20k+ | Curated list of LLM papers, tools, frameworks, and resources |
| [rasbt/LLMs-from-scratch](https://github.com/rasbt/LLMs-from-scratch) | 45k+ | Build a Large Language Model from scratch — step-by-step code and explanations |

### CS Education & Self-Taught Paths

| Repository | Stars | Description |
|---|---|---|
| [ossu/computer-science](https://github.com/ossu/computer-science) | 180k+ | Free self-taught CS education — structured like a 4-year degree. Intro, core, advanced, and capstone |
| [jwasham/coding-interview-university](https://github.com/jwasham/coding-interview-university) | 312k+ | Complete CS study plan to become a software engineer at top companies. Multi-month structured path |
| [charlax/professional-programming](https://github.com/charlax/professional-programming) | 47k+ | Full-stack resources for programmers — must-read books, articles, and 60+ topics from algorithms to writing |
| [mtdvio/every-programmer-should-know](https://github.com/mtdvio/every-programmer-should-know) | 86k+ | Technical things every developer should know — algorithms, security, architecture, distributed systems |
| [sindresorhus/awesome](https://github.com/sindresorhus/awesome) | 447k+ | The master meta-list of awesome lists. Covers every language, framework, platform, and domain |
| [trimstray/the-book-of-secret-knowledge](https://github.com/trimstray/the-book-of-secret-knowledge) | 211k+ | Inspiring lists, manuals, cheatsheets, CLI/web tools — Linux, networking, security, DevOps |
| [kamranahmedse/developer-roadmap](https://github.com/kamranahmedse/developer-roadmap) | 310k+ | Interactive roadmaps for every developer role — frontend, backend, DevOps, full-stack, and more |

### Personal Development & Self-Improvement

| Repository | Stars | Description |
|---|---|---|
| [hackerkid/Mind-Expanding-Books](https://github.com/hackerkid/Mind-Expanding-Books) | 11k+ | Curated mind-expanding books — startups, philosophy, psychology, economics, self-help |
| [jyguyomarch/awesome-productivity](https://github.com/jyguyomarch/awesome-productivity) | 2k+ | Productivity resources — time management, focus, habits, deep work, and personal effectiveness |
| [matteofigus/awesome-speaking](https://github.com/matteofigus/awesome-speaking) | 1k+ | Public speaking resources — presentation skills, storytelling, and conference speaking |
| [ashishb/awesome-personal-finance](https://github.com/ashishb/awesome-personal-finance) | 300+ | Personal finance resources — budgeting, investing, tax planning for tech professionals |

---

## Latest Features (2026)

Features from the latest VS Code Copilot documentation (March 2026):

### New Always-On Files

| File | Purpose |
|---|---|
| `AGENTS.md` | Multi-agent compatible instructions (read by Copilot, Claude Code, and other agents) |
| `CLAUDE.md` | Claude Code instructions (Copilot reads for cross-tool compatibility) |
| `.claude/rules` | Claude Code rules (Copilot reads these too) |

### New Frontmatter Fields

| Field | Applies To | Purpose |
|---|---|---|
| `handoffs:` | `.agent.md` | Define agent-to-agent handoff chains |
| `user-invocable:` | `.agent.md`, `SKILL.md` | Control visibility in dropdowns/commands (default: true) |
| `disable-model-invocation:` | `.agent.md`, `SKILL.md` | Prevent AI from auto-invoking (default: false) |
| `argument-hint:` | `.agent.md`, `SKILL.md`, `.prompt.md` | Hint text shown after user selects the item |
| `target:` | `.agent.md` | Set to `github-copilot` for cloud agents |
| `mcp-servers:` | `.agent.md` | MCP servers for cloud/coding agents |
| `description:` | `.instructions.md` | Now used for semantic matching (not just `applyTo`) |

### New Generation Commands

| Command | Creates |
|---|---|
| `/init` | Generates `copilot-instructions.md` from workspace analysis |
| `/create-instruction` | New `.instructions.md` file via AI interview |
| `/create-agent` | New `.agent.md` file via AI interview |
| `/create-skill` | New `SKILL.md` file via AI interview |
| `/create-prompt` | New `.prompt.md` file via AI interview |

### Key Architecture Changes

- **Skills appear as slash commands** — type `/skill-name` to invoke manually
- **Three-level skill loading** — discovery (name+description) → instructions (body) → resource access (files)
- **Organization-level sharing** — place customizations in the `.github` org repo
- **Settings Sync** — prompt and instruction files sync across devices
- **Chat Customizations editor** — `Chat: Open Chat Customizations` command (Preview)
- **Configurable locations** — `chat.instructionsFilesLocations`, `chat.agentFilesLocations`, etc.
- **Agent hooks** (Preview) — pre/post-response hook commands scoped to agents
- **Agent plugins** — extensions contribute skills via `chatSkills` in `package.json`
- **`.chatmode.md` → `.agent.md`** — old format renamed (still recognized for back-compat)
- **Instruction priority** — Personal > Repository > Organization

For full details, see [Part 11 of the Deep Dive](../docs/copilot-customization-deep-dive.md#part-11-latest-features--api-updates-2026).
