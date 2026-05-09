# Export to Your Real Project — Newbie Step-by-Step

> **Goal:** Copy Copilot customization from `learning-assistant` to your work or personal repo.
> **Time:** 10–20 minutes.
> **Prerequisites:** VS Code with GitHub Copilot installed. JDK 21+ (only if copying MCP servers).

---

## Step 0: Decide What You Need

| I want... | What to copy | Time | API keys needed? |
|---|---|---|---|
| Copilot to follow my coding rules | `.github/copilot-instructions.md` only | 2 min | No |
| Rules + slash commands + agents + skills | Entire `.github/` folder | 5 min | No |
| Copilot to search learning resources | `.github/` + `mcp-servers/` + `.vscode/mcp.json` | 15 min | No |
| Copilot to read Jira/Confluence (Server/DC) | `.github/` folder + `.env` file in skill | 10 min | Yes (Atlassian PAT) |
| Copilot to read Jira/Confluence (Cloud) | All above + MCP server + Atlassian credentials | 20 min | Yes (Atlassian API token) |
| Copilot to search GitHub repos | All above + GitHub PAT | 15 min | Yes (GitHub Personal Access Token) |

**Most users start with option 2** (the full `.github/` folder). It's free, needs no API keys,
and gives you the most value immediately.

---

## Step 1: Copy the Files

Open PowerShell in your `learning-assistant` folder and run:

### Option A: Just Copilot Customization (recommended start)

```powershell
# SET THIS to your project's path
$target = "C:\path\to\your-project"

# Create .github folder if it doesn't exist
New-Item -ItemType Directory -Force "$target\.github"

# Copy everything
Copy-Item -Recurse -Force ".github\copilot-instructions.md" "$target\.github\"
Copy-Item -Recurse -Force ".github\instructions" "$target\.github\instructions"
Copy-Item -Recurse -Force ".github\agents" "$target\.github\agents"
Copy-Item -Recurse -Force ".github\prompts" "$target\.github\prompts"
Copy-Item -Recurse -Force ".github\skills" "$target\.github\skills"
```

### Option B: Copilot + MCP Servers (full power)

```powershell
$target = "C:\path\to\your-project"

# Copy Copilot customization (same as above)
Copy-Item -Recurse -Force ".github" "$target\.github"

# Copy MCP servers
Copy-Item -Recurse -Force "mcp-servers" "$target\mcp-servers"

# Copy VS Code config
New-Item -ItemType Directory -Force "$target\.vscode"
Copy-Item -Force ".vscode\mcp.json" "$target\.vscode\mcp.json"
Copy-Item -Force ".vscode\tasks.json" "$target\.vscode\tasks.json"
```

---

## Step 2: Edit for YOUR Project

### Essential Edits (do these now)

Open `$target\.github\copilot-instructions.md` and change:

```markdown
# BEFORE (learning-assistant specific)
- **Language:** Java 21+
- **Build:** Manual compilation (no build tool yet)
- **Purpose:** Hands-on experimentation with GitHub Copilot

# AFTER (your project)
- **Language:** [your language and version]
- **Build:** [your build tool: Maven/Gradle/npm/etc.]
- **Purpose:** [one-line description of your project]
```

### Files to Remove (if they don't apply)

| Your project uses... | Remove these |
|---|---|
| Not Java | `.github/instructions/java.instructions.md`, `.github/instructions/clean-code.instructions.md` |
| Not this specific repo | `.github/instructions/change-completeness.instructions.md`, `.github/instructions/steering-modes.instructions.md` |
| No brain workspace | `.github/prompts/brain-*.prompt.md`, `.github/skills/knowledge-management/brain-management/` |
| No MCP servers | `.github/skills/devops-tooling/mcp-development/`, `.github/prompts/domain/mcp.prompt.md` |
| No macOS | `.github/skills/languages-platforms/mac-dev/`, `.github/prompts/tools/mac-dev.prompt.md` |

### Files to Keep (universal value)

| File | Why it's useful everywhere |
|---|---|
| `.github/agents/debugger.agent.md` | Debug expert persona — works for any language |
| `.github/agents/designer.agent.md` | Architecture review — language-agnostic |
| `.github/agents/code-reviewer.agent.md` | Code review — works for any language |
| `.github/prompts/code/debug.prompt.md` | `/debug` slash command — universal |
| `.github/prompts/code/refactor.prompt.md` | `/refactor` slash command — universal |
| `.github/skills/learning-resources/software-engineering-resources/` | SE learning resources — universal |
| `.github/skills/devops-tooling/copilot-customization/` | Copilot customization knowledge — always useful |

---

## Step 3: Configure MCP Servers (skip if you only copied `.github/`)

### What Needs Configuration

| Server | Config file | API key needed? | Skip if... |
|---|---|---|---|
| Learning Resources | None needed | No | You don't want learning resource search |
| Atlassian Skill CLI | `.github/skills/devops-tooling/atlassian-tools/.env` | Yes — Atlassian PAT | You don't use Jira/Confluence, or you use Cloud |
| Atlassian MCP Server | `mcp-servers/user-config/servers/atlassian/atlassian-config.local.properties` | Yes — Atlassian API token | You don't use Jira/Confluence, or you use Server/DC |
| GitHub | `.vscode/mcp.json` (VS Code prompts for token) | Yes — GitHub PAT | You don't need GitHub repo search in Copilot |
| Filesystem | None needed | No | You don't want Copilot to read/write files directly |

### Learning Resources Server (no API key needed)

```powershell
cd "$target\mcp-servers"

# Build the server
.\build.ps1

# If java isn't on PATH, create a local config:
Copy-Item build.env.example build.env.local
# Edit build.env.local: set JAVA_HOME=C:\path\to\your\jdk-21
```

Then in `$target\.vscode\mcp.json`, the `learning-resources` server is already enabled.

### Atlassian Integration (skip if not needed)

**You do NOT need this unless you use Jira/Confluence at work.**

Two options — pick the one that matches your Atlassian instance:

#### Option A: Skill CLI (simpler — for Server/Data Center)

The skill CLI is a standalone Node.js script with 89 actions. No MCP server needed.

**Prerequisites:** Node.js 18+

```powershell
cd "$target\.github\skills\atlassian-tools"
Copy-Item .env.example .env
```

Edit `.env`:

```bash
# PAT token — generate in your profile: https://your-instance.com/plugins/personalaccesstokens/manage
JIRA_PAT_TOKEN=your-jira-pat-here
JIRA_BASE_URL=https://your-jira-instance.company.com

CONFLUENCE_PAT_TOKEN=your-confluence-pat-here
CONFLUENCE_BASE_URL=https://your-confluence-instance.company.com

BITBUCKET_PAT_TOKEN=your-bitbucket-pat-here
BITBUCKET_BASE_URL=https://your-bitbucket-instance.company.com
```

Verify it works:

```powershell
$env:CLI_JSON_ARGS = '{}'; node scripts\atlassian_cli.js get_current_jira_user
```

> **Multi-account?** See `.env.accounts.example` in the same folder for how to manage
> multiple Atlassian instances.

#### Option B: MCP Server (for Atlassian Cloud)

```powershell
cd "$target\mcp-servers\user-config\servers\atlassian"
Copy-Item atlassian-config.local.example.properties atlassian-config.local.properties
```

Edit `atlassian-config.local.properties`:

```properties
# Your Atlassian email (the one you log into Jira with)
atlassian.email=your.name@company.com

# API token — generate at: https://id.atlassian.com/manage-profile/security/api-tokens
atlassian.api.token=your-api-token-here

# Your Jira URL (no trailing slash)
atlassian.jira.base.url=https://your-company.atlassian.net

# Your Confluence URL (no trailing slash)
atlassian.confluence.base.url=https://your-company.atlassian.net/wiki

# Your Bitbucket workspace name (found in your Bitbucket URL)
atlassian.bitbucket.workspace=your-workspace
```

Then in `.vscode/mcp.json`, change `"disabled": true` to `"disabled": false` for `"atlassian"`.

### GitHub Server (needs PAT — skip if not needed)

In `.vscode/mcp.json`, change `"disabled": true` to `"disabled": false` for `"github"`.
VS Code will prompt you for a GitHub Personal Access Token the first time.

Generate one at: [github.com/settings/tokens](https://github.com/settings/tokens)
— select `repo` scope (or `public_repo` for public repos only).

---

## Step 4: What You Can Safely Delete

After copying, these are **not needed** in your target project:

| Folder/File | Why it's safe to delete |
|---|---|
| `.github/docs/` | Developer documentation for learning-assistant — not needed in your project |
| `.github/skills/career/career-resources/` | Career reference — only if you don't want career guidance |
| `.github/skills/daily-life/daily-assistant-resources/` | Daily productivity tips — personal preference |
| `mcp-servers/scripts/server-specific/github/` | GitHub MCP server scripts — only needed if using that server |
| `brain/` | Brain workspace — only copy if you want personal knowledge management |
| `mac-os/` | macOS setup guides — not needed in a work project |
| `search-engine/` | Search engine learning module — not needed in a work project |
| `src/` | Learning-assistant entry point — not your code |

### Brain Workspace at a Different Path?

If you copied the brain workspace but want it at a different location (e.g., `knowledge/workspace`
instead of `brain/ai-brain`), see the [Export Guide § 3b](export-guide.md#3b-custom-location-different-path--eg-knowledgeworkspace)
for the full configuration steps. The short version:

1. Move the brain files to your preferred path
2. Update `.github/copilot-instructions.md` § Configurable Paths with the new path
3. Find-replace `brain/ai-brain` in `.vscode/tasks.json` brain task commands
4. Update the `.gitignore` inbox pattern

> The brain scripts auto-detect their location — no code changes needed after moving.

### Brain Inside a Module or Package?

In `learning-assistant`, the brain workspace (`brain/ai-brain/`) lives inside a Java
module (`brain/`) that also contains Java source code (`src/digitalnotetaking/`) and PKM
guides (`digitalnotetaking/`). **You only need to copy `brain/ai-brain/`** — the Java code
and guides are specific to this repo's learning exercises.

In your target project the brain workspace might live inside a module (Maven/Gradle),
npm package (monorepo), or Python package. This is fine — the brain folder contains only
markdown and shell scripts, not compilable code. See the
[Export Guide § 3d](export-guide.md#3d-brain-inside-a-module--package--monorepo-package)
for build tool exclusion examples (Gradle, npm, Maven) and module-specific tips.

---

## Step 5: Verify It Works

1. Open your project in VS Code
2. Press `Ctrl+Shift+P` → "Reload Window"
3. Open Copilot Chat (`Ctrl+Alt+I`)
4. Switch to **Agent** mode
5. Type `/` — you should see your slash commands
6. Ask "write a method to calculate tax" — Copilot should follow your project's conventions

**If slash commands don't appear:**

- Check that `.github/prompts/` exists and has `.prompt.md` files
- Reload VS Code window again (`Ctrl+Shift+P` → Reload Window)
- Make sure you're in Agent mode (not Ask mode)

**If MCP tools don't appear:**

- Run the build task first: `Ctrl+Shift+B` → "mcp-servers: build"
- Check `.vscode/mcp.json` — server must NOT have `"disabled": true`
- Reload VS Code window

---

## Step 6: For Work vs Personal Repos

### Work Repository

- Remove all learning-specific content (brain, mac-os, search-engine modules)
- Remove personal skills you don't need (daily-assistant, career-resources)
- Keep: agents, core prompts, copilot-instructions, language-specific instructions
- Configure Atlassian server if your company uses Jira/Confluence
- **Don't commit API tokens** — `*.local.properties` files are gitignored

### Personal Software Development Repository

- Keep everything from `.github/` — it's all useful for personal projects
- MCP Learning Resources server is great for learning
- Skip Atlassian server (no work Jira to connect to)
- Optionally copy brain workspace for personal knowledge management
- Configure GitHub server if you want Copilot to search your personal repos

---

## Quick Reference: Files That Need Your Edits

| File | What to edit | Required? |
|---|---|---|
| `.github/copilot-instructions.md` | Project name, language, conventions | **Yes — always** |
| `.github/instructions/*.instructions.md` | Remove files for languages you don't use | Recommended |
| `.vscode/mcp.json` | Enable/disable servers, adjust paths | Only if using MCP |
| `mcp-servers/build.env.local` | Set `JAVA_HOME` if java not on PATH | Only if build fails |
| `mcp-servers/user-config/servers/atlassian/atlassian-config.local.properties` | Atlassian v1 credentials | Only if using Atlassian v1 |
| `mcp-servers/user-config/servers/atlassian-v2/atlassian-v2-config.local.properties` | Atlassian v2 credentials | Only if using Atlassian v2 |

> **Full config inventory:** See the [Configuration Reference](configuration-reference.md)
> for every config file, env var, path, and credential — with export checklist.

---

## Next Steps

| I want to... | Read this |
|---|---|
| Understand all 6 customization types | [5-Minute Guide](copilot-customization-newbie.md) |
| Full export with all options | [Export Guide](export-guide.md) |
| All config files, env vars, export checklist | [Configuration Reference](configuration-reference.md) |
| Set up MCP servers from scratch | [MCP Server Setup](mcp-server-setup.md) |
| Create my own custom agents/prompts | [Deep Dive §Creation Walkthroughs](copilot-customization-deep-dive.md#part-8-step-by-step-creation-walkthroughs) |

---

**Navigation:** [← START-HERE](START-HERE.md) · [Export Guide →](export-guide.md) · [5-Minute Guide →](copilot-customization-newbie.md)
