# USAGE — Complete Developer Guide

> **The single file to rule them all.** Everything you need to use, understand, extend, and master this
> repo — from absolute first steps to advanced architecture deep-dives.
>
> Pick your starting tier, read what applies, skip what doesn't.

---

## Table of Contents

1. [What This Repo Is (and Isn't)](#1-what-this-repo-is-and-isnt)
2. [Tier Guide — Who Are You?](#2-tier-guide--who-are-you)
3. [One-Time Setup](#3-one-time-setup)
4. [Copilot Customization — Using the AI Features](#4-copilot-customization--using-the-ai-features)
   - [Slash Commands (30 commands)](#41-slash-commands-30-commands)
   - [AI Agents (7 personas)](#42-ai-agents-7-personas)
   - [Skill Packs — Knowledge That Loads Automatically](#43-skill-packs--knowledge-that-loads-automatically)
   - [Coding Instructions — Rules Copilot Always Follows](#44-coding-instructions--rules-copilot-always-follows)
   - [Copilot Chat Workflow Tips](#45-copilot-chat-workflow-tips)
   - [Steering — Controlling Completion Order](#46-steering--controlling-completion-order)
5. [MCP Servers — Start, Stop, Restart, Reset, Use](#5-mcp-servers--start-stop-restart-reset-use)
   - [How MCP Works (Plain English)](#51-how-mcp-works-plain-english)
   - [VS Code Auto-Management via mcp.json](#52-vs-code-auto-management-via-mcpjson)
   - [Manual Lifecycle (scripts + VS Code Tasks)](#53-manual-lifecycle-scripts--vs-code-tasks)
   - [Learning Resources Server — All 10 Tools](#54-learning-resources-server--all-10-tools)
   - [Atlassian Server — All 27 Tools](#55-atlassian-server--all-27-tools)
   - [GitHub & Filesystem Servers](#56-github--filesystem-servers)
   - [MCP Configuration System](#57-mcp-configuration-system)
   - [Troubleshooting MCP](#58-troubleshooting-mcp)
6. [Search Engine — How It's Built](#6-search-engine--how-its-built)
7. [Brain Workspace — Personal Knowledge System](#7-brain-workspace--personal-knowledge-system)
8. [Code Sandbox — src/](#8-code-sandbox--src)
9. [Your Learning Journey — Using This Repo to Learn Everything](#9-your-learning-journey--using-this-repo-to-learn-everything)
   - [Learning Paths by Topic](#91-learning-paths-by-topic)
   - [Daily Learning Workflow](#92-daily-learning-workflow)
   - [Building Real Skills](#93-building-real-skills)
   - [Mac Dev Module — macOS Environment](#94-mac-dev-module--macos-environment)
10. [All VS Code Tasks Quick Reference](#10-all-vs-code-tasks-quick-reference)
11. [All Files Quick Reference](#11-all-files-quick-reference)
12. [Extending & Adding Your Own Features](#12-extending--adding-your-own-features)
13. [Copying to Another Project](#13-copying-to-another-project)

---

## 1. What This Repo Is (and Isn't)

### What it IS

A **super-charged VS Code workspace** that combines three things:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                             │
│  1. KNOWLEDGE BASE — 2,400+ lines of curated SE/CS reference material      │
│     DSA, system design, DevOps, Git, design patterns, industry practices,  │
│     career planning, tech trends 2025–26, and more.                        │
│                                                                             │
│  2. AI TUTOR — GitHub Copilot customized with 36 slash commands,           │
│     7 AI agents, 11 knowledge packs, and project-wide coding rules.        │
│     Ask questions, get lessons, explore concepts, practice code.           │
│                                                                             │
│  3. MCP TOOLS — Java servers that give Copilot real-world superpowers:     │
│     search ~100+ curated learning resources, scrape web pages, browse      │
│     your Jira/Confluence, query GitHub repos — all from Chat.              │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### What it ISN'T

- Not a rigid course — you control the pace and direction
- Not tied to one technology — Java is the sandbox language, but you can learn anything
- Not cloud-dependent — everything runs locally (except GitHub Copilot itself)

---

## 2. Tier Guide — Who Are You?

This guide uses three tiers throughout. Find yours and follow the hints:

| Tier | You Are... | First Stop |
|------|-----------|-----------|
| 🟢 **Newbie** | New to VS Code, Copilot, MCP, or even coding. Want things explained step by step. | Start at [§3 One-Time Setup](#3-one-time-setup) |
| 🟡 **Amateur** | You know git, terminal, and VS Code. New to MCP/Copilot customization. | Start at [§4 Copilot Customization](#4-copilot-customization--using-the-ai-features) |
| 🔴 **Pro** | You know MCP, Copilot, Java. Want architecture details and extension points. | Jump to [§6 Search Engine](#6-search-engine--how-its-built) or [§12 Extending](#12-extending--adding-your-own-features) |

---

## 3. One-Time Setup

> 🟢 **Newbie:** Do every step in order. Nothing is optional here.  
> 🟡 **Amateur:** Check versions (1a, 1b), skip rest if installed.  
> 🔴 **Pro:** `java -version` ≥21, Copilot active, `node -v` ≥18.

### Step 1 — Install VS Code

Download from **https://code.visualstudio.com/**. Install normally.

### Step 2 — Install GitHub Copilot

In VS Code: `Ctrl+Shift+X` → search `GitHub Copilot` → install **GitHub Copilot** + **GitHub Copilot Chat**. Sign in with GitHub when prompted. You need a Copilot subscription (free tier works for personal repos).

### Step 3 — Install JDK 21+ (for MCP servers)

```bash
# Check first — if this shows 21+, skip this step:
java -version

# If not installed:
# → https://adoptium.net/ → download "Eclipse Temurin 21 (LTS)"
# Windows: run installer, check "Set JAVA_HOME" + "Add to PATH"
# Mac: brew install --cask temurin@21
# Linux: sudo apt install temurin-21-jdk  (or your distro's equivalent)

# Verify (open a NEW terminal):
java -version    # must show 21+
javac -version   # same
```

### Step 4 — Install Node.js (for GitHub + Filesystem MCP servers)

```bash
# Check first:
node -v    # need 18+

# Install if missing → https://nodejs.org/ → LTS version
```

### Step 5 — Clone and Open

```bash
git clone https://github.com/saharshpoddarorg/learning-assistant.git
cd learning-assistant
code .       # opens the workspace in VS Code
```

### Step 6 — Run the Setup Wizard

```powershell
# Windows PowerShell (from the repo root):
.\mcp-servers\scripts\setup.ps1
```
```bash
# Linux/macOS:
./mcp-servers/scripts/setup.sh
```

This creates your local config file, browser data directory, and reports what needs attention.

### Step 7 — Build MCP Servers

```
Ctrl+Shift+B  →  select "mcp-servers: build"
```

Or in terminal:
```powershell
cd mcp-servers; .\build.ps1     # Windows
cd mcp-servers && ./build.sh    # Linux/Mac
```

### Step 8 — Enable the Learning Resources Server

1. Open [`.vscode/mcp.json`](.vscode/mcp.json)
2. Find the `"learning-resources"` block
3. Make sure `"disabled"` is **not** present (or set to `false`)
4. Press `Ctrl+Shift+P` → **"Developer: Reload Window"**

### Step 9 — Verify Copilot Works

1. `Ctrl+Alt+I` or `Ctrl+Shift+I` → opens Copilot Chat
2. Switch to **Agent** mode (dropdown at top of chat panel)
3. Type: `/hub`  
   You should see a list of all 36 slash commands → setup is working.

### Step 10 — Verify MCP Tools Are Available

In the same chat, type:
```
what MCP tools do you have available?
```
You should see tools like `search_resources`, `browse_vault`, `discover_resources`, etc.

---

## 4. Copilot Customization — Using the AI Features

> This is what separates this repo from a simple code editor. Copilot is transformed from a
> generic assistant into a specialized learning tutor, code reviewer, architect, and debugger.

### 4.1 Slash Commands (36 commands)

**How to use:**
1. Open Copilot Chat (`Ctrl+Alt+I`)
2. Make sure you're in **Agent** mode
3. Type `/` → command picker appears → select or type the command

**Full command list:**

| Command | What it does | Example input |
|---------|-------------|--------------|
| `/hub` | Browse all 36 commands by category | (no input needed) |
| `/learn-concept` | Learn any CS/SE concept from scratch | `binary search`, `TCP handshake`, `dependency injection` |
| `/deep-dive` | Multi-layered exploration with analogies + code + quiz | `event-driven architecture` |
| `/learn-from-docs` | Learn via official documentation links | `React hooks`, `Spring Boot` |
| `/reading-plan` | Create a structured study plan | `become a backend engineer in 3 months` |
| `/teach` | Explain the currently open file | (open a .java file first) |
| `/dsa` | Data structures & algorithms lesson | `binary tree → level-order traversal → java → advanced` |
| `/system-design` | HLD/LLD system design | `design a URL shortener` |
| `/devops` | DevOps, CI/CD, Docker, Kubernetes, Git | `kubernetes pod scheduling` |
| `/language-guide` | Language-specific learning path | `go`, `rust`, `python` |
| `/tech-stack` | Frameworks and databases | `compare Spring vs Quarkus` |
| `/sdlc` | SDLC phases, methodologies, practices | `agile vs kanban`, `testing pyramid` |
| `/mcp` | Learn & build MCP servers | `how do I add a new MCP tool?` |
| `/explore-project` | Analyze open-source project architecture | `spring-boot repo` |
| `/resources` | Search + browse curated learning resources | `java concurrency tutorials` |
| `/interview-prep` | Technical interview preparation | `system design: design Twitter`, `behavioral: conflict` |
| `/career-roles` | Job roles, skills, pay ranges, roadmaps | `staff engineer`, `devops`, `ML engineer` |
| `/design-review` | SOLID/GRASP design review of current file | (open a .java file) |
| `/refactor` | Identify refactoring opportunities | (open a file to refactor) |
| `/explain` | Beginner-friendly explanation of current file | (open any file) |
| `/debug` | Systematic bug investigation | paste the error, or open the file |
| `/impact` | Change impact & ripple analysis | `what breaks if I rename this method?` |
| `/composite` | Combine multiple modes in one session | `learn DSA + implement + review` |
| `/context` | Continue prior conversation or start fresh | |
| `/scope` | Set generic vs. code-specific learning | |
| `/multi-session` | Save/resume state across chat sessions | |
| `/daily-assist` | Finance, productivity, news, research | `analyze my spending`, `summarize tech news` |
| `/brain-new` | Create a knowledge note | `key insights from today's DSA session` |
| `/brain-publish` | Publish a note to archive with git commit | |
| `/brain-search` | Search across all note tiers | `binary tree traversal` |

**Pro tips:**
- Chain commands: `/dsa` → learn → `/teach` the code you just implemented → `/design-review`
- Use `/composite` for multi-step sessions without repeating context
- `/hub` always works as a navigation anchor when you're lost

### 4.2 AI Agents (7 personas)

Agents are specialist AI personas. Select them from the **mode dropdown** in Copilot Chat, or some are auto-selected by slash commands.

| Agent | File | Best For |
|-------|------|---------|
| **Learning Mentor** | `learning-mentor.agent.md` | Teaching concepts with analogies, worked examples, and practice questions. The default for `/learn-*` and `/dsa` commands. |
| **Designer** | `designer.agent.md` | Architecture review, SOLID principles, design patterns, refactoring. Used by `/design-review` and `/refactor`. |
| **Debugger** | `debugger.agent.md` | Systematic, hypothesis-driven bug finding. Asks clarifying questions before guessing. Used by `/debug`. |
| **Impact Analyzer** | `impact-analyzer.agent.md` | Traces what will break if you change something. Used by `/impact`. |
| **Code Reviewer** | `code-reviewer.agent.md` | Read-only review: correctness, style, edge cases, security. Never writes code. |
| **Daily Assistant** | `daily-assistant.agent.md` | Finance tracking, productivity planning, news summaries, web research. Used by `/daily-assist`. |
| **Thinking Beast Mode** | `Thinking-Beast-Mode.agent.md` | Deep autonomous research on any topic. Chains multiple searches, summarizes findings, provides sources. |

**How to use an agent directly:**
1. Open Chat → Agent mode
2. Click the mode dropdown (shows "Copilot" by default)
3. Select the agent name
4. Start your question — Copilot behaves as that persona

### 4.3 Skill Packs — Knowledge That Loads Automatically

Skills are Markdown knowledge files that Copilot reads when the topic matches. You don't need to do anything — they load automatically. But knowing what's in them helps you ask better questions.

| Skill | File | What's Inside |
|-------|------|--------------|
| **Software Engineering Resources** | `software-engineering-resources/SKILL.md` | 2,400+ lines: DSA, system design, DevOps, Git, design patterns, industry practices (Netflix, Google, Uber), tech trends 2025–26, OWASP security, SDLC, microservices, circuit breakers, rate limiting, real-world system case studies |
| **Java Learning Resources** | `java-learning-resources/SKILL.md` | Java 21+ features, tutorials, official docs, recommended books, test frameworks, Spring ecosystem |
| **Career Resources** | `career-resources/SKILL.md` | 10+ tech roles with skills matrices, salary data, day-in-the-life, career transition roadmaps |
| **Daily Assistant Resources** | `daily-assistant-resources/SKILL.md` | Finance tools, productivity frameworks, news sources, research techniques |
| **Java Build** | `java-build/SKILL.md` | How to compile and run Java code in this repo (no build tool needed) |
| **MCP Development** | `mcp-development/SKILL.md` | 1,980+ lines: MCP protocol internals, JSON-RPC 2.0, STDIO/SSE/HTTP transports, tool schemas, writing Java MCP servers |
| **Software Engineering Resources** | `software-engineering-resources/SKILL.md` | All the above SE topics — the main knowledge base |
| **Search Engine** | *(inline in code)* | The pluggable search engine used internally by MCP servers |

**To trigger a skill deliberately:** Just ask a question in the relevant domain. Asking "how does a token bucket rate limiter work?" automatically loads the SE resources skill. You'll see richer, more specific answers.

### 4.4 Coding Instructions — Rules Copilot Always Follows

Instructions files apply silently. When you edit a Java file, Copilot automatically:
- Uses `UpperCamelCase` for classes, `lowerCamelCase` for methods/variables
- Adds Javadoc to all public methods
- Keeps methods under 30 lines
- Prefers `var` for local variables, `final` for constants
- Uses `Logger` instead of `System.out.println`
- Uses `Objects.requireNonNull()` in constructors

These rules come from:
- `.github/copilot-instructions.md` — always-on project rules
- `.github/instructions/java.instructions.md` — Java-specific rules (applies to `*.java`)
- `.github/instructions/clean-code.instructions.md` — clean code practices (all files)
- `.github/instructions/build.instructions.md` — build conventions (`*.gradle`)

**You can add your own** — create `*.instructions.md` in `.github/instructions/` with a `---applyTo---` frontmatter pattern.

### 4.5 Copilot Chat Workflow Tips

**Don't lose work when pressing Enter:**

The biggest Copilot Chat mistake is pressing Enter before finishing your instructions. Use the **queue pattern**:

```
# Instead of:
"fix this bug"  [Enter]   ← Copilot starts immediately, may miss context

# Do this:
"I'm about to give you several things to do. Wait for my signal.
TASK 1: Fix the null pointer in ConfigManager.java line 47
TASK 2: Add a unit test for the fix
TASK 3: Update the Javadoc
GO"   [Enter]             ← Copilot does all 3 in sequence
```

**Use `/scope` to stay focused:**
```
/scope → code-specific   ← focus only on this project's code
/scope → generic         ← discuss concepts broadly
```

**Use `/context` to carry state:**
```
/context → continue      ← picks up where last session ended
/context → fresh         ← clean slate
```

**Read the knowledge packs directly:**
The skill files in `.github/skills/*/SKILL.md` are excellent reference material even without Copilot. The Software Engineering Resources skill alone covers most of what a senior engineer needs to know.

### 4.6 Steering — Controlling Completion Order

> **Full deep-dive:** [`.github/docs/copilot-workflow.md`](.github/docs/copilot-workflow.md) — "Steering" section.

**The core problem:** You send Request A. While Copilot is responding (or right after), you send Request B. Copilot may mix them, skip parts of A, or silently abandon A.

**The solution:** steer through explicit phrases and, optionally, by editing the instruction files Copilot reads on every message.

#### Which files to edit for permanent steering rules

| File | Applies when | What to add |
|------|-------------|-------------|
| [`.github/copilot-instructions.md`](.github/copilot-instructions.md) | **Every message, always** | Task-execution ordering rules — the most powerful option |
| [`.github/instructions/java.instructions.md`](.github/instructions/java.instructions.md) | `*.java` edits | "Do not move to the next file until the current one compiles" |
| [`.github/instructions/clean-code.instructions.md`](.github/instructions/clean-code.instructions.md) | All files | "Confirm each change before suggesting the next" |
| [`.github/prompts/*.prompt.md`](.github/prompts/) | Per slash command | Add step-gate phrasing inside each command prompt |
| [`.github/agents/*.agent.md`](.github/agents/) | Per agent persona | Make the agent announce ✓ after each step |

**Permanent rule to add to `.github/copilot-instructions.md`:**

```markdown
## Task Execution Rules
- Do multiple tasks ONE AT A TIME in the order listed.
- After each task, state: "✓ Task [N] complete: [one-line summary]"
- Do NOT start the next task until the current task's completion is stated.
- If blocked, say so explicitly and wait for my instruction.
- Never silently skip a task.
```

#### Completion gate phrases (use inline in any message)

| Gate | Phrase pattern | Use when |
|------|---------------|---------|
| **Stop marker** | `Do A. — STOP — Only after confirming A, begin B.` | Two tasks, strict order |
| **Confirmation question** | `After A, ask me "Ready for B?" and wait for my reply.` | You want manual approval before each step |
| **Checklist gate** | `Show updated [x] checklist after each task before continuing.` | 3+ tasks, need visibility |
| **HOLD** | `HOLD. Confirm A is complete and list all files changed before accepting new tasks.` | You sent a follow-up too soon |
| **Audit** | `Pause. List all files changed + task status (done/in-progress/not-started). Then resume.` | Mid-session drift |

#### How to add a new request without disrupting in-progress work

```
WRONG: Type while Copilot is responding → interrupts current work

RIGHT:
1. Wait for Copilot to finish its response completely.
2. Confirm the earlier task is done (read the response, check git diff).
3. Send a new message:

   "Good — [Task A] is confirmed complete.
   
   Now: [Task B description].
   Do not start B until you confirm A is done."
```

#### Detecting silently abandoned work

| Signal | Ask this |
|--------|---------|
| "I'll do that in the next step" | "Do it now before anything else." |
| Very short response to multi-part request | "List all N tasks with their status: done / skipped / not started." |
| Copilot says done but git shows no changes | "Show me the actual file. Did you write to disk?" |
| Next task started without mentioning previous | "Stop. What is the status of [Task A]? List files changed for it." |

---

## 5. MCP Servers — Start, Stop, Restart, Reset, Use

### 5.1 How MCP Works (Plain English)

> 🟢 **Newbie:** Read this. Critical context.

```
Without MCP:                         With MCP:
─────────────────                    ─────────────────────────────────────────
You: "What Java resources do         You: "What Java resources do you have?"
     you have?"                      
Copilot: "I don't have access        Copilot: [calls search_resources tool]
to your files or the internet."      Copilot: "I found 12 Java resources in
                                     your vault: Baeldung (tutorials),
                                     Official Oracle docs, Effective Java..."
```

**MCP = Model Context Protocol.** A standard (like USB) for connecting AI to external tools. VS Code starts the Java server processes, Copilot calls their tools like function calls, and you see the results in Chat.

**The three components:**

```
.vscode/mcp.json          ← REGISTRY: tells VS Code which servers exist
mcp-servers/out/          ← COMPILED CODE: the Java server programs
mcp-servers/user-config/  ← SECRETS: your API keys (gitignored)
```

### 5.2 VS Code Auto-Management via mcp.json

When you open the workspace, VS Code reads [`.vscode/mcp.json`](.vscode/mcp.json) and starts the enabled servers automatically. Copilot gains access to their tools transparently.

**To enable/disable a server:**
1. Open `.vscode/mcp.json`
2. Find the server block
3. Add `"disabled": true` (to disable) or remove/set `false` (to enable)
4. `Ctrl+Shift+P` → "Developer: Reload Window"

**The four configured servers:**

| Server | Key in mcp.json | Auto-starts | Credentials needed |
|--------|-----------------|-------------|-------------------|
| `learning-resources` | `learning-resources` | Yes (if not disabled) | None |
| `atlassian` | `atlassian` | Yes (if enabled) | Atlassian email + API token |
| `github` | `github` | Disabled by default | GitHub PAT |
| `filesystem` | `filesystem` | Disabled by default | None |

### 5.3 Manual Lifecycle (scripts + VS Code Tasks)

Use these when you want to control servers outside of VS Code's auto-management — for local testing, smoke checks, or debugging.

**Quick reference — all commands:**

```powershell
# Windows PowerShell
.\mcp-servers\scripts\server.ps1 help         # print all commands

.\mcp-servers\scripts\server.ps1 status       # see what's running
.\mcp-servers\scripts\server.ps1 validate     # check java, out/, config, API keys

.\mcp-servers\scripts\server.ps1 start  learning-resources
.\mcp-servers\scripts\server.ps1 start  atlassian
.\mcp-servers\scripts\server.ps1 start  all   # ← (runs all sequentially)

.\mcp-servers\scripts\server.ps1 stop   learning-resources
.\mcp-servers\scripts\server.ps1 stop   atlassian
.\mcp-servers\scripts\server.ps1 stop   all

.\mcp-servers\scripts\server.ps1 restart  learning-resources
.\mcp-servers\scripts\server.ps1 restart  atlassian

.\mcp-servers\scripts\server.ps1 reset  learning-resources  # stop + clean build + start
.\mcp-servers\scripts\server.ps1 reset  atlassian
.\mcp-servers\scripts\server.ps1 reset  all                 # nuclear option

.\mcp-servers\scripts\server.ps1 demo   learning-resources  # foreground, Ctrl-C to quit
.\mcp-servers\scripts\server.ps1 demo   atlassian

.\mcp-servers\scripts\server.ps1 list-tools  learning-resources
.\mcp-servers\scripts\server.ps1 list-tools  atlassian

.\mcp-servers\scripts\server.ps1 logs   learning-resources  # tail logs
.\mcp-servers\scripts\server.ps1 logs   atlassian
```

```bash
# Linux / macOS / Git Bash — same commands, just:
./mcp-servers/scripts/server.sh  <command>  <server>
```

**Via VS Code Tasks** (`Ctrl+Shift+B` or `Terminal → Run Task`):

| Category | Task name |
|----------|-----------|
| Build | `mcp-servers: build` |
| Build | `mcp-servers: build (clean)` |
| Setup | `mcp-servers: setup` |
| Status | `mcp-servers: status` |
| Validate | `mcp-servers: validate` |
| Start | `mcp-servers: start (learning-resources)` |
| Start | `mcp-servers: start (atlassian)` |
| Start | `mcp-servers: start (all)` |
| Stop | `mcp-servers: stop (all)` |
| Stop | `mcp-servers: stop (learning-resources)` |
| Stop | `mcp-servers: stop (atlassian)` |
| Restart | `mcp-servers: restart (learning-resources)` |
| Restart | `mcp-servers: restart (atlassian)` |
| Reset | `mcp-servers: reset (all)` |
| Reset | `mcp-servers: reset (learning-resources)` |
| Reset | `mcp-servers: reset (atlassian)` |
| Demo | `mcp-servers: demo (learning-resources)` |
| Demo | `mcp-servers: demo (atlassian)` |
| List tools | `mcp-servers: list-tools (learning-resources)` |
| List tools | `mcp-servers: list-tools (atlassian)` |
| Logs | `mcp-servers: logs (learning-resources)` |
| Logs | `mcp-servers: logs (atlassian)` |

**Via F5 (debug/run):**

Open `.vscode/launch.json` — select from:
- `LR — STDIO Server (production mode)` — same as how mcp.json runs it
- `LR — Demo mode` — test output without a live client
- `LR — List tools` — print tools and exit
- `Atlassian — STDIO Server (production mode)`
- `Atlassian — Demo mode`
- `Atlassian — List tools`
- `Config Loader — validate + dump`
- `All Servers — Demo mode` (compound)
- `All Servers — STDIO mode` (compound)

### 5.4 Learning Resources Server — All 10 Tools

> The first built-in MCP server. No credentials needed. Works out of the box after build.

Built-in: **~100+ curated learning resources** across Java, Web, DevOps, Cloud, AI/ML, System Design, and more.

**Available tools — ask Copilot to use them:**

| Tool | What it does | Example prompt |
|------|-------------|----------------|
| `search_resources` | Search vault by text, category, type, or difficulty | *"search for Java concurrency resources"* |
| `browse_vault` | Browse all resources grouped by category or type | *"browse all DevOps resources"* |
| `get_resource` | Get full details (URL, summary, difficulty) for one resource | *"get details on the Baeldung resource"* |
| `list_categories` | List all available resource categories | *"what categories are available?"* |
| `discover_resources` | Smart 3-mode discovery: specific / vague / exploratory | *"discover resources about event-driven architecture"* |
| `scrape_url` | Scrape any URL → content summary | *"scrape https://baeldung.com/java-concurrency"* |
| `read_url` | Scrape URL → full readable content | *"read the full content at [URL]"* |
| `add_resource` | Add a new resource to the vault manually | *"add this resource: title=..., url=..."* |
| `add_resource_from_url` | Scrape URL → auto-infer metadata → add to vault | *"add https://... to the resource vault"* |
| `export_results` | Export search results as Markdown, PDF, or Word | *"export those results as markdown"* |

**Example workflows:**

```
You: "Find me intermediate Java concurrency tutorials"
→ Copilot calls search_resources(category=JAVA, difficulty=INTERMEDIATE, type=TUTORIAL)
→ Returns: Baeldung fork-join, VirtualThread guides, etc.

You: "Now scrape the Baeldung one and summarize it"
→ Copilot calls scrape_url(url="https://baeldung.com/...")
→ Returns: 3-sentence summary + key concepts + reading time

You: "Add it to my vault"
→ Copilot calls add_resource_from_url(url="...")
→ Resource added with auto-extracted metadata
```

**Run in demo mode to see it in action:**
```powershell
.\mcp-servers\scripts\server.ps1 demo learning-resources
# Or:
cd mcp-servers
java -cp out server.learningresources.LearningResourcesServer --demo
```

### 5.5 Atlassian Server — All 27 Tools

> Requires credentials. See [mcp-servers/user-config/servers/atlassian/](mcp-servers/user-config/servers/atlassian/) for config files.

**Credential setup:**
1. Copy `atlassian-config.local.example.properties` to `atlassian-config.local.properties`
2. Fill in your Atlassian email, API token, and base URLs
3. Restart the server: `Terminal → Run Task → "mcp-servers: restart (atlassian)"`

**The 27 tools by product:**

**Jira (11 tools):**
- `get_issue`, `search_issues`, `create_issue`, `update_issue`, `add_comment`
- `get_sprint`, `get_board`, `list_projects`
- `get_issue_transitions`, `transition_issue`
- `get_project_components`

**Confluence (7 tools):**
- `get_page`, `search_pages`, `create_page`, `update_page`
- `get_space`, `list_spaces`
- `get_child_pages`

**Bitbucket (8 tools):**
- `get_repository`, `list_repositories`, `list_branches`
- `get_pull_request`, `list_pull_requests`, `create_pull_request`
- `get_commit`, `list_commits`

**Unified search (1 tool):**
- `unified_search` — cross-product search across Jira + Confluence + Bitbucket

**Example workflows:**

```
You: "What Jira issues are assigned to me in the current sprint?"
→ Copilot calls get_sprint(boardId=...) + search_issues(assignee=currentUser, sprint=current)
→ Returns formatted list with status, priority, story points

You: "Create a Jira story for adding a rate limiter to the API"
→ Copilot calls create_issue(type=Story, summary=..., description=...)
→ Returns the new issue key (e.g., PROJ-42)

You: "Find Confluence pages about our system architecture"
→ Copilot calls search_pages(query="system architecture")
→ Returns matching pages with summaries
```

### 5.6 GitHub & Filesystem Servers

Both are disabled by default. Enable in `.vscode/mcp.json`.

**GitHub server** (`@modelcontextprotocol/server-github`):
- Requires: Node.js + GitHub Personal Access Token
- Enables: repo search, issue/PR management, file contents, code search
- Set token: VS Code will prompt once and cache securely

**Filesystem server** (`@modelcontextprotocol/server-filesystem`):
- Requires: Node.js
- Enables: Copilot reads/writes files in your workspace
- Note: Already has partial access through the workspace context — use this for explicit file operations

### 5.7 MCP Configuration System

> 🔴 **Pro / those building new servers.**

The Java config system (`mcp-servers/src/config/`) uses a **3-layer merge strategy**:

```
Layer 3 (highest): Environment variables    MCP_APIKEYS_GITHUB=...
Layer 2:           Local config             user-config/mcp-config.local.properties
Layer 1 (base):    Committed config         user-config/mcp-config.properties
```

**Key config keys:**

```properties
# API keys
apiKeys.github=ghp_your_token_here
apiKeys.openai=sk-proj-your_key_here

# Location
location.timezone=America/New_York
location.locale=en-US

# User preferences
preferences.logLevel=INFO
preferences.timeoutSeconds=30
preferences.maxRetries=3

# Server definitions (add your own here)
server.my-server.name=My New Server
server.my-server.enabled=true
server.my-server.transport=stdio      # or: sse, streamable-http
server.my-server.command=java
server.my-server.args=-cp,out,server.myserver.MyServer
server.my-server.env.MY_TOKEN=        # ← set in local config

# Profiles
config.activeProfile=development
profile.development.preferences.logLevel=DEBUG
profile.production.preferences.timeoutSeconds=15
```

**Environment variable format:** Replace `.` with `_`, uppercase, prefix `MCP_`:
- `apiKeys.github` → `MCP_APIKEYS_GITHUB`
- `browser.dataDir` → `MCP_BROWSER_DATADIR`
- `server.atlassian.enabled` → `MCP_SERVER_ATLASSIAN_ENABLED`

**Data model (all Java records — immutable):**

```
McpConfiguration
├── ApiKeyStore           (Map: service → key)
├── LocationPreferences   (timezone, locale, region)
├── UserPreferences       (theme, logLevel, retries, timeout, autoConnect)
├── BrowserPreferences    (executable, profile, launchMode, headless, dataDir)
├── Map<String, ServerDefinition>
│   └── ServerDefinition  (name, enabled, transport, command, args, url, envVars)
│       └── TransportType (STDIO | SSE | STREAMABLE_HTTP)
└── Map<String, ProfileDefinition>
    └── ProfileDefinition (description, preferences, locationPrefs, browserPrefs, serverOverrides)
```

### 5.8 Troubleshooting MCP

| Problem | Likely cause | Fix |
|---------|-------------|-----|
| Tools don't appear in Copilot Chat | Server disabled or not built | Check `.vscode/mcp.json` disabled flag; run build task |
| `java not found` when building | JDK not on PATH | Set `JAVA_HOME` or add JDK bin to PATH |
| Server starts then immediately exits | Credentials missing or invalid | Check `atlassian-config.local.properties`; run `validate` task |
| Changes to Java code not taking effect | Old `out/` being used | Run `mcp-servers: build` (or `reset`) |
| Copilot doesn't use MCP tools | Not in Agent mode | Switch Chat to Agent mode (dropdown at top) |
| Tool call returns empty results | Search term too specific, or vault empty | Try broader search; run `demo` to verify server works |
| Port conflict | Another process using same port | MCP STDIO servers use no port — check if process is still running with `status` task |

---

## 6. Search Engine — How It's Built

> 🔴 **Pro.** This section explains the internal search engine architecture used by MCP servers.
> 🟡 **Amateur:** Skim the diagram. Skip implementation details.

The `search-engine/` module is a **generic, pluggable search pipeline** — a reusable library that
any MCP server can wire up in minutes. The Learning Resources server uses it to power
`search_resources` and `discover_resources`.

### Architecture — 5-Phase Pipeline

```
User query
    │
    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ Phase 1: CLASSIFY                                                           │
│ QueryClassifier → SearchMode (SPECIFIC | VAGUE | EXPLORATORY)              │
│ KeywordQueryClassifier maps keyword patterns to search modes               │
└─────────────────────────────────┬───────────────────────────────────────────┘
                                  │
┌─────────────────────────────────▼───────────────────────────────────────────┐
│ Phase 2: RETRIEVE                                                           │
│ SearchIndex<T> → candidate set                                              │
│ InMemoryIndex — ConcurrentHashMap, fast lookup by id or full scan          │
│ KeywordRegistry — ~130 keyword-to-enum mappings for concept inference      │
└─────────────────────────────────┬───────────────────────────────────────────┘
                                  │
┌─────────────────────────────────▼───────────────────────────────────────────┐
│ Phase 3: FILTER                                                             │
│ FilterChain → List<SearchFilter<T>>                                        │
│ Each filter: CategoryFilter, TypeFilter, DifficultyFilter, etc.            │
│ Filters are AND-chained (all must pass)                                    │
└─────────────────────────────────┬───────────────────────────────────────────┘
                                  │
┌─────────────────────────────────▼───────────────────────────────────────────┐
│ Phase 4: SCORE                                                              │
│ CompositeScorer → ScoringStrategy[]                                        │
│ TextMatchScorer:   fuzzy title/description match (Levenshtein, n-gram)     │
│ CategoryScorer:    enum match weight                                        │
│ TypeScorer:        resource type weight                                     │
│ DifficultyScorer:  proximity to requested level                            │
│ RecencyScorer:     newer resources get slight boost                        │
│ ConceptScorer:     ConceptArea overlap (36 concepts across 8 domains)      │
│ LanguageScorer:    language applicability match                            │
│ PopularityScorer:  based on vault usage / add count                        │
└─────────────────────────────────┬───────────────────────────────────────────┘
                                  │
┌─────────────────────────────────▼───────────────────────────────────────────┐
│ Phase 5: RANK                                                               │
│ ScoreRanker → sorts by composite score descending                          │
│ Returns top-N SearchResult<T> with score + match explanation               │
└─────────────────────────────────┬───────────────────────────────────────────┘
                                  │
                         SearchResult<T>[]
```

### Key Interfaces (all generic over T)

```java
// Wire up any domain-specific search engine:
SearchEngineConfig<LearningResource> config = SearchEngineConfig.<LearningResource>builder()
    .index(myIndex)
    .classifier(new KeywordQueryClassifier<>())
    .filters(List.of(categoryFilter, typeFilter, difficultyFilter))
    .scorer(new CompositeScorer<>(List.of(textMatch, categoryScorer)))
    .ranker(new ScoreRanker<>())
    .build();

ConfigurableSearchEngine<LearningResource> engine = new ConfigurableSearchEngine<>(config);
List<SearchResult<LearningResource>> results = engine.search(context);
```

### Smart Discovery — 3 Search Modes

The `discover_resources` tool uses **QueryClassifier** to pick the right strategy:

| Mode | When triggered | Strategy |
|------|----------------|----------|
| `SPECIFIC` | Exact concepts, technology names | Direct index lookup + strict filtering |
| `VAGUE` | Broad topics, partial matches | Keyword inference + flexible scoring |
| `EXPLORATORY` | General/curious queries | Full scan + relevance-ranked, with "did you mean?" |

**KeywordIndex** maps ~130 keywords to ConceptArea enums:
```
"binary search" → ConceptArea.SEARCHING
"microservices" → ConceptArea.DISTRIBUTED_SYSTEMS
"docker" → ConceptArea.CONTAINERIZATION
"react" → ConceptArea.FRONTEND
```

This means a query for "how do I run my app in containers" infers `CONTAINERIZATION` and returns Docker/Kubernetes resources.

### Extending the Search Engine

To add a new scoring dimension:

```java
// Implement ScoringStrategy<T>
public class FreshnessScorer<T extends LearningResource> implements ScoringStrategy<T> {
    @Override
    public double score(T item, SearchContext context) {
        // return 0.0–1.0
        long daysSinceAdded = ChronoUnit.DAYS.between(item.addedAt(), LocalDate.now());
        return Math.max(0.0, 1.0 - (daysSinceAdded / 365.0));
    }
}
```

Add it to the `CompositeScorer` config. That's it — no other changes needed.

To add a new filter:

```java
public class LanguageFilter<T extends LearningResource> implements SearchFilter<T> {
    @Override
    public boolean test(T item, SearchContext context) {
        return context.language() == null || item.applicability().supports(context.language());
    }
}
```

---

## 7. Brain Workspace — Personal Knowledge System

> Your personal knowledge system. Three tiers — inbox is gitignored, notes and archive are committed.

```
brain/ai-brain/
├── inbox/    ← Draft notes  [GITIGNORED — session-scoped, cleared when done]
├── notes/    ← Curated knowledge notes  [TRACKED — committed to repo]
└── archive/  ← Formally published reference  [TRACKED — tagged, project-attributed]
```

**Three-tier summary:**

| Tier | Git-tracked? | When to use |
|---|---|---|
| `inbox/` | ❌ | Raw capture — rough ideas, mid-session reasoning, throw-away |
| `notes/` | ✅ | Reviewed insights worth keeping — committed for sharing |
| `archive/` | ✅ | Formally published: full frontmatter (kind, project, tags, status) |

**Via Copilot Chat:**

| Command | What it does |
|---------|-------------|
| `/brain-new` | Create a new note. Prompts for topic → creates `YYYY-MM-DD_topic.md` in inbox or notes |
| `/brain-publish` | Move a note from inbox/notes → archive, add tags, commit to git |
| `/brain-search` | Full-text search across all tiers |
| `/brain-capture-session` | Convert current AI session into a structured session note |

**Via VS Code Tasks** (`Terminal → Run Task`):

| Task | Does |
|------|------|
| `brain: new note` | Interactive note creation |
| `brain: new note (notes tier)` | Skip inbox, go direct to notes/ (committed) |
| `brain: publish note` | Promote to archive + git commit |
| `brain: search notes` | Search by keyword |
| `brain: list notes` | List all notes |
| `brain: list archive` | List archived notes |
| `brain: status` | Count notes per tier |
| `brain: clear inbox (preview)` | Preview what clear would do |
| `brain: clear inbox (force)` | Clear all inbox notes (destructive!) |
| `brain: help` | Show all commands |

**Via terminal:**
```powershell
.\brain\ai-brain\scripts\brain.ps1 new
.\brain\ai-brain\scripts\brain.ps1 new --tier notes          # goes into committed notes/
.\brain\ai-brain\scripts\brain.ps1 publish                   # promote inbox/notes → archive
.\brain\ai-brain\scripts\brain.ps1 search "binary tree"
.\brain\ai-brain\scripts\brain.ps1 list
.\brain\ai-brain\scripts\brain.ps1 status
```
```bash
./brain/ai-brain/scripts/brain.sh new
./brain/ai-brain/scripts/brain.sh search "binary tree"
```

**The learning workflow:**

```
🟢 NEWBIE — Quick capture
1. Learn a concept via /learn-concept or /dsa
2. Drop raw notes in inbox/: /brain-new → target: inbox
3. Clear inbox at session end when done

🟡 AMATEUR — Refined knowledge
1. Review your inbox note after the session
2. Move to notes/ (committed): brain move file.md --tier notes
3. Or create directly: /brain-new → target: notes (committed immediately)
4. Notes are searchable, team-accessible via git

🔴 PRO — Formal reference
1. Start from a notes/ file (or inbox)
2. /brain-publish → add project, kind, tags, status frontmatter
3. Note moves to archive/<project>/<YYYY-MM>/YYYY-MM-DD_slug.md
4. git add + git commit happens automatically
```

**Existing notes:**

| Note | Topic | Tier |
|---|---|---|
| [`2026-02-21_session-mcp-server-fixes-and-restructure.md`](brain/ai-brain/notes/2026-02-21_session-mcp-server-fixes-and-restructure.md) | MCP server bug fix + config restructure | notes/ (committed) |
| [`2026-03-06_ghcp-knowledge-sharing-distilled.md`](brain/ai-brain/notes/2026-03-06_ghcp-knowledge-sharing-distilled.md) | GHCP customization KS — 3-tier distilled insights | notes/ (committed) |
| [`archive/ghcp-knowledge-sharing/2026-03/`](brain/ai-brain/archive/ghcp-knowledge-sharing/2026-03/) | Source reference for GHCP KS session (original slides + presenter notes) | archive/ (committed) |

---

## 8. Code Sandbox — src/

> `src/` is your personal playground. No constraints except the coding conventions.

**Current content:** `src/Main.java` — a minimal entry point you can expand.

**Use it to:**
- Implement algorithms after learning them via `/dsa`
- Prototype system designs from `/system-design` sessions
- Try Java 21+ features: virtual threads (`Thread.ofVirtual()`), records, sealed classes, pattern matching
- Write solutions to interview problems from `/interview-prep`
- Experiment with design patterns from `/design-review`

**Workflow:**
1. Learn a concept: `/dsa → merge sort → java`
2. Copilot produces code → review it
3. Open a new file in `src/` → implement from scratch
4. `/teach` on your implementation → Copilot explains + reviews
5. `/design-review` → check for SOLID violations
6. `/debug` if something's wrong
7. Capture insights → `/brain-new`

**Compile and run anything in src/:**
```powershell
# From the repo root:
javac -d out src\*.java
java -cp out Main
```

**Any language:** You can add Python, Go, Rust, JavaScript files here. They won't be compiled automatically, but Copilot will help you learn and work with them.

---

## 9. Your Learning Journey — Using This Repo to Learn Everything

> This section shows you HOW to use this repo as your complete learning operating system.

### 9.1 Learning Paths by Topic

#### Data Structures & Algorithms
```
1. Start: /dsa → pick a topic (arrays, linked lists, trees, graphs, DP...)
2. Learn: Copilot gives theory + code + complexity + interview patterns
3. Practice: implement in src/
4. Review: /design-review on your implementation
5. Capture: /brain-new with key patterns and complexity tables
6. Interview prep: /interview-prep → DSA → specific pattern
```

**Key skill content:** Arrays, stacks, queues, linked lists, trees (BST, AVL, Red-Black), heaps, tries, hash maps, graphs (BFS, DFS, Dijkstra, Bellman-Ford), sorting algorithms, binary search, dynamic programming, greedy, backtracking — all with complexity tables.

#### System Design
```
1. Start: /system-design → describe a system to design
2. Work through: HLD (components, data flow) → LLD (classes, APIs, DB schema)
3. Deep dive: /learn-concept → specific components (caching, CDN, message queues)
4. Real examples: ask about "how Netflix does X" → skill auto-loads case studies
5. Capture: /brain-new with design decisions and trade-offs
```

**Key skill content:** Scaling strategies, load balancing, caching (Redis, Memcached), CDN, databases (SQL vs NoSQL, sharding, replication), message queues (Kafka architecture), API design (REST, gRPC, GraphQL), rate limiting (token bucket, sliding window), circuit breakers, service discovery, distributed consensus.

#### DevOps & CI/CD
```
1. Start: /devops → pick a topic
2. Learn pipeline anatomy, then specific tools (Docker, K8s, Terraform)
3. Use the Java servers as real code to build CI pipelines for
4. Practice: write a mini Dockerfile for the mcp-servers Java app
5. Capture: /brain-new with pipeline patterns
```

**Key skill content:** CI/CD pipeline anatomy (GitHub Actions, Jenkins), Docker (image layers, multi-stage builds), Kubernetes (pods, deployments, services, HPA), GitOps (ArgoCD, Flux), deployment strategies (blue-green, canary, rolling), IaC (Terraform), monitoring & observability (metrics, logs, traces, Prometheus, Grafana, Jaeger).

#### Java (Language + Ecosystem)
```
1. Deep dive: /language-guide → java
2. Modern features: Java 21 records, sealed classes, pattern matching, virtual threads
3. Practice: implement in src/ using the coding conventions in copilot-instructions.md
4. Frameworks: /tech-stack → "spring boot microservice"
5. Testing: /sdlc → "testing pyramid java"
```

**Key skill content:** Java 21+ syntax, concurrency (ExecutorService, CompletableFuture, VirtualThread), Java records, functional interfaces, streams, Optional, collections, Spring Boot/Data/Security/Cloud, JUnit 5, Mockito, JPA/Hibernate.

#### Career and Interview Prep
```
1. Role exploration: /career-roles → "senior software engineer" or any role
2. Interview prep: /interview-prep → choose: DSA | system design | behavioral
3. Mock interview: ask Copilot to "interview me on system design for a URL shortener"
4. Study plan: /reading-plan → "prepare for FAANG in 3 months"
5. Track progress: /brain-new with completed topics and weak areas
```

#### MCP & AI Engineering
```
1. Start: /mcp → learn the protocol, then how the servers here work
2. Read the skill: .github/skills/mcp-development/SKILL.md (1,980 lines)
3. Explore the Java source: mcp-servers/src/server/learningresources/
4. Add a new tool: see §12 Extending
5. Build your own server: /mcp → "how do I write an MCP server for my own data?"
```

### 9.2 Daily Learning Workflow

Here's a sustainable daily pattern:

```
MORNING (~30 min):
├── Review yesterday's brain notes       /brain-search "yesterday"
├── Pick today's topic                   /hub → choose a domain
└── Watch /reading-plan if you have one

FOCUS SESSION (~1–2 hours):
├── Learn the concept deeply             /deep-dive → topic
├── Implement in src/                    write code
├── Review what you wrote                /design-review or /teach
└── Fix + refine                        /debug if needed

CAPTURE (~10 min):
├── Create a note                        /brain-new
├── Write key insights, trade-offs, code snippets
└── Publish when ready                   /brain-publish

EVENING (~15 min):
├── Check your MCP server tools work    /resources → "today's topic"
├── Scrape an interesting article       ask Copilot to scrape + summarize a URL
└── Plan tomorrow's topic               ask Copilot for a logical progression
```

### 9.3 Building Real Skills

The fastest path to actual competence is **build → break → understand → rebuild**:

1. **Learn** with Copilot (`/learn-concept`, `/dsa`, `/system-design`)
2. **Implement** in `src/` — even if it's wrong
3. **Review** with Copilot (`/design-review`, `/teach`)
4. **Break it intentionally** — add bugs, create edge cases
5. **Debug** (`/debug` or the Debugger agent)
6. **Extend** — add features, make it production-grade
7. **Document** — `/brain-new` an architecture note

For MCP servers specifically:
1. Run the demo: `.\mcp-servers\scripts\server.ps1 demo learning-resources`
2. Explore the tools in Copilot Chat
3. Read the Java source in `mcp-servers/src/server/learningresources/`
4. Add a new tool (see §12)
5. Study the search engine (`mcp-servers/src/search/`) — exemplary clean code

---

### 9.4 Mac Dev Module — macOS Environment

> **Location:** `mac-os/` · **Entry point:** `mac-os/docs/START-HERE.md`  
> **Slash command:** `/mac-dev` · **Hub:** `/hub mac`

An incremental, iterative learning module for setting up a professional macOS development environment.  
Follow it at your own pace — each guide is self-contained and 3-tier aware.

**Learning Path:**

| Tier | What You'll Do | Guides |
|---|---|---|
| 🟢 Newbie | Install Homebrew, JDK, VS Code — have Java running | [START-HERE](../mac-os/docs/START-HERE.md) → [Homebrew](../mac-os/docs/homebrew-guide.md) → [JDK Setup](../mac-os/docs/jdk-setup.md) |
| 🟡 Amateur | Add Docker, IntelliJ, nvm, shell aliases | [Dev Tools](../mac-os/docs/dev-tools-guide.md) → [npm on Mac](../mac-os/docs/npm-on-mac.md) → [Environment](../mac-os/docs/mac-dev-environment.md) |
| 🔴 Pro | Brewfile, dotfiles, bootstrap script, jenv | [Full Environment](../mac-os/docs/mac-dev-environment.md) → [Homebrew §Brewfile](../mac-os/docs/homebrew-guide.md) → [JDK §jenv](../mac-os/docs/jdk-setup.md) |

**Quick install in 3 commands:**
```zsh
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
brew install --cask temurin visual-studio-code iterm2
java --version && code --version
```

**Interactive guide (in Copilot Chat):**
```
/mac-dev → homebrew → install → newbie
/mac-dev → jdk → temurin → newbie
/mac-dev → docker → install → amateur
/mac-dev → dotfiles → bootstrap → pro
```

**Java environment checker** (compile and run from repo root):
```zsh
javac mac-os/src/development/MacEnvChecker.java -d mac-os/out
java -cp mac-os/out MacEnvChecker
```

---

## 10. All VS Code Tasks Quick Reference

Open with `Ctrl+Shift+B` (default build task) or `Terminal → Run Task` (all tasks):

### MCP Servers
| Task | Description |
|------|-------------|
| `mcp-servers: build` ⭐ | Compile all Java sources → `out/` |
| `mcp-servers: build (clean)` | Wipe `out/` then recompile |
| `mcp-servers: setup` | One-time setup wizard |
| `mcp-servers: status` | Show running / stopped state |
| `mcp-servers: validate` | Check java, `out/`, config, API keys |
| `mcp-servers: start (learning-resources)` | Start as background process |
| `mcp-servers: start (atlassian)` | Start (credentials required) |
| `mcp-servers: start (all)` | Start all servers |
| `mcp-servers: stop (all)` | Stop all running servers |
| `mcp-servers: stop (learning-resources)` | Stop individual server |
| `mcp-servers: stop (atlassian)` | Stop individual server |
| `mcp-servers: restart (learning-resources)` | Stop then start |
| `mcp-servers: restart (atlassian)` | Stop then start |
| `mcp-servers: reset (all)` | Stop → clean build → start all |
| `mcp-servers: reset (learning-resources)` | Individual server reset |
| `mcp-servers: reset (atlassian)` | Individual server reset |
| `mcp-servers: demo (learning-resources)` | Foreground demo (Ctrl-C to quit) |
| `mcp-servers: demo (atlassian)` | Foreground demo |
| `mcp-servers: list-tools (learning-resources)` | Print all 10 tools |
| `mcp-servers: list-tools (atlassian)` | Print all 27 tools |
| `mcp-servers: logs (learning-resources)` | Tail live log |
| `mcp-servers: logs (atlassian)` | Tail live log |

### Brain Workspace
| Task | Description |
|------|-------------|
| `brain: new note` | Create a new knowledge note |
| `brain: new note (notes tier)` | Create directly in notes/ |
| `brain: publish note` | Promote note → archive + git commit |
| `brain: search notes` | Full-text search |
| `brain: list notes` | List all active notes |
| `brain: list archive` | List archived notes |
| `brain: status` | Count notes per tier |
| `brain: clear inbox (preview)` | Preview what clear would do |
| `brain: clear inbox (force)` | Clear all inbox notes (irreversible) |
| `brain: help` | Show brain script help |

---

## 11. All Files Quick Reference

### Copilot-Read Files (in `.github/`)
| File | What it does |
|------|-------------|
| `copilot-instructions.md` | Always-on coding rules (naming, Javadoc, Logger, etc.) |
| `instructions/java.instructions.md` | Java rules — applied automatically to `*.java` |
| `instructions/clean-code.instructions.md` | Clean code rules — applied to all files |
| `instructions/build.instructions.md` | Build tool conventions — applied to `*.gradle` |
| `agents/*.agent.md` | 7 AI personas (select from Chat dropdown) |
| `prompts/*.prompt.md` | 36 slash commands (type `/command` in Chat) |
| `skills/*/SKILL.md` | Knowledge packs (auto-load by topic match) |

### MCP Server Files
| File | What it does |
|------|-------------|
| `.vscode/mcp.json` | Registry — which servers VS Code starts |
| `mcp-servers/build.ps1` / `build.sh` | Compile Java → `out/` |
| `mcp-servers/scripts/server.ps1` / `server.sh` | Lifecycle: start/stop/restart/reset/demo/logs |
| `mcp-servers/scripts/setup.ps1` / `setup.sh` | One-time onboarding wizard |
| `mcp-servers/user-config/mcp-config.properties` | Base config — safe defaults (committed) |
| `mcp-servers/user-config/mcp-config.local.properties` | Your secrets (gitignored) |
| `mcp-servers/src/server/learningresources/` | Learning Resources server source |
| `mcp-servers/src/server/atlassian/` | Atlassian server source |
| `mcp-servers/src/config/` | Config system (loader, model, validator) |
| `mcp-servers/src/search/` | Pluggable search engine |

### VS Code Config
| File | What it does |
|------|-------------|
| `.vscode/tasks.json` | All VS Code tasks (build, start, stop, brain, etc.) |
| `.vscode/launch.json` | F5 run/debug configs for Java servers |
| `.vscode/mcp.json` | MCP server registry |
| `mcp-servers/.vscode/launch.json` | Additional launch configs (module-level) |
| `mcp-servers/.vscode/settings.json` | Java project settings |
| `mcp-servers/.vscode/extensions.json` | Recommended extensions |

### Documentation
| Doc | Purpose |
|-----|---------|
| `USAGE.md` | **This file** — complete developer guide |
| `README.md` | Project overview + quick start |
| `.github/docs/START-HERE.md` | Entry point — audience paths |
| `.github/docs/phase-guide.md` | Step-by-step phases 0–7 |
| `.github/docs/getting-started.md` | 30-min hands-on tutorial |
| `.github/docs/mcp-server-setup.md` | MCP server setup (complete) |
| `.github/docs/slash-commands.md` | All 36 commands reference |
| `.github/docs/customization-guide.md` | How the 5 Copilot primitives connect |
| `.github/docs/export-guide.md` | Copy to another project |
| `.github/docs/copilot-workflow.md` | Chat patterns and best practices |
| `.github/docs/navigation-index.md` | Master lookup index |
| `.github/docs/search-engine.md` | Search engine architecture |
| `.github/docs/search-engine-algorithms.md` | Scoring algorithm details |
| `mcp-servers/README.md` | MCP module deep dive |
| `mcp-servers/SETUP.md` | Step-by-step setup (quick) |
| `mcp-servers/scripts/README.md` | Scripts framework documentation |
| `brain/ai-brain/README.md` | Brain workspace guide |

---

## 12. Extending & Adding Your Own Features

### Add a New Slash Command

1. Create `e.g., my-command.prompt.md` in `.github/prompts/`
2. Add YAML frontmatter:
   ```yaml
   ---
   mode: agent
   description: My new command that does X
   ---
   ```
3. Write the prompt body — describe what Copilot should do
4. Type `/my-command` in Copilot Chat → it appears instantly

### Add a New AI Agent

1. Create `my-agent.agent.md` in `.github/agents/`
2. Write the persona description (role, tone, behavior rules)
3. It appears in the Chat mode dropdown

### Add a New Knowledge Skill

1. Create `.github/skills/my-topic/SKILL.md`
2. Fill with Markdown content on your topic
3. Add `"my-topic"` as a skill name to reference it in prompts
4. Copilot auto-loads it when questions match

### Add a New MCP Tool (to Learning Resources Server)

1. Open `mcp-servers/src/server/learningresources/handler/ToolHandler.java`
2. Add a case to the switch dispatch
3. Create `MyToolHandler.java` in the `handler/` package
4. Register the tool in the server's `--list-tools` output
5. Build: `mcp-servers: build` task
6. Restart: `mcp-servers: restart (learning-resources)` task

### Add a Completely New MCP Server

1. Create `mcp-servers/src/server/myserver/MyServer.java` with a `main()` that:
   - Reads from stdin (JSON-RPC 2.0 messages)
   - Writes to stdout (JSON-RPC 2.0 responses)
   - Handles `tools/list` and `tools/call` requests
2. Add a server definition to `mcp-servers/user-config/mcp-config.properties`:
   ```properties
   server.my-server.name=My Server
   server.my-server.enabled=true
   server.my-server.transport=stdio
   server.my-server.command=java
   server.my-server.args=-cp,out,server.myserver.MyServer
   ```
3. Add it to `.vscode/mcp.json`:
   ```jsonc
   "my-server": {
     "type": "stdio",
     "command": "java",
     "args": ["-cp", "out", "server.myserver.MyServer"],
     "cwd": "${workspaceFolder}/mcp-servers"
   }
   ```
4. Add it to `scripts/server.ps1` and `server.sh` SERVER_CLASS maps
5. Build and reload window

See the `/mcp` slash command and `.github/skills/mcp-development/SKILL.md` for the full MCP protocol reference.

### Add a New Search Scoring Dimension

See [§6 Search Engine](#6-search-engine--how-its-built) for the interface. Implement `ScoringStrategy<T>` and add to the `CompositeScorer` config.

---

## 13. Copying to Another Project

Both the Copilot customization system and the MCP servers are **portable**.

### Copy Copilot Customization

```bash
cp -r .github/                /path/to/target/project/.github/
# Done. Copilot in the target project now has all agents, prompts, skills, and instructions.
```

Customize after copying:
- Edit `copilot-instructions.md` for the target project's language/stack
- Add `instructions/*.instructions.md` for the target's file types
- Trim skills that aren't relevant

### Copy MCP Servers

```bash
cp -r mcp-servers/             /path/to/target/project/mcp-servers/
cp    .vscode/mcp.json         /path/to/target/project/.vscode/mcp.json
cp    .vscode/tasks.json       /path/to/target/project/.vscode/tasks.json  # merge, don't overwrite
cp    .vscode/launch.json      /path/to/target/project/.vscode/launch.json  # merge
```

Then in the target project:
```powershell
# Windows
.\mcp-servers\scripts\setup.ps1
```
```bash
# Linux/Mac
./mcp-servers/scripts/setup.sh
```

**Gitignore to add:**
```gitignore
mcp-servers/user-config/mcp-config.local.properties
mcp-servers/user-config/servers/**/*.local.properties
mcp-servers/out/
mcp-servers/scripts/.pids/
mcp-servers/scripts/.logs/
```

See [`.github/docs/export-guide.md`](.github/docs/export-guide.md) for a detailed checklist.

---

## Appendix — Quick Command Cheatsheet

```
COPILOT CHAT SHORTCUTS
──────────────────────
Ctrl+Alt+I or Ctrl+Shift+I   Open Copilot Chat
/hub                          Browse all 36 commands
/learn-concept <topic>        Learn anything from scratch
/dsa <structure/algorithm>    Deep DSA lesson
/system-design <system>       Design a system
/devops <topic>               DevOps, Docker, K8s, CI/CD
/mcp                          Learn MCP protocol
/interview-prep               Interview practice
/brain-new                    Create a knowledge note
/brain-search <term>          Search your notes

MCP SERVER TASKS (Ctrl+Shift+B → Run Task)
──────────────────────────────────────────
mcp-servers: build            Compile Java sources
mcp-servers: status           See what's running
mcp-servers: start (learning-resources)
mcp-servers: stop (all)
mcp-servers: restart (<name>)
mcp-servers: reset (all)      Nuclear: stop+clean+rebuild+start
mcp-servers: demo (<name>)    Foreground demo (Ctrl-C quit)
mcp-servers: list-tools (<name>)
mcp-servers: logs (<name>)    Tail live log
mcp-servers: validate         Check config + environment

BRAIN TASKS
───────────
brain: new note               Create a draft note
brain: publish note           Promote to archive
brain: search notes           Full-text search
brain: status                 Note counts per tier

TERMINAL (Windows PowerShell)
─────────────────────────────
.\mcp-servers\scripts\server.ps1 help
.\mcp-servers\scripts\server.ps1 status
.\mcp-servers\scripts\server.ps1 start  learning-resources
.\mcp-servers\scripts\server.ps1 stop   all
.\mcp-servers\scripts\server.ps1 reset  all
.\mcp-servers\scripts\server.ps1 demo   learning-resources
.\mcp-servers\scripts\server.ps1 logs   learning-resources
.\mcp-servers\scripts\server.ps1 validate

TERMINAL (Linux/Mac/Git Bash)
──────────────────────────────
./mcp-servers/scripts/server.sh help
./mcp-servers/scripts/server.sh status
./mcp-servers/scripts/server.sh start  learning-resources
./mcp-servers/scripts/server.sh stop   all
./mcp-servers/scripts/server.sh reset  all
./mcp-servers/scripts/server.sh demo   learning-resources
./mcp-servers/scripts/server.sh logs   learning-resources
./mcp-servers/scripts/server.sh validate
```

---

*Last updated: 2026-02-27 — covers server lifecycle scripts, all VS Code tasks, complete search engine architecture, and 30-tier usage guide.*
