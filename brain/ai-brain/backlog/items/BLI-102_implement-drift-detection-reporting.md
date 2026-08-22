---
id: BLI-102
title: Implement drift detection and reporting for customization artifacts
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
estimated-effort: S
actual-effort: null
tags: [drift-detection, reporting, automation, scripts, ci, powershell]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-102: Implement drift detection and reporting for customization artifacts

## Description

Implement `check-drift.ps1` to track how far target profiles drift from source customization
files and provide actionable output for local usage and CI warning gates.

## Acceptance Criteria

- [ ] `check-drift.ps1` supports thresholding via `-Days` and scoped reports via `-Report`
- [ ] Drift logic compares source modification timestamps/content hash against target outputs
- [ ] Output flags stale files with remediation hint (sync command)
- [ ] JSON and text summary outputs are available
- [ ] Drift checker integrates cleanly with validation pipeline from BLI-101

## Related

- BLI-097
- BLI-101
