# Team Copilot Adoption Playbook

> **Goal:** Roll out GitHub Copilot customization to a team in a way that sticks.
> **Result from real-world adoption (Capital IESD-24):** ~60-80% reduction in prompt re-engineering;
> significantly higher first-attempt code accuracy across the team.

---

## Contents

1. [🟢 Starter Kit (Newbie — Day 1)](#-starter-kit-newbie--day-1)
2. [🟡 Team Rollout (Amateur — Week 1-4)](#-team-rollout-amateur--week-1-4)
3. [🔴 Advanced Patterns (Pro — Month 2+)](#-advanced-patterns-pro--month-2)
4. [ROI Measurement](#roi-measurement)

---

## 🟢 Starter Kit (Newbie — Day 1)

### The Problem Copilot Solves for Teams (and Doesn't Solve by Default)

**Without customization:**

```
Dev A's prompt: "Add a setter. Call premodify() first, fire PropertyChangeEvent,
                use @NotNull, follow VaultKey pattern..."

Dev B's prompt: "Add a setter to Device.java"
                → Gets generic code, wrong patterns, 10 minutes fixing it

Dev C: Didn't know about premodify() at all, submits buggy PR
```

**With customization:**

```
All devs type: /cof-model  Add wireGauge setter to IDevice

→ Every dev gets the same correct, convention-compliant output.
→ No one needs to remember the context.
→ New joiners self-serve from day one.
```

### The Minimum Viable Setup (Do This First)

Get these 3 files into your `.github/` folder and commit. **Everything else can come later.**

#### File 1: `copilot-instructions.md` — The Foundation

```markdown
# Project Instructions

## Language & Framework
- Java 21+, Gradle, JUnit 4 with Mockito (NOT JUnit 5)

## Naming Conventions
- Classes: UpperCamelCase
- Methods: lowerCamelCase
- Constants: UPPER_SNAKE_CASE

## The 5 Most Critical Rules
1. [Your rule #1 — e.g., "All setters call premodify() before mutation"]
2. [Your rule #2 — e.g., "Extend AppAction, never AbstractAction"]
3. [Your rule #3 — e.g., "Use @NotNull/@Nullable on all parameters"]
4. [Your rule #4]
5. [Your rule #5]

## Commit Format
feat/fix/docs/chore(scope): Subject ≤50 chars
```

**Why this works:** Every Copilot conversation starts with these rules — for every developer, every day, without anyone thinking about it.

#### File 2: One `.instructions.md` for Your Main Coding Area

```markdown
---
applyTo: "src/**/*.java"
---
# Java Coding Standards

- [Rule specific to Java files, e.g., use try-with-resources]
- [Another rule, e.g., prefer Records for immutable data transfer objects]
- [etc.]
```

**Why this works:** When you open any Java file, these rules auto-activate. Your main coding area always gets the right conventions enforced.

#### File 3: One `SKILL.md` for Your Top Domain

```markdown
---
name: your-domain
description: >
  Use when asked about [TOPIC]. Covers [SUBTOPIC 1], [SUBTOPIC 2].
---

# [Domain] Quick Reference

## Most Common Patterns
[Cheatsheet]

## Code Templates
[Ready-to-copy templates for your most common tasks]
```

**Why this works:** When devs ask questions about your domain, Copilot loads this knowledge automatically — no manual "here's our pattern" explanation needed.

#### Commit and announce to the team:

```bash
git add .github/
git commit -m "feat(github): Add Copilot customization starter kit

Adds copilot-instructions.md, java.instructions.md, and [domain]/SKILL.md.
Every developer now gets [Project]-aware Copilot output automatically.

- created by gpt"
git push
```

Then: **"Pull the branch → get all the AI enhancements instantly."**

---

## 🟡 Team Rollout (Amateur — Week 1-4)

### Week 1: Knowledge Sharing Session

The most effective adoption technique discovered in practice is a **live demo-based session**. Not slides, not docs — live prompts with visible before/after output.

**Session structure (60-90 minutes):**

| Time | Topic |
|---|---|
| 0-10 min | The problem: "Raise your hand if you've told Copilot the same thing twice in one session..." |
| 10-20 min | Walk through the `.github/` folder structure |
| 20-40 min | LIVE DEMO: Show before/after with actual code from your project |
| 40-55 min | Walk through skills and slash commands available |
| 55-65 min | How to create your own prompt file |
| 65-75 min | Q&A |

**Demo script that works:**
1. Show the "without instructions" output first — let people identify what's wrong
2. Reveal the "with instructions" output — let the improvement speak for itself
3. Point at each correct element and name which instruction file caused it
4. One-liner prompt → 50-line correct implementation

**Opening hook (presenter notes):**
> _"Raise your hand if you've ever had to tell Copilot the same thing twice in the same session. 'We use JUnit 4 not 5', 'extend AppAction not AbstractAction', 'call premodify() before setters'... That pain is exactly what we're solving today."_

---

### Week 2-3: Add Prompt Files for Team's Most Common Tasks

Identify the 3-5 tasks developers repeat most, then create a prompt file for each.

**How to identify them:** Ask developers "What context do you always paste when using Copilot?"

Common high-value prompt files:

| Slash command | When to use |
|---|---|
| `/[domain]-model` | Creating/modifying domain entities (classes, interfaces, properties) |
| `/[domain]-service` | Creating services, handlers, processors |
| `/test-gen` | Generating unit tests following project patterns |
| `/review` | Code review checklist before raising a PR |
| `/build-help` | "I changed X — what do I build and in what order?" |
| `/help-me-choose` | Navigation prompt — "not sure which command to use?" |

**Minimal prompt file template:**

```markdown
---
name: my-domain-task
description: 'Create/modify [specific thing] following [project] patterns'
argument-hint: describe what to create or change
---

You are helping with [domain] in [project].

## Context
- [Key rule 1, e.g., "All entities must implement IEntity and extend BaseEntity"]
- [Key rule 2]
- [Key rule 3]

## Code Pattern to Follow
[Exact example of what correct output looks like]

## Steps
1. [Step 1]
2. [Step 2]
3. Always verify that [important rule] is satisfied
```

---

### Week 3-4: Create Skills for On-Demand Domain Knowledge

Skills are the "encyclopaedia" that loads automatically when relevant.

**Skills worth creating for most teams:**

| Skill | Activation keywords | What it provides |
|---|---|---|
| `build-workflow` | "build", "compile", "gradle/maven/npm" | Decision tree: when to clean/incremental build, build order for changed modules |
| `git-commands` | "git", "push", "branch", "commit" | Team-specific git conventions (remote names, branch conventions, rebase vs merge) |
| `code-review-checklist` | "review", "PR", "checklist", "before I commit" | Auto-generates review checklist from your instruction file rules |
| `test-generation` | "test", "unit test", "mock", "JUnit" | Correct test framework + patterns matching existing test files |
| `architecture-guide` | "architecture", "module", "which class/module to use" | Diagram-based module map + "which module to put this in" guide |

**Skill content formula:**

```markdown
---
name: skill-name
description: >
  Use when asked about [BUILD commands], [COMPILE], [GRADLE], [MODULE BUILD ORDER].
  Covers module-to-command mapping, clean vs incremental build decisions, and troubleshooting.
---

## 🟢 Quick Reference
[Most common commands and decisions — fits on one screen]

## 🟡 Decision Guide
[Flowchart or table for the most common decisions]

## 🔴 Advanced / Edge Cases
[Non-obvious situations and how to handle them]
```

---

### Measuring Adoption in First Month

Track these leading indicators:

| Indicator | How to measure | Target |
|---|---|---|
| Prompt file usage | Enable Copilot telemetry or survey | >50% of devs use at least 1 command/day |
| Code review iterations | PRs needing Copilot-convention fixes | <20% of PRs (vs baseline) |
| "Context re-explanation" | Survey devs: "How often do you repeat context?" | <1x per session |
| Time from task start to first PR | Story cycle time | Measurable improvement vs baseline |
| Onboarding speed | Days until new dev raises first PR | Reduce by 20-30% |

**From Capital IESD-24 experience:**
- 60-80% reduction in prompt re-engineering (devs stop pasting repeated context)
- First-attempt code accuracy significantly higher (follows team conventions immediately)
- New devs productive in days using the `/help-me-choose` prompt to self-navigate

---

## 🔴 Advanced Patterns (Pro — Month 2+)

### Custom Agents for Complex Team Workflows

Once instructions and skills are working well, invest in custom agents for your team's most complex tasks.

**Criteria for building a custom agent:**
1. Task requires reading 10+ files autonomously
2. Task has a multi-phase workflow (analysis → planning → implementation → validation)
3. Task benefits from quality gates (checkboxes that prevent incomplete output)
4. Multiple team members do this task regularly

**High-ROI agent types:**

| Agent | What it does | Team benefit |
|---|---|---|
| **Impact Analyzer** | Analyzes code change impact across modules before implementation | Catch breaking changes before they happen |
| **Test Generator** | Generates full test class matching project's test patterns | Consistent test quality across team |
| **Code Reviewer** | Reviews PR changes against instruction file rules | Catches 80% of review comments before code review |
| **PR Description Generator** | Generates PR description from git diff + affected modules | Saves 10-15 min per PR |
| **Migration Helper** | Handles API version migrations with cross-module consistency | Clean migrations without downstream breakage |

**Agent instruction layering architecture:**

```
┌─────────────────────────────────────────────────────────┐
│                  CUSTOM AGENT                            │
│                                                          │
│  Layer 1: Global                                         │
│  copilot-instructions.md (ALWAYS loaded)                │
│                                                          │
│  Layer 2: Agent-Specific                                 │
│  myagent.agent.md → loads specific instruction files    │
│  based on which modules the agent touches               │
│                                                          │
│  Layer 3: Skills (auto-detected from task)              │
│  build/SKILL.md, git/SKILL.md, domain/SKILL.md          │
│                                                          │
│  Layer 4: Hard Rules                                     │
│  Non-negotiable guardrails in agent body                │
└─────────────────────────────────────────────────────────┘
```

---

### Mermaid Diagrams for Always-On Architecture Context

The most powerful advanced pattern: embed architecture diagrams in instruction files so they're permanently loaded.

**Create `architecture-diagrams.instructions.md`:**

```markdown
---
applyTo: "**/*.java"
---
# Architecture Reference Diagrams

These diagrams are always loaded when editing Java files.

## Module Dependency Map
[Your module dependency Mermaid graph here]

## Core Entity Hierarchy
[Your entity class diagram here]

## Key Call Flows
[Your main sequence diagrams here]
```

**Why this transforms team productivity:**
- Every developer editing any Java file has full architectural context
- No one needs to know "which files implement IDevice" — the diagram tells them
- LLM can accurately reason about cross-module impact without reading every file
- New joiners immediately have architecture context that took experienced devs months to learn

**How to generate the initial diagrams:**

```
Generate a Mermaid classDiagram showing:
1. The top 5 core interfaces in [your domain]
2. Their implementation hierarchies
3. The 10 most important consumer classes

Then generate our module dependency graph as a Mermaid graph TD.
```

---

### Advanced Prompt Chaining (`#file:` References)

Link prompts together so one slash command activates multiple instructions:

```markdown
---
name: full-workflow
---

First, load the domain context:
#file:.github/prompts/my-domain.prompt.md

Then, apply the review checklist:
#file:.github/prompts/review.prompt.md

Now: ${input:task:What do you want to create or review?}
```

This lets you compose complex workflows from simpler building blocks.

---

### Confidence Badges and Quality Gates in Agents

For agents that produce high-stakes output (impact analysis, migration plans), add **mandatory quality gates**:

```markdown
## Quality Gates — Must Pass Before Output

- [ ] All file paths verified with `read_file` (no assumed paths)
- [ ] Every finding has a confidence badge (🟢/🔵/🟡/🔴)
- [ ] High-risk findings have explicit mitigation plans
- [ ] No [FILL IN] placeholders remain in the output
- [ ] Todo list shows all phases completed

DO NOT OUTPUT results until all gates are cleared.
```

**Confidence badge system (from CIA-Orchestrator pattern):**
| Badge | Meaning | Action |
|---|---|---|
| 🟢 VERIFIED | Multiple sources confirm | Ship it |
| 🔵 CONFIRMED | Primary source confirms | Likely correct |
| 🟡 SUGGESTED | Semantic/indirect signal | Verify before acting |
| 🔴 CONFLICT | Sources disagree | Must resolve manually |

---

### Team Knowledge Base — The `.github/` Folder as Living Documentation

Think of `.github/` as a living, version-controlled team knowledge base:

```
.github/
├── copilot-instructions.md     ← What every developer knows (enforced)
├── instructions/               ← What changes by file type (auto-applied)
├── skills/                     ← What the team has codified as expertise
├── prompts/                    ← Team workflow library (slash commands)
├── agents/                     ← Autonomous workers for complex tasks
└── docs/                       ← Architecture docs, guides, this file
```

**Governance model:**
- Any team member can propose a new prompt/skill via PR
- Prove it with a live demo before merging
- Retire outdated files (stale rules are actively harmful)
- Review `.github/` files quarterly as part of retrospectives

---

## ROI Measurement

### Quantitative Signals

| Metric | Before | After |
|---|---|---|
| Prompt re-engineering overhead | ~40% of Copilot time | <10% |
| First-attempt convention compliance | ~40% | >90% |
| Code review "style fix" comments | Baseline | Down 70-80% |
| Onboarding to first PR | Weeks | Days |
| New dev "which pattern should I use?" interrupts | Multiple per day | Near zero |

### How to Measure

1. **Survey (weekly for first month):** "How often did you need to explain context more than once in one session?" (scale: never / once / multiple times)
2. **PR analysis:** Flag PRs with any "convention" review comments. Track count over time.
3. **Cycle time:** Average days from task start to PR raised. Compare pre/post.
4. **Copilot telemetry:** Acceptance rate of completions (higher with better instructions = more relevant suggestions).

---

## See Also

- `.github/docs/mermaid-as-context.md` — The Mermaid diagram technique
- `.github/docs/copilot-customization-deep-dive.md` — All 6 primitives in detail
- `.github/skills/copilot-customization/SKILL.md` — Quick reference card
- `.github/prompts/copilot-customization.prompt.md` — Scaffold any customization file (`/copilot-customization`)
- `.github/prompts/write-docs.prompt.md` — Create/update any doc or guide (`/write-docs`)
