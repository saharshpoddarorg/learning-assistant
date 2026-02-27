---
applyTo: "**"
---

# Steering Modes — Default Behavior Profile

> **Purpose:** Define the default "steering mode" for all AI-assisted work in this repo.
> **When active:** Always — this instruction applies to every file via `applyTo: "**"`.

---

## What Is a Steering Mode?

A **steering mode** is a named behavioral profile that tells Copilot *how to work*
on this repo — not just *what to do* on a specific file. It controls:

- **Completeness** — how thoroughly to propagate a change across related files
- **Output depth** — how much context and explanation to include
- **Side-effect scope** — which adjacent files to touch beyond the immediate request
- **Verification** — whether to build/test before considering work done
- **Commit style** — whether to produce one commit or many

---

## Modes in This Project

### 🟢 `completeness` — DEFAULT (active now)

> **File:** `.github/instructions/change-completeness.instructions.md`
> **Scope:** `**` (all files — always on)

The completeness mode is the project's **default**. Every change is treated as a
"ripple" — you never add something in isolation. When making any change, you:

1. Follow the **Change Completeness Checklist** for the change type (A–F)
2. Update all related files: vault providers, prompt files, skill files, slash-commands, docs
3. Ensure the build passes before committing (`.\mcp-servers\build.ps1`)
4. Use a single, clean Conventional Commit wrapping the logical unit of work

This is the mode that makes this repo iterative and always-consistent.

---

### 🧠 `beast` — Deep Research Mode

> **Agent:** `Thinking-Beast-Mode` (select from dropdown)
> **Activation:** Select **"Thinking Beast Mode"** agent in VS Code Chat

Use when:
- Solving complex, multi-file problems that require broad research
- Fetching and synthesizing information from multiple web sources
- Debugging hard issues that need recursive investigation

**Does NOT replace completeness** — Beast mode gives depth; completeness gives breadth.
After a beast-mode session, always apply the completeness checklist.

---

### 🎓 `learning` — Teaching Mode

> **Agent:** `Learning-Mentor` (select from dropdown)
> **Commands:** `/learn-concept`, `/deep-dive`, `/dsa`, `/system-design`, `/mcp`

Use when:
- Learning a new concept rather than implementing a change
- Wanting theory-first explanations with code following
- Preparing for interviews, reading documentation, studying algorithms

Does not apply the completeness checklist — learning mode is observe-only unless
you explicitly ask Copilot to make code changes.

---

### 🏗️ `design` — Architecture & Review Mode

> **Agent:** `Designer` (select from dropdown)
> **Commands:** `/design-review`, `/refactor`

Use when:
- Reviewing SOLID/GRASP violations
- Planning a refactoring before implementing it
- Evaluating architecture options (without committing to one yet)

---

### 🐛 `debug` — Investigation Mode

> **Agent:** `Debugger` (select from dropdown)
> **Command:** `/debug`

Use when:
- Tracing a bug with systematic hypothesis-driven analysis
- Need to inspect code without making changes first
- Reading stack traces and correlating with code

---

### 📖 `focused` — Minimal, Single-File Mode

> **Activation:** Use the `/scope` command → `specific`

Use when:
- Making a tiny, isolated change scoped to ONE file
- Speed matters more than completeness (fast iteration in early experimentation)

> ⚠️ **Warning:** Focused mode skips the completeness checklist.
> Switch back to `completeness` before committing to the repo.

---

## How Steering Modes Stack

Modes stack on top of each other:

```
copilot-instructions.md     ← project conventions (always on)
    + change-completeness   ← completeness mode (always on, ** glob)
    + steering-modes        ← this file (always on, ** glob)
    + java.instructions     ← only when editing .java files
    + clean-code.inst.      ← only when editing .java files
    + Agent persona         ← only when agent is selected in dropdown
    + /command context      ← only during that chat message
```

---

## Switching Modes

| Mode | How to Activate |
|---|---|
| `completeness` (default) | Always active — no action needed |
| `beast` | Chat dropdown → **"Thinking Beast Mode"** |
| `learning` | Chat dropdown → **"Learning-Mentor"**, or type `/learn-concept` |
| `design` | Chat dropdown → **"Designer"**, or type `/design-review` |
| `debug` | Chat dropdown → **"Debugger"**, or type `/debug` |
| `focused` | Type `/scope` → `specific` in the chat before your request |

Use `/steer` (the slash command) to get a quick summary of available modes and switch.

---

## Making `completeness` the Default

The completeness steering mode is **already the default** in this project because
`change-completeness.instructions.md` has `applyTo: "**"`.

To reinforce this default whenever you start a session or hand off to another session:

```
Recommended session start:
  1. Type: /context → continue (to reconnect to prior work)
  2. Or: /steer → completeness (to confirm the mode is active)
  3. Then: Start your work — the completeness checklist applies automatically
```

---

## Adding a New Mode

To add a custom steering mode:

1. Create `.github/instructions/<mode-name>.instructions.md` with `applyTo` set
2. Add it to the **Modes in This Project** table above
3. Add it to the **Switching Modes** table above
4. Optionally create a `.github/prompts/<mode-name>.prompt.md` slash command
5. Update `slash-commands.md` and `hub.prompt.md`
