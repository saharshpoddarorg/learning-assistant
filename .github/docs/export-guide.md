# 📦 Export Guide — Use This Repo in Another Project

> **Goal:** Copy any combination of features from `learning-assistant` to your real project.
> **Read level:** 🟡 Amateur+ (assumes you know basic git and terminal usage)
> **Prerequisite:** Complete [Phase 3 (MCP servers)](phase-guide.md#phase-3-mcp-servers--build--connect) in this repo first, so you know what you're exporting.

---

## 📑 Table of Contents

- [What You Can Export (Overview)](#what-you-can-export-overview)
- [1. Copilot Customization (.github/)](#1-copilot-customization-github)
  - [1a. Minimal — Just Rules](#1a-minimal--just-the-rules)
  - [1b. Full — All 5 Primitives](#1b-full--all-5-primitives)
  - [1c. Selective — Cherry-pick](#1c-selective--cherry-pick)
- [2. MCP Servers](#2-mcp-servers)
  - [2a. Learning Resources only](#2a-learning-resources-server-only)
  - [2b. Full MCP module](#2b-full-mcp-module)
- [3. Brain Workspace](#3-brain-workspace)
- [4. Everything (Full Export)](#4-everything-full-export)
- [5. Gitignore Entries](#5-gitignore-entries-required-in-target-project)
- [6. Work vs Personal Repo — What's Different](#6-work-vs-personal-repo--whats-different)
- [7. What You Can Safely Skip](#7-what-you-can-safely-skip)
- [8. Config Files & Environment Variables Reference](#8-config-files--environment-variables-reference)
- [9. API Keys & Credentials Reference](#9-api-keys--credentials-reference)
- [10. Directory-Level Copilot Customization](#10-directory-level-copilot-customization)
- [11. Verify the Export](#11-verify-the-export)
- [12. Keep It In Sync](#12-keeping-the-export-in-sync)

> 🟢 **Brand new?** Start with the [Newbie Step-by-Step Export Guide](export-newbie-guide.md)
> instead — it's a simplified 10-minute walkthrough.

---

## What You Can Export (Overview)

| Feature | Min Copy | Benefit | Credential Needed |
|---|---|---|---|
| **Copilot rules** | 1 file | Copilot follows your coding standards | None |
| **Full Copilot customization** | `.github/` folder | Agents + slash commands + skills | None |
| **MCP: Learning Resources** | `mcp-servers/` + `.vscode/mcp.json` | Copilot searches learning docs | None |
| **MCP: Atlassian** | Above + `atlassian-config.local.properties` | Copilot reads Jira/Confluence/Bitbucket | Atlassian API token |
| **MCP: GitHub** | `.vscode/mcp.json` with github entry | Copilot searches your GitHub repos | GitHub PAT |
| **Brain workspace** | `brain/` folder + tasks | Personal knowledge notes | None |

---

## 1. Copilot Customization (`.github/`)

### 1a. Minimal — Just the Rules

Copies only the always-on project rules (`copilot-instructions.md`).
Zero setup, zero credentials, works immediately.

```powershell
# Windows PowerShell — run from this repo's root
$target = "C:\path\to\your-project"

# Copy the main instructions file
Copy-Item .github\copilot-instructions.md "$target\.github\copilot-instructions.md"
```

```bash
# macOS / Linux
TARGET="/path/to/your-project"
mkdir -p "$TARGET/.github"
cp .github/copilot-instructions.md "$TARGET/.github/copilot-instructions.md"
```

**After copying:** Edit `$target/.github/copilot-instructions.md` to reflect YOUR project's rules.
The current file has Java/MCP/learning-assistant specific conventions — update them to match your stack.

> 🟢 **Newbie tip:** Open the file and just read it. Remove any rules that don't apply (e.g., Java rules if your project is Python). Add rules specific to your project.

---

### 1b. Full — All 5 Primitives

Copies the entire `.github/` folder: rules + agents + prompts (slash commands) + skills + docs.

```powershell
# Windows PowerShell
$target = "C:\path\to\your-project"

# Copy entire .github/ (except .git internal files)
Copy-Item -Recurse .github "$target\.github"
```

```bash
# macOS / Linux
TARGET="/path/to/your-project"
cp -r .github "$TARGET/.github"
```

**After copying:**

1. Edit `$target/.github/copilot-instructions.md` — update for your project's language/conventions
2. Review `$target/.github/instructions/` — remove language files that don't apply (e.g., remove `java.instructions.md` if your project is TypeScript)
3. Optionally trim `$target/.github/prompts/` — remove slash commands you won't use
4. Test: Open target project in VS Code → Copilot Chat → type `/` → see commands

**VS Code gitignore note:** If your target project has `.gitignore`, add:

```gitignore
# Don't accidentally commit these docs as code docs
.github/docs/
```

Or keep them — they're developer documentation and safe to commit.

---

### 1c. Selective — Cherry-pick

Copy only the pieces you want:

```powershell
$target = "C:\path\to\your-project"
New-Item -ItemType Directory -Force "$target\.github\agents"
New-Item -ItemType Directory -Force "$target\.github\prompts"
New-Item -ItemType Directory -Force "$target\.github\instructions"

# Pick specific agents
Copy-Item .github\agents\debugger.agent.md   "$target\.github\agents\"
Copy-Item .github\agents\designer.agent.md   "$target\.github\agents\"
Copy-Item .github\agents\code-reviewer.agent.md "$target\.github\agents\"

# Pick specific prompts (slash commands)
Copy-Item .github\prompts\debug.prompt.md       "$target\.github\prompts\"
Copy-Item .github\prompts\refactor.prompt.md    "$target\.github\prompts\"
Copy-Item .github\prompts\multi-session.prompt.md "$target\.github\prompts\"
Copy-Item .github\prompts\brain-new.prompt.md   "$target\.github\prompts\"

# Pick specific skills
Copy-Item -Recurse .github\skills\java-build    "$target\.github\skills\"
Copy-Item -Recurse .github\skills\mcp-development "$target\.github\skills\"

# Always copy the base instructions
Copy-Item .github\copilot-instructions.md "$target\.github\"
```

---

### Directory-Level Copilot Customization

> 🟡 **Feature:** You can customize Copilot at **multiple levels** in the same project.

| Level | File | Effect |
|---|---|---|
| Repo-wide | `.github/copilot-instructions.md` | Active in all files in the repo |
| Folder-specific | `src/backend/.github/copilot-instructions.md` | Active only when editing files in `src/backend/` |
| Per file type | `instructions/java.instructions.md` with `applyTo: **/*.java` | Active only for Java files |

**Example use case:** Monorepo with frontend + backend:

```text
my-monorepo/
├── .github/
│   └── copilot-instructions.md      ← Global: "This repo uses TypeScript + Java"
├── frontend/
│   └── .github/
│       └── copilot-instructions.md  ← Frontend: "Use React 18, avoid class components"
└── backend/
    └── .github/
        └── copilot-instructions.md  ← Backend: Java 21, Spring Boot, use Records
```

```powershell
# Set up frontend-specific instructions
New-Item -ItemType Directory -Force "$target\frontend\.github"
New-Item -ItemType File "$target\frontend\.github\copilot-instructions.md"
# Then edit it with frontend-specific rules
```

---

## 2. MCP Servers

### 2a. Learning Resources Server Only

Minimum copy to get the Learning Resources MCP server working in another project:

```powershell
$target = "C:\path\to\your-project"

# Copy the entire mcp-servers module
Copy-Item -Recurse mcp-servers "$target\mcp-servers"

# Copy the VS Code MCP config (use the template version)
New-Item -ItemType Directory -Force "$target\.vscode"
Copy-Item mcp-servers\.vscode\mcp.json.example "$target\.vscode\mcp.json"
```

```bash
TARGET="/path/to/your-project"
cp -r mcp-servers "$TARGET/mcp-servers"
mkdir -p "$TARGET/.vscode"
cp mcp-servers/.vscode/mcp.json.example "$TARGET/.vscode/mcp.json"
```

**After copying:**

1. The `mcp.json.example` is pre-configured for Learning Resources. No changes needed to enable it.

2. Build the servers in the target project:

   ```powershell
   cd $target\mcp-servers
   .\build.ps1
   ```

3. If `java` is not on PATH, create `build.env.local`:

   ```powershell
   Copy-Item mcp-servers\build.env.example "$target\mcp-servers\build.env.local"
   # Edit build.env.local: set JAVA_HOME=C:\path\to\your\jdk-21
   ```

4. Reload VS Code in the target project → test in Copilot.

**Adjust paths if mcp-servers/ is not at project root:**

If you placed mcp-servers in a subfolder (e.g., `tools/mcp-servers`), update `.vscode/mcp.json`:

```json
"cwd": "${workspaceFolder}/tools/mcp-servers",
"args": ["-cp", "out", "server.learningresources.LearningResourcesServer"]
```

---

### 2b. Full MCP Module

```powershell
$target = "C:\path\to\your-project"

# Copy entire mcp-servers module
Copy-Item -Recurse mcp-servers "$target\mcp-servers"

# Copy MCP registry (starts with Learning Resources; Atlassian/GitHub disabled)
New-Item -ItemType Directory -Force "$target\.vscode"
Copy-Item mcp-servers\.vscode\mcp.json.example "$target\.vscode\mcp.json"

# Copy VS Code tasks (for the build task)
if (-not (Test-Path "$target\.vscode\tasks.json")) {
    Copy-Item .vscode\tasks.json "$target\.vscode\tasks.json"
} else {
    Write-Host "tasks.json already exists — manually merge the mcp-servers: build task"
    Write-Host "See: .vscode/tasks.json in learning-assistant for the task definition"
}
```

**After copying — for Atlassian server:**

```powershell
cd $target\mcp-servers
# Create local Atlassian config from template
Copy-Item user-config\servers\atlassian\atlassian-config.local.example.properties `
          user-config\servers\atlassian\atlassian-config.local.properties
# Edit the file: set email, token, jira/confluence URLs
# Then in .vscode/mcp.json: set "disabled": false for "atlassian"
```

---

## 3. Brain Workspace

```powershell
$target = "C:\path\to\your-project"
Copy-Item -Recurse brain "$target\brain"
```

**The brain workspace is self-contained** — it has its own scripts, README, and VS Code tasks.
The only dependency is PowerShell (Windows) or Bash (macOS/Linux).

**Add brain tasks to your target `.vscode/tasks.json`:** Copy the brain task entries from `learning-assistant/.vscode/tasks.json` (the brain-related tasks block) into your target project's tasks.json.

**Copilot prompts for brain (optional):** Copy the brain prompt files:

```powershell
$target = "C:\path\to\your-project"
New-Item -ItemType Directory -Force "$target\.github\prompts"
Copy-Item .github\prompts\brain-new.prompt.md    "$target\.github\prompts\"
Copy-Item .github\prompts\brain-publish.prompt.md "$target\.github\prompts\"
Copy-Item .github\prompts\brain-search.prompt.md  "$target\.github\prompts\"
```

---

## 4. Everything (Full Export)

```powershell
# Full export — all features
$source = "E:\mgcnoscan\learning\learning-assistant"
$target  = "C:\path\to\your-project"

# 1. Copilot customization
Copy-Item -Recurse "$source\.github" "$target\.github"

# 2. MCP servers
Copy-Item -Recurse "$source\mcp-servers" "$target\mcp-servers"

# 3. VS Code registry + tasks
New-Item -ItemType Directory -Force "$target\.vscode"
Copy-Item "$source\.vscode\mcp.json"   "$target\.vscode\mcp.json"
Copy-Item "$source\.vscode\tasks.json" "$target\.vscode\tasks.json"

# 4. Brain workspace
Copy-Item -Recurse "$source\brain" "$target\brain"

# 5. Build
Set-Location "$target\mcp-servers"
.\build.ps1

Write-Host "`nExport complete! Next steps:"
Write-Host "  1. Edit $target\.github\copilot-instructions.md for your project"
Write-Host "  2. Add credentials to $target\mcp-servers\user-config\*.local.properties"
Write-Host "  3. Edit $target\.vscode\mcp.json: set disabled=false for servers you want"
Write-Host "  4. Open $target in VS Code and reload window"
```

---

## 5. Gitignore Entries Required in Target Project

Add these to your target project's `.gitignore`:

```gitignore
# ── MCP Servers ─────────────────────────────────────────────────────────────
# Compiled Java classes (rebuilt by build task)
mcp-servers/out/
*.class

# Local secrets — NEVER commit these
mcp-servers/user-config/mcp-config.local.properties
mcp-servers/user-config/servers/atlassian/atlassian-config.local.properties
mcp-servers/build.env.local

# ── VS Code (selective) ──────────────────────────────────────────────────────
# Ignore personal VS Code settings (window size, editor prefs, etc.)
.vscode/*
# Keep project-level files
!.vscode/mcp.json
!.vscode/tasks.json
!.vscode/extensions.json
# Keep mcp-servers/ VS Code config (portable)
!mcp-servers/.vscode/

# ── Brain workspace ──────────────────────────────────────────────────────────
# (No gitignore needed — brain notes are meant to be committed)
# Optional: keep brain private:
# brain/
```

---

## 6. Work vs Personal Repo — What's Different

The same export works for both work and personal repos. The difference is which **MCP
servers** you enable and which **skills/prompts** you keep.

### Work Repository

| What | Action | Why |
|---|---|---|
| `.github/copilot-instructions.md` | Edit for your project's language, conventions, team rules | This becomes your team's coding standard |
| `.github/instructions/java.instructions.md` | Keep if Java; replace with your language | Language-specific rules |
| `.github/instructions/change-completeness.instructions.md` | Remove (or adapt for your team's workflow) | This is learning-assistant specific |
| `.github/instructions/chat-capture.instructions.md` | Remove unless you want session capture | Personal productivity feature |
| `.github/instructions/steering-modes.instructions.md` | Remove unless you use steering modes | Learning-assistant workflow |
| `.github/agents/` | Keep agents relevant to your work (debugger, designer, code-reviewer) | Universal value |
| `.github/skills/brain-management/` | Remove | Personal knowledge management — not for work repo |
| `.github/skills/mac-dev/` | Remove unless team uses macOS | OS-specific |
| `.github/skills/career-resources/` | Remove | Personal career guidance — not for work repo |
| `.github/skills/daily-assistant-resources/` | Remove | Personal productivity — not for work repo |
| MCP: Atlassian | **Enable** with your work Jira/Confluence credentials | Read work tickets and docs |
| MCP: GitHub | **Enable** with a PAT that has access to your org repos | Search work repos |
| MCP: Learning Resources | Keep (no credentials needed) | Learning resources are useful everywhere |
| MCP: Filesystem | Enable if you want Copilot to search local files directly | Optional |
| `brain/` | Do NOT copy to work repo | Personal workspace — keep separate |

### Personal Software Development Repository

| What | Action | Why |
|---|---|---|
| `.github/copilot-instructions.md` | Edit for your personal project's stack | Your personal coding rules |
| `.github/instructions/` | Keep relevant language files, remove others | Only keep what applies |
| `.github/instructions/change-completeness.instructions.md` | Optional — keep if you like the completeness workflow | Ensures thorough changes |
| `.github/agents/` | Keep all — they're useful for any project | Debugger, designer, reviewer |
| `.github/skills/` | Keep all you find useful — career, daily assistant, SE resources | Personal enrichment |
| MCP: Atlassian | **Skip** — no work Jira to connect to | Not needed for personal projects |
| MCP: GitHub | Optional — enable if you want Copilot to search your GitHub repos | Nice for OSS contributors |
| MCP: Learning Resources | **Keep** — great for learning | No credentials needed |
| `brain/` | Copy if you want a personal knowledge base | PKM system |

---

## 7. What You Can Safely Skip

Not every piece of learning-assistant is needed in every project. Here's what you can
safely omit **without breaking anything**:

### Always Safe to Skip

| Item | Path | Why it's safe to skip |
|---|---|---|
| Developer docs | `.github/docs/` | Documentation about learning-assistant itself — not your project |
| Change completeness checklist | `.github/instructions/change-completeness.instructions.md` | Specific to this repo's workflow |
| Steering modes | `.github/instructions/steering-modes.instructions.md` | Specific to this repo's workflow |
| Chat capture | `.github/instructions/chat-capture.instructions.md` | Session capture — personal productivity |
| Markdown formatting rules | `.github/instructions/md-formatting.instructions.md` | Unless your project enforces md lint |
| Brain workspace | `brain/` | Personal knowledge management module |
| macOS module | `mac-os/` | Setup guides for mac development |
| Search engine module | `search-engine/` | Learning module — not production code |
| Entry points | `src/Main.java` | This repo's Java entry point |
| `.github/prompts/hub.prompt.md` | `.github/prompts/hub.prompt.md` | Hub for this repo's slash commands |

### Skip Based on Your Stack

| If your project... | Skip these |
|---|---|
| Is NOT Java | `java.instructions.md`, `clean-code.instructions.md`, `java-build/SKILL.md`, `java-learning-resources/SKILL.md` |
| Does NOT use MCP servers | `mcp-servers/`, `.vscode/mcp.json`, `mcp-development/SKILL.md` |
| Does NOT use Jira/Confluence | `mcp-servers/user-config/servers/atlassian/`, Atlassian config in `.vscode/mcp.json` |
| Does NOT need GitHub search in Copilot | GitHub server entry in `.vscode/mcp.json` |
| Does NOT use macOS | `mac-dev/SKILL.md` |
| Does NOT want brain/PKM | `brain-management/SKILL.md`, `brain-*.prompt.md`, `digital-notetaking/SKILL.md` |

### Never Skip

| Item | Why it must stay |
|---|---|
| `.github/copilot-instructions.md` | Core project instructions — Copilot needs this |
| At least one agent (`.github/agents/`) | Without agents, no agent dropdown options |
| At least one prompt (`.github/prompts/`) | Without prompts, no `/` slash commands |

---

## 8. Config Files & Environment Variables Reference

Every configuration file and environment variable in this project, and whether you need it:

### Config Files

| File | Purpose | Required? | Contains Secrets? |
|---|---|---|---|
| `.github/copilot-instructions.md` | Project-wide Copilot rules | **Yes** | No |
| `.github/instructions/*.instructions.md` | File-type-specific Copilot rules | Recommended | No |
| `.vscode/mcp.json` | MCP server registry (which servers to run) | Only if using MCP | No (tokens via inputs) |
| `.vscode/tasks.json` | VS Code build/run tasks | Recommended | No |
| `mcp-servers/build.env.local` | Java build environment overrides | Only if `java` not on PATH | No |
| `mcp-servers/build.env.example` | Template for `build.env.local` | Reference only — don't edit | No |
| `mcp-servers/user-config/mcp-config.properties` | Shared (committed) MCP config | Committed — safe | No |
| `mcp-servers/user-config/mcp-config.local.properties` | Personal MCP config overrides | Only if customizing | **Yes — gitignored** |
| `mcp-servers/user-config/servers/atlassian/atlassian-config.local.properties` | Atlassian credentials | Only if using Atlassian MCP | **Yes — gitignored** |
| `mcp-servers/user-config/servers/atlassian/atlassian-config.local.example.properties` | Template for Atlassian config | Reference only — don't edit | No |

### Environment Variables

| Variable | Set Where | Purpose | Required? |
|---|---|---|---|
| `JAVA_HOME` | `build.env.local` or system PATH | JDK location for MCP server build | Only if `java` not on PATH |
| `ATLASSIAN_CONFIG_DIR` | `.vscode/mcp.json` (auto-set) | Path to Atlassian config directory | Auto — don't set manually |
| `GITHUB_TOKEN` | VS Code input prompt | GitHub Personal Access Token | Only if using GitHub MCP server |

### How `.vscode/mcp.json` Manages Servers

The MCP config uses VS Code's input variables for secrets — no env files needed:

```json
{
  "inputs": [
    {
      "id": "github-token",
      "type": "promptString",
      "description": "GitHub Personal Access Token (PAT)",
      "password": true
    }
  ],
  "servers": {
    "learning-resources": {
      // Always enabled — no credentials needed
      "type": "stdio",
      "command": "java",
      "args": ["-cp", "out", "server.learningresources.LearningResourcesServer"],
      "cwd": "${workspaceFolder}/mcp-servers"
    },
    "atlassian": {
      "disabled": true,   // <-- Change to false when ready
      "type": "stdio",
      "command": "java",
      "args": ["-cp", "out", "server.atlassian.AtlassianMcpServer"],
      "cwd": "${workspaceFolder}/mcp-servers",
      "env": {
        "ATLASSIAN_CONFIG_DIR": "${workspaceFolder}/mcp-servers/user-config/servers/atlassian"
      }
    },
    "github": {
      "disabled": true,   // <-- Change to false when ready
      "type": "stdio",
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_TOKEN": "${input:github-token}"
      }
    }
  }
}
```

### Creating Config Files from Templates

```powershell
# MCP build config (if java not on PATH)
Copy-Item mcp-servers\build.env.example mcp-servers\build.env.local
# Edit: set JAVA_HOME=C:\path\to\jdk-21

# Atlassian config (if using Jira/Confluence)
Copy-Item mcp-servers\user-config\servers\atlassian\atlassian-config.local.example.properties `
          mcp-servers\user-config\servers\atlassian\atlassian-config.local.properties
# Edit: set email, api.token, base URLs
```

---

## 9. API Keys & Credentials Reference

Which servers need credentials, where to get them, and what to set:

### Quick Decision Table

| Server | Credential Type | Where to Get It | Where to Put It | Can I Skip It? |
|---|---|---|---|---|
| Learning Resources | None | — | — | No credential needed — always works |
| Atlassian (Jira/Confluence) | API token | [Atlassian API tokens](https://id.atlassian.com/manage-profile/security/api-tokens) | `atlassian-config.local.properties` | **Yes** — skip if not using Jira/Confluence |
| GitHub | Personal Access Token (PAT) | [GitHub token settings](https://github.com/settings/tokens) | VS Code prompts at runtime | **Yes** — skip if not using GitHub search |
| Filesystem | None | — | — | No credential needed |

### Atlassian Credentials — Step by Step

1. Go to [id.atlassian.com/manage-profile/security/api-tokens](https://id.atlassian.com/manage-profile/security/api-tokens)
2. Click **Create API token**
3. Name it "VS Code Copilot" (or anything memorable)
4. Copy the token (you won't see it again)
5. Create the config file:

   ```powershell
   cd mcp-servers\user-config\servers\atlassian
   Copy-Item atlassian-config.local.example.properties atlassian-config.local.properties
   ```

6. Edit `atlassian-config.local.properties`:

   ```properties
   atlassian.email=your.name@company.com
   atlassian.api.token=ATATT3xFfGF0... (paste your token here)
   atlassian.jira.base.url=https://your-company.atlassian.net
   atlassian.confluence.base.url=https://your-company.atlassian.net/wiki
   atlassian.bitbucket.workspace=your-workspace
   ```

7. In `.vscode/mcp.json`, change atlassian's `"disabled": true` to `"disabled": false`
8. Reload VS Code window

### GitHub PAT — Step by Step

1. Go to [github.com/settings/tokens](https://github.com/settings/tokens)
2. Click **Generate new token** → choose **Fine-grained token** (recommended)
3. Select your repository access scope:
   - **Public repos only:** select `public_repo`
   - **All your repos:** select `repo`
4. In `.vscode/mcp.json`, change github's `"disabled": true` to `"disabled": false`
5. Reload VS Code window — it will **prompt** you for the token at runtime
6. Paste your PAT when prompted (VS Code stores it securely)

### Security Rules

- **Never commit** `.local.properties` files — they are gitignored by default
- **Never commit** API tokens in `.vscode/mcp.json` — use VS Code input variables instead
- **Rotate tokens** periodically — Atlassian tokens don't expire automatically; GitHub tokens
  can be set to expire
- **Minimal scope** — give tokens the minimum permissions needed (read-only where possible)

---

## 10. Directory-Level Copilot Customization

> 🟡 **Amateur / Pro:** VS Code supports `.github/copilot-instructions.md` at **any directory level**.
> Also see [parent repository discovery](#parent-repo-discovery) — VS Code can load customizations from parent directories.

This means you can layer customizations:

```text
project-root/
├── .github/copilot-instructions.md        ← Always active everywhere
├── src/
│   └── .github/copilot-instructions.md    ← Active only in src/
├── frontend/
│   └── .github/copilot-instructions.md    ← Active only in frontend/
└── backend/
    └── .github/copilot-instructions.md    ← Active only in backend/
```

**Use case: Monorepo with multiple teams**

```text
monorepo/
├── .github/copilot-instructions.md
│   # "This monorepo uses a microservices architecture"
│   # "All services share the error handling contract in shared/errors"
│
├── services/auth/
│   └── .github/copilot-instructions.md
│   # "This service uses Spring Security. Never bypass authentication filters."
│   # "JWT tokens expire after 1 hour. Always validate expiry."
│
└── services/payment/
    └── .github/copilot-instructions.md
    # "PCI-DSS compliance required. Never log raw card numbers."
    # "Use the PaymentGateway interface — never call Stripe directly."
```

**`*.instructions.md` files work per file type at any directory level too:**

```text
frontend/
└── .github/
    └── instructions/
        ├── typescript.instructions.md   (applyTo: **/*.ts)
        └── react.instructions.md        (applyTo: **/*.tsx)
```

---

## 11. Verify the Export

After copying to the target project, verify each feature:

**Copilot customization:**

```text
In VS Code (target project) → Copilot Chat → Agent mode → type /
→ If custom prompts appear: ✅ prompts exported correctly
→ Ask "write a method" → if it follows your conventions: ✅ instructions working
```

**MCP servers:**

```text
Ctrl+Shift+B → "mcp-servers: build" (should say "BUILD SUCCESS")
Ctrl+Shift+P → "Reload Window"
Copilot Chat → Agent mode → click 🔧 tools
→ If "learning-resources" appears: ✅ MCP server working
Test: "Search for resources about Java streams"
```

**Atlassian (if configured):**

```text
In Copilot Chat: "List the open Jira issues assigned to me"
→ If it returns real Jira data: ✅ Atlassian server working
→ If it says "I can't access Jira": check atlassian-config.local.properties
```

**Brain workspace:**

```text
Ctrl+Shift+P → "Run Task" → "brain: status"
→ If it prints note counts: ✅ brain workspace working
```

---

## 12. Keeping the Export in Sync

When `learning-assistant` receives updates (new agents, prompts, skills, MCP tools) you can pull them into your target project:

**Option A: Selective manual sync**

```powershell
# After pulling latest learning-assistant, copy specific new files:
$src = "E:\mgcnoscan\learning\learning-assistant"
$dst = "C:\my-project"

# Example: pull a new agent that was added
Copy-Item "$src\.github\agents\new-agent.agent.md" "$dst\.github\agents\"

# Example: pull an updated skill
Copy-Item -Recurse "$src\.github\skills\mcp-development" "$dst\.github\skills\"
```

**Option B: Git subtree / submodule (Pro)**

If you version-control your target project with git, use a git subtree to keep `.github/` in sync:

```bash
# Add learning-assistant as a subtree (one-time)
git subtree add --prefix=.github \
  https://github.com/saharshpoddarorg/learning-assistant.git \
  saharsh1 --squash

# Pull updates later
git subtree pull --prefix=.github \
  https://github.com/saharshpoddarorg/learning-assistant.git \
  saharsh1 --squash
```

> ⚠️ Subtree pulls override local changes in `.github/`. Use this only if you don't customize the copied files.

---

**Navigation:** [← Phase Guide](phase-guide.md) · [← START HERE](START-HERE.md) · [Newbie Export Guide →](export-newbie-guide.md) · [Copilot Workflow →](copilot-workflow.md) · [MCP Setup →](mcp-server-setup.md)
