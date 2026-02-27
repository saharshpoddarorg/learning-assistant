# 🖥️ macOS Dev Environment — Shell, Aliases, PATH & Dotfiles

> The complete reference for structuring a professional macOS development environment:  
> zsh configuration, shell aliases, PATH management, dotfiles strategy, and bootstrap scripts.

---

## 📑 Table of Contents

- [🟢 Newbie — Shell Basics](#-newbie--shell-basics)
- [🟡 Amateur — Aliases, PATH & Shell Plugins](#-amateur--aliases-path--shell-plugins)
- [🔴 Pro — Dotfiles & Bootstrap Automation](#-pro--dotfiles--bootstrap-automation)
- [Prompt Configuration (Starship / oh-my-zsh)](#-prompt-configuration)
- [Environment Variables Reference](#-environment-variables-reference)
- [Quick Wins Cheatsheet](#-quick-wins-cheatsheet)

---

## 🟢 Newbie — Shell Basics

### macOS default shell

macOS uses **zsh** by default (since Catalina). Your shell config file is `~/.zshrc`.

```zsh
echo $SHELL               # should print /bin/zsh
zsh --version             # check version
```

### Key config files

| File | When it runs | What to put there |
|---|---|---|
| `~/.zshrc` | Every new **interactive** shell | aliases, functions, PATH, plugins |
| `~/.zprofile` | Every **login** shell (Terminal open) | Homebrew path eval, env vars |
| `~/.zshenv` | Every shell (incl. scripts) | Minimal env vars only |

### Edit your shell config

```zsh
code ~/.zshrc                  # open in VS Code
nano ~/.zshrc                  # or use nano
source ~/.zshrc                # reload after editing (no need to restart terminal)
```

### Check your PATH

```zsh
echo $PATH                     # print all PATH entries
which java                     # find where a command lives
which python3
which brew
```

---

## 🟡 Amateur — Aliases, PATH & Shell Plugins

### Useful Aliases (add to `~/.zshrc`)

```zsh
# ── Navigation ──────────────────────────────────────────────
alias ll='ls -lAh'              # long list, all files, human-readable sizes
alias la='ls -A'               # list all including hidden
alias ..='cd ..'
alias ...='cd ../..'
alias ~='cd ~'

# ── Git ─────────────────────────────────────────────────────
alias gs='git status'
alias ga='git add'
alias gc='git commit'
alias gca='git commit --amend'
alias gco='git checkout'
alias gsw='git switch'
alias gb='git branch'
alias glog='git log --oneline --graph --all'
alias gd='git diff'
alias gds='git diff --staged'
alias gp='git push'
alias gpl='git pull'
alias grb='git rebase'
alias gst='git stash'

# ── Java ─────────────────────────────────────────────────────
alias java21='export JAVA_HOME=$(/usr/libexec/java_home -v 21) && java --version'
alias java17='export JAVA_HOME=$(/usr/libexec/java_home -v 17) && java --version'
alias java11='export JAVA_HOME=$(/usr/libexec/java_home -v 11) && java --version'
alias javaVersions='/usr/libexec/java_home -V'

# ── Node / npm ───────────────────────────────────────────────
alias ni='npm install'
alias nid='npm install --save-dev'
alias nr='npm run'
alias ns='npm start'
alias nt='npm test'
alias nb='npm run build'
alias nci='npm ci'

# ── Docker ──────────────────────────────────────────────────
alias dps='docker ps'
alias dpsa='docker ps -a'
alias dstop='docker stop $(docker ps -q)'           # stop all running
alias dprune='docker system prune -f'
alias dcu='docker compose up -d'
alias dcd='docker compose down'
alias dcl='docker compose logs -f'

# ── Brew ─────────────────────────────────────────────────────
alias bup='brew update && brew upgrade && brew cleanup'
alias bls='brew list'
alias bsr='brew search'

# ── Utilities ────────────────────────────────────────────────
alias cl='clear'
alias h='history'
alias path='echo $PATH | tr ":" "\n"'              # print PATH entries one per line
alias ports='lsof -i -P -n | grep LISTEN'          # show listening ports
alias ip='curl -s ifconfig.me'                     # public IP
alias localip='ipconfig getifaddr en0'             # local IP (Wi-Fi)
alias flush='dscacheutil -flushcache && killall -HUP mDNSResponder'  # flush DNS cache
alias perm='ls -lah'                               # show permissions

# ── Open common tools ────────────────────────────────────────
alias idea='open -a "IntelliJ IDEA"'
alias c='code .'
```

### PATH Management

```zsh
# ~/.zprofile — Homebrew (add FIRST — Homebrew installer tells you this)
eval "$(/opt/homebrew/bin/brew shellenv)"

# ~/.zshrc — Append paths (order matters: first entry wins on conflict)

# Homebrew-managed Python
export PATH="/opt/homebrew/opt/python@3.12/bin:$PATH"

# Homebrew-managed Node (if not using nvm)
export PATH="/opt/homebrew/opt/node/bin:$PATH"

# npm global bin
export PATH="$HOME/.npm-global/bin:$PATH"

# Go
export GOPATH="$HOME/go"
export PATH="$GOPATH/bin:$PATH"

# Rust (installed via rustup)
export PATH="$HOME/.cargo/bin:$PATH"

# Local scripts & tools
export PATH="$HOME/.local/bin:$PATH"
export PATH="$HOME/bin:$PATH"
```

### Shell Plugins (via Homebrew)

```zsh
# Install plugins
brew install zsh-autosuggestions           # fish-like grey suggestions as you type
brew install zsh-syntax-highlighting       # commands turn green/red
brew install fzf                           # fuzzy finder (Ctrl+R for history)

# Add to ~/.zshrc
source /opt/homebrew/share/zsh-autosuggestions/zsh-autosuggestions.zsh
source /opt/homebrew/share/zsh-syntax-highlighting/zsh-syntax-highlighting.zsh

# fzf key bindings (run once after install)
$(brew --prefix)/opt/fzf/install
```

### oh-my-zsh (Popular Plugin Framework)

```zsh
# Install oh-my-zsh
sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"

# Popular plugins (add to plugins=() in ~/.zshrc)
plugins=(
  git
  docker
  docker-compose
  npm
  node
  brew
  macos
  zsh-autosuggestions
  zsh-syntax-highlighting
)
```

---

## 🔴 Pro — Dotfiles & Bootstrap Automation

### Dotfiles Strategy

Track your shell config in a Git repository — so you can restore your full environment on a new Mac in minutes.

```
~/dotfiles/
├── .zshrc              → symlinked to ~/.zshrc
├── .zprofile           → symlinked to ~/.zprofile
├── .gitconfig          → symlinked to ~/.gitconfig
├── Brewfile            → brew bundle file
├── scripts/
│   └── bootstrap.sh   → one-command new-Mac setup
└── README.md
```

### Symlink dotfiles to home directory

```zsh
# Create symlinks (idempotent — replace if exists)
ln -sf ~/dotfiles/.zshrc ~/.zshrc
ln -sf ~/dotfiles/.zprofile ~/.zprofile
ln -sf ~/dotfiles/.gitconfig ~/.gitconfig
```

### Bootstrap Script (`~/dotfiles/scripts/bootstrap.sh`)

```bash
#!/usr/bin/env bash
set -euo pipefail

echo "=== 🍺 Installing Homebrew ==="
if ! command -v brew &>/dev/null; then
  /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi
eval "$(/opt/homebrew/bin/brew shellenv)"

echo "=== 📦 Installing all packages from Brewfile ==="
brew bundle --file="$(dirname "$0")/../Brewfile"

echo "=== 🔗 Symlinking dotfiles ==="
DOTFILES="$(dirname "$0")/.."
ln -sf "$DOTFILES/.zshrc" ~/.zshrc
ln -sf "$DOTFILES/.zprofile" ~/.zprofile
ln -sf "$DOTFILES/.gitconfig" ~/.gitconfig
ln -sf "$DOTFILES/Brewfile" ~/Brewfile

echo "=== ⚙️  Setting macOS defaults ==="
# Key repeat speed
defaults write -g KeyRepeat -int 2
defaults write -g InitialKeyRepeat -int 15

# Show hidden files in Finder
defaults write com.apple.finder AppleShowAllFiles YES
killall Finder 2>/dev/null || true

echo "=== ✅ Bootstrap complete! ==="
echo "    Restart your terminal and run: source ~/.zshrc"
```

```zsh
chmod +x ~/dotfiles/scripts/bootstrap.sh
```

### macOS System Preferences via `defaults` (Pro Tips)

```zsh
# Fastest key repeat (great for coders)
defaults write -g KeyRepeat -int 1
defaults write -g InitialKeyRepeat -int 10

# Disable press-and-hold for accented characters (enable key repeat for vim, etc.)
defaults write -g ApplePressAndHoldEnabled -bool false

# Show hidden files in Finder
defaults write com.apple.finder AppleShowAllFiles YES && killall Finder

# Show all file extensions in Finder
defaults write NSGlobalDomain AppleShowAllExtensions -bool true

# Faster Dock auto-hide
defaults write com.apple.dock autohide-delay -float 0
defaults write com.apple.dock autohide-time-modifier -float 0.3 && killall Dock

# Save screenshots to ~/Screenshots
mkdir -p ~/Screenshots
defaults write com.apple.screencapture location ~/Screenshots
```

### gitconfig Template (`~/.gitconfig`)

```ini
[user]
    name  = Your Name
    email = you@example.com

[core]
    editor       = code --wait
    autocrlf     = input
    excludesfile = ~/.gitignore_global

[alias]
    st   = status
    co   = checkout
    sw   = switch
    br   = branch
    lg   = log --oneline --graph --all
    undo = reset --soft HEAD~1
    wip  = commit -am "wip"

[pull]
    rebase = true

[init]
    defaultBranch = main

[push]
    autoSetupRemote = true

[rerere]
    enabled = true

[credential]
    helper = osxkeychain
```

---

## 🎨 Prompt Configuration

### Starship — Cross-Shell Beautiful Prompt

```zsh
# Install
brew install starship

# Add to ~/.zshrc (last line)
eval "$(starship init zsh)"

# Create config
mkdir -p ~/.config
```

**`~/.config/starship.toml` — Developer-optimized config:**
```toml
[character]
success_symbol = "[❯](green)"
error_symbol   = "[❯](red)"

[java]
symbol = " "
style  = "red bold"

[nodejs]
symbol = " "

[python]
symbol = " "

[docker_context]
symbol = " "

[git_branch]
symbol = " "
style  = "bold purple"

[git_status]
conflicted  = "⚡"
ahead       = "⇡${count}"
behind      = "⇣${count}"
modified    = "✎${count}"
staged      = "+${count}"
untracked   = "?${count}"
```

---

## 📋 Environment Variables Reference

```zsh
# Java
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Homebrew (M1/M2/M3/M4 Macs)
export HOMEBREW_PREFIX="/opt/homebrew"
export HOMEBREW_NO_ANALYTICS=1
export HOMEBREW_NO_AUTO_UPDATE=1      # set if you want to control when brew updates

# Go
export GOPATH="$HOME/go"
export GOROOT="$(brew --prefix go)/libexec"

# Rust
export CARGO_HOME="$HOME/.cargo"

# Node / nvm
export NVM_DIR="$HOME/.nvm"

# Editor
export EDITOR="code --wait"         # VS Code as default git editor
export VISUAL="code --wait"

# Locale (prevents issues with some CLI tools)
export LANG="en_US.UTF-8"
export LC_ALL="en_US.UTF-8"
```

---

## ⚡ Quick Wins Cheatsheet

### Daily workflow accelerators

```zsh
# Open current directory in Finder
open .

# Open current directory in VS Code
code .

# Open current directory in IntelliJ
idea .

# Quick HTTP server in current dir (requires Python 3)
python3 -m http.server 8000

# Copy a command's output to clipboard
cat myfile.txt | pbcopy

# Paste from clipboard
pbpaste

# Flush DNS (if sites aren't resolving)
sudo dscacheutil -flushcache && sudo killall -HUP mDNSResponder

# List who is listening on a port
lsof -i :8080

# Kill process on a port
lsof -ti :8080 | xargs kill

# Show large files (top 10)
du -sh * | sort -rh | head -10

# Check disk usage
df -h

# Show CPU / memory usage
top
htop              # (brew install htop)
```

---

**Navigation:** [← Dev Tools](dev-tools-guide.md) · [← START-HERE](START-HERE.md) · [Homebrew Guide →](homebrew-guide.md)
