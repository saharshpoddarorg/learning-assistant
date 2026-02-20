# Developer Setup Guide — MCP Servers

> **Time:** ~10 minutes  
> **Goal:** Get the MCP config system running and know exactly which files you need to edit.  
> **Prerequisite:** JDK 21+ ([Adoptium](https://adoptium.net/) or [Azul Zulu](https://www.azul.com/downloads/))

---

## Prerequisites

This project requires **Java 21+** for records, `var`, `Map.ofEntries()`, and other modern features.

```bash
# Verify your Java version:
java -version
# Must show 21 or higher

# If you have multiple JDKs, set JAVA_HOME:
export JAVA_HOME=/path/to/jdk-21        # Linux/Mac
$env:JAVA_HOME = "C:\path\to\jdk-21"     # Windows PowerShell
```

In VS Code, configure the JDK path in `.vscode/settings.json`:
```jsonc
"java.jdt.ls.java.home": "C:/Program Files/Eclipse Adoptium/jdk-21"
```

---

## What You'll Copy to Another Project

When you want to use this config system in a different repo, copy these folders:

```
FROM: mcp-servers/
  .vscode/          ← IDE settings, launch configs, extension recommendations
  user-config/      ← Your configuration files (example template + active config)
  src/              ← Java source code for the config system

TO: your-project/mcp-servers/   (or wherever makes sense)
```

---

## Files You MUST Manually Edit

These are the **only files** that require manual configuration. Everything else works out of the box.

### 1. `user-config/mcp-config.properties` (REQUIRED)

This is your **active configuration file** — the one the application loads at startup.

```
+===========================================================================+
|  ⚙️ ACTION REQUIRED                                                       |
|                                                                           |
|  1. Copy user-config/mcp-config.example.properties                        |
|           → user-config/mcp-config.properties                             |
|                                                                           |
|  2. Search for "<<<" and replace ALL placeholders                         |
|                                                                           |
|  3. At minimum, set:                                                      |
|     • apiKeys.github=ghp_your_real_token                                  |
|     • server.github.env.GITHUB_TOKEN=ghp_your_real_token                  |
+===========================================================================+
```

**What to configure:**

| Section | Key(s) | Required? | What to Put |
|---------|--------|-----------|-------------|
| **API Keys** | `apiKeys.github` | Yes (if using GitHub server) | Your GitHub PAT: `ghp_xxxx...` |
| **API Keys** | `apiKeys.openai` | Only if using OpenAI | Your OpenAI key: `sk-proj-xxxx...` |
| **Location** | `location.timezone` | No | Defaults to UTC |
| **Preferences** | `preferences.*` | No | Sensible defaults provided |
| **Browser** | `browser.profile` | Recommended | Create `MCP-Isolated` browser profile |
| **Servers** | `server.{name}.*` | Yes (at least 1) | Define your MCP server connections |
| **Profiles** | `profile.{name}.*` | No | Optional environment-specific overrides |

### 2. `.vscode/launch.json` (OPTIONAL)

If you want to pass API keys as environment variables instead of putting them in the properties file:

```jsonc
// In .vscode/launch.json → configurations[0].env:
"env": {
    "MCP_APIKEYS_GITHUB": "ghp_your_token_here",
    "MCP_APIKEYS_OPENAI": "sk-proj-your_key_here"
}
```

### 3. `.gitignore` (VERIFY)

Make sure your project's `.gitignore` includes:

```gitignore
# MCP config with real secrets
mcp-servers/user-config/mcp-config.properties
!mcp-servers/user-config/mcp-config.example.properties
```

---

## Step-by-Step Setup

### Step 1: Copy the Example Config

```bash
cd mcp-servers
cp user-config/mcp-config.example.properties user-config/mcp-config.properties
```

### Step 2: Find All Placeholders

```bash
# Shows every line that needs your attention:
grep -n "<<<" user-config/mcp-config.properties
```

Expected output (before editing):
```
37:apiKeys.github=<<<YOUR_GITHUB_PERSONAL_ACCESS_TOKEN>>>
40:apiKeys.openai=<<<YOUR_OPENAI_API_KEY>>>
91:server.github.env.GITHUB_TOKEN=<<<YOUR_GITHUB_PERSONAL_ACCESS_TOKEN>>>
96:server.filesystem.args=-y,@modelcontextprotocol/server-filesystem,<<<ABSOLUTE_PATH_TO_ALLOWED_DIRECTORY>>>
```

### Step 3: Replace Placeholders

Edit `user-config/mcp-config.properties` and replace each `<<<...>>>` with your actual value.

**Or use environment variables** (recommended for secrets):

```bash
# Linux/Mac
export MCP_APIKEYS_GITHUB="ghp_abc123def456ghi789jkl012mno345pqr678"

# Windows PowerShell
$env:MCP_APIKEYS_GITHUB = "ghp_abc123def456ghi789jkl012mno345pqr678"
```

### Step 4: (Recommended) Create an Isolated Browser Profile

If any of your MCP servers launch a browser:

1. **Chrome:** `chrome://settings/manageProfile` → Add Person → `MCP-Isolated`
2. **Edge:** `edge://settings/profiles` → Add Profile → `MCP-Isolated`
3. **Firefox:** `about:profiles` → Create a New Profile → `MCP-Isolated`

Then set in your config:
```properties
browser.profile=MCP-Isolated
```

### Step 5: Build & Run

```bash
cd mcp-servers

# Compile
javac -d out src/Main.java src/config/**/*.java

# Run (from mcp-servers/ directory)
java -cp out Main
```

Or use the VS Code launch configuration: press `F5` and select **"Run MCP Config Loader"**.

### Step 6: Verify Output

You should see:
```
=== MCP Configuration Summary ===
Active Profile : development
Timezone       : UTC
Locale         : en-US
Log Level      : DEBUG
Timeout        : 60s
Browser        : (system default)
Browser Profile: (default)
Launch Mode    : new-window
API Keys       : 2 configured
Servers        : 2 defined
  - filesystem [stdio] (enabled)
  - github [stdio] (enabled)
=================================
```

---

## Adding a New MCP Server

### For a Local (STDIO) Server

Add to `user-config/mcp-config.properties`:

```properties
server.my-tool.name=My Tool MCP Server
server.my-tool.enabled=true
server.my-tool.transport=stdio
server.my-tool.command=npx
server.my-tool.args=-y,@my-org/my-mcp-server
server.my-tool.env.API_KEY=<<<YOUR_MY_TOOL_API_KEY>>>
```

### For a Remote (SSE/HTTP) Server

```properties
server.my-api.name=My Remote Server
server.my-api.enabled=true
server.my-api.transport=sse
server.my-api.url=<<<https://your-server.example.com/mcp/sse>>>
```

---

## Copying to Another Project

When you want to reuse this config system elsewhere:

```bash
# From the source project root:
cp -r mcp-servers/.vscode    /path/to/target/mcp-servers/.vscode
cp -r mcp-servers/user-config /path/to/target/mcp-servers/user-config
cp -r mcp-servers/src         /path/to/target/mcp-servers/src

# Then in the target project:
cd /path/to/target/mcp-servers
cp user-config/mcp-config.example.properties user-config/mcp-config.properties
# Edit mcp-config.properties → replace <<<PLACEHOLDER>>> values
```

**Checklist after copying:**
- [ ] `user-config/mcp-config.properties` exists and has real values (no `<<<`)
- [ ] `.gitignore` excludes `mcp-servers/user-config/mcp-config.properties`
- [ ] `.gitignore` includes `!mcp-servers/user-config/mcp-config.example.properties`
- [ ] `.vscode/settings.json` — verify `java.project.sourcePaths` matches your structure
- [ ] Browser isolation profile created (if applicable)

---

## File Reference

| File | Editable? | Purpose |
|------|-----------|---------|
| `user-config/mcp-config.properties` | **YES — you must edit this** | Active config with your real values |
| `user-config/mcp-config.example.properties` | Reference only | Full template with docs (committed) |
| `.vscode/settings.json` | Rarely | IDE settings — adjust source paths if needed |
| `.vscode/launch.json` | Optional | Add env vars for secrets |
| `.vscode/extensions.json` | No | Extension recommendations |
| `src/Main.java` | No (unless extending) | Entry point |
| `src/config/**/*.java` | No (unless extending) | Config system internals |
