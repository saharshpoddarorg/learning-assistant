---
applyTo: "**"
---

# Query Alias Expansion

Before interpreting a user request, expand known shorthand aliases from
[`alias-bank.md`](../docs/alias-bank.md). Use the expanded meaning as context for the
request, but keep the user's original wording in mind when precision matters.

## Resolution Rules

- Prefer the alias-bank expansion when the surrounding words support it.
- Ask one clarifying question when an alias has multiple plausible meanings in context.
- Do not expand aliases inside code, file names, branch names, issue keys, commands, URLs,
  quoted text, or exact search strings unless the user explicitly asks for expansion.
- Treat aliases as user vocabulary, not as replacement text that must appear in generated
  output.
- When the user asks to add, remove, or change an alias, update the alias bank and keep this
  instruction file small.

## High-Frequency Aliases

| Alias | Default Meaning |
|---|---|
| `dev` | developer or development, resolved from sentence role |
| `se` | software engineering |
| `pp` | pair programming |
| `dsa` | data structures and algorithms |
| `hld` | high-level design |
| `lld` | low-level design |
| `rca` | root cause analysis |
| `nfr` | non-functional requirement |
| `ac` | acceptance criteria |
