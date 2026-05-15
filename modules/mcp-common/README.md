# mcp-common

Shared infrastructure for all MCP server implementations.

## Purpose

Provides the config system, utility classes, and base server interfaces that every
MCP server module depends on. Extracted from the monolithic `mcp-servers/` module to
enable independent compilation and testing of individual servers.

## Package Structure

```text
config/                     Layered configuration system
├── ConfigManager.java      Entry point — loads, validates, resolves config
├── exception/              ConfigLoadException, ConfigValidationException
├── loader/                 ConfigParser, ConfigSource, EnvironmentConfigSource, PropertiesConfigSource
├── model/                  ApiKeyStore, McpConfiguration, ServerDefinition, TransportType, etc.
└── validation/             ConfigValidator, ValidationResult

server/                     Base server contracts
├── McpServer.java          Interface — name, version, tools, lifecycle
└── McpServerRegistry.java  Registry — manages active server instances

util/                       Shared utilities
├── HtmlUtils.java          HTML parsing helpers
└── StringUtils.java        String manipulation helpers
```

## Dependencies

- `:modules:search-engine` — uses search infrastructure for keyword matching

## Build

```bash
./gradlew :modules:mcp-common:build
```
