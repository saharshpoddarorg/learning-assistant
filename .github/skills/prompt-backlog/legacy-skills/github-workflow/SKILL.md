---
name: github-workflow
description: >
  ARCHIVED — migrated to _modular/github-workflow/SKILL.md.
  Residual content: PR review workflow, tier-based reference (newbie/amateur/pro
  snippets), PR link handling (deferred), issue templates, branch protection rules,
  GitHub Actions workflow structure.
  See prompt-backlog/github-workflow-learning.md for migration notes.
---

> **ARCHIVED LEGACY SKILL** — core content migrated to `_modular/github-workflow/SKILL.md`.
> This file contains only the residual content not yet moved to a prompt.
> See `prompt-backlog/github-workflow-learning.md` for migration notes.

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

> **DEFERRED** — not yet migrated to a prompt.

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

## Branch Protection Rules

Common rules to set up in GitHub repository settings:

- Require PR reviews before merging
- Require status checks to pass
- Require branches to be up to date before merging
- Restrict who can push to `main`/`master`
- Enable CODEOWNERS for automatic reviewer assignment

---

## Issue Templates

Create `.github/ISSUE_TEMPLATE/` with:

- `bug_report.md` — steps to reproduce, expected vs. actual, environment
- `feature_request.md` — problem statement, proposed solution, alternatives considered

---

## GitHub Actions — Workflow Structure

### File Location

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
  workflow_dispatch:           # manual trigger from GitHub UI
  schedule:
    - cron: '0 0 * * 1'       # weekly on Monday at midnight UTC
```

### Checking Workflow Status via CLI

```sh
gh run list --limit 5                 # recent runs
gh run view <run-id> --log            # view logs
gh run rerun <run-id>                 # re-run a failed run
```
