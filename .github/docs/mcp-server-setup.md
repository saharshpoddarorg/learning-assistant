# MCP Server Setup Guide — Complete Beginner's Walkthrough

> **Who this is for:** Anyone — you don't need to know what MCP is, how AI works,  
> or have any prior setup experience. Follow the steps in order and you'll be done in ~10 minutes.

---

## Table of Contents

1. [What Is This and Why Do I Care?](#1-what-is-this-and-why-do-i-care)
2. [What Do These Servers Actually Do?](#2-what-do-these-servers-actually-do)
3. [Prerequisites — Install These First](#3-prerequisites--install-these-first)
4. [Setup for THIS Project (learning-assistant)](#4-setup-for-this-project-learning-assistant)
5. [Configure Credentials (Secrets)](#5-configure-credentials-secrets)
6. [Enable Servers in VS Code](#6-enable-servers-in-vs-code)
7. [Verify It Works](#7-verify-it-works)
8. [Using in a DIFFERENT Project](#8-using-in-a-different-project)
9. [Reference: What Files Do What](#9-reference-what-files-do-what)
10. [Troubleshooting](#10-troubleshooting)

---

## 1. What Is This and Why Do I Care?

### Plain-English Explanation

Normally, GitHub Copilot only has access to your open files and what you type.

**MCP** (Model Context Protocol) is a way to **give Copilot access to extra tools and data** — things like:
- Searching your company's Jira tickets
- Looking up documentation online
- Reading or writing files
- Querying your GitHub repositories

Think of it like installing apps on a phone. Each MCP server is one "app" that adds new capabilities to Copilot. You tell VS Code which apps to run (via `.vscode/mcp.json`), and Copilot can then use those tools while chatting with you.

### How It Looks in Practice

Without MCP:
> You: "What's the status of the PROJ-123 Jira ticket?"
> Copilot: "I don't have access to Jira, I can't answer that."

With the Atlassian MCP server enabled:
> You: "What's the status of the PROJ-123 Jira ticket?"
> Copilot: [calls `jira_get_issue` tool] → "PROJ-123 is In Progress, assigned to Alice, due Friday."

---

## 2. What Do These Servers Actually Do?

This repo ships with **two built-in Java MCP servers** and supports **two community servers**:

| Server | What Copilot Can Do With It | Credentials Needed? |
|---|---|---|
| **Learning Resources** | Search ~100+ curated docs/tutorials, scrape web pages, summarize content, export resource lists | None — works out of the box |
| **Atlassian** | Search Jira issues, create/update Confluence pages, list Bitbucket PRs, unified search across all 3 products (27 tools total) | Yes — Atlassian email + API token |
| **GitHub** (community) | Search repos, read files, list issues, manage PRs, search code | Yes — GitHub Personal Access Token |
| **Filesystem** (community) | Read/write files in your project folder | None |

---

## 3. Prerequisites — Install These First

You need two things before any MCP server will work.

### 3a. JDK 21+ (Java)

The Learning Resources and Atlassian servers are written in Java. You need JDK 21 or newer.

**Check if you already have it:**
```powershell
java -version
```
If you see `21` or higher → you're good. Skip to step 3b.

**Install if missing:**
1. Go to **https://adoptium.net/**
2. Download **"Eclipse Temurin 21 (LTS)"** for your OS
3. Run the installer — on Windows, check **"Set JAVA_HOME"** and **"Add to PATH"** during install
4. Open a new terminal and run `java -version` to confirm

> **Windows tip:** After installing, close all PowerShell/terminal windows and open a fresh one.
> The PATH update only applies to new windows.

### 3b. Node.js (for GitHub + Filesystem servers)

Only needed if you want the GitHub or Filesystem community servers.

**Check if you already have it:**
```powershell
node --version
npm --version
```
If both show version numbers → you're good.

**Install if missing:**
1. Go to **https://nodejs.org/**
2. Download the **LTS** version
3. Run the installer (defaults are fine)
4. Open a new terminal and run `node --version` to confirm

---

## 4. Setup for THIS Project (learning-assistant)

### Step 1 — Build the Java Servers

The MCP servers are pre-written but need to be compiled into runnable code first.

**In VS Code:** Press `Ctrl+Shift+B` → select **"mcp-servers: build"**

Or in a terminal:
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

You should see output ending with:
```
BUILD SUCCESS -- compiled 150 files
```

> **If you see javac not found:** Java is not on your PATH. See §3a above and restart your terminal.

The compiled files go into `mcp-servers/out/` — that's what the MCP servers run from.

### Step 2 — Create Your Local Config File

The local config is where you store your secrets (API keys, passwords). It's gitignored — it never gets committed to version control.

```powershell
# Windows PowerShell — run from the repo root
Copy-Item mcp-servers\user-config\mcp-config.local.example.properties `
          mcp-servers\user-config\mcp-config.local.properties
```
```bash
# macOS / Linux
cp mcp-servers/user-config/mcp-config.local.example.properties \
   mcp-servers/user-config/mcp-config.local.properties
```

Open `mcp-servers/user-config/mcp-config.local.properties` in VS Code — it's where you'll paste credentials in the next section.

### Step 3 — The MCP Config File Already Exists

`.vscode/mcp.json` is already in this repo. It tells VS Code which servers to run. You don't need to create anything — just configure credentials and enable the servers you want (see §5 and §6).

---

## 5. Configure Credentials (Secrets)

Each server that needs credentials has instructions below. **Only configure the servers you actually want to use.** Learning Resources needs nothing.

### 5a. Learning Resources Server — No Setup Needed ✅

Works immediately after building. No credentials required.

### 5b. Atlassian Server (Jira + Confluence + Bitbucket)

**Step 1 — Get your Atlassian API token:**
1. Go to **https://id.atlassian.com/manage-profile/security/api-tokens**
2. Click **"Create API token"**
3. Give it a name (e.g., "MCP Server") and click **Create**
4. **Copy the token immediately** — you can't see it again after closing the dialog

**Step 2 — Create the Atlassian local config:**
```powershell
# Windows PowerShell
Copy-Item mcp-servers\user-config\servers\atlassian\atlassian-config.local.example.properties `
          mcp-servers\user-config\servers\atlassian\atlassian-config.local.properties
```
```bash
# macOS / Linux
cp mcp-servers/user-config/servers/atlassian/atlassian-config.local.example.properties \
   mcp-servers/user-config/servers/atlassian/atlassian-config.local.properties
```

**Step 3 — Fill in your credentials:**

Open `mcp-servers/user-config/servers/atlassian/atlassian-config.local.properties` and set these values:

```properties
# Your Atlassian cloud workspace name (the part before .atlassian.net)
atlassian.instance.name=your-workspace-name
atlassian.variant=cloud

# Your Jira and Confluence URLs — replace "your-domain" with your workspace name
atlassian.jira.baseUrl=https://your-domain.atlassian.net
atlassian.confluence.baseUrl=https://your-domain.atlassian.net/wiki

# Only needed if you use Bitbucket Cloud — leave blank otherwise
atlassian.bitbucket.baseUrl=https://api.bitbucket.org

# Your Atlassian account email
atlassian.auth.email=you@yourcompany.com

# The API token you just generated (paste it here)
atlassian.auth.token=PASTE_YOUR_API_TOKEN_HERE

# What products do you have? Set true/false accordingly
atlassian.product.jira.enabled=true
atlassian.product.confluence.enabled=true
atlassian.product.bitbucket.enabled=false
```

> **Where to find your workspace name:**
> Go to any Jira/Confluence page → look at the URL → it will be `https://WORKSPACE.atlassian.net`

> **Data Center / self-hosted Atlassian?** Use a Personal Access Token (PAT) instead — see the detailed scenarios in `atlassian-config.local.example.properties`.

### 5c. GitHub Server

You'll be prompted for your token the first time you use it in VS Code — no config file needed. The prompt is secure (VS Code stores it in your system keychain).

**To generate your GitHub Personal Access Token:**
1. Go to **https://github.com/settings/tokens**
2. Click **"Generate new token"** → **"Generate new token (classic)"**
3. Give it a name (e.g., "VS Code MCP")
4. Select scopes:
   - `repo` — if you need access to private repositories
   - `public_repo` — if you only need public repos (recommended if unsure)
5. Click **"Generate token"** and copy it

When Copilot first uses the GitHub server, VS Code will show a dialog with:
> "GitHub Personal Access Token (ghp_...). Generate at https://github.com/settings/tokens"

Paste your token there.

### 5d. Filesystem Server

No credentials needed. It just needs to be enabled in `mcp.json` (see §6).

---

## 6. Enable Servers in VS Code

Open `.vscode/mcp.json`. For each server you've configured, change:
```json
"disabled": true
```
to:
```json
"disabled": false
```

**Recommended starting configuration:**

| Server | Set `disabled` to | Why |
|---|---|---|
| `learning-resources` | `false` | No credentials needed — enable it |
| `atlassian` | `false` (after step 5b) | Enable after filling in credentials |
| `github` | `false` (after step 5c token ready) | Enable when you want GitHub access |
| `filesystem` | `false` (optional) | Enable if you want Copilot to read local files |

**After changing `mcp.json`:**

Reload the VS Code window: `Ctrl+Shift+P` → type **"Reload Window"** → press Enter.

---

## 7. Verify It Works

### See Which Servers Are Running

1. Open **GitHub Copilot Chat** (`Ctrl+Alt+I` or click the Copilot icon)
2. Make sure you're in **Agent mode** — click the mode dropdown and select **"Agent"** (not "Ask" or "Edit")
3. Click the **tools icon** (🔧) near the chat input box
4. You should see the MCP servers listed with their available tools

If servers appear with tools listed — you're done! Try asking:
> "Show me what resources you have about Java collections"
> (uses the Learning Resources server)

> "What Jira issues are assigned to me?"
> (uses the Atlassian server — requires §5b)

### Quick Smoke Test

```
# In Copilot Chat (agent mode):
"Use the learning resources tool to find something about Java streams"
```

If Copilot responds with actual resources (not "I can't access that") — ✅ working.

### Check Server Logs

If a server doesn't appear, open the VS Code **Output** panel:
- `View → Output` → select **"GitHub Copilot"** or **"MCP Server: learning-resources"** from the dropdown
- Error messages will tell you exactly what went wrong

---

## 8. Using in a DIFFERENT Project

### What to Copy

To use these MCP servers in another project (e.g., `my-other-project`):

```
Things to copy:
  mcp-servers/              ← the entire folder (Java sources + config + build scripts)
  mcp-servers/.vscode/      ← included automatically in the above

Things to create in your target project:
  your-project/.vscode/mcp.json    ← the VS Code server registry
```

### Step-by-Step

**Step 1 — Copy the mcp-servers folder into your target project:**
```powershell
# Windows PowerShell
$target = "C:\path\to\your-project"
Copy-Item -Recurse mcp-servers "$target\mcp-servers"
```
```bash
# macOS / Linux
cp -r mcp-servers /path/to/your-project/mcp-servers
```

**Step 2 — Copy the mcp.json example to your target project's .vscode:**
```powershell
# Windows PowerShell (run from repo root)
New-Item -ItemType Directory -Force "$target\.vscode"
Copy-Item mcp-servers\.vscode\mcp.json.example "$target\.vscode\mcp.json"
```
```bash
mkdir -p /path/to/your-project/.vscode
cp mcp-servers/.vscode/mcp.json.example /path/to/your-project/.vscode/mcp.json
```

**Step 3 — Adjust paths in the copied `mcp.json`:**

Open `your-project/.vscode/mcp.json`. The `cwd` and classpath values use `${workspaceFolder}/mcp-servers` by default — this assumes `mcp-servers/` is directly inside your project root. If that's where you put it, no changes needed.

If you put it somewhere else (e.g., `tools/mcp-servers`), update both `cwd` and the `-cp` argument:
```json
"cwd": "${workspaceFolder}/tools/mcp-servers",
"args": ["-cp", "out", "server.learningresources.LearningResourcesServer"]
```

**Step 4 — Build and configure credentials (same as §4–5):**
```powershell
cd $target\mcp-servers
.\build.ps1
```

Then follow §5 to configure whichever credentials you need.

**Step 5 — Add to your target project's `.gitignore`:**
```gitignore
# MCP server secrets
mcp-servers/user-config/mcp-config.local.properties
mcp-servers/user-config/servers/atlassian/atlassian-config.local.properties
mcp-servers/build.env.local
mcp-servers/out/
```

**Step 6 — Reload VS Code** in the target project and verify as per §7.

---

## 9. Reference: What Files Do What

### In This Repo

| File / Folder | Committed? | Purpose |
|---|:---:|---|
| `.vscode/mcp.json` | ✅ Yes | VS Code MCP server registry — which servers to run |
| `.vscode/tasks.json` | ✅ Yes | VS Code build tasks (including "mcp-servers: build") |
| `mcp-servers/build.ps1` | ✅ Yes | Build script for Windows (auto-detects JDK) |
| `mcp-servers/build.sh` | ✅ Yes | Build script for macOS/Linux |
| `mcp-servers/out/` | ❌ No | Compiled Java classes — never committed (gitignored) |
| `mcp-servers/src/` | ✅ Yes | Java source code for both MCP servers |
| `mcp-servers/user-config/mcp-config.properties` | ✅ Yes | Base config: safe defaults, no secrets |
| `mcp-servers/user-config/mcp-config.local.properties` | ❌ **No** | YOUR secrets — never committed (gitignored) |
| `mcp-servers/user-config/mcp-config.local.example.properties` | ✅ Yes | Template showing what to put in local config |
| `mcp-servers/user-config/servers/atlassian/atlassian-config.properties` | ✅ Yes | Atlassian defaults — no credentials |
| `mcp-servers/user-config/servers/atlassian/atlassian-config.local.properties` | ❌ **No** | YOUR Atlassian credentials — never committed |
| `mcp-servers/user-config/servers/atlassian/atlassian-config.local.example.properties` | ✅ Yes | Template for Atlassian local config |
| `mcp-servers/.vscode/mcp.json.example` | ✅ Yes | Portable MCP config template for other projects |
| `mcp-servers/build.env.local` | ❌ **No** | Machine-specific JDK path override (optional) |
| `mcp-servers/build.env.example` | ✅ Yes | Template for build.env.local |

### Config Precedence (highest wins first)

```
Environment variables (MCP_*, ATLASSIAN_*)
    ↓
.local.properties files (gitignored, your secrets)
    ↓
.properties files (committed, shared defaults)
    ↓
Built-in defaults
```

### Quick Credential Cheat Sheet

| What you're configuring | File to edit | Key(s) to set |
|---|---|---|
| GitHub token | `mcp-config.local.properties` | `apiKeys.github` + `server.github.env.GITHUB_TOKEN` |
| Atlassian email | `atlassian-config.local.properties` | `atlassian.auth.email` |
| Atlassian API token | `atlassian-config.local.properties` | `atlassian.auth.token` |
| Jira URL | `atlassian-config.local.properties` | `atlassian.jira.baseUrl` |
| Confluence URL | `atlassian-config.local.properties` | `atlassian.confluence.baseUrl` |
| JDK path (if java not on PATH) | `build.env.local` | `JAVA_HOME` |

---

## 10. Troubleshooting

### "java is not recognized" when building

**Problem:** The build script can't find `java` or `javac`.

**Fix:**
1. Install JDK 21+ (see §3a)
2. If installed but still not found, create `mcp-servers/build.env.local` and set `JAVA_HOME`:
   ```
   # Copy from build.env.example, then edit:
   JAVA_HOME=C:\path\to\your\jdk-21
   ```
3. Rerun the build

### MCP server doesn't appear in Copilot tools panel

**Checklist:**
- Did you build first? Run `mcp-servers: build` task
- Is `disabled: false` set for that server in `.vscode/mcp.json`?
- Did you reload the VS Code window after changing `mcp.json`?
- Are you in **Agent mode** in Copilot Chat (not Ask or Edit mode)?
- Check `View → Output → GitHub Copilot` for error messages

### "Atlassian config incomplete" in Copilot output

**Problem:** The Atlassian server started but can't find credentials.

**Fix:** Make sure `atlassian-config.local.properties` exists and has all four values set:
- `atlassian.auth.email`
- `atlassian.auth.token`
- `atlassian.jira.baseUrl`
- `atlassian.confluence.baseUrl` (if using Confluence)

### "Unauthorized" errors when using Atlassian tools

**Possible causes:**
- API token is wrong or expired → generate a new one at https://id.atlassian.com/manage-profile/security/api-tokens
- Email/token mismatch → make sure the email matches the account that owns the token
- Wrong URL → check `atlassian.jira.baseUrl` is exactly `https://YOUR-DOMAIN.atlassian.net` (no trailing slash)
- Using Data Center with Cloud config → check `atlassian.variant` matches your deployment

### GitHub server: "Bad credentials" error

**Fix:** Your GitHub token is wrong or expired.
1. Generate a new token at https://github.com/settings/tokens
2. In VS Code: `Ctrl+Shift+P` → **"MCP: Reset Stored Secrets"** (or clear the cached secret)
3. Reload the window — VS Code will prompt for the new token

### npx: "command not found" (GitHub / Filesystem servers)

**Problem:** Node.js is not installed or not on PATH.

**Fix:** Install Node.js (see §3b) and open a fresh terminal window.

### MCP server crashes immediately on start

1. Check the VS Code Output panel for the exact error
2. Try running the server manually in a terminal to see the full error:
   ```powershell
   # From mcp-servers/
   java -cp out server.learningresources.LearningResourcesServer --list-tools
   java -cp out server.atlassian.AtlassianServer --list-tools
   ```
3. If `out/` is missing — rebuild: `.\build.ps1`
4. If class not found — use `.\build.ps1 -Clean` to force a full recompile

### "node_modules" or "npx" cache issues

```powershell
# Clear npx cache
npx clear-npx-cache
```

---

> **Still stuck?** Open an issue or ask Copilot:
> "Using the mcp-development skill, help me debug why my MCP server isn't starting"
