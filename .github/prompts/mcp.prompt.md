```prompt
---
name: mcp
description: 'Learn MCP (Model Context Protocol) — build custom MCP servers, configure agents with tools, combine with APIs, and master the protocol for agentic AI workflows'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Topic
${input:topic:What MCP topic? (overview / build-server / configure-agent / types-of-mcp / api-integration / agent-patterns / protocol-spec / troubleshoot / real-world-examples)}

## Goal
${input:goal:What's your goal? (learn-concept / build-my-own-mcp / configure-vscode / agent-architecture / compare-approaches / interview-prep)}

## Current Level
${input:level:Your experience level? (beginner / know-the-basics / intermediate / advanced)}

## Preferred Language
${input:language:What language for MCP server examples? (typescript / python / java / go / csharp / any)}

## Instructions

Teach or guide the user on MCP (Model Context Protocol). Adapt your response based on the topic and goal. Use the MCP Development skill for comprehensive reference material.

### What is MCP?

MCP (Model Context Protocol) is an **open standard** (created by Anthropic, adopted across the industry) that defines how AI assistants (clients) communicate with external tools and data sources (servers). Think of it as **USB-C for AI** — a universal plug-and-play protocol that lets any AI model connect to any tool.

```
Without MCP:                          With MCP:
┌──────┐    custom API    ┌──────┐    ┌──────┐   standard    ┌──────┐
│ AI   │───────────────→  │Tool A│    │ AI   │   protocol    │MCP   │
│Model │    custom API    ├──────┤    │Model │◄────────────► │Server│
│      │───────────────→  │Tool B│    │(MCP  │   (JSON-RPC)  │  A   │
│      │    custom API    ├──────┤    │Client│               ├──────┤
│      │───────────────→  │Tool C│    │  )   │◄────────────► │MCP   │
└──────┘  N integrations  └──────┘    └──────┘  same protocol │Server│
                                                              │  B   │
                                                              └──────┘
```

### MCP Domain Map

```
MCP (Model Context Protocol)
│
├── Core Concepts
│   ├── Protocol Architecture ···· Client-server, JSON-RPC 2.0, capabilities
│   ├── MCP Primitives ·········· Tools, Resources, Prompts (the 3 pillars)
│   ├── Transport Layer ·········· stdio, SSE (HTTP), Streamable HTTP
│   └── Lifecycle ················ Initialize → Negotiate → Operate → Shutdown
│
├── MCP Server Types (by purpose)
│   ├── Data Access ·············· Database, file system, API gateway
│   ├── Developer Tools ·········· Git, build tools, linting, testing
│   ├── Productivity ············· Email, calendar, task management
│   ├── Knowledge ················ Documentation, search, web scraping
│   ├── Infrastructure ··········· Cloud providers, monitoring, deployment
│   ├── Communication ············ Slack, Discord, Teams, notifications
│   └── Custom / Domain ·········· Your own business logic, internal APIs
│
├── Building MCP Servers
│   ├── TypeScript SDK ··········· @modelcontextprotocol/sdk (official, most mature)
│   ├── Python SDK ··············· mcp (official Python package)
│   ├── Java SDK ················· io.modelcontextprotocol (Spring AI integration)
│   ├── Go SDK ··················· github.com/mark3labs/mcp-go
│   ├── C# SDK ··················· ModelContextProtocol (NuGet)
│   └── Server Patterns ·········· Stateless, stateful, proxy, aggregator
│
├── Configuring MCP
│   ├── VS Code / Copilot ········ .vscode/mcp.json, settings.json
│   ├── Claude Desktop ··········· claude_desktop_config.json
│   ├── Cursor ··················· cursor settings
│   └── Other Clients ··········· Windsurf, Continue, Cline, custom
│
├── Agent Architecture with MCP
│   ├── Single Agent + Tools ····· One agent, multiple MCP servers
│   ├── Multi-Agent Systems ······ Agents delegating to specialized agents
│   ├── Tool Orchestration ······· Chaining tools, conditional routing
│   ├── Human-in-the-Loop ······· Approval gates, confirmation prompts
│   └── Agent + API Composition ·· MCP wrapping REST/GraphQL/gRPC APIs
│
└── Advanced Topics
    ├── Security ·················· Auth, secrets, sandboxing, trust boundaries
    ├── Testing MCP Servers ······· Inspector tool, unit tests, integration tests
    ├── Performance ··············· Connection pooling, caching, batching
    ├── Deployment ················ Local, Docker, cloud, serverless
    └── Ecosystem ················· MCP registries, community servers, standards
```

### Protocol Deep-Dive

When `topic` is **overview** or **protocol-spec**:

#### The Three MCP Primitives

```
MCP Primitives — What a Server Can Expose
│
├── 1. TOOLS (Model-controlled)
│   │   Functions the AI can invoke to perform actions
│   │
│   ├── What: Executable functions with typed input/output schemas
│   ├── Who decides: The AI MODEL decides when to call them
│   ├── Examples:
│   │   ├── query_database(sql) → results
│   │   ├── create_github_issue(title, body) → issue URL
│   │   ├── send_email(to, subject, body) → status
│   │   └── run_test(file_path) → pass/fail + output
│   └── Analogy: Tools in a workshop — AI picks the right one for the job
│
├── 2. RESOURCES (Application-controlled)
│   │   Data/context the client application can read
│   │
│   ├── What: URI-addressable data (text, JSON, binary)
│   ├── Who decides: The CLIENT APPLICATION decides what to load
│   ├── Examples:
│   │   ├── file://project/README.md → file contents
│   │   ├── db://users/schema → database schema
│   │   ├── api://weather/current → weather data
│   │   └── log://app/errors/today → today's error logs
│   └── Analogy: Reference books on a shelf — the app picks which to consult
│
└── 3. PROMPTS (User-controlled)
    │   Pre-built prompt templates the user can select
    │
    ├── What: Parameterized prompt templates
    ├── Who decides: The USER selects them explicitly
    ├── Examples:
    │   ├── review_code(language, code) → review prompt
    │   ├── explain_error(error_message) → explanation prompt
    │   └── generate_tests(function_name) → test generation prompt
    └── Analogy: Recipe cards — user picks which recipe to follow
```

#### Transport Mechanisms

```
Transport Options
│
├── stdio (Standard I/O)
│   ├── How: Client spawns server as child process, communicates via stdin/stdout
│   ├── Best for: Local tools, CLI-based servers, development
│   ├── Pros: Simple, no network setup, secure (local only)
│   ├── Cons: Must be on same machine, one client per server instance
│   └── Config: "command": "node", "args": ["server.js"]
│
├── SSE (Server-Sent Events) — Legacy
│   ├── How: HTTP POST for client→server, SSE stream for server→client
│   ├── Best for: Remote servers, shared infrastructure
│   ├── Pros: Works over network, multiple clients, standard HTTP
│   ├── Cons: Two channels (POST + SSE), being superseded
│   └── Config: "url": "http://localhost:3001/sse"
│
└── Streamable HTTP (Recommended for remote)
    ├── How: Single HTTP endpoint, supports streaming responses
    ├── Best for: Production remote servers, cloud deployment
    ├── Pros: Simpler than SSE, single endpoint, supports streaming
    ├── Cons: Newer, less tooling support currently
    └── Config: "url": "http://localhost:3001/mcp"
```

#### Protocol Lifecycle

```
MCP Session Lifecycle
│
├── 1. INITIALIZE
│   Client sends: { method: "initialize", params: { capabilities, clientInfo } }
│   Server responds: { capabilities, serverInfo, protocolVersion }
│   → Capabilities negotiation (what features each side supports)
│
├── 2. INITIALIZED (notification)
│   Client sends: { method: "notifications/initialized" }
│   → Server is now ready to handle requests
│
├── 3. OPERATE (main loop)
│   ├── Client → Server:
│   │   ├── tools/list → get available tools
│   │   ├── tools/call → invoke a tool
│   │   ├── resources/list → list available resources
│   │   ├── resources/read → read a resource
│   │   ├── prompts/list → list available prompts
│   │   └── prompts/get → get a prompt template
│   │
│   └── Server → Client (notifications):
│       ├── notifications/tools/list_changed → tools updated
│       ├── notifications/resources/list_changed → resources updated
│       └── notifications/progress → operation progress
│
└── 4. SHUTDOWN
    Client sends: close notification or drops connection
    Server cleans up resources
```

### Building MCP Servers — By Language

When `topic` is **build-server**:

Provide complete, working examples based on the user's chosen language. Always include:

#### TypeScript MCP Server (Complete Template)

```typescript
// package.json dependencies: @modelcontextprotocol/sdk
import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { z } from "zod";

// 1. Create server with metadata
const server = new McpServer({
  name: "my-custom-server",
  version: "1.0.0",
  description: "A custom MCP server"
});

// 2. Register TOOLS (AI-invokable functions)
server.tool(
  "search_database",                    // tool name
  "Search records in the database",     // description (helps AI decide when to use it)
  {                                     // input schema (Zod validation)
    query: z.string().describe("SQL-like search query"),
    limit: z.number().optional().default(10).describe("Max results to return")
  },
  async ({ query, limit }) => {         // handler function
    // Your business logic here
    const results = await db.search(query, limit);
    return {
      content: [{ type: "text", text: JSON.stringify(results, null, 2) }]
    };
  }
);

// 3. Register RESOURCES (context/data the client can read)
server.resource(
  "schema",                             // resource name
  "db://myapp/schema",                  // URI template
  "Database schema for reference",      // description
  async (uri) => ({
    contents: [{
      uri: uri.href,
      mimeType: "application/json",
      text: JSON.stringify(await db.getSchema())
    }]
  })
);

// 4. Register PROMPTS (user-selectable templates)
server.prompt(
  "analyze_table",                      // prompt name
  "Generate analysis prompt for a database table",
  { tableName: z.string() },           // parameters
  async ({ tableName }) => ({
    messages: [{
      role: "user",
      content: {
        type: "text",
        text: `Analyze the '${tableName}' table: structure, indexes, common queries, and optimization suggestions.`
      }
    }]
  })
);

// 5. Start the server
const transport = new StdioServerTransport();
await server.connect(transport);
```

#### Python MCP Server (Complete Template)

```python
# pip install mcp[cli]
from mcp.server.fastmcp import FastMCP
from typing import Any

# 1. Create server
mcp = FastMCP(
    name="my-custom-server",
    version="1.0.0"
)

# 2. Register TOOLS
@mcp.tool()
async def search_database(query: str, limit: int = 10) -> str:
    """Search records in the database.
    
    Args:
        query: SQL-like search query
        limit: Max results to return
    """
    results = await db.search(query, limit)
    return json.dumps(results, indent=2)

# 3. Register RESOURCES
@mcp.resource("db://myapp/schema")
async def get_schema() -> str:
    """Database schema for reference."""
    return json.dumps(await db.get_schema())

# Dynamic resources with URI templates
@mcp.resource("db://myapp/table/{table_name}")
async def get_table_info(table_name: str) -> str:
    """Get info about a specific table."""
    return json.dumps(await db.table_info(table_name))

# 4. Register PROMPTS
@mcp.prompt()
async def analyze_table(table_name: str) -> str:
    """Generate analysis prompt for a database table."""
    return f"Analyze the '{table_name}' table: structure, indexes, common queries, and optimization suggestions."

# 5. Run server
if __name__ == "__main__":
    mcp.run()  # defaults to stdio transport
    # For SSE: mcp.run(transport="sse")
```

#### Java MCP Server (Complete Template)

```java
// Maven: io.modelcontextprotocol:mcp-spring-webflux
// or: io.modelcontextprotocol:mcp (standalone)
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.StdioServerTransport;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.Map;

public class MyMcpServer {
    
    public static void main(String[] args) {
        // 1. Create transport
        var transport = new StdioServerTransport();
        
        // 2. Define tools
        var searchTool = new McpServerFeatures.SyncToolSpecification(
            new McpSchema.Tool(
                "search_database",
                "Search records in the database",
                Map.of(
                    "type", "object",
                    "properties", Map.of(
                        "query", Map.of("type", "string", "description", "Search query"),
                        "limit", Map.of("type", "integer", "description", "Max results", "default", 10)
                    ),
                    "required", List.of("query")
                )
            ),
            (exchange, toolArgs) -> {
                var query = (String) toolArgs.get("query");
                var limit = (Integer) toolArgs.getOrDefault("limit", 10);
                // Your business logic here
                var results = searchDatabase(query, limit);
                return new McpSchema.CallToolResult(
                    List.of(new McpSchema.TextContent(results)),
                    false
                );
            }
        );
        
        // 3. Build and start server
        var server = McpServer.sync(transport)
            .serverInfo("my-custom-server", "1.0.0")
            .capabilities(McpSchema.ServerCapabilities.builder()
                .tools(new McpSchema.ServerCapabilities.ToolCapabilities(true))
                .build())
            .tools(searchTool)
            .build();
        
        // Server runs until process exits
    }
}
```

### Configuring MCP in Your Environment

When `topic` is **configure-agent**:

#### VS Code / GitHub Copilot Configuration

**Workspace-level** (`.vscode/mcp.json`) — recommended for project-specific MCPs:

```jsonc
{
  "servers": {
    // stdio-based server (local)
    "my-database-server": {
      "type": "stdio",
      "command": "node",
      "args": ["${workspaceFolder}/mcp-servers/db-server/dist/index.js"],
      "env": {
        "DATABASE_URL": "${input:databaseUrl}"  // prompts user for value
      }
    },
    
    // npx-based server (no local install needed)
    "github-server": {
      "type": "stdio",
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_TOKEN": "${input:githubToken}"
      }
    },
    
    // Python-based server
    "my-python-server": {
      "type": "stdio",
      "command": "python",
      "args": ["-m", "my_mcp_server"],
      "env": {
        "API_KEY": "${input:apiKey}"
      }
    },
    
    // Docker-based server
    "containerized-server": {
      "type": "stdio",
      "command": "docker",
      "args": [
        "run", "-i", "--rm",
        "-e", "CONFIG_PATH=/config",
        "-v", "${workspaceFolder}:/workspace",
        "my-mcp-server:latest"
      ]
    },
    
    // Remote HTTP server
    "remote-api-server": {
      "type": "http",
      "url": "https://mcp.mycompany.com/api",
      "headers": {
        "Authorization": "Bearer ${input:apiToken}"
      }
    }
  }
}
```

**User-level** (`settings.json`) — for global MCPs available in all workspaces:

```jsonc
{
  "mcp": {
    "servers": {
      "filesystem": {
        "type": "stdio",
        "command": "npx",
        "args": ["-y", "@modelcontextprotocol/server-filesystem", "/path/to/allowed/dir"]
      }
    }
  }
}
```

#### Claude Desktop Configuration

**Location:** `~/Library/Application Support/Claude/claude_desktop_config.json` (macOS) or `%APPDATA%\Claude\claude_desktop_config.json` (Windows)

```jsonc
{
  "mcpServers": {
    "my-server": {
      "command": "node",
      "args": ["/path/to/server/index.js"],
      "env": {
        "API_KEY": "your-key-here"
      }
    }
  }
}
```

### Types of MCP Servers — Complete Catalog

When `topic` is **types-of-mcp**:

```
MCP Server Catalog (by category)
│
├── 📊 DATA ACCESS
│   ├── Database Servers
│   │   ├── PostgreSQL MCP ········ Query, schema inspection, migrations
│   │   ├── MySQL MCP ············· Full CRUD, explain plans
│   │   ├── MongoDB MCP ··········· Document queries, aggregations
│   │   ├── SQLite MCP ············ Local DB operations
│   │   ├── Redis MCP ············· Cache operations, pub/sub
│   │   └── Custom DB MCP ········· Wrap any JDBC/ODBC source
│   │
│   ├── File System Servers
│   │   ├── Filesystem MCP ········ Read/write/search files (official)
│   │   ├── Google Drive MCP ······ Access Drive files
│   │   └── S3 MCP ··············· AWS S3 bucket operations
│   │
│   └── Search & Knowledge
│       ├── Brave Search MCP ······ Web search integration
│       ├── Exa Search MCP ········ AI-optimized web search
│       ├── Memory MCP ············ Persistent knowledge graph
│       └── RAG MCP ··············· Vector search over your documents
│
├── 🛠️ DEVELOPER TOOLS
│   ├── Version Control
│   │   ├── GitHub MCP ············ Issues, PRs, repos, actions
│   │   ├── GitLab MCP ············ MRs, pipelines, project management
│   │   └── Git MCP ··············· Local git operations
│   │
│   ├── Build & CI/CD
│   │   ├── Docker MCP ············ Container management, image builds
│   │   ├── Kubernetes MCP ········ Cluster operations, kubectl wrapper
│   │   └── GitHub Actions MCP ···· Workflow management, run triggers
│   │
│   ├── Code Quality
│   │   ├── ESLint MCP ············ Linting JavaScript/TypeScript
│   │   ├── SonarQube MCP ········· Code quality metrics
│   │   └── Test Runner MCP ······· Execute and report test results
│   │
│   └── Documentation
│       ├── Swagger/OpenAPI MCP ···· API documentation
│       └── JSDoc/Javadoc MCP ····· Code documentation generation
│
├── 🌐 API INTEGRATION
│   ├── REST API Gateway MCP ······ Wrap any REST API as MCP tools
│   ├── GraphQL MCP ··············· Query any GraphQL endpoint
│   ├── Puppeteer/Playwright MCP ·· Browser automation, web scraping
│   ├── Fetch MCP ················· Generic HTTP requests
│   └── Webhook MCP ··············· Trigger and receive webhooks
│
├── 📧 PRODUCTIVITY & COMMUNICATION
│   ├── Slack MCP ················· Send messages, search channels
│   ├── Email (Gmail/SMTP) MCP ···· Send, read, search emails
│   ├── Calendar MCP ·············· Schedule, query events
│   ├── Notion MCP ················ Pages, databases, search
│   ├── Linear MCP ················ Issue tracking, project management
│   └── Jira MCP ·················· Ticket management, sprint tracking
│
├── ☁️ INFRASTRUCTURE & CLOUD
│   ├── AWS MCP ··················· EC2, S3, Lambda, CloudWatch
│   ├── Terraform MCP ············· Plan, apply, state management
│   ├── Cloudflare MCP ············ Workers, DNS, analytics
│   └── Vercel MCP ················ Deployments, domains, logs
│
├── 🤖 AI & ML
│   ├── HuggingFace MCP ··········· Model inference, dataset access
│   ├── OpenAI MCP ················ GPT/DALL-E/Whisper integration
│   ├── LangChain MCP ············· Chain/agent orchestration
│   └── Vector DB MCP ············· Embeddings, similarity search
│
└── 🏢 CUSTOM / DOMAIN-SPECIFIC
    ├── Internal API Wrapper ······ Your company's APIs as MCP tools
    ├── Business Logic Server ····· Domain-specific operations
    ├── Legacy System Bridge ······ Wrap SOAP/XML/mainframe systems
    └── Compliance/Audit MCP ······ Policy checks, audit logging
```

### Combining MCP with APIs — Patterns

When `topic` is **api-integration**:

#### Pattern 1: REST API Wrapper MCP

Wrap an existing REST API so the AI can use it as a tool:

```typescript
import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";
import { z } from "zod";

const server = new McpServer({ name: "rest-api-wrapper", version: "1.0.0" });

// Wrap a weather API as an MCP tool
server.tool(
  "get_weather",
  "Get current weather for a city",
  { city: z.string(), units: z.enum(["metric", "imperial"]).default("metric") },
  async ({ city, units }) => {
    const response = await fetch(
      `https://api.openweathermap.org/data/2.5/weather?q=${city}&units=${units}&appid=${process.env.WEATHER_API_KEY}`
    );
    const data = await response.json();
    return {
      content: [{
        type: "text",
        text: `Weather in ${city}: ${data.main.temp}°, ${data.weather[0].description}`
      }]
    };
  }
);

// Wrap a CRUD API
server.tool(
  "create_ticket",
  "Create a support ticket in the ticketing system",
  {
    title: z.string(),
    description: z.string(),
    priority: z.enum(["low", "medium", "high", "critical"]),
    assignee: z.string().optional()
  },
  async ({ title, description, priority, assignee }) => {
    const response = await fetch("https://api.mycompany.com/tickets", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${process.env.TICKET_API_KEY}`
      },
      body: JSON.stringify({ title, description, priority, assignee })
    });
    const ticket = await response.json();
    return {
      content: [{ type: "text", text: `Created ticket #${ticket.id}: ${ticket.url}` }]
    };
  }
);
```

#### Pattern 2: GraphQL API as MCP

```typescript
server.tool(
  "query_graphql",
  "Execute a GraphQL query against the product catalog",
  {
    query: z.string().describe("GraphQL query string"),
    variables: z.record(z.any()).optional()
  },
  async ({ query, variables }) => {
    const response = await fetch(process.env.GRAPHQL_ENDPOINT, {
      method: "POST",
      headers: { "Content-Type": "application/json", "Authorization": `Bearer ${process.env.GQL_TOKEN}` },
      body: JSON.stringify({ query, variables })
    });
    const result = await response.json();
    return { content: [{ type: "text", text: JSON.stringify(result.data, null, 2) }] };
  }
);
```

#### Pattern 3: Multi-API Aggregator MCP

One MCP server that orchestrates calls across multiple APIs:

```typescript
// A single MCP server that combines multiple APIs into cohesive tools
server.tool(
  "get_customer_360",
  "Get complete customer view from CRM + Orders + Support",
  { customerId: z.string() },
  async ({ customerId }) => {
    // Fan out to multiple APIs in parallel
    const [profile, orders, tickets] = await Promise.all([
      fetch(`${CRM_API}/customers/${customerId}`).then(r => r.json()),
      fetch(`${ORDER_API}/orders?customer=${customerId}`).then(r => r.json()),
      fetch(`${SUPPORT_API}/tickets?customer=${customerId}`).then(r => r.json())
    ]);
    
    return {
      content: [{
        type: "text",
        text: JSON.stringify({
          customer: profile,
          recentOrders: orders.slice(0, 5),
          openTickets: tickets.filter(t => t.status === "open"),
          totalSpend: orders.reduce((sum, o) => sum + o.total, 0)
        }, null, 2)
      }]
    };
  }
);
```

#### Pattern 4: OpenAPI Spec → Auto-Generated MCP

```typescript
// Automatically generate MCP tools from an OpenAPI spec
import { parse } from "yaml";
import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js";

async function createMcpFromOpenAPI(specUrl: string) {
  const spec = parse(await (await fetch(specUrl)).text());
  const server = new McpServer({ name: spec.info.title, version: spec.info.version });
  
  for (const [path, methods] of Object.entries(spec.paths)) {
    for (const [method, operation] of Object.entries(methods)) {
      const toolName = operation.operationId || `${method}_${path}`.replace(/[^a-zA-Z0-9]/g, "_");
      // Build Zod schema from OpenAPI parameters + requestBody
      // Register as tool
      server.tool(toolName, operation.summary, zodSchema, handler);
    }
  }
  return server;
}
```

### Building Agents with MCP

When `topic` is **agent-patterns**:

```
Agent Architecture Patterns with MCP
│
├── Pattern 1: SINGLE AGENT + MULTIPLE TOOLS
│   │   Simplest pattern. One AI agent with access to multiple MCP servers.
│   │
│   │   ┌────────────────┐
│   │   │   AI Agent      │
│   │   │   (Copilot)     │
│   │   └──┬───┬───┬──┘
│   │      │   │   │
│   │   ┌──▼┐ ┌▼──┐ ┌▼──┐
│   │   │DB │ │Git│ │API│  ← MCP Servers
│   │   └───┘ └───┘ └───┘
│   │
│   ├── Use when: Most tasks. One agent can handle the workflow.
│   └── Example: Copilot agent mode with GitHub + filesystem + database MCPs
│
├── Pattern 2: TOOL CHAINING (Sequential)
│   │   Agent calls tools in sequence, passing output as input to the next.
│   │
│   │   Agent: "Find failing tests → Read the code → Search for similar fixes → Apply fix"
│   │   Steps: test_runner.run() → fs.read() → search.find() → fs.write()
│   │
│   ├── Use when: Multi-step workflows with data dependencies
│   └── Key: Each tool's output informs the next tool's input
│
├── Pattern 3: ROUTING AGENT
│   │   Agent classifies the request and routes to the appropriate tool(s).
│   │
│   │   User: "Deploy the latest build to staging"
│   │   Agent thinks: This needs → git (get latest) + docker (build) + k8s (deploy)
│   │
│   ├── Use when: Diverse request types needing different tool combinations
│   └── Key: Agent acts as intelligent dispatcher
│
├── Pattern 4: MULTI-AGENT with MCP
│   │   Multiple specialized agents, each with their own MCP servers.
│   │
│   │   ┌──────────────┐
│   │   │ Orchestrator │
│   │   │    Agent     │
│   │   └──┬───────┬───┘
│   │      │       │
│   │   ┌──▼───┐ ┌─▼────┐
│   │   │Code  │ │DevOps│  ← Specialized Agents
│   │   │Agent │ │Agent │
│   │   └──┬───┘ └──┬───┘
│   │      │        │
│   │   ┌──▼───┐ ┌──▼───┐
│   │   │GitHub│ │K8s   │  ← MCP Servers
│   │   │MCP   │ │MCP   │
│   │   └──────┘ └──────┘
│   │
│   ├── Use when: Complex workflows requiring different expertise
│   └── Key: Each agent is focused and has appropriate tools
│
├── Pattern 5: HUMAN-IN-THE-LOOP
│   │   Agent proposes actions, waits for human approval before executing.
│   │
│   │   Agent: "I want to run DELETE FROM orders WHERE status='cancelled'"
│   │   Human: [Approve] / [Reject] / [Modify]
│   │   Agent: *executes if approved*
│   │
│   ├── Use when: Destructive operations, production changes, financial transactions
│   └── Key: MCP tools can surface confirmation prompts
│
└── Pattern 6: MCP + LLM API COMPOSITION
    │   Combine MCP tools with direct LLM API calls for advanced workflows.
    │
    │   Example: Custom agent that:
    │   1. Uses MCP to fetch data (database, files, APIs)
    │   2. Calls LLM API (OpenAI/Anthropic) for reasoning
    │   3. Uses MCP to execute the decided action
    │   4. Calls LLM API again to summarize results
    │
    ├── Use when: Building custom agent applications
    └── Key: MCP provides the "hands", LLM API provides the "brain"
```

#### Custom Agent with MCP + LLM API (Architecture)

```typescript
// Conceptual architecture: Custom agent combining MCP client + LLM API
import { Client } from "@modelcontextprotocol/sdk/client/index.js";
import { StdioClientTransport } from "@modelcontextprotocol/sdk/client/stdio.js";
import OpenAI from "openai";

class McpAgent {
  private mcpClients: Map<string, Client> = new Map();
  private llm = new OpenAI();
  
  // Connect to multiple MCP servers
  async connectServer(name: string, command: string, args: string[]) {
    const transport = new StdioClientTransport({ command, args });
    const client = new Client({ name: "my-agent", version: "1.0.0" });
    await client.connect(transport);
    this.mcpClients.set(name, client);
  }
  
  // Get all available tools across all MCP servers
  async getAllTools() {
    const tools = [];
    for (const [serverName, client] of this.mcpClients) {
      const { tools: serverTools } = await client.listTools();
      tools.push(...serverTools.map(t => ({
        ...t,
        _server: serverName  // track which server owns this tool
      })));
    }
    return tools;
  }
  
  // Agent loop: reason → act → observe → repeat
  async run(userMessage: string) {
    const tools = await this.getAllTools();
    const messages = [{ role: "user", content: userMessage }];
    
    while (true) {
      // 1. LLM reasons about what to do
      const response = await this.llm.chat.completions.create({
        model: "gpt-4",
        messages,
        tools: tools.map(t => ({
          type: "function",
          function: { name: t.name, description: t.description, parameters: t.inputSchema }
        }))
      });
      
      const choice = response.choices[0];
      if (choice.finish_reason === "stop") {
        return choice.message.content;  // Done!
      }
      
      // 2. Execute tool calls via MCP
      for (const toolCall of choice.message.tool_calls || []) {
        const tool = tools.find(t => t.name === toolCall.function.name);
        const client = this.mcpClients.get(tool._server);
        const result = await client.callTool({
          name: toolCall.function.name,
          arguments: JSON.parse(toolCall.function.arguments)
        });
        
        // 3. Feed result back to LLM
        messages.push(choice.message);
        messages.push({
          role: "tool",
          tool_call_id: toolCall.id,
          content: result.content.map(c => c.text).join("\n")
        });
      }
      // Loop back to step 1 — LLM decides next action
    }
  }
}

// Usage
const agent = new McpAgent();
await agent.connectServer("github", "npx", ["-y", "@modelcontextprotocol/server-github"]);
await agent.connectServer("db", "node", ["./my-db-server.js"]);
await agent.connectServer("slack", "npx", ["-y", "@modelcontextprotocol/server-slack"]);

const result = await agent.run(
  "Find all open bugs from GitHub, check if they match any recent DB errors, and post a summary to #engineering in Slack"
);
```

### Step-by-Step: Build Your First MCP Server

When `goal` is **build-my-own-mcp**, walk through this checklist:

```
BUILD YOUR OWN MCP SERVER — Checklist
│
├── Step 1: DEFINE PURPOSE
│   ├── What problem does this MCP solve?
│   ├── What tools (actions) should AI be able to perform?
│   ├── What resources (data) should be exposed?
│   └── What prompts (templates) would be useful?
│
├── Step 2: CHOOSE STACK
│   ├── Language: TypeScript (most mature SDK), Python (fast to prototype), Java (enterprise)
│   ├── Transport: stdio (local dev), HTTP (remote/production)
│   └── Dependencies: What APIs/databases does this server need?
│
├── Step 3: SCAFFOLD PROJECT
│   ├── TypeScript: npm init → npm install @modelcontextprotocol/sdk zod
│   ├── Python: uv init → uv add "mcp[cli]"
│   └── Java: Spring Boot + io.modelcontextprotocol:mcp-spring-webflux
│
├── Step 4: IMPLEMENT SERVER
│   ├── Create server instance with name + version
│   ├── Register tools with names, descriptions, schemas, handlers
│   ├── Register resources with URIs and read handlers
│   ├── Register prompts with templates and parameters
│   ├── Connect transport and start listening
│   └── Handle errors gracefully (return error content, don't crash)
│
├── Step 5: TEST WITH MCP INSPECTOR
│   ├── npx @modelcontextprotocol/inspector node dist/index.js
│   ├── Verify: tools list returns your tools
│   ├── Verify: each tool executes correctly with sample inputs
│   ├── Verify: resources are readable
│   └── Verify: error cases return helpful messages
│
├── Step 6: CONFIGURE IN YOUR CLIENT
│   ├── VS Code: Add to .vscode/mcp.json
│   ├── Claude Desktop: Add to claude_desktop_config.json
│   ├── Verify: Agent can see and use your tools
│   └── Test: Ask the agent to use your tools in a real scenario
│
├── Step 7: HARDEN FOR PRODUCTION
│   ├── Input validation (never trust AI-generated inputs blindly)
│   ├── Rate limiting (prevent runaway tool calls)
│   ├── Timeout handling (don't hang on slow APIs)
│   ├── Logging & observability (trace every tool call)
│   ├── Secret management (use env vars, never hardcode)
│   └── Error boundaries (catch and return, never crash the server)
│
└── Step 8: DISTRIBUTE
    ├── npm package (for TypeScript servers)
    ├── PyPI package (for Python servers)
    ├── Docker image (universal, recommended for production)
    └── Add to MCP server registries (mcp.so, Smithery, etc.)
```

### Security Best Practices

```
MCP Security Checklist
│
├── AUTHENTICATION
│   ├── Use OAuth 2.0 or API keys for remote servers
│   ├── Never hardcode secrets — use environment variables
│   ├── Rotate credentials regularly
│   └── Support token refresh for long-running sessions
│
├── AUTHORIZATION
│   ├── Principle of least privilege — expose only needed tools
│   ├── Scope tool permissions (read-only vs read-write)
│   ├── Validate all inputs (the AI can hallucinate bad inputs)
│   └── Implement row-level/resource-level access control
│
├── SANDBOXING
│   ├── Run in Docker containers with limited privileges
│   ├── Use filesystem allowlists (don't expose entire disk)
│   ├── Network isolation — restrict outbound connections
│   └── Resource limits (CPU, memory, timeout)
│
├── AUDIT & LOGGING
│   ├── Log every tool invocation with timestamps
│   ├── Log input parameters (redact sensitive values)
│   ├── Track which AI model/user triggered the call
│   └── Alerting on anomalous patterns
│
└── DATA PROTECTION
    ├── Sanitize outputs — don't leak internal data
    ├── PII handling — mask/filter personal data
    ├── Don't return raw database errors to the AI
    └── Consider what the AI might do with the data
```

### Testing & Debugging MCPs

```
Testing Workflow
│
├── MCP INSPECTOR (interactive testing)
│   npx @modelcontextprotocol/inspector <command> <args>
│   → Opens web UI to test tools, resources, and prompts interactively
│
├── UNIT TESTS (automated)
│   ├── Test each tool handler with mock inputs
│   ├── Test error cases (invalid input, API failures)
│   ├── Test schema validation rejects bad inputs
│   └── Test resource read handlers return expected formats
│
├── INTEGRATION TESTS (end-to-end)
│   ├── Spin up server programmatically
│   ├── Connect MCP client to it
│   ├── Call tools/list, tools/call, resources/read
│   ├── Verify responses match expected format
│   └── Test concurrent requests don't cause issues
│
└── DEBUGGING TIPS
    ├── Use MCP_DEBUG=1 environment variable for verbose logging
    ├── Check server stderr for error messages (stdio transport)
    ├── Verify JSON-RPC message format with protocol logs
    ├── Common issues:
    │   ├── "Server not responding" → check command path and args
    │   ├── "Tool not found" → verify tool name matches exactly
    │   ├── "Invalid params" → check Zod/JSON Schema matches
    │   └── "Connection refused" → check port and transport type
    └── VS Code: Output panel → "MCP" channel shows server logs
```

### Real-World Examples — What People Build

When `topic` is **real-world-examples**:

| MCP Server | What It Does | Why It's Useful |
|---|---|---|
| **@modelcontextprotocol/server-github** | Full GitHub integration: repos, issues, PRs, files | AI can manage your GitHub workflow |
| **@modelcontextprotocol/server-filesystem** | Safe file read/write/search with directory allowlist | AI can work with your files securely |
| **@modelcontextprotocol/server-postgres** | PostgreSQL queries, schema inspection | AI becomes your database assistant |
| **@modelcontextprotocol/server-puppeteer** | Browser automation, screenshots, scraping | AI can interact with web pages |
| **@modelcontextprotocol/server-brave-search** | Web search via Brave Search API | AI can research the internet |
| **@modelcontextprotocol/server-memory** | Persistent knowledge graph storage | AI remembers across sessions |
| **@modelcontextprotocol/server-slack** | Slack messaging, channel management | AI as Slack assistant |
| **@modelcontextprotocol/server-google-maps** | Geocoding, directions, place search | AI with location awareness |
| **Custom: Internal API wrapper** | Your company's REST API as MCP tools | AI works with your systems |
| **Custom: CI/CD dashboard** | Pipeline status, deploy triggers | AI monitors and manages deploys |

### Multi-MCP Project Structure

When `topic` is **build-server** or `goal` is **build-my-own-mcp**, and the user asks about organizing multiple MCPs:

Recommend the `mcp-servers/` directory pattern — each MCP as an independent subdirectory:

```
project-root/
├── .vscode/
│   └── mcp.json          ← ONE file registers ALL MCP servers
├── mcp-servers/
│   ├── weather-mcp/      ← TypeScript MCP (own package.json)
│   ├── db-mcp/           ← Python MCP (own pyproject.toml)
│   ├── github-tools-mcp/ ← TypeScript MCP (own package.json)
│   └── inventory-mcp/    ← Java MCP (own pom.xml)
└── src/                  ← Main application code
```

Key rules:
- **One server per folder** — independent deps, builds, and entry points
- **Mixed languages OK** — TS, Python, Java, Go can coexist
- **One `mcp.json`** — single config file wires them all to Copilot
- **gitignore** build artifacts: `mcp-servers/*/node_modules/`, `mcp-servers/*/dist/`, `mcp-servers/*/.venv/`

### `.vscode/mcp.json` Schema Quick Reference

```jsonc
{
  "servers": {
    "<unique-name>": {
      "type": "stdio" | "http" | "sse",  // Transport type
      
      // stdio fields:
      "command": "node|python|npx|java|docker",
      "args": ["path/to/entry-point"],
      "cwd": "optional-working-dir",
      "env": { "KEY": "${input:keyName}" },
      
      // http/sse fields:
      "url": "http://host:port/mcp",
      "headers": { "Authorization": "Bearer ${input:token}" }
    }
  },
  "inputs": [
    { "id": "keyName", "type": "promptString", "description": "...", "password": true }
  ]
}
```

**Variables available:** `${workspaceFolder}`, `${input:name}`, `${env:VAR}`, `${userHome}`

### Composition with Other Commands

- Use `/deep-dive MCP` for a progressive concept exploration
- Use `/learn-concept agentic-AI` for broader context on AI agent patterns
- Use `/devops` for CI/CD, Docker, and deployment topics related to MCP hosting
- Use `/tech-stack` to compare MCP with other tool-integration approaches
- Use `/hub trends` for AI coding assistants and agentic AI trends

### Rules
- Always provide WORKING code examples — never pseudocode for build-server topic
- Include the FULL project setup steps (package init, dependencies, run commands)
- Security is NOT optional — always mention auth, validation, and sandboxing
- When showing configuration, show BOTH workspace-level and user-level options
- Test with MCP Inspector before telling the user it works
- Explain trade-offs: stdio vs HTTP, TypeScript vs Python, local vs remote
```
