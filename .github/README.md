# learning-assistant

> **Purpose:** Learn how to customize GitHub Copilot using all 6 official primitives.
> **Project:** `learning-assistant` — a simple Java project for hands-on experimentation.
> **Audience:** Developers new to Copilot customization who want to learn by doing.

---

## 📑 Table of Contents

- [At a Glance](#-at-a-glance)- [Why Customize Copilot?](#why-customize-copilot)
- [The 6 Official Primitives](#the-6-official-primitives)
- [Folder Structure](#folder-structure)
- [This Project's Setup](#this-projects-current-setup)
  - [Specialist Agents](#-specialist-agents-modes)
  - [Slash Commands](#-slash-commands-prompts)
  - [Auto-Applied Instructions](#-auto-applied-instructions)
  - [Auto-Loaded Skills](#-auto-loaded-skills)
  - [Handoff Workflows](#-agent-workflow-handoffs)
- [How It All Connects](#how-these-work-together)
- [Priority Order](#priority-order)
- [Environment Support](#environment-support)
- [Documentation Map](#-documentation-map)
- [Learning Path](#-learning-path)
- [Reference Links](#-reference-links)

---

## 🚦 New Here? Pick Your Path

| Experience | Read these docs (in order) |
|---|---|
| 🟢 **Newbie** — never used Copilot customization | [START HERE](.github/docs/START-HERE.md) → [Phase Guide](docs/phase-guide.md) → [Getting Started](docs/getting-started.md) |
| 🟡 **Amateur** — used Copilot, new to MCP/agents | [START HERE](docs/START-HERE.md) → [MCP Server Setup](docs/mcp-server-setup.md) → [Navigation Index](docs/navigation-index.md) |
| 🔴 **Pro** — knows the primitives, setting up fast | [File Reference](docs/file-reference.md) → [Slash Commands](docs/slash-commands.md) → [Customization Guide](docs/customization-guide.md) |

---

## ⚡ At a Glance

| Action | How |
|---|---|
| **Switch persona** | Chat dropdown → select **Designer**, **Debugger**, **Impact-Analyzer**, **Learning-Mentor**, **Code-Reviewer**, **Daily-Assistant**, **My-Kanha**, or **Thinking-Beast-Mode** |
| **Run a workflow** | Type `/hub` in Chat to browse all 65 slash commands across 10 categories |
| **Coding standards** | Automatic — open any `.java` file, instructions load via glob match |
| **Extra knowledge** | Automatic — ask about building, patterns, or debugging and the matching skill loads |
| **See everything** | [Navigation Index](docs/navigation-index.md) · [Slash Commands](docs/slash-commands.md) · [Documentation Map](#-documentation-map) · [File Reference](docs/file-reference.md) · [Getting Started Tutorial](docs/getting-started.md) |

---

## Why Customize Copilot?

Out of the box, Copilot knows nothing about **your** project. It guesses based on generic training data. Customization fixes that:

| Without Customization | With Customization |
|---|---|
| Copilot suggests `System.out.println` | Copilot uses your preferred `Logger` |
| Generic variable names like `list1` | Follows your naming conventions |
| Doesn't know your project structure | Knows which files do what |
| You repeat instructions every chat session | Instructions persist in files |

---

## The 6 Official Primitives

GitHub Copilot in VS Code supports exactly **6 customization primitives**. Everything you can do falls into one of these:

```text
┌─────────────────────────────────────────────────────────────────────┐
│                    COPILOT CUSTOMIZATION                            │
│                                                                     │
│  1. copilot-instructions.md    Always-on project rules              │
│  2. *.instructions.md          Conditional rules per file type      │
│  3. *.agent.md                 Custom AI personas                   │
│  4. *.prompt.md                Reusable slash-command tasks          │
│  5. SKILL.md                   Tool folders with scripts/resources  │
│  6. MCP Servers (.vscode/mcp.json)  Live external data & tools     │
└─────────────────────────────────────────────────────────────────────┘
```

### Quick Decision Guide

| Need to... | Use this primitive |
|---|---|
| Set project-wide rules Copilot always follows | `copilot-instructions.md` |
| Add rules only when editing certain file types | `*.instructions.md` |
| Create a specialist persona (reviewer, planner) | `*.agent.md` |
| Save a reusable task as a `/slash-command` | `*.prompt.md` |
| Bundle instructions + scripts + templates | `SKILL.md` folder |
| Connect to external tools and live data | MCP server (`.vscode/mcp.json`) |

---

## Folder Structure

```text
.github/
│
├── copilot-instructions.md              ← Always-on (auto-loaded every request)
│
├── instructions/                        ← Path-specific (auto-loaded by glob)
│   ├── 📋 README.md                         Guide: how instructions work
│   ├── java.instructions.md                 applyTo: "**/*.java"
│   └── clean-code.instructions.md           applyTo: "**/*.java"
│
├── agents/                              ← Custom agents (select from dropdown)
│   ├── 🤖 README.md                         Guide: how agents work
│   ├── designer.agent.md                   Architecture & design review
│   ├── debugger.agent.md                   Systematic debugging
│   ├── impact-analyzer.agent.md            Change impact analysis
│   ├── learning-mentor.agent.md            Teaching & learning
│   ├── code-reviewer.agent.md             Read-only code review
│   ├── daily-assistant.agent.md            Daily life assistant (non-SE)
│   └── Thinking-Beast-Mode.agent.md        Deep research agent (autonomous)
│
├── prompts/                             ← Slash commands (type /command)
│   ├── 🎯 README.md                         Guide: how prompts work
│   ├── meta/                                Navigation, state, workflow (9 commands)
│   │   ├── hub.prompt.md                   /hub (master navigation index)
│   │   ├── composite.prompt.md             /composite (combine modes)
│   │   ├── context.prompt.md               /context (continue/fresh)
│   │   ├── scope.prompt.md                 /scope (generic/specific)
│   │   ├── multi-session.prompt.md         /multi-session (cross-session state)
│   │   ├── steer.prompt.md                 /steer (steering mode selection)
│   │   ├── request-steering.prompt.md      /request-steering (mid-task routing)
│   │   ├── session-scope.prompt.md         /session-scope (scope management)
│   │   └── write-docs.prompt.md            /write-docs (documentation writing)
│   ├── learning/                            Learning & concepts (5 commands)
│   │   ├── learn-concept.prompt.md         /learn-concept (any CS/SE concept)
│   │   ├── deep-dive.prompt.md             /deep-dive (concept mastery)
│   │   ├── learn-from-docs.prompt.md       /learn-from-docs (official docs)
│   │   ├── reading-plan.prompt.md          /reading-plan (study plan)
│   │   └── teach.prompt.md                 /teach (learn from current file)
│   ├── domain/                              Domain-specific learning (10 commands)
│   │   ├── dsa.prompt.md                   /dsa (data structures & algorithms)
│   │   ├── system-design.prompt.md         /system-design (HLD/LLD hierarchy)
│   │   ├── devops.prompt.md                /devops (CI/CD, Docker, K8s, cloud)
│   │   ├── sdlc.prompt.md                  /sdlc (phases & methodologies)
│   │   ├── tech-stack.prompt.md            /tech-stack (frameworks, databases)
│   │   ├── language-guide.prompt.md        /language-guide (language learning)
│   │   ├── mcp.prompt.md                   /mcp (MCP servers, agents, API integration)
│   │   ├── explore-project.prompt.md       /explore-project (OSS study)
│   │   ├── resources.prompt.md             /resources (learning resource vault)
│   │   └── digital-notetaking.prompt.md    /digital-notetaking (PKM tools)
│   ├── code/                                Code quality & analysis (8 commands)
│   │   ├── design-review.prompt.md         /design-review (SOLID/GRASP review)
│   │   ├── debug.prompt.md                 /debug (bug investigation)
│   │   ├── code-analysis.prompt.md         /code-analysis (code review)
│   │   ├── code-analysis-deep-dive.prompt.md /code-analysis-deep-dive (code internals)
│   │   ├── impact.prompt.md                /impact (change analysis)
│   │   ├── refactor.prompt.md              /refactor (refactoring)
│   │   ├── explain.prompt.md               /explain (file explanation)
│   │   └── check-standards.prompt.md       /check-standards (audit best practices)
│   ├── customization/                       Copilot customization (3 commands)
│   │   ├── copilot-customization.prompt.md /copilot-customization (primitives guide)
│   │   ├── create-agent.prompt.md          /create-agent (agent scaffolding)
│   │   └── mcp-to-skill.prompt.md          /mcp-to-skill (MCP→skill conversion)
│   ├── tools/                               Tool-specific operations (6 commands)
│   │   ├── atlassian-tools.prompt.md       /atlassian-tools (Jira/Confluence/Bitbucket)
│   │   ├── git-vcs.prompt.md               /git-vcs (Git commands & workflows)
│   │   ├── github-workflow.prompt.md       /github-workflow (PRs, issues, gh CLI)
│   │   ├── build-tools.prompt.md           /build-tools (Maven, Gradle, npm)
│   │   ├── mac-dev.prompt.md               /mac-dev (macOS dev environment)
│   │   └── read-url.prompt.md              /read-url (webpage reading)
│   ├── career/                              Career & daily life (3 commands)
│   │   ├── career-roles.prompt.md          /career-roles (job roles, skills, pay)
│   │   ├── interview-prep.prompt.md        /interview-prep (DSA/system design)
│   │   └── daily-assist.prompt.md          /daily-assist (finance, productivity)
│   ├── shipping/                            Deployment & release (2 commands)
│   │   ├── ship.prompt.md                  /ship (release workflow)
│   │   └── github-push.prompt.md           /github-push (push & PR)
│   ├── backlog/                             Backlog & task management (5 commands)
│   │   ├── backlog.prompt.md               /backlog (agile board)
│   │   ├── jot.prompt.md                   /jot (quick capture)
│   │   ├── read-file-jot.prompt.md         /read-file-jot (capture from file)
│   │   ├── todo.prompt.md                  /todo (single task)
│   │   └── todos.prompt.md                 /todos (batch tasks)
│   └── brain/                               Brain workspace (14 commands)
│       ├── new.prompt.md                   /brain-new (create knowledge note)
│       ├── publish.prompt.md               /brain-publish (publish to library)
│       ├── search.prompt.md                /brain-search (search notes)
│       ├── capture-session.prompt.md       /brain-capture-session (save AI session)
│       ├── fetch.prompt.md                 /brain-fetch (fetch from source)
│       ├── pull.prompt.md                  /brain-pull (pull into brain)
│       ├── push.prompt.md                  /brain-push (push to target)
│       ├── clone.prompt.md                 /brain-clone (clone from source)
│       ├── merge.prompt.md                 /brain-merge (merge notes)
│       ├── cherry-pick.prompt.md           /brain-cherry-pick (selective merge)
│       ├── stash.prompt.md                 /brain-stash (save work-in-progress)
│       ├── diff.prompt.md                  /brain-diff (compare notes)
│       ├── remote.prompt.md                /brain-remote (manage remote sources)
│       └── consolidate.prompt.md           /brain-consolidate (consolidate notes)
│
├── skills/                              ← Agent skills (auto by task match)
│   ├── 🛠️ README.md                         Guide: how skills work
│   ├── TAXONOMY.md                          Full taxonomy tree — 8 categories, 23 skills
│   ├── languages-platforms/                 Java, JVM, macOS (6 skills)
│   │   ├── java-build/SKILL.md              Compile & run Java
│   │   ├── java-debugging/SKILL.md          Exception patterns & debug techniques
│   │   ├── java-formatting/SKILL.md         Code formatting, style, inspections (opt-in)
│   │   ├── java-learning-resources/SKILL.md Curated Java learning resource index
│   │   ├── jvm-platform/SKILL.md            JVM internals, GC, class loading, serialization, GraalVM
│   │   └── mac-dev/SKILL.md                 Homebrew, JDK, npm, Docker, dotfiles
│   ├── design-architecture/                 Patterns & roles (2 skills)
│   │   ├── design-patterns/SKILL.md         OOP patterns & SOLID reference
│   │   └── software-development-roles/SKILL.md  PO, Developer, QA role workflows
│   ├── dev-process/                         Research & process (3 skills)
│   │   ├── deep-research/SKILL.md           Investigation, spike stories, RCA, trade-off analysis
│   │   ├── requirements-research/SKILL.md   User stories, BDD, acceptance criteria, MoSCoW
│   │   └── github-workflow/SKILL.md         PRs, issues, GitHub CLI, branch protection
│   ├── devops-tooling/                      DevOps & developer tools (5 skills)
│   │   ├── git-vcs/SKILL.md                 Git workflows, branching, commits, semver
│   │   ├── mcp-development/SKILL.md         MCP: build servers, configure agents, project structure
│   │   ├── copilot-customization/SKILL.md   Copilot 6 primitives — create, review, fix
│   │   ├── atlassian-tools/SKILL.md         Jira/Confluence/Bitbucket PAT CLI (89 actions)
│   │   └── web-reader/SKILL.md              Webpage reading, content extraction
│   ├── knowledge-management/                Brain & PKM (3 skills)
│   │   ├── brain-management/SKILL.md        brain/ai-brain/ tier routing, naming, frontmatter
│   │   ├── pkm-management/SKILL.md          PKM capture sources, git-inspired content ops
│   │   └── digital-notetaking/SKILL.md      PKM methods, tool cheatsheets, note templates
│   ├── learning-resources/                  Curated resource vaults (2 skills)
│   │   ├── learning-resources-vault/SKILL.md  176+ curated resources — master vault index
│   │   └── software-engineering-resources/SKILL.md  Comprehensive SE/CS resource index
│   ├── career/                              Career guidance (1 skill)
│   │   └── career-resources/SKILL.md        Career data (roles, skills, pay, roadmaps)
│   └── daily-life/                          Daily life (1 skill)
│       └── daily-assistant-resources/SKILL.md  Daily assistant resources (finance, productivity, news)
│
└── docs/                                ← Documentation & tutorials
    ├── mcp-server-setup.md                 Newbie MCP guide: install, build, credentials, use, copy to other projects
    ├── getting-started.md                  Step-by-step tutorial
    ├── customization-guide.md              Architecture deep-dive
    ├── file-reference.md                   Who reads what (Copilot vs developer)
    ├── navigation-index.md                 Master index of all commands & files
    └── slash-commands.md                   Developer slash command reference
```

### What's NOT Official

| Folder | Status | What to Use Instead |
|---|---|---|
| `.github/roles/` | **Not official** | Use `.github/agents/` — agents serve the "role" purpose |
| `.github/copilot/` | **Not official** | Use `.github/copilot-instructions.md` (file at root of `.github/`) |

---

## This Project's Current Setup

This learning project includes working samples of each primitive, organized into **four specialist modes** that work as a senior developer's toolkit.

<br>

### 🤖 Specialist Agents (Modes)

> **How to use:** Select from the agent dropdown in VS Code Chat.

| Agent | File | Purpose |
|---|---|---|
| **Designer** | [`designer.agent.md`](agents/designer.agent.md) | Architecture review, SOLID/GRASP, design patterns, clean code |
| **Debugger** | [`debugger.agent.md`](agents/debugger.agent.md) | Systematic root cause analysis, hypothesis-driven debugging |
| **Impact-Analyzer** | [`impact-analyzer.agent.md`](agents/impact-analyzer.agent.md) | Ripple effect analysis, dependency mapping, risk assessment |
| **Learning-Mentor** | [`learning-mentor.agent.md`](agents/learning-mentor.agent.md) | Concept teaching with theory, analogies, and hands-on code |
| **Code-Reviewer** | [`code-reviewer.agent.md`](agents/code-reviewer.agent.md) | Bug detection, style checks, best practices (read-only) |
| **Daily-Assistant** | [`daily-assistant.agent.md`](agents/daily-assistant.agent.md) | Finance, productivity, news, daily life tasks |
| **Thinking-Beast-Mode** | [`Thinking-Beast-Mode.agent.md`](agents/Thinking-Beast-Mode.agent.md) | Deep research agent — autonomous, thorough, web-fetching |
| **My-Kanha** | [`my-kanha.agent.md`](agents/my-kanha.agent.md) | Personal spiritual mentor — Krishna-inspired guidance |

<br>

### 🎯 Slash Commands (Prompts)

> **How to use:** Type `/command` in VS Code Chat.

| Command | File | What It Does |
|---|---|---|
| **Meta & Navigation** | | |
| `/hub` | [`hub.prompt.md`](prompts/meta/hub.prompt.md) | Master navigation index — browse all commands |
| `/composite` | [`composite.prompt.md`](prompts/meta/composite.prompt.md) | Combine multiple modes in one session |
| `/context` | [`context.prompt.md`](prompts/meta/context.prompt.md) | Continue prior conversation or start fresh |
| `/scope` | [`scope.prompt.md`](prompts/meta/scope.prompt.md) | Generic learning vs code/domain-specific |
| `/multi-session` | [`multi-session.prompt.md`](prompts/meta/multi-session.prompt.md) | Manage state across multiple chat sessions |
| `/steer` | [`steer.prompt.md`](prompts/meta/steer.prompt.md) | View or switch the active steering mode |
| `/request-steering` | [`request-steering.prompt.md`](prompts/meta/request-steering.prompt.md) | Route incoming request vs current work |
| `/session-scope` | [`session-scope.prompt.md`](prompts/meta/session-scope.prompt.md) | Manage session scope levels |
| `/write-docs` | [`write-docs.prompt.md`](prompts/meta/write-docs.prompt.md) | Generate documentation |
| **Learning & Concepts** | | |
| `/learn-concept` | [`learn-concept.prompt.md`](prompts/learning/learn-concept.prompt.md) | Learn any CS/SE concept (language-agnostic) |
| `/deep-dive` | [`deep-dive.prompt.md`](prompts/learning/deep-dive.prompt.md) | Multi-layered progressive concept exploration |
| `/learn-from-docs` | [`learn-from-docs.prompt.md`](prompts/learning/learn-from-docs.prompt.md) | Learn concepts via official documentation |
| `/reading-plan` | [`reading-plan.prompt.md`](prompts/learning/reading-plan.prompt.md) | Structured reading/learning plan with resources |
| `/teach` | [`teach.prompt.md`](prompts/learning/teach.prompt.md) | Learn concepts from current file's code |
| **Domain-Specific** | | |
| `/dsa` | [`dsa.prompt.md`](prompts/domain/dsa.prompt.md) | Data structures & algorithms with pattern hierarchy |
| `/system-design` | [`system-design.prompt.md`](prompts/domain/system-design.prompt.md) | Unified HLD/LLD with full internal hierarchy |
| `/devops` | [`devops.prompt.md`](prompts/domain/devops.prompt.md) | CI/CD, Docker, Kubernetes, cloud, IaC, Git, build tools |
| `/sdlc` | [`sdlc.prompt.md`](prompts/domain/sdlc.prompt.md) | SDLC phases, methodologies, engineering practices |
| `/tech-stack` | [`tech-stack.prompt.md`](prompts/domain/tech-stack.prompt.md) | Frameworks, libraries, databases — compare & learn |
| `/language-guide` | [`language-guide.prompt.md`](prompts/domain/language-guide.prompt.md) | Language-specific learning framework |
| `/mcp` | [`mcp.prompt.md`](prompts/domain/mcp.prompt.md) | Learn & build MCP servers, protocol deep-dive |
| `/explore-project` | [`explore-project.prompt.md`](prompts/domain/explore-project.prompt.md) | Learn by studying open-source projects |
| `/resources` | [`resources.prompt.md`](prompts/domain/resources.prompt.md) | Search, browse & export ~176 curated learning resources |
| `/digital-notetaking` | [`digital-notetaking.prompt.md`](prompts/domain/digital-notetaking.prompt.md) | PKM tools: Obsidian, Notion, Logseq, PARA |
| **Code Quality** | | |
| `/design-review` | [`design-review.prompt.md`](prompts/code/design-review.prompt.md) | Full SOLID/GRASP design review of current file |
| `/debug` | [`debug.prompt.md`](prompts/code/debug.prompt.md) | Systematic bug investigation workflow |
| `/code-analysis` | [`code-analysis.prompt.md`](prompts/code/code-analysis.prompt.md) | Code review and analysis |
| `/code-analysis-deep-dive` | [`code-analysis-deep-dive.prompt.md`](prompts/code/code-analysis-deep-dive.prompt.md) | Deep-dive into code internals and flow |
| `/impact` | [`impact.prompt.md`](prompts/code/impact.prompt.md) | Change impact & ripple effect analysis |
| `/refactor` | [`refactor.prompt.md`](prompts/code/refactor.prompt.md) | Identify and apply refactoring opportunities |
| `/explain` | [`explain.prompt.md`](prompts/code/explain.prompt.md) | Beginner-friendly file explanation |
| `/check-standards` | [`check-standards.prompt.md`](prompts/code/check-standards.prompt.md) | Audit file/folder against best practices |
| **Tools & Platforms** | | |
| `/atlassian-tools` | [`atlassian-tools.prompt.md`](prompts/tools/atlassian-tools.prompt.md) | Jira, Confluence, Bitbucket CLI workflows |
| `/git-vcs` | [`git-vcs.prompt.md`](prompts/tools/git-vcs.prompt.md) | Git workflows, branching, conventions |
| `/github-workflow` | [`github-workflow.prompt.md`](prompts/tools/github-workflow.prompt.md) | GitHub PRs, issues, gh CLI, Actions |
| `/build-tools` | [`build-tools.prompt.md`](prompts/tools/build-tools.prompt.md) | Maven, Gradle, Make, Bazel, npm |
| `/mac-dev` | [`mac-dev.prompt.md`](prompts/tools/mac-dev.prompt.md) | macOS dev environment |
| `/read-url` | [`read-url.prompt.md`](prompts/tools/read-url.prompt.md) | Read, extract, summarize webpage content |
| **Copilot Customization** | | |
| `/copilot-customization` | [`copilot-customization.prompt.md`](prompts/customization/copilot-customization.prompt.md) | Learn Copilot customization primitives |
| `/create-agent` | [`create-agent.prompt.md`](prompts/customization/create-agent.prompt.md) | Scaffold a new custom agent |
| `/mcp-to-skill` | [`mcp-to-skill.prompt.md`](prompts/customization/mcp-to-skill.prompt.md) | Convert MCP tool to SKILL.md |
| **Career & Daily Life** | | |
| `/career-roles` | [`career-roles.prompt.md`](prompts/career/career-roles.prompt.md) | Job roles, skills, pay ranges, career roadmaps |
| `/interview-prep` | [`interview-prep.prompt.md`](prompts/career/interview-prep.prompt.md) | DSA patterns, system design, interview strategies |
| `/daily-assist` | [`daily-assist.prompt.md`](prompts/career/daily-assist.prompt.md) | Finance, productivity, news, daily life tasks |
| **Shipping & Release** | | |
| `/ship` | [`ship.prompt.md`](prompts/shipping/ship.prompt.md) | Release workflow |
| `/github-push` | [`github-push.prompt.md`](prompts/shipping/github-push.prompt.md) | Push to remote & create PR |
| **Backlog & Tasks** | | |
| `/backlog` | [`backlog.prompt.md`](prompts/backlog/backlog.prompt.md) | Agile board management |
| `/jot` | [`jot.prompt.md`](prompts/backlog/jot.prompt.md) | Quick capture to backlog |
| `/read-file-jot` | [`read-file-jot.prompt.md`](prompts/backlog/read-file-jot.prompt.md) | Capture ideas from current file |
| `/todo` | [`todo.prompt.md`](prompts/backlog/todo.prompt.md) | Single task creation |
| `/todos` | [`todos.prompt.md`](prompts/backlog/todos.prompt.md) | Batch task creation |
| **Brain Workspace** | | |
| `/brain-new` | [`new.prompt.md`](prompts/brain/new.prompt.md) | Create a knowledge note in inbox/ or notes/ tier |
| `/brain-publish` | [`publish.prompt.md`](prompts/brain/publish.prompt.md) | Publish a note to library/ with tagging and git commit |
| `/brain-search` | [`search.prompt.md`](prompts/brain/search.prompt.md) | Search notes by tag, project, kind, date, or full text |
| `/brain-capture-session` | [`capture-session.prompt.md`](prompts/brain/capture-session.prompt.md) | Convert AI session into a structured session note |
| `/brain-fetch` | [`fetch.prompt.md`](prompts/brain/fetch.prompt.md) | Fetch content from external source |
| `/brain-pull` | [`pull.prompt.md`](prompts/brain/pull.prompt.md) | Pull content into brain workspace |
| `/brain-push` | [`push.prompt.md`](prompts/brain/push.prompt.md) | Push content to target |
| `/brain-clone` | [`clone.prompt.md`](prompts/brain/clone.prompt.md) | Clone from external source |
| `/brain-merge` | [`merge.prompt.md`](prompts/brain/merge.prompt.md) | Merge notes together |
| `/brain-cherry-pick` | [`cherry-pick.prompt.md`](prompts/brain/cherry-pick.prompt.md) | Selective merge from another note |
| `/brain-stash` | [`stash.prompt.md`](prompts/brain/stash.prompt.md) | Save work-in-progress |
| `/brain-diff` | [`diff.prompt.md`](prompts/brain/diff.prompt.md) | Compare notes |
| `/brain-remote` | [`remote.prompt.md`](prompts/brain/remote.prompt.md) | Manage remote sources |
| `/brain-consolidate` | [`consolidate.prompt.md`](prompts/brain/consolidate.prompt.md) | Consolidate notes |

> 📖 **Deep dive:** [Prompts Guide →](prompts/README.md)

<br>

### 📋 Auto-Applied Instructions

> **How to use:** These load automatically — no action needed.

| File | Applies To | Content |
|---|---|---|
| [`copilot-instructions.md`](copilot-instructions.md) | All requests | Project-wide rules and conventions |
| [`java.instructions.md`](instructions/java.instructions.md) | `**/*.java` | Java naming, style, Java 21+ features |
| [`clean-code.instructions.md`](instructions/clean-code.instructions.md) | `**/*.java` | Clean code practices, code smell detection |
| [`change-completeness.instructions.md`](instructions/change-completeness.instructions.md) | `**` | Iterative completeness checklist — default steering mode |
| [`md-formatting.instructions.md`](instructions/md-formatting.instructions.md) | `**` | Markdown formatting rules — always enforced |
| [`steering-modes.instructions.md`](instructions/steering-modes.instructions.md) | `**` | Steering mode profiles (completeness, beast, learning, design, debug, focused) |
| [`chat-capture.instructions.md`](instructions/chat-capture.instructions.md) | `**` | Session capture auto-policy and protocol |
| [`session-scoping.instructions.md`](instructions/session-scoping.instructions.md) | `**` | Session scope management (global/project/feature) |
| [`backlog.instructions.md`](instructions/backlog.instructions.md) | `brain/ai-brain/backlog/**` | Backlog/task management protocol |
| [`build.instructions.md`](instructions/build.instructions.md) | `**/*.gradle` | Gradle build commands & patterns |

> 📖 **Deep dive:** [Instructions Guide →](instructions/README.md)

<br>

### 🛠️ Auto-Loaded Skills

> **How to use:** Just ask a matching question — skills load automatically.

| Skill | Folder | Triggers On |
|---|---|---|
| **Languages & Platforms** | | |
| `java-build` | [`languages-platforms/java-build/`](skills/languages-platforms/java-build/SKILL.md) | Compile, run, build questions |
| `java-debugging` | [`languages-platforms/java-debugging/`](skills/languages-platforms/java-debugging/SKILL.md) | Exception analysis, debugging techniques |
| `java-formatting` | [`languages-platforms/java-formatting/`](skills/languages-platforms/java-formatting/SKILL.md) | Code formatting, style, inspections (opt-in) |
| `java-learning-resources` | [`languages-platforms/java-learning-resources/`](skills/languages-platforms/java-learning-resources/SKILL.md) | Java-specific learning resources, docs, tutorials |
| `jvm-platform` | [`languages-platforms/jvm-platform/`](skills/languages-platforms/jvm-platform/SKILL.md) | JVM internals, GC, class loading, serialization, GraalVM |
| `mac-dev` | [`languages-platforms/mac-dev/`](skills/languages-platforms/mac-dev/SKILL.md) | Homebrew, JDK, npm, Docker, dotfiles cheatsheets |
| **Design & Architecture** | | |
| `design-patterns` | [`design-architecture/design-patterns/`](skills/design-architecture/design-patterns/SKILL.md) | Design patterns, SOLID, architecture questions |
| `software-development-roles` | [`design-architecture/software-development-roles/`](skills/design-architecture/software-development-roles/SKILL.md) | PO, Developer, QA/Tester role workflows |
| **Dev Process** | | |
| `deep-research` | [`dev-process/deep-research/`](skills/dev-process/deep-research/SKILL.md) | Investigation, spike stories, RCA, trade-off analysis |
| `requirements-research` | [`dev-process/requirements-research/`](skills/dev-process/requirements-research/SKILL.md) | User stories, BDD, acceptance criteria, MoSCoW |
| `github-workflow` | [`dev-process/github-workflow/`](skills/dev-process/github-workflow/SKILL.md) | PRs, issues, GitHub CLI, branch protection |
| **DevOps & Tooling** | | |
| `git-vcs` | [`devops-tooling/git-vcs/`](skills/devops-tooling/git-vcs/SKILL.md) | Git workflows, branching, commits, semver |
| `mcp-development` | [`devops-tooling/mcp-development/`](skills/devops-tooling/mcp-development/SKILL.md) | MCP protocol, building servers, agent patterns |
| `copilot-customization` | [`devops-tooling/copilot-customization/`](skills/devops-tooling/copilot-customization/SKILL.md) | Copilot 6 primitives — create, review, fix |
| `atlassian-tools` | [`devops-tooling/atlassian-tools/`](skills/devops-tooling/atlassian-tools/SKILL.md) | Jira/Confluence/Bitbucket PAT CLI (89 actions) |
| `web-reader` | [`devops-tooling/web-reader/`](skills/devops-tooling/web-reader/SKILL.md) | Webpage reading, content extraction |
| **Knowledge Management** | | |
| `brain-management` | [`knowledge-management/brain-management/`](skills/knowledge-management/brain-management/SKILL.md) | brain/ai-brain/ tier routing, naming, frontmatter |
| `pkm-management` | [`knowledge-management/pkm-management/`](skills/knowledge-management/pkm-management/SKILL.md) | PKM capture sources, git-inspired content ops |
| `digital-notetaking` | [`knowledge-management/digital-notetaking/`](skills/knowledge-management/digital-notetaking/SKILL.md) | PKM methods, tool cheatsheets, note templates |
| **Learning Resources** | | |
| `learning-resources-vault` | [`learning-resources/learning-resources-vault/`](skills/learning-resources/learning-resources-vault/SKILL.md) | 176+ curated resources — master vault index |
| `software-engineering-resources` | [`learning-resources/software-engineering-resources/`](skills/learning-resources/software-engineering-resources/SKILL.md) | SE/CS: DSA, system design, OS, networking, DevOps |
| **Career** | | |
| `career-resources` | [`career/career-resources/`](skills/career/career-resources/SKILL.md) | Tech career roles, skills matrices, compensation |
| **Daily Life** | | |
| `daily-assistant-resources` | [`daily-life/daily-assistant-resources/`](skills/daily-life/daily-assistant-resources/SKILL.md) | Finance basics, productivity methods, news sources |

> 📖 **Deep dive:** [Skills Guide →](skills/README.md)

<br>

### 🔀 Agent Workflow (Handoffs)

The agents support **handoff buttons** for seamless multi-step workflows:

```text
  ┌────────────┐        ┌───────────────────┐        ┌─────────────┐
  │  Designer  │──────→ │  Impact-Analyzer  │──────→ │    Agent    │
  │  (think)   │        │  (assess risk)    │        │  (build)    │
  └──────┬─────┘        └────────┬──────────┘        └─────────────┘
         │                       │
         └──→ Agent              └──→ Code-Reviewer
              (implement)             (verify)

  ┌────────────┐        ┌───────────────────┐
  │  Debugger  │──────→ │  Impact-Analyzer  │
  │  (find)    │        │  (assess fix)     │
  └──────┬─────┘        └───────────────────┘
         │
         └──→ Code-Reviewer
              (review fix)

  ┌──────────────────┐        ┌─────────────┐
  │  Learning-Mentor │──────→ │    Agent    │
  │  (teach)         │        │  (practice) │
  └────────┬─────────┘        └─────────────┘
           │
           └──→ Code-Reviewer
                (review my code)

  ┌──────────────────┐        ┌──────────────────┐
  │ Daily-Assistant  │──────→ │  Learning-Mentor │
  │ (daily tasks)    │        │  (learn deeper)  │
  └────────┬─────────┘        └──────────────────┘
           │
           └──→ Agent
                (implement)
  ┌──────────────────────┐
  │ Thinking-Beast-Mode  │  Autonomous — no handoffs, fully self-contained
  │ (deep research)      │  Uses web fetching, terminal, code editing
  └──────────────────────┘

  ┌──────────────────┐        ┌──────────────────┐
  │    My-Kanha         │──────→ │  Learning-Mentor │
  │ (spiritual mentor)  │        └──────────────────┘
  └──────────────────┘
         │
         └──→ Daily-Assistant```

---

## How These Work Together

```

You open Main.java and ask a question
│
├── 📋 copilot-instructions.md               ← ALWAYS loaded
├── 📋 instructions/java.instructions.md     ← Loaded because *.java matches
├── 📋 instructions/clean-code...            ← Loaded because *.java matches
│
├── If you selected an agent:
│   └── 🤖 agents/designer.agent.md         ← Agent persona added
│
├── If you typed /design-review:
│   └── 🎯 prompts/code/design-review.prompt.md  ← Task template used
│
└── If your question matches a skill:
    └── 🛠️ skills/design-architecture/design-patterns/SKILL.md  ← Knowledge loaded

```yaml

---

## Priority Order

When multiple files are loaded, Copilot merges them in this order (highest priority wins on conflicts):

| Priority | Source | Example |
|---|---|---|
| **1 (highest)** | **Your message** | What you type in chat always wins |
| 2 | Prompt template | Task-specific steps from `/command` |
| 3 | Agent persona | Specialist behavior from active agent |
| 4 | Matching skills | Extra knowledge loaded by topic match |
| 5 | `*.instructions.md` | Path-specific coding standards |
| 6 (lowest) | `copilot-instructions.md` | General project-wide rules |

---

## Environment Support

| Feature | VS Code Chat | Copilot CLI | GitHub Web | GitHub PR |
|---|---|---|---|---|
| `copilot-instructions.md` | ✅ | ✅ | ✅ | ✅ |
| `*.instructions.md` | ✅ | ❌ | ❌ | ❌ |
| `*.agent.md` | ✅ | ❌ | ❌ | ❌ |
| `*.prompt.md` | ✅ | ❌ | ❌ | ❌ |
| `SKILL.md` | ✅ | ✅ | ❌ | ❌ |

---

## 📚 Documentation Map

```

📖 YOU ARE HERE
│
├─── Entry Points (read one based on experience: 🟢🟡🔴)
│    ├── docs/START-HERE.md ·············· 🟢🟡🔴 Which docs to read, based on your experience level
│    └── docs/phase-guide.md ············· 🟢🟡🔴 Phase-by-phase zero-to-operational guide
│
├─── Guides by Primitive
│    ├── instructions/README.md ·········· Glob patterns, conditional rules
│    ├── agents/README.md ················ Personas, tools, handoffs
│    ├── prompts/README.md ··············· Slash commands, variables, meta-prompts
│    └── skills/README.md ················ Skill folders, progressive loading
│
├─── Reference
│    ├── docs/file-reference.md ·········· Who reads what (🤖 Copilot vs 👤 developer)
│    ├── docs/navigation-index.md ········ Master index: all commands, files, workflows
│    └── docs/slash-commands.md ········· All 65 slash commands: aliases, inputs, composition
│
└─── Tutorials, Deep Dives & Workflows
     ├── docs/getting-started.md ········· Hands-on: verify setup, try each primitive
     ├── docs/customization-guide.md ····· Theory: how primitives connect & extend
     ├── docs/mcp-server-setup.md ········ MCP from scratch: install, build, secrets, use, copy
     ├── docs/export-guide.md ············ Copy features to another project (portable setup)
     └── docs/copilot-workflow.md ········ Copilot chat patterns, queuing, multi-session

```markdown

| Guide | What You'll Learn | Audience | Time |
|---|---|---|---|
| [**START HERE →**](docs/START-HERE.md) | **Which docs to read based on your experience** | 🟢🟡🔴 All | ~2 min |
| [Phase Guide →](docs/phase-guide.md) | Step-by-step: orient, build, configure, use, export | 🟢🟡🔴 All | ~20 min |
| [Getting Started →](docs/getting-started.md) | Verify setup, try each primitive hands-on | 🟢🟡 | ~30 min |
| [MCP Server Setup →](docs/mcp-server-setup.md) | Install, build, credentials, verify, copy to other projects | 🟢🟡 | ~10 min |
| [Copilot Workflow →](docs/copilot-workflow.md) | Chat patterns, queuing instructions, token limits | 🟢🟡🔴 All | ~10 min |
| [Export Guide →](docs/export-guide.md) | Copy customization + MCP servers to your own projects | 🟡🔴 | ~10 min |
| [Customization Guide →](docs/customization-guide.md) | Architecture, how primitives connect, extending | 🟡🔴 | ~20 min |
| [File Reference →](docs/file-reference.md) | Which files Copilot reads vs. developer docs | 🟡🔴 | ~5 min |
| [Navigation Index →](docs/navigation-index.md) | Master lookup: all commands, agents, skills, file map | 🔴 | ~5 min |
| [Slash Commands →](docs/slash-commands.md) | All 65 commands: details, aliases, inputs, composition | 🔴 | ~5 min |
| [Instructions Guide →](instructions/README.md) | Glob patterns, conditional rules, examples | 🟡🔴 | ~15 min |
| [Agents Guide →](agents/README.md) | Custom personas, tools, handoffs, examples | 🟡🔴 | ~15 min |
| [Prompts Guide →](prompts/README.md) | Slash commands, variables, meta-prompts | 🟡🔴 | ~15 min |
| [Skills Guide →](skills/README.md) | Skill folders, scripts, progressive loading | 🟡🔴 | ~15 min |

---

## 🔍 File Audience — Copilot vs Developer

Not every file in `.github/` is read by Copilot. Understanding who reads what prevents confusion:

| Icon | Audience | These files... |
|---|---|---|
| 🤖 | **Copilot (AI)** | Directly shape Copilot's behavior — instructions, agents, prompts, skills |
| 👤 | **Developer (you)** | Documentation, guides, tutorials — Copilot ignores them |

| File Type | Audience | How to Identify |
|---|---|---|
| `copilot-instructions.md` | 🤖 | Always loaded into AI context |
| `*.instructions.md` | 🤖 | Has `applyTo:` frontmatter |
| `*.agent.md` | 🤖 | Has `tools:` / `handoffs:` frontmatter |
| `*.prompt.md` | 🤖 | Slash command template |
| `SKILL.md` | 🤖 | Skill definition + resources |
| `README.md` (any folder) | 👤 | Guide for developers |
| `docs/*.md` | 👤 | Tutorials and deep-dives |

> 📖 **Full details:** [File Reference →](docs/file-reference.md) — complete breakdown of every file, when it loads, and what happens when you edit it.

---

## 🧭 Learning Path

| Step | What to Do | Guide |
|---|---|---|
| **1** | Read this README | ← You are here |
| **2** | Follow the hands-on tutorial | [Getting Started →](docs/getting-started.md) |
| **3** | Experiment with the sample files | Try agents, prompts, skills |
| **4** | Understand how it all connects | [Customization Guide →](docs/customization-guide.md) |
| **5** | Deep dive into each primitive as needed | See [Documentation Map](#-documentation-map) |
| **6** | Create your own customizations | Templates in each guide |
| **7** | Apply to your production project | Port what works |

---

## 🔗 Reference Links

**Official Documentation**

- [VS Code: Customizing Copilot](https://code.visualstudio.com/docs/copilot/customization)
- [VS Code: Custom Instructions](https://code.visualstudio.com/docs/copilot/customization/custom-instructions)
- [VS Code: Custom Agents](https://code.visualstudio.com/docs/copilot/customization/custom-agents)
- [VS Code: Prompt Files](https://code.visualstudio.com/docs/copilot/customization/prompt-files)
- [VS Code: Agent Skills](https://code.visualstudio.com/docs/copilot/customization/agent-skills)

**GitHub & Community**

- [GitHub: Repository Instructions](https://docs.github.com/en/copilot/customizing-copilot/adding-repository-custom-instructions-for-github-copilot)
- [Community Examples (awesome-copilot)](https://github.com/github/awesome-copilot)
- [Agent Skills Open Standard](https://agentskills.io/)

---

<p align="center">

**Navigation:** [Getting Started →](docs/getting-started.md) · [Customization Guide →](docs/customization-guide.md) · [File Reference →](docs/file-reference.md) · [Navigation Index →](docs/navigation-index.md) · [Slash Commands →](docs/slash-commands.md) · [Instructions](instructions/README.md) · [Agents](agents/README.md) · [Prompts](prompts/README.md) · [Skills](skills/README.md)

</p>
