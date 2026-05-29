# Prompt Backlog: git-vcs learning guide

## Source

- **Legacy skill:** `.github/skills/devops-tooling/git-vcs/SKILL.md`
- **Sections not migrated to modular skill:**
  - `## 3-Tier Learning Path` (lines 192–211) — newbie/amateur/pro with resources
  - `## Common Git Aliases` (lines 248–258) — personal git config shortcuts
  - Extra `.gitignore` entries (`*.properties`, `config/local/`) beyond the modular set

## What It Does

**3-Tier Learning Path:** Structured progression guide for learning Git by experience level:

- **Newbie** — `git init`, `git add .`, `git commit`, `git push`; understanding `status`, `log --oneline`, `diff`
- **Amateur** — GitHub Flow, conventional commits, PRs, merge conflicts, `rebase -i` for clean history
- **Pro** — GitFlow vs GitHub Flow vs Trunk-Based choice, git internals (object model, blobs, trees, DAG),
  hooks (pre-commit, commit-msg, pre-push), `bisect`, automated CHANGELOG from conventional commits

**Resources:**
- Newbie: [Learn Git Branching](https://learngitbranching.js.org/)
- Amateur: [Atlassian Git Tutorials](https://www.atlassian.com/git/tutorials), [GitHub Flow docs](https://docs.github.com/en/get-started/using-github/github-flow)
- Pro: [Git Internals (Pro Git Ch.10)](https://git-scm.com/book/en/v2/Git-Internals-Plumbing-and-Porcelain), [GitFlow](https://nvie.com/posts/a-successful-git-branching-model/), [Trunk-Based Development](https://trunkbaseddevelopment.com/)

**Git Aliases (removed as generic but recorded):**

```sh
git config --global alias.st status
git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.lg "log --oneline --graph --all --decorate"
git config --global alias.undo "reset --soft HEAD~1"
```

## Why It's a Prompt (Not a Skill)

The 3-tier learning path is a *teaching workflow* — structured progression, resource recommendations,
and step-by-step learning. Skills should contain reference knowledge Copilot injects at invocation,
not a curriculum for teaching users.

Git aliases are personal preference / environment setup — better suited to a prompt-driven setup guide.

## Existing Prompt Coverage

- **Name:** `/git-vcs` (exists at `.github/prompts/tools/git-vcs.prompt.md`)
- **Status:** Already fully covered — the existing prompt has:
  - Complete domain map (Fundamentals → Branching → Workflows → Conventions → Advanced)
  - 3-tier response structure (newbie/amateur/pro) matching the legacy skill's learning path
  - Branching strategy decision guide
  - Conventional Commits ↔ SemVer quick reference table
  - All curated resources from the legacy skill's learning path
- **Action needed:** No new prompt required. The `/git-vcs` prompt already handles all trimmed content.

## Potential Enhancement

The existing `/git-vcs.prompt.md` is a `learning-mentor` agent prompt. If a `/setup-git-aliases`
or `/git-setup` command is ever wanted, the aliases content above is the source material.
