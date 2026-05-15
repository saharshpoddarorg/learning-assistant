---
id: BLI-031
title: Fix stale path in mcp-to-skill.prompt.md
status: todo
priority: critical
type: task
created: 2026-05-15
updated: 2026-05-15
epic: EPIC-004
origin: repo-audit-2026-05-15
tags: [prompts, migration, mcp]
---

# BLI-031: Fix Stale Path in mcp-to-skill.prompt.md

## Description

`.github/prompts/customization/mcp-to-skill.prompt.md` line 31 tells users to
locate files in `mcp-servers/src/server/` — a directory that no longer exists.

### Change Required

- Replace `mcp-servers/src/server/` with the correct Gradle module paths:
  `modules/mcp-common/src/main/java/server/`, `modules/mcp-learning-resources/src/main/java/server/`,
  or `modules/mcp-atlassian/src/main/java/server/`

## Acceptance Criteria

- [ ] No references to `mcp-servers/src/` in the file
- [ ] Path guidance points users to correct module locations
