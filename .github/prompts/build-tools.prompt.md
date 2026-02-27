````prompt
---
name: build-tools
description: 'Learn build automation tools — Maven, Gradle, Make, Bazel, npm — lifecycle, dependency management, multi-module builds, and CI/CD integration'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Tool
${input:tool:Which build tool? (maven / gradle / make / bazel / npm / ant)}

## Topic
${input:topic:What aspect? (e.g., lifecycle, dependency-management, multi-module, plugins, gradle-kotlin-dsl, bom, version-catalog, publishing, ci-cd-integration)}

## Current Level
${input:level:Your experience level? (newbie / amateur / pro)}

## Instructions

Teach or guide on the selected build tool and topic. Adapt depth and detail to the level.

### Build Tools Domain Map
```
Maven
├── Core: pom.xml, coordinates (groupId:artifactId:version), repositories
├── Lifecycle: validate → compile → test → package → verify → install → deploy
├── Plugins: Surefire (test), Shade (fat JAR), Exec, Versions, Enforcer
├── Dependencies: scopes (compile/test/runtime/provided), exclusions, BOMs
├── Multi-module: parent POM, <modules>, inheritance vs aggregation
└── Advanced: profiles, property interpolation, dependency:tree

Gradle
├── Core: build.gradle.kts (Kotlin DSL), settings.gradle.kts, tasks
├── Lifecycle: assemble → check → build (DAG-based, not sequential like Maven)
├── Plugins: java, application, kotlin, jvm, com.google.protobuf, shadow
├── Dependencies: configurations (implementation/testImplementation/runtimeOnly)
├── Version Catalog: libs.versions.toml, type-safe accessors
├── Multi-project: settings.gradle.kts, project(), convention plugins
└── Advanced: composite builds, build scans, configuration avoidance, caching

GNU Make
├── Core: Makefile, targets, prerequisites, recipes
├── Variables: plain, automatic ($@, $<, $^), recursive vs immediate
├── Patterns: %, static pattern rules
├── Phony targets: .PHONY for clean, test, build, all
└── Use case: C/C++ projects, scripting, cross-language automation

Bazel
├── Core: BUILD files, WORKSPACE, Starlark (.bzl), rules, targets, deps
├── Rules: java_binary, java_library, java_test, cc_binary, py_binary
├── Remote caching and remote execution
├── Incremental builds: only rebuild what changed (content-addressed)
└── Use case: large monorepos, multi-language projects

npm / yarn / pnpm
├── Core: package.json (name, version, scripts, dependencies)
├── Scripts: "build", "test", "start", "lint" in scripts{}
├── Dependency types: dependencies, devDependencies, peerDependencies
├── Workspaces: monorepo setup, hoisting
├── Lockfiles: package-lock.json, yarn.lock, pnpm-lock.yaml (commit these)
└── Publishing: npm access, npm publish, scoped packages (@org/pkg)
```

### Response Structure by Level

#### Newbie — first steps only
1. **What problem it solves** — Before vs after using a build tool
2. **Install it** — Exact install command for their OS
3. **First project** — Scaffold a minimal project with the tool
4. **Three commands** — The three commands they'll use 80% of the time
5. **One mistake to avoid** — The most common newbie error

#### Amateur — practical day-to-day usage
1. **How the build works** — Lifecycle/phases/DAG explained plainly
2. **Dependency management** — How to add, scope, and troubleshoot deps
3. **Common tasks table** — Built vs run vs test vs clean
4. **Configuration example** — Annotated pom.xml or build.gradle.kts
5. **IDE integration tips** — IntelliJ / VS Code setup
6. **CI/CD snippet** — Example GitHub Actions step for this tool

#### Pro — internals, performance, scaling
1. **Plugin/task lifecycle internals** — How the tool's execution model works
2. **Performance** — Parallel builds, caching strategies, incremental compilation
3. **Multi-module patterns** — Shared config, BOM, version catalog
4. **Advanced configuration** — Profiles/buildSrc/convention plugins
5. **Troubleshooting** — Dependency conflicts, build failures, debug flags
6. **Tool comparison** — When to choose this tool vs alternatives

### Quick Command Reference

| Goal              | Maven                          | Gradle                       | npm              |
|-------------------|-------------------------------|------------------------------|------------------|
| Full build        | `mvn clean install`           | `./gradlew build`            | `npm run build`  |
| Run tests         | `mvn test`                    | `./gradlew test`             | `npm test`       |
| Skip tests        | `-DskipTests`                 | `-x test`                    | —                |
| Dependency tree   | `mvn dependency:tree`         | `./gradlew dependencies`     | `npm ls`         |
| List tasks        | `mvn help:describe -Dplugin=` | `./gradlew tasks`            | (see scripts)    |
| Clean             | `mvn clean`                   | `./gradlew clean`            | —                |

### Curated Resources
- **Maven getting started**: [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) → [Maven Getting Started Guide](https://maven.apache.org/guides/getting-started/)
- **Maven reference**: [Maven POM Reference](https://maven.apache.org/pom.html), [Build Lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html), [Dependency Mechanism](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)
- **Gradle**: [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html), [Kotlin DSL Primer](https://docs.gradle.org/current/userguide/kotlin_dsl.html), [Gradle Dependency Management](https://docs.gradle.org/current/userguide/dependency_management.html), [Multi-Project Builds](https://docs.gradle.org/current/userguide/multi_project_builds.html)
- **Make**: [Makefile Tutorial](https://makefiletutorial.com/)
- **Bazel**: [Bazel Docs](https://bazel.build/docs)
- **npm**: [npm Docs](https://docs.npmjs.com/)
- **Versioning**: [SemVer](https://semver.org/)
````
