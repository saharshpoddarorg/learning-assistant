# Brain Module

> The **Brain** module is the knowledge management layer of the Learning Assistant workspace.
> It provides both a **live note workspace** (`ai-brain/`) and a **guide library** (`digitalnotetaking/`)
> for software engineers who want structured, searchable notes alongside their code.

---

## Module Structure

```text
brain/
├── ai-brain/             ← Live knowledge workspace (notes, inbox, library, sessions)
│   ├── inbox/            ← Quick capture — gitignored, cleared per session
│   ├── notes/            ← Curated notes — git-tracked, committed to repo
│   ├── library/          ← Formal reference library — git-tracked, project-organized
│   ├── sessions/         ← Captured AI chat sessions — git-tracked, domain/category organized
│   └── scripts/          ← brain.ps1 / brain.sh CLI tools
├── digitalnotetaking/    ← Guides & knowledge hub
│   ├── README.md
│   ├── START-HERE.md     ← New to PKM? Start here
│   ├── tools-comparison.md
│   ├── para-method.md
│   ├── templates.md
│   └── migration-guide.md
└── src/
    ├── Main.java
    └── digitalnotetaking/  ← Java package: NoteKind, NoteMetadata, NoteTemplate
```

---

## Quick Start

### I want to take notes NOW

```powershell
# Windows — load brain aliases (one-time per terminal session)
. .\brain\ai-brain\scripts\brain-module.psm1

brain new                          # guided note creation (interactive)
brain new --tier inbox             # fast capture in inbox
brain status                       # see what's in each tier
```

Or use Copilot Chat: `/brain-new`

### I want to read the PKM guides

→ [brain/digitalnotetaking/START-HERE.md](digitalnotetaking/START-HERE.md)

### I want to search my notes

```powershell
brain search <query>               # full-text search across all tiers
brain search java --tag generics   # with tag filter
brain list --tier library          # list library notes
```

Or use Copilot Chat: `/brain-search`

### I want to publish an imported source to the library

```powershell
brain publish brain\ai-brain\inbox\GHCP_Agents_Guide.md --project ghcp-knowledge-sharing
```

Or use Copilot Chat: `/brain-publish`

---

## Three-Tier Workspace

| Tier | Folder | Git-tracked? | Purpose |
|---|---|---|---|
| **Inbox**    | `ai-brain/inbox/`    | ❌ | Raw capture — fast, temporary, gitignored |
| **Notes**    | `ai-brain/notes/`    | ✅ | **Your writing** — distilled insights, session logs, decisions you authored |
| **Library**  | `ai-brain/library/`  | ✅ | **Imported sources** — external docs, slide decks, reference material you preserved |
| **Sessions** | `ai-brain/sessions/` | ✅ | **Captured AI conversations** — research, analysis, learning sessions worth preserving |

The typical lifecycle:

```text
inbox/  →  brain-new (your writing)   →  notes/
inbox/  →  brain publish (external)    →  library/<project>/<YYYY-MM>/
(auto)  →  AI chat capture             →  sessions/<domain>/<category>/
```

**Routing questions:**
- Did you write this yourself? → Yes → `notes/` | No (imported) → `library/`
- Was it a valuable AI conversation? → Yes → `sessions/`

---

## Java Package — `digitalnotetaking`

The `brain/src/digitalnotetaking/` package provides the data model and template
engine for the workspace:

| Class | Purpose |
|---|---|
| `NoteKind` | Enum: NOTE, DECISION, SESSION, RESOURCE, SNIPPET, REF |
| `NoteStatus` | Enum: DRAFT, FINAL, ARCHIVED |
| `NoteMetadata` | Immutable record for YAML frontmatter |
| `NoteTemplate` | Factory — generates templated Markdown notes |

Usage:

```java
NoteMetadata metadata = NoteMetadata.builder()
        .kind(NoteKind.DECISION)
        .project("mcp-servers")
        .tags(List.of("adr", "architecture"))
        .build();

String markdown = NoteTemplate.generate(metadata, "ADR-001: Use SSE transport");
```

---

## Copilot Integration

| Slash command | Purpose |
|---|---|
| `/brain-new` | Create a note interactively via Copilot |
| `/brain-publish` | Promote note to archive and commit |
| `/brain-search` | Search by tag, project, kind, date, or full text |
| `/digital-notetaking` | PKM assistant — tools, methods, templates, migration |

---

## Related Sections

- [digitalnotetaking/README.md](digitalnotetaking/README.md) — PKM guide library
- [ai-brain/README.md](ai-brain/README.md) — Live workspace guide
- `.github/skills/digital-notetaking/SKILL.md` — Copilot PKM skill
- `.github/prompts/digital-notetaking.prompt.md` — `/digital-notetaking` slash command
- `.github/docs/slash-commands.md#brain` — All brain slash commands
