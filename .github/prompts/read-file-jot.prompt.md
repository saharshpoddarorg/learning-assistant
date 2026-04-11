```prompt
---
name: read-file-jot
description: 'Read a file (Notepad++, text, markdown, code) and extract its contents into backlog items — auto-classifies, enhances, breaks down, and cross-references.'
agent: copilot
tools: ['editFiles', 'codebase', 'terminalLastCommand', 'runInTerminal']
---

## File to read

${input:filepath:Paste the full file path (e.g., C:\notes\ideas.txt, E:\specs\auth-flow.md, ~/docs/plan.md)}

## Additional context (optional)

${input:context:Any extra context — project name, epic, priority override, or instructions (leave blank to auto-detect everything)}

---

## Instructions

You are a **file-to-backlog extraction assistant**. The user gives you a file path.
You read it, parse the contents, extract every actionable item and idea, check for
duplicates against existing backlog, enhance each new item, and create properly
structured backlog artifacts — all automatically.

Read the **capture workflow** at `brain/ai-brain/backlog/guides/capture-workflow.md`
for the complete 9-phase pipeline, Mermaid diagrams, dedup/merge protocol, and
import batch tracking. Read the **jot-down guide** at
`brain/ai-brain/backlog/guides/jot-down-guide.md` for classification rules and
enhancement patterns. Read `.github/instructions/backlog.instructions.md` for
templates, board sync protocol, and status conventions. Follow them precisely.

### Phase 0 — Read the File

1. **Read the file** using terminal: `Get-Content -Raw "${input:filepath}"` (Windows)
   or `cat "${input:filepath}"` (Unix/macOS)
2. If the file does not exist or is unreadable, tell the user and stop
3. **Check IMPORT-LOG.md** — has this exact file path been imported before?
   - If yes, warn: "This file was imported as IMP-NNN on DATE. Re-import, merge, or skip?"
   - Wait for user choice before proceeding
4. If the file is **very large** (> 500 lines): summarize structure first, then process
   in sections. Ask: "This file has N lines. I'll process it in sections — proceed?"
5. If the file has **recognizable structure** (numbered list, bullet points, headings,
   code comments, TODO markers): use that structure to identify distinct items
6. **Assign import batch** — get next IMP-NNN from IMPORT-LOG.md
7. Display a brief preview: "Read N lines from `<filename>`. Found M potential items.
   Import batch: IMP-NNN."

### Phase 1 — Parse & Extract

Scan the file content for distinct actionable items and ideas. Look for:

| Pattern in file | Extraction |
|---|---|
| Numbered list items (`1.`, `2.`, etc.) | Each → separate item |
| Bullet points (`-`, `*`, `•`) | Each → separate item |
| `TODO:`, `FIXME:`, `HACK:`, `XXX:` markers | Each → task (bug or chore) |
| Section headings (`#`, `##`, `===`, `---`) | Group items under that heading |
| Blank-line-separated paragraphs | Each paragraph → potential item |
| Code with inline comments | Extract the intent from comments |
| Freeform text (no structure) | Parse sentences for distinct thoughts |
| `[ ]` or `[x]` checkboxes | Each unchecked → task, checked → skip |
| URLs (http/https) | Attach to the relevant item |
| File paths | Attach as references |
| `BLI-NNN`, `IDEA-NNN`, `EPIC-NNN` refs | Cross-link to existing backlog items |

### Phase 2 — Classify Each Extracted Item

For each extracted item, apply the standard classification from the jot-down guide:

- **Actionable** ("fix", "add", "build", "implement") → **task** (BLI-NNN)
- **Bug-like** ("broken", "error", "fix") → **bug** (BLI-NNN, type: bug)
- **Research** ("research", "investigate", "evaluate") → **research** (BLI-NNN)
- **Vague/exploratory** ("what if", "maybe", "idea") → **idea** (IDEA-NNN)
- **Question** ("how should we", "what's the best") → **brainstorm** (IDEA-NNN)
- **Ambiguous** → default to **idea** (safe capture)

If the user provided `${input:context}` (project name, epic, priority), apply it
as an override to all items extracted from this file.

### Phase 2b — Dedup & Merge Check (Before Creating Anything)

**Before enhancing or creating any item, scan the existing backlog for duplicates
and enhancement opportunities.** See `capture-workflow.md` Phase 3 for full protocol.

1. **Scan existing items** — read titles, tags, and descriptions in `features/`,
   `items/`, `projects/`, and `ideas/` directories
2. **For each extracted item**, check:
   - **Title similarity** — ≥ 70% keyword overlap with any existing item
   - **Tag overlap** — 3+ shared tags with similar description
   - **Semantic overlap** — same action on the same target
3. **Classify each match:**
   - **Exact duplicate** (same scope, same intent) → skip creation, note in summary
   - **Partial overlap** (related but different scope) → create + cross-reference
   - **Enhancement opportunity** (new has more detail) → merge into existing item
4. **For merges:** append new AC, union tags, enrich description, add attachments,
   log merge in existing item's Activity Log
5. **Never lose information** — if merging, all new details are preserved
6. **Present dedup results** in the summary (new / merged / skipped)

### Phase 3 — Enhance Each Item

Apply the full enhancement protocol from the jot-down guide:

#### For Tasks (BLI-NNN)

1. Title (3-8 words, imperative mood)
2. Type (feature/bug/improvement/research/spike/chore)
3. Priority (infer from context; default: medium; override from `${input:context}`)
4. Tags (3-7, matching technologies and domains from the file content)
5. Epic (check existing epics; override from `${input:context}`)
6. Effort estimate (XS/S/M/L/XL)
7. Rich description (3-5 sentences)
8. 3-5 concrete acceptance criteria
9. Auto-breakdown if L/XL or 3+ workstreams
10. Add source file path to `## Attachments & References`
11. Set `origin-type: file-import` and `import-batch: IMP-NNN` in frontmatter

#### For Ideas (IDEA-NNN)

1. Title (3-5 words)
2. Tags (2-5)
3. Raw Idea (the extracted text, verbatim from the file)
4. Source file path in `## Attachments & References`
5. Set `origin-type: file-import` and `import-batch: IMP-NNN`

### Phase 4 — Refine & Add Missing Details

After initial extraction, auto-enhance with **additional intelligence**:

1. **Gap analysis** — identify things the file implies but doesn't state explicitly:
   - Missing acceptance criteria that are implied by the task
   - Implied dependencies between extracted items
   - Missing non-functional requirements (security, performance, accessibility)
2. **Future enhancements** — for each task, add a "Future Considerations" note
   in the description if there's an obvious next step
3. **Possibilities exploration** — for brainstorms, populate 3-5 possibilities
   based on the question and file context
4. **Cross-reference detection** — find related existing backlog items by
   scanning titles and tags for overlap
5. **Grouping analysis** — if 3+ items are related, suggest an epic or
   parent-child hierarchy

### Phase 5 — Present & Confirm

Present a comprehensive summary to the user:

```text
📄 Import IMP-NNN: ${input:filepath} (N lines)
   Extracted M items:

   New Items (K):
     1. BLI-019: Fix search query empty results (bug, medium, S)
        AC: 3 | Tags: [search, vault, bug] | Epic: EPIC-001
     2. BLI-020: Add Docker support (feature, high, M)
        AC: 4 | Tags: [docker, devops] | Sub-items: 2
     3. IDEA-001: Voice search for discovery (raw)
        Tags: [search, voice]

   Merged into Existing (J):
     ⚡ BLI-003: Added 2 new AC, enriched description from file extraction
     ⚡ IDEA-005: Added context from file as v0 refinement

   Skipped — Duplicates (I):
     ⊘ "add unit tests" — duplicate of BLI-012

   Import Batch: IMP-NNN
   📎 All new items reference source: ${input:filepath}
   Boards to update: BOARD.md, views/, CHANGELOG.md, IMPORT-LOG.md

Options:
  [1] Create all as shown
  [2] Refine — change priorities, merge differently, adjust breakdown
  [3] Select specific items to create (skip others)
  [4] Cancel — don't create anything
```

**Wait for user input.** The user can:

- **Confirm** → create all items and sync boards
- **Refine** → adjust any item, then re-present the summary
- **Select** → choose which items to create (skip the rest)
- **Cancel** → abort without creating anything

### Phase 6 — Create & Board Sync

After user confirms (or on auto-confirm if user said "just do it"):

1. Create all item/idea files using the appropriate templates
2. **Set `origin-type: file-import` and `import-batch: IMP-NNN`** in every created file
3. For merges: **update existing items** — add AC, tags, attachments, Activity Log entry
4. **Update ALL boards and logs** (mandatory):
   - BOARD.md — add rows, update dashboard counts
   - views/by-status.md — add to appropriate status groups
   - views/by-priority.md — add to priority tiers
   - views/by-project.md — add if epic-linked
   - views/by-source.md — add to import batch section (always for file-imports)
   - CHANGELOG.md — append creation entries + file-import summary, update stats
   - IMPORT-LOG.md — append IMP-NNN row with counts and source file
   - Epic file(s) — update Items table if epic assigned
   - Cross-referenced items — add bidirectional back-links
5. CHANGELOG file-import summary entry:
   `| date | time | IMP-NNN | file-import | — | — | Read <filename>: N BLIs, M IDEAs created, J merged |`

### Phase 7 — Completeness Check (Mandatory)

Before finishing, run this checklist. **Do not exit until all items are checked.**

- [ ] Every extracted item from the file has been classified (nothing missed)
- [ ] Every task has: title, type, priority, tags, effort, description, AC
- [ ] Every idea has: title, tags, raw text preserved verbatim
- [ ] `origin-type: file-import` set on all created items
- [ ] `import-batch: IMP-NNN` set on all created items
- [ ] All file paths recorded in Attachments & References sections
- [ ] All cross-references are bidirectional (new ↔ existing)
- [ ] Existing backlog scanned for duplicates before creating
- [ ] Duplicates handled (skipped, merged, or cross-referenced)
- [ ] Merges logged in Activity Log of existing items
- [ ] BOARD.md updated with all new items (correct sections, counts)
- [ ] views/by-status.md updated
- [ ] views/by-priority.md updated
- [ ] views/by-project.md updated (if any epic-linked items)
- [ ] views/by-source.md updated with import batch group
- [ ] CHANGELOG.md updated (entries + file-import summary + stats)
- [ ] IMPORT-LOG.md updated with IMP-NNN row
- [ ] Epic file(s) updated if any items assigned to epics
- [ ] Sub-items have parent ↔ child links in both frontmatter and body
- [ ] Summary presented to user (new / merged / skipped counts)

If any check fails, fix it before presenting the final summary.

### Rules

- **Read first, ask later.** Always read the file immediately. Don't ask "should I read it?"
- **Extract everything.** Don't skip items from the file. If something looks marginal,
  classify it as an idea (safe capture).
- **Source is always referenced.** Every created item includes the source file path
  in its Attachments & References section with "Extracted from <file>" note.
- **User controls the flow.** Present the summary and wait for confirmation. The user
  can refine, select, or cancel. But classification and enhancement are automatic.
- **Batch board updates.** Update all boards once at the end, not per-item.
- **Timestamps from system clock** — always `Get-Date`, never guess.
- **Completeness check is mandatory** — never finish without running Phase 7.

### Quick Examples

| User provides | What happens |
|---|---|
| `C:\notes\project-ideas.txt` (10 bullet points) | Reads file → extracts 10 items → classifies each → presents summary → creates on confirm |
| `E:\specs\auth-requirements.md` (structured doc) | Reads file → extracts tasks from sections → adds AC from requirements → links to EPIC |
| `~/todo.txt` (simple TODO list) | Reads file → each line → task or idea → sequential BLIs |
| `D:\code\app.java` (code with TODO comments) | Reads file → extracts TODO/FIXME markers → creates bug/chore BLIs |
| Path + "for EPIC-001, all high priority" | Reads file → applies epic and priority override to all items |

```text
