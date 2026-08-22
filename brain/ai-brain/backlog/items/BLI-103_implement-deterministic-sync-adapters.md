---
id: BLI-103
title: Implement deterministic sync pipeline with profile adapters
status: todo
priority: medium
type: feature
created: 2026-08-22
updated: 2026-08-22
started: null
completed: null
blocked-since: null
review-since: null
epic: BLI-090
sprint: null
parent: BLI-097
sub-items: []
origin: null
estimated-effort: M
actual-effort: null
tags: [sync, adapters, generation, powershell, deterministic-output, claude, gemini, chatgpt]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-103: Implement deterministic sync pipeline with profile adapters

## Description

Implement `sync-customization.ps1` and `transform-for-tool.ps1` so source artifacts can be
adapted for target platforms in deterministic order with dry-run safety and clear change
reporting.

## Acceptance Criteria

- [ ] `sync-customization.ps1` supports `-Source`, `-Target`, `-DryRun`, `-Force`, and `-Verbose`
- [ ] Adapter transformation path is profile-driven and reproducible across runs
- [ ] Dry-run output clearly shows create/update/skip actions
- [ ] Sync operation is idempotent (repeat run without source changes yields no diff)
- [ ] Unsupported primitive mappings are explicit warnings/errors

## Related

- BLI-097
- BLI-101
- BLI-104
