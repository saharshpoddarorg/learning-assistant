```prompt
---
name: brain-new
description: 'Create a new brain/ai-brain/ workspace note with proper frontmatter in inbox/ or notes/'
agent: copilot
tools: ['editFiles', 'codebase']
---

## What do you want to capture?

${input:topic:Describe what you want to write about (e.g. "Java generics", "SSE transport decision", "session notes for mcp-servers")}

## Tier

${input:tier:Where should this go? inbox (temporary, default) or notes (keep between sessions)?:inbox}

## Project

${input:project:Which project does this belong to? (e.g. mcp-servers, java, general):general}

## Instructions

You are a note-taking assistant. Create a well-structured markdown note file in the brain/ai-brain/ workspace.

### Steps

1. Determine the tier: `${input:tier}` (inbox or notes)
2. Determine the filename: `YYYY-MM-DD_<topic-slug>.md` using today's date
3. Infer the best `kind` from the topic:
   - `note` -- general note, explanation, thoughts
   - `decision` -- an architectural or design choice with rationale
   - `session` -- log of what happened in a work session
   - `resource` -- link, reference, reading material
   - `snippet` -- code or command reference
   - `ref` -- quick reference card / cheatsheet
4. Suggest 3-5 relevant tags from the topic
5. Create the file at `brain/ai-brain/${input:tier}/<filename>` with this frontmatter:

```markdown
---
date: <today YYYY-MM-DD>
kind: <inferred kind>
project: ${input:project}
tags: [<tag1>, <tag2>, ...]
status: draft
source: copilot
---

# <Title>

<!-- Begin notes -->
```

6. After the frontmatter, write a structured outline based on the topic:
   - For `note`: H2 sections for key concepts, with bullet points
   - For `decision`: Context / Decision / Consequences (ADR format)
   - For `session`: What was done / What was learned / Next steps
   - For `resource`: Summary / Key points / Link
   - For `snippet`: Language, code block, explanation
   - For `ref`: Table or bullet list of quick-reference items

7. Tell the user the full file path and what tags were chosen.

### Important

- Keep the file in `brain/ai-brain/${input:tier}/` -- do NOT create it anywhere else
- Do not commit the file (inbox and notes are gitignored anyway)
- After creating, tell the user: "Run `brain-publish` or `brain publish <path>` when ready to commit to the repo"
```
