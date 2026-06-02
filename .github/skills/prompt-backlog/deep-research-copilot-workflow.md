# Prompt Backlog: deep-research Copilot workflow

## Source

- **Legacy skill:** `.github/skills/dev-process/deep-research/SKILL.md`
- **Sections not migrated to a modular skill:**
  - `### Copilot-Assisted Research Workflow` (lines 304–322) — multi-step recipe chaining
    `/learn-concept` → `/deep-dive` → `/resources` → `/learn-from-docs` → `/design-review`
    → `/brain-new` → `/check-standards`.
  - `### Research Prompts for Copilot` (lines 324–336) — example prompt strings for
    Copilot to perform hypothesis-driven research, technology evaluation, and spike
    planning end-to-end.

## What It Does

A cookbook showing how to use existing Copilot primitives to run a complete research
investigation from "I have a question" to "decision captured":

1. **Concept fundamentals** via `/learn-concept`
2. **Progressive layered exploration** via `/deep-dive`
3. **Curated resource discovery** via `/resources`
4. **Official-source consultation** via `/learn-from-docs`
5. **Architecture sanity check** via `/design-review` (with POC code if applicable)
6. **Decision / findings capture** via `/brain-new` (ADR or Spike Findings)
7. **Verification of the captured note** via `/check-standards`

The original content also included three example prompt strings that Copilot itself
would consume — hypothesis-driven research, full-rubric tech evaluation, and spike plan
construction.

## Why It Belongs in a Prompt, Not a Skill

- It is a **workflow** (a sequence of slash-command invocations), not knowledge.
- It hard-codes orchestration of other primitives (`/learn-concept`, `/deep-dive`,
  `/resources`, `/learn-from-docs`, `/design-review`, `/brain-new`, `/check-standards`).
  That coupling belongs in a prompt, not a skill.
- The knowledge *behind* the workflow now lives in dedicated modular skills:
  - `investigation-methodology` — hypothesis-driven framework, source hierarchy, spike stories
  - `technology-evaluation` — the evaluation rubric and weighted trade-off matrix
  - `decision-records` — ADR + Spike Findings doc templates
  - `root-cause-analysis` — RCA / post-mortem methodology
  - `code-investigation` — code-analysis methodology
- Mirrors precedent set by `requirements-research-copilot-workflow.md`.

## Suggested Prompt

- **Name:** `research-plan`
- **Location:** `.github/prompts/research-plan.prompt.md`
- **Inputs:**
  - `topic` (string) — what to investigate
  - `mode` (`hypothesis` | `tech-eval` | `spike` | `full-pipeline`) — drives the chain
  - `level` (`newbie` | `amateur` | `pro`) — depth of each step
- **Structure:**
  - Dispatch on `mode`:
    - `hypothesis` → emit a Copilot prompt asking for ≥3 hypotheses, evidence sources,
      and a weighted trade-off table for the named alternatives. References the
      `investigation-methodology` skill.
    - `tech-eval` → run the full rubric (functional, operational, security, ecosystem,
      team, financial). References the `technology-evaluation` skill.
    - `spike` → produce a timeboxed spike plan (goal, questions, exit criteria, deliverable).
      References `investigation-methodology` (Spike Story Format) + `decision-records`
      (Spike Findings doc).
    - `full-pipeline` → orchestrate the seven-step chain: `/learn-concept` →
      `/deep-dive` → `/resources` → `/learn-from-docs` → `/design-review` →
      `/brain-new` → `/check-standards`. Emit each invocation as a fenced command
      the user can run, with the `topic` propagated.
  - References modular skills for terminology and frameworks instead of duplicating them.

## Original Content (verbatim)

```markdown
### Copilot-Assisted Research Workflow

Combine Copilot commands to build a complete research workflow:

```text
Step 1  /learn-concept → <topic>        # Concept fundamentals
          ↓
Step 2  /deep-dive → <topic>            # Progressive layers
          ↓
Step 3  /resources → search → <topic>  # Curated resources
          ↓
Step 4  /learn-from-docs → <official>  # Official source
          ↓
Step 5  /design-review (with POC code) # Architecture check
          ↓
Step 6  /brain-new → decision-<topic>  # Capture ADR/findings
          ↓
Step 7  /check-standards → brain-naming # Verify our note
```

**Prompts for Copilot research assistance:**

```text
"Research [topic] using the hypothesis-driven framework.
 List 3 hypotheses, evidence sources per hypothesis,
 and a weighted trade-off table for [options A, B, C]."

"Perform a technology evaluation of [tech] using the full
 rubric: functional, operational, security, ecosystem, team, financial."

"Build a research plan for a spike on [topic].
 Timebox: 2 days. Questions to answer: [...]"
```

```markdown

## Status

- Routed from `deep-research` skill migration (5-skill split).
- Awaiting prompt authoring pass (after all skills are migrated).
