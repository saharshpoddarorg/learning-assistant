# 🍺 Homebrew — The macOS Package Manager

> **The single most important tool on your Mac for software development.**  
> Homebrew lets you install command-line tools, languages, runtimes, and GUI apps  
> (via Casks) with a single command — no hunting for installers, no PATH wrangling.

---

## 📑 Table of Contents

- [What Is Homebrew?](#-what-is-homebrew)
- [Installation](#-installation)
- [Core Concepts](#-core-concepts)
- [🟢 Newbie — Essential Commands](#-newbie--essential-commands)
- [🟡 Amateur — Casks, Taps & Organization](#-amateur--casks-taps--organization)
- [🔴 Pro — Brewfile, Services & Automation](#-pro--brewfile-services--automation)
- [Common Formulae for Developers](#-common-formulae-for-developers)
- [Troubleshooting](#-troubleshooting)
- [Curated Resources](#-curated-resources)

---

## 🔍 What Is Homebrew?

Homebrew is the **de facto package manager for macOS** (and Linux). It:

- Installs **CLI tools and languages** (formulae): `git`, `java`, `python`, `go`, `node`, `wget`, `jq`
- Installs **GUI apps** (casks): VS Code, IntelliJ IDEA, Docker Desktop, Chrome, iTerm2
- Manages **services** (databases, web servers) you can start/stop with `brew services`
- Lets you **bundle your entire setup** into a `Brewfile` for reproducibility

```
Without Homebrew:  Google → download .pkg → drag to Applications → configure PATH
With Homebrew:     brew install --cask intellij-idea
```

---

## ⚙️ Installation

### Prerequisite — Xcode Command Line Tools

Homebrew needs the Xcode CLI tools. Homebrew's installer will prompt for them, but you
can install manually first:
```zsh
xcode-select --install
```

### Install Homebrew
```zsh
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

### Post-install — Add to PATH (Apple Silicon / M1/M2/M3/M4 Macs)

After installation, the installer prints a "Next steps" block. Run it:
```zsh
# Add these two lines to your ~/.zprofile (or ~/.zshrc)
echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
eval "$(/opt/homebrew/bin/brew shellenv)"
```

> **Intel Mac?** Homebrew installs to `/usr/local/` and is usually on PATH automatically.

### Verify
```zsh
brew --version      # should print: Homebrew 4.x.x
brew doctor         # diagnose any issues
```

---

## 📚 Core Concepts

| Concept | What It Is | Example |
|---|---|---|
| **Formula** | CLI tool / library / runtime | `brew install git` |
| **Cask** | GUI application (macOS .app) | `brew install --cask visual-studio-code` |
| **Tap** | Third-party formula repository | `brew tap homebrew/cask-fonts` |
| **Keg** | Installed version of a formula | `/opt/homebrew/Cellar/git/2.44.0/` |
| **Cellar** | Directory where everything is stored | `/opt/homebrew/Cellar/` |
| **Brewfile** | Manifest of all your packages | `brew bundle` reads this |
| **Service** | Background process managed by Homebrew | `brew services start postgresql` |

---

## 🟢 Newbie — Essential Commands

### Install a package
```zsh
brew install <formula>             # install a CLI tool or runtime
brew install --cask <name>         # install a GUI application
```

### Search before installing
```zsh
brew search java                   # find all java-related packages
brew search --casks intellij       # search only casks
brew info temurin                  # get info about a specific package
```

### Update & upgrade
```zsh
brew update                        # update Homebrew itself and formula index
brew upgrade                       # upgrade ALL installed packages
brew upgrade <formula>             # upgrade one specific package
```

### List & check installed
```zsh
brew list                          # list all installed formulae
brew list --casks                  # list all installed casks
brew list --versions               # show versions too
```

### Uninstall
```zsh
brew uninstall <formula>           # remove a formula
brew uninstall --cask <name>       # remove a GUI app
```

### Clean up
```zsh
brew cleanup                       # remove old versions, free disk space
brew cleanup --dry-run             # preview what would be deleted
brew autoremove                    # remove unused dependencies
```

---

## 🟡 Amateur — Casks, Taps & Organization

### Casks — Installing GUI Apps

```zsh
# Development IDEs
brew install --cask intellij-idea              # IntelliJ IDEA (Community ed)
brew install --cask intellij-idea-ce           # Community Edition explicitly
brew install --cask visual-studio-code         # VS Code
brew install --cask webstorm                   # WebStorm (JS IDE)
brew install --cask datagrip                   # DataGrip (Database IDE)
brew install --cask rider                      # Rider (.NET IDE)
brew install --cask pycharm                    # PyCharm (Python IDE)

# Containerisation
brew install --cask docker                     # Docker Desktop

# Terminals & Shell
brew install --cask iterm2                     # iTerm2 (better Terminal.app)
brew install --cask warp                       # Warp (modern AI terminal)

# API & Dev Utilities
brew install --cask postman                    # API client
brew install --cask insomnia                   # API client (alternative)
brew install --cask tableplus                  # database GUI client
brew install --cask dbngin                     # local DB engine manager

# Productivity
brew install --cask rectangle                  # window manager
brew install --cask alt-tab                    # Windows-style alt-tab
brew install --cask raycast                    # launcher (Spotlight replacement)
```

### Taps — Third-Party Repositories

```zsh
brew tap <user/repo>               # add a tap
brew tap homebrew/cask-fonts       # fonts via Homebrew
brew tap homebrew/cask-versions    # older cask versions (e.g., java11)
brew tap adoptopenjdk/openjdk      # legacy: AdoptOpenJDK tap
brew untap <user/repo>             # remove a tap
brew tap                           # list all taps
```

### Searching Across Taps

```zsh
brew search temurin                # finds across all taps
brew info --cask temurin           # detailed info + dependencies
brew deps --tree <formula>         # show dependency tree for a formula
```

### Pinning a Formula (prevent upgrades)

```zsh
brew pin <formula>                 # pin to current version
brew unpin <formula>               # unpin
brew list --pinned                 # list pinned formulae
```

---

## 🔴 Pro — Brewfile, Services & Automation

### Brewfile — Reproducible Environment

A `Brewfile` documents every package you need. Check it into Git.

**Generate a Brewfile from your current installation:**
```zsh
brew bundle dump                   # creates ./Brewfile
brew bundle dump --force           # overwrite existing
brew bundle dump --file=~/dotfiles/Brewfile     # specify path
```

**Example `Brewfile`:**
```ruby
# Taps
tap "homebrew/cask-fonts"
tap "adoptopenjdk/openjdk"

# CLI tools & runtimes
brew "git"
brew "wget"
brew "curl"
brew "jq"
brew "nvm"
brew "gh"                          # GitHub CLI
brew "tldr"                        # simplified man pages
brew "bat"                         # cat with syntax highlighting
brew "fd"                          # fast find alternative
brew "ripgrep"                     # fast grep (used by VS Code)
brew "fzf"                         # fuzzy finder

# GUI apps (casks)
cask "temurin"                     # Eclipse Temurin JDK
cask "visual-studio-code"
cask "intellij-idea"
cask "docker"
cask "iterm2"
cask "postman"
cask "tableplus"
cask "rectangle"
cask "font-jetbrains-mono-nerd-font"
```

**Install everything from Brewfile:**
```zsh
brew bundle install                       # install all in ./Brewfile
brew bundle install --file=~/dotfiles/Brewfile
```

**Check what's missing:**
```zsh
brew bundle check                         # exit 0 = all installed
brew bundle check --verbose               # list what's missing
```

**Clean orphaned packages (not in Brewfile):**
```zsh
brew bundle cleanup                       # show what would be removed
brew bundle cleanup --force               # actually remove
```

### Homebrew Services

Manage background services (databases, Redis, etc.):
```zsh
brew services list                         # list all services + status
brew services start postgresql             # start postgresql
brew services stop postgresql              # stop
brew services restart postgresql           # restart
brew services run postgresql               # start once (no autostart on login)
brew services start --all                  # start ALL services
```

### Advanced Diagnostics

```zsh
brew config                                # show Homebrew config & environment
brew doctor                                # check for known issues
brew info --json=v2 <formula>              # machine-readable JSON output
brew list --versions <formula>             # check all installed versions
brew deps --tree <formula>                 # dependency tree
brew leaves                                # list packages with no dependants (top-level)
brew uses --installed <formula>            # what depends on this formula?
HOMEBREW_NO_AUTO_UPDATE=1 brew install x   # skip auto-update for one install
```

### Keeping Homebrew Fast

```zsh
export HOMEBREW_NO_AUTO_UPDATE=1           # add to .zshrc to skip auto-update
export HOMEBREW_NO_ANALYTICS=1            # disable telemetry
export HOMEBREW_CASK_OPTS="--no-quarantine" # skip Gatekeeper quarantine
```

---

## 📦 Common Formulae for Developers

### Languages & Runtimes
```zsh
brew install --cask temurin                # Eclipse Temurin JDK (OpenJDK) — recommended
brew install --cask temurin@21             # specific version
brew install --cask temurin@17
brew install --cask amazon-corretto        # Amazon Corretto JDK
brew install --cask zulu                   # Azul Zulu JDK
brew install --cask graalvm-jdk            # GraalVM (native image support)
brew install node                          # Node.js (latest)
brew install nvm                           # Node Version Manager (recommended)
brew install python                        # Python 3
brew install pyenv                         # Python version manager
brew install go                            # Go language
brew install rust                          # Rust compiler (or use rustup)
brew install ruby                          # Ruby
brew install rbenv                         # Ruby version manager
```

### Version Control & Collaboration
```zsh
brew install git                           # Git
brew install gh                            # GitHub CLI
brew install git-lfs                       # Git Large File Storage
brew install gitui                         # terminal Git UI
```

### CLI Productivity
```zsh
brew install wget                          # file download
brew install curl                          # HTTP client
brew install jq                            # JSON processor
brew install yq                            # YAML processor
brew install bat                           # cat with syntax highlighting
brew install fd                            # fast file finder
brew install ripgrep                       # fast grep (rg)
brew install fzf                           # fuzzy finder
brew install tldr                          # simplified man pages
brew install tree                          # directory tree display
brew install htop                          # interactive process viewer
brew install watch                         # run command repeatedly
brew install direnv                        # per-directory environment variables
```

### Networking & APIs
```zsh
brew install httpie                        # HTTP client (human-friendly curl)
brew install grpcurl                       # gRPC CLI client
brew install tcpdump                       # network packet capture
brew install nmap                          # network scanner
```

### Databases (CLI)
```zsh
brew install postgresql@16                 # PostgreSQL
brew install mysql                         # MySQL
brew install redis                         # Redis
brew install sqlite                        # SQLite
brew install libpq                         # PostgreSQL client only (psql)
```

---

## 🔧 Troubleshooting

| Symptom | Fix |
|---|---|
| `brew: command not found` | Add Homebrew to PATH: `eval "$(/opt/homebrew/bin/brew shellenv)"` |
| `brew doctor` errors | Read each suggestion; most are harmless warnings |
| Package installs old version | `brew update && brew upgrade <package>` |
| Can't install because of permission errors | `sudo chown -R $(whoami) /opt/homebrew` (M1+) |
| Cask says app is damaged | `xattr -dr com.apple.quarantine /Applications/App.app` |
| Conflicts with system version | Use `brew link --overwrite <formula>` carefully |
| `brew install` takes forever | `HOMEBREW_NO_AUTO_UPDATE=1 brew install <pkg>` |
| Multiple JDK versions conflicting | Use `jenv` to manage (see [JDK Setup Guide](jdk-setup.md)) |

---

## 📎 Curated Resources

| Resource | URL | Level |
|---|---|---|
| Homebrew Official Docs | https://docs.brew.sh | All |
| Homebrew FAQ | https://docs.brew.sh/FAQ | Newbie |
| Homebrew Formula Cookbook | https://docs.brew.sh/Formula-Cookbook | Pro |
| Brew Bundle (Brewfile) | https://github.com/Homebrew/homebrew-bundle | Amateur/Pro |
| Homebrew Cask | https://formulae.brew.sh/cask/ | All |
| Formulae Search | https://formulae.brew.sh | All |

---

**Navigation:** [← START-HERE](START-HERE.md) · [JDK Setup →](jdk-setup.md) · [npm on Mac →](npm-on-mac.md) · [Dev Tools →](dev-tools-guide.md) · [Full Environment →](mac-dev-environment.md)
