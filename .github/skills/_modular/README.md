# Modular Skills — Staging Area

> **Purpose:** Staging folder for the skills modularization migration.
> Once all 23 skills are migrated here (flat, standardized), the legacy category
> folders will be deleted and these skills will move up to `.github/skills/`.

---

## Standard Skill Structure

Every modular skill follows this internal layout:

```text
<skill-name>/
├── SKILL.md              (required — frontmatter + full content)
├── references/           (optional — cheatsheets, catalogs, reference docs)
├── scripts/              (optional — automation scripts)
└── config/               (optional — .env examples, settings templates)
```

### SKILL.md Requirements

1. **YAML frontmatter** with at minimum: `name`, `description`
2. **Description** must clearly state activation triggers (when the skill fires)
3. **Content** follows 3-tier structure where applicable (newbie / amateur / pro)
4. **No broken internal links** — all references must be relative to the skill folder

---

## Migration Status

| # | Skill | Source (legacy) | Status | Notes |
|---|---|---|---|---|
| 1 | `java-build` | `languages-platforms/java-build/` | migrated | Single-file, clean |
| 2 | `java-debugging` | `languages-platforms/java-debugging/` | migrated | Trimmed ~60% — removed generic fix patterns |
| 3 | `java-formatting` | `languages-platforms/java-formatting/` | pending | opt-in |
| 4 | `java-learning-resources` | `languages-platforms/java-learning-resources/` | pending | |
| 5 | `jvm-platform` | `languages-platforms/jvm-platform/` | pending | |
| 6 | `mac-dev` | `languages-platforms/mac-dev/` | pending | |
| 7 | `design-patterns` | `design-architecture/design-patterns/` | migrated | ~31% trimmed — removed generic Clean Architecture diagram and Java-specific syntax (Records, sealed, pattern matching) |
| 8 | `software-development-roles` | `design-architecture/software-development-roles/` | migrated | ~24% trimmed — removed branching conventions (delegate git-vcs), collaboration patterns (generic), code review checklist (generic); deleted empty design-architecture/ folder |
| 9 | `deep-research` | `dev-process/deep-research/` | pending | |
| 10 | `requirements-research` | `dev-process/requirements-research/` | pending | |
| 11 | `github-workflow` | `dev-process/github-workflow/` | migrated | ~48% trimmed — kept gh CLI, PR best practices, Actions ref; PR-link-handling workflow deferred to prompt-backlog |
| 12 | `git-vcs` | `devops-tooling/git-vcs/` | migrated | ~57% trimmed — kept branching strategies, SemVer, conventional commits ref; removed basic commands, aliases, learning path |
| 13 | `mcp-development` | `devops-tooling/mcp-development/` | pending | |
| 14 | `copilot-customization` | `devops-tooling/copilot-customization/` | pending | |
| 15 | `atlassian-tools` | `devops-tooling/atlassian-tools/` | pending | Complex (scripts, config, refs) |
| 16 | `web-reader` | `devops-tooling/web-reader/` | migrated | 67% trimmed; workflow patterns routed to prompt-backlog |
| 17 | `brain-management` | `knowledge-management/brain-management/` | pending | |
| 18 | `pkm-management` | `knowledge-management/pkm-management/` | pending | |
| 19 | `digital-notetaking` | `knowledge-management/digital-notetaking/` | pending | |
| 20 | `learning-resources-vault` | `learning-resources/learning-resources-vault/` | pending | Large (14 files) |
| 21 | `software-engineering-resources` | `learning-resources/software-engineering-resources/` | pending | |
| 22 | `career-resources` | `career/career-resources/` | pending | |
| 23 | `daily-assistant-resources` | `daily-life/daily-assistant-resources/` | migrated | 40% trimmed — removed stale URLs, generic tool lists, meta section |

---

## Migration Protocol

For each skill:

1. **Copy** from legacy location to `_modular/<skill-name>/`
2. **Review & clean** — fix broken links, standardize frontmatter, ensure self-contained
3. **Verify** — skill should work from its new flat path
4. **Mark migrated** in the table above

### Final Swap (after all 23 are done)

```powershell
# 1. Delete legacy category folders
Remove-Item -Recurse .github/skills/career
Remove-Item -Recurse .github/skills/daily-life
Remove-Item -Recurse .github/skills/design-architecture
Remove-Item -Recurse .github/skills/dev-process
Remove-Item -Recurse .github/skills/devops-tooling
Remove-Item -Recurse .github/skills/knowledge-management
Remove-Item -Recurse .github/skills/languages-platforms
Remove-Item -Recurse .github/skills/learning-resources

# 2. Move modular skills up
Get-ChildItem .github/skills/_modular -Directory | Move-Item -Destination .github/skills/

# 3. Remove staging folder
Remove-Item -Recurse .github/skills/_modular

# 4. Update TAXONOMY.md paths
```

---

## What Changes After Migration

| Before | After |
|---|---|
| `skills/devops-tooling/atlassian-tools/SKILL.md` | `skills/atlassian-tools/SKILL.md` |
| `skills/languages-platforms/java-build/SKILL.md` | `skills/java-build/SKILL.md` |
| 8 category folders + 23 skill folders | 23 skill folders (flat) |
| TAXONOMY.md references category paths | TAXONOMY.md references flat paths |
| `copilot-instructions.md` mentions category paths | Only skill names (already the case) |
