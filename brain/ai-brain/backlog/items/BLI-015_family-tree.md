---
id: BLI-015
title: Family tree application
status: todo
priority: low
type: feature
created: 2026-03-28
updated: 2026-03-28
epic: EPIC-003
parent: null
sub-items: []
origin: null
tags: [project:family-tree, genealogy, family, tree-visualization, data-modelling]
---

# BLI-015: Family tree application

## Description

Build a personal family tree application for recording, organizing, and visualizing
family relationships, history, and biographical information. Support multiple
generations, relationship types, photo attachments, and interactive tree
visualization. Consider GEDCOM standard for import/export compatibility with
existing genealogy software.

## Acceptance Criteria

- [ ] Person data model — name, DOB, birthplace, photo, bio, relationships
- [ ] Relationship modelling — parent-child, spouse, sibling (derived), in-laws
- [ ] Tree visualization — interactive graph/tree view (expandable/collapsible nodes)
- [ ] Multiple generations — support 5+ generations in both directions
- [ ] GEDCOM import/export — compatibility with Family Tree Maker, Ancestry, Gramps
- [ ] Search and filter — find persons by name, date, location, relationship
- [ ] Photo gallery — attach multiple photos per person with captions and dates
- [ ] Timeline view — chronological view of births, marriages, deaths
- [ ] Event tracking — marriages, moves, education, career milestones
- [ ] Relationship path finder — show how two people are related (e.g., second cousin)
- [ ] Data validation — detect inconsistencies (DOB vs parent DOB, impossible dates)
- [ ] Privacy controls — mark living people's data as private in exports
- [ ] Print-friendly — generate PDF or image of the family tree for sharing

## Notes

- GEDCOM 5.5.5 is the industry standard for genealogy data interchange
- Graph data model — consider Neo4j for relationship queries, or simple adjacency list
- Tree visualization: D3.js, Vis.js, or Mermaid for rendering
- Tech stack: Java backend + web frontend, or full desktop app (JavaFX)
- Existing tools for reference: Gramps (open source), FamilySearch, Ancestry
- Important: handle incomplete/uncertain data gracefully (approximate dates, unknown parents)
