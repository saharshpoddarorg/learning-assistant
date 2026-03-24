---
date: 2026-03-24
time: "09:21 PM"
kind: session-capture
domain: personal
category: design
project: learning-assistant
subject: learningresources-master-architecture-specification
tags: [project:learning-assistant, gh:saharshpoddarorg/learning-assistant, design, architecture, java, mcp, skills, extensibility, search-engine, learning-library, web-scraper, specification]
status: draft
version: 1
parent: 2026-03-24_06-00pm_module-architecture-deep-dive.md
complexity: high
outcomes:
  - consolidated all prior design sessions into one exhaustive reference document
  - documented every enum value, interface, record, and extension point
  - specified complete search engine enhancement roadmap with algorithm details
  - specified complete web scraper/processor pipeline with package-level design
  - specified learning intelligence engine with prerequisite graph and assessment
  - defined configuration schema across all layers
  - provided phase-by-phase implementation guide with code patterns
  - documented all 7 design approaches with full trade-offs
  - established canonical extension patterns for adding any future feature
source: copilot
scope: project
scope-project: learning-assistant
scope-feature: null
scope-transitions: []
scope-refs:
  - file: "personal/software-dev/design/learningresources/2026-03-24_06-00pm_module-architecture-deep-dive.md"
    relationship: continuation
    note: "prior deep-dive analysis — this document consolidates and exhaustively expands it"
  - file: "personal/software-dev/design/learningresources/2026-03-24_03-00pm_package-restructuring-analysis.md"
    relationship: related
    note: "package restructuring analysis — 6 approaches for enum/model placement"
  - file: "personal/software-dev/design/learningresources/2026-03-22_02-00pm_mcp-to-skills-migration.md"
    relationship: related
    note: "original MCP-to-skills migration intent and capability inventory"
  - file: "personal/software-dev/design/learningresources/2026-03-22_05-30pm_migration-complete-audit.md"
    relationship: related
    note: "migration audit with every enum value and resource verified"
---

# Learning Resources — Master Architecture Specification

> **Purpose:** Single, exhaustive, canonical reference for designing the learning-assistant's
> learning resources module, search engine, web scraper, and learning intelligence engine.
> This document consolidates 4 prior design sessions into one comprehensive specification
> that covers current state, all evaluated approaches, the recommended architecture,
> complete domain model, extension points, configuration schema, algorithm design,
> and a phased implementation roadmap.
>
> **Audience:** Future-me designing and building this system. Every detail needed to make
> implementation decisions is here — no need to re-read the 4 prior sessions.
>
> **Consolidates:**
>
> - [MCP-to-skills migration intent](2026-03-22_02-00pm_mcp-to-skills-migration.md) (Mar 22)
> - [Migration audit](2026-03-22_05-30pm_migration-complete-audit.md) (Mar 22)
> - [Package restructuring analysis](2026-03-24_03-00pm_package-restructuring-analysis.md) (Mar 24)
> - [Architecture deep-dive](2026-03-24_06-00pm_module-architecture-deep-dive.md) (Mar 24)

---

## Table of Contents

1. [Vision and Goals](#1--vision-and-goals)
2. [Current State — Complete Inventory](#2--current-state--complete-inventory)
3. [Complete Domain Model Specification](#3--complete-domain-model-specification)
4. [Architecture Approaches — All 7 Evaluated](#4--architecture-approaches--all-7-evaluated)
5. [Recommended Architecture — Layered Hybrid](#5--recommended-architecture--layered-hybrid)
6. [Search Engine — Complete Specification](#6--search-engine--complete-specification)
7. [Web Scraper & Processor — Complete Specification](#7--web-scraper--processor--complete-specification)
8. [Learning Intelligence Engine — Complete Specification](#8--learning-intelligence-engine--complete-specification)
9. [Keyword/Tagging System — Algorithm Design](#9--keywordtagging-system--algorithm-design)
10. [Configuration & Extensibility — Complete Schema](#10--configuration--extensibility--complete-schema)
11. [Copilot Integration — Skills, Prompts, Agents, Instructions](#11--copilot-integration--skills-prompts-agents-instructions)
12. [Package Structure — Current and Target](#12--package-structure--current-and-target)
13. [Phased Implementation Roadmap](#13--phased-implementation-roadmap)
14. [Decision Log](#14--decision-log)
15. [Extension Patterns — How to Add Anything](#15--extension-patterns--how-to-add-anything)
16. [Appendix — Every Enum Value](#16--appendix--every-enum-value)
17. [Appendix — Interface Contracts](#17--appendix--interface-contracts)
18. [Follow-Up / Next Steps](#18--follow-up--next-steps)

---

## 1 — Vision and Goals

### The Vision

A **complete, self-sustaining learning platform** that:

- Curates and organizes learning resources across all software engineering domains
- Supports multiple learning purposes: professional SE, personal development, career growth
- Progresses from static curated resources to dynamic web-fetched content
- Provides adaptive learning guidance based on user level and goals
- Integrates search, discovery, assessment, and recommendation into a unified experience
- Is configurable, extensible, and customizable at every layer

### Design Principles

| Principle | Meaning | Implication |
|---|---|---|
| **Layered architecture** | Each concern gets its own layer | Skills for static knowledge, MCP for dynamic capabilities, search-engine for algorithms |
| **Open for extension** | New features don't require modifying existing code | Strategy pattern, provider interfaces, plugin registries |
| **Closed for modification** | Stable core code doesn't change when extending | Enum taxonomy stays stable; new features are new classes |
| **Static by default, dynamic on demand** | Curated content is instant; dynamic features are opt-in | Skills load at zero cost; MCP tools called only when needed |
| **Java backend** | Java 21+ for all coded components | Leverage existing 80+ file codebase, daily expertise, virtual threads |
| **Type-safe core, flexible edges** | Enums for stable taxonomy; strings for evolving tags | Compile-time safety where it matters; flexibility where it helps |
| **LLM-native where possible** | Let the LLM do what LLMs do best | Semantic search via skills; algorithmic search via code |
| **Incremental delivery** | Build toward the vision in phases | Each phase delivers usable value; no big-bang rewrite |

### Non-Functional Requirements

| Requirement | Target | Rationale |
|---|---|---|
| Search latency | < 50ms for vault queries | In-memory index; no I/O during search |
| Resource limit | 500+ without performance degradation | InMemoryIndex with ConcurrentHashMap |
| Token efficiency | < 2000 tokens for skill activation | Tiered skill sub-files loaded on demand |
| Offline capability | Full vault + skills offline | Only web fetch/scrape requires network |
| Build time | < 10s for full compile | Simple javac; no framework overhead |
| Extensibility | Add new resource in < 5 minutes | One markdown row (skill) or one Java object (vault) |
| Testability | Every algorithm independently testable | Strategy/interface patterns; no singletons |

---

## 2 — Current State — Complete Inventory

### Component Map

```text
learning-assistant/
├── .github/
│   ├── skills/
│   │   ├── learning-resources-vault/   ← 138 curated resources (CANONICAL)
│   │   │   ├── SKILL.md               ← Entry point, Quick Index, badge legend
│   │   │   ├── taxonomy-reference.md   ← All 8 enums (97 values), 300+ keywords
│   │   │   ├── migration-mapping.md    ← MCP → Skill migration traceability
│   │   │   ├── resources-java.md       ← 20 resources
│   │   │   ├── resources-python.md     ← 6 resources
│   │   │   ├── resources-web-javascript.md ← 12 resources
│   │   │   ├── resources-algorithms-ds.md  ← 11 resources
│   │   │   ├── resources-software-engineering.md ← 11 resources
│   │   │   ├── resources-devops-vcs-build.md     ← 25 resources
│   │   │   ├── resources-cloud-infra.md          ← 15 resources
│   │   │   ├── resources-ai-ml.md                ← 4 resources
│   │   │   ├── resources-productivity-pkm.md     ← 15 resources
│   │   │   └── resources-general-career.md       ← 19 resources
│   │   ├── java-learning-resources/    ← Deep Java content + offline scraped
│   │   ├── software-engineering-resources/ ← SE foundations, books, patterns
│   │   ├── career-resources/           ← Career paths, roles, compensation
│   │   └── daily-assistant-resources/  ← Finance, productivity
│   ├── prompts/                        ← 14+ slash commands
│   │   ├── resources.prompt.md         ← /resources
│   │   ├── learn-concept.prompt.md     ← /learn-concept
│   │   ├── deep-dive.prompt.md         ← /deep-dive
│   │   ├── reading-plan.prompt.md      ← /reading-plan
│   │   ├── teach.prompt.md             ← /teach
│   │   ├── interview-prep.prompt.md    ← /interview-prep
│   │   ├── dsa.prompt.md              ← /dsa
│   │   ├── system-design.prompt.md     ← /system-design
│   │   ├── language-guide.prompt.md    ← /language-guide
│   │   └── ... (4+ more)
│   ├── agents/
│   │   ├── learning-mentor.agent.md    ← Teaching persona, building-block tree
│   │   ├── designer.agent.md           ← Architecture review
│   │   ├── debugger.agent.md           ← Hypothesis-driven debugging
│   │   └── ... (3+ more)
│   └── instructions/
│       ├── change-completeness.instructions.md ← applyTo: **
│       ├── md-formatting.instructions.md       ← applyTo: **
│       ├── java.instructions.md                ← applyTo: **/*.java
│       └── ... (5+ more)
├── mcp-servers/src/server/learningresources/  ← 57 Java files (DEPRECATED)
│   ├── LearningResourcesServer.java           ← MCP entry point
│   ├── model/ (12 files)                      ← 8 enums (97 values) + 4 records
│   ├── vault/ (10 + 17 providers)            ← In-memory vault, discovery, scoring
│   ├── handler/ (6 files)                     ← MCP tool dispatch
│   ├── content/ (4 files)                     ← Content summarization
│   ├── scraper/ (5 files)                     ← Web scraping
│   └── search/ (2 files)                      ← Bridge to search-engine
├── search-engine/src/search/                  ← 26 Java files (ACTIVE + GENERIC)
│   ├── api/ (11 interfaces)                   ← SearchEngine, ScoringStrategy, etc.
│   └── engine/ (15 implementations)           ← BM25, TextMatch, Composite, etc.
└── brain/ai-brain/sessions/                   ← Session capture workspace
```

### Quantitative Summary

| Metric | Count | Notes |
|---|---|---|
| Total curated resources | 138 | Across 10 skill sub-files |
| Java model files | 12 | 8 enums + 4 records |
| Enum values (total) | 97 | Across 8 enum types |
| Keyword index entries | 300+ | In taxonomy-reference.md |
| Search engine interfaces | 11 | Generic, type-parameterized |
| Search engine implementations | 15 | BM25, TextMatch, Composite, etc. |
| Vault providers | 17 | One per domain |
| MCP tools | 10 | search, browse, discover, scrape, export, etc. |
| Slash commands (prompts) | 14+ | /resources, /learn-concept, /deep-dive, etc. |
| Agents | 5+ | learning-mentor (primary), designer, debugger, etc. |
| Skills (learning-related) | 5 | vault, java-learning, SE, career, daily |
| Instructions files | 9 | Formatting, build, completeness, capture, etc. |

### What Is Working vs. Deprecated

| Component | Status | Consumers |
|---|---|---|
| Skills (learning-resources-vault/) | **Active, canonical** | LLM reads directly |
| Skills (java-learning-resources/) | **Active** | LLM reads directly |
| Skills (software-engineering-resources/) | **Active** | LLM reads directly |
| Prompts (14 slash commands) | **Active** | User invokes via / |
| Agents (learning-mentor, etc.) | **Active** | User selects in dropdown |
| Search engine (search-engine/) | **Active, generic** | MCP server + future consumers |
| MCP learningresources server | **Deprecated** | Was used by /resources prompt |
| Java model enums (97 values) | **Active (code retained)** | MCP server + search engine |
| Java vault providers (17 classes) | **Deprecated (code retained)** | MCP server only |
| Java handlers (6 classes) | **Deprecated (code retained)** | MCP server only |

---

## 3 — Complete Domain Model Specification

### Entity Relationship Diagram

```text
┌─────────────────────────────────────────────────────────────┐
│                    LearningResource                          │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │ id: String (URL-safe slug)                              │ │
│  │ title: String                                           │ │
│  │ url: String (canonical URL)                             │ │
│  │ description: String (multi-sentence)                    │ │
│  │ author: String (name or organization)                   │ │
│  │ isOfficial: boolean                                     │ │
│  │ isFree: boolean                                         │ │
│  │ addedAt: Instant                                        │ │
│  └─────────────────────────────────────────────────────────┘ │
│                                                              │
│  ┌─── Enum References ──────────────────────────────────┐    │
│  │ type: ResourceType (1)                                │    │
│  │ categories: List<ResourceCategory> (1..N)            │    │
│  │ conceptAreas: List<ConceptArea> (1..N)               │    │
│  │ difficulty: DifficultyLevel (1)                       │    │
│  │ freshness: ContentFreshness (1)                      │    │
│  │ languageApplicability: LanguageApplicability (1)     │    │
│  └───────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌─── Free-Form ────────────────────────────────────────┐    │
│  │ tags: List<String> (0..N) — user-defined keywords    │    │
│  └───────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
         │                    │                     │
         ▼                    ▼                     ▼
    ResourceType(13)   ResourceCategory(17)   ConceptArea(40)
                                                    │
                                                    ▼
                                            ConceptDomain(9)
```

### LearningResource Record — Full Field Specification

```java
public record LearningResource(
    String id,                          // URL-safe slug: "oracle-java-tutorials"
    String title,                       // Human-readable: "Oracle Java Tutorials"
    String url,                         // Canonical: "https://docs.oracle.com/..."
    String description,                 // Multi-sentence summary of the resource
    ResourceType type,                  // Content format (13 values)
    List<ResourceCategory> categories,  // Topic domains (OR logic: any match)
    List<ConceptArea> conceptAreas,     // Fine-grained concepts (OR logic)
    List<String> tags,                  // Free-form keywords for search
    String author,                      // Name or organization
    DifficultyLevel difficulty,         // Skill level (4 ordered values)
    ContentFreshness freshness,         // Maintenance status (5 values)
    boolean isOfficial,                 // Authoritative source flag
    boolean isFree,                     // Accessibility flag
    LanguageApplicability languageApplicability,  // Language scope (6 values)
    Instant addedAt                     // Metadata timestamp
) {
    // Defensive copying in compact constructor
    // Helper methods: hasCategory(), hasConcept(), hasTag(), isDifficultyInRange()
    // searchableText() — concatenates all searchable fields for full-text matching
}
```

### ResourceQuery Record — Full Field Specification

```java
public record ResourceQuery(
    String searchText,                  // Free-text search across all fields
    ResourceType type,                  // Exact filter on content type
    List<ResourceCategory> categories,  // OR filter: any category matches
    ConceptArea conceptArea,            // Exact filter on concept area
    DifficultyLevel minDifficulty,      // Inclusive lower bound
    DifficultyLevel maxDifficulty,      // Inclusive upper bound
    ContentFreshness freshness,         // Exact filter on freshness
    boolean officialOnly,               // If true, only official sources
    List<String> tags,                  // AND filter: all tags must match
    boolean freeOnly,                   // If true, only free resources
    int maxResults                      // Result cap (default 15)
) {
    // Factory: ALL — returns everything (no filters)
    // Factory: byText(text) — free-text search only
    // Factory: byCategory(cat) — single category filter
    // Factory: byType(type) — single type filter
    // Factory: byConcept(area) — single concept filter
    // hasFilters() — returns true if any filter is active
}
```

### Skill Table Schema — How Resources Appear in Markdown

Each resource sub-file uses this table format:

```markdown
| Title | Type | Diff | Concepts | Author | Fresh | Off | Free | Lang |
|---|---|---|---|---|---|---|---|---|
| [Oracle Java Tutorials](https://...) | 📖 Docs | 🟢 BEG | language-basics, oop | Oracle | 🔄 Active | ✅ | ✅ | ☕ Java |
```

**Badge Legend:**

| Column | Badges | Meaning |
|---|---|---|
| Type | 📖 Docs, 📝 Tutorial, 📰 Blog, 📄 Article, 🎥 Video, 🎬 Playlist, 🎓 Course, 📚 Book, 🎮 Interactive, 📋 API Ref, 📎 Cheatsheet, 🔧 Repo | ResourceType enum |
| Diff | 🟢 BEG, 🟡 INT, 🔴 ADV, ⚫ EXP | DifficultyLevel enum |
| Fresh | ♾️ Evergreen, 🔄 Active, 📅 Periodic, 📜 Historical, 🗄️ Archived | ContentFreshness enum |
| Off | ✅ Official, ➖ Community | isOfficial boolean |
| Free | ✅ Free, 💰 Paid | isFree boolean |
| Lang | ☕ Java, 🐍 Python, 🌐 Web, 🔤 Multi, 🌍 Universal, ☕+ Java-Centric | LanguageApplicability enum |

---

## 4 — Architecture Approaches — All 7 Evaluated

### Summary Matrix (12 Dimensions, 1-5 Scale)

| Dimension | A: Pure Skills | B: Pure MCP | C: Hybrid | D: Agent-Centric | E: Instructions | F: Prompts | G: Full Platform |
|---|---|---|---|---|---|---|---|
| Configurability | 3 | 4 | 5 | 4 | 3 | 3 | 5 |
| Extensibility | 3 | 4 | 5 | 4 | 1 | 3 | 5 |
| Customizability | 4 | 3 | 5 | 5 | 3 | 4 | 5 |
| Search quality | 3 | 5 | 5 | 4 | 0 | 2 | 5 |
| Dynamic content | 0 | 4 | 4 | 4 | 0 | 1 | 5 |
| Runtime deps | 5 | 1 | 3 | 3 | 5 | 5 | 1 |
| Token cost | 4 | 2 | 4 | 3 | 5 | 5 | 3 |
| Maintenance | 5 | 3 | 3 | 3 | 5 | 5 | 2 |
| Future features | 1 | 5 | 5 | 4 | 1 | 1 | 5 |
| Type safety | 0 | 5 | 3 | 0 | 0 | 0 | 5 |
| Offline | 5 | 5 | 4 | 3 | 5 | 5 | 4 |
| Learning guidance | 3 | 5 | 5 | 5 | 1 | 3 | 5 |
| **Weighted Total** | **38** | **64** | **73** | **64** | **26** | **40** | **80** |

### Winner: Approach C — Layered Hybrid (Best Power-to-Complexity Ratio)

### Aspirational: Approach G — Full Platform (Build Toward Incrementally)

### Approach Definitions

#### A: Pure Skills (Markdown-Only)

- LLM IS the search engine; all knowledge as markdown tables
- Zero runtime, zero compilation, zero dependencies
- Cannot do web scraping, dynamic fetch, or algorithmic search
- **Role in hybrid:** Static knowledge layer — what you already have

#### B: Pure MCP Server (Java Backend Only)

- Code is authority; 10+ MCP tools for everything
- Maximum algorithmic power: BM25, composite scoring, web scraping
- High token cost per tool call; requires running JVM
- **Role in hybrid:** Dynamic capability layer for features skills can't do

#### C: Layered Hybrid (Skills + MCP + Search Engine)

- Skills for static curated knowledge (zero-cost LLM context)
- MCP server for dynamic capabilities (scrape, fetch, assess, guide)
- Search engine as shared, generic library
- Agents for teaching intelligence; prompts for user entry points
- **This is the recommended approach**

#### D: Agent-Centric (Multi-Agent Orchestration)

- Multiple specialized agents: mentor, researcher, assessor, curator
- Strong for teaching; agent handoffs still manual in Copilot
- **Role in hybrid:** Teaching layer — compose with C

#### E: Instructions-Based (Behavioral Rules)

- Encode rules for how to handle learning content
- Cannot add features; only modifies behavior
- **Role in hybrid:** Governance layer — quality enforcement on top of C

#### F: Prompt-Centric (Slash Commands)

- Each workflow is a parameterized prompt
- Entry points only; need skills + MCP behind them
- **Role in hybrid:** User interface layer — how users access the system

#### G: Full Platform (Maximum Vision)

- All of the above plus: learning-engine, web-processor, recommendation
- Prerequisite DAG, adaptive paths, assessments, collaborative filtering
- **Role in hybrid:** Target state to build toward incrementally

---

## 5 — Recommended Architecture — Layered Hybrid

### The 6-Layer Architecture

```text
┌─────────────────────────────────────────────────────────────────────┐
│  LAYER 6 — USER INTERFACE                                            │
│  Copilot Chat → Slash commands (/resources, /learn-concept, etc.)    │
│  Implementation: .prompt.md files                                    │
│  Token cost: ~100 tokens per prompt loaded                           │
└──────────────────────────────────┬──────────────────────────────────┘
                                   ▼
┌─────────────────────────────────────────────────────────────────────┐
│  LAYER 5 — INTELLIGENCE                                              │
│  Agents (learning-mentor, learning-researcher)                       │
│  Instructions (quality rules, taxonomy validation)                   │
│  Implementation: .agent.md + .instructions.md files                  │
│  Token cost: ~500-2000 tokens when active                            │
└───────────┬──────────────────────────────────┬──────────────────────┘
            ▼                                  ▼
┌──────────────────────────┐    ┌───────────────────────────────────┐
│  LAYER 4 — KNOWLEDGE     │    │  LAYER 3 — CAPABILITIES           │
│  (Skills — static)       │    │  (MCP Server — dynamic)           │
│                          │    │                                   │
│  learning-resources-     │    │  Vault search                     │
│    vault/ (138 rsrc)     │    │  Web scraping                     │
│  java-learning-          │    │  Dynamic fetch                    │
│    resources/            │    │  Learning path guidance           │
│  software-engineering-   │    │  Knowledge assessment             │
│    resources/            │    │  Export (Markdown/PDF)             │
│  career-resources/       │    │                                   │
│  daily-assistant-        │    │  Implementation: Java MCP server  │
│    resources/            │    │  Transport: STDIO JSON-RPC        │
│                          │    │  Runtime: JDK 21+                 │
│  Token cost: 0 (loaded   │    │  Token cost: ~2000/tool call      │
│    only when activated)  │    │                                   │
└──────────────────────────┘    └──────────────────┬────────────────┘
                                                   ▼
                                ┌───────────────────────────────────┐
                                │  LAYER 2 — ALGORITHMS              │
                                │  (Search Engine — generic library)  │
                                │                                   │
                                │  5-phase pipeline:                 │
                                │    classify→filter→score→rank→trim │
                                │  BM25, TextMatch, Composite       │
                                │  Fuzzy matching, tag scoring      │
                                │  Filter chain, rank composition   │
                                │                                   │
                                │  Implementation: Java (26 files)  │
                                │  Dependency: zero (standalone)     │
                                └──────────────────┬────────────────┘
                                                   ▼
                                ┌───────────────────────────────────┐
                                │  LAYER 1 — DOMAIN MODEL            │
                                │  (Shared types — enums + records)   │
                                │                                   │
                                │  8 enums (97 values)              │
                                │  4 records (LearningResource, etc)│
                                │  KeywordIndex (300+ mappings)     │
                                │                                   │
                                │  Implementation: Java (12 files)  │
                                │  Dependency: zero                  │
                                └───────────────────────────────────┘
```

### Layer Interaction Flow — Example Queries

**Query: "I want to learn Java concurrency" (static + teaching)**

```text
1. LAYER 6: User types in Copilot Chat
2. LAYER 5: learning-mentor agent activated (teaching persona)
3. LAYER 4: learning-resources-vault SKILL.md activated
   → LLM reads resources-java.md → finds concurrency resources
   → LLM reads taxonomy-reference.md for concept hierarchy
   → LLM applies native reasoning to filter by difficulty, official status
4. LAYER 5: Agent provides structured teaching response
   → What is concurrency? Why? Analogy → Code → Anti-pattern → Next steps
   → Cites specific resources from vault
```

**Query: "/resources search docker tutorial beginner --web" (dynamic fetch)**

```text
1. LAYER 6: /resources prompt activated with parameters
2. LAYER 5: learning-mentor agent selected
3. LAYER 4: Skill loads → finds curated Docker resources
4. LAYER 3: MCP tool call: fetch_web_resources("docker tutorial beginner")
   → Server fetches fresh content from web sources
   → Passes through LAYER 2 search engine for scoring
5. LAYER 2: BM25 + TextMatch + TagScorer rank results
6. LAYER 3: Returns merged curated + web results
7. LAYER 5: Agent formats response with difficulty badges
```

**Query: "/resources learning-path java-concurrency intermediate" (guidance)**

```text
1. LAYER 6: /resources prompt with learning-path mode
2. LAYER 3: MCP tool call: suggest_learning_path("java-concurrency", "intermediate")
   → Server consults prerequisite graph
   → Generates ordered path: threads → synchronization → locks → executors → virtual-threads
   → Maps each step to vault resources
3. LAYER 4: LLM enriches with teaching context from skill
4. LAYER 5: Agent structures response with checkboxes and milestones
```

### Why Each Layer Exists

| Layer | Why Not Skip It? |
|---|---|
| **6 — Prompts** | Without prompts, users have no structured entry points (just raw chat) |
| **5 — Agents/Instructions** | Without agents, responses lack teaching personality and methodology |
| **4 — Skills** | Without skills, every resource lookup costs 2000+ tokens (MCP round-trip) |
| **3 — MCP Server** | Without MCP, no web scraping, dynamic fetch, or algorithmic features |
| **2 — Search Engine** | Without search engine, MCP server reimplements search from scratch |
| **1 — Domain Model** | Without model, no type safety, no shared vocabulary across layers |

---

## 6 — Search Engine — Complete Specification

### Current Architecture (Built and Working)

```text
search-engine/src/search/
├── api/                              ← PUBLIC CONTRACTS (depend only on these)
│   ├── core/
│   │   ├── SearchEngine.java         ← Top-level interface: search(context)
│   │   ├── SearchContext.java        ← Query encapsulation (input, mode, filters)
│   │   ├── SearchResult.java        ← Output: items, mode, summary, suggestions
│   │   ├── ScoredItem.java          ← Item + score + optional breakdown
│   │   └── ScoreBreakdown.java      ← Score component explanation
│   ├── classify/
│   │   ├── SearchMode.java          ← SPECIFIC | VAGUE | EXPLORATORY
│   │   └── QueryClassifier.java     ← Determines intent from query text
│   ├── algorithm/
│   │   ├── ScoringStrategy.java     ← Functional: item × context → int score
│   │   └── Tokenizer.java          ← Text → token list
│   ├── filter/
│   │   └── SearchFilter.java       ← Predicate: item × context → boolean
│   ├── rank/
│   │   └── RankingStrategy.java    ← Reorders scored items
│   └── index/
│       └── SearchIndex.java        ← Document storage: add, remove, all, findById
├── engine/                            ← IMPLEMENTATIONS (internal, may change)
│   ├── core/
│   │   └── ConfigurableSearchEngine.java  ← 5-phase pipeline implementation
│   ├── classify/
│   │   └── KeywordQueryClassifier.java    ← Keyword-list-driven classifier
│   ├── algorithm/
│   │   ├── TextMatchScorer.java           ← Multi-tier text matching
│   │   ├── Bm25Scorer.java               ← Okapi BM25 probabilistic ranking
│   │   ├── TagScorer.java                ← Tag-set overlap scoring
│   │   ├── CompositeScorer.java          ← Weighted sum of strategies
│   │   ├── FuzzyMatcher.java             ← Fuzzy prefix/substring matching
│   │   └── DefaultTokenizer.java         ← Stop-word removal + min-length
│   ├── filter/
│   │   └── FilterChain.java             ← AND-chain with short-circuit
│   ├── rank/
│   │   ├── ScoreRanker.java             ← Default: score-descending stable sort
│   │   └── RecencyBoostRanker.java      ← Time-decay bonus for recent docs
│   ├── index/
│   │   ├── InMemoryIndex.java           ← ConcurrentHashMap store
│   │   └── KeywordRegistry.java         ← Keyword → enum resolution
│   └── config/
│       └── SearchEngineConfig.java      ← Builder for all components
```

### The 5-Phase Pipeline

```text
                    ┌──────────────────────────┐
                    │     USER INPUT            │
                    │  "java concurrency int"   │
                    └────────────┬─────────────┘
                                 ▼
┌──────────────────────────────────────────────────────────────────┐
│  PHASE 1: CLASSIFY                                                │
│  KeywordQueryClassifier                                          │
│  ┌────────────────────────────────────────────────────────┐      │
│  │ Rules (in priority order):                              │      │
│  │  1. Quoted text → SPECIFIC                              │      │
│  │  2. Specific keywords (docs, official, reference) → SP  │      │
│  │  3. Exploratory keywords (learn, start, beginner) → EX  │      │
│  │  4. Known vocabulary hit (3+ concept words) → VAGUE     │      │
│  │  5. Short query (≤ 2 words, no keywords) → SPECIFIC     │      │
│  │  6. Default → VAGUE                                     │      │
│  └────────────────────────────────────────────────────────┘      │
│  Result: SearchMode.VAGUE                                        │
└──────────────────────────────────┬───────────────────────────────┘
                                   ▼
┌──────────────────────────────────────────────────────────────────┐
│  PHASE 2: FILTER                                                  │
│  FilterChain (AND-composed)                                      │
│  ┌────────────────────────────────────────────────────────┐      │
│  │ Filters (all must pass):                                │      │
│  │  - category = JAVA (if specified)                       │      │
│  │  - NOT archived                                         │      │
│  │  - difficulty IN [BEGINNER..ADVANCED] (if specified)    │      │
│  │  - freeOnly = true (if specified)                       │      │
│  │  - officialOnly = true (if specified)                   │      │
│  └────────────────────────────────────────────────────────┘      │
│  Runs BEFORE scoring — fast predicate checks eliminate docs      │
│  Result: filtered document subset                                │
└──────────────────────────────────┬───────────────────────────────┘
                                   ▼
┌──────────────────────────────────────────────────────────────────┐
│  PHASE 3: SCORE (mode-dependent)                                  │
│                                                                  │
│  SPECIFIC mode:                                                  │
│    TextMatchScorer.titleOnly(LearningResource::title)            │
│    100 → exact title match                                       │
│     40 → partial title match                                     │
│      8 → fuzzy match (prefix)                                    │
│                                                                  │
│  VAGUE mode:                                                     │
│    CompositeScorer (weighted):                                   │
│      40% TextMatchScorer (title + description + tags)            │
│      25% Bm25Scorer (full-text BM25 relevance)                  │
│      20% TagScorer (tag overlap count)                           │
│      15% ConceptMatchScorer (concept area affinity)              │
│    + Boosts:                                                     │
│      +15 official source                                         │
│      +10 actively maintained                                     │
│      +12 language fit (java-specific)                            │
│      +10 difficulty fit (matches requested level)                │
│                                                                  │
│  EXPLORATORY mode:                                               │
│    ScoringStrategy.constant(50)                                  │
│    + difficulty-fit boost (beginner-friendly weighted higher)    │
│    + official-source boost                                       │
│                                                                  │
│  Result: List<ScoredItem<T>> with score > 0                     │
└──────────────────────────────────┬───────────────────────────────┘
                                   ▼
┌──────────────────────────────────────────────────────────────────┐
│  PHASE 4: RANK                                                    │
│  ScoreRanker (score-descending, stable sort)                     │
│    .thenRank(RecencyBoostRanker) — time-decay for freshness     │
│                                                                  │
│  FUTURE: .thenRank(DiversityRanker) — variety across types      │
│  FUTURE: .thenRank(PersonalRanker) — user history-aware         │
│                                                                  │
│  Result: sorted List<ScoredItem<T>>                              │
└──────────────────────────────────┬───────────────────────────────┘
                                   ▼
┌──────────────────────────────────────────────────────────────────┐
│  PHASE 5: TRIM & PRESENT                                          │
│  Take first maxResults (default 15)                              │
│  Build human-readable summary string                             │
│  Generate "did you mean?" suggestions                            │
│                                                                  │
│  Result: SearchResult<T> {items, mode, summary, suggestions}     │
└──────────────────────────────────────────────────────────────────┘
```

### Tier Model (Subclassing for Domain Engines)

```text
Tier 0 — SearchEngine<T>                     interface (generic)
    │
    ▼
Tier 1 — ConfigurableSearchEngine<T>         5-phase pipeline (generic)
    │
    ▼
Tier 2 — LearningSearchEngine               domain: configures pipeline for LearningResource
    │                                        adds: concept/category-aware scoring, keyword resolution
    ▼
Tier 3 — OfficialDocsSearchEngine            variant: pre-filters to official-only resources
```

**To create a new domain engine:** Subclass `ConfigurableSearchEngine`, wire domain-specific
scorers/filters in constructor, override `preSearch()`/`postSearch()` hooks if needed.

### Scoring Weight Reference (RelevanceScorer)

| Weight Constant | Points | Used By | Purpose |
|---|---|---|---|
| `EXACT_TITLE_MATCH` | 100 | TextMatchScorer | Perfect title match |
| `PARTIAL_TITLE_MATCH` | 40 | TextMatchScorer | Title contains search term |
| `DESCRIPTION_MATCH` | 20 | TextMatchScorer | Description contains term |
| `TAG_MATCH` | 15 | TagScorer | Individual tag or word match |
| `CONCEPT_MATCH` | 25 | ConceptMatchScorer | Resource covers inferred concept |
| `CATEGORY_MATCH` | 20 | ConceptMatchScorer | Resource in inferred category |
| `DOMAIN_AFFINITY` | 10 | ConceptMatchScorer | Concept in same domain |
| `OFFICIAL_BOOST` | 15 | Boost | Official/authoritative source |
| `FRESHNESS_BOOST` | 10 | Boost | Actively maintained |
| `DIFFICULTY_FIT` | 10 | Boost | Difficulty matches requested level |
| `LANGUAGE_FIT` | 12 | Boost | Language applicability alignment |
| `FUZZY_MATCH` | 8 | FuzzyMatcher | Partial word overlap |

### Search Engine Enhancement Roadmap

#### Phase 2 Additions — Enhanced Search

| Feature | Description | Implementation |
|---|---|---|
| **Faceted search** | Group results by category, difficulty, type | Add `FacetCollector` post-processing in Phase 5 |
| **Autocomplete** | Typeahead suggestions as user types | `TrieIndex` built from all resource titles + tags |
| **Spelling correction** | "conncurrency" → "concurrency" | Levenshtein distance with threshold ≤ 2 edits |
| **Query expansion** | "threads" also searches "concurrency" | `KeywordIndex` synonym resolution before scoring |
| **Result caching** | LRU cache for repeated queries | `ConcurrentHashMap` with TTL; key = normalized query |

#### Phase 3 Additions — Learning-Aware Search

| Feature | Description | Implementation |
|---|---|---|
| **Unseen-first ranking** | Boost resources user hasn't visited | `UserHistoryRanker` reads progress file |
| **Difficulty progression** | After BEGINNER resources, suggest INTERMEDIATE | `DifficultyProgressionRanker` checks user level |
| **Prerequisite-aware filter** | Don't show ADVANCED if BEGINNER not done | `PrerequisiteFilter` checks prerequisite graph |

#### Phase 4 Additions — Web-Augmented Search

| Feature | Description | Implementation |
|---|---|---|
| **Web fallback** | When vault has < 3 results, search web | `WebAugmentedSearchEngine` wraps `ConfigurableSearchEngine` |
| **Multi-source merge** | Vault + web → unified ranking | `MergingRanker` normalizes scores across sources |
| **Deduplication** | URL normalization + content fingerprint | `DeduplicationFilter` in Phase 2 |

#### Phase 5 Additions — Personalization

| Feature | Description | Implementation |
|---|---|---|
| **Content-based similarity** | "Resources like this one" | Cosine similarity on tag vectors |
| **Trending detection** | Which topics are popular now | Time-windowed query frequency analysis |
| **Personalized re-ranking** | Based on learning history | Collaborative filtering (users who learned X also learned Y) |

---

## 7 — Web Scraper & Processor — Complete Specification

### Pipeline Architecture

```text
┌─────────────────────────────────────────────────────────────────┐
│  STAGE 1 — SOURCE REGISTRY                                       │
│  Which sites to fetch from, organized by source type             │
│                                                                  │
│  ┌────────────────┐ ┌────────────────┐ ┌────────────────┐       │
│  │ Official Docs  │ │ Blog/RSS       │ │ GitHub         │       │
│  │ dev.java       │ │ Baeldung       │ │ Trending repos │       │
│  │ docs.oracle    │ │ InfoQ RSS      │ │ Awesome lists  │       │
│  │ MDN            │ │ DZone          │ │ README scrape  │       │
│  │ python.org     │ │ Real Python    │ │                │       │
│  └────────────────┘ └────────────────┘ └────────────────┘       │
│  Interface: WebSource { List<FetchTarget> targets(query, max) }  │
└───────────────────────────────┬─────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│  STAGE 2 — FETCHER                                                │
│  HTTP client with safeguards                                     │
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ HttpWebFetcher (java.net.http.HttpClient)                │    │
│  │ ├── RateLimiter: per-domain, configurable (default 2/s)  │    │
│  │ ├── RobotsChecker: parse robots.txt, respect Disallow   │    │
│  │ ├── RetryPolicy: exponential backoff (3 attempts max)    │    │
│  │ ├── ResponseCache: ETag/Last-Modified → 304 support     │    │
│  │ ├── Timeout: connect=5s, read=10s, total=30s            │    │
│  │ └── UserAgent: "LearningAssistant/1.0 (educational)"   │    │
│  └─────────────────────────────────────────────────────────┘    │
│  Output: FetchResult { url, statusCode, headers, body, timing }  │
└───────────────────────────────┬─────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│  STAGE 3 — EXTRACTOR                                              │
│  HTML → structured content                                       │
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ HtmlContentExtractor                                     │    │
│  │ ├── Title: <title> tag, <h1>, <meta og:title>           │    │
│  │ ├── Main content: largest text block, <article>, <main> │    │
│  │ ├── Author: <meta author>, byline patterns              │    │
│  │ ├── Date: <meta date>, <time>, URL date patterns        │    │
│  │ ├── Code blocks: <pre><code> extraction                 │    │
│  │ ├── Remove: nav, sidebar, ads, footer, scripts, styles  │    │
│  │ └── TOC: heading hierarchy → outline                    │    │
│  └─────────────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ MarkdownExtractor (GitHub READMEs, .md pages)            │    │
│  │ RssExtractor (RSS/Atom feeds → article list)             │    │
│  └─────────────────────────────────────────────────────────┘    │
│  Output: ExtractedContent { title, body, author, date, code }    │
└───────────────────────────────┬─────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│  STAGE 4 — CLASSIFIER                                             │
│  Auto-detect metadata for extracted content                      │
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ TopicClassifier (KeywordIndex + TF-IDF)                  │    │
│  │ → Infers ConceptArea and ResourceCategory                │    │
│  │ → Uses keyword frequency in extracted body               │    │
│  │                                                          │    │
│  │ DifficultyClassifier (ReadabilityScorer)                 │    │
│  │ → Flesch-Kincaid readability score                       │    │
│  │ → Code-to-prose ratio (more code = more advanced)        │    │
│  │ → Vocabulary complexity (domain terms density)           │    │
│  │ → Maps to DifficultyLevel                                │    │
│  │                                                          │    │
│  │ TypeDetector (URL patterns + content signals)            │    │
│  │ → GitHub URL → REPOSITORY                                │    │
│  │ → youtube.com → VIDEO or PLAYLIST                        │    │
│  │ → /docs/ or /reference/ in URL → DOCUMENTATION          │    │
│  │ → step-by-step structure → TUTORIAL                      │    │
│  │ → Opinion/commentary → BLOG                              │    │
│  │                                                          │    │
│  │ QualityScorer                                            │    │
│  │ → Freshness signal (last-modified date)                  │    │
│  │ → Author authority (known authors mapped)                │    │
│  │ → Code-to-prose ratio (good balance = higher quality)    │    │
│  │ → Content length (too short = low quality)               │    │
│  └─────────────────────────────────────────────────────────┘    │
│  Output: ClassifiedContent { conceptAreas, categories, type,     │
│           difficulty, freshness, quality, langApplicability }    │
└───────────────────────────────┬─────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│  STAGE 5 — DEDUPLICATOR                                          │
│  Prevent same content from being added twice                     │
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ UrlDeduplicator                                          │    │
│  │ ├── Normalize: strip query params, resolve redirects    │    │
│  │ ├── Canonical: https > http, www removed, trailing /    │    │
│  │ └── Lookup: check against vault URL set                 │    │
│  │                                                          │    │
│  │ ContentFingerprinter (SimHash)                           │    │
│  │ ├── Shingle text into 3-grams                           │    │
│  │ ├── Hash each shingle → simulate fingerprint            │    │
│  │ ├── Compare fingerprints: hamming distance < threshold  │    │
│  │ └── Detects near-duplicate content (rewrites, mirrors)  │    │
│  └─────────────────────────────────────────────────────────┘    │
│  Output: DeduplicationResult { isNew, existingMatch?, url }      │
└───────────────────────────────┬─────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│  STAGE 6 — INGESTER                                               │
│  Create LearningResource and add to vault                        │
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ VaultIngester                                            │    │
│  │ ├── Build LearningResource from classified content      │    │
│  │ ├── Generate ID (slug from title + source)              │    │
│  │ ├── Add to in-memory vault (ResourceVault)              │    │
│  │ ├── Optionally persist to skill file (markdown row)     │    │
│  │ └── Log addition for audit trail                        │    │
│  └─────────────────────────────────────────────────────────┘    │
│  Output: LearningResource (added to vault + optionally skill)    │
└─────────────────────────────────────────────────────────────────┘
```

### Proposed Package Structure

```text
web-processor/src/web/
├── fetcher/
│   ├── WebFetcher.java              ← Interface: fetch(url) → FetchResult
│   ├── HttpWebFetcher.java          ← java.net.http.HttpClient implementation
│   ├── RateLimiter.java             ← Token-bucket per-domain rate limiting
│   ├── RobotsChecker.java           ← Robots.txt parsing and checking
│   ├── RetryPolicy.java             ← Configurable retry with backoff
│   └── ResponseCache.java           ← ETag-based response caching
├── extractor/
│   ├── ContentExtractor.java        ← Interface: extract(html) → ExtractedContent
│   ├── HtmlContentExtractor.java    ← HTML → clean text (main + code blocks)
│   ├── MarkdownExtractor.java       ← GitHub READMEs → structured content
│   └── RssExtractor.java            ← RSS/Atom feed → article list
├── classifier/
│   ├── ResourceClassifier.java      ← Interface: classify(content) → ClassifiedContent
│   ├── TopicClassifier.java         ← Keyword + TF-IDF → ConceptArea + Category
│   ├── DifficultyClassifier.java    ← Readability + vocabulary → DifficultyLevel
│   ├── TypeDetector.java            ← URL patterns + structure → ResourceType
│   └── QualityScorer.java           ← Multi-signal quality estimation
├── dedup/
│   ├── DeduplicationEngine.java     ← Interface: isDuplicate(content, vault)
│   ├── UrlDeduplicator.java         ← URL normalization + vault lookup
│   └── ContentFingerprinter.java    ← SimHash for near-duplicate detection
├── summarizer/
│   ├── ContentSummarizer.java       ← Interface: summarize(content) → Summary
│   ├── ExtractiveSummarizer.java    ← Key sentence extraction (TextRank-style)
│   └── DescriptionGenerator.java    ← 2-sentence resource description
├── source/
│   ├── WebSource.java               ← Interface: targets(query, max) → List<FetchTarget>
│   ├── DocumentationSource.java     ← Official docs sites (dev.java, MDN, etc.)
│   ├── BlogAggregatorSource.java    ← RSS/Atom feed aggregation
│   ├── GitHubSource.java            ← GitHub API (trending, search, READMEs)
│   └── SourceRegistry.java          ← Manages all registered sources
└── model/
    ├── FetchTarget.java             ← Record: url, source, priority
    ├── FetchResult.java             ← Record: url, status, headers, body, timing
    ├── ExtractedContent.java        ← Record: title, body, author, date, code, toc
    ├── ClassifiedContent.java       ← Record: concepts, categories, type, diff, fresh
    └── DeduplicationResult.java     ← Record: isNew, existingMatch, normalizedUrl
```

---

## 8 — Learning Intelligence Engine — Complete Specification

### Prerequisite Graph (DAG)

```text
                    LANGUAGE_BASICS
                    /      |      \
                 OOP    GENERICS   FUNCTIONAL_PROGRAMMING
                /   \      |
    DESIGN_PATTERNS  LANGUAGE_FEATURES
                \      /
             CLEAN_CODE
                 |
            ARCHITECTURE
                 |
           SYSTEM_DESIGN
                / | \
    DATABASES  DISTRIBUTED_SYSTEMS  NETWORKING
                    |
            OBSERVABILITY

                DATA_STRUCTURES
                    |
                ALGORITHMS
               /    |    \
    COMPLEXITY  MATHEMATICS  MEMORY_MANAGEMENT
    _ANALYSIS

            CONCURRENCY
            /    |    \
      TESTING  CI_CD  CONTAINERS
                |       |
          BUILD_TOOLS  INFRASTRUCTURE
                \      /
             VERSION_CONTROL
```

### Learning Path Engine

```java
// Conceptual API
interface LearningPathEngine {
    LearningPath generatePath(
        ConceptArea target,          // What user wants to learn
        DifficultyLevel current,     // User's current level
        DifficultyLevel goal,        // Desired level
        Set<ConceptArea> completed   // Already mastered concepts
    );
}

record LearningPath(
    List<PathStep> steps,
    int estimatedHours,
    List<ConceptArea> prerequisites,    // Must learn first
    List<ConceptArea> unlocked          // Will be able to learn after
)

record PathStep(
    int order,
    ConceptArea concept,
    DifficultyLevel level,
    List<LearningResource> resources,   // Recommended resources for this step
    String description,                 // What you'll learn
    int estimatedMinutes
)
```

### Knowledge Assessment Engine

```java
interface AssessmentEngine {
    Assessment generateAssessment(
        ConceptArea topic,
        DifficultyLevel level,
        int questionCount             // 5-20 questions
    );

    AssessmentResult evaluate(
        Assessment assessment,
        List<String> userAnswers
    );
}

record Assessment(
    String id,
    ConceptArea topic,
    DifficultyLevel level,
    List<Question> questions
)

record Question(
    String id,
    String text,
    QuestionType type,                // MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER
    List<String> options,             // For MULTIPLE_CHOICE
    String correctAnswer,
    String explanation,               // Why the answer is correct
    ConceptArea concept               // Specific sub-concept tested
)

record AssessmentResult(
    int score,                        // 0-100
    DifficultyLevel estimatedLevel,   // Based on performance
    List<ConceptArea> strengths,      // Topics answered correctly
    List<ConceptArea> gaps,           // Topics answered incorrectly
    List<LearningResource> recommended // Resources to fill gaps
)
```

### Progress Tracker

```java
interface ProgressTracker {
    void markCompleted(String resourceId, Instant completedAt);
    void markInProgress(String resourceId);
    void markConceptMastered(ConceptArea concept, DifficultyLevel level);
    UserProgress getProgress();
    Set<ConceptArea> getCompletedConcepts();
    DifficultyLevel getEstimatedLevel(ConceptArea concept);
}

record UserProgress(
    Map<String, ResourceStatus> resources,      // resourceId → status
    Map<ConceptArea, DifficultyLevel> levels,  // concept → estimated level
    List<CompletedAssessment> assessments,      // assessment history
    Instant lastActive
)

// Persisted as JSON in brain/ai-brain/ or a local file
```

### Recommendation Engine (Phase 5)

```java
interface RecommendationEngine {
    List<ScoredItem<LearningResource>> recommend(
        UserProgress progress,         // What user has done
        int maxResults                 // How many to suggest
    );

    List<ScoredItem<LearningResource>> similarTo(
        String resourceId,            // "Resources like this"
        int maxResults
    );
}
```

**Recommendation strategies:**

| Strategy | Algorithm | When Used |
|---|---|---|
| Content-based | Cosine similarity on tag/concept vectors | "Resources like this" |
| Gap-filling | Match resources to weak concept areas | After assessment |
| Difficulty progression | Next-level resources in mastered areas | Ongoing learning |
| Freshness-first | Recently published in interested topics | Keeping current |
| Trending | Time-windowed query frequency | Discovery mode |

### Proposed Package Structure

```text
learning-engine/src/learning/
├── paths/
│   ├── LearningPathEngine.java      ← Path generation from prerequisite graph
│   ├── PrerequisiteGraph.java        ← DAG of concept dependencies
│   ├── PathOptimizer.java            ← Shortest path, skip known concepts
│   └── PathStep.java                 ← Record: step in learning path
├── assessment/
│   ├── AssessmentEngine.java         ← Quiz/assessment generation
│   ├── QuestionBank.java             ← Domain-organized question repository
│   ├── QuestionGenerator.java        ← Concept-to-question mapping
│   ├── KnowledgeEstimator.java       ← Score → estimated level
│   └── GapAnalyzer.java              ← Identify weak areas from results
├── progress/
│   ├── ProgressTracker.java          ← Interface for tracking completion
│   ├── FileProgressTracker.java      ← JSON-file-based persistence
│   └── UserProgress.java             ← Aggregate progress record
├── recommendation/
│   ├── RecommendationEngine.java     ← Interface
│   ├── ContentBasedRecommender.java  ← Tag/concept vector similarity
│   ├── GapFillingRecommender.java    ← Targets weak concept areas
│   └── CompositeRecommender.java     ← Weighted combination
└── model/
    ├── LearningPath.java             ← Record: ordered steps + metadata
    ├── Assessment.java               ← Record: questions + topic + level
    ├── Question.java                 ← Record: text + options + answer
    └── AssessmentResult.java         ← Record: score + gaps + recommendations
```

---

## 9 — Keyword/Tagging System — Algorithm Design

### Current Approach: Enum Core + String Tags (Hybrid)

```text
LearningResource
├── ENUM-BASED (type-safe, stable):
│   ├── conceptAreas: List<ConceptArea>         ← 40 fine-grained concepts
│   ├── categories: List<ResourceCategory>      ← 17 topic domains
│   ├── type: ResourceType                      ← 13 content formats
│   ├── difficulty: DifficultyLevel             ← 4 ordered levels
│   ├── freshness: ContentFreshness             ← 5 maintenance states
│   └── languageApplicability: LanguageApplicability ← 6 language scopes
│
└── STRING-BASED (flexible, evolving):
    └── tags: List<String>                      ← Free-form keywords
```

### KeywordIndex — Synonym Resolution Table

The `KeywordIndex` maps diverse user vocabulary to canonical enum values:

```java
// Example entries (300+ total in taxonomy-reference.md)
"concurrency"      → ConceptArea.CONCURRENCY
"threads"          → ConceptArea.CONCURRENCY
"async"            → ConceptArea.CONCURRENCY
"virtual threads"  → ConceptArea.CONCURRENCY
"multithreading"   → ConceptArea.CONCURRENCY

"design patterns"  → ConceptArea.DESIGN_PATTERNS
"singleton"        → ConceptArea.DESIGN_PATTERNS
"factory"          → ConceptArea.DESIGN_PATTERNS
"observer"         → ConceptArea.DESIGN_PATTERNS

"java"             → ResourceCategory.JAVA
"spring"           → ResourceCategory.JAVA
"jdk"              → ResourceCategory.JAVA
"maven"            → ResourceCategory.TOOLS
```

### Enhancement Roadmap for Keywords

#### Phase 2: Weighted Keywords

```java
record KeywordMapping(
    String keyword,
    ConceptArea concept,         // or ResourceCategory
    double weight               // 0.0-1.0 (1.0 = exact, 0.3 = loosely related)
) {}

// "concurrency" → CONCURRENCY at weight 1.0
// "async"       → CONCURRENCY at weight 0.8
// "promise"     → CONCURRENCY at weight 0.5 (also maps to WEB)
```

#### Phase 2: Multi-Mapping

```java
// A single keyword can map to multiple concepts with different weights
"async" → [
    (CONCURRENCY, 0.8),
    (NETWORKING, 0.3),
    (FUNCTIONAL_PROGRAMMING, 0.2)
]
```

#### Phase 3: Concept Graph (if taxonomy exceeds 300 values)

```java
class ConceptNode {
    String id;                       // "concurrency"
    String displayName;              // "Concurrency"
    Set<String> aliases;             // {"threads", "multithreading", "async"}
    Set<ConceptNode> parents;        // {coreCsFundamentals}
    Set<ConceptNode> children;       // {threads, locks, atomics, virtualThreads}
    Set<ConceptNode> related;        // {parallelism, distributedSystems}
    Map<String, Object> properties;  // {difficulty: "advanced", domain: "cs"}
}
```

### When to Switch from Enum to Graph

| Signal | Current (Enum) | Switch to Graph When |
|---|---|---|
| Value count | 97 values | > 300 values |
| Hierarchy depth | 2 levels (Domain → Area) | Need 3+ levels |
| Relationship types | "belongs-to" only | Need "related-to", "prerequisite-of" |
| Dynamic additions | Recompile per new value | Need runtime addition |
| Query complexity | Simple filter/match | Need traversal (shortest path, subgraph) |

**Current recommendation: Keep enums.** 97 values across 8 enums is well within the
manageable range. The free-form `tags` field provides flexibility for edge cases.

---

## 10 — Configuration & Extensibility — Complete Schema

### Configuration Layers

```text
LAYER 1: Java Properties (runtime config, key-value pairs)
  ┌──────────────────────────────────────────────────────────────┐
  │ mcp-config.properties             ← Base defaults (committed)│
  │ mcp-config.local.properties       ← User overrides (ignored) │
  │ Environment variables              ← Deployment overrides     │
  └──────────────────────────────────────────────────────────────┘

LAYER 2: Skill Markdown (content config, edited by humans/LLM)
  ┌──────────────────────────────────────────────────────────────┐
  │ SKILL.md                          ← Activation keywords       │
  │ taxonomy-reference.md             ← Enum definitions           │
  │ resources-*.md                    ← Curated resource data      │
  └──────────────────────────────────────────────────────────────┘

LAYER 3: Agent Markdown (behavior config, defines personas)
  ┌──────────────────────────────────────────────────────────────┐
  │ learning-mentor.agent.md          ← Teaching methodology       │
  │ learning-researcher.agent.md      ← Research strategy          │
  └──────────────────────────────────────────────────────────────┘

LAYER 4: Prompt Markdown (workflow config, defines entry points)
  ┌──────────────────────────────────────────────────────────────┐
  │ resources.prompt.md               ← /resources workflow        │
  │ learn-concept.prompt.md           ← /learn-concept workflow    │
  └──────────────────────────────────────────────────────────────┘
```

### Java Properties Schema

```properties
# ─── Search Engine Configuration ───────────────────────────────
learning.search.maxResults=15
learning.search.defaultMode=VAGUE
learning.search.scorer.textMatch.weight=0.40
learning.search.scorer.bm25.weight=0.25
learning.search.scorer.tags.weight=0.20
learning.search.scorer.concepts.weight=0.15
learning.search.boosts.official=15
learning.search.boosts.freshness=10
learning.search.boosts.difficultyFit=10
learning.search.boosts.languageFit=12
learning.search.fuzzy.minPrefix=3
learning.search.fuzzy.minWordLength=4

# ─── Web Scraper Configuration ─────────────────────────────────
learning.scraper.enabled=true
learning.scraper.rateLimit.requestsPerSecond=2
learning.scraper.respectRobotsTxt=true
learning.scraper.timeout.connect=5000
learning.scraper.timeout.read=10000
learning.scraper.timeout.total=30000
learning.scraper.retryAttempts=3
learning.scraper.userAgent=LearningAssistant/1.0 (educational)
learning.scraper.cache.enabled=true
learning.scraper.cache.maxEntries=500

# ─── Web Sources Configuration ─────────────────────────────────
learning.sources.github.enabled=true
learning.sources.github.maxResults=10
learning.sources.rss.enabled=true
learning.sources.rss.feeds=https://feeds.baeldung.com/baeldung,https://www.infoq.com/feed
learning.sources.docs.enabled=true
learning.sources.docs.sites=dev.java,developer.mozilla.org

# ─── Learning Path Configuration ───────────────────────────────
learning.paths.defaultDifficulty=BEGINNER
learning.paths.maxSteps=20
learning.paths.prerequisiteEnforcement=SOFT

# ─── Assessment Configuration ──────────────────────────────────
learning.assessment.defaultQuestionCount=10
learning.assessment.questionTypes=MULTIPLE_CHOICE,TRUE_FALSE
learning.assessment.passingScore=70

# ─── Progress Tracking Configuration ───────────────────────────
learning.progress.enabled=true
learning.progress.storePath=brain/ai-brain/progress.json
```

### Extension Points (Strategy/Plugin Interfaces)

Every major behavior is behind an interface, enabling extension without modification:

| Interface | Package | Purpose | How to Extend |
|---|---|---|---|
| `SearchEngine<T>` | `search.api.core` | Top-level search contract | Implement or subclass `ConfigurableSearchEngine` |
| `ScoringStrategy<T>` | `search.api.algorithm` | Scoring algorithm | Implement functional interface; compose via `CompositeScorer` |
| `QueryClassifier` | `search.api.classify` | Intent detection | Implement; wire into `SearchEngineConfig` |
| `SearchFilter<T>` | `search.api.filter` | Pre-scoring predicate | Implement; compose via `and()`/`or()`/`negate()` |
| `RankingStrategy<T>` | `search.api.rank` | Post-scoring ordering | Implement; compose via `thenRank()` |
| `SearchIndex<T>` | `search.api.index` | Document storage | Implement for DB/Elasticsearch |
| `Tokenizer` | `search.api.algorithm` | Text → tokens | Implement for language-specific tokenization |
| `ResourceProvider` | `vault` | Curated resource set | New class returning `List<LearningResource>` |
| `WebSource` | `web.source` | Web data source | New class returning `List<FetchTarget>` |
| `ContentExtractor` | `web.extractor` | HTML → structured text | New class for different HTML structures |
| `ResourceClassifier` | `web.classifier` | Auto-detect metadata | New class for different classification logic |
| `DeduplicationEngine` | `web.dedup` | Prevent duplicates | New class for different dedup strategies |
| `LearningPathEngine` | `learning.paths` | Path generation | New class for different path algorithms |
| `AssessmentEngine` | `learning.assessment` | Quiz generation | New class for different question strategies |
| `ProgressTracker` | `learning.progress` | User state tracking | New class for different persistence backends |
| `RecommendationEngine` | `learning.recommendation` | Suggestions | New class for different recommendation algorithms |

### Configuration Wiring Example

```java
// Building a fully-configured learning search engine
SearchEngineConfig<LearningResource> config = SearchEngineConfig.<LearningResource>builder()
    // Phase 1: Classifier
    .classifier(KeywordQueryClassifier.builder()
        .specificKeywords(List.of("docs", "official", "api ref", "reference", "javadoc"))
        .exploratoryKeywords(List.of("learn", "start", "beginner", "recommend", "tutorial"))
        .knownVocabulary(keywordIndex.conceptKeywords(), keywordIndex.categoryKeywords())
        .build())

    // Phase 2: Filter
    .filter(SearchFilter.<LearningResource>allowAll()
        .and((item, ctx) -> item.freshness() != ContentFreshness.ARCHIVED))

    // Phase 3: Scorers (per mode)
    .scorer(SearchMode.SPECIFIC,
        TextMatchScorer.titleOnly(LearningResource::title))
    .scorer(SearchMode.VAGUE,
        CompositeScorer.<LearningResource>builder()
            .add(TextMatchScorer.defaults(LearningResource::title, LearningResource::searchableText), 0.40)
            .add(new Bm25Scorer<>(LearningResource::searchableText, new DefaultTokenizer()), 0.25)
            .add(TagScorer.defaults(LearningResource::tags), 0.20)
            .add(new ConceptMatchScorer(keywordIndex), 0.15)
            .build())
    .scorer(SearchMode.EXPLORATORY,
        ScoringStrategy.<LearningResource>constant(50)
            .withConstantBoost(15))

    // Phase 4: Ranker
    .ranker(ScoreRanker.<LearningResource>instance()
        .thenRank(new RecencyBoostRanker<>(LearningResource::addedAt)))

    // Phase 5: Trim
    .maxResults(15)
    .build();

// Load all resources into the index
BuiltInResources.all().forEach(r -> config.index().add(r.id(), r));

// Create the engine
SearchEngine<LearningResource> engine = new ConfigurableSearchEngine<>(config);
```

---

## 11 — Copilot Integration — Skills, Prompts, Agents, Instructions

### Role of Each Primitive

| Primitive | Role in This System | What It Contains | When Active |
|---|---|---|---|
| **Skills** | Static knowledge base | Curated resources (tables), taxonomy (enums), deep references | Auto-activated by keyword match in user query |
| **MCP Server** | Dynamic capabilities | Java code: search, scrape, fetch, guide, assess, export | Called by agent when dynamic action needed |
| **Agents** | Teaching intelligence | Personas with methodology: mentor, researcher, assessor | Selected by user in dropdown |
| **Prompts** | User entry points | Slash commands with parameters: /resources, /learn-concept | Invoked by user typing / |
| **Instructions** | Quality governance | Rules: formatting, taxonomy validation, completeness checks | Auto-activated by file glob patterns |
| **copilot-instructions.md** | Project foundation | Coding standards, commit format, project structure | Always active |

### Skills Architecture

```text
.github/skills/
├── learning-resources-vault/          ← CURATED RESOURCES (138 entries)
│   ├── SKILL.md                       ← Master index, Quick Index table
│   │   description: "Curated vault of 138 learning resources covering..."
│   │   (activated when user asks about learning resources, specific topics)
│   ├── taxonomy-reference.md          ← All 8 enums (97 values), 300+ keywords
│   ├── migration-mapping.md           ← MCP→Skill traceability
│   └── resources-*.md (10 files)      ← Resources grouped by domain
│
├── java-learning-resources/           ← DEEP JAVA CONTENT
│   ├── SKILL.md                       ← Oracle tutorials, JDK APIs, community guides
│   │   description: "Comprehensive Java & IDE learning resource library..."
│   ├── deep-reference/                ← Scraped offline content
│   │   ├── oracle-tutorials-guide.md  ← Complete Oracle tutorial index
│   │   └── jdk-apis-reference.md      ← Code examples: concurrency, streams, etc.
│   └── resources-*.md                 ← Categorized resource lists
│
├── software-engineering-resources/    ← SE FOUNDATIONS
│   └── SKILL.md                       ← 39 books, DSA, systems, patterns
│       description: "Comprehensive curated index of software engineering..."
│
├── career-resources/                  ← CAREER GROWTH
│   └── SKILL.md                       ← Roles, skills matrices, compensation
│       description: "Curated career resources for tech job roles..."
│
└── daily-assistant-resources/         ← DAILY PRODUCTIVITY
    └── SKILL.md                       ← Finance, productivity, news
        description: "Resources for daily productivity, finance basics..."
```

### Agent Configuration

```yaml
# learning-mentor.agent.md (key fields)
name: learning-mentor
description: "Patient senior developer who teaches by building understanding"
tools: [search, codebase, usages, fetch, findTestFiles, terminalLastCommand]

# Key teaching components:
# - Building-block tree (hierarchical concept map)
# - Analogy library (50+ domain analogies)
# - Teaching flow: What→Why→Analogy→Code→Anti-pattern→Next steps
# - Multi-level explanations: 5yo→Beginner→Intermediate→Senior
```

### Prompt Configuration

```yaml
# resources.prompt.md (key fields)
name: resources
description: "Search, browse, scrape, discover, recommend learning resources"
agent: learning-mentor           # Auto-selects learning-mentor agent
tools: [search, codebase, fetch] # Available tools

# Input variables:
# ${input:action:Choose action (search/browse/...)}
# ${input:topic:What topic?}
# ${input:level:Skill level (beginner/intermediate/advanced)}
```

### MCP Tool Surface (Current + Planned)

| Tool | Status | Handler | Purpose |
|---|---|---|---|
| `search_resources` | Existing | SearchHandler | Full-text multi-criteria vault search |
| `browse_vault` | Existing | SearchHandler | Browse by category or type |
| `get_resource` | Existing | SearchHandler | Resource details by ID |
| `list_categories` | Existing | SearchHandler | Category summary with counts |
| `discover_resources` | Existing | ResourceDiscovery | Intent-classified smart discovery |
| `scrape_url` | Existing | ScrapeHandler | Fetch + extract + summarize URL |
| `read_url` | Existing | ScrapeHandler | Full content from URL |
| `add_resource` | Existing | UrlResourceHandler | Add custom resource with metadata |
| `add_resource_from_url` | Existing | UrlResourceHandler | Infer metadata from URL + add |
| `export_results` | Existing | ExportHandler | Export as Markdown/PDF/Word |
| `fetch_web_resources` | **Planned** | FetchHandler | Dynamic web search → vault |
| `suggest_learning_path` | **Planned** | GuidanceHandler | Adaptive path recommendation |
| `assess_knowledge` | **Planned** | AssessmentHandler | Topic quiz/assessment |
| `track_progress` | **Planned** | ProgressHandler | Mark resources as completed |
| `recommend` | **Planned** | RecommendHandler | Personalized recommendations |

---

## 12 — Package Structure — Current and Target

### Current File Tree (Comprehensive)

```text
mcp-servers/src/
├── Main.java
├── config/
│   ├── ConfigManager.java
│   ├── package-info.java
│   ├── exception/ (2 files)
│   ├── loader/ (4 files)
│   ├── model/ (8 files)
│   └── validation/ (3 files)
└── server/
    ├── McpServer.java                  ← Interface
    ├── McpServerRegistry.java          ← Registry
    ├── package-info.java
    ├── atlassian/ (active)
    │   ├── AtlassianServerFactory.java
    │   ├── AtlassianServerVersion.java
    │   ├── common/ (2 files)
    │   ├── v1/ (~15 files)
    │   └── v2/ (~15 files)
    └── learningresources/ (deprecated but retained)
        ├── LearningResourcesServer.java
        ├── package-info.java
        ├── README.md
        ├── model/
        │   ├── ConceptArea.java          (40 values)
        │   ├── ConceptDomain.java        (9 values)
        │   ├── ContentFreshness.java     (5 values)
        │   ├── ContentSummary.java       (record)
        │   ├── DifficultyLevel.java      (4 values)
        │   ├── LanguageApplicability.java (6 values)
        │   ├── LearningResource.java     (record, 15 fields)
        │   ├── ResourceCategory.java     (17 values)
        │   ├── ResourceQuery.java        (record, 11 fields)
        │   ├── ResourceType.java         (13 values)
        │   ├── SearchMode.java           (3 values — DUPLICATE of search.api)
        │   └── package-info.java
        ├── vault/
        │   ├── BuiltInResources.java     (composes 17 providers)
        │   ├── DiscoveryResult.java      (record)
        │   ├── KeywordIndex.java         (300+ mappings)
        │   ├── LearningSearchEngine.java (Tier-2 domain engine)
        │   ├── ResourceDiscovery.java    (3-mode discovery)
        │   ├── ResourceProvider.java     (interface)
        │   ├── ResourceVault.java        (in-memory store)
        │   ├── RelevanceScorer.java      (12 weights)
        │   ├── package-info.java
        │   └── providers/ (17 provider classes)
        ├── handler/
        │   ├── ToolHandler.java          (MCP dispatcher)
        │   ├── SearchHandler.java
        │   ├── ScrapeHandler.java
        │   ├── UrlResourceHandler.java
        │   ├── ExportHandler.java
        │   └── package-info.java
        ├── content/
        │   ├── ContentReader.java
        │   ├── ContentSummarizer.java
        │   ├── ReadabilityScorer.java
        │   └── package-info.java
        ├── scraper/
        │   ├── WebScraper.java
        │   ├── ContentExtractor.java
        │   ├── ScraperException.java
        │   ├── ScraperResult.java
        │   └── package-info.java
        └── search/
            ├── OfficialDocsSearchEngine.java
            └── package-info.java

search-engine/src/search/
├── api/
│   ├── core/ (5 files: SearchEngine, SearchContext, SearchResult, ScoredItem, ScoreBreakdown)
│   ├── classify/ (2 files: SearchMode, QueryClassifier)
│   ├── algorithm/ (2 files: ScoringStrategy, Tokenizer)
│   ├── filter/ (1 file: SearchFilter)
│   ├── rank/ (1 file: RankingStrategy)
│   └── index/ (1 file: SearchIndex)
└── engine/
    ├── core/ (1 file: ConfigurableSearchEngine)
    ├── classify/ (1 file: KeywordQueryClassifier)
    ├── algorithm/ (6 files: TextMatchScorer, Bm25Scorer, TagScorer, CompositeScorer, FuzzyMatcher, DefaultTokenizer)
    ├── filter/ (1 file: FilterChain)
    ├── rank/ (2 files: ScoreRanker, RecencyBoostRanker)
    ├── index/ (2 files: InMemoryIndex, KeywordRegistry)
    └── config/ (1 file: SearchEngineConfig)
```

### Target File Tree (After All Phases)

```text
learning-assistant/
├── learning-model/                     ← NEW MODULE (Phase 5, if needed)
│   └── src/learningresources/model/
│       ├── ConceptArea.java
│       ├── ConceptDomain.java
│       ├── ResourceCategory.java
│       ├── ResourceType.java
│       ├── DifficultyLevel.java
│       ├── ContentFreshness.java
│       ├── LanguageApplicability.java
│       ├── LearningResource.java
│       ├── LearningPath.java           ← NEW
│       ├── UserProgress.java           ← NEW
│       ├── AssessmentQuestion.java     ← NEW
│       └── ResourceQuery.java
├── search-engine/                      ← EXISTING (enhanced)
│   └── src/search/
│       ├── api/ (unchanged)
│       └── engine/
│           ├── (existing 15 files)
│           ├── algorithm/
│           │   └── SpellCorrector.java  ← NEW Phase 2
│           ├── index/
│           │   └── TrieIndex.java       ← NEW Phase 2 (autocomplete)
│           └── rank/
│               ├── DiversityRanker.java ← NEW Phase 2
│               └── PersonalRanker.java  ← NEW Phase 5
├── web-processor/                      ← NEW MODULE (Phase 4)
│   └── src/web/
│       ├── fetcher/ (6 files)
│       ├── extractor/ (4 files)
│       ├── classifier/ (5 files)
│       ├── dedup/ (3 files)
│       ├── summarizer/ (3 files)
│       ├── source/ (5 files)
│       └── model/ (5 files)
├── learning-engine/                    ← NEW MODULE (Phase 3)
│   └── src/learning/
│       ├── paths/ (4 files)
│       ├── assessment/ (5 files)
│       ├── progress/ (3 files)
│       ├── recommendation/ (4 files)
│       └── model/ (4 files)
├── mcp-servers/                        ← EXISTING (enhanced)
│   └── src/server/learningresources/
│       ├── (existing code — un-deprecated)
│       └── handler/
│           ├── (existing handlers)
│           ├── FetchHandler.java        ← NEW Phase 2
│           ├── GuidanceHandler.java     ← NEW Phase 3
│           ├── AssessmentHandler.java   ← NEW Phase 3
│           ├── ProgressHandler.java     ← NEW Phase 3
│           └── RecommendHandler.java    ← NEW Phase 5
└── .github/
    ├── skills/ (unchanged — static knowledge layer)
    ├── prompts/ (enhanced — new slash commands)
    ├── agents/ (enhanced — new personas)
    └── instructions/ (unchanged — governance layer)
```

---

## 13 — Phased Implementation Roadmap

### Phase 1 — Current State (COMPLETE)

| Deliverable | Status | Files |
|---|---|---|
| 138 curated resources in skill files | ✅ Done | 10 resources-*.md files |
| Taxonomy documented in markdown | ✅ Done | taxonomy-reference.md |
| Search engine (generic, 26 files) | ✅ Done | search-engine/src/ |
| MCP server (10 tools, deprecated) | ✅ Done | mcp-servers/src/server/learningresources/ |
| Learning-mentor agent | ✅ Done | learning-mentor.agent.md |
| 14 slash commands | ✅ Done | .github/prompts/*.prompt.md |
| Session capture system | ✅ Done | brain/ai-brain/sessions/ |

### Phase 2 — Revive MCP + Enhanced Search (NEXT)

**Goal:** Un-deprecate the MCP server for dynamic features. Enhance search. Add web fetching.

| Task | Effort | Priority | Details |
|---|---|---|---|
| Fix SearchMode duplication | Small | P0 | Delete `model.SearchMode`, use `search.api.classify.SearchMode` |
| Un-deprecate MCP server | Small | P0 | Remove deprecation notice from README; update docs |
| Add `FetchHandler` | Medium | P1 | Dynamic web fetch → vault ingestion via MCP tool |
| Add faceted search | Medium | P1 | Group results by category, difficulty, type |
| Add query expansion | Medium | P1 | KeywordIndex synonym resolution before scoring |
| Add spelling correction | Medium | P2 | Levenshtein distance with threshold ≤ 2 |
| Add result caching | Small | P2 | LRU cache for repeated queries |
| Update /resources prompt | Small | P1 | Reference both skills (curated) and MCP (dynamic) |
| Add /resources-web prompt | Small | P1 | New slash command for dynamic web search |

### Phase 3 — Learning Intelligence Engine

**Goal:** Adaptive learning features: paths, assessment, progress tracking.

| Task | Effort | Priority | Details |
|---|---|---|---|
| Build prerequisite graph | Medium | P0 | DAG of 40 ConceptArea dependencies |
| Implement LearningPathEngine | Large | P0 | Generate ordered paths with resource mapping |
| Implement ProgressTracker | Medium | P1 | JSON-file-based persistence in brain/ |
| Implement AssessmentEngine | Large | P1 | Question generation + evaluation |
| Add GuidanceHandler MCP tool | Medium | P1 | expose suggest_learning_path via MCP |
| Add AssessmentHandler MCP tool | Medium | P1 | expose assess_knowledge via MCP |
| Add /learning-path prompt | Small | P2 | Slash command for path generation |
| Add /assess prompt | Small | P2 | Slash command for assessment |

### Phase 4 — Web Processing Pipeline

**Goal:** Full web scraper → extractor → classifier → dedup → ingest pipeline.

| Task | Effort | Priority | Details |
|---|---|---|---|
| Create web-processor module | Medium | P0 | Build script, module structure, README |
| Implement HttpWebFetcher | Medium | P0 | java.net.http with rate-limit, retry, robots.txt |
| Implement HtmlContentExtractor | Large | P1 | DOM parsing, main content detection, code extraction |
| Implement TopicClassifier | Medium | P1 | Keyword + TF-IDF topic detection |
| Implement DifficultyClassifier | Medium | P2 | Readability scoring + vocabulary analysis |
| Implement UrlDeduplicator | Medium | P1 | URL normalization + vault cross-check |
| Implement ContentFingerprinter | Large | P2 | SimHash for near-duplicate detection |
| Implement SourceRegistry | Medium | P1 | Pluggable web sources (docs, blogs, GitHub) |
| Integrate with MCP FetchHandler | Medium | P1 | Wire pipeline into MCP tool |

### Phase 5 — Full Learning Library (Vision)

**Goal:** Personalization, recommendations, auto-discovery.

| Task | Effort | Priority | Details |
|---|---|---|---|
| Extract learning-model module | Medium | P1 | If 2+ modules need shared types |
| Implement ContentBasedRecommender | Large | P1 | Cosine similarity on tag/concept vectors |
| Implement GapFillingRecommender | Medium | P1 | Target weak areas after assessment |
| Implement TrendingDetector | Medium | P2 | Time-windowed query frequency analysis |
| Implement PersonalRanker | Large | P2 | Learning-history-aware re-ranking |
| Auto-discover from RSS feeds | Large | P2 | Scheduled fetch from configured feeds |
| Spaced repetition integration | Large | P3 | Review scheduling for learned concepts |

---

## 14 — Decision Log

| # | Decision | Alternatives Considered | Rationale | Date |
|---|---|---|---|---|
| D-001 | Use layered hybrid (skills + MCP + search) | Pure skills, pure MCP, pure agents | Best power-to-complexity ratio (scored 73/85) | 2026-03-24 |
| D-002 | Skills are canonical for curated resources | MCP-only, code-gen from Java | Zero runtime deps, works in all Copilot modes, LLM reads natively | 2026-03-22 |
| D-003 | Keep Java enums for taxonomy | Free-form strings only, graph DB | Type-safe, 97 values is manageable, tags provide flexibility | 2026-03-24 |
| D-004 | Keep Java as backend language | Python, TypeScript | 80+ existing files, daily expertise, search engine already built | 2026-03-24 |
| D-005 | Fix SearchMode dupe as Phase 2 P0 | Full model extraction, leave as-is | Only concrete problem; model extraction is YAGNI until needed | 2026-03-24 |
| D-006 | Do NOT retire Java code | Delete all 57 files | Code is working reference architecture; may revive for dynamic features | 2026-03-24 |
| D-007 | Do NOT extract to separate module yet | learning-model/ module | No cross-module consumer exists today; extract when one appears | 2026-03-24 |
| D-008 | Build web-processor as new module | Add to mcp-servers, add to search-engine | Clear separation of concerns; independent build/test cycle | 2026-03-24 |
| D-009 | Build learning-engine as new module | Monolith in mcp-servers | Different lifecycle from MCP transport; reusable beyond MCP | 2026-03-24 |

---

## 15 — Extension Patterns — How to Add Anything

### Add a New Curated Resource

```text
1. Edit: .github/skills/learning-resources-vault/resources-<domain>.md
   → Add a new row to the table with all 9 columns + badges

2. Update: .github/skills/learning-resources-vault/SKILL.md
   → Increment resource count in Quick Index

3. If new concept: Update taxonomy-reference.md
   → Add concept area mapping and keyword entries

4. If using Java vault too: Add to appropriate provider class
   → new LearningResource(...) in the provider's resources() method
   → Register provider in BuiltInResources if new provider class
```

### Add a New Concept Area (Enum Value)

```text
1. Java: Add value to ConceptArea.java with displayName and domain
2. Java: Add keyword mappings to KeywordIndex.java (≥3 synonyms)
3. Skill: Add to taxonomy-reference.md ConceptArea table
4. Skill: Add keyword entries to taxonomy-reference.md keyword index
5. Ensure ≥1 resource uses the new value
6. Build: .\mcp-servers\build.ps1 must pass
```

### Add a New Resource Category (Enum Value)

```text
1. Java: Add value to ResourceCategory.java with displayName
2. Java: Add keyword mappings to KeywordIndex.java
3. Skill: Add to taxonomy-reference.md ResourceCategory table
4. Skill: Create resources-<category>.md sub-file if domain is new
5. Skill: Update SKILL.md Quick Index with new sub-file
6. Build: .\mcp-servers\build.ps1 must pass
```

### Add a New MCP Tool

```text
1. Create handler class in handler/ (or extend existing)
2. Register tool in ToolHandler dispatcher (switch case)
3. Add tool definition in LearningResourcesServer.toolDefinitions()
4. Update .github/prompts/resources.prompt.md to mention new tool
5. Update mcp-servers/README.md tools table
6. Build: .\mcp-servers\build.ps1 must pass
```

### Add a New Search Scoring Strategy

```text
1. Implement ScoringStrategy<T> interface (one method: score(item, context))
2. Wire into SearchEngineConfig builder:
   .scorer(SearchMode.VAGUE, CompositeScorer.builder()
       .add(existingScorer, 0.30)
       .add(yourNewScorer, 0.20)
       .build())
3. Test independently: yourScorer.score(testItem, testContext)
```

### Add a New Web Source

```text
1. Implement WebSource interface: targets(query, maxResults) → List<FetchTarget>
2. Register in SourceRegistry
3. Configure in mcp-config.properties: learning.sources.<name>.enabled=true
```

### Add a New Slash Command

```text
1. Create .github/prompts/<name>.prompt.md with YAML frontmatter
2. Define input variables, agent assignment, tool restrictions
3. Update .github/docs/slash-commands.md (Quick Lookup + details)
4. Update .github/prompts/hub.prompt.md category tree
```

### Add a New Agent

```text
1. Create .github/agents/<name>.agent.md with persona + tools
2. Update .github/docs/slash-commands.md if agent has associated commands
3. Create prompts that auto-select the agent
```

---

## 16 — Appendix — Every Enum Value

### ConceptArea (40 values)

| # | Value | Slug | Domain | Description |
|---|---|---|---|---|
| 1 | `LANGUAGE_BASICS` | language-basics | PROGRAMMING_FUNDAMENTALS | Variables, types, operators, control flow |
| 2 | `OOP` | oop | PROGRAMMING_FUNDAMENTALS | Classes, inheritance, polymorphism, encapsulation |
| 3 | `FUNCTIONAL_PROGRAMMING` | functional-programming | PROGRAMMING_FUNDAMENTALS | Lambdas, streams, immutability, higher-order functions |
| 4 | `LANGUAGE_FEATURES` | language-features | PROGRAMMING_FUNDAMENTALS | Records, sealed classes, pattern matching, switch expressions |
| 5 | `GENERICS` | generics | PROGRAMMING_FUNDAMENTALS | Type parameters, bounds, wildcards, type erasure |
| 6 | `CONCURRENCY` | concurrency | CORE_CS | Threads, locks, atomics, virtual threads, executors |
| 7 | `DATA_STRUCTURES` | data-structures | CORE_CS | Arrays, lists, trees, graphs, hash maps, heaps |
| 8 | `ALGORITHMS` | algorithms | CORE_CS | Sorting, searching, graph algorithms, DP, greedy |
| 9 | `MATHEMATICS` | mathematics | CORE_CS | Linear algebra, probability, discrete math |
| 10 | `COMPLEXITY_ANALYSIS` | complexity-analysis | CORE_CS | Big-O, amortized analysis, space complexity |
| 11 | `MEMORY_MANAGEMENT` | memory-management | CORE_CS | GC, stack vs heap, object lifecycle, memory leaks |
| 12 | `DESIGN_PATTERNS` | design-patterns | SOFTWARE_ENGINEERING | GoF patterns, architectural patterns, idioms |
| 13 | `CLEAN_CODE` | clean-code | SOFTWARE_ENGINEERING | Readability, naming, refactoring, SOLID |
| 14 | `TESTING` | testing | SOFTWARE_ENGINEERING | Unit, integration, E2E, TDD, BDD, mocking |
| 15 | `API_DESIGN` | api-design | SOFTWARE_ENGINEERING | REST, GraphQL, gRPC, versioning, documentation |
| 16 | `ARCHITECTURE` | architecture | SOFTWARE_ENGINEERING | Hexagonal, layered, event-driven, microservices |
| 17 | `SYSTEM_DESIGN` | system-design | SYSTEM_DESIGN | HLD, LLD, scalability, availability, trade-offs |
| 18 | `DATABASES` | databases | SYSTEM_DESIGN | SQL, NoSQL, indexing, normalization, replication |
| 19 | `DISTRIBUTED_SYSTEMS` | distributed-systems | SYSTEM_DESIGN | Consensus, CAP, eventual consistency, partitioning |
| 20 | `NETWORKING` | networking | SYSTEM_DESIGN | TCP/IP, HTTP, DNS, TLS, WebSocket, load balancing |
| 21 | `OPERATING_SYSTEMS` | operating-systems | SYSTEM_DESIGN | Processes, scheduling, memory, file systems, I/O |
| 22 | `CI_CD` | ci-cd | DEVOPS_TOOLING | Pipelines, GitHub Actions, Jenkins, deployment strategies |
| 23 | `CONTAINERS` | containers | DEVOPS_TOOLING | Docker, Kubernetes, Docker Compose, container security |
| 24 | `VERSION_CONTROL` | version-control | DEVOPS_TOOLING | Git, branching strategies, code review, merge strategies |
| 25 | `BUILD_TOOLS` | build-tools | DEVOPS_TOOLING | Maven, Gradle, npm, Make, Bazel |
| 26 | `INFRASTRUCTURE` | infrastructure | DEVOPS_TOOLING | Terraform, Ansible, cloud provisioning, IaC |
| 27 | `OBSERVABILITY` | observability | DEVOPS_TOOLING | Logging, metrics, tracing, alerting, dashboards |
| 28 | `WEB_SECURITY` | web-security | SECURITY | OWASP, XSS, CSRF, SQL injection, auth |
| 29 | `CRYPTOGRAPHY` | cryptography | SECURITY | Encryption, hashing, TLS, PKI, key management |
| 30 | `MACHINE_LEARNING` | machine-learning | AI_DATA | Supervised/unsupervised, neural nets, training |
| 31 | `DEEP_LEARNING` | deep-learning | AI_DATA | CNNs, RNNs, transformers, transfer learning |
| 32 | `LLM_AND_PROMPTING` | llm-and-prompting | AI_DATA | LLMs, prompt engineering, fine-tuning, RAG |
| 33 | `INTERVIEW_PREP` | interview-prep | CAREER_META | Coding interviews, system design interviews, behavioral |
| 34 | `CAREER_DEVELOPMENT` | career-development | CAREER_META | Career paths, skill matrices, role transitions |
| 35 | `GETTING_STARTED` | getting-started | CAREER_META | Developer onboarding, first projects, learning paths |
| 36 | `KNOWLEDGE_MANAGEMENT` | knowledge-management | CAREER_META | Note-taking, PKM, second brain, Zettelkasten |
| 37 | `SELF_IMPROVEMENT` | self-improvement | PERSONAL_DEVELOPMENT | Habits, mindset, discipline, stoic philosophy |
| 38 | `COMMUNICATION_SKILLS` | communication-skills | PERSONAL_DEVELOPMENT | Writing, presenting, mentoring, feedback |
| 39 | `FINANCIAL_LITERACY` | financial-literacy | PERSONAL_DEVELOPMENT | Budgeting, investing, tax, personal finance |
| 40 | `PRODUCTIVITY_HABITS` | productivity-habits | PERSONAL_DEVELOPMENT | Time management, deep work, focus, energy |

### ConceptDomain (9 values)

| # | Value | Display Name | ConceptArea Count |
|---|---|---|---|
| 1 | `PROGRAMMING_FUNDAMENTALS` | Programming Fundamentals | 5 |
| 2 | `CORE_CS` | Core CS | 6 |
| 3 | `SOFTWARE_ENGINEERING` | Software Engineering | 5 |
| 4 | `SYSTEM_DESIGN` | System Design & Infrastructure | 5 |
| 5 | `DEVOPS_TOOLING` | DevOps & Tooling | 6 |
| 6 | `SECURITY` | Security | 2 |
| 7 | `AI_DATA` | AI & Data | 3 |
| 8 | `CAREER_META` | Career & Meta | 4 |
| 9 | `PERSONAL_DEVELOPMENT` | Personal Development | 4 |

### ResourceCategory (17 values)

| # | Value | Display Name | Skill Sub-File |
|---|---|---|---|
| 1 | `JAVA` | Java | resources-java.md |
| 2 | `PYTHON` | Python | resources-python.md |
| 3 | `JAVASCRIPT` | JavaScript | resources-web-javascript.md |
| 4 | `WEB` | Web | resources-web-javascript.md |
| 5 | `DATABASE` | Database | resources-cloud-infra.md |
| 6 | `DEVOPS` | DevOps | resources-devops-vcs-build.md |
| 7 | `CLOUD` | Cloud | resources-cloud-infra.md |
| 8 | `ALGORITHMS` | Algorithms | resources-algorithms-ds.md |
| 9 | `SOFTWARE_ENGINEERING` | Software Engineering | resources-software-engineering.md |
| 10 | `TESTING` | Testing | resources-software-engineering.md |
| 11 | `SECURITY` | Security | resources-cloud-infra.md |
| 12 | `AI_ML` | AI/ML | resources-ai-ml.md |
| 13 | `TOOLS` | Tools | resources-devops-vcs-build.md |
| 14 | `PRODUCTIVITY` | Productivity | resources-productivity-pkm.md |
| 15 | `SYSTEMS` | Systems | resources-cloud-infra.md |
| 16 | `GENERAL` | General | resources-general-career.md |
| 17 | `PERSONAL_DEVELOPMENT` | Personal Development | resources-general-career.md |

### ResourceType (13 values)

| # | Value | Display Name | Badge |
|---|---|---|---|
| 1 | `DOCUMENTATION` | Documentation | 📖 Docs |
| 2 | `TUTORIAL` | Tutorial | 📝 Tutorial |
| 3 | `BLOG` | Blog | 📰 Blog |
| 4 | `ARTICLE` | Article | 📄 Article |
| 5 | `VIDEO` | Video | 🎥 Video |
| 6 | `PLAYLIST` | Playlist | 🎬 Playlist |
| 7 | `VIDEO_COURSE` | Video Course | 🎓 Course |
| 8 | `BOOK` | Book | 📚 Book |
| 9 | `INTERACTIVE` | Interactive | 🎮 Interactive |
| 10 | `COURSE` | Course | 🎓 Course |
| 11 | `API_REFERENCE` | API Reference | 📋 API Ref |
| 12 | `CHEAT_SHEET` | Cheat Sheet | 📎 Cheatsheet |
| 13 | `REPOSITORY` | Repository | 🔧 Repo |

### DifficultyLevel (4 values)

| # | Value | Ordinal | Abbreviation | Badge |
|---|---|---|---|---|
| 1 | `BEGINNER` | 1 | beg | 🟢 BEG |
| 2 | `INTERMEDIATE` | 2 | int | 🟡 INT |
| 3 | `ADVANCED` | 3 | adv | 🔴 ADV |
| 4 | `EXPERT` | 4 | exp | ⚫ EXP |

### ContentFreshness (5 values)

| # | Value | Display Name | Badge |
|---|---|---|---|
| 1 | `EVERGREEN` | Evergreen | ♾️ Evergreen |
| 2 | `ACTIVELY_MAINTAINED` | Actively Maintained | 🔄 Active |
| 3 | `PERIODICALLY_UPDATED` | Periodically Updated | 📅 Periodic |
| 4 | `HISTORICAL` | Historical | 📜 Historical |
| 5 | `ARCHIVED` | Archived | 🗄️ Archived |

### LanguageApplicability (6 values)

| # | Value | Display Name | Badge | Transferable? | Java-Relevant? |
|---|---|---|---|---|---|
| 1 | `UNIVERSAL` | Universal | 🌍 Universal | Yes | Yes |
| 2 | `MULTI_LANGUAGE` | Multi-Language | 🔤 Multi | Yes | Yes |
| 3 | `JAVA_CENTRIC` | Java-Centric | ☕+ Java-Centric | Partially | Yes |
| 4 | `JAVA_SPECIFIC` | Java-Specific | ☕ Java | No | Yes |
| 5 | `PYTHON_SPECIFIC` | Python-Specific | 🐍 Python | No | No |
| 6 | `WEB_SPECIFIC` | Web-Specific | 🌐 Web | No | No |

### SearchMode (3 values)

| # | Value | Triggers | Strategy |
|---|---|---|---|
| 1 | `SPECIFIC` | Quoted text, "docs for", "reference", "official", short queries | Exact title match → fuzzy fallback |
| 2 | `VAGUE` | Topic keywords without exploratory language | Balanced multi-dimensional scoring |
| 3 | `EXPLORATORY` | "learn", "start", "beginner", "recommend", "suggest" | Beginner-friendly, official-first |

---

## 17 — Appendix — Interface Contracts

### SearchEngine<T>

```java
public interface SearchEngine<T> {
    SearchResult<T> search(SearchContext context);
    default SearchResult<T> search(String rawInput) {
        return search(SearchContext.of(rawInput));
    }
}
```

### ScoringStrategy<T>

```java
@FunctionalInterface
public interface ScoringStrategy<T> {
    int score(T item, SearchContext context);      // ≥ 0; 0 = not relevant
    static <T> ScoringStrategy<T> zero();          // always 0
    static <T> ScoringStrategy<T> constant(int p); // always p
    default ScoringStrategy<T> withConstantBoost(int constant);
    default ScoringStrategy<T> scaledBy(double factor);
}
```

### QueryClassifier

```java
@FunctionalInterface
public interface QueryClassifier {
    SearchMode classify(String normalizedInput);
    static QueryClassifier alwaysVague();
    static QueryClassifier alwaysSpecific();
    static QueryClassifier alwaysExploratory();
    static QueryClassifier fixed(SearchMode mode);
}
```

### SearchFilter<T>

```java
@FunctionalInterface
public interface SearchFilter<T> {
    boolean test(T item, SearchContext context);
    default SearchFilter<T> and(SearchFilter<T> other);
    default SearchFilter<T> or(SearchFilter<T> other);
    default SearchFilter<T> negate();
    static <T> SearchFilter<T> allowAll();
    static <T> SearchFilter<T> rejectAll();
}
```

### SearchIndex<T>

```java
public interface SearchIndex<T> {
    void add(String id, T item);
    void remove(String id);
    Collection<T> all();
    Optional<T> findById(String id);
    int size();
    default boolean isEmpty();
}
```

### RankingStrategy<T>

```java
@FunctionalInterface
public interface RankingStrategy<T> {
    List<ScoredItem<T>> rank(List<ScoredItem<T>> items, SearchContext context);
    default RankingStrategy<T> thenRank(RankingStrategy<T> next);
}
```

### ResourceProvider

```java
@FunctionalInterface
public interface ResourceProvider {
    List<LearningResource> resources();
}
```

---

## 18 — Follow-Up / Next Steps

### Immediate (This Week)

- [ ] **Decision:** Adopt the Layered Hybrid architecture (Approach C) as the target design
- [ ] **Phase 2 / P0:** Fix SearchMode duplication — delete `model.SearchMode`, update 3-5 files
- [ ] **Phase 2 / P0:** Un-deprecate LearningResourcesServer in README

### Short-Term (Next 2 Sprints)

- [ ] **Phase 2 / P1:** Implement `FetchHandler` for dynamic web resource fetching
- [ ] **Phase 2 / P1:** Add faceted search (group by category, difficulty, type)
- [ ] **Phase 2 / P1:** Add query expansion via KeywordIndex synonym resolution
- [ ] **Phase 2 / P1:** Update `/resources` prompt to use both skills and MCP
- [ ] **Phase 2 / P1:** Create `/resources-web` prompt for dynamic web search

### Medium-Term (Next Quarter)

- [ ] **Phase 3:** Design prerequisite graph for 40 ConceptArea values
- [ ] **Phase 3:** Implement `LearningPathEngine` with path optimization
- [ ] **Phase 3:** Implement `ProgressTracker` with JSON file persistence
- [ ] **Phase 3:** Implement `AssessmentEngine` with concept-aware questions
- [ ] **Phase 3:** Add MCP tools: suggest_learning_path, assess_knowledge, track_progress

### Long-Term (Vision)

- [ ] **Phase 4:** Create `web-processor` module with 6-stage pipeline
- [ ] **Phase 4:** Implement HTTP fetcher with rate-limiting and robots.txt
- [ ] **Phase 4:** Implement content extraction + topic classification
- [ ] **Phase 4:** Implement deduplication (URL normalization + SimHash)
- [ ] **Phase 5:** Build recommendation engine (content-based + gap-filling)
- [ ] **Phase 5:** Add trending detection and personalized re-ranking
- [ ] **Phase 5:** Extract `learning-model` module if cross-module consumers appear
- [ ] **Phase 5:** Consider graph-based ontology if taxonomy exceeds 300 values

---

## Session Metadata

| Property | Value |
|---|---|
| Duration | ~2 exchanges (consolidated from 4 prior sessions) |
| Files touched | None (specification document only) |
| Prior sessions consolidated | [Migration intent](2026-03-22_02-00pm_mcp-to-skills-migration.md), [Migration audit](2026-03-22_05-30pm_migration-complete-audit.md), [Package restructuring](2026-03-24_03-00pm_package-restructuring-analysis.md), [Architecture deep-dive](2026-03-24_06-00pm_module-architecture-deep-dive.md) |
