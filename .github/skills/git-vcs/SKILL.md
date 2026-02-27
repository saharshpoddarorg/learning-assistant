````skill
---
name: git-vcs
description: >
  Git version control workflows, branching strategies, commit conventions,
  and semantic versioning. Use when asked about git commands, branching,
  merging, rebasing, conflicts, workflows, GitFlow, GitHub Flow, trunk-based
  development, conventional commits, semver, or .gitignore.
---

# Git & VCS Skill

## Core Git Commands Cheatsheet

### Setup & Init
```sh
git init                          # initialize a new repo
git clone <url>                   # clone a remote repo
git config --global user.name "Your Name"
git config --global user.email "you@example.com"
```

### Staging & Committing
```sh
git status                        # show working-tree status
git add <file>                    # stage a file
git add .                         # stage all changes
git commit -m "feat: message"     # commit with message
git commit --amend                # amend the last commit
```

### Branching
```sh
git branch                        # list local branches
git branch <name>                 # create a branch
git checkout -b <name>            # create and switch to a branch
git switch -c <name>              # modern syntax (git 2.23+)
git branch -d <name>              # delete a branch (safe)
git branch -D <name>              # force-delete a branch
```

### Merging & Rebasing
```sh
git merge <branch>                # merge branch into current
git merge --no-ff <branch>        # merge with merge commit (GitFlow style)
git rebase <branch>               # rebase current branch onto branch
git rebase -i HEAD~3              # interactive rebase last 3 commits
git cherry-pick <sha>             # apply a specific commit
```

### Remote
```sh
git remote -v                     # list remotes
git fetch origin                  # fetch without merging
git pull origin main              # fetch + merge
git push origin <branch>          # push a branch
git push -u origin <branch>       # push and set upstream
```

### Undoing
```sh
git restore <file>                # discard working-tree changes
git reset HEAD <file>             # unstage a file
git reset --soft HEAD~1           # undo commit, keep staged
git reset --hard HEAD~1           # undo commit, discard changes
git revert <sha>                  # create an undo commit (safe for shared branches)
```

### Stash
```sh
git stash                         # stash uncommitted changes
git stash list                    # list all stashes
git stash pop                     # apply and drop last stash
git stash apply stash@{2}         # apply specific stash
git stash drop stash@{0}          # delete a stash
```

### History & Inspection
```sh
git log --oneline --graph --all   # visual branch history
git log -p <file>                 # show changes to a file
git diff                          # unstaged changes
git diff --staged                 # staged changes
git blame <file>                  # who changed each line
git bisect start                  # start binary search for a bug
git reflog                        # history of HEAD movements
```

---

## Branching Strategies

### GitHub Flow (simple, continuous delivery)
```
main  ──────────────────────────────▶  (always deployable)
          └── feature/login ──PR──┘
```
- Single `main` branch + short-lived feature branches
- Best for: web apps, SaaS, continuous deployment teams

### GitFlow (versioned releases)
```
main        ────────────────────────▶  (production releases)
develop     ──────────────────────▶
  feature/x ──────┘
  feature/y ────────────┘
release/1.0 ──────────────────┐
hotfix/bug  ─────────────────────┘
```
- `main` (production), `develop` (integration), `feature/*`, `release/*`, `hotfix/*`
- Best for: versioned products, libraries, apps with release cycles

### Trunk-Based Development (high-frequency CI/CD)
```
trunk (main)  ──●──●──●──●──▶  (commits directly, or via < 1-day branches)
```
- Small, frequent commits to trunk; feature flags hide incomplete work
- Best for: high-performing engineering teams using continuous integration

---

## Conventional Commits

Format: `<type>(<optional scope>): <description>`

| Type       | When to use                              |
|------------|------------------------------------------|
| `feat`     | A new feature                            |
| `fix`      | A bug fix                                |
| `docs`     | Documentation only changes               |
| `style`    | Formatting, whitespace (no logic change) |
| `refactor` | Code restructured (not feat/fix)         |
| `test`     | Adding or fixing tests                   |
| `chore`    | Build, tooling, deps (not src changes)   |
| `perf`     | Performance improvement                  |
| `ci`       | CI/CD config changes                     |

Breaking change: add `!` after type or `BREAKING CHANGE:` in footer.

```
feat!: remove legacy auth endpoint

BREAKING CHANGE: /api/v1/auth removed, use /api/v2/auth
```

---

## Semantic Versioning (SemVer)

Format: `MAJOR.MINOR.PATCH[-pre-release][+build]`

| Part    | Bump when…                              |
|---------|------------------------------------------|
| `MAJOR` | Breaking/incompatible API change         |
| `MINOR` | New feature, backward-compatible         |
| `PATCH` | Bug fix, backward-compatible             |

Examples: `1.0.0` → `1.1.0` (new feature) → `2.0.0` (breaking change)

Conventional Commits → SemVer mapping:
- `feat` → MINOR bump
- `fix` → PATCH bump  
- `feat!` or `BREAKING CHANGE` → MAJOR bump

---

## 3-Tier Learning Path

### Newbie — Get git working
1. `git init`, `git add .`, `git commit -m "..."`, `git push`
2. Understand `status`, `log --oneline`, `diff`
3. Resource: [Learn Git Branching](https://learngitbranching.js.org/) (levels 1–5)

### Amateur — Work in teams
1. Branching strategies: GitHub Flow for daily use
2. Conventional commits: `feat:`, `fix:`, `docs:`, `chore:`
3. Pull requests, code review, merge conflicts
4. `rebase -i` for clean history before PR
5. Resources: [Atlassian Git Tutorials](https://www.atlassian.com/git/tutorials), [GitHub Flow](https://docs.github.com/en/get-started/using-github/github-flow)

### Pro — Master git internals and advanced workflows
1. GitFlow vs GitHub Flow vs Trunk-Based — choose per team context
2. Git internals: object model, blobs, trees, DAG
3. Hooks: pre-commit linting, commit-msg validation, pre-push tests
4. `bisect` for bug hunting, `reflog` for recovery
5. Semantic versioning + conventional commits + automated CHANGELOG
6. Resources: [Git Internals (Pro Git Ch.10)](https://git-scm.com/book/en/v2/Git-Internals-Plumbing-and-Porcelain), [GitFlow](https://nvie.com/posts/a-successful-git-branching-model/), [Trunk-Based Development](https://trunkbaseddevelopment.com/)

---

## Useful .gitignore Patterns

```gitignore
# Java
*.class
target/
out/
*.jar

# IDE
.idea/
.vscode/
*.iml

# OS
.DS_Store
Thumbs.db

# Secrets
*.env
.env.local
*.properties
config/local/
```

---

## Common Git Aliases

```sh
git config --global alias.st status
git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.lg "log --oneline --graph --all --decorate"
git config --global alias.undo "reset --soft HEAD~1"
```
````
