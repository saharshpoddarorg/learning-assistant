```prompt
---
name: brain-publish
description: 'Publish an imported external source from inbox/ to library/ with frontmatter, tag prompting, project assignment, and git commit'
agent: copilot
tools: ['editFiles', 'codebase', 'runCommands']
---

## File to publish (imported external source)

${input:file:Path to the imported file to archive (relative to brain/ai-brain/, e.g. inbox/GHCP_Agents_Guide.md)}

## Project bucket

${input:project:Which project bucket? (e.g. mcp-servers, java, learning-assistant, general):general}

## Instructions

You are a knowledge workspace assistant. Publish an **imported external source** (slide deck, reference doc, presenter guide, etc.) from inbox/ into library/ with proper frontmatter and tagging, then commit it to the repo.

> **Routing rule:** This command is for external sources you imported (kind: ref, resource, snippet).
> For notes YOU wrote, use `/brain-new` → `notes/` tier instead.

### Steps

1. Read the file at `brain/ai-brain/${input:file}`
2. Determine if it has existing frontmatter; if not, generate it:
   - `date`: extract from filename date prefix, or today if none
   - `kind`: `ref` (default for imported docs), `resource`, or `snippet` based on content
   - `project: ${input:project}`
   - `tags`: extract keyword slugs from filename + first 20 lines; suggest 3-5 tags
   - `status: archived`
   - `source: imported`
3. Show the user the proposed frontmatter and ask to confirm or adjust tags
4. Determine destination: `brain/ai-brain/library/${input:project}/<YYYY-MM>/<YYYY-MM-DD_slug>.md`
   - YYYY-MM comes from the `date` field in frontmatter (or today if missing)
   - Slug: lowercase, hyphens, strip date prefix from original filename
5. Write the frontmatter to the file (prepend if missing, update if present)
6. Move the file to `brain/ai-brain/library/${input:project}/<YYYY-MM>/<filename>`
7. Run: `git add brain/ai-brain/library/${input:project}/<YYYY-MM>/<filename>`
8. Run: `git commit -m "brain: archive <title> to library/<project> [<tags>]\n\nProject: <project>  Kind: ref  Source: imported\n\n-- created by gpt"`
9. Confirm success and show the committed path

### Destination structure

```

brain/ai-brain/library/
  <project>/
    <YYYY-MM>/
      YYYY-MM-DD_<slug>.md

```

### Important

- Never move files outside brain/ai-brain/library/
- If the destination already exists, ask before overwriting
- Do not change the file's content, only its frontmatter and location
- After committing, tell the user the full git path of the archived file
```
