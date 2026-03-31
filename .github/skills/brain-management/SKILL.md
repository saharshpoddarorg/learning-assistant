---
name: brain-management
description: >
  Brain workspace management for brain/ai-brain/ — naming conventions, file organisation,
  frontmatter schema, tier routing (inbox/notes/library/sessions/backlog), timestamping best practices,
  PKM (personal knowledge management) industry standards, and structural decisions.
  Use when asked about: where to put a note, how to name a file, what frontmatter to add,
  when to use notes/ vs library/ vs sessions/ vs backlog/, how to structure the brain workspace,
  whether to create a new tier/folder, brain naming conventions, prefix/suffix usage, how to timestamp
  notes, chat session capture, session scoping (global/project/feature scope levels, widening/narrowing),
  software-dev umbrella structure (requirements/design/implementation/testing/devops activities),
  project-aware sessions, cross-referencing sessions at different scopes, backlog management
  (kanban board, sprint tracking, item status, views, CHANGELOG, time tracking, effort estimation),
  or any brain/ai-brain/ management question.
---

# Brain Management Skill

---

## Structure Decision — Why 5 Tiers

The brain/ai-brain/ workspace uses exactly **5 tiers**. The original 3 tiers (inbox/notes/library)
were extended with a `sessions/` tier for capturing valuable AI chat conversations and a
`backlog/` tier for personal agile work tracking.

| Tier | Purpose | Git |
|---|---|---|
| `inbox/` | Raw capture — gitignored, cleared each session | ❌ |
| `notes/` | Your authored writing — insights, decisions, how-tos | ✅ |
| `library/` | Imported external sources — docs, decks, references | ✅ |
| `sessions/` | Captured AI conversations — research, analysis, learning | ✅ |
| `backlog/` | Personal agile board — todos, features, sprints, ideas | ✅ |

**Extended routing questions:**

- _"Did you write it yourself?"_ → **Yes** → `notes/`
- _"Did you import it from an external source?"_ → **Yes** → `library/`
- _"Was it a valuable AI conversation?"_ → **Yes** → `sessions/`
- _"Is it work to track or plan?"_ → **Yes** → `backlog/`
- _"Not sure yet?"_ → `inbox/`

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

```text
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

```text
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

```text
brain: note <slug> [kind: session]               ← new note committed
brain: archive <slug> to library/<project>       ← external source archived
brain: update <slug> — <what changed>            ← note updated
brain: delete <slug> — <reason>                  ← note removed
brain: reorganise library/<project>/<YYYY-MM>/   ← structural change
```

### Weekly Review Pattern (Inbox Zero)

```text
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

---

## Sessions — Captured AI Chat Conversations

The `sessions/` tier captures valuable AI chat sessions that produce substantive content —
research, code analysis, complex debugging, learning deep-dives, architecture discussions.

### Session Hierarchy

```text
sessions/
  SESSION-LOG.md                      ← append-only index
  work/                               ← corporate / job-specific
    code-analysis/
    debugging/
    requirements/
    performance/
    feature-exploration/
    documentation/
    research/
    general/
  personal/                           ← personal projects & learning
    software-dev/                     ← umbrella for all s/w dev activities
      requirements/
      research/
      design/
      implementation/
      testing/
      code-review/
      devops/
      general/
    learning/
    financial/
    research/
    general/
  _templates/
    session-capture.md
    requirements-capture.md
```

### Session Naming Convention

```text
YYYY-MM-DD_HH-MMtt_<category>_<subject>[_v<N>].md
```

| Segment | Format | Example |
|---|---|---|
| Date | `YYYY-MM-DD` | `2026-03-20` |
| Time | `HH-MMtt` (12-hour, lowercase am/pm) | `10-30am`, `04-12pm` |
| Category | kebab-case, matches folder name | `code-analysis` |
| Subject | kebab-case, 3-8 words | `order-service-calculate-total` |
| Version | `_v<N>` (v2+ only) | `_v2`, `_v3` |

### Session Frontmatter

```yaml
---
date: 2026-03-20
time: "10:30 AM"
kind: session-capture
domain: work
category: code-analysis
project: learning-assistant
subject: order-service-calculate-total
tags: [java, refactoring, clean-code]
status: draft
version: 1
parent: null
complexity: high
outcomes:
  - identified code smells
  - proposed refactoring
source: copilot
---
```

### Auto-Capture Policy

Sessions are captured automatically when the conversation involves:

- Research, analysis, or technology evaluation
- Complex debugging with multi-step investigation
- Code analysis with architectural depth
- Requirements gathering or feature scoping
- Learning deep-dives or concept explanations
- Performance analysis or optimization strategies
- Documentation or design discussions

**Not captured:** simple refactoring, one-line fixes, quick questions, build commands,
routine tasks without analytical depth.

Full policy: `.github/instructions/chat-capture.instructions.md`

### Requirements Sessions (Personal Software Dev)

Requirements gathering for personal projects uses a specialised template
(`_templates/requirements-capture.md`) with structured sections:

- **Project Overview** — name, domain, target user, stage
- **Problem Statement** — user-perspective problem description
- **User Stories** — As/I want/So that with Gherkin acceptance criteria
- **NFRs** — measurable targets: performance, reliability, usability, security
- **Scope Definition** — explicit in/out MoSCoW classification
- **Dependencies & Constraints** — blockers and technical limits
- **Open Questions** — unresolved items

**When `requirements` vs `design` vs `implementation`:**

- Requirements = WHAT to build (stories, scope, criteria, NFRs)
- Design = HOW to structure it (architecture, API contracts, schemas)
- Implementation = Building it (coding, debugging, feature work)

All live under `personal/software-dev/<activity>/`.

**Naming examples:**

```text
sessions/personal/software-dev/requirements/
  2026-03-20_02-15pm_requirements_task-manager-mvp-scope.md
  2026-03-22_10-00am_requirements_task-manager-mvp-scope_v2.md
sessions/personal/software-dev/design/
  2026-03-20_03-00pm_design_task-manager-api-endpoints.md
```

**Requirements evolve** — use `_v2`, `_v3` suffixes with `parent:` linking.

### Session Versioning

| Scenario | Action |
|---|---|
| First session on subject | v1 (no suffix) |
| Continuing from v1 | `_v2` suffix, `parent:` → v1 file |
| Same area, different aspect | New file |
| Same topic, weeks later | New file |

### Sub-Package Escalation

When 5+ files exist for the same subject, group into a sub-directory:

```text
work/code-analysis/order-service/
  2026-03-20_10-30am_calculate-total.md
  2026-03-21_..._validate-order.md
```

### Session Scoping

Sessions can operate at three scope levels — `global`, `project`, or `feature` — and
scope can change mid-session. This enables fluid transitions between project-specific
requirements gathering and general-purpose learning.

| Level | Meaning | Example |
|---|---|---|
| `global` | Not tied to any project | "How does OAuth 2.0 work?" |
| `project` | Tied to a specific project | "ABSDevelopment tech stack options" |
| `feature` | Tied to a feature within a project | "ABSDevelopment login-page auth flow" |

**Scope operations:**

- **Widen** — feature → project → global (content becomes more reusable)
- **Narrow** — global → project → feature (content becomes more specific)
- **Switch** — jump to a different project/feature context entirely
- **Split** — fork a scope segment into its own session file with cross-references

**Frontmatter fields:** `scope`, `scope-project`, `scope-feature`, `scope-transitions`, `scope-refs`

**Command:** `/session-scope` (status / widen / narrow / switch / split / link / history)

Full protocol: `.github/instructions/session-scoping.instructions.md`

### Project-Aware Sessions

Sessions tied to personal software development projects support automatic project detection
and activity-based routing.

**Auto-detection triggers:**

| Signal | Action |
|---|---|
| Mentions a GitHub repo name | Set `scope: project`, `scope-project:` to repo name |
| Names a project explicitly | Set `scope: project`, route to `software-dev/<activity>/` |
| Uses keywords: MVP, feature, epic | Set `scope: feature` if specific enough |

**Activity context switching:**

Sessions naturally move between activities (requirements → design → implementation).
Use `scope-transitions` to log when you shift activities within a project session.
Fork to a new file when the new activity becomes substantial (3+ exchanges with depth).

**Tagging:** Every session uses 3-7 tags. Project-scoped sessions always include
`project:<project-name>` and optionally `gh:<owner/repo>`. Mix activity + technology tags
for discoverability.

**Project sub-packages:** When 3+ sessions exist for the same project within an activity
folder, create a project sub-directory (e.g., `software-dev/requirements/task-manager/`).

Full protocol: `.github/instructions/chat-capture.instructions.md`

---

## Backlog — Personal Agile Board (Tier 5)

The `backlog/` tier is a personal agile board with kanban columns, sprint tracking,
multiple views, and full activity logging.

### Backlog Structure

```text
backlog/
├── BOARD.md               ← Main kanban board (dashboard + visual columns)
├── CHANGELOG.md           ← Audit trail of all changes
├── views/
│   ├── by-status.md       ← Items grouped by status
│   ├── by-project.md      ← Per-project mini-boards
│   └── by-priority.md     ← Items ranked by priority
├── sprints/               ← Sprint/iteration history
├── _templates/            ← item, idea, epic, sprint, brainstorm, guide
├── features/              ← Learning-assistant feature items
├── projects/              ← Standalone personal s/w projects
├── items/                 ← General work items
├── ideas/                 ← Raw ideas + brainstorms
├── epics/                 ← Grouping themes
└── guides/                ← GHCP context guides
```

### Quick Commands

| Command | Effect |
|---|---|
| `/jot` | Capture any thought instantly |
| `/todo` | Add a concrete task with time tracking |
| `/todos` | View the board |
| `/todos status` | View by status |
| `/todos projects` | View by project |
| `/todos priority` | View by priority |
| `/todos log` | View recent CHANGELOG |
| `/backlog sprint start` | Start a sprint |
| `/backlog sprint end` | End a sprint with retrospective |

### Item Statuses

```text
todo → in-progress → in-review → done
                ↕
            blocked → archived
```

### Time Tracking

Every item tracks: `created`, `started`, `completed`, `blocked-since`, `review-since`.
Cycle time = completed − started.

### Effort Estimation

| Size | Duration |
|---|---|
| XS | < 1 hour |
| S | 1–4 hours |
| M | 0.5–2 days |
| L | 2–5 days |
| XL | 1–3 weeks |

### Update Protocol

Every status/priority change must update:

1. Item file (frontmatter + activity log section)
2. BOARD.md (kanban columns)
3. Relevant view(s) in `views/`
4. CHANGELOG.md (append new row)

Full protocol: `.github/instructions/backlog.instructions.md`

### Anti-Patterns

| Anti-pattern | Problem | Fix |
|---|---|---|
| No date prefix in notes/ | Unsortable; name collisions | Always `YYYY-MM-DD_` prefix |
| External content in notes/ | Breaks routing rule | External → library/ |
| CamelCase or spaces in filename | Breaks search, breaks URLs | lowercase-hyphens only |
| Over-tagging (8+ tags) | Tags lose discriminating power | Max 7 tags |
| No frontmatter | Not searchable or filterable | Always add frontmatter block |
| One giant note per session | Hard to link; hard to find | Split into atomic notes |
| Creating archive/ as a 5th tier | Adds complexity with no value | Date prefix + git history handle archival |
| Naming by topic only, no date | Topic collisions over time | Always date-prefix unless truly timeless |
| Inconsistent project names | library/ search breaks | Pick one spelling; enforce it |
| Capturing trivial sessions | sessions/ fills with noise | Only capture complex, analytical exchanges |
| No session versioning | Continuation sessions are disconnected | Use `_v<N>` suffix and `parent:` field |
| Sessions without outcomes | Hard to assess value later | Always list key outcomes in frontmatter |

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
