````prompt
---
name: git-vcs
description: 'Learn Git & version control — branching strategies, commit conventions, rebasing, workflows, internals, and semantic versioning'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Topic
${input:topic:What Git/VCS topic? (e.g., branching, rebase, merge, cherry-pick, hooks, gitflow, github-flow, trunk-based, conventional-commits, semver, internals, conflicts, stash)}

## Goal
${input:goal:What's your goal? (learn-concept / practice-hands-on / understand-internals / choose-workflow / fix-problem)}

## Current Level
${input:level:Your experience level? (newbie / amateur / pro)}

## Instructions

Teach or guide the user on the selected Git/VCS topic. Adapt your response based on goal and level.

### Git & VCS Domain Map
```
Git Fundamentals (newbie)
├── Core commands: init, clone, add, commit, push, pull, fetch
├── Inspecting: status, log, diff, show, blame
├── Undoing: restore, reset (soft/hard), revert, amend
└── Getting help: git help <command>

Branching & Merging (amateur)
├── Branches: branch, checkout, switch, merge, delete
├── Remote branches: fetch, pull, push, tracking branches
├── Conflicts: how they happen, resolving in IDE vs terminal
├── Rebasing: rebase, interactive rebase (rebase -i), rebase vs merge
└── Cherry-pick and stash

Branching Workflows (amateur → pro)
├── GitHub Flow: main + feature branches + PRs (simple, CD-friendly)
├── GitFlow: main/develop/feature/release/hotfix (versioned releases)
├── Trunk-Based Development: direct commits to trunk, feature flags
└── Forking Workflow: open-source contribution model

Commit Conventions (amateur)
├── Conventional Commits: feat/fix/docs/chore/refactor/test/ci types
├── BREAKING CHANGE footer and ! suffix
├── Automated CHANGELOGs from commit history
└── Semantic Versioning (SemVer): MAJOR.MINOR.PATCH alignment

Advanced Git (pro)
├── Internals: object model, blobs/trees/commits/tags, SHA-1 DAG
├── Plumbing commands: cat-file, hash-object, ls-tree, rev-parse
├── Hooks: pre-commit, commit-msg, pre-push, post-merge
├── Reflog: recovering lost commits, HEAD history
├── Bisect: binary search for bug-introducing commit
├── Worktrees: multiple checkouts of the same repo
└── Submodules and subtrees
```

### Response Structure by Level

#### Newbie — hands-on, minimal jargon
1. **What it is** — Simple analogy
2. **One command at a time** — Show the exact command with explanation
3. **What to expect** — What output looks like, what changed
4. **Common mistake** — One thing beginners mess up
5. **Next step** — Exact next command to run

#### Amateur — workflow-focused
1. **Concept overview** — When/why you use this
2. **Step-by-step example** — Real scenario (e.g., creating a PR with conventional commits)
3. **Diagram** — ASCII branch diagram when helpful
4. **Pitfalls** — 2–3 things to watch out for
5. **Comparison** — Where this fits vs alternatives

#### Pro — internals and edge cases
1. **How Git actually does this** — Object model, data structures
2. **The command(s)** — With all relevant flags
3. **Edge cases** — What can go wrong, how to recover
4. **Advanced usage** — Hooks, aliases, automation
5. **Mental model** — How to reason about this class of problem

### Branching Strategy Decision Guide

Recommend based on team context:
- **Continuous delivery web app** → GitHub Flow
- **Versioned software / library** → GitFlow
- **High-frequency CI/CD team (10+ deployments/day)** → Trunk-Based Development
- **Open source contribution** → Forking Workflow

### Conventional Commits Quick Reference

| Type       | Use for                            | SemVer impact |
|------------|------------------------------------|---------------|
| `feat`     | New feature                        | MINOR bump    |
| `fix`      | Bug fix                            | PATCH bump    |
| `feat!`    | Breaking feature                   | MAJOR bump    |
| `docs`     | Documentation only                 | No bump       |
| `refactor` | Code restructure (no behavior change) | No bump    |
| `test`     | Adding/fixing tests                | No bump       |
| `chore`    | Tooling, config, deps              | No bump       |
| `ci`       | CI/CD configuration                | No bump       |

### Curated Resources
- **Newbie**: [Learn Git Branching](https://learngitbranching.js.org/) (visual/interactive)
- **Amateur**: [Atlassian Git Tutorials](https://www.atlassian.com/git/tutorials), [GitHub Flow](https://docs.github.com/en/get-started/using-github/github-flow)
- **Workflows**: [GitFlow](https://nvie.com/posts/a-successful-git-branching-model/), [Trunk-Based Development](https://trunkbaseddevelopment.com/)
- **Conventions**: [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/), [SemVer](https://semver.org/)
- **Pro**: [Git Internals (Pro Git Ch.10)](https://git-scm.com/book/en/v2/Git-Internals-Plumbing-and-Porcelain), [Git Reference](https://git-scm.com/docs)
````
