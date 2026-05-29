---
name: web-reader
description: >
  ARCHIVED LEGACY SKILL — residual content not yet migrated to a prompt.
  Sections: Essentials (fetch-read-present loop, tool best practices),
  Patterns 1-5 (single URL read, summarization, multi-URL comparison,
  docs extraction, code extraction), Content Formatting Rules,
  Combining with Other Skills.
  Modular skill: _modular/web-reader/SKILL.md
  Prompt-backlog note: prompt-backlog/web-reader-patterns.md
metadata:
  allowed-tools:
    - fetch_webpage
  output-format: markdown
---

> **ARCHIVED LEGACY SKILL** — core reference knowledge migrated to `_modular/web-reader/SKILL.md`.
> This file contains only the residual content not yet moved to a prompt.
> See `prompt-backlog/web-reader-patterns.md` for migration notes.
>
> **Sections already migrated (stripped from this file):**
> Quick Decision Card, Query Formulation Guide, Handling Failures, Security Rules.

---

## Essentials — Web Reading Quick-Start

### The Fetch-Read-Present Loop

```text
1. DETECT    → Identify URL(s) in the user's message
2. FETCH     → Use fetch_webpage tool with a clear, specific query
3. EXTRACT   → Pull the main content, stripping navigation/ads/boilerplate
4. PRESENT   → Format as clean markdown with proper headings, code blocks, lists
5. CITE      → Always attribute the source URL at the end
```

### Using the `fetch_webpage` Tool

The `fetch_webpage` tool accepts:

- **`urls`** — array of URLs to fetch content from
- **`query`** — a clear description of what content to find on the page

**Best practices:**

| Practice | Why |
|---|---|
| Use a specific `query` that describes what you're looking for | Improves content extraction relevance |
| Fetch multiple related URLs in a single call when possible | More efficient than sequential fetches |
| Handle fetch failures gracefully — inform the user if a URL is unreachable | URLs may be behind auth, broken, or rate-limited |
| Never fabricate content if a fetch fails | Honesty about what was retrieved |

---

## Patterns & Workflows — Content Processing

> **Prompt-backlog status:** These 5 patterns are deferred. The existing `/read-url` prompt
> at `.github/prompts/tools/read-url.prompt.md` may already cover them — verify before
> creating a new prompt. See `prompt-backlog/web-reader-patterns.md` for context.

### Pattern 1 — Single URL Full Read

When the user pastes a single URL and wants the full content:

```text
User: https://example.com/blog/java-virtual-threads
      "Read this for me"

Steps:
  1. fetch_webpage(urls: ["https://example.com/blog/java-virtual-threads"],
                   query: "full article content, key points, code examples")
  2. Structure the extracted content with proper markdown headings
  3. Preserve code blocks with correct language tags
  4. Add source attribution at the bottom
```

### Pattern 2 — Summarization

When the user wants a concise summary rather than full content:

```text
User: "Summarize this article" + URL

Steps:
  1. Fetch the full content
  2. Identify the key themes, arguments, and conclusions
  3. Present as:
     - **TL;DR** — 1-2 sentence summary
     - **Key Points** — 3-7 bullet points
     - **Notable Details** — any specific data, code, or quotes worth highlighting
  4. Source attribution
```

### Pattern 3 — Multi-URL Comparison

When the user shares multiple URLs to compare:

```text
User: "Compare these two approaches" + URL1 + URL2

Steps:
  1. Fetch both URLs in a single fetch_webpage call
  2. Extract the core content from each
  3. Present a comparison table:
     | Aspect | Source 1 | Source 2 |
     |--------|----------|----------|
     | Approach | ... | ... |
     | Pros | ... | ... |
     | Cons | ... | ... |
  4. Provide a recommendation or synthesis
```

### Pattern 4 — Documentation Extraction

When the user shares a documentation URL:

```text
User: "Get the setup docs from" + URL

Steps:
  1. Fetch with query focused on setup/installation/configuration
  2. Present as actionable steps with code blocks
  3. Highlight prerequisites and common pitfalls
  4. Preserve all command examples verbatim
```

### Pattern 5 — Code Example Extraction

When the user wants code from a webpage:

```text
User: "Extract the code examples from" + URL

Steps:
  1. Fetch with query focused on code examples and snippets
  2. Present each code block with:
     - Language tag on the code fence
     - Brief explanation of what the code does
     - Context for when to use it
```

---

## Content Formatting Rules

- **Preserve structure** — maintain the original heading hierarchy where possible
- **Code blocks** — always use fenced code blocks with language tags
- **Tables** — convert HTML tables to markdown tables
- **Images** — note image descriptions/alt text but don't try to render images
- **Links** — preserve important internal links as markdown links
- **Lists** — maintain ordered/unordered list structure
- **Attribution** — always end with `Source: [page title](URL)`

---

## Combining with Other Skills

| Combined with | Use case |
|---|---|
| `deep-research` | Fetch multiple sources as part of a research investigation |
| `learning-resources-vault` | Verify/update resource metadata by fetching the actual page |
| `brain-management` | Fetch content, then save key insights to brain/ai-brain/notes/ |
| `requirements-research` | Read requirement docs or competitor pages during discovery |
