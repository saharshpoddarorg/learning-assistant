# Document Converter — Developer Guide

Full format matrix, routing architecture, and extension guide for the
[`document-converter`](SKILL.md) skill. For usage, start with `SKILL.md`; this file is for
understanding or extending the implementation.

## Full Format Matrix

Every extension below can convert to every other extension. Same-extension pairs are a
byte-for-byte copy.

> **Confirmed by testing: `.pdf` as a *source* (any row starting `.pdf` below) can hang
> Word COM automation indefinitely** — reproduced with a simple one-paragraph PDF, with
> Word both visible and invisible, and with zero conversion logic involved (a bare
> `Documents.Open()` on a `.pdf` hung the same way). This is Word's PDF-reflow feature,
> not a bug in this skill's code. `.pdf` as a *target* (writing a PDF) is unaffected and
> works reliably — see Operational Notes in `SKILL.md` for the required timeout guard and
> the recommendation to prefer `.docx`/`.html` sources when available.

| From \ To | `.docx`/`.doc`/`.rtf` | `.pdf` | `.html`/`.htm` | `.md`/`.markdown` | `.txt`/`.text`/`.log`/unknown |
|---|---|---|---|---|---|
| `.docx`/`.doc`/`.rtf` | Direct Word hop | Direct Word hop | Direct Word hop | Word object-model bridge | Direct Word hop |
| `.pdf` ⚠️ | Direct Word hop† | Direct Word hop† | Direct Word hop† | Word object-model bridge† | Direct Word hop† |
| `.html`/`.htm` | Direct Word hop | Direct Word hop | Direct Word hop | Word object-model bridge (or regex bridge with `-NoWord`) | Direct Word hop |
| `.md`/`.markdown` | Word object-model bridge | Word object-model bridge | Word object-model bridge (or regex bridge with `-NoWord`) | Copy | Verbatim copy (Markdown IS plain text) |
| `.txt`/`.text`/`.log`/unknown | Direct Word hop | Direct Word hop | Direct Word hop | Verbatim copy | Copy (plain-text passthrough, binary-content check) |

† Any row where `.pdf` is the **source** (left column) risks the hang above — always run
it inside a timeout-guarded job.

## Why Markdown, Not Always a Direct Hop

The open design question was: should every conversion go **format A → Markdown → format
B**, or should it go **directly A → B**? The answer implemented here is a hybrid, chosen
for maximum formatting fidelity:

1. **Word-openable formats among themselves** (`.docx`/`.doc`/`.rtf`/`.pdf`/`.html`/`.htm`/
   `.txt`) never touch Markdown. Word already understands all of them natively — opening
   one and calling `SaveAs2` on the other preserves layout, styles, and formatting better
   than any intermediate representation could. Introducing Markdown here would only lose
   fidelity (Markdown can't represent everything Word's rich formatting can).
2. **Markdown only becomes involved when Markdown is actually one of the two formats.**
   Word doesn't understand Markdown syntax (`#`, `**`, `-`) as formatting — it would just
   type the literal characters. So converting `.md` → `.docx` (or the reverse) needs a
   bridge that understands both sides structurally.

## The Word Document Object Model Bridge (primary path)

**File:** [`scripts/WordDocumentBridge.psm1`](scripts/WordDocumentBridge.psm1)

The first implementation of the Markdown bridge exported through Word's **Filtered HTML**
format and used a regex to translate HTML ↔ Markdown. This was abandoned: Word's HTML
export wraps everything in noisy MSO conditional comments and renders bullets as
symbol-font glyphs, which corrupted a regex bridge in testing (misplaced `**bold**`
markers, a bullet turning into a stray `�` character).

Instead, this bridge reads and writes Word's **Document object model directly**:

- **Word-openable format → Markdown** (`ConvertFrom-WordDocumentToMarkdown`): walks
  `Document.Paragraphs`, reading each paragraph's `Style.NameLocal` (`"Heading N"` →
  `#` × N), `ListFormat.ListString` (bulleted/numbered → `- `/`1. `), and each word's
  `Font.Bold`/`Font.Italic` to build `**bold**`/`*italic*` spans. `Document.Tables` are
  walked separately and interleaved into the output at the correct document position
  (paragraphs inside a table are skipped via `Range.Information(wdWithInTable)` to avoid
  duplicating their text).
- **Markdown → Word-openable format** (`New-WordDocumentFromMarkdown`): parses Markdown
  line-by-line and drives a new Word document via the `Selection` object — setting
  `Style` to `"Heading N"` for headings, `ListFormat.ApplyBulletDefault()`/
  `ApplyNumberDefault()` for lists, toggling `Font.Bold`/`Font.Italic` per inline span, and
  building real Word tables with `Document.Tables.Add()` for pipe tables.

This is used for **any** Word-openable extension ↔ Markdown, not just `.docx` — a `.pdf` or
`.html` file is opened into the same kind of Document object, so the same walker applies.

### Known limitations

- Links round-trip as literal `[text](url)` Markdown syntax typed into the Word document,
  not as native Word hyperlinks. Enhancing `Add-InlineMarkdownRun` to call
  `Selection.Hyperlinks.Add(...)` (and reading `Range.Hyperlinks` back out) would close
  this gap if a future need requires real clickable links.
- Inline formatting is detected per-word via `Range.Words` (one COM call per word), which
  is fine for appraisal-length documents but would be slow on very large documents.
- Nested/complex list structures (multi-level indentation) are flattened to a single level.

## The Regex HTML ↔ Markdown Bridge (`-NoWord` fallback)

**File:** [`scripts/HtmlMarkdownConvert.psm1`](scripts/HtmlMarkdownConvert.psm1)

A lightweight, dependency-free regex bridge between `.html`/`.htm` and `.md`/`.markdown`
only. Used when the `-NoWord` switch is passed (only meaningful for this specific pair).
Handles headings, bold/italic, inline code, fenced code blocks, ordered/unordered lists,
links, and simple pipe tables — good enough for clean, shallow HTML like typical
Notion/Google Keep/Confluence exports, but not a full CommonMark or HTML5 parser. Prefer
the Word-based bridge (the default) for maximum fidelity; use `-NoWord` only when avoiding
a Word dependency matters more than fidelity, or Word is not installed.

## Script Entry Point

**File:** [`scripts/convert-document.ps1`](scripts/convert-document.ps1)

Routing, in order:

1. Same extension in and out → byte-for-byte copy.
2. Both extensions in the Word-openable family (`word`/`pdf`/`html`/`text`) → direct Word
   `Open()` → `SaveAs2()` hop.
3. Markdown on one side, Word-openable on the other → the Word Document Object Model
   bridge (or the regex bridge for html↔md with `-NoWord`).
4. Markdown ↔ plain text (`.txt`/`.text`/`.log`/unknown) → verbatim copy (Markdown already
   *is* plain text — no information is lost).
5. Anything else → a clear "unsupported conversion" error, rather than guessing.

`Get-Family` classifies an extension into `word` / `pdf` / `html` / `markdown` / `text`.
Unknown/unusual extensions (Notepad++ files with no or non-standard extension) default to
`text`, with a binary-content check (`Copy-AsPlainText` throws if a null byte is found)
so a real binary file fails loudly instead of producing garbage output.

## Extending: Adding a New Format

1. **Identify what it really is.** Most "new formats" (a note-taking app export, a new
   editor) are actually one of the existing families under the hood. Check the App &
   Note-Taking Source Mapping table in `SKILL.md` first — you may not need any code change.
2. **If it's genuinely new** (e.g., a format Word can't open and isn't plain text or
   HTML), add it as its own family in `Get-Family` in `convert-document.ps1`, add a
   read/write function pair (mirroring `ConvertFrom-WordDocumentToMarkdown` /
   `New-WordDocumentFromMarkdown` or `ConvertTo-MarkdownFromHtml` /
   `ConvertTo-HtmlFromMarkdown`), and wire it into the routing `if`/`elseif` chain.
3. **Prefer Markdown as the bridge**, not a new direct pairwise converter — this keeps the
   number of converters linear in the number of formats (readers + writers) rather than
   quadratic (every pair).
4. **Test round-trips**, not just one direction: convert format → Markdown → format again
   and diff against the original structure (headings/bold/lists/tables), the way the fixes
   in this bridge were found (see Known Limitations above — these were caught by exactly
   this kind of round-trip test).

## Operational Notes

See `SKILL.md`'s Operational Notes for the timeout-guard pattern required around any
Word-launching conversion (Word COM automation can hang if a stray `WINWORD` process is
left over from a prior run).
