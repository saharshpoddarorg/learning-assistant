# START HERE — Digital Note-Taking for Developers

> Pick your path below based on where you are right now.

---

## Path A — "I have no system at all"

**Time needed:** 15 minutes to set up + 1 day to get the habit going.

### Step 1 — Pick a tool

| Your situation | Recommended tool |
|---|---|
| Windows, in the Microsoft ecosystem | **OneNote** (already have it) *or* **Obsidian** |
| macOS, privacy matters, developer | **Obsidian** |
| Team wiki / collaboration / iOS/Android | **Notion** |
| Open-source, version-controlled notes | **Logseq** |
| Don't know yet | **Obsidian** — it's free, local, and you can always export |

> **Bias toward Obsidian** for software engineers: your notes stay on your machine,
> they are plain Markdown files, you can version-control them with Git, and there is
> an enormous plugin ecosystem.

### Step 2 — Install

**Obsidian (Windows / macOS / Linux)**

```text
1. Download: https://obsidian.md/download
2. Run installer
3. Click "Create new vault" → choose a folder that is synced (Dropbox, iCloud, etc.)
   OR a regular folder if you plan to use Git sync
4. Done. Open the vault.
```

**Notion**

```text
1. Sign up: https://notion.so
2. Use the "Personal" template or start blank
3. Install the Web Clipper browser extension
4. Create your first page: "My Second Brain"
```

### Step 3 — Create your PARA structure

No matter which tool you chose, create these four top-level folders/pages NOW:

```text
📁 Projects   ← things with a deadline (mcp-servers, job search, course)
📁 Areas      ← ongoing responsibilities (Java, career, health)
📁 Resources  ← reference material (books, links, snippets)
📁 Archives   ← done / inactive (old job notes, completed courses)
📂 Inbox      ← drop everything here first, sort later
```

### Step 4 — Your first note

Open your tool and write one note right now:

```markdown
# Today's Log — [date]

## What I'm working on
- [your current task]

## Question I have
- [something you want to look up]

## Insight / thing I learned
- [anything]
```

That's it. You have a PKM system.

---

## Path B — "I have notes but they're a mess"

**Time needed:** 30–60 minutes to reorganise.

### The problem: you have no structure

The fix is applying PARA retroactively:

1. Create the four PARA folders (Projects, Areas, Resources, Archives)
2. Look at every existing note — ask ONE question:
   *"Does this have a deadline?"* → Projects
   *"Is this ongoing?"* → Areas
   *"Is this reference material?"* → Resources
   *"Is this done?"* → Archives
3. Move notes. Don't rename them yet.
4. Add an **Inbox** for new captures going forward.

This takes 30–60 minutes for most people and the improvement is dramatic.

### The problem: notes are isolated (no links)

If you are using Obsidian or Logseq:
- Add `[[links]]` to connect related notes
- Use tags (`#java`, `#architecture`) for cross-cutting themes
- Start your notes with a one-sentence summary — it forces you to distill the idea

→ See [templates.md](templates.md) for structured note templates that force good habits.

---

## Path C — "I have a system, I just want to level up"

### Progressive Summarization

When you save a note, don't just paste raw content. Process it in layers:

```text
Layer 0: Raw capture (quotes, links, code snippets)
Layer 1: Bold the most important sentences
Layer 2: Highlight within the bold (the essential 10%)
Layer 3: Executive summary in your own words (3-5 sentences)
Layer 4: Express — turn it into a deliverable (ADR, blog post, PR description)
```

### Connect your note-taking to your dev workflow

| Dev activity | Note format |
|---|---|
| Architectural decision | ADR — [see template](templates.md#adr) |
| Sprint / learning session | Daily log — [see template](templates.md#daily-log) |
| Code pattern learned | Snippet note — [see template](templates.md#snippet) |
| Book / article summarised | Resource note — [see template](templates.md#resource) |
| Bug post-mortem | Debug note — [see template](templates.md#debug) |

### Use the ai-brain workspace with Copilot

```powershell
brain new --tier notes --project java        # new curated note
brain new --tier inbox                        # quick capture
brain search zettelkasten --tier library     # semantic search
brain publish brain\ai-brain\notes\draft.md  # promote to library + commit
```

Or use slash commands in Copilot Chat:
- `/brain-new` → guided note creation
- `/brain-search` → search across all tiers

---

## Path D — "I want to migrate from one tool to another"

→ See [migration-guide.md](migration-guide.md) for step-by-step paths:
- Notion → Obsidian
- Logseq → Obsidian
- OneNote → Notion
- Any tool → plain Markdown (via Pandoc)

---

## Recommended Reading Order

1. **This file** — START-HERE.md ✅
2. [tools-comparison.md](tools-comparison.md) — pick or confirm your tool
3. [para-method.md](para-method.md) — set up your folder structure
4. [templates.md](templates.md) — add templates to your tool
5. [migration-guide.md](migration-guide.md) — if switching tools
6. [README.md](README.md) — full module overview and Copilot integration

---

## Quick Reference — Commands

```powershell
# Windows PowerShell — load brain aliases
. .\brain\ai-brain\scripts\brain-module.psm1

brain new                          # create a note interactively
brain status                       # see what's in each tier
brain search <query>               # search all notes
brain publish <path>               # promote to library + git commit
brain list --tier library          # list library notes
```

---

*Related: [brain/ai-brain/README.md](../ai-brain/README.md) · [README.md](README.md) · `/digital-notetaking` slash command*
