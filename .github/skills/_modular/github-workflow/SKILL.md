---
name: github-workflow
description: >
  GitHub platform workflows — pull requests, issues, GitHub CLI (gh), and
  repository management. Use when asked about: creating or updating pull
  requests, writing PR titles and descriptions from commits, reviewing PRs,
  managing GitHub issues, using the gh CLI, GitHub Actions basics, branch
  protection, labels, milestones, or any GitHub-specific workflow. Also
  activates when the user provides a GitHub PR or issue link, asks to
  summarize a PR, or asks to update PR metadata.
  Complements: git-vcs (local Git operations).
---

# GitHub Workflow

> **Scope:** GitHub platform operations — PRs, issues, CLI, and repo management.
> For local Git commands (commit, branch, merge, rebase), see `git-vcs`.

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
