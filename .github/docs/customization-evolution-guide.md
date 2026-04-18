# Customization Evolution Guide — Import, Merge, Evolve Without Regression

> **Audience:** Anyone growing or maintaining the `.github/` customization framework
> **Purpose:** Define how to safely import external skills, merge colleagues' contributions,
> evolve primitives over time, and audit the framework for fitness — all without losing
> existing functionality.
> **When to use:** Before importing skills from awesome-copilot, skills.sh, colleagues,
> or any external source. Before deciding whether to use a prompt, skill, or MCP server.
> After significant framework growth (every 10+ new primitives).

---

## Table of Contents

- [The Evolution Principles](#the-evolution-principles)
- [Import Protocol — Adding External Primitives](#import-protocol--adding-external-primitives)
- [Merge Protocol — Combining With Existing Primitives](#merge-protocol--combining-with-existing-primitives)
- [Primitive Fitness Review — Is This the Right Type?](#primitive-fitness-review--is-this-the-right-type)
- [Prompt vs Skill — Decision Framework](#prompt-vs-skill--decision-framework)
- [Current Prompt-Skill Audit](#current-prompt-skill-audit)
- [MCP Server Coexistence — Skills + Code + External](#mcp-server-coexistence--skills--code--external)
- [Colleague Contribution Protocol](#colleague-contribution-protocol)
- [Regression Prevention Checklist](#regression-prevention-checklist)
- [External Source Registry](#external-source-registry)
- [Framework Health Metrics](#framework-health-metrics)
- [Further Reading](#further-reading)

---

## The Evolution Principles

This framework is designed to **grow safely** over time. Five principles govern evolution:

1. **Additive by default** — new primitives add to, never replace, existing ones unless
   the existing primitive is provably wrong or obsolete
2. **No orphan primitives** — every skill, prompt, agent, and MCP server must be
   discoverable via at least one navigation path (TAXONOMY.md, slash-commands.md, hub)
3. **Primitive fitness** — periodically review whether each primitive is the right
   *type* (prompt vs skill vs MCP vs instruction) using the fitness checklist
4. **Source tracking** — every imported primitive records its origin (author, source
   repo, version, date imported) so updates and attribution are possible
5. **Non-destructive merge** — when merging external content with existing primitives,
   preserve all existing features and cross-references

---

## Import Protocol — Adding External Primitives

### Step 1 — Evaluate the Source

Before importing any primitive, answer these questions:

| Question | Why It Matters |
|---|---|
| What type is it? (skill, prompt, agent, MCP, instruction) | Determines where it goes and how to register it |
| Does it overlap with an existing primitive? | If yes → merge, don't duplicate |
| Is it from a trusted source? | Check author, repo stars, last update date |
| Does it match our project's conventions? | Description format, 3-tier depth, naming |
| Does it need adaptation? | External skills may assume different tools or conventions |

### Step 2 — Classify the Import Action

| Situation | Action |
|---|---|
| No overlap with existing primitives | **Import as new** — follow the Adding a New Skill checklist in TAXONOMY.md |
| Partial overlap with one existing primitive | **Merge** — enhance the existing primitive with new content |
| Full overlap (same domain, same depth) | **Skip or replace** — only import if it's clearly better |
| Different type for same domain (prompt vs skill) | **Add complementary** — keep both (dual pattern recommended) |
| External MCP server for a domain we have as skill | **Coexist** — see MCP Server Coexistence section |

### Step 3 — Adapt to Project Conventions

Before committing an imported primitive:

- [ ] **Rename** to match project naming (lowercase-kebab-case, no branded names)
- [ ] **Rewrite description** to match our description field convention (exhaustive triggers,
  "Use when asked about:" format — see skills-library.md)
- [ ] **Add 3-tier depth** if the skill only has one level
- [ ] **Add `Related skills:`** header linking to complementary skills in our library
- [ ] **Add source attribution** in a frontmatter comment or footer section:

```yaml
---
name: imported-skill-name
description: >
  ...
# Source: github/awesome-copilot/skills/original-name (imported 2026-04-18)
# Author: original-author
# License: MIT
---
```

### Step 4 — Register and Cross-Reference

Follow the standard registration checklist from [TAXONOMY.md](../skills/TAXONOMY.md):

1. TAXONOMY.md — tree + category table + cross-ref
2. skills README.md — categorized table
3. copilot-instructions.md — routing table
4. navigation-index.md — skills quick ref + file map
5. skills-library.md — inventory table
6. Prompt file (if applicable) + slash-commands.md + hub.prompt.md

### Step 5 — Verify No Regression

Run the [Regression Prevention Checklist](#regression-prevention-checklist) below.

---

## Merge Protocol — Combining With Existing Primitives

When an imported primitive overlaps with an existing one:

### Merge Decision Matrix

| Existing Coverage | Imported Content | Action |
|---|---|---|
| Comprehensive (3-tier, 200+ lines) | Adds a few new subtopics | **Append** — add new sections to existing file |
| Moderate (1-2 tiers, 50-100 lines) | More comprehensive | **Enhance** — restructure existing file with imported content |
| Minimal (stub or placeholder) | Comprehensive | **Replace** — swap in imported content, adapt to conventions |
| Both comprehensive | Different angles | **Cross-reference** — keep both, link to each other |

### Merge Execution Steps

1. **Read both files fully** — understand what each covers
2. **Create a diff outline** — list topics covered by existing vs imported
3. **Identify unique content** in each:
   - Existing-only topics → preserve them
   - Imported-only topics → add them
   - Both cover → keep the better version, merge unique details
4. **Preserve all cross-references** — existing inbound links must continue to work
5. **Update the description field** — expand triggers to include new topics
6. **Run the fitness review** — confirm the merged result is still the right primitive type

### Anti-Patterns

| Anti-Pattern | Why It's Bad | Fix |
|---|---|---|
| **Replace without reading** | Loses existing project-specific knowledge | Always diff first |
| **Import without adapting** | Breaks naming, description, and depth conventions | Run Step 3 checklist |
| **Duplicate without merging** | Two skills fighting for activation on same topic | Merge or clearly split domains |
| **Merge without testing** | Broken links, missing cross-references | Run regression checklist |

---

## Primitive Fitness Review — Is This the Right Type?

Periodically audit each primitive to confirm it's the right type. Run this review:
- After every 10 new primitives added
- When a primitive feels like it's in the wrong place
- When a colleague suggests "why isn't this a skill/prompt/MCP?"
- When the `/check-standards` command flags a fitness concern

### The Fitness Scorecard

For each primitive, answer these 5 questions:

| # | Question | If YES → | If NO → |
|---|---|---|---|
| 1 | Does the user need to explicitly trigger this? | **Prompt** (.prompt.md) | Continue to Q2 |
| 2 | Is this static domain knowledge (reference material)? | **Skill** (SKILL.md) | Continue to Q3 |
| 3 | Is this a behavioral rule for code output? | **Instruction** (.instructions.md) | Continue to Q4 |
| 4 | Does this need live/dynamic data from an external API? | **MCP server** | Continue to Q5 |
| 5 | Does this define a persistent persona for entire sessions? | **Agent** (.agent.md) | Re-evaluate — may not need a primitive |

### Migration Decision Table

| Current Type | Signs It Should Migrate | Migrate To | How |
|---|---|---|---|
| **Prompt** → Skill | Contains 100+ lines of reference knowledge; users forget the `/command` exists; Copilot should auto-detect the topic | Skill (keep prompt as trigger) | Extract knowledge to SKILL.md, simplify prompt to trigger + input vars |
| **Prompt** → Agent | Prompt defines a persona (tone, restrictions, tool limits); users want the persona for entire conversations | Agent (.agent.md) | Extract persona to agent file, simplify prompt to workflow trigger |
| **Skill** → MCP | Skill data goes stale quickly; needs live API calls; data changes weekly or daily | MCP server (keep skill as fallback) | Implement MCP server, keep skill as offline reference |
| **Skill** → Instruction | Skill is really just 5-10 behavioral rules, not reference knowledge | Instruction (.instructions.md) | Move rules to instruction, delete or repurpose skill |
| **Instruction** → Skill | Instruction grew to 200+ lines; contains reference tables and examples, not just rules | Skill (keep 5-line instruction for core rules) | Split: core rules stay in instruction, reference moves to skill |
| **MCP** → Skill | MCP server is overkill for data that changes rarely; maintenance burden is high | Skill (or skill + static JSON) | Extract data to skill, decommission server |

### The Dual Pattern (Recommended)

The **best practice** for most domains is the **dual pattern**: skill + prompt.

```text
SKILL.md (auto-loads)     → Deep knowledge Copilot can draw from anytime
.prompt.md (/command)     → Explicit trigger when user wants structured guidance

Example:
  skills/git-vcs/SKILL.md         → Git knowledge (auto-activates)
  prompts/git-vcs.prompt.md       → /git-vcs (explicit trigger with inputs)
```

This pattern gives you **both** auto-detection AND explicit invocation.

---

## Prompt vs Skill — Decision Framework

### When to Keep as Prompt Only

| Indicator | Example Prompts |
|---|---|
| **Workflow with input variables** | `/composite`, `/scope`, `/context`, `/steer`, `/multi-session` |
| **Orchestration command** (routes to other things) | `/hub` |
| **Action trigger** (does something, not teaches something) | `/brain-fetch`, `/brain-push`, `/ship`, `/jot` |
| **Thin wrapper** around a skill (just adds input vars) | `/git-vcs`, `/github-workflow`, `/dsa` (backed by skills) |

### When to Keep as Skill Only

| Indicator | Example Skills |
|---|---|
| **Deep reference material** (200+ lines) | `mcp-development`, `software-engineering-resources` |
| **Auto-detection is sufficient** (no explicit trigger needed) | `java-formatting` (opt-in but auto-detects) |
| **Knowledge base** not a workflow | `learning-resources-vault` |

### When to Use Both (Dual Pattern)

| Indicator | Example Pairs |
|---|---|
| **Domain with both knowledge AND workflow** | `git-vcs` (skill) + `/git-vcs` (prompt) |
| **Users want auto AND explicit access** | `design-patterns` (skill) + `/design-review` (prompt) |
| **Learning domain** (structured teaching) | `java-learning-resources` (skill) + `/resources` (prompt) |

---

## Current Prompt-Skill Audit

Status of all 59 prompts mapped against the dual pattern:

### Navigation & Meta (6 prompts) — Prompt-only is correct

| Prompt | Backing Skill | Status | Notes |
|---|---|---|---|
| `/hub` | — | ✅ Correct | Orchestration command, not knowledge |
| `/composite` | — | ✅ Correct | Workflow combiner |
| `/context` | — | ✅ Correct | Session management |
| `/scope` | — | ✅ Correct | Scope setter |
| `/multi-session` | — | ✅ Correct | Cross-session state |
| `/steer` | — | ✅ Correct | Mode switcher |

### Learning (5 prompts) — Prompt-only is correct

| Prompt | Backing Skill | Status | Notes |
|---|---|---|---|
| `/learn-concept` | multiple skills (auto) | ✅ Correct | Generic trigger, skill auto-loads |
| `/deep-dive` | multiple skills (auto) | ✅ Correct | Progressive learning workflow |
| `/learn-from-docs` | multiple skills (auto) | ✅ Correct | Docs-based learning |
| `/reading-plan` | multiple skills (auto) | ✅ Correct | Study plan generator |
| `/teach` | multiple skills (auto) | ✅ Correct | Code-based teaching |

### Domain-Specific (14 prompts) — Dual pattern in use

| Prompt | Backing Skill | Status | Notes |
|---|---|---|---|
| `/dsa` | `software-engineering-resources` | ✅ Dual | Prompt triggers, skill provides knowledge |
| `/system-design` | `software-engineering-resources` | ✅ Dual | Same pattern |
| `/devops` | `software-engineering-resources` | ✅ Dual | Same pattern |
| `/git-vcs` | `git-vcs` | ✅ Dual | Perfect match |
| `/github-workflow` | `github-workflow` | ✅ Dual | Perfect match |
| `/build-tools` | `software-engineering-resources` | ✅ Dual | Backed by SE resources |
| `/mac-dev` | `mac-dev` | ✅ Dual | Perfect match |
| `/digital-notetaking` | `digital-notetaking` | ✅ Dual | Perfect match |
| `/mcp` | `mcp-development` | ✅ Dual | Perfect match |
| `/resources` | `learning-resources-vault` | ✅ Dual | Search/browse trigger |
| `/language-guide` | varies (auto) | ✅ Correct | Generic language trigger |
| `/tech-stack` | varies (auto) | ✅ Correct | Framework comparison trigger |
| `/sdlc` | `software-engineering-resources` | ✅ Dual | SDLC backed by SE resources |
| `/interview-prep` | `career-resources` | ✅ Dual | Backed by career skill |

### Career & Daily (2 prompts)

| Prompt | Backing Skill | Status | Notes |
|---|---|---|---|
| `/career-roles` | `career-resources` | ✅ Dual | Perfect match |
| `/daily-assist` | `daily-assistant-resources` | ✅ Dual | Perfect match |

### Code Quality (6 prompts) — Prompt-only is correct

| Prompt | Backing Skill | Status | Notes |
|---|---|---|---|
| `/design-review` | `design-patterns` (auto) | ✅ Dual | Review triggers pattern skill |
| `/debug` | `java-debugging` (auto) | ✅ Dual | Debug triggers debugging skill |
| `/impact` | — | ✅ Correct | Analysis workflow |
| `/refactor` | `design-patterns` (auto) | ✅ Dual | Refactoring triggers patterns |
| `/explain` | — | ✅ Correct | File explanation workflow |
| `/explore-project` | — | ✅ Correct | OSS study workflow |

### Brain PKM (14 prompts) — Prompt-only is correct

| Prompt | Backing Skill | Status | Notes |
|---|---|---|---|
| `/brain-fetch` | `pkm-management` (auto) | ✅ Dual | Git-inspired PKM command |
| `/brain-pull` | `pkm-management` (auto) | ✅ Dual | Same pattern |
| `/brain-clone` | `pkm-management` (auto) | ✅ Dual | Same pattern |
| `/brain-merge` | `pkm-management` (auto) | ✅ Dual | Same pattern |
| `/brain-cherry-pick` | `pkm-management` (auto) | ✅ Dual | Same pattern |
| `/brain-stash` | `pkm-management` (auto) | ✅ Dual | Same pattern |
| `/brain-diff` | `pkm-management` (auto) | ✅ Dual | Same pattern |
| `/brain-push` | `pkm-management` (auto) | ✅ Dual | Same pattern |
| `/brain-remote` | `pkm-management` (auto) | ✅ Dual | Same pattern |
| `/brain-consolidate` | `pkm-management` (auto) | ✅ Dual | Same pattern |
| `/brain-new` | `brain-management` (auto) | ✅ Dual | Brain workspace command |
| `/brain-publish` | `brain-management` (auto) | ✅ Dual | Same pattern |
| `/brain-search` | `brain-management` (auto) | ✅ Dual | Same pattern |
| `/brain-capture-session` | `brain-management` (auto) | ✅ Dual | Session capture trigger |

### Utility & Customization (8 prompts) — Prompt-only is correct

| Prompt | Backing Skill | Status | Notes |
|---|---|---|---|
| `/copilot-customization` | `copilot-customization` | ✅ Dual | Perfect match |
| `/create-agent` | `copilot-customization` (auto) | ✅ Dual | Agent creation workflow |
| `/write-docs` | — | ✅ Correct | Document creation workflow |
| `/check-standards` | — | ✅ Correct | Audit workflow |
| `/mcp-to-skill` | `copilot-customization` (auto) | ✅ Dual | Migration workflow |
| `/ship` | — | ✅ Correct | Commit/push workflow |
| `/session-scope` | — | ✅ Correct | Session scoping control |

### Backlog & Notes (4 prompts) — Prompt-only is correct

| Prompt | Backing Skill | Status | Notes |
|---|---|---|---|
| `/backlog` | `brain-management` (auto) | ✅ Dual | Backlog management |
| `/jot` | `brain-management` (auto) | ✅ Dual | Quick capture |
| `/read-file-jot` | — | ✅ Correct | File-reading jot workflow |
| `/todo` / `/todos` | — | ✅ Correct | Todo management workflow |

### Audit Summary

**Result: No migrations needed.** All 59 prompts are correctly typed:

- **Prompts with backing skills (dual pattern):** 40 prompts — these are slash-command
  triggers backed by auto-loading skill knowledge. This is the recommended pattern.
- **Prompt-only (workflow/action commands):** 19 prompts — these are orchestration,
  navigation, or action commands that don't need persistent knowledge. Correct as-is.
- **Prompts that should migrate to skills:** 0 — none of the prompts contain substantial
  reference knowledge; they all delegate to skills for domain knowledge.

> **Community validation:** The awesome-copilot repository performed a mass
> "Convert all prompts to skills" migration because their prompts **contained the
> knowledge directly** (no backing skills). In this project, prompts are thin workflow
> triggers that delegate to skills — the architecture is already correct.

---

## MCP Server Coexistence — Skills + Code + External

### The Coexistence Model

MCP servers, skills, and coded Java modules can all provide knowledge for the same
domain. They are not mutually exclusive:

```text
Domain: Atlassian (Jira + Confluence)
│
├── SKILL.md (static reference)
│   └── Jira field conventions, workflow states, JQL patterns,
│       Confluence space structure, common API patterns
│
├── MCP Server (live data access)
│   └── Query Jira issues, read Confluence pages, create/update
│       items via REST API using Personal Access Token
│
├── Java Code Module (in mcp-servers/)
│   └── AtlassianServer.java — server-side implementation of
│       the MCP tools, handles auth, pagination, caching
│
└── Colleague's MCP Server (external contribution)
    └── Additional tools, different auth approach, extended
        coverage — merge protocol applies
```

### When to Use What

| Need | Use | Why |
|---|---|---|
| Copilot should know Jira conventions automatically | Skill | Auto-loads on topic detection |
| Copilot needs to query live Jira data | MCP server | Skills can't make API calls |
| Copilot needs to create/update Jira issues | MCP server | Write operations need live access |
| User wants structured Jira guidance | Prompt + Skill | `/jira` prompt triggers skill |
| Colleague has extra Jira MCP tools | Import + merge | Follow import protocol |

### Personal Access Token (PAT) Handling

When importing MCP servers that use PAT authentication:

1. **Never commit tokens** — use `.env` files in `mcp-servers/user-config/` (gitignored)
2. **Document the PAT setup** in the skill's "Setup" section and `SETUP.md`
3. **Fail gracefully** — MCP tools should return helpful errors when PAT is missing
4. **Fallback to skill** — when MCP is unavailable, the static skill still provides value

### Evolving MCP + Skill Together

When an MCP server is added for a domain that already has a skill:

```text
BEFORE: skill only
  skills/atlassian/SKILL.md → static Jira/Confluence knowledge

AFTER: skill + MCP
  skills/atlassian/SKILL.md → static knowledge (conventions, patterns, JQL)
  mcp-servers/src/server/atlassian/ → live Jira/Confluence access
  mcp-servers/user-config/servers/atlassian/ → PAT config

The skill STAYS — it provides offline reference knowledge.
The MCP ADDS — it provides live data access.
They complement, not compete.
```

---

## Colleague Contribution Protocol

When a colleague shares their skills, MCP servers, or customization files:

### Step 1 — Receive and Catalog

| What They Share | Where It Goes Initially |
|---|---|
| SKILL.md file(s) | `brain/ai-brain/inbox/` for review |
| .prompt.md file(s) | `brain/ai-brain/inbox/` for review |
| .agent.md file(s) | `brain/ai-brain/inbox/` for review |
| MCP server code | Feature branch for review |
| PAT/credentials | `mcp-servers/user-config/` (never committed) |
| .instructions.md | `brain/ai-brain/inbox/` for review |

### Step 2 — Evaluate and Classify

For each received file:

1. **Read fully** — understand what it does and what domain it covers
2. **Check overlap** — does it duplicate or extend an existing primitive?
3. **Check quality** — does it meet our description, naming, and depth conventions?
4. **Classify action**: import as new / merge with existing / skip / defer to backlog

### Step 3 — Merge or Import

Follow the [Import Protocol](#import-protocol--adding-external-primitives) or
[Merge Protocol](#merge-protocol--combining-with-existing-primitives) above.

### Step 4 — Attribution

Add attribution in the merged file's frontmatter or footer:

```markdown
## Source Attribution

| Contributor | Source | Date | What Was Imported |
|---|---|---|---|
| @colleague-name | colleague's-repo | 2026-04-18 | Atlassian MCP tools, PAT auth |
```

### Step 5 — Verify

Run the [Regression Prevention Checklist](#regression-prevention-checklist).

---

## Regression Prevention Checklist

Run this checklist **every time** you import, merge, or significantly modify primitives:

### Structural Checks

- [ ] **All skills in TAXONOMY.md** — every skill folder has a row in the taxonomy tree
  and category table
- [ ] **All skills in skills-library.md** — inventory count matches actual skill count
- [ ] **All prompts in slash-commands.md** — count in header matches actual row count
- [ ] **All prompts in hub.prompt.md** — navigation tree includes all domain prompts
- [ ] **copilot-instructions.md routing** — every skill has a routing table entry
- [ ] **navigation-index.md** — skills table and file map are current

### Content Checks

- [ ] **No duplicate descriptions** — no two skills have overlapping trigger keywords
  that would cause activation conflicts
- [ ] **Cross-references intact** — grep for `Related skills:` and verify all links resolve
- [ ] **No orphan files** — every `.prompt.md` and `SKILL.md` is referenced from at least
  one navigation file
- [ ] **Frontmatter valid** — every skill has `name:` and `description:`; every prompt has
  `name:` and `description:`

### Build & Lint

- [ ] **`.\__md_lint.ps1`** — exits with 0 issues (or only pre-existing ones)
- [ ] **`.\mcp-servers\build.ps1`** — compiles with 0 errors (if Java files changed)

### Functional Verification

- [ ] **Test 3 existing slash commands** — verify they still work after the change
- [ ] **Test skill auto-detection** — ask a question in the domain of a skill you changed;
  verify the skill activates
- [ ] **Verify MCP tools** — if MCP server changed, test at least one tool call

---

## External Source Registry

Track where you can find new skills and primitives to import:

| Source | URL | What's There | Quality |
|---|---|---|---|
| **awesome-copilot** | [github.com/github/awesome-copilot](https://github.com/github/awesome-copilot/tree/main/skills) | 300+ community skills (Java, .NET, Python, DevOps, security, testing) | High — GitHub-maintained |
| **skills.sh** | [skills.sh](https://skills.sh/) | Open agent skills ecosystem — cross-agent (Copilot, Cursor, Claude) | Varies — community contributed |
| **Colleague contributions** | Internal sharing | Project-specific skills, MCP servers, PAT configs | Review before import |
| **Your own experimentation** | `brain/ai-brain/inbox/` | Draft skills, prompts, ideas captured during work | Needs refinement |

### Browsing Strategy

When looking for new skills to import:

1. **Search by domain** — e.g., "java" in awesome-copilot → find `java-junit`,
   `java-springboot`, `java-docs`, `java-refactoring-*`
2. **Check the trending page** on skills.sh for popular community skills
3. **Read the description** — does it fill a gap in our taxonomy?
4. **Check the commit history** — recently updated skills are more reliable
5. **Prefer skills over prompts** — the community trend is prompt → skill conversion

---

## Framework Health Metrics

Track these metrics to understand framework health over time:

| Metric | Current | Target | How to Measure |
|---|---|---|---|
| Total skills | 21 | 25-30 | `ls .github/skills/ \| Measure-Object` |
| Total prompts | 59 | 60-70 | `ls .github/prompts/*.prompt.md \| Measure-Object` |
| Skills with dual pattern | 18 | 20+ | Count skills that have a matching prompt |
| Orphan skills (no nav link) | 0 | 0 | Grep skills not in TAXONOMY.md |
| Orphan prompts (no slash-commands entry) | 0 | 0 | Grep prompts not in slash-commands.md |
| Skills from external sources | 0 | 3-5 | Count skills with source attribution |
| MCP server domains | 2 | 3-4 | Count server directories |
| Last framework audit date | — | Monthly | Record in this table |

---

## Further Reading

| Document | What It Covers |
|---|---|
| [TAXONOMY.md](../skills/TAXONOMY.md) | Hierarchical skill index, cross-refs, adding new skills |
| [skills-library.md](skills-library.md) | Skill creation guide, naming, description, 3-tier depth |
| [copilot-primitives-crosswalk.md](copilot-primitives-crosswalk.md) | Primitive comparison, conversion paths, edge cases |
| [change-completeness.instructions.md](../instructions/change-completeness.instructions.md) | Completeness checklist for all change types |
| [navigation-index.md](navigation-index.md) | Master navigation for all files, commands, skills |

---

## What's Next?

| Navigation | Link |
|---|---|
| ← Back to docs index | [START-HERE.md](START-HERE.md) |
| → Primitive comparison | [copilot-primitives-crosswalk.md](copilot-primitives-crosswalk.md) |
| → Skills taxonomy | [TAXONOMY.md](../skills/TAXONOMY.md) |
| → Skills creation guide | [skills-library.md](skills-library.md) |
