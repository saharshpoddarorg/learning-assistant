# MCP Server Development Guide — 3-Tier Developer Reference

> **Audience:** Developers building on or extending MCP servers in this project
> **Language coverage:** Java (this project's pattern) · TypeScript/JavaScript (SDK pattern) · Python (SDK pattern)
> **Companion docs:** [mcp-how-it-works.md](mcp-how-it-works.md) · [mcp-implementations.md](mcp-implementations.md) · [mcp-ecosystem.md](mcp-ecosystem.md) · [mcp-vs-skills.md](mcp-vs-skills.md)

---

## 📑 Table of Contents

- [Tier 1 — Newbie: Add Your First Tool (5 Steps)](#tier-1--newbie-add-your-first-tool-5-steps)
- [Tier 2 — Amateur: How This Project's Java Pattern Works](#tier-2--amateur-how-this-projects-java-pattern-works)
- [Tier 3 — Pro: Advanced Patterns, Testing, Security](#tier-3--pro-advanced-patterns-testing-security)

---

## Tier 1 — Newbie: Add Your First Tool (5 Steps)

> **Goal:** Add a new tool to the Learning Resources MCP server and verify it works.
> **Time:** ~15 minutes.
> **Assumption:** The project builds successfully (`.\mcp-servers\build.ps1`).

### What is a "tool"?

A tool is a named function exposed by an MCP server that the AI can call on behalf of a user.
In this project, the AI (GitHub Copilot) calls tools instead of browsing the web directly.

```
User asks: "Find Java concurrency resources"
Copilot calls: search_resources { query: "java concurrency" }
Server returns: list of matching resources from the vault
Copilot shows: formatted response to the user
```

### Step 1 — Find the handler

Open `mcp-servers/src/server/learningresources/handler/ToolHandler.java`.
This is where tool calls are dispatched to implementation methods.

### Step 2 — Add a tool definition

In `LearningResourcesServer.java` (or in the relevant server's `toolDefinitions()` method),
add an entry for your new tool name and description:

```java
// In the method that maps tool names to descriptions:
"my_new_tool", "Does X when given Y. Returns Z."
```

The description is shown to the AI when it decides which tool to call. Make it:
- Specific (what does it actually do?)
- What arguments does it expect?
- What format does it return?

### Step 3 — Add the case to the dispatcher

In `ToolHandler.java`, add a case to `handleToolCall`:

```java
case "my_new_tool" -> handleMyNewTool(args);
```

### Step 4 — Implement the handler method

Add the private method to `ToolHandler`:

```java
private String handleMyNewTool(final Map<String, String> args) {
    final var topic = args.getOrDefault("topic", "");
    if (topic.isBlank()) {
        return "{\"error\":\"topic argument is required\"}";
    }
    // Your logic here — return JSON string
    return "{\"result\":\"Did something with: " + topic + "\"}";
}
```

**Key rules:**
- Always return a `String` (JSON)
- Validate required arguments — return descriptive error JSON if missing
- Never throw unchecked exceptions from a handler (the dispatcher should catch at the boundary)

### Step 5 — Build and verify

```powershell
.\mcp-servers\build.ps1

# Start the server in demo mode to test interactively:
.\mcp-servers\scripts\server.ps1 demo learning-resources

# Or restart if already running:
.\mcp-servers\scripts\server.ps1 restart learning-resources

# List all tools to verify yours appears:
.\mcp-servers\scripts\server.ps1 list-tools learning-resources
```

In the Copilot chat, try invoking the tool:

```
Use my_new_tool with topic="java"
```

---

## Tier 2 — Amateur: How This Project's Java Pattern Works

### Architecture Overview

This project implements MCP servers **without** an SDK — using raw JSON-RPC over STDIO.
This is a deliberate choice: zero external dependencies, portable, educational.

```
mcp-servers/src/
│
├── Main.java                          ← entry point; creates registry + starts all servers
├── config/                            ← config loading (mcp-config.properties)
│   └── ConfigManager.java
│
└── server/
    ├── McpServer.java                 ← interface every server must implement
    ├── McpServerRegistry.java         ← registry pattern — manages active servers
    │
    ├── learningresources/             ← Learning Resources server package
    │   ├── LearningResourcesServer.java  ← STDIO loop + JSON-RPC dispatcher
    │   ├── handler/
    │   │   └── ToolHandler.java          ← tool dispatch → handler methods
    │   ├── vault/                        ← resource storage + built-in providers
    │   └── search/                       ← search engine
    │
    └── atlassian/                     ← Atlassian server package
        └── ...
```

### The McpServer Interface

Every server implements this 5-method contract:

```java
public interface McpServer {
    String name();                                          // server identifier, e.g. "learning-resources"
    String version();                                       // semantic version, e.g. "1.0.0"
    Map<String, String> toolDefinitions();                  // tool name → description (shown to AI)
    String handleToolCall(String toolName,                  // dispatch incoming tool call
                          Map<String, String> args);
    void start();                                           // blocks on STDIO loop
    void stop();                                            // signals loop to exit
}
```

### The STDIO Loop Pattern

`LearningResourcesServer.start()` implements the minimal STDIO transport:

```java
public void start() {
    isRunning = true;
    try (var reader = new BufferedReader(
            new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

        while (isRunning) {
            final var line = reader.readLine();   // block until next JSON-RPC message
            if (line == null) break;              // EOF = host disconnected
            if (line.isBlank()) continue;         // skip blank lines (keepalive)

            final var response = dispatch(line);  // parse JSON → dispatch → produce response
            System.out.println(response);         // write response back to host
            System.out.flush();                   // CRITICAL: flush or host never sees response
        }
    }
}
```

**Why `System.out.flush()` is critical:** Without it, buffered I/O holds responses in memory.
The AI host never receives them and the tool call hangs indefinitely.

### The Dispatch Pattern

```java
private String dispatch(final String line) {
    try {
        final var request = parseJson(line);            // {"method": "tools/call", "id": "...", ...}
        return switch (request.method()) {
            case "initialize"     -> handleInitialize();
            case "tools/list"     -> handleToolsList();
            case "tools/call"     -> toolHandler.handleToolCall(
                                        request.toolName(),
                                        request.args());
            default               -> errorResponse(-32601, "Method not found");
        };
    } catch (Exception e) {
        return errorResponse(-32603, "Internal error: " + e.getMessage());
    }
}
```

### The `initialize` Handshake

When the AI connects, it sends `initialize`. Your server **must** respond with capabilities:

```json
// Request from host:
{"jsonrpc":"2.0","id":"1","method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{},...}}

// Your response:
{"jsonrpc":"2.0","id":"1","result":{
  "protocolVersion":"2024-11-05",
  "serverInfo":{"name":"learning-resources","version":"1.0.0"},
  "capabilities":{"tools":{}}
}}
```

After `initialize` the host sends `initialized` (a notification — no response needed).
Only after this handshake does the host send `tools/list` and begin calling tools.

### Adding a Brand New Server

To add a completely new server to the project (not just a new tool to an existing one):

1. **Create a package:** `mcp-servers/src/server/<yourserver>/`
2. **Create the server class** implementing `McpServer`:

   ```java
   package server.yourserver;

   public final class YourServer implements McpServer {
       @Override public String name()    { return "your-server"; }
       @Override public String version() { return "1.0.0"; }
       @Override public Map<String, String> toolDefinitions() {
           return Map.of("your_tool", "Description of what it does.");
       }
       @Override public String handleToolCall(String name, Map<String, String> args) {
           return switch (name) {
               case "your_tool" -> doSomething(args);
               default -> errorJson(name);
           };
       }
       @Override public void start() { /* STDIO loop */ }
       @Override public void stop()  { isRunning = false; }
   }
   ```

3. **Register in `Main.java`:**

   ```java
   registry.register(new YourServer(config));
   ```

4. **Add config** (if needed) to `mcp-servers/user-config/mcp-config.properties`
5. **Add VS Code task** to `.vscode/tasks.json` (start / stop / restart / logs)
6. **Build and test:**

   ```powershell
   .\mcp-servers\build.ps1
   .\mcp-servers\scripts\server.ps1 start your-server
   .\mcp-servers\scripts\server.ps1 list-tools your-server
   ```

### TypeScript Equivalent Pattern

If building a server in TypeScript instead (for a different project), the SDK handles
the protocol for you — you focus purely on tool logic:

```typescript
import { Server } from "@modelcontextprotocol/sdk/server/index.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";

const server = new Server(
  { name: "your-server", version: "1.0.0" },
  { capabilities: { tools: {} } }
);

// Declare this server's tools (equivalent to toolDefinitions())
server.setRequestHandler(ListToolsRequestSchema, async () => ({
  tools: [{
    name: "your_tool",
    description: "Description of what it does.",
    inputSchema: {
      type: "object",
      properties: {
        topic: { type: "string", description: "The topic to process" }
      },
      required: ["topic"]
    }
  }]
}));

// Handle tool calls (equivalent to handleToolCall())
server.setRequestHandler(CallToolRequestSchema, async (request) => {
  if (request.params.name === "your_tool") {
    const topic = request.params.arguments?.topic;
    return { content: [{ type: "text", text: `Result for ${topic}` }] };
  }
  throw new Error(`Unknown tool: ${request.params.name}`);
});

await server.connect(new StdioServerTransport());
```

**Java vs TypeScript tradeoff:**

| Aspect | Java (this project) | TypeScript (SDK) |
|---|---|---|
| Dependencies | Zero — raw JSON-RPC | `@modelcontextprotocol/sdk`, Node.js |
| Protocol compliance | Manual — you implement handshake | Automatic — SDK handles it |
| Schema validation | Manual | Automatic (Zod) |
| Setup time | Higher (protocol details visible) | Lower (SDK hides complexity) |
| Learning value | High (you see everything) | Lower (magic happens in SDK) |
| Production use | Fine for controlled environments | Recommended for external distribution |

---

## Tier 3 — Pro: Advanced Patterns, Testing, Security

### Server Versioning Pattern

When updating a server without breaking existing clients:

```java
// Original: server.atlassian.AtlassianServer → name() = "atlassian", version() = "1.0.0"
// New version: server.atlassian.v2.AtlassianServerV2 → name() = "atlassian", version() = "2.0.0"

// In Main.java:
registry.register(new AtlassianServer(config));    // registers v1
registry.register(new AtlassianServerV2(config));  // overwrites with v2 (same name key)
```

The registry uses the server name as the key. Registering a server with the same name replaces
the previous entry. This lets you hot-swap implementations without changing startup logic.

For breaking changes (changed tool schemas), create a new server name instead:
`"atlassian"` → `"atlassian-v2"` and update the VS Code `mcp.json` config.

### Testing MCP Servers

**Unit testing handlers** (no network/STDIO needed):

```java
// Test your ToolHandler directly without the STDIO loop:
@Test
void searchResources_withValidQuery_returnsResults() {
    final var vault  = new ResourceVault().loadBuiltInResources();
    final var handler = new ToolHandler(vault);

    final var result = handler.handleToolCall(
        "search_resources",
        Map.of("query", "java concurrency")
    );

    assertThat(result).contains("\"results\"");
    assertThat(result).doesNotContain("\"error\"");
}
```

**Integration testing the full STDIO loop:**

```bash
# In a terminal, start the server in demo mode:
.\mcp-servers\scripts\server.ps1 demo learning-resources

# In a second terminal, send raw JSON-RPC:
echo '{"jsonrpc":"2.0","id":"1","method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{},"clientInfo":{"name":"test","version":"1.0"}}}' | java -cp mcp-servers\out server.learningresources.LearningResourcesServer
```

**Testing with MCP Inspector:**

```bash
# Install MCP Inspector (TypeScript tool from Anthropic):
npx @modelcontextprotocol/inspector

# Connect to a running STDIO server:
# The inspector provides a GUI to invoke tools and inspect responses
```

### Security Considerations

#### Input Validation

Never trust values from `args` — they come from the AI, which processed user input:

```java
// ❌ Dangerous — args values used directly in shell commands:
Runtime.getRuntime().exec("grep " + args.get("query") + " /data/file.txt");

// ✅ Safe — validate, sanitise, use parameterized APIs:
final var query = args.getOrDefault("query", "").strip();
if (query.isBlank() || query.length() > 500) {
    return "{\"error\":\"invalid query\"}";
}
// Use a search API, not shell exec
```

#### Secrets Management

```java
// ❌ Never hardcode API keys:
private static final String API_KEY = "sk-prod-abc123...";

// ✅ Load from environment or config (gitignored):
final var apiKey = config.get("atlassian.api.token");  // from mcp-config.local.properties
```

All secrets live in `mcp-servers/user-config/mcp-config.local.properties` — gitignored.
The `.example` file shows the schema without exposing values; see [local-setup-guide.md](local-setup-guide.md).

#### SSRF Prevention

If your tool makes outbound HTTP requests based on user-provided URLs:

```java
// ❌ SSRF vulnerable — fetches any URL the AI provides:
var content = httpClient.get(args.get("url"));

// ✅ Allowlist: only fetch from known-safe domains:
private static final Set<String> ALLOWED_DOMAINS = Set.of(
    "docs.oracle.com", "dev.java", "spring.io", "github.com"
);

private void validateUrl(final String url) {
    final var host = URI.create(url).getHost();
    if (!ALLOWED_DOMAINS.contains(host)) {
        throw new IllegalArgumentException("URL domain not in allowlist: " + host);
    }
}
```

#### Response Size Limits

Large responses slow Copilot down and may hit context window limits:

```java
// Cap large result sets in handler methods:
private static final int MAX_RESULTS = 20;

final var results = vault.search(query);
final var limited = results.stream().limit(MAX_RESULTS).toList();
```

### Multi-Transport: SSE (Server-Sent Events)

STDIO is ideal for local tools. For remote/multi-user scenarios, MCP also supports SSE:

```
STDIO transport (this project):
  AI host ← spawns → server process (1:1 relationship)
  Best for: local CLI tools, desktop AI assistants

SSE / Streamable HTTP transport:
  AI host ← HTTP GET/POST → server (HTTP endpoint, multi-connection)
  Best for: shared team tools, cloud-deployed servers, multi-user scenarios

Registering in VS Code mcp.json for SSE:
  "type": "sse"  instead of  "type": "stdio"
  "url": "http://localhost:8080/sse"
```

### VS Code `mcp.json` Configuration

Your server is only useful when the AI client knows how to connect to it.
The configuration lives in `.vscode/mcp.json` (or globally in VS Code settings):

```json
{
  "servers": {
    "learning-resources": {
      "type": "stdio",
      "command": "java",
      "args": ["-cp", "${workspaceFolder}/mcp-servers/out",
               "server.learningresources.LearningResourcesServer"],
      "env": {}
    },
    "atlassian": {
      "type": "stdio",
      "command": "java",
      "args": ["-cp", "${workspaceFolder}/mcp-servers/out",
               "server.atlassian.AtlassianServer"],
      "env": {
        "ATLASSIAN_API_TOKEN": "${config:mcpServers.atlassian.apiToken}"
      }
    }
  }
}
```

### Migrating a Tool to a Copilot Skill

When a tool provides static, context-rich reference knowledge (as opposed to dynamic data lookup),
consider whether it should be a Copilot **skill** instead of an MCP tool. See [mcp-vs-skills.md](mcp-vs-skills.md)
for the full decision guide and 6-step migration playbook.

**Quick rule:** If the tool's "data" could be written as a static markdown document that rarely
changes, it should be a skill. If it needs real-time data, API calls, or user-specific data,
keep it as an MCP tool.

### Performance Patterns

```java
// 1. Pre-load expensive data at construction time, not per tool call:
public LearningResourcesServer() {
    this.vault = new ResourceVault().loadBuiltInResources();  // ← once at startup
    this.toolHandler = new ToolHandler(vault);
}
// ✅ loadBuiltInResources() is called once; handleToolCall() is fast per request

// 2. Use StringBuilder for JSON construction in tight loops:
final var sb = new StringBuilder("{\"results\":[");
for (final var resource : results) {
    sb.append(resource.toJson()).append(",");
}
// remove trailing comma, close array/object...

// 3. Log to stderr only (stdout is the MCP channel):
LOGGER.info("Processing request...");  // goes to stderr — correct
System.err.println("debug");          // also fine
System.out.println("debug");          // ❌ corrupts the MCP JSON stream
```
