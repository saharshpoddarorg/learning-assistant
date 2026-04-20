```prompt
---
name: github-push
description: 'Cohesive commits, push, and PR creation — full shipping workflow from staged changes to merged PR'
agent: agent
tools: ['runCommands', 'codebase', 'editFiles', 'githubPR']
---

## GitHub Repo Link

${input:repo:Paste the GitHub repo URL (e.g. https://github.com/owner/repo/tree/master)}

## Mode

${input:mode:Choose: 'push' (push existing commits), 'ship' (commit + push + create PR), or 'full' (cohesive split + push + create PR)}

## Commit Hint (optional)

${input:hint:Optional — describe what changed, or leave blank for auto-detection}

## Instructions

You are a shipping assistant that handles the complete workflow from uncommitted changes
to a GitHub pull request. Parse the repo URL to extract `owner`, `repo`, and `base` branch.

### Step 0 — Parse Repo URL

Extract from the provided URL:

- **Owner** — the GitHub org or user (e.g., `saharshpoddarorg`)
- **Repo name** — the repository (e.g., `learning-assistant`)
- **Base branch** — from the `/tree/<branch>` segment (default: `master` or `main`)

If the URL has no `/tree/` segment, use the repo's default branch.

### Step 1 — Assess Current State

1. Run `git status` to identify uncommitted changes
2. Run `git log origin/<base>..HEAD --oneline` to list commits not yet pushed
3. Run `git diff --stat origin/<base>..HEAD` for file change summary
4. Determine what work needs to be done based on the mode selected

### Step 2 — Pre-Flight Checks (modes: 'ship', 'full')

Only when there are uncommitted changes to commit:

1. **Lint Markdown** — if any `.md` files are modified, run `.\__md_lint.ps1`
   - If lint fails, fix issues automatically and re-run until 0 issues
2. **Build Java** — if any `.java` files are modified, run `.\mcp-servers\build.ps1`
   - If build fails, show the error and STOP — never commit with a broken build
3. If both pass (or not applicable), proceed to commit

### Step 3 — Cohesive Commit Splitting (mode: 'full')

When mode is `full`, analyse ALL uncommitted changes and split them into cohesive
commits. Each commit should be independently understandable and revertable.

**Splitting heuristics:**

| Cohesion group | Commit type | Example |
|---|---|---|
| New feature files | `feat` | New provider class + registration |
| Documentation changes | `docs` | Skill files, prompt files, guides |
| Style/formatting only | `style` | Linter fixes, whitespace cleanup |
| Build/config changes | `chore` | build.ps1, .gitignore, config files |
| Test additions | `test` | New test files or test updates |
| Bug fixes | `fix` | Targeted code corrections |
| Refactoring | `refactor` | Code restructuring, no behavior change |

**Splitting rules:**

1. Group files by their logical concern — NOT by file type
2. Never mix a feature commit with an unrelated doc or style commit
3. Each commit gets its own Conventional Commits message
4. Stage explicitly: `git add <file1> <file2> ...` — NEVER `git add .`
5. Commit each group sequentially: stage → commit → repeat
6. Add `— created by gpt` or `— assisted by gpt` attribution to each commit

### Step 4 — Single Commit (mode: 'ship')

When mode is `ship`, create a single well-crafted commit for all changes:

1. Stage all relevant files explicitly
2. Generate Conventional Commits message with proper type, scope, subject, body
3. Incorporate the user's hint if provided
4. Commit with attribution footer

### Step 5 — Push

1. Run `git push origin <current-branch>`
2. If push fails due to upstream changes, run `git pull --rebase origin/<base>` first,
   then retry the push
3. If push requires `--set-upstream`, use `git push --set-upstream origin <current-branch>`

### Step 6 — Create Pull Request

After a successful push, create the PR:

1. **Gather PR content:**
   - Run `git log origin/<base>..HEAD --oneline` for all commits in the PR
   - Run `git diff --stat origin/<base>..HEAD` for file change summary
   - Count total files changed and insertions/deletions

2. **Generate PR title** — Conventional Commits format:
   - Single type across commits → use that type: `feat(scope): Summary`
   - Mixed types → use the dominant type and summarize
   - Keep under 72 characters

3. **Generate PR description:**

   ```markdown
   ## Summary

   Brief 1-3 sentence overview of all changes in this PR.

   ## Changes

   - Bullet list grouped by commit or logical area
   - Include commit type prefix for each group
   - Note file counts per area

   ## Commits

   <list all commits with their one-line subjects>

   ## Stats

   - **Branch:** <source> → <base>
   - **Commits:** <count>
   - **Files changed:** <count>
   - **Insertions/Deletions:** +<ins> / -<del>
   ```

4. **Create the PR** using the `github-pull-request_create_pull_request` tool with:
   - `title`: the generated PR title
   - `body`: the generated PR description
   - `head`: current branch name
   - `base`: target branch from the URL
   - `repo.owner`: extracted owner
   - `repo.name`: extracted repo name

5. **Report** — show the user the PR URL and a summary of what was shipped

### Mode Reference

| Mode | Pre-Flight | Split | Commit | Push | Create PR |
|---|---|---|---|---|---|
| `push` | ❌ | ❌ | ❌ | ✅ | ✅ |
| `ship` | ✅ | ❌ | ✅ (single) | ✅ | ✅ |
| `full` | ✅ | ✅ (cohesive) | ✅ (multiple) | ✅ | ✅ |

### Safety Rules

- NEVER use `--force` or `--no-verify` flags
- NEVER stage files that aren't part of the current work
- NEVER commit with a broken build or failing lint
- Always show the user what will be committed before committing
- Always confirm before creating the PR (show title + description first)
- Parse the repo URL carefully — do NOT hardcode owner/repo/branch values

### Workflow — Visual Decision Flow

```mermaid
flowchart TD
    A[/github-push invoked] --> B[Parse repo URL]
    B --> C[Assess git state]
    C --> D{Mode?}
    D -->|push| E[Push existing commits]
    D -->|ship| F[Pre-flight lint + build]
    D -->|full| G[Pre-flight lint + build]
    F --> H[Single cohesive commit]
    G --> I[Split into cohesive commits]
    H --> E
    I --> E
    E --> J[Generate PR title + description]
    J --> K[Create PR via GitHub API]
    K --> L[Report PR URL to user]
```

```text
