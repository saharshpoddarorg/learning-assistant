```prompt
---
name: brain-fetch
description: 'Fetch content from an external capture source into brain/ai-brain/inbox/ for triage (git fetch analogy)'
agent: copilot
tools: ['editFiles', 'codebase', 'fetch']
---

## Source

${input:source:Which capture source? (e.g. notion, keep, confluence-personal, apple-notes, onenote, bookmarks, gdocs)}

## Scope

${input:scope:What to fetch? (e.g. "all L0 content", "Learning label only", "specific page title", "last 30 days"):all accessible}

## Instructions

You are a PKM content operations assistant using git-inspired workflows.
The user wants to **fetch** content from an external capture source into brain/ai-brain.

**`/brain fetch` = `git fetch`** — first-time retrieval. Content lands in `inbox/` unprocessed.

### Before Fetching

1. **Read the PKM management skill** at `.github/skills/pkm-management/SKILL.md`
2. **Check sensitivity** — look up `${input:source}` in `brain/ai-brain/pkm/sensitivity-and-access-control.md`
3. **If L2 or L3** → DENY. Inform the user. Log the denial in `access-log.md`. Stop.
4. **If L1** → ASK the user for explicit permission before proceeding
5. **If L0** → Proceed

### Fetching

1. Retrieve content from `${input:source}` (scope: `${input:scope}`)
2. For each piece of content, create a markdown file in `brain/ai-brain/inbox/`
3. Add origin frontmatter:

   ```yaml
   ---
   origin: ${input:source}
   origin-url: <url if available>
   origin-title: "<original title>"
   fetched-at: <today's date>
   status: inbox
   tags: [imported, ${input:source}]
   ---
   ```

4. **Log the access** in `brain/ai-brain/pkm/access-log.md`

### After Fetching

Tell the user:
- How many items were fetched
- Where they landed (`inbox/`)
- Next step: use `/brain-merge` to route items to their proper tiers, or `/brain-stash` to park them

### Rules

- NEVER fetch L2/L3 content — deny and log
- ALWAYS log every fetch in access-log.md
- ALWAYS inform the user what was accessed
- Content goes to inbox/ first — never directly to notes/library/backlog
- If the user hasn't provided the actual content (e.g., they say "fetch from Notion" but you can't access Notion), ask them to paste or export the content, then process it

```text
