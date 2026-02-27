# Digital Note-Taking — Module Guide

> **What is this module?**  
> The `brain/digitalnotetaking/` folder is the structured knowledge hub for everything
> related to personal knowledge management (PKM), digital note-taking, and productivity
> tools — tailored specifically for software engineers.

---

## Quick Navigation

| I want to... | Go to |
|---|---|
| Start taking notes for the first time | [START-HERE.md](START-HERE.md) |
| Compare Notion, Obsidian, Logseq, OneNote | [tools-comparison.md](tools-comparison.md) |
| Learn the PARA method for devs | [para-method.md](para-method.md) |
| Learn the CODE workflow (Capture, Organize, Distill, Express) | [code-method.md](code-method.md) |
| Get copy-pasteable note templates | [templates.md](templates.md) |
| Migrate between tools | [migration-guide.md](migration-guide.md) |
| Link AI sessions to my note system | [ai-brain-integration.md](ai-brain-integration.md) |
| Write or search notes right now | [../ai-brain/README.md](../ai-brain/README.md) |
| Get Copilot help (slash command) | `/digital-notetaking` in Copilot Chat |
| Capture today's AI session as a note | `/brain-capture-session` in Copilot Chat |
| Browse curated PKM resources | `/resources → search → pkm` |

---

## What Is PKM?

**Personal Knowledge Management (PKM)** is the practice of systematically capturing,
organizing, and retrieving information you encounter — so you can build on it later
instead of re-researching the same things repeatedly.

For software engineers, PKM takes the form of:

- **Architecture Decision Records (ADRs)** — *why* you made a technical choice
- **Learning journals** — daily/sprint logs of what you studied
- **Code snippet vaults** — copy-pasteable patterns & idioms
- **Reading notes** — summaries of books, blog posts, and docs
- **Debug post-mortems** — root cause + resolution for recurring issues

---

## The Two Methodologies You Need

### PARA — for organizing
```
Projects/    ← active work with a deadline   (mcp-servers, interview-prep)
Areas/       ← ongoing responsibilities       (Java, Architecture, Career)
Resources/   ← topics of interest             (Distributed Systems, Books)
Archives/    ← completed / inactive           (old-job, finished-courses)
```
→ Full guide: [para-method.md](para-method.md)

### CODE — for the workflow

```
C → Capture    Don't filter — grab it now (brain new / brain-capture-session)
O → Organize   Route into PARA (brain move / brain publish)
D → Distill    Extract the signal — bold, TL;DR, progressive summarization
E → Express    Create from what you know (code, PR, doc, ADR, blog post)
```
→ Full guide: [code-method.md](code-method.md)

### CODE — for processing
```
Capture   → save anything worth keeping
Organize  → file into PARA
Distill   → highlight the key insight (progressive summarization)
Express   → produce something: ADR, blog post, PR description, wiki
```

---

## Tools at a Glance

| Tool | Best for | Storage | Free? | Offline? |
|---|---|---|---|---|
| **Obsidian** | Devs, Markdown lovers, local-first | Local .md files | ✅ personal | ✅ |
| **Notion** | Teams, wikis, databases | Cloud | ✅ personal | ⚠️ limited |
| **Logseq** | Open-source, Zettelkasten | Local .md / graph | ✅ | ✅ |
| **OneNote** | Microsoft 365 ecosystem | OneDrive | ✅ M365 | ✅ Windows |

→ Full comparison: [tools-comparison.md](tools-comparison.md)

---

## The ai-brain Workspace

This module connects to the live note workspace at [`brain/ai-brain/`](../ai-brain/):

```
brain/
├── ai-brain/          ← live workspace (inbox → notes → archive)
│   ├── inbox/         temporary capture (not git-tracked)
│   ├── notes/         curated notes (not git-tracked)
│   └── archive/       published notes (git-tracked)
└── digitalnotetaking/ ← guides & knowledge hub (this folder)
    ├── README.md      ← you are here
    ├── START-HERE.md
    ├── tools-comparison.md
    ├── para-method.md
    ├── templates.md
    └── migration-guide.md
```

**Workflow:**
1. Capture in `brain/ai-brain/inbox/` (use `brain new` or `/brain-new`)
2. Curate in `brain/ai-brain/notes/`
3. Publish to `brain/ai-brain/archive/` (use `brain publish` or `/brain-publish`)
4. Search anytime: `brain search <topic>` or `/brain-search`

---

## Copilot Integration

### Slash Commands

| Command | Purpose |
|---|---|
| `/digital-notetaking` | Full PKM assistant (tool advice, templates, migration) |
| `/brain-new` | Create a note in the ai-brain workspace |
| `/brain-publish` | Promote note to archive and commit |
| `/brain-search` | Search notes by tag, project, or full text |
| `/resources → search → pkm` | Browse curated PKM resources from the vault |

### Skill Activation

When you ask Copilot about Obsidian, Notion, PARA, Zettelkasten, or PKM,
the `digital-notetaking` skill auto-activates with:
- Tool-specific setup guides
- Note templates (ADR, sprint log, snippet vault)
- Migration checklists
- JDK version management (SDKMAN! / Temurin)

---

## Java Package — `digitalnotetaking`

The companion Java package (`brain/src/digitalnotetaking/`) provides:

| Class | Purpose |
|---|---|
| `NoteKind` | Enum for note kinds: NOTE, DECISION, SESSION, RESOURCE, SNIPPET, REF |
| `NoteMetadata` | Immutable record representing a note's frontmatter |
| `NoteTemplate` | Factory for generating Markdown note templates |

These classes mirror the frontmatter schema used in `ai-brain/` notes and can be
used for tooling (note generation, validation, search index building).

→ Source: [`brain/src/digitalnotetaking/`](../src/digitalnotetaking/)

---

## Learning Path

### If you are new to PKM
1. Read [START-HERE.md](START-HERE.md)
2. Pick Obsidian (offline) or Notion (cloud) and set it up in 15 minutes
3. Create your first PARA structure
4. Use `/brain-new` to capture your first note

### If you have a system and want to improve it
1. Read [para-method.md](para-method.md) — align your folder structure
2. Add templates from [templates.md](templates.md) to your tool
3. Run `/digital-notetaking → code-method → advanced` for progressive summarization tips

### If you want to migrate tools
1. See [migration-guide.md](migration-guide.md) for step-by-step migration paths
2. Run `/digital-notetaking → migration → notion-to-obsidian`

---

## Related Sections

- [brain/ai-brain/README.md](../ai-brain/README.md) — live workspace guide
- `.github/skills/digital-notetaking/SKILL.md` — Copilot skill (cheatsheets, templates)
- `.github/prompts/digital-notetaking.prompt.md` — `/digital-notetaking` slash command
- `.github/docs/slash-commands.md` — all slash commands reference
- `.github/docs/navigation-index.md` — full project navigation
