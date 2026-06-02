---
id: BLI-085
title: Skill-importer versioning + cross-repo re-import enhancements
status: todo
priority: medium
type: feature
created: 2026-06-02
updated: 2026-06-02
started: null
completed: null
blocked-since: null
review-since: null
epic: null
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: M
actual-effort: null
tags: [skill-importer, versioning, cross-repo, re-import, 3-way-merge, provenance, customization-evolution]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-085: Skill-importer versioning + cross-repo re-import enhancements

## Description

Enhance the `skill-importer` skill
(`.github/skills/_modular/skill-importer/SKILL.md`) and its wrapper prompt
(`.github/prompts/_modular/import-skill.prompt.md`) so that re-importing a
previously imported skill — including the **cross-repo enhancement loop** —
is a first-class, explicitly versioned, step-by-step protocol rather than a
side-note inside the existing "Re-Import and 3-Way Merge" section.

Two concrete scenarios must be supported end-to-end with checkpointed steps:

### Scenario A — Upstream source got versioned

> I imported a skill from `awesome-copilot` (or a colleague, GitHub URL, ZIP)
> at v1, then enhanced it locally over time. The upstream source has now
> released v2 (better than my v1 baseline). I want to re-import v2 while
> preserving my local enhancements.

Required behaviour:

- Detect prior import via `.github/skills/_imported-originals/<name>/`
- Determine **next local version** (`vN+1` based on existing
  `_imported-originals/<name>/<date>/_metadata.json` history)
- Run the existing 3-way merge (BASE = stashed v1, OURS = current repo,
  THEIRS = newly fetched v2) but track it under an explicit
  `local-version: N+1` and `re-imports[]` chain in provenance frontmatter
- Stash the new THEIRS as `_imported-originals/<name>/<YYYY-MM-DD>__v<N+1>/`
  without overwriting older snapshots
- Re-evaluate Phase 5 enhancement pack against the merged result
  (some enhancements may already be upstream in v2 and become no-ops)

### Scenario B — Cross-repo enhancement re-import

> I copied this repo's `.github/` folder to my corporate repo to use the
> `code-analysis` skill there. While using it I made observations and
> enhanced the skill in the corporate repo. Now I want to bring those
> enhancements back into this repo by pointing the importer at the
> corporate-repo path of the enhanced skill file/folder.

Required behaviour:

- New mode (e.g. `cross-repo-reimport`) selectable from the prompt
- Source = path to the **enhanced fork** of an already-imported skill (or
  a skill that originated here and was exported sideways)
- BASE detection: use the version snapshot in `_imported-originals/<name>/`
  whose hash is closest to the corporate copy's lineage (fall back to most
  recent if no exact hash match)
- Distinguish from upstream re-import in provenance:
  `re-import-source: cross-repo` plus `cross-repo-origin: <path-or-repo-id>`
- Explicit conflict handling for the common case where BOTH OURS (this
  repo) and THEIRS (corporate fork) have diverged from the same BASE
- Sanitisation pass MUST re-run on the incoming corporate content
  (Phase 2 rules — employer hostnames, AD usernames, confidentiality
  markers) because cross-repo imports are the highest-risk source

## Future Considerations

- Auto-detect cross-repo vs upstream by hashing the source against
  `_imported-originals/<name>/*/SKILL.md` snapshots
- A `/skill-version` slash command that just reports the current local
  version + upstream version + diff summary without importing
- Track "enhancements applied" per local version so v2 → v3 re-imports
  know which enhancements to re-apply automatically vs re-prompt
- Optional GitHub-Actions check that warns when an imported skill drifts
  more than N commits behind its known upstream URL
- A symmetric `export-skill` skill for pushing this repo's skills out
  to other repos (the producer side of Scenario B)

## Sub-Items

| ID | Title | Priority | Status |
|---|---|---|---|

## Acceptance Criteria

- [ ] `SKILL.md` has a new top-level section **"Versioning"** that defines
      `local-version`, the snapshot directory naming convention
      (`_imported-originals/<name>/<YYYY-MM-DD>__v<N>/`), the provenance
      `re-imports[]` schema with explicit step ordering, and how
      `local-version` is incremented (only on re-import, never on local
      enhancement)
- [ ] `SKILL.md` "Re-Import and 3-Way Merge" section is split into
      **two distinct sub-protocols** with their own step-by-step phases:
      (a) **Upstream re-import** (Scenario A), (b) **Cross-repo re-import**
      (Scenario B) — each with its own BASE/OURS/THEIRS resolution rules
- [ ] `SKILL.md` "Modes" table adds `cross-repo-reimport` mode with the
      same checkpointed phase flow as `re-import` plus the cross-repo
      sanitisation re-run requirement
- [ ] `SKILL.md` "Trigger-to-action map" adds rows for the two new
      scenarios so activation is unambiguous
- [ ] Provenance frontmatter schema (in Phase 5) extends with:
      `local-version: N`, `upstream-version-hash`, `cross-repo-origin`,
      `re-import-source: upstream | cross-repo`, and an enriched
      `re-imports[]` entry shape
- [ ] `import-skill.prompt.md` exposes the new mode in its `${input:mode}`
      list and documents the cross-repo source-path expectation in the
      `${input:source}` hint
- [ ] Anti-patterns section gains 2 entries: "overwriting an older
      `_imported-originals` snapshot on re-import" and "skipping
      sanitisation on cross-repo re-imports"
- [ ] Worked example walks through Scenario A end-to-end (v1 import →
      local enhance → v2 re-import → merged v2 with enhancements preserved)
- [ ] Worked example walks through Scenario B end-to-end (export via
      `.github/` copy → enhance in corporate repo → re-import back)
- [ ] Cross-references added/updated in `customization-evolution-guide.md`,
      `change-completeness.instructions.md` (Section O), `TAXONOMY.md`,
      and `skills-library.md` per Section H of the completeness checklist
- [ ] `.\__md_lint.ps1` exits 0 after all edits

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Skill to enhance | `.github/skills/_modular/skill-importer/SKILL.md` | 2026-06-02 | Primary file |
| Prompt to enhance | `.github/prompts/_modular/import-skill.prompt.md` | 2026-06-02 | Wrapper prompt |
| Reference guide | `.github/docs/customization-evolution-guide.md` | 2026-06-02 | Holds Import/Merge protocols; must stay in sync |
| Stash directory | `.github/skills/_imported-originals/` | 2026-06-02 | Snapshot home; naming convention changes |
| Related item | `brain/ai-brain/backlog/features/BLI-023_cross-repo-merge-sync.md` | 2026-06-02 | Same cross-repo theme for backlog items |
| Related item | `brain/ai-brain/backlog/features/BLI-022_export-migration-portability.md` | 2026-06-02 | Producer side of the cross-repo flow |

## Related

- [BLI-023](BLI-023_cross-repo-merge-sync.md) — cross-repo merge/sync for
  backlog items (parallel concept on a different tier)
- [BLI-022](BLI-022_export-migration-portability.md) — export & migration
  primitives; the natural producer counterpart to cross-repo re-import
- [BLI-026](BLI-026_evolve-6-primitive-layered-architecture.md) — broader
  evolution of the 6-primitive architecture this importer participates in

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-06-02 | 04:49 PM | system | created | Item created from user request to add explicit versioning + cross-repo re-import support to skill-importer |

## Notes

- The existing skill already has a "Re-Import and 3-Way Merge" section and
  a `re-import` mode — this BLI is about making versioning **explicit and
  numbered**, splitting upstream vs cross-repo flows, and adding worked
  examples plus the missing provenance fields.
- The cross-repo flow is the riskier path because it can carry
  employer-confidential content. Sanitisation re-run is non-negotiable
  for that mode.
- Snapshot directory naming change (`<YYYY-MM-DD>__v<N>/` instead of
  just `<YYYY-MM-DD>/`) is backward-compatible if the importer falls
  back to date-only directories when no `__v<N>` suffix is present.

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | M |
| Actual effort | — |
| Created | 2026-06-02 |
| Started | — |
| Completed | — |
| Cycle time | — |
