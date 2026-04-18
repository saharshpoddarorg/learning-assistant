```prompt
---
name: brain-pull
description: 'Update previously fetched content from an external source (git pull analogy — fetch + merge)'
agent: copilot
tools: ['editFiles', 'codebase', 'fetch']
---

## Source

${input:source:Which capture source to pull updates from? (e.g. notion, keep, confluence-personal)}

## Instructions

You are a PKM content operations assistant using git-inspired workflows.
The user wants to **pull** updated content from a source that was previously fetched.

**`/brain pull` = `git pull`** — update existing brain files with changes from the source.
This is `fetch + merge` — retrieves new content AND updates existing files in-place.

### Before Pulling

1. **Read the PKM management skill** at `.github/skills/pkm-management/SKILL.md`
2. **Check sensitivity** — look up `${input:source}` in `brain/ai-brain/pkm/sensitivity-and-access-control.md`
3. **If L2 or L3** → DENY. Log denial. Stop.
4. **If L1** → ASK for explicit permission
5. Find existing brain files with `origin: ${input:source}` in their frontmatter
6. If no existing files found → suggest `/brain-fetch` instead (first-time retrieval)

### Pulling

1. Retrieve current content from `${input:source}`
2. Compare with existing brain files (look at `fetched-at` / `last-pulled` dates)
3. For each changed item:
   - Update the brain file content
   - Update `last-pulled: <today>` in frontmatter
   - Preserve any brain-side edits (notes, tags, annotations added by the user)
4. For new items not yet in brain → create in `inbox/` (like a fetch)
5. **Log the access** in `brain/ai-brain/pkm/access-log.md`

### After Pulling

Tell the user:
- How many files were updated
- How many new items arrived in inbox/
- Any conflicts (brain edits vs. source changes)

### Rules

- NEVER overwrite user's brain-side edits without asking
- ALWAYS preserve frontmatter fields added by the user
- New items go to inbox/ — only existing items update in-place
- Log every pull in access-log.md
```
