# JDK Version Management — Backlog Content

> **Source:** Removed from `digital-notetaking` skill during modularization migration.
> **Decision needed:** Move to `java-build` skill, or create a dedicated `jdk` skill.

---

## Content to Place

### macOS / Linux / WSL

```bash
# Install SDKMAN!
curl -s "https://get.sdkman.io" | bash

# List available JDK distributions
sdk list java

# Install JDK (Eclipse Temurin)
sdk install java 25-tem

# Pin JDK to a project
sdk env init   # creates .sdkmanrc with current version
sdk env        # apply .sdkmanrc in this shell

# Switch default globally
sdk default java 21.0.7-tem
```

### Windows (without WSL)

Download from [adoptium.net](https://adoptium.net), then:

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-25"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
java --version
```

### Resources

| Resource | URL |
|---|---|
| SDKMAN! | https://sdkman.io/ |
| Eclipse Temurin (Adoptium) | https://adoptium.net/ |
| JDK Release Notes | https://openjdk.org/projects/jdk/ |

---

## Suggested Destination

- **Option A:** Merge into `java-build` skill (already handles Gradle/Maven — JDK install fits)
- **Option B:** New dedicated `jdk` skill (if JDK management grows beyond install commands)
