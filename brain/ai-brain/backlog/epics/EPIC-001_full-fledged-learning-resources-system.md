---
id: EPIC-001
title: Full-fledged learning resources system
status: draft
priority: high
created: 2026-03-26
updated: 2026-04-11
tags: [learning-resources, mcp, skills, brain, sessions]
---

# EPIC-001: Full-fledged learning resources system

## Vision

Transform the current learning resources vault and MCP server into a comprehensive,
production-quality system that combines all 5 Copilot customization primitives
(instructions, prompts, agents, skills, MCP servers) into a hybrid architecture.
The system should have clean module hierarchies, deep brain workspace integration,
and sophisticated chat session orchestration with scoped sessions (project/work/personal),
orchestrator chats, and specialised agents.

## Items

| ID | Title | Status | Priority |
|---|---|---|---|
| BLI-002 | Hybrid skills + MCP server + 5 primitives combo | todo | high |
| BLI-003 | Module hierarchies for learning resources | todo | high |
| BLI-004 | Brain integration with learning resources | todo | medium |
| BLI-005 | Chat session orchestration — scoped sessions, agents | todo | medium |
| BLI-024 | Workflow and terminal conditions framework | todo | medium |
| BLI-025 | Library/glossary for abbreviations and aliases | todo | low |

## Open Questions

- [ ] What is the right boundary between MCP tools vs. skills vs. agent capabilities?
- [ ] Should learning resources be discoverable via brain search, or separate?
- [ ] How should orchestrator chats coordinate between multiple agents?
- [ ] What module hierarchy depth is practical without over-engineering?

## Notes

- Current MCP server: `mcp-servers/src/server/learningresources/`
- Current vault: `mcp-servers/src/server/learningresources/vault/`
- Current skills: `.github/skills/learning-resources-vault/`, `java-learning-resources/`
- Session scoping already exists — see `.github/instructions/session-scoping.instructions.md`
