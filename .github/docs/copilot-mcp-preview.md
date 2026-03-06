# What's New in GitHub Copilot MCP & Customization — March 2026

> **Status:** Open Preview (globally available, not GA)
> **Last updated:** March 2026
> **Scope:** VS Code + GitHub Copilot Extension — MCP, agent mode, customization APIs

---

## Table of Contents

1. [MCP Support — Now Open Preview](#1-mcp-support--now-open-preview)
2. [The `/create-agent` Slash Command](#2-the-create-agent-slash-command)
3. [GitHub's Official MCP Server](#3-githubs-official-mcp-server)
4. [MCP Configuration in VS Code](#4-mcp-configuration-in-vs-code)
5. [New Transport: Streamable HTTP](#5-new-transport-streamable-http)
6. [Copilot Agent Mode](#6-copilot-agent-mode)
7. [Model Selection in Agents](#7-model-selection-in-agents)
8. [Language Model API for Extensions](#8-language-model-api-for-extensions)
9. [New Tools Available in Agents](#9-new-tools-available-in-agents)
10. [Protocol Version 2025-03-26](#10-protocol-version-2025-03-26)
11. [MCP Server Discovery](#11-mcp-server-discovery)
12. [Quick Migration Guide](#12-quick-migration-guide)

---

## 1. MCP Support — Now Open Preview

GitHub Copilot's MCP integration has moved from **limited private preview** to **open preview** — available to all GitHub Copilot subscribers (Individual, Business, Enterprise) with VS Code + the Copilot extension.

### What This Means

| Before (Limited Preview) | Now (Open Preview) |
|---|---|
| Invite-only access | Available to all Copilot subscribers |
| Experimental, may break | Stable enough for daily use |
| Limited documentation | Official docs at docs.github.com |
| stdio transport only (mostly) | stdio + SSE + new Streamable HTTP |
| No official GitHub MCP server | Official GitHub MCP server available |

### How to Enable MCP in VS Code

1. Install **GitHub Copilot** (`github.copilot`) and **GitHub Copilot Chat** (`github.copilot-chat`) extensions
2. Ensure you're on VS Code **1.99+** (for full agent mode + MCP support)
3. Add MCP server config to `.vscode/mcp.json` (see [section 4](#4-mcp-configuration-in-vs-code))
4. Open Copilot Chat in **Agent Mode** — look for the agent selector dropdown

> **Verify:** In VS Code, open Settings (`Ctrl+,`) and search for `github.copilot.chat.agent.agentMode` — if it exists, you're on a version with agent mode support.

---

## 2. The `/create-agent` Slash Command

**NEW in March 2026 preview:** VS Code Copilot Chat now includes a built-in **`/create-agent`** slash command in the Copilot Chat input.

### What It Does

The `/create-agent` command opens an **interactive wizard** that:
1. Asks for the agent name and description (plain language)
2. Presents a tool selection checklist (search, editFiles, terminal, fetch, etc.)
3. Lets you provide persona instructions or lets Copilot generate them based on your description
4. Saves the scaffolded file to `.github/agents/<name>.agent.md`

### Usage

```
Ctrl+Shift+I (open Copilot Chat)
→ Type: /create-agent
→ Follow the wizard
```

Or use the `/create-agent` prompt in this repo (`.github/prompts/create-agent.prompt.md`) for more granular control.

### Output Format

The command generates a valid `.agent.md` file:

```markdown
---
name: My-Agent
description: 'What this agent does'
tools: ['search', 'codebase', 'editFiles']
model: Claude Sonnet 4.5 (copilot)
---

# Agent Persona

Your persona instructions...
```

### Key Difference from Manual Creation

| `/create-agent` (built-in) | Manual creation |
|---|---|
| Interactive wizard in chat UI | Edit file directly or use `/create-agent` prompt |
| Auto-saves to `.github/agents/` | You choose the path |
| Generates persona with AI | You write the persona |
| Can't fine-tune all fields | Full control over every YAML field |
| Great for quick scaffolding | Great for precise, production-quality agents |

> **This project's convention:** After using the wizard, always verify the agent follows this repo's conventions (see `agents/README.md`) and add it to the agents table.

---

## 3. GitHub's Official MCP Server

GitHub has published an **official MCP server** (`github/github-mcp-server`) that gives Copilot direct access to GitHub APIs through the MCP protocol.

### Capabilities

| Category | Tools Available |
|---|---|
| **Repositories** | search_repositories, get_file_contents, list_branches, create_or_update_file |
| **Issues** | get_issue, list_issues, create_issue, update_issue, add_issue_comment |
| **Pull Requests** | get_pull_request, list_pull_requests, create_pull_request, merge_pull_request |
| **Code Search** | search_code, search_repositories |
| **Actions** | list_workflow_runs, get_workflow_run |

### VS Code Configuration

```json
// .vscode/mcp.json
{
  "servers": {
    "github": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "${input:GITHUB_TOKEN}"
      }
    }
  }
}
```

**Required token scope:** `repo`, `read:org`

### Why Use the GitHub MCP Server?

Without it, Copilot Chat's GitHub integration is limited to the repository it knows from context. With the GitHub MCP server, Copilot can:
- Search across ALL your repos for code snippets
- Read issues and PRs by number without opening them in the browser
- Create, update, and comment on issues directly from chat
- Cross-reference code across private repos

---

## 4. MCP Configuration in VS Code

### The `.vscode/mcp.json` File

VS Code now treats `.vscode/mcp.json` as a **first-class MCP configuration file** — the canonical place to declare MCP servers for any VS Code workspace.

```json
{
  "servers": {
    "my-server": {
      "command": "node",
      "args": ["./dist/server.js"],
      "env": {
        "API_KEY": "${input:API_KEY}"
      }
    },
    "remote-server": {
      "url": "https://my-server.example.com/mcp",
      "headers": {
        "Authorization": "Bearer ${input:BEARER_TOKEN}"
      }
    }
  },
  "inputs": [
    {
      "id": "API_KEY",
      "type": "promptString",
      "description": "Your API Key",
      "password": true
    },
    {
      "id": "BEARER_TOKEN",
      "type": "promptString",
      "description": "Bearer Token",
      "password": true
    }
  ]
}
```

### Key Fields

| Field | Purpose |
|---|---|
| `servers.<name>.command` | CLI command to start the server (stdio transport) |
| `servers.<name>.args` | Arguments to the command |
| `servers.<name>.env` | Environment variables (use `${input:VAR}` for secrets) |
| `servers.<name>.url` | HTTP endpoint (Streamable HTTP or SSE transport) |
| `servers.<name>.headers` | HTTP headers for remote servers |
| `inputs[]` | Input definitions — prompts VS Code to ask user for secrets at startup |

### Server Configuration in User Settings

You can also configure MCP servers globally in VS Code User Settings (`settings.json`):

```json
{
  "github.copilot.chat.mcp.servers": {
    "my-global-server": {
      "command": "java",
      "args": ["-cp", "/path/to/server.jar", "com.example.McpServer"]
    }
  }
}
```

> **Precedence:** `.vscode/mcp.json` (workspace) takes precedence over user settings for the same server name.

### The `inputs` Secret Management Pattern

Never hardcode secrets in `.vscode/mcp.json`. Use the `${input:VAR}` pattern:

```json
{
  "servers": {
    "atlassian": {
      "command": "java",
      "args": ["-cp", "out", "server.atlassian.AtlassianServer"],
      "env": {
        "ATLASSIAN_EMAIL":    "${input:ATLASSIAN_EMAIL}",
        "ATLASSIAN_API_TOKEN": "${input:ATLASSIAN_TOKEN}"
      }
    }
  },
  "inputs": [
    { "id": "ATLASSIAN_EMAIL", "type": "promptString", "description": "Atlassian email" },
    { "id": "ATLASSIAN_TOKEN", "type": "promptString", "description": "Atlassian API token", "password": true }
  ]
}
```

VS Code prompts the user once per session and caches the values in the secret store.

---

## 5. New Transport: Streamable HTTP

The MCP spec has formally introduced **Streamable HTTP** as the recommended transport for remote/production MCP servers. It replaces the older SSE transport.

### Comparison

| Transport | Use Case | Connection | Status |
|---|---|---|---|
| **stdio** | Local development, single-user | Parent process spawns child | Stable ✅ |
| **SSE** (Server-Sent Events) | Legacy remote HTTP | Two channels (POST + GET/SSE) | Deprecated 🔶 |
| **Streamable HTTP** | Remote/production, cloud | Single POST endpoint, optional streaming | Recommended ✅ |

### Streamable HTTP — How It Works

```
Client                          Server
  │                                │
  │── POST /mcp ──────────────────→│  (all client requests)
  │                                │
  │                                │  Server may respond with:
  │←── 200 + JSON ────────────────│  (1) Immediate response, OR
  │←── 200 + transfer-encoding ───│  (2) Chunked stream of SSE events
  │                                │
  │   (Only streams when needed)   │
```

**Single endpoint.** The server decides at response time whether to stream back results or return immediately.

### Server Configuration (Streamable HTTP)

```json
// .vscode/mcp.json — Streamable HTTP remote server
{
  "servers": {
    "my-cloud-server": {
      "url": "https://api.example.com/mcp",
      "headers": {
        "Authorization": "Bearer ${input:API_TOKEN}"
      }
    }
  }
}
```

### Building a Streamable HTTP Server (TypeScript)

```typescript
import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StreamableHTTPServerTransport } from "@modelcontextprotocol/sdk/server/streamableHttp.js";
import express from "express";

const app = express();
const server = new McpServer({ name: "my-server", version: "1.0.0" });

// Register tools...
server.tool("my_tool", "Does something", {}, async () => ({
  content: [{ type: "text", text: "result" }]
}));

app.post("/mcp", async (req, res) => {
  const transport = new StreamableHTTPServerTransport({ sessionIdHeader: "mcp-session-id" });
  await server.connect(transport);
  await transport.handleRequest(req, res);
});

app.listen(3000);
```

---

## 6. Copilot Agent Mode

**Copilot agent mode** is VS Code's implementation of an autonomous AI loop that can:
- Use MCP tools iteratively
- Edit multiple files in sequence
- Run terminal commands and react to output
- Iterate on test failures until they pass
- Continue working without waiting for user input after each step

### How to Enable Agent Mode

1. Open Copilot Chat sidebar (`Ctrl+Shift+I`)
2. In the chat input dropdown, switch from **"Ask"** to **"Agent"** mode
3. When in Agent mode, Copilot will use all registered MCP tools automatically

### Agent Mode vs Ask Mode

| Feature | Ask Mode | Agent Mode |
|---|---|---|
| MCP tool calls | Manual, one at a time | Autonomous, multi-step |
| File edits | Shows diffs, you apply | Applies automatically |
| Terminal commands | Suggests, you run | Runs automatically (with confirmation) |
| Multi-step tasks | You guide each step | Copilot decides the steps |
| Token usage | Lower | Higher (multiple tool calls) |

### Best Practices for Agent Mode

1. **Be specific about the goal** — vague goals lead to unnecessary tool calls
2. **Use confirmation gates** — sensitive operations (git push, delete) still require confirmation
3. **Watch the tool call log** — the sidebar shows every tool call in progress
4. **Stop if it goes off track** — click Stop immediately; don't wait for it to finish
5. **Close unused MCP servers** — fewer active servers = less decision overhead for the model

---

## 7. Model Selection in Agents

Custom agents can now **pin a specific AI model** using the `model` frontmatter field:

```yaml
---
name: My-Agent
description: 'My specialized agent'
tools: ['search', 'codebase']
model: Claude Sonnet 4.5 (copilot)   ← new field
---
```

### Supported Model Values (March 2026)

| Model String | Provider | Best For |
|---|---|---|
| `Claude Sonnet 4.5 (copilot)` | Anthropic via Copilot | Balanced: reasoning + speed (default) |
| `Claude Sonnet 4 (copilot)` | Anthropic via Copilot | Faster, slightly less capable |
| `gpt-4o (copilot)` | OpenAI via Copilot | Code generation, fast |
| `o3-mini (copilot)` | OpenAI via Copilot | Strong reasoning, slower |
| `gemini-2.0-flash (copilot)` | Google via Copilot | Very fast, large context window |

> **Note:** Available models depend on your Copilot subscription tier. Omitting the `model` field uses the user's currently selected model.

### When to Pin a Model

- **Thinking-heavy agents** (e.g., system design, architecture review) → `o3-mini`
- **Code generation agents** → `gpt-4o` or `Claude Sonnet 4.5`
- **Research/fetch agents** → `gemini-2.0-flash` (large context window for reading web pages)
- **Leave unpinned** — if you want the user's model choice to apply

---

## 8. Language Model API for Extensions

For VS Code extension authors, GitHub Copilot exposes the **Language Model API** (`vscode.lm`) — allowing extensions to call AI models without building their own LLM integration.

### API Surface

```typescript
// List available models
const models = await vscode.lm.selectChatModels({ vendor: 'copilot' });

// Send a request
const response = await models[0].sendRequest(
  [vscode.LanguageModelChatMessage.User("Summarize this code")],
  {},
  token          // CancellationToken
);

// Stream the response
for await (const chunk of response.text) {
  console.log(chunk);
}
```

### Chat Participant API

Extensions can register as a **chat participant** — appearing as `@my-extension` in Copilot Chat:

```typescript
const participant = vscode.chat.createChatParticipant(
  'my-extension.myParticipant',
  async (request, context, stream, token) => {
    stream.markdown("# My Response\n\n");
    
    const models = await vscode.lm.selectChatModels({ vendor: 'copilot' });
    const response = await models[0].sendRequest(
      [vscode.LanguageModelChatMessage.User(request.prompt)],
      {},
      token
    );
    
    for await (const chunk of response.text) {
      stream.markdown(chunk);
    }
  }
);
participant.iconPath = new vscode.ThemeIcon('robot');
```

> **This is for extension developers.** If you're just customizing Copilot behavior for your project, use `.github/agents/` and `.github/prompts/` instead — no coding required.

---

## 9. New Tools Available in Agents

Agent files can now reference additional tool types (added in 2025-2026):

| Tool | added | Description |
|---|---|---|
| `vscode_searchExtensions_internal` | 2025 | Search VS Code marketplace |
| `get_vscode_api` | 2025 | Query VS Code extension API docs |
| `github-pull-request_*` | 2025 | GitHub PR integration tools |
| `runCommand` | 2025 | Run VS Code commands programmatically |
| `terminalLastCommand` | 2025 | Access last terminal output |
| `testFailure` | 2025 | Get structured test failure data |
| `findTestFiles` | 2025 | Locate test files for source files |

### Updated Tool List for `tools` Frontmatter

```yaml
tools: [
  'search',             # workspace file search
  'codebase',           # code structure analysis
  'editFiles',          # file create/edit/delete
  'terminal',           # run terminal commands
  'fetch',              # HTTP / web page fetch
  'githubRepo',         # GitHub repo search
  'usages',             # find symbol references
  'problems',           # compile/lint errors
  'debugger',           # VS Code debugger
  'findTestFiles',      # locate test files
  'testFailure',        # test failure data
  'terminalLastCommand',# last terminal command
  'runCommand'          # VS Code command runner
]
```

---

## 10. Protocol Version 2025-03-26

The current stable MCP protocol version is **`2025-03-26`** (released March 2026).

### What Changed from 2024-11-05

| Feature | Old (2024-11-05) | New (2025-03-26) |
|---|---|---|
| **Transport** | stdio, SSE | stdio, Streamable HTTP (SSE deprecated) |
| **Capabilities** | Basic tools + resources | Tools, resources, prompts + elicitation |
| **Tool annotations** | None | `readOnlyHint`, `destructiveHint`, `idempotentHint` |
| **Structured tool output** | Text/images only | JSON structured data support |
| **Resource templates** | Basic URI templates | Completion support for URI params |
| **Sampling** | Not in spec | Server-initiated LLM sampling |
| **Elicitation** | Not in spec | Server can request user input mid-session |

### Tool Annotations (New)

MCP tools can now declare behavioral hints:

```json
{
  "name": "delete_file",
  "description": "Delete a file",
  "annotations": {
    "readOnlyHint": false,
    "destructiveHint": true,
    "idempotentHint": false
  },
  "inputSchema": { ... }
}
```

These hints help AI clients (like Copilot) decide when to ask for confirmation before calling destructive tools.

### Initialize Request (Updated)

```json
{
  "method": "initialize",
  "params": {
    "protocolVersion": "2025-03-26",
    "capabilities": {
      "tools": {},
      "resources": { "subscribe": true },
      "elicitation": {}
    },
    "clientInfo": { "name": "vscode-copilot", "version": "2.0" }
  }
}
```

---

## 11. MCP Server Discovery

### VS Code Marketplace Integration

VS Code now has **MCP server discovery** built into the Extensions panel. Search for extensions tagged `#mcp-server` to find community-published servers.

### Popular Community MCP Servers (March 2026)

| Server | Publisher | What It Does |
|---|---|---|
| `@modelcontextprotocol/server-github` | Anthropic/GitHub | GitHub repos, issues, PRs, code search |
| `@modelcontextprotocol/server-filesystem` | Anthropic | Read/write local files |
| `@modelcontextprotocol/server-brave-search` | Anthropic | Brave web search API |
| `@modelcontextprotocol/server-postgres` | Anthropic | PostgreSQL query execution |
| `@modelcontextprotocol/server-slack` | Anthropic | Slack messages, channels |
| `@modelcontextprotocol/server-memory` | Anthropic | Persistent knowledge graph for AI memory |
| `@modelcontextprotocol/server-puppeteer` | Anthropic | Browser automation |
| `mcp-server-atlassian` | Community | Jira, Confluence, Bitbucket |

All official servers: `github.com/modelcontextprotocol/servers`

### MCP Registry

The community maintains a registry at `mcpservers.org` — lists categorized MCP servers with install instructions.

---

## 12. Quick Migration Guide

### If You Have an Older `.vscode/mcp.json`

Old format (pre-2025):
```json
{
  "mcpServers": {
    "my-server": {
      "command": "node",
      "args": ["server.js"]
    }
  }
}
```

New format (2025+):
```json
{
  "servers": {
    "my-server": {
      "command": "node",
      "args": ["server.js"]
    }
  }
}
```

> **Note:** VS Code still accepts the old `mcpServers` key for backward compatibility, but `servers` is the canonical key.

### If You're Using SSE Transport

SSE transport still works but is being phased out. Migrate to Streamable HTTP:

**Old (SSE server):**
```typescript
import { SSEServerTransport } from "@modelcontextprotocol/sdk/server/sse.js";
// ... two-channel setup
```

**New (Streamable HTTP server):**
```typescript
import { StreamableHTTPServerTransport } from "@modelcontextprotocol/sdk/server/streamableHttp.js";
// ... single POST endpoint
```

**Client config change:**
```json
// Old SSE: needed both url and /message endpoint separately
{ "url": "http://localhost:3001/sse" }

// New Streamable HTTP: single endpoint
{ "url": "http://localhost:3001/mcp" }
```

### If You're Building Custom Agents

The agent file format is stable. The only additions are:
- `model` field (optional, now supported)
- More tools available in the `tools` array

No breaking changes — existing `.agent.md` files continue to work.

---

## Related Documentation in This Project

| Doc | Content |
|---|---|
| [mcp-server-setup.md](mcp-server-setup.md) | Full walkthrough for setting up this project's Java MCP servers |
| [mcp-ecosystem.md](mcp-ecosystem.md) | Combining multiple MCP servers, LLMs, agent frameworks |
| [mcp-how-it-works.md](mcp-how-it-works.md) | Deep protocol explanation |
| [mcp-implementations.md](mcp-implementations.md) | Implementation examples |
| [agents/README.md](../agents/README.md) | Custom agent guide + file format |
| [customization-guide.md](customization-guide.md) | How all Copilot primitives fit together |
| [slash-commands.md](slash-commands.md) | All `/commands` including `/create-agent` |
| `mcp-development/SKILL.md` | Comprehensive MCP build reference (via skill system) |
