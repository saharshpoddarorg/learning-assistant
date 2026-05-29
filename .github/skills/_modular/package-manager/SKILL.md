---
name: package-manager
description: >
  Windows, Linux, and WSL package management, PATH/environment variable configuration,
  and cross-platform version managers (SDKMAN, nvm). The skill for installing tools,
  managing environment variables, and configuring PATH on Windows (winget), Linux (apt),
  and WSL. Activates on: winget, install on Windows, install on Linux, JAVA_HOME, PATH,
  environment variable, SDKMAN, sdk install, nvm install (non-Mac context), apt install,
  PowerShell env, System Properties, version manager, pin version, .sdkmanrc, .nvmrc.
  This skill is mutually exclusive with mac-dev (which covers macOS/Homebrew entirely).
---

# Package Manager Skill — Windows, Linux & Cross-Platform

> **When to use this skill:** User is on Windows or Linux, or asking about cross-platform
> version managers (SDKMAN, nvm usage commands).
> **When NOT to use:** User is on macOS → use `mac-dev` (owns all Homebrew/macOS tooling).
> Java build commands (Gradle/Maven) → use `java-build`.

---

## Windows — winget (Primary)

> The default package manager on Windows 11+. Pre-installed. No admin setup needed.

### Core Commands

```powershell
winget search <term>             # find packages
winget install <package-id>      # install
winget upgrade <package-id>      # upgrade specific
winget upgrade --all             # upgrade everything
winget list                      # installed packages
winget uninstall <package-id>    # remove
winget show <package-id>         # package details
```

### Common Dev Tool Installs

```powershell
# JDK
winget install EclipseAdoptium.Temurin.21.JDK    # Temurin 21 (recommended)
winget install EclipseAdoptium.Temurin.17.JDK    # Temurin 17
winget install Amazon.Corretto.21                 # Amazon Corretto 21
winget install Microsoft.OpenJDK.21               # Microsoft Build of OpenJDK

# Build tools
winget install Gradle.Gradle
winget install Apache.Maven

# Dev tools
winget install Git.Git
winget install GitHub.cli
winget install OpenJS.NodeJS.LTS
winget install Python.Python.3.12
winget install Microsoft.VisualStudioCode
winget install JetBrains.IntelliJIDEA.Ultimate
winget install Docker.DockerDesktop
winget install Postman.Postman
```

### Searching for exact package IDs

```powershell
winget search "temurin"          # find all Temurin JDK versions
winget search "openjdk"          # find all OpenJDK builds
winget search --id "Git"         # search by ID prefix
```

---

## Windows — Environment Variables & PATH

### View current values

```powershell
# Current session
$env:JAVA_HOME
$env:PATH -split ';'

# System-wide (persisted)
[System.Environment]::GetEnvironmentVariable("JAVA_HOME", "Machine")
[System.Environment]::GetEnvironmentVariable("PATH", "Machine")
```

### Set for current session (temporary)

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
$env:GRADLE_HOME = "C:\Gradle\gradle-8.8"
$env:PATH = "$env:GRADLE_HOME\bin;" + $env:PATH
```

### Set permanently (survives reboot)

```powershell
# User-level (no admin needed)
[System.Environment]::SetEnvironmentVariable("JAVA_HOME",
    "C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot", "User")

# Add to PATH (user-level)
$currentPath = [System.Environment]::GetEnvironmentVariable("PATH", "User")
[System.Environment]::SetEnvironmentVariable("PATH",
    "$env:JAVA_HOME\bin;$currentPath", "User")

# System-level (requires admin PowerShell)
[System.Environment]::SetEnvironmentVariable("JAVA_HOME",
    "C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot", "Machine")
```

### GUI method

```text
Win + R → sysdm.cpl → Advanced → Environment Variables
  → User variables: JAVA_HOME, PATH additions
  → System variables: for all users (admin)
```

### JDK Version Switching on Windows

```powershell
# List installed JDKs (check common locations)
Get-ChildItem "C:\Program Files\Eclipse Adoptium\"
Get-ChildItem "C:\Program Files\Java\"
Get-ChildItem "C:\Program Files\Microsoft\"

# Switch by changing JAVA_HOME (current session)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot"
java --version

# PowerShell profile aliases (~\Documents\PowerShell\Microsoft.PowerShell_profile.ps1)
function Use-Java21 {
    $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot"
    Write-Host "Switched to Java 21: $(java --version 2>&1 | Select-Object -First 1)"
}
function Use-Java17 {
    $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.11.9-hotspot"
    Write-Host "Switched to Java 17: $(java --version 2>&1 | Select-Object -First 1)"
}
```

### PowerShell Profile Setup

```powershell
# Create/edit profile (runs on every PowerShell session)
if (!(Test-Path -Path $PROFILE)) { New-Item -ItemType File -Path $PROFILE -Force }
code $PROFILE

# Example profile content
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

---

## Windows — nvm-windows (Node.js version manager)

> nvm-windows is a separate tool from Unix nvm (different implementation, same concept).

```powershell
# Install via winget
winget install CoreyButler.NVMforWindows

# Restart terminal after install

# Usage
nvm list available                # show available Node versions
nvm install 20.14.0              # install specific version
nvm install lts                  # install latest LTS
nvm use 20.14.0                  # switch version
nvm list                         # list installed versions
nvm current                      # show active version
nvm alias default 20.14.0       # set default
```

**Pin per project:** Create `.nvmrc` file (same as Unix):

```text
20
```

---

## Linux / WSL — apt + SDKMAN

### Core Commands

```bash
sudo apt update                    # refresh package index
sudo apt install <package>         # install
sudo apt upgrade <package>         # upgrade specific
sudo apt full-upgrade              # upgrade all (handles new deps)
apt list --installed               # installed packages
apt search <term>                  # find packages
apt show <package>                 # package details
sudo apt remove <package>          # remove (keep config)
sudo apt purge <package>           # remove + delete config
sudo apt autoremove                # remove unused deps
```

### Common Dev Tool Installs (apt)

```bash
sudo apt install git curl wget unzip zip
sudo apt install build-essential    # gcc, make, etc.
sudo apt install ca-certificates   # for HTTPS
```

> **For JDKs and JVM tools on Linux/WSL:** Use SDKMAN (see below) — much better than apt
> for managing multiple JDK versions.

### Environment Variables & PATH (Linux)

```bash
# View current
echo $JAVA_HOME
echo $PATH | tr ':' '\n'

# Set for current session
export JAVA_HOME="$HOME/.sdkman/candidates/java/current"
export PATH="$JAVA_HOME/bin:$PATH"

# Set permanently — add to ~/.bashrc (or ~/.zshrc if using zsh)
echo 'export JAVA_HOME="$HOME/.sdkman/candidates/java/current"' >> ~/.bashrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.bashrc
source ~/.bashrc

# System-wide (all users) — add to /etc/environment
sudo nano /etc/environment
# Add: JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"
```

### Shell Profile Files (Linux)

| File | When it runs | Use for |
|---|---|---|
| `~/.profile` | Login shell (once) | Environment exports, PATH |
| `~/.bashrc` | Every interactive bash session | Aliases, functions, prompt |
| `~/.bash_aliases` | Sourced by .bashrc (if exists) | Keep aliases separate |
| `/etc/environment` | System-wide (all users) | JAVA_HOME, global PATH |

---

## Cross-Platform Version Managers

### SDKMAN! (JVM ecosystem — works on macOS, Linux, WSL)

> Best way to manage multiple JDK, Gradle, Maven, Kotlin versions.
> Works on Linux, WSL, and macOS (alternative to jenv on Mac).

```bash
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# List available JDK distributions
sdk list java                        # shows all vendors + versions

# Install and manage
sdk install java 21.0.3-tem          # Temurin 21 LTS
sdk install java 17.0.11-tem         # Temurin 17 LTS
sdk default java 21.0.3-tem          # set global default
sdk use java 17.0.11-tem             # switch for current shell only
sdk current java                     # show active version

# Other JVM tools
sdk install gradle 8.8               # specific Gradle version
sdk install maven 3.9.7              # specific Maven version
sdk install kotlin                   # Kotlin compiler

# Version per project
sdk env init                         # creates .sdkmanrc in project root
sdk env                              # apply versions from .sdkmanrc
```

**`.sdkmanrc` format:**

```properties
java=21.0.3-tem
gradle=8.8
maven=3.9.7
```

**Auto-switch on `cd`:** Add to `~/.sdkman/etc/config`:

```properties
sdkman_auto_env=true
```

### nvm (Node.js — Linux/WSL/macOS)

> For macOS-specific nvm setup (Apple Silicon paths), see `mac-dev`.
> This section covers nvm usage commands (cross-platform).

```bash
# Install nvm (Linux/WSL)
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
source ~/.bashrc

# Usage (same on all platforms)
nvm install --lts                    # install latest LTS
nvm install 20                       # install specific major
nvm use 20                           # switch for current shell
nvm alias default 'lts/*'           # set default
nvm list                            # installed versions
nvm ls-remote --lts                 # available LTS versions

# Pin per project
echo "20" > .nvmrc                  # create version file
nvm use                             # auto-reads .nvmrc
```

---

## Version Pinning Best Practices

| Manager | Pin file | Auto-switch | Platforms |
|---|---|---|---|
| SDKMAN | `.sdkmanrc` | `sdkman_auto_env=true` | Linux, WSL, macOS |
| nvm | `.nvmrc` | Shell hook or manual `nvm use` | Linux, WSL, macOS |
| nvm-windows | `.nvmrc` | Manual `nvm use` | Windows |
| jenv | `.java-version` | Automatic (shell hook) | macOS (see mac-dev) |

**Rule:** Always pin versions in project root. CI and teammates get the same version.

---

## Downloads — Manual Install (fallback)

| Tool | Source | When to use |
|---|---|---|
| OpenJDK | [jdk.java.net](https://jdk.java.net/) | Latest EA builds |
| Eclipse Temurin | [adoptium.net](https://adoptium.net/) | Recommended JDK |
| Gradle | [gradle.org/install](https://gradle.org/install/) | Manual install |
| Maven | [maven.apache.org](https://maven.apache.org/download.cgi) | Manual install |
| Node.js | [nodejs.org](https://nodejs.org/) | Direct download |
| Git | [git-scm.com](https://git-scm.com/) | Windows installer |

---

## Common Errors & Fixes (Windows)

| Error | Cause | Fix |
|---|---|---|
| `java is not recognized` | JAVA_HOME not set or not on PATH | Set JAVA_HOME + add `%JAVA_HOME%\bin` to PATH |
| `gradlew.bat : File cannot be loaded` | Execution policy | `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned` |
| winget not found | Old Windows version | Update App Installer from Microsoft Store |
| PATH too long (> 2048 chars) | Accumulated installs | Clean up unused PATH entries in System Properties |
| `JAVA_HOME is set to an invalid directory` | Points to deleted JDK | Update JAVA_HOME to valid path |
| npm EACCES / permission denied | Global install conflict | Use nvm-windows instead of system Node |

---

## Common Errors & Fixes (Linux)

| Error | Cause | Fix |
|---|---|---|
| `java: command not found` | JDK not installed | `sdk install java 21.0.3-tem` (SDKMAN) |
| `E: Unable to locate package` | Index stale or wrong name | `sudo apt update` then retry |
| `Permission denied` on install | Missing sudo | Prefix with `sudo` (apt) or use SDKMAN (no sudo) |
| SDKMAN `sdk: command not found` | Not sourced in shell | Add `source ~/.sdkman/bin/sdkman-init.sh` to `~/.bashrc` |
| PATH changes not persisting | Added to wrong file | Add to `~/.bashrc` (interactive) or `~/.profile` (login) |
| `dpkg was interrupted` | Broken install | `sudo dpkg --configure -a` then `sudo apt install -f` |

---

## Skill Routing — Which Skill to Use When

| Situation | Skill |
|---|---|
| **On Windows** — install anything, set PATH, JAVA_HOME, winget | **`package-manager`** ← you are here |
| **On Linux/WSL** — apt, SDKMAN, env vars, `~/.bashrc` | **`package-manager`** ← you are here |
| On macOS — install anything via Homebrew, shell config | `mac-dev` |
| On macOS — set JAVA_HOME, jenv, nvm (Apple Silicon) | `mac-dev` |
| Any OS — run `./gradlew`, `mvn`, build lifecycle commands | `java-build` |

### Boundary Rules

```text
package-manager = Windows + Linux — winget, apt, SDKMAN, nvm, env vars
mac-dev         = macOS ONLY — Homebrew IS the package manager on Mac
java-build      = ANY OS — purely Gradle/Maven commands; never installs
```

> **Install then build:** Use `package-manager` to install the JDK + set JAVA_HOME →
> then `java-build` to compile. `java-build` assumes JDK is already on PATH.
