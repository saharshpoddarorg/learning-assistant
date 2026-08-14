---
name: psi-tools
description: "Java code intelligence via the PSI Tools IntelliJ plugin. Use whenever the user asks about Java code structure, usages, call graphs, type hierarchies, class dependencies, inspections, or symbol search. Provides 21+ actions covering semantic code analysis backed by IntelliJ PSI indexes."
metadata:
  allowed-tools:
    - run_in_terminal
  output-format: markdown
---

# PSI Tools — Agent Skill

> **PSI** = Program Structure Interface — IntelliJ's semantic code model that powers
> code navigation, refactoring, and analysis. This skill exposes PSI capabilities
> to the Copilot agent via a lightweight CLI.

## What This Skill Provides

Semantic Java code intelligence powered by IntelliJ's PSI indexes:

| Capability | Actions |
|------------|---------|
| **Symbol discovery** | `symbol_search`, `file_search`, `text_search` |
| **Code structure** | `get_class_structure`, `get_method_body`, `get_imports`, `get_annotations` |
| **Usage analysis** | `find_usages`, `get_call_graph`, `explore_class_dependencies` |
| **Type hierarchies** | `get_type_hierarchy`, `show_subclasses`, `show_superclasses`, `find_implementations` |
| **Method hierarchies** | `get_method_hierarchy` (overrides, base declarations) |
| **Git integration** | `get_changed_line_ranges` |
| **Code inspections** | `get_file_inspections`, `get_changeset_inspections` |
| **Diagnostics** | `health`, `ping`, `echo`, `server_info`, `list_tools`, `help` |

## Prerequisites

1. **IntelliJ IDEA** — must be running with the project open
2. **PSI Tools plugin** — installed and active in IntelliJ (hosts the server on `localhost:3000`)
3. **PowerShell 5.1+** — built-in on Windows (uses `Invoke-RestMethod`)

> The PSI Tools plugin automatically starts a local server when IntelliJ opens a project.
> No manual server setup is required. No Node.js or npm dependencies needed.

## Reference Files

Load these only when you need parameter details or concrete examples:

- `references/action-catalog.md` — exact parameters, types, defaults, and response shapes for every action
- `references/usage-recipes.md` — copy-paste-ready examples and multi-step workflows

## Architecture

```text
Copilot Agent
  ↓ run_in_terminal (sets CLI_JSON_ARGS, invokes PowerShell CLI)
psi_tools_cli.ps1  (PowerShell 5.1+, zero dependencies)
  ↓ Invoke-RestMethod → JSON-RPC over HTTP
PSI Tools IntelliJ Plugin (localhost:3000 / :3001)
  ↓ IntelliJ PSI / VFS / Indexes
Java source code (full semantic model)
```

The CLI sends a single JSON-RPC `tools/call` request per action. No handshake, no session management, no projectPath resolution needed — the server handles all of that internally. The agent only needs to set arguments and call actions.

## How to Call

Set JSON arguments in `CLI_JSON_ARGS`, then invoke the CLI:

```powershell
$env:CLI_JSON_ARGS = '{"className":"com.example.MyService"}'; & "<skill>/scripts/psi_tools_cli.ps1" get_class_structure
```

> Replace `<skill>` with the actual path to this skill folder (e.g., `.github/skills/_modular/psi-tools`).

**Rules:**

1. Always use **single quotes** around the JSON value in PowerShell.
2. Put `CLI_JSON_ARGS` assignment and `& "..."` on the **same line** separated by `;`.
3. **Always** set `CLI_JSON_ARGS`, even for no-argument actions: use `'{}'`.
4. Always check the `success` field in the response before presenting results.
5. On error, read the `error` field for a diagnostic message.

## Pre-flight Check

Before the first tool call in a conversation, run `health` to verify the servers are reachable:

```powershell
$env:CLI_JSON_ARGS = '{}'; & "<skill>/scripts/psi_tools_cli.ps1" health
```

Expected response when everything is working:

```json
{
  "success": true,
  "data": {
    "psiServer": { "status": "up", "port": 3000 },
    "inspectionServer": { "status": "up", "port": 3001 },
    "summary": "All servers operational"
  }
}
```

If the server is down, tell the user: *"The PSI Tools server is not reachable. Please ensure IntelliJ IDEA is running with the PSI Tools plugin active, then try again."*

## Configuration

The CLI reads settings from environment variables. Set them before invoking if non-default:

| Variable | Default | Description |
|----------|---------|-------------|
| `PSI_TOOLS_HOST` | `localhost` | PSI Tools server host |
| `PSI_TOOLS_PORT` | `3000` | PSI Tools server port |
| `PSI_TOOLS_INSPECTION_PORT` | `3001` | Inspection server port |
| `PSI_TOOLS_TIMEOUT` | `120000` | Request timeout in milliseconds |

Defaults match the PSI Tools plugin defaults. No `.env` file needed.

## Defaults and Guardrails

### Symbol Resolution

- Always use **fully qualified class names** (e.g., `com.example.MyClass`, not `MyClass`).
- If the user gives a simple name, use `symbol_search` first to resolve the FQN.
- For overloaded methods, append parameter types: `com.example.MyClass.process(String, int)`.
- Use **simple type names** inside parentheses (not fully qualified).

### Action Selection

| User Intent | Action | Key Parameter |
|-------------|--------|---------------|
| "What calls this method?" | `get_call_graph` | `direction: "callers"` |
| "What does this method call?" | `get_call_graph` | `direction: "callees"` |
| "Who overrides this method?" | `get_method_hierarchy` | `direction: "down"` |
| "What does this method override?" | `get_method_hierarchy` | `direction: "up"` |
| "Show class structure / fields / methods" | `get_class_structure` | — |
| "Find subclasses / subtypes" | `show_subclasses` | — |
| "Find superclasses / parents" | `show_superclasses` | — |
| "Find implementations of interface" | `find_implementations` | — |
| "Show full inheritance tree" | `get_type_hierarchy` | — |
| "Get annotations on a class" | `get_annotations` | — |
| "Get imports in a file" | `get_imports` | — |
| "Show dependencies / coupling" | `explore_class_dependencies` | — |
| "Find usages / references" | `find_usages` | — |
| "Read method source code" | `get_method_body` | — |
| "Show git changes" | `get_changed_line_ranges` | — |
| "Code inspections / warnings" | `get_file_inspections` or `get_changeset_inspections` | — |

### Limits and Defaults

- `find_usages` caps at **500 results**. If `truncated: true`, inform the user and suggest narrowing the scope.
- `get_call_graph` depth is exponential — **start at 2**, only go higher if explicitly asked.
- `explore_class_dependencies` max depth is **3**. Start at **1** for immediate dependencies.
- Default `scope` for `find_usages` is `project`. Only narrow to `module` or `file` if asked.
- Default `direction` for call graphs, type hierarchies, and method hierarchies is `both`.
- Inspection tools use the **inspection server** (port 3001) — the CLI routes automatically.
- **Only run inspections when the user explicitly asks.** Never run them as automatic post-generation checks.

## Multi-Step Workflows

### Understand a class before modifying it

1. `get_class_structure` — fields, methods, modifiers
2. `find_usages` — who references it
3. `explore_class_dependencies` — what it depends on and what depends on it

### Navigate a method's ecosystem

1. `get_method_body` — read the source
2. `get_call_graph` — callers and callees
3. `get_method_hierarchy` — overrides and base declarations
4. `find_usages` — all call sites

### Find usages when only a simple name is known

1. `symbol_search` with the simple name — resolve the FQN
2. `find_usages` with the resolved FQN — get all call sites

### Find all implementations of a pattern

1. `symbol_search` with `kind: "interface"` — find the interface
2. `find_implementations` — find all concrete classes
3. `get_class_structure` on each — inspect their structure

### Inspect only changed code

1. `get_changed_line_ranges` — discover changed files and line ranges
2. `get_file_inspections` with `changedLineRanges` — get warnings scoped to changes only
