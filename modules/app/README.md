# app

Application entry point and operational infrastructure for MCP servers.

## Purpose

Contains the `Main.java` entry point that bootstraps and runs the MCP servers,
plus all operational files (scripts, configuration, setup guides) needed to
deploy and manage the servers.

## Structure

```text
app/
├── src/Main.java           Entry point — server bootstrap
├── scripts/                Lifecycle scripts (start, stop, restart, reset)
│   ├── server.ps1 / .sh   Server management CLI
│   ├── setup.ps1 / .sh    One-time onboarding wizard
│   └── common/             Shared script utilities
├── config/                 Configuration files
│   ├── mcp-config.properties           Base config (committed)
│   ├── mcp-config.local.properties     Secrets (gitignored)
│   └── mcp-config.local.example.properties  Template
├── .vscode/                VS Code integration (launch, settings)
├── SETUP.md                Step-by-step setup guide
├── MCP-README.md           MCP architecture documentation
└── build.env.example       Build environment template
```

## Dependencies

- `:modules:mcp-common` — config system, base server
- `:modules:mcp-learning-resources` — Learning Resources server
- `:modules:mcp-atlassian` — Atlassian server

## Build & Run

```bash
./gradlew :modules:app:build
./gradlew :modules:app:run
```
