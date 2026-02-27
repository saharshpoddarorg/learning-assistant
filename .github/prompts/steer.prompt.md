---
name: steer
description: >
  View or switch the active steering mode for this Copilot session.
  Modes control how thoroughly Copilot propagates changes, how deep it goes,
  and which behavioral profile to apply. Default: completeness mode.
agent: copilot
tools:
  - codebase
---

# /steer — Steering Mode Navigator

## What do you want to do?

${input:action:What steering action? (view / switch / explain / default):view}

## Target mode (optional, for switch)

${input:mode:Which mode to switch to? (completeness / beast / learning / design / debug / focused):completeness}

---

## Instructions

### If `action` = `view` or `status`

Show the user the **current and all available steering modes**:

```
📡 Steering Modes — Current Session

Default (always on):
  ✅ completeness   — Change Completeness checklist applied to every edit
                       Activated by: change-completeness.instructions.md (applyTo: **)
                       Rule: Never add in isolation. Every change has a ripple.

Optional modes (activate via agent dropdown or slash command):
  🧠 beast          — Thinking-Beast-Mode agent (deep research, web fetch)
  🎓 learning       — Learning-Mentor agent (theory, analogies, exercises)
  🏗️ design         — Designer agent (SOLID/GRASP, architecture review)
  🐛 debug          — Debugger agent (systematic root cause analysis)
  📖 focused        — /scope specific (single file, skips completeness)

To switch mode:    /steer → switch → <mode-name>
To explain mode:   /steer → explain → <mode-name>
To reset default:  /steer → default
```

---

### If `action` = `switch`

Guide the user to activate their chosen mode:

```
Switching to: ${input:mode}

completeness  →  Already active (default). Use /context continue to carry forward.
beast         →  Select "Thinking Beast Mode" from the Chat agent dropdown.
learning      →  Select "Learning-Mentor" from the Chat dropdown, or type /learn-concept.
design        →  Select "Designer" from the Chat dropdown, or type /design-review.
debug         →  Select "Debugger" from the Chat dropdown, or type /debug.
focused       →  Type /scope → specific, then make your request.
```

After switching, remind the user:
> **Tip:** After finishing focused/beast/debug work, return to `completeness` mode
> before committing. Run the checklist from `change-completeness.instructions.md`.

---

### If `action` = `explain`

Explain the chosen mode in detail. Pull from `.github/instructions/steering-modes.instructions.md`:

- What the mode is for
- When to use it
- How to activate it
- What it changes about Copilot's behavior
- How it stacks with other modes

---

### If `action` = `default`

Confirm and reinforce the default mode:

```
✅ Default Steering Mode: completeness

The completeness mode is always active in this project.
It is enforced by: .github/instructions/change-completeness.instructions.md

What this means for your current session:
  1. Every change triggers the Change Completeness Checklist (A–F)
  2. Build must pass before committing: .\mcp-servers\build.ps1
  3. Cross-reference files must be updated: slash-commands, hub, skills, prompts, docs
  4. Commit follows Conventional Commits with attribution

If you want to temporarily use a lighter mode, type: /steer → switch → focused
Remember to return to completeness before your final commit.
```

---

## Mode Reference Card

```
Mode           | Agent/Activation          | When to Use
completeness   | Always active (**glob)    | ANY repo change — the default
beast          | Thinking-Beast-Mode agent | Deep research, complex multi-file problems
learning       | Learning-Mentor agent     | Learn concepts, study, prepare
design         | Designer agent            | Architecture review, SOLID checks
debug          | Debugger agent            | Bug hunting, stack trace analysis
focused        | /scope → specific         | Tiny isolated single-file edits only
```

## Stacking Reference

```
copilot-instructions.md         (always)
  + change-completeness.md      (always, ** glob) ← DEFAULT MODE
  + steering-modes.md           (always, ** glob) ← THIS FILE
  + java.instructions.md        (*.java only)
  + Agent persona (dropdown)    (session-scoped)
  + /command (prompt)           (per-message)
```
