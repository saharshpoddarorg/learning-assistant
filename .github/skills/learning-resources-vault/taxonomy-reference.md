# Learning Resources Vault — Taxonomy Reference

> Complete reference for every categorization dimension used in the vault.
> This file documents the full taxonomy that was originally implemented as Java enums
> in the Learning Resources MCP server and is now embedded directly in the skill files
> as structured Markdown tables.

---

## Overview

The vault uses **8 classification dimensions** to describe each resource.
Together they enable multi-faceted discovery — by topic, difficulty, format, freshness,
language scope, and more.

| Dimension | Values | Purpose |
|---|---|---|
| Concept Domain | 9 | Top-level knowledge area grouping |
| Concept Area | 36 | Fine-grained topic within a domain |
| Resource Category | 18 | Technology/ecosystem classification |
| Difficulty Level | 4 | Prerequisite knowledge required |
| Resource Type | 13 | Content format (docs, video, book, etc.) |
| Content Freshness | 5 | Maintenance & currency status |
| Language Applicability | 6 | Programming language scope |
| Search Mode | 3 | Query intent classification |

**Total distinct enum values:** 92

---

## 1. Concept Domains (9)

Concept Domains are the highest-level grouping. Each domain contains 2-6 Concept Areas.

| Domain | Display Name | Areas | Description |
|---|---|---|---|
| `PROGRAMMING_FUNDAMENTALS` | Programming Fundamentals | 5 | Language syntax, OOP, FP, generics, features |
| `CORE_CS` | Core CS | 6 | Data structures, algorithms, math, concurrency, complexity, memory |
| `SOFTWARE_ENGINEERING` | Software Engineering | 5 | Design patterns, clean code, testing, API design, architecture |
| `SYSTEM_DESIGN` | System Design & Infrastructure | 5 | System design, databases, distributed systems, networking, OS |
| `DEVOPS_TOOLING` | DevOps & Tooling | 6 | CI/CD, containers, VCS, build tools, infrastructure, observability |
| `SECURITY` | Security | 2 | Web security, cryptography |
| `AI_DATA` | AI & Data | 3 | Machine learning, deep learning, LLMs & prompting |
| `CAREER_META` | Career & Meta | 4 | Interview prep, career development, getting started, PKM |
| `PERSONAL_DEVELOPMENT` | Personal Development | 4 | Self-improvement, communication, financial literacy, productivity |

---

## 2. Concept Areas (36)

Each Concept Area belongs to exactly one Domain. Resources list 1-5 concept areas
to describe what topics they cover.

### Programming Fundamentals (5 areas)

| Concept Area | Slug | Description |
|---|---|---|
| `LANGUAGE_BASICS` | language-basics | Language syntax, core features, first programs |
| `OOP` | oop | Classes, inheritance, polymorphism, encapsulation |
| `FUNCTIONAL_PROGRAMMING` | functional-programming | Lambdas, streams, FP patterns, immutability |
| `LANGUAGE_FEATURES` | language-features | New versions, upgrades, migration, modern features |
| `GENERICS` | generics | Type parameters, variance, wildcards, type erasure |

### Core CS (6 areas)

| Concept Area | Slug | Description |
|---|---|---|
| `CONCURRENCY` | concurrency | Threads, async, virtual threads, synchronization |
| `DATA_STRUCTURES` | data-structures | Arrays, lists, trees, graphs, heaps, tries, hashes |
| `ALGORITHMS` | algorithms | Sorting, searching, DP, greedy, backtracking |
| `MATHEMATICS` | mathematics | Linear algebra, calculus, probability, discrete math |
| `COMPLEXITY_ANALYSIS` | complexity-analysis | Big-O notation, time/space complexity, benchmarking |
| `MEMORY_MANAGEMENT` | memory-management | GC, heap allocation, memory models |

### Software Engineering (5 areas)

| Concept Area | Slug | Description |
|---|---|---|
| `DESIGN_PATTERNS` | design-patterns | GoF, enterprise, structural, behavioral, creational |
| `CLEAN_CODE` | clean-code | SOLID, GRASP, DRY, KISS, refactoring |
| `TESTING` | testing | Unit, integration, TDD, BDD, mocking, test pyramids |
| `API_DESIGN` | api-design | REST, GraphQL, gRPC, WebSocket, OpenAPI |
| `ARCHITECTURE` | architecture | Layers, hexagonal, microservices, monolith |

### System Design & Infrastructure (5 areas)

| Concept Area | Slug | Description |
|---|---|---|
| `SYSTEM_DESIGN` | system-design | Scalability, load balancing, caching, CDN, sharding |
| `DATABASES` | databases | SQL, NoSQL, indexing, optimization, replication, schemas |
| `DISTRIBUTED_SYSTEMS` | distributed-systems | Consensus, CAP theorem, consistency, leader election |
| `NETWORKING` | networking | TCP/IP, HTTP, DNS, TLS, sockets |
| `OPERATING_SYSTEMS` | operating-systems | Processes, scheduling, file systems, I/O |

### DevOps & Tooling (6 areas)

| Concept Area | Slug | Description |
|---|---|---|
| `CI_CD` | ci-cd | Continuous integration, delivery, and deployment |
| `CONTAINERS` | containers | Docker, Kubernetes, orchestration, images |
| `VERSION_CONTROL` | version-control | Git, branching strategies, merging, rebasing |
| `BUILD_TOOLS` | build-tools | Maven, Gradle, Ant, Bazel, Make, npm, yarn |
| `INFRASTRUCTURE` | infrastructure | Terraform, Ansible, CloudFormation, Pulumi, IaC |
| `OBSERVABILITY` | observability | Monitoring, logging, tracing, alerting, SLI/SLO |

### Security (2 areas)

| Concept Area | Slug | Description |
|---|---|---|
| `WEB_SECURITY` | web-security | XSS, CSRF, injection, OWASP, auth, OAuth |
| `CRYPTOGRAPHY` | cryptography | Encryption, hashing, digital signatures, TLS |

### AI & Data (3 areas)

| Concept Area | Slug | Description |
|---|---|---|
| `MACHINE_LEARNING` | machine-learning | Supervised/unsupervised, models, training |
| `DEEP_LEARNING` | deep-learning | Neural networks, CNNs, RNNs, transformers, backprop |
| `LLM_AND_PROMPTING` | llm-and-prompting | LLMs, prompt engineering, RAG, fine-tuning, agents |

### Career & Meta (4 areas)

| Concept Area | Slug | Description |
|---|---|---|
| `INTERVIEW_PREP` | interview-prep | Coding challenges, system design interviews |
| `CAREER_DEVELOPMENT` | career-development | Growth, learning paths, roadmaps, skill mapping |
| `GETTING_STARTED` | getting-started | Setup, first project, hello world, onboarding |
| `KNOWLEDGE_MANAGEMENT` | knowledge-management | PKM, note-taking, CODE method, PARA, Zettelkasten |

### Personal Development (4 areas)

| Concept Area | Slug | Description |
|---|---|---|
| `SELF_IMPROVEMENT` | self-improvement | Habits, mindset, growth, discipline, reading |
| `COMMUNICATION_SKILLS` | communication-skills | Writing, public speaking, storytelling, feedback |
| `FINANCIAL_LITERACY` | financial-literacy | Budgeting, investing, taxes, personal finance |
| `PRODUCTIVITY_HABITS` | productivity-habits | Time management, deep work, focus, routines |

---

## 3. Resource Categories (18)

Categories classify resources by **technology ecosystem or topic area**. A resource
can belong to multiple categories (e.g., a Spring Boot testing guide might be both
`JAVA` and `TESTING`).

| Category | Slug | Sub-File(s) | Description |
|---|---|---|---|
| `JAVA` | java | resources-java.md | JDK, JVM, Spring, Hibernate, Guava, JUnit |
| `PYTHON` | python | resources-python.md | CPython, Django, Flask, pip |
| `JAVASCRIPT` | javascript | resources-web-javascript.md | JS, TS, Node.js, React, Angular, Vue, npm |
| `WEB` | web | resources-web-javascript.md | HTML, CSS, frontend, browser APIs |
| `DATABASE` | database | resources-cloud-infra.md | SQL, PostgreSQL, MongoDB, Redis, Elasticsearch |
| `DEVOPS` | devops | resources-devops-vcs-build.md | Docker, Kubernetes, Git, CI/CD, Monitoring |
| `CLOUD` | cloud | resources-cloud-infra.md | AWS, GCP, Azure, IaC |
| `ALGORITHMS` | algorithms | resources-algorithms-ds.md | DSA, competitive programming, visualizations |
| `SOFTWARE_ENGINEERING` | software-engineering | resources-software-engineering.md | Patterns, clean code, architecture, 12-factor |
| `TESTING` | testing | resources-software-engineering.md | JUnit, Mockito, Selenium, Cypress, pytest |
| `SECURITY` | security | resources-cloud-infra.md | OWASP, secure coding, cryptography |
| `AI_ML` | ai-ml | resources-ai-ml.md | Machine learning, deep learning, LLMs |
| `TOOLS` | tools | resources-devops-vcs-build.md | Build tools, package managers, dev utilities |
| `PRODUCTIVITY` | productivity | resources-productivity-pkm.md | Note-taking, PKM, GTD, time management |
| `SYSTEMS` | systems | resources-cloud-infra.md | Distributed systems, networking, OS |
| `GENERAL` | general | resources-general-career.md | Roadmaps, curated lists, CS50, cross-cutting |
| `PERSONAL_DEVELOPMENT` | personal-development | resources-general-career.md | Self-help, habits, communication, finance |

---

## 4. Difficulty Levels (4)

Ordinal scale from 1 (lowest) to 4 (highest). Used for filtering and recommendation.

| Level | Ordinal | Badge | Description |
|---|---|---|---|
| `BEGINNER` | 1 | 🟢 | No prior knowledge required; hello-world level |
| `INTERMEDIATE` | 2 | 🟡 | Basic knowledge assumed; working-level content |
| `ADVANCED` | 3 | 🔴 | Solid understanding required; deep-dive level |
| `EXPERT` | 4 | ⚫ | Research-level, formal specs, or highly specialized |

---

## 5. Resource Types (13)

Classification by content **format**, not topic.

| Type | Badge | Description |
|---|---|---|
| `DOCUMENTATION` | 📖 Docs | Official language/framework documentation |
| `TUTORIAL` | 📝 Tutorial | Step-by-step guides with exercises |
| `BLOG` | 📰 Blog | Opinion/experience articles, blog posts |
| `ARTICLE` | 📄 Article | In-depth technical articles (longer than blog posts) |
| `VIDEO` | 🎥 Video | Standalone talks, screencasts, conference recordings |
| `PLAYLIST` | 🎬 Playlist | Ordered video series (no formal course structure) |
| `VIDEO_COURSE` | 🎓 Course | Full structured video courses with syllabus |
| `BOOK` | 📚 Book | Published books or book chapters |
| `INTERACTIVE` | 🎮 Interactive | Coding exercises, sandboxes, playgrounds |
| `COURSE` | 🏫 Course | Structured courses with lessons, quizzes, projects |
| `API_REFERENCE` | 📋 API Ref | API reference with method signatures and types |
| `CHEAT_SHEET` | 📋 Cheatsheet | Quick reference cards, one-pagers, summary sheets |
| `REPOSITORY` | 💻 Repo | GitHub repos, open-source projects, example codebases |

---

## 6. Content Freshness (5)

How well the content tracks the current state of its subject.

| Freshness | Badge | Description | Example |
|---|---|---|---|
| `EVERGREEN` | ♾️ | Timeless content; rarely needs updates | CLRS, design patterns, Big-O |
| `ACTIVELY_MAINTAINED` | 🔄 | Tracks latest versions; updated regularly | MDN, dev.java, Docker docs |
| `PERIODICALLY_UPDATED` | 📅 | Mostly current; may lag behind latest releases | Many blog posts, Baeldung |
| `HISTORICAL` | 📜 | Accurate for its stated version; not updated | Older JDK migration guides |
| `ARCHIVED` | 🗄️ | Kept for reference; may be misleading if treated as current | Deprecated API docs |

---

## 7. Language Applicability (6)

How tied a resource is to a specific programming language.

| Applicability | Badge | Transferable? | Java-Relevant? | Description |
|---|---|---|---|---|
| `UNIVERSAL` | 🌐 | Yes | Yes | Language-agnostic (algorithms, patterns, system design, math) |
| `MULTI_LANGUAGE` | 🔤 | Yes | Yes | Multiple platforms (REST, SQL, containers, CI/CD, OWASP, VCS) |
| `JAVA_CENTRIC` | ☕ | Yes | Yes | Java-focused but concepts transfer (Spring-style DI, JUnit patterns) |
| `JAVA_SPECIFIC` | ☕ | No | Yes | JVM-only (GC tuning, virtual threads, records, sealed classes, Maven) |
| `PYTHON_SPECIFIC` | 🐍 | No | No | Python-only (decorators, GIL, asyncio, comprehensions) |
| `WEB_SPECIFIC` | 🌐✦ | No | No | JS/TS/Web-only (event loop, DOM, React hooks, npm) |

---

## 8. Search Modes (3) — Query Intent Classification

The MCP server classified user queries into three intent modes. With the skill-based
approach, the LLM naturally handles this classification — but understanding the modes
helps you phrase effective queries.

| Mode | Original Trigger | How to Phrase for Skill | Example |
|---|---|---|---|
| `SPECIFIC` | Quoted text, URLs, "docs for", "reference for", "official" | Ask by name or use precise terms | "Show me the JUnit 5 User Guide" |
| `VAGUE` | Topic-area keywords that match concept/category mappings | Describe the topic area | "What resources cover Java concurrency?" |
| `EXPLORATORY` | "learn", "beginner", "recommend", "where to start" | Describe your level and goals | "I'm a beginner — recommend Java resources" |

### Query Phrasing Guide

**For specific resources (was SPECIFIC mode):**

- "Show me the official Spring Boot documentation"
- "Find the Docker Getting Started guide"
- "What is the Effective Java book about?"

**For topic exploration (was VAGUE mode):**

- "What resources cover design patterns?"
- "Show me advanced Java concurrency resources"
- "List all testing resources for Java"

**For guided discovery (was EXPLORATORY mode):**

- "I'm new to programming — where should I start?"
- "Recommend beginner-friendly resources for web development"
- "What should I learn after mastering Java basics?"

---

## Keyword Discovery Index

The MCP server used a `KeywordIndex` with 300+ keyword-to-enum mappings to infer
user intent from natural language. With the skill-based approach, the LLM's native
language understanding replaces this lookup — but the keyword vocabulary below
shows what topics and synonyms are covered.

### Concept Area Keywords (170+ mappings)

These keywords (and their synonyms) map to the 36 Concept Areas above:

| Topic Cluster | Keywords | Maps To |
|---|---|---|
| Object-Oriented | oop, object-oriented, classes, inheritance, polymorphism, encapsulation | `OOP` |
| Functional Programming | functional, lambda, lambdas, streams | `FUNCTIONAL_PROGRAMMING` |
| Generics | generics, type-system, wildcards | `GENERICS` |
| Concurrency | concurrency, threads, async, parallel, virtual threads, synchronization | `CONCURRENCY` |
| Data Structures | data structures, collections, list, map, tree, graph, heap, trie, stack, queue, deque, linked list, hash table, hashmap, bst, avl, red-black tree, union-find, neetcode, java collections | `DATA_STRUCTURES` |
| Algorithms | algorithms, sorting, searching, dynamic programming | `ALGORITHMS` |
| Complexity | big-o, complexity, time complexity | `COMPLEXITY_ANALYSIS` |
| Memory | memory, garbage collection, heap, jvm | `MEMORY_MANAGEMENT` |
| Design Patterns | design patterns, patterns, singleton, factory, observer, strategy | `DESIGN_PATTERNS` |
| Clean Code | clean code, refactoring, best practices, solid | `CLEAN_CODE` |
| Testing | testing, unit test, tdd, junit, mocking, mockito, selenium, cypress, pytest, testcontainers, e2e, end-to-end | `TESTING` |
| API Design | api, rest, graphql, express, expressjs, flask | `API_DESIGN` |
| Architecture | architecture, microservices, hexagonal, spring boot, react, angular, vue, django, nodejs | `ARCHITECTURE` |
| System Design | system design, scalability, load balancing | `SYSTEM_DESIGN` |
| Databases | database, sql, postgresql, indexing, hibernate, jpa, orm, redis, mongodb, nosql, elasticsearch, mysql, dynamodb | `DATABASES` |
| Distributed Systems | distributed, consensus, kafka, rabbitmq | `DISTRIBUTED_SYSTEMS` |
| Networking | networking, http, tcp, dns | `NETWORKING` |
| Operating Systems | operating systems, processes | `OPERATING_SYSTEMS` |
| CI/CD | ci/cd, ci-cd, pipeline, github actions | `CI_CD` |
| Containers | docker, kubernetes, k8s, containers | `CONTAINERS` |
| Version Control | git, version control, branching, conventional commits, gitflow, trunk-based, semver, rebase, merge, stash, cherry-pick, hooks, pull requests | `VERSION_CONTROL` |
| Build Tools | gradle, maven, build, pom, mvn, bazel, make, npm, yarn, pnpm, ant, package manager, build tool, dependency | `BUILD_TOOLS` |
| Observability | monitoring, logging, tracing, prometheus, grafana, elk, alerting, dashboards | `OBSERVABILITY` |
| Infrastructure | terraform, ansible, iac, infrastructure as code, cloudformation, pulumi, aws, gcp, azure, cloud, ec2, s3, lambda | `INFRASTRUCTURE` |
| Web Security | security, owasp, xss, injection, authentication | `WEB_SECURITY` |
| Cryptography | cryptography, encryption, hashing | `CRYPTOGRAPHY` |
| Machine Learning | machine learning, deep learning, neural, ai | `MACHINE_LEARNING` |
| Deep Learning | neural network, neural networks, gradient descent, backpropagation, transformers | `DEEP_LEARNING` |
| LLM & Prompting | llm, prompt, gpt, rag | `LLM_AND_PROMPTING` |
| Mathematics | 3blue1brown, 3b1b, linear algebra, calculus, statistics, probability, discrete math | `MATHEMATICS` |
| Interview Prep | interview, leetcode | `INTERVIEW_PREP` |
| Career | career, roadmap | `CAREER_DEVELOPMENT` |
| Getting Started | getting started, beginner, hello world, playlist, video series, cs50, harvard cs | `GETTING_STARTED` |
| Knowledge Management | note-taking, notetaking, notes, notion, obsidian, onenote, logseq, roam, pkm, second brain, para method, code method, zettelkasten, tiago forte, knowledge management, digital notes, gtd, getting things done, david allen, basb, progressive summarization, foam, adr, daily notes, obsidian plugins, dataview, templater, migration, todoist | `KNOWLEDGE_MANAGEMENT` |
| Self-Improvement | self-improvement, personal development, personal growth, habits, atomic habits, mindset, growth mindset, motivation, discipline, self-help, reading list | `SELF_IMPROVEMENT` |
| Communication | communication, public speaking, writing, storytelling, presentation, soft skills | `COMMUNICATION_SKILLS` |
| Financial Literacy | financial literacy, personal finance, budgeting, investing, tax | `FINANCIAL_LITERACY` |
| Productivity | deep work, time management, focus, pomodoro, routine | `PRODUCTIVITY_HABITS` |

### Category Keywords (120+ mappings)

These keywords map to the 18 Resource Categories:

| Topic Cluster | Keywords | Maps To |
|---|---|---|
| Java | java, spring, jdk, jvm, spring-boot, spring boot, hibernate, jpa | `JAVA` |
| Python | python, django, flask | `PYTHON` |
| JavaScript | javascript, typescript, node, react, npm, yarn, pnpm, angular, vue, vuejs, nextjs, expressjs | `JAVASCRIPT` |
| Web | web, html, css, frontend | `WEB` |
| DevOps | devops, docker, kubernetes, git, maven, gradle, bazel, prometheus, grafana, monitoring, elk | `DEVOPS` |
| Security | security, owasp | `SECURITY` |
| Database | data, database, sql, redis, mongodb, nosql, elasticsearch, kafka, mysql, dynamodb, postgresql | `DATABASE` |
| AI/ML | ai, ml, machine learning, deep learning, 3blue1brown, 3b1b, linear algebra | `AI_ML` |
| Algorithms | algorithm, algorithms, data structures, data-structures, ds, dsa, linked list, trees, graphs, heaps, trie, hash table, neetcode | `ALGORITHMS` |
| Testing | testing, junit, mockito, selenium, cypress, pytest, testcontainers, e2e | `TESTING` |
| Engineering | engineering | `SOFTWARE_ENGINEERING` |
| Cloud | aws, amazon web services, gcp, google cloud, azure, cloud, terraform, ansible, iac, ec2, s3, lambda | `CLOUD` |
| Productivity | notion, obsidian, onenote, logseq, pkm, note-taking, productivity, gtd, para, second-brain, todoist, foam, zettelkasten | `PRODUCTIVITY` |
| General | cs50, harvard, playlist, video series | `GENERAL` |
| Personal Dev | personal development, self-improvement, self-help, personal growth, habits, mindset, soft skills, communication, public speaking, financial literacy, personal finance, budgeting, investing, deep work, time management | `PERSONAL_DEVELOPMENT` |

### Difficulty Keywords (13 mappings)

| Keywords | Maps To |
|---|---|
| beginner, new, start, easy, intro, basic | `BEGINNER` |
| intermediate, moderate, mid | `INTERMEDIATE` |
| advanced, deep, hard | `ADVANCED` |
| expert, master | `EXPERT` |

---

## Relevance Scoring — How the MCP Server Ranked Results

The MCP server used a 12-dimensional weighted scoring system to rank resources.
With the skill-based approach, the LLM replaces this scoring with native reasoning.
This section documents the original scoring model for reference and to inform
how the LLM should weigh factors when recommending resources.

### Scoring Weights

| Factor | Weight | Description |
|---|---|---|
| Exact title match | 100 | Resource title exactly matches query |
| Partial title match | 40 | Title or ID contains the search string |
| Description match | 20 | Description contains search terms |
| Tag match | 15 | Tags contain matching keywords |
| Concept match | 25 | Resource covers an inferred concept area |
| Category match | 20 | Resource belongs to an inferred category |
| Domain affinity | 10 | Resource concept is in the same domain as query concept |
| Official source | 15 | Official/authoritative source bonus |
| Freshness | 10 | Actively maintained resource bonus |
| Difficulty fit | 10 | Difficulty matches target range |
| Language fit | 12 | Language applicability matches user preference |
| Fuzzy match | 8 | Typo tolerance (min 4-char word, 3-char shared prefix) |

### Scoring by Query Mode

**Specific queries** prioritized: exact title match (100) > partial title (40) > fuzzy (8) > language/official bonuses.

**Vague queries** prioritized: concept match (25) > category match (20) > description (20) > tag match (15) > domain affinity (10) > official/freshness bonuses.

**Exploratory queries** prioritized: difficulty fit (10×2) > getting-started concept match > official (15×2) > freshness (10) > free resource bonus (+5) > language fit.

### LLM Recommendation Guidelines

When recommending resources, the LLM should naturally apply similar priorities:

1. **Match the query intent** — specific name matches beat topic matches
2. **Prefer official sources** — official documentation over community content for reference
3. **Consider difficulty** — match resource difficulty to the user's stated level
4. **Favour freshness** — actively maintained over historical for current learning
5. **Respect language scope** — universal resources are always relevant; language-specific only when applicable
6. **Check concept coverage** — resources covering the exact concept area should rank higher
7. **Prefer free** — when quality is comparable, favour free resources

---

## Resource Record Structure

Each resource in the vault tables has these 15 metadata fields, represented as
table columns with badge notation:

| Field | Column Header | Values | Purpose |
|---|---|---|---|
| Title | Title | Markdown link `[name](url)` | Resource name + URL |
| Type | Type | Badge (📖, 📝, 📰, etc.) | Content format |
| Difficulty | Diff | Badge (🟢, 🟡, 🔴, ⚫) | Prerequisite level |
| Concept Areas | Concepts | Comma-separated names | Topics covered |
| Author | Author | Text | Creator/organization |
| Freshness | Fresh | Badge (🔄, ♾️, 📅, etc.) | Maintenance status |
| Is Official | Off | ✅ or ➖ | Authoritative source? |
| Is Free | Free | ✅ or 💰 | Freely accessible? |
| Language Applicability | Lang | Badge (🌐, 🔤, ☕, 🐍, 🌐✦) | Language scope |
| ID | *(in Tags Index)* | URL-safe slug | Unique identifier |
| Description | *(in Tags Index)* | Text | Brief summary |
| Categories | *(in Tags Index)* | Comma-separated slugs | Ecosystem categories |
| Tags | *(in Tags Index)* | Comma-separated keywords | Free-form search terms |
| Added Date | *(not shown)* | ISO 8601 | When added to vault |

**Table columns** (visible in resource tables): Title, Type, Diff, Concepts, Author, Fresh, Off, Free, Lang

**Tags Index** (at the bottom of each sub-file): lists all tags, IDs, and categories for searchability

---

## Domain → Concept Area → Category Cross-Reference

This table shows how the three main classification axes relate:

| Domain | Concept Areas | Typical Categories |
|---|---|---|
| Programming Fundamentals | Language basics, OOP, FP, Generics, Features | JAVA, PYTHON, JAVASCRIPT |
| Core CS | Concurrency, DS, Algorithms, Math, Complexity, Memory | ALGORITHMS, JAVA, GENERAL |
| Software Engineering | Patterns, Clean code, Testing, API design, Architecture | SOFTWARE_ENGINEERING, TESTING |
| System Design | System design, DBs, Distributed, Networking, OS | DATABASE, SYSTEMS, CLOUD |
| DevOps & Tooling | CI/CD, Containers, VCS, Build, Infra, Observability | DEVOPS, TOOLS, CLOUD |
| Security | Web security, Cryptography | SECURITY |
| AI & Data | ML, Deep learning, LLM & Prompting | AI_ML |
| Career & Meta | Interview, Career, Getting started, PKM | GENERAL, PRODUCTIVITY |
| Personal Development | Self-improvement, Communication, Finance, Productivity | PERSONAL_DEVELOPMENT |

---

## How to Use This Taxonomy

### For Searching

Ask using any keyword from the keyword index tables above. The LLM will match your
natural language to the appropriate concept areas and categories. Examples:

- "virtual threads" → matches `CONCURRENCY` concept, `JAVA` category
- "design patterns" → matches `DESIGN_PATTERNS` concept, `SOFTWARE_ENGINEERING` category
- "docker" → matches `CONTAINERS` concept, `DEVOPS` category
- "leetcode" → matches `INTERVIEW_PREP` concept, `ALGORITHMS` category

### For Filtering

Combine dimensions for precise filtering:

- **By difficulty:** "beginner Java resources" → 🟢 resources in `JAVA` category
- **By type:** "Java books" → 📚 Book type in `JAVA` category
- **By freshness:** "actively maintained Python docs" → 🔄 freshness in `PYTHON` category
- **By language:** "universal design resources" → 🌐 applicability in `SOFTWARE_ENGINEERING`
- **By concept + difficulty:** "advanced concurrency resources" → 🔴 + `CONCURRENCY` concept

### For Learning Paths

Use the domain hierarchy to build progression paths:

1. **Start** with `GETTING_STARTED` concept → beginner resources
2. **Foundation** with `LANGUAGE_BASICS` + `OOP` → core language skills
3. **Intermediate** with `DATA_STRUCTURES` + `ALGORITHMS` → CS fundamentals
4. **Applied** with `DESIGN_PATTERNS` + `TESTING` + `CLEAN_CODE` → engineering practices
5. **Advanced** with `SYSTEM_DESIGN` + `ARCHITECTURE` + `DISTRIBUTED_SYSTEMS` → system thinking
6. **Specialise** with domain-specific concepts (Security, AI, DevOps, etc.)
