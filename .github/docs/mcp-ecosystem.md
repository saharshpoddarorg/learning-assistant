# MCP Ecosystem — Combining Servers, LLMs, Agents, and Multi-Language Stacks

> **The complete guide to using MCP servers in the real AI ecosystem.**
>
> Covers: multi-server composition, version blending, mixing Java + JavaScript,
> LLM APIs (OpenAI, Anthropic, Gemini), industry SDKs (LangChain, LlamaIndex,
> Semantic Kernel), agent frameworks, and how to build the best-of-all-worlds system.

---

## Table of Contents

1. [The MCP Ecosystem — Big Picture](#1-the-mcp-ecosystem--big-picture)
2. [Combining Multiple MCP Servers](#2-combining-multiple-mcp-servers)
3. [Blending Different Server Versions](#3-blending-different-server-versions)
4. [Multi-Language MCP Development — Java + JavaScript](#4-multi-language-mcp-development--java--javascript)
5. [LLM APIs and the MCP Connection](#5-llm-apis-and-the-mcp-connection)
6. [Industry SDKs and Frameworks](#6-industry-sdks-and-frameworks)
7. [Agent Frameworks and MCP](#7-agent-frameworks-and-mcp)
8. [Best-of-All-Worlds Architecture](#8-best-of-all-worlds-architecture)
9. [Practical Recipes](#9-practical-recipes)

---

## 1. The MCP Ecosystem — Big Picture

MCP (Model Context Protocol) is the **connector layer** between AI brains (LLMs)
and the world (APIs, databases, files, services). Understanding the full stack:

```
┌─────────────────── AI HOST / CLIENT ───────────────────────┐
│  Claude Desktop │ VS Code Copilot │ Continue.dev │ Any IDE  │
└──────────────────────────┬─────────────────────────────────┘
                           │  MCP Protocol (JSON-RPC 2.0 / STDIO)
         ┌─────────────────┼──────────────────┐
         ▼                 ▼                  ▼
   ┌──────────┐     ┌──────────────┐    ┌───────────────┐
   │ Learning │     │  Atlassian   │    │   GitHub MCP  │
   │ Resources│     │  MCP Server  │    │   (Node.js)   │
   │ (Java)   │     │  (Java)      │    │               │
   └──────────┘     └──────────────┘    └───────────────┘
         │                 │                  │
    Built-in vault    Jira/Confluence     GitHub REST API
    (no network)      Bitbucket REST       GitHub GraphQL
```

**The AI model never calls external APIs directly.** It calls MCP tools.
MCP servers call external APIs on the model's behalf. This is the core design:

```
User → AI Model → picks tool → MCP server → external API → result → AI Model → User
```

### Who is responsible for what

| Component | Role | Who writes it |
|-----------|------|---------------|
| **LLM / AI Model** | Reasoning, tool selection, response generation | AI provider (OpenAI, Anthropic…) |
| **AI Host** | Manages sessions, routes messages, shows UI | Claude Desktop, VS Code, IDE |
| **MCP Server** | Executes tools, calls APIs, returns results | YOU (this repo) |
| **External API** | The actual data source | Atlassian, GitHub, OpenAI, etc. |

---

## 2. Combining Multiple MCP Servers

### 2.1 Running Multiple Servers Simultaneously

The AI client registers each MCP server independently. They all run in parallel,
each in its own process, each with their own STDIO connection. The AI model
has access to **all tools from all servers at once**.

**Example: AI client config with 4 servers**
```json
{
  "mcpServers": {
    "learning-resources": {
      "command": "java",
      "args": ["-cp", "E:/my-project/mcp-servers/out", "server.learningresources.LearningResourcesServer"]
    },
    "atlassian": {
      "command": "java",
      "args": ["-cp", "E:/my-project/mcp-servers/out", "server.atlassian.AtlassianServer"]
    },
    "github": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": { "GITHUB_TOKEN": "ghp_your_token_here" }
    },
    "filesystem": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-filesystem", "E:/my-project"]
    }
  }
}
```

Now the AI model can — in a single conversation — search Jira, find a related
blog post in the learning vault, check the GitHub PR status, and read a local file.
The model orchestrates across all four servers automatically.

### 2.2 Tool Namespace Conflicts

If two servers expose a tool with the same name (e.g., both have `search`),
the AI client prefixes them with the server name:
- `learning-resources::search`
- `github::search_code`

Always give tools **distinct, descriptive names** to avoid collisions and help
the AI understand which tool to use:

| Bad | Good |
|-----|------|
| `search` | `search_learning_resources` |
| `get_data` | `get_jira_issue` |
| `list` | `list_github_repos` |

### 2.3 Cross-Server Workflows

The most powerful capability: the AI chains tools from multiple servers in one
response, without any explicit wiring from you.

**Example: "Create a Jira ticket for the bug I described in my notes"**

```
AI step 1: filesystem::read_file("my-notes.md")          → reads bug description
AI step 2: atlassian::create_jira_issue(summary, desc)   → creates ticket
AI step 3: atlassian::add_jira_comment(key, "Created!")  → adds confirmation
AI step 4: learning-resources::search("Java debugging")  → suggests learning resources
```

No code change needed — the AI does this cross-server orchestration by itself,
based on tool descriptions alone.

### 2.4 Designing Tools for Composability

Tools that work well across servers share these traits:

- **Return structured data** (JSON-like text), not formatted prose — so the AI can
  use the result as input to another tool
- **Atomic not monolithic** — one tool does one thing; let the AI chain them
- **IDs as outputs** — return Jira issue keys, GitHub PR numbers, etc. so the AI
  can pass them to the next tool
- **Consistent parameter naming** — `projectKey`, `issueKey`, `repoName` across tools
  so the AI learns the vocabulary fast

---

## 3. Blending Different Server Versions

### 3.1 Running v1 and v2 Side-by-Side

The most practical approach during a migration: run both versions simultaneously
with different server names in the AI client. The AI can use both.

```json
{
  "mcpServers": {
    "atlassian": {
      "command": "java",
      "args": ["-cp", "out", "server.atlassian.AtlassianServer"],
      "cwd": "E:/my-project/mcp-servers"
    },
    "atlassian-v2": {
      "command": "java",
      "args": ["-cp", "out", "server.atlassian.v2.AtlassianServerV2"],
      "cwd": "E:/my-project/mcp-servers"
    }
  }
}
```

Now you can explicitly compare:
- "Use atlassian to search Jira" → calls v1
- "Use atlassian-v2 to create an issue" → calls v2
- Notice differences in output, tool names, response quality

### 3.2 A/B Testing Server Versions

Run the same query against both versions and compare:

```
Prompt: "Using atlassian, search for open bugs in project PAYMENT"
Prompt: "Now do the same using atlassian-v2"
```

This is the safest way to validate v2 before switching over. No code change,
no downtime, no risk — just add the v2 server and compare responses.

### 3.3 Cherry-Picking the Best of v1 + v2

After running both in parallel, you'll see which version handles which tools better:

```
v1 strengths: Jira search (more accurate JQL translation)
v2 strengths: Bitbucket PR creation (cleaner response format)
```

Create a **combined v3** by:
1. Starting from v1's code base
2. Copying v2's `BitbucketHandler` into the project
3. Wiring it into v1's `ToolHandler` router
4. Incrementing the version to `"3.0.0"`
5. Retiring both `atlassian` and `atlassian-v2` from the AI client config

### 3.4 Cross-Language Version Blending

Your v1 might be Java; a colleague's v2 might be JavaScript. Both work:

```json
{
  "mcpServers": {
    "atlassian":    { "command": "java", "args": ["-cp", "out", "server.atlassian.AtlassianServer"] },
    "atlassian-v2": { "command": "node", "args": ["dist/atlassian-server.js"] }
  }
}
```

The AI client doesn't care what language a server is written in.
It only speaks JSON-RPC 2.0. Both servers are equally usable from the AI's perspective.

---

## 4. Multi-Language MCP Development — Java + JavaScript

### 4.1 Why Use Multiple Languages?

| Reason | Example |
|--------|---------|
| **Ecosystem access** | Python has better ML/data science libs; use Python for an AI analysis server |
| **Team skills** | Some team members know Java, others know TypeScript |
| **SDK maturity** | Official TypeScript SDK is most mature; use it for new greenfield servers |
| **Legacy integration** | Existing Java service → wrap with Java MCP server |
| **Performance** | CPU-intensive tasks → Java/Rust; rapid prototyping → Python |
| **Runtime constraints** | Browser automation → Node.js (Puppeteer); data processing → Python |

### 4.2 The MCP Language Matrix

```
               PROTOTYPING  ENTERPRISE  ML/DATA  BROWSER  LOWEST DEPS
TypeScript         ⭐⭐⭐        ⭐⭐         ⭐       ⭐⭐⭐       ⭐⭐
Python             ⭐⭐⭐        ⭐⭐         ⭐⭐⭐     ⭐          ⭐⭐
Java               ⭐           ⭐⭐⭐        ⭐        ⭐          ⭐⭐⭐ ← this project
Go                 ⭐⭐          ⭐⭐⭐        ⭐        ⭐          ⭐⭐⭐
Rust               ⭐            ⭐⭐          ⭐        ⭐          ⭐⭐⭐
```

### 4.3 Project Layout for Multi-Language MCP Project

```
my-mcp-project/
├── mcp-java/              ← Java servers (this project's style)
│   ├── src/
│   │   └── server/
│   │       ├── atlassian/   ← Enterprise Atlassian server
│   │       └── internal/    ← Internal data warehouse server
│   └── build.ps1
│
├── mcp-node/              ← TypeScript/Node.js servers
│   ├── src/
│   │   ├── github-server.ts    ← GitHub (official SDK)
│   │   └── slack-server.ts     ← Slack
│   ├── package.json
│   └── tsconfig.json
│
├── mcp-python/            ← Python servers
│   ├── ml_server.py           ← LLM/ML analysis tools (uses langchain, transformers)
│   ├── data_server.py         ← Pandas, numpy data tools
│   └── requirements.txt
│
└── ai-client-config/
    └── claude_desktop_config.json   ← registers all servers from all languages
```

**Root AI client config connecting all languages:**
```json
{
  "mcpServers": {
    "atlassian":     { "command": "java",   "args": ["-cp", "mcp-java/out", "server.atlassian.AtlassianServer"] },
    "github":        { "command": "node",   "args": ["mcp-node/dist/github-server.js"] },
    "ml-analysis":   { "command": "python", "args": ["mcp-python/ml_server.py"] },
    "data-tools":    { "command": "python", "args": ["-m", "mcp_python.data_server"] }
  }
}
```

### 4.4 Shared Contracts Across Languages

If you want consistent tool names and response formats across Java and TypeScript:

**Option A — Document in Markdown (simple)**
```
tool-contracts/
├── atlassian-tools.md    ← tool names, param formats, response schema
├── search-tools.md
└── common-formats.md
```

**Option B — JSON Schema (structured)**
```
tool-contracts/
├── schemas/
│   ├── search_issues.json    ← JSON Schema for the tool's inputSchema
│   └── create_issue.json
└── generate-stubs.sh         ← generate Java records + TS types from schemas
```

**Option C — OpenAPI / AsyncAPI (advanced)**
Define your tool contracts in OpenAPI 3.x, then generate both Java POJOs
and TypeScript interfaces from the same spec.

---

## 5. LLM APIs and the MCP Connection

### 5.1 The Major LLM Providers

| Provider | Model | API | MCP Integration |
|----------|-------|-----|----------------|
| **Anthropic** | Claude 3.x / Claude Opus | REST API | Native MCP support (Claude Desktop) |
| **OpenAI** | GPT-4o, GPT-4 Turbo, o1 | REST API | Via function calling / custom integration |
| **Google** | Gemini 1.5 Pro, Flash | REST API | Via function calling |
| **Meta** | Llama 3 (open source) | Self-hosted | Via Ollama + custom MCP bridge |
| **Mistral** | Mistral Large, Codestral | REST API | Via function calling |
| **Cohere** | Command R+ | REST API | Via function calling |

### 5.2 How LLMs Use Tools (The Technical Truth)

Under the hood, MCP tools map to **function calling** — a feature all major LLMs support.
When the AI client connects to your MCP server, it translates the MCP tool catalogue
into the LLM's native function-calling format:

**MCP tool definition →**
```json
{ "name": "search_jira_issues", "description": "Search Jira...",
  "inputSchema": { "type": "object", "properties": { "query": {"type":"string"} } } }
```

**Claude API `tools` parameter (same structure, Anthropic format):**
```json
{ "name": "search_jira_issues", "description": "Search Jira...",
  "input_schema": { "type": "object", "properties": { "query": {"type":"string"} } } }
```

**OpenAI API `tools` parameter:**
```json
{ "type": "function", "function": {
    "name": "search_jira_issues", "description": "Search Jira...",
    "parameters": { "type": "object", "properties": { "query": {"type":"string"} } } } }
```

The AI client handles this translation automatically. You write one MCP server;
it works with any AI client that supports MCP.

### 5.3 Calling LLM APIs Directly from an MCP Server

Your MCP server can itself call LLM APIs to add AI-powered features:

**Example: Java MCP server with OpenAI GPT-4 for summarisation**
```java
// In your tool handler — summarise a Confluence page using GPT-4
private String summarisePage(final String pageContent) {
    final var client = HttpClient.newHttpClient();
    final var requestBody = """
        {
          "model": "gpt-4o",
          "messages": [
            {"role": "system", "content": "Summarise this in 3 bullet points."},
            {"role": "user", "content": "%s"}
          ]
        }
        """.formatted(pageContent.replace("\"", "\\\""));

    final var request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.openai.com/v1/chat/completions"))
        .header("Authorization", "Bearer " + openAiKey)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

    final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
    return extractContent(response.body()); // parse "choices[0].message.content"
}
```

This pattern creates a **two-level AI system:**
```
User → AI Client (Claude) → MCP tool call → Java server → OpenAI GPT-4 → enriched result → Claude → User
```

The outer Claude model handles conversation; the inner GPT-4 call handles
specific enrichment (summarisation, classification, translation, etc.).

### 5.4 Self-Hosted LLMs (Ollama)

For offline use or data privacy requirements, use Ollama with local models:

```python
# Python MCP server using Ollama for local AI processing
import ollama

def classify_document(content: str) -> str:
    response = ollama.chat(
        model="llama3.2",
        messages=[{"role": "user", "content": f"Classify this document: {content}"}]
    )
    return response["message"]["content"]
```

Ollama API is OpenAI-compatible, so Python's `openai` library works:
```python
from openai import OpenAI
client = OpenAI(base_url="http://localhost:11434/v1", api_key="ollama")
```

---

## 6. Industry SDKs and Frameworks

### 6.1 LangChain

**What it is:** The most popular framework for building LLM applications.
Provides chains, agents, memory, RAG pipelines, and tool abstractions.

**Languages:** Python (primary), JavaScript/TypeScript

**How it relates to MCP:**
- LangChain's `Tool` abstraction is the equivalent of an MCP tool
- LangChain apps can *call* MCP servers via the MCP client SDK
- Or: you can expose LangChain chains as MCP server tools

**LangChain calling an MCP server (Python):**
```python
from mcp import ClientSession, StdioServerParameters
from mcp.client.stdio import stdio_client
from langchain_core.tools import tool

async def create_mcp_tool(server_params: StdioServerParameters):
    """Wraps an MCP server tool as a LangChain tool."""
    async with stdio_client(server_params) as (read, write):
        async with ClientSession(read, write) as session:
            await session.initialize()
            tools = await session.list_tools()
            # Return LangChain-compatible Tool objects
            return [wrap_as_langchain_tool(t, session) for t in tools.tools]

# Now use in a LangChain agent:
mcp_tools = await create_mcp_tool(StdioServerParameters(
    command="java",
    args=["-cp", "out", "server.atlassian.AtlassianServer"]
))
agent = create_react_agent(llm=ChatOpenAI(), tools=mcp_tools)
```

**Exposing a LangChain chain as an MCP tool (Python):**
```python
from mcp.server import Server
from langchain.chains import RetrievalQA

server = Server("langchain-rag-server")
qa_chain = RetrievalQA.from_chain_type(llm=llm, retriever=vectorstore.as_retriever())

@server.call_tool()
async def call_tool(name: str, arguments: dict):
    if name == "answer_from_documents":
        result = qa_chain.run(arguments["question"])
        return [TextContent(type="text", text=result)]
```

### 6.2 LangChain Key Components You'd Use with MCP

| LangChain Component | What it does | MCP Use Case |
|--------------------|-------------|-------------|
| **ChatModels** | LLM wrappers (GPT-4, Claude, Gemini) | Backend for processing in your MCP server |
| **Tools / StructuredTool** | Function the agent can call | Wrap MCP tools as LangChain tools, or vice versa |
| **Agents** | LLM that picks and calls tools autonomously | ReAct agent calling multiple MCP servers |
| **Memory** | Conversation history management | Cross-turn context in long agent sessions |
| **RAG + VectorStores** | Retrieval-augmented generation | Expose as MCP tool that searches embeddings |
| **Chains** | Sequential LLM pipeline | Expose complex multi-step logic as a single MCP tool |
| **Callbacks** | Tracing, logging, monitoring | Debug what an agent does with your MCP tools |

### 6.3 LlamaIndex

**What it is:** Specialised in RAG (Retrieval-Augmented Generation) and data ingestion.
Best for: search over documents, knowledge bases, structured data query.

**Languages:** Python (primary), TypeScript

```python
from llama_index.core import VectorStoreIndex, SimpleDirectoryReader
from llama_index.core.tools import FunctionTool

# Create an index over your documents
docs = SimpleDirectoryReader("./docs").load_data()
index = VectorStoreIndex.from_documents(docs)
query_engine = index.as_query_engine()

# Expose as MCP tool
@server.call_tool()
async def call_tool(name: str, arguments: dict):
    if name == "search_docs":
        response = query_engine.query(arguments["query"])
        return [TextContent(type="text", text=str(response))]
```

**LlamaIndex vs LangChain:**
- LlamaIndex: better at document ingestion, chunking, RAG, structured data
- LangChain: better at agent orchestration, chains, multi-step reasoning
- Many projects use both

### 6.4 Microsoft Semantic Kernel

**What it is:** Microsoft's SDK for AI orchestration, used heavily in .NET and C# enterprise.
Also available for Python and Java.

```csharp
// C# — Semantic Kernel calling MCP tools
var kernel = Kernel.CreateBuilder()
    .AddOpenAIChatCompletion("gpt-4o", apiKey)
    .Build();

// Import MCP tools into Semantic Kernel as functions
var mcpPlugin = await kernel.ImportMcpServerAsync(
    serverName: "atlassian",
    command: "java",
    args: ["-cp", "out", "server.atlassian.AtlassianServer"]
);

// Now use in a plan
var result = await kernel.InvokePromptAsync(
    "Find all open Jira bugs in PAYMENT project and summarise them",
    new KernelArguments()
);
```

**Java Semantic Kernel (Preview):**
```java
// Microsoft Semantic Kernel for Java
Kernel kernel = Kernel.builder()
    .withAIService(ChatCompletionService.class, 
        new OpenAIChatCompletion("gpt-4o", apiKey))
    .build();
```

### 6.5 Haystack (deepset)

**What it is:** Python framework for building search and RAG pipelines.

```python
from haystack import Pipeline
from haystack.components.retrievers import InMemoryBM25Retriever

# Wire your MCP server's search results into a Haystack pipeline
pipeline = Pipeline()
pipeline.add_component("retriever", InMemoryBM25Retriever(document_store))
pipeline.add_component("llm", OpenAIChatGenerator(model="gpt-4o"))
pipeline.connect("retriever.documents", "llm.documents")
```

### 6.6 OpenAI Functions vs MCP vs LangChain Tools — Comparison

| | OpenAI Function Calling | MCP Tools | LangChain Tools |
|-|------------------------|-----------|-----------------|
| **Protocol** | OpenAI-specific JSON | Open standard (JSON-RPC 2.0) | Framework-internal Python |
| **Transport** | REST API call | STDIO / SSE / HTTP | In-process function call |
| **Language** | Any (HTTP client) | Any (STDIO process) | Python / TypeScript |
| **Multi-model** | OpenAI only | Any AI client | Any LLM via LangChain |
| **Tool isolation** | In-process | Separate process | In-process |
| **Secret isolation** | Same process | Own process (safe) | Same process |
| **Reusability** | OpenAI only | Any MCP client | Python/TS apps only |

**MCP's key advantage:** tools run as **isolated processes** — your Java MCP server
runs in its own JVM, completely separate from the AI client. Secrets never touch
the AI client's memory.

---

## 7. Agent Frameworks and MCP

### 7.1 What is an Agent?

An agent is an AI model in a loop — it calls tools, observes results, decides
the next action, and repeats until the goal is reached. MCP servers provide
the tools that agents use.

```
User request → Agent (LLM in loop)
                    │
                    ├─ tool call 1 (search_jira)
                    │       └─ result: [3 issues]
                    │
                    ├─ tool call 2 (get_jira_issue "PAY-101")
                    │       └─ result: full issue details
                    │
                    ├─ reasoning: "I should also check related Confluence page"
                    │
                    ├─ tool call 3 (search_confluence "payment service")
                    │       └─ result: [2 pages]
                    │
                    └─ final response: "Here's the summary of open payment bugs..."
```

### 7.2 Agent Frameworks That Work with MCP

**LangChain Agents (Python)**
```python
from langchain.agents import AgentExecutor, create_react_agent
from langchain_openai import ChatOpenAI

# ReAct agent — Reason + Act loop
agent = create_react_agent(
    llm=ChatOpenAI(model="gpt-4o"),
    tools=mcp_tools,  # tools converted from MCP server
    prompt=react_prompt
)
executor = AgentExecutor(agent=agent, tools=mcp_tools, verbose=True)
result = executor.invoke({"input": "Find all critical Jira bugs in our payment service"})
```

**AutoGPT / BabyAGI Pattern**
These are "autonomous agents" that set their own goals and sub-goals:
```python
# Simplified AutoGPT loop
task_list = ["Find open Jira bugs", "Categorise by severity", "Write summary report"]
while task_list:
    task = task_list.pop(0)
    result = agent.execute(task, tools=mcp_tools)
    new_tasks = agent.create_tasks_from_result(result)
    task_list.extend(new_tasks)
```

**CrewAI (Multi-Agent)**
Multiple specialised agents collaborating, each with their own MCP tool subset:
```python
from crewai import Agent, Task, Crew

jira_agent = Agent(
    role="Jira Analyst",
    tools=[jira_search_tool, jira_create_tool],  # from MCP
    llm=ChatOpenAI(model="gpt-4o")
)
confluence_agent = Agent(
    role="Documentation Writer",
    tools=[confluence_search_tool, confluence_create_tool],  # from MCP
    llm=Claude3Sonnet()
)
crew = Crew(agents=[jira_agent, confluence_agent], tasks=[task1, task2])
crew.kickoff()
```

**LangGraph (Stateful Agent)**
LangGraph models agent state as a graph — ideal for long-running, stateful workflows:
```python
from langgraph.graph import StateGraph, END

workflow = StateGraph(AgentState)
workflow.add_node("jira_lookup",   call_jira_mcp_tool)
workflow.add_node("confluence_lookup", call_confluence_mcp_tool)
workflow.add_node("summarize",     call_summarize_tool)
workflow.add_edge("jira_lookup", "confluence_lookup")
workflow.add_edge("confluence_lookup", "summarize")
workflow.add_edge("summarize", END)

app = workflow.compile()
result = app.invoke({"query": "Find and summarise open payment bugs"})
```

### 7.3 Building Your Own Simple Agent in Java

For Java devs who want to understand agent loops without a framework:

```java
// Simplified agent loop in Java (using OpenAI API directly)
public class SimpleAgent {

    private final String openAiKey;
    private final McpClient mcpClient; // wraps STDIO calls to your MCP server

    public String run(final String userGoal) {
        final var messages = new ArrayList<Message>();
        messages.add(new Message("user", userGoal));

        while (true) {
            // Call LLM with current messages + tool definitions
            final var llmResponse = callOpenAI(messages, getToolDefinitions());

            if (llmResponse.isFinished()) {
                return llmResponse.content();
            }

            if (llmResponse.hasToolCall()) {
                // Execute the tool call via MCP server
                final var toolResult = mcpClient.call(
                    llmResponse.toolName(),
                    llmResponse.toolArgs()
                );
                // Add results to message history
                messages.add(new Message("tool", toolResult));
            }
        }
    }
}
```

---

## 8. Best-of-All-Worlds Architecture

### 8.1 The Reference Architecture

This architecture uses the right language for each job:

```
┌─────────────────────────── AI CLIENT ───────────────────────────┐
│            Claude Desktop / VS Code / Continue.dev               │
└───────────────────────────────┬─────────────────────────────────┘
                                │ MCP (JSON-RPC)
         ┌──────────────────────┼──────────────────────┐
         ▼                      ▼                      ▼
┌────────────────┐   ┌────────────────────┐   ┌──────────────────┐
│  Java Servers  │   │  Python Servers    │   │  Node.js Servers │
│                │   │                   │   │                  │
│ Atlassian MCP  │   │  ML-Analysis MCP  │   │  GitHub MCP      │
│  (enterprise,  │   │  (LangChain +     │   │  (official SDK,  │
│  complex auth, │   │  transformers,    │   │  fastest to ship) │
│  no npm needed)│   │  local Ollama)    │   │                  │
│                │   │                   │   │  Slack MCP       │
│ Learning-Res   │   │  Doc-QA MCP       │   │  (Node Slack SDK) │
│  MCP (search   │   │  (LlamaIndex RAG  │   │                  │
│  engine)       │   │  over Confluence) │   │  Filesystem MCP  │
└────────┬───────┘   └────────┬──────────┘   └──────┬───────────┘
         │                    │                      │
     Atlassian APIs       OpenAI / Ollama         GitHub API
     Java ecosystem       vectorstore DB           Slack API
     (no Node needed)     Python ML libs           npm ecosystem
```

**Why each language in its right place:**

| Server | Language | Why here |
|--------|----------|---------|
| Atlassian | Java | Complex auth, Java REST client, no npm needed, fits enterprise Maven/Gradle |
| Learning Resources | Java | Self-contained, no deps, search engine in Java |
| ML Analysis | Python | `transformers`, `torch`, `scikit-learn` — Python-only ecosystem |
| Doc QA (RAG) | Python | `llama_index`, `langchain`, `faiss` — Python-first |
| GitHub | Node.js | Official MCP SDK is TypeScript; official GitHub SDK is Node |
| Slack | Node.js | Slack Bolt SDK is Node-native |
| Filesystem | Node.js | Official MCP server from Anthropic is Node |

### 8.2 Inter-Server Communication Patterns

MCP servers are isolated processes — they cannot directly call each other.
But the **AI model** orchestrates between them. Three patterns:

**Pattern 1 — AI Orchestration (simplest)**
Let the AI call each server in turn. No extra code needed.
```
User: "Find open bugs and create a confluence report"
AI: search_jira_issues("open bugs") → [3 bugs]
AI: create_confluence_page("Bug Report", format(3 bugs)) → page created
```

**Pattern 2 — Aggregator MCP Server**
Write one MCP server that internally calls two others:
```java
// AggregatorServer.java — one tool that internally does multi-server logic
public String handleToolCall(String toolName, Map<String, String> args) {
    if ("create_bug_report".equals(toolName)) {
        // 1. Call Atlassian MCP server internally
        var bugs = jiraClient.searchIssues(args.get("jql"));
        // 2. Format as Confluence content
        var pageContent = confluenceFormatter.format(bugs);
        // 3. Create page via Confluence directly
        return confluenceClient.createPage(args.get("spaceKey"), "Bug Report", pageContent);
    }
}
```

**Pattern 3 — Shared Message Bus**
For complex multi-agent systems:
```
MCP Server A → publishes to Redis Pub/Sub
MCP Server B → subscribes from Redis Pub/Sub
```
This breaks MCP's design philosophy (stateless tools) and is only for advanced cases.

### 8.3 When to Use What Language

**Start with Java (this project's pattern) when:**
- You're calling enterprise Java APIs
- You need zero npm/Node dependencies
- Type safety and long-term maintainability matter more than velocity
- The team is primarily Java
- You want the search-engine library (BM25, ConfigurableSearchEngine)

**Add TypeScript when:**
- You need an official MCP server from Anthropic's registry (npm package)
- You're building browser automation tools (Puppeteer, Playwright)
- The tool is simple and needs to ship fast
- You're integrating with Node.js-first services (Slack, Discord, etc.)

**Add Python when:**
- You need ML/AI capabilities (transformers, PyTorch, scikit-learn)
- You're building RAG pipelines (LangChain, LlamaIndex, Haystack)
- You're using LLM APIs directly (most Python-first in docs and support)
- Data processing is involved (pandas, numpy, spark)

---

## 9. Practical Recipes

### Recipe 1 — Learning + Atlassian Combined Workflow

```
"I want to learn about microservices. Also find any Jira tickets we have
 on the topic so I know what we've already tried."

AI:
1. learning-resources::search_resources("microservices") → 5 resources
2. atlassian::search_jira_issues("microservices") → 3 tickets
3. Synthesises: "Here are 5 learning resources + 3 related tickets in your Jira…"
```

### Recipe 2 — Multi-Version Atlassian Comparison

```bash
# Register v1 and v2 both in claude_desktop_config.json
# Then in conversation:

"Using atlassian, search for PAY-101"
→ [v1 response]

"Using atlassian-v2, search for the same issue"
→ [v2 response — compare formatting, fields, speed]
```

### Recipe 3 — Java Server + Python RAG Server

```python
# mcp-python/rag_server.py — Python MCP server with LlamaIndex RAG
from mcp.server import Server
from llama_index.core import VectorStoreIndex, SimpleDirectoryReader

server = Server("confluence-rag")
docs = SimpleDirectoryReader("./confluence-export").load_data()
index = VectorStoreIndex.from_documents(docs)

@server.call_tool()
async def call_tool(name, arguments):
    if name == "search_confluence_semantic":
        result = index.as_query_engine().query(arguments["query"])
        return [{"type": "text", "text": str(result)}]
```

```json
// Register alongside Java Atlassian server
{
  "mcpServers": {
    "atlassian": { "command": "java", "args": ["-cp", "out", "server.atlassian.AtlassianServer"] },
    "confluence-rag": { "command": "python", "args": ["mcp-python/rag_server.py"] }
  }
}
```

Now the AI can use keyword search via Atlassian MCP AND semantic search via
the Python RAG server — best of both worlds.

### Recipe 4 — Agent with Multi-Language Tools

```python
# LangChain agent using tools from all 3 languages
from langchain_openai import ChatOpenAI
from langchain.agents import AgentExecutor, create_tool_calling_agent
from mcp_langchain import McpToolkit

# Connect to all MCP servers
java_tools  = await McpToolkit.from_server("java", ["-cp", "out", "server.atlassian.AtlassianServer"])
node_tools  = await McpToolkit.from_server("npx", ["-y", "@modelcontextprotocol/server-github"],
                                            env={"GITHUB_TOKEN": os.getenv("GITHUB_TOKEN")})
py_tools    = await McpToolkit.from_server("python", ["mcp-python/rag_server.py"])

all_tools = java_tools + node_tools + py_tools

agent = create_tool_calling_agent(
    llm=ChatOpenAI(model="gpt-4o"),
    tools=all_tools
)
executor = AgentExecutor(agent=agent, tools=all_tools)
result = executor.invoke({
    "input": "Find the Jira issue PAY-101, search the GitHub PR linked to it, "
             "and find learning resources on the same topic"
})
```

---

## Summary — Decision Guide

```
You want to...                              → Use this approach
──────────────────────────────────────────────────────────────────────
Expose Java APIs as AI tools               → Java MCP server (this project)
Quick MVP server in 30 minutes             → TypeScript + @modelcontextprotocol/sdk
Add ML/NLP to your tools                  → Python MCP server + langchain
Semantic search over your documents       → Python + LlamaIndex RAG MCP server
Run multiple LLM providers                → LangChain abstraction layer
Build autonomous multi-step agent         → LangChain ReAct / LangGraph
Orchestrate multiple agents               → CrewAI or LangGraph multi-agent
Use Atlassian + GitHub + docs together    → Register all 3 servers in AI client
Keep secrets out of AI's reach            → MCP's process isolation (default benefit)
Run offline / air-gapped                  → Java MCP + Ollama (Llama3 local LLM)
Enterprise .NET integration               → Microsoft Semantic Kernel
```

---

*See also:*
- [mcp-how-it-works.md](mcp-how-it-works.md) — protocol internals
- [mcp-implementations.md](mcp-implementations.md) — Java vs TypeScript code comparison
- [versioning-guide.md](versioning-guide.md) — adding v2 server versions
- [local-setup-guide.md](local-setup-guide.md) — local config files and secrets management
- [architecture-overview.md](architecture-overview.md) — this project's module architecture
