---
id: BLI-004
title: Brain integration with learning resources
status: todo
priority: medium
type: feature
created: 2026-03-26
updated: 2026-03-26
origin: EPIC-001
tags: [learning-resources, brain, pkm, notes, sessions]
---

# BLI-004: Brain integration with learning resources

## Description

Connect the learning resources system with the brain workspace so that learning
sessions, notes, and captured knowledge feed back into resource discovery and
recommendations. The brain tiers (notes, library, sessions) should be aware of
learning resources, and vice versa.

## Acceptance Criteria

- [ ] Learning sessions can reference vault resources
- [ ] Brain search can surface relevant learning resources alongside notes
- [ ] Completed learning paths tracked in brain
- [ ] Session captures for learning topics link to vault entries
- [ ] Resource recommendations based on brain content (what you've studied)

## Notes

- Brain workspace: `brain/ai-brain/` (5 tiers)
- Session captures: `brain/ai-brain/sessions/`
- Learning resources vault: `mcp-servers/src/server/learningresources/vault/`
