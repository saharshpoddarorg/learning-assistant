```prompt
---
name: debug
description: 'Systematically investigate and diagnose a bug or unexpected behavior'
agent: debugger
tools: ['codebase', 'usages', 'search', 'problems', 'testFailure', 'terminalLastCommand']
---

I'm encountering a problem. Help me debug it systematically.

## Context
${file}

## Instructions
1. **Gather evidence** — examine the file, related errors, and recent terminal output
2. **Reproduce** — identify the exact conditions that trigger the issue
3. **Form hypotheses** — list ranked hypotheses with predicted outcomes
4. **Investigate** — trace the code path, check data flow, examine edge cases
5. **Root cause** — pinpoint the exact cause with evidence
6. **Fix** — propose the minimal fix with explanation
7. **Verification** — suggest how to verify the fix and what test to add
```
