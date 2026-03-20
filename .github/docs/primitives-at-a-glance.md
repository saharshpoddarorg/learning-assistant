# GHCP Primitives — At-a-Glance Cheatsheet

> **One-page rapid reference for all 6 GitHub Copilot customization primitives.**
> Print this, pin it, or keep it open while building your `.github/` setup.
>
> **Full guide:** [Copilot Customization Deep Dive](copilot-customization-deep-dive.md)
> **New to this?** [5-Minute Newbie Guide](copilot-customization-newbie.md)

---

## The 5 Sentences (Memorize These)

```text
1. "Copilot must ALWAYS do this"                → instruction
2. "Copilot should KNOW this when relevant"      → skill
3. "I want to TRIGGER a specific task"           → prompt
4. "I want Copilot to BECOME someone different"  → agent
5. "I need LIVE data from an external system"    → MCP server
```

---

## Mega Comparison Card

| | Instructions | Skills | Prompts | Agents | MCP Servers |
|---|---|---|---|---|---|
| **File** | `.instructions.md` | `SKILL.md` | `.prompt.md` | `.agent.md` | `.vscode/mcp.json` |
| **Location** | `.github/instructions/` | `.github/skills/<domain>/` | `.github/prompts/` | `.github/agents/` | `.vscode/` + server code |
| **Activation** | Auto (file glob) | Auto (semantic) | Manual (`/cmd`) | Manual (dropdown) | Auto (agent mode) |
| **Content** | Behavioral rules | Domain knowledge | Workflow template | Persona definition | External tool/API |
| **Stacks** | Yes (many apply) | Yes (many activate) | No (one at a time) | No (one at a time) | Yes (all registered) |
| **Changes** | How Copilot writes | What Copilot knows | What Copilot does | Who Copilot is | What Copilot can DO |
| **Size** | 20-60 lines | 100-500 lines | 10-40 lines | 20-80 lines | Any complexity |
| **Needs code** | No | No | No | No | Yes |

**Also:** `copilot-instructions.md` — ONE project-wide file, always on, 30-80 lines.

---

## Decision Flowchart

```text
Is it a RULE?
├── Yes → Specific file types? → .instructions.md (applyTo glob)
│         All files?           → copilot-instructions.md
└── No → Is it KNOWLEDGE?
    ├── Yes → Static?  → SKILL.md
    │         Dynamic? → MCP server
    └── No → One-shot task?     → .prompt.md
             Persistent persona? → .agent.md
```

---

## Frontmatter Quick Reference

### `.instructions.md`

```yaml
---
applyTo: "**/*.java"
description: "Optional semantic trigger (2026)"
---
```

### `SKILL.md`

```yaml
---
name: my-domain
description: >
  Use when asked about [topic A], [topic B], or [use case].
---
```

### `.prompt.md`

```yaml
---
name: my-command
description: 'Shown in autocomplete'
agent: optional-agent
tools: ['codebase', 'search']
mode: ask
---
```

### `.agent.md`

```yaml
---
description: >
  Use when X. Expert in Y, Z.
tools: [codebase, search, usages]
model: gpt-4o
---
```

---

## Composition Patterns (Quick)

| Goal | Combine |
|---|---|
| Rules + Knowledge (always on) | `.instructions.md` + `SKILL.md` |
| Workflow + Expert mindset | `.prompt.md` + `.agent.md` |
| Expert + Live external data | `.agent.md` + MCP server |
| Rules + Workflow + Expert | `.instructions.md` + `.prompt.md` + `.agent.md` |
| Multi-step sequential analysis | Agent handoff chain |
| Domain slash command with depth | `.prompt.md` + auto-matching `SKILL.md` |
| Maximum power (all 6) | instructions + skill + agent + prompt + MCP + copilot-inst |

---

## Migration Quick Matrix

```text
FROM ↓ / TO →    instructions  skill   prompt  agent   MCP
──────────────────────────────────────────────────────────
instructions     —            ✅ know  ⚠️ rare ❌      ❌
skill            ✅ enforce   —        ❌      ❌      ✅ live
prompt           ❌           ❌       —       ✅ mode  ❌
agent            ❌           ❌       ✅ thin —        ❌
MCP              ❌           ✅ static ❌     ❌      —
```

✅ = natural path — ⚠️ = possible but unusual — ❌ = not applicable

---

## Priority / Stacking Order

```text
1 (highest)  User's message
2            .prompt.md (when invoked)
3            .agent.md (when selected)
4            SKILL.md (when topic matches)
5            .instructions.md (when file matches)
6 (lowest)   copilot-instructions.md (always)
```

MCP servers are tools the agent calls — they don't fit the priority model.

---

## Token Budget

| Primitive | Typical Tokens | Budget Rule |
|---|---|---|
| `copilot-instructions.md` | 200-800 | Keep under 500 — always-on tax |
| `.instructions.md` | 100-300 each | Lean rules; scope narrowly |
| `SKILL.md` | 200-2 000 | OK to be large — loads on-demand |
| `.agent.md` | 100-400 | Moderate — session overhead |
| `.prompt.md` | 50-200 | Lean templates; depth from skills |
| MCP schemas | 50-200/server | Concise tool descriptions |

---

## Glob Patterns (Common)

| Pattern | Matches |
|---|---|
| `**` | Everything (steering modes) |
| `**/*.java` | All Java files |
| `**/*.{ts,tsx}` | TypeScript + TSX |
| `**/*Test.java` | Java test files |
| `**/controller/**` | Controller layer |
| `**/pom.xml` | Maven POM files |
| `**/Dockerfile` | Dockerfiles |

---

## Tool Names (for `tools:` arrays)

**Read:** `codebase`, `search`, `usages`, `problems`, `findTestFiles`,
`terminalLastCommand`, `terminalSelection`, `testFailure`

**Write:** `editFiles`, `runCommands`

**Network:** `fetch`

**Agent:** `runSubagent`

---

## Cross-Tool Portability

| File Type | VS Code Copilot | Claude Code | Gemini CLI | Others |
|---|---|---|---|---|
| `SKILL.md` | ✅ | ✅ | ✅ | ✅ (Agent Skills standard) |
| `AGENTS.md` | ✅ | ✅ | — | — |
| `copilot-instructions.md` | ✅ | — | — | — |
| `.instructions.md` | ✅ | — | — | — |
| `.agent.md` | ✅ | — | — | — |
| `.prompt.md` | ✅ | — | — | — |
| `CLAUDE.md` | ✅ (compat) | ✅ | — | — |

**Maximum portability:** Invest in `SKILL.md` and `AGENTS.md`.

---

## Anti-Pattern Checklist

- ❌ Putting rules in skills (unreliable activation for enforcement)
- ❌ Bloated `copilot-instructions.md` (> 200 lines = move to skills/instructions)
- ❌ Vague skill descriptions (too vague = never activates)
- ❌ Agents without `tools:` restriction (default = ALL tools)
- ❌ Persona in prompts instead of agents (not persistent across conversation)
- ❌ MCP for static content (use a skill instead)
- ❌ Duplicated rules across multiple files (single source of truth)

---

## Further Reading

| Topic | Doc |
|---|---|
| Full 18-part deep dive | [copilot-customization-deep-dive.md](copilot-customization-deep-dive.md) |
| 5-minute newbie intro | [copilot-customization-newbie.md](copilot-customization-newbie.md) |
| Architecture & composition | [customization-guide.md](customization-guide.md) |
| MCP vs Skills decision | [mcp-vs-skills.md](mcp-vs-skills.md) |
| All slash commands | [slash-commands.md](slash-commands.md) |
| Export to other repos | [export-guide.md](export-guide.md) |
| Newbie export walkthrough | [export-newbie-guide.md](export-newbie-guide.md) |
| Skill domain reference | [skills-library.md](skills-library.md) |
| Getting started (30 min) | [getting-started.md](getting-started.md) |

---

**Navigation:** [← START-HERE](START-HERE.md) · [Deep Dive →](copilot-customization-deep-dive.md)
· [Newbie Guide →](copilot-customization-newbie.md) · [MCP vs Skills →](mcp-vs-skills.md)
