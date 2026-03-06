---
applyTo: "**"
---

# Markdown Formatting Guide

> **Audience:** All contributors (human and AI) working in this repo.
> **Purpose:** Define the canonical formatting conventions for every `.md` file
> and explain what the `__md_lint.ps1` linter checks and fixes.

---

## Why Formatting Matters

Raw Markdown that looks bad in a text editor creates friction for:

- **Code reviews** — reviewers read raw diffs, not rendered output
- **AI context loading** — Copilot reads raw `.md` files; malformed structure creates
  context ambiguity
- **Documentation maintenance** — consistent structure makes it easier to find and update
  content months later

The rules here are enforced automatically by `__md_lint.ps1` and by the
`.github/instructions/md-formatting.instructions.md` always-on instruction file.

---

## Rule Summary

| Category | Rule | Auto-fixed? |
|---|---|---|
| **Trailing whitespace** | No spaces at end of any line | ✅ Yes |
| **Heading spacing** | Blank line before AND after every heading | ✅ Yes |
| **Code fence spacing** | Blank line before opening ` ``` ` and after closing ` ``` ` | ✅ Yes |
| **Code fence language** | Always specify language after ` ``` ` | ⚠️ Reported only |
| **Consecutive blank lines** | Max 1 consecutive blank line in regular content | ✅ Yes |
| **End of file** | Exactly one `\n` after last content line | ✅ Yes |
| **Heading hierarchy** | No level skips (H1→H2→H3, not H1→H3) | ⚠️ Reported only |
| **Single H1** | One `# Title` per file | ⚠️ Reported only |
| **List markers** | Use only `-` or only `*` within one list block | ⚠️ Reported only |
| **Ordered list numbering** | 1. 2. 3. — not 1. 1. 1. | ⚠️ Reported only |
| **Table structure** | Header row + separator row + data rows; every row has `\|` | ⚠️ Reported only |
| **Table pipe spacing** | At least one space between `\|` and cell text | ⚠️ Reported only |
| **Link format** | `[text](url)` — no space between `]` and `(` | ⚠️ Reported only |
| **YAML frontmatter** | Starts line 1 with `---`, ends `---`, blank line before first heading | ⚠️ Reported only |

---

## Heading Rules in Detail

### Spacing

```markdown
<!-- ✅ correct -->
Some paragraph.

## My Section

More content.

<!-- ❌ wrong — no blank lines -->
Some paragraph.
## My Section
More content.
```

### Hierarchy — No Level Skips

The heading level may only increase by 1 at a time:

```markdown
<!-- ✅ correct -->
# Document Title

## Section

### Subsection

#### Detail

<!-- ❌ wrong — jumps from H1 to H3 -->
# Document Title

### Subsection   ← skipped H2
```

### One H1 Per File

```markdown
<!-- ✅ correct — one H1 at top -->
# My Document

## Section A

## Section B

<!-- ❌ wrong — two H1s -->
# My Document

# Another Title   ← second H1
```

---

## Code Block Rules in Detail

### Blank Lines Around Fences

```markdown
<!-- ✅ correct -->
Run this command:

```bash

./build.sh

```text

The output will appear in the terminal.

<!-- ❌ wrong — no blank lines -->
Run this command:
```bash

./build.sh

```text
The output will appear in the terminal.
```

### Language Tags

Always specify a language:

```markdown
<!-- ✅ correct -->
```java

public class Foo {}

```xml

<!-- ❌ wrong — no language tag -->
```

public class Foo {}

```text
```

Common language identifiers used in this project:

| Content | Tag |
|---|---|
| Java source | `java` |
| PowerShell | `powershell` |
| Bash / sh | `bash` |
| JSON | `json` |
| YAML | `yaml` |
| Markdown | `markdown` |
| Plain text / file trees | `text` |
| Properties files | `properties` |
| SQL | `sql` |

---

## Table Rules in Detail

### Required Structure

```markdown
| Column A | Column B |
|---|---|
| value 1  | value 2  |
```

Every table must have:

1. A **header row** as the first row
2. A **separator row** immediately after the header (only `---`, `|`, and optional `:`)
3. At least one **data row**

### Alignment Specifiers (optional)

```markdown
| Left | Centre | Right |
|:---|:---:|---:|
| aaa | bbb | ccc |
```

### Pipe Spacing

```markdown
<!-- ✅ correct — space after and before | -->
| name | value |
|---|---|
| foo  | bar   |

<!-- ❌ wrong — no space -->
|name|value|
|---|---|
|foo|bar|
```

---

## List Rules in Detail

### Consistent Markers

```markdown
<!-- ✅ correct — only dashes -->
- item one
- item two
- item three

<!-- ❌ wrong — mixed markers -->
- item one
* item two
- item three
```

### Sequential Ordered Lists

```markdown
<!-- ✅ correct -->
1. First
2. Second
3. Third

<!-- ❌ wrong — all 1. -->
1. First
1. Second
1. Third
```

### Nested Lists (2-space indent)

```markdown
- Top-level item
  - Nested item (2 spaces)
    - Double-nested (4 spaces)
  - Another nested item
- Another top-level
```

---

## Whitespace Rules in Detail

### Trailing Spaces

Trailing spaces are invisible in most editors but cause `git diff` noise and confuse some
Markdown renderers. The linter auto-strips them.

Run `.\__md_lint.ps1` before any commit that touches `.md` files.

### Multiple Blank Lines

```markdown
<!-- ✅ correct — single blank line separates blocks -->
Paragraph one.

Paragraph two.

<!-- ❌ wrong — three consecutive blank lines -->
Paragraph one.

Paragraph two.
```

### End of File

The file must end with exactly one newline character after the last line of content:

```text
Last content line\n   ← correct: one newline
Last content line\n\n ← wrong: two newlines (trailing blank line)
Last content line     ← wrong: no newline
```

---

## YAML Frontmatter Rules

Instruction files (`.instructions.md`) and prompt files (`.prompt.md`) use YAML frontmatter:

```markdown
---
applyTo: "**"
---

# Title of the File

Content starts here.
```

Rules:

- `---` on line 1, exactly
- `---` closing delimiter on its own line
- **Blank line** between closing `---` and the first `#` heading
- No Markdown content above the opening `---`

---

## `__md_lint.ps1` — The Linting Script

Located at the **repo root**: `.\__md_lint.ps1`

### Usage

```powershell
# Fix all .md files in the repo (default)
.\__md_lint.ps1

# Check only — report issues without making changes
.\__md_lint.ps1 -Check

# Fix or check a single file
.\__md_lint.ps1 -Target "README.md"
.\__md_lint.ps1 -Target "README.md" -Check
```

### What It Auto-Fixes

| Issue | Fix Applied |
|---|---|
| Trailing whitespace | Stripped from every line |
| Missing blank line before heading | Blank line inserted |
| Missing blank line after heading | Blank line inserted |
| Missing blank line before opening code fence | Blank line inserted |
| Missing blank line after closing code fence | Blank line inserted |
| 3+ consecutive blank lines | Collapsed to 1 |
| Missing final newline | Single `\n` appended |

### What It Reports Only (Requires Human Fix)

| Issue | Why Not Auto-Fixed |
|---|---|
| Heading level skip (H1→H3) | Requires semantic decision about document structure |
| Multiple H1 in one file | Requires deciding which is the canonical title |
| Mixed list markers | Requires deciding which marker is intended |
| Non-sequential ordered list | May be intentional in some reference docs |
| Missing code fence language tag | Requires knowing the content's language |
| Broken link (space in `] (`) | Requires knowing if URL or anchor is correct |
| Table structure (missing header/separator) | Requires knowing intended content |

### Exit Codes

| Code | Meaning |
|---|---|
| `0` | All auto-fixable issues resolved; no remaining issues |
| `1` | Issues remain that require human attention (listed in output) |

---

## Integration with Change Completeness

The formatting check is part of the **Section G — Pre-Commit Formatting Gates** in
`.github/instructions/change-completeness.instructions.md`.

It applies to **every change type** (A through F). You do not need to invoke it
explicitly — it is part of the always-on universal checklist.

---

## Quick Reference

```text
RULE                   FIX
─────────────────────────────────────────────────────────────
Heading blank lines    Add blank line before AND after # heading
Code fence blank lines Add blank line before ``` and after closing ```
Trailing spaces        Strip, or run .\__md_lint.ps1
Multiple blank lines   Collapse 3+ blanks → 1 blank line
End of file            Exactly one \n after last content line
Heading hierarchy      H1→H2→H3→H4, no skips
Single H1              Only one # Title per file
Table structure        | header | → | --- | → | data |
List consistency       One marker type (- or *) per list block
Ordered list numbers   1. 2. 3. not 1. 1. 1.
Link format            [text](url) — no space before (
```
