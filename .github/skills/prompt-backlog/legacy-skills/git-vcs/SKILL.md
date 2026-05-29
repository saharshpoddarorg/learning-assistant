---
name: git-vcs
description: >
  ARCHIVED — migrated to _modular/git-vcs/SKILL.md.
  Residual content: 3-tier learning path, git aliases.
  See prompt-backlog/git-vcs-learning.md for migration notes.
---

> **ARCHIVED LEGACY SKILL** — core content migrated to `_modular/git-vcs/SKILL.md`.
> This file contains only the residual content not yet moved to a prompt.
> See `prompt-backlog/git-vcs-learning.md` for migration notes.

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
5. Resources: [Atlassian Git Tutorials](https://www.atlassian.com/git/tutorials),
   [GitHub Flow](https://docs.github.com/en/get-started/using-github/github-flow)

### Pro — Master git internals and advanced workflows

1. GitFlow vs GitHub Flow vs Trunk-Based — choose per team context
2. Git internals: object model, blobs, trees, DAG
3. Hooks: pre-commit linting, commit-msg validation, pre-push tests
4. `bisect` for bug hunting, `reflog` for recovery
5. Semantic versioning + conventional commits + automated CHANGELOG
6. Resources:
   - [Git Internals — Pro Git Ch.10](https://git-scm.com/book/en/v2/Git-Internals-Plumbing-and-Porcelain)
   - [GitFlow](https://nvie.com/posts/a-successful-git-branching-model/)
   - [Trunk-Based Development](https://trunkbaseddevelopment.com/)

---

## Git Aliases

```sh
git config --global alias.st status
git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.lg "log --oneline --graph --all --decorate"
git config --global alias.undo "reset --soft HEAD~1"
```
