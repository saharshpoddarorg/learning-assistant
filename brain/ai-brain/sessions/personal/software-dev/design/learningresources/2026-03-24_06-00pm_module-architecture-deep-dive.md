---
date: 2026-03-24
time: "06:00 PM"
kind: session-capture
domain: personal
category: design
project: learning-assistant
subject: learningresources-module-architecture-deep-dive
tags: [project:learning-assistant, gh:saharshpoddarorg/learning-assistant, design, architecture, java, mcp, skills, extensibility, search-engine, learning-library]
status: draft
version: 1
parent: null
complexity: high
outcomes:
  - identified 7 distinct architectural approaches for the learning resources module
  - compared all approaches across 12 evaluation dimensions
  - recommended a layered hybrid architecture combining MCP server + skills + search engine
  - designed a phased roadmap from current state to future vision
  - documented how search engine, web scraper, and learning library integrate
  - established extensibility patterns for future features
source: copilot
scope: project
scope-project: learning-assistant
scope-feature: null
scope-transitions:
  - at: "Exchange 1 — mid-analysis"
    from: "feature:learning-assistant/learningresources-design"
    to: "project:learning-assistant"
    reason: "analysis expanded from learningresources package to full learning platform architecture"
scope-refs:
  - file: "personal/software-dev/design/learningresources/2026-03-24_03-00pm_package-restructuring-analysis.md"
    relationship: related
    note: "prior session analyzed 6 package restructuring approaches — this session expands to full module architecture"
  - file: "personal/software-dev/design/learningresources/2026-03-22_02-00pm_mcp-to-skills-migration.md"
    relationship: related
    note: "original MCP-to-skills migration design"
---

# Learning Resources Module — Full Architecture Deep-Dive

> **Context:** The learning-assistant repo needs a robust, extensible architecture for its
> learning resources module — one that supports multiple learning use-cases (professional
> software engineering, personal development), multiple delivery mechanisms (search, browse,
> web scraping, guided learning), and is configurable/extensible for future features.
> This session investigates ALL possible design approaches using skills, instructions,
> prompts, agents, MCP servers, and hybrid combinations — then recommends the best path.

---

## Request

Analyze and design the best approach for the learning resources module, considering:

- Multiple learning purposes (SE concepts, personal development, etc.)
- Multiple resource types (docs, blogs, videos, courses, repos, etc.)
- Multiple categorization dimensions (concept, topic, difficulty, language, freshness, etc.)
- Future features: advanced search engine, web scraper/processor, dynamic web fetching, learning guidance
- Configurability, extensibility, and customizability as first-class requirements
- All possible implementation approaches: skills, instructions, prompts, agents, MCP servers, hybrid
- The existing codebase (138 resources, search-engine module, MCP server, skills migration)

---

## Analysis / Response

### Part 1 — Current State Assessment

#### What Exists Today

| Component | Location | Status | Purpose |
|---|---|---|---|
| **MCP Server** (Java) | `mcp-servers/src/server/learningresources/` | Deprecated (but working) | 10 MCP tools: search, browse, discover, scrape, export |
| **Search Engine** (Java) | `search-engine/src/search/` | Active, generic | 5-phase pipeline: classify→filter→score→rank→trim |
| **Skills** (Markdown) | `.github/skills/learning-resources-vault/` | Active, canonical | 138 curated resources in 10 sub-files + taxonomy |
| **Skills** (Markdown) | `.github/skills/java-learning-resources/` | Active | Deep Java-specific resources + offline scraped content |
| **Skills** (Markdown) | `.github/skills/software-engineering-resources/` | Active | SE foundations, books, real-world patterns |
| **Prompts** (Markdown) | `.github/prompts/resources.prompt.md` + 13 others | Active | `/resources`, `/learn-concept`, `/deep-dive`, etc. |
| **Agents** (Markdown) | `.github/agents/learning-mentor.agent.md` | Active | Teaching persona with building-block tree |
| **Brain** (Workspace) | `brain/ai-brain/sessions/` | Active | Session capture for learning conversations |

#### The 57-File Java Implementation

```text
mcp-servers/src/server/learningresources/
├── LearningResourcesServer.java          — MCP entry point
├── model/ (12 files)                     — 8 enums (97 values) + 4 records
├── vault/ (10 files + 17 providers)     — In-memory vault, discovery, scoring
├── handler/ (6 files)                   — MCP tool dispatch
├── content/ (4 files)                   — Content summarization
├── scraper/ (5 files)                   — Web scraping
└── search/ (2 files)                    — Bridge to search-engine
```

#### The Generic Search Engine (26 Java files)

```text
search-engine/src/search/
├── api/ (11 interfaces)                 — SearchEngine, ScoringStrategy, Tokenizer, etc.
└── engine/ (15 implementations)         — ConfigurableSearchEngine, BM25, TextMatch, etc.
```

Key design: **Tier model** (SearchEngine<T> → ConfigurableSearchEngine<T> → DomainEngine<T>)
makes the search engine fully reusable for any document type, not just learning resources.

---

### Part 2 — The 7 Design Approaches

I've identified 7 fundamentally different approaches for architecting the learning resources
module. Each is evaluated across 12 dimensions.

---

#### Approach A: Pure Skills (Markdown-Only, No Java Backend)

**Philosophy:** The LLM IS the search engine. Encode all knowledge as markdown that the
LLM reads directly. No runtime, no server, no compilation.

**Architecture:**

```text
.github/
├── skills/
│   ├── learning-resources-vault/
│   │   ├── SKILL.md                    ← Master index (activates on keyword match)
│   │   ├── taxonomy-reference.md       ← Full enum taxonomy (97 values, 300+ keywords)
│   │   ├── resources-java.md           ← 20 Java resources (table format)
│   │   ├── resources-web-javascript.md
│   │   ├── resources-python.md
│   │   ├── resources-algorithms-ds.md
│   │   ├── resources-software-engineering.md
│   │   ├── resources-devops-vcs-build.md
│   │   ├── resources-cloud-infra.md
│   │   ├── resources-ai-ml.md
│   │   ├── resources-productivity-pkm.md
│   │   └── resources-general-career.md
│   ├── java-learning-resources/        ← Deep-reference with scraped content
│   ├── software-engineering-resources/ ← SE foundations + real-world patterns
│   ├── career-resources/               ← Career paths, roles, compensation
│   └── daily-assistant-resources/      ← Finance, productivity, news
├── prompts/
│   ├── resources.prompt.md             ← /resources slash command
│   ├── learn-concept.prompt.md
│   ├── deep-dive.prompt.md
│   └── ... (14 total learning prompts)
└── agents/
    └── learning-mentor.agent.md        ← Teaching persona
```

**How It Works:**

1. User asks "I want to learn Java concurrency"
2. Copilot matches `learning-resources-vault` SKILL.md description keywords
3. LLM loads SKILL.md → reads `resources-java.md` → finds concurrency resources
4. LLM reads `taxonomy-reference.md` for concept hierarchy context
5. LLM applies native reasoning to rank/filter based on difficulty badges, official status
6. Response includes curated resources + teaching explanation from learning-mentor agent

**Extensibility Model:**

| Operation | How to Do It |
|---|---|
| Add new resource | Add a row to the appropriate `resources-*.md` table |
| Add new category | Create new `resources-*.md` file, update SKILL.md index |
| Add new concept | Add to `taxonomy-reference.md` concept area table |
| Add new resource type | Add badge mapping to taxonomy-reference.md |
| Change search behavior | Update SKILL.md instructions for how LLM should filter |
| Add feature (web scraping) | Not possible without code |
| Add feature (dynamic fetch) | Not possible without code |

**Evaluation:**

| Dimension | Rating | Notes |
|---|---|---|
| Configurability | Medium | Markdown tables are easy to edit but have no schema enforcement |
| Extensibility | Medium | New resources = new rows; new features = impossible without code |
| Customizability | High | User can edit markdown directly; LLM instructions are flexible |
| Search quality | Medium | LLM reasoning is good but not algorithmic; no BM25/TF-IDF |
| Dynamic content | None | Cannot fetch from web; purely static curated content |
| Runtime deps | None | No JVM, no server, no compilation |
| Token cost | Low-Medium | Skills loaded into context as needed (~500-2000 tokens per skill) |
| Maintenance | Very Low | Edit markdown, git commit, done |
| Future features | Low | Web scraping, dynamic fetch, learning guidance all need code |
| Type safety | None | No compile-time checks; typos in badges go undetected |
| Offline capability | Full | Everything is local markdown |
| Learning guidance | Medium | Agent persona provides guidance; no adaptive algorithm |

**Verdict:** This is **what you already have** — and it works well for curated, static
resources. But it cannot fulfill the vision of dynamic web fetching, advanced search,
or a fully-fledged learning library.

---

#### Approach B: Pure MCP Server (Java Backend Only)

**Philosophy:** Code is the authority. All search, classification, scoring, web scraping,
and learning guidance lives in Java. The LLM is a consumer of tool outputs.

**Architecture:**

```text
mcp-servers/src/server/learningresources/
├── LearningResourcesServer.java     ← MCP entry point (10+ tools)
├── model/                           ← Type-safe domain model (enums + records)
├── vault/                           ← In-memory vault + providers + scoring
├── handler/                         ← MCP tool dispatch
├── content/                         ← Summarization
├── scraper/                         ← Web scraping (HttpClient + HTML parsing)
├── search/                          ← Domain-specific search bridge
└── guidance/                        ← NEW: adaptive learning path engine

search-engine/src/search/
├── api/                             ← Generic search interfaces
└── engine/                          ← BM25, TF-IDF, fuzzy, composite scoring
```

**MCP Tool Surface:**

| Tool | Purpose | Params |
|---|---|---|
| `search_resources` | Full-text multi-criteria search | query, type, category, difficulty, tags |
| `discover_resources` | Intent-classified discovery | userInput, forcedMode |
| `browse_vault` | Browse by category/type | category, type |
| `get_resource` | Resource details by ID | id |
| `list_categories` | Category summary with counts | — |
| `scrape_url` | Fetch + extract + summarize URL | url, mode |
| `add_resource` | Add custom resource with metadata | title, url, type, ... |
| `export_results` | Export as Markdown/PDF | format, query |
| `suggest_learning_path` | NEW: Adaptive path recommendation | topic, level, goal |
| `fetch_web_resources` | NEW: Dynamic web search → vault | topic, sources, count |
| `assess_knowledge` | NEW: Quiz/assessment on topic | topic, level |

**Extensibility Model:**

| Operation | How to Do It |
|---|---|
| Add new resource | New provider class implementing `ResourceProvider` |
| Add new category | Add to `ResourceCategory` enum + `KeywordIndex` |
| Add new concept | Add to `ConceptArea` enum + keyword mappings |
| Add feature | New handler class + register in `ToolHandler` |
| Change search algorithm | Swap `ScoringStrategy` in config |
| Dynamic web fetch | Extend `WebScraper` + new handler |
| Learning guidance | New `GuidanceEngine` + MCP tool |

**Evaluation:**

| Dimension | Rating | Notes |
|---|---|---|
| Configurability | High | Type-safe config, builder patterns, strategy injection |
| Extensibility | High | New features = new Java classes + register in dispatcher |
| Customizability | Medium | Requires Java coding; not end-user customizable |
| Search quality | Very High | BM25 + WeightedScorer + FuzzyMatch + dedicated tuning |
| Dynamic content | High | WebScraper is production-ready; can fetch from any URL |
| Runtime deps | High | JDK 21+, running server process, compilation |
| Token cost | High | Every tool call = JSON-RPC round trip (~2000 tokens) |
| Maintenance | Medium-High | Java code, compile cycle, type safety helps |
| Future features | Very High | Unlimited — any code-based feature can be added |
| Type safety | Full | Compiler catches errors; enums enforce valid values |
| Offline capability | Full | Vault is local; scraper works with network |
| Learning guidance | Very High | Can build adaptive algorithms with state |

**Verdict:** Maximum power and extensibility, but **heavy** for AI-assistant integration.
Token cost per tool call is significant. Every interaction requires a running JVM.
However, this is the ONLY approach that supports dynamic web fetching and algorithmic
learning guidance.

---

#### Approach C: Skills + MCP Server Hybrid (Recommended)

**Philosophy:** Use **skills for static knowledge** (curated resources, taxonomy, learning
paths) and **MCP server for dynamic capabilities** (web scraping, advanced search, dynamic
fetch, assessment). The LLM reads skills for instant context; calls MCP tools for actions.

**Architecture:**

```text
LAYER 1 — STATIC KNOWLEDGE (Skills, always in context)
.github/skills/
├── learning-resources-vault/       ← 138+ curated resources (markdown tables)
│   ├── SKILL.md                    ← Master index + activation keywords
│   ├── taxonomy-reference.md       ← Domain model as prose (97 enum values)
│   └── resources-*.md              ← 10 category sub-files
├── java-learning-resources/        ← Deep Java content (offline-ready)
├── software-engineering-resources/ ← SE foundations, books, patterns
├── career-resources/               ← Career growth, roles, compensation
└── daily-assistant-resources/      ← Finance, productivity

LAYER 2 — TEACHING INTELLIGENCE (Agents + Prompts)
.github/agents/
└── learning-mentor.agent.md        ← Teaching persona, building-block tree
.github/prompts/
├── resources.prompt.md             ← /resources — MCP-backed resource search
├── learn-concept.prompt.md         ← /learn-concept — skill-backed teaching
├── deep-dive.prompt.md             ← /deep-dive — 3-tier exploration
└── ... (14 total prompts)

LAYER 3 — DYNAMIC CAPABILITIES (MCP Server, called on demand)
mcp-servers/src/server/learningresources/
├── LearningResourcesServer.java
├── model/                          ← Type-safe domain model
├── vault/                          ← In-memory search + discovery
├── scraper/                        ← Web scraping pipeline
├── search/                         ← Bridge to search-engine
├── fetcher/                        ← NEW: Dynamic web resource fetcher
├── guidance/                       ← NEW: Adaptive learning path engine
├── assessment/                     ← NEW: Knowledge assessment engine
└── handler/                        ← MCP tool dispatch

LAYER 4 — GENERIC SEARCH ENGINE (Reusable library)
search-engine/src/search/
├── api/                            ← Interfaces (SearchEngine, ScoringStrategy, etc.)
└── engine/                         ← Implementations (BM25, TextMatch, Composite, etc.)
```

**How the Layers Interact:**

```text
User: "I want to learn Java concurrency"

┌─────────────────────────────────────────────────────────────────┐
│  COPILOT CHAT                                                    │
│                                                                  │
│  1. Skill activation: learning-resources-vault loads             │
│     → LLM reads resources-java.md → finds concurrency resources │
│     → Instant, zero-cost, in-context                            │
│                                                                  │
│  2. Agent activation: learning-mentor persona                    │
│     → Provides teaching structure (what, why, analogy, code)    │
│     → Uses building-block tree for prerequisite ordering         │
│                                                                  │
│  3. If user wants MORE (dynamic content):                        │
│     → /resources search concurrency --web                       │
│     → MCP tool call: fetch_web_resources("java concurrency")    │
│     → Server fetches from Baeldung, dev.java, Inside.java       │
│     → Returns fresh articles not in static vault                │
│                                                                  │
│  4. If user wants GUIDANCE:                                      │
│     → /resources learning-path concurrency intermediate          │
│     → MCP tool call: suggest_learning_path(...)                 │
│     → Server generates adaptive path based on level + goal      │
│                                                                  │
│  5. If user wants ASSESSMENT:                                    │
│     → /resources assess concurrency intermediate                 │
│     → MCP tool call: assess_knowledge(...)                      │
│     → Server generates topic-specific quiz questions            │
└─────────────────────────────────────────────────────────────────┘
```

**Extensibility Model:**

| Operation | Layer | How to Do It |
|---|---|---|
| Add curated resource | Skills | Add row to `resources-*.md` table |
| Add new concept/category | Skills + Java | Update `taxonomy-reference.md` AND `ConceptArea` enum |
| Change teaching style | Agent | Edit `learning-mentor.agent.md` |
| Add new slash command | Prompt | Create `.prompt.md` file |
| Add web scraping source | MCP Server | Add source to `fetcher/` config |
| Add advanced search | MCP Server | Add scorer/filter to search pipeline |
| Add learning guidance | MCP Server | Implement `GuidanceEngine` |
| Add assessment/quiz | MCP Server | Implement `AssessmentEngine` |
| Add new data source | MCP Server | New provider implementing `ResourceProvider` |
| User-customizable config | Skills + Config | Skill instructions + `mcp-config.properties` |

**Evaluation:**

| Dimension | Rating | Notes |
|---|---|---|
| Configurability | Very High | Skills for static config, Java for runtime config |
| Extensibility | Very High | Skills for content, Java for features — best of both |
| Customizability | Very High | Users edit skills; devs extend Java; agents adjust behavior |
| Search quality | Very High | LLM for semantic; BM25/TF-IDF for algorithmic; composable |
| Dynamic content | High | MCP scraper + fetcher for live web content |
| Runtime deps | Medium | Skills work without JVM; MCP features need JDK |
| Token cost | Low-Medium | Skills are free; MCP calls only when needed |
| Maintenance | Medium | Two systems to maintain, but clear division of labor |
| Future features | Very High | Any code feature via MCP; any knowledge via skills |
| Type safety | Partial | Java model is type-safe; skills are markdown (no types) |
| Offline capability | High | Skills fully offline; MCP vault offline; only web fetch needs network |
| Learning guidance | Very High | LLM reasoning + algorithmic paths + assessment |

**Verdict:** **Best overall architecture.** Combines the instant, zero-cost knowledge of
skills with the power of coded algorithms for dynamic features. Each layer does what it
does best.

---

#### Approach D: Agent-Centric Architecture

**Philosophy:** The learning-mentor agent is the hub. All learning flows through agent
personality, teaching methodology, and conversation management. Skills provide knowledge;
MCP provides tools; the agent orchestrates everything.

**Architecture:**

```text
.github/agents/
├── learning-mentor.agent.md           ← PRIMARY: orchestrates all learning
│   ├── [building-block-tree]          ← Concept hierarchy with prerequisites
│   ├── [analogy-library]              ← 50+ domain analogies
│   ├── [teaching-methodology]         ← What→Why→Analogy→Code→Anti-pattern
│   └── [tool-restrictions]            ← search, codebase, fetch, MCP tools
├── learning-researcher.agent.md       ← NEW: researches topics via web
├── learning-assessor.agent.md         ← NEW: assesses knowledge, creates quizzes
└── learning-curator.agent.md          ← NEW: maintains and updates resource vault

.github/skills/                        ← Knowledge base (read by agents)
.github/prompts/                       ← Entry points that select agents
mcp-servers/                           ← Tools available to agents
```

**Key Difference from Approach C:** Here, agents are **first-class orchestrators**, not
just personas. Each agent has a specific role:

| Agent | Role | When Activated |
|---|---|---|
| `learning-mentor` | Teaches concepts, explains, provides examples | `/learn-concept`, `/deep-dive`, `/teach` |
| `learning-researcher` | Fetches web content, evaluates resources, expands vault | `/resources search --web`, `/resources discover` |
| `learning-assessor` | Quizzes, evaluates understanding, suggests next steps | `/resources assess`, `/interview-prep` |
| `learning-curator` | Maintains vault quality, deduplicates, updates metadata | `/resources curate`, `/resources audit` |

**Evaluation:**

| Dimension | Rating | Notes |
|---|---|---|
| Configurability | High | Agent personas are highly configurable via markdown |
| Extensibility | High | New agent = new capability; compose via handoffs |
| Customizability | Very High | Agent behavior is plain English in markdown |
| Search quality | Medium-High | Depends on agent's use of MCP tools |
| Dynamic content | High | Researcher agent can fetch + process web content |
| Runtime deps | Medium | Agents need nothing; MCP tools need JVM |
| Token cost | Medium-High | Multiple agent contexts may be loaded |
| Maintenance | Medium | Agent coordination adds complexity |
| Future features | High | New agents for new capabilities |
| Type safety | None | Agent behavior is natural language |
| Offline capability | Medium | Agents work offline; web research doesn't |
| Learning guidance | Very High | Agent personality = teaching methodology baked in |

**Verdict:** Strong for teaching-focused use cases. The multi-agent pattern is elegant
for separating concerns (teaching vs. research vs. assessment). However, agent
orchestration is still nascent in Copilot — hand-offs between agents are manual (user
must switch in dropdown). Best combined with Approach C.

---

#### Approach E: Instructions-Based Architecture

**Philosophy:** Encode learning behavior as instructions that activate contextually.
Instructions modify how Copilot behaves when working with learning content.

**Architecture:**

```text
.github/instructions/
├── learning-resources.instructions.md    ← When editing resource files
│   applyTo: "**/resources-*.md"
│   Rules: validate badge format, enforce taxonomy, check URL reachability
│
├── learning-guidance.instructions.md     ← When learning topics
│   applyTo: "**"
│   Rules: always check difficulty level, suggest prerequisites, cite sources
│
├── taxonomy-validation.instructions.md   ← When editing taxonomy
│   applyTo: "**/taxonomy-*.md"
│   Rules: enforce enum naming, validate concept-domain associations
│
└── web-scraping.instructions.md          ← When scraping web content
    applyTo: "**/scraper/**/*.java"
    Rules: respect robots.txt, rate-limit, sanitize HTML
```

**Evaluation:**

| Dimension | Rating | Notes |
|---|---|---|
| Configurability | Medium | Instructions are rules, not data or features |
| Extensibility | Low | Instructions modify behavior, not add capabilities |
| Customizability | Medium | Glob patterns control activation |
| Search quality | N/A | Instructions don't search; they guide behavior |
| Dynamic content | None | Instructions are passive rules |
| Runtime deps | None | Pure markdown, no compilation |
| Token cost | Low | Instructions are terse; activated only per-file-type |
| Maintenance | Low | Set once, rarely update |
| Future features | Very Low | Instructions cannot add features |
| Type safety | None | Natural language rules |
| Offline capability | Full | Passive rules, always available |
| Learning guidance | Low | Rules, not teaching; no adaptive behavior |

**Verdict:** Instructions are **complementary, never primary**. They enforce quality and
consistency (e.g., "always validate taxonomy badges") but cannot deliver features like
search, web scraping, or learning guidance. Use as a **governance layer** on top of
other approaches.

---

#### Approach F: Prompt-Centric Architecture

**Philosophy:** Each learning workflow is a prompt (slash command) with carefully designed
input variables, agent assignment, and tool restrictions.

**Architecture:**

```text
.github/prompts/
├── resources.prompt.md           ← /resources — search/browse/discover
├── learn-concept.prompt.md       ← /learn-concept — concept teaching
├── deep-dive.prompt.md           ← /deep-dive — 3-tier exploration
├── reading-plan.prompt.md        ← /reading-plan — structured learning path
├── teach.prompt.md               ← /teach — multi-level explanation
├── interview-prep.prompt.md      ← /interview-prep — practice questions
├── dsa.prompt.md                 ← /dsa — algorithms reference
├── system-design.prompt.md       ← /system-design — HLD/LLD
├── language-guide.prompt.md      ← /language-guide — language learning path
├── resources-web.prompt.md       ← NEW: /resources-web — dynamic web search
├── resources-assess.prompt.md    ← NEW: /resources-assess — knowledge test
├── learning-path.prompt.md       ← NEW: /learning-path — adaptive guidance
└── resources-curate.prompt.md    ← NEW: /resources-curate — vault maintenance
```

**Evaluation:**

| Dimension | Rating | Notes |
|---|---|---|
| Configurability | Medium | Input variables provide user-facing config |
| Extensibility | Medium | New prompt = new workflow; but limited by LLM context |
| Customizability | High | Prompts are user-facing, easy to understand |
| Search quality | Low-Medium | Prompts guide LLM but don't add algorithmic capability |
| Dynamic content | Low | Prompts can reference tools but don't implement them |
| Runtime deps | None | Pure markdown |
| Token cost | Low | Prompts are short; activate only when invoked |
| Maintenance | Low | Edit markdown, add new prompts |
| Future features | Low | Prompts invoke features but don't create them |
| Type safety | None | Natural language |
| Offline capability | Full | Always available |
| Learning guidance | Medium | Prompt structure guides teaching flow |

**Verdict:** Prompts are **entry points, never engines**. They define workflows ("how to
start learning X") but can't implement search algorithms or web scraping. Essential as
the user-facing interface layer, but need skills (for knowledge) and MCP (for capabilities)
behind them.

---

#### Approach G: Full-Stack Learning Platform (Maximum Vision)

**Philosophy:** Build a complete, production-grade learning platform with all layers
fully implemented. This is the "target state" if all features are built.

**Architecture:**

```text
LAYER 1 — DATA MODEL (learning-model module)
learning-model/src/
└── learningresources/model/
    ├── ConceptArea.java          ← 40+ concept areas with domain grouping
    ├── ConceptDomain.java        ← 9 high-level domains
    ├── ResourceCategory.java     ← 17+ categories
    ├── ResourceType.java         ← 13+ content types
    ├── DifficultyLevel.java      ← 4 levels
    ├── ContentFreshness.java     ← 5 freshness states
    ├── LanguageApplicability.java ← 6 language scopes
    ├── LearningResource.java     ← Core entity (15+ fields)
    ├── LearningPath.java         ← NEW: ordered sequence of resources + milestones
    ├── UserProgress.java         ← NEW: tracks what user has learned
    ├── AssessmentQuestion.java   ← NEW: quiz question model
    └── ResourceQuery.java        ← Search criteria composite

LAYER 2 — GENERIC SEARCH ENGINE (search-engine module)
search-engine/src/search/
├── api/                          ← Interfaces (unchanged, already generic)
└── engine/                       ← BM25, TF-IDF, fuzzy, composite, filters, rankers

LAYER 3 — WEB PROCESSING (web-processor module) — NEW
web-processor/src/
└── web/
    ├── scraper/
    │   ├── WebScraper.java            ← HTTP fetching with retry, rate-limit
    │   ├── HtmlExtractor.java         ← DOM parsing, content extraction
    │   ├── MetadataInferrer.java      ← Auto-detect type, difficulty, topic
    │   └── RobotsChecker.java         ← Respect robots.txt
    ├── processor/
    │   ├── ContentProcessor.java      ← Clean, normalize, summarize
    │   ├── ReadabilityScorer.java     ← Flesch-Kincaid, difficulty inference
    │   ├── TopicClassifier.java       ← Keyword-based topic detection
    │   └── DeduplicationEngine.java   ← URL + content similarity matching
    └── sources/
        ├── WebSource.java             ← Interface for data sources
        ├── GoogleScholarSource.java   ← Academic papers
        ├── GitHubTrendingSource.java  ← Trending repos
        ├── DevBlogAggregator.java     ← RSS/Atom feed aggregation
        └── DocumentationCrawler.java  ← Crawl official docs sites

LAYER 4 — LEARNING INTELLIGENCE (learning-engine module) — NEW
learning-engine/src/
└── learning/
    ├── paths/
    │   ├── LearningPathEngine.java     ← Generates adaptive learning paths
    │   ├── PrerequisiteGraph.java      ← DAG of concept dependencies
    │   ├── ProgressTracker.java        ← Tracks completed resources/concepts
    │   └── PathOptimizer.java          ← Optimizes path based on goals
    ├── assessment/
    │   ├── AssessmentEngine.java       ← Generates quiz questions
    │   ├── QuestionGenerator.java      ← Topic-aware question creation
    │   ├── KnowledgeEstimator.java     ← Estimates user level from answers
    │   └── GapAnalyzer.java            ← Identifies knowledge gaps
    └── recommendation/
        ├── RecommendationEngine.java   ← Collaborative + content-based filtering
        ├── SimilarityScorer.java       ← Resource similarity (tag overlap, concept)
        └── TrendingDetector.java       ← Detect popular/rising topics

LAYER 5 — MCP SERVER (mcp-servers module)
mcp-servers/src/server/learningresources/
├── LearningResourcesServer.java
├── handler/
│   ├── SearchHandler.java         ← search, browse, discover (existing)
│   ├── ScrapeHandler.java         ← scrape URL (existing)
│   ├── FetchHandler.java          ← NEW: dynamic web resource fetching
│   ├── GuidanceHandler.java       ← NEW: learning path + guidance
│   ├── AssessmentHandler.java     ← NEW: quiz + assessment
│   ├── RecommendHandler.java      ← NEW: personalized recommendations
│   └── ExportHandler.java         ← export as Markdown/PDF (existing)
└── vault/                         ← In-memory resource store (existing)

LAYER 6 — COPILOT INTEGRATION (skills + prompts + agents)
.github/
├── skills/
│   ├── learning-resources-vault/  ← Curated static knowledge
│   ├── java-learning-resources/   ← Deep Java content
│   └── software-engineering-resources/
├── prompts/
│   ├── resources.prompt.md        ← Primary entry point
│   ├── learn-concept.prompt.md
│   └── ... (14+ prompts)
├── agents/
│   ├── learning-mentor.agent.md   ← Teaching persona
│   ├── learning-researcher.agent.md ← NEW
│   └── learning-assessor.agent.md   ← NEW
└── instructions/
    ├── learning-resources.instructions.md ← Quality rules
    └── taxonomy-validation.instructions.md
```

**Evaluation:**

| Dimension | Rating | Notes |
|---|---|---|
| Configurability | Maximum | Every layer has its own config surface |
| Extensibility | Maximum | New module = new capability domain |
| Customizability | Maximum | Skills for content, agents for behavior, config for tuning |
| Search quality | Maximum | BM25 + semantic + web search + collaborative filtering |
| Dynamic content | Maximum | Web scraper, RSS aggregator, doc crawler, trending detector |
| Runtime deps | High | JDK 21+ for all Java modules |
| Token cost | Medium | Skills free, MCP on-demand |
| Maintenance | High | 4+ Java modules, skills, prompts, agents |
| Future features | Unlimited | Any feature fits into one of the layers |
| Type safety | Full | Java model shared across all modules |
| Offline capability | High | Vault + skills offline, web features need network |
| Learning guidance | Maximum | Prerequisite DAG, adaptive paths, assessments, recommendations |

**Verdict:** This is the **end-state vision**. It's what the system would look like if fully
realized. Not all layers need to be built at once — the phased roadmap below shows how to
get there incrementally.

---

### Part 3 — Comprehensive Decision Matrix

#### Approach Comparison (12 Dimensions)

| Dimension | A: Pure Skills | B: Pure MCP | C: Hybrid ★ | D: Agent-Centric | E: Instructions | F: Prompts | G: Full Platform |
|---|---|---|---|---|---|---|---|
| **Configurability** | Medium | High | Very High | High | Medium | Medium | Maximum |
| **Extensibility** | Medium | High | Very High | High | Low | Medium | Maximum |
| **Customizability** | High | Medium | Very High | Very High | Medium | High | Maximum |
| **Search quality** | Medium | Very High | Very High | Medium-High | N/A | Low-Medium | Maximum |
| **Dynamic content** | None | High | High | High | None | Low | Maximum |
| **Runtime deps** | None | High | Medium | Medium | None | None | High |
| **Token cost** | Low-Med | High | Low-Med | Med-High | Low | Low | Medium |
| **Maintenance** | Very Low | Med-High | Medium | Medium | Low | Low | High |
| **Future features** | Low | Very High | Very High | High | Very Low | Low | Unlimited |
| **Type safety** | None | Full | Partial | None | None | None | Full |
| **Offline** | Full | Full | High | Medium | Full | Full | High |
| **Learning guidance** | Medium | Very High | Very High | Very High | Low | Medium | Maximum |

#### Scoring Summary (1-5 scale, weighted by importance)

| Approach | Config (×2) | Extend (×3) | Custom (×2) | Search (×2) | Dynamic (×3) | Token (×1) | Maintain (×1) | Future (×3) | Total |
|---|---|---|---|---|---|---|---|---|---|
| A: Pure Skills | 6 | 6 | 8 | 6 | 0 | 4 | 5 | 3 | **38** |
| B: Pure MCP | 8 | 12 | 4 | 8 | 12 | 2 | 3 | 15 | **64** |
| **C: Hybrid** ★ | **8** | **15** | **8** | **8** | **12** | **4** | **3** | **15** | **73** |
| D: Agent-Centric | 8 | 12 | 8 | 6 | 12 | 3 | 3 | 12 | **64** |
| E: Instructions | 6 | 3 | 4 | 0 | 0 | 5 | 5 | 3 | **26** |
| F: Prompts | 6 | 6 | 8 | 4 | 3 | 5 | 5 | 3 | **40** |
| G: Full Platform | 10 | 15 | 10 | 10 | 15 | 3 | 2 | 15 | **80** |

**Winner: Approach C (Hybrid)** — best balance of power vs. complexity.
**Aspirational: Approach G (Full Platform)** — build toward this incrementally.

---

### Part 4 — The Recommended Architecture: Layered Hybrid (C+D+G)

The recommended approach synthesizes Approaches C, D, and G into a phased plan:

#### Phase 1 — Current State (Done)

**What you already have:**

- Skills layer: 138 curated resources, taxonomy, deep Java content
- MCP server: 10 tools (search, browse, discover, scrape, export) — deprecated but working
- Search engine: Generic 5-phase pipeline (BM25, text match, composite scoring)
- Agent: learning-mentor with building-block tree and 50+ analogies
- Prompts: 14 slash commands for different learning workflows

**Status: COMPLETE. Working system for curated, static learning.**

#### Phase 2 — Revive & Enhance MCP Server (Next)

**Goal:** Un-deprecate the learning resources MCP server and enhance it for dynamic
capabilities. The skills approach handles static knowledge; the MCP server handles
dynamic operations.

**Changes:**

```text
1. KEEP skills as the primary static knowledge source
   - Resources live in markdown (easy to edit, zero-cost for LLM)
   - Taxonomy lives in taxonomy-reference.md

2. REVIVE MCP server for dynamic capabilities:
   a. Web scraping (existing — clean up and enhance)
   b. Dynamic resource fetching (NEW)
   c. Advanced search with BM25/composite scoring (bridge to search-engine)

3. REFACTOR model package:
   - Fix SearchMode duplication (use search.api.classify.SearchMode)
   - Keep model/ where it is (YAGNI — no cross-module consumer yet)

4. UPDATE prompts:
   - /resources should use BOTH skills (curated) and MCP (dynamic)
   - Add /resources-web for dynamic web search
```

**Effort:** Medium. Mostly re-enabling existing code + adding fetcher handler.

#### Phase 3 — Learning Intelligence Engine (Future)

**Goal:** Build adaptive learning features that go beyond static resources.

**New Modules/Components:**

```text
1. learning-engine/ (new module or package within mcp-servers)
   - PrerequisiteGraph: DAG of concept dependencies
   - LearningPathEngine: generates personalized paths
   - ProgressTracker: remembers what user has learned (file-based)
   - GapAnalyzer: identifies knowledge gaps

2. assessment/ (within mcp-servers)
   - QuestionGenerator: creates topic-specific quiz questions
   - KnowledgeEstimator: estimates user level from quiz responses

3. New MCP tools:
   - suggest_learning_path(topic, level, goal)
   - assess_knowledge(topic, level)
   - track_progress(resourceId, status)
```

**Effort:** High. Requires designing prerequisite graph, path optimization algorithms.

#### Phase 4 — Web Processing Pipeline (Future)

**Goal:** Build a full web content processing pipeline for dynamic resource discovery.

**New Module:**

```text
web-processor/ (new top-level module)
├── scraper/       ← HTTP with retry, rate-limit, robots.txt
├── processor/     ← Content extraction, summarization, dedup
├── classifier/    ← Topic detection, difficulty inference
└── sources/       ← Pluggable data sources (GitHub, RSS, docs)
```

**Effort:** High. Requires web crawling infrastructure, content processing pipeline.

#### Phase 5 — Full Learning Library (Vision)

**Goal:** A complete, self-sustaining learning library that discovers, curates, and
teaches from web content automatically.

**Features:**

- Auto-discovering new resources from RSS feeds, GitHub trending, blog aggregators
- Auto-classifying resources by topic, difficulty, language, quality
- Deduplication engine (URL-based + content similarity)
- Personalized recommendation engine (content-based + collaborative filtering)
- Spaced repetition for reviewed concepts
- Integration with brain/sessions for tracking learning progress

**Effort:** Very High. This is a capstone feature built on all previous phases.

---

### Part 5 — Keyword/Tagging Algorithm Design

Your question about the best approach for the keyword/tagging system in the enums deserves
dedicated analysis.

#### Current State: Enum-Based Taxonomy

```java
// 8 enums, 97 total values
ConceptArea (40 values)    — fine-grained CS/SE concepts
ConceptDomain (9 values)   — high-level groupings
ResourceCategory (17 values) — topic domains
ResourceType (13 values)   — content format
DifficultyLevel (4 values) — skill level
ContentFreshness (5 values) — maintenance status
LanguageApplicability (6 values) — language scope
SearchMode (3 values)      — query classification
```

Plus `KeywordIndex.java` with 200+ keyword → enum mappings.

#### Alternative Approaches for Taxonomy/Tagging

**Option 1: Enum-Based (Current)**

```java
enum ConceptArea {
    CONCURRENCY("concurrency", ConceptDomain.PROGRAMMING_FUNDAMENTALS),
    DESIGN_PATTERNS("design-patterns", ConceptDomain.SOFTWARE_ENGINEERING),
    // ... 38 more
}
```

- **Pros:** Type-safe, compile-time validation, IDE autocomplete, exhaustive switch
- **Cons:** Rigid (adding a value = recompile), limited hierarchy (flat within enum)
- **Best for:** Stable taxonomies that change infrequently

**Option 2: Hierarchical Enum with Nested Categories**

```java
enum ConceptArea {
    // Level 1: Domain
    PROGRAMMING_FUNDAMENTALS__LANGUAGE_BASICS("language-basics", 1),
    PROGRAMMING_FUNDAMENTALS__OOP("oop", 1),
    PROGRAMMING_FUNDAMENTALS__CONCURRENCY("concurrency", 1),
    // Level 2: Sub-concept
    PROGRAMMING_FUNDAMENTALS__CONCURRENCY__THREADS("threads", 2),
    PROGRAMMING_FUNDAMENTALS__CONCURRENCY__LOCKS("locks", 2),
    PROGRAMMING_FUNDAMENTALS__CONCURRENCY__VIRTUAL_THREADS("virtual-threads", 2),
    // ...
}
```

- **Pros:** Deeper hierarchy, still type-safe, can query by level
- **Cons:** Verbose naming, explosive growth, still flat in Java
- **Best for:** When you need 2-3 levels of hierarchy with compile-time safety

**Option 3: Tag-Based (Free-Form Strings)**

```java
record LearningResource(
    // ... other fields
    Set<String> tags,           // e.g., {"java", "concurrency", "threads", "advanced"}
    Set<String> concepts,       // e.g., {"concurrency", "multithreading", "synchronization"}
    Set<String> categories      // e.g., {"java", "programming-fundamentals"}
) {}
```

- **Pros:** Infinitely flexible, no recompilation for new tags, easy user-contributed
- **Cons:** No type safety, typo-prone, no autocomplete, inconsistent naming
- **Best for:** User-generated content, crowd-sourced tagging

**Option 4: Hybrid (Enum Core + String Extensions)**

```java
record LearningResource(
    // Type-safe core taxonomy (stable, well-known values)
    List<ConceptArea> conceptAreas,      // enum
    List<ResourceCategory> categories,    // enum
    DifficultyLevel difficulty,           // enum

    // Flexible extensions (user-defined, evolving)
    Set<String> tags,                     // free-form
    Map<String, String> metadata          // arbitrary key-value
) {}
```

- **Pros:** Type safety for stable concepts + flexibility for evolving ones
- **Cons:** Two systems to maintain, query complexity
- **Best for:** Growing projects where core taxonomy is stable but extensions evolve

**Option 5: Ontology/Graph-Based**

```java
class ConceptNode {
    String id;                     // "concurrency"
    String displayName;            // "Concurrency"
    Set<String> aliases;           // {"threads", "multithreading", "async"}
    Set<ConceptNode> parents;      // {programmingFundamentals}
    Set<ConceptNode> children;     // {threads, locks, atomics, virtualThreads}
    Set<ConceptNode> related;      // {parallelism, distributedSystems}
    Map<String, Object> properties; // {difficulty: "advanced", domain: "cs"}
}

class ConceptGraph {
    Map<String, ConceptNode> nodes;
    // Query: all children of "concurrency" at depth 2
    // Query: all ancestors of "virtual-threads"
    // Query: shortest path from "oop" to "design-patterns"
}
```

- **Pros:** Rich relationships, unlimited hierarchy depth, semantic queries
- **Cons:** Complex implementation, no compile-time safety, harder to maintain
- **Best for:** Large-scale knowledge systems, true ontologies

#### Recommended: Option 4 (Hybrid) for Java, Option 1 (Enum) retained as-is

**Rationale:**

1. **Current enums are good enough.** 97 values across 8 enums covers the domain well.
   The taxonomy changes ~once per quarter, not daily.

2. **The `tags` field already provides free-form flexibility.** Every `LearningResource`
   has `List<String> tags` alongside the enum fields. This IS Option 4 already.

3. **KeywordIndex provides the bridge.** 200+ keywords mapped to enums means natural
   language queries resolve to type-safe values.

4. **For the full platform vision (Phase 5)**, migrating to a graph-based ontology
   (Option 5) would make sense — but only when the resource count exceeds 500+ and
   the concept hierarchy needs deeper nesting.

#### The KeywordIndex Pattern (Algorithm Deep-Dive)

The current `KeywordIndex` uses a clever pattern worth preserving:

```java
// Static map: human keyword → type-safe enum
Map<String, ConceptArea> CONCEPT_MAP = Map.ofEntries(
    entry("concurrency",      ConceptArea.CONCURRENCY),
    entry("threads",          ConceptArea.CONCURRENCY),
    entry("async",            ConceptArea.CONCURRENCY),
    entry("virtual threads",  ConceptArea.CONCURRENCY),
    entry("design patterns",  ConceptArea.DESIGN_PATTERNS),
    entry("singleton",        ConceptArea.DESIGN_PATTERNS),
    entry("factory",          ConceptArea.DESIGN_PATTERNS),
    // ... 200+ entries
);
```

This is essentially a **synonym resolution table** — maps diverse user vocabulary to
canonical concepts. The search engine's `KeywordQueryClassifier` can use this to
transform vague queries into structured searches.

**For the future search engine, enhance this with:**

1. **Weighted keywords** — "concurrency" is a stronger signal for CONCURRENCY than "async"
2. **Multi-mapping** — "async" could map to CONCURRENCY + NETWORKING
3. **Negative keywords** — "not security" explicitly excludes WEB_SECURITY
4. **Phrase detection** — "virtual threads" as a phrase vs. "virtual" + "threads" separately

---

### Part 6 — Search Engine Architecture Recommendations

Your existing `search-engine` module is already well-designed. Here's how to evolve it
for the full learning library vision:

#### Current Capabilities (Already Built)

```text
✅ 5-phase pipeline (classify → filter → score → rank → trim)
✅ BM25 probabilistic ranking
✅ Text match scoring (exact, partial, per-word, fuzzy)
✅ Tag scoring with overlap calculation
✅ Composite scoring (weighted sum of multiple strategies)
✅ Intent classification (specific / vague / exploratory)
✅ Filter composition (AND/OR/NOT)
✅ In-memory index with thread-safe ConcurrentHashMap
✅ Recency boost ranking
✅ Score breakdown for debugging
✅ Generic type parameter — works for any document type
```

#### What to Add for Enhanced Search

```text
Phase 2 additions:
  □ Faceted search (group results by category, difficulty, type)
  □ Autocomplete / typeahead suggestions
  □ Spelling correction (Levenshtein distance)
  □ Query expansion (synonym-based, using KeywordIndex)
  □ Result caching (LRU cache for repeated queries)

Phase 3 additions:
  □ Learning-aware ranking (boost resources user hasn't seen)
  □ Difficulty-progression ranking (suggest next-difficulty resources)
  □ Prerequisite-aware filtering (don't show ADVANCED if BEGINNER not done)

Phase 4 additions:
  □ Web-augmented search (fallback to web when vault results are thin)
  □ Multi-source aggregation (vault + web + scraped → unified ranking)
  □ Deduplication across sources

Phase 5 additions:
  □ Collaborative filtering (users who liked X also liked Y)
  □ Content-based similarity (cosine similarity on tag vectors)
  □ Trending detection (which topics are hot right now)
  □ Personalized re-ranking (based on user's learning history)
```

#### Recommended Search Algorithm Stack

```text
┌─────────────────────────────────────────────────┐
│                 QUERY INPUT                       │
│  "java concurrency for intermediate"              │
└───────────────┬─────────────────────────────────┘
                ▼
┌─────────────────────────────────────────────────┐
│  PHASE 1: CLASSIFY                               │
│  KeywordQueryClassifier → VAGUE                  │
│  + Extract: topic=concurrency, lang=java,         │
│    level=intermediate                             │
└───────────────┬─────────────────────────────────┘
                ▼
┌─────────────────────────────────────────────────┐
│  PHASE 2: FILTER                                 │
│  FilterChain:                                    │
│    AND(category=JAVA, NOT(archived),             │
│        difficulty IN [BEGINNER..ADVANCED])        │
└───────────────┬─────────────────────────────────┘
                ▼
┌─────────────────────────────────────────────────┐
│  PHASE 3: SCORE                                  │
│  CompositeScorer (weighted):                     │
│    40% TextMatchScorer (title + description)     │
│    25% BM25Scorer (full-text relevance)          │
│    20% TagScorer (tag overlap)                   │
│    15% ConceptMatchScorer (concept area affinity)│
│  + Boosts:                                       │
│    +15 official source                           │
│    +10 actively maintained                       │
│    +12 language fit (Java)                       │
│    +10 difficulty fit (intermediate)             │
└───────────────┬─────────────────────────────────┘
                ▼
┌─────────────────────────────────────────────────┐
│  PHASE 4: RANK                                   │
│  ScoreRanker (sort by score desc)                │
│    .thenRank(RecencyBoostRanker)                 │
│    .thenRank(DiversityRanker)  ← NEW: ensure    │
│      result variety across types                 │
└───────────────┬─────────────────────────────────┘
                ▼
┌─────────────────────────────────────────────────┐
│  PHASE 5: TRIM + PRESENT                         │
│  Top 15 results                                  │
│  + Facet summary (by category, by difficulty)    │
│  + "Did you mean?" suggestions                   │
│  + Next steps recommendation                     │
└─────────────────────────────────────────────────┘
```

---

### Part 7 — Web Scraper & Processor Design

For the web scraping + processing pipeline you envision:

#### Architectural Layers

```text
┌────────────────────────────────────────┐
│         SOURCE REGISTRY                 │
│  (which sites to fetch from)            │
│  ├── Official docs (dev.java, MDN)      │
│  ├── Blogs (Baeldung, Real Python)      │
│  ├── GitHub (trending repos, READMEs)   │
│  ├── RSS feeds (InfoQ, DZone)           │
│  └── Search APIs (Google, Bing)         │
└──────────┬─────────────────────────────┘
           ▼
┌────────────────────────────────────────┐
│         FETCHER                         │
│  (HTTP client with safeguards)          │
│  ├── Rate limiter (per-domain)          │
│  ├── Robots.txt checker                 │
│  ├── Retry with exponential backoff     │
│  ├── Response caching (ETag/304)        │
│  └── Timeout management                 │
└──────────┬─────────────────────────────┘
           ▼
┌────────────────────────────────────────┐
│         EXTRACTOR                       │
│  (HTML → structured content)            │
│  ├── Title extraction                   │
│  ├── Main content detection             │
│  │   (remove nav, ads, sidebar)         │
│  ├── Code block extraction              │
│  ├── Author extraction                  │
│  ├── Date extraction                    │
│  └── Table-of-contents extraction       │
└──────────┬─────────────────────────────┘
           ▼
┌────────────────────────────────────────┐
│         CLASSIFIER                      │
│  (auto-detect metadata)                 │
│  ├── Topic classification               │
│  │   (KeywordIndex + TF-IDF on content) │
│  ├── Difficulty inference               │
│  │   (readability + vocabulary level)   │
│  ├── Resource type detection            │
│  │   (tutorial? doc? blog? video?)      │
│  ├── Language applicability             │
│  │   (Java-specific? universal?)        │
│  └── Quality score                      │
│      (freshness, author authority,      │
│       code-to-prose ratio)              │
└──────────┬─────────────────────────────┘
           ▼
┌────────────────────────────────────────┐
│         DEDUPLICATOR                    │
│  (prevent duplicate resources)          │
│  ├── URL normalization                  │
│  │   (strip params, resolve redirects)  │
│  ├── Content fingerprinting             │
│  │   (SimHash for near-duplicate        │
│  │    detection)                         │
│  └── Existing vault cross-check         │
└──────────┬─────────────────────────────┘
           ▼
┌────────────────────────────────────────┐
│         SUMMARIZER                      │
│  (create concise resource entry)        │
│  ├── Extract key points (top-3)         │
│  ├── Generate 2-sentence description    │
│  ├── Identify prerequisites             │
│  └── Assign to concept graph            │
└──────────┬─────────────────────────────┘
           ▼
┌────────────────────────────────────────┐
│         VAULT INGESTER                  │
│  (add to learning resource store)       │
│  ├── Create LearningResource record     │
│  ├── Add to in-memory vault             │
│  ├── Optionally persist to skills file  │
│  └── Log addition for audit             │
└────────────────────────────────────────┘
```

#### Recommended Java Package Structure

```text
web-processor/src/web/
├── fetcher/
│   ├── WebFetcher.java           ← Interface
│   ├── HttpWebFetcher.java       ← java.net.http.HttpClient implementation
│   ├── RateLimiter.java          ← Per-domain rate limiting
│   ├── RobotsChecker.java        ← Robots.txt parsing and checking
│   └── ResponseCache.java        ← ETag-based caching
├── extractor/
│   ├── ContentExtractor.java     ← Interface
│   ├── HtmlContentExtractor.java ← HTML → clean text
│   ├── MarkdownExtractor.java    ← GitHub README extraction
│   └── RssExtractor.java         ← RSS/Atom feed parsing
├── classifier/
│   ├── ResourceClassifier.java   ← Interface
│   ├── KeywordClassifier.java    ← Uses KeywordIndex for topic detection
│   ├── ReadabilityClassifier.java ← Flesch-Kincaid score → difficulty
│   └── TypeDetector.java         ← URL patterns → ResourceType
├── dedup/
│   ├── DeduplicationEngine.java  ← Interface
│   ├── UrlDeduplicator.java      ← URL normalization + lookup
│   └── ContentFingerprinter.java ← SimHash for content similarity
├── summarizer/
│   ├── ContentSummarizer.java    ← Interface
│   ├── ExtractiveSummarizer.java ← Key sentence extraction
│   └── DescriptionGenerator.java ← 2-sentence resource description
└── source/
    ├── WebSource.java            ← Interface
    ├── DocumentationSource.java  ← Official docs crawling
    ├── BlogAggregatorSource.java ← RSS/Atom aggregation
    ├── GitHubSource.java         ← GitHub API (trending, search)
    └── SourceRegistry.java       ← Manages all registered sources
```

---

### Part 8 — Recommended Configuration Strategy

For making the system configurable and extensible:

#### Configuration Approach: Layered Properties + Skills Instructions

```text
LAYER 1: Java Properties (runtime config)
  mcp-config.properties           ← Base defaults
  mcp-config.local.properties     ← User overrides (gitignored)
  Environment variables           ← Deployment overrides

  Examples:
    learning.vault.maxResults=15
    learning.search.scorerWeights.textMatch=0.40
    learning.search.scorerWeights.bm25=0.25
    learning.scraper.rateLimit.requestsPerSecond=2
    learning.scraper.respectRobotsTxt=true
    learning.paths.defaultDifficulty=BEGINNER

LAYER 2: Skills Markdown (content config)
  taxonomy-reference.md           ← Concept/category definitions
  SKILL.md                        ← LLM behavior instructions
  resources-*.md                  ← Curated resource data

LAYER 3: Agent Markdown (behavior config)
  learning-mentor.agent.md        ← Teaching methodology
  learning-researcher.agent.md    ← Research strategy

LAYER 4: Prompt Markdown (workflow config)
  resources.prompt.md             ← How /resources works
  learn-concept.prompt.md         ← How /learn-concept works
```

#### Plugin/Extension Pattern for Java

```java
// Strategy pattern — swap implementations at config time
interface ScoringStrategy<T> { int score(T item, SearchContext ctx); }
interface WebSource { List<ScrapedResource> fetch(String query, int maxResults); }
interface ContentExtractor { ExtractedContent extract(String html); }
interface ResourceClassifier { ClassifiedResource classify(ExtractedContent content); }

// RegisteredProvider pattern — add new providers without modifying core
interface ResourceProvider { List<LearningResource> resources(); }
// Just create a new class implementing ResourceProvider, register in BuiltInResources

// Configuration builder — wire components at startup
SearchEngineConfig<LearningResource> config = SearchEngineConfig.builder()
    .classifier(myClassifier)
    .scorer(VAGUE, myCompositeScorer)
    .ranker(myRanker)
    .maxResults(20)
    .build();
```

---

### Part 9 — Why Java is the Right Choice for the Backend

Given your comfort with Java and the existing codebase:

| Factor | Java | Python | TypeScript/Node | Verdict |
|---|---|---|---|---|
| **Your expertise** | Daily job | Familiar | Less familiar | Java |
| **Existing codebase** | 80+ Java files | 0 files | 0 files | Java |
| **Search engine** | Already built (26 files) | Would rebuild | Would rebuild | Java |
| **Type safety** | Full (enums, records, generics) | Optional (typing) | Full (TS) | Java/TS tie |
| **Concurrency** | Virtual threads (JDK 21+) | asyncio | async/await | Java |
| **HTTP client** | java.net.http (built-in) | requests/httpx | fetch/axios | All good |
| **HTML parsing** | jsoup (lightweight) | BeautifulSoup | cheerio | Python edge |
| **MCP SDK** | Custom (already built) | Official SDK | Official SDK | TS/Python edge |
| **Build system** | Custom scripts | pip | npm | TS/Python edge |
| **Performance** | JIT-compiled, fast | Interpreted, slower | V8, fast | Java |
| **Long-running server** | Excellent (JVM) | Good (uvicorn) | Good (Node) | Java |

**Decision:** Stay with Java. The investment in the existing codebase (search engine,
MCP server, model types) would be lost by switching. Java 21+ with virtual threads,
records, sealed classes, and pattern matching is modern enough for this use case.

---

### Part 10 — Final Recommendation Summary

#### The Architecture

**Layered Hybrid (Approach C + elements of D and G):**

```text
┌─────────────────────────────────────────────────────────────┐
│  USER INTERFACE                                              │
│  Copilot Chat → Prompts (/resources, /learn-concept, etc.)  │
└────────────────────────┬────────────────────────────────────┘
                         ▼
┌─────────────────────────────────────────────────────────────┐
│  INTELLIGENCE LAYER                                          │
│  Agents (learning-mentor, learning-researcher)               │
│  + Instructions (quality rules, taxonomy validation)         │
└───────┬──────────────────────────────────┬──────────────────┘
        ▼                                  ▼
┌──────────────────────┐    ┌────────────────────────────────┐
│  KNOWLEDGE LAYER     │    │  CAPABILITY LAYER              │
│  (Skills — static)   │    │  (MCP Server — dynamic)        │
│                      │    │                                │
│  learning-resources- │    │  search (vault + web)          │
│  vault/              │    │  scrape (URL processing)       │
│  java-learning-      │    │  fetch (dynamic discovery)     │
│  resources/          │    │  guidance (learning paths)     │
│  software-eng-       │    │  assessment (knowledge test)   │
│  resources/          │    │  export (Markdown/PDF)         │
│  career-resources/   │    │                                │
└──────────────────────┘    └──────────────┬─────────────────┘
                                           ▼
                            ┌────────────────────────────────┐
                            │  ENGINE LAYER                   │
                            │  (Java libraries — reusable)    │
                            │                                │
                            │  search-engine/ (generic)       │
                            │  web-processor/ (future)        │
                            │  learning-engine/ (future)      │
                            │  learning-model/ (if extracted)  │
                            └────────────────────────────────┘
```

#### The Phased Roadmap

| Phase | Name | Key Deliverables | When |
|---|---|---|---|
| **1** | Current State | ✅ Done — skills, MCP, search engine, agent, prompts | Complete |
| **2** | Revive MCP + Dynamic Fetch | Un-deprecate server, add web fetcher, fix SearchMode dupe | Next sprint |
| **3** | Learning Intelligence | Prerequisite graph, learning paths, progress tracking | After Phase 2 |
| **4** | Web Processing Pipeline | Full scraper → extractor → classifier → dedup → ingest | After Phase 3 |
| **5** | Full Learning Library | Recommendations, trending, spaced repetition, personalization | Long-term vision |

#### Which Copilot Primitive for What

| Primitive | Role in This System | Content |
|---|---|---|
| **Skills** | Static knowledge base | Curated resources, taxonomy, deep-reference content |
| **MCP Server** | Dynamic capabilities | Search, scrape, fetch, guidance, assessment |
| **Agents** | Teaching intelligence | learning-mentor, learning-researcher personas |
| **Prompts** | User entry points | Slash commands for each workflow |
| **Instructions** | Quality governance | Taxonomy validation, formatting rules |
| **copilot-instructions.md** | Project foundation | Coding standards, commit format, project structure |

#### The Tagging/Keyword Strategy

| Concern | Current Approach | Recommended Approach | When to Change |
|---|---|---|---|
| Core taxonomy | Java enums (97 values) | Keep enums — stable, type-safe | Never (until 300+ values) |
| Free-form tags | `List<String> tags` on records | Keep as-is — provides flexibility | Never |
| Keyword resolution | `KeywordIndex` (200+ entries) | Enhance with weights + multi-mapping | Phase 2 |
| Concept hierarchy | Flat enums + `ConceptDomain` grouping | Keep flat until Phase 3 (then consider graph) | Phase 3 |
| Category expansion | Enum values | Add new enum values as needed (recompile) | As needed |

---

## Key Outcomes

- Identified 7 distinct architectural approaches (A-G) for the learning resources module
- Scored all approaches across 12 evaluation dimensions (weighted)
- **Winner: Layered Hybrid (Approach C)** with score 73/85 — best power-to-complexity ratio
- **Aspirational: Full Platform (Approach G)** with score 80/85 — build incrementally
- Designed a 5-phase roadmap from current state to full learning library
- Detailed the search engine enhancement path (faceted search, spell-check, web-augmented)
- Designed the web scraper/processor architecture (6-stage pipeline)
- Confirmed Java as the right backend language (existing investment + daily expertise)
- Established that skills = static knowledge, MCP = dynamic capabilities, agents = teaching intelligence
- Recommended keeping enum-based taxonomy for keywords/tagging (type-safe, stable, good enough)
- Designed the configuration strategy (layered properties + skills + agents + prompts)

---

## Follow-Up / Next Steps

- [ ] Decision: Adopt the Layered Hybrid architecture (Approach C) as the target design
- [ ] Phase 2: Un-deprecate LearningResourcesServer, fix SearchMode duplication
- [ ] Phase 2: Add `FetchHandler` for dynamic web resource fetching
- [ ] Phase 2: Update `/resources` prompt to use both skills and MCP
- [ ] Phase 3: Design prerequisite graph for concept dependencies
- [ ] Phase 3: Implement `LearningPathEngine` for adaptive learning paths
- [ ] Phase 4: Create `web-processor` module with scraper pipeline
- [ ] Phase 5: Add recommendation engine and personalization
- [ ] Capture this session for architectural reference

---

## Session Metadata

| Property | Value |
|---|---|
| Duration | ~1 exchange (comprehensive analysis) |
| Files touched | None (design analysis only) |
| Related sessions | [Package restructuring analysis](2026-03-24_03-00pm_design_learningresources-package-restructuring-analysis.md), [MCP-to-skills migration](2026-03-22_02-00pm_design_learningresources-mcp-to-skills-migration.md) |
