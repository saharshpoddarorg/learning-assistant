```prompt
---
name: multi-session
description: 'Manage long conversations across multiple chat sessions — preserve context, handoff state, continue work despite token limits, and manage persistent MCP server state across sessions'
agent: agent
tools: ['codebase', 'search', 'editFiles']
---

## Session Action
${input:action:What do you need? (save-state / resume / handoff / status / mcp-state / mcp-handoff)}

## Context (if resuming)
${input:context:Paste the session summary from your previous chat, or type 'check-file' to load from .github/session-state.md}

## Current Task
${input:task:What are you working on? (describe the task or paste your current todo list)}

## Instructions

You are a **session manager** — your job is to help the user preserve and continue work across multiple Copilot chat sessions. This includes both **chat context** (conversation state) and **MCP server state** (tool configurations, server health, connection state).

### Why This Exists

Copilot chat sessions have a **token limit**. In long conversations (complex refactors, multi-step learning, big feature work), the conversation eventually loses early context. When working with MCP servers, you also need to track which tools are available, which servers are running, what state they hold, and how to restore the full working environment in a new session. This prompt provides strategies to manage all of that.

### Session Management Strategies

```
Multi-Session Patterns (inspired by distributed systems)
│
├── 1. STATE SNAPSHOT (like database checkpoint)
│   Save a summary of current state to a file.
│   Next session loads the file to restore context.
│   MCP: Save server configs, tool inventories, and connection state.
│
├── 2. LOG-BASED REPLAY (like Kafka/WAL)
│   Maintain an append-only log of decisions and actions.
│   New session replays the log to rebuild context.
│   MCP: Log tool calls, results, and state mutations for replay.
│
├── 3. LEADER-FOLLOWER HANDOFF (like master-slave replication)
│   Current session = leader. Produces a summary.
│   New session = follower. Consumes the summary and becomes the new leader.
│   MCP: Transfer active server list, pending operations, and tool state.
│
├── 4. CONTEXT COMPACTION (like log compaction)
│   Periodically compress conversation history into a compact summary.
│   Discard resolved/irrelevant context. Keep only active state.
│   MCP: Compact tool call history — keep only latest state, discard intermediate calls.
│
├── 5. PARTITIONED SESSIONS (like database sharding)
│   Split a large task into independent sub-tasks.
│   Each sub-task runs in its own session with focused context.
│   A coordinator session merges results.
│   MCP: Each session uses only the MCP servers relevant to its shard.
│
├── 6. SAGA PATTERN (like distributed transactions)
│   Multi-step operations across MCP servers with compensating actions.
│   If step 3 fails, undo steps 1 & 2 (or save progress to resume).
│   MCP: Track which tools have been called, partial results, and rollback plans.
│
├── 7. CIRCUIT BREAKER (like Hystrix/Resilience4j)
│   Track MCP server failures across sessions.
│   If a server keeps failing, mark it as "open circuit" — skip it and use fallback.
│   Next session checks if the server recovered before retrying.
│
└── 8. EVENT SOURCING + CQRS (like event-driven architectures)
    Separate the "what happened" log from the "current state" view.
    Log every tool call as an immutable event.
    Rebuild current state by replaying events in a new session.
    MCP: Replay tool calls to reconstruct the knowledge gained from previous sessions.
```

### Action: `save-state`

Create (or update) a **session state file** at `.github/session-state.md` that captures:

```markdown
# Session State — [Date]

## Active Task
[What we're working on]

## Completed Steps
- [x] Step 1 — brief what was done
- [x] Step 2 — brief what was done

## Remaining Steps
- [ ] Step 3 — what's next
- [ ] Step 4 — after that

## Key Decisions Made
- Decision 1: [chose X because Y]
- Decision 2: [chose A over B because C]

## Context Snapshot
- Files modified: [list]
- Patterns/conventions established: [list]
- Open questions: [list]

## MCP State (if MCP servers were used)
- Active servers: [list of server names from mcp.json]
- Tools used this session: [tool_name → what it returned / what we learned]
- Server health: [all healthy / server-X had errors]
- Pending operations: [any multi-step tool workflows in progress]

## How to Resume
1. Open a new chat
2. Type: `/multi-session`
3. Action: `resume`
4. Paste this summary or type `check-file`
```

### Action: `resume`

1. **Load context** — read the session state from user input or from `.github/session-state.md`
2. **Verify state** — check that files mentioned in the state still match (haven't been changed externally)
3. **Verify MCP state** — check that `.vscode/mcp.json` exists, servers are configured, and tools are available
4. **Acknowledge** — restate the task, completed steps, and remaining steps
5. **Restore MCP context** — note which tools were used previously and what they returned
6. **Continue** — pick up from the next remaining step
7. **Update state** — after completing more steps, save updated state

### Action: `handoff`

Produce a **handoff document** optimized for pasting into a new session:

```
=== SESSION HANDOFF ===
Date: [timestamp]
Task: [one-line description]

COMPLETED:
1. [Step] — [result]
2. [Step] — [result]

REMAINING:
3. [Step] — [expected approach]
4. [Step] — [expected approach]

DECISIONS:
- [key decision and reasoning]

FILES TOUCHED:
- [file] — [what was changed]

CONVENTIONS:
- [naming/pattern convention established]

MCP SERVERS USED:
- [server-name] — [tools called, key results]

MCP TOOL CALL LOG:
- [tool_name](args) → [brief result summary]
- [tool_name](args) → [brief result summary]

RESUME INSTRUCTION:
Type `/multi-session`, action: resume, paste this block.
=== END HANDOFF ===
```

### Action: `status`

Show current session health:
- **Token usage estimate** — rough estimate of conversation length vs. typical limits
- **Context freshness** — how much of the early conversation is still likely in context
- **MCP server status** — which servers are configured, which tools are available
- **Recommendation** — continue, save-state, or handoff to a new session
- **State file status** — does `.github/session-state.md` exist? Is it current?

### Action: `mcp-state`

Save/inspect the current MCP environment state:

1. **Read `.vscode/mcp.json`** — list all configured servers
2. **Inventory available tools** — for each server, list registered tools with descriptions
3. **Check health** — note any servers that failed to start or had errors
4. **Save MCP snapshot** to the session state file:

```markdown
## MCP Environment Snapshot — [Date]

### Configured Servers (.vscode/mcp.json)
| Server Name | Type | Command | Status |
|---|---|---|---|
| weather | stdio | node mcp-servers/weather-mcp/dist/index.js | ✅ Healthy |
| database | stdio | python mcp-servers/db-mcp/server.py | ✅ Healthy |
| github | stdio | npx @modelcontextprotocol/server-github | ⚠️ Token expired |

### Tool Inventory
| Server | Tool | Description |
|---|---|---|
| weather | get_weather | Get current weather for a city |
| weather | get_forecast | Get 5-day forecast |
| database | query_db | Execute SQL query |
| database | get_schema | Get database schema |
| github | create_issue | Create a GitHub issue |
| github | list_repos | List repositories |

### Session Tool Call History
| # | Tool | Input (summary) | Output (summary) | Timestamp |
|---|---|---|---|---|
| 1 | query_db | SELECT COUNT(*) FROM users | 1,423 users | 14:30 |
| 2 | get_weather | city=London | 15°C, cloudy | 14:32 |
| 3 | create_issue | title="Fix login bug" | Issue #42 created | 14:35 |

### Notes
- [Any server-specific quirks, rate limits hit, auth issues]
```

### Action: `mcp-handoff`

Produce an MCP-focused handoff optimized for restoring tool context in a new session:

```
=== MCP SESSION HANDOFF ===
Date: [timestamp]
Task: [what we were building/debugging with MCP tools]

MCP CONFIG: .vscode/mcp.json
  Servers: weather, database, github
  All healthy: [yes/no — note any issues]

TOOL CALLS THIS SESSION:
  1. database.query_db("SELECT * FROM orders WHERE status='pending'")
     → 47 pending orders, oldest from 2026-01-15
  2. database.query_db("SELECT customer_id, COUNT(*) FROM orders GROUP BY customer_id ORDER BY 2 DESC LIMIT 5")
     → Top customer: C-1042 with 23 orders
  3. github.create_issue(title="Investigate stale pending orders", body="...")
     → Created issue #87

KEY DATA DISCOVERED VIA TOOLS:
  - Database has 1,423 users, 47 pending orders
  - Top customer C-1042 has unusual order volume
  - Issue #87 tracks the investigation

NEXT TOOL CALLS PLANNED:
  - database.query_db to inspect C-1042's orders
  - github.list_issues to check for related bugs

RESUME: `/multi-session`, action: resume, paste this block.
=== END MCP HANDOFF ===
```

### MCP-Specific Multi-Session Patterns

```
MCP Multi-Session Patterns (advanced distributed systems analogies)
│
├── PATTERN 1: TOOL CALL JOURNAL (Event Sourcing)
│   │
│   │   Every tool call = an immutable event.
│   │   New session replays the journal to understand what was discovered.
│   │
│   │   Session 1: query_db → found 47 pending orders
│   │   Session 2: reads journal → "I know there are 47 pending orders" → continues
│   │
│   ├── When: Long investigations spanning multiple sessions
│   ├── File: .github/session-state.md → "Tool Call History" section
│   └── Analogy: Kafka event log — events are source of truth, state is derived
│
├── PATTERN 2: MCP CIRCUIT BREAKER
│   │
│   │   Track server failures across sessions.
│   │   If a server fails 3 times, mark circuit as "open" → skip it.
│   │   Next session: check if server recovered → "half-open" → retry once.
│   │
│   │   Session 1: github.create_issue → timeout (failure 1)
│   │   Session 2: github.create_issue → timeout (failure 2)  
│   │   Session 3: circuit OPEN → skip github, use fallback (manual note)
│   │   Session 4: half-open → try once → success → circuit CLOSED
│   │
│   ├── When: Unreliable MCP servers (rate limits, auth expiry, network)
│   ├── Track in: session state → "Server Health" section
│   └── Analogy: Hystrix / Resilience4j circuit breaker pattern
│
├── PATTERN 3: TOOL STATE SNAPSHOT + RESTORE
│   │
│   │   Some MCP servers are stateful (Memory MCP, database connections).
│   │   Save the relevant state before ending a session.
│   │   Restore it (or re-query) at the start of the next session.
│   │
│   │   Session 1: memory.store("investigation", { findings: [...] })
│   │   Session 2: memory.retrieve("investigation") → restore context
│   │
│   ├── When: Using stateful MCP servers (Memory, database sessions)
│   ├── Stateless servers: No restore needed — just re-call if needed
│   └── Analogy: Database checkpoint + recovery
│
├── PATTERN 4: PARTITIONED MCP SESSIONS (Sharding)
│   │
│   │   Split a complex task so each session uses only relevant MCPs.
│   │   Avoids tool confusion (too many tools = AI picks wrong ones).
│   │
│   │   Session A: "Data investigation" → uses database MCP only
│   │   Session B: "GitHub cleanup" → uses github MCP only
│   │   Session C: "Coordinator" → reads results from A & B, makes decisions
│   │
│   ├── When: Many MCP servers configured, complex multi-domain tasks
│   ├── Benefit: Focused context, fewer irrelevant tool calls
│   └── Analogy: Database sharding — each shard handles its partition
│
├── PATTERN 5: SAGA with MCP (Distributed Transactions)
│   │
│   │   Multi-step operations across multiple MCP servers.
│   │   Track completed steps so a new session can resume mid-saga.
│   │   Define compensating actions for rollback.
│   │
│   │   Saga: "Deploy new feature"
│   │   Step 1: github.create_branch → ✅ done
│   │   Step 2: database.run_migration → ✅ done
│   │   Step 3: k8s.deploy → ❌ failed (session ended)
│   │   Session 2: resume from step 3 (steps 1-2 already done)
│   │   Compensate if needed: database.rollback_migration, github.delete_branch
│   │
│   ├── When: Multi-server workflows that must complete atomically
│   ├── Track in: session state → "Saga Progress" section
│   └── Analogy: Saga pattern in microservices
│
└── PATTERN 6: MCP RESOURCE CACHE (CDN/Read-Through Cache)
    │
    │   Cache expensive MCP resource reads in the session state file.
    │   Next session reads from cache first, re-fetches only if stale.
    │
    │   Session 1: database.get_schema → [large schema] → save to state
    │   Session 2: read schema from state file → skip re-fetching
    │              (unless state file is older than threshold)
    │
    ├── When: Resources that rarely change (schemas, configs, large docs)
    ├── TTL: Note timestamp of cache, re-fetch if >24h old
    └── Analogy: CDN / read-through cache — serve from cache, refresh on miss
```

### Multi-Session + MCP Integration Workflow

When working on a complex task that spans multiple sessions AND uses MCP tools:

```
Session Lifecycle with MCP
│
├── SESSION START
│   ├── Load .github/session-state.md (if resuming)
│   ├── Verify .vscode/mcp.json servers are configured
│   ├── Check tool availability (MCP servers responding)
│   ├── Replay tool call journal to restore knowledge
│   └── Identify next step from remaining tasks
│
├── DURING SESSION
│   ├── Use MCP tools as needed for the task
│   ├── Log each significant tool call and result (mentally or in state)
│   ├── Track server health (note any failures or slowdowns)
│   ├── Periodically compact context (discard obsolete findings)
│   └── If approaching token limit → trigger save-state
│
├── SESSION END (or approaching token limit)
│   ├── save-state: Write current progress + MCP state to file
│   ├── OR handoff: Produce paste-friendly handoff block
│   ├── Include: tool call journal, key data discovered, next steps
│   └── Note any MCP servers that need attention (auth refresh, errors)
│
└── NEXT SESSION START
    ├── resume: Load state, verify, continue
    ├── Re-establish MCP connections (automatic in VS Code)
    ├── Optionally re-run key queries to verify data freshness
    └── Continue from where we left off
```

### Distributed Systems Analogy

This multi-session approach maps to real distributed systems concepts:

| Chat Concept | Distributed Systems Analogy | Why It Works |
|---|---|---|
| Session state file | Write-ahead log / checkpoint | Durable state survives restarts |
| Handoff summary | Leader election + state transfer | New leader inherits prior state |
| Context compaction | Log compaction (Kafka) | Remove obsolete entries, keep latest |
| Partitioned sessions | Sharding by task | Each shard handles focused context |
| Key decisions log | Event sourcing | Replay decisions to rebuild state |
| Resume verification | Consistency check / quorum read | Ensure state is still valid |
| Tool call journal | Event log (Kafka/EventStore) | Immutable record of all actions |
| MCP circuit breaker | Hystrix / Resilience4j | Fail fast, recover gracefully |
| MCP state snapshot | Database checkpoint | Restore stateful server context |
| Saga progress tracking | Saga pattern (microservices) | Resume multi-step workflows |
| Resource cache in state | CDN / read-through cache | Avoid re-fetching unchanged data |
| MCP server health tracking | Health checks / heartbeats | Know what's alive before calling |

> 💡 This is also a great way to **learn** distributed systems concepts — you're living them!

### Composition with Other Commands

- After resuming, use `/composite` to combine multiple analysis modes
- Use `/context continue` within a session for lighter context management
- Use `/hub` to navigate to the right command for your next step
- Use `/mcp` to learn about building MCP servers or configuring them
- Use `/mcp` with topic `troubleshoot` if MCP servers aren't responding after resume

### Rules
- Always save state in a structured, paste-friendly format
- When resuming, verify before assuming — files may have changed
- Keep handoff summaries under 50 lines — concise is reliable
- Include "why" for decisions, not just "what"
- Tag each step with a status: ✅ done, 🔄 in-progress, ⬜ not-started
- For MCP state: log tool calls with inputs AND outputs (summarized)
- For MCP handoffs: include server health and planned next tool calls
- For sagas: always note compensating actions in case rollback is needed
- For circuit breakers: track failure count per server across sessions
```
