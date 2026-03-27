---
id: BLI-006
title: Create code formatting instructions and skill
status: done
priority: medium
type: feature
created: 2026-03-26
updated: 2026-03-27
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

- [x] ~~Formatting instruction file~~ → Created as skill (`.github/skills/java-formatting/SKILL.md`) — opt-in to save tokens
- [x] Coverage: stream pipelines, operator placement, control flow, chaining
- [x] IntelliJ formatter settings committed to repo (`.idea/codeStyles/`, `.idea/inspectionProfiles/`)
- [x] Rules for `||`/`&&` — always on next line
- [x] Rules for stream pipelines — each `.method()` on its own line
- [x] Rules for `try-catch` / `if-else` brace style
- [x] Skill file created — comprehensive formatting + inspection reference
- [x] Registered in `copilot-instructions.md` as opt-in skill

## Notes

- Current Java instructions: `.github/instructions/java.instructions.md`
- Current clean-code instructions: `.github/instructions/clean-code.instructions.md`
- Consider `.editorconfig` for cross-IDE portability
- IntelliJ can export formatter settings as XML — these can be VCS-committed
