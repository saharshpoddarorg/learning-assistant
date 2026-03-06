# 🚦 START HERE — Your Entry Point to This Repo

> **You landed here. Perfect. This page tells you exactly where to go next.**  
> Takes 2 minutes to read. Skip nothing — it will save you hours.

---

## ❓ What IS This Repo?

A **super-powered developer workspace** with two independent feature sets you can use together or separately:

| Feature Set | What It Does | Where It Lives |
|---|---|---|
| **Copilot Customization** | Teaches GitHub Copilot your coding rules, gives it specialist personas, and adds 36 slash commands | `.github/` |
| **MCP Servers** | Connects Copilot to real tools — search Jira, browse learning docs, read GitHub repos, scrape web pages | `mcp-servers/` |

Both work in **this repo** AND can be **copied to any other project** you work on.

---

## 🗺️ Choose Your Path

> **Not sure which you are? Pick Newbie — you can skip ahead at any time.**

---

### 🟢 NEWBIE — "I'm new to Copilot, MCP, AI tools... all of this"

You don't know what MCP is, haven't customized Copilot before, and just want things to work.

**Your reading order (do these in sequence):**

| Step | Read This | Time | What You'll Get |
|---|---|---|---|
| 1 | **This page** (you're here) | 2 min | Know where to go |
| 2 | [Phase Guide — Read Me First §§ Phase 0–2](phase-guide.md#phase-0) | 10 min | Understand the whole system |
| 3 | [MCP Server Setup — Complete Guide](mcp-server-setup.md) | 10 min | Get MCP servers working |
| 4 | [Getting Started Tutorial](getting-started.md) | 30 min | Try Copilot customization hands-on |
| 5 | [Phase Guide — Phases 3–7](phase-guide.md#phase-3) | 20 min | Brain workspace + export to other projects |

**After reading, your first 5 actions:**
1. `Ctrl+Shift+B` → **"mcp-servers: build"** — compiles the Java servers
2. Open [`.vscode/mcp.json`](../../.vscode/mcp.json) → change `"disabled": true` to `"disabled": false` for `learning-resources`  
3. Press `Ctrl+Shift+P` → "Reload Window"
4. Open Copilot Chat (`Ctrl+Alt+I`) → switch to **Agent** mode
5. Type `/hub` → see all 36 slash commands

**Manage MCP servers after setup:**
```
Terminal → Run Task → "mcp-servers: status"   ← see what's running
Terminal → Run Task → "mcp-servers: start (learning-resources)"
Terminal → Run Task → "mcp-servers: stop (all)"
Terminal → Run Task → "mcp-servers: restart (atlassian)"
Terminal → Run Task → "mcp-servers: reset (all)"   ← nuclear reset: stop+clean+rebuild+start
```

---

### 🟡 AMATEUR — "I know VS Code and GitHub but haven't used MCP or Copilot customization"

You understand terminal commands, git, and file structures. You just haven't set up MCP or customized Copilot before.

**Your reading order:**

| Step | Read This | Time | Skip if... |
|---|---|---|---|
| 1 | [Phase Guide — Phases 1–3](phase-guide.md#phase-1) | 15 min | Skip Phase 0 (basics) |
| 2 | [MCP Server Setup §§5–7](mcp-server-setup.md#5-configure-credentials-secrets) | 10 min | You've done MCP before |
| 3 | [Customization Guide](customization-guide.md) | 20 min | You understand the 5 primitives |
| 4 | [Export Guide](export-guide.md) | 15 min | You only use this one repo |

**Jump straight to something specific:**
- Copy to another project → [Export Guide](export-guide.md)
- What all 36 commands do → [Slash Commands](slash-commands.md)
- MCP credentials setup → [MCP Server Setup §5](mcp-server-setup.md#5-configure-credentials-secrets)
- How to not lose work when pressing Enter → [Copilot Workflow Tips §Queue](copilot-workflow.md#queuing-multiple-instructions)

---

### 🔴 PRO — "I know MCP, Copilot, and just need the reference"

**Quick access everything:**

| I need to... | Go to |
|---|---|
| Find a slash command | [navigation-index.md §Slash Commands](navigation-index.md#-slash-command-quick-reference) |
| Copy `.github/` to another repo | [export-guide.md §Copilot customization](export-guide.md#1-copilot-customization-github) |
| Copy MCP servers to another repo | [export-guide.md §MCP Servers](export-guide.md#2-mcp-servers) |
| Configure Atlassian credentials | [mcp-server-setup.md §5b](mcp-server-setup.md#5b-atlassian-server-jira--confluence--bitbucket) |
| Queue Copilot instructions | [copilot-workflow.md](copilot-workflow.md) |
| Chain agents + prompts | [customization-guide.md §Composition](customization-guide.md#-how-to-extend-the-system) |
| Build the Java servers | `Ctrl+Shift+B` → `mcp-servers: build` |
| `.vscode/mcp.json` format | [.vscode/mcp.json](../../.vscode/mcp.json) |
| Full file map | [file-reference.md](file-reference.md) |

---

## 🏗️ System Architecture (2-minute overview)

```
YOUR VS CODE WORKSPACE
│
├── .github/                    ← Copilot reads this automatically
│   ├── copilot-instructions.md ← Always-on rules ("use Logger, not println")
│   ├── instructions/*.md       ← Rules for specific file types (Java → Java rules)
│   ├── agents/*.md             ← Specialist personas (Debugger, Designer, Mentor...)
│   ├── prompts/*.md            ← Slash commands (/dsa, /debug, /brain-new...)
│   └── skills/*/SKILL.md       ← Deep knowledge packs (~100+ resources, MCP guide...)
│
├── .vscode/
│   ├── mcp.json                ← Tells VS Code WHICH MCP servers to start ← KEY FILE
│   └── tasks.json              ← Build tasks (Ctrl+Shift+B)
│
├── mcp-servers/                ← The actual server code
│   ├── src/server/             ← Java: LearningResources + Atlassian servers
│   ├── user-config/            ← Your credentials go here (gitignored)
│   └── out/                    ← Compiled classes (gitignored, built by task)
│
├── mac-os/                     ← macOS dev environment learning module
│   └── docs/                   ← Homebrew, JDK, npm, IDEs, Docker guides
│
└── brain/                      ← Personal knowledge notes (inbox→notes→library)
    ├── inbox/                  ← Draft notes
    ├── notes/                  ← Active knowledge
    └── library/                ← Published, tagged reference
```

**The magic flow:**
```
You ask Copilot a question
    → Copilot reads .github/ (instructions + agent persona + skill)  
    → Copilot optionally calls MCP tools (search Jira, browse docs, read files)
    → Copilot answers with full context of your project AND external data
```

---

## ⚡ Most Common Things You'll Do

| Task | How | Doc |
|---|---|---|
| Use a slash command | `Ctrl+Alt+I` → Agent mode → type `/command` | [Slash Commands](slash-commands.md) |
| Switch Copilot persona | Chat mode dropdown → select agent name | [Agents Guide](../agents/README.md) |
| Build MCP servers | `Ctrl+Shift+B` → `mcp-servers: build` | [MCP Setup](mcp-server-setup.md) |
| Enable a server | Edit `.vscode/mcp.json` → `"disabled": false` | [mcp.json](../../.vscode/mcp.json) |
| See server status | `Terminal → Run Task` → `mcp-servers: status` | [scripts/README](../../mcp-servers/scripts/README.md) |
| Start a server | `Terminal → Run Task` → `mcp-servers: start (learning-resources)` | [server.ps1](../../mcp-servers/scripts/server.ps1) |
| Stop all servers | `Terminal → Run Task` → `mcp-servers: stop (all)` | [server.sh](../../mcp-servers/scripts/server.sh) |
| Restart / reset a server | `Terminal → Run Task` → `mcp-servers: restart` or `mcp-servers: reset` | [SETUP.md](../../mcp-servers/SETUP.md) |
| Tail a server log | `Terminal → Run Task` → `mcp-servers: logs (learning-resources)` | [scripts/README](../../mcp-servers/scripts/README.md) |
| Run demo mode | `Terminal → Run Task` → `mcp-servers: demo (learning-resources)` | [server.ps1](../../mcp-servers/scripts/server.ps1) |
| List all MCP tools | `Terminal → Run Task` → `mcp-servers: list-tools (atlassian)` | [server.sh](../../mcp-servers/scripts/server.sh) |
| Create a knowledge note | `/brain-new` in Chat | [Brain README](../../brain/ai-brain/README.md) |
| Set up macOS dev environment | `/mac-dev` in Chat — or read [mac-os/docs/START-HERE](../../mac-os/docs/START-HERE.md) | [mac-os module](../../mac-os/docs/START-HERE.md) |
| Add Copilot rules to another project | [Export Guide §1](export-guide.md#1-copilot-customization-github) | [Export Guide](export-guide.md) |
| Not lose work when chaining instructions | [Copilot Workflow](copilot-workflow.md) | [Workflow Tips](copilot-workflow.md) |
| Understand what all files do | [File Reference](file-reference.md) | [File Reference](file-reference.md) |

---

## 📂 Complete Documentation Map

> 🟢 = Newbie must-read · 🟡 = Amateur useful · 🔴 = Pro reference

| Doc | Audience | Purpose |
|---|---|---|
| **[START-HERE.md](START-HERE.md)** ← you are here | 🟢🟡🔴 | Entry point, audience paths, architecture overview |
| **[Phase Guide](phase-guide.md)** | 🟢🟡 | Step-by-step phases 0–7: from zero to fully operational |
| **[MCP Server Setup](mcp-server-setup.md)** | 🟢🟡 | Get MCP servers working: build, credentials, verify |
| **[Export Guide](export-guide.md)** | 🟡🔴 | Copy Copilot customization + MCP servers to other projects |
| **[Copilot Workflow Tips](copilot-workflow.md)** | 🟢🟡🔴 | Chat queuing, chaining, not losing context, best practices |
| **[Getting Started Tutorial](getting-started.md)** | 🟢🟡 | Hands-on: try every primitive (~30 min) |
| **[Customization Guide](customization-guide.md)** | 🟡🔴 | Architecture deep-dive: how the 5 primitives connect |
| **[Navigation Index](navigation-index.md)** | 🟡🔴 | Master lookup: all commands, agents, skills, files |
| **[Slash Commands](slash-commands.md)** | 🟡🔴 | All 36 commands: aliases, inputs, composition |
| **[File Reference](file-reference.md)** | 🟡🔴 | Which files Copilot reads vs. developer docs |

---

## 🆘 I'm stuck / something's not working

| Symptom | Go to |
|---|---|
| MCP tools don't appear in Copilot | [MCP Setup §10 Troubleshooting](mcp-server-setup.md#10-troubleshooting) |
| java not found when building | [MCP Setup §3a](mcp-server-setup.md#3a-jdk-21-java) |
| Copilot isn't following my rules | [Getting Started §Troubleshooting](getting-started.md#-troubleshooting) |
| I lose context when I press Enter | [Copilot Workflow](copilot-workflow.md) |
| I don't know which command to use | [Navigation Index](navigation-index.md) or type `/hub` |
| Atlassian server errors | [MCP Setup §5b](mcp-server-setup.md#5b-atlassian-server-jira--confluence--bitbucket) |
| How do I copy this to my real project? | [Export Guide](export-guide.md) |

---

**Navigation:** [Phase Guide →](phase-guide.md) · [MCP Setup →](mcp-server-setup.md) · [Export Guide →](export-guide.md) · [Copilot Workflow →](copilot-workflow.md) · [Getting Started →](getting-started.md) · [Navigation Index →](navigation-index.md)
