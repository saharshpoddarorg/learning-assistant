# MCP Server Versioning Guide

> **Audience:** Developers extending this project with new or improved server implementations.

---

## 1. The Problem

You have a working `AtlassianServer` (v1). A colleague provides an improved
`AtlassianServer` (v2). You want to:

- Keep v1 intact (don't overwrite working code)
- Introduce v2 without breaking the existing build
- Switch between v1 and v2 easily (or run them side-by-side for comparison)
- Eventually combine the best of both into a unified v3

This guide defines the conventions and tooling for doing this cleanly.

---

## 2. Core Principle — Package-per-Version

Each server version lives in its own package. The current (stable) version uses the
base package name; new versions add a `.v2`, `.v3` suffix:

```
server/
├── atlassian/               ← v1 (stable, current — DO NOT MODIFY)
│   └── AtlassianServer.java
├── atlassian/v2/            ← v2 (colleague's implementation)
│   └── AtlassianServerV2.java
└── atlassian/               ← vFinal (combined best-of, replaces v1 in-place
    └── AtlassianServer.java    after v1 code is no longer needed)
```

**Why not overwrite?** Keeping v1 and v2 side-by-side lets you:
- diff them to understand what changed
- fall back to v1 if v2 has bugs
- create v3 by cherry-picking the best parts of each
- run both simultaneously for A/B comparison

---

## 3. The `McpServer` Interface

`server.McpServer` is the contract every server version must implement:

```java
public interface McpServer {
    String name();                                              // e.g. "atlassian"
    String version();                                           // e.g. "2.0.0"
    Map<String, String> toolDefinitions();
    String handleToolCall(String toolName, Map<String, String> args);
    void start();
    void stop();
}
```

Both `AtlassianServer` (v1) and `AtlassianServerV2` (v2) return the **same name**
(`"atlassian"`) but different version strings (`"1.0.0"` vs `"2.0.0"`). The registry
uses the name as the key.

---

## 4. The `McpServerRegistry`

`server.McpServerRegistry` holds the **active** implementation per server name.
Registering a new instance with the same name replaces the previous one:

```java
// Main.java or startup code
var registry = new McpServerRegistry()
    .register(new AtlassianServer(config));  // v1 active

// To switch to v2 — just register it last:
var registry = new McpServerRegistry()
    .register(new AtlassianServer(config))   // v1 registered first
    .register(new AtlassianServerV2(config)); // v2 overwrites v1 → v2 is now active
```

**Key:** `register()` always uses `McpServer#name()` as the key, so v2 naturally
replaces v1 by returning the same name.

---

## 5. Step-by-step: Integrating a Colleague's V2

### Step 1 — Receive the code

Place the colleague's files in a new package. Do **not** touch the existing
`server.atlassian` package:

```
mcp-servers/src/server/atlassian/v2/
    AtlassianServerV2.java
    handler/
        ToolHandlerV2.java
    ...
```

### Step 2 — Implement `McpServer`

Add `implements McpServer` to the v2 entry class and make sure `name()` returns
the same string as v1:

```java
package server.atlassian.v2;

import server.McpServer;

public class AtlassianServerV2 implements McpServer {

    private static final String SERVER_NAME    = "atlassian";  // same as v1!
    private static final String SERVER_VERSION = "2.0.0";

    @Override public String name()    { return SERVER_NAME; }
    @Override public String version() { return SERVER_VERSION; }

    @Override
    public Map<String, String> toolDefinitions() {
        // Return the v2 tool catalogue
    }

    @Override
    public String handleToolCall(String toolName, Map<String, String> args) {
        // Delegate to v2 ToolHandlerV2
    }

    @Override public void start() { /* STDIO loop */ }
    @Override public void stop()  { isRunning = false; }
}
```

### Step 3 — Run both versions side-by-side (optional)

To test v2 without deactivating v1, start each in a separate thread and connect
them as separate MCP entries in your AI client config:

```java
// Startup
var v1 = new AtlassianServer(config);
var v2 = new AtlassianServerV2(config);
new Thread(v1::start, "atlassian-v1").start();
new Thread(v2::start, "atlassian-v2").start();
```

In your AI client (e.g., `claude_desktop_config.json`):
```json
{
  "mcpServers": {
    "atlassian-v1": { "command": "java", "args": ["-cp", "out", "server.atlassian.AtlassianServer"] },
    "atlassian-v2": { "command": "java", "args": ["-cp", "out", "server.atlassian.v2.AtlassianServerV2"] }
  }
}
```

### Step 4 — Switch active version via Registry

Once v2 is stable, update the startup to register v2 last so it becomes active:

```java
var registry = new McpServerRegistry()
    .register(new AtlassianServer(config))    // v1 — still compiled, just inactive
    .register(new AtlassianServerV2(config)); // v2 — active
```

### Step 5 — Create the final combined version (optional)

After studying both, create a clean combined implementation back in `server.atlassian`:

```
Approach: copy the best of v1 + v2 into a new commit on top of v1.
          Delete v2 package once the combined version is stable.
          Bump version to "3.0.0".
```

---

## 6. Versioning Rules Summary

| Rule | Rationale |
|------|-----------|
| New version → new package (`server.atlassian.v2`) | Never overwrite working code |
| Same `name()`, new `version()` | Registry key = name; version is metadata |
| Implement `McpServer` interface | Enables registry-based version swapping |
| Last registration wins in registry | Simplest possible activation mechanism |
| Keep v1 compilable | Fall-back safety net |
| Document with `package-info.java` | Use Javadoc header to explain what changed in v2 |

---

## 7. What NOT to Do

| Anti-pattern | Problem |
|--------------|---------|
| Overwrite `server/atlassian/AtlassianServer.java` with v2 code | Destroys v1; no fall-back |
| Name v2 server `"atlassian-v2"` | Breaks AI tool names (AI has learned `"atlassian"`) |
| Copy v1 code into v2 and tweak | Creates a maintenance nightmare — diff becomes useless |
| Run v2 without the interface | Makes registry-based switching impossible |

---

## 8. Adding a Brand New Server

When adding a completely new server (not a new version of an existing one):

1. Create a new package: `server.myservice/`
2. Implement `McpServer`
3. Register in startup: `registry.register(new MyServiceServer(config))`
4. Add entry to your AI client config

No version namespace is needed — just a unique `name()` string
(e.g., `"github"`, `"slack"`, `"my-service"`).

---

## 9. Related

- [McpServer.java](../../mcp-servers/src/server/McpServer.java) — the interface
- [McpServerRegistry.java](../../mcp-servers/src/server/McpServerRegistry.java) — the registry
- [mcp-how-it-works.md](mcp-how-it-works.md) — protocol internals
- [architecture-overview.md](architecture-overview.md) — module + package structure
