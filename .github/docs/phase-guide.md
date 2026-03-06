# 📋 Phase Guide — Zero to Fully Operational

> **Who this is for:** Anyone setting up this repo from scratch, or iteratively adding features.  
> **Read [START-HERE.md](START-HERE.md) first** if you haven't — it tells you which phases apply to you.

---

## 📑 Table of Contents

- [Phase 0: Orient — What Is All This?](#phase-0-orient--what-is-all-this) 🟢
- [Phase 1: Prerequisites — Install Once](#phase-1-prerequisites--install-once) 🟢
- [Phase 2: Copilot Customization — Works Immediately](#phase-2-copilot-customization--works-immediately) 🟢
- [Phase 3: MCP Servers — Build & Connect](#phase-3-mcp-servers--build--connect) 🟢🟡
- [Phase 4: Configure Credentials](#phase-4-configure-credentials--secrets) 🟢🟡
- [Phase 5: Brain Workspace](#phase-5-brain-workspace--your-knowledge-base) 🟡
- [Phase 6: Use Everything Together](#phase-6-use-everything-together) 🟡🔴
- [Phase 7: Export to Another Project](#phase-7-export-to-another-project) 🟡🔴
- [iterative Enhancement — Build Incrementally](#iterative-enhancement--build-incrementally) 🔴

---

## Audience Legend
> 🟢 **Newbie** — explains every term · 🟡 **Amateur** — knows basics · 🔴 **Pro** — reference only

---

## Phase 0: Orient — What Is All This?

> 🟢 **Newbie:** Read this whole section. It's 5 minutes that will save hours of confusion.  
> 🟡 **Amateur:** Skim the diagram. Skip to Phase 1.  
> 🔴 **Pro:** Skip entirely.

### What you have

| Component | Plain-English Description |
|---|---|
| **`.github/` folder** | A special folder that GitHub Copilot reads automatically. Put files here → Copilot learns your rules, personas, commands. |
| **`.vscode/mcp.json`** | A config file that tells VS Code which helper programs (MCP servers) to run when you open this project. |
| **`mcp-servers/`** | The actual Java programs that become those "helper tools" — they let Copilot look up Jira tickets, browse docs, read files, etc. |
| **`brain/`** | A personal note-taking system. Inbox → notes → archive. Use Copilot slash commands to create/search notes. |
| **`src/`** | A code sandbox. Experiment here. The Java in `src/` is a learning playground, separate from the MCP servers. |

### What "MCP" means (🟢 Newbie only)

> **MCP = Model Context Protocol.** It's a standard that lets AI assistants (Copilot, Claude, etc.) connect to external tools.  
> Think of it like USB — one standard plug that works with any device. Instead of Copilot only knowing your open files, MCP lets it search Jira, read documentation, query databases, etc.

### What "Copilot Customization" means (🟢 Newbie only)

> By default, Copilot is generic. Customization makes it specific to YOUR project:  
> - **Instructions files** = rules it always follows ("use Logger, not System.out.println")  
> - **Agents** = specialist personas ("act as a code reviewer, not a code writer")  
> - **Prompts** = saved commands you type as `/command` shortcuts  
> - **Skills** = deep knowledge packs it loads when the topic matches

---

## Phase 1: Prerequisites — Install Once

> 🟢 **Newbie:** Do every step. This is one-time setup.  
> 🟡 **Amateur:** Check versions, install what's missing.  
> 🔴 **Pro:** `java -version` (need 21+), `node -v` (need 18+), VS Code Copilot extension active.

### 1.1 — VS Code + GitHub Copilot

1. Install VS Code: **https://code.visualstudio.com/**
2. In VS Code: click Extensions icon (left sidebar, looks like blocks)
3. Search **"GitHub Copilot"** → install both:
   - `GitHub Copilot` (suggestions)
   - `GitHub Copilot Chat` (the chat panel)
4. Sign in with your GitHub account when prompted

> **Why:** Copilot Chat is where you type `/commands` and talk to AI agents.

### 1.2 — Clone or Download This Repo

```bash
# If you have git installed:
git clone https://github.com/saharshpoddarorg/learning-assistant.git
cd learning-assistant

# Then open in VS Code:
code .
```

> **No git?** Download the ZIP from GitHub → unzip → File → Open Folder in VS Code.

### 1.3 — JDK 21+ (Java for MCP servers)

**Check first:**
```powershell
java -version
```
If it shows `21` or higher → skip to 1.4.

**Install if missing:**
1. Go to **https://adoptium.net/**
2. Download "Eclipse Temurin 21 (LTS)" for your OS
3. Run installer — on Windows, check **"Set JAVA_HOME"** and **"Add to PATH"**
4. Open a **new** terminal → run `java -version` again

### 1.4 — Node.js (for GitHub + Filesystem MCP servers — optional)

Only needed if you want the GitHub or Filesystem community servers. Learning Resources and Atlassian servers are pure Java — no Node.js needed.

**Check:** `node --version` (need 18+)  
**Install if missing:** https://nodejs.org/ → download LTS version

### 1.5 — Recommended VS Code Extensions

In VS Code: `Ctrl+Shift+X` → install these:

| Extension | Why | ID |
|---|---|---|
| Java Extension Pack | Java support for MCP server development | `vscjava.vscode-java-pack` |
| Red Hat Java Language Server | Powers Java IntelliSense | `redhat.java` |
| GitLens | Git blame, history, diff | `eamodio.gitlens` |
| (Optional) Git Graph | Visual branch history | `mhutchie.git-graph` |

> 🟢 **Tip:** When you open `mcp-servers/` folder, VS Code will prompt "Install recommended extensions?" — say **Yes**. The list is in `mcp-servers/.vscode/extensions.json`.

---

## Phase 2: Copilot Customization — Works Immediately

> 🟢 **Newbie:** This phase requires ZERO setup. Just open the folder and it works.  
> 🟡 **Amateur:** Skim to understand what's already active.  
> 🔴 **Pro:** See [customization-guide.md](customization-guide.md) for architecture details.

### What's already working (no setup needed)

The moment you open this folder in VS Code with Copilot installed:

| What's active | How to verify |
|---|---|
| **Project rules** | Ask Copilot to write a Java method — it uses `Logger`, proper naming, Javadoc |
| **Specialist agents** | Copilot Chat → mode dropdown → see Designer, Debugger, Learning-Mentor, etc. |
| **36 slash commands** | Copilot Chat → type `/` → see all commands in picker |
| **Auto-loaded skills** | Ask Copilot "explain binary search" → it loads the skill automatically |

### 2.1 — Verify it works: Try a slash command

1. Open Copilot Chat: `Ctrl+Alt+I`
2. Make sure mode is **"Agent"** (dropdown at top of chat, not "Ask" or "Edit")
3. Type `/hub` → press Enter
4. Copilot shows all 36 commands organized by category

> **If `/hub` doesn't appear in the dropdown:** The `.github/prompts/` folder isn't being read. Check that you opened the repo root folder, not a subfolder.

### 2.2 — Try an agent

1. In Copilot Chat mode dropdown → select **"Learning-Mentor"**
2. Type: `Explain how Java generics work`
3. You should get a structured lesson with analogies, not just a code snippet

### 2.3 — What's in the `.github/` folder

```
.github/
├── copilot-instructions.md        ← Always active: your project rules
├── instructions/
│   ├── java.instructions.md       ← Active when editing .java files
│   └── clean-code.instructions.md ← Active when editing .java files
├── agents/                        ← 7 specialist personas
│   ├── learning-mentor.agent.md
│   ├── designer.agent.md
│   ├── debugger.agent.md
│   ├── impact-analyzer.agent.md
│   ├── code-reviewer.agent.md
│   ├── daily-assistant.agent.md
│   └── Thinking-Beast-Mode.agent.md
├── prompts/                       ← 36 slash commands
│   └── [36 *.prompt.md files]
└── skills/                        ← 11 knowledge packs
    ├── software-engineering-resources/
    ├── java-learning-resources/
    ├── java-build/
    ├── mcp-development/
    └── [7 more...]
```

### 2.4 — How to customize further

> 🟡 **Amateur+:** Add your own rules, agents, or commands.

| Want to add | Create this | Example |
|---|---|---|
| New coding rule | Line in `copilot-instructions.md` | `Always use Optional instead of null returns` |
| Rule only for Python files | `instructions/python.instructions.md` with `applyTo: **/*.py` | Python naming conventions |
| New specialist persona | `agents/my-agent.agent.md` | Security reviewer, performance analyst |
| New slash command | `prompts/my-command.prompt.md` | `/standup` that drafts daily standups |

See [customization-guide.md](customization-guide.md) for detailed how-to.

---

## Phase 3: MCP Servers — Build & Connect

> 🟢 **Newbie:** Follow every step in order. See [mcp-server-setup.md](mcp-server-setup.md) for the full guide.  
> 🟡 **Amateur:** Quick version below. Full guide in [mcp-server-setup.md](mcp-server-setup.md).  
> 🔴 **Pro:** `Ctrl+Shift+B` → `mcp-servers: build` → edit `.vscode/mcp.json` → reload.

### 3.1 — Build the Java servers

**Option A: VS Code task (recommended)**
```
Ctrl+Shift+B → select "mcp-servers: build"
```

**Option B: Terminal**
```powershell
# Windows PowerShell
cd mcp-servers
.\build.ps1
```
```bash
# macOS / Linux
cd mcp-servers
./build.sh
```

Expected output:
```
BUILD SUCCESS -- compiled 150 files
```

> **"javac not found"?** Java isn't on PATH. See [Phase 1.3](#13--jdk-21-java-for-mcp-servers).

### 3.2 — The MCP registry file

Open **`.vscode/mcp.json`** — this file tells VS Code which servers to start.

The servers in this file:
| Server | Status | Needs |
|---|---|---|
| `learning-resources` | ✅ Enabled by default | Nothing — works out of the box |
| `atlassian` | Disabled (needs credentials) | Atlassian email + API token |
| `github` | Disabled (needs token) | GitHub Personal Access Token |
| `filesystem` | Disabled (optional) | Nothing |

### 3.3 — Enable the Learning Resources server

The Learning Resources server is already `"disabled": false` in `.vscode/mcp.json`. Just reload VS Code:

```
Ctrl+Shift+P → "Reload Window" → Enter
```

### 3.4 — Verify MCP tools appear in Copilot

1. Open Copilot Chat (`Ctrl+Alt+I`) → Agent mode
2. Click the **🔧 tools icon** (near the chat input box)
3. You should see: `learning-resources` with ~10 tools listed

**Test it:**
```
In Copilot Chat: "Search for Java concurrency learning resources"
```
Copilot will call the `search_resources` tool and return actual results.

---

## Phase 4: Configure Credentials (Secrets)

> 🟢 **Newbie:** Only do the servers you want. Start with Atlassian if you use Jira.  
> 🟡 **Amateur:** See detailed instructions in [mcp-server-setup.md §5](mcp-server-setup.md#5-configure-credentials-secrets).  
> 🔴 **Pro:** Fill `atlassian-config.local.properties`, set `"disabled": false` in `mcp.json`, reload.

### 4.1 — Atlassian (Jira + Confluence + Bitbucket)

**Step 1 — Get API token:**
- Go to https://id.atlassian.com/manage-profile/security/api-tokens
- Click "Create API token" → name it → copy it immediately

**Step 2 — Create local config:**
```powershell
# Windows
Copy-Item mcp-servers\user-config\servers\atlassian\atlassian-config.local.example.properties `
          mcp-servers\user-config\servers\atlassian\atlassian-config.local.properties
```

**Step 3 — Fill in credentials** in the new file:
```properties
atlassian.instance.name=work-cloud
atlassian.variant=cloud
atlassian.jira.baseUrl=https://YOUR-DOMAIN.atlassian.net
atlassian.confluence.baseUrl=https://YOUR-DOMAIN.atlassian.net/wiki
atlassian.auth.email=you@company.com
atlassian.auth.token=YOUR_API_TOKEN
atlassian.product.jira.enabled=true
atlassian.product.confluence.enabled=true
```

**Step 4 — Enable in `.vscode/mcp.json`:**
```json
"atlassian": {
    ...
    "disabled": false    ← change this
}
```

**Step 5 — Reload:** `Ctrl+Shift+P` → "Reload Window"

### 4.2 — GitHub

Open `.vscode/mcp.json` → set `"disabled": false` for `github`.  
VS Code will prompt for your GitHub Personal Access Token the first time Copilot uses it.

**Get your token:** https://github.com/settings/tokens → Generate → scopes: `repo` (or `public_repo`)

### 4.3 — Never commit secrets

Files that are gitignored (safe for secrets):
```
mcp-servers/user-config/mcp-config.local.properties
mcp-servers/user-config/servers/atlassian/atlassian-config.local.properties
mcp-servers/build.env.local
```

Files that are committed (no secrets allowed):
```
mcp-servers/user-config/mcp-config.properties          ← base defaults only
mcp-servers/user-config/servers/atlassian/atlassian-config.properties  ← base defaults only
.vscode/mcp.json                                       ← server registry (uses VS Code inputs for secrets)
```

---

## Phase 5: Brain Workspace — Your Knowledge Base

> 🟢 **Newbie:** This is optional. Your personal note vault — Copilot writes, you organize.  
> 🟡 **Amateur:** Use the VS Code tasks or slash commands.  
> 🔴 **Pro:** `brain.ps1 new|publish|search` — see `brain/scripts/README.md`.

### What it is

```
brain/
├── inbox/      ← Raw capture (drafts, quick grabs — gitignored)
├── notes/      ← Your writing — distilled insights, session logs, decisions
└── library/    ← Imported sources — slide decks, reference docs you preserved
```

**One routing question:** "Did you write this yourself?"
- Yes → `notes/` | No (imported external content) → `library/`

**Two typical flows:**
```
brain-new   → inbox/ → (edit/refine) → notes/
brain publish → inbox/ → library/<project>/<YYYY-MM>/
```

### 5.1 — Create a note with Copilot

In Copilot Chat (Agent mode):
```
/brain-new
→ Title: "Java generics cheatsheet"
→ Tier: notes
→ Project: java
```
Copilot creates a properly formatted markdown note with YAML frontmatter.

### 5.2 — Search notes

```
/brain-search
→ "generics"
→ tier: notes
```

### 5.3 — Publish an imported source to library

```
/brain-publish
→ brain/ai-brain/inbox/GHCP_Agents_Guide.md
→ Project: ghcp-knowledge-sharing
```
Copilot archives the source file to `library/<project>/<YYYY-MM>/`, prompts for tags, and commits it.

### 5.4 — VS Code Tasks for brain

`Ctrl+Shift+P` → "Run Task" (or `Terminal → Run Task`):
- `brain: new note`
- `brain: list notes`
- `brain: search notes`
- `brain: status`
- `brain: publish note`

---

## Phase 6: Use Everything Together

> 🟡 **Amateur+:** This is where the system becomes genuinely powerful.

### Example 1: Learning session with resources

```
1. Switch to "Learning-Mentor" agent
2. /dsa → binary trees → learn-concept → python → intermediate
3. Copilot explains the concept (using DSA prompt template)
4. /resources → search → "binary tree Java visualization"
5. MCP Learning Resources server finds actual docs and tutorials
6. /brain-new → save the session as a knowledge note
```

### Example 2: Debugging a real issue

```
1. Open the file with the bug
2. Switch to "Debugger" agent
3. /debug → Copilot investigates systematically
4. /impact → see what else might break if you change this
5. /refactor → clean up after fixing
6. /brain-new → document what you learned about this bug pattern
```

### Example 3: Planning a system design for a Jira ticket

```
1. Enable Atlassian MCP server (Phase 4.1)
2. "Find the PROJ-123 Jira ticket and summarize the requirements"
   → Copilot calls jira_get_issue tool
3. Switch to "Designer" agent
4. /system-design → LLD → "design a caching layer for these requirements"
5. /design-review → review the design for SOLID violations
```

### Chaining instructions without losing work

> 🟢 **Newbie:** This is the "I pressed Enter but Copilot started a new topic" problem.

**Problem:** You give Copilot one instruction. While it works, you think of more things to tell it. You type them and press Enter, and Copilot stops the first task and starts the second.

**Solution:** See **[Copilot Workflow Tips](copilot-workflow.md)** — covers multi-turn chaining, /multi-session for saving state, and how to build a task queue.

---

## Phase 7: Export to Another Project

> 🟡 **Amateur+:** Copy any subset of this repo's features to another project.  
> 🔴 **Pro:** See [export-guide.md](export-guide.md) for exact commands.

### What you can export independently

| Feature | Complexity | What to copy |
|---|---|---|
| Copilot rules only | ⭐ Simple | Just `.github/copilot-instructions.md` |
| Full Copilot customization | ⭐⭐ Moderate | Entire `.github/` folder |
| MCP servers only | ⭐⭐⭐ Involved | `mcp-servers/` + `.vscode/mcp.json` + build |
| Everything | ⭐⭐⭐ Involved | All the above |

**Quick export:** See [export-guide.md](export-guide.md) — it has exact copy commands for each scenario.

---

## Iterative Enhancement — Build Incrementally

> 🔴 **Pro / Everyone building on top of this.**

### The building loop

```
1. PICK one feature to add or improve
2. IDENTIFY the right primitive (instruction / agent / prompt / skill / MCP tool)
3. CREATE or EDIT the file
4. TEST by asking Copilot something that exercises the feature
5. COMMIT with a descriptive message
6. REPEAT
```

### What to add iteratively

| If you want to... | Add this |
|---|---|
| Copilot to always follow your team's PR rules | Add to `copilot-instructions.md` |
| A `/standup` command that drafts your daily standup | New `prompts/standup.prompt.md` |
| A Python code reviewer persona | New `agents/python-reviewer.agent.md` |
| Python-specific coding standards | New `instructions/python.instructions.md` with `applyTo: **/*.py` |
| Copilot to know about your internal libraries | New `skills/internal-libs/SKILL.md` |
| A new MCP data source (e.g., your internal wiki) | New Java class in `mcp-servers/src/server/` |
| Your own Copilot rules in a completely different project | [Export Guide](export-guide.md) |

### Enhancement checklist per change

- [ ] Tested the change by using the feature in Copilot
- [ ] Updated the relevant README (agents/README.md, prompts/README.md, etc.)
- [ ] Added to [navigation-index.md](navigation-index.md) if it's a new command/agent
- [ ] Added to [slash-commands.md](slash-commands.md) if it's a new prompt
- [ ] **Build passes** — run `cd mcp-servers && .\build.ps1` and verify `BUILD SUCCESS` before committing
- [ ] Committed with a meaningful message (see commit guidelines in [copilot-instructions.md](../copilot-instructions.md))

---

**Navigation:** [← START-HERE](START-HERE.md) · [MCP Setup →](mcp-server-setup.md) · [Export Guide →](export-guide.md) · [Copilot Workflow →](copilot-workflow.md)
