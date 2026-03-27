---
id: BLI-003
title: Module hierarchies for learning resources
status: todo
priority: high
type: feature
created: 2026-03-26
updated: 2026-03-26
origin: EPIC-001
tags: [learning-resources, architecture, modules, concept-areas]
---

# BLI-003: Module hierarchies for learning resources

## Description

Design and implement proper module hierarchies for the learning resources system.
The current flat provider model (one Java class per domain) needs to evolve into a
structured hierarchy where concept areas, resource categories, and providers have
clear parent-child relationships and discoverable navigation paths.

This includes the `ConceptArea` sub-hierarchy framework already started and needs
to be completed across all domains.

## Acceptance Criteria

- [ ] Concept area hierarchy fully defined (parent → child relationships)
- [ ] Resource providers organised by module/domain
- [ ] Navigation: users can drill from broad domain → specific topic
- [ ] Keyword index covers all hierarchy levels
- [ ] No orphan resources — every resource reachable from at least one path
- [ ] Module boundaries documented

## Notes

- Sub-hierarchy framework: `ConceptArea` already has `parentArea()` method
- Current providers: Java, VCS, BuildTools, DevOps, Software Engineering, etc.
- KeywordIndex maps keywords → ConceptArea and ResourceCategory
