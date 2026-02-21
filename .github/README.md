# learning-assistant

> **Purpose:** Learn how to customize GitHub Copilot using all 5 official primitives.  
> **Project:** `learning-assistant` — a simple Java project for hands-on experimentation.  
> **Audience:** Developers new to Copilot customization who want to learn by doing.

---

## 📑 Table of Contents
- [At a Glance](#-at-a-glance)- [Why Customize Copilot?](#why-customize-copilot)
- [The 5 Official Primitives](#the-5-official-primitives)
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

## ⚡ At a Glance

| Action | How |
|---|---|
| **Switch persona** | Chat dropdown → select **Designer**, **Debugger**, **Impact-Analyzer**, **Learning-Mentor**, **Daily-Assistant**, or **Thinking-Beast-Mode** |
| **Run a workflow** | Type `/hub`, `/dsa`, `/system-design`, `/devops`, `/mcp`, `/resources`, `/language-guide`, `/tech-stack`, `/sdlc`, `/career-roles`, `/daily-assist`, `/multi-session`, `/learn-concept`, `/learn-from-docs`, `/explore-project`, `/deep-dive`, `/reading-plan`, `/interview-prep`, `/design-review`, `/debug`, `/impact`, `/teach`, `/refactor`, `/explain`, `/composite`, `/context`, `/scope`, `/brain-new`, `/brain-publish`, or `/brain-search` in Chat |
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

## The 5 Official Primitives

GitHub Copilot in VS Code supports exactly **5 customization primitives**. Everything you can do falls into one of these:

```
┌─────────────────────────────────────────────────────────────────────┐
│                    COPILOT CUSTOMIZATION                            │
│                                                                     │
│  1. copilot-instructions.md    Always-on project rules              │
│  2. *.instructions.md          Conditional rules per file type      │
│  3. *.agent.md                 Custom AI personas                   │
│  4. *.prompt.md                Reusable slash-command tasks          │
│  5. SKILL.md                   Tool folders with scripts/resources  │
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

---

## Folder Structure

```
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
│   ├── hub.prompt.md                       /hub (master navigation index)
│   ├── dsa.prompt.md                       /dsa (data structures & algorithms)
│   ├── system-design.prompt.md             /system-design (HLD/LLD hierarchy)
│   ├── devops.prompt.md                    /devops (CI/CD, Docker, K8s, cloud)
│   ├── language-guide.prompt.md            /language-guide (language learning)
│   ├── tech-stack.prompt.md                /tech-stack (frameworks, databases)
│   ├── sdlc.prompt.md                      /sdlc (phases & methodologies)
│   ├── daily-assist.prompt.md              /daily-assist (finance, productivity)
│   ├── career-roles.prompt.md              /career-roles (job roles, skills, pay)
│   ├── multi-session.prompt.md             /multi-session (cross-session state)
│   ├── design-review.prompt.md             /design-review
│   ├── debug.prompt.md                     /debug
│   ├── impact.prompt.md                    /impact
│   ├── teach.prompt.md                     /teach
│   ├── refactor.prompt.md                  /refactor
│   ├── explain.prompt.md                   /explain
│   ├── composite.prompt.md                 /composite (combine modes)
│   ├── context.prompt.md                   /context (continue/fresh)
│   ├── scope.prompt.md                     /scope (generic/specific)
│   ├── learn-from-docs.prompt.md           /learn-from-docs (official docs)
│   ├── explore-project.prompt.md           /explore-project (OSS study)
│   ├── deep-dive.prompt.md                 /deep-dive (concept mastery)
│   ├── reading-plan.prompt.md              /reading-plan (study plan)
│   ├── learn-concept.prompt.md             /learn-concept (any CS/SE concept)
│   ├── interview-prep.prompt.md            /interview-prep (DSA/system design)
│   ├── resources.prompt.md                 /resources (learning resource vault)
│   └── mcp.prompt.md                       /mcp (MCP servers, agents, API integration)
│
├── skills/                              ← Agent skills (auto by task match)
│   ├── 🛠️ README.md                         Guide: how skills work
│   ├── java-build/SKILL.md                 Compile & run Java
│   ├── design-patterns/SKILL.md            OOP patterns & SOLID reference
│   ├── java-debugging/SKILL.md             Exception patterns & debug techniques
│   ├── java-learning-resources/SKILL.md    Curated Java learning resource index
│   ├── software-engineering-resources/SKILL.md  Comprehensive SE/CS resource index
│   ├── daily-assistant-resources/SKILL.md  Daily assistant resources (finance, productivity, news)
│   ├── career-resources/SKILL.md           Career data (roles, skills, pay, roadmaps)
│   └── mcp-development/SKILL.md            MCP: build servers, configure agents, project structure
│
└── docs/                                ← Documentation & tutorials
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

> 📖 **Deep dive:** [Agents Guide →](agents/README.md)

<br>

### 🎯 Slash Commands (Prompts)

> **How to use:** Type `/command` in VS Code Chat.

| Command | File | What It Does |
|---|---|---|
| `/design-review` | [`design-review.prompt.md`](prompts/design-review.prompt.md) | Full SOLID/GRASP design review of current file |
| `/debug` | [`debug.prompt.md`](prompts/debug.prompt.md) | Systematic bug investigation workflow |
| `/impact` | [`impact.prompt.md`](prompts/impact.prompt.md) | Change impact & ripple effect analysis |
| `/teach` | [`teach.prompt.md`](prompts/teach.prompt.md) | Learn concepts from current file's code |
| `/refactor` | [`refactor.prompt.md`](prompts/refactor.prompt.md) | Identify and apply refactoring opportunities |
| `/explain` | [`explain.prompt.md`](prompts/explain.prompt.md) | Beginner-friendly file explanation |
| `/composite` | [`composite.prompt.md`](prompts/composite.prompt.md) | Combine multiple modes in one session |
| `/context` | [`context.prompt.md`](prompts/context.prompt.md) | Continue prior conversation or start fresh |
| `/scope` | [`scope.prompt.md`](prompts/scope.prompt.md) | Generic learning vs code/domain-specific |
| `/learn-from-docs` | [`learn-from-docs.prompt.md`](prompts/learn-from-docs.prompt.md) | Learn concepts via official documentation |
| `/explore-project` | [`explore-project.prompt.md`](prompts/explore-project.prompt.md) | Learn by studying open-source projects |
| `/deep-dive` | [`deep-dive.prompt.md`](prompts/deep-dive.prompt.md) | Multi-layered progressive concept exploration |
| `/reading-plan` | [`reading-plan.prompt.md`](prompts/reading-plan.prompt.md) | Structured reading/learning plan with resources |
| `/learn-concept` | [`learn-concept.prompt.md`](prompts/learn-concept.prompt.md) | Learn any CS/SE concept (language-agnostic) |
| `/interview-prep` | [`interview-prep.prompt.md`](prompts/interview-prep.prompt.md) | DSA patterns, system design, interview strategies |
| `/hub` | [`hub.prompt.md`](prompts/hub.prompt.md) | Master navigation index — browse all commands |
| `/dsa` | [`dsa.prompt.md`](prompts/dsa.prompt.md) | Data structures & algorithms with pattern hierarchy |
| `/system-design` | [`system-design.prompt.md`](prompts/system-design.prompt.md) | Unified HLD/LLD with full internal hierarchy |
| `/devops` | [`devops.prompt.md`](prompts/devops.prompt.md) | CI/CD, Docker, Kubernetes, cloud, IaC, Git, build tools, monitoring |
| `/language-guide` | [`language-guide.prompt.md`](prompts/language-guide.prompt.md) | Language-specific learning framework |
| `/tech-stack` | [`tech-stack.prompt.md`](prompts/tech-stack.prompt.md) | Frameworks, libraries, databases — compare & learn |
| `/sdlc` | [`sdlc.prompt.md`](prompts/sdlc.prompt.md) | SDLC phases, methodologies, engineering practices |
| `/daily-assist` | [`daily-assist.prompt.md`](prompts/daily-assist.prompt.md) | Finance, productivity, news, daily life tasks |
| `/career-roles` | [`career-roles.prompt.md`](prompts/career-roles.prompt.md) | Job roles, skills, pay ranges, career roadmaps |
| `/multi-session` | [`multi-session.prompt.md`](prompts/multi-session.prompt.md) | Manage state across multiple chat sessions |
| `/resources` | [`resources.prompt.md`](prompts/resources.prompt.md) | Search, browse, discover & export 47+ curated learning resources |
| `/mcp` | [`mcp.prompt.md`](prompts/mcp.prompt.md) | Learn & build MCP servers, configure agents, protocol deep-dive |
| `/brain-new` | [`brain-new.prompt.md`](prompts/brain-new.prompt.md) | Create a knowledge note in inbox/ or notes/ tier |
| `/brain-publish` | [`brain-publish.prompt.md`](prompts/brain-publish.prompt.md) | Publish a note to archive/ with tagging and git commit |
| `/brain-search` | [`brain-search.prompt.md`](prompts/brain-search.prompt.md) | Search notes by tag, project, kind, date, or full text |

> 📖 **Deep dive:** [Prompts Guide →](prompts/README.md)

<br>

### 📋 Auto-Applied Instructions

> **How to use:** These load automatically — no action needed.

| File | Applies To | Content |
|---|---|---|
| [`copilot-instructions.md`](copilot-instructions.md) | All requests | Project-wide rules and conventions |
| [`java.instructions.md`](instructions/java.instructions.md) | `**/*.java` | Java naming, style, Java 21+ features |
| [`clean-code.instructions.md`](instructions/clean-code.instructions.md) | `**/*.java` | Clean code practices, code smell detection |

> 📖 **Deep dive:** [Instructions Guide →](instructions/README.md)

<br>

### 🛠️ Auto-Loaded Skills

> **How to use:** Just ask a matching question — skills load automatically.

| Skill | Folder | Triggers On |
|---|---|---|
| `java-build` | [`skills/java-build/`](skills/java-build/SKILL.md) | Compile, run, build questions |
| `design-patterns` | [`skills/design-patterns/`](skills/design-patterns/SKILL.md) | Design patterns, SOLID, architecture questions |
| `java-debugging` | [`skills/java-debugging/`](skills/java-debugging/SKILL.md) | Exception analysis, debugging techniques |
| `java-learning-resources` | [`skills/java-learning-resources/`](skills/java-learning-resources/SKILL.md) | Java-specific learning resources, official docs, tutorials |
| `software-engineering-resources` | [`skills/software-engineering-resources/`](skills/software-engineering-resources/SKILL.md) | Comprehensive SE/CS: DSA, system design, OS, networking, DBMS, testing, DevOps, build tools, Git, security, industry concepts, tech trends, frameworks, books |
| `daily-assistant-resources` | [`skills/daily-assistant-resources/`](skills/daily-assistant-resources/SKILL.md) | Finance basics, productivity methods, news sources, research tools |
| `career-resources` | [`skills/career-resources/`](skills/career-resources/SKILL.md) | Tech career roles, skills matrices, compensation data, roadmaps |
| `mcp-development` | [`skills/mcp-development/`](skills/mcp-development/SKILL.md) | MCP protocol, building servers (Java/TypeScript/Python), agent patterns, deployment |

> 📖 **Deep dive:** [Skills Guide →](skills/README.md)

<br>

### 🔀 Agent Workflow (Handoffs)

The agents support **handoff buttons** for seamless multi-step workflows:

```
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
  └──────────────────────┘```

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
│   └── 🎯 prompts/design-review.prompt.md  ← Task template used
│
└── If your question matches a skill:
    └── 🛠️ skills/design-patterns/SKILL.md  ← Knowledge loaded
```

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
├─── Guides by Primitive
│    ├── instructions/README.md ·········· Glob patterns, conditional rules
│    ├── agents/README.md ················ Personas, tools, handoffs
│    ├── prompts/README.md ··············· Slash commands, variables, meta-prompts
│    └── skills/README.md ················ Skill folders, progressive loading
│
├─── Reference
│    ├── docs/file-reference.md ·········· Who reads what (🤖 Copilot vs 👤 developer)
│    ├── docs/navigation-index.md ········ Master index: all commands, files, workflows
│    └── docs/slash-commands.md ········· All 30 slash commands: aliases, inputs, composition
│
└─── Tutorials & Deep Dives
     ├── docs/getting-started.md ········· Hands-on: verify setup, try each primitive
     └── docs/customization-guide.md ····· Theory: how primitives connect & extend
```

| Guide | What You'll Learn | Time |
|---|---|---|
| [Getting Started →](docs/getting-started.md) | Verify setup, try each primitive hands-on | ~30 min |
| [Customization Guide →](docs/customization-guide.md) | Architecture, how primitives connect, extending | ~20 min |
| [File Reference →](docs/file-reference.md) | Which files Copilot reads vs. developer docs | ~5 min |
| [Navigation Index →](docs/navigation-index.md) | Master lookup: all commands, agents, skills, file map | ~5 min |
| [Slash Commands →](docs/slash-commands.md) | All 30 commands: details, aliases, inputs, composition | ~5 min |
| [Instructions Guide →](instructions/README.md) | Glob patterns, conditional rules, examples | ~15 min |
| [Agents Guide →](agents/README.md) | Custom personas, tools, handoffs, examples | ~15 min |
| [Prompts Guide →](prompts/README.md) | Slash commands, variables, meta-prompts | ~15 min |
| [Skills Guide →](skills/README.md) | Skill folders, scripts, progressive loading | ~15 min |

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
