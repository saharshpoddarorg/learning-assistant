---
id: BLI-090
title: "Epic: Universal AI customization framework (.github → tool-agnostic)"
status: todo
priority: high
type: epic
created: 2026-07-31
updated: 2026-07-31
started: null
completed: null
blocked-since: null
review-since: null
epic: null
sprint: null
parent: null
sub-items:
  - BLI-091
  - BLI-092
  - BLI-093
  - BLI-094
  - BLI-095
  - BLI-096
  - BLI-097
  - BLI-098
origin: null
estimated-effort: XL
actual-effort: null
tags: [copilot-customization, universal-framework, tool-agnostic, cursor, claude, gemini, chatgpt, instructions, prompts, skills]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-090: Epic — Universal AI customization framework (.github → tool-agnostic)

## Description

Currently, all Copilot customization files live in `.github/`. This framework is
powerful but tightly coupled to VS Code/GitHub Copilot. The goal is to make the
customization primitives (instructions, prompts, skills, agents, MCP tools)
**universal and reusable** across different AI coding assistants:

- **Cursor** (`.claude/` for Claude mode)
- **Claude** (desktop / web editor integrations)
- **Google Gemini** (with Code Execution)
- **ChatGPT** (with Code Interpreter)
- **Other IDE-integrated or chat-based AI assistants**

### Vision

A single unified workspace where domain knowledge, skill definitions, prompt templates,
and instructions are authored **once** in a tool-agnostic format, then **adapted** for
each tool's native customization syntax. Similar to how:

- **Markdown** is tool-agnostic; every Markdown editor renders it similarly
- **OpenAPI specs** define APIs once; tools generate client SDKs
- **YAML pipelines** (GitHub Actions, GitLab CI) are defined once, reused across platforms

### Success Criteria

- [x] All AI tool customization patterns documented and compared (BLI-091)
- [x] Tool-agnostic abstraction layer designed (BLI-092)
- [x] Cursor/.claude/ directory structure implemented and tested (BLI-093)
- [x] Gemini customization pattern documented and scaffolded (BLI-094)
- [x] ChatGPT/OpenAI Assistant pattern documented and scaffolded (BLI-095)
- [x] Migration guides created for each tool (BLI-096)
- [x] Validation and sync scripts created (BLI-097)
- [x] Tool-specific config validation tests added (BLI-098)
- [x] README updated with universal framework overview
- [x] Example customization shown for all supported tools

---

## Context & Rationale

### Problem

Today, if you want to use the same instructions, prompts, and skills in Cursor (Claude mode)
or Gemini, you must:

1. **Manually copy and adapt** the content — error-prone, creates maintenance burden
2. **Maintain two versions** of every instruction file — one for `.github/`, one for `.claude/`
3. **Lose cross-tool consistency** — a change in `.github/` might not propagate to other tools
4. **Lack tool-specific adaptation** — some tools need different syntax or structure (e.g., Claude's
   XMLish blocks vs. Copilot's YAML)

### Opportunity

By defining an **abstraction layer** that captures the intent and structure of customization
primitives once, we can:

1. **Author once** — write in a unified, tool-agnostic format
2. **Adapt locally** — use tool-specific adapters to transform for each AI assistant
3. **Sync easily** — scripts validate and propagate changes across tool directories
4. **Onboard others** — provide clear patterns for users working with other tools

### Tool Coverage (Phase 1)

| Tool | Directory | Native Format | Scope |
|---|---|---|---|
| GitHub Copilot | `.github/` | YAML frontmatter, Markdown | ✅ Existing (foundation) |
| Cursor (Claude mode) | `.claude/` | Markdown + Claude XML blocks | 🔲 To implement |
| Claude (web + desktop) | `.claude/` | Markdown + Claude XML blocks | 🔲 (Same as Cursor) |
| Google Gemini | `.gemini/` | Markdown + JSON schema | 🔲 To scaffold |
| ChatGPT / OpenAI Assistants | `.chatgpt/` | JSON (Assistant config) | 🔲 To scaffold |

### Non-Goals (Phase 1)

- Full parity with IDE plugins (some tools don't support all primitives)
- Automatic code generation for all tool formats (manual adaptation expected)
- Real-time sync during chat sessions (static content sync only)
- Handling of tool-specific features with no equivalent in other tools

---

## Framework Architecture

### Layer 1 — Shared Core (tool-agnostic)

```text
learning-assistant/
  .common/                        ← Tool-agnostic source of truth
    instructions/                 ← Instructions applicable to all tools
    prompts/                      ← Prompt templates (tool-neutral markdown)
    skills/                       ← Skill definitions (structured markdown)
    agents/                       ← Agent personas (role + behavior descriptions)
    mcp/                         ← MCP tool specs (tool-agnostic JSON schema)
    README.md                    ← Mapping showing which content goes where
```

### Layer 2 — Tool-Specific Adapters

```text
learning-assistant/
  .github/                        ← GitHub Copilot (original)
  .claude/                        ← Cursor + Claude (adapted for Claude XML)
  .gemini/                        ← Google Gemini (adapted for Gemini API schema)
  .chatgpt/                       ← ChatGPT / OpenAI Assistants (JSON config)
  tools/                          ← Validation & sync scripts
    sync-customization.ps1       ← Propagate changes from .common → tool-specific dirs
    validate-all-tools.ps1       ← Validate all tool configs for consistency
```

### Layer 3 — Documentation

```text
learning-assistant/
  .github/docs/
    universal-framework-guide.md        ← Overview of tool-agnostic pattern
    tool-migration-guides/
      cursor-claude-setup.md
      gemini-setup.md
      chatgpt-openai-setup.md
      contributing-new-tool.md
```

---

## Implementation Roadmap

### Phase 1a — Analysis & Design (BLI-091, BLI-092)

1. Document existing patterns for each tool (BLI-091)
2. Design the abstraction layer (BLI-092)
3. Create mapping rules for primitives

### Phase 1b — Cursor Support (BLI-093)

1. Create `.claude/` directory structure
2. Adapt `.github/instructions/` → `.claude/instructions/`
3. Create Claude-specific prompt templates
4. Document `.claude` setup in Cursor

### Phase 1c — Gemini & ChatGPT Scaffolding (BLI-094, BLI-095)

1. Create `.gemini/` and `.chatgpt/` structures
2. Document tool-specific patterns and limitations
3. Create template files for easy adoption

### Phase 2 — Validation & Sync (BLI-097, BLI-098)

1. Create PowerShell scripts to validate all tool configs
2. Create sync scripts to propagate changes
3. Add CI/CD validation

### Phase 3 — Documentation & Migration (BLI-096)

1. Write setup guides for each tool
2. Create "Adding a new tool" guide
3. Update root README with universal framework section

---

## Related Backlog Items

- **BLI-091**: Document tool-specific customization patterns
- **BLI-092**: Design tool-agnostic abstraction layer
- **BLI-093**: Implement Cursor/.claude/ directory support
- **BLI-094**: Scaffold Google Gemini customization pattern
- **BLI-095**: Scaffold ChatGPT/OpenAI Assistants pattern
- **BLI-096**: Create tool migration and setup guides
- **BLI-097**: Implement validation and sync scripts
- **BLI-098**: Add tool-specific configuration validation tests

---

## Acceptance Criteria

- [ ] All tool patterns documented (BLI-091 completed)
- [ ] Abstraction layer design reviewed and approved (BLI-092 completed)
- [ ] `.claude/` fully functional in Cursor (BLI-093 completed)
- [ ] `.gemini/` scaffolded with clear templates (BLI-094 completed)
- [ ] `.chatgpt/` scaffolded with config examples (BLI-095 completed)
- [ ] Setup guides written and tested for all tools (BLI-096 completed)
- [ ] Sync and validation scripts created (BLI-097 completed)
- [ ] CI/CD validation tests added (BLI-098 completed)
- [ ] Root README updated with universal framework overview
- [ ] All tool directories pass validation on main

---

## Notes & Assumptions

- We assume each tool has some form of customization API or directory pattern
- Tool-agnostic core lives in `.common/` or within tool-specific directories (TBD by BLI-092)
- "Tool-agnostic" means the intent and content are shareable; syntax will differ
- Initial scope covers 5 tools; pattern extensible to more
