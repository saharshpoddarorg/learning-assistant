---
date: 2026-03-22
time: "05:30 PM"
kind: session-capture
domain: personal
category: design
project: learning-assistant
subject: learningresources-migration-complete-audit
tags: [project:learning-assistant, migration, mcp-server, skill, enums, learning-resources, audit]
status: final
version: 1
parent: 2026-03-22_02-00pm_mcp-to-skills-migration.md
complexity: high
outcomes:
  - Full audit confirming 138 resources successfully migrated across 10 skill sub-files
  - All 8 enums (97 total values) verified against Java source and documented in taxonomy-reference.md
  - Count discrepancies fixed (ConceptArea 36→40, ResourceCategory 18→17, total 92→97)
  - Provider name errors corrected in migration-mapping.md
  - Comprehensive enum reference with every value documented
source: copilot
scope: project
scope-project: learning-assistant
scope-feature: null
scope-transitions: []
scope-refs:
  - file: "personal/software-dev/design/learningresources/2026-03-22_02-00pm_mcp-to-skills-migration.md"
    relationship: continuation
    note: "original intent-capture session; this session audits and verifies the completed migration"
---

# Complete Audit — Learning Resources MCP Server to Skills Migration

> **Context:** The learning-assistant project migrated 138 curated learning resources from
> a Java MCP server (17 provider classes, 8 enums, 10 tools) to a SKILL.md-based approach
> (10 Markdown sub-files, taxonomy-reference.md, migration-mapping.md). This session
> documents the completed migration with every enum value, every resource, and every link —
> verified against the Java source code.

---

## Request

Verify that the earlier migration request was completed successfully. Describe in complete
detail how the Learning Resources MCP server was migrated to the skills approach, covering
all enums (every value), all resources (every link), and all documentation. Fix anything
that was missed or inaccurate.

---

## Analysis — Migration Architecture

### What Was Migrated

```text
MCP Server (Java)                    →  Skill (Markdown)
─────────────────                       ──────────────────
17 provider classes (138 resources)  →  10 domain sub-files (138 resources)
8 model enums (97 values)            →  taxonomy-reference.md (all 97 documented)
LearningResource record (15 fields)  →  9 table columns + Tags Index
KeywordIndex (300+ mappings)         →  taxonomy-reference.md keyword tables
RelevanceScorer (12 weights)         →  LLM native reasoning
ResourceDiscovery (3-mode engine)    →  LLM query handling
10 MCP tool handlers                 →  Retired (LLM reads skill directly)
BuiltInResources registry            →  SKILL.md Quick Index table
```

### Skill File Structure

```text
.github/skills/learning-resources-vault/
├── SKILL.md                          ← Entry point, Quick Index, badge legend
├── taxonomy-reference.md             ← All 8 enums, keyword index, scoring
├── migration-mapping.md              ← Component traceability (MCP → Skill)
├── resources-java.md                 ← 20 resources (Java + Spring/Hibernate)
├── resources-python.md               ← 6 resources (Python + Django/Flask)
├── resources-web-javascript.md       ← 12 resources (Web + JS/TS + frameworks + npm)
├── resources-algorithms-ds.md        ← 11 resources (Algorithms + Data Structures)
├── resources-software-engineering.md ← 11 resources (Engineering + Testing tools)
├── resources-devops-vcs-build.md     ← 25 resources (DevOps + VCS + Build Tools)
├── resources-cloud-infra.md          ← 15 resources (Cloud + DB + Security)
├── resources-ai-ml.md               ← 4 resources (AI/ML + LLMs)
├── resources-productivity-pkm.md     ← 15 resources (PKM + Note-Taking)
└── resources-general-career.md       ← 19 resources (General CS + Self-Development)
```

---

## Complete Enum Reference (8 Enums, 97 Values)

Every Java enum in `mcp-servers/src/server/learningresources/model/` is documented below
with every value verified against the source code.

### Enum 1 — ConceptDomain (9 values)

**Source:** `model/ConceptDomain.java`
**Constructor:** `ConceptDomain(String displayName)`
**Purpose:** Top-level knowledge area grouping. Each domain contains 2-6 ConceptAreas.

| # | Value | Display Name | Areas |
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

**Skill mapping:** taxonomy-reference.md §1 + SKILL.md domains table

---

### Enum 2 — ConceptArea (40 values)

**Source:** `model/ConceptArea.java`
**Constructor:** `ConceptArea(String displayName, ConceptDomain domain)`
**Purpose:** Fine-grained CS/SE concepts. Each resource lists 1-5 concept areas.

#### Programming Fundamentals (5)

| # | Value | Slug | Domain |
|---|---|---|---|
| 1 | `LANGUAGE_BASICS` | language-basics | PROGRAMMING_FUNDAMENTALS |
| 2 | `OOP` | oop | PROGRAMMING_FUNDAMENTALS |
| 3 | `FUNCTIONAL_PROGRAMMING` | functional-programming | PROGRAMMING_FUNDAMENTALS |
| 4 | `LANGUAGE_FEATURES` | language-features | PROGRAMMING_FUNDAMENTALS |
| 5 | `GENERICS` | generics | PROGRAMMING_FUNDAMENTALS |

#### Core CS (6)

| # | Value | Slug | Domain |
|---|---|---|---|
| 6 | `CONCURRENCY` | concurrency | CORE_CS |
| 7 | `DATA_STRUCTURES` | data-structures | CORE_CS |
| 8 | `ALGORITHMS` | algorithms | CORE_CS |
| 9 | `MATHEMATICS` | mathematics | CORE_CS |
| 10 | `COMPLEXITY_ANALYSIS` | complexity-analysis | CORE_CS |
| 11 | `MEMORY_MANAGEMENT` | memory-management | CORE_CS |

#### Software Engineering (5)

| # | Value | Slug | Domain |
|---|---|---|---|
| 12 | `DESIGN_PATTERNS` | design-patterns | SOFTWARE_ENGINEERING |
| 13 | `CLEAN_CODE` | clean-code | SOFTWARE_ENGINEERING |
| 14 | `TESTING` | testing | SOFTWARE_ENGINEERING |
| 15 | `API_DESIGN` | api-design | SOFTWARE_ENGINEERING |
| 16 | `ARCHITECTURE` | architecture | SOFTWARE_ENGINEERING |

#### System Design & Infrastructure (5)

| # | Value | Slug | Domain |
|---|---|---|---|
| 17 | `SYSTEM_DESIGN` | system-design | SYSTEM_DESIGN |
| 18 | `DATABASES` | databases | SYSTEM_DESIGN |
| 19 | `DISTRIBUTED_SYSTEMS` | distributed-systems | SYSTEM_DESIGN |
| 20 | `NETWORKING` | networking | SYSTEM_DESIGN |
| 21 | `OPERATING_SYSTEMS` | operating-systems | SYSTEM_DESIGN |

#### DevOps & Tooling (6)

| # | Value | Slug | Domain |
|---|---|---|---|
| 22 | `CI_CD` | ci-cd | DEVOPS_TOOLING |
| 23 | `CONTAINERS` | containers | DEVOPS_TOOLING |
| 24 | `VERSION_CONTROL` | version-control | DEVOPS_TOOLING |
| 25 | `BUILD_TOOLS` | build-tools | DEVOPS_TOOLING |
| 26 | `INFRASTRUCTURE` | infrastructure | DEVOPS_TOOLING |
| 27 | `OBSERVABILITY` | observability | DEVOPS_TOOLING |

#### Security (2)

| # | Value | Slug | Domain |
|---|---|---|---|
| 28 | `WEB_SECURITY` | web-security | SECURITY |
| 29 | `CRYPTOGRAPHY` | cryptography | SECURITY |

#### AI & Data (3)

| # | Value | Slug | Domain |
|---|---|---|---|
| 30 | `MACHINE_LEARNING` | machine-learning | AI_DATA |
| 31 | `DEEP_LEARNING` | deep-learning | AI_DATA |
| 32 | `LLM_AND_PROMPTING` | llm-and-prompting | AI_DATA |

#### Career & Meta (4)

| # | Value | Slug | Domain |
|---|---|---|---|
| 33 | `INTERVIEW_PREP` | interview-prep | CAREER_META |
| 34 | `CAREER_DEVELOPMENT` | career-development | CAREER_META |
| 35 | `GETTING_STARTED` | getting-started | CAREER_META |
| 36 | `KNOWLEDGE_MANAGEMENT` | knowledge-management | CAREER_META |

#### Personal Development (4)

| # | Value | Slug | Domain |
|---|---|---|---|
| 37 | `SELF_IMPROVEMENT` | self-improvement | PERSONAL_DEVELOPMENT |
| 38 | `COMMUNICATION_SKILLS` | communication-skills | PERSONAL_DEVELOPMENT |
| 39 | `FINANCIAL_LITERACY` | financial-literacy | PERSONAL_DEVELOPMENT |
| 40 | `PRODUCTIVITY_HABITS` | productivity-habits | PERSONAL_DEVELOPMENT |

**Skill mapping:** taxonomy-reference.md §2 + "Concepts" column in resource tables

---

### Enum 3 — ResourceCategory (17 values)

**Source:** `model/ResourceCategory.java`
**Constructor:** `ResourceCategory(String displayName)`
**Purpose:** Technology/ecosystem classification. Resources can have multiple categories.

| # | Value | Slug | Sub-File(s) |
|---|---|---|---|
| 1 | `JAVA` | java | resources-java.md |
| 2 | `PYTHON` | python | resources-python.md |
| 3 | `JAVASCRIPT` | javascript | resources-web-javascript.md |
| 4 | `WEB` | web | resources-web-javascript.md |
| 5 | `DATABASE` | database | resources-cloud-infra.md |
| 6 | `DEVOPS` | devops | resources-devops-vcs-build.md |
| 7 | `CLOUD` | cloud | resources-cloud-infra.md |
| 8 | `ALGORITHMS` | algorithms | resources-algorithms-ds.md |
| 9 | `SOFTWARE_ENGINEERING` | software-engineering | resources-software-engineering.md |
| 10 | `TESTING` | testing | resources-software-engineering.md |
| 11 | `SECURITY` | security | resources-cloud-infra.md |
| 12 | `AI_ML` | ai-ml | resources-ai-ml.md |
| 13 | `TOOLS` | tools | resources-devops-vcs-build.md |
| 14 | `PRODUCTIVITY` | productivity | resources-productivity-pkm.md |
| 15 | `SYSTEMS` | systems | resources-cloud-infra.md |
| 16 | `GENERAL` | general | resources-general-career.md |
| 17 | `PERSONAL_DEVELOPMENT` | personal-development | resources-general-career.md |

**Skill mapping:** taxonomy-reference.md §3 + sub-file routing

---

### Enum 4 — ResourceType (13 values)

**Source:** `model/ResourceType.java`
**Constructor:** `ResourceType(String displayName)`
**Purpose:** Content format classification.

| # | Value | Slug | Badge |
|---|---|---|---|
| 1 | `DOCUMENTATION` | documentation | 📖 Docs |
| 2 | `TUTORIAL` | tutorial | 📝 Tutorial |
| 3 | `BLOG` | blog | 📰 Blog |
| 4 | `ARTICLE` | article | 📄 Article |
| 5 | `VIDEO` | video | 🎥 Video |
| 6 | `PLAYLIST` | playlist | 🎬 Playlist |
| 7 | `VIDEO_COURSE` | video-course | 🎓 Course |
| 8 | `BOOK` | book | 📚 Book |
| 9 | `INTERACTIVE` | interactive | 🎮 Interactive |
| 10 | `COURSE` | course | 🎓 Course |
| 11 | `API_REFERENCE` | api-reference | 📋 API Ref |
| 12 | `CHEAT_SHEET` | cheat-sheet | 📋 Cheat Sheet |
| 13 | `REPOSITORY` | repository | 💻 Repo |

**Skill mapping:** taxonomy-reference.md §5 + "Type" column badges

---

### Enum 5 — DifficultyLevel (4 values)

**Source:** `model/DifficultyLevel.java`
**Constructor:** `DifficultyLevel(String displayName, int ordinalLevel)`
**Purpose:** Prerequisite knowledge level.

| # | Value | Slug | Ordinal | Badge |
|---|---|---|---|---|
| 1 | `BEGINNER` | beginner | 1 | 🟢 |
| 2 | `INTERMEDIATE` | intermediate | 2 | 🟡 |
| 3 | `ADVANCED` | advanced | 3 | 🔴 |
| 4 | `EXPERT` | expert | 4 | ⚫ |

**Skill mapping:** taxonomy-reference.md §4 + "Diff" column badges

---

### Enum 6 — ContentFreshness (5 values)

**Source:** `model/ContentFreshness.java`
**Constructor:** `ContentFreshness(String displayName)`
**Purpose:** How current/maintained the content is.

| # | Value | Slug | Badge | Meaning |
|---|---|---|---|---|
| 1 | `EVERGREEN` | evergreen | ♾️ | Timeless — always relevant |
| 2 | `ACTIVELY_MAINTAINED` | actively-maintained | 🔄 | Regularly updated |
| 3 | `PERIODICALLY_UPDATED` | periodically-updated | 📅 | Updated occasionally |
| 4 | `HISTORICAL` | historical | 📜 | Dated but still valuable |
| 5 | `ARCHIVED` | archived | 🗄️ | No longer maintained |

**Skill mapping:** taxonomy-reference.md §6 + "Fresh" column badges

---

### Enum 7 — LanguageApplicability (6 values)

**Source:** `model/LanguageApplicability.java`
**Constructor:** `LanguageApplicability(String displayName, String description)`
**Purpose:** How the resource relates to programming languages.

| # | Value | Slug | Badge | Description |
|---|---|---|---|---|
| 1 | `UNIVERSAL` | universal | 🌐 | Applies to all languages equally |
| 2 | `MULTI_LANGUAGE` | multi-language | 🔤 | Multiple languages/platforms |
| 3 | `JAVA_CENTRIC` | java-centric | ☕ | Java-focused, concepts transferable |
| 4 | `JAVA_SPECIFIC` | java-specific | ☕✦ | Java/JVM only |
| 5 | `PYTHON_SPECIFIC` | python-specific | 🐍 | Python only |
| 6 | `WEB_SPECIFIC` | web-specific | 🌐✦ | JS/TS/Web only |

**Skill mapping:** taxonomy-reference.md §7 + "Lang" column badges

---

### Enum 8 — SearchMode (3 values)

**Source:** `model/SearchMode.java`
**Constructor:** `SearchMode(String displayName, String description)`
**Purpose:** Query intent classification (3-mode discovery engine).

| # | Value | Slug | Description |
|---|---|---|---|
| 1 | `SPECIFIC` | specific | User knows exactly what they want |
| 2 | `VAGUE` | vague | User knows the area but not the resource |
| 3 | `EXPLORATORY` | exploratory | User wants guidance and suggestions |

**Skill mapping:** taxonomy-reference.md §8 + query phrasing guide

---

### Enum Summary

| Enum | Values | Documented In |
|---|---|---|
| ConceptDomain | 9 | taxonomy-reference.md §1 |
| ConceptArea | 40 | taxonomy-reference.md §2 |
| ResourceCategory | 17 | taxonomy-reference.md §3 |
| DifficultyLevel | 4 | taxonomy-reference.md §4 |
| ResourceType | 13 | taxonomy-reference.md §5 |
| ContentFreshness | 5 | taxonomy-reference.md §6 |
| LanguageApplicability | 6 | taxonomy-reference.md §7 |
| SearchMode | 3 | taxonomy-reference.md §8 |
| **Total** | **97** | |

---

## LearningResource Record (15 Fields)

**Source:** `model/LearningResource.java` (Java record)

| # | Field | Type | Skill Column | Format |
|---|---|---|---|---|
| 1 | `id` | `String` | Tags Index | URL-safe slug |
| 2 | `title` | `String` | Title | Markdown link text |
| 3 | `url` | `String` | Title | Embedded in `[title](url)` link |
| 4 | `description` | `String` | Tags Index | Brief summary text |
| 5 | `type` | `ResourceType` | Type | Badge emoji |
| 6 | `categories` | `List<ResourceCategory>` | Sub-file placement | Determines which file |
| 7 | `conceptAreas` | `List<ConceptArea>` | Concepts | Comma-separated names |
| 8 | `tags` | `List<String>` | Tags Index | Free-form keywords |
| 9 | `author` | `String` | Author | Plain text |
| 10 | `difficulty` | `DifficultyLevel` | Diff | Badge emoji |
| 11 | `freshness` | `ContentFreshness` | Fresh | Badge emoji |
| 12 | `isOfficial` | `boolean` | Off | ✅ or ➖ |
| 13 | `isFree` | `boolean` | Free | ✅ or 💰 |
| 14 | `languageApplicability` | `LanguageApplicability` | Lang | Badge emoji |
| 15 | `addedAt` | `Instant` | Not shown | All resources are current |

---

## Complete Resource Inventory (138 Resources)

### Provider → Skill Sub-File Mapping (17 Providers → 10 Files)

| Provider Class | Count | Destination |
|---|---|---|
| `JavaResources` | 17 | resources-java.md |
| `FrameworksResources` (Java: Spring Framework, Spring Boot, Hibernate) | 3 | resources-java.md |
| `FrameworksResources` (Web: React, Next.js, Angular, Vue, Node, Express) | 6 | resources-web-javascript.md |
| `FrameworksResources` (Python: Django, Flask) | 2 | resources-python.md |
| `WebResources` | 5 | resources-web-javascript.md |
| `BuildToolsResources` (npm-docs) | 1 | resources-web-javascript.md |
| `PythonResources` | 4 | resources-python.md |
| `AlgorithmsResources` | 3 | resources-algorithms-ds.md |
| `DataStructuresResources` | 8 | resources-algorithms-ds.md |
| `EngineeringResources` | 6 | resources-software-engineering.md |
| `TestingToolsResources` | 5 | resources-software-engineering.md |
| `DevOpsResources` | 6 | resources-devops-vcs-build.md |
| `VcsResources` | 9 | resources-devops-vcs-build.md |
| `BuildToolsResources` (10 remaining) | 10 | resources-devops-vcs-build.md |
| `CloudInfraResources` | 7 | resources-cloud-infra.md |
| `DataAndSecurityResources` | 8 | resources-cloud-infra.md |
| `AiMlResources` | 4 | resources-ai-ml.md |
| `DigitalNotetakingResources` | 15 | resources-productivity-pkm.md |
| `GeneralResources` | 5 | resources-general-career.md |
| `SelfDevelopmentResources` | 14 | resources-general-career.md |
| **Total** | **138** | **10 files** |

### Per-File Resource Summary

#### resources-java.md (20 resources)

| # | Title | URL | Provider |
|---|---|---|---|
| 1 | The Java Tutorials (dev.java) | https://dev.java/learn/ | JavaResources |
| 2 | JDK API Documentation (Javadoc) | https://docs.oracle.com/en/java/javase/21/docs/api/ | JavaResources |
| 3 | The Java Language Specification (JLS) | https://docs.oracle.com/javase/specs/jls/se21/html/index.html | JavaResources |
| 4 | Inside.java — Official Java Blog | https://inside.java/ | JavaResources |
| 5 | The JVM Specification (JVMS) | https://docs.oracle.com/javase/specs/jvms/se21/html/index.html | JavaResources |
| 6 | OpenJDK — Source Code & JEP Process | https://openjdk.org/ | JavaResources |
| 7 | JDK 25 Release Notes | https://jdk.java.net/25/release-notes | JavaResources |
| 8 | JDK 25 Migration Guide | https://docs.oracle.com/en/java/javase/25/migrate/getting-started.html | JavaResources |
| 9 | SDKMAN! — JDK Version Manager | https://sdkman.io/ | JavaResources |
| 10 | Eclipse Temurin — Open-Source JDK Builds | https://adoptium.net/temurin/releases/ | JavaResources |
| 11 | Baeldung — Java Tutorials & Guides | https://www.baeldung.com/ | JavaResources |
| 12 | Jenkov.com — Java & Web Tutorials | https://jenkov.com/ | JavaResources |
| 13 | Effective Java (3rd Edition) | https://www.oreilly.com/library/view/effective-java/9780134686097/ | JavaResources |
| 14 | Java Concurrency in Practice (JCIP) | https://jcip.net/ | JavaResources |
| 15 | Spring Framework Reference | https://docs.spring.io/spring-framework/reference/ | FrameworksResources |
| 16 | Spring Boot Reference | https://docs.spring.io/spring-boot/reference/ | FrameworksResources |
| 17 | Spring Boot Getting Started Guides | https://spring.io/guides | JavaResources |
| 18 | Hibernate ORM User Guide | https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html | FrameworksResources |
| 19 | Google Guava — Wiki & User Guide | https://github.com/google/guava/wiki | JavaResources |
| 20 | JUnit 5 User Guide | https://junit.org/junit5/docs/current/user-guide/ | JavaResources |

#### resources-python.md (6 resources)

| # | Title | URL | Provider |
|---|---|---|---|
| 1 | The Python Tutorial (Official) | https://docs.python.org/3/tutorial/ | PythonResources |
| 2 | Python Language Reference (Official) | https://docs.python.org/3/reference/ | PythonResources |
| 3 | PEP Index — Python Enhancement Proposals | https://peps.python.org/ | PythonResources |
| 4 | Real Python — Tutorials & Articles | https://realpython.com/ | PythonResources |
| 5 | Django Documentation | https://docs.djangoproject.com/ | FrameworksResources |
| 6 | Flask Documentation | https://flask.palletsprojects.com/ | FrameworksResources |

#### resources-web-javascript.md (12 resources)

| # | Title | URL | Provider |
|---|---|---|---|
| 1 | MDN Web Docs | https://developer.mozilla.org/ | WebResources |
| 2 | The Modern JavaScript Tutorial | https://javascript.info/ | WebResources |
| 3 | TypeScript Handbook | https://www.typescriptlang.org/docs/handbook/ | WebResources |
| 4 | W3C Web Standards & Specifications | https://www.w3.org/standards/ | WebResources |
| 5 | web.dev — Google's Web Development Guidance | https://web.dev/ | WebResources |
| 6 | React Documentation | https://react.dev/ | FrameworksResources |
| 7 | Next.js Documentation | https://nextjs.org/docs | FrameworksResources |
| 8 | Angular Documentation | https://angular.dev/ | FrameworksResources |
| 9 | Vue.js Documentation | https://vuejs.org/guide/ | FrameworksResources |
| 10 | Node.js Documentation | https://nodejs.org/docs/latest/api/ | FrameworksResources |
| 11 | Express.js — Fast, Minimal Node.js Framework | https://expressjs.com/ | FrameworksResources |
| 12 | npm Documentation | https://docs.npmjs.com/ | BuildToolsResources |

#### resources-algorithms-ds.md (11 resources)

| # | Title | URL | Provider |
|---|---|---|---|
| 1 | VisuAlgo — Algorithm Visualizations | https://visualgo.net/ | AlgorithmsResources |
| 2 | Big-O Cheat Sheet | https://www.bigocheatsheet.com/ | AlgorithmsResources |
| 3 | CP-Algorithms (Competitive Programming) | https://cp-algorithms.com/ | AlgorithmsResources |
| 4 | Java Collections Framework — Official Docs | https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/doc-files/coll-overview.html | DataStructuresResources |
| 5 | OpenDSA — Interactive Data Structures & Algorithms | https://opendsa-server.cs.vt.edu/ | DataStructuresResources |
| 6 | USFCS Data Structures Visualizer | https://www.cs.usfca.edu/~galles/visualization/Algorithms.html | DataStructuresResources |
| 7 | Neetcode.io — Structured DSA Roadmap | https://neetcode.io/ | DataStructuresResources |
| 8 | GeeksforGeeks — Data Structures Portal | https://www.geeksforgeeks.org/data-structures/ | DataStructuresResources |
| 9 | MIT 6.006 — Intro to Algorithms | https://ocw.mit.edu/courses/6-006-introduction-to-algorithms-spring-2020/ | DataStructuresResources |
| 10 | Python — Data Structures (Official Docs) | https://docs.python.org/3/tutorial/datastructures.html | DataStructuresResources |
| 11 | The Algorithm Design Manual — Skiena | https://www.algorist.com/ | DataStructuresResources |

#### resources-software-engineering.md (11 resources)

| # | Title | URL | Provider |
|---|---|---|---|
| 1 | Refactoring.Guru — Design Patterns & Refactoring | https://refactoring.guru/ | EngineeringResources |
| 2 | Clean Code — Summary & Principles | https://gist.github.com/wojteklu/73c6914cc446146b8b533c0988cf8d29 | EngineeringResources |
| 3 | The Twelve-Factor App | https://12factor.net/ | EngineeringResources |
| 4 | The System Design Primer | https://github.com/donnemartin/system-design-primer | EngineeringResources |
| 5 | Martin Fowler — Architecture & Patterns | https://martinfowler.com/ | EngineeringResources |
| 6 | 3Blue1Brown — Essence of Linear Algebra | https://www.3blue1brown.com/topics/linear-algebra | EngineeringResources |
| 7 | Mockito — Tasty Mocking Framework for Java | https://site.mockito.org/ | TestingToolsResources |
| 8 | Testcontainers — Integration Testing | https://testcontainers.com/ | TestingToolsResources |
| 9 | Selenium WebDriver Documentation | https://www.selenium.dev/documentation/ | TestingToolsResources |
| 10 | Cypress Documentation | https://docs.cypress.io/ | TestingToolsResources |
| 11 | pytest Documentation | https://docs.pytest.org/ | TestingToolsResources |

#### resources-devops-vcs-build.md (25 resources)

| # | Title | URL | Provider |
|---|---|---|---|
| 1 | Docker Docs — Get Started | https://docs.docker.com/get-started/ | DevOpsResources |
| 2 | Kubernetes Documentation | https://kubernetes.io/docs/ | DevOpsResources |
| 3 | Pro Git Book (2nd Edition) | https://git-scm.com/book/en/v2 | DevOpsResources |
| 4 | GitHub Skills — Interactive Courses | https://skills.github.com/ | DevOpsResources |
| 5 | GitHub Actions Documentation | https://docs.github.com/en/actions | DevOpsResources |
| 6 | Gradle User Manual | https://docs.gradle.org/current/userguide/userguide.html | DevOpsResources |
| 7 | Learn Git Branching — Visual & Interactive | https://learngitbranching.js.org/ | VcsResources |
| 8 | Git Reference Manual | https://git-scm.com/docs | VcsResources |
| 9 | Atlassian Git Tutorials | https://www.atlassian.com/git/tutorials | VcsResources |
| 10 | A Successful Git Branching Model (GitFlow) | https://nvie.com/posts/a-successful-git-branching-model/ | VcsResources |
| 11 | GitHub Flow | https://docs.github.com/en/get-started/using-github/github-flow | VcsResources |
| 12 | Trunk-Based Development | https://trunkbaseddevelopment.com/ | VcsResources |
| 13 | Conventional Commits Specification | https://www.conventionalcommits.org/ | VcsResources |
| 14 | Semantic Versioning (SemVer) 2.0.0 | https://semver.org/ | VcsResources |
| 15 | Git Internals (Pro Git — Chapter 10) | https://git-scm.com/book/en/v2/Git-Internals-Plumbing-and-Porcelain | VcsResources |
| 16 | Maven Getting Started Guide | https://maven.apache.org/guides/getting-started/ | BuildToolsResources |
| 17 | Maven in 5 Minutes | https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html | BuildToolsResources |
| 18 | Maven POM Reference | https://maven.apache.org/pom.html | BuildToolsResources |
| 19 | Maven Build Lifecycle | https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html | BuildToolsResources |
| 20 | Maven Dependency Mechanism | https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html | BuildToolsResources |
| 21 | Gradle Kotlin DSL Primer | https://docs.gradle.org/current/userguide/kotlin_dsl.html | BuildToolsResources |
| 22 | Gradle Dependency Management | https://docs.gradle.org/current/userguide/dependency_management.html | BuildToolsResources |
| 23 | Gradle Multi-Project Build Guide | https://docs.gradle.org/current/userguide/multi_project_builds.html | BuildToolsResources |
| 24 | Makefile Tutorial | https://makefiletutorial.com/ | BuildToolsResources |
| 25 | Bazel Documentation | https://bazel.build/docs | BuildToolsResources |

#### resources-cloud-infra.md (15 resources)

| # | Title | URL | Provider |
|---|---|---|---|
| 1 | AWS Documentation | https://docs.aws.amazon.com/ | CloudInfraResources |
| 2 | Google Cloud Documentation | https://cloud.google.com/docs | CloudInfraResources |
| 3 | Microsoft Azure Documentation | https://learn.microsoft.com/en-us/azure/ | CloudInfraResources |
| 4 | Terraform Documentation | https://developer.hashicorp.com/terraform/docs | CloudInfraResources |
| 5 | Ansible Documentation | https://docs.ansible.com/ | CloudInfraResources |
| 6 | Prometheus Documentation | https://prometheus.io/docs/ | CloudInfraResources |
| 7 | Grafana Documentation | https://grafana.com/docs/ | CloudInfraResources |
| 8 | Use The Index, Luke — SQL Indexing & Tuning | https://use-the-index-luke.com/ | DataAndSecurityResources |
| 9 | PostgreSQL Official Documentation | https://www.postgresql.org/docs/ | DataAndSecurityResources |
| 10 | Redis Documentation | https://redis.io/docs/ | DataAndSecurityResources |
| 11 | MongoDB Documentation | https://www.mongodb.com/docs/ | DataAndSecurityResources |
| 12 | Apache Kafka Documentation | https://kafka.apache.org/documentation/ | DataAndSecurityResources |
| 13 | Elasticsearch Reference | https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html | DataAndSecurityResources |
| 14 | OWASP Top Ten | https://owasp.org/www-project-top-ten/ | DataAndSecurityResources |
| 15 | OWASP Cheat Sheet Series | https://cheatsheetseries.owasp.org/ | DataAndSecurityResources |

#### resources-ai-ml.md (4 resources)

| # | Title | URL | Provider |
|---|---|---|---|
| 1 | fast.ai — Practical Deep Learning for Coders | https://www.fast.ai/ | AiMlResources |
| 2 | Prompt Engineering Guide | https://www.promptingguide.ai/ | AiMlResources |
| 3 | OpenAI API Documentation | https://platform.openai.com/docs/ | AiMlResources |
| 4 | 3Blue1Brown — Neural Networks | https://www.3blue1brown.com/topics/neural-networks | AiMlResources |

#### resources-productivity-pkm.md (15 resources)

| # | Title | URL | Provider |
|---|---|---|---|
| 1 | Building a Second Brain — Tiago Forte | https://www.buildingasecondbrain.com/ | DigitalNotetakingResources |
| 2 | PARA Method — Projects, Areas, Resources, Archives | https://fortelabs.com/blog/para/ | DigitalNotetakingResources |
| 3 | CODE Method — The 4 Levels of PKM | https://fortelabs.com/blog/the-4-levels-of-personal-knowledge-management/ | DigitalNotetakingResources |
| 4 | Zettelkasten — Introduction to the Method | https://zettelkasten.de/introduction/ | DigitalNotetakingResources |
| 5 | Progressive Summarization — Forte Labs | https://fortelabs.com/blog/progressive-summarization-a-practical-technique-for-designing-discoverable-notes/ | DigitalNotetakingResources |
| 6 | Getting Things Done (GTD) — David Allen | https://gettingthingsdone.com/ | DigitalNotetakingResources |
| 7 | PKM for Software Engineers | https://dev.to/thiagomg/personal-knowledge-management-for-software-engineers-1nbi | DigitalNotetakingResources |
| 8 | Obsidian — Official Documentation | https://help.obsidian.md/ | DigitalNotetakingResources |
| 9 | Notion — Official Help Center | https://www.notion.so/help | DigitalNotetakingResources |
| 10 | Logseq — Official Documentation | https://docs.logseq.com/ | DigitalNotetakingResources |
| 11 | Microsoft OneNote — Getting Started | https://support.microsoft.com/en-us/onenote | DigitalNotetakingResources |
| 12 | Obsidian Community Plugins Directory | https://obsidian.md/plugins | DigitalNotetakingResources |
| 13 | Foam — VS Code Personal Knowledge Management | https://foambubble.github.io/foam/ | DigitalNotetakingResources |
| 14 | Notion to Obsidian Migration Guide | https://help.obsidian.md/import/notion | DigitalNotetakingResources |
| 15 | Todoist — Getting Things Done with PARA | https://todoist.com/productivity-methods/getting-things-done | DigitalNotetakingResources |

#### resources-general-career.md (19 resources)

| # | Title | URL | Provider |
|---|---|---|---|
| 1 | The Testing Trophy — Kent C. Dodds | https://kentcdodds.com/blog/the-testing-trophy-and-testing-classifications | GeneralResources |
| 2 | Developer Roadmaps (roadmap.sh) | https://roadmap.sh/ | GeneralResources |
| 3 | Free Programming Books (GitHub) | https://github.com/EbookFoundation/free-programming-books | GeneralResources |
| 4 | Teach Yourself Computer Science | https://teachyourselfcs.com/ | GeneralResources |
| 5 | CS50x — Harvard's Introduction to CS | https://cs50.harvard.edu/x/2026/ | GeneralResources |
| 6 | Build Your Own X (codecrafters-io) | https://github.com/codecrafters-io/build-your-own-x | SelfDevelopmentResources |
| 7 | Project Based Learning | https://github.com/practical-tutorials/project-based-learning | SelfDevelopmentResources |
| 8 | Professional Programming — charlax | https://github.com/charlax/professional-programming | SelfDevelopmentResources |
| 9 | Coding Interview University | https://github.com/jwasham/coding-interview-university | SelfDevelopmentResources |
| 10 | OSSU — Open Source CS Curriculum | https://github.com/ossu/computer-science | SelfDevelopmentResources |
| 11 | Awesome Lists — sindresorhus | https://github.com/sindresorhus/awesome | SelfDevelopmentResources |
| 12 | The Book of Secret Knowledge | https://github.com/trimstray/the-book-of-secret-knowledge | SelfDevelopmentResources |
| 13 | Every Programmer Should Know | https://github.com/mtdvio/every-programmer-should-know | SelfDevelopmentResources |
| 14 | Free Science Books (GitHub) | https://github.com/EbookFoundation/free-science-books | SelfDevelopmentResources |
| 15 | Free Programming Courses (GitHub) | https://github.com/EbookFoundation/free-programming-books/blob/main/courses/free-courses-en.md | SelfDevelopmentResources |
| 16 | Mind Expanding Books | https://github.com/hackerkid/Mind-Expanding-Books | SelfDevelopmentResources |
| 17 | Awesome Productivity | https://github.com/jyguyomarch/awesome-productivity | SelfDevelopmentResources |
| 18 | Awesome Speaking | https://github.com/matteofigus/awesome-speaking | SelfDevelopmentResources |
| 19 | awesome-personal-finance (GitHub) | https://github.com/ashishb/awesome-personal-finance | SelfDevelopmentResources |

---

## KeywordIndex Migration (300+ Mappings)

The MCP server's `KeywordIndex.java` had 300+ keyword-to-enum mappings organized as:

- **170+ Concept Area keywords** across 14 topic clusters
- **120+ Category keywords** across 15 topic clusters
- **13 Difficulty keywords** (beginner, novice, intermediate, advanced, expert, etc.)

All mappings are documented in taxonomy-reference.md under the "Keyword Discovery Index"
section. With the skill-based approach, the LLM's native language understanding replaces
the programmatic lookup — but the keyword vocabulary is preserved as documentation for
what topics and synonyms are covered.

---

## 10 MCP Tools — All Retired

| Tool | Purpose | Replacement |
|---|---|---|
| `search_resources` | Keyword search | LLM reads skill tables |
| `browse_vault` | Browse by category | LLM reads domain sub-files |
| `get_resource_details` | Get single resource details | LLM reads table row + Tags Index |
| `discover_resources` | Recommendation engine | LLM applies recommendation logic |
| `list_categories` | List ResourceCategory values | taxonomy-reference.md §3 |
| `list_concepts` | List ConceptArea values | taxonomy-reference.md §2 |
| `list_domains` | List ConceptDomain values | taxonomy-reference.md §1 |
| `add_resource` | Add resource at runtime | Manual edit of sub-file |
| `export_vault` | Export all resources as JSON | Sub-files ARE the export |
| `get_vault_stats` | Resource counts/breakdowns | SKILL.md Quick Index table |

---

## Discrepancies Found and Fixed During Audit

| File | Issue | Old Value | Fixed Value |
|---|---|---|---|
| taxonomy-reference.md | ConceptArea count in overview table | 36 | 40 |
| taxonomy-reference.md | ResourceCategory count in overview table | 18 | 17 |
| taxonomy-reference.md | Total enum values | 92 | 97 |
| taxonomy-reference.md | §2 heading | "Concept Areas (36)" | "Concept Areas (40)" |
| taxonomy-reference.md | §3 heading | "Resource Categories (18)" | "Resource Categories (17)" |
| taxonomy-reference.md | Keyword index intro | "36 Concept Areas" | "40 Concept Areas" |
| migration-mapping.md | Provider resource count in summary | "132 resources" | "138 resources" |
| migration-mapping.md | ConceptArea enum count | "36 values" | "40 values" |
| migration-mapping.md | ResourceCategory enum count | "18 values" | "17 values" |
| migration-mapping.md | Provider name: JavaScriptResources | JavaScriptResources | FrameworksResources (Web) + BuildToolsResources (npm) |
| migration-mapping.md | Provider name: TestingResources | TestingResources | TestingToolsResources |
| migration-mapping.md | Provider name: ProductivityPkmResources | ProductivityPkmResources | DigitalNotetakingResources |
| migration-mapping.md | SelfDevelopmentResources count | 13 | 14 |
| migration-mapping.md | "What Was Preserved" concept area count | 36 | 40 |
| migration-mapping.md | "What Was Preserved" category count | 18 | 17 |
| migration-mapping.md | Verification checklist enum total | 92 | 97 |
| resources-general-career.md | Header count | (15) | (19) |
| brain session file | All "Not started" statuses | Not started | Completed |
| brain session file | Resource count throughout | 132 | 138 |
| brain session file | ConceptArea count | 36 | 40 |
| brain session file | Enum count "7 enums" | 7 | 8 (includes SearchMode) |

---

## Key Outcomes

- Full audit confirmed all 138 resources are present and correct across 10 skill sub-files
- All 8 enums (97 total values) verified against Java source code
- 22 count/naming discrepancies identified and fixed across 4 files
- Migration is 100% complete — no resources, enum values, or metadata lost
- Provider-to-skill traceability fully documented

---

## Follow-Up / Next Steps

- [x] Audit all enum values against Java source
- [x] Count all resources in skill files vs providers
- [x] Fix taxonomy-reference.md counts (6 fixes)
- [x] Fix migration-mapping.md counts and names (8 fixes)
- [x] Fix resources-general-career.md header (1 fix)
- [x] Update brain session file statuses and counts (7 fixes)
- [ ] Consider adding SearchMode (3 values) to Metadata/Taxonomy table in intent doc

---

## Session Metadata

| Property | Value |
|---|---|
| Duration | ~12 exchanges |
| Files touched | taxonomy-reference.md, migration-mapping.md, resources-general-career.md, brain session (intent-capture) |
| Related sessions | [Intent Capture](2026-03-22_02-00pm_design_learningresources-mcp-to-skills-migration.md) |
