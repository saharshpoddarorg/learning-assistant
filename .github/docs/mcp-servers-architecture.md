# MCP Servers Architecture — Developer Guide

> **Who this is for:** Developers who want to understand how MCP servers work,
> how this project implements the MCP protocol, and how to add new servers or tools.

---

## Table of Contents

1. [What is MCP?](#1-what-is-mcp)
2. [How MCP Fits in the System](#2-how-mcp-fits-in-the-system)
3. [MCP Protocol Internals — JSON-RPC 2.0](#3-mcp-protocol-internals--json-rpc-20)
4. [Server Lifecycle](#4-server-lifecycle)
5. [Tool Registration and Dispatch](#5-tool-registration-and-dispatch)
6. [Learning Resources Server — Deep Dive](#6-learning-resources-server--deep-dive)
7. [Atlassian Server — Deep Dive](#7-atlassian-server--deep-dive)
8. [Configuration Architecture](#8-configuration-architecture)
9. [Industry Patterns Used](#9-industry-patterns-used)
10. [Adding a New Server](#10-adding-a-new-server)
11. [Adding a New Tool to an Existing Server](#11-adding-a-new-tool-to-an-existing-server)

---

## 1. What is MCP?

**MCP** (Model Context Protocol) is a JSON-RPC 2.0 protocol designed by Anthropic
to let AI models (GitHub Copilot, Claude, etc.) call external services as **tools**.

Think of MCP as a standardised plugin API for AI assistants:

```
User in Copilot Chat
      │  "find me java concurrency tutorials"
      ▼
GitHub Copilot (AI model)
      │  calls tool: search_resources(query="java concurrency")
      ▼
MCP Server (this project)
      │  runs the search engine
      ▼
Response: [{"title": "Java Concurrency in Practice", "url": "...", "score": 142}, ...]
      │
      ▼
Copilot formats and presents the result to the user
```

### MCP vs REST vs GraphQL

| Aspect | REST | GraphQL | MCP |
|--------|------|---------|-----|
| Consumer | Any HTTP client | Any HTTP client | AI models only |
| Schema | OpenAPI/Swagger | GraphQL SDL | JSON Schema (per tool) |
| Transport | HTTP | HTTP | STDIO or HTTP |
| Style | Resource-based | Query-based | Tool/function-based |
| Primary use | Web services | Data APIs | AI tool augmentation |

MCP is semantically closer to **RPC** (Remote Procedure Call) than to REST, because
tools are named operations with specific inputs, not resources with CRUD verbs.

---

## 2. How MCP Fits in the System

```
┌────────────────────────────────────────────────────────────────┐
│                    AI Assistant (Copilot)                       │
│  Reads tool schemas → calls tools → presents results to user   │
└───────────────────────────┬────────────────────────────────────┘
                            │ MCP (JSON-RPC over STDIO)
              ┌─────────────┴──────────────┐
              ▼                            ▼
  ┌───────────────────────┐    ┌───────────────────────┐
  │  LearningResourcesServer│   │  AtlassianServer       │
  │  server.learningresources│  │  server.atlassian       │
  └───────────┬─────────────┘   └───────────┬────────────┘
              │ delegates to                │ delegates to
  ┌───────────┴───────────┐    ┌────────────┴────────────┐
  │      ToolHandler       │    │      ToolHandler         │
  │  SearchHandler         │    │  BitbucketHandler        │
  │  ScrapeHandler         │    │  JiraHandler             │
  │  ExportHandler         │    │  ConfluenceHandler       │
  │  UrlResourceHandler    │    │                          │
  └───────────┬────────────┘    └────────────┬─────────────┘
              │ uses                         │ calls
  ┌───────────┴────────────┐    ┌────────────┴─────────────┐
  │  ResourceVault          │    │  AtlassianRestClient      │
  │  LearningSearchEngine   │    │  (JiraClient,             │
  │  OfficialDocsSearchEngine    │   ConfluenceClient, etc.) │
  └────────────────────────┘    └──────────────────────────┘
```

---

## 3. MCP Protocol Internals — JSON-RPC 2.0

### Initialization Handshake

When the AI assistant starts an MCP session, it sends:

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": {
    "protocolVersion": "2024-11-05",
    "clientInfo": { "name": "copilot", "version": "1.0" },
    "capabilities": { "roots": {}, "sampling": {} }
  }
}
```

The MCP server responds with its capabilities and tool list:

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "protocolVersion": "2024-11-05",
    "serverInfo": { "name": "learning-resources", "version": "1.0.0" },
    "capabilities": {
      "tools": {
        "search_resources":    { "description": "Search the curated vault..." },
        "discover_resources":  { "description": "Smart discovery with intent..." },
        "browse_vault":        { "description": "Browse by category or type..." }
      }
    }
  }
}
```

### Tool Call

When the AI decides to call a tool:

```json
{
  "jsonrpc": "2.0",
  "id": 42,
  "method": "tools/call",
  "params": {
    "name": "search_resources",
    "arguments": {
      "query": "java concurrency",
      "difficulty": "intermediate",
      "max_results": "10"
    }
  }
}
```

### Tool Response

```json
{
  "jsonrpc": "2.0",
  "id": 42,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "Found 8 results for 'java concurrency':\n\n1. **Java Concurrency in Practice**..."
      }
    ]
  }
}
```

### Error Response

```json
{
  "jsonrpc": "2.0",
  "id": 42,
  "error": {
    "code": -32602,
    "message": "Invalid params: 'query' is required"
  }
}
```

### Standard JSON-RPC Error Codes

| Code | Meaning |
|------|---------|
| `-32700` | Parse error (invalid JSON) |
| `-32600` | Invalid request |
| `-32601` | Method not found |
| `-32602` | Invalid params |
| `-32603` | Internal error |

### Transport: STDIO

This project uses **STDIO transport** — the simplest MCP transport:
- Server reads JSON-RPC messages from `System.in` (one per line)
- Server writes responses to `System.out`
- AI assistant controls the server process lifecycle

This is appropriate for local development and learning. Production deployments
typically use **HTTP/SSE transport** for multi-client support. See
[SETUP.md](../../mcp-servers/SETUP.md) for VS Code integration.

---

## 4. Server Lifecycle

```
main() in Main.java
    │
    ├── 1. ConfigManager.fromDefaults()    ← load & validate configuration
    ├── 2. configManager.loadAndValidate()
    ├── 3. configManager.resolveEffectiveConfig(base)
    │
    └── (future: start server instances)
         │
         ▼
LearningResourcesServer()
    │
    ├── 1. new ResourceVault().loadBuiltInResources()   ← load 48+ resources
    ├── 2. new ToolHandler(vault)                        ← wire tool handlers
    │
    └── server.start()
         │
         └── loop: read line from stdin
                  │
                  ├── blank → skip
                  └── non-blank → processMessage(line)
                                   │
                                   ├── parse JSON-RPC
                                   ├── route by method:
                                   │     "initialize"   → handshake()
                                   │     "tools/call"   → toolHandler.handleToolCall()
                                   │     "tools/list"   → listTools()
                                   └── write JSON response to stdout
```

### Graceful shutdown

The server stops when:
- `stdin` returns `EOF` (AI assistant closes the connection)
- `server.stop()` is called (sets `isRunning = false`)

---

## 5. Tool Registration and Dispatch

### How tools are registered

```java
// LearningResourcesServer.getToolDefinitions()
public Map<String, String> getToolDefinitions() {
    return Map.ofEntries(
        Map.entry("search_resources",       "Search the curated vault..."),
        Map.entry("discover_resources",     "Smart discovery with intent..."),
        Map.entry("browse_vault",           "Browse by category or type..."),
        Map.entry("scrape_url",             "Scrape a URL and summarize..."),
        // ...
    );
}
```

### How tool calls are dispatched

`ToolHandler.handleToolCall()` is the dispatcher (Command pattern):

```java
public String handleToolCall(String toolName, Map<String, String> arguments) {
    return switch (toolName) {
        case "search_resources"     -> searchHandler.search(arguments);
        case "discover_resources"   -> discovery.discover(arguments);
        case "browse_vault"         -> searchHandler.browse(arguments);
        case "scrape_url"           -> scrapeHandler.scrape(arguments);
        case "read_url"             -> scrapeHandler.read(arguments);
        case "add_resource"         -> urlResourceHandler.add(arguments);
        case "export_results"       -> exportHandler.export(arguments);
        default -> "Unknown tool: " + toolName;
    };
}
```

### Design: Why separate handlers?

Each handler is responsible for ONE category of tools:

| Handler | Responsibility | Tools |
|---------|---------------|-------|
| `SearchHandler` | Query the resource vault | `search_resources`, `browse_vault`, `get_resource`, `list_categories` |
| `ScrapeHandler` | Fetch and parse web content | `scrape_url`, `read_url` |
| `ExportHandler` | Convert results to files | `export_results` |
| `UrlResourceHandler` | Add resources from URLs | `add_resource`, `add_resource_from_url` |

This is **Single Responsibility Principle** at the class level — each handler class
can be tested, extended, and replaced independently.

---

## 6. Learning Resources Server — Deep Dive

### Data Model

```
LearningResource (record)
├── id             : String           — unique identifier
├── title          : String           — display name
├── url            : String           — resource URL
├── description    : String           — content summary
├── type           : ResourceType     — DOCUMENTATION, TUTORIAL, BOOK, VIDEO, ...
├── category       : ResourceCategory — JAVA, PYTHON, DEVOPS, ...
├── difficulty     : DifficultyLevel  — BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
├── isOfficial     : boolean          — from an authoritative source?
├── tags           : List<String>     — searchable facets
├── conceptAreas   : Set<ConceptArea> — domain taxonomy
├── addedAt        : Instant          — timestamp (used by RecencyBoostRanker)
└── contentFreshness: ContentFreshness — CURRENT, DATED, LEGACY
```

### Search Tier Hierarchy

```
User query "java concurrency official docs"
      │
      ▼
LearningSearchEngine (Tier-2)
  ├── classifies: SPECIFIC (contains "official docs")
  ├── delegates to ResourceDiscovery for SPECIFIC mode
  └── ResourceDiscovery → discoverByConcept(CONCURRENCY) → scores 48 resources
      │
      ▼
OfficialDocsSearchEngine (Tier-3) — used for official-docs-only queries
  ├── pre-filter: isOfficial == true (skips all non-official resources)
  ├── scorer: TextMatchScorer.Scores.titleHeavy() (title signals 2x)
  ├── ranker: ScoreRanker (score descending)
  └── postSearch: empty result → inject suggestions
```

### ResourceVault

The vault is a curated in-memory collection of 48+ famous learning resources,
loaded from `content/ResourceLibrary.java`. Resources include:

- **Official docs**: Oracle Java docs, MDN, Docker docs, Kubernetes docs, ...
- **Books**: "Clean Code", "Effective Java", "Design Patterns (GoF)", ...
- **Tutorials**: Baeldung, Spring guides, official tutorials, ...
- **Interactive**: LeetCode, HackerRank, Exercism, Codecademy, ...
- **Videos**: YouTube channels, conference talks, ...

### Smart Discovery (ResourceDiscovery)

`ResourceDiscovery` offers three discovery modes, wired to the `SearchMode`:

```
SPECIFIC    → discoverByConcept(concept)  — pinpoint matching
VAGUE       → discoverByDomain(domain)    — broad domain browsing
EXPLORATORY → exploreCategory(category)  — curated starter lists
```

---

## 7. Atlassian Server — Deep Dive

The Atlassian server integrates with **Jira**, **Confluence**, and **Bitbucket**
via their REST APIs (Atlassian Cloud API).

### Authentication

All three products use **OAuth 2.0** (three-legged flow for cloud) or
**API tokens** (simpler, for personal use). The `AtlassianConfigLoader` reads
credentials from `user-config/servers/atlassian/atlassian-config.local.properties`.

```
atlassian.jira.url            = https://myorg.atlassian.net
atlassian.jira.email          = dev@example.com
atlassian.jira.api_token      = <personal_api_token>
atlassian.confluence.url      = https://myorg.atlassian.net/wiki
atlassian.bitbucket.workspace = myworkspace
```

### REST Client Architecture

Each product has its own REST client:

```
AtlassianRestClient (base HTTP machinery)
    ├── JiraClient        (Jira REST API v3)
    ├── ConfluenceClient  (Confluence REST API v2)
    └── BitbucketClient   (Bitbucket REST API 2.0)
```

The base client handles:
- HTTP connection management (Java's `java.net.http.HttpClient`)
- Authentication header injection (`Basic` auth with email + API token)
- JSON response parsing
- Rate-limit retry logic

### Tools Exposed

**Jira tools:**
- `get_jira_issue` — fetch issue details, summary, status, assignee
- `search_jira_issues` — JQL search
- `get_sprint_board` — active sprint items

**Confluence tools:**
- `search_confluence` — full-text CQL search
- `get_page_content` — fetch a page's body as text

**Bitbucket tools:**
- `list_pull_requests` — list open PRs in a repo
- `get_pull_request` — get PR diff and review comments

---

## 8. Configuration Architecture

Configuration uses a **layered override system**:

```
Precedence (lowest to highest):
┌─────────────────────────────────────────┐
│ mcp-config.properties      ← defaults  │
│ mcp-config.local.properties ← overrides│  ← gitignored (machine-specific)
│ Environment variables       ← top      │
└─────────────────────────────────────────┘
```

### Loading Pipeline

```
ConfigManager.fromDefaults()
    │
    ├── 1. PropertiesConfigSource  ← reads mcp-config.properties
    ├── 2. PropertiesConfigSource  ← reads mcp-config.local.properties (if exists)
    └── 3. EnvironmentConfigSource ← reads System.getenv()
         │
         ▼
    ConfigParser.merge(sources)   ← later sources win on key conflict
         │
         ▼
    ConfigValidator.validate()    ← rejects invalid states early
         │
         ▼
    McpConfiguration (record)     ← immutable, validated config object
```

### ConfigSource Pattern

`ConfigSource` is an interface:

```java
public interface ConfigSource {
    Map<String, String> load() throws ConfigLoadException;
    String name(); // for error messages
}
```

Three implementations:
- `PropertiesConfigSource` — loads from `*.properties` files
- `EnvironmentConfigSource` — loads from `System.getenv()`
- (extensible: add `JsonConfigSource`, `YamlConfigSource`, etc.)

### Why Records for Config?

```java
public record McpConfiguration(
    String activeProfile,
    LocationPreferences location,
    UserPreferences preferences,
    BrowserPreferences browser,
    ApiKeyStore apiKeys,
    List<ServerDefinition> servers
) { }
```

Records are immutable by design — once loaded and validated, config can never
be accidentally mutated. This prevents entire categories of bugs.

---

## 9. Industry Patterns Used

### Command Pattern
`ToolHandler.handleToolCall()` dispatches based on a command name string.
Each case in the `switch` is one command. The switch itself is the invoker;
handler classes are the concrete commands.

### Strategy Pattern (Config loading)
Each `ConfigSource` is a different strategy for loading key-value pairs.
`ConfigManager` composes them without knowing their specifics.

### Chain of Responsibility (Config override)
`PropertiesConfigSource` → `PropertiesConfigSource (local)` → `EnvironmentConfigSource`
Each layer's values override the previous layer's values for matching keys.

### Repository Pattern
`ResourceVault` is a Repository — it provides a domain-model-level collection API
(`findById(id)`, `findByCategory(category)`, `listAll()`) without exposing storage
details. The vault currently uses an in-memory `Map`, but could be swapped for a
database-backed implementation without changing callers.

### Value Object Pattern
All domain records (`LearningResource`, `McpConfiguration`, `ServerDefinition`, etc.)
are Java records — value objects with no identity beyond their data, no setters,
and structural equality. This is exactly what the Value Object pattern prescribes.

### HTTP Client encapsulation
`AtlassianRestClient` encapsulates Java's `HttpClient` — no code outside the
`client/` package knows what HTTP library is used. This allows swapping to OkHttp,
Retrofit, or any other HTTP library without touching handlers or models.

---

## 10. Adding a New Server

**Example: add a GitHub server for searching repos and issues.**

### Step 1 — Create the server package

```
mcp-servers/src/server/github/
├── GitHubServer.java       ← STDIO loop + tool registration
├── package-info.java
├── client/
│   └── GitHubRestClient.java  ← wraps GitHub REST API v3
├── handler/
│   └── ToolHandler.java       ← dispatches tool calls
└── model/
    ├── GitHubIssue.java
    └── GitHubRepository.java
```

### Step 2 — Implement `GitHubServer`

```java
public final class GitHubServer {

    private static final String SERVER_NAME = "github";
    private static final String SERVER_VERSION = "1.0.0";

    private final ToolHandler toolHandler;
    private volatile boolean isRunning;

    public GitHubServer(GitHubRestClient client) {
        this.toolHandler = new ToolHandler(client);
    }

    public Map<String, String> getToolDefinitions() {
        return Map.of(
            "search_repos",  "Search GitHub repositories by keyword",
            "list_issues",   "List open issues for a repository",
            "get_issue",     "Get details of a specific issue"
        );
    }

    public void start() {
        isRunning = true;
        try (var reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            while (isRunning) {
                var line = reader.readLine();
                if (line == null) break;
                if (!line.isBlank()) processMessage(line.trim());
            }
        } catch (IOException e) {
            Logger.getLogger(GitHubServer.class.getName()).severe(e.getMessage());
        }
    }

    // processMessage → parse JSON → route to toolHandler or return handshake
}
```

### Step 3 — Add config

```properties
# mcp-servers/user-config/servers/github/github-config.properties
github.api.url = https://api.github.com
github.token   =   ← set in github-config.local.properties (gitignored)
```

### Step 4 — Register in Main.java

```java
var githubConfig = GitHubConfigLoader.load(configManager);
var githubServer = new GitHubServer(new GitHubRestClient(githubConfig));
githubServer.start();
```

---

## 11. Adding a New Tool to an Existing Server

**Example: add `recommend_resources` tool that uses RecencyBoostRanker.**

### Step 1 — Register in server's tool map

```java
// LearningResourcesServer.getToolDefinitions()
Map.entry("recommend_resources", "Recommend recently-added resources by topic"),
```

### Step 2 — Add dispatch case in ToolHandler

```java
case "recommend_resources" -> searchHandler.recommend(arguments);
```

### Step 3 — Implement in SearchHandler

```java
public String recommend(Map<String, String> arguments) {
    var topic = arguments.getOrDefault("topic", "java");
    var engine = new RecentResourceEngine(vault);
    var result = engine.search(SearchContext.of(topic));
    return formatResult(result);
}
```

### Step 4 — Create the engine (Tier-3)

```java
public final class RecentResourceEngine extends ConfigurableSearchEngine<LearningResource> {

    public RecentResourceEngine(ResourceVault vault) {
        super(buildConfig(vault));
    }

    private static SearchEngineConfig<LearningResource> buildConfig(ResourceVault vault) {
        var index = new InMemoryIndex<LearningResource>();
        vault.listAll().forEach(r -> index.add(r.id(), r));

        return SearchEngineConfig.<LearningResource>builder()
            .index(index)
            .classifier(new KeywordQueryClassifier.Builder().build())
            .defaultScorer(new TextMatchScorer.Builder<LearningResource>()
                .titleExtractor(LearningResource::title)
                .bodyExtractor(LearningResource::description)
                .build())
            .ranker(ScoreRanker.<LearningResource>instance()
                .thenRank(new RecencyBoostRanker<>(LearningResource::addedAt)))
            .build();
    }
}
```

---

*For the search engine algorithm details, see [search-engine-algorithms.md](search-engine-algorithms.md).*
*For the overall module architecture, see [architecture-overview.md](architecture-overview.md).*
