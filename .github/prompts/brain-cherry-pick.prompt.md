```prompt
---
name: brain-cherry-pick
description: 'Selectively import one specific item from a capture source (git cherry-pick analogy)'
agent: copilot
tools: ['editFiles', 'codebase', 'fetch']
---

## Source

${input:source:Which capture source? (e.g. notion, keep, confluence-personal, gdocs)}

## Item

${input:item:Which specific item? (page title, note name, document name, URL, or label)}

## Instructions

You are a PKM content operations assistant.
The user wants to **cherry-pick** one specific item from a source into brain.

**`/brain cherry-pick` = `git cherry-pick`** — select and import ONE item, leaving the rest untouched.

### Steps

1. **Read the PKM management skill** at `.github/skills/pkm-management/SKILL.md`
2. **Check sensitivity** for `${input:source}`
3. Locate the specific item: `${input:item}` in `${input:source}`
4. Import it into `brain/ai-brain/inbox/` with origin frontmatter:

   ```yaml
   ---
   origin: ${input:source}
   origin-url: <url if available>
   origin-title: "${input:item}"
   fetched-at: <today>
   cherry-picked: true
   tags: [imported, ${input:source}, cherry-pick]
   ---
   ```

5. Log the access in `access-log.md`
6. Suggest next step: `/brain-merge` to route to the correct tier

### Rules

- NEVER cherry-pick L2/L3 content
- Only import the ONE specified item — not related pages or sub-items
- If the user provides a URL, fetch that specific page
- If the user provides a title, help them locate it first

```text
