# 🍎 Mac Dev Module — START HERE

> **Purpose:** Learn how to set up and use a professional macOS development environment.  
> This module is your incremental, iterative guide — from zero to a fully equipped dev machine.  
> Built for all three tiers: **Newbie → Amateur → Pro**.

---

## 📑 Table of Contents

- [What This Module Covers](#-what-this-module-covers)
- [Choose Your Tier](#-choose-your-tier)
- [Quick Start (10 Minutes)](#-quick-start-10-minutes)  
- [Full Learning Path](#-full-learning-path)
- [All Guides In This Module](#-all-guides-in-this-module)
- [Slash Command Quick Access](#-slash-command-quick-access)

---

## 🎯 What This Module Covers

```
Mac Development Environment
│
├── Package Management
│   ├── Homebrew — the macOS package manager (install everything from one place)
│   └── npm / npx — Node.js package manager for JS/TS dev tools
│
├── Core Dev Runtimes
│   ├── JDK (Java) — open-source distributions via Homebrew
│   ├── Node.js     — install via Homebrew or nvm
│   ├── Python      — install via Homebrew or pyenv
│   └── Go, Rust, Ruby, etc. — all via Homebrew
│
├── Dev Tools & IDEs
│   ├── IntelliJ IDEA — Java IDE (via Homebrew cask)
│   ├── VS Code       — universal editor
│   ├── Xcode CLI     — required by Homebrew itself
│   └── Other: WebStorm, DataGrip, etc.
│
├── Containerisation
│   └── Docker Desktop — install via Homebrew cask
│
└── Terminal Tooling
    ├── zsh, oh-my-zsh, Starship prompt
    ├── Aliases & PATH management
    └── Iterative dotfiles management
```

---

## 🎓 Choose Your Tier

| Tier | Who You Are | Goal |
|---|---|---|
| 🟢 **Newbie** | Fresh Mac, never used Homebrew | Install Homebrew, JDK, VS Code — have a working Java project running |
| 🟡 **Amateur** | Homebrew installed, want to structure the full environment | Add Docker, IntelliJ, configure nvm, set up shell aliases |
| 🔴 **Pro** | Full environment, want efficiency | Dotfiles automation, Brewfile, bootstrap scripts, team onboarding |

---

## ⚡ Quick Start (10 Minutes)

### Step 1 — Install Homebrew (the foundation of everything)
```zsh
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```
After installation, follow the "Next steps" printed in the terminal (add Homebrew to PATH).

### Step 2 — Install a JDK (open-source)
```zsh
brew install --cask temurin          # Eclipse Temurin (OpenJDK) — recommended
```

### Step 3 — Install VS Code
```zsh
brew install --cask visual-studio-code
```

### Step 4 — Verify everything works
```zsh
java --version    # should print 21 or later
javac --version
code --version
```

> **Done!** You now have a working Java development environment on macOS in 10 minutes.

---

## 📚 Full Learning Path

### 🟢 Newbie Path — "Get me running"

| Step | Read | Learn |
|---|---|---|
| 1 | [Homebrew Guide](homebrew-guide.md) | What Homebrew is, how to install it, basic commands |
| 2 | [JDK Setup Guide](jdk-setup.md) | Install open-source JDK (Temurin/Corretto) via Homebrew |
| 3 | [Dev Tools Guide](dev-tools-guide.md) §1 | Install VS Code |
| 4 | [npm on macOS](npm-on-mac.md) §1 | Install Node.js + npm (if doing web/JS work) |

### 🟡 Amateur Path — "Structure my environment"

| Step | Read | Learn |
|---|---|---|
| 1 | [Homebrew Guide](homebrew-guide.md) §Casks | Install IDEs and GUI tools |
| 2 | [Dev Tools Guide](dev-tools-guide.md) | IntelliJ IDEA, Docker Desktop, DBngin |
| 3 | [npm on macOS](npm-on-mac.md) | nvm, global packages, package.json scripts |
| 4 | [Mac Dev Environment](mac-dev-environment.md) §Shell | Aliases, PATH, zsh config |

### 🔴 Pro Path — "Automate and scale"

| Step | Read | Learn |
|---|---|---|
| 1 | [Homebrew Guide](homebrew-guide.md) §Brewfile | Create a Brewfile to reproduce environment in minutes |
| 2 | [Mac Dev Environment](mac-dev-environment.md) §Dotfiles | Dotfiles strategy, symlinks, Git-tracked config |
| 3 | [Mac Dev Environment](mac-dev-environment.md) §Bootstrap | One-command machine setup scripts |
| 4 | [Dev Tools Guide](dev-tools-guide.md) §Pro | Multiple JDK versions, jenv, Docker contexts |

---

## 📂 All Guides In This Module

| Guide | Purpose | Tier |
|---|---|---|
| **[START-HERE.md](START-HERE.md)** ← you are here | Entry point & navigation | All |
| **[homebrew-guide.md](homebrew-guide.md)** | Complete Homebrew reference (install, casks, taps, Brewfile) | All |
| **[jdk-setup.md](jdk-setup.md)** | Open-source JDK installation (Temurin, Corretto, Zulu, GraalVM) | All |
| **[npm-on-mac.md](npm-on-mac.md)** | npm, npx, nvm, global packages, scripts — on macOS | All |
| **[dev-tools-guide.md](dev-tools-guide.md)** | IDEs (IntelliJ/VS Code), Docker Desktop, dev databases | All |
| **[mac-dev-environment.md](mac-dev-environment.md)** | Full environment: shell, aliases, PATH, dotfiles, Brewfile bootstrap | 🟡🔴 |

---

## 💬 Slash Command Quick Access

Use `/mac-dev` in Copilot Chat for an interactive guide on any topic:

```
/mac-dev → homebrew → install → newbie
/mac-dev → npm → nvm → amateur
/mac-dev → jdk → temurin → newbie
/mac-dev → docker → install → amateur
/mac-dev → brewfile → bootstrap → pro
/mac-dev → ide → intellij → amateur
```

Or use the AI navigator:
```
/hub mac       → See the full mac-dev branch in the hub
```

---

**Navigation:** [Homebrew Guide →](homebrew-guide.md) · [JDK Setup →](jdk-setup.md) · [npm on Mac →](npm-on-mac.md) · [Dev Tools →](dev-tools-guide.md) · [Full Environment →](mac-dev-environment.md)
