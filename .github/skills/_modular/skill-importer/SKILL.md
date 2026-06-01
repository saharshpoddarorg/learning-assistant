---
name: skill-importer
description: >
  Import, analyse, enhance and merge Copilot skills (and prompts/instructions/agents/MCP
  bundles) from external sources into this repo's skills library. Use when the user
  says they copied a skill folder from a colleague, downloaded a skill from awesome-copilot
  or skills.sh, has a path to a skill on local disk, pasted skill content in chat,
  has a GitHub URL or ZIP archive containing skills, wants to import company work
  content as a skill, mentions importing/merging/absorbing/onboarding/adopting external
  skills, wants to enhance an existing skill with content from outside, asks to make
  an imported PAT-based skill support multiple accounts, wants to detect overlap
  between an imported skill and existing library skills, asks for help re-importing
  a newer version of a previously imported skill, mentions 3-way merging skill versions,
  wants to sanitise hardcoded secrets/URLs/usernames out of an imported skill, wants
  to propose alternative implementations (Bash vs PowerShell vs Python; PAT vs OAuth
  vs keychain; single-account vs multi-account) for an imported skill, or asks how
  to cohesively grow the skills library as a union of existing + imported content.
  Always activates in pair-programming style with per-step user approval.
---

# Skill Importer

A pair-programmed workflow for ingesting external skills (and related primitives)
into this repo's library, detecting overlap, sanitising risky content, applying
chosen enhancements, and merging cleanly — with full provenance and 3-way re-import
support.

This skill **runs** the protocol. The reference material lives in
[`customization-evolution-guide.md`](../../../docs/customization-evolution-guide.md)
(Import Protocol, Merge Protocol, Fitness Scorecard, Regression Checklist). Do not
duplicate it — link into it.

## Operating principles

1. **Always pair-programmed.** Every phase ends at a checkpoint; never auto-advance.
2. **Always dry-run.** Every file write is previewed as a diff and approved individually.
3. **Always provenance.** Originals are stashed untouched; imported skills carry
   `source`, `imported-on`, `original-version-hash`, and `enhancements-applied`.
4. **Always sanitise.** Secrets and confidential markers block import until resolved.
5. **Always classify.** No skill enters the library without overlap classification
   against existing skills.

## Trigger-to-action map

| User said | First move |
|---|---|
| "I copied a skill from a colleague at `<path>`" | Phase 1, source = local folder |
| "Pasted below, please import" | Phase 1, source = chat paste → temp file |
| "Here's a GitHub URL with a skill" | Phase 1, source = shallow clone |
| "ZIP at `<path>`" | Phase 1, source = extract |
| "Make this Confluence skill support multiple accounts" | Skip to Phase 5 (enhance-existing mode) |
| "Re-import the newer version of `<skill>`" | Phase 1 detects prior import → 3-way merge mode |
| "Just check if this overlaps with my existing skills" | Phases 1, 2, 4 only — no writes |

## The 6 phases

```text
1 Ingest        detect source shape, inventory files, check prior import
2 Deep-read     full logic walkthrough + secrets/sanitisation scan
3 Alternatives  propose variants for pain points / overlaps / extensibility gaps
4 Overlap       auto-classify against TAXONOMY.md, pair-program decision
5 Personalise   walk enhancement pack item-by-item, y/n each
6 Plan + apply  per-file dry-run diffs, per-file approval, then propagate
```

---

## Phase 1 — Ingest

### Source-shape detection

| Source | Detection | Handling |
|---|---|---|
| Local folder | Path is a directory | Walk the tree |
| Local file | Path is a file | Read directly |
| Chat paste | User pastes content in the request | Save to `session-state/.../import-<ts>/` and proceed as local |
| GitHub repo or subtree URL | `https://github.com/<owner>/<repo>(/tree/<path>)?` | Shallow clone to `$env:TEMP\skill-import-<ts>\` |
| Raw GitHub file URL | `raw.githubusercontent.com/...` | Fetch via `web_fetch` |
| ZIP archive | Path ends `.zip` | Extract to `$env:TEMP\skill-import-<ts>\` |
| Multi-skill bundle | 2+ `SKILL.md` files at different depths | Iterate Phase 1-6 once per skill |

### Primitive-shape classification

After inventory, classify into one of: `skill-only`, `skill+scripts`, `prompt-only`,
`instruction-only`, `agent-only`, `mcp-bundle`, `loose-snippets`, `multi-skill-bundle`.

For non-skill primitives, ask the user whether to import as-is (different folder)
or convert into a skill.

### Prior-import check

Before exiting Phase 1, check `.github/skills/_imported-originals/<inferred-name>/`.
If present → switch to **re-import / 3-way merge** mode (see end of doc).

### Phase 1 checkpoint

Output:

- Source + resolved local path
- Shape classification
- File inventory (path, size, type)
- Detected primitive types
- Prior-import status
- Proposed next phase

Wait for user confirmation before Phase 2.

---

## Phase 2 — Deep-Read and Sanitisation

Read every file **completely**. No skimming, no truncation.

### Extract from `SKILL.md`

- `name`, `description`, activation triggers (each keyword)
- Tier structure, cheatsheets, examples
- Tool references in `tools:` and embedded commands
- External-system assumptions (Jira account, AWS profile, etc.)

### Per-script analysis

| Aspect | Capture |
|---|---|
| Runtime | `pwsh` / `bash` / `python3` + version constraints |
| Purpose | One-line summary |
| Entry point | Invocation + args |
| External calls | HTTP endpoints, CLIs invoked, files read/written |
| Auth model | PAT / OAuth / basic / IAM / keychain / env-var |
| OS assumptions | Path separators, `$HOME`/`~`, OS-only APIs |
| Error handling | Present? Graceful? Logged? |
| Idempotency | Safe to re-run? Side effects? |
| Logic summary | Block-by-block prose, 5-15 lines |

### Sanitisation scan (blocking)

| Category | Examples | Action |
|---|---|---|
| Live secrets | `ghp_*`, `xoxb-*`, `AKIA*`, `Bearer eyJ`, `pat_*`, `password\s*=`, `token\s*=\s*['"]` | **Must replace** — blocks Phase 6 |
| Company hostnames | `*.corp`, `*.internal`, employer-specific domains | Flag for confirmation |
| Personal identifiers | Email, AD username, employee ID, colleague's name | Replace with placeholder or env-var |
| Hardcoded paths | `C:\Users\<name>\`, `/home/<name>/`, `/Users/<name>/` | Convert to env-var or parameter |
| Internal service URLs | Specific Jira/Confluence/Bitbucket instance hostnames | Move to config or env-var |
| Confidentiality markers | "Confidential", "Internal use only", "Proprietary" | **Stop** — ask user if importable |

### Pain points to surface

Single-tenant assumptions; OS-locked scripts; missing error handling; no retry/rate-limit
on API calls; no logging; no `--help`; coupling to a specific directory layout;
sequential calls that should be parallel.

### Phase 2 checkpoint

Output:

- Skill purpose (one paragraph)
- Per-script summary table
- Sanitisation findings: blocking + flagged + suggested replacements
- Pain-point list

Wait for user resolution of blocking items before Phase 3.

---

## Phase 3 — Alternatives

Propose alternatives **only** when one of these triggers fires:

| Trigger | Proposal type |
|---|---|
| Hardcoded auth flagged in Phase 2 | Auth models: PAT, OAuth, keychain, env-var, config file |
| OS-locked script | Shell variants (PowerShell + Bash) or single-source Python rewrite |
| Single-account assumption | Multi-account: named profiles, account-selector arg, env-var per account |
| Overlap with existing skill (Phase 4 preview) | Pattern alignment with existing convention |
| Weak extensibility (hardcoded list) | Config-file, plugin, registry approach |
| User explicitly asks | Whatever was asked |

Otherwise: **do not propose alternatives**. Silence is correct.

### Proposal format

Per alternative: short name, 2-3 sentence approach, pros, cons, effort (low/med/high),
reversibility (easy/moderate/hard), recommendation rank.

User picks per decision area or "stick with original".

---

## Phase 4 — Overlap Classification

### Classes

| Class | Meaning | Default action |
|---|---|---|
| `new` | No meaningful overlap | Create new skill folder |
| `merge-into-<X>` | 30-80% topical overlap; X is natural home | Append/enhance X |
| `supersedes-<X>` | Imported version is clearly better/newer | Replace X (after diff review) |
| `superseded-by-<X>` | X already covers everything imported | Skip; possibly cherry-pick fragments |
| `conflicts-with-<X>` | Same triggers/domain, different approach | Pair-program merge or rename one |
| `complementary-to-<X>` | Different facet of same domain | Add as new; cross-link both |

### Protocol

1. For each existing skill, score description-keyword overlap (Jaccard on trigger
   phrases), topical overlap (judgement from Phase 2), tool/system overlap.
2. Pick top 3 candidates.
3. Auto-classify against each, present with confidence (H/M/L) and reasoning.
4. Pair-program the final decision.

### Phase 4 checkpoint

Output:

- Top 3 candidate overlaps with score + class + confidence + reasoning
- Recommended action + at least one alternative
- Wait for user's call

---

## Phase 5 — Personalisation Pass

Walk the **enhancement pack** item-by-item. For each: explain what it would change
for THIS specific import; ask y/n; record decision.

| # | Enhancement | Applies when |
|---|---|---|
| 1 | Multi-account auth | Single-account/tenant/PAT assumption |
| 2 | OS-agnostic scripts | One-OS scripts; offer parallel variants or Python rewrite |
| 3 | Externalised secrets | Secrets in source / undocumented env-vars; move to keychain + documented env-var fallback |
| 4 | Brain/vault linkage | Skill produces or consumes content that fits `brain/ai-brain/` |
| 5 | Dual-pattern check | Reference-only but workflow-heavy (or vice versa) — consider matching prompt |
| 6 | Provenance frontmatter | Always |
| 7 | Sanitisation | Apply confirmed sanitisations from Phase 2 |

### Provenance frontmatter

Append to the imported skill's frontmatter:

```yaml
# Provenance (added by skill-importer)
source: "<path-or-url>"
imported-on: 2026-06-01
original-author: "<name-or-unknown>"
original-license: "<spdx-or-unknown>"
original-version-hash: <sha256-of-original-SKILL.md>
import-tool-version: 1
enhancements-applied:
  - multi-account-auth
  - secrets-externalised
re-imports: []
```

---

## Phase 6 — Plan and Apply

### Pre-write

1. **Stash original** untouched to
   `.github/skills/_imported-originals/<skill-name>/<YYYY-MM-DD>/`
   with a `_metadata.json` (source, hash, date).
2. **Build staged tree** in a temp location with sanitisation + enhancements applied.

### Dry-run preview (per file)

```text
File: <path>  [NEW | EDIT]
─── Diff (unified) ───
<diff against current repo, or full content if NEW>
─── End ───
Approve write? [y / n / edit / skip]
```

`edit` lets the user dictate adjustments before approving.

### Typical write set

| File | Action | When |
|---|---|---|
| `.github/skills/<category>/<name>/SKILL.md` | NEW or EDIT | Always |
| `.github/skills/<category>/<name>/scripts/*` | NEW | If skill+scripts |
| `.github/skills/_imported-originals/<name>/<date>/**` | NEW | Always (untouched) |
| `.github/skills/TAXONOMY.md` | EDIT | If `new` / `supersedes` / category move |
| `.github/copilot-instructions.md` (skills routing) | EDIT | If `new` |
| `.github/docs/skills-library.md` | EDIT | If inventory changed |
| `.github/docs/navigation-index.md` | EDIT | If `new` |
| `.github/prompts/<name>.prompt.md` | NEW | Only if dual-pattern enhancement approved |
| `.github/docs/slash-commands.md` | EDIT | Only if prompt added |
| `KeywordIndex.java` | EDIT | Only if new concept area introduced |

### Post-write

1. `.\__md_lint.ps1` must exit 0
2. `.\gradlew.bat build` must succeed (only if Java enum changes)
3. Run the Section A-O checklist from
   `.github/instructions/change-completeness.instructions.md`
4. Suggest a Conventional Commit:

```text
feat(skills): import <name> from <source> as <classification>

Imported via skill-importer. <one-sentence purpose>.
Classification: <new | merge-into-X | supersedes-X | ...>.
Enhancements applied: <list>.
Sanitisations: <count> secrets/URLs/usernames replaced.
Original stashed under .github/skills/_imported-originals/<name>/<date>/.

— assisted by gpt
```

---

## Re-Import and 3-Way Merge

Triggered when Phase 1 finds an existing
`.github/skills/_imported-originals/<inferred-name>/`.

| Label | Source |
|---|---|
| `BASE` | Most recent original under `_imported-originals/<name>/<latest-date>/` |
| `THEIRS` | Newly imported version (this session) |
| `OURS` | Current contents of `.github/skills/<category>/<name>/` |

### Per-file merge

1. `diff BASE OURS` → local changes
2. `diff BASE THEIRS` → upstream changes
3. Intersection → conflict
4. Union of non-conflicts → auto-apply (still subject to Phase 6 dry-run approval)

For each conflict: present BASE / OURS / THEIRS side-by-side; user picks or writes
a hybrid.

### Post-merge

1. Stash new THEIRS under `_imported-originals/<name>/<new-date>/` — do **not**
   overwrite older originals; keep the full history.
2. Append a `re-imports[]` entry to the provenance frontmatter.
3. Re-run Phases 5-6 — enhancements may need re-applying against new content.

---

## Modes (set by the slash command)

| Mode | Phases run |
|---|---|
| `full` (default) | 1 → 6 |
| `overlap-only` | 1, 2, 4 — no writes |
| `sanitise-only` | 1, 2 — produce report only |
| `re-import` | 1 → 6 with 3-way merge |
| `enhance-existing` | Skip Phase 1; treat source as an already-imported skill; run 3, 5, 6 |

---

## Anti-patterns

| Anti-pattern | Why bad | Do instead |
|---|---|---|
| Importing without reading every file | Misses secrets, OS assumptions, broken scripts | Phase 2 is mandatory |
| Auto-applying the enhancement pack | Violates pair-programming preference | Walk item-by-item |
| Overwriting existing skill without diff | Loses local enhancements | Phase 4 + 3-way merge |
| Skipping the original stash | Future re-imports can't 3-way merge | Always stash |
| Writing before per-file approval | Violates dry-run preference | Phase 6 dry-run is mandatory |
| Treating a multi-skill bundle as one import | Wrong classification, wrong folder | Iterate per skill |
| Importing company-confidential content | Legal/IP risk | Stop at Phase 2 if marked |
| Skipping Phase 4 (overlap) | Activation conflicts, library bloat | Always classify |

## Related

- [`copilot-customization`](../../devops-tooling/copilot-customization/SKILL.md) — primitive types, frontmatter, fitness scorecard
- [`mcp-development`](../../devops-tooling/mcp-development/SKILL.md) — when an import is actually an MCP bundle
- [`atlassian-tools`](../../devops-tooling/atlassian-tools/SKILL.md) — common merge target for Jira/Confluence imports
- [`git-vcs`](../git-vcs/SKILL.md) — 3-way merge concepts
- [`brain-management`](../../knowledge-management/brain-management/SKILL.md) — when imports produce/consume brain content
- [`customization-evolution-guide.md`](../../../docs/customization-evolution-guide.md) — full reference protocol
- [`TAXONOMY.md`](../../TAXONOMY.md) — existing skills (overlap reference)
- [`change-completeness.instructions.md`](../../../instructions/change-completeness.instructions.md) — Section A-O propagation
