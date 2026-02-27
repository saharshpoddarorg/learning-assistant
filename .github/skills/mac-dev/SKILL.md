---
name: mac-dev
description: >
  macOS development environment setup: Homebrew, npm/nvm, JDK installations,
  IDEs (IntelliJ IDEA, VS Code), Docker Desktop, shell aliases, PATH management,
  and dotfiles automation. Use when asked about setting up a Mac for development,
  installing tools on macOS, Homebrew commands, managing multiple JDK/Node versions,
  or automating Mac dev environment bootstrap.
---

# macOS Development Environment Skill

## Homebrew — Core Commands Cheatsheet

### Install & Search
```zsh
brew install <formula>              # install a CLI tool or runtime
brew install --cask <name>         # install a GUI application
brew search <term>                 # search formulae + casks
brew info <formula>                # show formula details & deps
brew deps --tree <formula>         # dependency tree
```

### Update & Maintain
```zsh
brew update                        # update Homebrew + formula index
brew upgrade                       # upgrade all installed packages
brew upgrade <formula>             # upgrade one package
brew cleanup                       # remove old versions (free space)
brew autoremove                    # remove unused dependencies
brew doctor                        # diagnose issues
brew list --versions               # list installed packages with versions
```

### Cask Quick Install (GUI Apps)
```zsh
brew install --cask temurin                  # Eclipse Temurin JDK (OpenJDK)
brew install --cask visual-studio-code       # VS Code
brew install --cask intellij-idea            # IntelliJ IDEA
brew install --cask intellij-idea-ce         # IntelliJ Community (free)
brew install --cask docker                   # Docker Desktop
brew install --cask iterm2                   # iTerm2 terminal
brew install --cask postman                  # Postman API client
brew install --cask tableplus                # TablePlus DB GUI
brew install --cask dbngin                   # DBngin local DB manager
brew install --cask rectangle                # window manager
brew install --cask raycast                  # Spotlight replacement
```

### Brewfile (Reproducible Environment)
```zsh
brew bundle dump                             # generate Brewfile from current install
brew bundle install                          # install everything in ./Brewfile
brew bundle check --verbose                  # check what's missing
brew bundle cleanup --force                  # remove packages not in Brewfile
```

---

## JDK — Installation & Management

### Install JDKs via Homebrew
```zsh
brew install --cask temurin                  # Eclipse Temurin (recommended)
brew install --cask temurin@21               # Java 21 LTS specifically
brew install --cask temurin@17               # Java 17 LTS
brew install --cask amazon-corretto          # Amazon Corretto
brew install --cask amazon-corretto@21
brew install --cask zulu                     # Azul Zulu
brew install --cask graalvm-jdk              # GraalVM (native image)
```

### List & Switch JDK versions
```zsh
/usr/libexec/java_home -V                    # list all installed JDKs
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # switch to Java 21
export JAVA_HOME=$(/usr/libexec/java_home -v 17)   # switch to Java 17

# jenv — automatic switching per project
brew install jenv
jenv add /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
jenv global 21                               # set global default
jenv local 17                                # set local version (.java-version file)
jenv enable-plugin export                    # auto-set JAVA_HOME
```

### .zshrc aliases for JDK switching
```zsh
alias java21='export JAVA_HOME=$(/usr/libexec/java_home -v 21) && java --version'
alias java17='export JAVA_HOME=$(/usr/libexec/java_home -v 17) && java --version'
alias javaVersions='/usr/libexec/java_home -V'
```

---

## npm / nvm — Node.js Version Management

### nvm (recommended)
```zsh
brew install nvm

# Add to ~/.zshrc
export NVM_DIR="$HOME/.nvm"
[ -s "/opt/homebrew/opt/nvm/nvm.sh" ] && \. "/opt/homebrew/opt/nvm/nvm.sh"

# Use it
nvm install --lts                            # install latest LTS Node.js
nvm use --lts                               # switch to LTS
nvm alias default 'lts/*'                   # set default
nvm list                                    # list installed versions
echo "20" > .nvmrc                          # pin version per project
nvm use                                     # auto-read .nvmrc
```

### npm essentials
```zsh
npm install                                  # install from package.json
npm install <pkg>                           # add dependency
npm install -D <pkg>                        # add devDependency
npm install -g <pkg>                        # install globally
npm ci                                      # clean install (CI)
npm run <script>                            # run script from package.json
npm outdated                                # check for updates
npm audit                                   # security audit
npm audit fix                               # fix vulnerabilities
```

---

## Shell Config — Aliases & PATH (~/.zshrc)

### Essential PATH setup
```zsh
# Homebrew (in ~/.zprofile)
eval "$(/opt/homebrew/bin/brew shellenv)"

# In ~/.zshrc
export JAVA_HOME=$(/usr/libexec/java_home)
export GOPATH="$HOME/go"
export PATH="$GOPATH/bin:$HOME/.cargo/bin:$HOME/.local/bin:$PATH"
export EDITOR="code --wait"
export LANG="en_US.UTF-8"
```

### Reload config
```zsh
source ~/.zshrc                              # reload without restarting terminal
```

---

## Docker on macOS — Core Commands

### Installation
```zsh
brew install --cask docker                   # Docker Desktop for Mac
```

### Essential commands
```zsh
docker ps                                    # list running containers
docker ps -a                                 # all containers
docker pull <image>                          # pull image
docker run -d -p 5432:5432 postgres:16       # run Postgres in background
docker stop <container-id>                   # stop container
docker rm <container-id>                     # remove container
docker images                               # list local images
docker system prune -f                       # cleanup
docker compose up -d                         # start all services (docker-compose.yml)
docker compose down                          # stop all services
docker compose logs -f                       # follow logs
docker exec -it <id> bash                    # shell into container
```

---

## Common Errors & Fixes

| Error | Cause | Fix |
|---|---|---|
| `brew: command not found` | Homebrew not on PATH | Add to `~/.zshrc`: `eval "$(/opt/homebrew/bin/brew shellenv)"` |
| `java: command not found` | JDK not installed or JAVA_HOME not set | `brew install --cask temurin` then `export JAVA_HOME=$(/usr/libexec/java_home)` |
| `npm: command not found` | Node.js not installed via nvm | `nvm install --lts && nvm use --lts` |
| `EACCES` on npm install | Running npm as root | Never `sudo npm`; use `nvm` |
| Docker Desktop not started | Docker daemon not running | Open Docker.app from Applications |
| Cask app "damaged or can't be opened" | Gatekeeper quarantine | `xattr -dr com.apple.quarantine /Applications/App.app` |
| Wrong JDK version used | JAVA_HOME points to old version | `export JAVA_HOME=$(/usr/libexec/java_home -v 21)` |
| Port already in use | Another process on same port | `lsof -ti :8080 \| xargs kill` |

---

## 3-Tier Quick Reference

### 🟢 Newbie (Get Running)
```zsh
# 1. Install Homebrew
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
# 2. Follow "Next steps" printed by installer (add to PATH)

# 3. Install core tools
brew install --cask temurin visual-studio-code iterm2

# 4. Verify
java --version && code --version
```

### 🟡 Amateur (Structure Your Environment)
```zsh
# IDEs
brew install --cask intellij-idea docker postman tableplus

# Node
brew install nvm
nvm install --lts && nvm use --lts

# Shell plugins
brew install zsh-autosuggestions zsh-syntax-highlighting fzf starship
# Add to ~/.zshrc + add aliases

# Git config
git config --global user.name "Your Name"
git config --global user.email "your@email.com"
git config --global core.editor "code --wait"
```

### 🔴 Pro (Automate Everything)
```zsh
# Brewfile for reproducibility
brew bundle dump --file=~/dotfiles/Brewfile
brew bundle install --file=~/dotfiles/Brewfile

# jenv for automatic JDK version switching
brew install jenv
echo 'eval "$(jenv init -)"' >> ~/.zshrc

# Dotfiles in Git
git init ~/dotfiles && cd ~/dotfiles
# symlink configs: ln -sf ~/dotfiles/.zshrc ~/.zshrc
# Bootstrap script: bootstrap.sh that runs brew bundle + symlinks

# macOS defaults
defaults write -g KeyRepeat -int 1
defaults write -g ApplePressAndHoldEnabled -bool false
defaults write com.apple.finder AppleShowAllFiles YES && killall Finder
```

---

## Curated Resources

| Resource | URL | Level |
|---|---|---|
| Homebrew Docs | https://docs.brew.sh | All |
| Homebrew Formulae Search | https://formulae.brew.sh | All |
| Eclipse Temurin | https://adoptium.net | Newbie |
| jenv | https://www.jenv.be | Amateur/Pro |
| SDKMAN | https://sdkman.io | Amateur/Pro |
| nvm | https://github.com/nvm-sh/nvm | All |
| npm Docs | https://docs.npmjs.com | All |
| oh-my-zsh | https://ohmyz.sh | Amateur |
| Starship Prompt | https://starship.rs | Amateur/Pro |
| GraalVM | https://www.graalvm.org | Pro |
| Docker Docs | https://docs.docker.com | All |

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
