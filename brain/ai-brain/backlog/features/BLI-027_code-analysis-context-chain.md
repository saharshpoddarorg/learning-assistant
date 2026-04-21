---
id: BLI-027
title: Build code analysis context enhancement chain
status: todo
priority: high
type: feature
created: 2026-04-21
updated: 2026-04-21
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
tags: [code-analysis, vcs, git, bitbucket, jira, confluence, context-chain, atlassian, session-capture]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-027: Build code analysis context enhancement chain

## Description

Build an orchestrated context enhancement chain for code analysis sessions (especially
deep-dives) that automatically enriches the analysis with VCS history and linked Atlassian
artifacts. When analysing a class or method, the chain should pull local git history
(log, blame, diff), follow references to Bitbucket commits and PRs, extract linked Jira
ticket IDs from commit messages and PR descriptions, fetch Jira ticket details, and
retrieve linked Confluence pages — providing a complete traceability chain from code to
requirements. This transforms code analysis from isolated code reading into a fully
contextualised investigation that surfaces the **why** behind every change.

The chain leverages existing Atlassian MCP tools (`fetch_bitbucket_pr_diff`,
`get_bitbucket_file_diff`, `fetch_bitbucket_file`, Jira/Confluence tools) and local
git CLI commands (`git log`, `git blame`, `git diff`), orchestrating them into a coherent
multi-step pipeline. The output feeds directly into session capture templates (especially
`code-analysis-deep-dive-capture.md` § Recent Changes Impact).

## Sub-Items

| ID | Title | Priority | Status |
|---|---|---|---|

> No sub-items — M effort, single deliverable with detailed AC.

## Acceptance Criteria

- [ ] **AC-1: Git history extraction** — Given a target file or class, the chain runs
  `git log --oneline -20 <file>` and `git blame <file>` to surface recent commits,
  authors, and change frequency
- [ ] **AC-2: Bitbucket PR discovery** — Given commit SHAs from git log, the chain
  queries Bitbucket API to find associated PRs (via commit → PR mapping) and fetches
  PR titles, descriptions, reviewers, and approval status
- [ ] **AC-3: Jira ID extraction** — Given commit messages and PR descriptions, the chain
  extracts Jira ticket IDs using regex patterns (e.g., `[A-Z]+-\d+`) and deduplicates
  the list of referenced tickets
- [ ] **AC-4: Jira ticket enrichment** — Given extracted Jira IDs, the chain fetches
  ticket summaries, status, type, priority, acceptance criteria, and linked Confluence
  pages via Jira REST API / MCP tools
- [ ] **AC-5: Confluence page context** — Given Confluence page links from Jira tickets,
  the chain fetches page titles and key content sections to surface design docs,
  requirements, and architecture decisions related to the code
- [ ] **AC-6: Session template integration** — The `code-analysis-deep-dive-capture.md`
  template's "Recent Changes Impact" section is updated to include a structured sub-section
  for the full traceability chain: commits → PRs → Jira tickets → Confluence pages
- [ ] **AC-7: Skill/instruction wiring** — The `atlassian-tools` skill and/or a new
  instruction file documents the chain invocation pattern so that code analysis sessions
  automatically trigger context enrichment when Atlassian integration is available

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Skill | `.github/skills/atlassian-tools/SKILL.md` | 2026-04-21 | Existing Bitbucket/Jira/Confluence MCP tools |
| Template | `brain/ai-brain/sessions/_templates/code-analysis-deep-dive-capture.md` | 2026-04-21 | Deep-dive template with Recent Changes Impact section |
| Instruction | `.github/instructions/chat-capture.instructions.md` | 2026-04-21 | Deep-dive protocol referencing git/Bitbucket tools |

## Related

- **BLI-001** — Build production Atlassian MCP server (prerequisite for Bitbucket/Jira/Confluence API access)
- **BLI-026** — Evolve 6-primitive layered architecture (Phase 3: MCP integration — this is a concrete instance)
- **BLI-005** — Chat session orchestration (enriched sessions consume context from this chain)

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-04-21 | 05:43 PM | system | created | Item created via `/jot` — context enhancement chain for code analysis |

## Notes

### Context Chain Flow

```text
Target Code (file/class/method)
  │
  ├─→ git log --oneline -20 <file>     → recent commits (SHA, message, author, date)
  ├─→ git blame <file>                  → line-by-line attribution
  ├─→ git diff <commit>..HEAD <file>    → recent change diffs
  │
  ├─→ Bitbucket: commit SHA → PR lookup → PR details, reviewers, description
  │
  ├─→ Regex: extract JIRA-123 from commit messages + PR descriptions
  │     └─→ Jira: fetch ticket summary, status, AC, linked pages
  │           └─→ Confluence: fetch linked page titles + key sections
  │
  └─→ Output: structured context block for session capture template
```

### Dependencies

- **Hard dependency on BLI-001** — Atlassian MCP server must be production-ready for
  Bitbucket/Jira/Confluence API calls. Without it, only local git commands work.
- **Soft dependency on BLI-026** — the chain is a concrete example of MCP integration
  identified during Phase 3 of the architecture evolution.
- The local git portion (AC-1) can be built independently and works without any MCP server.

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | M |
| Actual effort | — |
| Created | 2026-04-21 |
| Started | — |
| Completed | — |
| Cycle time | — |
