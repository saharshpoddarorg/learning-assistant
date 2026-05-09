```prompt
---
name: context
description: 'Continue from a previous conversation or start fresh — explicitly set the context baseline'
agent: agent
tools: ['codebase', 'usages', 'search', 'editFiles']
---

## Context Mode
${input:contextMode:Type 'continue' to carry forward prior context, or 'fresh' to start clean}

## Prior Context (if continuing)
${input:priorSummary:Briefly summarize what was discussed/decided previously — or type 'none' if starting fresh}

## Current Task
${input:task:What do you want to work on now?}

## File
${file}

## Instructions

### If context mode is `continue`:
You are **resuming a prior conversation**. The user has provided a summary of previous context above.

1. **Acknowledge prior context** — restate the key decisions, findings, or state from the summary to confirm alignment
2. **Identify what changed** — check if the file or codebase has evolved since the prior conversation (look for recent changes, new code, resolved issues)
3. **Build on prior work** — do not re-analyze or re-explain things already covered unless the code changed; focus on the next logical step
4. **Maintain consistency** — use the same naming conventions, patterns, and terminology established in the prior conversation
5. **Track progress** — note what was completed previously vs. what remains

### If context mode is `fresh`:
You are **starting a new conversation** with no prior assumptions.

1. **No assumptions** — do not assume any prior decisions, naming choices, or design direction
2. **Full analysis** — examine the file and codebase from scratch with fresh eyes
3. **Independent assessment** — form your own conclusions about the code's state, design, and naming
4. **Clean slate** — if the user mentions something from a previous session, ask for clarification rather than guessing

### General Rules (both modes)
- Follow all project conventions from [Java instructions](../instructions/java.instructions.md) and [clean code instructions](../instructions/clean-code.instructions.md)
- When making changes, apply naming rules: readable, concise, context-aware, intent-revealing
- State your context mode at the start of your response so the user knows which mode is active
```
