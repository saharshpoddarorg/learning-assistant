# Copilot Primitives — Quick Crosswalk & Comparison

> **The practical reference for choosing, comparing, and converting between all 6
> GitHub Copilot customization primitives.**
>
> Not a deep-dive — a decision-making tool. For full details on any primitive, see
> [Copilot Customization Deep Dive](copilot-customization-deep-dive.md).

| Audience | What to read |
|---|---|
| 🟢 **Newbie** | Start with [The 5 Sentences](#the-5-sentences), then [Visual Flowchart](#decision-flowchart), then [When NOT to Use](#when-not-to-use-each) |
| 🟡 **Amateur** | [Head-to-Head Matrix](#head-to-head-matrix), [Similarities](#what-they-share), [Converting Between Types](#converting-between-types) |
| 🔴 **Pro** | [Edge Cases](#edge-cases--grey-areas), [Composition Cheatsheet](#composition-cheatsheet), [Full Crosswalk Table](#full-crosswalk-table) |

---

## The 5 Sentences

Memorize these and you'll always pick the right primitive:

```text
1. "Copilot must ALWAYS do this"                → instruction
2. "Copilot should KNOW this when relevant"      → skill
3. "I want to TRIGGER a specific task"           → prompt
4. "I want Copilot to BECOME someone different"  → agent
5. "I need LIVE data from an external system"    → MCP server
```

---

## The 6 Primitives — One Line Each

| # | Primitive | File | One-line purpose |
|---|---|---|---|
| 1 | **copilot-instructions** | `.github/copilot-instructions.md` | Project-wide rules — always on, every request |
| 2 | **instructions** | `.github/instructions/*.instructions.md` | File-scoped rules — activate by file glob |
| 3 | **skills** | `.github/skills/<domain>/SKILL.md` | Domain knowledge — activate by topic relevance |
| 4 | **prompts** | `.github/prompts/*.prompt.md` | Slash commands — manual trigger via `/command` |
| 5 | **agents** | `.github/agents/*.agent.md` (or `AGENTS.md`) | Personas — change who Copilot IS |
| 6 | **MCP servers** | `.vscode/mcp.json` + server code | External tools — live API calls, external data |

---

## Decision Flowchart

```text
What are you trying to do?
│
├── Enforce a RULE that always applies?
│   ├── To ALL files → copilot-instructions.md
│   └── To specific file types → .instructions.md (applyTo: "**/*.java")
│
├── Teach Copilot KNOWLEDGE it should recall when relevant?
│   ├── Static reference docs → SKILL.md
│   └── Live/dynamic data → MCP server
│
├── Create a SHORTCUT for a repeatable task?
│   └── .prompt.md (becomes /slash-command)
│
├── Change Copilot's PERSONALITY or restrict its tools?
│   └── .agent.md (appears in agent dropdown)
│
└── Connect to an EXTERNAL SYSTEM (API, database, service)?
    └── MCP server
```

---

## Head-to-Head Matrix

| Dimension | copilot-instructions | .instructions.md | SKILL.md | .prompt.md | .agent.md | MCP server |
|---|---|---|---|---|---|---|
| **Activation** | Always (auto) | Auto (file glob) | Auto (semantic) | Manual (`/cmd`) | Manual (dropdown) | Auto (agent mode) |
| **Scope** | Whole project | Matching files | Matching topics | One chat turn | Entire chat session | All agent sessions |
| **Stacks** | Base layer | Yes (many) | Yes (many) | No (one at a time) | No (one at a time) | Yes (all registered) |
| **Changes** | Rules for writing | Rules for writing | What Copilot knows | What Copilot does | Who Copilot is | What Copilot can access |
| **Typical size** | 30-80 lines | 20-60 lines | 100-500 lines | 10-40 lines | 20-80 lines | Any complexity |
| **User sees it?** | No | No | No | Yes (as `/command`) | Yes (in dropdown) | Via tool calls |
| **Needs code?** | No | No | No | No | No | Yes (Java/TS/Python) |
| **Can restrict tools?** | No | No | No | Yes (`tools:`) | Yes (`tools:`) | N/A — IS a tool |
| **Can specify model?** | No | No | No | Yes (`model:`) | Yes (`model:`) | N/A |

---

## What They Share (Similarities)

All 6 primitives share these traits:

1. **Markdown-based** — all are `.md` files (except MCP server code)
2. **Git-tracked** — all live in the repo and are version-controlled
3. **Composable** — they layer on top of each other, not replace each other
4. **Project-scoped** — they apply to the repo they live in (not global to VS Code)
5. **No runtime code** — except MCP servers, none require compilation or execution
6. **Frontmatter-driven** — instructions, skills, prompts, and agents all use YAML frontmatter

---

## What Makes Each Unique (Differences)

| Primitive | Unique strength | No other primitive can... |
|---|---|---|
| **copilot-instructions** | Always-on foundation | ...be guaranteed to apply to EVERY request |
| **instructions** | Path-scoped auto-activation | ...auto-activate only when editing specific file types |
| **skills** | Semantic relevance matching | ...auto-activate based on conversation TOPIC |
| **prompts** | Manual deterministic trigger | ...be invoked as `/command` in chat |
| **agents** | Persona + tool restriction | ...change Copilot's personality AND limit available tools |
| **MCP servers** | External system access | ...make live API calls to external services |

---

## When to Use Each

| I want to... | Use this | Example |
|---|---|---|
| Enforce coding conventions project-wide | `copilot-instructions.md` | "Always use `final` for local variables" |
| Add Java-specific rules that don't affect React files | `.instructions.md` | `applyTo: "**/*.java"` — modifier order, Javadoc |
| Give Copilot knowledge about a topic | `SKILL.md` | MCP development guide, career resources |
| Create a repeatable workflow I trigger manually | `.prompt.md` | `/review` — code review checklist |
| Have a specialized persona with limited tools | `.agent.md` | Code-Reviewer agent (read-only, no edits) |
| Connect to live Jira/GitHub/search data | MCP server | Learning resources server, Atlassian server |

---

## When NOT to Use Each

| Primitive | Don't use when... | Use this instead |
|---|---|---|
| **copilot-instructions** | The rule only applies to some file types | `.instructions.md` with `applyTo` |
| **instructions** | You need semantic topic matching, not file matching | `SKILL.md` |
| **skills** | You need guaranteed activation (not probabilistic) | `.instructions.md` with `applyTo: "**"` |
| **skills** | You want a manual `/command` trigger | `.prompt.md` |
| **prompts** | You want automatic activation without typing `/command` | `SKILL.md` or `.instructions.md` |
| **agents** | You just need one rule, not a whole persona | `.instructions.md` |
| **MCP servers** | The knowledge is static (doesn't change at runtime) | `SKILL.md` |

---

## Converting Between Types

Primitives are not interchangeable, but content can migrate between them:

### Instruction → Skill

**When:** A rule grew into comprehensive domain knowledge (50+ lines of reference
material, not just rules).

```text
Before (instruction):
  .github/instructions/java.instructions.md
  "Use final for local vars. Use var when type is obvious."

After (skill):
  .github/skills/java-build/SKILL.md
  Full guide with cheatsheet, 3-tier depth, curated resources.
```

**Keep the instruction** for the 5-line rule; **add the skill** for the 500-line guide.
They complement each other.

### Skill → Prompt

**When:** You want to trigger specific skill knowledge on demand rather than waiting
for Copilot to detect the topic.

```text
Before (skill only):
  .github/skills/mcp-development/SKILL.md
  Copilot MAY load this when MCP is mentioned.

After (skill + prompt):
  .github/skills/mcp-development/SKILL.md    ← knowledge stays
  .github/prompts/mcp.prompt.md              ← /mcp triggers it reliably
```

### Prompt → Agent

**When:** A one-shot slash command needs to become a persistent persona for an
entire conversation.

```text
Before (prompt):
  /review — runs a one-time code review checklist

After (agent):
  "Code-Reviewer" agent — stays in reviewer mode for the whole chat,
  restricted to read-only tools, uses the review persona continuously.
```

### Instruction → Prompt

**When:** A rule needs a manual trigger instead of always-on activation.

```text
Before: instructions/change-completeness.instructions.md (always on)
After:  prompts/completeness-check.prompt.md (/completeness-check)
```

### Static Knowledge → MCP Server

**When:** The SKILL.md content becomes stale because the data changes frequently.

```text
Before: SKILL.md with a manually maintained list of resources
After:  MCP server that queries a live database or API
```

---

## Edge Cases & Grey Areas

| Situation | Recommendation |
|---|---|
| Rule is only 2 lines but very important | `copilot-instructions.md` — short rules belong in the foundation |
| Knowledge is 1000+ lines | `SKILL.md` — break into sections; Copilot loads what's relevant |
| Want `/command` AND auto-activation | Create BOTH a `.prompt.md` and a `SKILL.md` for the same domain |
| Need persona for one prompt only | Use `.prompt.md` with `agent:` frontmatter to borrow an agent's context |
| MCP server for simple static data | Use `SKILL.md` instead — don't over-engineer with code |
| Same rule for Java AND TypeScript | `.instructions.md` with `applyTo: "**/*.{java,ts}"` |
| Rule for ALL files except tests | `applyTo: "**"` in instruction, then add test-specific overrides |

---

## Composition Cheatsheet

Primitives compose (stack) in this priority order (highest wins):

```text
Priority (high to low):
  1. Agent persona (.agent.md)         ← overrides everything below
  2. Prompt template (.prompt.md)      ← overrides instructions when invoked
  3. Skill knowledge (SKILL.md)        ← supplements instructions
  4. Path instructions (.instructions.md) ← supplements copilot-instructions
  5. Project instructions (copilot-instructions.md) ← always-on base
     + MCP server tools (available in agent mode)
```

### Common Composition Recipes

| Recipe | Components | How they work together |
|---|---|---|
| **Strict Java project** | `copilot-instructions` + `java.instructions.md` | Base rules + Java-specific rules stack |
| **Learning assistant** | `SKILL.md` + `.prompt.md` for each domain | Skills provide knowledge, prompts trigger structured learning |
| **Security auditor** | `.agent.md` + tool restrictions + `SKILL.md` | Agent persona + limited tools + security knowledge |
| **Live data + knowledge** | `SKILL.md` + MCP server | Static reference + dynamic queries |

---

## Full Crosswalk Table

The complete comparison across every dimension:

| Dimension | copilot-instructions | instructions | skills | prompts | agents | MCP |
|---|---|---|---|---|---|---|
| File count | 1 | Many | Many | Many | Many | Many |
| Location | `.github/` | `.github/instructions/` | `.github/skills/*/` | `.github/prompts/` | `.github/agents/` | `.vscode/` |
| Extension | `.md` | `.instructions.md` | `SKILL.md` | `.prompt.md` | `.agent.md` | `.json` + code |
| Frontmatter | None | `applyTo` | `description` | `name`, `description`, `agent`, `tools`, `model` | `name`, `description`, `tools`, `model` | N/A |
| Activation | Always | File glob match | Semantic/topic | `/command` | Dropdown selection | Agent mode |
| Deterministic | Yes (100%) | Yes (100%) | No (probabilistic) | Yes (100%) | Yes (100%) | Yes |
| Multiple active | N/A (one file) | Yes (all matching) | Yes (all matching) | No (one) | No (one) | Yes (all) |
| Token cost | Always injected | Injected when matched | Injected when matched | Injected on invocation | Injected on selection | Per tool call |
| User facing | No | No | No | Yes (`/cmd`) | Yes (dropdown) | Via tool results |
| Can reference tools | No | No | No | Yes | Yes | IS a tool |
| Can specify model | No | No | No | Yes | Yes | No |
| Portability | Copy file | Copy file | Copy folder | Copy file | Copy file | Copy server code + config |

---

## Quick Decision Quiz

Answer these 3 questions to find the right primitive:

```text
Q1: Should it activate AUTOMATICALLY or MANUALLY?
    Auto → Q2
    Manual → prompt (.prompt.md) or agent (.agent.md)

Q2: Should it activate based on FILE TYPE or TOPIC?
    File type → instruction (.instructions.md)
    Topic → skill (SKILL.md)
    Always → copilot-instructions.md

Q3: Does it need LIVE DATA from an external system?
    Yes → MCP server
    No → One of the above
```

---

## Learning Path

| Your level | Read next |
|---|---|
| 🟢 Just starting | [5-Minute Newbie Guide](copilot-customization-newbie.md) → [Getting Started Tutorial](getting-started.md) |
| 🟡 Know the basics | [Customization Architecture Guide](customization-guide.md) → [Deep Dive Parts 4-7](copilot-customization-deep-dive.md#part-4-composition-patterns) |
| 🔴 Want mastery | [Deep Dive Full Reference](copilot-customization-deep-dive.md) → [API Surface](copilot-customization-deep-dive.md#part-16-complete-api-surface-reference) |

---

## Related Documentation

| Document | Focus |
|---|---|
| [Primitives At-a-Glance](primitives-at-a-glance.md) | One-page cheatsheet with frontmatter templates |
| [5-Minute Newbie Guide](copilot-customization-newbie.md) | Plain-language intro, restaurant analogy |
| [Customization Guide](customization-guide.md) | Architecture and how primitives work together |
| [Deep Dive Reference](copilot-customization-deep-dive.md) | 18-part exhaustive reference (14K+ lines) |
| [MCP vs Skills](mcp-vs-skills.md) | Detailed comparison of when to use MCP vs SKILL.md |
| [Skills Library](skills-library.md) | Catalog of all skills in this repo |
| [Slash Commands](slash-commands.md) | All prompts/commands available |
