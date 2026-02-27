# Note Templates — Developer PKM Collection

> Copy-pasteable Markdown templates for common developer note-taking scenarios.  
> Works with Obsidian, Logseq, any Markdown tool, or the `brain/ai-brain/` workspace.

---

## Template Index

| Template | Use when... |
|---|---|
| [Daily Log](#daily-log) | Sprint notes, learning journal, daily capture |
| [ADR — Architecture Decision Record](#adr) | Recording *why* a technical decision was made |
| [Code Snippet Note](#snippet) | Saving a reusable code pattern |
| [Resource Note](#resource) | Summarising a book, article, or course |
| [Debug Post-Mortem](#debug) | Root cause + resolution for a bug |
| [Meeting Notes](#meeting) | Design sessions, stand-ups, retros |
| [Learning Session](#learning-session) | After a study session (concept, video, course) |
| [Project Charter](#project-charter) | Starting a new project or area of work |

---

## Daily Log <a name="daily-log"></a>

```markdown
---
date: YYYY-MM-DD
kind: session
project: general
tags: [daily-log]
status: draft
---

# YYYY-MM-DD — Daily Log

## Top 3 for Today
- [ ] 
- [ ] 
- [ ] 

## What I Worked On
- 

## What I Learned
- 

## Questions / Stuck Points
- 

## Tomorrow
- 

## Links & Resources
- 
```

**Where to file:** `Projects/<active-project>/` or `Areas/Journal/`

---

## ADR — Architecture Decision Record <a name="adr"></a>

```markdown
---
date: YYYY-MM-DD
kind: decision
project: <project-name>
tags: [adr, architecture]
status: final
---

# ADR-NNN: <short title>

**Status:** Proposed | Accepted | Deprecated | Superseded by ADR-NNN  
**Date:** YYYY-MM-DD  
**Deciders:** <names or teams>

## Context

<!-- What is the situation driving this decision? What problem are we solving? -->

## Decision

<!-- What is the change we are proposing / making? Be specific. -->

## Consequences

### Positive
- 

### Negative / Trade-offs
- 

### Neutral
- 

## Alternatives Considered

| Alternative | Why rejected |
|---|---|
|  |  |

## References
- 
```

**Where to file:** `Areas/Architecture/ADRs/` or `Projects/<project>/decisions/`

---

## Code Snippet Note <a name="snippet"></a>

```markdown
---
date: YYYY-MM-DD
kind: snippet
project: <project-or-area>
tags: [java, streams]  ← change to your tech
status: final
---

# <Language>: <pattern name>

**Tags:** #<lang> #<concept>  
**Source:** <URL or book reference>

## The Pattern

```<language>
// Your code here
```

## When to Use
- 

## When NOT to Use
- 

## Key Points
- 

## See Also
- [[related-note]]
```

**Where to file:** `Resources/Code-Snippets/<language>/`

---

## Resource Note <a name="resource"></a>

```markdown
---
date: YYYY-MM-DD
kind: resource
project: <area-or-project>
tags: [book, java]  ← change tags
status: final
---

# Resource: <Title>

**Type:** Book | Article | Course | Video | Documentation  
**Author:** <author>  
**URL:** <link>  
**Read:** YYYY-MM-DD  
**Rating:** ⭐⭐⭐⭐⭐

## One-Line Summary

<!-- What is the single most important takeaway? -->

## Key Ideas

### 1. <Idea>
<!-- Summary in your own words -->

### 2. <Idea>
<!-- Summary in your own words -->

### 3. <Idea>
<!-- Summary in your own words -->

## Best Quotes / Highlights

> "..."

## How I Will Apply This
- 

## Related Notes
- [[]]
```

**Where to file:** `Resources/<topic>/` or `Resources/Books/`

---

## Debug Post-Mortem <a name="debug"></a>

```markdown
---
date: YYYY-MM-DD
kind: note
project: <project>
tags: [debug, post-mortem, <tech>]
status: final
---

# Debug: <short description of the bug>

**Date found:** YYYY-MM-DD  
**Time to resolve:** <duration>  
**Severity:** Low | Medium | High | Critical

## Symptom

<!-- What was observed? What was the error message? -->

## Root Cause

<!-- What actually caused the problem? -->

## Hypotheses Explored

| Hypothesis | How tested | Result |
|---|---|---|
|  |  |  |

## Fix Applied

```<language>
// Before
// After
```

## Prevention / Follow-Up

- [ ] Add test: <what test would catch this next time>
- [ ] Update docs: <what doc/comment is missing>
- [ ] Refactor: <optional — what could be cleaner>

## Timeline

| Time | Action |
|---|---|
|  |  |
```

**Where to file:** `Projects/<project>/post-mortems/` or `Areas/<tech>/bugs/`

---

## Meeting Notes <a name="meeting"></a>

```markdown
---
date: YYYY-MM-DD
kind: session
project: <project>
tags: [meeting, design]
status: final
---

# Meeting: <title>

**Date:** YYYY-MM-DD HH:MM  
**Attendees:** <names>  
**Purpose:** <one sentence>

## Agenda

1. 
2. 
3. 

## Notes

### Topic 1
- 

### Topic 2
- 

## Decisions Made

- **Decision:** <what was decided>  
  **Rationale:** <why>

## Action Items

| Action | Owner | Due |
|---|---|---|
|  |  |  |

## Next Meeting
- Date: 
- Agenda: 
```

---

## Learning Session <a name="learning-session"></a>

```markdown
---
date: YYYY-MM-DD
kind: session
project: <area>
tags: [learning, <topic>]
status: draft
---

# Learning: <topic>

**Duration:** <time spent>  
**Source:** <book/course/video/doc>  
**Level pre-session:** Newbie | Intermediate | Advanced

## What I Wanted to Learn
- 

## What I Actually Learned

### Core concept
<!-- In your own words, explain the concept as if to a colleague -->

### Key API / commands / patterns

```<language>
// Most important code from this session
```

### What surprised me
- 

## Questions that remain
- 

## Next step
- 

## Resources to follow up
- 
```

---

## Project Charter <a name="project-charter"></a>

```markdown
---
date: YYYY-MM-DD
kind: note
project: <project-name>
tags: [charter, project]
status: draft
---

# Project Charter: <project name>

**Start date:** YYYY-MM-DD  
**Target completion:** YYYY-MM-DD  
**Priority:** Low | Medium | High

## Goal

<!-- One sentence: what does "done" look like? -->

## Motivation

<!-- Why does this matter? What problem does it solve? -->

## Scope

### In scope
- 

### Out of scope
- 

## Key Milestones

| Milestone | Target date | Status |
|---|---|---|
|  |  |  |

## Success Criteria

- [ ] 
- [ ] 

## Risks

| Risk | Likelihood | Mitigation |
|---|---|---|
|  |  |  |

## Resources & References
- 
```

---

## Using Templates with the ai-brain Workspace

### From the terminal (PowerShell)

```powershell
# Load brain aliases
. .\brain\ai-brain\scripts\brain-module.psm1

# Create a new note — interactive (prompts for tier, project, kind)
brain new

# Create with explicit settings
brain new --tier notes --project mcp-servers --kind decision
```

### From Copilot Chat

```
/brain-new → topic: "ADR for transport layer choice" → tier: notes → kind: decision
```

Copilot will generate a completed ADR template using the ai-brain schema.

---

*Related: [para-method.md](para-method.md) · [START-HERE.md](START-HERE.md) · [brain/ai-brain/README.md](../ai-brain/README.md)*  
*Java source: [`brain/src/digitalnotetaking/NoteTemplate.java`](../src/digitalnotetaking/NoteTemplate.java)*
