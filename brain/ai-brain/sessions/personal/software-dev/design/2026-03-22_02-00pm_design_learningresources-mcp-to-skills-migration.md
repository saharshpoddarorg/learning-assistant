---
date: 2026-03-22
time: "02:00 PM"
kind: intent-capture
domain: personal
category: design
project: learning-assistant
subject: learningresources-mcp-to-skills-migration
tags: [intent, migration, mcp-server, skill, primitive-selection, learning-resources, project:learning-assistant]
status: draft
version: 1
parent: null
complexity: high
outcomes:
  - Complete catalog of 132 resources across 17 providers
  - Primitive selection analysis covering all 6 GHCP primitives
  - Intent documentation framework created (intent-capture template)
  - Detailed migration guide produced
source: copilot
scope: project
scope-project: learning-assistant
scope-feature: null
scope-transitions: []
scope-refs: []
---

# Intent Capture — Learning Resources MCP Server to Skills Migration

> **Context:** The learning-assistant project has a fully-built Learning Resources MCP server
> (28 Java source files, 17 providers, 132 curated resources, 10 MCP tools, 12-dimensional
> scoring engine). This session evaluates whether the dominant use case (static resource
> discovery) is better served by a SKILL.md approach, and documents the complete intent
> so future iterations can build on this analysis without re-discovering the same context.

---

## Intent Statement

We want to **migrate the curated learning resource vault** (132 static, hand-picked resources
with 5-dimensional metadata) **from a Java MCP server to a SKILL.md-based approach**, because
the dominant use case — discovering and recommending learning resources — is static knowledge
retrieval, which is what skills are natively designed for. The MCP server's search engine,
relevance scorer, and keyword index effectively reimplements capabilities that an LLM already
has natively (semantic search, relevance ranking, intent classification). By migrating to
skills, we eliminate 28 Java source files, a JVM process, and a build system — while making
the resources available in ALL Copilot modes (Ask, Edit, Agent) rather than Agent-only.

The web scraping capabilities (`scrape_url`, `read_url`) and file export (`export_results`)
**cannot** migrate to skills and will be handled separately (either kept as a thin MCP server
or delegated to the `fetch` tool available in agents).

---

## Problem Space

### Current State

| Aspect | Current Implementation | Notes |
|---|---|---|
| Resource storage | 17 Java provider classes returning hardcoded `LearningResource` records | 28 source files total |
| Resource count | 132 resources across 17 providers | Hand-curated with verified metadata |
| Search/discovery | ResourceDiscovery + RelevanceScorer + KeywordIndex | 12-dimensional scoring, 3-mode intent classifier |
| Taxonomy | 7 enums: ResourceCategory(17), ConceptArea(36), ConceptDomain(9), DifficultyLevel(4), ResourceType(13), ContentFreshness(5), LanguageApplicability(6) | Type-safe, exhaustive |
| Keyword mappings | ~130 keyword-to-enum entries in KeywordIndex | Maps natural language to structured queries |
| Web scraping | WebScraper + ContentExtractor + ContentSummarizer + ReadabilityScorer | Full pipeline: HTTP fetch → HTML parse → summarize |
| Export | ExportHandler with Markdown/PDF/Word output | Requires pandoc for PDF/Word |
| Transport | STDIO JSON-RPC | Requires JVM process running |
| Availability | Agent mode only (MCP tools not available in Ask/Edit) | Limits discoverability |
| Maintenance | Java code changes require recompile + restart | Heavy for what is essentially static data |
| Prompt integration | `/resources` slash command delegates to MCP tools | Well-structured with 7 action modes |
| Skill overlap | java-learning-resources, software-engineering-resources skills already exist | Partial duplication of resource data |

### Desired State

| Aspect | Target Implementation | Notes |
|---|---|---|
| Resource storage | SKILL.md with structured Markdown tables | Zero infrastructure |
| Resource count | All 132 resources preserved, organized by domain | Nothing lost |
| Search/discovery | LLM native semantic search over structured tables | LLM already does this |
| Taxonomy | Preserved as section headings and table columns | Human-readable + LLM-parseable |
| Keyword mappings | Not needed — LLM natively understands synonyms | Eliminated complexity |
| Web scraping | Agent `fetch` tool OR thin MCP server | Only if needed |
| Export | Copilot can format Markdown natively; PDF/Word dropped or deferred | Rarely used |
| Transport | None — skill is text injected into context | Zero process overhead |
| Availability | ALL Copilot modes (Ask, Edit, Agent) | Major improvement |
| Maintenance | Edit Markdown files, instant effect | Dramatically simpler |
| Prompt integration | `/resources` prompt updated to reference skill tables | Same user experience |

### Gap Analysis

| Gap | Impact | Priority |
|---|---|---|
| Token budget: 132 resources × ~80 tokens = ~10.5K tokens | Too large for single skill activation | HIGH — requires tiered architecture |
| Web scraping lost | Cannot scrape URLs from a skill | MEDIUM — `fetch` tool in agents covers most cases |
| PDF/Word export lost | Cannot generate files from a skill | LOW — rarely used |
| Runtime resource addition lost | Cannot add resources dynamically | LOW — adding = editing skill file |
| Structured JSON responses lost | Skill produces prose, not JSON | LOW — LLM formats output well |

---

## Capability Inventory

### Capabilities to Preserve (MUST migrate)

| ID | Capability | Current Location | Migration Target | Status |
|---|---|---|---|---|
| C-001 | 132 curated resources with full metadata | 17 provider classes | Skill sub-files organized by domain | Not started |
| C-002 | 5-dimensional categorization (category, concept, type, difficulty, freshness) | Model enums + LearningResource record | Table columns in skill files | Not started |
| C-003 | Category browsing | `browse_vault` tool + SearchHandler | Section headings in skill file | Not started |
| C-004 | Difficulty-based filtering | DifficultyLevel enum + range queries | Difficulty column + LLM filtering | Not started |
| C-005 | Resource type classification | ResourceType enum (13 values) | Type column with emoji badges | Not started |
| C-006 | Free/paid and official/community flags | Boolean fields on LearningResource | Badge columns in tables | Not started |
| C-007 | Language applicability metadata | LanguageApplicability enum (6 values) | Language scope column | Not started |
| C-008 | Freshness metadata | ContentFreshness enum (5 values) | Freshness column | Not started |
| C-009 | Tag-based discovery | Tags list on each resource | Tags column or section grouping | Not started |
| C-010 | ConceptArea-based grouping | ConceptArea enum (36 values) + ConceptDomain (9) | Hierarchical sections by domain | Not started |
| C-011 | `/resources` slash command UX | resources.prompt.md | Updated prompt referencing skill | Not started |
| C-012 | Display formatting (badges, icons, difficulty indicators) | Defined in resources.prompt.md | Preserved in prompt instructions | Not started |
| C-013 | Cross-references to other commands | Defined in resources.prompt.md | Preserved in prompt instructions | Not started |

### Capabilities to Enhance (SHOULD improve)

| ID | Capability | Current Limitation | Enhancement | Priority |
|---|---|---|---|---|
| E-001 | Resource availability | Agent mode only | Available in ALL modes via skill | HIGH |
| E-002 | Discovery speed | Requires JVM startup + JSON-RPC round-trip | Instant (text in context) | HIGH |
| E-003 | Maintenance effort | Java code change + recompile + restart | Edit Markdown, instant | HIGH |
| E-004 | Consolidation with existing skills | Separate from java-learning-resources and software-engineering-resources | Single unified resource skill or coordinated sub-skills | MEDIUM |
| E-005 | Portability | 28 Java files + build system | Single skill folder | MEDIUM |

### Capabilities to Deprecate (WILL remove)

| ID | Capability | Reason for Deprecation |
|---|---|---|
| D-001 | Web scraping (`scrape_url`, `read_url`) | Agent `fetch` tool provides equivalent capability natively |
| D-002 | PDF/Word export (`export_results`) | Rarely used; Copilot generates Markdown natively; pandoc dependency is heavy |
| D-003 | Runtime resource addition (`add_resource`, `add_resource_from_url`) | Adding resources = editing the skill file directly; this is simpler |
| D-004 | 12-dimensional relevance scoring | LLM natively ranks by relevance; the algorithm reimplements LLM capabilities |
| D-005 | KeywordIndex (130 keyword-to-enum mappings) | LLM natively understands synonyms and concept relationships |
| D-006 | 3-mode intent classification (specific/vague/exploratory) | LLM naturally adapts response style based on query intent |
| D-007 | ResourceVault thread-safe ConcurrentHashMap | No concurrent access in a text file; irrelevant |
| D-008 | STDIO JSON-RPC transport | No external process needed |

---

## Primitive Selection Rationale

### Evaluated Options

| Primitive | Fit (1-5) | Strengths | Weaknesses | Verdict |
|---|---|---|---|---|
| MCP Server | 3 | Web scraping, structured JSON, runtime state | Over-engineered for static data, Agent-only, heavy infra | Deprecate for vault; keep option for scraping |
| SKILL.md | 5 | Perfect fit for static reference data, all-mode access, zero infra, auto-activation | Token budget requires tiering, no file I/O | **PRIMARY choice** |
| .prompt.md | 4 | `/resources` workflow already exists, structured UX | Not a data source — companion to skill/MCP | Keep as workflow layer |
| .agent.md | 3 | Learning-Mentor persona well-suited | Not a data source — orthogonal to data question | Keep as persona layer |
| .instructions.md | 1 | Wrong primitive — rules, not knowledge | Semantic mismatch | Not applicable |
| Hybrid (Skill + thin MCP) | 4 | Best of both worlds if scraping is needed | Extra complexity | Fallback if `fetch` tool is insufficient |

### Decision

**Primary: SKILL.md** — The 132 curated resources are **static knowledge** that Copilot should
**know when relevant** (sentence 2 of the 5-sentence rule). This is textbook skill territory.

**Companion: .prompt.md** — The `/resources` slash command provides the **workflow trigger**
(sentence 3) and stays as the interaction layer.

**Persona: .agent.md** — The Learning-Mentor agent provides the **expert persona** (sentence 4)
and stays unchanged.

**Deprecated: MCP Server** — The Learning Resources MCP server is deprecated because it stores
static data in a process that requires JVM infrastructure and limits availability to Agent mode.

**Web scraping coverage:** The `fetch` tool available in agent mode covers URL fetching. If deeper
scraping (HTML extraction, readability scoring) is needed in the future, a thin MCP server can
be reintroduced with only the scraping tools.

---

## Content Inventory

### Resources to Migrate (132 total)

| Provider | Count | Domain Grouping |
|---|---|---|
| JavaResources | 17 | Programming > Java |
| WebResources | 5 | Programming > Web/JavaScript |
| PythonResources | 4 | Programming > Python |
| FrameworksResources | 11 | Programming > Frameworks |
| AlgorithmsResources | 3 | Core CS > Algorithms |
| DataStructuresResources | 8 | Core CS > Data Structures |
| EngineeringResources | 6 | Software Engineering |
| DevOpsResources | 6 | DevOps & Tooling |
| VcsResources | 9 | DevOps & Tooling > Version Control |
| BuildToolsResources | 11 | DevOps & Tooling > Build Tools |
| CloudInfraResources | 7 | Infrastructure > Cloud & IaC |
| DataAndSecurityResources | 8 | Databases & Security |
| TestingToolsResources | 5 | Testing |
| AiMlResources | 4 | AI & Machine Learning |
| DigitalNotetakingResources | 15 | Productivity > PKM & Note-Taking |
| GeneralResources | 5 | General CS & Career |
| SelfDevelopmentResources | 13 | Personal Development |
| **TOTAL** | **132** | |

### Mechanisms to Migrate

| # | Mechanism | Current Implementation | Migration Approach | Status |
|---|---|---|---|---|
| 1 | Resource catalog | Java records in providers | Markdown tables in skill sub-files | Not started |
| 2 | Category browsing | `browse_vault` hand-coded logic | Section headings by domain | Not started |
| 3 | Difficulty indication | DifficultyLevel enum | Emoji badges (green/yellow/red/black) | Not started |
| 4 | Type classification | ResourceType enum | Type column with descriptive labels | Not started |
| 5 | Official/free flags | Boolean fields | Badge columns (checkmark/cross) | Not started |
| 6 | Display formatting | Defined in resources.prompt.md | Preserved and enhanced in prompt | Not started |

### Metadata / Taxonomy to Migrate

| # | Taxonomy | Values | Migration Target | Status |
|---|---|---|---|---|
| 1 | ResourceCategory | 17 values | Section group headings | Not started |
| 2 | ConceptArea | 36 values | Sub-section headings or tags | Not started |
| 3 | ConceptDomain | 9 values | Top-level sections in skill | Not started |
| 4 | DifficultyLevel | 4 values | Emoji column | Not started |
| 5 | ResourceType | 13 values | Type column | Not started |
| 6 | ContentFreshness | 5 values | Maintenance column | Not started |
| 7 | LanguageApplicability | 6 values | Scope column | Not started |

---

## Key Outcomes

- Complete catalog of 132 resources across 17 providers documented
- All 6 GHCP primitives analyzed for fit; SKILL.md selected as primary
- Intent documentation framework (intent-capture template) created
- Capability inventory with MUST/SHOULD/DEPRECATE classification
- Migration phases defined with verification checklist

---

## Follow-Up / Next Steps

- [ ] Create the migration guide with step-by-step instructions
- [ ] Execute Phase 1: Create skill file structure
- [ ] Execute Phase 2: Migrate all 132 resources to skill tables
- [ ] Execute Phase 3: Update `/resources` prompt and cross-references
- [ ] Execute Phase 4: Deprecate MCP server
- [ ] Execute Phase 5: Build verification and testing

---

## Session Metadata

| Property | Value |
|---|---|
| Duration | ~8 exchanges |
| Files touched | intent-capture template, this intent document, migration guide |
| Related sessions | none (first session on this topic) |
