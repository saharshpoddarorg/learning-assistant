---
applyTo: "**"
---

# Markdown Formatting — Always-On Style Rules

> **Purpose:** Define mandatory Markdown formatting conventions for every `.md` file in
> this repo. Applies to all files via `applyTo: "**"`.
>
> **When active:** Always — whenever you create or edit any Markdown content you MUST
> follow all rules in this file. No explicit reminder is needed.
>
> **Enforcement:** Run `.\__md_lint.ps1` before every commit. Zero issues = ready to commit.

---

## The Golden Formatting Rule

> **Every Markdown file must render correctly AND look clean in raw form.**
> Both the rendered view (GitHub, VS Code Preview) and the raw text view must be consistent,
> structured, and professional. Poor raw formatting makes collaboration harder.

---

## 1. Headings

### 1.1 Blank Lines

Every heading (`#`, `##`, `###`, `####`) MUST be surrounded by blank lines on both sides
(except at the very start of the file where a leading blank line is not needed).

**Correct:**

```markdown
Some paragraph text.

## My Heading

More content follows here.
```

**Wrong (missing blank lines):**

```markdown
Some paragraph text.
## My Heading
More content follows here.
```

### 1.2 Heading Hierarchy

Never skip heading levels. Descend one level at a time.

| Allowed | Not Allowed |
|---|---|
| H1 → H2 → H3 | H1 → H3 (skips H2) |
| H2 → H3 → H4 | H2 → H4 (skips H3) |

### 1.3 Single H1 Per File

Each file should have exactly **one H1** (`# Title`), placed at the top. All other
sections use H2–H4.

---

## 2. Code Blocks

### 2.1 Blank Lines Around Fences

Every triple-backtick code fence MUST have a blank line immediately before the opening
fence and immediately after the closing fence.

**Correct:**

```markdown
Here is the command:

```powershell

.\build.ps1

```text

And the next paragraph continues here.
```

**Wrong:**

```markdown
Here is the command:
```powershell
.\build.ps1
```text
And the next paragraph continues here.
```

### 2.2 Language Tags

Always specify the language after the opening fence (`powershell`, `java`, `bash`,
`markdown`, `json`, `yaml`, `text`, etc.). Never use a bare ` ``` ` without a lang tag
unless the content truly has no language (e.g., a file tree displayed as `text`).

### 2.3 No Empty Code Blocks

Do not leave empty code fences (open and immediately closed with nothing between them).
Either fill them or remove them.

---

## 3. Lists

### 3.1 Consistent Markers

Within a single list block (no blank lines between items), use only **one** marker type.
Do NOT mix `-` and `*` in the same list.

**Allowed:**

```markdown
- item one
- item two
- item three
```

**Not Allowed:**

```markdown
- item one
* item two    ← wrong: mixing markers
- item three
```

### 3.2 Ordered Lists

Use sequential numbers for ordered lists: `1.`, `2.`, `3.` — NOT all `1.` (even though
Markdown renders it correctly, raw readability suffers).

**Correct:**

```markdown
1. First step
2. Second step
3. Third step
```

**Wrong:**

```markdown
1. First step
1. Second step   ← not sequential
1. Third step
```

### 3.3 Blank Lines Around Lists

Add a blank line before a list when it follows a paragraph or heading. Add a blank line
after a list when content follows it.

### 3.4 Nested List Indentation

Use **2 spaces** per nesting level for nested lists.

---

## 4. Tables

### 4.1 Required Structure

Every Markdown table must have:

1. A header row
2. A separator row with `---` in each column (optionally left-aligned `|---|`, right `|---:|`, center `|:---:|`)
3. At least one data row

```markdown
| Column A | Column B | Column C |
|---|---|---|
| value 1 | value 2 | value 3 |
```

### 4.2 Pipes and Spacing

- Every row begins and ends with `|`
- At least one space between `|` and cell content: `| text |` not `|text|`
- The separator row uses only `---` with optional `:` for alignment — no other content

### 4.3 Column Alignment

Keep column widths consistent across rows for readability in raw form. Use padding spaces
to align content when tables are short (≤ 15 rows). For long tables it is acceptable to
let columns be uneven.

**Well-aligned (preferred for small tables):**

```markdown
| Tool       | Purpose              | Command          |
|------------|----------------------|------------------|
| Maven      | Java build tool      | `mvn package`    |
| Gradle     | Java build tool      | `gradle build`   |
| npm        | Node package manager | `npm install`    |
```

**Minimally valid (acceptable for large tables):**

```markdown
| Tool | Purpose | Command |
|---|---|---|
| Maven | Java build tool | `mvn package` |
```

### 4.4 No Orphan Separator Rows

A `|---|---|` separator row must always be preceded by a header row. Never place a
separator row as the first or last line of a table block.

---

## 5. Horizontal Rules

Use `---` (three hyphens on their own line) for horizontal rules. Always surround with
blank lines.

```markdown
Previous section content.

---

Next section content.
```

Do NOT use `***` or `___` as horizontal rules (inconsistent rendering).

---

## 6. Links

### 6.1 No Space Between Brackets and Parentheses

```markdown
[link text](url)   ← correct
[link text] (url)  ← wrong: space before (
```

### 6.2 Descriptive Link Text

Use descriptive text, never bare URLs as link text where a label is appropriate.

```markdown
[Conventional Commits specification](https://www.conventionalcommits.org/)  ← correct
[https://www.conventionalcommits.org/](https://www.conventionalcommits.org/)  ← avoid
```

### 6.3 Internal File Links

When linking to other `.md` files within the repo, use relative paths.

```markdown
See [Architecture Overview](../docs/architecture-overview.md) for details.
```

---

## 7. Emphasis and Inline Formatting

- Use `**bold**` for important terms, UI labels, or key callouts
- Use `*italic*` for titles of external works, new term introductions, or light emphasis
- Use `` `backtick` `` for: file names, code symbols, command names, tool names, key paths
- Do NOT use UPPERCASE for emphasis — use bold instead
- Do NOT nest bold inside italic or vice versa unless absolutely necessary

---

## 8. Whitespace Rules

### 8.1 Trailing Spaces

Every line must end without trailing whitespace. No exceptions.

Run `.\__md_lint.ps1` to auto-strip trailing spaces.

### 8.2 Multiple Blank Lines

Never more than **one consecutive blank line** between content blocks (two blank lines max
is acceptable only inside code blocks or when creating visual separation in very long
documents — never three or more in a row in regular content).

### 8.3 End of File

Every `.md` file must end with **exactly one newline** (a single `\n` after the last
content line). No trailing blank lines before EOF. No missing final newline.

---

## 9. YAML Frontmatter (instruction and prompt files)

When a file begins with YAML frontmatter (used in `.instructions.md` and `.prompt.md`
files), the block must:

1. Start on line 1 with `---`
2. End with `---` on its own line
3. Be followed immediately by a blank line before the first Markdown heading

```markdown
---
applyTo: "**"
---

# My Title
```

---

## 10. Pre-Commit Formatting Checklist

Before committing ANY change that touches `.md` files, verify:

- [ ] `.\__md_lint.ps1` reports **0 issues** (run from repo root)
- [ ] All headings have blank lines before and after
- [ ] All code fences have blank lines before and after, with a language tag
- [ ] No trailing spaces on any line
- [ ] No heading level jumps (H1→H3, H2→H4, etc.)
- [ ] Only one H1 per file
- [ ] All tables have a header row + separator row, every row starts/ends with `|`
- [ ] No mixed list markers within a single list block
- [ ] Sequential numbers on ordered lists
- [ ] All links use `[text](url)` with no space between `]` and `(`
- [ ] File ends with exactly one newline

---

## Quick Reference Card

```text
HEADING     .   blank line BEFORE and AFTER every heading
CODE FENCE  .   blank line BEFORE opening ``` and AFTER closing ```
            .   always include a language tag after ```
TRAILING WS .   no spaces at end of any line
BLANK LINES .   max 1 consecutive blank line (2 max for rare separation)
EOF         .   exactly 1 newline at end of file
TABLES      .   | header | → | --- | → | data |  — every row has leading/trailing |
LISTS       .   one marker type per block  (- or *, not both)
            .   ordered: 1. 2. 3.  (not 1. 1. 1.)
HEADINGS    .   one H1 per file, no level skips
LINKS       .   [text](url)  — no space before (
```

---

## Enforcement

```powershell
# Check and fix all .md files in the repo (run from repo root)
.\__md_lint.ps1

# Dry-run to see what would change without writing
.\__md_lint.ps1 -Check

# Check a single file
.\__md_lint.ps1 -Target "README.md"
```

The script auto-fixes:
trailing spaces · blank lines around headings · blank lines around code fences ·
3+ consecutive blank lines → 2 · missing final newline

The script reports but does NOT auto-fix (requires human judgment):
heading level jumps · mixed list markers · missing language tags · broken links ·
table structure issues · multiple H1s
