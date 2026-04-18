```prompt
---
name: brain-clone
description: 'Full import of an entire external source into brain/ai-brain (git clone analogy)'
agent: copilot
tools: ['editFiles', 'codebase', 'fetch']
---

## Source

${input:source:Which source to clone? (e.g. notion, confluence-personal, onenote)}

## Target Tier

${input:tier:Primary target tier for the cloned content? (library = reference material, notes = your authored content):library}

## Instructions

You are a PKM content operations assistant for brain consolidation.
The user wants to **clone** an entire external source into brain/ai-brain.

**`/brain clone` = `git clone`** — full structured import of an entire source.
Unlike fetch (which dumps to inbox), clone creates a structured mirror in the target tier.

### Before Cloning

1. **Read the PKM management skill** at `.github/skills/pkm-management/SKILL.md`
2. **Check sensitivity** — look up `${input:source}` in `brain/ai-brain/pkm/sensitivity-and-access-control.md`
3. **If L2 or L3** → DENY entirely. Log denial. Stop.
4. **If mixed (L0 + L1 + L2)** → Clone only L0 content, ask about L1, skip L2+
5. **Warn the user** this is a large operation — confirm before proceeding

### Cloning

1. Retrieve all accessible content from `${input:source}`
2. Create a structured sub-directory: `brain/ai-brain/${input:tier}/<source-name>/`
3. Mirror the source's folder/page hierarchy as markdown files
4. For each file, add origin frontmatter:

   ```yaml
   ---
   origin: ${input:source}
   origin-url: <url>
   origin-title: "<original title>"
   fetched-at: <today>
   last-pulled: <today>
   tier: ${input:tier}
   cloned: true
   tags: [imported, ${input:source}, cloned]
   ---
   ```

5. Create `README.md` in the cloned directory listing all imported files
6. **Log the access** in `brain/ai-brain/pkm/access-log.md`

### Content Routing During Clone

Not everything from a source belongs in one tier. Route by content type:

| Content type | Route to |
|---|---|
| Reference docs, team pages | `library/` |
| Your authored notes, reflections | `notes/` |
| Actionable items, todos | `/jot` → `backlog/` |
| Meeting notes, discussions | `notes/` or `sessions/` |

### After Cloning

Tell the user:
- Total items imported
- Breakdown by tier
- Items skipped (L2/L3 sensitivity)
- Where to find the cloned content
- Next steps: review, tag, organize

### Rules

- NEVER clone L2/L3 content
- ALWAYS create a README.md index in the cloned directory
- ALWAYS log the clone operation in access-log.md
- Preserve source structure where meaningful
- If the user can't provide direct export, guide them through the export process for that tool

```text
