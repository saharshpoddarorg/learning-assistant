---
id: BLI-009
title: Third-party libs, JDK 26, SDK tools & annotations
status: todo
priority: high
type: feature
created: 2026-03-28
updated: 2026-03-28
epic: EPIC-003
parent: null
sub-items: []
origin: null
tags: [jdk-26, junit, mockito, annotations, libraries, gradle, maven, sdk]
---

# BLI-009: Third-party libs, JDK 26, SDK tools & annotations

## Description

Upgrade the project infrastructure to use the latest JDK (OpenJDK 26), adopt a
proper build tool (Gradle or Maven), integrate essential third-party libraries
(JUnit 5, Mockito, logging frameworks), and build a custom annotations framework
for declarative configuration and metadata. This is foundational infrastructure
that enables all other projects.

## Acceptance Criteria

- [ ] Upgrade to OpenJDK 26 (latest) — update build scripts, verify compatibility
- [ ] Adopt Gradle or Maven — replace manual `javac` compilation with proper build tool
- [ ] JUnit 5 integration — test framework with parameterized tests, nested tests
- [ ] Mockito integration — mocking framework for unit testing
- [ ] SLF4J + Logback — replace `java.util.logging` with industry-standard logging
- [ ] Custom annotations framework — define project-specific annotations for:
  - [ ] `@McpTool` — declarative MCP tool registration
  - [ ] `@ResourceProvider` — auto-discovery of vault providers
  - [ ] `@ConfigProperty` — typed configuration injection
  - [ ] Annotation processing at compile time (APT)
- [ ] Dependency management — centralized version catalog (Gradle TOML or Maven BOM)
- [ ] Code quality tools — Checkstyle, SpotBugs, or Error Prone integration
- [ ] CI-ready build — ensure build works in GitHub Actions
- [ ] SDKMAN! `.sdkmanrc` — pin JDK version for team consistency

## Notes

- Currently using JDK 21.0.7 with manual `javac` compilation (198 source files)
- Build script: `mcp-servers/build.ps1` — would be replaced by Gradle/Maven
- JDK 26 expected features: Valhalla value types, Panama FFM finalization, etc.
- Consider Gradle Kotlin DSL (`build.gradle.kts`) as the modern default
- AssertJ as a complement to JUnit assertions
- Testcontainers for integration tests with Docker (databases, message brokers)
