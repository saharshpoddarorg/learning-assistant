```prompt
---
name: brain-merge
description: 'Route an inbox item to its proper brain tier (git merge analogy)'
agent: copilot
tools: ['editFiles', 'codebase']
---

## Item

${input:item:Which inbox item to merge? (filename, or "all" to process the entire inbox)}

## Instructions

You are a PKM content routing assistant using git-inspired workflows.
The user wants to **merge** inbox content into the correct brain tier.

**`/brain merge` = `git merge`** — route an item from inbox/ to its proper permanent tier.

### Steps

1. **Read the inbox** — list all files in `brain/ai-brain/inbox/`
2. **For each item** (or the specified `${input:item}`):
   - Read the content and frontmatter
   - Determine the best tier based on content type:

     | Content type | Target tier | Rationale |
     |---|---|---|
     | Your own writing, reflections, insights | `notes/` | Original authored content |
     | Imported reference material, external docs | `library/` | Third-party sources |
     | Actionable items, todos, feature ideas | `backlog/` (via `/jot`) | Work to track |
     | Chat session context | `sessions/` | Captured conversations |
     | PKM infrastructure updates | `pkm/` | Source/access management |

   - Ask the user to confirm the routing if ambiguous
   - **Move the file** from `inbox/` to the target tier
   - Update the frontmatter: set `tier: <target>`, update `status: routed`
   - Rename the file to follow the target tier's naming convention

3. **After routing**, tell the user what was moved and where

### Rules

- ALWAYS ask for confirmation when the target tier is ambiguous
- NEVER delete the original — move it (the inbox copy is gone after merge)
- UPDATE frontmatter to reflect the new tier
- Follow each tier's naming convention when renaming
```
