---
id: BLI-091
title: Document tool-specific customization patterns (Cursor, Claude, Gemini, ChatGPT)
status: todo
priority: high
type: analysis
created: 2026-07-31
updated: 2026-07-31
started: null
completed: null
blocked-since: null
review-since: null
epic: BLI-090
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: M
actual-effort: null
tags: [analysis, tool-comparison, cursor, claude, gemini, chatgpt, customization-api, research]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-091: Analysis — Document tool-specific customization patterns

## Description

Before designing the universal framework (BLI-092), we need a comprehensive audit of how
each AI coding assistant supports customization. This BLI produces detailed documentation
of each tool's capabilities, limitations, and native customization patterns.

### Goals

1. **Understand each tool's native customization** — where files live, what formats they use,
   how the tool loads them
2. **Identify common patterns** — what primitives exist across tools (instructions, prompts,
   rules, context, tools)
3. **Spot gaps and limitations** — which tools don't support certain primitives
4. **Create a comparison matrix** — visual summary showing feature parity
5. **Document by-hand procedures** — for tools without native directory/file support

---

## Research Scope

### Tools to Research

| Tool | Tier | Research Focus |
|---|---|---|
| **GitHub Copilot** | ✅ Complete | Baseline; Copilot-specific, VS Code exclusive |
| **Cursor** | 🔲 Research | Claude-powered; `.cursor/` or similar; how to sync with Copilot |
| **Claude** | 🔲 Research | Web + desktop; custom instructions, system prompts, character.ai-like features |
| **Google Gemini** | 🔲 Research | Web + extensions; custom instructions, context API, extensions |
| **ChatGPT** | 🔲 Research | Web + GPT Builder; Custom Instructions, file upload, system prompts |
| **Other tools?** | 📍 TBD | Open source: LLaVA, Ollama; IDEs: VS Code + CodeGPT, Codeium |

### Research Questions

For each tool, answer:

1. **Where do customization files live?**
   - Directory structure (`.cursor/`, `.claude/`, environment variables, config files)?
   - File format (Markdown, YAML, JSON, XML)?
   - Load mechanism (automatic on startup, API call, UI config)?

2. **What primitives does it support?**
   - Instructions / System Prompts (custom guidelines)?
   - Prompts / Templates (slash commands, multi-turn flows)?
   - Skills / Tools (integrating external APIs or MCP tools)?
   - Agents / Personas (role-based profiles)?
   - Context Management (how to inject project-specific knowledge)?

3. **Limitations & gotchas?**
   - Token limits on instructions?
   - File size limits?
   - Supported file formats?
   - Does it cache or re-load on each session?
   - How are credentials handled?

4. **Access & setup?**
   - Free tier or Pro/paid?
   - API available for automation?
   - Official documentation or community guides?
   - GitHub repo or closed-source?

### Deliverables

#### D1 — Tool Customization Pattern Document

A detailed Markdown file (`.github/docs/tool-customization-patterns.md`) covering each
tool with:

- **Tool name & overview** — what it is, where to get it
- **Native customization support** — directory structure, file formats, loading mechanism
- **Supported primitives** — checklist of instructions, prompts, skills, agents, context
- **Limitations** — token limits, format constraints, caching, credentials
- **Access & setup** — free/paid, API availability, documentation links
- **Example usage** — snippet showing how to add a custom instruction or prompt
- **Tool-specific gotchas** — quirks or workarounds needed
- **Mapping to Copilot** — which Copilot `.github/` primitives map to this tool

#### D2 — Comparison Matrix

A visual table (`.github/docs/tool-feature-matrix.md`) showing:

```markdown
| Primitive | Copilot | Cursor | Claude | Gemini | ChatGPT | Support |
|---|---|---|---|---|---|---|
| Instructions | ✅ Full | ✅ Full | ✅ Custom Inst. | ✅ Custom Inst. | ✅ Custom Inst. | 5/5 |
| Prompts/Templates | ✅ Full | ✅ Full | ⚠️ Limited | ⚠️ Limited | ⚠️ Limited | 2/5 |
| Skills/MCP Tools | ✅ Full | ❌ No | ⚠️ Claude Tools | ⚠️ Extensions | ⚠️ GPT Actions | 2/5 |
| Agents/Personas | ✅ Full | ✅ Roles | ⚠️ Limited | ⚠️ Personas | ✅ GPT Modes | 4/5 |
```

#### D3 — Tool Setup Checklist

A per-tool setup checklist (`.github/docs/tool-setup-checklist.md`) listing:

- Prerequisites (IDE version, API key, account type)
- Installation steps
- Initial configuration
- Verification steps
- Troubleshooting links

---

## Research Method

1. **Official documentation** — read each tool's official docs, API references, guides
2. **Community knowledge** — check Reddit, GitHub issues, Discord, Hacker News discussions
3. **Hands-on testing** — if accessible, install/try the tool and test customization loading
4. **Expert interviews** — if available, interview users or maintainers of each tool
5. **Comparative analysis** — identify commonalities and differences

---

## Acceptance Criteria

- [ ] Tool Customization Pattern document written and reviewed (≥4 pages)
- [ ] All 6 tools researched and documented
- [ ] Comparison matrix created and verified
- [ ] Setup checklists created for each tool
- [ ] External links validated (no 404s)
- [ ] Examples tested or verified as current
- [ ] Gotchas and limitations clearly flagged
- [ ] Document integrated into `.github/docs/` navigation

---

## Resources & Links

- [GitHub Copilot Docs](https://docs.github.com/en/copilot)
- [Cursor Docs](https://cursor.sh/)
- [Claude Custom Instructions](https://claude.ai/account/settings) (requires account)
- [Google Gemini Extensions](https://gemini.google.com/)
- [ChatGPT GPT Builder](https://openai.com/blog/gpts/)
- [LLaVA Project](https://llava-vl.github.io/)
- [Codeium](https://codeium.com/)

---

## Dependencies & Blockers

- No blockers — pure research, can start immediately
- Output feeds into BLI-092 (design phase)

---

## Notes

- Keep tone objective and factual; avoid promotion of any one tool
- Focus on **capability gaps** not tool quality
- Document as-of the current date; note that these tools evolve rapidly
- Include version numbers or API versions where applicable
