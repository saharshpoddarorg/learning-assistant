```prompt
---
name: ai-save
description: 'Promote an AI workspace note to saved/ with tagging, project assignment, and git commit'
agent: copilot
tools: ['editFiles', 'codebase', 'runCommands']
---

## File to save

${input:file:Path to the file to promote (relative to ai/, e.g. scratch/2026-02-21_draft.md)}

## Project bucket

${input:project:Which project bucket? (e.g. mcp-servers, java, learning-assistant, general):general}

## Instructions

You are an AI workspace assistant. Promote a note from scratch/ or local/ into saved/ with proper tagging and structure, then commit it to the repo.

### Steps

1. Read the file at `ai/${input:file}`
2. Parse its existing frontmatter (date, kind, project, tags, status)
3. **Tag enrichment**:
   - Keep existing tags
   - Extract keyword slugs from the filename (strip date prefix, split on `-` and `_`, keep words > 2 chars)
   - Read the first 20 lines of content and suggest 3-5 additional relevant tags
   - Merge, deduplicate, present the full suggested tag list
4. Show the user the proposed final frontmatter and ask to confirm or adjust tags
5. Determine destination: `ai/saved/${input:project}/<YYYY-MM>/<filename>`
   - YYYY-MM comes from the `date` field in frontmatter (or today if missing)
6. Update the frontmatter in the file:
   - Set `project: ${input:project}`
   - Set `status: final` (unless user specifies otherwise)
   - Set the final tags list
7. Move the file to `ai/saved/${input:project}/<YYYY-MM>/<filename>`
8. Run: `git add ai/saved/${input:project}/<YYYY-MM>/<filename>`
9. Run: `git commit -m "Save: <title> [<tags>]\n\nProject: <project>  Kind: <kind>\n\n-- created by gpt"`
10. Confirm success and show the committed path

### Destination structure

```
ai/saved/
  <project>/
    <YYYY-MM>/
      YYYY-MM-DD_<slug>.md
```

### Important

- Never move files outside ai/saved/
- If the destination already exists, ask before overwriting
- Do not change the file's content, only its frontmatter and location
- After committing, tell the user the full git path of the saved file
```
