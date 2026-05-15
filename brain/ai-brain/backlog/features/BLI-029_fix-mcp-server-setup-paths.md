---
id: BLI-029
title: Fix stale paths in mcp-server-setup.md
status: todo
priority: critical
type: task
created: 2026-05-15
updated: 2026-05-15
epic: EPIC-004
origin: repo-audit-2026-05-15
tags: [documentation, migration, mcp, setup]
---

# BLI-029: Fix Stale Paths in mcp-server-setup.md

## Description

`.github/docs/mcp-server-setup.md` has 22+ references to old `mcp-servers/` paths.
This is a critical setup guide — incorrect paths prevent new users from setting up
MCP servers.

### Key Changes

- Replace all `mcp-servers/` path references with correct `modules/` equivalents
- Replace `mcp-servers/build.ps1` with `.\gradlew.bat build`
- Replace `mcp-servers/user-config/` with `modules/app/user-config/`
- Replace `mcp-servers/build.env.local` with `modules/app/build.env.local`
- Verify all code blocks and commands still work

## Acceptance Criteria

- [ ] Zero references to `mcp-servers/` directory in the file
- [ ] All code blocks reference correct paths
- [ ] `.\__md_lint.ps1` passes
