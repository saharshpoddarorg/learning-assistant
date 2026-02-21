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
- [6. Directory-Level Copilot Customization](#6-directory-level-copilot-customization)
- [7. Verify the Export](#7-verify-the-export)
- [8. Keep It In Sync](#8-keeping-the-export-in-sync)

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

```
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

## 6. Directory-Level Copilot Customization

> 🟡 **Amateur / Pro:** VS Code supports `.github/copilot-instructions.md` at **any directory level**.

This means you can layer customizations:

```
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

```
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

```
frontend/
└── .github/
    └── instructions/
        ├── typescript.instructions.md   (applyTo: **/*.ts)
        └── react.instructions.md        (applyTo: **/*.tsx)
```

---

## 7. Verify the Export

After copying to the target project, verify each feature:

**Copilot customization:**
```
In VS Code (target project) → Copilot Chat → Agent mode → type /
→ If custom prompts appear: ✅ prompts exported correctly
→ Ask "write a method" → if it follows your conventions: ✅ instructions working
```

**MCP servers:**
```
Ctrl+Shift+B → "mcp-servers: build" (should say "BUILD SUCCESS")
Ctrl+Shift+P → "Reload Window"
Copilot Chat → Agent mode → click 🔧 tools
→ If "learning-resources" appears: ✅ MCP server working
Test: "Search for resources about Java streams"
```

**Atlassian (if configured):**
```
In Copilot Chat: "List the open Jira issues assigned to me"
→ If it returns real Jira data: ✅ Atlassian server working
→ If it says "I can't access Jira": check atlassian-config.local.properties
```

**Brain workspace:**
```
Ctrl+Shift+P → "Run Task" → "brain: status"
→ If it prints note counts: ✅ brain workspace working
```

---

## 8. Keeping the Export in Sync

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

**Navigation:** [← Phase Guide](phase-guide.md) · [← START HERE](START-HERE.md) · [Copilot Workflow →](copilot-workflow.md) · [MCP Setup →](mcp-server-setup.md)
