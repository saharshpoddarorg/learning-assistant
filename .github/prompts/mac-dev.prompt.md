---
name: mac-dev
description: 'macOS development environment setup — Homebrew, npm/nvm, JDK, IDEs, Docker, shell aliases, dotfiles, and dev environment bootstrap. Interactive 3-tier guide from first install to full automation.'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Tool / Topic
${input:topic:What do you want to set up or learn? (homebrew / npm / nvm / jdk / ide / docker / shell / aliases / dotfiles / brewfile / bootstrap)}

## Specific Area
${input:area:What specific aspect? (e.g., install, commands, switch-versions, configure, automate, troubleshoot)}

## Current Level
${input:level:Your experience level? (newbie / amateur / pro)}

## Instructions

Guide the user on macOS development environment setup. Adapt depth and detail to their level.
Always prefer Homebrew for installation. Always recommend open-source JDK distributions (Temurin, Corretto, Zulu).
Reference module guides in `mac-os/docs/` for detailed reference.

### Mac Dev Domain Map

```
macOS Development Environment
│
├── Package Management
│   ├── Homebrew (formulae + casks)
│   │   ├── Install: /bin/bash -c "$(curl ...install.sh)"
│   │   ├── Core commands: brew install, search, update, upgrade, cleanup
│   │   ├── Casks: GUI apps (IDEs, Docker, Postman, etc.)
│   │   ├── Taps: third-party repos
│   │   └── Brewfile: reproducible environment
│   └── npm / npx
│       ├── Install via nvm (recommended) or direct Homebrew
│       ├── nvm: multi-version Node.js management
│       ├── npm commands: install, run, ci, audit, outdated
│       └── Global tools: typescript, nodemon, eslint, prettier
│
├── Core Dev Runtimes
│   ├── JDK (Java)
│   │   ├── Distributions: Temurin, Corretto, Zulu, GraalVM
│   │   ├── Install: brew install --cask temurin@21
│   │   ├── Version management: jenv or SDKMAN
│   │   └── JAVA_HOME: managed via /usr/libexec/java_home
│   ├── Node.js (via nvm)
│   ├── Python (via Homebrew or pyenv)
│   └── Go, Rust, Ruby (all via Homebrew)
│
├── IDEs & Editors
│   ├── IntelliJ IDEA (Community / Ultimate)
│   ├── VS Code (universal editor + Java Extension Pack)
│   ├── PyCharm, WebStorm, DataGrip, Rider, Android Studio
│   └── All installed via: brew install --cask <name>
│
├── Containerisation
│   └── Docker Desktop
│       ├── Install: brew install --cask docker
│       ├── Core commands: run, ps, stop, rm, images, system prune
│       ├── Docker Compose: up -d, down, logs -f
│       └── Dev services: postgres, mysql, redis, mongo
│
└── Shell & Environment
    ├── zsh config: ~/.zshrc, ~/.zprofile, ~/.zshenv
    ├── Aliases: git shortcuts, java switching, docker, npm, brew
    ├── PATH management: Homebrew, Go, Cargo, .local/bin
    ├── Plugins: zsh-autosuggestions, zsh-syntax-highlighting, fzf
    ├── Prompt: Starship or oh-my-zsh
    └── Dotfiles: Git-tracked config + bootstrap.sh for new Macs
```

### Response Structure by Level

#### 🟢 Newbie — First steps only
1. **Why this tool/step matters** — what problem it solves
2. **Install it** — exact command (always via Homebrew where possible)
3. **Verify it works** — what to run and what output to expect
4. **Three commands** — the 3 commands they'll use 80% of the time
5. **One mistake to avoid** — the most common newbie error
6. **Next step** — what to install/do next

#### 🟡 Amateur — Practical day-to-day usage
1. **How it works** — mental model of the tool
2. **Essential config** — what to add to `~/.zshrc`, `~/.zprofile`
3. **Command table** — all day-to-day commands with comments
4. **Multiple versions** — switching between JDK/Node versions
5. **Aliases** — time-saving zsh aliases to add
6. **Integration** — how this tool connects to the IDE, CI, other tools
7. **Quick win** — one thing to do right now that improves quality of life

#### 🔴 Pro — Automation & reproducibility
1. **Brewfile strategy** — dump, track in Git, CI/CD new-machine bootstrap
2. **Dotfiles** — directory structure, symlinking approach, Git repo
3. **Bootstrap script** — one-command new-Mac setup
4. **Automation** — `jenv`, `nvm auto-switch`, `direnv` for per-project env vars
5. **macOS `defaults`** — key repeat, Finder, Dock settings
6. **Team onboarding** — how to share the environment with a team
7. **CI/CD implications** — GitHub Actions setup for the tools used

### Quick Command Reference

#### Homebrew Essentials
```zsh
brew install <formula>                        # CLI tool or runtime
brew install --cask <name>                    # GUI application
brew search <term>                            # search all packages
brew update && brew upgrade && brew cleanup   # full update cycle
brew bundle dump                              # generate Brewfile
brew bundle install                           # install from Brewfile
```

#### JDK Management
```zsh
brew install --cask temurin@21                # install Temurin Java 21
/usr/libexec/java_home -V                     # list installed JDKs
export JAVA_HOME=$(/usr/libexec/java_home -v 21)  # switch Java 21
jenv global 21                                # set default (with jenv)
jenv local 17                                 # set per-project (with jenv)
```

#### npm / nvm
```zsh
nvm install --lts                             # install latest LTS Node.js
nvm use --lts                                 # switch to LTS
nvm list                                      # list installed versions
echo "20" > .nvmrc                            # pin version in project
npm install                                   # install from package.json
npm ci                                        # clean install (CI)
npm run <script>                              # run script
```

#### Docker
```zsh
brew install --cask docker                    # install Docker Desktop
docker ps                                     # list running containers
docker compose up -d                          # start services
docker compose down                           # stop services
docker system prune -f                        # cleanup
```

#### Shell Config
```zsh
source ~/.zshrc                               # reload config
echo $JAVA_HOME                               # check JAVA_HOME
echo $PATH | tr ":" "\n"                      # print PATH entries
lsof -i :8080                                 # who is on port 8080
lsof -ti :8080 | xargs kill                  # kill port 8080
```

### Curated Resources
- **Homebrew Docs**: https://docs.brew.sh
- **Homebrew Formulae**: https://formulae.brew.sh
- **Eclipse Temurin**: https://adoptium.net
- **Amazon Corretto**: https://aws.amazon.com/corretto/
- **Azul Zulu**: https://www.azul.com/downloads/
- **GraalVM**: https://www.graalvm.org
- **jenv**: https://www.jenv.be
- **SDKMAN**: https://sdkman.io
- **nvm**: https://github.com/nvm-sh/nvm
- **Docker Docs**: https://docs.docker.com
- **Starship prompt**: https://starship.rs
- **oh-my-zsh**: https://ohmyz.sh

### Module Docs (in this repo)
- [START-HERE](../../mac-os/docs/START-HERE.md)
- [Homebrew Guide](../../mac-os/docs/homebrew-guide.md)
- [JDK Setup](../../mac-os/docs/jdk-setup.md)
- [npm on macOS](../../mac-os/docs/npm-on-mac.md)
- [Dev Tools Guide](../../mac-os/docs/dev-tools-guide.md)
- [Mac Dev Environment](../../mac-os/docs/mac-dev-environment.md)
