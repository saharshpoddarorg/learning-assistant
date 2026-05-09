---
name: github-workflow
description: 'Learn GitHub platform workflows — PRs, issues, GitHub CLI (gh), branch protection, Actions, and repository management'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Topic

${input:topic:What GitHub topic? (e.g., pr-create, pr-review, pr-title, issues, gh-cli, actions, branch-protection, labels, milestones, repo-management, releases)}

## Goal

${input:goal:What's your goal? (learn-concept / do-it-now / automate / review-workflow / fix-problem)}

## Current Level

${input:level:Your experience level? (newbie / amateur / pro)}

## Instructions

Teach or guide the user on the selected GitHub platform topic. Adapt your response based on goal and level.

### GitHub Platform Domain Map

```text
GitHub Basics (newbie)
├── Repository: create, clone, fork, browse, settings
├── Issues: create, label, assign, close, templates
├── Pull Requests: create, review, merge, squash, rebase
└── gh CLI: auth, pr create, pr view, issue list

PR Workflow (amateur)
├── Creating PRs: title (Conventional Commits), description, draft
├── PR from commits: git log → title + body → gh pr create/edit
├── Review cycle: request review, approve, request changes
├── Merging strategies: merge commit, squash, rebase
└── Linking: Closes #N, Fixes #N, cross-repo references

Issue Management (amateur)
├── Lifecycle: Open → In Progress → Review → Closed
├── Templates: bug_report.md, feature_request.md
├── Labels & milestones: triage, priority, sprint tracking
├── Projects: GitHub Projects (board view, table view)
└── Automation: auto-close via PR, auto-label via Actions

Branch & Repo Management (amateur → pro)
├── Branch naming: type/short-description
├── Branch protection: required reviews, status checks
├── CODEOWNERS: automatic review assignment
├── Repository settings: merge strategies, secrets, environments
└── Collaboration: teams, permissions, forks

GitHub Actions (pro)
├── Workflow files: .github/workflows/*.yml
├── Triggers: push, pull_request, schedule, workflow_dispatch
├── Common actions: checkout, setup-java, cache
├── CI pipeline: build → test → lint → deploy
└── Monitoring: gh run list, gh run view, gh run rerun

Releases & Tags (pro)
├── Semantic versioning: tag → release → changelog
├── gh release create: notes, assets, draft
├── Automating releases: Actions + conventional commits
└── GitHub Packages: publish artifacts
```

### 3-Tier Response Structure

**Newbie** — focus on the simplest `gh` commands that get the job done:

- Show exact copy-pasteable commands
- Explain what each flag does
- Link to official docs for deeper reading
- One task at a time — don't overwhelm

**Amateur** — structured workflows with Conventional Commits integration:

- PR title/description best practices with examples
- Review workflow (as author and reviewer)
- Issue-to-PR linking and lifecycle
- Branch naming and protection rules

**Pro** — automation, scripting, and advanced GitHub platform features:

- Generating PR descriptions from commit history
- GitHub Actions CI/CD pipelines
- JSON queries with `--json` and `--jq`
- Repository management automation
- CODEOWNERS, branch rulesets, environments

### Quick Reference Table

| Task | Command |
|---|---|
| Create PR | `gh pr create --fill` |
| Create PR with title | `gh pr create --title "feat: ..." --body "..."` |
| View PR | `gh pr view <number>` |
| Edit PR | `gh pr edit <number> --title "..." --body "..."` |
| Merge PR | `gh pr merge <number> --squash --delete-branch` |
| Review PR | `gh pr review <number> --approve` |
| Create issue | `gh issue create --title "..." --body "..."` |
| List issues | `gh issue list --label "bug"` |
| Check CI | `gh pr checks <number>` |
| Open in browser | `gh browse` |

### Curated Resources

- [GitHub CLI Manual](https://cli.github.com/manual/)
- [GitHub Docs — Pull Requests](https://docs.github.com/en/pull-requests)
- [GitHub Docs — Issues](https://docs.github.com/en/issues)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- Related skill: `git-vcs` (local Git commands and branching workflows)
