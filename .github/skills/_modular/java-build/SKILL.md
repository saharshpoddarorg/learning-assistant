---
name: java-build
description: >
  Java build tooling — Gradle (Groovy & Kotlin DSL) and Maven commands, multi-module
  builds, common properties, and build lifecycle.
  Activates on: gradle, gradlew, mvn, maven, build, compile, test, dependency tree,
  build.gradle, settings.gradle, pom.xml, multi-module, subproject.
  Delegates to: package-manager (for installing JDK, Gradle, Maven).
---

# Java Build Skill

> **Scope:** Using Gradle/Maven to build Java projects. For installing these tools,
> see `package-manager`. For macOS dev environment setup, see `mac-dev`.

---

## Gradle — Commands

```sh
./gradlew build                  # compile + test + assemble
./gradlew clean build            # clean full rebuild
./gradlew test                   # tests only
./gradlew build -x test          # skip tests
./gradlew dependencies           # dependency tree (all configs)
./gradlew dependencies --configuration runtimeClasspath  # specific config
./gradlew :modules:MODULE:build  # single module
./gradlew tasks                  # list available tasks
./gradlew tasks --all            # including subproject tasks
```

### Multi-Module Builds

```sh
# Build one module
./gradlew :modules:mcp-common:build

# Build module + its dependencies
./gradlew :modules:mcp-learning-resources:build

# Run tests for one module
./gradlew :modules:search-engine:test

# List all modules
./gradlew projects
```

### Useful Flags

```sh
./gradlew build --info           # verbose output
./gradlew build --debug          # debug output
./gradlew build --scan           # generate build scan (performance)
./gradlew build --parallel       # parallel module builds
./gradlew build --continuous     # re-run on source changes
./gradlew build --no-daemon      # disable daemon (CI)
./gradlew --stop                 # stop all Gradle daemons
```

### Kotlin DSL Quick Reference (build.gradle.kts)

```kotlin
// Dependencies
dependencies {
    implementation("group:artifact:version")
    implementation(project(":modules:mcp-common"))  // module dependency
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

// Java toolchain
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// Custom task
tasks.register("hello") {
    doLast { println("Hello from Gradle") }
}
```

### settings.gradle.kts

```kotlin
rootProject.name = "my-project"
include(":modules:core")
include(":modules:api")
include(":modules:app")
```

### gradle.properties

```properties
# Performance
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.daemon=true
org.gradle.jvmargs=-Xmx2g -XX:+UseG1GC

# Project
group=com.example
version=1.0.0-SNAPSHOT
```

---

## Maven — Commands

```sh
mvn clean install                # clean + full build
mvn clean install -DskipTests    # skip tests
mvn test                         # tests only
mvn dependency:tree              # dependency tree
mvn dependency:tree -Dincludes=groupId:artifactId  # filter tree
mvn versions:display-dependency-updates  # check for updates
mvn help:effective-pom           # resolved POM with inheritance
```

### Lifecycle

```text
validate → compile → test → package → verify → install → deploy
```

### Multi-Module

```sh
mvn -pl module-name clean install          # build one module
mvn -pl module-name -am clean install      # module + its dependencies
mvn -pl !module-to-skip clean install      # exclude a module
```

### Useful Flags

```sh
mvn -T 4 clean install           # parallel (4 threads)
mvn -T 1C clean install          # 1 thread per CPU core
mvn -o clean install             # offline mode (use local cache)
mvn -U clean install             # force snapshot updates
mvn -X clean install             # debug output
```

---

## Skill Routing — Which Skill to Use When

| Situation | Skill |
|---|---|
| Run Gradle or Maven build commands (any OS) | **`java-build`** ← you are here |
| Install JDK, Gradle, Maven **on macOS** | `mac-dev` |
| Install JDK, Gradle, Maven **on Windows or Linux** | `package-manager` |
| Set JAVA_HOME or fix PATH on Windows | `package-manager` |
| Set JAVA_HOME or fix PATH on macOS | `mac-dev` |
| Switch between JDK versions on macOS (jenv) | `mac-dev` |
| Switch between JDK versions on Windows/Linux (SDKMAN) | `package-manager` |

### Boundary Rules

```text
java-build     = ANY OS — purely ./gradlew / mvn commands; never installs
mac-dev        = macOS ONLY — install + env setup via Homebrew
package-manager = Windows + Linux — install + env setup via winget / apt / SDKMAN
```

> **Build failing with "JAVA_HOME not set"?**
> → On macOS: see `mac-dev` (jenv / java_home).
> → On Windows/Linux: see `package-manager` (JAVA_HOME env var).
