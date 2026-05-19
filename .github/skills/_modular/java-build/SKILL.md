---
name: java-build
description: >
  Java build tooling — Gradle and Maven commands.
  Activates on: gradle, gradlew, mvn, maven, build, compile, test, dependency tree.
  Delegates to: package-manager (for installing JDK, Gradle, Maven).
---

# Java Build Skill

## Gradle

```sh
./gradlew build                  # compile + test + assemble
./gradlew clean build            # clean full rebuild
./gradlew test                   # tests only
./gradlew build -x test          # skip tests
./gradlew dependencies           # dependency tree
./gradlew :modules:MODULE:build  # single module
```

## Maven

```sh
mvn clean install                # clean + full build
mvn clean install -DskipTests    # skip tests
mvn test                         # tests only
mvn dependency:tree              # dependency tree
```

Lifecycle: `validate → compile → test → package → verify → install → deploy`
