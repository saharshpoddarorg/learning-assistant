# modules/ — Gradle Multi-Module Project

All Java source code lives in this directory, organized as independent Gradle modules
with explicit dependency declarations.

## Module Map

```text
modules/
├── search-engine/             Zero-dependency generic search library
├── mcp-common/                Shared MCP infrastructure (config, util, base server)
├── mcp-learning-resources/    Learning Resources MCP server (vault, search, tools)
├── mcp-atlassian/             Atlassian MCP server (Jira, Confluence, Bitbucket)
├── app/                       Application entry point + operational scripts
├── brain-models/              Note metadata models for digital notetaking
└── mac-os/                    macOS development environment sandbox
```

## Dependency Graph

```text
search-engine              (0 deps — standalone library)
     ↑
mcp-common                 (depends on: search-engine)
     ↑
  ┌──┴──────────┐
  │             │
mcp-atlassian  mcp-learning-resources
  │             │
  └──┬──────────┘
     ↓
    app                    (depends on: mcp-common, both servers)

brain-models               (0 deps — standalone)
mac-os                     (0 deps — standalone)
```

## Build Commands

```bash
# Build all modules
./gradlew build

# Build a specific module
./gradlew :modules:search-engine:build
./gradlew :modules:mcp-atlassian:build

# Clean build
./gradlew clean build

# Run the application
./gradlew :modules:app:run
```

## IntelliJ IDEA

Open the root `settings.gradle.kts` in IntelliJ — it will auto-detect all modules
and create the project structure. No manual `.iml` files needed.

## VS Code

The root `.vscode/tasks.json` provides build tasks. Use `Ctrl+Shift+B` to access them.
