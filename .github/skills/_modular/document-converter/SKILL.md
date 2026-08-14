---
name: document-converter
description: >
  Converts documents and notes between formats — Word (docx/doc/rtf), PDF, HTML, Markdown,
  and plain text — preserving headings, bold/italic, lists, links, and tables as much as
  each format allows. Covers exports from note-taking and docs apps (Notion, Google Keep,
  Apple Notes, Confluence, Google Docs, Notepad++) by handling the real file format each
  one exports to. Use whenever a skill needs a document's content in a different format,
  not only "convert to plain text."
---

# Document Converter

## Purpose

Convert a document from any supported format to any other supported format, preserving as
much structure (headings, bold/italic, lists, links, tables) as the source and target
formats allow. This is a general-purpose format converter — not a "convert to plain text"
tool — usable by any skill that needs a document's content in a different shape, including
[`self-appraisal`](../self-appraisal/SKILL.md) Phase 3, which uses it to read past `.docx`/
`.pdf` appraisal submissions as Markdown.

No pandoc, no Python, no network calls, no upload of file contents anywhere. Everything
runs locally via PowerShell and (for Word-family formats) Microsoft Word COM automation.

## Supported Formats

| Extension | Family | Notes |
|---|---|---|
| `.docx`, `.doc` | Word | Native Word formats |
| `.rtf` | Word | Rich Text Format, opened/saved by Word |
| `.pdf` | PDF | Word 2013+ opens PDFs (converts to an editable layout); no OCR for scanned/image PDFs |
| `.html`, `.htm` | HTML | Opened/saved by Word, or bridged directly with `-NoWord` |
| `.md`, `.markdown` | Markdown | The structural "source of truth" format for cross-format editing |
| `.txt`, `.text`, `.log` | Plain text | No structure to preserve — passthrough or direct Word open |
| Any other/unknown extension | Plain text | Covers Notepad++ files, which have no special format regardless of extension |

Any pairing of the above is supported directly. See
[`README.md`](README.md) in this folder for the full format matrix, the routing
architecture, and how each note-taking/docs app maps onto these formats.

## App & Note-Taking Source Mapping

These apps don't have their own convertible file format — export to one of the formats
above first, then convert:

| Source | How to get a convertible file |
|---|---|
| Notion | Page menu → *Export* → Markdown & CSV, HTML, or PDF |
| Google Keep | Google Takeout → Keep → each note exports as `.html` |
| Apple Notes | *File → Export as PDF*, or copy/paste into a `.txt` file |
| Confluence | Page → *Export to PDF/Word*, or export the space to HTML |
| Google Docs | *File → Download* → Word (`.docx`), PDF, plain text, web page (`.html`), or Markdown |
| Notepad++ | Whatever it's editing is already plain text — any extension works as source |

## Requirements

- Windows with Microsoft Word installed — needed for `.docx`/`.doc`/`.rtf`/`.pdf`/`.html`
  conversions and for any conversion involving Markdown (Word's Document object model is
  the bridge — see [`README.md`](README.md)).
- Plain-text-only conversions (`.txt`/`.md`/unknown ↔ `.txt`/`.md`/unknown) need no Word.
- Nothing is installed or downloaded for any format.

## Script

Use [`scripts/convert-document.ps1`](scripts/convert-document.ps1):

```powershell
.\scripts\convert-document.ps1 -InputPath <source-file> -OutputPath <target-file>
.\scripts\convert-document.ps1 -InputPath <source-file> -OutputPath <target-file> -TargetFormat pdf
.\scripts\convert-document.ps1 -InputPath fixture.html -OutputPath fixture.md -NoWord
```

The target format is inferred from `-OutputPath`'s extension unless `-TargetFormat` is
given. See [`README.md`](README.md) for the full routing logic (direct Word hop vs. the
Markdown/Word-object-model bridge vs. the no-Word regex bridge).

## Operational Notes

- **Run with a timeout guard — mandatory for any PDF source.** Opening a `.pdf` via Word
  COM automation was confirmed, through repeated testing, to **hang indefinitely** in
  headless/automated contexts — even a simple one-paragraph PDF, with `Visible` both
  `$true` and `$false`. Word's PDF-reflow conversion appears to require an interaction that
  never resolves under automation, and it is not a bug in this script's code (verified by
  opening the same PDF with zero conversion logic involved). Always run any `.pdf`-source
  conversion inside a background job with a bounded wait — never call it directly and
  block, and treat PDF input as **higher risk** than every other source format:

  ```powershell
  $job = Start-Job -ScriptBlock { param($s,$i,$o) & $s -InputPath $i -OutputPath $o } `
      -ArgumentList (Resolve-Path .\scripts\convert-document.ps1).Path, $InputPath, $OutputPath
  if (Wait-Job $job -Timeout 30) { Receive-Job $job } else { Stop-Job $job; "TIMED OUT" }
  Remove-Job $job -Force
  ```

- **If a `.pdf`-source run times out**, stop the lingering `WINWORD` process (it will not
  finish on its own): `Get-Process WINWORD -ErrorAction SilentlyContinue | Stop-Process -Force`.
  Then prefer a non-PDF source instead of retrying — see below.
- **Prefer `.docx`/`.html` over `.pdf` as a source whenever you have the choice.** Every
  app in the App & Note-Taking Source Mapping table above offers a non-PDF export option
  (Word/Google Docs → `.docx`; Notion → Markdown or HTML; Confluence → Word or HTML).
  `.pdf` only works reliably here as a **target** (writing a PDF from `.docx`/`.md`/etc.
  is fine — that direction never opens a PDF, only creates one).
- **Scanned/image PDFs** will not yield usable text even if the hang above is worked
  around (Word has no OCR). Flag this to the developer rather than returning near-empty
  output silently.
- **Known fidelity limits** (see [`README.md`](README.md) for detail): links round-trip as
  literal `[text](url)` Markdown syntax rather than becoming native Word hyperlinks; very
  large documents are slower to walk (Word COM is called per-word for inline formatting).

## Guardrails

- Local, offline conversion only — never send file contents to a remote service.
- Do not modify or overwrite the original source file; only ever write to `-OutputPath`.
- Treat both the source file and the converted output with the same sensitivity as the
  original document (see the calling skill's own guardrails, e.g. self-appraisal's private
  reference-document handling).

## Three-Tier Usage

### Newbie

Run the script once per file, pointing `-OutputPath` at the target file with the desired
extension, inside a timeout-guarded job.

### Amateur

Batch-convert a folder of source documents by iterating file paths and calling the script
per file, keeping the timeout guard and cleaning up any stray `WINWORD` process between
files if one run hangs.

### Pro

Wrap the timeout-guarded call in a loop over a references folder (e.g.
`self-appraisal/references/private/raw/<category>/*.docx`), writing each output beside the
matching `converted/<category>/` path as Markdown, and surface a clear failure per file
(scanned PDF, corrupt file, hang) rather than aborting the whole batch. For a new source
app not yet covered, first identify its real export format from the App & Note-Taking
Source Mapping table above, then treat it as that format.
