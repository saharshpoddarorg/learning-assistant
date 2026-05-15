# 📂 File Reference — Who Reads What

> **Purpose:** Clear guide showing which files are consumed by **GitHub Copilot** (the AI) vs. which files are for **developers** (you). Knowing this distinction helps you understand what to edit to change Copilot's behavior vs. where to look for learning and documentation.

---

## At a Glance

```text
.github/
│
│  ┌─────────────────────────────────────────────────────────┐
│  │  🤖 FILES COPILOT READS (affect AI behavior)           │
│  └─────────────────────────────────────────────────────────┘
│
├── copilot-instructions.md              🤖 Always loaded into Copilot context
│
├── instructions/
│   ├── java.instructions.md             🤖 Loaded when editing *.java files
│   └── clean-code.instructions.md       🤖 Loaded when editing *.java files
│
├── agents/
│   ├── designer.agent.md                🤖 Loaded when agent selected in dropdown
│   ├── debugger.agent.md                🤖 Loaded when agent selected
│   ├── impact-analyzer.agent.md         🤖 Loaded when agent selected
│   ├── learning-mentor.agent.md         🤖 Loaded when agent selected
│   ├── code-reviewer.agent.md           🤖 Loaded when agent selected
│   ├── daily-assistant.agent.md         🤖 Loaded when agent selected
│   └── Thinking-Beast-Mode.agent.md     🤖 Loaded when agent selected
│
├── prompts/
│   ├── meta/                            🤖 Meta & workflow prompts (9 commands)
│   │   ├── hub.prompt.md               🤖 Loaded when /hub invoked
│   │   ├── composite.prompt.md         🤖 Loaded when /composite invoked
│   │   ├── context.prompt.md           🤖 Loaded when /context invoked
│   │   ├── scope.prompt.md             🤖 Loaded when /scope invoked
│   │   ├── multi-session.prompt.md     🤖 Loaded when /multi-session invoked
│   │   ├── steer.prompt.md             🤖 Loaded when /steer invoked
│   │   ├── request-steering.prompt.md  🤖 Loaded when /request-steering invoked
│   │   ├── session-scope.prompt.md     🤖 Loaded when /session-scope invoked
│   │   └── write-docs.prompt.md        🤖 Loaded when /write-docs invoked
│   ├── domain/                          🤖 Domain learning prompts (10 commands)
│   │   ├── dsa.prompt.md               🤖 Loaded when /dsa invoked
│   │   ├── system-design.prompt.md     🤖 Loaded when /system-design invoked
│   │   ├── devops.prompt.md            🤖 Loaded when /devops invoked
│   │   ├── sdlc.prompt.md              🤖 Loaded when /sdlc invoked
│   │   ├── tech-stack.prompt.md        🤖 Loaded when /tech-stack invoked
│   │   ├── language-guide.prompt.md    🤖 Loaded when /language-guide invoked
│   │   ├── mcp.prompt.md              🤖 Loaded when /mcp invoked
│   │   ├── explore-project.prompt.md   🤖 Loaded when /explore-project invoked
│   │   ├── resources.prompt.md         🤖 Loaded when /resources invoked
│   │   └── digital-notetaking.prompt.md 🤖 Loaded when /digital-notetaking invoked
│   ├── customization/                   🤖 Copilot customization prompts (3 commands)
│   │   ├── copilot-customization.prompt.md 🤖 Loaded when /copilot-customization invoked
│   │   ├── create-agent.prompt.md      🤖 Loaded when /create-agent invoked
│   │   └── mcp-to-skill.prompt.md      🤖 Loaded when /mcp-to-skill invoked
│   ├── tools/                           🤖 Tool-specific prompts (6 commands)
│   │   ├── atlassian-tools.prompt.md   🤖 Loaded when /atlassian-tools invoked
│   │   ├── git-vcs.prompt.md           🤖 Loaded when /git-vcs invoked
│   │   ├── github-workflow.prompt.md   🤖 Loaded when /github-workflow invoked
│   │   ├── build-tools.prompt.md       🤖 Loaded when /build-tools invoked
│   │   ├── mac-dev.prompt.md           🤖 Loaded when /mac-dev invoked
│   │   └── read-url.prompt.md          🤖 Loaded when /read-url invoked
│   ├── career/                          🤖 Career & daily prompts (3 commands)
│   │   ├── career-roles.prompt.md      🤖 Loaded when /career-roles invoked
│   │   ├── interview-prep.prompt.md    🤖 Loaded when /interview-prep invoked
│   │   └── daily-assist.prompt.md      🤖 Loaded when /daily-assist invoked
│   ├── design-review.prompt.md          🤖 Loaded when /design-review invoked
│   ├── debug.prompt.md                  🤖 Loaded when /debug invoked
│   ├── impact.prompt.md                 🤖 Loaded when /impact invoked
│   ├── teach.prompt.md                  🤖 Loaded when /teach invoked
│   ├── refactor.prompt.md              🤖 Loaded when /refactor invoked
│   ├── explain.prompt.md               🤖 Loaded when /explain invoked
│   ├── learn-from-docs.prompt.md       🤖 Loaded when /learn-from-docs invoked
│   ├── deep-dive.prompt.md             🤖 Loaded when /deep-dive invoked
│   ├── reading-plan.prompt.md          🤖 Loaded when /reading-plan invoked
│   ├── learn-concept.prompt.md         🤖 Loaded when /learn-concept invoked
│   ├── brain/                           🤖 Brain workspace prompts (14 commands)
│   │   ├── new.prompt.md               🤖 Loaded when /brain-new invoked
│   │   ├── publish.prompt.md           🤖 Loaded when /brain-publish invoked
│   │   └── search.prompt.md            🤖 Loaded when /brain-search invoked
│
├── skills/
│   ├── languages-platforms/
│   │   ├── java-build/SKILL.md         🤖 Auto-loaded when topic matches
│   │   ├── java-debugging/SKILL.md     🤖 Auto-loaded when topic matches
│   │   └── java-learning-resources/SKILL.md 🤖 Auto-loaded when topic matches
│   ├── design-architecture/
│   │   └── design-patterns/SKILL.md    🤖 Auto-loaded when topic matches
│   ├── devops-tooling/
│   │   └── mcp-development/SKILL.md    🤖 Auto-loaded (MCP, protocol, tools, agent architecture, server building)
│   ├── learning-resources/
│   │   └── software-engineering-resources/SKILL.md 🤖 Auto-loaded (DSA, system design, DevOps, Git, build tools, security, industry, trends)
│   ├── career/
│   │   └── career-resources/SKILL.md   🤖 Auto-loaded when topic matches
│   └── daily-life/
│       └── daily-assistant-resources/SKILL.md 🤖 Auto-loaded when topic matches
│
│  ┌─────────────────────────────────────────────────────────┐
│  │  👤 FILES FOR DEVELOPERS (documentation & learning)    │
│  └─────────────────────────────────────────────────────────┘
│
├── README.md (root)                         👤 Project overview — what this repo is, quick start
│
├── README.md                            👤 Project overview & navigation hub
│
├── instructions/
│   └── README.md                        👤 Guide: how instructions work
│
├── agents/
│   └── README.md                        👤 Guide: how agents work
│
├── prompts/
│   └── README.md                        👤 Guide: how prompts work
│
├── skills/
│   └── README.md                        👤 Guide: how skills work
│
└── docs/
    ├── getting-started.md               👤 Hands-on tutorial (~30 min)
    ├── customization-guide.md           👤 Architecture deep-dive
    ├── file-reference.md               👤 This file — who reads what
    ├── navigation-index.md             👤 Master index of all commands & files
    └── slash-commands.md               👤 Developer slash command reference

mcp-servers/                              ← MCP Server Configuration Module
│
├── README.md                            👤 Module overview, config guide, architecture
│
├── .vscode/
│   ├── settings.json                    👤 IDE settings (portable — copy to other projects)
│   ├── launch.json                      👤 Run/debug launch configurations
│   └── extensions.json                  👤 Recommended VS Code extensions
│
├── user-config/                          ⚙️ DEVELOPER-CONFIGURABLE
│   ├── mcp-config.example.properties    👤 Full reference template (committed, ~280 lines)
│   └── mcp-config.properties            👤 Active config (GITIGNORED — your secrets live here)
│
└── src/
    ├── Main.java                        👤 Entry point — loads & prints config summary
    └── config/
        ├── ConfigManager.java           👤 Facade: load → merge → parse → validate → resolve
        ├── model/
        │   ├── McpConfiguration.java    👤 Root config record
        │   ├── ApiKeyStore.java         👤 Service → API key map
        │   ├── LocationPreferences.java 👤 Timezone, locale, region
        │   ├── UserPreferences.java     👤 Theme, log level, retries, timeouts
        │   ├── BrowserPreferences.java  👤 Browser isolation (executable, profile, headless)
        │   ├── ServerDefinition.java    👤 Per-server config (transport, command, URL)
        │   ├── ProfileDefinition.java   👤 Named override sets (dev, prod, testing)
        │   ├── TransportType.java       👤 Enum: STDIO, SSE, STREAMABLE_HTTP
        │   └── package-info.java        👤 Package-level Javadoc
        ├── loader/
        │   ├── ConfigSource.java        👤 Interface for pluggable config sources
        │   ├── PropertiesConfigSource.java  👤 Loads from .properties files
        │   ├── EnvironmentConfigSource.java 👤 Loads MCP_* environment variables
        │   └── ConfigParser.java        👤 Flat properties → structured records
        ├── validation/
        │   ├── ConfigValidator.java     👤 Validates servers, profiles, transports
        │   └── ValidationResult.java    👤 Error list with reporting
        └── exception/
            ├── ConfigLoadException.java     👤 File not found / unreadable
            └── ConfigValidationException.java 👤 Invalid config values
```

---

## Detailed Breakdown

### 🤖 Files Copilot Reads

These files **directly affect Copilot's AI behavior**. Editing them changes what Copilot knows, how it responds, and what rules it follows.

| File Type | Extension | Location | When Loaded | What It Controls |
|---|---|---|---|---|
| **Project instructions** | `.md` | `.github/copilot-instructions.md` | Every request (always) | Project-wide rules all responses must follow |
| **Path-scoped instructions** | `.instructions.md` | `.github/instructions/` | When editing a file matching `applyTo` glob | Coding standards for specific file types |
| **Agent definitions** | `.agent.md` | `.github/agents/` | When you select the agent from the dropdown | Copilot's persona, expertise, tools, and handoffs |
| **Prompt templates** | `.prompt.md` | `.github/prompts/` | When you type `/command` in Chat | Task workflow, steps, and structure |
| **Skill definitions** | `SKILL.md` | `.github/skills/<name>/` | Auto — when your question matches the description | Extra knowledge, scripts, and templates |

#### Key rules for Copilot-read files:

- **Content is instructions to the AI** — write them as directives ("Use X", "Never do Y", "When asked to Z, follow these steps")
- **Frontmatter matters** — YAML headers (`applyTo`, `description`, `tools`, etc.) control when and how the file loads
- **Keep them focused** — shorter, targeted instructions get better compliance than long documents
- **Test after editing** — changes take effect immediately, verify by asking Copilot a relevant question

### 👤 Files for Developers

These files are **documentation for humans**. Copilot does NOT read these to shape its behavior. They exist to help you understand, learn, and extend the customization system.

| File | Location | Purpose | When to Read |
|---|---|---|---|
| **Root README** | `README.md` (project root) | What this repo is, quick start, learning domains | First — if you're new to the project |
| **Main README** | `.github/README.md` | Overview of the Copilot customization system, navigation hub | Start here for Copilot customization |
| **Instructions README** | `.github/instructions/README.md` | How instructions work, glob patterns, examples | When creating/editing instructions |
| **Agents README** | `.github/agents/README.md` | How agents work, tools, handoffs, examples | When creating/editing agents |
| **Prompts README** | `.github/prompts/README.md` | How prompts work, variables, slash commands | When creating/editing prompts |
| **Skills README** | `.github/skills/README.md` | How skills work, progressive loading, structure | When creating/editing skills |
| **Getting Started** | `.github/docs/getting-started.md` | Step-by-step hands-on tutorial | Second — try everything |
| **Customization Guide** | `.github/docs/customization-guide.md` | Architecture, how primitives connect | When you want the big picture |
| **MCP Server Setup** ← **START HERE** | `.github/docs/mcp-server-setup.md` | **Complete newbie guide**: what MCP is, prerequisites, build, credentials, verify, copy to other projects | When setting up MCP servers for the first time |
| **START HERE** | `.github/docs/START-HERE.md` | Single entry point — picks your reading path by experience level (🟢🟡🔴) | First time opening this repo |
| **Phase Guide** | `.github/docs/phase-guide.md` | 8-phase zero-to-operational guide with audience-tiered sections | When you want a step-by-step onboarding |
| **Export Guide** | `.github/docs/export-guide.md` | Copy Copilot customization, MCP servers, and brain workspace to another project | When porting this setup to your own project |
| **Copilot Workflow** | `.github/docs/copilot-workflow.md` | Copilot chat patterns, queuing instructions, multi-session, token limits | When working on complex multi-step tasks |
| **File Reference** | `.github/docs/file-reference.md` | This file — which files are for whom | When confused about a file's purpose |
| **Slash Commands Ref** | `.github/docs/slash-commands.md` | All 36 slash commands with aliases, inputs, composition | When looking up a specific command |
| **MCP Servers README** | `mcp-servers/README.md` | Config architecture, setup guide, server definitions, browser isolation | When configuring or adding MCP servers |
| **MCP Config Template** | `mcp-servers/user-config/mcp-config.example.properties` | Full property reference with documented placeholders (~280 lines) | When setting up MCP config for the first time |

#### Key rules for developer files:

- **Content is explanation for humans** — write clearly, use examples, add links
- **Editing these does NOT change Copilot** — they are reference material only
- **Keep them in sync** — when you add a new agent/prompt/skill, update the relevant README

---

## How to Tell Them Apart

Quick heuristics to determine any file's audience:

| Clue | Audience | Example |
|---|---|---|
| Has YAML frontmatter with `applyTo`, `tools`, `handoffs`, `description` | 🤖 Copilot | `java.instructions.md`, `designer.agent.md` |
| Named `README.md` | 👤 Developer | `instructions/README.md` |
| Lives in `docs/` | 👤 Developer | `getting-started.md` |
| Extension is `.instructions.md`, `.agent.md`, `.prompt.md` | 🤖 Copilot | All of them |
| Named `SKILL.md` (uppercase) | 🤖 Copilot | `java-build/SKILL.md` |
| Contains "How to use", "Table of Contents", "Experiments to Try" | 👤 Developer | Any README |
| Contains "You are a...", "When the user asks...", "Always follow..." | 🤖 Copilot | Agent/prompt files |

---

## What Happens When You Edit Each Type

| You edit... | Effect | Takes effect... |
|---|---|---|
| `copilot-instructions.md` | All Copilot responses change | Next message |
| `*.instructions.md` | Responses for matching files change | Next message (with matching file open) |
| `*.agent.md` | Agent persona changes | Next message (with agent selected) |
| `*.prompt.md` | Slash command behavior changes | Next time you run `/command` |
| `SKILL.md` | Skill knowledge changes | Next time topic matches |
| Any `README.md` or `docs/*.md` | Nothing in Copilot changes | When a developer reads the file |

---

## Common Questions

**Q: If I put tips in a README, will Copilot follow them?**
A: No. README files in the `instructions/`, `agents/`, `prompts/`, `skills/`, and `docs/` folders are for developer reference only. To make Copilot follow rules, put them in the appropriate Copilot-read file (`.instructions.md`, `.agent.md`, etc.).

**Q: Can I reference a developer README from a Copilot file?**
A: Yes — prompt files support `[link text](path)` — but Copilot may or may not follow the link. It's better to include the rules directly in the Copilot file.

**Q: Does `copilot-instructions.md` count as a developer file too?**
A: It's primarily a 🤖 Copilot file (the AI reads it every request). But developers should also read it to understand the project-wide rules Copilot follows.

**Q: What about resource files inside skill folders (scripts, templates)?**
A: Files alongside `SKILL.md` (like `.sh` scripts, `.java` templates) are 🤖 Copilot resources — the AI can read and use them when the skill loads. They are *also* useful for developers as reference.

---

<p align="center">

[← Back to main guide](../README.md) · [Getting Started](getting-started.md) · [Customization Guide](customization-guide.md) · [Slash Commands](slash-commands.md) · [Instructions](../instructions/README.md) · [Agents](../agents/README.md) · [Prompts](../prompts/README.md) · [Skills](../skills/README.md)

</p>
