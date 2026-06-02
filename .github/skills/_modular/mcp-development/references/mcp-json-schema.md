# `.vscode/mcp.json` — Complete Schema Reference

> Extracted from the `mcp-development` skill. Companion reference file.
> For usage context and tool registration examples, see [`../SKILL.md`](../SKILL.md).

---

The `mcp.json` file is the **single configuration file** that registers all MCP servers for VS Code / Copilot. Place it at `.vscode/mcp.json` (workspace-level) so it's shared with the project.

## Full JSON Schema

```jsonc
{
  // Top-level: "servers" object — each key is a unique server name
  "servers": {

    // ─── SERVER NAME (unique identifier) ──────────────────────
    "<server-name>": {

      // REQUIRED: Transport type
      // "stdio"  → local process (stdin/stdout)
      // "http"   → remote HTTP endpoint (Streamable HTTP)
      // "sse"    → remote Server-Sent Events (legacy)
      "type": "stdio" | "http" | "sse",

      // ─── For stdio transport ──────────────────────────────
      "command": "<executable>",    // REQUIRED: "node", "python", "npx", "java", "docker", etc.
      "args": ["<arg1>", "<arg2>"],  // OPTIONAL: command-line arguments
      "cwd": "<working-directory>",  // OPTIONAL: working directory for the process
      "env": {                       // OPTIONAL: environment variables
        "KEY": "value",
        "SECRET": "${input:secretName}"  // Prompts user for value (secure)
      },

      // ─── For http / sse transport ────────────────────────
      "url": "<endpoint-url>",       // REQUIRED: "http://localhost:3001/mcp" or "https://..."
      "headers": {                   // OPTIONAL: HTTP headers
        "Authorization": "Bearer ${input:token}"
      }
    }
  },

  // OPTIONAL: Input variable definitions (prompted from user)
  "inputs": [
    {
      "id": "secretName",          // Referenced as ${input:secretName}
      "type": "promptString",
      "description": "Enter your API key",
      "password": true              // Masks input in the prompt
    }
  ]
}
```

## Variable Substitution in mcp.json

| Variable | Resolves To | Example |
|---|---|---|
| `${workspaceFolder}` | Absolute path to workspace root | `E:\projects\my-app` |
| `${input:variableName}` | User-prompted value (secure) | API keys, tokens, passwords |
| `${env:VARIABLE_NAME}` | System environment variable | `${env:HOME}`, `${env:PATH}` |
| `${userHome}` | User's home directory | `C:\Users\saharsh` |

## Complete Multi-MCP mcp.json Example

```jsonc
{
  "servers": {
    // ─── TypeScript MCP (local, stdio) ─────────────────────
    "weather": {
      "type": "stdio",
      "command": "node",
      "args": ["${workspaceFolder}/mcp-servers/weather-mcp/dist/index.js"],
      "env": {
        "WEATHER_API_KEY": "${input:weatherApiKey}"
      }
    },

    // ─── Python MCP (local, stdio) ─────────────────────────
    "database": {
      "type": "stdio",
      "command": "python",
      "args": ["${workspaceFolder}/mcp-servers/db-mcp/server.py"],
      "env": {
        "DATABASE_URL": "${input:databaseUrl}"
      }
    },

    // ─── npx-based MCP (no local install needed) ───────────
    "github": {
      "type": "stdio",
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_TOKEN": "${input:githubToken}"
      }
    },

    // ─── Docker-based MCP ──────────────────────────────────
    "containerized-api": {
      "type": "stdio",
      "command": "docker",
      "args": [
        "run", "-i", "--rm",
        "-e", "API_KEY=${input:apiKey}",
        "-v", "${workspaceFolder}:/workspace:ro",
        "my-mcp-server:latest"
      ]
    },

    // ─── Remote HTTP MCP ───────────────────────────────────
    "remote-service": {
      "type": "http",
      "url": "https://mcp.mycompany.com/api/mcp",
      "headers": {
        "Authorization": "Bearer ${input:remoteToken}"
      }
    },

    // ─── Java MCP (local, stdio) ───────────────────────────
    "inventory": {
      "type": "stdio",
      "command": "java",
      "args": ["-jar", "${workspaceFolder}/mcp-servers/inventory-mcp/target/inventory-mcp.jar"]
    },

    // ─── uvx-based Python MCP (no local install) ───────────
    "search": {
      "type": "stdio",
      "command": "uvx",
      "args": ["mcp-server-brave-search"],
      "env": {
        "BRAVE_API_KEY": "${input:braveKey}"
      }
    }
  },

  "inputs": [
    {
      "id": "weatherApiKey",
      "type": "promptString",
      "description": "OpenWeatherMap API key",
      "password": true
    },
    {
      "id": "databaseUrl",
      "type": "promptString",
      "description": "Database connection URL (e.g., postgresql://user:pass@host:5432/db)",
      "password": true
    },
    {
      "id": "githubToken",
      "type": "promptString",
      "description": "GitHub Personal Access Token",
      "password": true
    }
  ]
}
```

## User-Level mcp.json (Global, All Workspaces)

For MCPs you want available everywhere, add to VS Code `settings.json`:

```jsonc
// settings.json (Ctrl+Shift+P → "Preferences: Open User Settings (JSON)")
{
  "mcp": {
    "servers": {
      "filesystem": {
        "type": "stdio",
        "command": "npx",
        "args": ["-y", "@modelcontextprotocol/server-filesystem", "C:\\Users\\saharsh\\projects"]
      },
      "memory": {
        "type": "stdio",
        "command": "npx",
        "args": ["-y", "@modelcontextprotocol/server-memory"]
      }
    }
  }
}
```

## Claude Desktop Configuration

**Location (Windows):** `%APPDATA%\Claude\claude_desktop_config.json`
**Location (macOS):** `~/Library/Application Support/Claude/claude_desktop_config.json`

```jsonc
{
  "mcpServers": {
    "weather": {
      "command": "node",
      "args": ["C:\\path\\to\\mcp-servers\\weather-mcp\\dist\\index.js"],
      "env": {
        "WEATHER_API_KEY": "your-key-here"
      }
    },
    "database": {
      "command": "python",
      "args": ["C:\\path\\to\\mcp-servers\\db-mcp\\server.py"],
      "env": {
        "DATABASE_URL": "postgresql://user:pass@localhost:5432/mydb"
      }
    }
  }
}
```

> **Note:** Claude Desktop uses `"mcpServers"` (camelCase), VS Code uses `"servers"` inside `mcp.json`. The server config shape is slightly different — Claude doesn't have `"type"` field, it assumes stdio.

---
