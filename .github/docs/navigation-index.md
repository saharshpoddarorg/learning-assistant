# 🗺️ Navigation Index — Developer Quick Reference

> 🟢 **New here?** → Start at **[START-HERE.md](START-HERE.md)** — it tells you what to read based on experience level.

> **Audience:** 👤 Developer  
> **Purpose:** Master index of ALL files, slash commands, agents, and skills — with quick lookup by need.  
> **When to use:** When you're not sure which command or file does what you need.

---

## 📑 Table of Contents

- [Slash Command Quick Reference](#-slash-command-quick-reference) · [Full Slash Command Reference →](slash-commands.md)
- [Agents Quick Reference](#-agents-quick-reference)
- [Skills Quick Reference](#-skills-quick-reference)
- [File Map — All .md Files](#-file-map--all-md-files)
- [Find by Need](#-find-by-need)
- [Command Composition Guide](#-command-composition-guide)
- [Session Management](#-session-management)

---

## ⚡ Slash Command Quick Reference

### Navigation & Meta
| Command | Purpose | Agent | Quick Example |
|---|---|---|---|
| `/hub` | Browse all commands by category | Learning-Mentor | `/hub` → `se` |
| `/composite` | Combine multiple modes in one session | Agent | `/composite` → `refactor, design-review` |
| `/context` | Continue prior conversation or start fresh | Agent | `/context` → `continue` |
| `/scope` | Set generic vs code-specific scope | Agent | `/scope` → `generic` |
| `/multi-session` | Manage state across chat sessions | Agent | `/multi-session` → `save-state` |
| `/steer` | View or switch the active steering mode | Copilot | `/steer` → `view` |

### Learning & Concepts
| Command | Purpose | Agent | Quick Example |
|---|---|---|---|
| `/learn-concept` | Learn any CS/SE concept | Learning-Mentor | `/learn-concept` → `deadlocks` |
| `/deep-dive` | Progressive multi-layered exploration | Learning-Mentor | `/deep-dive` → `generics` |
| `/learn-from-docs` | Learn via official documentation | Learning-Mentor | `/learn-from-docs` → `sealed classes` |
| `/reading-plan` | Create a structured study plan | Learning-Mentor | `/reading-plan` → `design patterns` |
| `/teach` | Learn concepts from current file | Learning-Mentor | `/teach` → (open a file first) |

### Domain-Specific Learning
| Command | Purpose | Agent | Quick Example |
|---|---|---|---|
| `/dsa` | Data structures & algorithms | Learning-Mentor | `/dsa` → `binary trees` |
| `/system-design` | HLD/LLD system design | Learning-Mentor | `/system-design` → `HLD` → `URL shortener` |
| `/devops` | CI/CD, Docker, K8s, cloud, IaC, Git, build tools | Learning-Mentor | `/devops` → `Docker` |
| `/language-guide` | Language-specific learning path | Learning-Mentor | `/language-guide` → `Rust` |
| `/tech-stack` | Frameworks, databases, compare tools | Learning-Mentor | `/tech-stack` → `compare Spring vs FastAPI` |
| `/sdlc` | SDLC phases, methodologies, E2E lifecycle | Learning-Mentor | `/sdlc` → `testing` |
| `/mcp` | Learn & build MCP (Model Context Protocol) servers | Learning-Mentor | `/mcp` → `build-server` → `java` |
| `/interview-prep` | Interview preparation | Learning-Mentor | `/interview-prep` → `DSA` → `sliding window` |
| `/resources` | Search, browse & scrape learning resources | Learning-Mentor | `/resources` → `search` → `java concurrency` |

### Career
| Command | Purpose | Agent | Quick Example |
|---|---|---|---|
| `/career-roles` | Job roles, skills, pay, roadmaps | Learning-Mentor | `/career-roles` → `MLE` → `overview` |

### Code Quality & Analysis
| Command | Purpose | Agent | Quick Example |
|---|---|---|---|
| `/design-review` | SOLID/GRASP design review | Designer | `/design-review` (with file open) |
| `/refactor` | Identify refactoring opportunities | Designer | `/refactor` (with file open) |
| `/explain` | Beginner-friendly file explanation | Ask | `/explain` (with file open) |
| `/debug` | Systematic bug investigation | Debugger | `/debug` (with file open) |
| `/impact` | Change impact analysis | Impact-Analyzer | `/impact` (with file open) |
| `/explore-project` | Learn from open-source projects | Learning-Mentor | `/explore-project` → `Redis` |

### Daily Life
| Command | Purpose | Agent | Quick Example |
|---|---|---|---|
| `/daily-assist` | Finance, productivity, news, research | Daily-Assistant | `/daily-assist` → `finance` |

### Brain Workspace
| Command | Purpose | Agent | Quick Example |
|---|---|---|---|
| `/brain-new` | Create a new knowledge note (inbox or notes tier) | Copilot | `/brain-new` → `"generics cheatsheet"` → `notes` |
| `/brain-publish` | Publish note to archive/ with tagging and git commit | Copilot | `/brain-publish` → `inbox/2026-02-21_draft.md` |
| `/brain-search` | Search notes by tag, project, kind, date, or text | Copilot | `/brain-search` → `"sse transport"` → `tier=archive` |
| `/brain-capture-session` | Convert current AI session into a structured session note | Copilot | `/brain-capture-session` → `topic` → `full` |

---

## 🤖 Agents Quick Reference

| Agent | Select When You Want To... | Tools |
|---|---|---|
| **Learning-Mentor** | Learn concepts, get explanations with analogies | search, codebase, usages, fetch |
| **Designer** | Review architecture, SOLID, design patterns | search, codebase, usages, fetch |
| **Debugger** | Debug systematically with hypothesis-driven approach | search, codebase, debugger, terminal |
| **Impact-Analyzer** | Understand ripple effects of a change | search, codebase, usages, problems |
| **Code-Reviewer** | Get code quality review (read-only) | search, codebase, usages |
| **Daily-Assistant** | Daily life tasks (finance, productivity, news) | search, fetch |
| **Thinking-Beast-Mode** | Deep research — autonomous, thorough, web-fetching | search, codebase, editFiles, fetch, terminal |

---

## 🛠️ Skills Quick Reference

| Skill | Auto-Triggers On | What It Provides |
|---|---|---|
| `java-build` | Compile, run, build | Java compilation commands |
| `design-patterns` | Patterns, SOLID | Pattern decision guide |
| `java-debugging` | Exceptions, debugging | Exception diagnosis |
| `java-learning-resources` | Java tutorials, docs | Java resource index |
| `software-engineering-resources` | DSA, system design, OS, DevOps, Git, build tools, security, industry, trends | Comprehensive SE/CS resources |
| `daily-assistant-resources` | Finance, productivity, news | Daily life resources |
| `career-resources` | Job roles, salaries, career | Career data and roadmaps |
| `mcp-development` | MCP servers, protocol, tools, agents | MCP server setup, architecture & dev guide (1,980 lines) |
| `digital-notetaking` | PKM, PARA, Obsidian, Notion, Logseq, Zettelkasten, second brain, note-taking | Tool comparison, note templates (ADR, sprint log, snippet), migration guides, JDK commands |
| `mac-dev` | Homebrew, JDK on Mac, nvm, Docker Desktop, zsh, dotfiles | macOS dev environment cheatsheets — install, configure, automate |

---

## 📁 File Map — All .md Files

```
.github/
│
├── (root) README.md                    👤 Project overview, quick start, learning domains
│
├── copilot-instructions.md          🤖 Project-wide rules (always loaded)
│
├── instructions/
│   ├── README.md                    👤 How instructions work
│   ├── java.instructions.md         🤖 Java coding standards
│   ├── clean-code.instructions.md   🤖 Clean code practices
│   ├── change-completeness.instructions.md  🤖 Completeness checklist — DEFAULT steering mode (applyTo: **)
│   └── steering-modes.instructions.md  🤖 All steering modes — completeness | beast | learning | design | debug | focused
│
├── agents/
│   ├── README.md                    👤 How agents work
│   ├── designer.agent.md            🤖 Architecture & design persona
│   ├── debugger.agent.md            🤖 Debugging persona
│   ├── impact-analyzer.agent.md     🤖 Impact analysis persona
│   ├── learning-mentor.agent.md     🤖 Teaching persona
│   ├── code-reviewer.agent.md       🤖 Code review persona
│   ├── daily-assistant.agent.md     🤖 Daily life persona
│   └── Thinking-Beast-Mode.agent.md 🤖 Deep research persona
│
├── prompts/
│   ├── README.md                    👤 How prompts work
│   │
│   │── [Navigation & Meta]
│   ├── hub.prompt.md                🤖 /hub — master navigation
│   ├── composite.prompt.md          🤖 /composite — combine modes
│   ├── context.prompt.md            🤖 /context — continue/fresh
│   ├── scope.prompt.md              🤖 /scope — generic/specific
│   ├── multi-session.prompt.md      🤖 /multi-session — cross-session state
│   ├── steer.prompt.md              🤖 /steer — view or switch steering mode (default: completeness)
│   │
│   │── [Learning & Concepts]
│   ├── learn-concept.prompt.md      🤖 /learn-concept — any concept
│   ├── deep-dive.prompt.md          🤖 /deep-dive — progressive mastery
│   ├── learn-from-docs.prompt.md    🤖 /learn-from-docs — official docs
│   ├── reading-plan.prompt.md       🤖 /reading-plan — study plan
│   ├── teach.prompt.md              🤖 /teach — learn from code
│   │
│   │── [Domain-Specific]
│   ├── dsa.prompt.md                🤖 /dsa — data structures & algorithms
│   ├── system-design.prompt.md      🤖 /system-design — HLD/LLD
│   ├── devops.prompt.md             🤖 /devops — CI/CD, containers, cloud
│   ├── language-guide.prompt.md     🤖 /language-guide — any language
│   ├── tech-stack.prompt.md         🤖 /tech-stack — frameworks, DBs
│   ├── sdlc.prompt.md               🤖 /sdlc — lifecycle & methods
│   ├── mcp.prompt.md                🤖 /mcp — MCP protocol & server development
│   ├── interview-prep.prompt.md     🤖 /interview-prep — interviews
│   ├── career-roles.prompt.md       🤖 /career-roles — job roles & pay
│   ├── resources.prompt.md          🤖 /resources — learning resource vault
│   ├── git-vcs.prompt.md            🤖 /git-vcs — Git workflows, branching, conventions
│   ├── build-tools.prompt.md        🤖 /build-tools — Maven, Gradle, Make, Bazel, npm
│   ├── mac-dev.prompt.md            🤖 /mac-dev — macOS dev environment
│   ├── digital-notetaking.prompt.md 🤖 /digital-notetaking — PKM, tools, Obsidian, Notion, PARA
│   │
│   │── [Code Quality]
│   ├── design-review.prompt.md      🤖 /design-review — SOLID review
│   ├── debug.prompt.md              🤖 /debug — bug investigation
│   ├── impact.prompt.md             🤖 /impact — change analysis
│   ├── refactor.prompt.md           🤖 /refactor — refactoring
│   ├── explain.prompt.md            🤖 /explain — file explanation
│   ├── explore-project.prompt.md    🤖 /explore-project — OSS study
│   │
│   │── [Daily Life]
│   └── daily-assist.prompt.md       🤖 /daily-assist — daily tasks
│
│   │── [Brain Workspace]
│   ├── brain-new.prompt.md          🤖 /brain-new — create inbox/notes note
│   ├── brain-publish.prompt.md      🤖 /brain-publish — publish to archive & commit
│   ├── brain-search.prompt.md       🤖 /brain-search — search across tiers
│   └── brain-capture-session.prompt.md  🤖 /brain-capture-session — convert AI session to session note
│
├── skills/
│   ├── README.md                    👤 How skills work
│   ├── java-build/SKILL.md          🤖 Compile & run
│   ├── design-patterns/SKILL.md     🤖 Pattern guide
│   ├── java-debugging/SKILL.md      🤖 Exception diagnosis
│   ├── java-learning-resources/SKILL.md  🤖 Java resources
│   ├── software-engineering-resources/SKILL.md  🤖 SE/CS resources
│   ├── daily-assistant-resources/SKILL.md  🤖 Daily life resources
│   ├── career-resources/SKILL.md    🤖 Career data
│   ├── digital-notetaking/SKILL.md  🤖 PKM methods, tool cheatsheets, note templates, JDK commands
│   ├── mac-dev/SKILL.md             🤖 Homebrew, JDK, npm, Docker, shell, dotfiles cheatsheets
│   └── mcp-development/SKILL.md     🤖 MCP protocol & server development (1,980 lines)
│
└── docs/
    ├── getting-started.md           👤 Hands-on tutorial
    ├── customization-guide.md       👤 Architecture deep-dive
    ├── mcp-server-setup.md          👤 Complete MCP setup guide (newbie-friendly, install → verify → copy to other project)
    ├── file-reference.md            👤 Who reads what (🤖 vs 👤)
    ├── navigation-index.md          👤 This file — master index
    ├── slash-commands.md            👤 Developer slash command reference
    ├── architecture-overview.md     👤 Module architecture, design patterns, industry standards (merged search-engine design)
    ├── mcp-servers-architecture.md  👤 MCP protocol internals, server lifecycle, tool dispatch, config system
    ├── mcp-how-it-works.md          👤 How MCP works behind-the-scenes (JSON-RPC, STDIO, session lifecycle, how LLM invokes tools)
    ├── mcp-implementations.md       👤 MCP in Java vs JavaScript/TypeScript — side-by-side code comparison
    ├── mcp-ecosystem.md             👤 Combining servers, LLM APIs (OpenAI/Anthropic/Gemini), LangChain, LlamaIndex, agents, multi-language
    ├── local-setup-guide.md         👤 What's gitignored + how to set it up — config files, secrets, 3-tier onboarding (🟢/🟡/🔴)
    ├── versioning-guide.md          👤 Server versioning strategy (McpServer interface, registry, package-per-version pattern)
    ├── search-engine.md             👤 Search engine developer guide (🟢 Newbie / 🟡 Amateur / 🔴 Pro)
    └── search-engine-algorithms.md  👤 BM25, TextMatchScorer, FuzzyMatcher, QueryClassifier deep-dive

brain/
│
├── README.md                        👤 Brain module overview — note workspace + PKM guide library
│
├── digitalnotetaking/               👤 PKM guide library for developers
│   ├── README.md                    👤 Module overview + Copilot integration guide
│   ├── START-HERE.md                👤 Onboarding: pick a tool, set up PARA, capture first note
│   ├── tools-comparison.md          👤 Notion vs Obsidian vs Logseq vs OneNote — decision guide
│   ├── para-method.md               👤 PARA method applied to devs (Obsidian, Notion, Logseq, ai-brain)
│   ├── code-method.md               👤 CODE method guide — Capture, Organize, Distill (Progressive Summarization), Express
│   ├── ai-brain-integration.md      👤 Linking AI sessions to PKM — session lifecycle, Obsidian/Notion/Logseq, /brain-capture-session
│   ├── templates.md                 👤 Note templates: ADR, daily log, snippet, resource, debug, meeting
│   └── migration-guide.md           👤 Step-by-step migration: Notion→Obsidian, OneNote→Notion, etc.
│
├── ai-brain/
│   ├── README.md                    👤 Live workspace guide — 3 tiers, scripts, frontmatter schema
│   ├── inbox/                       🔒 Gitignored — quick capture (drop anything here)
│   ├── notes/                       🔒 Gitignored — curated notes (stays on this machine)
│   ├── archive/                     ✅ Git-tracked — published notes, permanent reference
│   └── scripts/                     👤 brain.ps1 / brain.sh CLI + brain-module.psm1 aliases
│
└── src/
    ├── Main.java                    👤 Entry point
    └── digitalnotetaking/           🤖 Java package: NoteKind, NoteStatus, NoteMetadata, NoteTemplate
```

**Legend:** 🤖 = Copilot reads this file | 👤 = Developer documentation only | 🔒 = Gitignored | ✅ = Git-tracked

---

## 🔍 Find by Need

| I want to... | Use this | Type |
|---|---|---|
| **Browse all commands** | `/hub` | Prompt |
| **Learn a concept from scratch** | `/learn-concept` | Prompt |
| **Study with a plan** | `/reading-plan` | Prompt |
| **Learn a programming language** | `/language-guide` | Prompt |
| **Practice DSA / LeetCode** | `/dsa` | Prompt |
| **Prepare for system design interviews** | `/system-design` | Prompt |
| **Learn Docker / K8s / CI-CD** | `/devops` | Prompt |
| **Learn Git commands & branching** | `/devops` → `Git` | Prompt |
| **Learn Maven / Gradle build tools** | `/devops` → `build tools` | Prompt |
| **Search curated learning resources** | `/resources` → `search` | Prompt |
| **Browse resource library by category** | `/resources` → `browse` | Prompt |
| **Scrape & analyze a tutorial URL** | `/resources` → `scrape` → URL | Prompt |
| **Compare frameworks** | `/tech-stack` | Prompt |
| **Understand SDLC phases** | `/sdlc` | Prompt |
| **Learn industry patterns (rate limiter, circuit breaker)** | `/hub industry` or `/learn-concept` → topic | Prompt |
| **Learn tech trends (AI, Wasm, platform eng)** | `/hub trends` or `/learn-concept` → topic | Prompt |
| **Explore a job role / salary** | `/career-roles` | Prompt |
| **Prepare for coding interviews** | `/interview-prep` | Prompt |
| **Review my code's design** | `/design-review` | Prompt |
| **Find and fix bugs** | `/debug` | Prompt |
| **Understand code impact** | `/impact` | Prompt |
| **Refactor code** | `/refactor` | Prompt |
| **Explain a file** | `/explain` | Prompt |
| **Learn from open-source code** | `/explore-project` | Prompt |
| **Plan my day / track habits** | `/daily-assist` → `productivity` | Prompt |
| **Understand finance basics** | `/daily-assist` → `finance` | Prompt |
| **Get tech news summary** | `/daily-assist` → `news` | Prompt |
| **Continue work from last session** | `/multi-session` → `resume` | Prompt |
| **Save progress before ending** | `/multi-session` → `save-state` | Prompt |
| **Combine multiple analyses** | `/composite` | Prompt |
| **Ask Copilot as a specialist** | Select agent from dropdown | Agent |
| **Configure MCP servers** | See [mcp-servers/README.md](../../mcp-servers/README.md) | Config |
| **Add a new MCP server** | Add `server.{name}.*` block in `user-config/mcp-config.properties` | Config |
| **Set up browser isolation** | See [Browser Isolation](../../mcp-servers/README.md#browser-isolation) | Config |
| **Manage API keys for MCP** | Set `apiKeys.*` or `MCP_APIKEYS_*` env var | Config |
| **Understand how MCP protocol works internally** | [mcp-how-it-works.md](mcp-how-it-works.md) | Doc |
| **Compare MCP in Java vs JavaScript** | [mcp-implementations.md](mcp-implementations.md) | Doc |
| **Combine multiple MCP servers / use LangChain + LLM APIs + agents** | [mcp-ecosystem.md](mcp-ecosystem.md) | Doc |
| **Set up local config files, create .local.properties, manage secrets** | [local-setup-guide.md](local-setup-guide.md) | Doc |
| **Onboard a new developer (what to create first)** | [local-setup-guide.md](local-setup-guide.md) — Tier 1 | Doc |
| **Configure Atlassian Cloud / Data Center credentials** | [local-setup-guide.md](local-setup-guide.md) — Tier 2 | Doc |
| **Run multiple Atlassian instances or CI/CD secrets** | [local-setup-guide.md](local-setup-guide.md) — Tier 3 | Doc |
| **Understand how to version MCP servers** | [versioning-guide.md](versioning-guide.md) | Doc |
| **Understand the search engine (newbie)** | [search-engine.md](search-engine.md) — Newbie tier | Doc |
| **Add keywords to search vocabulary** | [search-engine.md](search-engine.md) — Amateur tier | Doc |
| **Tune scoring constants** | [search-engine.md](search-engine.md) — Amateur tier | Doc |
| **Build a custom search engine for a new server** | [search-engine.md](search-engine.md) — Pro tier | Doc |
| **Full search module API reference** | [mcp-servers/src/search/README.md](../../mcp-servers/src/search/README.md) | Doc |
| **Create a knowledge note** | `/brain-new` | Brain |
| **Publish a note to the repo** | `/brain-publish` | Brain |
| **Search my notes** | `/brain-search` | Brain |
| **Use brain from the terminal** | `.\brain\scripts\brain.ps1 <command>` | Script |
| **Use brain short aliases** | `. .\brain\scripts\brain-module.psm1` then `brain <command>` | Script |
| **Learn PKM methods (PARA, CODE, Zettelkasten)** | `/digital-notetaking` → `para-method` | Prompt |
| **Set up Obsidian / Notion / Logseq** | `/digital-notetaking` → tool → level | Prompt |
| **Migrate between note-taking tools** | `/digital-notetaking` → `migration` | Prompt |
| **Browse note templates (ADR, daily log, snippet)** | [brain/digitalnotetaking/templates.md](../../brain/digitalnotetaking/templates.md) | Doc |
| **Compare Notion vs Obsidian vs Logseq** | [brain/digitalnotetaking/tools-comparison.md](../../brain/digitalnotetaking/tools-comparison.md) | Doc |
| **Understand PARA method for devs** | [brain/digitalnotetaking/para-method.md](../../brain/digitalnotetaking/para-method.md) | Doc |
| **Understand CODE method (Capture/Organize/Distill/Express)** | [brain/digitalnotetaking/code-method.md](../../brain/digitalnotetaking/code-method.md) | Doc |
| **Link AI sessions to your PKM / note workspace** | [brain/digitalnotetaking/ai-brain-integration.md](../../brain/digitalnotetaking/ai-brain-integration.md) | Doc |
| **Capture current AI session as a structured note** | `/brain-capture-session` | Brain |
| **New to PKM? Start here** | [brain/digitalnotetaking/START-HERE.md](../../brain/digitalnotetaking/START-HERE.md) | Doc |
| **View or switch Copilot steering mode** | `/steer` | Meta |
| **See all steering modes and which is active** | `/steer` → `view` | Meta |
| **Switch to deep-research mode** | `/steer` → `switch` → `beast` | Meta |

---

## 🔀 Command Composition Guide

Commands can be **composed** to create powerful workflows. Here are recommended patterns:

### Learning Workflow
```
/career-roles → identify skills gap
    ↓
/language-guide → learn required language
    ↓
/dsa → practice required algorithms
    ↓
/system-design → learn required design skills
    ↓
/reading-plan → create structured study plan
    ↓
/interview-prep → practice for interviews
```

### Code Quality Workflow
```
/explain → understand the code
    ↓
/design-review → find design issues
    ↓
/refactor → fix the issues
    ↓
/impact → check what the changes affect
    ↓
/composite refactor,design-review → unified analysis
```

### Cross-Session Workflow
```
Session 1:  /multi-session save-state → saves progress
Session 2:  /multi-session resume → continues from checkpoint
            /context continue → reconnects to prior decisions
```

### DevOps Learning Path
```
/devops → Docker → learn containers
    ↓
/devops → Kubernetes → learn orchestration
    ↓
/devops → GitHub Actions → set up CI/CD
    ↓
/tech-stack → compare cloud providers
    ↓
/career-roles → DevOps/SRE → see career path
```

### Meta-Composition with `/composite`
```
/composite → refactor, design-review
  → Combines both into a single deduplicated analysis
  → Shows cross-mode insights

/composite → debug, impact
  → Debug the issue AND assess fix impact in one pass
```

---

## 🔄 Session Management

### Token Limit Strategies

| Strategy | When to Use | Command |
|---|---|---|
| **Save state** | Before a long session might hit limits | `/multi-session` → `save-state` |
| **Resume** | Starting a new session to continue work | `/multi-session` → `resume` |
| **Handoff** | Clean transfer to a new session | `/multi-session` → `handoff` |
| **Status check** | Unsure if context is still intact | `/multi-session` → `status` |
| **Context continue** | Within a session, reconnect to prior discussion | `/context` → `continue` |
| **Partition tasks** | Large task → split into focused sub-sessions | Split into independent tasks, each in its own chat |

### Session State File

The `/multi-session` prompt can create/update `.github/session-state.md` — a persistent checkpoint file:

```
.github/
└── session-state.md    ← Auto-generated session checkpoint (gitignored or tracked, your choice)
```

---

<p align="center">

[← Main README](../README.md) · [Getting Started](getting-started.md) · [Customization Guide](customization-guide.md) · [File Reference](file-reference.md) · [Slash Commands](slash-commands.md)

</p>
