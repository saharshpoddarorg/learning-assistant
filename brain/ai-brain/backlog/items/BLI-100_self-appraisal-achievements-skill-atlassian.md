---
id: BLI-100
title: Create achievements and self-appraisal skill using Atlassian artefacts
status: todo
priority: medium
type: feature
created: 2026-08-01
updated: 2026-08-01
started: null
completed: null
blocked-since: null
review-since: null
epic: null
sprint: null
parent: BLI-099
sub-items: []
origin: null
estimated-effort: M
actual-effort: null
tags: [skill, appraisal, achievements, self-review, performance-review, jira, bitbucket, confluence, career]
---

# BLI-100 — Create achievements and self-appraisal skill using Atlassian artefacts

## Summary

Build a `.github/skills/career/self-appraisal/SKILL.md` that helps a developer write
performance review documentation — achievements, impact statements, and self-assessment
scoring — by querying their real delivery history from Jira, Bitbucket, and Confluence.

## Motivation

Annual or quarterly performance reviews require developers to articulate their impact in
business terms. Most developers undersell themselves because translating code changes into
business outcomes is hard. This skill mines the same Atlassian sources as BLI-099 but
formats the output for an internal appraisal system rather than an external CV — producing
categorised achievement bullets, impact metrics, and self-scoring narratives aligned with
common engineering competency frameworks (e.g., individual contributor levels, SFIA, etc.).

## Acceptance Criteria

- [ ] Skill file at `.github/skills/career/self-appraisal/SKILL.md` with YAML frontmatter
  and a description that activates on keywords: appraisal, self-review, performance review,
  achievements, impact statement, self-assessment
- [ ] **Achievement extraction** — aggregates from:
  - Jira: completed stories/epics with story points and sprint names → quantify delivery
    volume and complexity
  - Bitbucket: merged PRs → code contribution metrics (PRs merged, review comments
    received/given, files changed, hotfix vs. feature)
  - Confluence: pages created/co-authored → knowledge-sharing and documentation impact
- [ ] **Competency mapping** — skill maps extracted artefacts to common engineering
  competency dimensions, e.g.:
  - **Delivery** (stories completed, on-time sprint record)
  - **Technical quality** (PR review comments, defect count from bug tickets)
  - **Collaboration** (PRs reviewed for others, comments given, Confluence contributions)
  - **Leadership / influence** (ADRs authored, design docs, mentoring evidence in comments)
  - **Innovation** (spike stories, R&D tickets, new tech introduced)
- [ ] **Appraisal document generation** — produces:
  - `Achievements` section: 5–10 bullet-point highlights with quantified impact
  - `Competency narrative`: 3–5 sentence paragraphs per competency dimension
  - `Self-score suggestions`: for numbered or levelled rating scales, suggests a score
    based on evidence (with reasoning the developer can adjust)
  - `Evidence appendix`: list of Jira ticket links, PR URLs, and Confluence page links
    as supporting evidence
- [ ] **Time-boxed queries** — all queries accept a review period (e.g., `2026-Q1`,
  `last 6 months`, or explicit `from`/`to` dates)
- [ ] **3-tier content**: newbie (guided walkthrough with prompts), amateur (templates
  for competency sections), pro (automated pipeline via MCP tools → draft in one pass)
- [ ] **Slash command** `/appraisal` added to `.github/prompts/career/appraisal.prompt.md`
  and registered in `slash-commands.md`

## Technical Notes

- This skill is a sibling to BLI-099 (resume-builder) — they share the same data sources
  but differ in output format and framing (external CV vs. internal performance evidence)
- Consider a shared `CareerDataCollector` prompt that both skills can call to avoid
  duplicating the Jira/Bitbucket/Confluence query logic
- Output formats: Markdown (for internal wikis / doc tools) and plain text (for HR systems)
- The competency mapping table should be configurable — different organisations use
  different frameworks; the skill should document how to adapt the mapping

## Related

- BLI-099 — Resume builder skill (sibling — shares data sources and query patterns)
- `.github/skills/jira/SKILL.md`
- `.github/skills/bitbucket/SKILL.md`
- `.github/skills/confluence/SKILL.md`
