---
name: mac-dev
description: >
  macOS development environment workflow — Homebrew advanced usage (Brewfile, services,
  taps, cleanup), JDK version switching (jenv, /usr/libexec/java_home), Apple Silicon
  PATH setup, shell configuration (.zshrc, .zprofile), dotfiles automation, and macOS
  system tuning. Use when asked about setting up a Mac for development, managing a
  reproducible macOS environment, Brewfile automation, jenv, macOS-specific troubleshooting,
  or Apple Silicon dev quirks.
  Delegates to: package-manager (for basic "install X" questions with no macOS workflow context).
---

# macOS Development Environment Skill

> **Scope:** macOS-specific dev workflows. For generic "install X" commands across any OS,
> see `package-manager`. For Java build commands (Gradle/Maven), see `java-build`.

---

## Apple Silicon vs Intel — PATH Differences

| Chip | Homebrew prefix | Shell config file |
|---|---|---|
| Apple Silicon (M1+) | `/opt/homebrew` | `~/.zprofile` |
| Intel | `/usr/local` | `~/.bash_profile` or `~/.zprofile` |

```zsh
# Apple Silicon — add to ~/.zprofile (runs once per login)
eval "$(/opt/homebrew/bin/brew shellenv)"

# Intel — usually automatic, but if needed:
eval "$(/usr/local/bin/brew shellenv)"
```

**Detection:**

```zsh
uname -m                          # arm64 = Apple Silicon, x86_64 = Intel
arch                              # same info
```

---

## Homebrew — Workflow Commands

> Basic install/search/info commands live in `package-manager`.
> This section covers macOS dev workflow automation.

### Brewfile — Reproducible Environment

```zsh
brew bundle dump                             # generate Brewfile from current install
brew bundle dump --file=~/dotfiles/Brewfile  # custom path
brew bundle install                          # install everything in ./Brewfile
brew bundle install --file=~/dotfiles/Brewfile
brew bundle check --verbose                  # what's missing vs Brewfile
brew bundle cleanup --force                  # remove packages NOT in Brewfile
```

**Brewfile format:**

```ruby
# ~/dotfiles/Brewfile
tap "homebrew/cask-fonts"
tap "homebrew/services"

# CLI tools
brew "git"
brew "gh"
brew "jq"
brew "fzf"
brew "ripgrep"
brew "starship"
brew "zsh-autosuggestions"
brew "zsh-syntax-highlighting"

# Languages & runtimes
brew "nvm"
brew "python"

# GUI apps (casks)
cask "temurin"
cask "temurin@21"
cask "visual-studio-code"
cask "intellij-idea"
cask "docker"
cask "iterm2"
cask "rectangle"
cask "raycast"
cask "tableplus"
cask "postman"

# Mac App Store (requires `mas` CLI)
# mas "Xcode", id: 497799835
```

### Taps — Third-Party Repositories

```zsh
brew tap                                     # list taps
brew tap <user/repo>                         # add a tap
brew untap <user/repo>                       # remove a tap
brew tap homebrew/cask-fonts                 # fonts tap
brew tap homebrew/services                   # services tap
```

### Services — Manage Background Daemons

```zsh
brew services list                           # show all services + status
brew services start postgresql@16            # start and auto-launch on login
brew services stop postgresql@16             # stop and remove from login items
brew services restart redis                  # restart
brew services run mysql                      # start once (no auto-launch)
brew services cleanup                        # remove stale plists
```

**Common services:**

```zsh
brew install postgresql@16 && brew services start postgresql@16
brew install redis && brew services start redis
brew install mysql && brew services start mysql
```

### Maintenance Workflow

```zsh
brew update                        # update Homebrew + formula index
brew outdated                      # list packages with available updates
brew upgrade                       # upgrade all
brew upgrade <formula>             # upgrade one
brew cleanup                       # remove old versions (free disk space)
brew autoremove                    # remove unused dependencies
brew doctor                        # diagnose issues
```

---

## Cask Quick Install — GUI Applications

```zsh
brew install --cask temurin                  # Eclipse Temurin JDK (OpenJDK)
brew install --cask temurin@21               # Java 21 LTS specifically
brew install --cask visual-studio-code       # VS Code
brew install --cask intellij-idea            # IntelliJ IDEA Ultimate
brew install --cask intellij-idea-ce         # IntelliJ Community (free)
brew install --cask docker                   # Docker Desktop
brew install --cask iterm2                   # iTerm2 terminal
brew install --cask postman                  # Postman API client
brew install --cask tableplus                # TablePlus DB GUI
brew install --cask dbngin                   # DBngin local DB manager
brew install --cask rectangle                # window manager
brew install --cask raycast                  # Spotlight replacement
brew install --cask graalvm-jdk              # GraalVM (native image)
```

---

## JDK — Version Switching on macOS

### List installed JDKs

```zsh
/usr/libexec/java_home -V                    # list all installed JDKs with paths
```

### Switch versions (manual)

```zsh
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # switch to Java 21
export JAVA_HOME=$(/usr/libexec/java_home -v 17)   # switch to Java 17
java --version                                      # verify
```

### jenv — Automatic per-project switching

```zsh
brew install jenv

# Add to ~/.zshrc
eval "$(jenv init -)"

# Register JDKs
jenv add /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
jenv add /Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home

# Use
jenv versions                               # list registered versions
jenv global 21                              # set global default
jenv local 17                               # per-directory (.java-version file)
jenv enable-plugin export                   # auto-set JAVA_HOME on switch
```

### .zshrc aliases for quick switching

```zsh
alias java21='export JAVA_HOME=$(/usr/libexec/java_home -v 21) && java --version'
alias java17='export JAVA_HOME=$(/usr/libexec/java_home -v 17) && java --version'
alias javaVersions='/usr/libexec/java_home -V'
```

---

## nvm on macOS — Apple Silicon Setup

```zsh
brew install nvm

# Add to ~/.zshrc (Apple Silicon paths)
export NVM_DIR="$HOME/.nvm"
[ -s "/opt/homebrew/opt/nvm/nvm.sh" ] && \. "/opt/homebrew/opt/nvm/nvm.sh"
[ -s "/opt/homebrew/opt/nvm/etc/bash_completion.d/nvm" ] && \. "/opt/homebrew/opt/nvm/etc/bash_completion.d/nvm"

# Usage
nvm install --lts                            # install latest LTS Node.js
nvm use --lts                               # switch to LTS
nvm alias default 'lts/*'                   # set default
nvm list                                    # list installed versions
echo "20" > .nvmrc                          # pin version per project
nvm use                                     # auto-read .nvmrc
```

---

## Shell Configuration — .zshrc / .zprofile

### File roles

| File | When it runs | Put here |
|---|---|---|
| `~/.zprofile` | Once per login session | `eval "$(brew shellenv)"`, env exports |
| `~/.zshrc` | Every new terminal window/tab | Aliases, plugins, prompt, PATH additions |

### Essential .zshrc structure

```zsh
# === Environment ===
export JAVA_HOME=$(/usr/libexec/java_home)
export EDITOR="code --wait"
export LANG="en_US.UTF-8"
export PATH="$HOME/.local/bin:$PATH"

# === NVM ===
export NVM_DIR="$HOME/.nvm"
[ -s "/opt/homebrew/opt/nvm/nvm.sh" ] && \. "/opt/homebrew/opt/nvm/nvm.sh"

# === jenv ===
eval "$(jenv init -)"

# === Plugins ===
source /opt/homebrew/share/zsh-autosuggestions/zsh-autosuggestions.zsh
source /opt/homebrew/share/zsh-syntax-highlighting/zsh-syntax-highlighting.zsh

# === Prompt ===
eval "$(starship init zsh)"

# === Aliases ===
alias java21='export JAVA_HOME=$(/usr/libexec/java_home -v 21) && java --version'
alias java17='export JAVA_HOME=$(/usr/libexec/java_home -v 17) && java --version'
alias ll='ls -la'
alias gst='git status'
alias gco='git checkout'
alias gcm='git commit -m'

# === fzf ===
[ -f ~/.fzf.zsh ] && source ~/.fzf.zsh
```

### Reload

```zsh
source ~/.zshrc                              # reload without restarting terminal
exec zsh                                     # full restart of shell
```

---

## Dotfiles Automation

### Bootstrap pattern

```zsh
# ~/dotfiles/bootstrap.sh
#!/bin/zsh
set -e

echo "Installing Homebrew..."
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
eval "$(/opt/homebrew/bin/brew shellenv)"

echo "Installing from Brewfile..."
brew bundle install --file=~/dotfiles/Brewfile

echo "Symlinking configs..."
ln -sf ~/dotfiles/.zshrc ~/.zshrc
ln -sf ~/dotfiles/.zprofile ~/.zprofile
ln -sf ~/dotfiles/.gitconfig ~/.gitconfig
ln -sf ~/dotfiles/starship.toml ~/.config/starship.toml

echo "Setting macOS defaults..."
source ~/dotfiles/macos-defaults.sh

echo "Done! Restart terminal."
```

### macOS system defaults

```zsh
# ~/dotfiles/macos-defaults.sh
defaults write -g KeyRepeat -int 1                                  # fast key repeat
defaults write -g InitialKeyRepeat -int 10                          # short delay
defaults write -g ApplePressAndHoldEnabled -bool false              # key repeat over accents
defaults write com.apple.finder AppleShowAllFiles YES               # show hidden files
defaults write com.apple.dock autohide -bool true                   # auto-hide dock
defaults write com.apple.dock tilesize -int 36                      # smaller dock icons
defaults write NSGlobalDomain NSAutomaticSpellingCorrectionEnabled -bool false
killall Finder Dock
```

---

## Docker Desktop on macOS — Config Tips

```zsh
brew install --cask docker                   # install Docker Desktop
```

**Settings to configure (Docker Desktop → Settings):**

| Setting | Recommended | Why |
|---|---|---|
| Resources → CPUs | 4+ | Faster builds |
| Resources → Memory | 8 GB+ | Avoids OOM on multi-container setups |
| File sharing → VirtioFS | Enable | Much faster volume mounts on Apple Silicon |
| General → Start on login | Disable | Start manually to save RAM when not needed |

**Verify:**

```zsh
docker --version && docker compose version
```

---

## Common Errors & Fixes

| Error | Cause | Fix |
|---|---|---|
| `brew: command not found` | Homebrew not on PATH | Add to `~/.zprofile`: `eval "$(/opt/homebrew/bin/brew shellenv)"` |
| `java: command not found` | JDK not installed or JAVA_HOME not set | `brew install --cask temurin` then export JAVA_HOME |
| Cask app "damaged or can't be opened" | Gatekeeper quarantine | `xattr -dr com.apple.quarantine /Applications/App.app` |
| Wrong JDK version used | JAVA_HOME stale | `export JAVA_HOME=$(/usr/libexec/java_home -v 21)` |
| Port already in use | Another process on same port | `lsof -ti :8080 \| xargs kill` |
| `brew doctor` warnings after macOS update | Xcode CLI tools stale | `xcode-select --install` or `sudo xcode-select --reset` |
| nvm: command not found | Missing source in .zshrc | Add nvm init lines (see nvm section above) |
| Homebrew slow on Apple Silicon | Rosetta conflict | Ensure using `/opt/homebrew` not `/usr/local` |

---

## 3-Tier Quick Reference

### 🟢 Newbie — Get Running

```zsh
# 1. Install Homebrew
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
# 2. Follow "Next steps" printed by installer (add to PATH)

# 3. Install core tools
brew install --cask temurin visual-studio-code iterm2

# 4. Verify
java --version && code --version
```

### 🟡 Amateur — Structure Your Environment

```zsh
# IDEs + Tools
brew install --cask intellij-idea docker postman tableplus rectangle

# Node via nvm
brew install nvm && nvm install --lts && nvm use --lts

# Shell plugins
brew install zsh-autosuggestions zsh-syntax-highlighting fzf starship
# → add source lines to ~/.zshrc

# jenv for JDK switching
brew install jenv && eval "$(jenv init -)"
```

### 🔴 Pro — Automate Everything

```zsh
# Brewfile for reproducibility
brew bundle dump --file=~/dotfiles/Brewfile

# Dotfiles repo
git init ~/dotfiles
ln -sf ~/dotfiles/.zshrc ~/.zshrc
ln -sf ~/dotfiles/.zprofile ~/.zprofile

# Bootstrap script for new machines
chmod +x ~/dotfiles/bootstrap.sh

# macOS defaults automation
source ~/dotfiles/macos-defaults.sh
```

---

## Curated Resources

| Resource | URL | Level |
|---|---|---|
| Homebrew Docs | <https://docs.brew.sh> | All |
| Homebrew Formulae Search | <https://formulae.brew.sh> | All |
| Eclipse Temurin | <https://adoptium.net> | Newbie |
| jenv | <https://www.jenv.be> | Amateur/Pro |
| nvm | <https://github.com/nvm-sh/nvm> | All |
| Starship Prompt | <https://starship.rs> | Amateur/Pro |
| GraalVM | <https://www.graalvm.org> | Pro |
| Docker Desktop for Mac | <https://docs.docker.com/desktop/mac/install/> | All |

---

## Module Docs Quick Links

| Guide | Path |
|---|---|
| START HERE | `mac-os/docs/START-HERE.md` |
| Homebrew Guide | `mac-os/docs/homebrew-guide.md` |
| JDK Setup | `mac-os/docs/jdk-setup.md` |
| npm on macOS | `mac-os/docs/npm-on-mac.md` |
| Dev Tools (IDEs, Docker) | `mac-os/docs/dev-tools-guide.md` |
| Full Environment (Shell, Dotfiles) | `mac-os/docs/mac-dev-environment.md` |
