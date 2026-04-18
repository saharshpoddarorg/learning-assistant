# Cross-Source Workflows

> **Purpose:** Describes how content flows between multiple capture sources.
> These pipelines show the routes from initial capture to final destination.
>
> **Back to:** [PKM Hub](README.md) | [Consolidated inventory](capture-sources-inventory.md)

---

## Workflow 1 — Quick Idea Capture Pipeline

```text
Fleeting idea (anytime, anywhere)
    ↓
Google Keep / WhatsApp self-chat / ike     ← capture in < 30 seconds
    ↓  review within 48 hours
Actionable?
  ├── Work task → Jira
  ├── Personal task → ike (with reminder)
  ├── Learning topic → brain/ai-brain/backlog or Google Keep (Learning)
  ├── Project idea → brain/ai-brain/backlog/ideas
  └── Not worth keeping → delete
```

---

## Workflow 2 — Work Day Pipeline

```text
Start of day
    ↓
Check: Jira sprint board → what's assigned
Check: Teams self-chat → any overnight notes
Check: Outlook flagged → any follow-ups
    ↓
During the day
    ↓
Quick capture → Notepad++ (scratch) / Teams self-chat (links)
Meetings → OneNote (structured notes)
Tasks completed → update Jira
    ↓
End of day
    ↓
Process Notepad++ scratch → Jira / Confluence / discard
Process Teams self-chat → starred or promoted
```

---

## Workflow 3 — Learning Session Pipeline

```text
Copilot Chat / research session
    ↓
brain/ai-brain/sessions/ (auto-captured)   ← AI conversation
    ↓  distill
brain/ai-brain/notes/ (your synthesis)     ← your understanding
    ↓  if resource is reusable
brain/ai-brain/library/ (imported source)  ← external reference
```

---

## Workflow 4 — Physical-to-Digital Pipeline

```text
Physical notebook / whiteboard / A4 paper
    ↓  photo with phone (within 24 hours)
Google Keep (label: Work or relevant label)
    ↓  process within 48 hours
OneNote (work context)
    OR
brain/ai-brain/notes/ (personal learning)
    OR
Discard (if ephemeral)
```

---

## Workflow 5 — Cross-Platform Todo Pipeline

```text
Todo emerges (meeting, idea, reading, etc.)
    ↓
Where does it belong?
  ├── Work sprint item → Jira (official)
  ├── Work quick reminder → Teams self-chat
  ├── Personal task → ike (with deadline/reminder)
  ├── Personal software project task → brain/ai-brain/backlog
  ├── Learning goal → brain/ai-brain/backlog or Google Keep (Learning)
  └── Grocery / errand → Google Keep (Shopping)
```

---

## Workflow 6 — PKM-to-Backlog Pipeline (Jot Down Integration)

```text
Content discovered in any capture source
    ↓
Is it actionable work to track?
  ├── Yes → /jot "description" — creates BLI/IDEA in brain/ai-brain/backlog
  │         (auto-classifies, dedup-checks, enhances, cross-refs)
  ├── Import from file? → /read-file-jot "path" — extracts all items from file
  └── No → stays in capture source or promoted to notes/library
    ↓
Backlog item created → BOARD.md + views + CHANGELOG updated
    ↓
Sprint planning → items pulled into active sprint
```

---

## Workflow 7 — Git-Inspired Content Operations

```text
Need to import or consolidate notes from external tools?
    ↓
/brain remote                     ← list sources + sensitivity
    ↓
Check sensitivity (L0/L1/L2/L3)
  ├── L2/L3 → DENY (see sensitivity-and-access-control.md)
  ├── L1 → ASK user for explicit permission
  └── L0 → proceed
    ↓
First time? → /brain fetch <source>     ← copies to inbox/
Full import? → /brain clone <source>    ← structured mirror in library/notes/
One item? → /brain cherry-pick <source> <item>
    ↓
Content arrives in brain/ai-brain/inbox/
    ↓
/brain merge <item>               ← route to correct tier
  ├── Reference material → brain/ai-brain/library/
  ├── Actionable items → /jot → brain/ai-brain/backlog/
  ├── Learning notes → brain/ai-brain/notes/
  └── Not ready → /brain stash (park for later)
    ↓
Later: /brain diff <source>       ← check what changed
    ↓
/brain pull <source>              ← update existing files with changes
    ↓
Log everything in access-log.md
```

---

## Jot Down Integration — Source-to-Backlog Import

The existing `/jot` and `/read-file-jot` commands bridge PKM sources and backlog:

| Source | How to import | Example |
|---|---|---|
| Notepad++ scratch file | `/read-file-jot "C:\notes\todos.txt"` | Extracts items from work scratch |
| Google Keep note (text) | `/jot "pasted content from Keep"` | User pastes, system classifies |
| Teams self-chat (text) | `/jot "copied from Teams"` | User copies, system classifies |
| WhatsApp self-chat (text) | `/jot "copied from WhatsApp"` | User copies, system classifies |
| Browser bookmark | `/jot "https://example.com — topic"` | Creates reference-type item |
| Physical notebook (photo) | Phone photo → `/jot "description"` | Manual description of captured image |

**Git-inspired content commands** (extending the jot-down workflow):

| Command | Use case |
|---|---|
| `/brain fetch keep` | First-time import of Google Keep notes into inbox/ |
| `/brain clone notion` | Full structured import of all Notion pages |
| `/brain cherry-pick confluence-personal "Page Title"` | Import one specific Confluence page |
| `/brain pick keep` | Interactive: browse Keep labels, select items to jot |
| `/brain consolidate notion plan` | Create a migration plan for consolidating Notion into brain |

---

## Related Documents

- [PKM Hub](README.md) — Central index
- [sources-work.md](sources-work.md) — Work capture tools
- [sources-personal.md](sources-personal.md) — Personal capture tools
- [sensitivity-and-access-control.md](sensitivity-and-access-control.md) — Access rules
