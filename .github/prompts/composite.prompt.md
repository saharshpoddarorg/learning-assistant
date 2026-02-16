```prompt
---
name: composite
description: 'Compose multiple analysis modes into a single combined session (e.g., refactor + design review)'
agent: agent
tools: ['codebase', 'usages', 'search', 'editFiles', 'problems', 'findTestFiles']
---

Run a **composite analysis** combining multiple modes on:
${file}

## Modes to Combine
${input:modes:Choose modes separated by commas — e.g., refactor, design-review, impact, explain, debug, teach}

## Instructions

Activate **each selected mode** and produce a unified report. For each mode, follow its full methodology — but merge overlapping findings instead of repeating them.

### Available Modes (apply only those selected above)

**refactor** — Identify refactoring opportunities
- Name each refactoring using standard catalog names
- Show code smells addressed, before/after, and risk
- Follow naming rules: readable, concise, context-aware, intent over implementation
- Reference: [refactor.prompt.md](refactor.prompt.md)

**design-review** — SOLID/GRASP/clean-code design review
- Evaluate SRP, OCP, LSP, ISP, DIP compliance
- Assess coupling, cohesion, design smells, testability
- Rate findings: Critical / Recommended / Consider
- Reference: [design-review.prompt.md](design-review.prompt.md)

**impact** — Change ripple-effect analysis
- Map direct callers and transitive dependencies (2+ levels)
- Rate risk per affected component: High / Medium / Low
- Recommend implementation order
- Reference: [impact.prompt.md](impact.prompt.md)

**explain** — Beginner-friendly explanation
- Purpose, line-by-line walkthrough, Java concepts used
- Reference: [explain.prompt.md](explain.prompt.md)

**debug** — Systematic bug investigation
- Gather evidence, form hypotheses, trace root cause, propose fix
- Reference: [debug.prompt.md](debug.prompt.md)

**teach** — Learn concepts from the code
- Identify concepts, explain with analogies, show pain without them, quiz
- Reference: [teach.prompt.md](teach.prompt.md)

### Cross-Cutting Skills (apply when relevant to selected modes)

Incorporate domain knowledge from available skills when applicable:
- **Design patterns** — identify pattern opportunities or misapplications ([design-patterns skill](../skills/design-patterns/))
- **Java build** — consider build/compilation implications ([java-build skill](../skills/java-build/))
- **Java debugging** — apply debugging methodology ([java-debugging skill](../skills/java-debugging/))

### Output Structure

1. **Summary** — one paragraph: what was analyzed, which modes were applied, key takeaway
2. **Combined Findings** — deduplicated, grouped by code area (class/method/block), each tagged with the mode(s) that surfaced it
3. **Priority Matrix** — table with columns: Finding | Mode(s) | Severity | Effort | Recommendation
4. **Action Plan** — ordered list of concrete steps, sequenced to minimize risk and maximize value
5. **Cross-Mode Insights** — observations that only emerge from combining modes (e.g., a refactoring that also fixes a design violation and reduces impact risk)
```
