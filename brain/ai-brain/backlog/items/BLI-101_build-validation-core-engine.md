---
id: BLI-101
title: Build validation core engine for cross-agent customization profiles
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
tags: [validation, automation, scripts, cross-agent-compliance, powershell, github-copilot, claude, antigravity]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-101: Build validation core engine for cross-agent customization profiles

## Description

Implement the core validation engine in `validate-customization.ps1` so the workspace can
verify structure, metadata, format, and parity rules across platform profiles before any
sync/generation logic is trusted.

## Acceptance Criteria

- [ ] `validate-customization.ps1` supports `-All`, `-Tool`, `-Deep`, `-Strict`, and `-Report`
- [ ] Validation runs against profile definitions for GHCP, Claude-family, Gemini, ChatGPT,
  and Antigravity (validation-only profile allowed)
- [ ] Report separates shared-content parity failures from platform adaptation failures
- [ ] Exit behavior is deterministic: errors fail; warnings fail only with `-Strict`
- [ ] JSON report format is available for CI parsing

## Related

- BLI-097
- BLI-092
- BLI-098
