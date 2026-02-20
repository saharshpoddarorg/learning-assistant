---
name: mcp-development
description: >
  Comprehensive guide to MCP (Model Context Protocol) — the open standard for connecting AI assistants to external
  tools, data sources, and services. Covers MCP architecture, protocol specification, all three primitives
  (Tools, Resources, Prompts), transport mechanisms (stdio, SSE, Streamable HTTP), building MCP servers in
  TypeScript/Python/Java/Go/C#, configuring MCP clients (VS Code/Copilot, Claude Desktop, Cursor), types of
  MCP servers (data access, developer tools, productivity, infrastructure, custom domain), combining MCP with
  REST/GraphQL/gRPC APIs, agent architecture patterns (single agent, multi-agent, tool chaining, routing,
  human-in-the-loop), MCP + LLM API composition for custom agents, security best practices (auth, sandboxing,
  audit), testing with MCP Inspector, deployment (local, Docker, cloud, serverless), ecosystem and registries,
  real-world examples, and step-by-step guides for building and distributing your own MCP servers.
  Use when asked about MCP, Model Context Protocol, building MCP servers, configuring AI agents with tools,
  tool-use protocol, agentic AI tooling, MCP clients, or custom tool integration for AI assistants.
---

# MCP (Model Context Protocol) — Comprehensive Development Guide

## What Is MCP?

**MCP (Model Context Protocol)** is an open standard (originally created by Anthropic, now broadly adopted) that defines a universal protocol for AI assistants to connect with external tools, data sources, and services. It standardizes how AI models discover, invoke, and receive results from tools — replacing the need for custom integrations per tool.

**Analogy:** MCP is to AI tools what USB-C is to devices — one universal connector instead of proprietary cables for every device.

**Key Value:**
- **For AI users:** Access any tool (databases, APIs, file systems, Git, Slack) through your AI assistant without writing glue code
- **For tool makers:** Build once, work with every MCP-compatible AI client (VS Code Copilot, Claude, Cursor, etc.)
- **For agent builders:** Compose sophisticated multi-tool agents using a standard protocol

---

## Protocol Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────┐
│                   AI APPLICATION                 │
│  (VS Code, Claude Desktop, Cursor, Custom App)  │
│                                                  │
│  ┌─────────────┐  ┌─────────────┐  ┌──────────┐│
│  │ MCP Client  │  │ MCP Client  │  │MCP Client ││
│  │     #1      │  │     #2      │  │    #3     ││
│  └──────┬──────┘  └──────┬──────┘  └─────┬─────┘│
└─────────┼────────────────┼────────────────┼──────┘
          │                │                │
    ┌─────▼─────┐   ┌─────▼─────┐   ┌─────▼─────┐
    │MCP Server │   │MCP Server │   │MCP Server │
    │ (GitHub)  │   │(Database) │   │ (Custom)  │
    └─────┬─────┘   └─────┬─────┘   └─────┬─────┘
          │                │                │
    ┌─────▼─────┐   ┌─────▼─────┐   ┌─────▼─────┐
    │GitHub API │   │PostgreSQL │   │Your APIs  │
    └───────────┘   └───────────┘   └───────────┘
```

**Key architectural properties:**
- **1:1 client-server:** Each MCP client connects to exactly one MCP server
- **Host application:** The AI app (VS Code, Claude) is the "host" that manages multiple client-server pairs
- **Server isolation:** Each server runs independently — a crash in one doesn't affect others
- **Protocol:** JSON-RPC 2.0 over the chosen transport

### The Three MCP Primitives

MCP servers can expose three types of capabilities:

| Primitive | Controlled By | Purpose | Analogy |
|---|---|---|---|
| **Tools** | AI Model | Executable functions the AI can invoke | Power tools in a workshop |
| **Resources** | Application | Data/context the client app can read | Reference books on a shelf |
| **Prompts** | User | Pre-built prompt templates | Recipe cards in a kitchen |

#### 1. Tools (Model-Controlled)

Tools are the most commonly used primitive. They represent functions that the AI model can decide to call.

**Characteristics:**
- Declared with a name, description, and JSON Schema for inputs
- The AI MODEL autonomously decides when to invoke them (based on user intent)
- May have side effects (write to DB, send email, create file)
- Should include clear descriptions so the AI knows when to use them
- Results are returned as content blocks (text, images, or embedded resources)

**JSON-RPC flow:**
```
Client → Server: { "method": "tools/list" }
Server → Client: { "tools": [{ "name": "query_db", "description": "...", "inputSchema": {...} }] }

Client → Server: { "method": "tools/call", "params": { "name": "query_db", "arguments": { "sql": "SELECT ..." } } }
Server → Client: { "content": [{ "type": "text", "text": "..." }], "isError": false }
```

#### 2. Resources (Application-Controlled)

Resources represent data that the client application (not the AI model) decides to read. Think of them as context the app can attach to the conversation.

**Characteristics:**
- Addressed by URI (e.g., `file://project/README.md`, `db://users/schema`)
- The APPLICATION logic decides which resources to include
- Read-only — resources expose data, not actions
- Can be static (always available) or dynamic (URI templates like `db://table/{name}`)
- Support subscriptions — server notifies client when data changes

**Use cases:**
- Project file contents
- Database schemas
- API documentation
- Configuration files
- Log data

#### 3. Prompts (User-Controlled)

Prompts are pre-built templates that users can explicitly select. They help users invoke common AI workflows without writing the full prompt.

**Characteristics:**
- User explicitly chooses which prompt to use (often via slash commands or menus)
- Parameterized — accept arguments to customize the template
- Return structured messages that prepopulate the AI conversation
- Great for standardized workflows (code review, test generation, analysis)

---

## Transport Mechanisms

### stdio (Standard I/O) — Default for Local Development

```
Host Application
    │
    ├── spawns child process: "node server.js"
    │
    │   stdin  → JSON-RPC messages TO server
    │   stdout ← JSON-RPC messages FROM server
    │   stderr ← Server logs (not protocol messages)
    │
    └── Process lifecycle managed by host
```

**When to use:** Local development, CLI tools, single-user setups
**Pros:** No network config, inherently secure, simple
**Cons:** Local only, one client per server instance

### SSE (Server-Sent Events) — Legacy Remote Transport

```
Client                          Server
  │                                │
  │── POST /message ──────────────→│  (client → server messages)
  │                                │
  │←── GET /sse (event stream) ───│  (server → client messages)
  │                                │
```

**When to use:** Remote servers (being superseded by Streamable HTTP)
**Pros:** Works over HTTP, standard web tech
**Cons:** Two-channel communication, more complex

### Streamable HTTP — Recommended for Remote/Production

```
Client                          Server
  │                                │
  │── POST /mcp ──────────────────→│
  │←── Response (may be streamed) ─│
  │                                │
```

**When to use:** Production remote deployments, cloud-hosted servers
**Pros:** Single endpoint, supports streaming, simpler than SSE
**Cons:** Newer standard, evolving tooling

---

## Protocol Lifecycle

### Session Flow

```
Phase 1: INITIALIZE
──────────────────
Client → Server:
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": {
    "protocolVersion": "2025-03-26",
    "capabilities": { "tools": {}, "resources": {} },
    "clientInfo": { "name": "vscode-copilot", "version": "1.0.0" }
  }
}

Server → Client:
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "protocolVersion": "2025-03-26",
    "capabilities": {
      "tools": { "listChanged": true },
      "resources": { "subscribe": true, "listChanged": true }
    },
    "serverInfo": { "name": "my-server", "version": "1.0.0" }
  }
}

Phase 2: INITIALIZED (notification, no response expected)
──────────────────────
Client → Server:
{ "jsonrpc": "2.0", "method": "notifications/initialized" }

Phase 3: OPERATION (main loop — repeats)
──────────────────────
tools/list, tools/call, resources/list, resources/read, prompts/list, prompts/get
+ Server notifications: tools/list_changed, resources/list_changed, progress

Phase 4: SHUTDOWN
──────────────────────
Client closes connection or sends shutdown notification
Server releases resources and exits
```

### Capability Negotiation

During initialization, both sides declare what they support:

**Server capabilities:**
- `tools` — can serve tool definitions and handle tool calls
- `resources` — can serve resources (with optional `subscribe` and `listChanged`)
- `prompts` — can serve prompt templates
- `logging` — supports structured logging
- `experimental` — optional experimental features

**Client capabilities:**
- `roots` — can provide workspace root URIs
- `sampling` — can handle server-initiated LLM sampling requests
- `experimental` — optional experimental features

---

## Building MCP Servers — Complete Guides by Language

### TypeScript (Official SDK — Most Mature)

#### Project Setup

```bash
# Create project
mkdir my-mcp-server && cd my-mcp-server
npm init -y

# Install dependencies
npm install @modelcontextprotocol/sdk zod
npm install -D typescript @types/node

# TypeScript config
npx tsc --init --target es2022 --module nodenext --moduleResolution nodenext --outDir dist --rootDir src

# Create source file
mkdir src && touch src/index.ts
```

**package.json additions:**
```json
{
  "type": "module",
  "bin": { "my-mcp-server": "dist/index.js" },
  "scripts": {
    "build": "tsc",
    "start": "node dist/index.js",
    "inspect": "npx @modelcontextprotocol/inspector node dist/index.js"
  }
}
```

#### Complete Server Implementation

```typescript
#!/usr/bin/env node
// src/index.ts

import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { z } from "zod";

// ─── SERVER CREATION ───────────────────────────────────────────────
const server = new McpServer({
  name: "my-awesome-server",
  version: "1.0.0",
  description: "An example MCP server demonstrating all primitives"
});

// ─── TOOLS ─────────────────────────────────────────────────────────

// Simple tool: no parameters
server.tool(
  "get_server_status",
  "Check the health status of the server",
  {},
  async () => ({
    content: [{ type: "text", text: JSON.stringify({ status: "healthy", uptime: process.uptime() }) }]
  })
);

// Tool with parameters and validation
server.tool(
  "search_items",
  "Search for items in the inventory by name, category, or price range",
  {
    query: z.string().describe("Search query string"),
    category: z.enum(["electronics", "clothing", "food", "all"]).default("all").describe("Category filter"),
    minPrice: z.number().min(0).optional().describe("Minimum price filter"),
    maxPrice: z.number().min(0).optional().describe("Maximum price filter"),
    limit: z.number().min(1).max(100).default(20).describe("Maximum results to return")
  },
  async ({ query, category, minPrice, maxPrice, limit }) => {
    try {
      // Your actual search logic here
      const results = await performSearch({ query, category, minPrice, maxPrice, limit });
      return {
        content: [{
          type: "text",
          text: JSON.stringify({
            query,
            resultCount: results.length,
            items: results
          }, null, 2)
        }]
      };
    } catch (error) {
      return {
        content: [{ type: "text", text: `Search failed: ${error.message}` }],
        isError: true
      };
    }
  }
);

// Tool that performs a write/mutation
server.tool(
  "create_item",
  "Create a new item in the inventory",
  {
    name: z.string().min(1).describe("Item name"),
    category: z.enum(["electronics", "clothing", "food"]).describe("Item category"),
    price: z.number().positive().describe("Item price"),
    description: z.string().optional().describe("Item description")
  },
  async ({ name, category, price, description }) => {
    // Input validation beyond schema
    if (price > 1_000_000) {
      return {
        content: [{ type: "text", text: "Price exceeds maximum allowed value of 1,000,000" }],
        isError: true
      };
    }
    
    const item = await db.createItem({ name, category, price, description });
    return {
      content: [{ type: "text", text: `Created item: ${JSON.stringify(item, null, 2)}` }]
    };
  }
);

// ─── RESOURCES ─────────────────────────────────────────────────────

// Static resource
server.resource(
  "inventory-schema",
  "inventory://schema",
  "The database schema for the inventory system",
  async (uri) => ({
    contents: [{
      uri: uri.href,
      mimeType: "application/json",
      text: JSON.stringify({
        tables: {
          items: { columns: ["id", "name", "category", "price", "description", "created_at"] },
          categories: { columns: ["id", "name", "parent_id"] }
        }
      }, null, 2)
    }]
  })
);

// Dynamic resource with URI template
server.resource(
  "item-detail",
  "inventory://items/{itemId}",
  "Detailed information about a specific inventory item",
  async (uri, { itemId }) => {
    const item = await db.getItem(itemId);
    if (!item) {
      throw new Error(`Item ${itemId} not found`);
    }
    return {
      contents: [{
        uri: uri.href,
        mimeType: "application/json",
        text: JSON.stringify(item, null, 2)
      }]
    };
  }
);

// ─── PROMPTS ───────────────────────────────────────────────────────

server.prompt(
  "analyze_inventory",
  "Analyze inventory levels and suggest optimizations",
  { category: z.string().optional().describe("Category to focus on, or all") },
  async ({ category }) => ({
    messages: [{
      role: "user",
      content: {
        type: "text",
        text: `Analyze the ${category || "entire"} inventory:\n` +
          `1. Identify items with low stock\n` +
          `2. Find pricing anomalies\n` +
          `3. Suggest reorder quantities\n` +
          `4. Highlight slow-moving items\n` +
          `Use the search_items tool to gather data before analysis.`
      }
    }]
  })
);

// ─── START SERVER ──────────────────────────────────────────────────
const transport = new StdioServerTransport();
await server.connect(transport);
console.error("MCP server running on stdio");  // stderr for logs, stdout reserved for protocol
```

#### TypeScript — HTTP Transport Variant

```typescript
import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StreamableHTTPServerTransport } from "@modelcontextprotocol/sdk/server/streamableHttp.js";
import express from "express";

const app = express();
const server = new McpServer({ name: "my-http-server", version: "1.0.0" });

// Register tools/resources/prompts same as above...

const transport = new StreamableHTTPServerTransport({ sessionIdGenerator: undefined });
await server.connect(transport);

app.post("/mcp", async (req, res) => {
  await transport.handleRequest(req, res, req.body);
});

app.listen(3001, () => console.log("MCP HTTP server on port 3001"));
```

### Python (Official SDK)

#### Project Setup

```bash
# Using uv (recommended)
uv init my-mcp-server && cd my-mcp-server
uv add "mcp[cli]"

# Or using pip
pip install "mcp[cli]"
```

#### Complete Server Implementation

```python
# server.py
from mcp.server.fastmcp import FastMCP
from typing import Optional
import json

# ─── SERVER CREATION ─────────────────────────────────────────
mcp = FastMCP(
    name="my-awesome-server",
    version="1.0.0",
    description="An example MCP server demonstrating all primitives"
)

# ─── TOOLS ───────────────────────────────────────────────────

@mcp.tool()
async def get_server_status() -> str:
    """Check the health status of the server."""
    return json.dumps({"status": "healthy", "version": "1.0.0"})

@mcp.tool()
async def search_items(
    query: str,
    category: str = "all",
    min_price: Optional[float] = None,
    max_price: Optional[float] = None,
    limit: int = 20
) -> str:
    """Search for items in the inventory by name, category, or price range.
    
    Args:
        query: Search query string
        category: Category filter (electronics, clothing, food, or all)
        min_price: Minimum price filter
        max_price: Maximum price filter
        limit: Maximum results to return (1-100)
    """
    results = await perform_search(query, category, min_price, max_price, limit)
    return json.dumps({"query": query, "count": len(results), "items": results}, indent=2)

@mcp.tool()
async def create_item(
    name: str,
    category: str,
    price: float,
    description: Optional[str] = None
) -> str:
    """Create a new item in the inventory.
    
    Args:
        name: Item name
        category: Item category (electronics, clothing, food)
        price: Item price (must be positive)
        description: Optional item description
    """
    if price > 1_000_000:
        raise ValueError("Price exceeds maximum allowed value of 1,000,000")
    
    item = await db.create_item(name=name, category=category, price=price, description=description)
    return json.dumps(item, indent=2)

# ─── RESOURCES ───────────────────────────────────────────────

@mcp.resource("inventory://schema")
async def get_schema() -> str:
    """The database schema for the inventory system."""
    return json.dumps({
        "tables": {
            "items": {"columns": ["id", "name", "category", "price", "description", "created_at"]},
            "categories": {"columns": ["id", "name", "parent_id"]}
        }
    }, indent=2)

@mcp.resource("inventory://items/{item_id}")
async def get_item_detail(item_id: str) -> str:
    """Detailed information about a specific inventory item."""
    item = await db.get_item(item_id)
    if not item:
        raise ValueError(f"Item {item_id} not found")
    return json.dumps(item, indent=2)

# ─── PROMPTS ─────────────────────────────────────────────────

@mcp.prompt()
async def analyze_inventory(category: str = "all") -> str:
    """Analyze inventory levels and suggest optimizations."""
    return (
        f"Analyze the {category} inventory:\n"
        "1. Identify items with low stock\n"
        "2. Find pricing anomalies\n"
        "3. Suggest reorder quantities\n"
        "4. Highlight slow-moving items\n"
        "Use the search_items tool to gather data before analysis."
    )

# ─── RUN SERVER ──────────────────────────────────────────────
if __name__ == "__main__":
    mcp.run()  # stdio by default
    # For HTTP: mcp.run(transport="sse")
```

#### Running and Testing (Python)

```bash
# Run with stdio
python server.py

# Run with SSE transport
python server.py --transport sse

# Test with MCP Inspector
mcp dev server.py

# Install as global tool (uv)
uv tool install .
```

### Java (Spring AI MCP Integration)

#### Maven Dependencies

```xml
<dependencies>
    <!-- MCP Server (standalone) -->
    <dependency>
        <groupId>io.modelcontextprotocol</groupId>
        <artifactId>mcp</artifactId>
        <version>0.9.0</version>
    </dependency>
    
    <!-- OR: Spring Boot MCP (recommended for Spring projects) -->
    <dependency>
        <groupId>io.modelcontextprotocol</groupId>
        <artifactId>mcp-spring-webflux</artifactId>
        <version>0.9.0</version>
    </dependency>
    
    <!-- Spring AI (for Spring Boot auto-configuration) -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-mcp-server-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

#### Standalone Java Server

```java
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.StdioServerTransport;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.Map;

public class InventoryMcpServer {
    
    public static void main(String[] args) {
        var transport = new StdioServerTransport();
        
        // Define tools
        var searchTool = new McpServerFeatures.SyncToolSpecification(
            new McpSchema.Tool(
                "search_items",
                "Search for items in the inventory",
                Map.of(
                    "type", "object",
                    "properties", Map.of(
                        "query", Map.of("type", "string", "description", "Search query"),
                        "category", Map.of("type", "string", "enum", List.of("electronics", "clothing", "food", "all")),
                        "limit", Map.of("type", "integer", "default", 20, "minimum", 1, "maximum", 100)
                    ),
                    "required", List.of("query")
                )
            ),
            (exchange, arguments) -> {
                final var query = (String) arguments.get("query");
                final var limit = (Integer) arguments.getOrDefault("limit", 20);
                final var results = InventoryService.search(query, limit);
                return new McpSchema.CallToolResult(
                    List.of(new McpSchema.TextContent(results)),
                    false
                );
            }
        );
        
        // Build server
        var server = McpServer.sync(transport)
            .serverInfo("inventory-server", "1.0.0")
            .capabilities(McpSchema.ServerCapabilities.builder()
                .tools(new McpSchema.ServerCapabilities.ToolCapabilities(true))
                .resources(new McpSchema.ServerCapabilities.ResourceCapabilities(true, true))
                .build())
            .tools(searchTool)
            .build();
    }
}
```

#### Spring Boot MCP Server

```java
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class InventoryTools {
    
    @Tool(description = "Search for items in the inventory")
    public String searchItems(
        @ToolParam(description = "Search query") String query,
        @ToolParam(description = "Category filter", required = false) String category,
        @ToolParam(description = "Max results", required = false) Integer limit
    ) {
        // Implementation
        return inventoryService.search(query, category, limit != null ? limit : 20);
    }
    
    @Tool(description = "Create a new inventory item")
    public String createItem(
        @ToolParam(description = "Item name") String name,
        @ToolParam(description = "Category") String category,
        @ToolParam(description = "Price") double price
    ) {
        return inventoryService.create(name, category, price);
    }
}
```

**application.yml:**
```yaml
spring:
  ai:
    mcp:
      server:
        name: inventory-server
        version: 1.0.0
        type: SYNC  # or ASYNC
        stdio: true
```

### Go MCP Server

```go
// Using github.com/mark3labs/mcp-go
package main

import (
    "context"
    "encoding/json"
    "fmt"
    "github.com/mark3labs/mcp-go/mcp"
    "github.com/mark3labs/mcp-go/server"
)

func main() {
    s := server.NewMCPServer("inventory-server", "1.0.0",
        server.WithToolCapabilities(true),
        server.WithResourceCapabilities(true, true),
    )
    
    // Register tool
    searchTool := mcp.NewTool("search_items",
        mcp.WithDescription("Search for items in the inventory"),
        mcp.WithString("query", mcp.Required(), mcp.Description("Search query")),
        mcp.WithNumber("limit", mcp.Description("Max results"), mcp.DefaultNumber(20)),
    )
    
    s.AddTool(searchTool, func(ctx context.Context, req mcp.CallToolRequest) (*mcp.CallToolResult, error) {
        query := req.Params.Arguments["query"].(string)
        results, err := Search(query)
        if err != nil {
            return mcp.NewToolResultError(fmt.Sprintf("search failed: %v", err)), nil
        }
        jsonBytes, _ := json.MarshalIndent(results, "", "  ")
        return mcp.NewToolResultText(string(jsonBytes)), nil
    })
    
    // Start stdio server
    if err := server.ServeStdio(s); err != nil {
        panic(err)
    }
}
```

### C# MCP Server

```csharp
// NuGet: ModelContextProtocol
using ModelContextProtocol;
using ModelContextProtocol.Server;
using System.ComponentModel;

var builder = Host.CreateApplicationBuilder(args);
builder.Services.AddMcpServer()
    .WithStdioServerTransport()
    .WithToolsFromAssembly();
var app = builder.Build();
await app.RunAsync();

// Tools defined as static methods with attributes
[McpServerToolType]
public static class InventoryTools
{
    [McpServerTool, Description("Search for items in the inventory")]
    public static async Task<string> SearchItems(
        [Description("Search query")] string query,
        [Description("Max results")] int limit = 20)
    {
        var results = await InventoryService.SearchAsync(query, limit);
        return JsonSerializer.Serialize(results, new JsonSerializerOptions { WriteIndented = true });
    }
}
```

---

## Configuration Reference

### VS Code / GitHub Copilot

**Workspace-level** (`.vscode/mcp.json`):
```jsonc
{
  "servers": {
    // Local stdio server
    "my-server": {
      "type": "stdio",
      "command": "node",
      "args": ["${workspaceFolder}/mcp-server/dist/index.js"],
      "env": {
        "DATABASE_URL": "${input:databaseUrl}",
        "API_KEY": "${input:apiKey}"
      }
    },
    
    // npx-based (no local install)
    "github": {
      "type": "stdio",
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": { "GITHUB_TOKEN": "${input:githubToken}" }
    },
    
    // Python server
    "python-server": {
      "type": "stdio",
      "command": "python",
      "args": ["-m", "my_mcp_server"],
      "cwd": "${workspaceFolder}/mcp-servers/python"
    },
    
    // Docker-based
    "docker-server": {
      "type": "stdio",
      "command": "docker",
      "args": [
        "run", "-i", "--rm",
        "-e", "DB_URL=${input:dbUrl}",
        "-v", "${workspaceFolder}:/workspace:ro",
        "my-mcp-server:latest"
      ]
    },
    
    // Remote HTTP server
    "remote-server": {
      "type": "http",
      "url": "https://mcp.example.com/api",
      "headers": {
        "Authorization": "Bearer ${input:token}"
      }
    }
  }
}
```

**User-level** (VS Code `settings.json`):
```jsonc
{
  "mcp": {
    "servers": {
      "global-filesystem": {
        "type": "stdio",
        "command": "npx",
        "args": ["-y", "@modelcontextprotocol/server-filesystem", "C:\\Users\\me\\projects"]
      }
    }
  }
}
```

### Claude Desktop

**macOS:** `~/Library/Application Support/Claude/claude_desktop_config.json`  
**Windows:** `%APPDATA%\Claude\claude_desktop_config.json`

```jsonc
{
  "mcpServers": {
    "my-server": {
      "command": "node",
      "args": ["/absolute/path/to/server/dist/index.js"],
      "env": {
        "API_KEY": "your-key"
      }
    },
    "python-server": {
      "command": "python",
      "args": ["-m", "my_mcp_server"]
    }
  }
}
```

### Cursor

Add in Cursor settings → Features → MCP:
```jsonc
{
  "mcpServers": {
    "my-server": {
      "command": "node",
      "args": ["path/to/server.js"]
    }
  }
}
```

---

## Combining MCP with APIs — Patterns & Examples

### Pattern 1: REST API → MCP Wrapper

The most common pattern. Wrap an existing REST API so AI can use it.

```
┌──────────┐      ┌──────────────┐      ┌──────────────┐
│  AI      │ MCP  │  MCP Server  │ HTTP │  REST API    │
│  Agent   │◄────►│  (Wrapper)   │─────→│  (Existing)  │
└──────────┘      └──────────────┘      └──────────────┘
```

**Implementation strategy:**
1. Read the API's OpenAPI/Swagger spec
2. Map each endpoint to an MCP tool
3. Convert API request params → Zod/JSON schema
4. Convert API response → MCP text content
5. Handle auth (API key, OAuth) via environment variables

### Pattern 2: GraphQL → MCP

```typescript
// One flexible tool that wraps a GraphQL endpoint
server.tool("graphql_query", "Query the product catalog via GraphQL", {
  query: z.string().describe("GraphQL query string"),
  variables: z.record(z.any()).optional().describe("Query variables")
}, async ({ query, variables }) => {
  const res = await fetch(GRAPHQL_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json", "Authorization": `Bearer ${TOKEN}` },
    body: JSON.stringify({ query, variables })
  });
  const { data, errors } = await res.json();
  if (errors) return { content: [{ type: "text", text: `Errors: ${JSON.stringify(errors)}` }], isError: true };
  return { content: [{ type: "text", text: JSON.stringify(data, null, 2) }] };
});
```

### Pattern 3: Multi-API Aggregator

One MCP server that fans out to multiple APIs for a unified view:

```typescript
server.tool("customer_360", "Get complete customer profile from CRM + Orders + Support", {
  customerId: z.string()
}, async ({ customerId }) => {
  const [profile, orders, tickets] = await Promise.all([
    fetch(`${CRM_URL}/customers/${customerId}`).then(r => r.json()),
    fetch(`${ORDER_URL}/orders?customer=${customerId}`).then(r => r.json()),
    fetch(`${SUPPORT_URL}/tickets?customer=${customerId}`).then(r => r.json())
  ]);
  return {
    content: [{
      type: "text",
      text: JSON.stringify({ profile, recentOrders: orders.slice(0, 5), openTickets: tickets.filter(t => t.status === "open") }, null, 2)
    }]
  };
});
```

### Pattern 4: gRPC → MCP Bridge

```typescript
import * as grpc from "@grpc/grpc-js";
import * as protoLoader from "@grpc/proto-loader";

// Load protobuf definition and create gRPC client
const packageDef = protoLoader.loadSync("./protos/inventory.proto");
const proto = grpc.loadPackageDefinition(packageDef);
const grpcClient = new proto.inventory.InventoryService(GRPC_HOST, grpc.credentials.createInsecure());

server.tool("grpc_search", "Search inventory via gRPC service", {
  query: z.string(), limit: z.number().default(20)
}, async ({ query, limit }) => {
  return new Promise((resolve) => {
    grpcClient.search({ query, limit }, (err, response) => {
      if (err) resolve({ content: [{ type: "text", text: `gRPC error: ${err.message}` }], isError: true });
      else resolve({ content: [{ type: "text", text: JSON.stringify(response, null, 2) }] });
    });
  });
});
```

### Pattern 5: WebSocket / Real-time Data → MCP

```typescript
// MCP tool that subscribes to real-time data and returns latest state
import WebSocket from "ws";

let latestPrices = {};
const ws = new WebSocket("wss://stream.exchange.com/prices");
ws.on("message", (data) => { latestPrices = JSON.parse(data.toString()); });

server.tool("get_live_prices", "Get latest real-time stock/crypto prices", {
  symbols: z.array(z.string()).describe("Ticker symbols")
}, async ({ symbols }) => {
  const filtered = Object.fromEntries(symbols.map(s => [s, latestPrices[s] || "N/A"]));
  return { content: [{ type: "text", text: JSON.stringify(filtered, null, 2) }] };
});
```

### Pattern 6: OpenAPI Spec → Auto-Generated MCP Server

Automatically generate an MCP server from any OpenAPI/Swagger specification:

```typescript
import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { parse } from "yaml";
import { z } from "zod";

async function generateFromOpenAPI(specUrl: string, baseUrl: string, apiKey?: string) {
  const specText = await (await fetch(specUrl)).text();
  const spec = parse(specText);
  const server = new McpServer({ name: spec.info.title, version: spec.info.version });
  
  for (const [path, methods] of Object.entries(spec.paths)) {
    for (const [httpMethod, operation] of Object.entries(methods)) {
      const toolName = operation.operationId;
      if (!toolName) continue;
      
      // Convert OpenAPI parameters to Zod schema
      const schemaProps = {};
      for (const param of operation.parameters || []) {
        schemaProps[param.name] = param.required
          ? z.string().describe(param.description || param.name)
          : z.string().optional().describe(param.description || param.name);
      }
      
      server.tool(toolName, operation.summary || operation.description || toolName, schemaProps,
        async (args) => {
          let url = `${baseUrl}${path}`;
          // Substitute path params
          for (const [key, val] of Object.entries(args)) {
            url = url.replace(`{${key}}`, encodeURIComponent(val));
          }
          const headers = { "Content-Type": "application/json" };
          if (apiKey) headers["Authorization"] = `Bearer ${apiKey}`;
          
          const res = await fetch(url, { method: httpMethod.toUpperCase(), headers });
          const data = await res.json();
          return { content: [{ type: "text", text: JSON.stringify(data, null, 2) }] };
        }
      );
    }
  }
  return server;
}
```

---

## Agent Architecture with MCP

### Single Agent + Multiple MCP Servers

The simplest and most common pattern. One AI agent (e.g., VS Code Copilot in agent mode) connects to multiple MCP servers.

```
.vscode/mcp.json → configure all servers
Agent mode → AI decides which tools to use based on the task
```

**Best for:** Most development workflows. Copilot's agent mode handles tool selection automatically.

### Building a Custom Agent (MCP Client + LLM API)

For custom applications that go beyond IDE integration:

```typescript
// Complete custom agent implementation
import { Client } from "@modelcontextprotocol/sdk/client/index.js";
import { StdioClientTransport } from "@modelcontextprotocol/sdk/client/stdio.js";
import Anthropic from "@anthropic-ai/sdk";

class CustomAgent {
  private mcpClients: Map<string, Client> = new Map();
  private anthropic = new Anthropic();
  
  async connectMcpServer(name: string, command: string, args: string[]) {
    const transport = new StdioClientTransport({ command, args });
    const client = new Client({ name: "custom-agent", version: "1.0.0" });
    await client.connect(transport);
    this.mcpClients.set(name, client);
    console.log(`Connected to ${name}, tools: ${(await client.listTools()).tools.map(t => t.name).join(", ")}`);
  }
  
  async run(userMessage: string): Promise<string> {
    // Collect all tools from all servers
    const allTools = [];
    const toolServerMap = new Map();
    
    for (const [serverName, client] of this.mcpClients) {
      const { tools } = await client.listTools();
      for (const tool of tools) {
        allTools.push({
          name: tool.name,
          description: tool.description || "",
          input_schema: tool.inputSchema
        });
        toolServerMap.set(tool.name, serverName);
      }
    }
    
    // Agent loop
    const messages = [{ role: "user" as const, content: userMessage }];
    
    while (true) {
      const response = await this.anthropic.messages.create({
        model: "claude-sonnet-4-20250514",
        max_tokens: 4096,
        tools: allTools,
        messages
      });
      
      // If no tool use, return the text response
      if (response.stop_reason === "end_turn") {
        return response.content
          .filter(b => b.type === "text")
          .map(b => b.text)
          .join("\n");
      }
      
      // Process tool calls
      messages.push({ role: "assistant", content: response.content });
      const toolResults = [];
      
      for (const block of response.content) {
        if (block.type !== "tool_use") continue;
        
        const serverName = toolServerMap.get(block.name);
        const client = this.mcpClients.get(serverName);
        
        try {
          const result = await client.callTool({ name: block.name, arguments: block.input });
          toolResults.push({
            type: "tool_result" as const,
            tool_use_id: block.id,
            content: result.content.map(c => ({ type: "text" as const, text: c.text || JSON.stringify(c) }))
          });
        } catch (error) {
          toolResults.push({
            type: "tool_result" as const,
            tool_use_id: block.id,
            content: [{ type: "text" as const, text: `Error: ${error.message}` }],
            is_error: true
          });
        }
      }
      
      messages.push({ role: "user", content: toolResults });
    }
  }
  
  async shutdown() {
    for (const client of this.mcpClients.values()) {
      await client.close();
    }
  }
}

// Usage
const agent = new CustomAgent();
await agent.connectMcpServer("github", "npx", ["-y", "@modelcontextprotocol/server-github"]);
await agent.connectMcpServer("db", "node", ["./db-server/dist/index.js"]);

const answer = await agent.run("Find all open issues labeled 'bug', check the database for related error logs, and summarize the findings.");
console.log(answer);
await agent.shutdown();
```

### Multi-Agent System with MCP

```
┌─────────────────────────────────────────────────┐
│              ORCHESTRATOR AGENT                  │
│  (Receives user request, delegates to agents)    │
└────────┬──────────────┬──────────────┬──────────┘
         │              │              │
    ┌────▼────┐   ┌────▼─────┐  ┌────▼─────┐
    │Research │   │Code      │  │Deploy    │
    │Agent    │   │Agent     │  │Agent     │
    │         │   │          │  │          │
    │MCP:     │   │MCP:      │  │MCP:      │
    │- Search │   │- GitHub  │  │- K8s     │
    │- Fetch  │   │- FileSystem│ │- Docker  │
    │- Memory │   │- Tests   │  │- AWS     │
    └─────────┘   └──────────┘  └──────────┘
```

**Implementation approach:**
1. Each agent is a separate MCP client connected to domain-specific servers
2. The orchestrator agent uses an LLM to decompose tasks and route to sub-agents
3. Sub-agents return results to the orchestrator for synthesis
4. The orchestrator presents the unified result to the user

---

## Types of MCP Servers — Catalog

### Data Access Servers

| Server | Language | What It Does |
|---|---|---|
| `@modelcontextprotocol/server-postgres` | TS | PostgreSQL queries, schema inspection |
| `@modelcontextprotocol/server-sqlite` | TS | SQLite database operations |
| `@modelcontextprotocol/server-filesystem` | TS | Secure file read/write/search |
| `@modelcontextprotocol/server-google-drive` | TS | Google Drive file access |
| `mcp-server-redis` | Python | Redis cache operations |
| Custom | Any | Wrap any database via JDBC/ODBC/ORM |

### Developer Tool Servers

| Server | Language | What It Does |
|---|---|---|
| `@modelcontextprotocol/server-github` | TS | GitHub repos, issues, PRs, actions |
| `@modelcontextprotocol/server-gitlab` | TS | GitLab projects, MRs, pipelines |
| `@modelcontextprotocol/server-git` | TS | Local git operations |
| `mcp-server-docker` | Python | Docker container management |
| `mcp-server-kubernetes` | Go | kubectl wrapper, cluster ops |

### Knowledge & Search Servers

| Server | Language | What It Does |
|---|---|---|
| `@modelcontextprotocol/server-brave-search` | TS | Web search via Brave API |
| `@modelcontextprotocol/server-puppeteer` | TS | Browser automation, web scraping |
| `@modelcontextprotocol/server-memory` | TS | Persistent knowledge graph |
| `mcp-server-fetch` | Python | Generic HTTP fetching |
| `mcp-server-rag` | Python | Vector search, RAG pipeline |

### Productivity & Communication Servers

| Server | Language | What It Does |
|---|---|---|
| `@modelcontextprotocol/server-slack` | TS | Slack messaging, channels |
| `@modelcontextprotocol/server-google-maps` | TS | Maps, geocoding, directions |
| `mcp-server-notion` | TS | Notion pages, databases |
| `mcp-server-linear` | TS | Linear issue tracking |
| Custom email MCP | Any | SMTP/IMAP email operations |

### Infrastructure & Cloud Servers

| Server | Language | What It Does |
|---|---|---|
| `mcp-server-aws` | Python | AWS service operations |
| `mcp-server-cloudflare` | TS | Workers, DNS, analytics |
| `mcp-server-terraform` | Go | Terraform plan/apply |
| `mcp-server-vercel` | TS | Deployments, domains |

---

## Security Best Practices

### Authentication & Authorization

```
Security Layers
│
├── Transport Security
│   ├── Local (stdio): Inherently secure — same machine, no network
│   ├── Remote (HTTP): Always use HTTPS/TLS
│   └── Auth: OAuth 2.0, API keys, or mutual TLS for remote servers
│
├── Input Validation
│   ├── ALWAYS validate AI-provided inputs (AI can hallucinate)
│   ├── Use schema validation (Zod, JSON Schema) as first defense
│   ├── Apply business logic validation in handlers
│   ├── Sanitize inputs that touch databases (prevent SQL injection)
│   └── Limit string lengths, number ranges, array sizes
│
├── Least Privilege
│   ├── Expose only the tools needed for the use case
│   ├── Separate read-only and read-write tools
│   ├── Use database users with minimal permissions
│   ├── Restrict file system access to specific directories
│   └── Limit API scopes in OAuth tokens
│
├── Secrets Management
│   ├── Use environment variables for all credentials
│   ├── Use ${input:...} in VS Code config to prompt users
│   ├── Never hardcode secrets in server code
│   ├── Support secret rotation without server restart
│   └── Consider vault integration (HashiCorp Vault, AWS Secrets Manager)
│
├── Sandboxing
│   ├── Run servers in Docker containers
│   ├── Use read-only file system mounts where possible
│   ├── Apply network policies (restrict outbound)
│   ├── Set resource limits (CPU, memory, timeout)
│   └── Use OS-level sandboxing (seccomp, AppArmor)
│
└── Audit & Monitoring
    ├── Log every tool invocation with context
    ├── Track user/session that triggered calls
    ├── Alert on unusual patterns (many calls, errors, sensitive operations)
    ├── Retain logs for compliance (if applicable)
    └── Redact sensitive data in logs
```

### Dangerous Operations Pattern

For tools that perform destructive or sensitive operations:

```typescript
server.tool(
  "delete_records",
  "Delete records from a table (DANGEROUS — requires confirmation)",
  {
    table: z.string(),
    condition: z.string().describe("WHERE clause for deletion"),
    confirm: z.literal("yes-delete").describe("Must be exactly 'yes-delete' to proceed")
  },
  async ({ table, condition, confirm }) => {
    // Defense in depth
    if (confirm !== "yes-delete") {
      return { content: [{ type: "text", text: "Deletion NOT performed. Set confirm='yes-delete' to proceed." }] };
    }
    
    // Audit log
    console.error(`[AUDIT] DELETE FROM ${table} WHERE ${condition} — confirmed`);
    
    const result = await db.execute(`DELETE FROM ${table} WHERE ${condition}`);
    return {
      content: [{ type: "text", text: `Deleted ${result.rowCount} rows from ${table}` }]
    };
  }
);
```

---

## Testing MCP Servers

### MCP Inspector (Official Testing Tool)

```bash
# TypeScript server
npx @modelcontextprotocol/inspector node dist/index.js

# Python server
npx @modelcontextprotocol/inspector python server.py

# With environment variables
API_KEY=test123 npx @modelcontextprotocol/inspector node dist/index.js

# Python alternative
mcp dev server.py
```

The Inspector opens a web UI where you can:
- See all registered tools, resources, and prompts
- Call tools with custom inputs and see results
- Read resources and inspect their content
- Test error handling with invalid inputs
- Monitor JSON-RPC messages in real-time

### Unit Testing

```typescript
// TypeScript unit test example (Vitest/Jest)
import { describe, it, expect } from "vitest";

describe("search_items tool", () => {
  it("returns results for valid query", async () => {
    const result = await searchItemsHandler({
      query: "laptop",
      category: "electronics",
      limit: 5
    });
    
    expect(result.isError).toBeFalsy();
    const data = JSON.parse(result.content[0].text);
    expect(data.items.length).toBeLessThanOrEqual(5);
    expect(data.query).toBe("laptop");
  });
  
  it("handles empty results gracefully", async () => {
    const result = await searchItemsHandler({
      query: "nonexistent-item-xyz",
      category: "all",
      limit: 10
    });
    
    expect(result.isError).toBeFalsy();
    const data = JSON.parse(result.content[0].text);
    expect(data.items).toHaveLength(0);
  });
  
  it("returns error for invalid input", async () => {
    const result = await searchItemsHandler({
      query: "",  // empty query
      limit: -1   // invalid limit
    });
    
    expect(result.isError).toBe(true);
  });
});
```

### Integration Testing

```typescript
// Full integration test — spin up server + connect client
import { Client } from "@modelcontextprotocol/sdk/client/index.js";
import { StdioClientTransport } from "@modelcontextprotocol/sdk/client/stdio.js";

describe("MCP Server Integration", () => {
  let client: Client;
  
  beforeAll(async () => {
    const transport = new StdioClientTransport({
      command: "node",
      args: ["dist/index.js"]
    });
    client = new Client({ name: "test-client", version: "1.0.0" });
    await client.connect(transport);
  });
  
  afterAll(async () => {
    await client.close();
  });
  
  it("lists tools correctly", async () => {
    const { tools } = await client.listTools();
    expect(tools.map(t => t.name)).toContain("search_items");
    expect(tools.map(t => t.name)).toContain("create_item");
  });
  
  it("calls tool and returns valid response", async () => {
    const result = await client.callTool({
      name: "search_items",
      arguments: { query: "test", limit: 5 }
    });
    expect(result.content).toBeDefined();
    expect(result.content[0].type).toBe("text");
  });
  
  it("lists resources correctly", async () => {
    const { resources } = await client.listResources();
    expect(resources.map(r => r.uri)).toContain("inventory://schema");
  });
});
```

---

## Deployment Strategies

### Local Development (stdio)
- Server runs as child process of the AI client
- Best for personal tools, development, experimentation
- Zero network configuration

### Docker Container
```dockerfile
FROM node:22-slim
WORKDIR /app
COPY package*.json ./
RUN npm ci --production
COPY dist/ ./dist/
ENV NODE_ENV=production
ENTRYPOINT ["node", "dist/index.js"]
```

```jsonc
// .vscode/mcp.json — Docker-based server
{
  "servers": {
    "my-server": {
      "type": "stdio",
      "command": "docker",
      "args": ["run", "-i", "--rm", "-e", "API_KEY=${input:apiKey}", "my-mcp-server:latest"]
    }
  }
}
```

### Cloud / Serverless (HTTP transport)
- Deploy as HTTP service (AWS Lambda, Cloud Run, Fly.io, Railway)
- Use Streamable HTTP transport
- Add authentication (API key, OAuth)
- Multiple clients can connect to same server instance

### npm / PyPI Distribution
```bash
# Publish TypeScript server
npm publish  # Users run: npx -y your-package-name

# Publish Python server
uv build && uv publish  # Users run: uvx your-package-name
```

---

## Multi-MCP Project Structure

When building multiple MCP servers in a single project, keep each server as an **independent, self-contained** subdirectory under a shared `mcp-servers/` folder.

### Recommended Directory Layout

```
your-project/
├── .github/                     ← Copilot customization (unchanged)
├── .vscode/
│   └── mcp.json                 ← Register ALL your MCPs here (one file)
├── src/                          ← Your main application code
│
├── mcp-servers/                  ← All MCP servers live here
│   │
│   ├── weather-mcp/              ← MCP #1 (TypeScript)
│   │   ├── package.json          ← Own dependencies
│   │   ├── tsconfig.json
│   │   ├── src/
│   │   │   └── index.ts
│   │   ├── dist/                 ← Build output (gitignored)
│   │   │   └── index.js
│   │   └── README.md             ← What this MCP does, how to build/test
│   │
│   ├── db-mcp/                   ← MCP #2 (Python)
│   │   ├── pyproject.toml        ← Own dependencies
│   │   ├── server.py
│   │   └── README.md
│   │
│   ├── github-tools-mcp/         ← MCP #3 (TypeScript)
│   │   ├── package.json
│   │   ├── src/
│   │   │   └── index.ts
│   │   └── README.md
│   │
│   └── inventory-mcp/            ← MCP #4 (Java — Spring Boot)
│       ├── pom.xml
│       ├── src/main/java/...
│       └── README.md
│
└── README.md
```

### Why This Structure

| Concern | Solution |
|---|---|
| **Independence** | Each MCP has its own `package.json` / `pyproject.toml` / `pom.xml` — no dependency conflicts |
| **Mixed languages** | TypeScript, Python, Java, Go MCPs all coexist in the same repo |
| **Single config** | One `.vscode/mcp.json` registers all MCPs — Copilot sees them all |
| **Easy to add/remove** | Add a new folder under `mcp-servers/`, no restructuring |
| **Clean builds** | Each MCP builds independently, `dist/` and `node_modules/` are gitignored |

### When to Split vs. Combine

| Situation | One MCP or Many? |
|---|---|
| Tools share the same data source (e.g., same DB) | **One MCP** with multiple tools |
| Tools are in completely different domains (weather vs Git) | **Separate MCPs** |
| You want to reuse an MCP across projects | **Separate MCP** (publish to npm/PyPI) |
| Tools need different auth/secrets | **Separate MCPs** (cleaner secret isolation) |
| Building a learning example per topic | **Separate MCPs** (easier to understand) |

### .gitignore Additions for MCP Servers

```gitignore
# MCP server build artifacts
mcp-servers/*/node_modules/
mcp-servers/*/dist/
mcp-servers/*/.venv/
mcp-servers/*/target/
mcp-servers/*/__pycache__/
mcp-servers/*/*.egg-info/
```

---

## `.vscode/mcp.json` — Complete Schema Reference

The `mcp.json` file is the **single configuration file** that registers all MCP servers for VS Code / Copilot. Place it at `.vscode/mcp.json` (workspace-level) so it's shared with the project.

### Full JSON Schema

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

### Variable Substitution in mcp.json

| Variable | Resolves To | Example |
|---|---|---|
| `${workspaceFolder}` | Absolute path to workspace root | `E:\projects\my-app` |
| `${input:variableName}` | User-prompted value (secure) | API keys, tokens, passwords |
| `${env:VARIABLE_NAME}` | System environment variable | `${env:HOME}`, `${env:PATH}` |
| `${userHome}` | User's home directory | `C:\Users\saharsh` |

### Complete Multi-MCP mcp.json Example

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

### User-Level mcp.json (Global, All Workspaces)

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

### Claude Desktop Configuration

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

## Workflow: Creating a New MCP Server (Step-by-Step)

### TypeScript MCP — From Zero to Working

```bash
# 1. Create directory
cd mcp-servers
mkdir weather-mcp && cd weather-mcp

# 2. Initialize project
npm init -y
npm install @modelcontextprotocol/sdk zod
npm install -D typescript @types/node

# 3. Configure TypeScript
npx tsc --init --target es2022 --module nodenext --moduleResolution nodenext --outDir dist --rootDir src

# 4. Add to package.json
# "type": "module"
# "scripts": { "build": "tsc", "start": "node dist/index.js", "inspect": "npx @modelcontextprotocol/inspector node dist/index.js" }
# "bin": { "weather-mcp": "dist/index.js" }

# 5. Create src/index.ts (see templates in this guide)
mkdir src
# ... write your server code ...

# 6. Build
npm run build

# 7. Test with Inspector (opens web UI)
npm run inspect
# OR: npx @modelcontextprotocol/inspector node dist/index.js

# 8. Register in .vscode/mcp.json (add to "servers" object)
# 9. Restart VS Code / reload MCP servers
# 10. Use in Copilot agent mode — your tools appear automatically
```

### Python MCP — From Zero to Working

```bash
# 1. Create directory
cd mcp-servers
mkdir db-mcp && cd db-mcp

# 2. Initialize project (using uv — recommended)
uv init
uv add "mcp[cli]"

# OR using pip:
pip install "mcp[cli]"

# 3. Create server.py (see templates in this guide)
# ... write your server code ...

# 4. Test with Inspector
mcp dev server.py
# OR: npx @modelcontextprotocol/inspector python server.py

# 5. Register in .vscode/mcp.json
# 6. Use in Copilot agent mode
```

### Java MCP — From Zero to Working

```bash
# 1. Create Spring Boot project
cd mcp-servers
mkdir inventory-mcp && cd inventory-mcp

# 2. Use Spring Initializr or create pom.xml with:
#    - io.modelcontextprotocol:mcp-spring-webflux
#    - org.springframework.ai:spring-ai-mcp-server-spring-boot-starter

# 3. Implement your @Tool methods (see templates in this guide)

# 4. Build
mvn clean package -DskipTests

# 5. Test
java -jar target/inventory-mcp.jar
# Use MCP Inspector: npx @modelcontextprotocol/inspector java -jar target/inventory-mcp.jar

# 6. Register in .vscode/mcp.json with:
#    "command": "java", "args": ["-jar", "${workspaceFolder}/mcp-servers/inventory-mcp/target/inventory-mcp.jar"]
```

---

## Ecosystem & Community

### MCP Server Registries
- **mcp.so** — Community MCP server registry
- **Smithery** — MCP server marketplace
- **GitHub Topics** — Search `mcp-server` on GitHub
- **Awesome MCP Servers** — Curated list on GitHub

### Official Resources
- **Specification:** https://spec.modelcontextprotocol.io
- **Documentation:** https://modelcontextprotocol.io
- **TypeScript SDK:** https://github.com/modelcontextprotocol/typescript-sdk
- **Python SDK:** https://github.com/modelcontextprotocol/python-sdk
- **Java SDK:** https://github.com/modelcontextprotocol/java-sdk
- **Inspector:** https://github.com/modelcontextprotocol/inspector
- **Official Servers:** https://github.com/modelcontextprotocol/servers

### Learning Path

```
MCP Learning Path
│
├── Level 1: CONSUMER (use existing MCPs)
│   ├── Configure GitHub MCP in VS Code
│   ├── Add filesystem MCP for project access
│   ├── Try Brave Search MCP for web research
│   └── Understand how Copilot agent mode uses tools
│
├── Level 2: BUILDER (create simple MCPs)
│   ├── Build a "Hello World" MCP server (TypeScript or Python)
│   ├── Wrap a single REST API as MCP tools
│   ├── Test with MCP Inspector
│   └── Configure in VS Code and use with Copilot
│
├── Level 3: INTEGRATOR (complex MCPs + APIs)
│   ├── Build multi-API aggregator MCP
│   ├── Add resources and prompts alongside tools
│   ├── Implement proper error handling and validation
│   ├── Dockerize and deploy
│   └── Publish to npm/PyPI
│
├── Level 4: ARCHITECT (agent systems)
│   ├── Build custom agent with MCP client + LLM API
│   ├── Design multi-agent systems with specialized MCPs
│   ├── Implement tool chaining and orchestration
│   ├── Add human-in-the-loop approval flows
│   └── Production security, monitoring, and scaling
│
└── Level 5: CONTRIBUTOR (protocol & ecosystem)
    ├── Contribute to MCP specification
    ├── Build SDK plugins or extensions
    ├── Create MCP server templates and generators
    └── Write MCP middleware (logging, auth, caching)
```

---

## Quick Reference — MCP Cheat Sheet

### Tool Registration Pattern
```
server.tool(name, description, inputSchema, handler)
→ name: lowercase-kebab-case, verb-first (search_items, create_user)
→ description: One sentence explaining WHEN to use this tool
→ inputSchema: Zod (TS), type hints (Python), JSON Schema (Java)
→ handler: async function returning { content: [...], isError?: boolean }
```

### Resource Registration Pattern
```
server.resource(name, uri, description, handler)
→ uri: scheme://path (e.g., db://myapp/schema)
→ Dynamic: use {param} in URI template
→ handler: returns { contents: [{ uri, mimeType, text }] }
```

### Prompt Registration Pattern
```
server.prompt(name, description, paramSchema, handler)
→ handler: returns { messages: [{ role, content }] }
→ User selects prompts explicitly (slash commands, menus)
```

### Debugging Commands
```bash
# Test with Inspector
npx @modelcontextprotocol/inspector <command> <args>

# Enable debug logging
MCP_DEBUG=1 node dist/index.js

# VS Code: Check MCP output
Output panel → "MCP" channel

# List registered tools in a running server
Send: { "method": "tools/list", "id": 1 }
```

### Common Pitfalls
| Issue | Cause | Fix |
|---|---|---|
| "Server not found" | Wrong command path | Use absolute paths or verify `npx`/`node` is in PATH |
| "Tool not recognized" | Tool name mismatch | Names are exact-match, case-sensitive |
| "Invalid params" | Schema mismatch | Check Zod schema matches what AI sends |
| Server hangs | Blocking I/O on main thread | Use async handlers, add timeouts |
| No output | Writing to stdout | Use stderr for logs; stdout is reserved for JSON-RPC |
| Auth failures | Missing env vars | Check `env` in mcp.json, use `${input:...}` for secrets |
