---
id: BLI-022
title: Add export and migration support for easy portability
status: todo
priority: medium
type: feature
created: 2026-04-11
updated: 2026-04-11
started: null
completed: null
blocked-since: null
review-since: null
epic: EPIC-002
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: M
actual-effort: null
tags: [export, migration, jdk, api-keys, portability, documentation]
origin-type: file-import
import-batch: IMP-001
source-file: "D:\\workdir\\MG_FTE\\notepad\\personal dev\\learning-assistant\\gpt.txt"
---

# BLI-022: Add export and migration support for easy portability

## Description

Create comprehensive export and migration documentation and tooling so that the
entire learning-assistant workspace (config, API keys, JDK setup, MCP server config,
brain content, backlog items) can be easily exported, backed up, or migrated to a
new machine or environment. The existing `.github/docs/export-guide.md` and
`export-newbie-guide.md` cover some of this, but need tooling support (scripts,
validation) and coverage of all config files and credentials.

## Future Considerations

- One-command workspace backup/restore script
- Environment validation script ("is this machine ready to run the project?")
- Secrets management integration (e.g., `pass`, `1Password CLI`, `dotenv-vault`)
- Cross-OS migration support (Windows ↔ macOS ↔ Linux)

## Sub-Items

| ID | Title | Status | Effort |
|---|---|---|---|

## Acceptance Criteria

- [ ] Export script — collects all config, env vars, and API keys into a portable bundle
- [ ] Import script — restores from export bundle on a fresh machine
- [ ] JDK setup documentation — path, version, SDKMAN!/Temurin instructions
- [ ] API key inventory — list of all required keys with setup instructions
- [ ] Config file map — which files to copy, which to regenerate
- [ ] Validation command — verify all dependencies and configs after migration
- [ ] Sensitive data handling — API keys excluded from git, documented separately

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Source file | `D:\workdir\MG_FTE\notepad\personal dev\learning-assistant\gpt.txt` | 2026-04-11 | Extracted from file import IMP-001 |
| Existing guide | `.github/docs/export-guide.md` | 2026-04-11 | Current export documentation |
| Existing guide | `.github/docs/export-newbie-guide.md` | 2026-04-11 | Newbie-friendly export docs |

## Related

- [EPIC-002](../epics/EPIC-002_knowledge-consolidation.md) — parent epic

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-04-11 | 10:08 PM | system | created | Created via /read-file-jot from IMP-001 |

## Notes

- Raw input: "appropriate docs etc - support easy export/migration etc -- eg. jdk, api keys.... etc" from "// jot-todos"
- Existing docs provide some coverage but lack automation
- Should tie into the `mcp-servers/SETUP.md` and `build.env` patterns
