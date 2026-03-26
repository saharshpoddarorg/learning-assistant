---
id: BLI-005
title: Chat session orchestration — scoped sessions, agents
status: todo
priority: medium
type: feature
created: 2026-03-26
updated: 2026-03-26
origin: EPIC-001
tags: [sessions, agents, orchestration, scoping, chat]
---

# BLI-005: Chat session orchestration — scoped sessions, agents

## Description

Build out the chat session orchestration layer — scoped sessions (project/work/personal),
orchestrator chats that coordinate across agents, and specialised agent workflows.

Current session scoping (global/project/feature) exists in instructions but needs to
be operationalised with proper tooling and agent support. Orchestrator patterns need
to be designed for multi-agent coordination (e.g., learning-mentor + designer +
code-reviewer working on the same project context).

## Acceptance Criteria

- [ ] Session scoping works end-to-end (project/work/personal routing)
- [ ] Orchestrator chat pattern defined and documented
- [ ] Multi-agent coordination — hand-off context between agents
- [ ] Session continuity — resume context across chat sessions
- [ ] Agent-specific session capture (different agents produce different outputs)
- [ ] Cross-session search and linking

## Notes

- Session scoping instructions: `.github/instructions/session-scoping.instructions.md`
- Chat capture instructions: `.github/instructions/chat-capture.instructions.md`
- Existing agents: learning-mentor, designer, debugger, Code-Reviewer, Thinking Beast Mode
- Multi-session prompt: `.github/prompts/multi-session.prompt.md`
