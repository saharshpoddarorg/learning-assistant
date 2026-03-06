# MCP Servers vs Copilot Skills — When to Use Which

> **Purpose:** A complete decision guide for choosing between MCP servers and Copilot skills,
> with a worked migration playbook and translation patterns from this repo's own servers.

---

## At a Glance

| | MCP Server | Copilot Skill (SKILL.md) |
|---|---|---|
| **What it is** | A running Java process that Copilot calls as a tool | A markdown file Copilot reads for domain knowledge |
| **Runtime** | Always-on process (Java server, stdio/SSE) | No runtime — loaded from file on demand |
| **Data** | Live, dynamic, from APIs / databases / computation | Static, curated, embedded in the markdown file |
| **Activation** | Copilot calls it via tool call (explicit, traced) | Copilot loads it when query matches description |
| **Auth** | Can handle OAuth, API tokens, session state | No auth — pure text content |
| **Latency** | Tool call adds a round-trip (seconds) | Zero latency — already in context |
| **Maintenance** | Java code, build step, deployment | Edit a markdown file |
| **Token cost** | Tool result is injected into context | Skill content is injected into context |
| **Output** | JSON / structured data, can vary per call | Same content every time |
| **Best for** | Jira tickets, GitHub PRs, live search, CRUD | Reference docs, cheatsheets, curated lists |

---

## Tier 1 — Newbie: The Core Rule

> **Does the content change between calls with the same input?**
> - **Yes (live data)** → MCP Server
> - **No (always the same)** → Copilot Skill

Examples:
- "Get the Jira ticket PROJ-123" → Changes when someone edits the ticket → **MCP**
- "What are the Java stream operations?" → Same every time → **Skill**
- "Search curated learning resources for concurrency" → Could return different scores but same vault → **Skill or partial MCP**
- "List open PRs on this repo" → Changes continuously → **MCP**

---

## Tier 2 — Amateur: Decision Matrix

### Full Comparison Table

| Criterion | → Copilot Skill | → MCP Server |
|---|---|---|
| Data source | Static Java constants, hardcoded lists | External API, live database |
| Output variability | Same input = same output | Changes over time |
| Computation | Simple lookup / filtering of small set | Complex scoring, ranking, aggregation |
| Auth required | Never | OAuth, API tokens, session-based |
| State | Stateless | Stateful (session, CRUD, auth flow) |
| Volume | < 500 lines of content | Thousands of records / dynamic size |
| Update frequency | Rarely (manual PR edit) | Continuously or real-time |
| Primary use case | Teaching, reference, guide | Operational task execution |
| Build dependency | None (just a .md file) | Java build required |
| Offline capable | ✅ Yes | ❌ No (needs server process) |

### Decision Flowchart

```text
Does the content come from an external API/database?
  YES → MCP Server

Does the content change between requests (even with same input)?
  YES → MCP Server

Does the tool require user authentication?
  YES → MCP Server

Is there complex computation (BM25, ranking, aggregation)?
  YES → MCP Server (or keep in MCP; expose summary in skill)

Does the content fit in < 500 readable lines?
  YES → Skill is viable

Is the content educational/reference that rarely changes?
  YES → Copilot Skill

Can't decide? → Start with Skill, add MCP only if you need live data
```

### Partial Migration Pattern

Some MCP tools are hybrids. The best pattern:

```text
MCP server: handles search + scoring + live computation
Skill file: contains curated examples, quick reference, and domain knowledge

User gets both:
  - Skill loaded automatically for context/guidance
  - MCP tool called when Copilot needs live search results
```

---

## Tier 3 — Pro: Migration Playbook

### How MCP Capabilities Translate to Skills

| MCP Capability | Skill Equivalent | Notes |
|---|---|---|
| `list_resources(category)` | H2 section per category with curated list | Direct 1:1 if items are static |
| `get_server_info()` | Metadata table at top of skill | Static description of what it covers |
| `get_resource(id)` | Direct link `[title](url)` in relevant section | Only if URL is stable |
| `search_with_bm25(query)` | Copilot's own semantic search (no tool needed) | Copilot finds relevant skill sections |
| `recommend(level, category)` | Table: Newbie / Amateur / Pro resource rows | 3-tier structure matches |
| Static enum values | Table: value, description, when to use | Direct embedding |
| Config map / defaults | YAML code block in skill | Direct copy from Java source |
| Error message lookup | Table in skill | Direct copy |
| HTTP API call | ❌ Cannot migrate — stays in MCP | Needs live data |
| OAuth flow | ❌ Cannot migrate — stays in MCP | Needs auth state |
| File system CRUD | ❌ Cannot migrate — stays in MCP | Needs write access |

### Migration Workflow

```text
Step 1 — Audit the MCP server
  Read: mcp-servers/src/server/<name>/
  List all tools in handleCallTool()
  For each tool: static or dynamic? Classify using decision matrix.

Step 2 — Extract static content
  Copy the static Java data (enums, constants, hardcoded lists) into skill sections
  Convert Java enum → Markdown table
  Convert Java constants → Code blocks or tables
  Convert item lists → Bullet points with descriptions

Step 3 — Write the SKILL.md
  Use /mcp-to-skill prompt to generate the scaffold
  Add 3-tier structure: newbie (commands/essentials) → amateur (patterns) → pro (advanced)
  Add description field that will trigger auto-activation

Step 4 — Register the skill
  Add to copilot-instructions.md skills block
  Update slash-commands.md if applicable
  Update hub.prompt.md navigation tree

Step 5 — Decide MCP fate
  Option A — Retire the MCP tool if fully replaced
  Option B — Keep MCP for live computation, skill for reference (recommended for search)
  Option C — Keep MCP tool + skill = best of both worlds

Step 6 — Test
  Ask Copilot questions that should activate the skill
  Verify skill content appears in responses
  Verify MCP tool still works for live queries
  Run: /check-standards skill-file .github/skills/<name>/SKILL.md
```

### Worked Example — Learning Resources Server

This repo's `learning-resources` MCP server is a perfect example of the hybrid pattern:

#### What stays in MCP (dynamic)

```java
// Complex BM25 search + multi-dimension scoring — too dynamic for a skill
handleSearch() → BM25Scorer, RelevanceEngine, FreshnessScorer
// Returns differently ranked results depending on query + context
```

#### What could move to a skill (static reference)

```java
// Static enum definitions — perfect for a skill table
enum ConceptArea { ALGORITHMS, CONCURRENCY, SYSTEM_DESIGN, ... }
enum ResourceCategory { OFFICIAL_DOCS, VIDEO, BOOK, TUTORIAL, ... }
// Curated resource highlights — example items from vault
```

**Result:** Keep the MCP server for live search; create a skill section with:
- ConceptArea and ResourceCategory tables (for knowing what to search for)
- Top 5 resources per category (hand-curated highlights)
- How to use the MCP tools effectively

This is the recommended pattern for **search-based MCP servers**.

#### What fully moves to skill (purely static)

```java
// Server metadata — never changes
handleGetServerInfo() → "Learning Resources v1.0, 500+ resources"
```

This becomes a one-liner in the skill: "Use the `learning-resources` MCP server to search 500+ curated resources."

---

## Anti-Patterns

| Anti-pattern | Problem | Better approach |
|---|---|---|
| Putting live API data in a skill | Skill becomes stale immediately | Keep in MCP |
| Building an MCP tool for static reference text | Overengineering + build overhead | Use a skill |
| Migrating everything to skill | Skills can't call APIs or handle auth | Keep dynamic tools in MCP |
| Duplicating content between MCP and skill | Maintenance burden; drift | Single source of truth |
| No skill for an MCP server | Users can't learn the domain without calling a tool | Add a companion skill |
| Skill with 1000+ lines | Too large to load efficiently | Split into multiple focused skills |

---

## Scripts & Alias Support

Use the `/mcp-to-skill` slash command to automate the analysis:

```markdown
# In Copilot Chat:
/mcp-to-skill
→ target: src/server/learningresources/
→ mode: full
→ outputPath: .github/skills/learning-resources/SKILL.md
```

After generating:

```powershell
# Build still needed only if MCP server stays active
.\mcp-servers\build.ps1

# Verify skill is loadable (check YAML frontmatter)
Get-Content .github/skills/learning-resources/SKILL.md | Select-Object -First 10
```

---

## When NOT to Migrate

Some MCP servers should NEVER become skills:

| Server | Reason to keep as MCP |
|---|---|
| `atlassian` | Jira/Confluence/Bitbucket — live API, OAuth, real-time data |
| GitHub MCP | Live repo data, PR state, issue tracking |
| Filesystem MCP | File read/write — needs actual access |
| Any future database MCP | SQL queries require live connection |

**Rule:** If the tool's value disappears when you can't make a network call, it's an MCP concern — not a skill concern.

---

## Reference Files in This Repo

| File | Purpose |
|---|---|
| `mcp-servers/src/server/learningresources/` | Learning Resources MCP server (Java) |
| `mcp-servers/src/server/atlassian/` | Atlassian MCP server (Java) |
| `.github/skills/java-learning-resources/SKILL.md` | Example skill (curated Java resources) |
| `.github/skills/mcp-development/SKILL.md` | MCP development reference skill |
| `.github/docs/mcp-servers-architecture.md` | MCP server architecture deep-dive |
| `.github/docs/mcp-how-it-works.md` | Protocol-level MCP explanation |
| `.github/prompts/mcp-to-skill.prompt.md` | Automated migration prompt |
| `.vscode/mcp.json` | MCP server registry for VS Code |
