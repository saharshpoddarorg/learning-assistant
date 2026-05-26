# Prompt Backlog: web-reader workflow patterns

## Source

- **Legacy skill:** `.github/skills/devops-tooling/web-reader/SKILL.md`
- **Section:** Patterns 1–5 (lines 86–164)

## What It Does

Step-by-step workflow procedures for handling different URL-reading modes:

1. **Single URL Full Read** — fetch full content, structure with markdown headings, preserve code blocks, add attribution
2. **Summarization** — fetch content, produce TL;DR + Key Points + Notable Details
3. **Multi-URL Comparison** — fetch multiple URLs, extract core content, produce comparison table + recommendation
4. **Documentation Extraction** — fetch with setup/config focus, present as actionable steps with prerequisites
5. **Code Example Extraction** — fetch code examples, present with language tags + context + dependencies

## Why It's a Prompt (Not a Skill)

These are sequential procedures ("when user does X, do steps 1-2-3-4") — workflow logic,
not reference knowledge. A skill should tell Copilot *what it knows*, not *what steps to follow*.

## Suggested Prompt

- **Name:** `/read-url` (already exists at `.github/prompts/tools/read-url.prompt.md`)
- **Status:** Already covered — the existing `/read-url` prompt handles all 5 modes
- **Action needed:** None — this content already has a prompt home. No new prompt required.
