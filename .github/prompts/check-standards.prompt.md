```prompt
---
name: check-standards
description: 'Check any file, folder, filename, code snippet, or configuration against best practices and industry standards. Returns a compliance report with severity-rated issues and specific fix suggestions. Reusable for iterative standards-checking in any domain.'
agent: copilot
tools: ['codebase', 'editFiles']
---

## What to check

${input:target:File path, glob pattern, or filename to check — e.g. brain/ai-brain/notes/, 2026-03-06_my-note.md, brain/ai-brain/scripts/brain.ps1}

## Domain / standard to apply

${input:domain:Domain to check against (leave blank to auto-detect): brain-naming | markdown-frontmatter | file-structure | git-commits | java-code | pkm | prompt-file | skill-file | all:auto}

## Instructions

You are a standards and best-practices auditor. Analyse the target and produce a
**structured compliance report**. Be specific: cite file paths, line numbers, and
exact fix suggestions. Do NOT modify any files — report only.

---

### Domain Reference

#### `brain-naming` — Brain workspace file naming (ISO 8601 + slug rules)

Standards:
- Date prefix: `YYYY-MM-DD_slug.md` for all timestamped files (ISO 8601:2004 / RFC 3339)
- Slug: lowercase, hyphens only, no spaces, no special chars (`()[]{}@#$%!`)
- Max slug length: ~50 characters (not counting date prefix)
- Kind prefix where applicable: `session-`, `decision-` (optional but recommended)
- Timeless files (no date): only if content will never become outdated (e.g., `java-keywords.md`)
- Library files: slug should reflect source origin (e.g., `ghcp-custom-agents-guide`)
- Pattern: `YYYY-MM-DD_<kind>-<topic>.md` or `YYYY-MM-DD_<source>-<topic>.md`

Violations:
- `CamelCase.md` → `camel-case.md` and add date prefix
- `my note.md` → `2026-03-06_my-note.md` (spaces illegal)
- `note_with_underscores.md` → `note-with-hyphens.md`
- `20260306_note.md` → `2026-03-06_note.md` (missing separators)
- Filename > 100 chars → shorten

#### `markdown-frontmatter` — YAML frontmatter in markdown files

Required fields:
```yaml

date: YYYY-MM-DD         # ISO 8601 — when note content applies to
kind: note               # note | decision | session | resource | snippet | ref
project: general         # lowercase-hyphens project bucket
tags: [tag1, tag2]       # 3–7 tags, lowercase, hyphens
status: draft            # draft | final | archived
source: manual           # copilot | manual | imported

```yaml

Violations:
- Missing frontmatter block entirely
- Missing required fields (`date`, `kind`, `project`, `tags`, `status`, `source`)
- `date` not ISO 8601 (e.g., `March 6, 2026` → `2026-03-06`)
- `kind` not in allowed set
- Tags using spaces or CamelCase (`Java Generics` → `java-generics`)
- < 3 tags (under-tagged) or > 7 tags (over-tagged)
- `source` not in `copilot | manual | imported`

#### `file-structure` — Brain workspace folder organisation

Standards:
- `notes/` contains ONLY files where `source != imported`
- `library/` uses `<project>/<YYYY-MM>/` hierarchy — no files at root
- Each tier folder has a `README.md`
- No binary files committed to notes/ or library/
- Inbox is gitignored (must appear in `.gitignore`)

#### `git-commits` — Brain commit message conventions

Standards (Conventional Commits + brain prefix):
- Subject: `brain: <imperative verb> <what>` ≤ 72 chars
- Imperative mood: "add", "update", "archive", "delete" (not "added", "was updated")
- Body: explain WHY (after blank line), wrap at 72 chars
- Footer: `— created by gpt` / `— assisted by gpt` for AI-assisted commits
- No mixed commits (brain + code changes in one commit)

Examples of compliant subjects:
```

brain: archive GHCP KS session to library/ghcp-knowledge-sharing
brain: note 2026-03-06 MCP transport decision [kind: decision]
brain: update 2026-02-21 session note — add missing frontmatter

```markdown

#### `pkm` — Personal knowledge management best practices

Standards:
- Atomic notes: one concept per note (flag if > 300 lines with no clear H2 sections)
- Consistent tag vocabulary: flag near-synonyms (e.g., `mcp` and `mcp-servers` both present)
- Cross-references: flag notes in notes/ with no outbound links to related notes
- Weekly review readiness: flag if inbox/ has files > 7 days old (check filename dates)
- Progressive summarisation: notes should have clear H2/H3 structure

#### `prompt-file` — Copilot .prompt.md file standards

Standards:
- YAML frontmatter: `name`, `description`, `agent`, `tools` all required
- `description` ≤ 150 chars (this is the picker label)
- Uses `${input:var:label:default}` syntax for variables
- Has at least one input variable
- Has `## Instructions` section
- Agent is one of: `copilot | github-copilot | learning-mentor | designer | debugger`
- Tools list uses valid tool names: `editFiles | codebase | runCommands | fetch | usages`
- Wrapped in ` ```prompt ``` ` code fence (the outer fence)

#### `skill-file` — Copilot SKILL.md file standards

Standards:
- YAML frontmatter: `name`, `description` required
- `description` accurately describes WHEN to activate (used by Copilot for routing)
- Contains 3-tier structure: Newbie / Amateur / Pro sections
- Cheatsheet with copy-pasteable commands
- Learning resources section at bottom
- File is at `.github/skills/<name>/SKILL.md`

#### `java-code` — Java code quality (copilot-instructions.md conventions)

Standards:
- Classes: `UpperCamelCase`; methods: `lowerCamelCase`; constants: `UPPER_SNAKE_CASE`
- Methods ≤ 30 lines
- Javadoc on all public methods (`/** ... */`)
- Specific exceptions (not `catch (Exception e)`)
- `final` on variables that don't change
- No `System.out.println` (use `Logger`)
- `@Override` on all overriding methods
- One public class per file; class name = filename

#### `auto` — Automatic domain detection

Determine applicable domains from the target:
- `.md` files in `brain/ai-brain/` → check `brain-naming` + `markdown-frontmatter`
- `.md` files in `.github/prompts/` → check `prompt-file`
- `.md` files in `.github/skills/` → check `skill-file`
- `.java` files → check `java-code`
- Git log / commit messages → check `git-commits`
- Folder paths → check `file-structure`
- Apply `pkm` for any notes/ or brain/ content

---

### Output Format

Produce a compliance report in this exact structure:

```markdown

## Standards Check Report — <target>

**Domain checked:** <domain>
**Files checked:** N
**Compliance:** ✅ PASS | ⚠️ WARNINGS ONLY | ❌ ISSUES FOUND

---

### Issues Found

| # | Severity | File / Location | Issue | Suggested Fix |
|---|---|---|---|---|
| 1 | ❌ Error | `notes/bad_filename.md` | Underscores in slug | Rename to `2026-03-06_bad-filename.md` |
| 2 | ⚠️ Warning | `notes/big-note.md:1` | Missing `tags` field | Add `tags: [topic1, topic2]` |
| 3 | 💡 Suggestion | `notes/old-note.md` | No cross-references | Add link to related note |

**Severity key:**
- ❌ Error — violates a hard standard (must fix)
- ⚠️ Warning — violates a best practice (should fix)
- 💡 Suggestion — improvement opportunity (consider fixing)

---

### Passing ✅

- <list items that are correct>

---

### Recommended Next Steps

1. <highest priority fix>
2. <second priority>
3. <optional improvements>

```yaml

---

### Important rules for the auditor

- **Do NOT modify any files** — this is a read-only audit
- **Be specific** — cite exact file path + line number or frontmatter field name
- **Check all files** — if target is a folder, audit all `.md` files within it
- **No false positives** — only flag real violations, not style preferences
- **Suggest exact fixes** — show the corrected value, not just "fix it"
- After reporting, offer: "Run `/check-standards` again after fixes to verify"
```
