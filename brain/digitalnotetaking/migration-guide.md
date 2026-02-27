# Migration Guide — Moving Between Note-Taking Tools

> Step-by-step instructions for migrating between popular PKM tools.  
> Covers: Notion → Obsidian · Logseq → Obsidian · OneNote → Notion · Any → Markdown

---

## Before You Migrate

**Checklist (do this for any migration):**

- [ ] Export a full backup from the source tool TODAY
- [ ] Test the backup — spot-check 10 random notes
- [ ] Identify notes you care about (not everything needs migrating)
- [ ] Decide on your PARA structure in the target tool first
- [ ] Keep the source tool running for at least 2 weeks after migration (safety net)

---

## Notion → Obsidian

**Why people migrate:** Privacy, offline access, developer-friendly Markdown, Git sync.

### Step 1 — Export from Notion

```
Notion → Settings & members → Settings → Export content
Export format: Markdown & CSV
Include subpages: ✅
Create folders for subpages: ✅

Click Export → download the .zip file
```

### Step 2 — Use Obsidian's Built-in Importer

```
Obsidian → Community plugins → search "Importer" → install
Open the Importer panel
Select: Notion
Choose your exported .zip
Target folder: choose a staging folder (e.g., "Imports/")
Click Import
```

### Step 3 — Clean up

**What the importer handles well:**
- Headings, bold, italic, inline code, lists
- Images (embedded in folder)

**What needs manual fixing:**
- Notion databases → become Markdown tables or Dataview queries
- Notion relations/rollups → become plain text links
- Notion embeds (video, PDF) → become broken links (re-embed manually)
- Callout blocks → loose Markdown text (add `> [!NOTE]` manually)

**Fix linked databases with Dataview:**
```dataview
TABLE status, due, priority FROM "Projects"
WHERE type = "project" AND status != "done"
SORT due ASC
```

### Step 4 — Reorganise into PARA

Move imported notes from `Imports/` into the correct PARA folder. Don't try to keep Notion's hierarchy — Notion organises by subject, PARA organises by actionability.

---

## Logseq → Obsidian

**Why people migrate:** Obsidian has better mobile app, more themes, or you prefer non-outliner format.

**Good news:** Logseq stores notes as plain `.md` files, so migration is mostly mechanical.

### Step 1 — Locate your Logseq files

```
Logseq → ... menu → Open in Explorer (the vault folder)
Your notes are in:
  pages/      ← all named pages
  journals/   ← daily journal pages (YYYY_MM_DD.md format)
  assets/     ← images and attachments
```

### Step 2 — Copy to Obsidian vault

```powershell
# Windows — copy Logseq pages into Obsidian vault
Copy-Item -Path "C:\path\to\logseq\pages\*" -Destination "C:\path\to\obsidian-vault\Imports\" -Recurse
Copy-Item -Path "C:\path\to\logseq\journals\*" -Destination "C:\path\to\obsidian-vault\Journals\" -Recurse
```

### Step 3 — Handle Logseq-specific syntax

| Logseq syntax | Obsidian equivalent | Action |
|---|---|---|
| `[[page-name]]` | `[[page-name]]` | ✅ Works as-is |
| `#tag` | `#tag` | ✅ Works as-is |
| `- item` (bullet outliner) | Same | ✅ Works, but looks outliner-heavy |
| `{{query ...}}` | Dataview query | ❌ Manual rewrite |
| `[:div ...]` (advanced Hiccup) | ❌ No equivalent | Delete or convert |
| `DONE`, `TODO`, `LATER`, `NOW` | `- [x]`, `- [ ]` | Manual replace |

**Batch-fix TODO keywords (PowerShell):**
```powershell
# Replace Logseq task markers with Obsidian checkbox style
Get-ChildItem "C:\obsidian\Imports\" -Recurse -Filter "*.md" | ForEach-Object {
    (Get-Content $_.FullName) -replace "^- DONE ", "- [x] " `
                              -replace "^- TODO ", "- [ ] " `
                              -replace "^- LATER ", "- [ ] " `
                              -replace "^- NOW ", "- [ ] " |
    Set-Content $_.FullName
}
```

### Step 4 — Create PARA structure and sort

Same as the Notion migration: move files from `Imports/` into Projects/Areas/Resources/Archives.

---

## OneNote → Notion

**Why people migrate:** Better cross-platform support, databases, sharing.

### Step 1 — Export from OneNote to Word

```
OneNote (Desktop) → File → Export
Export as: Word (.docx)
Export all: entire notebook
```

### Step 2 — Convert Word → Markdown (via Pandoc)

```powershell
# Install Pandoc: https://pandoc.org/installing.html
# or via winget:
winget install JohnMacFarlane.Pandoc

# Convert a single .docx to Markdown
pandoc "my-note.docx" -o "my-note.md"

# Batch convert all .docx files in a folder
Get-ChildItem ".\onenote-export\" -Filter "*.docx" | ForEach-Object {
    pandoc $_.FullName -o "$($_.BaseName).md"
}
```

### Step 3 — Import Markdown into Notion

```
Notion → sidebar ... → Import → Markdown & CSV
Select your .md files
Notion creates pages from them
```

### Step 4 — Rebuild structure

OneNote's notebook → section → page hierarchy will need manual reorganisation into PARA Notion pages/databases.

---

## Any Tool → Plain Markdown

**goal:** Get your notes as plain `.md` files that work anywhere.

| Source tool | Export path |
|---|---|
| **Notion** | Settings → Export → Markdown & CSV |
| **Logseq** | Files are already Markdown in your vault folder |
| **OneNote** | OneNote → Word → Pandoc → Markdown |
| **Evernote** | File → Export → .enex → use [evernote2md](https://github.com/wormi4ok/evernote2md) |
| **Bear** | File → Export Notes → Markdown |
| **Apple Notes** | No official export — use [apple-notes-to-obsidian](https://github.com/nickvdyck/apple-notes-to-obsidian) |
| **Google Docs** | File → Download → Plain Text (loses formatting) or use Docs to Markdown addon |
| **Confluence** | Space Tools → Export Space → HTML → Pandoc |

---

## After Migration — Health Check

Run through this checklist 1 week after migrating:

- [ ] Can I find a note I wrote 3 months ago in under 30 seconds?
- [ ] Can I capture a new note in under 60 seconds?
- [ ] Are images / attachments loading correctly?
- [ ] Is sync working across all my devices?
- [ ] Did I keep my old tool's backup somewhere safe?
- [ ] Are my templates working in the new tool?
- [ ] Have I set up the PARA structure and can I instantly file new content?

---

## Copilot-Assisted Migration

Use Copilot to help with any step:

```
/digital-notetaking → migration → notion-to-obsidian → advanced
/digital-notetaking → migration → onenote-to-notion → intermediate
```

---

*Related: [tools-comparison.md](tools-comparison.md) · [para-method.md](para-method.md) · [START-HERE.md](START-HERE.md)*  
*Vault resources: `/resources → search → migration`*
