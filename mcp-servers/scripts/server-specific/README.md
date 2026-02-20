# Server-Specific Scripts

This directory contains scripts tailored to individual MCP servers.

## Structure

```
server-specific/
├── github/          ← GitHub MCP server scripts
├── <server-name>/   ← Add your own server-specific scripts here
└── README.md        ← This file
```

## Adding Scripts for a New Server

1. Create a directory named after your server (matching `server.<name>` in config):
   ```
   mkdir -p scripts/server-specific/<your-server>
   ```

2. Add scripts following these conventions:
   - Use the shared config reader: `source "$PROJECT_ROOT/scripts/common/utils/read-config.sh"`
   - Read server-specific config with: `read_config "server.<name>.<key>"`
   - Provide both `.sh` (Linux/macOS) and `.ps1` (Windows) versions when practical
   - Include a header comment block with usage instructions

3. Common script types per server:
   - `setup.sh` — One-time setup (install dependencies, configure auth)
   - `test-connection.sh` — Verify server connectivity
   - `start.sh` / `stop.sh` — Lifecycle management for self-hosted servers

## Naming Conventions

| Script Name          | Purpose                              |
|----------------------|--------------------------------------|
| `setup.sh`           | First-time setup and bootstrapping   |
| `test-connection.sh` | Quick connectivity/auth test         |
| `start.sh`           | Start the MCP server process         |
| `stop.sh`            | Stop the MCP server process          |
| `logs.sh`            | Tail or retrieve server logs         |
