# Implementing MCP Servers — Java vs JavaScript/TypeScript

> **Audience:** Developers who want to understand how MCP servers are built in different
> languages, and why this project chose Java.

---

## 1. Overview

MCP servers can be implemented in any language because the protocol is just
JSON-RPC 2.0 over STDIO (or HTTP/SSE). Anthropic provides official SDKs for
the most common languages:

| Language | Package | Notes |
|----------|---------|-------|
| **TypeScript / JavaScript** | `@modelcontextprotocol/sdk` | Most mature; used in official examples |
| **Python** | `mcp` | Clean `@server.list_tools()` decorator API |
| **Java** | `io.modelcontextprotocol:sdk` | Newer; Spring Boot integration available |
| **Go**, **Rust**, **C#** | Community SDKs | Varying maturity |
| **Any language** | Raw JSON-RPC | What this project does — zero dependencies |

---

## 2. TypeScript / JavaScript (Official SDK)

The TypeScript SDK is the reference implementation. It handles all JSON-RPC
framing, session management, and schema validation automatically.

### Installation

```bash
npm install @modelcontextprotocol/sdk
```

### Minimal TypeScript MCP Server

```typescript
import { Server } from "@modelcontextprotocol/sdk/server/index.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import { CallToolRequestSchema, ListToolsRequestSchema } from "@modelcontextprotocol/sdk/types.js";

// 1. Create server
const server = new Server(
  { name: "my-server", version: "1.0.0" },
  { capabilities: { tools: {} } }
);

// 2. Declare tools
server.setRequestHandler(ListToolsRequestSchema, async () => ({
  tools: [
    {
      name: "greet",
      description: "Greet a person by name.",
      inputSchema: {
        type: "object",
        properties: {
          name: { type: "string", description: "The person's name" }
        },
        required: ["name"]
      }
    }
  ]
}));

// 3. Handle tool calls
server.setRequestHandler(CallToolRequestSchema, async (request) => {
  if (request.params.name === "greet") {
    const name = request.params.arguments?.name as string;
    return {
      content: [{ type: "text", text: `Hello, ${name}!` }]
    };
  }
  throw new Error(`Unknown tool: ${request.params.name}`);
});

// 4. Start STDIO transport
const transport = new StdioServerTransport();
await server.connect(transport);
```

**What the SDK gives you for free:**
- JSON-RPC 2.0 framing (newline-delimited)
- Zod schema validation of incoming requests
- Error code mapping (`-32601`, `-32602`, etc.)
- `initialize` / `initialized` handshake
- Session lifecycle management

---

## 3. Python (Official SDK)

```python
from mcp.server import Server
from mcp.server.stdio import stdio_server
from mcp import types

server = Server("my-server")

@server.list_tools()
async def list_tools() -> list[types.Tool]:
    return [
        types.Tool(
            name="greet",
            description="Greet a person by name.",
            inputSchema={
                "type": "object",
                "properties": {"name": {"type": "string"}},
                "required": ["name"]
            }
        )
    ]

@server.call_tool()
async def call_tool(name: str, arguments: dict) -> list[types.TextContent]:
    if name == "greet":
        return [types.TextContent(type="text", text=f"Hello, {arguments['name']}!")]
    raise ValueError(f"Unknown tool: {name}")

async def main():
    async with stdio_server() as streams:
        await server.run(*streams, server.create_initialization_options())
```

The Python SDK uses Python's `asyncio` and decorator-based routing — very
idiomatic for Python developers.

---

## 4. Java — This Project (Raw JSON-RPC)

This project deliberately avoids external dependencies. Each server implements
its own minimal STDIO loop and JSON-RPC handling. This is intentional:
it makes the protocol visible and educational.

### Key Structural Difference

| Concern | TypeScript SDK | Java (this project) |
|---------|---------------|---------------------|
| JSON-RPC framing | SDK handles it | Manual `BufferedReader.readLine()` |
| `initialize` handshake | SDK handles it | Manual `sendInitialized()` |
| `tools/list` | `setRequestHandler()` | Manual `sendToolsList()` |
| `tools/call` routing | `setRequestHandler()` | `ToolHandler.handle(name, args)` |
| Schema validation | Zod (TypeScript) | None (manual arg extraction) |
| Error codes | SDK maps them | Manual JSON construction |

### Java — This Project's Pattern

```java
// McpServer.java — the common contract
public interface McpServer {
    String name();
    String version();
    Map<String, String> toolDefinitions();
    String handleToolCall(String toolName, Map<String, String> args);
    void start();
    void stop();
}

// AtlassianServer.java — the implementation
public class AtlassianServer /* implements McpServer */ {
    public void start() {
        try (var reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            while (isRunning) {
                final var line = reader.readLine();
                if (line == null) break;     // EOF → shutdown
                handleMessage(line.trim());  // dispatch JSON-RPC
            }
        }
    }

    private void handleMessage(final String json) {
        // parse method, id, params from raw JSON string
        // route to: sendInitialized(), sendToolsList(), toolHandler.handle()
        // write response to System.out
    }
}
```

### Java — With the Official MCP SDK (Alternative)

If this project adopted `io.modelcontextprotocol:sdk`, the structure would look like:

```java
// pom.xml (Maven)
// <dependency>
//   <groupId>io.modelcontextprotocol</groupId>
//   <artifactId>mcp</artifactId>
//   <version>0.9.0</version>
// </dependency>

var server = new McpServer.sync(
    new ServerInfo("atlassian", "1.0.0"),
    ServerCapabilities.builder().tools(true).build()
);

server.tool("search_issues",
    "Search Jira issues using JQL or natural language.",
    Map.of("query", new JsonSchemaProperty("string", "Search query")),
    args -> new CallToolResult(searchIssues(args.get("query"))));

var transport = new StdioServerTransport();
server.connect(transport);

// SDK blocks on STDIO until EOF
```

---

## 5. Side-by-Side Comparison

```
Feature                  TypeScript SDK    Python SDK       Java (this project)
─────────────────────────────────────────────────────────────────────────────
Protocol handling        ✅ Automatic      ✅ Automatic     ❌ Manual
Schema validation        ✅ Zod            ✅ Pydantic       ❌ Manual
Error code mapping       ✅ Automatic      ✅ Automatic     ❌ Manual
Zero-dependency build    ❌ npm needed     ❌ pip needed    ✅ javac only
Learning value           Medium            Medium           ✅ High (protocol visible)
Production readiness     ✅ High           ✅ High          Medium (educational)
Async support            ✅ Native         ✅ asyncio       Limited (threads)
Type safety              ✅ Strong         Medium           ✅ Strong
```

---

## 6. When to Use Each Language

| Use case | Best choice |
|----------|------------|
| Quick prototype or AI-native tool | TypeScript (fastest to iterate) |
| Data science / ML integration | Python |
| Enterprise system with existing Java backend | Java with MCP SDK |
| Learning MCP internals | Java without SDK (this project) |
| Cross-language team | TypeScript (most examples + community) |

---

## 7. The MCP Ecosystem

Beyond servers, MCP defines:

- **MCP Clients** — integrated into AI hosts (Claude Desktop, VS Code Copilot, Continue.dev)
- **MCP Resources** — read-only data sources the AI can browse (files, DB rows, API endpoints)
- **MCP Prompts** — reusable prompt templates the AI can invoke
- **MCP Sampling** — server-initiated AI generation requests (advanced)

This project implements **tools only**, which covers the most common use case
(giving the AI callable functions).

---

## 8. Further Reading

- [MCP TypeScript SDK](https://github.com/modelcontextprotocol/typescript-sdk)
- [MCP Python SDK](https://github.com/modelcontextprotocol/python-sdk)
- [MCP Java SDK](https://github.com/modelcontextprotocol/java-sdk)
- [Official MCP examples](https://github.com/modelcontextprotocol/servers)
- See also: [mcp-how-it-works.md](mcp-how-it-works.md) — protocol internals
- See also: [mcp-servers-architecture.md](mcp-servers-architecture.md) — this project's architecture
