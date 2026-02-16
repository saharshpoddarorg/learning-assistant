# 🧩 Customization Guide — Architecture Deep-Dive

> **Audience:** You've read the [Getting Started](getting-started.md) tutorial and want to understand how everything fits together.  
> **Goal:** See the big picture, learn the architecture, and know how to extend the system.

---

## 📑 Table of Contents

- [The Big Picture](#-the-big-picture)
- [How the Primitives Work Together](#-how-the-primitives-work-together)
  - [Instructions — The Rules](#1-instructions--the-rules)
  - [Agents — The Specialists](#2-agents--the-specialists)
  - [Prompts — The Shortcuts](#3-prompts--the-shortcuts)
  - [Skills — The Toolkits](#4-skills--the-toolkits)
- [Priority & Stacking Order](#-priority--stacking-order)
- [Architecture Diagram](#-architecture-diagram)
- [How to Extend the System](#-how-to-extend-the-system)
  - [Adding a New Agent](#adding-a-new-agent)
  - [Adding a New Skill](#adding-a-new-skill)
  - [Adding a New Prompt](#adding-a-new-prompt)
  - [Adding a New Instruction](#adding-a-new-instruction)
- [File Audience — Copilot vs Developer](#-file-audience--copilot-vs-developer)
- [Real-World Workflow Examples](#-real-world-workflow-examples)
- [Tips & Best Practices](#-tips--best-practices)
- [Further Reading](#-further-reading)

---

## 🌍 The Big Picture

GitHub Copilot customization is a **layered system** that progressively shapes Copilot's behavior:

```
┌─────────────────────────────────────────────────┐
│                  YOUR QUESTION                  │
│          "Review Main.java for SOLID"           │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│              COPILOT ENGINE                     │
│                                                 │
│  1. Load copilot-instructions.md    (always)    │
│  2. Load matching *.instructions.md (by file)   │
│  3. Load active agent persona       (if set)    │
│  4. Load matching skills            (by topic)  │
│  5. Execute prompt template         (if /cmd)   │
│                                                 │
│         Everything merges → AI responds         │
└─────────────────────────────────────────────────┘
```

**Key insight:** These aren't separate features — they **stack together**. When you ask the Designer agent to review `Main.java` using `/design-review`, Copilot combines:

- Project-wide rules from `copilot-instructions.md`
- Java-specific rules from `java.instructions.md` + `clean-code.instructions.md`
- Designer agent's persona and expertise
- Design-patterns skill (if the topic matches)
- The review structure from `design-review.prompt.md`

---

## 🔗 How the Primitives Work Together

### 1. Instructions — The Rules

```
copilot-instructions.md          ← Base layer (always on)
     +
java.instructions.md             ← When editing *.java
     +
clean-code.instructions.md       ← When editing *.java (stacks!)
     =
Combined coding standards        ← What Copilot follows
```

**Role:** Define *how* code should be written — naming, structure, patterns, style.  
**Activation:** Automatic, based on the file being edited.  
**Docs:** [Instructions Guide](../instructions/README.md)

### 2. Agents — The Specialists

```
Default (Ask)  ← General-purpose Copilot
Designer       ← Thinks in patterns, SOLID, architecture
Debugger       ← Thinks in hypotheses, root causes, evidence
Impact Analyzer← Thinks in ripple effects, dependencies, risk
Learning Mentor← Thinks in analogies, exercises, progression
```

**Role:** Define *who* Copilot becomes — the persona, expertise, and approach.  
**Activation:** Manual — you select from the dropdown in Chat.  
**Docs:** [Agents Guide](../agents/README.md)

### 3. Prompts — The Shortcuts

```
/design-review  → Runs design review workflow with Designer agent
/debug          → Runs debugging workflow with Debugger agent
/teach          → Runs teaching workflow with Learning Mentor agent
/impact         → Runs impact analysis with Impact Analyzer agent
/refactor       → Runs refactoring suggestions with Designer agent
/explain        → Runs file explanation with default Ask agent
/composite      → Combines multiple modes into a unified analysis
/context        → Continues prior conversation or starts fresh
/scope          → Sets generic-learning vs code-specific scope
```

**Role:** Define *what* to do — pre-built workflows you trigger with a command.  
**Activation:** Manual — type `/command` in Chat.  
**Docs:** [Prompts Guide](../prompts/README.md)

> 💡 **Meta-prompts** (`/composite`, `/context`, `/scope`) are special — they control *how* Copilot works rather than *what* it works on. They can be combined with each other and with task prompts for powerful workflows. See [Meta-Prompts](../prompts/README.md#-meta-prompts-composite-context--scope) for details.

### 4. Skills — The Toolkits

```
java-build/        ← Activates for: compile, build, run
design-patterns/   ← Activates for: Pattern decisions, SOLID
java-debugging/    ← Activates for: Exceptions, stack traces
```

**Role:** Provide *extra knowledge* — scripts, templates, domain expertise.  
**Activation:** Automatic — Copilot matches your question to skill descriptions.  
**Docs:** [Skills Guide](../skills/README.md)

---

## 📊 Priority & Stacking Order

When multiple customizations apply, Copilot merges them. Highest priority wins on conflicts:

| Priority | Source | Loaded When |
|---|---|---|
| **1 (highest)** | **Your message** | What you type always overrides everything |
| 2 | Prompt template | `/command` invoked |
| 3 | Active agent persona | Agent selected in dropdown |
| 4 | Matching skills | Topic matches skill description |
| 5 | Path-specific instructions | File matches `applyTo` glob |
| 6 (lowest) | `copilot-instructions.md` | Always loaded |

> ⚠️ **Conflicts:** If an agent says "use Strategy pattern" but instructions say "avoid complex patterns," the agent's guidance typically wins (higher priority). Design your customizations to complement, not contradict.
>
> This table matches the [Priority Order](../README.md#priority-order) in the main README.

---

## 🏗️ Architecture Diagram

```
                         YOU
                          │
                ┌─────────┴─────────┐
                │   VS Code Chat    │
                │                   │
                │  Agent: Designer  │ ← You chose this
                │  /design-review   │ ← You typed this
                └─────────┬─────────┘
                          │
               ┌──────────┴──────────┐
               │   COPILOT ENGINE    │
               │                     │
               │  ┌───────────────┐  │
               │  │  Base Rules   │  │ ← copilot-instructions.md
               │  ├───────────────┤  │
               │  │  Java Rules   │  │ ← java.instructions.md
               │  ├───────────────┤  │
               │  │ Clean Code    │  │ ← clean-code.instructions.md
               │  ├───────────────┤  │
               │  │ Designer      │  │ ← designer.agent.md
               │  │ Persona       │  │
               │  ├───────────────┤  │
               │  │ Design        │  │ ← design-patterns/SKILL.md
               │  │ Patterns      │  │
               │  ├───────────────┤  │
               │  │ Prompt        │  │ ← design-review.prompt.md
               │  │ Template      │  │
               │  └───────┬───────┘  │
               │          │          │
               │    MERGED CONTEXT   │
               │          │          │
               └──────────┴──────────┘
                          │
                    AI RESPONSE
                  (fully customized)
```

### Handoff Flow Between Agents

Agents can **hand off** to each other for multi-step workflows:

```
Designer ──handoff──▶ Impact Analyzer ──handoff──▶ Code Reviewer
   │                       │                           │
   │ "Here's my            │ "Here's what              │ "Here's the
   │  design review"       │  changes would            │  code quality
   │                       │  affect"                  │  assessment"
   ▼                       ▼                           ▼
Design                 Ripple-effect              Code quality
recommendations        analysis                   feedback
```

---

## 🔧 How to Extend the System

### Adding a New Agent

<details>
<summary><strong>Template</strong></summary>

Create `.github/agents/<name>.agent.md`:

```markdown
---
description: One-line summary of what this agent specializes in
tools:
  - search
  - codebase
  - usages
handoffs:
  - agent-name-1
  - agent-name-2
---

You are a [ROLE]. Your expertise is [DOMAIN].

When the user asks you to [TASK]:

1. First, [STEP 1]
2. Then, [STEP 2]
3. Finally, [STEP 3]

## Rules
- Always [RULE 1]
- Never [RULE 2]
- Prefer [PREFERENCE]
```

</details>

### Adding a New Skill

<details>
<summary><strong>Template</strong></summary>

1. Create folder: `.github/skills/<skill-name>/`
2. Create `.github/skills/<skill-name>/SKILL.md`:

```markdown
---
name: skill-name
description: >
  Specific description of what this skill provides.
  Use when asked to [ACTION 1], [ACTION 2], or [ACTION 3].
---

# Skill Name

## Quick Reference
- Key fact 1
- Key fact 2

## Detailed Instructions
...
```

3. *(Optional)* Add resource files alongside SKILL.md

</details>

### Adding a New Prompt

<details>
<summary><strong>Template</strong></summary>

Create `.github/prompts/<name>.prompt.md`:

```markdown
---
agent: agent-name
description: What this prompt does (shown in autocomplete)
---

Your task: [CLEAR INSTRUCTION]

Steps:
1. [STEP 1]
2. [STEP 2]
3. [STEP 3]

Format your response as:
## Section 1
...
## Section 2
...
```

</details>

### Adding a New Instruction

<details>
<summary><strong>Template</strong></summary>

Create `.github/instructions/<name>.instructions.md`:

```markdown
---
applyTo: "glob/pattern/**/*.ext"
---

# Category Name

- Rule 1: Be specific and actionable
- Rule 2: Include concrete examples
- Rule 3: Keep it concise (10-20 rules max)
```

</details>

---

## 🔍 File Audience — Copilot vs Developer

A common point of confusion: not every file in `.github/` is read by Copilot. Understanding the distinction is critical when extending the system.

### 🤖 Files Copilot Reads (affect AI behavior)

These files are **loaded into the AI context** and directly shape how Copilot responds:

| Type | Extension/Name | When Loaded | Purpose |
|---|---|---|---|
| Project instructions | `copilot-instructions.md` | Every request | Project-wide rules |
| Path-scoped instructions | `*.instructions.md` | When `applyTo` glob matches | File-type-specific rules |
| Agents | `*.agent.md` | When selected in dropdown | Persona, expertise, tools |
| Prompts | `*.prompt.md` | When `/command` invoked | Task workflow template |
| Skills | `SKILL.md` | When question matches description | Extra knowledge & tools |

**How to write them:**
- Write as **directives to an AI**: "Use X", "Never do Y", "When asked to Z..."
- Keep focused — shorter instructions get better compliance
- YAML frontmatter controls activation (`applyTo`, `tools`, `description`, etc.)
- Changes take effect **immediately** on the next Copilot interaction

### 👤 Files for Developers (documentation only)

These files are **never loaded by Copilot**. They exist as reference material for you:

| File | Location | Purpose |
|---|---|---|
| `README.md` | Every subfolder | Guide explaining that folder's primitive |
| `getting-started.md` | `docs/` | Hands-on tutorial |
| `customization-guide.md` | `docs/` | Architecture deep-dive (this file) |
| `file-reference.md` | `docs/` | Complete breakdown of who reads what |

**How to write them:**
- Write as **explanation for humans**: clear prose, examples, links
- Editing these does NOT change Copilot's behavior
- Keep them in sync — update when you add new agents/prompts/skills

### Quick Test: "Is this file for Copilot?"

| Clue | Audience |
|---|---|
| Has `applyTo:`, `tools:`, `handoffs:`, or `description:` in YAML frontmatter | 🤖 Copilot |
| Extension is `.instructions.md`, `.agent.md`, `.prompt.md` | 🤖 Copilot |
| Named `SKILL.md` (uppercase) | 🤖 Copilot |
| Named `README.md` | 👤 Developer |
| Lives in `docs/` folder | 👤 Developer |
| Contains "Table of Contents", "Experiments to Try" | 👤 Developer |

> 📖 **Full reference:** [File Reference →](file-reference.md)

---

## 🎯 Real-World Workflow Examples

<details>
<summary><strong>Workflow 1: Full Design Review</strong></summary>

1. Select **Designer** agent
2. Type `/design-review`
3. Get design analysis with SOLID/GRASP assessment
4. Designer offers handoff to **Impact Analyzer**
5. Accept → get ripple-effect analysis
6. Impact Analyzer offers handoff to **Code Reviewer**
7. Accept → get code quality feedback
8. **Result:** Complete design + impact + quality review in one flow

</details>

<details>
<summary><strong>Workflow 2: Debug a Tricky Bug</strong></summary>

1. Select **Debugger** agent
2. Type `/debug`
3. Describe the symptom: "NullPointerException on line 42"
4. Debugger forms hypotheses and guides investigation
5. Root cause found → Debugger offers handoff to **Impact Analyzer**
6. Accept → understand what else the fix might affect
7. **Result:** Systematic bug fix with confidence in impact

</details>

<details>
<summary><strong>Workflow 3: Learn a New Concept</strong></summary>

1. Select **Learning Mentor** agent
2. Type `/teach`
3. Enter topic: "Observer pattern"
4. Get structured lesson: theory → analogy → code → exercise
5. Try the exercise → ask follow-up questions
6. **Result:** Deep understanding with hands-on practice

</details>

---

## 💡 Tips & Best Practices

### Design Principles

1. **Complement, don't contradict** — agents and instructions should reinforce each other
2. **Keep instructions short** — 10-20 bullet points per instruction file
3. **Keep agent personas focused** — one specialty per agent
4. **Use handoffs** — let agents collaborate instead of making one agent do everything
5. **Include action verbs in skill descriptions** — compile, build, run, debug, test

### Common Mistakes

| Mistake | Problem | Fix |
|---|---|---|
| Giant instruction files | Rules get diluted | Split into focused files with narrow `applyTo` |
| Agent does everything | Persona is unfocused | One specialty per agent, use handoffs |
| Vague skill descriptions | Skill never loads | Include specific keywords and "Use when..." |
| Duplicate rules | Conflicting behavior | Put shared rules in `copilot-instructions.md` |
| Too many prompts | Hard to remember | Keep to 5-8 core workflows |

### Performance

- **Instructions:** Very cheap — plain text, small context cost
- **Agents:** Medium — persona adds to context but worth it
- **Skills:** Cheap at rest (only name+description), medium when loaded
- **Prompts:** Cheap — just a template

---

## 📚 Further Reading

| Resource | Link |
|---|---|
| Custom Instructions docs | [VS Code docs](https://code.visualstudio.com/docs/copilot/customization/custom-instructions) |
| Custom Agents docs | [VS Code docs](https://code.visualstudio.com/docs/copilot/customization/custom-agents) |
| Prompt Files docs | [VS Code docs](https://code.visualstudio.com/docs/copilot/customization/prompt-files) |
| Agent Skills spec | [agentskills.io](https://agentskills.io/) |
| Copilot customization overview | [VS Code docs](https://code.visualstudio.com/docs/copilot/customization) |

---

<p align="center">

[← Back to main guide](../README.md) · [Getting Started](getting-started.md) · [File Reference](file-reference.md) · [Agents](../agents/README.md) · [Prompts](../prompts/README.md) · [Skills](../skills/README.md) · [Instructions](../instructions/README.md)

</p>
