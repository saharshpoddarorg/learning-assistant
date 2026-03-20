# Copilot Customization — The 5-Minute Guide

> **New to GitHub Copilot customization? Start here.** This page explains everything
> you need to know in 5 minutes, with zero jargon.

---

## What Is Copilot Customization?

You can teach GitHub Copilot **your rules**, **your knowledge**, and **your workflows**
so it works better in your specific project. You do this by adding special files to
your repository's `.github/` folder.

---

## The 6 Types of Customization Files

There are exactly **6 types** of files you can create. Each does one specific job.

### The Analogy: Think of a Restaurant

| Type | Restaurant Analogy | What It Does |
|---|---|---|
| `copilot-instructions.md` | **House rules** (posted on the wall — everyone follows them) | Rules that ALWAYS apply to every request |
| `.instructions.md` | **Station rules** (grill station has grill rules, pastry station has pastry rules) | Rules that apply only to specific file types |
| `SKILL.md` | **Recipe book** (consulted when the chef needs to know how to make something) | Knowledge that loads when the topic is relevant |
| `.agent.md` | **Job role** (head chef vs. sous chef vs. pastry chef — different mindset, different tools) | A persona that changes HOW Copilot thinks and behaves |
| `.prompt.md` | **Order ticket** (customer says "Table 5 wants the special" — triggers a specific workflow) | A slash command that triggers a specific task |
| MCP server | **Food delivery service** (goes outside the restaurant to get fresh ingredients) | Connects Copilot to live external data |

---

## The 5 Sentences You Need to Memorize

These 5 sentences tell you which type to use, every time:

```text
1. "Copilot must ALWAYS do this"                → instruction file
2. "Copilot should KNOW this when relevant"      → skill file (SKILL.md)
3. "I want to TRIGGER a specific task"           → prompt file (.prompt.md)
4. "I want Copilot to BECOME someone different"  → agent file (.agent.md)
5. "I need LIVE data from an external system"    → MCP server
```

**That's it.** Memorize these and you'll always pick the right one.

---

## "My Colleague Says Just Use Skills for Everything"

This is a common misconception. Here's why it doesn't work:

| What you want | Why skills can't do it | What you actually need |
|---|---|---|
| "Always use `final` in Java" | Skills only load when Copilot thinks the topic is relevant — it might skip your rule | `.instructions.md` with `applyTo: "**/*.java"` |
| "Be a security auditor" | Skills give knowledge, they can't change Copilot's personality or restrict its tools | `.agent.md` |
| "Run my code review checklist" | You can't type `/review` to trigger a skill — skills have no manual trigger | `.prompt.md` |
| "Show me today's Jira tickets" | Skills are static files — they can't make API calls | MCP server |

**Skills are great for ONE thing:** giving Copilot reference knowledge when the topic
comes up. For rules, personas, workflows, and live data — you need the other types.

---

## Your First 3 Files (Do These Today)

### File 1: Project Rules (5 minutes)

Create `.github/copilot-instructions.md`:

```markdown
# Project Instructions

- Use Java 21+ features
- Class names: UpperCamelCase
- Method names: lowerCamelCase
- All public methods need Javadoc
- Commit messages follow Conventional Commits
```

**Result:** Copilot follows these rules on every single request.

### File 2: Your First Skill (5 minutes)

Create `.github/skills/my-project/SKILL.md`:

```markdown
---
name: my-project
description: >
  Use when asked about our project architecture, conventions, or setup.
---

# Project Architecture

We use a layered architecture:
- Controller → Service → Repository → Database
- All APIs return ResponseEntity
- Validation happens in the Service layer
```

**Result:** When you ask Copilot about your project's architecture, it automatically
loads this knowledge.

### File 3: Your First Slash Command (5 minutes)

Create `.github/prompts/review.prompt.md`:

```markdown
---
name: review
description: 'Quick code review of the current file'
mode: ask
---

Review the current file for:
1. Naming issues
2. Missing error handling
3. SOLID violations

Be specific — cite line numbers.
```

**Result:** Type `/review` in Copilot Chat and it runs this workflow.

---

## When to Add More Types

| You realize... | Create... |
|---|---|
| Some rules only matter for Java files, not Markdown | `.github/instructions/java.instructions.md` (with `applyTo: "**/*.java"`) |
| You want a "debugging expert" mode you can switch into | `.github/agents/debugger.agent.md` |
| You need live data from Jira/GitHub/APIs | An MCP server (more complex — see [MCP Setup](mcp-server-setup.md)) |

---

## Where Files Go

```text
.github/
├── copilot-instructions.md           ← YOUR HOUSE RULES (1 file)
├── instructions/
│   ├── java.instructions.md          ← Rules for Java files
│   └── md-formatting.instructions.md ← Rules for Markdown files
├── skills/
│   └── my-project/
│       └── SKILL.md                  ← Knowledge about your project
├── agents/
│   └── debugger.agent.md             ← "Debugging expert" persona
├── prompts/
│   └── review.prompt.md              ← /review slash command
└── .vscode/
    └── mcp.json                      ← MCP server configuration
```

---

## Quick Comparison Table

| Feature | Instructions | Skills | Prompts | Agents | MCP |
|---|---|---|---|---|---|
| Activates automatically? | Yes (by file pattern) | Yes (by topic relevance) | No (you type `/command`) | No (you select from dropdown) | Yes (in agent mode) |
| Can enforce rules? | **Yes** | No | No | Partially | No |
| Can add knowledge? | Short rules only | **Yes, deep knowledge** | Template text only | Persona context only | Live data |
| Can change Copilot's behavior? | Somewhat | No | For one task | **Yes, for whole conversation** | No |
| Can access external APIs? | No | No | No | No | **Yes** |
| Requires coding? | No | No | No | No | **Yes (Java/Python/TS)** |

---

## Next Steps

| I want to... | Read this |
|---|---|
| Understand each type in depth | [Copilot Customization Deep Dive](copilot-customization-deep-dive.md) |
| Try every type hands-on (30 min) | [Getting Started Tutorial](getting-started.md) |
| See how types work together | [Customization Guide — Architecture](customization-guide.md) |
| Decide: MCP server or skill? | [MCP vs Skills Guide](mcp-vs-skills.md) |
| Migrate content between types | [Deep Dive §Migration Guide](copilot-customization-deep-dive.md#part-7-migration--interchange-guide) |
| See official Microsoft docs | [Deep Dive §Official Resources](copilot-customization-deep-dive.md#part-10-official-resources--standards) |

---

**Navigation:** [← START-HERE](START-HERE.md) · [Deep Dive →](copilot-customization-deep-dive.md) · [Getting Started →](getting-started.md)
