# ai-brain Integration — Linking AI Sessions to Your PKM

> **Purpose:** How to turn Copilot sessions, MCP tool outputs, and AI-generated content
> into structured, searchable, permanent knowledge in your digital note-taking system.
> **Quick nav:** [README](README.md) · [CODE Method](code-method.md) · [PARA Method](para-method.md) · [Templates](templates.md)

---

## Overview

The `brain/ai-brain/` workspace bridges two worlds:

```
AI Session (Copilot Chat / MCP tools)
        ↓  Copilot generates output
    brain/ai-brain/inbox/         ← temporary capture (gitignored)
        ↓  You review and promote
    brain/ai-brain/notes/         ← notes on this machine (gitignored)
        ↓  Worth keeping permanently?
    brain/ai-brain/archive/       ← committed to repo ← connects to your PKM
        ↓
  /digital-notetaking tools       ← Obsidian, Notion, Logseq, etc.
```

---

## Session Lifecycle — Step by Step

### At the start of a session

```powershell
# 1. Check what's left from last session
brain status

# 2. Open a session note to log what you're about to do
brain new --tier inbox --kind session --project mcp-servers --title "SESSION: <brief topic>"
```

The session note becomes your running log. Edit it throughout the session.

### During a session

Every time Copilot produces something worth keeping, either:

**Option A: Paste directly into the open session note**
```markdown
## 2026-02-27 — Java generics, ? super T

### What Copilot explained
[paste the useful part]

### My understanding
[your summary in 1-3 sentences]

### Questions / follow-up
- [ ] Try this with Comparable
```

**Option B: Create a separate note per insight**
```powershell
# Quick capture, don't stop your flow
brain new --tier inbox --kind snippet --project java --title "Lower bounded wildcard example"
```

### At the end of a session

```powershell
# 1. Check what you created
brain status
brain list --tier inbox

# 2. Review each note
#    - One-liner summary:  keep in notes/
#    - Permanent insight:  publish to archive/
#    - Pure scratch:       clear

# Publish a session note
brain publish brain\ai-brain\inbox\2026-02-27_session-java-generics.md --project java

# Move a note to notes/ (doesn't commit)
brain move brain\ai-brain\inbox\2026-02-27_snippet.md --tier notes

# Clear scratch
brain clear --force
```

---

## Frontmatter for AI-Sourced Content

Use `source: copilot` to track where notes came from:

```markdown
---
date: 2026-02-27
kind: session          # or note, snippet, decision, resource, ref
project: mcp-servers
tags: [java, generics, wildcards, copilot-session]
status: draft
source: copilot        # ← marks this as AI-assisted content
---

# SESSION: Java Generics Deep Dive — 2026-02-27

## Summary


## Key insights
- 

## Follow-up items
- [ ] 
```

All `kind` values: `note` | `decision` | `session` | `resource` | `snippet` | `ref`
All `source` values: `copilot` | `manual` | `mcp`

---

## Copilot Slash Commands for ai-brain

| Command | When to use | Example |
|---|---|---|
| `/brain-new` | Create a structured note from a topic | `/brain-new → "Java generics session" → notes → java` |
| `/brain-publish` | Promote a note to archive/ and commit | `/brain-publish → inbox/2026-02-27_draft.md → java` |
| `/brain-search` | Find notes by tag, project, kind, date | `/brain-search → "generics" → tag=java` |
| `/brain-capture-session` | Convert session content into a publishable note | `/brain-capture-session` |

---

## `/brain-capture-session` — Session → Note

The `/brain-capture-session` slash command helps you convert a Copilot session's
output into a clean, publishable session note. It:

1. Reads the current session context
2. Scaffolds a session note with `kind: session` frontmatter
3. Organizes the key outputs into sections: Summary / Key Insights / Code Snippets / Next Steps
4. Creates the file in `brain/ai-brain/inbox/`
5. Suggests tags and asks for your confirmation

```
Usage:  /brain-capture-session
Then:   Review the created file
Then:   brain publish <file> --project <bucket>
```

---

## Linking ai-brain to Your External PKM Tool

### Obsidian

```
Option 1 — Open brain/ai-brain/ as a vault folder:
  1. Obsidian → Open Folder as Vault → pick brain/ai-brain/
  2. Notes in inbox/ and notes/ appear inside Obsidian
  3. archive/ appears as your committed reference library
  Note: inbox/ and notes/ are gitignored → stay on your machine only

Option 2 — Sync archive/ to your Obsidian vault:
  1. After brain publish, copy the archive/ file to your Obsidian vault's Resources folder
  2. Or use Obsidian Sync / iCloud if your vault is in a synced folder
```

### Notion

```
1. Create a "Dev Sessions" database in Notion
2. After brain publish, create a new page in that database
3. Paste the note content — frontmatter fields → Notion properties
   date → Date property
   project → Select property
   tags → Multi-select property
   kind → Select property (note/session/decision/snippet/resource/ref)
4. Link back to the git commit URL for traceability
```

### Logseq

```
1. Open brain/ai-brain/ as a Logseq graph (File → Open Folder)
2. Logseq reads all .md files — inbox/, notes/, archive/ become pages
3. Use [[]] links to connect session notes to concept pages
4. Add :source: copilot as a property to AI-generated pages
```

---

## VS Code Tasks for Session Capture

From the Command Palette (`Ctrl+Shift+P` → *Tasks: Run Task*):

| Task | What it does |
|---|---|
| `brain: new note` | Interactive note creation with prompts |
| `brain: new note (notes tier)` | Create directly in notes/ |
| `brain: publish note` | Promote a file to archive/ + commit |
| `brain: search notes` | Search across all tiers |
| `brain: status` | Quick tier summary |
| `brain: list notes` | List notes/ contents |

---

## MCP Integration

When using the Learning Resources MCP server during a session:

```powershell
# After exploring/searching resources via MCP, capture what you found
brain new --tier inbox --kind resource --project java --title "Resources on Java virtual threads"

# Paste the MCP search results into the note
# Then distill: which 3 links are most useful? Write 1 sentence per link.
# Then publish if it's a keeper.
brain publish brain\ai-brain\inbox\YYYY-MM-DD_resources-java-virtual-threads.md --project java
```

---

## Integration with NoteTemplate.java

The `digitalnotetaking` Java package includes a `NoteTemplate` class that generates
markdown notes programmatically — matching the same frontmatter schema used by the
brain workspace:

```java
// In brain/src/digitalnotetaking/NoteTemplate.java
NoteMetadata meta = new NoteMetadata.Builder()
        .kind(NoteKind.SESSION)
        .project("mcp-servers")
        .tags(List.of("java", "generics", "copilot-session"))
        .status(NoteStatus.DRAFT)
        .build();

String note = NoteTemplate.generate(meta, "Java Generics Deep Dive");
// Returns a complete markdown note with frontmatter + body sections
```

This Java API mirrors exactly what `brain new` does at the script level. As the
`digitalnotetaking` package grows, it will expose:
- Note validation (required fields, valid tag format)
- Note promotion logic (inbox → notes → archive)
- Template rendering for additional kinds

---

## Session Note Naming Convention

```
YYYY-MM-DD_session-<brief-topic>.md       ← most session notes
YYYY-MM-DD_<project>-<topic>.md           ← when project matters
<topic-slug>.md                            ← timeless reference notes
```

The `brain new` command auto-generates the filename — you just supply the title.

---

## Best Practices

1. **One session note per working session** — not per concept. One session may contain
   multiple sub-topics. Create child notes for specific insights.

2. **Always add `source: copilot`** when the core content is AI-generated. This helps
   you distinguish your own thinking from AI output when reviewing months later.

3. **Distill before publishing**. A session note should have a written TL;DR section
   before it's promoted to `archive/`. Raw AI output alone is not a good archive entry.

4. **Use `kind: decision` for architectural choices**. Even a small "I chose HashMap over
   TreeMap because..." is worth an ADR-format note.

5. **Link back to sessions from code**. Add a comment in the relevant source file:
   ```java
   // See brain/ai-brain/archive/java/2026-02/2026-02-27_generics-session.md
   ```

6. **Review inbox at the end of every session** — never let it accumulate over multiple days.
