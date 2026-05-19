---
name: package-manager
description: >
  System package management and dev tool installation across macOS, Windows, and Linux.
  This is the **central skill for all installations**. Other skills delegate here when
  a tool, runtime, or dependency needs to be installed.
  Activates on: install, brew, homebrew, winget, choco, apt, sdkman, setup, download,
  dependency install, tool setup, environment variable, JAVA_HOME, PATH.
---

# Package Manager Skill

> **Delegation rule:** Any skill that requires a tool/runtime to be installed should
> defer to this skill for the install commands. Do not inline install instructions
> in other skills — reference `package-manager` instead.

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
