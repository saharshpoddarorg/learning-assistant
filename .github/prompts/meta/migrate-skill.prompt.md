---
name: migrate-skill
description: >
  Iteratively migrate a legacy skill into the _modular/ folder.
  Covers: understanding the skill, trimming/splitting, discarding redundant content,
  and cleaning up the legacy version. One skill per chat session.
agent: agent
---

# /migrate-skill — Skills Modularization Workflow

## Input

${input:skill:Which skill to migrate? (name or 'pick' to auto-select the next simple one):pick}

---

## Context

- **Staging folder:** `.github/skills/_modular/`
- **Legacy folder:** `.github/skills/<category>/<skill-name>/`
- **Migration tracker:** `.github/skills/_modular/README.md`
- **Already migrated:** `java-build`, `package-manager` (new), `career-resources`
- **Delegation rule:** Skills requiring tool installs delegate to `package-manager`

---

## Workflow (execute step-by-step, pausing for user input at decision points)

### Step 1 — Pick the skill

If `${input:skill}` = `pick`:
- Check the migration tracker README for the next pending skill
- Prefer simple single-file skills first, defer complex ones (atlassian-tools, learning-resources-vault) to later
- Tell the user which skill you've picked and why

If a specific skill is named:
- Locate it in the legacy folder structure

### Step 2 — Understand & explain

- Read the full legacy SKILL.md (and any companion files)
- Present a breakdown to the user:
  - What sections exist and their line counts
  - What each section provides
  - Initial assessment: keep / trim / remove / split

**Pause here.** Wait for the user to confirm or adjust the plan.

### Step 3 — Clean & migrate

Based on user direction:

1. **Copy** the skill folder to `_modular/<skill-name>/`
2. **Trim** — remove content that is:
   - Trivial (things Copilot already knows without being told)
   - Stale (hardcoded versions, dates, counts that go out of date)
   - Generic (knowledge not specific enough to need injection)
   - Meta/filler ("How to use this skill", learning paths unless requested)
3. **Split** — if a skill covers two distinct concerns, propose splitting into two skills
4. **Standardize** frontmatter:
   - `name`: kebab-case skill name
   - `description`: clear activation triggers + delegation note if applicable
5. **Add delegation** if the skill requires tool installations: add `Delegates to: package-manager` in description

### Step 4 — Iterative refinement

After presenting the cleaned version:
- Ask if the user wants further changes
- Apply any additional trims, splits, or restructuring
- Repeat until the user is satisfied

### Step 5 — Delete legacy & finalize

Once the user approves:

1. **Delete** the legacy skill folder
2. **Delete** the parent category folder if now empty
3. **Update** `_modular/README.md` migration tracker (mark as migrated)

---

## Guiding Principles

- **Less is more** — a skill should contain only what Copilot can't figure out on its own
- **No install commands in non-package-manager skills** — delegate to `package-manager`
- **No hardcoded versions** — use `<version>` placeholders
- **No generic knowledge** — if Copilot knows it without the skill file, remove it
- **Flat structure** — no category nesting in `_modular/`
- **One skill = one concern** — split if it covers two distinct domains
- **Multi-OS** — if OS-specific content is needed, cover macOS + Windows (+ Linux if relevant)
