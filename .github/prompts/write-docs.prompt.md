```prompt
---
name: write-docs
description: 'Create or update any markdown file — dev docs, guides, start-here, cheatsheets, skill files, slash commands — with proper 3-tier structure from raw content, notes, or a description'
agent: copilot
tools: ['codebase', 'editFiles', 'search']
---

## What are you creating or updating?
${input:docType:What type of doc? (start-here / dev-doc / guide / cheatsheet / quick-guide / skill / prompt / alias-command / readme / brain-note / all-of-above)}

## What content do you have?
${input:source:Source? (inbox-notes / session-notes / concept-description / code-analysis / url / i-will-describe)}

## What is the topic or domain?
${input:topic:What is the topic, domain, or title for the doc?}

## Target audience and depth
${input:level:Who is this for? (solo-dev / team / newbie / amateur / pro / 3-tier)}

---

## Instructions

You are a technical writing expert and developer documentation specialist. Create high-quality,
actionable documentation from the provided content, organized with 3-tier structure wherever appropriate.

Adapt output based on `docType`, `source`, `topic`, and `level`.

---

### FIRST: Understand the Source Material

When `source` is `inbox-notes` or `session-notes`:
- Search the workspace for related markdown files (`.github/`, `brain/ai-brain/inbox/`, `brain/ai-brain/notes/`)
- Read and synthesize all relevant content
- Identify: key insights, patterns, techniques, actionable takeaways, code examples

When `source` is `concept-description` or `i-will-describe`:
- Use the `topic` field as the primary subject
- Ask clarifying questions ONLY if critical info is missing
- Proceed with reasonable assumptions otherwise

When `source` is `url`:
- Use the fetch tool to retrieve the content
- Extract key points, structure, and examples

When `source` is `code-analysis`:
- Search the codebase for the relevant code
- Analyze patterns, extract knowledge, then document

---

### SECOND: Create the Appropriate File Type

---

#### When `docType` is `start-here`

Create a `START-HERE.md` file at the appropriate location.

**Structure:**
```markdown

# [Topic] — Start Here

> **You are in the right place if:** [one sentence on who this is for]
> **Time to read:** ~[N] minutes
> **Prerequisites:** [none / list]

---

## 🟢 If You're New — Read This First (5 minutes)

[The single most important thing to understand]
[The most common first action]
[Where to go next]

---

## 🗺️ What's Here

| File / Section | What it covers |
|---|---|
| [link](link.md) | Brief description |

---

## 🟡 Quick Wins — Start Doing This Today

1. [First quick win with copy-paste command/template]
2. [Second quick win]
3. [Third quick win]

---

## 🔴 Reference — When You Need More

- [Deeper doc 1](link.md) — what it covers
- [Deeper doc 2](link.md) — what it covers

---

## ❓ Common Questions

**Q: [Most common question?]**
A: [Direct answer]

**Q: [Second question?]**
A: [Answer]

```yaml

---

#### When `docType` is `dev-doc`

Create a comprehensive developer documentation file.

**Structure (3-tier):**
```markdown

# [Topic] — Developer Guide

> [One-sentence description of what this covers and who it's for]

---

## Contents

1. [🟢 Concepts (Newbie)](#-concepts-newbie)
2. [🟡 How-To (Amateur)](#-how-to-amateur)
3. [🔴 Advanced Patterns (Pro)](#-advanced-patterns-pro)
4. [Reference](#reference)

---

## 🟢 Concepts (Newbie)

### What Is [Topic]?

[Clear explanation assuming zero prior knowledge]

### Why Does It Matter?

[Concrete problem it solves + before/after comparison]

### The Mental Model

[One analogy or visual that makes it click]

### Your First [X] (Step-by-Step)

[Minimal working example — fewest steps possible]

---

## 🟡 How-To (Amateur)

### [Most Common Task 1]

[Step-by-step with code examples]

### [Most Common Task 2]

[Step-by-step]

### Common Patterns

[Table or list of patterns]

### Do's and Don'ts

| ✅ Do | ❌ Don't |
|---|---|

---

## 🔴 Advanced Patterns (Pro)

### [Advanced Topic 1]

[Deep explanation with edge cases]

### [Advanced Topic 2]

### Performance Considerations

### Security Considerations (if applicable)

### Common Pitfalls and How to Avoid Them

---

## Reference

### Quick Command Reference

[Cheatsheet — most common commands/APIs]

### See Also

- [Related doc 1](link.md)
- [Related doc 2](link.md)

```yaml

---

#### When `docType` is `guide`

Create a focused, task-oriented guide.

**Structure:**
```markdown

# How to [Accomplish Task] — [Topic] Guide

> **Goal:** [Specific outcome after reading this]
> **Prerequisites:** [List or "none"]
> **Time:** ~[N] minutes

---

## Overview

[Why you'd do this. Problem → Solution in 3 sentences.]

---

## Step-by-Step

### Step 1: [First Action]

[Clear instructions + command or code block]

### Step 2: [Second Action]

[Instructions]

### Step 3: [Third Action]

[Instructions]

---

## Variations

### [Variation A — e.g., When using X instead of Y]

[Modified steps]

### [Variation B]

[Modified steps]

---

## Troubleshooting

| Problem | Cause | Fix |
|---|---|---|
| [Error message or symptom] | [Root cause] | [Exact fix] |

---

## Next Steps

- [What to do after this guide]
- [Related guide](link.md)

```yaml

---

#### When `docType` is `cheatsheet` or `quick-guide`

Create a fast-reference document optimized for scanning.

**Structure:**
```markdown

# [Topic] — Cheat Sheet

> Quick reference for [audience]. When in doubt, check this first.

---

## Most Common Commands / Patterns

| What you want | Command / Pattern |
|---|---|
| [Goal 1] | `command or snippet` |
| [Goal 2] | `command or snippet` |

---

## The Most Important Rules

1. **[Rule 1]** — [one-sentence explanation]
2. **[Rule 2]** — [one-sentence explanation]
3. **[Rule 3]** — [one-sentence explanation]

---

## Decision Tree

```text
Need to do X?
├── If [condition A] → use [approach A]
├── If [condition B] → use [approach B]
└── If unsure → [default approach]
```

---

## Common Gotchas

- ⚠️ [Gotcha 1 — brief warning]
- ⚠️ [Gotcha 2]
- ✅ [Correct approach]

---

## Quick Templates

[Copy-paste ready template 1]

[Copy-paste ready template 2]

```yaml

---

#### When `docType` is `skill`

Create a `.github/skills/<topic>/SKILL.md` file.

**File location:** `.github/skills/<topic-as-kebab-case>/SKILL.md`

**Structure:**
```markdown

---
name: [topic-kebab-case]
description: >
  [Write this carefully — it's the activation trigger for semantic matching.]
  Use when asked about [TERM 1], [TERM 2], [TERM 3], [USE CASE 1], [USE CASE 2].
  Covers [subtopic A], [subtopic B], [subtopic C].
  Also useful for [adjacent use case].
---

# [Topic] — Skill Reference

---

## 🟢 Quick Reference

[Cheatsheet — answers 80% of questions immediately]
[Commands, patterns, quick decision table]

---

## 🟡 [Primary Topic Area]

[Full explanation with examples and code templates]

---

## 🟡 [Secondary Topic Area]

[Full explanation]

---

## 🔴 Advanced / Pro Patterns

[Deep dives, edge cases, performance, architecture decisions]

---

## Learning Resources

| Resource | Type | What It Covers |
|---|---|---|
| [Official docs](url) | Official | ... |
| [Tutorial](url) | Tutorial | ... |

```text

**Critical rule for `description` field:**
```

Formula: "Comprehensive guide to [DOMAIN].
          Covers [SUBTOPIC A], [SUBTOPIC B], [SUBTOPIC C].
          Use when asked about [TERM 1], [TERM 2], [TERM 3], or [USE CASE].
          Also activates for [ADJACENT TOPIC]."

Too vague → never activates
Too specific → misses related questions
Cover synonyms, adjacent terms, and common question patterns.

```yaml

---

#### When `docType` is `prompt` or `alias-command`

Create a `.github/prompts/<name>.prompt.md` slash command.

**File location:** `.github/prompts/<command-name>.prompt.md`

**Structure:**
```markdown

---
name: [command-name]          # User types /command-name
description: '[Brief description shown in Copilot Chat autocomplete picker]'
agent: copilot               # or specific agent name
tools: ['codebase', 'search', 'editFiles']
---

## [Primary input]

${input:topic:What topic? ([option1] / [option2] / [option3])}

## [Secondary input]

${input:goal:What goal? ([option1] / [option2])}

## [Depth input]

${input:level:Depth? (newbie / amateur / pro)}

---

## Instructions

[Main instructions for Copilot organized by input combinations]

### When `topic` is `[option1]`:

[Full instructions + structure + examples for this topic]

### When `topic` is `[option2]`:

[Instructions for this topic]

### Rules

- [Non-negotiable rule 1]
- [Rule 2]
- Always provide working, copy-paste ready examples
- Adapt depth to `level` input

```text

**Design principles for effective prompts:**
1. `${input:}` variables — collect ALL needed context upfront (users shouldn't need to re-prompt)
2. `### When X is Y:` — cover every combination of key inputs
3. `### Rules` — non-negotiable at the end (output quality gates)
4. Reference matching SKILL.md if one exists — it auto-activates for the domain
5. Name the command after the workflow, not the tool (e.g., `/write-docs` not `/markdown-creator`)

---

#### When `docType` is `readme`

Create a README.md for a component, directory, or project.

**Structure:**
```markdown

# [Component / Directory Name]

> [One-sentence description of what this is and what it's for]

---

## What Is This?

[2-3 sentences expanding on the description]

---

## Contents / Structure

```text
[directory or file tree]
```

| File/Folder | Purpose |
|---|---|
| [name](path) | Brief description |

---

## Getting Started

[Minimal steps to use this immediately]

---

## How It Works

[Key concept or mechanism explained briefly]

---

## See Also

- [Related file/doc](link.md)

```yaml

---

#### When `docType` is `brain-note`

Create a structured brain note in `brain/ai-brain/notes/`.

**File naming:** `brain/ai-brain/notes/YYYY-MM-DD_[topic-slug].md`

**Structure:**
```markdown

# [Topic] — [YYYY-MM-DD]

## Summary

[2-3 sentence summary of what this note captures]

---

## Source / Context

[Where this knowledge came from — session, reading, experiment, etc.]

---

## Key Insight #1 — [Name It]

[The insight, clearly stated]

[Supporting evidence, examples, or code]

---

## Key Insight #2 — [Name It]

[The insight]

---

## Key Insight #N — [Name It]

[The insight]

---

## Action Items

- [ ] [Something to do based on these learnings]
- [x] [Something already done]

---

## Tags

`#tag1` `#tag2` `#tag3`

```yaml

---

#### When `docType` is `all-of-above`

Analyze the source content and create the **full documentation stack**:

1. **Brain note** — raw insights distilled from source
2. **Dev doc** — comprehensive 3-tier reference
3. **Cheat sheet** — fast-reference summary
4. **SKILL.md** — domain knowledge for auto-activation
5. **Start-here** — onboarding entry point (if applicable)
6. **Prompt** (if a repeatable workflow exists)

For each file produced:
- State the exact file path
- Produce the complete, ready-to-commit file content
- Note which other files in the project should link to it

---

### THIRD: Apply 3-Tier Structure Throughout

Regardless of `docType`, always organize content at 3 tiers when `level` is `3-tier` or `team`:

```

🟢 Newbie tier: "I can do this RIGHT NOW with zero prior knowledge"
   - Most common use case
   - Minimal steps
   - Copy-paste ready commands or code
   - "When in doubt, do THIS"

🟡 Amateur tier: "I understand the full picture and can handle most situations"
   - Full workflow explanation
   - Decision guides and option tables
   - Common patterns and their trade-offs
   - Troubleshooting the top 5 issues

🔴 Pro tier: "I can reason from principles and handle edge cases"
   - Advanced patterns
   - Performance, security, architecture considerations
   - Non-obvious techniques and why they work
   - When NOT to use this approach

```yaml

---

### FOURTH: After Creating Files

1. **State the file paths** of everything created
2. **Check for cross-references** — do existing files need links to the new docs?
3. **Check slash-commands.md** — if a new prompt was created, does it need a row added?
4. **Check hub.prompt.md** — does the new command need adding to the hub navigation?
5. **Verify no duplicate coverage** — does a doc already exist that should be updated instead?

---

### Rules

- Always produce **complete, ready-to-commit file content** — not outlines or frameworks with `[FILL IN]` placeholders
- Use the `docType` to pick the right template — don't create a dev-doc when a brain-note was asked for
- Prioritize **density and scanability** — tables over prose, code blocks over inline code, bullets over paragraphs
- When creating a `SKILL.md`, the `description` field is the most important line — make it rich with synonyms and adjacent terms
- When creating a `.prompt.md`, include AT LEAST 2 `${input:}` variables to collect context (a prompt with no inputs forces the user to re-prompt)
- When `source` is inbox/session notes, distill — don't just reformat. Extract the **10x insight** from the raw content
- 3-tier structure means content that anyone can IMMEDIATELY USE at their current level, not just read
- Cross-link to related docs in the repo — standalone docs are orphans
```
