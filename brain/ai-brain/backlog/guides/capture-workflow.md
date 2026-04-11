# Capture Workflow — Complete System Reference

> **Purpose:** Single source of truth for ALL capture workflows — manual jot, file-import,
> deduplication, merge/enhance, brainstorm, refine, and board sync. Contains every Mermaid
> diagram, flowchart, sequence diagram, and state machine for the system.
>
> **Always-on context:** This file is referenced by every capture-related prompt and
> instruction file. Read it before processing any `/jot`, `/read-file-jot`, `/todo`,
> or `/backlog` command.
>
> **Related files:**
>
> - `.github/prompts/jot.prompt.md` — universal capture slash command
> - `.github/prompts/read-file-jot.prompt.md` — file-to-backlog extraction
> - `.github/prompts/todo.prompt.md` — task alias for `/jot`
> - `.github/prompts/todos.prompt.md` — board view and status management
> - `.github/prompts/backlog.prompt.md` — advanced operations
> - `.github/instructions/backlog.instructions.md` — full backlog protocol
> - `brain/ai-brain/backlog/guides/jot-down-guide.md` — classification rules and
>   enhancement patterns (reference doc)
> - `brain/ai-brain/backlog/_templates/` — all item/idea/brainstorm/epic templates

---

## System Architecture Overview

The capture system has **two entry points** that share a common pipeline, diverge at
the import tracking layer, and converge again for dedup/merge and board sync:

```mermaid
flowchart TD
    subgraph "Entry Points"
        JOT(["/jot — Manual Capture"])
        RFJ(["/read-file-jot — File Import"])
    end

    subgraph "Shared Pipeline"
        PARSE[Phase 1: Parse & Detect]
        CLASSIFY[Phase 2: Classify]
        DEDUP[Phase 3: Dedup & Merge Check]
        ENHANCE[Phase 4: Enhance]
        REFINE[Phase 5: Refine & Gap Analysis]
        XREF[Phase 6: Cross-Reference]
        CONFIRM[Phase 7: Confirm & Refine with User]
        SYNC[Phase 8: Create & Board Sync]
        CHECK[Phase 9: Completeness Check]
    end

    JOT --> PARSE
    RFJ -->|"Read file first"| READ_FILE[Phase 0: Read File]
    READ_FILE --> PARSE

    PARSE --> CLASSIFY
    CLASSIFY --> DEDUP
    DEDUP -->|"New item"| ENHANCE
    DEDUP -->|"Duplicate found"| MERGE_DECISION{Merge or enhance\nexisting?}
    MERGE_DECISION -->|"Merge"| MERGE[Merge into existing item]
    MERGE_DECISION -->|"Enhance"| ENHANCE_EXISTING[Add details to existing]
    MERGE_DECISION -->|"Keep both"| ENHANCE
    MERGE --> XREF
    ENHANCE_EXISTING --> XREF
    ENHANCE --> REFINE
    REFINE --> XREF
    XREF --> CONFIRM
    CONFIRM --> SYNC
    SYNC --> CHECK
    CHECK -->|"Pass"| DONE([Done])
    CHECK -->|"Fail"| FIX[Fix gaps] --> CHECK
```

---

## Two Capture Tracks — Manual vs File Import

Items enter the backlog through two distinct tracks. Each is tracked separately
for auditing, traceability, and different workflow needs.

### Track Comparison

| Aspect | Manual Capture (`/jot`) | File Import (`/read-file-jot`) |
|---|---|---|
| Entry point | User types text in chat | User provides a file path |
| Origin type | `manual` | `file-import` |
| Source tracking | Optional `source-file` | Mandatory `source-file` + `import-batch` |
| Batch grouping | Optional (batch jot) | Always grouped by `import-batch: IMP-NNN` |
| Dedup trigger | Check on creation | Check before AND after extraction |
| Import log | Standard CHANGELOG entry | Special `file-import` CHANGELOG entry + import log |
| Template | Standard `item.md` / `idea.md` | Standard templates + `origin-type: file-import` |
| View tracking | Standard views | Standard views + `views/by-source.md` |

### Import Batch Tracking

Every `/read-file-jot` invocation creates an **import batch** identified by `IMP-NNN`:

```yaml
# In each created item's frontmatter:
origin-type: file-import
import-batch: IMP-001
source-file: "C:\\notes\\project-ideas.txt"
```

The import batch groups all items extracted from a single file read, enabling:

- **Traceability** — "which items came from that Notepad++ file?"
- **Bulk operations** — "show me everything from IMP-003"
- **Re-import detection** — "this file was already imported as IMP-001"

### Import Log

File imports are tracked in `brain/ai-brain/backlog/IMPORT-LOG.md`:

```markdown
| IMP-ID | Date | Time | Source File | Items Created | Ideas Created | Notes |
|---|---|---|---|---|---|---|
| IMP-001 | 2026-04-11 | 03:15 PM | C:\notes\project-ideas.txt | 5 BLIs | 3 IDEAs | Initial project ideas import |
```

---

## Phase 0 — Read File (File Import Only)

```mermaid
flowchart TD
    START([User provides file path]) --> EXISTS{File exists?}
    EXISTS -->|No| ERROR([Tell user — stop])
    EXISTS -->|Yes| SIZE{File size?}
    SIZE -->|"< 200 lines"| FULL[Read full content]
    SIZE -->|"200-500 lines"| SECTION[Summarize + process in sections]
    SIZE -->|"500+ lines"| CONFIRM_SIZE[Ask user before processing]
    CONFIRM_SIZE -->|User confirms| SECTION
    CONFIRM_SIZE -->|User declines| STOP([Stop])

    FULL --> CHECK_REIMPORT{Previously imported?\nCheck IMPORT-LOG.md}
    SECTION --> CHECK_REIMPORT

    CHECK_REIMPORT -->|"Yes — IMP-NNN exists"| WARN[Warn user:\n'This file was imported as IMP-NNN.\nRe-import, merge with existing, or skip?']
    CHECK_REIMPORT -->|No| PREVIEW

    WARN -->|Re-import| PREVIEW[Preview:\n'Read N lines. Found M items.']
    WARN -->|Merge with existing| MERGE_EXISTING[Load existing items\nfrom IMP-NNN batch]
    WARN -->|Skip| STOP2([Stop — already imported])

    MERGE_EXISTING --> PARSE_PHASE
    PREVIEW --> PARSE_PHASE([→ Phase 1: Parse & Extract])
```

### File Reading Protocol

1. **Read file** — `Get-Content -Raw "<path>"` (Windows) or `cat "<path>"`
2. **Check IMPORT-LOG.md** — has this exact file path been imported before?
3. **If re-import:** warn user with options (re-import fresh, merge, skip)
4. **Preview** — show line count and estimated item count
5. **Assign import batch** — `IMP-NNN` (next sequential from IMPORT-LOG.md)

### File Content Extraction Patterns

| File type | Extraction strategy |
|---|---|
| Plain text (`.txt`) | Split by blank lines, bullets, or numbered items |
| Markdown (`.md`) | Use headings as groups, bullets as items |
| Code (`.java`, `.py`, `.js`) | Extract `TODO`, `FIXME`, `HACK`, `XXX` comments |
| Structured notes (Notepad++) | Parse numbered lists, checkboxes, section markers |
| Mixed format | Combine strategies, classify each section |

---

## Phase 1 — Parse & Detect

Analyze input for signals before classifying:

### Attachment Detection Table

| Pattern | Type | Action |
|---|---|---|
| Windows path: `C:\...`, `E:\...` | Local file | Read with `Get-Content` |
| Unix path: `~/...`, `/home/...` | Local file | Read with `cat` |
| UNC path: `\\server\share\...` | Network file | Read with `Get-Content` |
| `http://` or `https://` | URL | Record in Attachments |
| `BLI-NNN`, `IDEA-NNN`, `EPIC-NNN` | Backlog ref | Cross-link |
| `backlog/features/...` | Backlog path | Cross-link |
| `brain/ai-brain/notes/...` | Brain note | Cross-link |
| `brain/ai-brain/sessions/...` | Session ref | Cross-link |

### Extraction from File Content

```mermaid
flowchart TD
    CONTENT([File content]) --> STRUCTURE{Has structure?}

    STRUCTURE -->|"Numbered list"| NUM[Each number → item]
    STRUCTURE -->|"Bullet points"| BULLET[Each bullet → item]
    STRUCTURE -->|"Headings"| HEADING[Group by heading]
    STRUCTURE -->|"TODO/FIXME markers"| TODO[Each marker → task]
    STRUCTURE -->|"Checkboxes"| CHECK["[ ] → task, [x] → skip"]
    STRUCTURE -->|"Paragraphs"| PARA[Each paragraph → potential item]
    STRUCTURE -->|"Code comments"| CODE[Extract intent from comments]
    STRUCTURE -->|"Freeform"| FREE[Parse sentences for thoughts]

    NUM --> ITEMS
    BULLET --> ITEMS
    HEADING --> ITEMS
    TODO --> ITEMS
    CHECK --> ITEMS
    PARA --> ITEMS
    CODE --> ITEMS
    FREE --> ITEMS

    ITEMS([Extracted item list])
```

---

## Phase 2 — Classification

### Classification Decision Tree

```mermaid
flowchart TD
    INPUT([Extracted text]) --> HAS_VERB{Has action verb?\nfix/add/build/implement/create}
    HAS_VERB -->|Yes| IS_BUG{Bug language?\nbroken/error/crash}
    HAS_VERB -->|No| IS_RESEARCH{Research language?\nresearch/investigate/evaluate}

    IS_BUG -->|Yes| BUG([Bug → BLI type:bug])
    IS_BUG -->|No| TASK([Task → BLI type:feature/improvement])

    IS_RESEARCH -->|Yes| RESEARCH([Research → BLI type:research])
    IS_RESEARCH -->|No| IS_QUESTION{Question form?\nhow should/what's the best}

    IS_QUESTION -->|Yes| BRAINSTORM([Brainstorm → IDEA brainstorm])
    IS_QUESTION -->|No| IS_BATCH{Multiple items?\nlist/comma-separated}

    IS_BATCH -->|Yes| BATCH([Batch → classify each])
    IS_BATCH -->|No| IS_LARGE{Large scope?\n3+ workstreams}

    IS_LARGE -->|Yes| EPIC([Epic-scale → Parent + subs])
    IS_LARGE -->|No| IS_VAGUE{Exploratory?\nwhat if/maybe/eventually}

    IS_VAGUE -->|Yes| IDEA([Idea → IDEA raw])
    IS_VAGUE -->|No| DEFAULT([Default → Idea\nprefer safe capture])
```

### Classification Signal Table

| Priority | Signal | Classification | Creates |
|---|---|---|---|
| 1 | Action verb: "fix", "add", "build", "implement" | **task** | BLI-NNN |
| 2 | Bug language: "broken", "error", "crash" | **bug** | BLI-NNN (type: bug) |
| 3 | Research: "research", "investigate", "evaluate" | **research** | BLI-NNN (type: research) |
| 4 | Question: "how should we", "what's the best way" | **brainstorm** | IDEA-NNN (brainstorm) |
| 5 | Batch: numbered list, comma-separated | **batch** | Multiple items |
| 6 | Large scope: 3+ workstreams | **epic-scale** | Parent + sub-items |
| 7 | Exploratory: "what if", "maybe", "eventually" | **idea** | IDEA-NNN |
| 8 | Ambiguous (default) | **idea** | IDEA-NNN |

### Artifact → Folder Routing

| Classification | Template | Folder |
|---|---|---|
| task / bug / research | `_templates/item.md` | `features/` or `projects/` or `items/` |
| brainstorm | `_templates/brainstorm.md` | `ideas/` |
| idea | `_templates/idea.md` | `ideas/` |
| epic-scale | `_templates/item.md` (parent + children) | `features/` or `projects/` |

---

## Phase 3 — Deduplication & Merge Check

**Before creating ANY new item, check the existing backlog for duplicates and
enhancement opportunities.** This prevents duplication and enriches existing items.

### Dedup & Merge Flowchart

```mermaid
flowchart TD
    NEW([New item to create]) --> SCAN[Scan existing backlog:\n• features/\n• items/\n• projects/\n• ideas/]

    SCAN --> TITLE_MATCH{Title similarity?\n≥ 70% keyword overlap}
    TITLE_MATCH -->|Yes| CANDIDATE[Candidate found]
    TITLE_MATCH -->|No| TAG_MATCH{Tag overlap?\n≥ 3 shared tags +\nsimilar description}

    TAG_MATCH -->|Yes| CANDIDATE
    TAG_MATCH -->|No| NO_MATCH([No duplicate → create new item])

    CANDIDATE --> COMPARE[Compare new vs existing:\n• Scope overlap\n• AC overlap\n• Description overlap]

    COMPARE --> MATCH_TYPE{Match type?}

    MATCH_TYPE -->|"Exact duplicate\n(same scope, same intent)"| EXACT_DUP
    MATCH_TYPE -->|"Partial overlap\n(related but different scope)"| PARTIAL
    MATCH_TYPE -->|"Enhancement opportunity\n(new has more detail)"| ENHANCE_OPP

    EXACT_DUP[Exact Duplicate] --> DUP_ACTION{Action?}
    DUP_ACTION -->|"Skip"| SKIP([Skip — note in summary])
    DUP_ACTION -->|"Merge details into existing"| MERGE_IN[Add new details\nto existing item]

    PARTIAL[Partial Overlap] --> PARTIAL_ACTION{Action?}
    PARTIAL_ACTION -->|"Create anyway + cross-ref"| CREATE_XREF([Create new\n+ link to existing])
    PARTIAL_ACTION -->|"Merge into existing"| MERGE_IN

    ENHANCE_OPP[Enhancement Opportunity] --> ENHANCE_ACTION{Action?}
    ENHANCE_ACTION -->|"Enhance existing"| ENHANCE_EXIST[Update existing item:\n• Merge AC\n• Merge tags\n• Enrich description\n• Add attachments]
    ENHANCE_ACTION -->|"Create as sub-item"| SUB_ITEM([Create as child\nof existing item])

    MERGE_IN --> LOG_MERGE[Log merge in Activity Log\nof existing item]
    ENHANCE_EXIST --> LOG_MERGE
    LOG_MERGE --> MERGE_DONE([Merge complete])
```

### Dedup Scanning Protocol

For each item about to be created:

1. **Scan titles** — compare against all existing BLI/IDEA titles
   - Keyword extraction: strip stop words, compare remaining keywords
   - ≥ 70% keyword overlap → candidate for duplicate
2. **Scan tags** — if 3+ tags match AND descriptions are similar → candidate
3. **Scan descriptions** — look for semantic overlap (same action on same target)
4. **Check import history** — if `source-file` matches a previous import, flag it

### Merge Protocol

When merging new content into an existing item:

1. **Compare AC** — add any new AC that the existing item doesn't have
2. **Compare tags** — union the tag sets (keep all)
3. **Enrich description** — if new item has more detail, append to existing description
4. **Add attachments** — merge attachment tables (no duplicates)
5. **Update Activity Log** — log the merge with source reference:

```markdown
| 2026-04-11 | 03:15 PM | system | merged | Merged details from IMP-001 extraction (file: C:\notes\ideas.txt) |
```

6. **Update `updated` date** — set to today
7. **Cross-reference** — if not merging fully, add bidirectional link

### Dedup Decision Matrix

| Scenario | Default Action | User Override |
|---|---|---|
| Exact title + exact scope | Skip (note in summary) | User can force create |
| Similar title, broader scope | Create as parent/child | User can merge |
| Same domain, different angle | Create + cross-reference | User can merge |
| New details for existing item | Enhance existing | User can create separate |
| File re-import (same IMP source) | Warn + merge option | User can re-import fresh |

---

## Phase 4 — Enhancement

### Task Enhancement (BLI-NNN) — Full Field Protocol

| Field | Required | Source | Default |
|---|---|---|---|
| Title | Yes | Derived, 3-8 words imperative | — |
| Type | Yes | Inferred from signals | `feature` |
| Priority | Yes | Urgency signals | `medium` |
| Tags | Yes | 3-7 from content/domain | — |
| Epic | Conditional | Match existing epics | `null` |
| Sprint | No | Manual assignment only | `null` |
| Effort | Yes | Complexity assessment | — |
| Description | Yes | 3-5 sentences | — |
| AC | Yes | 3-5 testable criteria | — |
| Breakdown | Conditional | If L/XL or 3+ workstreams | — |
| origin-type | Yes | `manual` or `file-import` | `manual` |
| import-batch | Conditional | File-import only | `null` |
| source-file | Conditional | When file/path involved | `null` |

### Idea Enhancement

| Field | Required | Source |
|---|---|---|
| Title | Yes | 3-5 word descriptive phrase |
| Tags | Yes | 2-5 inferred |
| Raw Idea | Yes | User's exact words verbatim |
| origin-type | Yes | `manual` or `file-import` |
| import-batch | Conditional | File-import only |

### Auto-Breakdown Rules

```mermaid
flowchart TD
    ITEM([Enhanced item]) --> SIZE{Effort?}
    SIZE -->|"XS / S / M"| KEEP([Keep as single item])
    SIZE -->|"L / XL"| CHECK_WS{Distinct workstreams?}
    CHECK_WS -->|"< 3"| KEEP
    CHECK_WS -->|"3+"| BREAK[Create parent + sub-items]

    BREAK --> PARENT[Parent BLI:\n• sub-items: list in frontmatter\n• Sub-Items table in body\n• Effort = sum of children]
    BREAK --> CHILD1[Child BLI-NNN+1:\n• parent: in frontmatter\n• Own title, AC, tags, effort]
    BREAK --> CHILD2[Child BLI-NNN+2]
    BREAK --> CHILD3[Child BLI-NNN+3]
```

---

## Phase 5 — Refine & Gap Analysis

After enhancement, auto-detect and fill gaps:

### Gap Analysis Checklist

| Gap Type | Detection | Auto-Fix |
|---|---|---|
| Missing AC | Task has < 3 criteria | Add implied from description |
| Missing NFRs | No security/performance/a11y | Add relevant NFRs as AC |
| Implied dependencies | References another system | Add to Related, note dependency |
| Missing error handling | Involves user input / external calls | Add "handles error gracefully" AC |
| Missing rollback | Data migration / schema change | Add "rollback plan documented" AC |
| Future work obvious | Clear next step not mentioned | Add Future Considerations note |

### Possibilities Exploration (Brainstorms)

For brainstorm items, auto-populate:

1. **3-5 concrete possibilities** with brief pros/cons
2. **Constraints** identified from context
3. **Wild ideas** — 2-3 unconventional approaches
4. **Emerging direction** — most promising and why
5. **Next actions** — concrete evaluation steps

### Grouping Analysis

```mermaid
flowchart TD
    ITEMS(["3+ items from same input"]) --> RELATED{Are they related?}
    RELATED -->|"Same feature area"| PARENT[Create parent BLI\n+ sub-item BLIs]
    RELATED -->|"Same theme, different features"| EPIC[Suggest new EPIC\nto group them]
    RELATED -->|"Unrelated"| FLAT[Keep as flat\nindependent items]
```

---

## Phase 6 — Cross-Referencing

### Cross-Reference Sources

| Source | Location | Match On |
|---|---|---|
| Existing BLIs | `features/`, `items/`, `projects/` | Title, tags, description |
| Existing IDEAs | `ideas/` | Title, tags |
| Existing EPICs | `epics/` | Theme, tags |
| Brain notes | `brain/ai-brain/notes/` | Topic, title |
| Sessions | `brain/ai-brain/sessions/` | Subject, tags |
| Attachments | Files/URLs provided | Direct reference |
| Import batches | IMPORT-LOG.md | Same source file |

### Bidirectional Link Protocol

```mermaid
flowchart LR
    A[New BLI-019] -->|"links to"| B[Existing BLI-003]
    B -->|"back-link"| A
    A -->|"assigned to"| C[EPIC-001]
    C -->|"Items table row"| A
    A -->|"references"| D[IDEA-005]
    D -->|"Related section"| A
    A -->|"same batch"| E[BLI-020\nIMP-001 sibling]
```

All cross-references are **bidirectional**. When A links B, B links back to A.

---

## Phase 7 — Confirm & Refine (User Interaction)

### Interaction Sequence

```mermaid
sequenceDiagram
    participant A as AI Assistant
    participant U as User

    A->>U: 📋 Summary: N items classified & enhanced<br/>(shows dedup results, merges, new items)

    Note over U: User reviews summary

    alt Confirm all
        U->>A: "looks good" / "create all"
        A->>A: Create items + board sync
    else Refine
        U->>A: "change BLI-019 to high priority"
        A->>A: Update draft
        A->>U: Updated summary
    else Select specific
        U->>A: "only items 1, 3, 5"
        A->>A: Create selected + board sync
    else Merge differently
        U->>A: "merge item 2 into BLI-003 instead"
        A->>A: Adjust merge plan
        A->>U: Updated summary
    else Cancel
        U->>A: "cancel"
        A->>U: Aborted — nothing created
    end

    A->>U: ✅ Final summary
```

### Summary Format — Manual Capture

```text
Jotted N item(s):

  New Items:
    1. BLI-019: Fix search query empty results (bug, medium, S)
       → features/BLI-019_fix-search-query-empty-results.md
       AC: 3 | Tags: [search, vault, bug] | Epic: EPIC-001

  Merged/Enhanced:
    ⚡ BLI-003: Added 2 new AC, 3 tags from jot input

  Skipped (duplicates):
    ⊘ "fix search results" — duplicate of BLI-003

Boards updated: BOARD.md, views/, CHANGELOG.md
```

### Summary Format — File Import

```text
📄 Import IMP-001: C:\notes\project-ideas.txt (42 lines)
   Extracted 8 items:

  New Items (5):
    1. BLI-019: Fix search query empty results (bug, medium, S)
    2. BLI-020: Add Docker support (feature, high, M)
    3. BLI-021: Implement auth flow (feature, high, L → 3 sub-items)
    4. IDEA-001: Voice search for discovery (raw)
    5. IDEA-002: How to handle caching? (brainstorm)

  Merged into Existing (2):
    ⚡ BLI-003: Added deployment steps from file extraction
    ⚡ IDEA-005: Enriched with additional context from file

  Skipped (1):
    ⊘ "add unit tests" — duplicate of BLI-012

  Import Batch: IMP-001
  All items reference source: C:\notes\project-ideas.txt
  Boards to update: BOARD.md, views/, CHANGELOG.md, IMPORT-LOG.md
```

---

## Phase 8 — Create & Board Sync

### Board Sync Sequence

```mermaid
sequenceDiagram
    participant F as Item Files
    participant B as BOARD.md
    participant VS as views/by-status
    participant VP as views/by-priority
    participant VJ as views/by-project
    participant VO as views/by-source
    participant C as CHANGELOG.md
    participant IL as IMPORT-LOG.md
    participant E as Epic Files
    participant X as Cross-ref Items

    Note over F,X: Phase 8 — Board Sync (after user confirms)

    F->>F: Create item/idea files
    F->>B: Add rows + update dashboard counts
    F->>VS: Add to status group (Todo)
    F->>VP: Add to priority tier
    F->>VJ: Add to project section (if epic-linked)
    F->>VO: Add to source section (file-import only)
    F->>C: Append entries + update stats
    F->>IL: Append import row (file-import only)
    F->>E: Update Items table (if epic)
    F->>X: Add bidirectional back-links
```

### Board Update Targets

| Target | Manual Capture | File Import |
|---|---|---|
| `BOARD.md` | Always | Always |
| `views/by-status.md` | Always (BLIs) | Always (BLIs) |
| `views/by-priority.md` | Always (BLIs) | Always (BLIs) |
| `views/by-project.md` | When epic-linked | When epic-linked |
| `views/by-source.md` | Not updated | Always — tracks import batches |
| `CHANGELOG.md` | Standard entry | Standard entries + `file-import` summary |
| `IMPORT-LOG.md` | Not updated | Always — new IMP-NNN row |
| Epic files | When epic assigned | When epic assigned |
| Existing items | When cross-referenced | When cross-referenced or merged |

### CHANGELOG Entry Formats

Standard creation:

```markdown
| 2026-04-11 | 03:15 PM | BLI-019 | created | — | todo | Fix search query empty results (bug, medium) |
```

File import summary:

```markdown
| 2026-04-11 | 03:15 PM | IMP-001 | file-import | — | — | Read C:\notes\ideas.txt: 5 BLIs, 3 IDEAs created, 2 merged |
```

Merge into existing:

```markdown
| 2026-04-11 | 03:16 PM | BLI-003 | merged | — | todo | Enhanced with details from IMP-001 (2 new AC, 3 tags) |
```

---

## Phase 9 — Completeness Check (Mandatory)

**Never exit without running this. Fix any failure before final summary.**

### Item-Level

- [ ] Every extracted item classified (nothing skipped without user's explicit choice)
- [ ] Every BLI has: id, title, status, priority, type, created, updated, effort,
  tags (3-7), description (3-5 sentences), AC (3-5)
- [ ] Every IDEA has: id, title, status, created, updated, tags (2-5), Raw Idea verbatim
- [ ] Every brainstorm has: question, 3-5 possibilities, constraints, next actions
- [ ] L/XL items broken down with parent ↔ child links
- [ ] `origin-type` set correctly (`manual` or `file-import`)
- [ ] `import-batch` set for file-import items

### Dedup & Merge

- [ ] Existing backlog scanned for duplicates before creating
- [ ] Duplicates handled (skipped, merged, or cross-referenced)
- [ ] Merges logged in Activity Log of existing items
- [ ] No information lost during merge (new AC, tags, details preserved)

### Attachments & References

- [ ] All file paths in `## Attachments & References` tables
- [ ] All URLs recorded
- [ ] File-import items reference source with "Extracted from `<file>`" note
- [ ] All cross-references bidirectional

### Board Sync

- [ ] BOARD.md updated — rows + dashboard counts
- [ ] views/by-status.md updated
- [ ] views/by-priority.md updated
- [ ] views/by-project.md updated (if epic-linked)
- [ ] views/by-source.md updated (file-import only)
- [ ] CHANGELOG.md updated — entries + stats
- [ ] IMPORT-LOG.md updated (file-import only)
- [ ] Epic files updated (if assigned)

### Timestamps & IDs

- [ ] All dates from system clock (`Get-Date`)
- [ ] All IDs sequential — checked BOARD.md for highest
- [ ] Import batch ID sequential — checked IMPORT-LOG.md for highest
- [ ] Activity Logs have creation entries with timestamps

---

## Item Lifecycle — State Machine

```mermaid
stateDiagram-v2
    [*] --> todo : Created via /jot or /read-file-jot
    todo --> in_progress : Start work
    in_progress --> blocked : Blocker found
    blocked --> in_progress : Unblocked
    in_progress --> in_review : Ready for review
    in_review --> done : Verified
    in_review --> in_progress : Rework needed
    todo --> archived : Cancelled
    in_progress --> archived : Abandoned
    blocked --> archived : Won't fix
    done --> [*]
    archived --> [*]

    note right of todo
        Fields set: created, updated
        Board: Backlog column
    end note

    note right of in_progress
        Fields set: started
        Board: In Progress column
    end note

    note right of done
        Fields set: completed
        Cycle time calculated
        Board: Done column
    end note
```

---

## Idea Lifecycle

```mermaid
stateDiagram-v2
    [*] --> raw : Captured via /jot or /read-file-jot
    raw --> refining : First refinement (v1)
    refining --> refining : Further refinement (v2, v3...)
    refining --> refined : Clear scope, AC possible
    refined --> promoted : /backlog promote
    raw --> parked : Shelved for later
    refining --> parked : Shelved for later
    parked --> raw : Resumed
    raw --> archived : Discarded
    refining --> archived : Discarded
    promoted --> [*]
    archived --> [*]

    note right of promoted
        Creates BLI-NNN with origin: IDEA-NNN
    end note
```

---

## Brainstorm & Refine Workflow

```mermaid
flowchart LR
    RAW([Raw idea captured]) --> V1[v1 Refinement:\nClarity pass]
    V1 --> V2[v2 Refinement:\nScope & unknowns]
    V2 --> V3[v3 Refinement:\nAC drafted]
    V3 --> PROMOTE{Ready to promote?}
    PROMOTE -->|Yes| BLI[Promote → BLI-NNN\nwith full enhancement]
    PROMOTE -->|No| VN[vN Refinement:\nFurther detail]
    VN --> PROMOTE

    subgraph "Brainstorm extras"
        BRAIN_Q([Question framed]) --> POSS[3-5 Possibilities]
        POSS --> CONSTRAINTS[Constraints identified]
        CONSTRAINTS --> WILD[Wild ideas]
        WILD --> DIRECTION[Emerging direction]
        DIRECTION --> NEXT[Next actions]
    end
```

---

## File Import — Full Sequence Diagram

```mermaid
sequenceDiagram
    participant U as User
    participant A as AI Assistant
    participant T as Terminal
    participant BK as Existing Backlog
    participant F as New Item Files
    participant BD as Boards & Views
    participant IL as IMPORT-LOG.md

    U->>A: /read-file-jot "C:\notes\ideas.txt"
    A->>T: Get-Content -Raw "C:\notes\ideas.txt"
    T-->>A: File contents (N lines)

    A->>IL: Check: was this file imported before?
    IL-->>A: No previous import (or IMP-002 exists)

    Note over A: Phase 1 — Parse & Extract
    A->>A: Identify M distinct items

    Note over A: Phase 2 — Classify each
    A->>A: task / bug / idea / brainstorm / research

    Note over A: Phase 3 — Dedup & Merge Check
    A->>BK: Scan existing items for duplicates
    BK-->>A: Found 2 candidates
    A->>A: Decide: 1 merge, 1 skip, M-2 new

    Note over A: Phase 4 — Enhance new items
    A->>A: title, type, priority, tags, effort, AC

    Note over A: Phase 5 — Refine & Gap Analysis
    A->>A: gap analysis, future considerations

    Note over A: Phase 6 — Cross-Reference
    A->>BK: Find related items
    A->>A: Add bidirectional links

    A->>U: 📄 Summary: M items extracted<br/>N new, 1 merged, 1 skipped<br/>[1] Create [2] Refine [3] Select [4] Cancel

    U->>A: Create all

    Note over A: Phase 8 — Create & Board Sync
    A->>F: Create item/idea files (origin-type: file-import, import-batch: IMP-003)
    A->>BK: Merge details into existing BLI-003
    A->>BD: Update BOARD.md, views/, CHANGELOG.md
    A->>IL: Append IMP-003 row

    Note over A: Phase 9 — Completeness Check
    A->>A: Verify all checks pass

    A->>U: ✅ Created N items, merged 1, skipped 1
```

---

## Merge/Enhance Existing — Detailed Protocol

When the dedup check finds an existing item that overlaps with a new item:

### Merge Types

| Type | When | Action |
|---|---|---|
| **AC Merge** | New item has AC the existing one doesn't | Append new AC to existing item |
| **Tag Merge** | New item has tags the existing one doesn't | Union tag sets on existing item |
| **Description Enrich** | New item has richer detail | Append or replace description sections |
| **Attachment Add** | New item has attachments existing doesn't | Add rows to existing Attachments table |
| **Full Absorb** | New item is a subset of existing | Skip creation, note in summary |
| **Sub-Item Convert** | New item is a sub-task of existing | Create as child BLI with parent link |

### Merge Sequence

```mermaid
sequenceDiagram
    participant N as New Item (draft)
    participant E as Existing Item
    participant L as Activity Log

    Note over N,E: Merge Decision Made

    N->>E: Compare AC lists
    E->>E: Append unique new AC

    N->>E: Compare tag sets
    E->>E: Union tags (deduplicated)

    N->>E: Compare descriptions
    E->>E: Enrich with new detail (if richer)

    N->>E: Transfer attachments
    E->>E: Add new attachment rows

    E->>L: Log merge entry with source
    E->>E: Update 'updated' date

    Note over N: New item NOT created (absorbed)
    Note over E: Existing item enriched
```

### Safety Rules for Merging

1. **Never lose information** — all new AC, tags, and attachments are preserved
2. **Never modify Raw Idea** — if merging into an IDEA, only add to Refinements
3. **Always log** — every merge gets an Activity Log entry with actor `system`
4. **User can override** — at the confirm phase, user can reject a merge
5. **Preserve existing structure** — don't reorganize existing item's sections

---

## ID Schemas

### Item IDs

| Type | Prefix | Sequence Source | Example |
|---|---|---|---|
| Backlog item | `BLI-` | BOARD.md Kanban section | BLI-019 |
| Idea | `IDEA-` | BOARD.md Ideas section | IDEA-001 |
| Epic | `EPIC-` | BOARD.md Epics section | EPIC-004 |
| Guide | `GUIDE-` | guides/ folder | GUIDE-001 |
| Sprint | `SPRINT-` | sprints/ folder | SPRINT-001 |

### Import Batch IDs

| Prefix | Sequence Source | Example |
|---|---|---|
| `IMP-` | IMPORT-LOG.md | IMP-001 |

IDs are sequential and **never reused**, even for archived or deleted items.

---

## Template Field Reference

### Common Fields (All Templates)

```yaml
origin-type: manual          # "manual" (from /jot) or "file-import" (from /read-file-jot)
import-batch: null           # IMP-NNN — only set for file-import items
source-file: null            # Full file path — set when file involved
```

### BLI-NNN (item.md) Full Fields

```yaml
id: BLI-NNN
title: Short imperative title
status: todo
priority: medium
type: feature
created: YYYY-MM-DD
updated: YYYY-MM-DD
started: null
completed: null
blocked-since: null
review-since: null
epic: null
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: null
actual-effort: null
tags: []
origin-type: manual
import-batch: null
source-file: null
```

### IDEA-NNN (idea.md / brainstorm.md) Full Fields

```yaml
id: IDEA-NNN
title: Short descriptive title
status: raw
type: brainstorm             # only for brainstorm template
created: YYYY-MM-DD
updated: YYYY-MM-DD
tags: []
promoted-to: null
origin-type: manual
import-batch: null
source-file: null
```

---

## File & Location Index

| What | Where |
|---|---|
| This workflow doc | `brain/ai-brain/backlog/guides/capture-workflow.md` |
| Classification & enhancement rules | `brain/ai-brain/backlog/guides/jot-down-guide.md` |
| Item template | `brain/ai-brain/backlog/_templates/item.md` |
| Idea template | `brain/ai-brain/backlog/_templates/idea.md` |
| Brainstorm template | `brain/ai-brain/backlog/_templates/brainstorm.md` |
| Epic template | `brain/ai-brain/backlog/_templates/epic.md` |
| Board | `brain/ai-brain/backlog/BOARD.md` |
| Changelog | `brain/ai-brain/backlog/CHANGELOG.md` |
| Import log | `brain/ai-brain/backlog/IMPORT-LOG.md` |
| Views | `brain/ai-brain/backlog/views/` |
| Source view | `brain/ai-brain/backlog/views/by-source.md` |
| Backlog instructions | `.github/instructions/backlog.instructions.md` |
| Jot prompt | `.github/prompts/jot.prompt.md` |
| Read-file-jot prompt | `.github/prompts/read-file-jot.prompt.md` |
| Todo prompt (alias) | `.github/prompts/todo.prompt.md` |
| Todos prompt (board) | `.github/prompts/todos.prompt.md` |
| Backlog prompt (advanced) | `.github/prompts/backlog.prompt.md` |
