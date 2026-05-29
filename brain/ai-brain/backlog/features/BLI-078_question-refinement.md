---
id: BLI-078
title: Question refinement — smarter query formulation for GHCP
status: todo
priority: medium
type: feature
created: 2026-05-29
updated: 2026-05-29
started: null
completed: null
blocked-since: null
review-since: null
epic: EPIC-006
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: M
actual-effort: null
tags: [question-refinement, query, prompt-engineering, ghcp, clarity, specificity]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-078: Question refinement — smarter query formulation for GHCP

## Description

When the user asks a question, this feature refines it — making it more specific,
well-structured, and actionable so GHCP can provide a better, more targeted answer.
Think of it as a **query preprocessor** that detects vague or broad questions and
suggests (or automatically applies) improvements: adding context, specifying scope,
disambiguating terms, and breaking compound questions into focused sub-questions.

### Future Considerations

- Learning from past refinements to improve suggestions over time
- Domain-aware refinement (Java questions refined differently from system design)
- User preference for auto-refine vs suggest-and-confirm
- Integration with the vault's concept taxonomy for term disambiguation
- Multi-turn refinement dialogue ("Did you mean X or Y?")

## Acceptance Criteria

- [ ] Detects vague or overly broad questions
- [ ] Suggests a refined, more specific version of the question
- [ ] Adds relevant context (e.g., language, framework, scope) when missing
- [ ] Can break compound questions into focused sub-questions
- [ ] Preserves the user's original intent while improving clarity
- [ ] Works transparently — user can accept, modify, or skip refinement

## Related

- BLI-079: Presentation of information — structured and distributable output
- BLI-075: Search engine — refined queries improve search results

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-05-29 | 08:58 PM | system | created | Brainstormed as part of EPIC-006 iterative feature ideation |

## Notes

- Could be implemented as a skill that activates on vague queries, or as
  a prompt preprocessing step
- The `/deep-dive` and `/learn-concept` prompts already do some implicit
  refinement — this makes it explicit and reusable
- Lightweight first version: a skill that suggests 2-3 refined alternatives.
  Advanced version: auto-refinement with confidence scoring

## Time Tracking

| Metric | Value |
|---|---|
| Estimated effort | M |
| Actual effort | — |
| Created | 2026-05-29 |
| Started | — |
| Completed | — |
| Cycle time | — |
