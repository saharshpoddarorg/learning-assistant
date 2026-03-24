---
date: 2026-03-24
time: "03:00 PM"
kind: session-capture
domain: personal
category: design
project: learning-assistant
subject: learningresources-package-restructuring-analysis
tags: [project:learning-assistant, gh:saharshpoddarorg/learning-assistant, design, architecture, java, refactoring, package-structure]
status: draft
version: 1
parent: null
complexity: high
outcomes:
  - identified 6 distinct restructuring approaches for learning resources enums/model
  - compared all approaches across 6 dimensions (reuse, effort, risk, clarity, future, best-when)
  - recommended phased strategy (approach 3 short-term, approach 5 medium-term)
  - documented complete current package structure (57 Java files, 8 enums, 97 values)
  - confirmed skills migration is complete and no Java consumer exists outside mcp-servers
source: copilot
scope: project
scope-project: learning-assistant
scope-feature: null
scope-transitions: []
scope-refs:
  - file: "personal/software-dev/design/learningresources/2026-03-22_02-00pm_mcp-to-skills-migration.md"
    relationship: related
    note: "original migration design session — this session explores post-migration restructuring"
  - file: "personal/software-dev/design/learningresources/2026-03-22_05-30pm_migration-complete-audit.md"
    relationship: related
    note: "migration audit session that confirmed 138 resources migrated to skills"
---

# Learning Resources Package Restructuring — 6-Approach Analysis

> **Context:** With the learning resources MCP-to-skills migration complete (138 resources
> migrated to markdown skill files), the question arose: should the Java enums and model
> types currently nested under `server.learningresources.model` be extracted to a higher-level
> shared package for reuse, or should they stay in place? This session explores all viable
> restructuring approaches with full trade-off analysis.

---

## Request

Investigate and analyze all possible approaches for restructuring the learning resources
package — specifically whether the enums (`ConceptArea`, `ResourceCategory`, etc.) and
model types (`LearningResource`, `ResourceQuery`, etc.) currently under
`server.learningresources.model` should be extracted to a higher (above) level in the
package hierarchy for reuse in the skills approach and beyond.

Compare the best approaches with complete trade-offs.

---

## Analysis / Response

### Current State — Package Architecture

#### Complete File Tree

```text
mcp-servers/src/
├── Main.java
├── config/
│   ├── ConfigManager.java
│   ├── ConfigValidator.java
│   ├── package-info.java
│   ├── exception/
│   │   ├── ConfigLoadException.java
│   │   ├── ConfigValidationException.java
│   │   └── package-info.java
│   ├── loader/
│   │   ├── ConfigParser.java
│   │   ├── ConfigSource.java
│   │   ├── EnvironmentConfigSource.java
│   │   ├── PropertiesConfigSource.java
│   │   └── package-info.java
│   ├── model/
│   │   ├── ApiKeyStore.java
│   │   ├── BrowserPreferences.java
│   │   ├── LocationPreferences.java
│   │   ├── McpConfiguration.java
│   │   ├── ProfileDefinition.java
│   │   ├── ServerDefinition.java
│   │   ├── TransportType.java
│   │   ├── UserPreferences.java
│   │   └── package-info.java
│   └── validation/
│       ├── ConfigValidator.java
│       ├── ValidationResult.java
│       └── package-info.java
└── server/
    ├── McpServer.java                    [INTERFACE — all servers implement]
    ├── McpServerRegistry.java            [CENTRAL REGISTRY — manages servers]
    ├── package-info.java
    ├── atlassian/
    │   ├── AtlassianServerFactory.java
    │   ├── AtlassianServerVersion.java
    │   ├── package-info.java
    │   ├── common/
    │   │   ├── JsonExtractor.java
    │   │   └── package-info.java
    │   ├── v1/                           [v1 implementation]
    │   │   ├── AtlassianServerV1.java
    │   │   ├── config/
    │   │   ├── model/
    │   │   ├── client/
    │   │   ├── formatter/
    │   │   └── handler/
    │   └── v2/                           [v2 implementation]
    │       ├── AtlassianServerV2.java
    │       ├── auth/
    │       ├── config/
    │       ├── model/
    │       ├── client/
    │       └── handler/
    └── learningresources/                [FOCUS PACKAGE]
        ├── LearningResourcesServer.java
        ├── package-info.java
        ├── README.md
        ├── content/
        │   ├── ContentReader.java
        │   ├── ContentSummarizer.java
        │   ├── ReadabilityScorer.java
        │   └── package-info.java
        ├── handler/
        │   ├── ExportHandler.java
        │   ├── SearchHandler.java
        │   ├── ScrapeHandler.java
        │   ├── ToolHandler.java
        │   ├── UrlResourceHandler.java
        │   └── package-info.java
        ├── model/                        [ENUMS & DATA CLASSES — THE QUESTION]
        │   ├── ConceptArea.java          [40 enum values]
        │   ├── ConceptDomain.java        [9 enum values]
        │   ├── ContentFreshness.java     [5 enum values]
        │   ├── ContentSummary.java       [Record]
        │   ├── DifficultyLevel.java      [4 enum values]
        │   ├── LanguageApplicability.java [6 enum values]
        │   ├── LearningResource.java     [Record — 15 fields]
        │   ├── ResourceCategory.java     [17 enum values]
        │   ├── ResourceQuery.java        [Record]
        │   ├── ResourceType.java         [13 enum values]
        │   ├── SearchMode.java           [3 enum values — DUPLICATED]
        │   └── package-info.java
        ├── scraper/
        │   ├── ContentExtractor.java
        │   ├── ScraperException.java
        │   ├── ScraperResult.java
        │   ├── WebScraper.java
        │   └── package-info.java
        ├── search/
        │   ├── OfficialDocsSearchEngine.java
        │   └── package-info.java
        └── vault/
            ├── BuiltInResources.java     [Composes all 17 providers]
            ├── DiscoveryResult.java
            ├── KeywordIndex.java         [170+ keyword → enum mappings]
            ├── LearningSearchEngine.java
            ├── ResourceDiscovery.java
            ├── ResourceProvider.java     [Interface]
            ├── ResourceVault.java        [In-memory index]
            ├── RelevanceScorer.java
            ├── package-info.java
            └── providers/                [17 domain-specific providers]
                ├── AiMlResources.java
                ├── AlgorithmsResources.java
                ├── BuildToolsResources.java
                ├── CloudInfraResources.java
                ├── DataAndSecurityResources.java
                ├── DataStructuresResources.java
                ├── DevOpsResources.java
                ├── DigitalNotetakingResources.java
                ├── EngineeringResources.java
                ├── FrameworksResources.java
                ├── GeneralResources.java
                ├── JavaResources.java    [28 Java resources]
                ├── PythonResources.java
                ├── SelfDevelopmentResources.java
                ├── TestingToolsResources.java
                ├── VcsResources.java
                ├── WebResources.java
                └── package-info.java
```

#### Search-Engine Module (Peer Module)

```text
search-engine/src/
└── search/
    ├── api/                              [Generic contracts — ALREADY REUSABLE]
    │   ├── classify/
    │   │   ├── QueryClassifier.java
    │   │   └── SearchMode.java           [Separate from learningresources]
    │   ├── core/
    │   │   ├── SearchEngine.java
    │   │   ├── SearchContext.java
    │   │   ├── SearchResult.java
    │   │   ├── ScoredItem.java
    │   │   └── ScoreBreakdown.java
    │   ├── filter/
    │   │   └── SearchFilter.java
    │   ├── index/
    │   │   └── SearchIndex.java
    │   └── algorithm/
    │       ├── Tokenizer.java
    │       └── ScoringStrategy.java
    └── engine/
        ├── classify/
        │   └── KeywordQueryClassifier.java
        ├── core/
        │   └── ConfigurableSearchEngine.java
        ├── filter/
        │   └── FilterChain.java
        ├── index/
        │   ├── InMemoryIndex.java
        │   └── KeywordRegistry.java
        ├── rank/
        │   ├── ScoreRanker.java
        │   └── RecencyBoostRanker.java
        └── config/
            └── SearchEngineConfig.java
```

#### Skills Layer (Migrated Destination)

```text
.github/skills/learning-resources-vault/
├── SKILL.md                          ← Master index (138 resources)
├── migration-mapping.md              ← MCP→skill migration mapping
├── taxonomy-reference.md             ← Full enum taxonomy (97 values)
├── resources-java.md                 ← 20 Java resources
├── resources-web-javascript.md       ← 12 web/JS resources
├── resources-python.md               ← 6 Python resources
├── resources-algorithms-ds.md        ← 11 DSA resources
├── resources-software-engineering.md ← 11 SE resources
├── resources-devops-vcs-build.md     ← 25 DevOps/VCS/build resources
├── resources-cloud-infra.md          ← 15 cloud/infra/security resources
├── resources-ai-ml.md                ← 4 AI/ML resources
├── resources-productivity-pkm.md     ← 15 PKM/productivity resources
└── resources-general-career.md       ← 19 general/career resources
```

---

### Core Enum & Model Type Inventory

#### Enums (8 types, 97 total values)

| Enum | Values | Domain | Purpose |
|---|---|---|---|
| `ConceptArea` | 40 | Fine-grained CS/SE concepts | `LANGUAGE_BASICS`, `OOP`, `CONCURRENCY`, `DESIGN_PATTERNS`, `CI_CD`, ... |
| `ConceptDomain` | 9 | High-level groupings | `PROGRAMMING_FUNDAMENTALS`, `CORE_CS`, `SOFTWARE_ENGINEERING`, ... |
| `ResourceCategory` | 17 | Topic domains | `JAVA`, `PYTHON`, `WEB`, `DEVOPS`, `CLOUD`, `AI_ML`, ... |
| `ResourceType` | 13 | Content format | `DOCUMENTATION`, `TUTORIAL`, `BLOG`, `VIDEO`, `BOOK`, `COURSE`, ... |
| `DifficultyLevel` | 4 | Skill level | `BEGINNER`, `INTERMEDIATE`, `ADVANCED`, `EXPERT` |
| `ContentFreshness` | 5 | Maintenance status | `EVERGREEN`, `ACTIVELY_MAINTAINED`, `PERIODICALLY_UPDATED`, ... |
| `LanguageApplicability` | 6 | Language scope | `UNIVERSAL`, `MULTI_LANGUAGE`, `JAVA_CENTRIC`, `JAVA_SPECIFIC`, ... |
| `SearchMode` | 3 | Query classification | `SPECIFIC`, `VAGUE`, `EXPLORATORY` |

**Key detail:** Each `ConceptArea` value carries a `displayName` (kebab-case) and an
associated `ConceptDomain` (the high-level grouping). This hierarchy enables domain-level
filtering.

#### Records (4 types)

| Record | Fields | Purpose |
|---|---|---|
| `LearningResource` | 15 | Core entity — id, title, url, description, type, categories, conceptAreas, tags, author, difficulty, freshness, isOfficial, isFree, languageApplicability, addedAt |
| `ResourceQuery` | 11 | Search/filter criteria — searchText, type, categories, conceptArea, difficulty range, freshness, officialOnly, tags, freeOnly, maxResults |
| `DiscoveryResult` | 4 | Search result wrapper — searchMode, scored results, suggestions, summary |
| `ContentSummary` | 8 | Scraped content metadata — url, title, text, summary, wordCount, readingTime, difficulty, scrapedAt |

---

### Cross-Module Dependency Analysis

#### Dependency Direction

```text
search-engine (standalone, zero external deps)
    ↑
    │ (compiled into same classloader via build.ps1)
    │
mcp-servers (depends on search-engine)

brain (INDEPENDENT — no imports from server or search)
```

#### Key Import Chains Inside mcp-servers

```text
vault/providers/*.java
  → imports: model.LearningResource, model.ConceptArea, model.ResourceCategory,
             model.ResourceType, model.DifficultyLevel, model.ContentFreshness,
             model.LanguageApplicability, vault.ResourceProvider

vault/BuiltInResources.java
  → imports: model.LearningResource, all 17 providers

vault/KeywordIndex.java
  → imports: model.ConceptArea, model.ResourceCategory, model.DifficultyLevel

vault/ResourceVault.java
  → imports: model.LearningResource, model.ResourceQuery, model.ResourceCategory

vault/LearningSearchEngine.java
  → imports: model.ConceptArea, model.ConceptDomain, model.DifficultyLevel,
             model.LearningResource, model.ResourceCategory
  → imports: search.api.classify.SearchMode (FROM search-engine)
  → adapts between search.api.classify.SearchMode and server.learningresources.model.SearchMode

handler/*.java
  → imports: vault.ResourceVault, model.LearningResource, model.SearchMode
```

**Critical finding:** The `model` package is the **center** of the dependency graph —
everything depends on it, but it depends on nothing else. No circular dependencies.

#### SearchMode Duplication

Two separate `SearchMode` enums with identical values exist:

- `server.learningresources.model.SearchMode` — used by domain-specific vault code
- `search.api.classify.SearchMode` — used by generic search-engine API

`LearningSearchEngine` contains an adapter method that maps between them:

```java
private server.learningresources.model.SearchMode toLrSearchMode(final SearchMode mode) {
    return switch (mode) {
        case SPECIFIC    -> server.learningresources.model.SearchMode.SPECIFIC;
        case VAGUE       -> server.learningresources.model.SearchMode.VAGUE;
        case EXPLORATORY -> server.learningresources.model.SearchMode.EXPLORATORY;
    };
}
```

---

### How the Skills Approach Represents the Same Data

#### Translation Schema: Java Model → Markdown Table

| Java Field | Java Type | Skill Table Column | Markdown Format |
|---|---|---|---|
| `title` | String | Title | `[Name](url)` link |
| `url` | String | Title | Embedded in link |
| `type` | ResourceType enum | Type | Badge emoji (📖 📝 📚 🎥 🎓) |
| `difficulty` | DifficultyLevel enum | Diff | Badge emoji (🟢 🟡 🔴 ⚫) |
| `conceptAreas` | List\<ConceptArea\> | Concepts | Comma-separated display names |
| `author` | String | Author | Plain text |
| `freshness` | ContentFreshness enum | Fresh | Badge emoji (🔄 ♾️ 📅 📜 🗄️) |
| `isOfficial` | boolean | Off | ✅ or ➖ |
| `isFree` | boolean | Free | ✅ or 💰 |
| `languageApplicability` | LanguageApplicability enum | Lang | Badge emoji (☕ 🐍 🌐 🔤) |
| `id` | String | Tags Index | Slug reference |
| `description` | String | Tags Index | Summary text |
| `categories` | List\<ResourceCategory\> | Sub-file routing | File determines category |
| `tags` | List\<String\> | Tags Index | Comma-separated keywords |
| `addedAt` | Instant | Not shown | Omitted (all current) |

#### Enum Taxonomy → taxonomy-reference.md

All 97 enum values are fully documented in `taxonomy-reference.md`:

- Section 1: ConceptDomain (9 values) — table with display names and descriptions
- Section 2: ConceptArea (40 values) — grouped by domain, with slugs and descriptions
- Section 3: ResourceCategory (17 values) — with sub-file routing
- Section 4: DifficultyLevel (4 values) — ordinal scale with badge mapping
- Section 5: ResourceType (13 values) — with badge mapping
- Section 6: ContentFreshness (5 values) — with badge mapping
- Section 7: LanguageApplicability (6 values) — with transferability notes
- Section 8: SearchMode (3 values) — with query phrasing guide
- Keyword Discovery Index: 300+ keyword→enum mappings

#### Key Finding: No Code Generation

The skill files were **manually migrated**, not generated from Java code. There is no
synchronization mechanism between the Java enums and the markdown taxonomy. The skill
files are **self-contained** — they stand alone without referencing the Java types.

---

### The 6 Restructuring Approaches

---

### Approach 1: Extract `model` to a Top-Level Shared Package

Move the 8 enums + 4 records **OUT** of `server.learningresources.model` to a
top-level package like `learningresources.model` (peer to `server/`, `config/`).

#### Proposed Structure

```text
mcp-servers/src/
├── learningresources/
│   └── model/                ← SHARED: enums + records live here
│       ├── ConceptArea.java
│       ├── ConceptDomain.java
│       ├── ContentFreshness.java
│       ├── ContentSummary.java
│       ├── DifficultyLevel.java
│       ├── LanguageApplicability.java
│       ├── LearningResource.java
│       ├── ResourceCategory.java
│       ├── ResourceQuery.java
│       ├── ResourceType.java
│       └── SearchMode.java
├── server/
│   └── learningresources/
│       ├── vault/            ← imports learningresources.model.*
│       ├── handler/          ← imports learningresources.model.*
│       ├── content/
│       ├── scraper/
│       └── search/
├── config/
└── (search-engine compiled alongside)
```

#### Impact Analysis

- **Files affected:** ~30 files (all files that import `server.learningresources.model.*`)
- **Change type:** Package rename — find-replace all import statements
- **Build impact:** Recompile everything (same as today — single-pass compilation)

#### Trade-Off Assessment

| Dimension | Rating | Notes |
|---|---|---|
| Reusability | Medium | Reusable within mcp-servers but no cross-module benefit yet |
| Effort | Medium | Rename package in ~30 files, update all imports |
| Breaking change | Yes | All imports change; existing compiled output invalidated |
| Clarity | Good | Separates "what things are" (model) from "what the server does" |
| Future-proofing | Medium | Still inside mcp-servers module boundary |

#### Verdict

Cleaner separation but limited benefit if no other module will import these types.
It is moving deckchairs within the same module — the types are still only compilable
by including `mcp-servers/src/`.

---

### Approach 2: Extract to a Standalone `learning-model` Module

Create a **new top-level module** alongside `mcp-servers/`, `search-engine/`, `brain/`.

#### Proposed Structure

```text
learning-assistant/
├── learning-model/
│   ├── build.ps1             ← New build script
│   └── src/
│       └── learningresources/
│           └── model/
│               ├── ConceptArea.java
│               ├── ConceptDomain.java
│               ├── ContentFreshness.java
│               ├── DifficultyLevel.java
│               ├── LanguageApplicability.java
│               ├── LearningResource.java
│               ├── ResourceCategory.java
│               ├── ResourceQuery.java
│               ├── ResourceType.java
│               └── package-info.java
├── search-engine/            ← Could depend on learning-model
├── mcp-servers/              ← Depends on learning-model + search-engine
├── brain/
└── .github/skills/           ← Skills approach (markdown)
```

#### Build Implications

- `learning-model/build.ps1` compiles itself (zero deps, pure Java)
- `search-engine/build.ps1` stays unchanged (no learning-model dependency)
- `mcp-servers/build.ps1` adds `learning-model/src/` to its source list (like it already
  does with `search-engine/src/`)

#### Impact Analysis

- **New files:** `learning-model/build.ps1`, `learning-model/README.md`,
  `learning-model/src/` tree, `learning-model/learning-model.iml`
- **Modified files:** `mcp-servers/build.ps1` (add classpath), all imports in mcp-servers
- **Deleted files:** `mcp-servers/src/server/learningresources/model/*.java` (moved)

#### Trade-Off Assessment

| Dimension | Rating | Notes |
|---|---|---|
| Reusability | High | Any module can compile against it independently |
| Effort | High | New module, new build script, update mcp-servers build |
| Breaking change | Yes | Package rename + build restructure |
| Clarity | Excellent | Clear module boundaries, SRP at the module level |
| Future-proofing | High | Ready for new consumers (CLI tools, plugins, other servers) |

#### Verdict

The "textbook correct" answer for a growing multi-module project. However, this project
currently has **zero** other Java consumers of these types. Building infrastructure for
a hypothetical future consumer violates YAGNI (You Ain't Gonna Need It).

---

### Approach 3: Status Quo+ (Fix Duplication Only)

Keep the Java enums where they are. Accept that:

- The **MCP server** is the only Java consumer
- The **skills files** are the canonical LLM consumer
- `taxonomy-reference.md` is the **single source of documented truth** for the taxonomy

Only change: eliminate the `SearchMode` duplication.

#### Proposed Change

```text
DELETE:  server.learningresources.model.SearchMode
USE:    search.api.classify.SearchMode everywhere

UPDATE: LearningSearchEngine.java — remove adapter method, use search.api.classify.SearchMode directly
UPDATE: Any domain code referencing server.learningresources.model.SearchMode
```

#### Impact Analysis

- **Files affected:** 3-5 files (SearchMode.java deleted, LearningSearchEngine adapter
  removed, any handler references updated)
- **Risk:** Minimal — the two enums have identical values

#### Trade-Off Assessment

| Dimension | Rating | Notes |
|---|---|---|
| Reusability | Low (Java) / High (skills) | Java stays as-is; skills already working |
| Effort | **Minimal** | Delete one enum, update ~3-5 files |
| Breaking change | Tiny | One enum removed, one adapter eliminated |
| Clarity | Good enough | Model lives where it is used |
| Future-proofing | Low for Java reuse / High for LLM reuse | Skills solved the reuse problem |

#### Verdict

**Most pragmatic option.** Follows YAGNI. The skills approach already solved the reuse
problem for LLMs. Java refactoring only matters if there is a Java consumer to serve.
Removing the SearchMode duplication is a clean, low-risk improvement.

---

### Approach 4: Retire Java Model Entirely (Skills-Only)

Since skill files have **already migrated all 138 resources** and the MCP server's 10
tools are retired, fully deprecate the `server.learningresources` package.

#### Proposed Structure

```text
mcp-servers/src/server/
├── atlassian/              ← KEEP (active MCP server)
├── McpServer.java          ← KEEP (interface)
├── McpServerRegistry.java  ← KEEP (registry)
└── learningresources/      ← DEPRECATE / ARCHIVE
    └── (everything — 57 Java files)

.github/skills/learning-resources-vault/   ← THE sole source of truth
    ├── SKILL.md
    ├── taxonomy-reference.md
    └── resources-*.md
```

#### What Gets Archived/Deleted

| Component | Files | Action |
|---|---|---|
| Model (enums, records) | 12 | Archive |
| Vault (providers, search, index) | 27 | Archive |
| Handlers (MCP tool handlers) | 6 | Archive |
| Content (scraping) | 4 | Archive |
| Scraper (web scraping) | 5 | Archive |
| Search (bridge) | 2 | Archive |
| Server (LearningResourcesServer) | 1 | Archive |
| **Total** | **57** | Archive |

#### Trade-Off Assessment

| Dimension | Rating | Notes |
|---|---|---|
| Reusability | N/A | Nothing to reuse in Java anymore |
| Effort | Medium | Mark deprecated, stop compiling, update docs/README |
| Breaking change | **Major** | Removes 57 working Java files |
| Clarity | Excellent | One system, not two; eliminates dual-system confusion |
| Future-proofing | Depends | If you ever need programmatic access, you re-implement |

#### Verdict

The cleanest end-state if skills are the permanent strategy. Eliminates the dual-system
maintenance burden. But **destructive** — you lose 57 Java files of working, tested code.
Reversibility is low (must pull from git history to recover).

---

### Approach 5: Hybrid — Shared Enums + Skills-Only Content

Keep the **enums** (taxonomy) because they represent a reusable domain model. Move the
**resource content** (the 17 provider classes with 138 resources) to skills-only.

#### Proposed Structure

```text
mcp-servers/src/
├── learningresources/
│   └── model/              ← KEEP: enums + core records (taxonomy)
│       ├── ConceptArea.java        (40 concepts)
│       ├── ConceptDomain.java      (9 domains)
│       ├── ResourceCategory.java   (17 categories)
│       ├── DifficultyLevel.java    (4 levels)
│       ├── ResourceType.java       (13 types)
│       ├── ContentFreshness.java   (5 freshness markers)
│       ├── LanguageApplicability.java (6 applicability)
│       └── LearningResource.java   (core record)
├── server/
│   └── learningresources/
│       ├── vault/
│       │   ├── KeywordIndex.java   ← KEEP: programmatic keyword resolution
│       │   ├── ResourceVault.java  ← KEEP: if search engine needs it
│       │   ├── ResourceProvider.java ← KEEP: interface
│       │   └── providers/          ← ARCHIVE: content migrated to skills
│       ├── handler/                ← ARCHIVE: MCP tools retired
│       ├── content/                ← ARCHIVE: scraping retired
│       ├── scraper/                ← ARCHIVE: scraping retired
│       └── search/                 ← KEEP: bridges to search-engine
```

#### Rationale: Taxonomy vs. Content

| Aspect | Taxonomy (enums) | Content (providers) |
|---|---|---|
| Lifecycle | Long-lived — rarely changes | Frequently updated — resources added/removed |
| Consumers | Potentially many (Java + LLM) | LLM only (skills approach won) |
| Representation | Better as code (type safety) | Better as markdown (LLM reads directly) |
| Maintenance | Low (enums are stable) | High in Java (new class per domain) |

#### Trade-Off Assessment

| Dimension | Rating | Notes |
|---|---|---|
| Reusability | High for taxonomy, N/A for content | Enums available for future Java consumers |
| Effort | Medium | Partial deletion, partial relocation |
| Breaking change | Medium | Provider classes removed, model stays |
| Clarity | Good | Clear separation of taxonomy vs. content |
| Future-proofing | **High** | Taxonomy is long-lived; content delivery can evolve |

#### Verdict

A balanced middle ground. Recognizes that **taxonomy (enums) and content (resources)
have different lifecycles**. Enums are structural; resources are data. Different concerns
deserve different homes. This keeps the valuable type-safe taxonomy while embracing the
skills approach for content delivery.

---

### Approach 6: Code-Gen Bridge (Java → Markdown)

Keep the Java model as the **single source of truth** and auto-generate the skill
markdown tables from it via a build step.

#### Proposed Architecture

```text
mcp-servers/src/server/learningresources/  ← SOURCE OF TRUTH
    ├── model/        (enums — authoritative taxonomy)
    └── vault/providers/  (resources — authoritative content)
            │
            ▼
    [build step: ExportToMarkdown.java or codegen.ps1]
            │
            ▼
.github/skills/learning-resources-vault/   ← GENERATED OUTPUT
    ├── resources-*.md  (auto-generated from providers)
    └── taxonomy-reference.md (auto-generated from enums)
```

#### Implementation Options

**Option A: Java main class**

```java
// ExportToMarkdown.java — runs during build
public class ExportToMarkdown {
    public static void main(String[] args) {
        var resources = BuiltInResources.all();
        // Group by category, emit markdown tables
        // Generate taxonomy-reference.md from enum values
    }
}
```

**Option B: PowerShell reflection**

```powershell
# codegen.ps1 — parses Java source files, emits markdown
# More brittle but doesn't require compilation
```

#### Trade-Off Assessment

| Dimension | Rating | Notes |
|---|---|---|
| Reusability | High | Both Java and LLM get the same data |
| Effort | **High** | Build a code generator, integrate into build pipeline |
| Breaking change | Medium | Adds tooling, does not remove anything |
| Clarity | Good | Obvious who is authoritative (Java model) |
| Future-proofing | High | Single source of truth, multiple output formats |

#### Verdict

Engineering-heavy for a learning project. Appropriate if you expect frequent changes
where synchronization becomes a real pain point. Overkill if the resource set is
relatively stable (which it currently is — changes are infrequent after initial migration).

---

### Comprehensive Decision Matrix

| Approach | Reuse | Effort | Risk | Clarity | Future | Best When |
|---|---|---|---|---|---|---|
| 1. Extract to top-level pkg | Med | Med | Med | Good | Med | Want cleaner boundaries within one module |
| 2. Standalone module | High | High | Med | Excellent | High | Multiple Java consumers planned |
| **3. Status quo+ (fix dupes)** | Low | **Min** | **Low** | Good | Low-Java | **No new Java consumers planned** |
| 4. Retire Java entirely | N/A | Med | **High** | Excellent | Depends | Skills approach is permanent; Java is dead weight |
| **5. Hybrid (keep enums, skill content)** | High | Med | Med | Good | **High** | Taxonomy is valuable; content delivery settled |
| 6. Code-gen bridge | High | **High** | Med | Good | High | Frequent changes; need perfect sync |

---

### Skills Approach vs. MCP Server Approach — Why Skills Won

| Aspect | MCP Tools + Java | Markdown Skills | Winner |
|---|---|---|---|
| Token cost | ~2000 tokens per tool call | 0 extra tokens (in-context) | Skills |
| Latency | JSON-RPC round trip | Instant (LLM reads context) | Skills |
| Works in Chat | Yes | Yes | Tie |
| Works in Inline completions | No (MCP unavailable) | Yes (skill in context) | Skills |
| Works in Terminal | No | Yes | Skills |
| Maintenance | Java code + compile + version | Markdown edit + Git | Skills |
| Runtime dependency | JDK + running server process | None | Skills |
| Search intelligence | Coded 12-factor algorithm | LLM native reasoning | Skills |
| Extensibility | Add Java class + register | Add Markdown row | Skills |
| Version control diffs | Java diffs (harder to review) | Markdown diffs (readable) | Skills |

#### From 12-Factor Algorithm to LLM Native Reasoning

The MCP server used a 12-factor weighted scoring algorithm:

```text
Scoring weights (RelevanceScorer.java):
  exactTitleMatch:    100
  partialTitleMatch:   40
  descriptionMatch:    20
  tagMatch:            15
  conceptMatch:        25
  categoryMatch:       20
  domainAffinity:      10
  officialBoost:       15
  freshnessBoost:      10
  difficultyFit:       10
  languageFit:         12
  fuzzyMatch:           8
```

The LLM applies equivalent reasoning **natively** by reading badge columns:

- 🟢 difficulty badge = beginner filter
- ✅ official badge = authority signal
- 🔄 freshness badge = maintenance signal
- ☕ language badge = Java-specific filter

No code needed — the LLM does this through language understanding.

---

### Phased Recommendation

#### Short-Term (Now): Approach 3 — Status Quo+

**Action:** Fix the `SearchMode` duplication only.

- Delete `server.learningresources.model.SearchMode`
- Update `LearningSearchEngine.java` to use `search.api.classify.SearchMode` directly
- Update any handler code referencing the old SearchMode
- ~3-5 files affected, minimal risk

**Rationale:** The skills migration is complete and working. There is no Java consumer
waiting for extracted enums. Fixing the duplication is the only concrete improvement
that addresses a real problem today.

#### Medium-Term (When Adding Another Feature): Approach 5 — Hybrid

**Trigger:** When you build another module or feature that needs the domain taxonomy
(e.g., a CLI tool, a different MCP server, a Gradle plugin).

**Action:** Extract the **model classes only** to `learningresources.model` at the top
level of `mcp-servers/src/` (or into their own `learning-model/` module). Do NOT extract
the providers/handlers — they are MCP-server-specific and effectively retired.

**Rationale:** Taxonomy (enums) is long-lived and structurally valuable. Content (providers)
has been replaced by skills. Different lifecycles, different homes.

#### Avoid

- **Approach 4 (full retirement):** Do not delete working code prematurely. The Java
  implementation serves as reference architecture and could be revived.
- **Approach 6 (code-gen):** Over-engineering for a learning project with stable,
  infrequent resource changes.

---

## Key Outcomes

- Identified 6 distinct restructuring approaches for learning resources enums/model types
- Compared all approaches across 6 dimensions with full trade-off analysis
- Confirmed skills migration is complete: 138 resources, 97 enum values, 300+ keywords
  all documented in markdown skill files
- Found `SearchMode` duplication between `server.learningresources.model` and
  `search.api.classify` — the only concrete problem worth fixing now
- Confirmed no module outside `mcp-servers` imports learning resources types
- Recommended phased strategy: Approach 3 (fix dupes now) → Approach 5 (hybrid when needed)

---

## Follow-Up / Next Steps

- [ ] Fix `SearchMode` duplication (Approach 3) — delete `server.learningresources.model.SearchMode`, update 3-5 files
- [ ] Decide on long-term strategy: fully retire Java learning resources or keep as reference architecture
- [ ] If keeping Java model: ensure `taxonomy-reference.md` stays in sync when enums change
- [ ] Consider approach 5 (hybrid) if/when a new Java consumer for the taxonomy emerges

---

## Session Metadata

| Property | Value |
|---|---|
| Duration | ~2 exchanges |
| Files touched | None (analysis only) |
| Related sessions | [MCP-to-skills migration design](2026-03-22_02-00pm_mcp-to-skills-migration.md), [Migration audit](2026-03-22_05-30pm_migration-complete-audit.md) |
