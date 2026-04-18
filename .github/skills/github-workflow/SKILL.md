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
---

# GitHub Workflow Skill

> **Scope:** GitHub platform operations — PRs, issues, CLI, and repo management.
> **Complements:** `git-vcs` (local Git operations), `copilot-customization` (Copilot config).
> **Boundary:** This skill covers GitHub-specific workflows. For local Git commands
> (commit, branch, merge, rebase), see `git-vcs`.

---

## Quick Reference — `gh` CLI Cheatsheet

### Authentication

```sh
gh auth login                         # interactive login (browser or token)
gh auth status                        # check current auth state
gh auth token                         # print current token
gh auth logout                        # log out
```

### Pull Requests

```sh
# Create
gh pr create                          # interactive PR creation
gh pr create --title "feat: Add X" --body "Details..." --base main
gh pr create --draft                  # create as draft PR
gh pr create --fill                   # auto-fill title/body from commits
gh pr create --fill-verbose           # auto-fill with full commit messages

# View & List
gh pr list                            # list open PRs
gh pr list --state all                # list all PRs (open, closed, merged)
gh pr view                            # view current branch's PR
gh pr view <number>                   # view specific PR
gh pr view <number> --web             # open in browser
gh pr view <number> --json title,body,commits  # JSON output

# Update
gh pr edit <number> --title "new title"
gh pr edit <number> --body "new description"
gh pr edit <number> --add-label "bug" --add-assignee "@me"
gh pr edit <number> --milestone "v1.0"

# Review
gh pr review <number> --approve
gh pr review <number> --comment --body "Looks good!"
gh pr review <number> --request-changes --body "Please fix X"
gh pr checks <number>                 # view CI status checks

# Merge & Close
gh pr merge <number>                  # interactive merge
gh pr merge <number> --squash --delete-branch
gh pr merge <number> --rebase
gh pr close <number>                  # close without merging
gh pr reopen <number>                 # reopen a closed PR

# Diff & Commits
gh pr diff <number>                   # view the PR diff
gh pr diff <number> --patch           # patch format
```

### Issues

```sh
# Create
gh issue create                       # interactive
gh issue create --title "Bug: X" --body "Steps to reproduce..."
gh issue create --label "bug" --assignee "@me"

# View & List
gh issue list                         # list open issues
gh issue list --label "bug"           # filter by label
gh issue view <number>                # view specific issue
gh issue view <number> --web          # open in browser

# Update
gh issue edit <number> --title "new title"
gh issue edit <number> --add-label "priority:high"
gh issue close <number>               # close an issue
gh issue reopen <number>              # reopen
gh issue comment <number> --body "Comment text"
```

### Repository

```sh
gh repo view                          # view current repo info
gh repo view <owner/repo>             # view specific repo
gh repo clone <owner/repo>            # clone a repo
gh repo fork <owner/repo>             # fork a repo
gh repo create <name> --public        # create a new repo
gh browse                             # open repo in browser
gh browse <file>                      # open specific file in browser
```

### Releases & Actions

```sh
gh release list                       # list releases
gh release create v1.0.0 --title "v1.0.0" --notes "Release notes"
gh run list                           # list recent workflow runs
gh run view <run-id>                  # view a specific run
gh run watch <run-id>                 # live-watch a running workflow
```

---

## PR Title & Description — Best Practices

### Writing Good PR Titles

Follow **Conventional Commits** format for PR titles:

```text
<type>(<scope>): <subject>
```

| Type | When to Use |
|---|---|
| `feat` | New feature or capability |
| `fix` | Bug fix |
| `refactor` | Code restructuring, no behavior change |
| `docs` | Documentation only |
| `chore` | Build, CI, tooling changes |
| `test` | Adding or updating tests |
| `perf` | Performance improvement |
| `style` | Formatting, whitespace changes |

**Examples:**

```text
feat(vault): Add VcsResources provider with 9 curated resources
fix(config): Null-check API key before HTTP request
docs(skills): Add hierarchical taxonomy for skills library
refactor(vault): Split GitAndBuildResources into separate providers
```

### Writing Good PR Descriptions

Structure the PR description with these sections:

```markdown
## Summary

Brief 1-3 sentence overview of what this PR does and why.

## Changes

- Bullet list of key modifications
- Grouped by area or commit
- Include file counts for large PRs

## Testing

How was this tested? What should reviewers verify?

## Breaking Changes

(If any) Describe what breaks and migration steps.
```

### Generating PR Title & Description from Commits

**Step-by-step workflow:**

1. **List all commits** in the PR:

   ```sh
   git log origin/master..HEAD --oneline
   ```

2. **Get detailed stats:**

   ```sh
   git diff --stat origin/master..HEAD
   ```

3. **Construct the title:**
   - If single type: use that type (e.g., `feat(vault): ...`)
   - If mixed types: use the dominant type and summarize
   - Keep under 72 characters

4. **Construct the body:**
   - Summarize ALL commits (not just the latest)
   - Group changes by area
   - Include total file count from `git diff --stat`

5. **Update the PR:**

   ```sh
   gh pr edit <number> --title "feat(scope): Summary" --body "## Summary\n\n..."
   ```

### Automating PR Description from Commits

Quick one-liner to generate a draft description:

```sh
# Get commit list formatted for PR body
git log origin/master..HEAD --format="- %s" | clip

# Get file change summary
git diff --stat origin/master..HEAD | tail -1
```

---

## PR Review Workflow

### As a Reviewer

1. **Fetch and checkout** the PR locally:

   ```sh
   gh pr checkout <number>
   ```

2. **Review the diff:**

   ```sh
   gh pr diff <number>
   ```

3. **Check CI status:**

   ```sh
   gh pr checks <number>
   ```

4. **Leave review:**

   ```sh
   gh pr review <number> --approve
   gh pr review <number> --comment --body "Suggestion: ..."
   gh pr review <number> --request-changes --body "Please fix: ..."
   ```

### As an Author

1. **Check review status:**

   ```sh
   gh pr view <number> --json reviews --jq '.reviews[] | {author: .author.login, state: .state}'
   ```

2. **Address feedback** — push new commits, then comment:

   ```sh
   gh pr comment <number> --body "Addressed all review comments in latest commit."
   ```

3. **Request re-review:**

   ```sh
   gh pr edit <number> --add-reviewer <username>
   ```

---

## Issue Workflow

### Issue Lifecycle

```text
Open → In Progress → Review → Closed
  │                              ↑
  └─── Won't Fix / Duplicate ───┘
```

### Linking Issues to PRs

Reference issues in PR body or commit messages to auto-close:

```text
Closes #42
Fixes #42
Resolves #42
```

Multiple issues:

```text
Closes #42, closes #43
```

### Issue Templates

Create `.github/ISSUE_TEMPLATE/` with:

- `bug_report.md` — bug report template
- `feature_request.md` — feature request template

---

## Branch Protection & Collaboration

### Branch Naming Convention

```text
<type>/<short-description>

feat/user-auth-flow
fix/null-pointer-config
docs/skills-taxonomy
chore/update-dependencies
```

### Common Branch Protection Rules

- Require PR reviews before merging
- Require status checks to pass
- Require branches to be up to date
- Restrict who can push to `main`/`master`

---

## Tier-Based Reference

### Newbie — Just Get It Done

```sh
# Create a PR from current branch
gh pr create --fill

# View your PR
gh pr view --web

# Merge when ready
gh pr merge --squash --delete-branch
```

### Amateur — Structured Workflow

```sh
# Create with proper metadata
gh pr create --title "feat(scope): Description" \
  --body "## Summary\n\nWhat and why.\n\n## Changes\n\n- Change 1\n- Change 2" \
  --label "enhancement" --assignee "@me"

# Update PR after review
gh pr edit <number> --title "Updated title" --body "Updated description"

# Squash merge with clean history
gh pr merge <number> --squash --subject "feat(scope): Final title" --delete-branch
```

### Pro — Full Automation

```sh
# Generate PR description from commits
TITLE=$(git log origin/master..HEAD --oneline | head -1 | sed 's/^[a-f0-9]* //')
BODY=$(git log origin/master..HEAD --format="- %s")
STATS=$(git diff --stat origin/master..HEAD | tail -1)

gh pr create --title "$TITLE" --body "## Changes\n\n$BODY\n\n## Stats\n\n$STATS"

# Batch label management
gh pr edit <number> --add-label "ready-for-review" --remove-label "wip"

# JSON queries for scripting
gh pr view <number> --json title,body,labels,reviewRequests,statusCheckRollup
```

---

## Handling a User-Provided PR Link

When a user provides a GitHub PR link (e.g., `https://github.com/owner/repo/pull/42`):

1. **Parse** the owner, repo, and PR number from the URL
2. **Fetch PR details** using available tools or `gh pr view`
3. **Analyze commits** to understand the scope of changes
4. **Generate appropriate title/description** following Conventional Commits
5. **Present** the suggested title and description for user approval
6. **Update** the PR if the user confirms

### URL Pattern

```text
https://github.com/<owner>/<repo>/pull/<number>
```

Extract: `owner`, `repo`, `number` — then use `gh pr view owner/repo#number`.

---

## GitHub Actions — Quick Reference

### Workflow File Location

```text
.github/workflows/<name>.yml
```

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

### Checking Workflow Status

```sh
gh run list --limit 5                 # recent runs
gh run view <run-id> --log            # view logs
gh run rerun <run-id>                 # re-run a failed run
```
