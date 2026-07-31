---
id: BLI-099
title: Create resume-builder skill using Jira/Bitbucket/Confluence history
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
parent: null
sub-items: [BLI-100]
origin: null
estimated-effort: L
actual-effort: null
tags: [skill, resume, career, jira, bitbucket, confluence, atlassian, self-promotion]
---

# BLI-099 — Create resume-builder skill using Jira/Bitbucket/Confluence history

## Summary

Build a `.github/skills/career/resume-builder/SKILL.md` that guides Copilot to generate
or update a professional resume by mining real work artefacts from Jira, Bitbucket, and
Confluence — turning ticket titles, PR descriptions, and design docs into bullet-point
achievements suitable for a CV.

## Motivation

A developer's actual work is scattered across Jira tickets, PRs, and Confluence pages.
Manually translating that into resume bullets is tedious and often undersells impact.
This skill automates the extraction and reformatting step, producing STAR-format bullets
(Situation, Task, Action, Result) grounded in real delivery evidence.

## Acceptance Criteria

- [ ] Skill file at `.github/skills/career/resume-builder/SKILL.md` with YAML frontmatter
  and a description that activates on keywords: resume, CV, job application, career profile
- [ ] **Jira integration** — skill uses `fetch_jira_issue` / `search_jira_jql` to pull
  tickets the developer completed (e.g., `assignee = currentUser() AND status = Done`)
  and extracts: summary, story points, labels, epic, sprint dates
- [ ] **Bitbucket integration** — skill uses `fetch_bitbucket_pr_diff` /
  `get_bitbucket_file_diff` to surface merged PRs authored by the developer; extracts PR
  title, linked Jira key, files changed, and any reviewer comments
- [ ] **Confluence integration** — skill uses `fetch_confluence_page` /
  `search_confluence_cql` to find pages authored or significantly edited by the developer;
  surfaces design docs, ADRs, or runbooks as "documentation" bullets
- [ ] **Resume section generation** — skill produces ready-to-paste resume bullets:
  - `Experience` section bullets in STAR format
  - `Key Projects` section with project names, technologies, and quantified outcomes
  - `Technical Skills` section inferred from ticket labels, PR file types, and page tags
- [ ] **Time-boxed queries** — all queries accept a date range (e.g., last 12 months,
  or from/to sprint) so the skill can target a role-specific period
- [ ] **3-tier content** in the skill: newbie (guided prompts), amateur (templates),
  pro (automated pipeline via MCP tools)
- [ ] **Slash command** `/resume` added to `.github/prompts/career/resume.prompt.md`
  and registered in `slash-commands.md`

## Technical Notes

- Reuse the existing `jira`, `confluence`, and `bitbucket` skills for tool invocation
- The skill itself is an orchestration layer — it defines the workflow, not the raw API calls
- Output format: Markdown (paste into a doc tool) and optionally JSON (for structured export)
- Consider a `ResumeBuilder` prompt that chains: JQL query → PR query → CQL query →
  bullet formatting → review/edit loop

## Related

- BLI-100 — Achievements & self-appraisal skill (sibling — shares data sources)
- `.github/skills/jira/SKILL.md`
- `.github/skills/bitbucket/SKILL.md`
- `.github/skills/confluence/SKILL.md`
