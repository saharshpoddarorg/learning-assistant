---
id: EPIC-005
title: Full repo revamp — modularize prompts, skills, agents, MCP, brain
status: draft
priority: high
created: 2026-05-15
updated: 2026-05-15
tags: [modularization, revamp, prompts, skills, agents, mcp, brain, restructuring]
---

# EPIC-005: Full Repo Revamp — Modularize Prompts, Skills, Agents, MCP, Brain

## Vision

Iteratively revamp and modularize the entire repository — prompts, skills, agents,
MCP servers, instructions, brain workspace, and documentation — while retaining all
existing features (search engine, learning resources, brain/PKM, Atlassian integration).
Nothing is removed until its replacement is verified working. The goal is a cleaner,
more maintainable, more composable architecture.

## Guiding Principles

1. **Safety first** — remove legacy content only AFTER migration is verified
2. **Feature parity** — every current capability must survive the revamp
3. **Composition over monolith** — prompts, skills, agents should be composable
4. **Convention over configuration** — establish clear patterns others can follow
5. **Self-documenting** — the structure should explain itself

---

## Phase 1 — Prompt Modularization (BLI-041 through BLI-047)

### Current State

65 prompts in 11 folder categories. Many prompts are large monoliths that embed
domain knowledge inline instead of delegating to skills. Some prompts duplicate
content found in skills. Composition between prompts is ad-hoc.

### Target State

Prompts become thin orchestration layers that:

- Define the **workflow** (what steps to follow)
- Declare **input variables** cleanly
- Delegate **domain knowledge** to skills (not embedded inline)
- Support **composition** (one prompt calling or referencing others)
- Have consistent **frontmatter** (name, description, agent, tools, variables)

| ID | Title | Status | Priority | Description |
|---|---|---|---|---|
| BLI-041 | Audit prompt frontmatter consistency | todo | high | Ensure all 65 prompts have consistent YAML: name, description, agent, tools, variables. Identify prompts missing fields or using inconsistent patterns |
| BLI-042 | Extract inline domain knowledge from prompts into skills | todo | high | Many prompts (e.g., `/dsa`, `/system-design`, `/devops`) embed large domain hierarchies inline. Extract these into companion skills and have prompts reference the skill instead. Reduces duplication, enables skill auto-activation |
| BLI-043 | Standardize prompt composition patterns | todo | high | Define and document how prompts compose: (1) prompt → skill delegation, (2) prompt → prompt chaining (e.g., `/hub` → `/learn-concept`), (3) prompt → agent handoff, (4) `/composite` multi-mode. Create a composition reference doc |
| BLI-044 | Refactor meta prompts for clarity | todo | medium | `/context`, `/scope`, `/session-scope`, `/steer`, `/request-steering` — simplify overlapping scoping/steering prompts. Clarify which is the primary entry point vs. alias |
| BLI-045 | Consolidate brain prompts | todo | medium | 12 brain prompts (`/new`, `/search`, `/publish`, `/fetch`, `/pull`, `/push`, `/diff`, `/merge`, `/clone`, `/cherry-pick`, `/remote`, `/stash`) follow git-inspired patterns but are individually authored. Create a shared brain-operations template and refactor each to use it |
| BLI-046 | Standardize prompt input variables | todo | medium | Inconsistent variable naming across prompts: some use `topic`, others `concept`; some use `level`, others `tier`. Establish a variable vocabulary and apply consistently |
| BLI-047 | Create prompt composition map document | todo | low | Visual document showing all 65 prompts, their composition relationships (calls, references, delegates-to), agent associations, and skill activations. Mermaid diagram showing the full prompt graph |

---

## Phase 2 — Skill Modularization (BLI-048 through BLI-053)

### Current State

23 skills in 8 domain folders. Skills vary dramatically in size (some are 50 lines,
others 2000+ lines). Some skills are tightly coupled to specific prompts. Activation
keywords sometimes overlap between skills, causing unpredictable auto-activation.

### Target State

Skills become well-scoped, consistently-sized knowledge modules that:

- Have clear **activation boundaries** (no overlap conflicts)
- Follow a consistent **structure** (overview, cheatsheet, resources, 3-tier depth)
- Are **independent** of specific prompts (any prompt can activate any skill)
- Have **sub-files** for large domains (already done for some, not all)

| ID | Title | Status | Priority | Description |
|---|---|---|---|---|
| BLI-048 | Audit skill activation keyword overlaps | todo | high | Identify skills with overlapping `description` keywords that cause both to activate on the same query. Document conflicts and resolve by tightening activation conditions |
| BLI-049 | Standardize skill file structure | todo | high | Define a canonical skill structure: (1) frontmatter with description/applyTo, (2) Quick Reference table, (3) 3-tier content (newbie/amateur/pro), (4) Resources section, (5) Sub-files for large skills. Apply to all 23 skills |
| BLI-050 | Split oversized skills into sub-files | todo | medium | Skills like `atlassian-tools` (2000+ lines) and `learning-resources-vault` should be split into sub-files by sub-domain. The main SKILL.md becomes a router that references sub-files |
| BLI-051 | Extract prompt-embedded knowledge into new skills | todo | medium | Companion to BLI-042. Create new skills for domains currently only documented in prompts (e.g., DSA hierarchy from `/dsa`, system design hierarchy from `/system-design`, DevOps tools from `/devops`). This enables auto-activation without needing the slash command |
| BLI-052 | Verify skill ↔ prompt ↔ agent cross-references | todo | medium | Ensure every skill that auto-activates is referenced by at least one prompt, and every prompt that delegates to a skill actually triggers it. Fix broken linkages |
| BLI-053 | Create skill activation map document | todo | low | Document showing all 23 skills, their activation keywords, which prompts trigger them, and which agents they pair with. Include conflict analysis |

---

## Phase 3 — Agent Modularization (BLI-054 through BLI-057)

### Current State

8 agents with varying levels of sophistication. Handoff chains exist but are
documented only in individual agent files, not centrally. Agent tool restrictions
are inconsistent (some allow all tools, some restrict tightly). No standard
agent template.

### Target State

Agents follow a consistent template with:

- Clear **persona definition** (role, expertise, tone)
- Explicit **tool allowlist** (what they can access)
- Documented **handoff chains** (who they hand off to and when)
- Matching **prompt associations** (which prompts invoke this agent)
- Consistent **instruction integration** (what instructions they follow)

| ID | Title | Status | Priority | Description |
|---|---|---|---|---|
| BLI-054 | Standardize agent template and structure | todo | high | Create a canonical `.agent.md` template with sections: persona, tools, handoffs, associated prompts, instruction compatibility. Apply to all 8 agents |
| BLI-055 | Document complete agent handoff graph | todo | high | Create a central document showing all agent handoff chains, bidirectional relationships, and the complete flow from user intent → agent → handoff → final agent. Include the Mermaid diagram from features-inventory.md and expand it |
| BLI-056 | Review and normalize agent tool restrictions | todo | medium | Compare tool allowlists across agents. Ensure restrictions are intentional (e.g., code-reviewer is read-only by design). Remove overly broad or overly narrow permissions where they don't match the agent's purpose |
| BLI-057 | Create agent selection guide | todo | low | Decision tree for users: "Which agent should I use?" Based on task type, desired depth, read-only vs. editing, domain specificity. Embed in `/hub` and docs |

---

## Phase 4 — MCP Server Modularization (BLI-058 through BLI-062)

### Current State

2 MCP servers: Learning Resources (10 tools, skill-migrated) and Atlassian (94 tools,
v1 legacy + v2 current). The v1 Atlassian server is still in the codebase alongside v2.
Configuration is split between `modules/app/user-config/`, env vars, and `.vscode/mcp.json`.

### Target State

MCP servers with:

- **Clear v1/v2 boundary** — v1 Atlassian marked deprecated or removed
- **Consistent configuration** — single config pattern for all servers
- **Tool documentation** auto-generated from Java source
- **Skill wrappers** for all MCP tools (so they're discoverable without MCP running)
- **Health checks** and status reporting via VS Code tasks

| ID | Title | Status | Priority | Description |
|---|---|---|---|---|
| BLI-058 | Deprecate/remove Atlassian v1 server | todo | high | v1 (27 tools) is superseded by v2 (94 tools). Mark v1 as deprecated with removal timeline, or remove if v2 is fully stable. Update `.vscode/mcp.json`, docs, skills |
| BLI-059 | Unify MCP server configuration pattern | todo | high | Both servers use different config approaches. Standardize: (1) default properties, (2) local override file, (3) env var override, (4) CLI arg override. Document the layered config in one place |
| BLI-060 | Create skill wrappers for all MCP tool categories | todo | medium | Currently only `atlassian-tools` skill wraps MCP tools. Create equivalent skill wrappers for: learning-resources tools (10), so they're discoverable when MCP server is not running. Pattern: skill describes the tool capability, prompt invokes it |
| BLI-061 | Auto-generate MCP tool documentation from Java source | todo | medium | Tool names and descriptions are in Java handler classes. Create a Gradle task or script that extracts tool metadata and generates a markdown reference doc (like `mcp-implementations.md` but auto-maintained) |
| BLI-062 | Add MCP server health check and status tasks | todo | low | VS Code tasks for: server health check (is it running? what tools are available?), config validation (are API keys set?), connectivity test (can it reach Jira/Confluence?). Some exist already in `server.ps1 validate`; enhance and document |

---

## Phase 5 — Instruction Modularization (BLI-063 through BLI-066)

### Current State

9 instructions: 5 always-on (`**` glob), 4 scoped. The always-on instructions are
very large (chat-capture is 1000+ lines, change-completeness is 500+ lines). They
create significant context overhead for every conversation. Some instructions overlap
(session-scoping vs. chat-capture both manage session metadata).

### Target State

Instructions that are:

- **Right-sized** — large instructions broken into focused modules
- **Clearly scoped** — no overlap between instructions
- **Documented** — each instruction explains when it activates and what it controls
- **Measurable** — impact on context window documented

| ID | Title | Status | Priority | Description |
|---|---|---|---|---|
| BLI-063 | Measure instruction context overhead | todo | high | Calculate the token count of each always-on instruction. The 5 always-on files are loaded into every conversation — measure their combined size and identify which ones could be loaded on-demand instead |
| BLI-064 | Split chat-capture into core + reference | todo | medium | `chat-capture.instructions.md` is 1000+ lines covering: capture gate, domain routing, file naming, escalation protocol, de-escalation, templates, versioning. Split into: (1) core capture rules (always-on, small), (2) reference doc (loaded on-demand when capture triggers) |
| BLI-065 | Split change-completeness into core + checklists | todo | medium | `change-completeness.instructions.md` is 500+ lines with checklists A through P. Split into: (1) core principle + change-type router (always-on, small), (2) individual checklist files loaded when the change type is identified |
| BLI-066 | Resolve instruction overlaps | todo | medium | chat-capture and session-scoping both define frontmatter fields and session metadata. Clarify ownership: session-scoping owns scope fields, chat-capture owns capture gate and routing. Remove duplicated field definitions |

---

## Phase 6 — Brain Workspace Restructuring (BLI-067 through BLI-071)

### Current State

6-tier brain workspace (inbox, notes, library, sessions, backlog, pkm) with
comprehensive session capture (14 categories, 7 templates, escalation protocol).
The brain is feature-rich but:

- Scripts are PowerShell + shell (no cross-platform single-language solution)
- Backlog is comprehensive but the `guides/` folder has large workflow docs
- PKM tier has accumulated many policy files (9 files) that may overlap

### Target State

Brain workspace with:

- **Simplified scripts** — consistent cross-platform operation
- **Leaner PKM tier** — consolidated policy files
- **Clear boundary** between brain workspace content and brain automation
- **Documented migration path** for exporting to other projects

| ID | Title | Status | Priority | Description |
|---|---|---|---|---|
| BLI-067 | Audit brain PKM policy files for overlap | todo | medium | 9 files in `pkm/`: access-policy, access-log, accounts-and-credentials, capture-sources-inventory, sensitivity-and-access-control, sources-personal, sources-work, unused-tools, workflows. Check for duplicated content between access-policy ↔ sensitivity-and-access-control, sources-personal ↔ capture-sources-inventory |
| BLI-068 | Consolidate brain scripts | todo | medium | 4 script pairs (brain.ps1/.sh, promote.ps1/.sh, clear-inbox.ps1/.sh, brain-module.psm1). Evaluate: can PowerShell Core handle both platforms? Can we reduce to one script per operation? |
| BLI-069 | Create brain workspace export template | todo | low | Package the brain workspace as an exportable template: which files are required, which are optional, what to customize per project. Update export-guide.md and export-newbie-guide.md |
| BLI-070 | Document brain tier boundaries | todo | low | Create a concise reference showing when content goes to inbox vs. notes vs. library vs. sessions vs. backlog vs. pkm. Currently defined across multiple instruction files — consolidate into a single decision tree |
| BLI-071 | Review session escalation protocol complexity | todo | low | The escalation protocol (3 patterns, sub-patterns, de-escalation, name truncation) is thorough but complex. Evaluate if the complexity matches actual usage patterns. Simplify if most sessions never reach escalation |

---

## Phase 7 — Documentation Consolidation (BLI-072 through BLI-074)

### Current State

37 documentation files in `.github/docs/`. Some overlap in coverage (e.g., MCP
is documented in 7 files, Copilot customization in 8 files). Navigation is
supported by `navigation-index.md` and `START-HERE.md` but cross-linking is
incomplete.

### Target State

Documentation that:

- Has a **clear hierarchy** (entry point → overview → deep-dive)
- **No duplication** between docs covering the same topic
- **Complete cross-linking** — no orphan docs
- **Accurate** — all docs reflect the current codebase state

| ID | Title | Status | Priority | Description |
|---|---|---|---|---|
| BLI-072 | Audit docs for content duplication | todo | medium | Compare the 7 MCP docs and 8 customization docs for overlapping content. Identify sections that appear in multiple files and designate a single source of truth for each |
| BLI-073 | Verify all cross-links and navigation | todo | medium | Check that every doc in `.github/docs/` is reachable from START-HERE.md or navigation-index.md. Verify all internal links resolve to existing files. Fix broken or circular links |
| BLI-074 | Create documentation hierarchy diagram | todo | low | Mermaid diagram showing the complete doc hierarchy: entry points → category docs → deep-dive docs → reference docs. Embed in navigation-index.md |

---

## Cross-Cutting Concerns

### Feature Parity Verification (after each phase)

After completing each phase, verify using `features-inventory.md`:

- [ ] All 65 prompts still functional
- [ ] All 23 skills still auto-activate correctly
- [ ] All 8 agents still reachable and handoffs work
- [ ] All 104 MCP tools still available
- [ ] Brain workspace operations (new, search, publish) still work
- [ ] Build passes: `.\gradlew.bat build`
- [ ] Markdown passes: `.\__md_lint.ps1`

### Migration Safety Protocol

For every item in this epic:

1. **Create** the new modularized version alongside the old
2. **Verify** the new version works correctly
3. **Update** references to point to the new version
4. **Archive** the old version (don't delete yet)
5. **Delete** the old version only after verification period

---

## Items Summary

| Phase | Items | Priority Range | Focus |
|---|---|---|---|
| 1. Prompts | BLI-041 through BLI-047 (7 items) | high → low | Frontmatter, composition, extraction, standardization |
| 2. Skills | BLI-048 through BLI-053 (6 items) | high → low | Activation, structure, splitting, cross-references |
| 3. Agents | BLI-054 through BLI-057 (4 items) | high → low | Template, handoffs, tools, selection guide |
| 4. MCP | BLI-058 through BLI-062 (5 items) | high → low | Deprecation, config, wrappers, auto-docs, health |
| 5. Instructions | BLI-063 through BLI-066 (4 items) | high → medium | Context overhead, splitting, overlaps |
| 6. Brain | BLI-067 through BLI-071 (5 items) | medium → low | PKM audit, scripts, export, boundaries, escalation |
| 7. Documentation | BLI-072 through BLI-074 (3 items) | medium → low | Duplication, cross-links, hierarchy |
| **Total** | **34 items** | | |

## Open Questions

- [ ] Should prompt modularization (Phase 1) or skill modularization (Phase 2) come first?
      (They're interdependent — extracting from prompts creates skills)
- [ ] Is the v1 Atlassian server still in use by anyone? Safe to deprecate?
- [ ] Should brain scripts be rewritten in a single language (PowerShell Core)?
- [ ] How much context overhead do the 5 always-on instructions actually consume?
- [ ] Should new skills be created for DSA/system-design/devops domains?

## Dependencies

- **EPIC-004** (stale reference cleanup) should complete first — no point modularizing
  content that still references old paths
- **BLI-042** and **BLI-051** are tightly coupled — extracting from prompts creates
  the skills that BLI-051 defines
- **BLI-063** informs **BLI-064** and **BLI-065** — measure before splitting
