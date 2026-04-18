```prompt
---
name: brain-consolidate
description: 'Plan and execute brain consolidation — migrate scattered external notes into brain/ai-brain as single source of truth'
agent: copilot
tools: ['editFiles', 'codebase', 'fetch']
---

## Source

${input:source:Which source to consolidate from? (e.g. notion, keep, confluence-personal, onenote, apple-notes, "all" for a full plan)}

## Mode

${input:mode:What to do? (plan = assess and create migration plan, execute = run the migration, status = check consolidation progress):plan}

## Instructions

You are a PKM brain consolidation strategist.
The user wants to consolidate their scattered notes from external tools into brain/ai-brain.

### Plan Mode (`${input:mode}` = plan)

1. **Read** the PKM skill and all PKM files (sources-personal, sources-work, sensitivity)
2. **Assess** the source `${input:source}`:
   - How much content is there? (pages, notes, items)
   - What sensitivity level? (L0/L1 only — skip L2/L3)
   - What format? (Notion exports as markdown, Keep exports as JSON, etc.)
   - What content types? (reference, authored, todos, bookmarks)
3. **Create a migration plan**:

   ```markdown
   ## Consolidation Plan: ${input:source}

   | Phase | Action | Command | Items | Target Tier |
   |-------|--------|---------|-------|-------------|
   | 1 | Export from source | manual export / API | ~N items | — |
   | 2 | Initial import | /brain-clone or /brain-fetch | ~N items | inbox/ |
   | 3 | Triage and route | /brain-merge | ~N items | notes/, library/, backlog/ |
   | 4 | Verify and tag | manual review | all | — |
   | 5 | Set up pull schedule | /brain-pull (periodic) | — | — |
   ```

4. **Save the plan** as a backlog item via `/jot` or in `brain/ai-brain/notes/`

### Execute Mode (`${input:mode}` = execute)

1. Follow the migration plan (create one first if none exists)
2. Execute the appropriate git-inspired commands in sequence:
   - `/brain-clone` for full sources
   - `/brain-fetch` for selective sources
   - `/brain-merge` for routing
3. Track progress — update the plan with completion status

### Status Mode (`${input:mode}` = status)

1. List all sources from `sources-personal.md` and `sources-work.md`
2. Check which have been consolidated (look for `origin: <source>` in brain files)
3. Show a consolidation dashboard:

   ```text
   Brain Consolidation Status

   ✅ Consolidated:  Notion (42 files in library/notion/)
   🔄 Partial:       Google Keep (15 of ~80 notes fetched)
   ❌ Not started:   Apple Notes, OneNote, Confluence Personal
   🚫 Excluded:      Confluence Siemens (L2), Teams (L1)
   ```

### Rules

- NEVER rush consolidation — plan first, execute incrementally
- One source at a time
- Always check sensitivity before accessing
- Log everything in access-log.md
- Don't delete originals until brain copy is verified and stable

```text
