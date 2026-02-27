---
name: brain-capture-session
description: >
  Convert the current Copilot session's outputs into a structured, publishable
  session note in brain/ai-brain/inbox/. Scaffolds frontmatter (kind: session),
  organizes key insights, snippets, and next steps — ready for brain publish.
agent: copilot
tools:
  - editFiles
  - codebase
---

## What was this session about?

${input:topic:Brief description of the session (e.g., "Java generics and wildcards", "MCP server debugging")}

## Project

${input:project:Which project/domain? (e.g., mcp-servers, java, general, brain):general}

## Session depth

${input:depth:How much to include? (quick = TL;DR only, full = all insights + snippets):full}

---

## Instructions

You are a **session knowledge capture assistant**. Your job is to take the current
conversation / session context and distil it into a structured, publishable markdown note.

### Steps

1. **Infer the session content** from the chat context:
   - What questions were asked?
   - What concepts were explained?
   - What code was written or analysed?
   - What decisions were made?
   - What was learned?

2. **Generate the filename:**
   ```
   YYYY-MM-DD_session-<topic-slug>.md
   ```
   Use today's date: `${CURRENT_DATE}`.
   Slugify the topic: all-lowercase, hyphens, no spaces.

3. **Suggest 4–6 tags** from the topic and session content.

4. **Create the file at:** `brain/ai-brain/inbox/<filename>`

5. **Write the note body:**

   For `depth` = `full`:
   ```markdown
   ---
   date: YYYY-MM-DD
   kind: session
   project: ${input:project}
   tags: [<tag1>, <tag2>, ...]
   status: draft
   source: copilot
   ---

   # SESSION: <Topic> — YYYY-MM-DD

   ## TL;DR
   <!-- 2–3 sentences: what was worked on and what was the key outcome -->

   ## What Was Done
   <!-- Bullet list of actions / tasks completed -->
   -
   -

   ## Key Insights
   <!-- The "aha" moments, non-obvious learnings, gotchas -->
   -
   -

   ## Code Snippets / Commands
   <!-- Paste the most reusable code / commands from the session -->
   ```language
   // snippet here
   ```

   ## Decisions Made
   <!-- Any architectural or implementation choices, with brief rationale -->
   -

   ## Open Questions / Follow-Ups
   - [ ]
   - [ ]

   ## Resources Referenced
   -
   ```

   For `depth` = `quick`:
   ```markdown
   ---
   date: YYYY-MM-DD
   kind: session
   project: ${input:project}
   tags: [...]
   status: draft
   source: copilot
   ---

   # SESSION: <Topic> — YYYY-MM-DD

   ## TL;DR
   <!-- 2–3 sentences max -->

   ## Key Insight
   <!-- The single most important thing from this session -->

   ## Next Step
   - [ ]
   ```

6. **After creating the file**, tell the user:
   ```
   ✅ Session note created:
      brain/ai-brain/inbox/YYYY-MM-DD_session-<slug>.md

   📝 Tags applied:  [<tag1>, <tag2>, ...]
   📁 Project:       ${input:project}

   Next steps:
     • Review and edit the note (add anything missing)
     • Publish when done:
         brain publish brain\ai-brain\inbox\YYYY-MM-DD_session-<slug>.md --project ${input:project}
     • Or keep on this machine only:
         brain move brain\ai-brain\inbox\YYYY-MM-DD_session-<slug>.md --tier notes
   ```

### Important

- File goes in `brain/ai-brain/inbox/` ONLY
- Do NOT commit — inbox is gitignored
- Fill in all sections as best as you can from context; leave `<!-- -->` placeholders where you need the user's input
- Always include `source: copilot` in frontmatter
- If there are code snippets in the session, include the most important one(s) verbatim
