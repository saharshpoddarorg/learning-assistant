# Content Tone & AI Disclaimer

## Tone Rules (apply to all published content)

**Voice:** Professional, concise, objective. Active voice. Global/multilingual audience — no idioms, slang, humor, or culture-specific metaphors. Sentences ≤ 25 words.

**Avoid → Prefer:**
- "guys / stuff / things / hey" → "team / the specific artefact name"
- "ASAP / FYI" (formal) → "at the earliest opportunity / for reference"
- "I think / I feel" → "analysis indicates / based on results"
- "easy / just / simple" → "straightforward / requires a single step"
- "broke / killed / nuked" → "failed / terminated / removed"
- "awesome / cool / nailed it" → precise factual statement
- Emojis in body/headings → Confluence status macros where available

**Structure:**
- Headings: sentence case. Abbreviations: spell out on first use, e.g. "Capital Logic Designer (CLD)".
- Dates: `03 Apr 2026` or `2026-04-03`. Never `MM/DD/YYYY`.
- Links: descriptive text, not raw URLs.
- Tables over nested bullet lists for comparisons.

---

## AI Disclaimer Templates

**When required:**

| Content | Disclaimer |
|---|---|
| Confluence page (AI-created or substantially AI-edited) | `note` macro at **bottom** of page — required |
| Confluence comment (AI-generated) | Italic footer — required |
| Jira description/comment (AI-authored) | Italic footer — recommended |
| Bitbucket PR description/comment (AI-authored) | Italic footer — recommended |
| Internal scratch/temp files | None |

**Confluence — append `note` macro at the very end of the page body:**

```html
<ac:structured-macro ac:name="note" ac:schema-version="1">
  <ac:parameter ac:name="title">AI-Assisted Content</ac:parameter>
  <ac:rich-text-body>
    <p>This content was generated or substantially edited with AI assistance . Please verify critical details independently. Report inaccuracies to the page author.</p>
  </ac:rich-text-body>
</ac:structured-macro>
```

Yellow `note` macro. Title: `AI-Assisted Content`. No author name. No date. Always at the **bottom**.

**Jira / Bitbucket — append at end of comment:**

```
_This content was generated or substantially edited with AI assistance. Please verify critical details independently._
```

**Disclaimer lifecycle:** Present on all AI-authored pages. A human full-rewrite may remove it.

---

## Pre-Publish Checklist

- [ ] Professional, concise, objective tone; no slang/emojis
- [ ] Active voice; sentences ≤ 25 words
- [ ] Abbreviations spelled out on first use; unambiguous dates
- [ ] AI disclaimer `note` macro present at the bottom of the page (title: AI-Assisted Content)
