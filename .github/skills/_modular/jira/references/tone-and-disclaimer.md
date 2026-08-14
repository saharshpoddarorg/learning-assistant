# Content Tone & AI Disclaimer

## Tone Rules (apply to all published content)

**Voice:** Professional, concise, objective. Active voice. Global/multilingual audience - no idioms, slang, humor, or culture-specific metaphors. Sentences <= 25 words.

**Avoid -> Prefer:**
- "guys / stuff / things / hey" -> "team / the specific artefact name"
- "ASAP / FYI" (formal) -> "at the earliest opportunity / for reference"
- "I think / I feel" -> "analysis indicates / based on results"
- "easy / just / simple" -> "straightforward / requires a single step"
- "broke / killed / nuked" -> "failed / terminated / removed"
- "awesome / cool / nailed it" -> precise factual statement
- Emojis in body/headings -> Confluence status macros where available

**Structure:**
- Headings: sentence case. Abbreviations: spell out on first use.
- Dates: `03 Apr 2026` or `2026-04-03`. Never `MM/DD/YYYY`.
- Links: descriptive text, not raw URLs.
- Tables over nested bullet lists for comparisons.

---

## AI Disclaimer Templates

**Jira / Bitbucket - append at end of comment:**

```text
_This content was generated or substantially edited with AI assistance. Please verify critical details independently._
```

**Confluence - append `note` macro at the very end of the page body:**

```html
<ac:structured-macro ac:name="note" ac:schema-version="1">
  <ac:parameter ac:name="title">AI-Assisted Content</ac:parameter>
  <ac:rich-text-body>
    <p>This content was generated or substantially edited with AI assistance. Please verify critical details independently. Report inaccuracies to the page author.</p>
  </ac:rich-text-body>
</ac:structured-macro>
```

**Disclaimer lifecycle:** Present on all AI-authored content. A human full-rewrite may remove it.

---

## Pre-Publish Checklist

- [ ] Professional, concise, objective tone; no slang/emojis
- [ ] Active voice; sentences <= 25 words
- [ ] Abbreviations spelled out on first use; unambiguous dates
- [ ] AI disclaimer present where required
