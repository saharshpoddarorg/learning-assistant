---
id: BLI-006
title: Create code formatting instructions and skill
status: todo
priority: medium
type: feature
created: 2026-03-26
updated: 2026-03-26
origin: null
tags: [formatting, intellij, java, code-style, instructions]
---

# BLI-006: Create code formatting instructions and skill

## Description

Create comprehensive code formatting instructions and/or a skill file that codifies
formatting conventions for the project. This covers both IDE-level settings (IntelliJ
inspections, formatter config) and language-specific style rules beyond what
`copilot-instructions.md` currently defines.

Key areas:

- **IntelliJ inspections** — VCS-committed, repo-based `.idea/` settings/config for
  code inspections, formatter profiles, and editor config
- **Stream pipeline formatting** — chained operations on next line (`.map()`, `.filter()`)
- **Currying / method chaining** — continuation on next line
- **Logical operators** — `||`, `&&` on next line (not trailing previous line)
- **Control flow** — `try/catch`, `if-else`, `switch` formatting conventions
- **Language-specific rules** — Java-specific, potentially others later

## Acceptance Criteria

- [ ] Formatting instruction file (`.github/instructions/formatting.instructions.md`)
- [ ] Coverage: stream pipelines, operator placement, control flow, chaining
- [ ] IntelliJ formatter settings committed to repo (`.idea/` or `.editorconfig`)
- [ ] Rules for `||`/`&&` — always on next line
- [ ] Rules for stream pipelines — each `.method()` on its own line
- [ ] Rules for `try-catch` / `if-else` brace style
- [ ] Skill file if formatting guidance is complex enough to warrant one
- [ ] Applied to `*.java` files via `applyTo` glob

## Notes

- Current Java instructions: `.github/instructions/java.instructions.md`
- Current clean-code instructions: `.github/instructions/clean-code.instructions.md`
- Consider `.editorconfig` for cross-IDE portability
- IntelliJ can export formatter settings as XML — these can be VCS-committed
