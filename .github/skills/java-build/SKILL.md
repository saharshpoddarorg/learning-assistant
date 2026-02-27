---
name: java-build
description: >
  Compile and run Java source files from the command line.
  Use when asked to build, compile, run, execute, or troubleshoot Java compilation errors.
---

# Java Build Skill

## Project Info
- **Language:** Java 21+
- **Source directory:** `src/`
- **Entry point:** `Main.java`
- **Build tool:** None (manual compilation)

## Compile a Single File

```sh
javac src/Main.java
```

## Compile All Java Files

```sh
javac src/*.java
```

## Run

```sh
java -cp src Main
```

## Compile and Run in One Step

```sh
javac src/Main.java && java -cp src Main
```

## Windows (PowerShell)

```powershell
javac src\Main.java; java -cp src Main
```

## Common Errors

| Error | Cause | Fix |
|---|---|---|
| `'javac' is not recognized` | Java not on PATH | Set `JAVA_HOME` and add to PATH |
| `cannot find symbol` | Typo in class/method name, or missing import | Check spelling, add `import` statement |
| `class not found` | Wrong classpath or class name | Use `-cp src` and match the class name exactly |
| `unreported exception` | Checked exception not handled | Add `try/catch` or `throws` declaration |
| `incompatible types` | Type mismatch in assignment/return | Check that variable types match |
| `reached end of file while parsing` | Missing closing brace `}` | Count opening and closing braces |
| `illegal start of expression` | Syntax error (missing semicolon, wrong keyword) | Check the line above the error |

## Check Java Version

```sh
java --version
javac --version
```

Both should show 21 or higher.

---

## Maven (pom.xml projects)

### Common Maven Commands
```sh
mvn compile                      # compile source code
mvn test                         # compile + run tests
mvn package                      # compile + test + build JAR/WAR
mvn install                      # package + install to local ~/.m2 repo
mvn clean                        # delete target/ directory
mvn clean install                # clean then full build (most common)
mvn clean install -DskipTests    # skip tests for faster build
mvn dependency:tree              # show full dependency tree
mvn dependency:resolve           # download all dependencies
```

### Maven Lifecycle Order
```
validate -> compile -> test -> package -> verify -> install -> deploy
```

### Run a Main Class via Maven
```sh
mvn exec:java -Dexec.mainClass="com.example.Main"
```

---

## Gradle (build.gradle / build.gradle.kts projects)

### Common Gradle Commands
```sh
./gradlew build                  # compile + test + assemble
./gradlew clean build            # clean then full build
./gradlew test                   # run tests only
./gradlew run                    # run the application
./gradlew dependencies           # show dependency tree
./gradlew tasks                  # list all available tasks
./gradlew build -x test          # skip tests
```

### Kotlin DSL skeleton
```kotlin
plugins { application }
group = "com.example"
version = "1.0.0"
repositories { mavenCentral() }
dependencies {
    testImplementation(kotlin("test"))
}
application { mainClass.set("com.example.Main") }
```

---

## 3-Tier Build Learning Path

### Newbie — Manual javac
1. `javac src/Main.java` and `java -cp src Main`
2. Understand classpath: where Java looks for `.class` files
3. Fix the 7 common compilation errors above

### Amateur — Maven or Gradle for real projects
1. Maven: `mvn clean install`, `mvn test`, `mvn dependency:tree`
2. Or Gradle: `./gradlew build`, `./gradlew dependencies`
3. Understand lifecycle phases and plugin binding
4. Resources: [Maven Getting Started](https://maven.apache.org/guides/getting-started/), [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)

### Pro — Multi-module, CI/CD, advanced tooling
1. Multi-module builds: parent POM / settings.gradle.kts
2. Kotlin DSL for type-safe Gradle builds
3. Version catalogs, BOM imports, dependency locking
4. Resources: [Maven POM Reference](https://maven.apache.org/pom.html), [Gradle Multi-Project Builds](https://docs.gradle.org/current/userguide/multi_project_builds.html)
