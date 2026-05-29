---
name: package-manager
description: >
  Cross-platform package management and dev tool installation (macOS, Windows, Linux).
  The central skill for all "install X" questions. Other skills delegate here when
  a tool, runtime, or dependency needs to be installed.
  Activates on: install, brew, homebrew, winget, choco, apt, sdkman, setup, download,
  dependency install, tool setup, environment variable, JAVA_HOME, PATH, sdk install,
  nvm install, version manager, pin version.
  Delegates to: mac-dev (for macOS workflow automation — Brewfile, brew services, dotfiles).
---

# Package Manager Skill

> **Delegation rule:** Any skill needing install commands defers here.
> For macOS-specific *workflow* automation (Brewfile, services, dotfiles, jenv), see `mac-dev`.
> For Java *build* commands (Gradle/Maven), see `java-build`.

## macOS — Homebrew

```bash
brew install <package>           # install a package
brew upgrade <package>           # upgrade specific package
brew upgrade                     # upgrade all
brew list                        # installed packages
brew search <term>               # find packages
brew info <package>              # details + dependencies
brew uninstall <package>         # remove
brew cleanup                     # remove old versions
```

### Common Dev Tool Installs

```bash
brew install openjdk             # Java (OpenJDK)
brew install gradle              # Gradle build tool
brew install maven               # Maven build tool
brew install node                # Node.js + npm
brew install python              # Python 3
brew install git                 # Git (latest)
brew install gh                  # GitHub CLI
brew install docker              # Docker CLI
```

### Taps (Third-Party Formulae)

```bash
brew tap <user/repo>             # add a tap
brew tap                         # list taps
brew untap <user/repo>           # remove a tap
```

### Environment After Install (add to ~/.zshrc)

```bash
export JAVA_HOME=$(/usr/libexec/java_home)
export PATH="$JAVA_HOME/bin:$PATH"
```

---

## Windows — winget / manual

```powershell
winget install <package-id>      # install
winget upgrade <package-id>      # upgrade specific
winget upgrade --all             # upgrade all
winget list                      # installed packages
winget search <term>             # find packages
```

### Common Dev Tool Installs

```powershell
winget install Microsoft.OpenJDK.<version>
winget install Gradle.Gradle
winget install Apache.Maven
winget install OpenJS.NodeJS
winget install Git.Git
winget install GitHub.cli
```

### Environment Variables (System Properties or PowerShell)

```powershell
$env:JAVA_HOME = "C:\Program Files\OpenJDK\jdk-<version>"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
$env:GRADLE_HOME = "C:\Gradle\gradle-<version>"
$env:PATH = "$env:GRADLE_HOME\bin;" + $env:PATH
```

---

## Linux / WSL — apt + sdkman

```bash
sudo apt update && sudo apt install <package>
sudo apt upgrade <package>
apt list --installed
apt search <term>
```

### SDKMAN! (JVM tools)

```bash
curl -s "https://get.sdkman.io" | bash
sdk list java                    # available JDK distributions
sdk install java <version>-open  # install OpenJDK
sdk default java <version>-open  # set global default
sdk install gradle               # install Gradle
sdk install maven                # install Maven
```

### Environment

```bash
# Auto-set by SDKMAN, or add to ~/.bashrc
export JAVA_HOME="$HOME/.sdkman/candidates/java/current"
export PATH="$JAVA_HOME/bin:$PATH"
```

---

## Downloads

| Tool | Source |
|---|---|
| OpenJDK | [jdk.java.net](https://jdk.java.net/) |
| Gradle | [gradle.org/install](https://gradle.org/install/) |
| Maven | [maven.apache.org/download](https://maven.apache.org/download.cgi) |
| Node.js | [nodejs.org](https://nodejs.org/) |
| Git | [git-scm.com](https://git-scm.com/) |

---

## Version Managers — Cross-Platform

### SDKMAN! (JVM ecosystem — Java, Gradle, Maven, Kotlin)

```bash
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# List available JDK distributions
sdk list java

# Install and manage versions
sdk install java 21.0.3-tem          # install Temurin 21
sdk install java 17.0.11-tem         # install Temurin 17
sdk default java 21.0.3-tem          # set global default
sdk use java 17.0.11-tem             # switch for current shell only

# Other JVM tools
sdk install gradle                   # latest Gradle
sdk install maven                    # latest Maven
sdk install kotlin                   # Kotlin compiler

# Pin version per project
sdk env init                         # creates .sdkmanrc in project root
sdk env                              # apply versions from .sdkmanrc
```

**`.sdkmanrc` format:**

```properties
java=21.0.3-tem
gradle=8.8
```

### nvm (Node.js version manager)

```bash
# Install nvm (see OS-specific sections above for install commands)

nvm install --lts                    # install latest LTS
nvm install 20                       # install specific major
nvm use 20                           # switch for current shell
nvm alias default 'lts/*'           # set default
nvm list                            # list installed versions
nvm ls-remote --lts                 # list available LTS versions

# Pin version per project
echo "20" > .nvmrc                  # create version file
nvm use                             # auto-reads .nvmrc
```

### Version Pinning Best Practices

| Manager | Pin file | Auto-switch |
|---|---|---|
| SDKMAN | `.sdkmanrc` | `sdk env` (or `sdkman_auto_env=true` in config) |
| nvm | `.nvmrc` | Manual `nvm use` (or add auto-switch to .zshrc) |
| jenv | `.java-version` | Automatic (shell hook) |

**Rule:** Always pin versions in project root for reproducibility. CI and teammates
get the same version without guessing.
