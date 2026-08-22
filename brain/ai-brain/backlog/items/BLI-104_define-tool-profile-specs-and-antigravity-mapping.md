---
id: BLI-104
title: Define tool profile specs and Antigravity mapping contract
status: todo
priority: medium
type: design
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
tags: [tool-profiles, schema, mapping, antigravity, config, validation]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-104: Define tool profile specs and Antigravity mapping contract

## Description

Define and version `tool-specs.json` and transformation rule contracts used by validation and
sync scripts, including a first-pass Antigravity profile that can run validation even before
full adapter implementation.

## Acceptance Criteria

- [ ] `tool-specs.json` includes GHCP, Claude-family, Gemini, ChatGPT, and Antigravity profiles
- [ ] Each profile defines directory roots, artifact classes, metadata requirements, and limits
- [ ] Unsupported primitives policy is defined per profile (warn vs error)
- [ ] Profile schema is documented with examples
- [ ] Backward-compatible profile evolution strategy is documented

## Related

- BLI-097
- BLI-092
- BLI-103
