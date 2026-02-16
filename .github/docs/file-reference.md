# 📂 File Reference — Who Reads What

> **Purpose:** Clear guide showing which files are consumed by **GitHub Copilot** (the AI) vs. which files are for **developers** (you). Knowing this distinction helps you understand what to edit to change Copilot's behavior vs. where to look for learning and documentation.

---

## At a Glance

```
.github/
│
│  ┌─────────────────────────────────────────────────────────┐
│  │  🤖 FILES COPILOT READS (affect AI behavior)           │
│  └─────────────────────────────────────────────────────────┘
│
├── copilot-instructions.md              🤖 Always loaded into Copilot context
│
├── instructions/
│   ├── java.instructions.md             🤖 Loaded when editing *.java files
│   └── clean-code.instructions.md       🤖 Loaded when editing *.java files
│
├── agents/
│   ├── designer.agent.md                🤖 Loaded when agent selected in dropdown
│   ├── debugger.agent.md                🤖 Loaded when agent selected
│   ├── impact-analyzer.agent.md         🤖 Loaded when agent selected
│   ├── learning-mentor.agent.md         🤖 Loaded when agent selected
│   └── code-reviewer.agent.md           🤖 Loaded when agent selected
│
├── prompts/
│   ├── design-review.prompt.md          🤖 Loaded when /design-review invoked
│   ├── debug.prompt.md                  🤖 Loaded when /debug invoked
│   ├── impact.prompt.md                 🤖 Loaded when /impact invoked
│   ├── teach.prompt.md                  🤖 Loaded when /teach invoked
│   ├── refactor.prompt.md              🤖 Loaded when /refactor invoked
│   ├── explain.prompt.md               🤖 Loaded when /explain invoked
│   ├── composite.prompt.md             🤖 Loaded when /composite invoked
│   ├── context.prompt.md               🤖 Loaded when /context invoked
│   └── scope.prompt.md                 🤖 Loaded when /scope invoked
│
├── skills/
│   ├── java-build/SKILL.md             🤖 Auto-loaded when topic matches
│   ├── design-patterns/SKILL.md        🤖 Auto-loaded when topic matches
│   └── java-debugging/SKILL.md         🤖 Auto-loaded when topic matches
│
│  ┌─────────────────────────────────────────────────────────┐
│  │  👤 FILES FOR DEVELOPERS (documentation & learning)    │
│  └─────────────────────────────────────────────────────────┘
│
├── README.md                            👤 Project overview & navigation hub
│
├── instructions/
│   └── README.md                        👤 Guide: how instructions work
│
├── agents/
│   └── README.md                        👤 Guide: how agents work
│
├── prompts/
│   └── README.md                        👤 Guide: how prompts work
│
├── skills/
│   └── README.md                        👤 Guide: how skills work
│
└── docs/
    ├── getting-started.md               👤 Hands-on tutorial (~30 min)
    ├── customization-guide.md           👤 Architecture deep-dive
    └── file-reference.md               👤 This file — who reads what
```

---

## Detailed Breakdown

### 🤖 Files Copilot Reads

These files **directly affect Copilot's AI behavior**. Editing them changes what Copilot knows, how it responds, and what rules it follows.

| File Type | Extension | Location | When Loaded | What It Controls |
|---|---|---|---|---|
| **Project instructions** | `.md` | `.github/copilot-instructions.md` | Every request (always) | Project-wide rules all responses must follow |
| **Path-scoped instructions** | `.instructions.md` | `.github/instructions/` | When editing a file matching `applyTo` glob | Coding standards for specific file types |
| **Agent definitions** | `.agent.md` | `.github/agents/` | When you select the agent from the dropdown | Copilot's persona, expertise, tools, and handoffs |
| **Prompt templates** | `.prompt.md` | `.github/prompts/` | When you type `/command` in Chat | Task workflow, steps, and structure |
| **Skill definitions** | `SKILL.md` | `.github/skills/<name>/` | Auto — when your question matches the description | Extra knowledge, scripts, and templates |

#### Key rules for Copilot-read files:
- **Content is instructions to the AI** — write them as directives ("Use X", "Never do Y", "When asked to Z, follow these steps")
- **Frontmatter matters** — YAML headers (`applyTo`, `description`, `tools`, etc.) control when and how the file loads
- **Keep them focused** — shorter, targeted instructions get better compliance than long documents
- **Test after editing** — changes take effect immediately, verify by asking Copilot a relevant question

### 👤 Files for Developers

These files are **documentation for humans**. Copilot does NOT read these to shape its behavior. They exist to help you understand, learn, and extend the customization system.

| File | Location | Purpose | When to Read |
|---|---|---|---|
| **Main README** | `.github/README.md` | Overview of the entire system, navigation hub | First — start here |
| **Instructions README** | `.github/instructions/README.md` | How instructions work, glob patterns, examples | When creating/editing instructions |
| **Agents README** | `.github/agents/README.md` | How agents work, tools, handoffs, examples | When creating/editing agents |
| **Prompts README** | `.github/prompts/README.md` | How prompts work, variables, slash commands | When creating/editing prompts |
| **Skills README** | `.github/skills/README.md` | How skills work, progressive loading, structure | When creating/editing skills |
| **Getting Started** | `.github/docs/getting-started.md` | Step-by-step hands-on tutorial | Second — try everything |
| **Customization Guide** | `.github/docs/customization-guide.md` | Architecture, how primitives connect | When you want the big picture |
| **File Reference** | `.github/docs/file-reference.md` | This file — which files are for whom | When confused about a file's purpose |

#### Key rules for developer files:
- **Content is explanation for humans** — write clearly, use examples, add links
- **Editing these does NOT change Copilot** — they are reference material only
- **Keep them in sync** — when you add a new agent/prompt/skill, update the relevant README

---

## How to Tell Them Apart

Quick heuristics to determine any file's audience:

| Clue | Audience | Example |
|---|---|---|
| Has YAML frontmatter with `applyTo`, `tools`, `handoffs`, `description` | 🤖 Copilot | `java.instructions.md`, `designer.agent.md` |
| Named `README.md` | 👤 Developer | `instructions/README.md` |
| Lives in `docs/` | 👤 Developer | `getting-started.md` |
| Extension is `.instructions.md`, `.agent.md`, `.prompt.md` | 🤖 Copilot | All of them |
| Named `SKILL.md` (uppercase) | 🤖 Copilot | `java-build/SKILL.md` |
| Contains "How to use", "Table of Contents", "Experiments to Try" | 👤 Developer | Any README |
| Contains "You are a...", "When the user asks...", "Always follow..." | 🤖 Copilot | Agent/prompt files |

---

## What Happens When You Edit Each Type

| You edit... | Effect | Takes effect... |
|---|---|---|
| `copilot-instructions.md` | All Copilot responses change | Next message |
| `*.instructions.md` | Responses for matching files change | Next message (with matching file open) |
| `*.agent.md` | Agent persona changes | Next message (with agent selected) |
| `*.prompt.md` | Slash command behavior changes | Next time you run `/command` |
| `SKILL.md` | Skill knowledge changes | Next time topic matches |
| Any `README.md` or `docs/*.md` | Nothing in Copilot changes | When a developer reads the file |

---

## Common Questions

**Q: If I put tips in a README, will Copilot follow them?**
A: No. README files in the `instructions/`, `agents/`, `prompts/`, `skills/`, and `docs/` folders are for developer reference only. To make Copilot follow rules, put them in the appropriate Copilot-read file (`.instructions.md`, `.agent.md`, etc.).

**Q: Can I reference a developer README from a Copilot file?**
A: Yes — prompt files support `[link text](path)` — but Copilot may or may not follow the link. It's better to include the rules directly in the Copilot file.

**Q: Does `copilot-instructions.md` count as a developer file too?**
A: It's primarily a 🤖 Copilot file (the AI reads it every request). But developers should also read it to understand the project-wide rules Copilot follows.

**Q: What about resource files inside skill folders (scripts, templates)?**
A: Files alongside `SKILL.md` (like `.sh` scripts, `.java` templates) are 🤖 Copilot resources — the AI can read and use them when the skill loads. They are *also* useful for developers as reference.

---

<p align="center">

[← Back to main guide](../README.md) · [Getting Started](getting-started.md) · [Customization Guide](customization-guide.md) · [Instructions](../instructions/README.md) · [Agents](../agents/README.md) · [Prompts](../prompts/README.md) · [Skills](../skills/README.md)

</p>
