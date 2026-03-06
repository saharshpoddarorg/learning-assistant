# Learning Assistant

> **New to this repo?** → Start at [**.github/docs/START-HERE.md**](.github/docs/START-HERE.md) — it tells you exactly what to read based on your experience level.  
> **Want everything in one place?** → [**USAGE.md**](USAGE.md) — comprehensive guide covering all features, all levels, all commands.

> **Learn anything. Experiment freely. Grow continuously.**  
> An open-source, AI-enhanced learning workspace that turns curiosity into structured knowledge.

[![Made with GitHub Copilot](https://img.shields.io/badge/Made%20with-GitHub%20Copilot-blue?logo=github)](https://github.com/features/copilot)
[![Java 21+](https://img.shields.io/badge/Java-21%2B-orange?logo=openjdk)](https://openjdk.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## Why This Exists

Most learning happens in fragments — a tutorial here, a blog post there, a half-finished course somewhere. This repository is an attempt to solve that by providing a **single, organized, extensible workspace** where learning resources, hands-on experimentation, and AI-powered guidance live together.

It started as a way to learn GitHub Copilot's customization features, but evolved into something broader: a **general-purpose learning assistant** that covers software engineering deeply while remaining open to any subject — professional growth, daily productivity, career planning, or pure curiosity.

### Who Is This For?

- **Developers** who want a structured way to learn CS/SE concepts with curated resources
- **Students** preparing for technical interviews (DSA, system design, behavioral)
- **Engineers** exploring industry practices from Netflix, Google, Uber, Spotify, and others
- **Anyone** interested in building their own AI-assisted learning environment
- **Copilot users** who want to see what's possible with agents, prompts, and skills

---

## What You Get

### A Curated Knowledge Base (2,400+ lines)

Hand-curated, continuously expanded reference material covering:

| Area | Depth |
|---|---|
| **Data Structures & Algorithms** | Complexity tables, pattern taxonomy, interview prep, industry use cases |
| **System Design** | HLD/LLD deep dives, estimation cheat sheets, 10+ case studies |
| **DevOps & CI/CD** | Pipeline anatomy, deployment strategies, Docker/K8s, GitOps, feature flags |
| **Build Tools & Git** | Maven/Gradle deep dives, 60+ Git commands, branching strategies, internals |
| **Industry Patterns** | Rate limiting, circuit breakers, event-driven architecture, microservices |
| **Real-World Engineering** | How Netflix, Google, Uber, and Spotify build systems at scale |
| **Tech Trends (2025–26)** | AI coding assistants, agentic AI, platform engineering, Wasm, Rust, eBPF |
| **Full SDLC** | 8-phase lifecycle, testing pyramid, frontend/backend/DB aspects |
| **Security** | OWASP Top 10, zero trust, supply chain security, secrets management |
| **Career** | 10+ tech roles, skills matrices, salary data, transition roadmaps |

> This isn't just a link dump — it's structured reference material with explanations, comparison tables, ASCII diagrams, and real code examples.

### An AI-Powered Learning Environment

If you use **VS Code + GitHub Copilot**, the repo transforms Chat into a personalized tutor:

- **30 slash commands** — type `/dsa`, `/system-design`, `/devops`, `/learn-concept`, `/mcp`, or `/hub` to navigate
- **7 specialist agents** — Learning Mentor, Designer, Debugger, Impact Analyzer, Code Reviewer, Daily Assistant, and a deep-research "Thinking Beast Mode"
- **8 auto-loaded skill packs** — Copilot automatically gains domain knowledge when you ask relevant questions

> **No Copilot? No problem.** The knowledge base, book recommendations, and learning paths are valuable as standalone reference material — the `.github/skills/` folder is a readable, well-organized Markdown knowledge base.

### A Code Sandbox

The `src/` directory is your playground. Use it to:
- Implement algorithms after learning them
- Prototype system designs
- Experiment with new language features (Java 21+: records, sealed classes, virtual threads)
- Try design patterns from the skill packs
- Add code in any language — Java, Python, Go, Rust, whatever you're learning

---

## Quick Start

### Option 1: With GitHub Copilot (Full Experience)

**Prerequisites:** VS Code + GitHub Copilot extension + Java 21+ (optional)

```bash
git clone https://github.com/<your-username>/learning-assistant.git
cd learning-assistant
code .
```

1. Open VS Code Chat (`Ctrl+Shift+I`)
2. Type `/hub` → browse all available commands
3. Try `/learn-concept` → type any topic → get a structured lesson
4. Try `/dsa` → pick a data structure → learn with code + complexity analysis
5. Try `/system-design` → design a URL shortener, rate limiter, or chat system

For a guided walkthrough, see the [Getting Started Tutorial](.github/docs/getting-started.md) (~30 min).

### Option 2: Without Copilot (Knowledge Base Only)

Browse the skill files directly — they're well-structured Markdown:

- [Software Engineering Resources](.github/skills/software-engineering-resources/SKILL.md) — the main 2,400+ line knowledge base
- [Java Learning Resources](.github/skills/java-learning-resources/SKILL.md) — Java-specific tutorials, docs, projects
- [Career Resources](.github/skills/career-resources/SKILL.md) — roles, skills, compensation, roadmaps
- [Design Patterns](.github/skills/design-patterns/SKILL.md) — GoF patterns, SOLID, decision guide

---

## Repository Structure

```
learning-assistant/
│
├── README.md                        ← You are here
├── .vscode/
│   ├── mcp.json                         MCP server registry — tells VS Code/Copilot which servers to run
│   └── tasks.json                       Build tasks (Ctrl+Shift+B → "mcp-servers: build")
│
├── src/                             ← Code sandbox
│   └── Main.java                        Java entry point (expandable to any language)
│
├── mcp-servers/                     ← MCP Server configuration + implementations
│   ├── README.md                        Module docs, setup guide, architecture
│   ├── SETUP.md                         Step-by-step setup guide
│   ├── build.ps1 / build.sh             Build scripts — auto-detect JDK, compile → out/
│   ├── .vscode/
│   │   ├── mcp.json.example             Portable MCP config template (copy to other projects)
│   │   ├── settings.json                Java project settings (portable)
│   │   ├── launch.json                  F5 launch configs for each server
│   │   └── extensions.json              Recommended extensions
│   ├── user-config/
│   │   ├── mcp-config.properties            Base config — safe defaults, no secrets (committed)
│   │   ├── mcp-config.local.properties      YOUR secrets — gitignored, never committed
│   │   ├── mcp-config.local.example.properties  Template for local config
│   │   └── servers/atlassian/               Atlassian-specific config (same pattern)
│   └── src/
│       ├── config/                      Java config system (records, loader, validator)
│       └── server/
│           ├── learningresources/       Learning Resources MCP Server (~100+ built-in resources)
│           └── atlassian/               Atlassian MCP Server (27 tools: Jira, Confluence, Bitbucket)
│
└── .github/                         ← AI customization + knowledge base
    ├── copilot-instructions.md          Project-wide coding rules
    ├── instructions/                    Auto-loaded coding standards (Java, clean code)
    ├── agents/                          7 specialist AI personas
    ├── prompts/                         36 slash commands (type /command in Chat)
    ├── skills/                          11 auto-loaded knowledge packs
    │   └── software-engineering-resources/SKILL.md  ← The main knowledge base
    └── docs/                            Developer documentation & tutorials
```

<details>
<summary><b>Expand full structure</b></summary>

```
.github/
├── copilot-instructions.md              Always-on project rules
│
├── instructions/
│   ├── java.instructions.md             Java naming, style, Java 21+ features
│   ├── clean-code.instructions.md       Clean code practices, refactoring naming
│   └── build.instructions.md            Gradle build commands & patterns
│
├── agents/
│   ├── learning-mentor.agent.md         Teaching with analogies & exercises
│   ├── designer.agent.md               Architecture & SOLID review
│   ├── debugger.agent.md               Hypothesis-driven bug finding
│   ├── impact-analyzer.agent.md        Change ripple effect analysis
│   ├── code-reviewer.agent.md          Read-only code quality review
│   ├── daily-assistant.agent.md        Finance, productivity, news
│   └── Thinking-Beast-Mode.agent.md    Autonomous deep research
│
├── prompts/                             36 slash commands including:
│   ├── hub.prompt.md                    /hub — master navigation index
│   ├── learn-concept.prompt.md          /learn-concept — any CS/SE concept
│   ├── dsa.prompt.md                    /dsa — data structures & algorithms
│   ├── system-design.prompt.md          /system-design — HLD/LLD
│   ├── devops.prompt.md                 /devops — CI/CD, Docker, K8s, Git
│   ├── mcp.prompt.md                    /mcp — MCP protocol & server development
│   ├── interview-prep.prompt.md         /interview-prep — technical interviews
│   ├── career-roles.prompt.md           /career-roles — roles, pay, roadmaps
│   ├── brain-new.prompt.md              /brain-new — create knowledge note
│   ├── brain-publish.prompt.md          /brain-publish — publish to archive & commit
│   ├── brain-search.prompt.md           /brain-search — search across note tiers
│   └── ...and 19 more
│
├── skills/
│   ├── software-engineering-resources/  2,400+ lines: DSA, system design, DevOps,
│   │                                    Git, industry patterns, tech trends, security
│   ├── java-learning-resources/         Java tutorials, docs, projects
│   ├── career-resources/                Roles, skills, compensation data
│   ├── daily-assistant-resources/       Finance, productivity, news
│   ├── java-build/                      Compile & run help
│   ├── design-patterns/                 SOLID & GoF patterns reference
│   ├── java-debugging/                  Exception diagnosis
│   └── mcp-development/                 MCP protocol & server development (1,980 lines)
│
└── docs/
    ├── START-HERE.md                    ← NEW HERE? Start here — picks your reading path by experience
    ├── phase-guide.md                   8-phase zero-to-operational guide (🟢🟡🔴 tiered)
    ├── mcp-server-setup.md              Complete newbie MCP guide: install, build, credentials, use
    ├── export-guide.md                  Copy features to another project
    ├── copilot-workflow.md              Chat patterns, queuing instructions, token limits
    ├── getting-started.md               Step-by-step tutorial (~30 min)
    ├── customization-guide.md           Architecture deep-dive
    ├── slash-commands.md                All 36 commands reference
    ├── navigation-index.md              Master file & command index
    └── file-reference.md               Copilot vs developer file guide
```

</details>

---

## MCP Servers Module

The `mcp-servers/` directory contains a **Java-based configuration system** and **MCP server implementations** for the Model Context Protocol — the protocol that lets AI assistants connect to external tools and data sources.

### Learning Resources Server (NEW)

The first built-in MCP server — a **web scraper + curated resource vault** with ~100+ hand-picked learning resources:

| Tool | Description |
|------|-------------|
| `search_resources` | Search the vault by text, category, type, or difficulty |
| `browse_vault` | Browse all resources grouped by category or type |
| `get_resource` | Get full details for a specific resource |
| `list_categories` | List all available resource categories |
| `discover_resources` | Smart 3-mode discovery (specific/vague/exploratory) |
| `scrape_url` | Scrape any URL and get a content summary |
| `read_url` | Scrape any URL and get full readable content |
| `add_resource` | Add a new resource to the vault |
| `add_resource_from_url` | Scrape URL → auto-extract metadata → add to vault |
| `export_results` | Export results as Markdown, PDF, or Word |

```bash
# Try it:
cd mcp-servers
javac -d out src/**/*.java
java -cp out server.learningresources.LearningResourcesServer --demo
java -cp out server.learningresources.LearningResourcesServer --list-tools
```

See [Learning Resources Server README](mcp-servers/src/server/learningresources/README.md) for full documentation.

### Configuration System

**What it manages:**
- API keys and secrets (GitHub, OpenAI, Slack, databases)
- Location preferences (timezone, locale, cloud region)
- User preferences (theme, log level, retries, timeouts)
- Browser isolation (dedicated profile to prevent MCP servers from hijacking personal tabs)
- Server definitions (stdio, SSE, streamable-http transports)
- Named profiles (development, production, testing) with config merging
- Environment variable overrides (`MCP_*` prefix)

**Quick start:**
```bash
cp mcp-servers/user-config/mcp-config.example.properties mcp-servers/user-config/mcp-config.properties
# Replace <<<PLACEHOLDER>>> values (search for "<<<")
```

See the [MCP Servers README](mcp-servers/README.md) for the full setup guide, architecture docs, and how to add new servers.

---

## What's Covered

<details>
<summary><b>Technical & Engineering</b> (click to expand)</summary>

| Domain | Coverage |
|---|---|
| Data Structures & Algorithms | Arrays, trees, graphs, heaps, tries, DP, sorting, searching — with complexity tables |
| System Design | Scaling, caching, CDN, replication, OOP modeling, API design, case studies |
| DevOps & CI/CD | Jenkins, GitHub Actions, Docker, Kubernetes, deployment strategies, GitOps |
| Build Tools | Maven lifecycle/POM/scopes, Gradle phases/DSL, Ant, Bazel |
| Version Control | 60+ Git commands, Git Flow, Trunk-Based Development, recovery, internals |
| SDLC | 8-phase E2E lifecycle, Agile/Scrum/Kanban/XP/SAFe, frontend/backend/DB aspects |
| Programming Languages | Java 21+, Python, Go, Rust, C++, JS/TS, Kotlin via `/language-guide` |
| Frameworks | Spring, FastAPI, React, Angular, PostgreSQL, MongoDB, Redis, Kafka |
| Operating Systems | Process management, memory, file systems, scheduling |
| Networking | TCP/UDP, HTTP/HTTPS, DNS, TLS, gRPC, REST, WebSocket |
| Databases | SQL, NoSQL, indexing, normalization, ACID, CAP theorem |
| Distributed Systems | Consensus (Raft, Paxos), consistency models, partitioning |
| Security | OWASP Top 10, zero trust, supply chain, SBOM, secrets management |
| Testing | TDD, BDD, unit/integration/e2e, mutation testing, test pyramid |
| Design Patterns | GoF patterns, SOLID, GRASP, clean architecture |

</details>

<details>
<summary><b>Industry & Real-World Systems</b> (click to expand)</summary>

| Topic | Coverage |
|---|---|
| Rate Limiting | Token bucket, sliding window — Netflix, Cloudflare, Stripe implementations |
| Circuit Breakers | Hystrix, Resilience4j, state machine patterns |
| Transformers & LLMs | 2017–2025 evolution: BERT → GPT → Claude → Gemini |
| Event-Driven Architecture | Kafka, CQRS, event sourcing — Uber, LinkedIn, Netflix |
| Microservices Patterns | Saga, outbox, strangler fig, BFF, service mesh, API gateway |
| Twelve-Factor App | All 12 principles with modern cloud-native implementations |
| Observability at Scale | How top companies do metrics, logs, traces, incidents |
| Engineering Practices | 10+ companies' approaches documented |
| Engineering Blogs | 13 curated sources (Netflix, Uber, Spotify, Stripe, Google, etc.) |

</details>

<details>
<summary><b>Tech Trends (2025–2026)</b> (click to expand)</summary>

| Area | Trends |
|---|---|
| AI in Engineering | Coding assistants, agentic AI, AI code review, vibe coding, RAG |
| Infrastructure | Platform engineering, eBPF, GitOps, serverless v2, cloud dev envs |
| Languages | Rust adoption, Java Loom, Zig, Kotlin Multiplatform |
| Data | Vector databases, data lakehouse, streaming-first, feature stores |
| Architecture | Modular monolith, cell-based, edge computing, zero trust |

</details>

<details>
<summary><b>Career & Daily Life</b> (click to expand)</summary>

| Domain | Coverage |
|---|---|
| Career Exploration | 10+ tech roles with skills matrices, salary data, day-in-the-life |
| Interview Prep | DSA patterns, system design frameworks, behavioral strategies |
| Career Roadmaps | Transition plans between roles with timelines |
| Finance | Budget tracking, investment basics, expense analysis |
| Productivity | Time management, habit tracking, daily planning |
| News & Research | Tech news summaries, web research |

</details>

---

## How the AI Features Work

The `.github/` directory uses GitHub Copilot's 5 customization primitives:

| Primitive | What It Does | Example |
|---|---|---|
| **Instructions** (`.instructions.md`) | Auto-applied coding rules per file type | Java naming conventions load when editing `.java` |
| **Agents** (`.agent.md`) | Specialist personas you select from a dropdown | "Learning Mentor" teaches with analogies and exercises |
| **Prompts** (`.prompt.md`) | Slash commands that run pre-built workflows | `/dsa → binary search → python` produces a full lesson |
| **Skills** (`SKILL.md`) | Knowledge packs auto-loaded by topic match | Ask about "rate limiting" → industry patterns load automatically |
| **Project Instructions** (`copilot-instructions.md`) | Always-on project rules | Naming conventions, method size limits, Javadoc requirements |

For the full architecture, see the [Customization Guide](.github/docs/customization-guide.md).

---

## Documentation

| Doc | Purpose | Audience | Time |
|---|---|---|---|
| [**USAGE.md →**](USAGE.md) | **All-in-one guide: setup, Copilot, MCP servers, search engine, learning paths** | 🟢🟡🔴 All | ~45 min |
| [**START HERE →**](.github/docs/START-HERE.md) | **Pick your reading path based on experience** | 🟢🟡🔴 All | ~2 min |
| [Phase Guide](.github/docs/phase-guide.md) | Step-by-step: orient, build, configure, use, export | 🟢🟡🔴 All | ~20 min |
| [Getting Started](.github/docs/getting-started.md) | Hands-on tutorial — try every feature | 🟢🟡 | ~30 min |
| [MCP Server Setup](.github/docs/mcp-server-setup.md) | Complete newbie guide: install, credentials, verify, copy | 🟢🟡 | ~10 min |
| [Copilot Workflow](.github/docs/copilot-workflow.md) | Chat patterns, queuing instructions, token limits | 🟢🟡🔴 | ~10 min |
| [Export Guide](.github/docs/export-guide.md) | Copy features to another project | 🟡🔴 | ~10 min |
| [Customization Guide](.github/docs/customization-guide.md) | How the 5 primitives connect | 🟡🔴 | ~20 min |
| [Slash Commands](.github/docs/slash-commands.md) | All 36 commands — inputs, aliases, composition | 🔴 | ~5 min |
| [Navigation Index](.github/docs/navigation-index.md) | Master lookup — commands, agents, skills, files | 🔴 | ~5 min |
| [File Reference](.github/docs/file-reference.md) | Which files Copilot reads vs. developer docs | 🟡🔴 | ~5 min |
| [MCP Servers Deep Dive](mcp-servers/README.md) | Config architecture, Java sources, adding servers, browser isolation | 🟡🔴 | ~10 min |
| [Copilot README](.github/README.md) | Deep dive into all 5 Copilot primitives | 🟡🔴 | ~10 min |

---

## Extending & Contributing

The architecture is designed to grow. Add your own:

| What | How | Guide |
|---|---|---|
| New AI agent | Create `*.agent.md` in `.github/agents/` | [Agents Guide](.github/agents/README.md) |
| New slash command | Create `*.prompt.md` in `.github/prompts/` | [Prompts Guide](.github/prompts/README.md) |
| New knowledge pack | Create `<name>/SKILL.md` in `.github/skills/` | [Skills Guide](.github/skills/README.md) |
| New coding rules | Create `*.instructions.md` in `.github/instructions/` | [Instructions Guide](.github/instructions/README.md) |
| New MCP server | Add `server.{name}.*` block in `mcp-servers/user-config/mcp-config.properties` | [MCP Servers Guide](mcp-servers/README.md) |
| New code experiments | Add files in `src/` in any language | Just start coding |

Contributions, ideas, and forks are welcome. If you build something interesting on top of this structure, open an issue — I'd love to hear about it.

---

## Built With

| Component | Technology |
|---|---|
| **Primary Language** | Java 21+ (sandbox — expandable to any language) |
| **IDE** | VS Code |
| **AI** | GitHub Copilot (agents, prompts, skills, instructions) |
| **Knowledge Base** | Markdown (auto-loaded by Copilot as skill files) |

---

## License

MIT — use it, fork it, learn from it, build on it.

---

<p align="center">
<b>Get started:</b> Clone → open in VS Code → type <code>/hub</code> in Chat → explore
<br><br>
If this helped you learn something, consider giving it a ⭐
</p>
