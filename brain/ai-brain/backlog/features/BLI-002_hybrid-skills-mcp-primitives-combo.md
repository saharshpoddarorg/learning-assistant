---
id: BLI-002
title: Hybrid skills + MCP server + 5 primitives combo
status: todo
priority: high
type: feature
created: 2026-03-26
updated: 2026-03-26
origin: EPIC-001
tags: [learning-resources, mcp, skills, instructions, prompts, agents]
---

# BLI-002: Hybrid skills + MCP server + 5 primitives combo

## Description

Design and implement a hybrid architecture that properly combines all 5 Copilot
customization primitives for the learning resources system:

1. **Instructions** (`.instructions.md`) — always-on rules for how resources are managed
2. **Prompts** (`.prompt.md`) — slash commands for resource discovery and interaction
3. **Skills** (`SKILL.md`) — domain knowledge activated by context
4. **Agents** (`.agent.md`) — specialised agents for learning workflows
5. **MCP servers** — runtime tools for search, scrape, and vault operations

The current system uses MCP tools + a few skills. This needs to be elevated into a
coherent hybrid where each primitive handles what it's best at.

## Acceptance Criteria

- [ ] Clear responsibility matrix: which primitive handles what
- [ ] Instructions for learning resource conventions and standards
- [ ] Prompt files for common learning workflows
- [ ] Skills activated by domain context (not just manually invoked)
- [ ] Agent(s) for learning mentor workflows
- [ ] MCP tools for runtime operations (search, fetch, scrape)
- [ ] No duplication between primitives — each has a clear lane
- [ ] Documentation of the hybrid architecture

## Notes

- See `copilot-customization` skill for primitive guidance
- Current `/resources` prompt already exists
- `learning-mentor` agent already exists — extend or refine
