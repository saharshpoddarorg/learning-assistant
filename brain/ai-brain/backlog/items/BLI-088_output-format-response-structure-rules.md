---
id: BLI-088
title: Define output format rules — response structure, density, and format-by-intent
status: todo
priority: medium
type: feature
created: 2026-07-31
updated: 2026-07-31
started: null
completed: null
blocked-since: null
review-since: null
epic: null
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: S
actual-effort: null
tags: [copilot-customization, output-format, instructions, response-structure, developer-experience]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-088: Define output format rules — response structure, density, and format-by-intent

## Description

There is a gap in the repo's customization primitive stack: `md-formatting.instructions.md`
governs how `.md` files look on disk; `steering-modes.instructions.md` governs how Copilot
works. Neither governs how **responses are structured and presented** in chat.

The idea is to codify a **request-intent → format contract** that tells Copilot:
when a user asks X type of question, produce Y shape of response — concise vs. verbose,
code-first vs. prose-first, table vs. bullets vs. numbered steps, with or without headers.

The system prompt already covers basics (no filler preambles, brief by default). This item
is only worth doing if concrete pain points are identified — see Pre-Work below.

### Brainstorm Context (2026-07-31)

Key dimensions identified in the brainstorm session:

- **Verbosity axis** — terse (command fixes, quick lookups) ↔ verbose (architecture, learning)
- **Format axis** — prose ↔ structured (table, numbered, bullets, code, mermaid)
- **Code prominence** — code-first (implement/fix) ↔ prose-first (explain/should I)
- **Response depth** — answer only ↔ answer + why ↔ explore options + trade-offs

Key request-intent → format candidates:

| Request intent | Format contract |
|---|---|
| Fix/write code | Code block, minimal prose before |
| Explain X | Prose-first, examples inline |
| Compare A vs B | Table → recommendation |
| How do I do X | Numbered steps, code inline |
| Should I use X/Y | Structured analysis → recommendation |
| Quick factual answer | 1-3 sentences, no structure |
| Architecture/design | Headers + table + optional mermaid |
| Debug session | Hypothesis → evidence → fix → prevention |
| Learning deep-dive | Progressive: overview → detail → examples |

**Three implementation options discussed:**

- **Option A** — New dedicated instruction file (`.github/instructions/output-format.instructions.md`, `applyTo: **`)
- **Option B** — New `## Output Format` section in `copilot-instructions.md` *(preferred starting point)*
- **Option C** — Per-skill/per-mode output sections (scales badly — duplication and drift)

Preliminary recommendation: **start with Option B** (section in `copilot-instructions.md`)
unless the ruleset grows large enough to justify its own file.

### Pre-Work (Required Before Implementation)

Identify **3+ concrete pain points** where Copilot's output format is wrong for the
request type. Without specific examples, the rules will be speculative and low-value.
Questions to answer:

1. What specific response shape is most annoying today?
2. When does Copilot add unwanted preamble/summary?
3. When does it give prose where you wanted code (or vice versa)?
4. When are headers used that shouldn't be (or missing where they should be)?

### Future Considerations

- Mode-specific output format overrides (e.g., `learning` mode → richer explanations;
  `debug` mode → hypothesis/evidence/fix template; `completeness` mode → checkboxes)
- Grow from section → dedicated instruction file if ruleset exceeds ~20 rules
- Could evolve into a skill containing rich format examples per pattern type
- User-controlled overrides ("give me just the code", "explain step by step")

## Acceptance Criteria

- [ ] Pre-work done: 3+ concrete pain points identified and documented
- [ ] Decision made: Option A (new file) vs Option B (section in copilot-instructions.md)
- [ ] Request-intent → format contract table defined (min 8 request types covered)
- [ ] Verbosity rules defined (when to be terse vs. verbose)
- [ ] Code-first vs. prose-first default rule defined
- [ ] No-fluff zones defined (where preamble/summary/intro must never appear)
- [ ] Mode-specific overrides scoped (in or out for this iteration)
- [ ] New rules pass the "does it change current behavior?" test — rules with no delta are cut

## Related Captures

| Type | Path | Date | Note |
|---|---|---|---|
| session | [`design/2026-07-31_09-33pm_design_copilot-output-format-primitive.md`](../sessions/personal/personal-work/software-dev/design/2026-07-31_09-33pm_design_copilot-output-format-primitive.md) | 2026-07-31 | Full brainstorm — gap analysis, 3 options, 4 output dimensions, 5 open questions |

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| related-bli | `items/BLI-089_backlog-item-workspace-cross-index-promotion-path.md` | 2026-07-31 | The `by-backlog-item` view idea (Option 2 from this BLI) is now fully defined in BLI-089 |
| related-bli | `features/BLI-079_presentation-of-information.md` | 2026-07-31 | Related but different angle — BLI-079 is about distributable/shareable output; this is about copilot instruction layer |
| instruction | `.github/instructions/md-formatting.instructions.md` | 2026-07-31 | Adjacent primitive — file formatting rules |
| instruction | `.github/instructions/steering-modes.instructions.md` | 2026-07-31 | Adjacent primitive — work style modes |
| config | `copilot-instructions.md` | 2026-07-31 | Likely home for Option B (new section here) |
