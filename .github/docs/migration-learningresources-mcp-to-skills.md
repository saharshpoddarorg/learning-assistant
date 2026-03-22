---
date: 2026-03-22
kind: migration-guide
project: learning-assistant
subject: learningresources-mcp-to-skills-migration-guide
tags: [migration, mcp-server, skill, learning-resources, guide]
status: completed
version: 1
---

# Migration Guide — Learning Resources MCP Server to Skills Approach

> **Intent document:** [Intent Capture](../sessions/personal/software-dev/design/2026-03-22_02-00pm_design_learningresources-mcp-to-skills-migration.md)
>
> **Purpose:** Step-by-step guide to migrate the Learning Resources MCP server (138 resources,
> 10 tools, 17 providers, 28 Java files) to a SKILL.md-based approach without losing any
> content, mechanism, or discoverability.
>
> **Post-migration reference:**
>
> - [taxonomy-reference.md](../../.github/skills/learning-resources-vault/taxonomy-reference.md) — complete enum taxonomy (92 values), keyword index (300+ mappings), scoring weights
> - [migration-mapping.md](../../.github/skills/learning-resources-vault/migration-mapping.md) — component-by-component mapping from MCP to skill

---

## Table of Contents

- [Executive Summary](#executive-summary)
- [Architecture: Before & After](#architecture-before--after)
- [Phase 1 — Skill File Architecture](#phase-1--skill-file-architecture)
- [Phase 2 — Resource Migration (132 Resources)](#phase-2--resource-migration-132-resources)
- [Phase 3 — Taxonomy Migration](#phase-3--taxonomy-migration)
- [Phase 4 — Prompt & Agent Updates](#phase-4--prompt--agent-updates)
- [Phase 5 — Documentation Updates](#phase-5--documentation-updates)
- [Phase 6 — Deprecation](#phase-6--deprecation)
- [Phase 7 — Verification](#phase-7--verification)
- [Appendix A — Complete Resource Catalog](#appendix-a--complete-resource-catalog)
- [Appendix B — Capability Mapping](#appendix-b--capability-mapping)
- [Appendix C — Token Budget Analysis](#appendix-c--token-budget-analysis)

---

## Executive Summary

### Why Migrate

| Problem with MCP Approach | How Skills Solve It |
|---|---|
| 132 resources are **hardcoded Java records** — static data disguised as a service | Skills are **designed for static reference knowledge** |
| Available only in **Agent mode** (MCP tools not in Ask/Edit) | Skills auto-activate in **ALL modes** |
| Requires **JVM process**, compilation, STDIO transport | Skills are **text files** — zero infrastructure |
| 12-dimensional scoring engine **reimplements LLM capabilities** | LLM **natively** does semantic search, ranking, filtering |
| 28 Java source files + build system for what is essentially a catalog | A skill folder achieves the same with **Markdown tables** |
| Changes require **recompile + restart** | Editing a `.md` file takes **instant** effect |

### What Gets Lost (and Why It's Acceptable)

| Lost Capability | Mitigation |
|---|---|
| Web scraping (`scrape_url`, `read_url`) | Agent's `fetch` tool covers URL retrieval; for HTML extraction, use `fetch` + LLM parsing |
| PDF/Word export | Copilot generates Markdown natively; user can copy-paste to any format tool |
| Runtime resource addition | Edit the skill `.md` file directly — simpler workflow |
| Structured JSON responses | LLM formats output from skill context — tables, badges, lists |

### What Gets Better

| Improved Aspect | Before (MCP) | After (Skill) |
|---|---|---|
| Availability | Agent mode only | Ask + Edit + Agent |
| Latency | JVM startup + JSON-RPC | Instant (text in context) |
| Maintenance | Java code → recompile → restart | Edit Markdown → done |
| Portability | 28 Java files + build system | 1 folder of `.md` files |
| Discoverability | Must invoke tool explicitly | Auto-activates by semantic match |
| Integration with other skills | Isolated process | Stacks with all other skills |

---

## Architecture: Before & After

### Before (Current MCP Architecture)

```text
User asks about learning resources
    ↓
/resources prompt → selects Learning-Mentor agent
    ↓
Agent calls MCP tools (search_resources, browse_vault, discover_resources, etc.)
    ↓
LearningResourcesServer (JVM process, STDIO)
    ↓
ToolHandler → SearchHandler → ResourceVault → 17 Providers
    ↓
ResourceDiscovery → KeywordIndex → RelevanceScorer
    ↓
JSON-RPC response back to agent
    ↓
Agent formats and presents to user
```

### After (Skills Architecture)

```text
User asks about learning resources
    ↓
VS Code detects semantic match → auto-loads learning-resources-vault skill
    ↓
Skill content (resource tables + taxonomy) injected into LLM context
    ↓
LLM natively searches, filters, ranks resources from context
    ↓
If user invokes /resources → prompt adds workflow structure
    ↓
If agent mode → Learning-Mentor persona adds teaching style
    ↓
Response formatted with badges, tables, and recommendations
```

### File Structure — After Migration

```text
.github/skills/learning-resources-vault/
├── SKILL.md                          ← Main skill: description, taxonomy reference,
│                                        quick lookup index, activation triggers
├── resources-java.md                 ← Java resources (17)
├── resources-web-javascript.md       ← Web/JS/TS resources (5 + frameworks 11)
├── resources-python.md               ← Python resources (4 + Python frameworks)
├── resources-algorithms-ds.md        ← Algorithms (3) + Data Structures (8)
├── resources-software-engineering.md ← Engineering (6) + Testing (5)
├── resources-devops-vcs-build.md     ← DevOps (6) + VCS (9) + Build Tools (11)
├── resources-cloud-infra.md          ← Cloud (7) + Security/Databases (8)
├── resources-ai-ml.md                ← AI/ML resources (4)
├── resources-productivity-pkm.md     ← Digital Notetaking (15)
└── resources-general-career.md       ← General (5) + Self-Development (13)
```

**Why sub-files?** Token budget. 132 resources at ~80 tokens each = ~10,500 tokens.
VS Code loads the main SKILL.md always, and loads sub-files when the topic matches.
Each sub-file stays under 2,000 tokens for efficient loading.

---

## Phase 1 — Skill File Architecture

### Step 1.1: Create the Skill Directory

```text
.github/skills/learning-resources-vault/
```

### Step 1.2: Create the Main SKILL.md

The main file should contain:

1. **YAML frontmatter** with comprehensive `description` field (activation trigger)
2. **Quick index** — summary table of all resource domains and counts
3. **Taxonomy reference** — the 9 concept domains and their concept areas
4. **How to use** — guide for the LLM on how to search and present resources
5. **Cross-references** to sub-files for detailed resource tables

**Critical:** The `description` field is the **activation trigger**. It must include every
keyword that a user might type when looking for learning resources. Model it after the MCP
server's KeywordIndex — every keyword that was mapped there should appear in the description.

### Step 1.3: Template for Main SKILL.md

```markdown
---
name: learning-resources-vault
description: >
  Curated vault of 132+ learning resources covering Java, Python, JavaScript/TypeScript,
  web development, frameworks (Spring, React, Angular, Vue, Django, Flask, Next.js),
  algorithms, data structures, software engineering, design patterns, clean code,
  system design, DevOps, CI/CD, Docker, Kubernetes, Git, version control, build tools
  (Maven, Gradle, Bazel, npm), cloud platforms (AWS, GCP, Azure), infrastructure as code
  (Terraform, Ansible), databases (PostgreSQL, MongoDB, Redis, Elasticsearch, Kafka),
  security (OWASP), testing (JUnit, Mockito, Testcontainers, Cypress, pytest, Selenium),
  AI/ML (fast.ai, OpenAI, prompt engineering, neural networks), digital note-taking
  (Obsidian, Notion, Logseq, Zettelkasten, PARA, GTD), personal development, career
  resources, and general CS education. Each resource includes: title, URL, description,
  type (documentation/tutorial/book/video/interactive/repository), difficulty
  (beginner/intermediate/advanced/expert), categories, concept areas, tags, author,
  freshness, official/community, free/paid, and language applicability.
  Use when asked about learning resources, tutorials, documentation, books, courses,
  where to learn [topic], best resources for [topic], beginner/advanced resources,
  official docs, free resources, study plan, learning path, or resource recommendations.
---
```

### Step 1.4: Sub-File Naming Convention

Each sub-file follows the pattern `resources-<domain>.md`. Sub-files do NOT have YAML
frontmatter — they are plain Markdown referenced from the main SKILL.md.

---

## Phase 2 — Resource Migration (132 Resources)

### Step 2.1: Resource Table Format

Every resource table uses this column structure to preserve ALL metadata:

```markdown
| Title | URL | Type | Difficulty | Categories | Concepts | Author | Fresh | Off | Free | Lang |
|---|---|---|---|---|---|---|---|---|---|---|
```

**Column key:**

| Column | Source Field | Format |
|---|---|---|
| Title | `title` | Linked: `[Title](url)` |
| URL | `url` | Embedded in title link |
| Type | `type` | Emoji + label (see below) |
| Difficulty | `difficulty` | Emoji badge |
| Categories | `categories` | Comma-separated |
| Concepts | `conceptAreas` | Comma-separated |
| Author | `author` | Plain text |
| Fresh | `freshness` | Short code |
| Off | `isOfficial` | ✅ or ➖ |
| Free | `isFree` | ✅ or 💰 |
| Lang | `languageApplicability` | Short code |

### Step 2.2: Badge System (Emoji Encoding)

**Difficulty:**

| Level | Badge |
|---|---|
| BEGINNER | 🟢 Beginner |
| INTERMEDIATE | 🟡 Intermediate |
| ADVANCED | 🔴 Advanced |
| EXPERT | ⚫ Expert |

**Type:**

| Type | Badge |
|---|---|
| DOCUMENTATION | 📖 Docs |
| TUTORIAL | 📝 Tutorial |
| BLOG | 📰 Blog |
| ARTICLE | 📄 Article |
| VIDEO | 🎥 Video |
| PLAYLIST | 🎬 Playlist |
| VIDEO_COURSE | 🎓 Course |
| BOOK | 📚 Book |
| INTERACTIVE | 🎮 Interactive |
| COURSE | 🏫 Course |
| API_REFERENCE | 📋 API Ref |
| CHEAT_SHEET | 📋 Cheatsheet |
| REPOSITORY | 💻 Repo |

**Freshness:**

| Freshness | Code |
|---|---|
| EVERGREEN | ♾️ |
| ACTIVELY_MAINTAINED | 🔄 |
| PERIODICALLY_UPDATED | 📅 |
| HISTORICAL | 📜 |
| ARCHIVED | 🗄️ |

**Language Applicability:**

| Applicability | Code |
|---|---|
| UNIVERSAL | 🌐 |
| MULTI_LANGUAGE | 🔤 |
| JAVA_CENTRIC | ☕→ |
| JAVA_SPECIFIC | ☕ |
| PYTHON_SPECIFIC | 🐍 |
| WEB_SPECIFIC | 🌐✦ |

### Step 2.3: Migration by Sub-File

Each sub-file groups resources under clear section headings.

**Example — `resources-java.md`:**

```markdown
# Java Learning Resources (17)

> Official documentation, community tutorials, books, and tools for Java development.

## Official Oracle / OpenJDK

| Title | Type | Diff | Concepts | Author | Fresh | Off | Free | Lang |
|---|---|---|---|---|---|---|---|---|
| [The Java Tutorials](https://dev.java/learn/) | 📝 Tutorial | 🟢 | Language basics, OOP, Generics, FP | Oracle / dev.java | 🔄 | ✅ | ✅ | ☕ |
| [JDK API Docs (Javadoc)](https://docs.oracle.com/en/java/javase/21/docs/api/) | 📋 API Ref | 🟡 | Language features, Concurrency, DS | Oracle | 🔄 | ✅ | ✅ | ☕ |
(... continue for all 17 Java resources ...)

## Tags Index

java, oracle, jdk, spring, spring-boot, concurrency, jvm, effective-java,
baeldung, jenkov, junit, guava, sdkman, temurin, jdk25, migration
```

### Step 2.4: Provider-to-SubFile Mapping

| Sub-File | Providers Merged | Resource Count |
|---|---|---|
| `resources-java.md` | JavaResources (17) | 17 |
| `resources-web-javascript.md` | WebResources (5) + FrameworksResources web portion (7) | 12 |
| `resources-python.md` | PythonResources (4) + FrameworksResources python portion (2) + Python-specific from others | 6 |
| `resources-algorithms-ds.md` | AlgorithmsResources (3) + DataStructuresResources (8) | 11 |
| `resources-software-engineering.md` | EngineeringResources (6) + TestingToolsResources (5) | 11 |
| `resources-devops-vcs-build.md` | DevOpsResources (6) + VcsResources (9) + BuildToolsResources (11) | 26 |
| `resources-cloud-infra.md` | CloudInfraResources (7) + DataAndSecurityResources (8) | 15 |
| `resources-ai-ml.md` | AiMlResources (4) | 4 |
| `resources-productivity-pkm.md` | DigitalNotetakingResources (15) | 15 |
| `resources-general-career.md` | GeneralResources (5) + SelfDevelopmentResources (13) | 18 |
| **TOTAL** | **17 providers** | **135** |

Note: Some FrameworksResources entries (Spring, Hibernate = Java-specific; Django, Flask =
Python-specific; React, Angular, Vue, Next.js, Node.js, Express = Web-specific) are distributed
to the language-appropriate sub-file. Counts may adjust slightly due to this redistribution —
verify total stays at 132 after redistribution.

### Step 2.5: Verification Checklist per Sub-File

For each sub-file, verify:

- [ ] Every resource from the source provider(s) is present
- [ ] All 14 metadata fields are represented (id implicit in title link)
- [ ] URLs are correct and not duplicated across sub-files
- [ ] Difficulty badges match the source enum value
- [ ] Type badges match the source enum value
- [ ] Official/Free flags match the source boolean values
- [ ] Tags from the source are preserved (as a tags index section or inline)
- [ ] Description text is preserved (as hover text, inline description, or separate column)

---

## Phase 3 — Taxonomy Migration

### Step 3.1: ConceptDomain → Top-Level Sections in Main SKILL.md

```markdown
## Resource Domains

| Domain | Concept Areas | Sub-File |
|---|---|---|
| Programming Fundamentals | Language basics, OOP, FP, Language features, Generics | java, web, python |
| Core CS | Concurrency, Data structures, Algorithms, Math, Complexity, Memory | algorithms-ds |
| Software Engineering | Design patterns, Clean code, Testing, API design, Architecture | software-engineering |
| System Design | System design, Databases, Distributed systems, Networking, OS | cloud-infra |
| DevOps & Tooling | CI/CD, Containers, Version control, Build tools, Infra, Observability | devops-vcs-build |
| Security | Web security, Cryptography | cloud-infra |
| AI & Data | Machine learning, Deep learning, LLM & Prompting | ai-ml |
| Career & Meta | Interview prep, Career development, Getting started, PKM | general-career |
| Personal Development | Self-improvement, Communication, Financial literacy, Productivity | general-career |
```

### Step 3.2: DifficultyLevel → Selection Guide

Include in main SKILL.md:

```markdown
## Difficulty Guide

| Level | Badge | Meaning | Example |
|---|---|---|---|
| Beginner | 🟢 | No prior knowledge required | The Java Tutorials, Docker Get Started |
| Intermediate | 🟡 | Basic knowledge assumed | Baeldung, Kubernetes Docs, Spring Boot |
| Advanced | 🔴 | Solid understanding required | JCIP, System Design Primer, Terraform |
| Expert | ⚫ | Research/formal specs | JLS, JVM Specification, academic papers |
```

### Step 3.3: ResourceType → Type Legend

```markdown
## Resource Types

| Type | Badge | Description |
|---|---|---|
| Documentation | 📖 | Official reference docs |
| Tutorial | 📝 | Step-by-step learning |
| Blog | 📰 | Articles and opinion pieces |
| Book | 📚 | Full-length books |
| Video | 🎥 | Individual video content |
| Playlist | 🎬 | Curated video series |
| Video Course | 🎓 | Structured course with assessments |
| Interactive | 🎮 | Hands-on, in-browser learning |
| API Reference | 📋 | API documentation |
| Cheat Sheet | 📋 | Quick reference cards |
| Repository | 💻 | GitHub repos and open-source collections |
```

---

## Phase 4 — Prompt & Agent Updates

### Step 4.1: Update `/resources` Prompt

The current [resources.prompt.md](.github/prompts/resources.prompt.md) references MCP tools
(`search_resources`, `browse_vault`, etc.). Update to reference the skill instead.

**Changes needed:**

1. Remove `tools: ['codebase', 'fetch']` — or keep `fetch` for URL scraping fallback
2. Update "Available Tools (10 MCP tools)" section → "Available Knowledge (Skill-Based)"
3. Replace MCP tool invocation instructions with instructions to search the skill's tables
4. Keep the action modes (search, browse, discover, recommend, details, export)
5. Keep the display format (badges, icons, difficulty indicators)
6. Keep cross-references to other commands
7. Add instruction: "The learning-resources-vault skill contains 132 curated resources.
   Search the skill's sub-files for matching resources by reading the tables."

**Key changes in each action mode:**

| Action | Before (MCP) | After (Skill) |
|---|---|---|
| search | Call `search_resources` tool | Search skill tables in context by keyword |
| browse | Call `browse_vault` tool | Read skill sub-file section headings |
| discover | Call `discover_resources` tool | Apply semantic understanding to skill tables |
| scrape | Call `scrape_url` tool | Use `fetch` tool (if in agent mode) |
| recommend | Call `discover_resources` + format | Read skill tables + apply teaching philosophy |
| add | Call `add_resource` tool | Instruct user to edit the skill file |
| details | Call `get_resource` tool | Find specific entry in skill tables |
| export | Call `export_results` tool | Generate Markdown output from skill context |

### Step 4.2: Update Learning-Mentor Agent (if needed)

The [learning-mentor.agent.md](.github/agents/learning-mentor.agent.md) currently doesn't
reference MCP tools explicitly — it uses `tools: ['search', 'codebase', 'usages', 'fetch',
'findTestFiles', 'terminalLastCommand']`. No changes needed to the agent itself.

### Step 4.3: Handle Existing Skill Overlap

Two existing skills overlap with the vault resources:

| Existing Skill | Overlap | Resolution |
|---|---|---|
| `java-learning-resources` | Java resources + deep-reference sub-files | Keep as deep-reference skill (scraped content); vault skill provides the catalog index |
| `software-engineering-resources` | SE books, patterns, methodologies | Keep as curated reading list; vault skill provides structured resource metadata |

**Resolution strategy:** The new `learning-resources-vault` skill focuses on **structured
resource discovery** (metadata-rich tables). The existing skills provide **deep reference
content** (full explanations, book summaries, methodology descriptions). They complement
each other — the vault helps you **find** resources; the existing skills help you **learn**
from them.

Add cross-references between skills:

```markdown
> **Deep references:** For in-depth Java content (scraped tutorials, API deep-dives),
> see the `java-learning-resources` skill. For SE methodology details, see the
> `software-engineering-resources` skill. This vault provides the discovery index.
```

---

## Phase 5 — Documentation Updates

### Step 5.1: Files to Update

| File | Change |
|---|---|
| `.github/docs/slash-commands.md` | Update `/resources` entry: remove MCP tool references, note skill-based approach |
| `mcp-servers/README.md` | Add deprecation notice for Learning Resources server |
| `USAGE.md` | Update Learning Resources section for skill-based usage |
| `README.md` (root) | Update if resource count or architecture mentioned |
| `.github/prompts/hub.prompt.md` | Update if `/resources` description changed |

### Step 5.2: KeywordIndex → Skill Description Mapping

Every keyword from `KeywordIndex.java` must appear in the skill's `description` field
or in the sub-file content. This ensures semantic activation matches the same queries
the MCP server handled.

**Concept keywords (must appear in skill description or content):**

```text
oop, functional, generics, concurrency, data-structures, algorithms, design-patterns,
clean-code, testing, api, architecture, system-design, databases, distributed-systems,
networking, operating-systems, ci-cd, containers, docker, kubernetes, version-control,
git, build-tools, maven, gradle, infrastructure, terraform, observability, security,
owasp, cryptography, machine-learning, deep-learning, llm, prompting, interview,
career, getting-started, knowledge-management, self-improvement, communication,
finance, productivity
```

**Category keywords:**

```text
java, python, javascript, typescript, web, database, devops, cloud, aws, gcp, azure,
algorithms, software-engineering, testing, security, ai, ml, tools, productivity,
general, personal-development
```

**Difficulty keywords:**

```text
beginner, new, start, easy, intro, basic, intermediate, moderate, mid,
advanced, deep, hard, expert, master
```

---

## Phase 6 — Deprecation

### Step 6.1: Mark MCP Server as Deprecated

Add a deprecation notice to `mcp-servers/src/server/learningresources/LearningResourcesServer.java`:

```java
/**
 * @deprecated The Learning Resources MCP server is deprecated in favor of the
 * learning-resources-vault skill (.github/skills/learning-resources-vault/SKILL.md).
 * The skill provides the same 132 curated resources with zero infrastructure,
 * available in all Copilot modes (Ask, Edit, Agent).
 *
 * Web scraping capabilities are covered by the agent's fetch tool.
 * See: brain/ai-brain/sessions/personal/software-dev/design/
 *      2026-03-22_02-00pm_design_learningresources-mcp-to-skills-migration.md
 */
```

### Step 6.2: Disable in MCP Config

In `.vscode/mcp.json`, set the learning-resources server to `"disabled": true`:

```jsonc
"learning-resources": {
  "disabled": true,
  // ... existing config
}
```

### Step 6.3: Preserve the Code (Do NOT Delete Yet)

Keep the Java source files in place for:

1. **Reference implementation** — valuable for learning MCP server development
2. **Rollback option** — if the skill approach has unforeseen issues
3. **Future reintroduction** — if web scraping or export features are needed later

### Step 6.4: Update Server Documentation

In `mcp-servers/README.md`, add a section:

```markdown
## Deprecated: Learning Resources Server

The Learning Resources server has been migrated to a SKILL.md-based approach.
The curated vault of 132 resources is now in `.github/skills/learning-resources-vault/`.

**Why:** The resources are static data — skills are the correct GHCP primitive for static
knowledge. The skill approach provides: all-mode availability, zero infrastructure,
instant maintenance, and auto-activation by semantic match.

**The code is preserved** as a reference implementation for learning MCP server development.
To re-enable, set `"disabled": false` in `.vscode/mcp.json`.
```

---

## Phase 7 — Verification

### Step 7.1: Resource Count Verification

```text
Total resources in MCP server providers: 132
Total resources in skill sub-files:      ___  (must equal 132)
```

Verify by counting rows in each sub-file table (excluding header and separator rows).

### Step 7.2: Capability Verification

| Capability | Verification Method | Pass? |
|---|---|---|
| Search by topic | Ask "find java concurrency resources" in Copilot Chat | [ ] |
| Browse by category | Ask "what learning resources do you have for DevOps?" | [ ] |
| Filter by difficulty | Ask "beginner resources for algorithms" | [ ] |
| Show resource details | Ask "tell me about the System Design Primer" | [ ] |
| `/resources` command | Invoke `/resources → search → java concurrency → intermediate` | [ ] |
| Auto-activation | Ask about learning resources WITHOUT using `/resources` | [ ] |
| Works in Ask mode | Use Ask mode (not Agent) — still answers resource queries | [ ] |
| Display formatting | Check badges, tables, difficulty indicators render correctly | [ ] |
| Cross-references | `/resources` suggests `/learn-concept`, `/deep-dive`, etc. | [ ] |
| No orphan links | All links in slash-commands.md and docs point to valid targets | [ ] |

### Step 7.3: Build Verification

```powershell
# Build must still pass (MCP server code stays but is disabled)
.\mcp-servers\build.ps1
```

### Step 7.4: Markdown Lint

```powershell
# All new .md files must pass lint
.\__md_lint.ps1
```

---

## Appendix A — Complete Resource Catalog

The complete catalog of 132 resources is documented in the intent capture session:

**Session file:** `brain/ai-brain/sessions/personal/software-dev/design/2026-03-22_02-00pm_design_learningresources-mcp-to-skills-migration.md`

Provider summary:

| Provider | Count |
|---|---|
| JavaResources | 17 |
| WebResources | 5 |
| PythonResources | 4 |
| FrameworksResources | 11 |
| AlgorithmsResources | 3 |
| DataStructuresResources | 8 |
| EngineeringResources | 6 |
| DevOpsResources | 6 |
| VcsResources | 9 |
| BuildToolsResources | 11 |
| CloudInfraResources | 7 |
| DataAndSecurityResources | 8 |
| TestingToolsResources | 5 |
| AiMlResources | 4 |
| DigitalNotetakingResources | 15 |
| GeneralResources | 5 |
| SelfDevelopmentResources | 13 |
| **TOTAL** | **132** |

---

## Appendix B — Capability Mapping

### MCP Tool → Skill/Prompt Equivalent

| MCP Tool | Skill Equivalent | Notes |
|---|---|---|
| `search_resources` | LLM searches skill tables by semantic match | No code needed — LLM does this natively |
| `browse_vault` | Section headings in skill sub-files | User says "browse DevOps resources" → LLM reads that section |
| `get_resource` | LLM finds specific entry by title/ID | Works because resources are in structured tables |
| `list_categories` | Category summary table in main SKILL.md | Static — always available |
| `discover_resources` | LLM applies semantic understanding | The 3-mode intent classification is what LLMs already do |
| `scrape_url` | Agent `fetch` tool | Requires Agent mode; LLM parses the fetched content |
| `read_url` | Agent `fetch` tool | Same as scrape but returns full content |
| `add_resource` | Edit skill sub-file | User adds a table row directly |
| `add_resource_from_url` | `fetch` URL + manually add to skill | Two-step but transparent |
| `export_results` | Copilot generates Markdown output | LLM formats tables, lists, badges from skill context |

### Scoring Engine → LLM Natural Ability

| Scoring Dimension | LLM Equivalent |
|---|---|
| Exact title match (100) | LLM finds exact matches in table titles |
| Partial title match (40) | LLM recognizes partial matches |
| Description match (20) | LLM reads description text |
| Tag match (15) | LLM reads tags index sections |
| Concept match (25) | LLM understands concept relationships semantically |
| Category match (20) | LLM reads category columns |
| Domain affinity (10) | LLM groups related concepts naturally |
| Official boost (15) | LLM reads ✅ in Official column |
| Freshness boost (10) | LLM reads freshness badges |
| Difficulty fit (10) | LLM reads difficulty column and filters |
| Language fit (12) | LLM reads language applicability column |
| Fuzzy match (8) | LLM handles typos and variations natively |

---

## Appendix C — Token Budget Analysis

### Per-Sub-File Estimates

| Sub-File | Resources | Est. Tokens | Status |
|---|---|---|---|
| SKILL.md (main) | 0 (index only) | ~1,200 | Within budget |
| resources-java.md | 17 | ~1,800 | Within budget |
| resources-web-javascript.md | 12 | ~1,300 | Within budget |
| resources-python.md | 6 | ~650 | Within budget |
| resources-algorithms-ds.md | 11 | ~1,200 | Within budget |
| resources-software-engineering.md | 11 | ~1,200 | Within budget |
| resources-devops-vcs-build.md | 26 | ~2,800 | On the edge — may split |
| resources-cloud-infra.md | 15 | ~1,600 | Within budget |
| resources-ai-ml.md | 4 | ~450 | Within budget |
| resources-productivity-pkm.md | 15 | ~1,600 | Within budget |
| resources-general-career.md | 18 | ~1,900 | Within budget |
| **TOTAL** | **135** | **~15,800** | Sub-files loaded on demand |

**Key insight:** VS Code loads the main SKILL.md (~1,200 tokens) on every activation.
Sub-files are loaded **only when the topic matches** — so a Java question loads
`resources-java.md` (~1,800 tokens), not all 15,800 tokens. This tiered approach
keeps the token budget manageable.

If `resources-devops-vcs-build.md` is too large, split into:

- `resources-devops.md` (DevOps 6 resources)
- `resources-vcs.md` (VCS 9 resources)
- `resources-build-tools.md` (Build Tools 11 resources)

---

## Execution Order Summary

```text
Phase 1: Create skill directory + SKILL.md + sub-file stubs     [Architecture]
Phase 2: Populate all 10 sub-files with 132 resources            [Content]
Phase 3: Migrate taxonomy (domains, types, difficulty, badges)   [Metadata]
Phase 4: Update /resources prompt + verify agent compatibility   [Integration]
Phase 5: Update slash-commands.md, README, USAGE.md, hub.prompt  [Documentation]
Phase 6: Disable MCP server + add deprecation notices            [Deprecation]
Phase 7: Count resources, test capabilities, build, lint         [Verification]
```

Each phase has a clear deliverable and verification step. No phase depends on a
future phase, so they can be executed sequentially with commit points between each.
