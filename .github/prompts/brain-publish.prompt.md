```prompt
---
name: brain-publish
description: 'Publish a brain/ai-brain/ workspace note to archive/ with tagging, project assignment, and git commit'
agent: copilot
tools: ['editFiles', 'codebase', 'runCommands']
---

## File to save

${input:file:Path to the file to publish (relative to brain/ai-brain/, e.g. inbox/2026-02-21_draft.md)}

## Project bucket

${input:project:Which project bucket? (e.g. mcp-servers, java, learning-assistant, general):general}

## Instructions

You are a knowledge workspace assistant. Publish a note from inbox/ or notes/ into archive/ with proper tagging and structure, then commit it to the repo.

### Steps

1. Read the file at `brain/ai-brain/${input:file}`
2. Parse its existing frontmatter (date, kind, project, tags, status)
3. **Tag enrichment**:
   - Keep existing tags
   - Extract keyword slugs from the filename (strip date prefix, split on `-` and `_`, keep words > 2 chars)
   - Read the first 20 lines of content and suggest 3-5 additional relevant tags
   - Merge, deduplicate, present the full suggested tag list
4. Show the user the proposed final frontmatter and ask to confirm or adjust tags
5. Determine destination: `brain/ai-brain/archive/${input:project}/<YYYY-MM>/<filename>`
   - YYYY-MM comes from the `date` field in frontmatter (or today if missing)
6. Update the frontmatter in the file:
   - Set `project: ${input:project}`
   - Set `status: final` (unless user specifies otherwise)
   - Set the final tags list
7. Move the file to `brain/ai-brain/archive/${input:project}/<YYYY-MM>/<filename>`
8. Run: `git add brain/ai-brain/archive/${input:project}/<YYYY-MM>/<filename>`
9. Run: `git commit -m "brain: publish <title> [<tags>]\n\nProject: <project>  Kind: <kind>\n\n-- created by gpt"`
10. Confirm success and show the committed path

### Destination structure

```
brain/ai-brain/archive/
  <project>/
    <YYYY-MM>/
      YYYY-MM-DD_<slug>.md
```

### Important

- Never move files outside brain/ai-brain/archive/
- If the destination already exists, ask before overwriting
- Do not change the file's content, only its frontmatter and location
- After committing, tell the user the full git path of the published file
```
