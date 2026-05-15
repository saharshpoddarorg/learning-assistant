---
id: BLI-028
title: Fix copilot-instructions.md build description and stale paths
status: todo
priority: critical
type: task
created: 2026-05-15
updated: 2026-05-15
epic: EPIC-004
origin: repo-audit-2026-05-15
tags: [documentation, migration, copilot-instructions, gradle]
---

# BLI-028: Fix copilot-instructions.md Build Description and Stale Paths

## Description

`copilot-instructions.md` has two critical inaccuracies:

1. **Line 5:** States `- **Build:** Manual compilation (no build tool yet)` — this is
   wrong since PR #45 migrated to Gradle multi-module
2. **Line 16:** References `.\mcp-servers\build.ps1` — should be `.\gradlew.bat build`

### Changes Required

- Update build description to: `- **Build:** Gradle Kotlin DSL (multi-module, `./gradlew build`)`
- Update build command references from `.\mcp-servers\build.ps1` to `.\gradlew.bat build`
- Verify Project Structure section matches actual `modules/` layout (may already be correct)

## Acceptance Criteria

- [ ] Build tool description is accurate
- [ ] No references to `mcp-servers/build.ps1` remain
- [ ] `.\__md_lint.ps1` passes on the file
