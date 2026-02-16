---
applyTo: "**/*.gradle"
---

# Build Instructions

> **How this file is organized:**
>
> - **Part 1 — Common Gradle Guidelines:** Universal best practices applicable to any Gradle project. Uses generic `:module-name` placeholders.
> - **Part 2 — Company Project Example (Capital Project):** A real-world 200+ module enterprise project that demonstrates how to customize these guidelines. Read it as a template for organizing your own project's build instructions.
>
> If you're setting up build instructions for your own project, copy/adapt the structure in Part 2 using the principles from Part 1.

---

# Part 1 — Common Gradle Guidelines

Everything in this section is **universal Gradle knowledge** — applicable to any Java/Kotlin/Groovy project regardless of company or domain.

---

## 1.1 Gradle Wrapper (gradlew)

The **Gradle Wrapper** is the recommended way to run Gradle — it ensures everyone uses the same Gradle version, requires no pre-installed Gradle, and is the industry standard for CI/CD.

### Always Use the Wrapper

```bash
# Instead of:    gradle build
# Use:           ./gradlew build    (Linux/Mac)
#                gradlew.bat build  (Windows)

# Module-specific builds also use the wrapper:
./gradlew :module-name:build
./gradlew :module-name:compileJava
./gradlew :module-name:test --info
```

### Wrapper Files

```
project-root/
├── gradlew              ← Shell script (Linux/Mac) — commit to VCS
├── gradlew.bat          ← Batch script (Windows) — commit to VCS
└── gradle/
    └── wrapper/
        ├── gradle-wrapper.jar         ← Wrapper bootstrap JAR — commit to VCS
        └── gradle-wrapper.properties  ← Gradle version config — commit to VCS
```

### Managing the Wrapper

```bash
# Check wrapper version
./gradlew --version

# Update wrapper to specific version
./gradlew wrapper --gradle-version=8.10

# Update wrapper to latest
./gradlew wrapper --gradle-version=latest

# Generate wrapper files (if missing — requires system Gradle)
gradle wrapper --gradle-version=8.10
```

### gradle-wrapper.properties

```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.10-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

> **Tip:** Use `-all` distribution (`gradle-8.10-all.zip`) for IDE support — includes sources for better code completion.

> **Security:** Always verify wrapper JAR integrity. Use `gradle wrapper --gradle-version=X.Y --verify` or the [Gradle Wrapper Validation GitHub Action](https://github.com/gradle/wrapper-validation-action).

---

## 1.2 Gradle Build Lifecycle

Gradle executes builds in **three distinct phases**. Understanding these prevents common misconfigurations.

### The Three Phases

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Initialization  │────→│  Configuration  │────→│    Execution    │
│                 │     │                 │     │                 │
│  Which projects │     │  Configure ALL  │     │  Run SELECTED   │
│  are in build?  │     │  project tasks  │     │  task graph     │
│                 │     │  (even unused)  │     │                 │
│  settings.gradle│     │  build.gradle   │     │  Task actions   │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

| Phase | What Happens | Key File |
|---|---|---|
| **Initialization** | Determines which projects participate (single vs multi-project) | `settings.gradle` |
| **Configuration** | Evaluates ALL `build.gradle` files, creates task dependency graph | `build.gradle` |
| **Execution** | Runs only the tasks selected by command + their dependencies | Task actions |

### Why This Matters

```groovy
// ❌ BAD — runs during CONFIGURATION (every build, even unrelated tasks)
task myTask {
    def files = fileTree('src').files  // Expensive operation runs ALWAYS
    doLast {
        files.each { println it }
    }
}

// ✅ GOOD — runs only during EXECUTION (only when this task is requested)
task myTask {
    doLast {
        def files = fileTree('src').files  // Runs only when task executes
        files.each { println it }
    }
}
```

### Task Configuration Avoidance

Modern Gradle uses `register` (lazy) instead of `create` (eager) to avoid configuring tasks that won't run:

```groovy
// ❌ Eager — always configured even if never executed
tasks.create('myTask') { ... }

// ✅ Lazy — configured only when this task is actually needed
tasks.register('myTask') { ... }
```

---

## 1.3 Common Build Commands

### Full Project Build

```bash
# Build everything (WARNING: slow in large projects — 30-60+ min for 200+ modules)
./gradlew build

# Clean and build everything
./gradlew clean build

# Build without tests
./gradlew build -x test

# Parallel build (faster for multi-module)
./gradlew build --parallel --max-workers=8
```

### Module-Specific Builds

**Key Principle:** In multi-module projects, always target specific modules instead of building everything.

```bash
# Build single module
./gradlew :module-name:build

# Build multiple specific modules
./gradlew :module-a:build :module-b:build :module-c:build

# Clean and build specific module
./gradlew :module-name:clean :module-name:build
```

### Compile Only (No JAR Creation)

```bash
# Compile source (fast syntax/dependency check)
./gradlew :module-name:compileJava

# Compile test sources
./gradlew :module-name:compileTestJava
```

### Testing

```bash
# Run tests for specific module
./gradlew :module-name:test

# Run with detailed output
./gradlew :module-name:test --info

# Run single test class
./gradlew :module-name:test --tests com.example.MyTestClass

# Run tests matching pattern
./gradlew :module-name:test --tests "*Integration*"

# Run specific test method
./gradlew :module-name:test --tests "com.example.MyTest.shouldCalculateTotal"

# Run tests by tag (JUnit 5 @Tag)
./gradlew :module-name:test -PincludeTags="integration"

# Exclude slow tests
./gradlew :module-name:test -PexcludeTags="slow"

# Re-run failed tests only
./gradlew :module-name:test --rerun

# Force re-run all tests (skip up-to-date check)
./gradlew :module-name:test --rerun-tasks

# Skip tests
./gradlew build -x test
```

### JAR Tasks

```bash
# Create JAR for specific module
./gradlew :module-name:jar

# Create all JARs
./gradlew jar

# List JAR contents
jar -tf build/libs/module-name.jar
```

### Dependency Management

```bash
# Show all dependencies for module
./gradlew :module-name:dependencies

# Show runtime dependencies only
./gradlew :module-name:dependencies --configuration runtimeClasspath

# Show compile dependencies only
./gradlew :module-name:dependencies --configuration compileClasspath

# Dependency insight (why is this dependency included?)
./gradlew :module-name:dependencyInsight --dependency library-name

# Force re-download dependencies
./gradlew build --refresh-dependencies
```

### Clean Tasks

```bash
# Clean specific module
./gradlew :module-name:clean

# Clean everything
./gradlew clean

# Clean multiple modules
./gradlew :module-a:clean :module-b:clean :module-c:clean
```

### IntelliJ IDEA Integration

```bash
# Generate IntelliJ IDEA project files
./gradlew ideaProject

# Generate module files
./gradlew ideaModule

# Clean IDEA files
./gradlew cleanIdea
```

### Information & Diagnostics

```bash
# List all available tasks
./gradlew tasks

# List all tasks including internal
./gradlew tasks --all

# Show project/module structure
./gradlew projects

# Show Gradle version
./gradlew --version

# Show all project properties
./gradlew properties

# Dry run (show what would execute, without executing)
./gradlew build --dry-run

# Build with info-level logging
./gradlew build --info

# Build with debug-level logging
./gradlew build --debug

# Build scan (interactive performance report at scans.gradle.com)
./gradlew build --scan
```

---

## 1.4 Build Performance Optimization

### Parallel Builds

```bash
./gradlew build --parallel --max-workers=8
```

### Build Cache

```bash
./gradlew build --build-cache
```

### Configuration Cache

Caches the result of the configuration phase — subsequent builds skip re-evaluating `build.gradle` files entirely:

```bash
# Enable for a single build
./gradlew build --configuration-cache

# Enable permanently in gradle.properties
org.gradle.configuration-cache=true

# Check plugin compatibility without failing
./gradlew build --configuration-cache --configuration-cache-problems=warn
```

### Continuous Build (Watch Mode)

```bash
# Auto-rebuild when source files change
./gradlew :module-name:build --continuous
```

### Gradle Daemon

The daemon keeps Gradle's JVM warm between builds (enabled by default):

```bash
# Check daemon status
./gradlew --status

# Stop daemon (if needed)
./gradlew --stop
```

### Skip Unnecessary Tasks

```bash
# Skip tests
./gradlew build -x test

# Skip any specific task
./gradlew build -x :module-name:someTask
```

### Recommended gradle.properties for Performance

```properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.daemon=true
org.gradle.configuration-cache=true
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m -XX:+UseG1GC
org.gradle.configureondemand=true
```

---

## 1.5 Gradle Configuration Files

### gradle.properties

Location: project root. Controls Gradle behavior globally:

```properties
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.daemon=true
```

### build.gradle

Location: project root + each submodule. Contains:
- Plugin applications
- Dependency declarations
- Custom tasks
- Repository definitions

### settings.gradle

Location: project root. Defines:
- Root project name
- All included subprojects/modules
- Build inclusions/exclusions
- Multi-project or composite build layout

### Init Scripts

Optional scripts (e.g., `init.gradle` or custom `*init.gradle`) that run before any other build script. Used for:
- Company-wide repository declarations
- Enterprise proxy/authentication setup
- Shared properties across all builds
- Environment-specific initialization

---

## 1.6 Gradle Properties Precedence

Gradle resolves properties from multiple sources in this priority order:

| Priority | Source | Example |
|---|---|---|
| **1 (highest)** | Command-line flags | `./gradlew build -Penv=prod` |
| 2 | System properties | `-Dorg.gradle.parallel=true` |
| 3 | `gradle.properties` (project root) | `org.gradle.jvmargs=-Xmx4g` |
| 4 | `gradle.properties` (GRADLE_USER_HOME) | `~/.gradle/gradle.properties` |
| 5 (lowest) | Environment variables | `GRADLE_OPTS="-Xmx4g"` |

---

## 1.7 Version Catalogs (Modern Dependency Management)

Version catalogs centralize dependency versions in a single TOML file — the recommended approach since Gradle 7.0+.

### File: `gradle/libs.versions.toml`

```toml
[versions]
spring-boot = "3.3.0"
junit = "5.11.0"
jackson = "2.17.0"
slf4j = "2.0.12"
guava = "33.1.0-jre"

[libraries]
spring-boot-starter = { module = "org.springframework.boot:spring-boot-starter", version.ref = "spring-boot" }
spring-boot-test = { module = "org.springframework.boot:spring-boot-starter-test", version.ref = "spring-boot" }
junit-jupiter = { module = "org.junit.jupiter:junit-jupiter", version.ref = "junit" }
jackson-core = { module = "com.fasterxml.jackson.core:jackson-core", version.ref = "jackson" }
guava = { module = "com.google.guava:guava", version.ref = "guava" }
slf4j-api = { module = "org.slf4j:slf4j-api", version.ref = "slf4j" }

[bundles]
testing = ["junit-jupiter", "spring-boot-test"]
jackson = ["jackson-core"]

[plugins]
spring-boot = { id = "org.springframework.boot", version.ref = "spring-boot" }
```

### Usage in build.gradle

```groovy
dependencies {
    implementation libs.spring.boot.starter
    implementation libs.guava
    implementation libs.slf4j.api

    testImplementation libs.bundles.testing
}
```

### Benefits
- **Single source of truth** for all dependency versions
- **IDE autocomplete** for dependency references
- **Shareable** across modules in multi-project builds
- **Type-safe** accessors generated automatically

---

## 1.8 Code Quality Plugins (Third-Party)

These are widely-used **third-party and built-in plugins** for enforcing code quality in any Gradle project.

### Checkstyle (Code Style Enforcement)

```groovy
plugins {
    id 'checkstyle'  // Built-in Gradle plugin
}

checkstyle {
    toolVersion = '10.17.0'
    configFile = file("${rootDir}/config/checkstyle/checkstyle.xml")
    maxWarnings = 0  // Fail on warnings
}
```

```bash
./gradlew :module-name:checkstyleMain
./gradlew :module-name:checkstyleTest
```

### SpotBugs (Bug Detection)

```groovy
plugins {
    id 'com.github.spotbugs' version '6.0.9'  // Third-party plugin
}

spotbugs {
    toolVersion = '4.8.4'
    effort = 'max'
    reportLevel = 'medium'
}
```

```bash
./gradlew :module-name:spotbugsMain
```

### JaCoCo (Code Coverage)

```groovy
plugins {
    id 'jacoco'  // Built-in Gradle plugin
}

jacocoTestReport {
    reports {
        xml.required = true    // For CI tools (SonarQube, Codecov, etc.)
        html.required = true   // For human review
    }
}

jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = 0.80  // 80% coverage minimum
            }
        }
    }
}
```

```bash
# Generate coverage report
./gradlew :module-name:test :module-name:jacocoTestReport

# Verify coverage thresholds
./gradlew :module-name:jacocoTestCoverageVerification

# Report at: build/reports/jacoco/test/html/index.html
```

### PMD (Static Analysis)

```groovy
plugins {
    id 'pmd'  // Built-in Gradle plugin
}

pmd {
    toolVersion = '7.2.0'
    ruleSets = ['category/java/bestpractices.xml', 'category/java/errorprone.xml']
}
```

```bash
./gradlew :module-name:pmdMain
```

### Running All Quality Checks

```bash
# The 'check' lifecycle task runs test + all configured quality tools
./gradlew check   # Runs: test + checkstyleMain + spotbugsMain + pmdMain
```

---

## 1.9 Dependency Security Scanning (Third-Party)

### OWASP Dependency-Check Plugin

Scans dependencies for known CVEs (Common Vulnerabilities and Exposures):

```groovy
plugins {
    id 'org.owasp.dependencycheck' version '10.0.3'  // Third-party plugin
}

dependencyCheck {
    failBuildOnCVSS = 7.0f   // Fail on High/Critical vulnerabilities
    formats = ['HTML', 'JSON']
}
```

```bash
# Scan for vulnerable dependencies
./gradlew dependencyCheckAnalyze

# Report at: build/reports/dependency-check-report.html
```

### Gradle Dependency Verification

Built-in mechanism to verify dependency checksums (no plugin needed):

```bash
# Generate verification metadata
./gradlew --write-verification-metadata sha256

# Creates gradle/verification-metadata.xml — commit to VCS
# Subsequent builds verify checksum integrity of all dependencies
```

---

## 1.10 Publishing Artifacts (Third-Party Repositories)

### Maven Publish Plugin

```groovy
plugins {
    id 'maven-publish'  // Built-in Gradle plugin
}

publishing {
    publications {
        mavenJava(MavenPublication) {
            from components.java
            groupId = 'com.example'
            artifactId = 'my-library'
            version = '1.0.0'
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/OWNER/REPO")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

```bash
# Publish to configured repository
./gradlew publish

# Publish to local Maven repo (~/.m2/repository)
./gradlew publishToMavenLocal
```

---

## 1.11 Test Configuration Best Practices

### JUnit 5 Platform Configuration

```groovy
tasks.withType(Test).configureEach {
    useJUnitPlatform()  // Required for JUnit 5

    // Parallel test execution
    systemProperty 'junit.jupiter.execution.parallel.enabled', 'true'
    systemProperty 'junit.jupiter.execution.parallel.mode.default', 'concurrent'

    // Better test logging
    testLogging {
        events "passed", "skipped", "failed"
        showExceptions true
        showCauses true
        showStackTraces true
        exceptionFormat "full"
    }

    // Fail fast — stop on first failure
    failFast = false

    // JVM args for tests
    jvmArgs '-Xmx2g'
}
```

### Test Reports

```bash
# HTML report location
# build/reports/tests/test/index.html

# XML report location (for CI tools)
# build/test-results/test/*.xml

# Aggregate test report across all modules
./gradlew testReport
```

---

## 1.12 Groovy DSL vs Kotlin DSL

Gradle supports two build script languages:

| Feature | Groovy DSL (`build.gradle`) | Kotlin DSL (`build.gradle.kts`) |
|---|---|---|
| **Syntax** | Dynamic, flexible | Static, type-safe |
| **IDE Support** | Good | Excellent (autocomplete, refactoring) |
| **Error Detection** | Runtime | Compile-time |
| **Learning Curve** | Lower (more forgiving) | Higher (stricter) |
| **Performance** | Slightly faster script compilation | Slightly slower first build |
| **Industry Trend** | Legacy (still widely used) | Recommended for new projects |

```groovy
// Groovy DSL (build.gradle)
plugins {
    id 'java'
}

dependencies {
    implementation 'com.google.guava:guava:33.1.0-jre'
    testImplementation 'org.junit.jupiter:junit-jupiter:5.11.0'
}
```

```kotlin
// Kotlin DSL (build.gradle.kts)
plugins {
    java
}

dependencies {
    implementation("com.google.guava:guava:33.1.0-jre")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
}
```

---

## 1.13 Advanced Commands

### Custom Task Execution

```bash
# Run named task
./gradlew taskName

# Run task for specific module
./gradlew :module-name:taskName
```

### Build Profiles

```bash
# Build with custom property flags
./gradlew build -Pdebug=true
./gradlew build -Prelease=true
```

### Offline Build (No Network)

```bash
./gradlew build --offline
```

### Profile Build Performance

```bash
# Generate performance report
./gradlew build --profile

# Report at: build/reports/profile/
```

### Composite Build

Include another project's build directly:

```groovy
// settings.gradle
includeBuild('../shared-library')   // Build from another repo
includeBuild('tools/build-plugins') // Build from subdirectory
```

```bash
# Build included builds
./gradlew :included-build-name:taskName
```

### Deprecation Checks

```bash
# Show all deprecation warnings
./gradlew build --warning-mode all
```

---

## 1.14 Troubleshooting Build Issues

### Issue: "Could not find or load main class"

```bash
# Clean and rebuild
./gradlew clean build

# Or specific module
./gradlew :module-name:clean :module-name:build
```

### Issue: "Dependency not found"

```bash
# Force re-download dependencies
./gradlew build --refresh-dependencies

# Nuclear option — clear entire Gradle cache
rm -rf ~/.gradle/caches/
./gradlew build
```

### Issue: "Out of memory"

Edit `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m
```

### Issue: "Task execution failed"

```bash
# Run with stack trace
./gradlew build --stacktrace

# Run with full debug output (save to file — very verbose)
./gradlew build --debug > build-debug.log 2>&1
```

### Issue: "Compilation failed with errors"

```bash
# Check specific module compilation with details
./gradlew :module-name:compileJava --info

# Check for conflicting dependencies
./gradlew :module-name:dependencies
```

### Issue: "Tests failing"

```bash
# Run single failing test in isolation
./gradlew :module-name:test --tests "TestClassName"

# Run with verbose output
./gradlew :module-name:test --info

# Check HTML report for details
# build/reports/tests/test/index.html
```

---

## 1.15 Environment Variables

Common environment variables used by Gradle builds:

```bash
# Required — JDK location
JAVA_HOME=/path/to/jdk-21

# Optional — JVM options for the Gradle daemon
GRADLE_OPTS="-Xmx4g -XX:MaxMetaspaceSize=512m"

# Optional — Gradle user home (default: ~/.gradle)
GRADLE_USER_HOME=/custom/path/.gradle
```

---

## 1.16 Do's and Don'ts (Generic)

### Do:
✅ Use the Gradle Wrapper (`./gradlew`) instead of system `gradle`  
✅ Use module-specific builds when changing individual files  
✅ Use parallel builds for faster compilation (`--parallel`)  
✅ Use `--build-cache` and `--configuration-cache` for performance  
✅ Use `--warning-mode all` periodically to catch deprecations  
✅ Check `settings.gradle` to find correct module name  
✅ Clean before building if you encounter strange errors  
✅ Use `compileJava` for quick syntax checking  
✅ Run tests after making changes to ensure nothing broke  
✅ Use `--info` or `--stacktrace` when debugging build issues  
✅ Build dependent modules when changing shared interfaces  

### Don't:
❌ Don't build the entire project when only one module changed  
❌ Don't modify `settings.gradle` without understanding module dependencies  
❌ Don't skip tests when pushing to shared branches  
❌ Don't commit build artifacts (`build/` directories, `.class` files, `.jar` files)  
❌ Don't run multiple Gradle commands simultaneously in the same workspace  
❌ Don't ignore dependency errors — they cascade into dependent modules  
❌ Don't use outdated cached dependencies — refresh if builds behave unexpectedly  

---

## 1.17 Additional Resources

- **Gradle Documentation:** https://docs.gradle.org/
- **Multi-Project Builds:** https://docs.gradle.org/current/userguide/multi_project_builds.html
- **Dependency Management:** https://docs.gradle.org/current/userguide/dependency_management.html
- **Build Performance:** https://docs.gradle.org/current/userguide/performance.html
- **Version Catalogs:** https://docs.gradle.org/current/userguide/platforms.html
- **Configuration Cache:** https://docs.gradle.org/current/userguide/configuration_cache.html
- **Gradle Wrapper:** https://docs.gradle.org/current/userguide/gradle_wrapper.html
- **Task Configuration Avoidance:** https://docs.gradle.org/current/userguide/task_configuration_avoidance.html

---
---

# Part 2 — Company Project Example: Capital Project

> **What this section is:** A real-world example of how a large enterprise project (200+ Gradle modules) customizes its build workflow. Use it as a **template** for organizing your own project's build documentation.
>
> **Everything below is specific to the Capital Project.** Replace module names, paths, remote names, and environment variables with your own project's equivalents.

---

## 2.1 Project Overview

| Property | Value |
|---|---|
| **Build System** | Gradle (with legacy Ant support) |
| **Root Project** | `iesmodules` |
| **Build Files** | `build.gradle`, `settings.gradle`, `iesinit.gradle` |
| **Git Remote** | `iesd25` (NOT `origin` — see §2.2) |
| **Module Count** | 200+ |
| **Key Principle** | Always use targeted module builds — full project build takes 30–60+ minutes |

---

## 2.2 Git Remote Configuration

> **Example of customization:** This project uses a non-standard Git remote name (`iesd25` instead of `origin`). Your project may use `origin`, `upstream`, or a company-specific name.

```bash
# ⚠️ IMPORTANT: This workspace uses 'iesd25' as the remote, NOT 'origin'

# Fetch updates
git fetch iesd25

# Pull latest
git pull iesd25 main

# Push changes
git push iesd25 your-branch-name

# Push new branch
git push -u iesd25 feature/my-new-feature

# View remote configuration
git remote -v

# Common git operations (standard)
git branch
git checkout branch-name
git checkout -b feature/my-new-feature
git log --oneline -10
git status
git diff
git add .
git commit -m "Your commit message"
```

---

## 2.3 Module Structure

> **Example of customization:** This shows how a large project organizes 200+ modules into logical categories. Map your own project's modules the same way.

### Core Framework Modules
- **`capitalcaf`** — Capital Application Framework (CAF)
- **`capitalcof`** — Capital Object Framework interfaces
- **`capitalcofimpl`** — COF implementations
- **`capitalcofutils`** — COF utilities
- **`capitalcommon`** — Common utilities

### Manager & Persistence
- **`capitalcapmanimpl`** — Capital Manager implementation
- **`capitalpostgres`** — PostgreSQL support
- **`capitalddl`** — Database DDL scripts
- **`capitalpof`** — Persistence Object Framework

### Domain Modules
- **`capitallogic`** — Logical design (electrical schematics)
- **`capitalharness`** — Physical harness design
- **`capitaltopo`** — Topology management
- **`capitalans`** — Analysis services
- **`capitaldrafting`** — Drafting functionality

### Bridge Adapters (CAD/PLM Integration)
- **`capitalbridges`** — Bridge implementation framework
- **`capitalbridgessdk`** — Bridge SDK
- **`capitalkbl`** — KBL adapter
- **`capitalugsnx2`** — NX adapter
- **`capitaltc`** — Teamcenter adapter
- **`capitalenovia`** — Enovia adapter
- **`capitalproe`** — ProE adapter

### Tools & Services
- **`capitalboot`** — Boot/launcher
- **`capitalclaunch`** — Client launcher
- **`capitalrestserver`** — REST server
- **`capitalwebservices`** — Web services
- **`capitalreporterserver`** — Reporting server

### Testing Modules
- **`capitaltestscof`** — COF tests
- **`capitaltestscaf`** — CAF tests
- **`capitaltestlogic`** — Logic tests
- **`capitaltestharness`** — Harness tests

---

## 2.4 Module-Specific Build Examples

> **Example of customization:** These use the Capital Project's actual module names. In your project, replace `:capitalXxx` with your module names.

```bash
# Core framework modules
./gradlew :capitalcof:build
./gradlew :capitalcofimpl:build
./gradlew :capitalcofutils:build
./gradlew :capitalcaf:build

# Domain modules
./gradlew :capitallogic:build
./gradlew :capitalharness:build
./gradlew :capitaltopo:build

# Manager module
./gradlew :capitalcapmanimpl:build

# Bridge modules
./gradlew :capitalbridges:build
./gradlew :capitalkbl:build
./gradlew :capitalugsnx2:build

# Testing modules
./gradlew :capitaltestscof:build
./gradlew :capitaltestlogic:build

# Compile only (fast syntax check)
./gradlew :capitallogic:compileJava
./gradlew :capitalharness:compileJava

# Test specific modules
./gradlew :capitaltestscof:test
./gradlew :capitaltestlogic:test
./gradlew :capitaltestharness:test
```

---

## 2.5 Finding the Right Module for a File

> **Example of customization:** Every large project needs a way to map source files to module names. Here are four methods this project uses.

### Method 1: Check settings.gradle

Search for the directory path in `settings.gradle`:
```groovy
includeBuild("charness_src/src/capitalharness") {name="capitalharness"}
```
This means files in `charness_src/src/capitalharness/` belong to module `:capitalharness`.

### Method 2: Module Name Mapping Table

| Source Path | Module Name | Build Command |
|---|---|---|
| `cframework_src/src/caf/` | `:capitalcaf` | `./gradlew :capitalcaf:build` |
| `clogic_src/src/capitallogic/` | `:capitallogic` | `./gradlew :capitallogic:build` |
| `charness_src/src/capitalharness/` | `:capitalharness` | `./gradlew :capitalharness:build` |
| `ctopology_src/src/capitaltopo/` | `:capitaltopo` | `./gradlew :capitaltopo:build` |
| `datamodel_src/src/impl/cofImpl/` | `:capitalcofimpl` | `./gradlew :capitalcofimpl:build` |
| `datamodel_src/src/utils/` | `:capitalcofutils` | `./gradlew :capitalcofutils:build` |
| `interfaces_src/src/java/` | `:capitalcof` | `./gradlew :capitalcof:build` |
| `interfaces_src/src/utils/` | `:capitalutils` | `./gradlew :capitalutils:build` |
| `interfaces_src/src/grpc/` | `:capitalgrpc` | `./gradlew :capitalgrpc:build` |
| `cmanager_src/src/capitalManager/` | `:capitalcapmanimpl` | `./gradlew :capitalcapmanimpl:build` |
| `cbridges_src/src/impl/` | `:capitalbridges` | `./gradlew :capitalbridges:build` |
| `cbridges_src/src/adpt/kbl/` | `:capitalkbl` | `./gradlew :capitalkbl:build` |
| `cbridges_src/src/adpt/ugsnx2/` | `:capitalugsnx2` | `./gradlew :capitalugsnx2:build` |
| `cbridges_src/src/adpt/tceng/` | `:capitaltc` | `./gradlew :capitaltc:build` |

### Method 3: Search settings.gradle Programmatically

**PowerShell:**
```powershell
$filePath = "charness_src/src/capitalharness/MyFile.java"
$directory = Split-Path $filePath -Parent
Select-String -Path settings.gradle -Pattern $directory
```

**Bash:**
```bash
FILE_PATH="charness_src/src/capitalharness/MyFile.java"
DIR_PATH=$(dirname "$FILE_PATH")
grep "$DIR_PATH" settings.gradle
```

### Method 4: Common Directory → Module Groups

| Directory Prefix | Likely Module(s) |
|---|---|
| `cframework_src/` | `:capitalcaf`, `:capitalcofcafimpl` |
| `clogic_src/` | `:capitallogic` |
| `charness_src/` | `:capitalharness` |
| `ctopology_src/` | `:capitaltopo` |
| `cmanager_src/` | `:capitalcapmanimpl`, `:capitalpostgres`, `:capitalddl` |
| `datamodel_src/src/impl/cofImpl/` | `:capitalcofimpl` |
| `datamodel_src/src/utils/` | `:capitalcofutils` |
| `interfaces_src/src/java/` | `:capitalcof` |
| `interfaces_src/src/grpc/` | `:capitalgrpc` |
| `cbridges_src/src/impl/` | `:capitalbridges` |
| `cbridges_src/src/adpt/*` | Bridge-specific (`:capitalkbl`, `:capitalugsnx2`, etc.) |

---

## 2.6 Build Workflow Example

> **Example of customization:** Step-by-step workflow when modifying a file. Adapt the module names and paths for your project.

**Scenario:** You modified `clogic_src/src/capitallogic/src/chs/caplets/logic/actions/MyAction.java`

### Step 1: Identify the module

```bash
grep "clogic_src/src/capitallogic" settings.gradle
# Output: includeBuild("clogic_src/src/capitallogic") {name="capitallogic"}
# → Module is :capitallogic
```

### Step 2: Compile (fast check)

```bash
./gradlew :capitallogic:compileJava
```

### Step 3: Build (compile + JAR)

```bash
./gradlew :capitallogic:build
```

### Step 4: Verify output

```bash
ls clogic_src/src/capitallogic/build/libs/
ls clogic_src/src/capitallogic/build/classes/java/main/
```

---

## 2.7 Common Build Scenarios

> **Example of customization:** Real multi-module dependency chains from the Capital Project. Document equivalent scenarios for your own project's module relationships.

### Scenario 1: Single Module Change — capitalharness

```bash
./gradlew :capitalharness:compileJava           # Quick syntax check
./gradlew :capitalharness:build                  # Full build with JAR
./gradlew :capitalharness:clean :capitalharness:build  # From scratch
```

### Scenario 2: Related Interface + Implementation Change

Files in `:capitalcof` (interfaces), `:capitalcofimpl` (implementations), `:capitalcofutils` (utilities):

```bash
./gradlew :capitalcof:build :capitalcofimpl:build :capitalcofutils:build
# Or with parallel:
./gradlew :capitalcof:build :capitalcofimpl:build :capitalcofutils:build --parallel
```

### Scenario 3: gRPC Proto Change

Proto file in `interfaces_src/src/grpc/proto/capitalManager.proto`:

```bash
./gradlew :capitalgrpc:build           # 1. Generate Java from proto
./gradlew :capitalcapmanimpl:build     # 2. Build server implementation
./gradlew :capitalcofutils:build       # 3. Build client utilities
```

### Scenario 4: Bridge Adapter Change

File in `cbridges_src/src/adpt/kbl/KBLAdapter.java`:

```bash
./gradlew :capitalkbl:build            # Build KBL adapter
./gradlew :capitalbridgessdk:build     # Also rebuild SDK if interfaces changed
```

### Scenario 5: Running Tests After Changes

Changed `clogic_src/src/capitallogic/src/chs/caplets/logic/LogicCaplet.java`:

```bash
./gradlew :capitallogic:build          # Build the module
./gradlew :capitaltestlogic:test       # Run tests
# Or both:
./gradlew :capitallogic:build :capitaltestlogic:test
```

### Scenario 6: Database Schema Change

Changed `cmanager_src/src/postgres/ddl/create_tables.sql`:

```bash
./gradlew :capitalpostgres:build       # Build postgres module
./gradlew :capitalddl:build            # Build DDL module
./gradlew :capitalcapmanimpl:build     # Build manager implementation
```

---

## 2.8 Build Dependency Patterns

> **Example of customization:** Common dependency chains. Identify equivalent patterns in your project.

### Pattern 1: Interface → Implementation → Consumer

```bash
./gradlew :capitalcof:build         # 1. Interface module
./gradlew :capitalcofimpl:build     # 2. Implementation module
./gradlew :capitalcaf:build :capitallogic:build :capitalharness:build  # 3. Consumers
```

### Pattern 2: Framework → Caplet → Tests

```bash
./gradlew :capitalcaf:build         # 1. Framework
./gradlew :capitallogic:build       # 2. Caplet
./gradlew :capitaltestlogic:test    # 3. Tests
```

### Pattern 3: Proto → Server → Client

```bash
./gradlew :capitalgrpc:build        # 1. Proto → generated classes
./gradlew :capitalcapmanimpl:build  # 2. Server implementation
./gradlew :capitalcofutils:build    # 3. Client utilities
```

---

## 2.9 Project-Specific Configuration Files

> **Example of customization:** This project uses a custom init script (`iesinit.gradle`) for company-wide setup. Your project may have its own init scripts or convention plugins.

### iesinit.gradle

Location: project root. Contains:
- Project-wide initialization
- Common properties shared across all 200+ modules
- Path configurations for the Capital build environment
- Environment-specific setup

### IDEA Integration

```bash
# Generate IntelliJ IDEA project files (project-specific task)
./gradlew runIdeaModules
```

---

## 2.10 Project-Specific Environment Variables

> **Example of customization:** These are Capital Project environment variables. Your project will have its own — document them the same way.

```bash
# Standard (any project)
JAVA_HOME=/path/to/jdk-21
GRADLE_OPTS="-Xmx4g -XX:MaxMetaspaceSize=512m"

# Capital Project-specific
CHS_HOME=/path/to/chs_home
CHS_BUILD_TOOLS_HOME=/path/to/build/tools
```

---

## 2.11 Shell Aliases (Project-Specific)

> **Example of customization:** Create aliases matching your project's remote name and common modules.

### PowerShell (add to `$PROFILE`)

```powershell
# Gradle shortcuts
function gb { ./gradlew build }
function gbc { ./gradlew build --continuous }
function gcb { ./gradlew clean build }
function gt { ./gradlew test }
function gm { param($module) ./gradlew ":${module}:build" }

# Git shortcuts for iesd25 remote (replace 'iesd25' with your remote name)
function gf { git fetch iesd25 }
function gp { git pull iesd25 }
function gpu { param($branch) git push iesd25 $branch }
function gs { git status }
function gd { git diff }
```

### Bash (add to `~/.bashrc` or `~/.bash_profile`)

```bash
# Gradle shortcuts
alias gb='./gradlew build'
alias gbc='./gradlew build --continuous'
alias gcb='./gradlew clean build'
alias gt='./gradlew test'
alias gm='function _gm(){ ./gradlew ":$1:build"; }; _gm'

# Git shortcuts for iesd25 remote (replace 'iesd25' with your remote name)
alias gf='git fetch iesd25'
alias gp='git pull iesd25'
alias gpu='function _gpu(){ git push iesd25 "$1"; }; _gpu'
alias gs='git status'
alias gd='git diff'
```

---

## 2.12 Do's and Don'ts (Project-Specific)

> **Example of customization:** These supplement the generic do's/don'ts in §1.16 with project-specific rules.

### Do:
✅ Use `git push iesd25` (NOT `origin`) for this project  
✅ Build dependent modules when changing interfaces (COF → COFImpl → consumers)  
✅ Build proto modules before building implementation modules  
✅ Check `settings.gradle` to find the correct module name for a file  
✅ Use `compileJava` for quick syntax checking before full builds  

### Don't:
❌ Don't use `git push origin` — the remote is `iesd25`  
❌ Don't run `./gradlew build` for the entire project unless necessary (30–60+ minutes)  
❌ Don't modify `settings.gradle` without understanding module dependencies in this project  
❌ Don't forget to build proto modules (`capitalgrpc`) before building implementation  
❌ Don't mix Ant and Gradle builds — prefer Gradle for everything  

---

## 2.13 Quick Reference Table

| File Path | Module | Build Command |
|---|---|---|
| `cframework_src/src/caf/` | `:capitalcaf` | `./gradlew :capitalcaf:build` |
| `clogic_src/src/capitallogic/` | `:capitallogic` | `./gradlew :capitallogic:build` |
| `charness_src/src/capitalharness/` | `:capitalharness` | `./gradlew :capitalharness:build` |
| `ctopology_src/src/capitaltopo/` | `:capitaltopo` | `./gradlew :capitaltopo:build` |
| `cmanager_src/src/capitalManager/` | `:capitalcapmanimpl` | `./gradlew :capitalcapmanimpl:build` |
| `interfaces_src/src/java/` | `:capitalcof` | `./gradlew :capitalcof:build` |
| `interfaces_src/src/utils/` | `:capitalutils` | `./gradlew :capitalutils:build` |
| `interfaces_src/src/grpc/` | `:capitalgrpc` | `./gradlew :capitalgrpc:build` |
| `datamodel_src/src/impl/cofImpl/` | `:capitalcofimpl` | `./gradlew :capitalcofimpl:build` |
| `datamodel_src/src/utils/` | `:capitalcofutils` | `./gradlew :capitalcofutils:build` |
| `cbridges_src/src/impl/` | `:capitalbridges` | `./gradlew :capitalbridges:build` |
| `cbridges_src/src/adpt/kbl/` | `:capitalkbl` | `./gradlew :capitalkbl:build` |
| `cbridges_src/src/adpt/ugsnx2/` | `:capitalugsnx2` | `./gradlew :capitalugsnx2:build` |
| `cbridges_src/src/adpt/tceng/` | `:capitaltc` | `./gradlew :capitaltc:build` |
