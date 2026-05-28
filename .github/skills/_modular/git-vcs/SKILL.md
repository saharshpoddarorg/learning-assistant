---
name: git-vcs
description: >
  Git version control — branching strategies, commit conventions, semantic
  versioning, and local Git operations. Use when asked about git commands,
  branching, merging, rebasing, conflicts, workflows, GitFlow, GitHub Flow,
  trunk-based development, conventional commits, semver, or .gitignore.
---

# Git & VCS

## Core Git Commands

### Branching

```sh
git branch <name>                 # create a branch
git switch -c <name>              # create and switch (git 2.23+)
git branch -d <name>              # delete (safe)
git branch -D <name>              # force-delete
```

### Merging & Rebasing

```sh
git merge <branch>                # merge into current
git merge --no-ff <branch>        # merge with merge commit (GitFlow style)
git rebase <branch>               # rebase onto branch
git rebase -i HEAD~3              # interactive rebase last 3 commits
git cherry-pick <sha>             # apply a specific commit
```

### Undoing

```sh
git restore <file>                # discard working-tree changes
git reset --soft HEAD~1           # undo commit, keep staged
git reset --hard HEAD~1           # undo commit, discard changes
git revert <sha>                  # safe undo commit (shared branches)
```

### History & Inspection

```sh
git log --oneline --graph --all   # visual branch history
git diff --staged                 # staged changes
git blame <file>                  # who changed each line
git bisect start                  # binary search for a bug
git reflog                        # history of HEAD movements
```

### Stash

```sh
git stash                         # stash uncommitted changes
git stash pop                     # apply and drop last stash
git stash apply stash@{2}         # apply specific stash
```

---

## Branching Strategies

### GitHub Flow — simple, continuous delivery

- Single `main` + short-lived feature branches → PR → merge
- Best for: web apps, SaaS, continuous deployment teams

### GitFlow — versioned releases

- `main` (production) + `develop` (integration) + `feature/*` + `release/*` + `hotfix/*`
- Best for: versioned products, libraries with release cycles

### Trunk-Based Development — high-frequency CI/CD

- Small frequent commits directly to trunk; feature flags hide incomplete work
- Best for: high-performing engineering teams with continuous integration

---

## Conventional Commits

Format: `<type>(<scope>): <description>`

| Type | When |
|---|---|
| `feat` | New feature |
| `fix` | Bug fix |
| `docs` | Documentation only |
| `refactor` | Restructuring — no feat/fix |
| `chore` | Build, tooling, deps |
| `test` | Adding or updating tests |
| `perf` | Performance improvement |

Breaking change: `feat!:` suffix or `BREAKING CHANGE:` in footer.

> Full commit message rules (body, footer, scope, attribution) are defined in
> `copilot-instructions.md`.

---

## Semantic Versioning (SemVer)

Format: `MAJOR.MINOR.PATCH`

| Bump | When |
|---|---|
| `MAJOR` | Breaking / incompatible change |
| `MINOR` | New feature, backward-compatible |
| `PATCH` | Bug fix, backward-compatible |

Conventional Commits → SemVer mapping:
`feat` → MINOR · `fix` → PATCH · `feat!` / `BREAKING CHANGE` → MAJOR

---

## .gitignore — Java & IDE Patterns

```gitignore
# Java
*.class
target/
out/
*.jar

# IDE
.idea/
*.iml
.vscode/

# OS
.DS_Store
Thumbs.db

# Secrets
*.env
.env.local
```
