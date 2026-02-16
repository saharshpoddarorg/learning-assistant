# Learning Assistant

> A personal learning workspace — powered by AI, built for curiosity.

---

## What Is This?

This is a **general-purpose learning assistant** — a living, evolving repository designed for learning anything, experimenting freely, and growing skills across all areas of life.

While it heavily covers **software engineering, computer science, and technology**, it is **not restricted** to those domains. This workspace is a launchpad for:

- **Technical learning** — programming languages, data structures, system design, DevOps, databases, security, architecture, and more
- **Professional growth** — career exploration, interview preparation, SDLC practices, industry trends
- **Technology experimentation** — trying new tools, frameworks, languages, and architectures hands-on
- **AI-assisted learning** — leveraging GitHub Copilot's customization features (agents, prompts, skills) as a personalized tutor
- **Daily life productivity** — finance basics, habit tracking, time management, web research
- **General curiosity** — any concept, any domain, any depth

---

## What Makes It Special?

This repo is enhanced with a **deeply customized GitHub Copilot setup** that transforms VS Code Chat into a personal learning assistant. But the value goes beyond Copilot — the repository itself serves as:

| Role | How |
|---|---|
| **Learning Hub** | 2,400+ lines of curated SE resources (books, tutorials, patterns, industry practices) |
| **Experimentation Sandbox** | Write Java, Python, or any code — test ideas, prototype, explore |
| **AI Tutor** | 25 slash commands, 7 specialist agents, 8 skill packs — ask anything, get structured answers |
| **Career Coach** | Role exploration, skills matrices, salary data, interview prep |
| **Industry Guide** | Real-world patterns from Netflix, Google, Uber, Spotify + engineering blogs |
| **Tech Trends Tracker** | AI/LLM evolution, platform engineering, Wasm, Rust, edge computing, and more |
| **Daily Companion** | Finance, productivity, news, and general research |

> **Important:** This project **includes** GitHub Copilot customization as a powerful learning accelerator, but it is **not limited** to Copilot. It's equally useful as a standalone knowledge base, experimentation environment, and study guide.

---

## Quick Start

### Prerequisites
- **VS Code** with **GitHub Copilot** extension (for AI features)
- **Java 21+** (for the included Java sandbox — optional)
- Git

### Get Started
```bash
git clone <repo-url>
cd learning-assistant
code .
```

### Try It Out
1. Open VS Code Chat (`Ctrl+Shift+I`)
2. Type `/hub` — browse all available commands
3. Try `/learn-concept` → type any topic → get a structured lesson
4. Try `/dsa` → pick an algorithm → learn with code examples
5. Try `/system-design` → design a real system from scratch
6. Explore the full [Getting Started Tutorial](.github/docs/getting-started.md) (~30 min)

---

## What's Inside

```
learning-assistant/
│
├── README.md                    ← You are here
├── src/                         ← Code sandbox (Java entry point + experiments)
│   └── Main.java
│
└── .github/                     ← AI customization + knowledge base
    │
    ├── copilot-instructions.md  ← Project-wide coding rules
    │
    ├── instructions/            ← Auto-loaded coding standards
    │   ├── java.instructions.md     (Java naming, style, Java 21+)
    │   └── clean-code.instructions.md (Clean code practices)
    │
    ├── agents/                  ← 7 specialist AI personas
    │   ├── learning-mentor      → Teaching with analogies & exercises
    │   ├── designer             → Architecture & SOLID review
    │   ├── debugger             → Hypothesis-driven bug finding
    │   ├── impact-analyzer      → Change ripple effect analysis
    │   ├── code-reviewer        → Read-only code quality review
    │   ├── daily-assistant      → Finance, productivity, news
    │   └── thinking-beast-mode  → Autonomous deep research
    │
    ├── prompts/                 ← 25 slash commands
    │   ├── /hub                 → Master navigation index
    │   ├── /learn-concept       → Learn any concept from scratch
    │   ├── /dsa                 → Data structures & algorithms
    │   ├── /system-design       → HLD/LLD system design
    │   ├── /devops              → CI/CD, Docker, K8s, Git, build tools
    │   ├── /language-guide      → Language-specific learning
    │   ├── /tech-stack          → Frameworks, databases, comparison
    │   ├── /sdlc                → Full SDLC lifecycle & methodologies
    │   ├── /interview-prep      → Interview preparation
    │   ├── /career-roles        → Job roles, skills, pay, roadmaps
    │   ├── /daily-assist        → Daily life tasks
    │   └── ...and 14 more       → See /hub for full list
    │
    ├── skills/                  ← 8 auto-loaded knowledge packs
    │   ├── software-engineering-resources/  → 2,400+ line SE knowledge base
    │   │   Covers: DSA, system design, OS, networking, DBMS,
    │   │   DevOps, CI/CD, Git, build tools, containers, K8s,
    │   │   SDLC, testing, security, distributed systems,
    │   │   industry patterns (Netflix, Google, Uber, Spotify),
    │   │   tech trends 2025-26, engineering blogs, and more
    │   ├── java-learning-resources/    → Java-specific learning index
    │   ├── career-resources/           → Roles, skills, compensation data
    │   ├── daily-assistant-resources/  → Finance, productivity, news
    │   ├── java-build/                 → Compile & run help
    │   ├── design-patterns/            → SOLID & patterns reference
    │   └── java-debugging/             → Exception diagnosis
    │
    └── docs/                    ← Developer documentation
        ├── getting-started.md       → Step-by-step tutorial
        ├── customization-guide.md   → Architecture deep-dive
        ├── slash-commands.md        → All 25 commands reference
        ├── navigation-index.md      → Master file & command index
        └── file-reference.md        → Copilot vs developer file guide
```

---

## Learning Domains Covered

### Technical & Engineering (Extensive)

| Domain | What's Available |
|---|---|
| **Data Structures & Algorithms** | Full DSA with complexity tables, patterns, interview prep (arrays, trees, graphs, DP, sorting, searching) |
| **System Design** | HLD (scaling, caching, CDN, replication), LLD (OOP modeling, API design), case studies |
| **DevOps & CI/CD** | Jenkins, GitHub Actions, Docker, Kubernetes, deployment strategies, GitOps, feature flags |
| **Build Tools** | Maven (lifecycle, POM, scopes), Gradle (phases, DSL), Ant, Bazel |
| **Version Control (Git)** | 60+ commands, branching strategies (Git Flow, Trunk-Based), recovery, internals |
| **SDLC** | 8-phase E2E lifecycle, Agile/Scrum/Kanban/XP/SAFe, testing pyramid, frontend/backend/DB aspects |
| **Programming Languages** | Java 21+, Python, Go, Rust, C++, JavaScript/TypeScript, Kotlin, and more via `/language-guide` |
| **Frameworks & Tools** | Spring, FastAPI, React, Angular, PostgreSQL, MongoDB, Redis, Kafka, and more |
| **Operating Systems** | Process management, memory, file systems, concurrency, scheduling |
| **Networking** | TCP/UDP, HTTP/HTTPS, DNS, TLS, gRPC, REST, WebSocket, protocols |
| **Databases** | SQL, NoSQL, indexing, normalization, ACID, CAP theorem, replication |
| **Distributed Systems** | Consensus (Raft, Paxos), consistency models, partitioning, fault tolerance |
| **Security** | OWASP Top 10, zero trust, supply chain security, SBOM, secrets management |
| **Testing** | TDD, BDD, unit/integration/e2e, mutation testing, test pyramid |
| **Design Patterns** | GoF patterns, SOLID, GRASP, clean architecture |

### Industry & Real-World Systems

| Topic | Coverage |
|---|---|
| **Rate Limiting** | Token bucket, sliding window, Netflix/Cloudflare/Stripe approaches |
| **Circuit Breakers** | Hystrix, Resilience4j, state machine, industry usage |
| **Transformers & LLMs** | Architecture evolution (2017–2025), BERT → GPT → Claude → Gemini |
| **Event-Driven Architecture** | Kafka, CQRS, event sourcing — Uber, LinkedIn, Netflix patterns |
| **Microservices Patterns** | Saga, outbox, strangler fig, BFF, service mesh, API gateway |
| **Twelve-Factor App** | All 12 principles with modern cloud-native implementations |
| **Observability** | How Netflix, Google, Uber, Spotify do metrics/logs/traces |
| **Top Company Practices** | Engineering approaches from 10+ major tech companies |
| **Engineering Blogs** | 13 curated blogs (Netflix, Uber, Spotify, Stripe, Google, Meta, etc.) |

### Tech Trends (2025–2026)

| Area | Trends Tracked |
|---|---|
| **AI in Engineering** | Coding assistants, agentic AI, AI code review, vibe coding, RAG |
| **Infrastructure** | Platform engineering, eBPF, GitOps, serverless v2, cloud dev environments |
| **Languages** | Rust adoption, Java modernization (Loom), Zig, Kotlin Multiplatform |
| **Data** | Vector databases, data lakehouse, streaming-first, feature stores |
| **Architecture** | Modular monolith, cell-based, edge computing, zero trust, micro-frontends |

### Professional & Career

| Domain | What's Available |
|---|---|
| **Career Exploration** | 10+ tech roles with skills matrices, salary data, day-in-the-life |
| **Interview Prep** | DSA patterns, system design frameworks, behavioral strategies |
| **Career Roadmaps** | Transition plans between roles with timelines |

### Daily Life & Productivity

| Domain | What's Available |
|---|---|
| **Finance** | Budget tracking, investment basics, expense analysis |
| **Productivity** | Time management, habit tracking, daily planning |
| **News & Research** | Tech news summaries, web research, comparison shopping |

---

## Not Just Copilot

While GitHub Copilot's customization features are a key accelerator, this repo provides value in multiple ways:

| Without Copilot | With Copilot |
|---|---|
| Browse 2,400+ lines of curated SE resources directly | Ask questions and get structured, contextual answers |
| Read book recommendations & learning paths | Get personalized study plans via `/reading-plan` |
| Reference industry patterns & architecture guides | Interactive system design sessions via `/system-design` |
| Use as a coding sandbox for any language | AI-assisted coding, debugging, and review |
| Study the customization files as documentation | Full AI tutor experience with 25 commands |

### Using This Repo for Experimentation

The `src/` directory is your sandbox. Use it to:
- **Try new Java features** — records, sealed classes, virtual threads
- **Implement DSA problems** — after learning via `/dsa`, code the solutions here
- **Prototype designs** — after `/system-design`, build a proof of concept
- **Experiment with patterns** — apply design patterns from the skill pack
- **Test anything** — add files in any language, this is your playground

---

## Documentation

| Doc | Purpose | Time |
|---|---|---|
| [Getting Started](.github/docs/getting-started.md) | Hands-on tutorial — verify setup, try each feature | ~30 min |
| [Customization Guide](.github/docs/customization-guide.md) | Architecture — how all the pieces connect | ~20 min |
| [Slash Commands](.github/docs/slash-commands.md) | All 25 commands — inputs, aliases, composition | ~5 min |
| [Navigation Index](.github/docs/navigation-index.md) | Master lookup — commands, agents, skills, files | ~5 min |
| [File Reference](.github/docs/file-reference.md) | Which files Copilot reads vs. developer docs | ~5 min |
| [Copilot Customization README](.github/README.md) | Deep dive into all 5 Copilot primitives | ~10 min |

---

## Tech Stack

| Component | Technology |
|---|---|
| **Language** | Java 21+ (sandbox — expandable to any language) |
| **IDE** | VS Code with GitHub Copilot |
| **AI** | GitHub Copilot (agents, prompts, skills, instructions) |
| **Build** | Manual compilation (build tool setup available via `/devops`) |
| **Knowledge Base** | Markdown-based skill files (auto-loaded by Copilot) |

---

## Contributing & Extending

This is a personal learning workspace, but the structure is designed to be **extensible**:

- **Add new agents** — create `*.agent.md` in `.github/agents/`
- **Add new commands** — create `*.prompt.md` in `.github/prompts/`
- **Add new skills** — create `<name>/SKILL.md` in `.github/skills/`
- **Add new languages** — drop files in `src/` or create subdirectories
- **Expand knowledge** — edit SKILL.md files to add resources, patterns, concepts

See the [Customization Guide](.github/docs/customization-guide.md) for detailed instructions.

---

## License

This is a personal learning project. Use it as inspiration for your own learning setup.

---

<p align="center">
<b>Start learning:</b> Open VS Code Chat → type <code>/hub</code> → explore
</p>
