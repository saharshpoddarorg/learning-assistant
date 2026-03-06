# PARA Method — Practical Guide for Developers

> PARA = **P**rojects · **A**reas · **R**esources · **A**rchives
> Created by Tiago Forte. Works with ANY tool: Obsidian, Notion, Logseq, Google Drive, or your file system.

---

## What is PARA?

PARA is a universal folder structure based on **actionability** — not subject matter.

| Folder | Definition | Key question |
|---|---|---|
| **Projects** | A series of tasks with a clear goal and deadline | *"Is there a deadline?"* |
| **Areas** | An ongoing responsibility with no end date | *"Is this continuous?"* |
| **Resources** | A topic of interest (reference material) | *"Might I use this later?"* |
| **Archives** | Inactive items from the above three categories | *"Am I done with this?"* |

The insight: **most knowledge systems fail because they organise by subject.** PARA organises by *when you will need it*, which dramatically reduces the friction of filing and retrieving.

---

## PARA for Software Engineers

### Projects — Active Work with Deadlines

```
Projects/
├── mcp-servers-v2/          ← Release by 2026-03-31
├── java-generics-study/     ← Finish before team review 2026-03-10
├── interview-prep-google/   ← Interview 2026-04-15
└── jdk-25-upgrade/          ← Migrate by 2026-05-01
```

Rules:
- Each project = one folder (or Notion page)
- Every project has a **next action** — the immediate physical step
- Review weekly: is this still active? Move to Archives if done.

### Areas — Ongoing Responsibilities

```
Areas/
├── Java/                    ← Always learning Java
│   ├── concurrency-notes.md
│   └── streams-cheatsheet.md
├── Architecture/            ← Ongoing interest area
│   ├── adr-log.md
│   └── design-patterns.md
├── Career/                  ← Continuous responsibility
│   ├── companies-tracker.md
│   └── skills-inventory.md
├── Health/
└── Finances/
```

Rules:
- Areas are ongoing — they never "done" and move to Archives
- Keep only **current** reference material here
- Review monthly: is this still relevant?

### Resources — Reference Material

```
Resources/
├── Books/
│   ├── clean-code-notes.md
│   └── designing-data-intensive-notes.md
├── Articles/
│   ├── java-virtual-threads-loom.md
│   └── para-method-tiago-forte.md
├── Code-Snippets/
│   ├── java-streams-patterns.md
│   └── git-rebase-workflows.md
└── Courses/
    └── system-design-primer.md
```

Rules:
- Resources are **not actionable** — you won't act on them soon
- They are reference material you might want later
- If something in Resources becomes actionable: move it to Projects or Areas

### Archives — Done or Inactive

```
Archives/
├── old-job-notes/
├── completed-courses/
│   └── java-21-upgrade-2025/
└── deprecated-projects/
    └── prototype-search-engine/
```

Rules:
- Never delete — archive instead
- Archived material is searchable (that's the point)
- Review archives quarterly: anything worth reviving?

---

## Applying PARA to Each Tool

### Obsidian

```
vault/
├── Inbox/              ← Triage before sorting
├── Projects/
├── Areas/
├── Resources/
├── Archives/
└── Templates/
```

**Tip:** Use the Dataview plugin to query across folders:

```dataview
TABLE status, due FROM "Projects"
WHERE status != "done"
SORT due ASC
```

### Notion

Create a **top-level "PARA" page** with 4 sub-pages, or use linked databases:

```
My Second Brain (root page)
├── 🎯 Projects     ← database with: status, goal, deadline, priority
├── 🔵 Areas        ← pages for each ongoing area
├── 📚 Resources    ← database with: topic, tags, URL, summary
└── 🗃 Archives     ← filter: status = archived
```

**Power tip:** Use a **single master database** with a `Type` property (Project / Area / Resource / Archive) and create filtered views for each PARA category.

### Logseq

Logseq is page-based (no folders), so PARA maps to tags:

```
#project/mcp-servers
#area/java
#resource/book
#archive
```

Use the built-in query to see all active projects:

```
{{query (and (property type project) (not (property status done)))}}
```

### `ai-brain` Workspace

The `brain/ai-brain/` workspace uses a simplified two-axis approach:

- **Tier** (inbox → notes → library) maps to **actionability**
- **Project** tag (`--project mcp-servers`) maps to PARA's "which project"

```powershell
# Capture (maps to PARA: choose Projects or Inbox)
brain new --tier inbox --project mcp-servers

# Curate (maps to PARA: distill and file properly)
brain move brain\ai-brain\inbox\note.md --tier notes

# Archive (maps to PARA: Archives = published)
brain publish brain\ai-brain\notes\note.md --project java
```

---

## PARA Workflow — Weekly Review (15 minutes)

```
Every week — run through this:

1. INBOX          → Empty your inbox: sort each item to P/A/R/A
2. PROJECTS       → Is each project still active? Any new next actions?
3. AREAS          → Any notes to add? Any stale material to archive?
4. RESOURCES      → Skim new captures — are they really reference?
5. ARCHIVES       → Nothing to do here unless you need to retrieve something
```

---

## Common PARA Mistakes (and Fixes)

| Mistake | Fix |
|---|---|
| Creating too many projects (>10) | Keep active projects under 10; rest go to Areas or Archives |
| Filing by subject instead of actionability | Ask "when will I use this?" not "what is this about?" |
| Never archiving completed projects | Schedule a monthly archive pass |
| Using Areas as a graveyard | Move dormant areas to Archives immediately |
| Not having an Inbox | Add Inbox as the zeroth folder — capture first, sort second |

---

## PARA + CODE Together

PARA handles **where things live**. CODE handles **how you process information**:

```
CAPTURE   → Drop raw content into Inbox
ORGANIZE  → Sort Inbox items into P / A / R / A
DISTILL   → Progressive summarization inside each note
EXPRESS   → Produce a deliverable (ADR, blog post, PR description, wiki page)
```

The two methods work together, not separately.

---

*Related: [START-HERE.md](START-HERE.md) · [templates.md](templates.md) · [tools-comparison.md](tools-comparison.md)*
*Copilot: `/digital-notetaking → para-method → obsidian → intermediate → windows`*
