# Prompt Backlog: github-workflow learning guide

## Source

- **Legacy skill:** `.github/skills/dev-process/github-workflow/SKILL.md`
- **Sections not migrated to modular skill:**
  - `## Tier-Based Reference` (lines 330–375) — newbie/amateur/pro shell snippets for PR workflow
  - `## PR Review Workflow` (lines 222–268) — step-by-step reviewer and author procedures
  - `## Branch Protection & Collaboration` (lines 308–327) — branch protection rules, CODEOWNERS
  - `## Issue Workflow` → Issue Templates section (lines 298–305) — `.github/ISSUE_TEMPLATE/` setup

## What It Does

**Tier-Based Reference (removed as learning guide, not reference knowledge):**

- Newbie: `gh pr create --fill`, `gh pr view --web`, `gh pr merge --squash --delete-branch`
- Amateur: `gh pr create` with full title/body/label, `gh pr edit`, squash merge with `--subject`
- Pro: bash one-liners to auto-generate PR title and body from `git log`, batch label management,
  `--json` queries for scripting CI

**PR Review Workflow (removed as step-by-step procedure):**

As a reviewer:
1. `gh pr checkout <number>` → `gh pr diff <number>` → `gh pr checks <number>`
2. `gh pr review <number> --approve / --comment / --request-changes`

As an author:
1. Check review status via `--json reviews --jq`
2. Push fixes, comment "Addressed all review comments"
3. `gh pr edit <number> --add-reviewer <username>` to re-request review

**Branch Protection (removed as generic GitHub knowledge):**
- Require PR reviews before merging
- Require status checks to pass
- Require branches to be up to date before merging
- Restrict push to `main`/`master`

**Issue Templates:**
- Create `.github/ISSUE_TEMPLATE/bug_report.md` and `feature_request.md`

## Why It's a Prompt (Not a Skill)

The tier-based reference and PR review workflow are *teaching procedures* and *step-by-step guides* —
not reference knowledge Copilot injects. A reviewer workflow saying "step 1: checkout, step 2: diff,
step 3: approve" is a procedure, not a knowledge injection.

Branch protection and issue templates are generic GitHub platform knowledge with no project-specific
nuance — Copilot already knows these without being told.

## Existing Prompt Coverage

- **Name:** `/github-workflow` (exists at `.github/prompts/tools/github-workflow.prompt.md`)
- **Status:** Already fully covered — the existing prompt has:
  - Complete GitHub Platform domain map (Basics → PR Workflow → Issues → Branch/Repo → Actions → Releases)
  - 3-tier response structure (newbie/amateur/pro) matching the removed tier-based reference
  - PR review cycle in the domain map (amateur level)
  - Branch protection and CODEOWNERS in the domain map (amateur→pro level)
  - Issue lifecycle, templates, and labels in the domain map
  - Full `gh` CLI quick reference table
  - Curated resources (GitHub CLI Manual, PR docs, Issues docs, Actions docs)
- **Action needed:** No new prompt required. The `/github-workflow` prompt already handles all trimmed content.

## Potential Enhancement

The PR generation one-liners from the Pro tier (bash automation) are now covered by the
`github-workflow` modular skill's "Cohesive Multiple Commits" and "PR Creation from Changes"
sections. No separate prompt needed for these.
