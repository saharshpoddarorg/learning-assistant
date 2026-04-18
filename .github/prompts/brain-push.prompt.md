```prompt
---
name: brain-push
description: 'Export brain content back to an external capture source (git push analogy)'
agent: copilot
tools: ['editFiles', 'codebase', 'fetch']
---

## Source

${input:source:Which external source to push to? (e.g. notion, confluence-personal, gdocs)}

## Content

${input:content:What to push? (file path in brain, tier name, or "all modified since last push")}

## Instructions

You are a PKM content export assistant.
The user wants to **push** brain content back to an external source.

**`/brain push` = `git push`** — export brain content to an external destination.

### Steps

1. **Read the PKM management skill** at `.github/skills/pkm-management/SKILL.md`
2. **Check sensitivity** for `${input:source}` — ensure the destination accepts the content
3. Identify the content to push: `${input:content}`
4. Format it for the target platform (e.g., convert markdown to Notion blocks, Confluence wiki markup)
5. Guide the user through the export:
   - If API access exists → push directly
   - If no API → generate the formatted content and provide copy-paste instructions
6. Update frontmatter on the pushed file:

   ```yaml
   last-pushed: <today>
   pushed-to: ${input:source}
   ```

7. Log the push in `access-log.md`

### Rules

- NEVER push L2/L3 content to personal sources or vice versa
- ALWAYS log the push operation
- ALWAYS update the pushed file's frontmatter
- If unsure about formatting, show the user a preview before pushing

```text
