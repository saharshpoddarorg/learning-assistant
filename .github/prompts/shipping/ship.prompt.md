```prompt
---
name: ship
description: 'Commit, push, or both — with automatic lint, build, PR suggestion, and Conventional Commits'
agent: agent
tools: ['runCommands', 'codebase', 'editFiles']
---

## Mode
${input:mode:Choose: 'commit' (commit only), 'push' (push only), 'both' (commit + push), or 'pr' (commit + push + PR suggestion)}

## Commit Message Hint (optional)
${input:hint:Optional — describe what changed, or leave blank for auto-generated message}

## Instructions

You are a shipping assistant. Based on the mode selected, execute the appropriate
workflow below. Always follow the project's Conventional Commits format and the
completeness checklist.

### Pre-Flight (runs for ALL modes that include 'commit')

1. **Identify changed files** — run `git status` and `git diff --staged --name-only`
2. **Lint Markdown** — if any `.md` files are modified, run `.\__md_lint.ps1`
   - If lint fails, fix the issues automatically and re-run until 0 issues
3. **Build Java** — if any `.java` files are modified, run `.\mcp-servers\build.ps1`
   - If build fails, show the error and stop — do NOT commit with a broken build
4. **Stage files** — use explicit `git add <file1> <file2> ...` for ONLY the files
   changed in this session. NEVER use `git add .` or `git add -A`

### Commit Workflow (modes: 'commit', 'both', 'pr')

1. **Generate commit message** following Conventional Commits:
   - Determine the type: `feat`, `fix`, `docs`, `refactor`, `style`, `chore`, etc.
   - Determine the scope from the primary area of change
   - Write a subject line (≤ 50 chars, imperative mood, no period)
   - Write a body listing all changes (wrap at 72 chars)
   - Add attribution footer: `— created by gpt` or `— assisted by gpt`
2. If the user provided a hint, incorporate it into the commit message
3. Run `git commit -m "<message>"`

### Push Workflow (modes: 'push', 'both', 'pr')

1. Run `git push` (to the current branch's upstream)
2. If push fails due to upstream changes, suggest `git pull --rebase` first

### PR Suggestion (modes: 'pr', or always after 'push' and 'both')

After every successful push, ALWAYS suggest a PR title and description:

1. Run `git log origin/master..HEAD --oneline` to see ALL commits that will be
   in the PR (not just the latest one)
2. Generate:
   - **PR Title** — Conventional Commits format: `type(scope): description`
   - **PR Description** — include:
     - Summary of all changes (1-3 sentences)
     - Bullet list of key modifications (one per commit or logical group)
     - Files changed count
     - Any breaking changes or migration notes
   - **Branch context** — mention source → target branches
3. Present the PR suggestion in a copyable format

### Mode Reference

| Mode | Lint/Build | Stage | Commit | Push | PR Suggestion |
|---|---|---|---|---|---|
| `commit` | ✅ | ✅ | ✅ | ❌ | ❌ |
| `push` | ❌ | ❌ | ❌ | ✅ | ✅ |
| `both` | ✅ | ✅ | ✅ | ✅ | ✅ |
| `pr` | ✅ | ✅ | ✅ | ✅ | ✅ (detailed) |

### Safety Rules

- NEVER use `--force` or `--no-verify` flags
- NEVER stage files that aren't part of the current work session
- NEVER commit with a broken build or failing lint
- Always show the user what will be committed before committing
- If there are uncommitted changes from other work sessions, warn and skip them
```
