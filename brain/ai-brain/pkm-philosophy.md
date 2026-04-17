# PKM Philosophy — Why ai-brain Is Structured This Way

> **Tier:** Reference doc for `brain/ai-brain/`
> **Audience:** Anyone new to this workspace, or anyone questioning a structural decision
> **Companion docs:** [README.md](README.md) (how to use) · [scripts/README.md](scripts/README.md) (commands reference) · [../digitalnotetaking/](../digitalnotetaking/) (PKM methods) · [pkm/README.md](pkm/README.md) (capture sources & access control)

---

## The One Question That Drives Everything

> **Did you write it yourself — or did you import it?**

This single question routes every piece of content to the correct tier.
- **Inbox** = raw capture, not ready, not committed (gitignored)
- **Notes** = your own writing: insights, decisions, session logs, cheatsheets
- **Library** = imported sources: slide decks, guides, AI-generated reference docs you received
- **Sessions** = captured AI conversations: research, analysis, learning deep-dives worth revisiting
- **Backlog** = work to track: todos, features, ideas, sprints, brainstorming
- **PKM** = knowledge infrastructure: capture sources, access control, sensitivity, audit logging

Before asking "where does this go?" ask "did I write it?" If you can't answer yet, it goes in **inbox** until you know. If it was a valuable AI conversation, it goes in **sessions/**. If it's about how/where you capture knowledge, it goes in **pkm/**.

---

## 📑 Table of Contents

- [Why Three Zones?](#why-three-zones)
- [How It Maps to Established PKM Methods](#how-it-maps-to-established-pkm-methods)
- [The Inbox — Capture Zone](#the-inbox--capture-zone)
- [Notes — Your Knowledge](#notes--your-knowledge)
- [Library — Source Material](#library--source-material)
- [Sessions — Captured AI Conversations](#sessions--captured-ai-conversations)
- [Frontmatter Philosophy](#frontmatter-philosophy)
- [Tagging Strategy](#tagging-strategy)
- [The Review Cycle](#the-review-cycle)
- [Naming Philosophy](#naming-philosophy)
- [Anti-Patterns and Why They Fail](#anti-patterns-and-why-they-fail)
- [Design Decisions Log](#design-decisions-log)

---

## Why Three Zones?

Most PKM systems struggle with two failure modes:

1. **Too flat** (single folder): everything piles up, nothing is findable, capture = loss
2. **Too deep** (folders within folders): organisation overhead kills the habit; you spend more time filing than thinking

Three zones solve this by mapping to three distinct **psychological states** during knowledge work:

| Zone | Psychological state | Commitment | Git |
|---|---|---|---|
| **Inbox** | "I haven't processed this yet" | None — ephemeral | Gitignored |
| **Notes** | "This is my thinking" | Permanent — I own this | Tracked |
| **Library** | "This is a source I curate" | Permanent — I reference this | Tracked |
| **Sessions** | "This was a valuable AI exploration" | Permanent — I can revisit this | Tracked |

> **Note:** The workspace started with three zones and evolved to four when AI chat sessions
> became a distinct content type. See [Why a 4th tier for sessions?](#design-decisions-log)
> in the Design Decisions Log.

### Why not 2?

A two-zone system (inbox + notes) loses the distinction between *your synthesis* and *imported sources*.
When you receive an excellent guide from a colleague and put it in `notes/`, it creates an ownership
illusion — you confuse "I once read this" with "I know this." The three-zone separation forces
deliberate distillation: to move from library to notes, you must write something original.

### Why not 4+?

Four zones (`inbox` → `drafts` → `notes` → `library`) adds process overhead that benefits
only large-scale PKM systems (Zettelkasten for writing books). For a developer practitioner's
workspace, the friction of maintaining 4+ zones kills the capture habit. Three is the sweet spot:
capture, own, reference.

---

## How It Maps to Established PKM Methods

### PARA Method (Tiago Forte)

PARA: **Projects** / **Areas** / **Resources** / **Archive**

| PARA concept | ai-brain equivalent | Notes |
|---|---|---|
| Projects | `project:` field in frontmatter | Project scoping is done via metadata, not folders |
| Areas | `tags:` + `kind: note` in notes/ | Ongoing areas (e.g., `java`, `mcp-servers`) tagged, not siloed |
| Resources | `library/` tier | Imported source material lives here |
| Archive | `status: archived` in frontmatter | Notes marked as archived in-place; no separate folder needed |

**Key difference from PARA:** PARA uses folders to separate concerns; ai-brain uses
frontmatter fields. This enables cross-project notes while maintaining PARA's spirit.
Any note can be in multiple areas via tags — no duplication.

### CODE Method (Tiago Forte)

CODE: **Capture** / **Organise** / **Distill** / **Express**

| CODE step | ai-brain action |
|---|---|
| **Capture** | Drop in `inbox/` (no structure required) |
| **Organise** | `brain publish` → library/, or `brain move` → notes/ |
| **Distill** | Write a note in notes/ synthesising multiple sources |
| **Express** | Use distilled notes to generate output (blog, PR, design doc, Copilot prompt) |

**Key insight from CODE applied here:** The `inbox/` → `notes/` move should always involve
a distillation step — you don't move verbatim captures to `notes/`; you write your own version.

### GTD (Getting Things Done — David Allen)

GTD's `inbox` concept maps directly to ai-brain's `inbox/`. Core GTD rules applied:
- **Inbox is not a storage location** — it's a processing queue to be cleared
- **The "2-minute rule":** If clarifying where a file belongs takes < 2 min, do it now
- **Regular sweep:** Clear inbox at end of every session (`brain clear`)

### Zettelkasten (Niklas Luhmann)

Zettelkasten adds two concepts that ai-brain partially adopts:
- **Atomic notes:** Each note = one idea (encouraged in `notes/`, not enforced)
- **Link-first thinking:** Every note should link to at least one other note (done via `tags:` sharing, not wiki-links)

**Where ai-brain diverges from Zettelkasten:**
Zettelkasten was designed for academic writing and building a lifelong idea network.
ai-brain is designed for a practitioner who builds software day-to-day — it prioritises
speed of capture and practical retrieval over theoretical link density.

---

## The Inbox — Capture Zone

**Path:** `brain/ai-brain/inbox/`
**Git status:** Gitignored — never committed

### Purpose

Inbox is a **friction-free capture zone**. Nothing you put here is permanent.
No frontmatter required. No naming convention required. No guilt for dumping garbage.

### What goes here

- Raw paste from a Copilot session
- A draft note you started but didn't finish
- An external document you downloaded and haven't classified
- Meeting notes from a call (before you distil them)
- A link dump for later processing
- AI-generated content you received (slide decks, guides, summaries)

### Rules

1. **Don't organise here** — the point of inbox is zero friction capture
2. **Clear after every session** — `brain clear` (preview) or `brain clear --force` (delete)
3. **Never commit inbox contents** — it's gitignored by design; raw capture isn't knowledge

### Why gitignored?

Three reasons:
1. **Confidentiality:** You might paste sensitive info mid-session without thinking
2. **Quality control:** Inbox is unprocessed — it shouldn't represent "what you know"
3. **Encourages processing:** When inbox is gitignored, you're motivated to distil it into notes

---

## Notes — Your Knowledge

**Path:** `brain/ai-brain/notes/`
**Git status:** Tracked — committed and version-controlled

### Purpose

Notes is where **your knowledge lives**. The ownership test is strict:
You wrote it. You distilled it. You decided it. You understood it well enough to explain it.

### What belongs in notes/

| Kind | Description | Example |
|---|---|---|
| `note` | General knowledge, explanations, thoughts you authored | `2026-03-01_java-streams-patterns.md` |
| `decision` | Architectural or design choices with rationale (ADR format) | `2026-03-10_decision-use-gradle-over-maven.md` |
| `session` | Log of what happened in a work/learning session | `2026-03-06_mcp-servers-session.md` |
| `snippet` | Code or command references you authored | `2026-03-01_powershell-aliases.md` |

### What does NOT belong in notes/

- Verbatim copy of an external doc → library/
- AI-generated summary you received → library/ (unless you rewrote it)
- Raw dump from a Copilot session → inbox/ first, then distil

### The distillation test

Before moving something from inbox to notes, ask:
> "Could I explain the main points of this to a colleague without reading the source?"

If yes → it belongs in notes/ (write your explanation).
If no → it belongs in library/ (keep the source; revisit for distillation later).

---

## Library — Source Material

**Path:** `brain/ai-brain/library/`
**Git status:** Tracked — committed and version-controlled

### Purpose

Library is a **curated reference archive** of source material you want to preserve and reference,
but that you did not author. Think of it as your personal research library — you own the curation
(choosing what to keep), but not the authorship.

### Library hierarchy

```text
library/
  <project-bucket>/
    <YYYY-MM>/
      YYYY-MM-DD_slug.md
```

- **Project bucket:** Groups library content by topic/project (e.g., `ghcp-knowledge-sharing`, `mcp-servers`, `java-learning`)
- **Month folder:** Auto-created by `brain publish` — you never create folders manually
- **File naming:** Same as notes — date-prefixed slug

### What belongs in library/

| Kind | Description | Example |
|---|---|---|
| `ref` | Imported external reference, slide deck, source document | `2026-03-06_ghcp-knowledge-sharing-session.md` |
| `resource` | Imported links, reading lists, curated resource collections | `2026-03-01_java-concurrency-resources.md` |
| `snippet` | Imported code examples you want to preserve | `2026-03-01_spring-security-examples.md` |

### The publish workflow

```powershell
brain publish brain\ai-brain\inbox\draft.md --project ghcp-knowledge-sharing
# Prompts for: project bucket, tags
# Auto-creates: library/<project>/<YYYY-MM>/YYYY-MM-DD_slug.md
# Commits: with conventional commit message
```

---

## Sessions — Captured AI Conversations

**Path:** `brain/ai-brain/sessions/`
**Git status:** Tracked — committed and version-controlled

### Purpose

Sessions preserve **valuable AI chat conversations** in their native request-response format.
Unlike notes (your distilled writing) or library (imported external sources), sessions capture
the analytical flow of AI-assisted exploration — research, code analysis, debugging, learning,
and design discussions.

### What belongs in sessions/

| Category | Domain | Description |
|---|---|---|
| `code-analysis` | work | Architecture analysis, code review, pattern identification |
| `debugging` | work | Complex investigation, root cause analysis |
| `requirements` | work | User stories, acceptance criteria, feature scoping |
| `performance` | work | Profiling, optimization, benchmarking |
| `feature-exploration` | work | Design alternatives, POC planning, feasibility |
| `documentation` | work | API docs, design docs, architecture overviews |
| `research` | work / personal | Technology evaluation, protocol analysis, tool comparison |
| `learning` | personal | Concept deep-dives, tutorials, skill-building |
| `project-dev` | personal | Personal project development, side-project architecture |
| `financial` | personal | Budgeting, investment, tax strategies |

### What does NOT belong in sessions/

- Simple refactoring (rename, extract method) — too routine
- One-line fixes — no analytical depth
- Build/compile commands — no learning value
- Quick factual answers — no reference value

### The capture gate

Sessions are captured **automatically** when the conversation meets complexity criteria:
research, analysis, multi-exchange depth, lengthy explanations, or explicit user request.
See `.github/instructions/chat-capture.instructions.md` for the full policy.

### Sessions hierarchy

```text
sessions/
  work/
    <category>/
      YYYY-MM-DD_HH-MMtt_<category>_<subject>[_v<N>].md
  personal/
    <category>/
      YYYY-MM-DD_HH-MMtt_<category>_<subject>[_v<N>].md
```

### The promotion path

Captured sessions can be **promoted** to notes/ when you distil them into your own writing:
1. Read the session capture
2. Write your synthesis in `notes/YYYY-MM-DD_slug.md`
3. Link back to the source session
4. The session remains as evidence; the note becomes your knowledge

### Versioning

Sessions support continuation versioning: `_v2`, `_v3`, etc., with a `parent:` field
in frontmatter linking to the previous version. Use versioning when continuing analysis
on the same subject. Start a new file for different aspects or fresh perspectives.

---

## Frontmatter Philosophy

Every note (in `notes/` or `library/`) carries YAML frontmatter. The philosophy:

> **Frontmatter is not bureaucracy — it's the interface for future retrieval.**

Without frontmatter, you rely on filenames and full-text search. With frontmatter:
- `brain search --kind decision` → instant filtering
- `brain list --tier notes --project mcp-servers` → scoped view
- Future tooling (grep, web UI, Obsidian vault) can index these fields

### Every required field and why

```yaml
date: 2026-03-10        # YYYY-MM-DD — enables chronological sort and recency filtering
kind: note              # see kind taxonomy below — enables filtering by content type
project: mcp-servers    # project bucket — groups related notes across sessions
tags: [mcp, java, tools] # freeform — search index, related-note discovery
status: draft           # draft | final | archived — lifecycle state
source: copilot         # copilot | manual | imported — provenance tracking
```

### Field decisions

**`date`:** Always the date the note was *created*, not the date of the event it describes.
If you write a session log on 2026-04-01 about work done on 2026-03-28, use `2026-04-01`.

**`kind`:** Not the format (markdown) but the *conceptual type* of note. Changing kind
later is fine — it's a routing hint, not an ontological commitment.

**`status: draft` vs `final`:** Most notes live as `draft` forever. That's OK.
Mark `final` only when you deliberately consider the note complete and polished.
Mark `archived` when the content is obsolete but you want to preserve it.

**`source: copilot | manual | imported`:**
- `copilot` — note was created or substantially drafted by an AI session
- `manual` — note was entirely human-written (your own thinking)
- `imported` — note is imported external source material (library tier)

---

## Tagging Strategy

Tags are the primary discovery mechanism after filename search.

### Rules

1. **3-5 tags per note** — more fragments discovery, fewer loses context
2. **Prefer existing tags** — `brain search --tag java` shows all java-tagged notes; new tags dilute this
3. **Lowercase, hyphenated** — `java-concurrency`, not `JavaConcurrency` or `java concurrency`
4. **Domain tags** — `java`, `mcp`, `git`, `design-patterns`, `python`
5. **Topic tags** — `streams`, `concurrency`, `bcrypt`, `password-reset`
6. **Kind tags** — `how-to`, `decision`, `research`, `cheatsheet` (supplement, not replace, `kind:` field)

### Tag taxonomy (suggested)

**Domain tags:** `java`, `python`, `javascript`, `go`, `sql`, `shell`, `powershell`
**Practice tags:** `mcp`, `git`, `docker`, `kubernetes`, `ci-cd`, `spring`, `gradle`, `maven`
**Concept tags:** `concurrency`, `streams`, `generics`, `design-patterns`, `testing`, `security`
**Copilot tags:** `copilot`, `skills`, `prompts`, `agents`, `mcp-tools`
**Brain tags:** `brain`, `pkm`, `workflow`, `tools`

---

## The Review Cycle

PKM only works if content is periodically surfaced and refined.

### Session cycle (end of every session)

```text
brain status                    # see what's in each tier
brain clear (--force)           # clear inbox if nothing worth keeping
```

### Weekly review (5 minutes)

```text
brain list --tier notes         # what did I write this week?
brain list --tier library       # what did I import this week?
# Pick 1 note to refine → promote to status: final
# Pick 1 library source → write a distillation note in notes/
```

### Monthly review (15 minutes)

```text
brain search --tag <domain>     # find all notes in a domain
# Identify: outdated content → mark status: archived
# Identify: related notes → add shared tags or cross-references
# Plan: what domain needs more coverage? → add to brain capture backlog
```

---

## Naming Philosophy

The naming convention balances **searchability** and **chronological sortability**:

```text
YYYY-MM-DD_topic-slug.md           ← most notes (date-prefixed)
YYYY-MM-DD_session-<topic>.md      ← session logs
YYYY-MM-DD_decision-<topic>.md     ← architectural decisions (ADR format)
YYYY-MM-DD_<source>-<topic>.md     ← imported source in library/
topic-slug.md                      ← timeless reference (no date prefix)
```

### Why date-prefix first?

- Files sort chronologically in any file browser (OS, VS Code, GitHub)
- Chronological view is often the most useful view for a practitioner's notes
- Date makes "when did I write about this?" instantly answerable without opening the file

### Why slugs, not full titles?

- Slugs are URL-safe and shell-safe (no quoting needed in commands)
- Shorter filenames are easier to read in `brain list` output
- Title lives inside the document (as the H1 heading); the filename is the locator, not the title

### When NOT to date-prefix

Use a bare slug for **timeless reference** content where the date would be misleading:
- `java-concurrency-cheatsheet.md` (not `2026-03-01_java-concurrency-cheatsheet.md`)
- `powershell-aliases.md`

If you're unsure, add the date. You can always rename later.

---

## Anti-Patterns and Why They Fail

| Anti-pattern | Symptom | Fix |
|---|---|---|
| **Never clearing inbox** | Inbox accumulates hundreds of files; becomes unstructured storage | Schedule: clear inbox at end of every session |
| **Putting imports in notes/** | You can't find your own insights (buried in imported content) | Ask "did I write it?" → if no, library/ |
| **Tagging everything** | 50+ tags make it impossible to filter usefully | Limit to 3-5 tags; prefer existing tags |
| **No `kind:` field** | `brain list` shows undifferentiated flat list | Always set kind on every note |
| **Writing essays in notes/** | Notes become too long to scan | One idea per note; link notes together via shared tags |
| **Using notes/ as a to-do list** | Notes become action items that are never deleted | Tasks belong in a task manager (Jira, Linear, GitHub Issues, etc.) |
| **Skipping frontmatter** | Notes are unsearchable; `brain search` doesn't find them | Always include all 6 frontmatter fields |
| **Committing inbox content** | Raw, unprocessed content pollutes git history | inbox/ is gitignored — never override this |

---

## Design Decisions Log

### Why not Obsidian-inside-git?

Obsidian uses `.obsidian/` config, plugins, and a proprietary link format (`[[wikilink]]`).
Keeping `ai-brain` as plain markdown:
- Works in any editor (VS Code, GitHub web UI, any terminal)
- No platform lock-in
- Copilot can read and write it natively without plugin dependencies
- Future: can be imported into Obsidian if desired (markdown is forward-compatible)

### Why `kind:` instead of subfolders?

An alternative design would be: `notes/decisions/`, `notes/sessions/`, `notes/snippets/`.
We chose frontmatter over folders because:
- A note can change kind without moving files
- Git history is simpler (content edits vs. file moves)
- `brain search --kind decision` is as fast as folder lookup but more flexible
- Copilot can filter by frontmatter field in a search query

### Why not PARA folders inside notes/?

PARA with folders (`notes/projects/mcp-servers/`, `notes/areas/java/`) would create:
- Deep nesting for cross-project notes
- Folder-move operations when projects complete
- Duplicate content when a note belongs to multiple areas

Using the `project:` frontmatter field achieves PARA's project grouping without the folder overhead.

### Why a 4th tier for sessions?

The original three-tier design (inbox / notes / library) handles human-authored knowledge and
imported sources well. But AI chat sessions don't fit cleanly into either:

- They're **not your distilled writing** (so not notes/)
- They're **not imported external sources** (so not library/)
- They're **not raw, disposable captures** (they have lasting reference value)

AI-assisted exploration sessions — research, code analysis, complex debugging, architecture
discussions — produce structured, valuable content that sits between "imported source" and
"your own notes." The `sessions/` tier captures these conversations in their native
request-response format, preserving the analytical flow.

**When to promote:** If you distil insights from a session capture into your own words,
that distillation goes in `notes/` with a link back to the session. The session stays as
the raw evidence trail; the note becomes your knowledge.

**Why not put sessions in library/?** Library holds external sources you didn't create.
Sessions are collaborative — you prompted them, you shaped the conversation. They deserve
their own tier with domain/category hierarchy that matches how you'd search for them later.

### Why gitignore inbox/ instead of a separate repo?

A separate repo for raw captures would require context switching and separate git workflows.
Gitignoring inbox keeps it in the same workspace — instantly accessible — while maintaining
the boundary that raw, unprocessed content shouldn't be in version control.
