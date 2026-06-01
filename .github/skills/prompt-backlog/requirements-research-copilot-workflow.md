# Prompt Backlog: requirements-research Copilot workflow

## Source

- **Legacy skill:** `.github/skills/dev-process/requirements-research/SKILL.md`
- **Section not migrated to modular skill:**
  - `### Requirements Research with Copilot` (lines 292–305) — workflow recipes
    that chain `Thinking-Beast-Mode`, `/deep-dive`, and `/brain-new`

## What It Does

A short cookbook showing how to use existing Copilot primitives to do requirements
research work:

1. **Domain research via Thinking-Beast-Mode** — ask the agent to identify entities,
   relationships, events, and business rules using DDD vocabulary, and to suggest
   stakeholder clarification questions.
2. **Tech constraint analysis via `/deep-dive`** — run the deep-dive prompt on a
   target technology to surface limitations that constrain requirements.
3. **Decision capture via `/brain-new`** — capture requirements decisions as brain
   notes tagged `requirements` for later traceability.

## Why It Belongs in a Prompt, Not a Skill

- It is a **workflow** (step-by-step procedure invoking specific slash commands),
  not domain knowledge.
- It hard-codes references to other primitives (`/deep-dive`, `/brain-new`,
  `Thinking-Beast-Mode`) — those couplings belong in a prompt that orchestrates them.
- The knowledge *behind* the workflow (DDD vocabulary, stakeholder analysis, etc.)
  is already kept in `_modular/requirements-research/SKILL.md`.

## Suggested Prompt

- **Name:** `requirements-discovery`
- **Location:** `.github/prompts/requirements-discovery.prompt.md`
- **Inputs:** `domain` (string), `mode` (`domain-model` | `tech-constraints` | `capture-decision`)
- **Structure:**
  - Dispatch on `mode`:
    - `domain-model` → invoke `Thinking-Beast-Mode` with a DDD-flavoured research prompt
      seeded with the `domain` input; produce entity/relationship/event lists and
      5 stakeholder questions.
    - `tech-constraints` → run `/deep-dive` on the `domain` (treated as a technology)
      and synthesise requirement-impacting limitations.
    - `capture-decision` → run `/brain-new` to record the decision as a brain note
      with `requirements` tag.
  - References the `requirements-research` skill for terminology (FURPS+, MoSCoW,
    INVEST, RTM, BDD) without duplicating it.

## Original Content (verbatim)

```markdown
### Requirements Research with Copilot

# Use the Thinking-Beast-Mode agent for deep domain research:
Ask: "Research the domain of [your area]. Identify key entities, relationships,
     events, and business rules using DDD vocabulary. Suggest 5 questions I should
     ask stakeholders to clarify requirements."

# Use /deep-dive for technology constraints:
/deep-dive → [technology] → what are the limitations that affect our requirements?

# Use /brain-new to capture a requirements decision:
/brain-new → "decision-password-reset-scope" → notes → ["requirements", "auth"]
```

## Status

- Routed from `requirements-research` skill migration
- Awaiting prompt authoring pass (after all skills are migrated)
