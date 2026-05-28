---
name: github-workflow
description: >
  GitHub platform workflows — pull requests, issues, GitHub CLI (gh),
  repository management, and commit crafting. Use when asked about: creating
  or updating pull requests, writing PR titles and descriptions from changes,
  creating commits from staged/unstaged changes, splitting changes into
  cohesive atomic commits, reviewing PRs, managing GitHub issues, using the
  gh CLI, GitHub Actions basics, or any GitHub-specific workflow. Also
  activates when the user provides a GitHub PR or issue link, asks to
  summarize a PR, or asks to update PR metadata.
  Complements: git-vcs (local Git operations).
---

# GitHub Workflow

> **Scope:** GitHub platform operations — PRs, issues, CLI, commit crafting,
> and repo management. For branching strategies, SemVer, and rebasing, see `git-vcs`.

---

## Commit Creation from Changes

When asked to create a commit, always follow this process:

### 1 — Inspect What Changed

```sh
git status                        # what files are modified/staged/untracked
git diff                          # unstaged changes
git diff --staged                 # what is already staged
git diff --stat                   # summary of file changes
```

### 2 — Determine the Right Commit Type and Scope

Read the diff and classify:

| What changed | Type |
|---|---|
| New feature, new file, new capability | `feat` |
| Bug fix or incorrect behavior corrected | `fix` |
| Only documentation / comments / skill files | `docs` |
| Restructuring with no behavior change | `refactor` |
| Build config, dependencies, tooling | `chore` |
| Tests added or updated | `test` |
| Performance improvement | `perf` |

**Scope** = the module, package, or area most affected (e.g., `vault`, `config`, `skills`, `auth`).

### 3 — Write the Commit Message

Format: `<type>(<scope>): <subject>`

Rules:

- Subject: imperative mood, ≤ 72 chars, no trailing period
- Body (optional): explain WHY, not WHAT — wrap at 72 chars
- Footer: `BREAKING CHANGE:` if applicable; `Closes #N` to link issues
- Attribution last line: `— created by gpt` or `— assisted by gpt`

```sh
git commit -m "feat(auth): Add JWT refresh token endpoint

Existing tokens expire after 1h with no renewal path, causing users
to be logged out mid-session. This adds POST /auth/refresh which
accepts a valid refresh token and returns a new access token.

Closes #42
— assisted by gpt"
```

---

## Cohesive Multiple Commits — Splitting Changes by Concern

When changes span multiple concerns, **never bundle them into one commit**.
Split by logical cohesion so each commit is independently understandable,
reviewable, and revertable.

### How to Identify Cohesive Groups

1. **Run `git diff --stat`** — list every changed file
2. **Group files by concern**:
   - Same feature/fix across files → one commit
   - Unrelated areas (e.g., new feature + formatting cleanup) → separate commits
   - Documentation that matches its code → can go in same commit OR separate `docs:` commit
3. **Stage and commit one group at a time** using `git add <files>` or `git add -p`

### Split Decision Guide

| Scenario | Wrong | Right |
|---|---|---|
| New feature + unrelated bug fix | One commit | `feat:` then `fix:` |
| Code refactor + test additions | One commit | `refactor:` then `test:` |
| Feature code + its own docs | One commit | OK either way — keep together if tightly coupled |
| Formatting sweep + logic change | One commit | `style:` then `feat:` |
| Multiple independent features | One commit | One `feat:` per feature |

### Workflow for Cohesive Commits

```sh
# Stage only the files for the first logical group
git add src/auth/TokenService.java src/auth/TokenRequest.java
git commit -m "feat(auth): Add JWT refresh token service"

# Stage the second logical group
git add src/config/SecurityConfig.java
git commit -m "chore(config): Register TokenService in security config"

# Stage docs separately if they span broader context
git add docs/api/auth.md
git commit -m "docs(auth): Document refresh token endpoint"
```

### Interactive Staging (patch-level splits)

When a single file has changes belonging to different concerns:

```sh
git add -p <file>     # stage individual hunks interactively
                      # y = stage, n = skip, s = split hunk, e = edit hunk
```

---

## PR Creation from Changes

When asked to create or update a PR, **always check the current PR state first**
before doing anything. The user may have manually raised, edited, merged, or
closed PRs — never assume the state; always query it.

### Step 0 — Check Existing PR State (always first)

```sh
# Check if an open PR already exists for the current branch
gh pr list --head <current-branch> --state open --json number,title,url,isDraft

# Check ALL PRs (open + closed + merged) for the branch
gh pr list --head <current-branch> --state all --json number,title,state,mergedAt,url
```

**Decision table based on the result:**

| Situation | Action |
|---|---|
| Open PR exists for this branch | **Update** the existing PR — do NOT create a new one |
| Open PR exists but user says "create new PR" | Clarify — ask if they want to update the existing one |
| Draft PR exists | Ask: promote to ready-for-review, or just update content? |
| PR was merged | All commits are already in the base — **create a new PR** for any new work |
| PR was closed (not merged) | Ask user: reopen it (`gh pr reopen`) or create a fresh one? |
| No PR exists for this branch | **Create** a new PR |
| Multiple open PRs for the branch | Show the list, ask user which one to update |

### Step 1 — Inspect All Commits Going into the PR

```sh
# Identify base branch
git remote show origin | grep "HEAD branch"

# List commits not yet in base
git log origin/<base>..HEAD --oneline

# File change summary
git diff --stat origin/<base>..HEAD
```

### Step 2 — Derive the PR Title

- **Single commit type** → use that type: `feat(scope): Summary`
- **Mixed types** → use the dominant type; summarize all in the subject
- **Many commits** → summarize the overall goal, not individual commits
- ≤ 72 characters; imperative mood; no trailing period

### Step 3 — Build the PR Description

Always derive from actual commits and `git diff --stat` — never fabricate.

```markdown
## Summary

<1-3 sentences: what this PR does and why it was needed>

## Changes

- <area 1: what changed — tie to commits>
- <area 2: what changed>
- <N files changed, N insertions(+), N deletions(-) — from git diff --stat>

## Testing

<how it was tested, or "manual testing" / "covered by existing tests">

## Breaking Changes

<omit this section entirely if none>
```

### Step 4a — Update Existing Open PR

```sh
# Show current title/body first so we don't blindly overwrite
gh pr view <number> --json title,body

# Update — only after showing user what will change
gh pr edit <number> \
  --title "feat(scope): Updated summary" \
  --body "## Summary\n\n...\n\n## Changes\n\n- ..."
```

> **Important:** If the user has manually edited the PR title or description,
> show them the current content and the proposed replacement side by side.
> Ask for confirmation before overwriting.

### Step 4b — Create New PR

```sh
gh pr create \
  --title "feat(scope): Summary of change" \
  --body "## Summary\n\n...\n\n## Changes\n\n- ...\n\n## Testing\n\n..." \
  --base main

# Or as a draft if work is still in progress
gh pr create --draft \
  --title "feat(scope): WIP — Summary" \
  --body "..."
```

### Step 4c — Reopen a Closed PR

```sh
# Reopen a previously closed (unmerged) PR
gh pr reopen <number>

# Then update its content
gh pr edit <number> --title "..." --body "..."
```

### Handling a User-Provided PR Link

When the user pastes `https://github.com/<owner>/<repo>/pull/<number>`:

1. **Parse** owner, repo, PR number from the URL
2. **Check state**: `gh pr view <number> --json state,title,body,mergedAt,closedAt`
3. **If open**: fetch full details, generate improved title/description, show
   current vs. proposed side by side, update only after user confirms
4. **If merged**: inform the user — "This PR is already merged. Do you want to
   create a new PR for new changes on this branch?"
5. **If closed (not merged)**: ask — "This PR was closed without merging. Do you
   want to reopen it or create a fresh PR?"

---

## `gh` CLI — Quick Reference

### Authentication

```sh
gh auth login                         # interactive login (browser or token)
gh auth status                        # check current auth state
gh auth token                         # print current token
```

### Pull Requests

```sh
# Create
gh pr create                          # interactive PR creation
gh pr create --title "feat: Add X" --body "Details..." --base main
gh pr create --draft                  # create as draft
gh pr create --fill                   # auto-fill from commits

# View & List
gh pr list                            # list open PRs
gh pr view <number>                   # view specific PR
gh pr view <number> --web             # open in browser
gh pr view <number> --json title,body,commits

# Update
gh pr edit <number> --title "new title"
gh pr edit <number> --body "new description"
gh pr edit <number> --add-label "bug" --add-assignee "@me"

# Review
gh pr review <number> --approve
gh pr review <number> --comment --body "Looks good!"
gh pr review <number> --request-changes --body "Please fix X"
gh pr checks <number>                 # view CI status

# Merge & Close
gh pr merge <number> --squash --delete-branch
gh pr merge <number> --rebase
gh pr close <number>
```

### Issues

```sh
gh issue create --title "Bug: X" --body "Steps to reproduce..."
gh issue list                         # list open issues
gh issue list --label "bug"
gh issue view <number>
gh issue edit <number> --add-label "priority:high"
gh issue close <number>
gh issue comment <number> --body "Comment text"
```

### Repository & Releases

```sh
gh repo view                          # view current repo
gh repo clone <owner/repo>
gh repo fork <owner/repo>
gh repo create <name> --public
gh browse                             # open repo in browser

gh release list
gh release create v<version> --title "v<version>" --notes "Release notes"
```

### GitHub Actions

```sh
gh run list --limit 5                 # recent workflow runs
gh run view <run-id> --log            # view logs
gh run rerun <run-id>                 # re-run a failed run
gh run watch <run-id>                 # live-watch a run
```

---

## PR Title & Description — Best Practices

### PR Titles

Follow **Conventional Commits** format:

```text
<type>(<scope>): <subject>
```

Examples:

```text
feat(vault): Add VcsResources provider with 9 curated resources
fix(config): Null-check API key before HTTP request
docs(skills): Add hierarchical taxonomy for skills library
```

### PR Description Structure

```markdown
## Summary

Brief 1-3 sentence overview of what this PR does and why.

## Changes

- Bullet list of key modifications
- Grouped by area or commit

## Testing

How was this tested? What should reviewers verify?

## Breaking Changes

(If any) What breaks and migration steps.
```

### Generating PR Title & Description from Commits

```sh
# List all commits in the PR
git log origin/master..HEAD --oneline

# Get file change stats
git diff --stat origin/master..HEAD

# Update PR with generated content
gh pr edit <number> --title "feat(scope): Summary" --body "## Summary..."
```

---

## Branch Naming Convention

```text
<type>/<short-description>

feat/user-auth-flow
fix/null-pointer-config
docs/skills-taxonomy
chore/update-dependencies
```

---

## Issue Linking

Reference issues in PR body or commit messages to auto-close on merge:

```text
Closes #42
Fixes #42
Resolves #42
```

---

## GitHub Actions — Workflow File

Location: `.github/workflows/<name>.yml`

### Common Triggers

```yaml
on:
  push:
    branches: [main, master]
  pull_request:
    branches: [main, master]
  workflow_dispatch:        # manual trigger
  schedule:
    - cron: '0 0 * * 1'    # weekly on Monday
```
