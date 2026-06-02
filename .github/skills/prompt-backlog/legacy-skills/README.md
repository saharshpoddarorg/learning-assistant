# Legacy Skills Archive

This folder contains legacy skill files that have been migrated to the `_modular/` staging area
but are preserved here for reference until the migration cycle is fully validated.

## Policy

- **Do NOT delete** files from this folder manually
- **Do NOT edit** files here — they are read-only archives
- When the corresponding `_modular/` skill is confirmed stable, the legacy file here
  can be permanently deleted as part of the next migration cleanup pass
- Files here are NOT loaded by Copilot (they are outside the active skill paths)

## Contents

| Skill | Migrated To | Prompt-Backlog Entry | Status |
|---|---|---|---|
| `git-vcs/SKILL.md` | `_modular/git-vcs/SKILL.md` | `git-vcs-learning.md` | Awaiting cleanup |
| `github-workflow/SKILL.md` | `_modular/github-workflow/SKILL.md` | `github-workflow-learning.md` | Awaiting cleanup |
| `web-reader/SKILL.md` | `_modular/web-reader/SKILL.md` | `web-reader-patterns.md` | Awaiting cleanup — Patterns 1-5, Formatting Rules, Skill Combos not yet in a prompt |
| `requirements-research/SKILL.md` | `_modular/requirements-research/SKILL.md` | `requirements-research-copilot-workflow.md` | Awaiting cleanup — Copilot workflow snippet |
| `deep-research/SKILL.md` | Split into 5 modular skills (`investigation-methodology`, `technology-evaluation`, `root-cause-analysis`, `decision-records`, `code-investigation`) | `deep-research-copilot-workflow.md` | Awaiting cleanup — workflow chain + Copilot prompt strings |
