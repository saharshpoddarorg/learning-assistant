---
id: BLI-030
title: Fix stale paths in configuration-reference.md
status: todo
priority: critical
type: task
created: 2026-05-15
updated: 2026-05-15
epic: EPIC-004
origin: repo-audit-2026-05-15
tags: [documentation, migration, configuration]
---

# BLI-030: Fix Stale Paths in configuration-reference.md

## Description

`.github/docs/configuration-reference.md` has 20+ references to old `mcp-servers/`
paths. As the "single source of truth" for configuration, accuracy is critical.

### Key Changes

- Replace `mcp-servers/build.ps1` → `.\gradlew.bat build`
- Replace `mcp-servers/build.env.local` → `modules/app/build.env.local`
- Replace `mcp-servers/user-config/` → `modules/app/user-config/`
- Replace `mcp-servers/scripts/` → `modules/app/scripts/`
- Update any directory structure diagrams

## Acceptance Criteria

- [ ] Zero references to old `mcp-servers/` directory structure
- [ ] All config file paths point to existing locations
- [ ] `.\__md_lint.ps1` passes
