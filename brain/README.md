# Brain Module

> The **Brain** module is the knowledge management layer of the Learning Assistant workspace.  
> It provides both a **live note workspace** (`ai-brain/`) and a **guide library** (`digitalnotetaking/`)  
> for software engineers who want structured, searchable notes alongside their code.

---

## Module Structure

```
brain/
├── ai-brain/             ← Live knowledge workspace (notes, inbox, archive)
│   ├── inbox/            ← Quick capture — gitignored, cleared per session
│   ├── notes/            ← Curated notes — gitignored, permanent on this machine
│   ├── archive/          ← Published notes — git-tracked, permanent in the repo
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
brain list --tier archive          # list published notes
```

Or use Copilot Chat: `/brain-search`

### I want to publish a note to the repo

```powershell
brain publish brain\ai-brain\inbox\my-note.md --project java
```

Or use Copilot Chat: `/brain-publish`

---

## Three-Tier Workspace

| Tier | Folder | Git-tracked? | Purpose |
|---|---|---|---|
| **Inbox** | `ai-brain/inbox/` | ❌ | Raw capture — fast, no pressure to be good |
| **Notes** | `ai-brain/notes/` | ❌ | Curated, stays on this machine |
| **Archive** | `ai-brain/archive/` | ✅ | Committed to the repo, permanent reference |

The typical lifecycle:
```
/brain-new → inbox/  →  curate  →  notes/  →  brain publish  →  archive/
```

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
