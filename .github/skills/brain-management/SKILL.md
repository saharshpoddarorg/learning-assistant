---
name: brain-management
description: >
  Brain workspace management for brain/ai-brain/ — naming conventions, file organisation,
  frontmatter schema, tier routing (inbox/notes/library), timestamping best practices,
  PKM (personal knowledge management) industry standards, and structural decisions.
  Use when asked about: where to put a note, how to name a file, what frontmatter to add,
  when to use notes/ vs library/, how to structure the brain workspace, whether to create
  a new tier/folder, brain naming conventions, prefix/suffix usage, how to timestamp notes,
  or any brain/ai-brain/ management question.
---

# Brain Management Skill

---

## Structure Decision — Why 3 Tiers (Not 4)

The brain/ai-brain/ workspace uses exactly **3 tiers**. Do NOT create `reference/`, `archive/`,
`sources/`, or any additional tier. Here's why each common request is already covered:

| You might want... | It already lives in... | Reason |
|---|---|---|
| A folder for "original sources" | `library/` | library/ IS the sources/reference tier |
| A "reference/" folder | `library/` | Different name; same concept |
| An "archive/" for old notes | `notes/` (with date prefix) | Date prefix handles chronology; git preserves history |
| Completed project notes | `notes/` | Keep them; the date tells you when |
| Downloaded reference docs | `library/` | That's exactly what library/ is for |

**Decision:** Keep 3 tiers. Distinguish content by frontmatter `kind`, not by folder structure.

```
inbox/    TEMP     gitignored — raw capture, cleared each session
notes/    YOURS    you authored it (insights, sessions, decisions, how-tos)
library/  SOURCES  you imported it (external docs, slide decks, reference material)
```

**The one routing question:** _"Did you write it yourself?"_
- **Yes** → `notes/`
- **No, it's imported** → `library/`
- **Not sure yet** → `inbox/`

---

## Essentials — Quick Commands & Basic Naming

### Basic Commands

```powershell
brain new                            # create a note (guided, interactive)
brain status                         # see counts per tier
brain list --tier notes              # what notes do you have?
brain list --tier library            # what sources are archived?
brain search java                    # full-text search across all tiers
brain clear                          # preview + clean inbox
```

### The One Naming Rule

```
YYYY-MM-DD_slug.md
```

| ✅ Good | ❌ Bad | Problem |
|---|---|---|
| `2026-03-06_java-generics.md` | `java generics.md` | Spaces break paths |
| `2026-03-06_mcp-decision.md` | `MCP-Decision.md` | CamelCase, no date |
| `2026-03-06_session-bug-fix.md` | `notes03-06.md` | Ambiguous date format |
| `java-keywords.md` | `JavaKeywords.md` | No date only OK for truly timeless files |

---

## Patterns & Workflows — Naming Conventions, Frontmatter & Timestamping

### Full Naming Convention Table

| Pattern | Use for | Example |
|---|---|---|
| `YYYY-MM-DD_slug.md` | General notes, any personal content | `2026-03-06_java-streams-notes.md` |
| `YYYY-MM-DD_session-<topic>.md` | Session logs (what you did/built) | `2026-03-06_session-mcp-server-fix.md` |
| `YYYY-MM-DD_decision-<topic>.md` | Architecture/design decisions (ADRs) | `2026-03-06_decision-use-sse-transport.md` |
| `YYYY-MM-DD_<source>-<topic>.md` | Imported library sources | `2026-03-06_ghcp-custom-agents-guide.md` |
| `slug.md` | Truly timeless reference (no date) — rare | `java-keywords.md` |

### Slug Construction Rules (Industry Standard)

1. **Lowercase only** — `java-generics`, not `Java-Generics`
2. **Hyphens as word separators** — `mcp-server-fix`, not `mcp_server_fix` or `mcpServerFix`
3. **No spaces or special characters** — strip `()[]{}@#$%!`
4. **Max ~50 characters** for the slug part (excluding the date prefix)
5. **3–6 words** in the slug is the sweet spot (descriptive + concise)
6. **Kind prefix for clarity** (optional but recommended):
   - `session-` for session logs
   - `decision-` for ADRs
   - `note-` only when genuinely ambiguous (usually omit it)

### Why ISO 8601 Date Prefix (YYYY-MM-DD)

ISO 8601 is the **international standard** (RFC 3339, ISO 8601:2004) for date representation:

| Format | Problem | Standard? |
|---|---|---|
| `2026-03-06` | None — sorts correctly everywhere | ✅ ISO 8601 |
| `03-06-26` | Ambiguous: March 6 or June 3? | ❌ |
| `20260306` | No separators — hard to read | ❌ |
| `March-6` | Not sortable, locale-specific | ❌ |
| `06-03-2026` | Day/month European format — ambiguous | ❌ |

**Key benefit:** `ls` / file explorer sorts files in chronological order automatically.

### Complete Frontmatter Schema

```yaml
---
date: 2026-03-06           # ISO 8601 — when the note was created or the event date
kind: note                 # see kind table below
project: mcp-servers       # project bucket (consistent, lowercase-hyphens)
tags: [java, generics, wildcards]   # 3–7 tags, lowercase, hyphens only
status: draft              # draft | final | archived
source: copilot            # copilot | manual | imported
---
```

**Extra fields for library files** (when import date differs from content date):

```yaml
---
date: 2026-03-06           # when the content was originally created/presented
addedAt: 2026-04-02        # when you added it to the library (different from date)
kind: ref
source: imported
---
```

### `kind` Values — Tier Router

| `kind` | Tier | Filename prefix | Use for |
|---|---|---|---|
| `note` | notes/ | (none) | General authored notes, explanations, thoughts |
| `decision` | notes/ | `decision-` | Architecture/design choices with rationale (ADR format) |
| `session` | notes/ | `session-` | Log of what happened in a work or learning session |
| `snippet` | notes/ or library/ | (none) | Code or command reference (yours = notes, imported = library) |
| `ref` | library/ | (none) | Imported external reference, slide deck, or source document |
| `resource` | library/ | (none) | Imported links, reading lists, curated external content |

### Tag Best Practices

```
✅ 3–7 tags per note     (under 3 = hidden; over 7 = noise)
✅ Lowercase hyphens     — mcp-servers, not "MCP Servers"
✅ Broad to narrow       — java → generics → wildcards
✅ Consistent vocab      — always mcp-servers (not sometimes mcp)
✅ Don't repeat project  — if project: mcp-servers, skip the mcp-servers tag
✅ Verb form for actions — debugging, refactoring (not debug, refactor)
❌ Avoid single-char tags — too broad, useless filter
❌ No adjectives alone   — "complex" or "important" tells you nothing
```

### Timestamping — Two-Date Pattern

Use two timestamps when a library file's **content date** differs from its **import date**:

```yaml
# Example: KS session happened March 6, but you imported it April 2
date: 2026-03-06       # content date (when the event happened / when it was published)
addedAt: 2026-04-02    # repo date (when YOU added it — for auditing library growth)
```

Rules:
- `date` — always required. Refers to the **content**'s date.
- `addedAt` — optional. Only when `source: imported` AND dates differ significantly.
- For your own notes (`source: copilot | manual`) — `date` = today = file creation date, skip `addedAt`.

---

## Advanced Reference — PKM Methods & Industry Standards

### PKM Method Mapping

| Method | Inventor | Core idea | brain/ai-brain/ mapping |
|---|---|---|---|
| **PARA** | Tiago Forte | Projects / Areas / Resources / Archives | inbox=raw capture; notes=Projects+Areas; library=Resources |
| **CODE** | Tiago Forte | Capture / Organise / Distill / Express | inbox=Capture; library=Organise; notes=Distill+Express |
| **Zettelkasten** | Niklas Luhmann | Atomic notes + bidirectional links | notes/ = slip-box (permanent); inbox = fleeting notes |
| **Evergreen Notes** | Andy Matuschak | Concept-named, always refined | notes/ = evergreen; inbox = rough capture |
| **Progressive Summarisation** | Tiago Forte | Highlight → Bold → Summary → Note | inbox→notes pipeline maps exactly |
| **GTD** | David Allen | Capture → Clarify → Organise → Reflect | brain status weekly review = GTD Weekly Review |

**Practical rule:** You don't need to pick one method. The 3-tier structure naturally
supports all of them. The routing question ("Did you write it?") is the unifying principle.

### Atomic Notes Principle

> **One concept per note.** If a note covers more than one thing, split it.

Signs a note should be split:
- Title contains "and" or "/" — `java-streams-and-collectors.md` → split into two
- Over 300 lines without clear H2 boundaries
- Hard to link to a specific section (too broad)
- You'd tag it with two completely different domains

Benefits of atomic notes:
- **Reusable** — can be linked from many places
- **Searchable** — single-topic notes surface in exact searches
- **Maintainable** — update one thing without tangling unrelated content

### Cross-Referencing Notes

Use relative markdown links for bidirectional cross-referencing:

```markdown
Implemented the approach from my [MCP session note](../notes/2026-03-06_session-mcp-server-fix.md).
Source: [KS Custom Agents Guide](../library/ghcp-knowledge-sharing/2026-03/2026-03-06_ghcp-custom-agents-guide.md)
See also: [Java generics cheatsheet](../notes/2026-03-06_java-generics-cheatsheet.md)
```

### Git Commit Conventions for Brain Changes

```
brain: note <slug> [kind: session]               ← new note committed
brain: archive <slug> to library/<project>       ← external source archived
brain: update <slug> — <what changed>            ← note updated
brain: delete <slug> — <reason>                  ← note removed
brain: reorganise library/<project>/<YYYY-MM>/   ← structural change
```

### Weekly Review Pattern (Inbox Zero)

```
1. brain status                          # how full is each tier?
2. brain list --tier inbox               # what is in inbox?
3. For each inbox file:
   a. Is it worth keeping?   No  → delete
   b. Did you write it?      Yes → brain move <file> --tier notes
   c. Did you import it?     Yes → brain publish <file> --project <bucket>
4. brain clear                           # verify inbox is clean
5. brain status                          # confirm: 0 in inbox
```

### Project Bucket Naming Standards

| Pattern | Example | Use for |
|---|---|---|
| Repo / product name | `mcp-servers`, `learning-assistant` | Code you're working on |
| Technology / domain | `java`, `docker`, `kubernetes` | Technology learning |
| Event / programme | `ghcp-knowledge-sharing`, `onboarding-2026` | Sessions, events |
| Generic catch-all | `general` | Anything that doesn't fit |

**Rules:** lowercase-hyphens. Never vary capitalisation or spelling (`mcp-servers` ≠ `MCP-Servers`).

### Anti-Patterns

| Anti-pattern | Problem | Fix |
|---|---|---|
| No date prefix in notes/ | Unsortable; name collisions | Always `YYYY-MM-DD_` prefix |
| External content in notes/ | Breaks routing rule | External → library/ |
| CamelCase or spaces in filename | Breaks search, breaks URLs | lowercase-hyphens only |
| Over-tagging (8+ tags) | Tags lose discriminating power | Max 7 tags |
| No frontmatter | Not searchable or filterable | Always add frontmatter block |
| One giant note per session | Hard to link; hard to find | Split into atomic notes |
| Creating archive/ as a 4th tier | Adds complexity with no value | Date prefix + git history handle this |
| Naming by topic only, no date | Topic collisions over time | Always date-prefix unless truly timeless |
| Inconsistent project names | library/ search breaks | Pick one spelling; enforce it |

---

## Quick Reference Cheatsheet

```powershell
# Create a note
brain new
brain new --tier notes --project mcp-servers --title "SSE transport fix"

# Archive an external source
brain publish brain\ai-brain\inbox\GHCP_Agents_Guide.md --project ghcp-knowledge-sharing

# Move inbox draft to notes
brain move brain\ai-brain\inbox\draft.md --tier notes

# Search
brain search java --tag generics --kind note
brain search --project mcp-servers --tier library --date 2026-03

# Housekeeping
brain status
brain list --tier notes
brain list --tier library
brain clear --force
```

## Learning Resources

| Topic | Resource |
|---|---|
| PARA method | https://fortelabs.com/blog/para/ |
| Zettelkasten intro | https://zettelkasten.de/introduction/ |
| Evergreen notes | https://notes.andymatuschak.org/Evergreen_notes |
| Building a Second Brain | https://www.buildingasecondbrain.com/ |
| ISO 8601 date standard | https://www.iso.org/iso-8601-date-and-time-format.html |
