# Brain Module

> The **Brain** module is the knowledge management layer of the Learning Assistant workspace.
> It provides both a **live note workspace** (`ai-brain/`) and a **guide library** (`digitalnotetaking/`)
> for software engineers who want structured, searchable notes alongside their code.

> **Module duality:** This module contains two distinct parts:
>
> - **`ai-brain/`** — the knowledge workspace (markdown + scripts). Technology-neutral,
>   exportable to any project. This is what you copy when exporting.
> - **`src/digitalnotetaking/`** — Java code modelling note metadata (NoteKind, NoteStatus,
>   NoteTemplate). Specific to this repo's learning exercises — not needed when exporting.
> - **`digitalnotetaking/`** — PKM guides and knowledge hub. Reference material, not
>   required for the workspace to function.
>
> See [Export Guide § 3](../.github/docs/export-guide.md#3-brain-workspace) for copy instructions.

---

## Module Structure

```text
brain/
├── ai-brain/             ← Live knowledge workspace (exportable — markdown + scripts only)
│   ├── inbox/            ← Quick capture — gitignored, cleared per session
│   ├── notes/            ← Curated notes — git-tracked, committed to repo
│   ├── library/          ← Formal reference library — git-tracked, project-organized
│   ├── sessions/         ← Captured AI chat sessions — git-tracked, domain/category organized
│   ├── backlog/          ← Todos, ideas, epics, brainstorming — kanban board
│   ├── pkm/              ← Capture sources, access policy, sensitivity
│   └── scripts/          ← brain.ps1 / brain.sh CLI tools
├── digitalnotetaking/    ← Guides & knowledge hub (reference only)
│   ├── README.md
│   ├── START-HERE.md     ← New to PKM? Start here
│   ├── tools-comparison.md
│   ├── para-method.md
│   ├── templates.md
│   └── migration-guide.md
└── src/                  ← Java package (learning exercises — not needed for export)
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

## Six-Tier Workspace

| Tier | Folder | Git-tracked? | Purpose |
|---|---|---|---|
| **Inbox**    | `ai-brain/inbox/`    | ❌ | Raw capture — fast, temporary, gitignored |
| **Notes**    | `ai-brain/notes/`    | ✅ | **Your writing** — distilled insights, session logs, decisions you authored |
| **Library**  | `ai-brain/library/`  | ✅ | **Imported sources** — external docs, slide decks, reference material you preserved |
| **Sessions** | `ai-brain/sessions/` | ✅ | **Captured AI conversations** — research, analysis, learning sessions worth preserving |
| **Backlog**  | `ai-brain/backlog/`  | ✅ | **Tracked work** — todos, features, ideas, brainstorming, kanban board |
| **PKM**      | `ai-brain/pkm/`      | ✅ | **Infrastructure** — capture sources, access policy, sensitivity, logging |

The typical lifecycle:

```text
inbox/  →  brain-new (your writing)   →  notes/
inbox/  →  brain publish (external)    →  library/<project>/<YYYY-MM>/
(auto)  →  AI chat capture             →  sessions/<domain>/<category>/
(auto)  →  /jot or /todo               →  backlog/items/ or backlog/ideas/
```

**Routing questions:**

- Did you write this yourself? → Yes → `notes/` | No (imported) → `library/`
- Was it a valuable AI conversation? → Yes → `sessions/`
- Is it work to track or an idea to capture? → Yes → `backlog/`
- Is it about where/how you capture knowledge? → Yes → `pkm/`

---

## Exporting the Brain Workspace

The brain workspace (`ai-brain/`) is **technology-neutral** — it contains only markdown
(`.md`) and shell scripts (`.ps1`/`.sh`). It has no build system dependency, no language
requirement, and no runtime dependency beyond PowerShell or Bash.

### What to copy

| Scenario | What to copy | Notes |
|---|---|---|
| **Workspace only** (recommended) | `brain/ai-brain/` | Just the knowledge workspace — works in any project |
| **Full module** | `brain/` | Includes Java code + PKM guides — useful if you want the data model too |
| **Workspace to custom path** | `brain/ai-brain/*` → `your/path/` | See [Export Guide § 3b](../.github/docs/export-guide.md#3b-custom-location-different-path--eg-knowledgeworkspace) |

### In your target project, the brain is NOT a Java module

In *this* repo, `brain/` happens to be a Java module (`brain.iml`) because the learning
assistant uses Java. But when you export `ai-brain/` to another project:

- It is **not** a Java module, npm package, Python package, or any language-specific artefact
- Do **not** add `build.gradle`, `package.json`, `setup.py`, or similar to it
- If your build tool scans all files (e.g., `npm run lint` on `.md`), exclude the brain path
- It works at any nesting depth — scripts find the repo root via `git rev-parse`

### Configurable path

The brain workspace defaults to `brain/ai-brain/` but can live anywhere:

```text
# Default (this repo)
brain/ai-brain/

# Examples in other projects
knowledge/workspace/
docs/brain/
tools/brain/
packages/brain/
```

Set the `BRAIN_PATH` environment variable to the path relative to your repo root.
See [copilot-instructions.md § Configurable Paths](../.github/copilot-instructions.md#configurable-paths)
for the full configuration guide — which files reference the path and how to update them.

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
- `.github/skills/knowledge-management/digital-notetaking/SKILL.md` — Copilot PKM skill
- `.github/prompts/domain/digital-notetaking.prompt.md` — `/digital-notetaking` slash command
- `.github/docs/slash-commands.md#brain` — All brain slash commands
