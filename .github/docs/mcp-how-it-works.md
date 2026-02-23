# How MCP Works — Behind the Scenes

> **Audience:** Developers new to the Model Context Protocol who want to understand
> what happens between the moment an AI assistant decides to use a tool and the
> moment it reads the result.

---

## 1. What Is MCP?

The **Model Context Protocol (MCP)** is an open standard (published by Anthropic, 2024)
that defines how an AI assistant connects to external tools and data sources in a
language-agnostic way.

Think of MCP as **USB-C for AI**: just as USB-C provides a universal plug regardless
of which brand made the device, MCP provides a universal protocol regardless of which
company made the AI or the tool server.

```
┌─────────────────────┐     MCP (JSON-RPC 2.0)    ┌──────────────────────────┐
│   AI Assistant      │ ◄────────────────────────► │   MCP Server (this repo) │
│  (Claude, GPT, etc) │                             │  - Atlassian tools       │
└─────────────────────┘                             │  - Learning resources    │
                                                    └──────────────────────────┘
```

---

## 2. The Transport Layer — STDIO

This project uses **STDIO transport**: the simplest possible transport.

```
AI host process
    │
    ├─► spawns ──► java -cp out server.atlassian.AtlassianServer
    │                   │
    │    stdin ─────────┤ ← JSON-RPC requests from AI
    │    stdout ────────┤ → JSON-RPC responses to AI
    │    stderr ────────┤ → logging only (ignored by AI)
```

- The AI host (e.g. Claude Desktop) **spawns** the server as a subprocess.
- Communication is plain UTF-8 text over stdin/stdout, one JSON object per line.
- No network port, no HTTP, no authentication at the transport layer.
- The server runs until the host process terminates it (or EOF is reached on stdin).

---

## 3. The Protocol — JSON-RPC 2.0

Every message is a [JSON-RPC 2.0](https://www.jsonrpc.org/specification) object.
There are three types:

| Type | Direction | Purpose |
|------|-----------|---------|
| **Request** | Host → Server | Ask the server to do something |
| **Response** | Server → Host | Return a result or error |
| **Notification** | Either direction | Fire-and-forget event (no response) |

### Example Request
```json
{
  "jsonrpc": "2.0",
  "id": "req-42",
  "method": "tools/call",
  "params": {
    "name": "search_issues",
    "arguments": {
      "query": "open bugs in payment service",
      "maxResults": "10"
    }
  }
}
```

### Example Success Response
```json
{
  "jsonrpc": "2.0",
  "id": "req-42",
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Found 3 open bugs:\n1. PAY-101 — NullPointerException in checkout...\n..."
      }
    ]
  }
}
```

### Example Error Response
```json
{
  "jsonrpc": "2.0",
  "id": "req-42",
  "error": {
    "code": -32601,
    "message": "Tool not found: search_issues_v3"
  }
}
```

---

## 4. The MCP Handshake — Session Lifecycle

Every MCP session follows a fixed sequence:

```
Host                          Server
 │                              │
 │─── initialize ──────────────►│  "Here is my client info"
 │◄── initialized ──────────────│  "Here is my server info + capabilities"
 │                              │
 │─── tools/list ──────────────►│  "What tools do you have?"
 │◄── tools/list response ──────│  JSON array of tool schemas
 │                              │
 │ ... (AI decides to use a tool)
 │                              │
 │─── tools/call ──────────────►│  "Call this tool with these args"
 │◄── tools/call response ──────│  Tool result text
 │                              │
 │─── tools/call ──────────────►│  (as many times as needed)
 │◄── tools/call response ──────│
 │                              │
 │  (host process terminates)   │
 │                              X  EOF → server shuts down
```

### `initialize` / `initialized`

The first exchange establishes protocol compatibility. The server declares its name,
version, and which MCP capability sets it supports (e.g., `tools`, `resources`, `prompts`).
This project implements the **tools** capability only.

### `tools/list`

The host asks for all available tools. The server responds with a JSON array where
each entry describes one tool:

```json
{
  "name": "search_issues",
  "description": "Search Jira issues using JQL or natural language text.",
  "inputSchema": {
    "type": "object",
    "properties": {
      "query":      { "type": "string", "description": "Search query" },
      "maxResults": { "type": "string", "description": "Max results (default 10)" }
    },
    "required": ["query"]
  }
}
```

The `inputSchema` is a [JSON Schema](https://json-schema.org/) object. The AI uses
this schema to know which parameters to pass.

### `tools/call`

The host invokes a specific tool. The server:
1. Parses the `name` and `arguments` from the request
2. Routes to the correct handler (e.g., `JiraHandler.searchIssues()`)
3. Returns the result as structured text

---

## 5. How the AI Decides to Use a Tool

This is the key question for understanding MCP: **who decides when to call a tool?**

The answer is the **AI language model itself**, through its reasoning process.

1. **Tool awareness** — during `tools/list`, the AI learns every available tool and
   its description. Good descriptions are critical: the AI reads them to decide
   which tool fits the user's request.

2. **Intent matching** — when processing a user message, the AI internally checks
   whether any tool would help answer it. This is not keyword matching; it is
   semantic reasoning over the tool descriptions.

3. **Argument extraction** — the AI populates tool arguments by extracting relevant
   information from the conversation context (user message + history).

4. **Result integration** — after receiving the tool result, the AI incorporates it
   into its response to the user. The user never sees raw JSON; they see a natural
   language answer based on the tool's output.

> **Example:** The user says "show me all open Jira bugs in the payment service".
> The AI recognises this matches `search_issues` (from the description), extracts
> `query = "open bugs payment service"`, calls the tool, reads the JSON result of
> three issues, and responds in natural language: "I found 3 open bugs: ...".

---

## 6. Tool Schema — What Makes a Good Tool Description

The `description` field is the AI's primary signal. Write it like documentation
for a colleague who has never seen the system:

| Do | Don't |
|----|-------|
| `"Search Jira issues using JQL or free text. Returns issue keys, summaries, and status."` | `"Searches issues"` |
| Mention what the tool accepts (JQL, free text, URLs, etc.) | Leave input format ambiguous |
| Mention what it returns (keys, summaries, comments, etc.) | Say only "returns results" |
| Include usage hints (e.g., "Use for: finding bugs, sprint tracking") | Use jargon the AI won't know |

---

## 7. This Project's Server Lifecycle (Java Implementation)

```
Main.java
  │
  ├── loads config (ConfigManager / AtlassianConfigLoader)
  ├── constructs AtlassianServer(config)
  └── calls server.start()
          │
          └── opens BufferedReader(System.in)
                  │
                  ┌── while (isRunning) ────────────────────────────┐
                  │   readLine()                                      │
                  │   parseJsonRpc(line)                              │
                  │   dispatch(method):                               │
                  │     "initialize"   → sendInitialized()           │
                  │     "tools/list"   → sendToolsList()             │
                  │     "tools/call"   → toolHandler.handle(name, args) → sendResult()
                  │     other          → sendMethodNotFound()        │
                  └────────────────────────────────────────────────-─┘
                  │
                  EOF → LOGGER.info("Shutting down")
```

The loop is intentionally simple. A production-grade implementation would use the
[MCP Java SDK](https://github.com/modelcontextprotocol/java-sdk) which handles
JSON framing, error codes, and capability negotiation automatically.

---

## 8. Error Codes

| Code | Name | When |
|------|------|------|
| `-32700` | Parse error | Request JSON is malformed |
| `-32600` | Invalid request | Missing `jsonrpc` or `method` field |
| `-32601` | Method not found | Unknown method name |
| `-32602` | Invalid params | Missing required argument |
| `-32603` | Internal error | Server threw an unexpected exception |

These are standard JSON-RPC 2.0 codes; MCP does not define additional error codes.

---

## 9. Further Reading

- [MCP official specification](https://modelcontextprotocol.io/specification)
- [MCP introduction](https://modelcontextprotocol.io/introduction)
- [JSON-RPC 2.0 specification](https://www.jsonrpc.org/specification)
- [How Claude uses MCP tools](https://docs.anthropic.com/en/docs/agents-and-tools/mcp)
- See also: [mcp-implementations.md](mcp-implementations.md) — Java vs JavaScript comparison
- See also: [mcp-servers-architecture.md](mcp-servers-architecture.md) — this project's architecture
