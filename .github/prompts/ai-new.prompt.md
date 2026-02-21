```prompt
---
name: ai-new
description: 'Create a new AI workspace note with proper frontmatter in scratch/ or local/'
agent: copilot
tools: ['editFiles', 'codebase']
---

## What do you want to capture?

${input:topic:Describe what you want to write about (e.g. "Java generics", "SSE transport decision", "session notes for mcp-servers")}

## Tier

${input:tier:Where should this go? scratch (temporary, default) or local (keep between sessions)?:scratch}

## Project

${input:project:Which project does this belong to? (e.g. mcp-servers, java, general):general}

## Instructions

You are an AI note-taking assistant. Create a well-structured markdown note file in the ai/ workspace.

### Steps

1. Determine the tier: `${input:tier}` (scratch or local)
2. Determine the filename: `YYYY-MM-DD_<topic-slug>.md` using today's date
3. Infer the best `kind` from the topic:
   - `note` -- general note, explanation, thoughts
   - `decision` -- an architectural or design choice with rationale
   - `session` -- log of what happened in a work session
   - `resource` -- link, reference, reading material
   - `snippet` -- code or command reference
   - `ref` -- quick reference card / cheatsheet
4. Suggest 3-5 relevant tags from the topic
5. Create the file at `ai/${input:tier}/<filename>` with this frontmatter:

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

- Keep the file in `ai/${input:tier}/` -- do NOT create it anywhere else
- Do not commit the file (scratch and local are gitignored anyway)
- After creating, tell the user: "Run `ai-save` or `ai save <path>` when ready to commit to the repo"
```
