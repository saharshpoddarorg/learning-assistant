# 🤖 Custom Agents — Guide

> **What:** Custom agents are AI **personas** — specialist roles that Copilot can assume.  
> **Where:** `.github/agents/*.agent.md`  
> **How to use:** Select from the agents dropdown in VS Code Chat.

---

## 📑 Table of Contents

- [What Are Custom Agents?](#-what-are-custom-agents)
- [Agents in This Project](#-agents-in-this-project)
- [File Format](#-file-format)
  - [Frontmatter Fields](#frontmatter-fields-reference)
  - [Available Tools](#available-tools)
- [Handoffs](#-handoffs)
- [Examples](#-examples)
- [How to Create an Agent](#-how-to-create-an-agent)
- [FAQ](#-faq)
- [Experiments to Try](#-experiments-to-try)

---

## 📌 What Are Custom Agents?

Think of agents as **hats** Copilot can wear. When you select an agent:

- Copilot adopts that persona's **instructions** (what it knows and how it behaves)
- It's restricted to specific **tools** (what it can do — edit files? search only? run terminal?)
- It can **hand off** to another agent when done (workflow chaining)

When you switch away from the agent, Copilot reverts to its default behavior.

**Without Agents** — you type detailed instructions every time:
> *"Act as a security reviewer. Only search code, don't edit anything. Focus on input validation, thread safety..."*

**With Agents** — you select from a dropdown:
> Select `Security-Reviewer`. Done.

---

## 🗂️ Agents in This Project

| Agent | Purpose | Tools | Hands Off To |
|---|---|---|---|
| [**Designer**](designer.agent.md) | Architecture review, SOLID/GRASP, design patterns | search, codebase, usages, fetch | Impact-Analyzer, Agent |
| [**Debugger**](debugger.agent.md) | Systematic root cause analysis | search, codebase, debugger, terminal, problems | Impact-Analyzer, Code-Reviewer |
| [**Impact-Analyzer**](impact-analyzer.agent.md) | Ripple effect analysis, risk assessment | search, codebase, usages, problems | Designer, Code-Reviewer |
| [**Learning-Mentor**](learning-mentor.agent.md) | Concept teaching with analogies & exercises | search, codebase, usages, fetch | Agent, Code-Reviewer |
| [**Code-Reviewer**](code-reviewer.agent.md) | Bug detection, style checks (read-only) | search, codebase, usages | — |
| [**Daily-Assistant**](daily-assistant.agent.md) | Finance, productivity, news, daily life | search, fetch | Learning-Mentor, Agent |
| [**Thinking-Beast-Mode**](Thinking-Beast-Mode.agent.md) | Deep research agent — autonomous, thorough, web-fetching | search, codebase, editFiles, fetch, terminal | — |

---

## 📄 File Format

Agent files are Markdown with a YAML frontmatter header:

```markdown
---
name: Agent-Display-Name
description: 'Short description shown as placeholder in chat input'
tools: ['search', 'codebase', 'editFiles']
model: Claude Sonnet 4.5 (copilot)
handoffs:
  - label: Next Step
    agent: agent
    prompt: Continue with the above.
    send: false
---

# Agent Instructions

Your detailed persona instructions go here.
Written in Markdown. Can be as detailed as needed.
```

### Frontmatter Fields Reference

| Field | Required? | Description | Example |
|---|---|---|---|
| `name` | No | Display name in dropdown (defaults to filename) | `Planner` |
| `description` | No | Placeholder text when agent is active | `'Research-only planning agent'` |
| `tools` | No | Array of tools the agent can use | `['search', 'codebase']` |
| `agents` | No | Sub-agents this agent can invoke (`*` = all) | `['implementation']` |
| `model` | No | AI model to use | `Claude Sonnet 4.5 (copilot)` |
| `handoffs` | No | Workflow transitions to other agents | See [Handoffs](#-handoffs) |
| `user-invokable` | No | Set `false` to hide from dropdown | `false` |
| `disable-model-invocation` | No | Prevent use as a subagent | `true` |

### Available Tools

Available tools evolve as VS Code and Copilot are updated. Full list as of March 2026:

| Tool Name | What It Does | Added |
|---|---|---|
| `search` | Search through workspace files | Original |
| `codebase` | Understand code structure and relationships | Original |
| `editFiles` | Create / edit / delete files | Original |
| `terminal` | Run commands in terminal | Original |
| `fetch` | Fetch web pages | Original |
| `githubRepo` | Search GitHub repositories | Original |
| `usages` | Find references / usages of symbols | Original |
| `problems` | Read compile / lint errors | Original |
| `debugger` | Interact with the VS Code debugger | Original |
| `findTestFiles` | Locate test files for given source files | 2025 |
| `terminalLastCommand` | Get the last command run in the active terminal | 2025 |
| `terminalSelection` | Get the current selection in the active terminal | 2025 |
| `testFailure` | Access test failure information | 2025 |
| `runCommand` | Run a VS Code command programmatically | 2025 |

> **Tip:** Omitting `tools` gives the agent access to **all** tools. Listing specific tools **restricts** it.

---

## 🔀 Handoffs

Handoffs let you chain agents into **workflows**. After the first agent responds, a button appears to transition to the next agent.

```yaml
handoffs:
  - label: Start Implementation       # Button text
    agent: agent                       # Target agent
    prompt: Implement the plan above.  # Pre-filled prompt
    send: false                        # false = user reviews first
```

### Workflow Diagram

```
┌──────────┐     handoff      ┌────────────────┐     handoff      ┌──────────────┐
│ Designer │ ──────────────→  │     Agent      │ ──────────────→  │ Code-Reviewer│
│ (search  │  "Start          │ (editFiles,    │  "Review         │ (search,     │
│  only)   │   Implementation" │  terminal)     │   Changes"      │  codebase)   │
└──────────┘                  └────────────────┘                  └──────────────┘
```

| `send` value | Behavior |
|---|---|
| `true` | Prompt is sent automatically — no user review |
| `false` | Prompt is pre-filled, user reviews and presses Enter |

---

## ✍️ Examples

<details>
<summary><strong>Example 1: Code Reviewer (Read-Only)</strong></summary>

This agent can only search and read — it cannot edit files or run terminal commands.

```markdown
---
name: Code-Reviewer
description: 'Reviews Java code for best practices, bugs, and style issues'
tools: ['search', 'codebase', 'usages']
---

# Code Review Agent

You are a senior Java code reviewer. You review code for:

## Focus Areas
- **Bug detection:** Null pointer risks, off-by-one errors, resource leaks
- **Style:** Naming conventions, code organization, method length
- **Best practices:** Exception handling, immutability, encapsulation

## Rules
- Do NOT edit files — you are read-only
- Always cite specific line numbers
- Rate each issue: Critical / Warning / Suggestion
```

> **Try it:** Select `Code-Reviewer` → ask *"Review Main.java"*

</details>

<details>
<summary><strong>Example 2: Planner (Research + Handoff)</strong></summary>

This agent researches and plans, then hands off to the default agent for implementation.

```markdown
---
name: Planner
description: 'Analyzes code and creates implementation plans without editing files'
tools: ['search', 'codebase', 'fetch', 'usages']
handoffs:
  - label: Start Implementation
    agent: agent
    prompt: Implement the plan outlined above.
    send: false
---

# Planner Agent

You are a planning specialist. Research the codebase and create
a detailed implementation plan — but NEVER edit files yourself.

## Output Format
For each change:
1. **File:** full path
2. **What:** describe the change
3. **Before:** current code
4. **After:** proposed code
5. **Risk:** any concerns
```

> **Try it:** Select `Planner` → ask your question → click **"Start Implementation"**

</details>

<details>
<summary><strong>Example 3: Java Tutor (Educational)</strong></summary>

```markdown
---
name: Java-Tutor
description: 'Teaches Java concepts with explanations, examples, and exercises'
tools: ['search', 'codebase', 'editFiles']
---

# Java Tutor Agent

You are a patient, encouraging Java programming tutor.

## Teaching Style
- Explain WHY, not just WHAT
- Use simple analogies for complex concepts
- Show both the wrong way and the right way
- Include a small exercise after each explanation
```

> **Try it:** Select `Java-Tutor` → ask *"Explain interfaces using a real-world analogy"*

</details>

---

## 📂 How to Create an Agent

### Option A — `/create-agent` Slash Command (New — March 2026 Preview)

> **VS Code built-in wizard:** GitHub Copilot Chat now includes a native `/create-agent` command.

1. Open Copilot Chat (`Ctrl+Shift+I`)
2. Type `/create-agent` in the input box
3. Follow the interactive wizard: enter a name, description, choose tools
4. The wizard generates the `.github/agents/<name>.agent.md` file automatically

Or use the richer guided version in this repo:
```
/create-agent → agentName → purpose → tools → depth
```
File: `.github/prompts/create-agent.prompt.md`

See [copilot-mcp-preview.md](../docs/copilot-mcp-preview.md) for the full `/create-agent` feature details.

### Option B — VS Code Command Palette

1. Press `Ctrl+Shift+P`
2. Type: **Chat: New Custom Agent**
3. Choose **Workspace** (saves to `.github/agents/`)
4. Enter a filename → VS Code generates a template → edit it

### Option C — Manual

1. Create file: `.github/agents/<name>.agent.md`
2. Add frontmatter between `---` markers
3. Write instructions below
4. Save — agent appears in the dropdown immediately

### Where Agents Are Stored

| Location | Scope | Use Case |
|---|---|---|
| `.github/agents/` | Workspace | Shared with team via Git |
| User profile folder | Personal | Available across ALL your workspaces |

---

## ❓ FAQ

<details>
<summary><strong>How do I switch to an agent?</strong></summary>

In the Chat view, click the agent dropdown (top of chat input) and select your agent.

</details>

<details>
<summary><strong>Can I use agents AND instructions at the same time?</strong></summary>

Yes! When you select an agent, `copilot-instructions.md` and matching `*.instructions.md` files are still loaded alongside the agent's instructions.

</details>

<details>
<summary><strong>An agent doesn't appear in the dropdown?</strong></summary>

Check: (1) file is in `.github/agents/`, (2) extension is `.agent.md`, (3) YAML frontmatter is valid.  
Right-click in Chat → **Diagnostics** to see errors.

</details>

<details>
<summary><strong>What's the difference between agents and prompts?</strong></summary>

Agents are **persistent personas** (active throughout the conversation).  
Prompts are **one-shot tasks** (run once and done).

</details>

<details>
<summary><strong>How detailed should agent instructions be?</strong></summary>

As detailed as needed — 20 lines for simple roles, 200+ for complex specialists. Longer = more tokens consumed per request.

</details>

---

## 📌 File Audience Note

This folder contains two types of files:

| File | Audience | Purpose |
|---|---|---|
| `*.agent.md` | 🤖 **Copilot** | Agent persona definitions — loaded when you select the agent from the dropdown |
| `README.md` | 👤 **Developer** | This guide — Copilot does not read it |

When you edit an `.agent.md` file, you change Copilot's persona and behavior. When you edit this README, nothing changes in Copilot — it's documentation for you.

> 📖 **Full breakdown:** [File Reference →](../docs/file-reference.md)

---

## 🧪 Experiments to Try

1. **Create a "No-Code" agent** — set `tools: []` and see what happens (it can only chat, no tools)
2. **Create a "Terminal-Only" agent** — `tools: ['terminal']` — it can only run commands
3. **Create two agents that hand off** — Planner → Implementer workflow
4. **Compare models** — duplicate an agent with a different `model` field and compare outputs

---

<p align="center">

[← Back to main guide](../README.md) · [Instructions](../instructions/README.md) · [Prompts](../prompts/README.md) · [Skills](../skills/README.md) · [File Reference](../docs/file-reference.md) · [Getting Started](../docs/getting-started.md) · [Customization Guide](../docs/customization-guide.md)

</p>
