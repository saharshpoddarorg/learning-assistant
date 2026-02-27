# 🛠️ Dev Tools on macOS — IDEs, Docker & Essential Apps

> Install and configure the development tools every Mac developer needs:
> IntelliJ IDEA, VS Code, Docker Desktop, database clients, and more.
> All installations done via Homebrew Cask — no hunting for `.dmg` files.

---

## 📑 Table of Contents

- [🟢 Newbie — Install Core Tools](#-newbie--install-core-tools)
- [🟡 Amateur — Full Dev Toolchain](#-amateur--full-dev-toolchain)
- [🔴 Pro — Configuration & Optimization](#-pro--configuration--optimization)
- [IDEs Deep Dive](#-ides-deep-dive)
- [Docker on macOS](#-docker-on-macos)
- [Database Tools](#-database-tools)
- [API & Network Tools](#-api--network-tools)
- [All Tools Quick Reference](#-all-tools-quick-reference)

---

## 🟢 Newbie — Install Core Tools

### The essentials in one block

```zsh
# Editor & IDE
brew install --cask visual-studio-code     # universal code editor

# JDK (if not already installed)
brew install --cask temurin                # Eclipse Temurin OpenJDK

# Git (upgrade to latest — macOS ships an old version)
brew install git

# Terminal browser for docs
brew install --cask iterm2                 # better than Terminal.app
```

### Verify

```zsh
code --version                             # VS Code
git --version                              # Git
java --version                             # Java
```

---

## 🟡 Amateur — Full Dev Toolchain

### IDEs

```zsh
brew install --cask intellij-idea          # IntelliJ IDEA (Ultimate + Community)
brew install --cask intellij-idea-ce       # Community Edition (free, Java/Kotlin)
brew install --cask webstorm               # WebStorm (JS/TS)
brew install --cask pycharm                # PyCharm (Python)
brew install --cask pycharm-ce             # PyCharm Community (free)
brew install --cask datagrip               # DataGrip (databases)
brew install --cask rider                  # Rider (.NET)
brew install --cask android-studio         # Android Studio
brew install --cask xcode                  # Xcode (macOS/iOS dev) — also from App Store
```

### Containerisation

```zsh
brew install --cask docker                 # Docker Desktop
```

### Terminal & Shell

```zsh
brew install --cask iterm2                 # iTerm2 (most popular)
brew install --cask warp                   # Warp (modern AI-powered terminal)
brew install zsh-autosuggestions           # fish-like autosuggestions
brew install zsh-syntax-highlighting       # command syntax highlighting
brew install starship                      # beautiful cross-shell prompt
```

### API Clients

```zsh
brew install --cask postman               # Postman (API testing)
brew install --cask insomnia              # Insomnia (alternative)
brew install httpie                       # command-line HTTP client (http, https)
brew install grpcurl                      # gRPC CLI client
```

### Database Tools

```zsh
brew install --cask tableplus             # TablePlus (all-in-one DB GUI)
brew install --cask dbngin                # DBngin (local Postgres/MySQL/Redis, zero config)
brew install --cask pgadmin4              # pgAdmin 4 (Postgres-specific)
brew install --cask sequel-ace            # Sequel Ace (MySQL/MariaDB — successor to Sequel Pro)
brew install --cask mongodb-compass       # MongoDB Compass
brew install --cask redis-insight         # Redis Insight (GUI for Redis)
```

---

## 🔴 Pro — Configuration & Optimization

### VS Code — Power Configuration

```zsh
# Install the VS Code shell command (if not auto-installed)
# From VS Code: Cmd+Shift+P → "Shell Command: Install 'code' command in PATH"
code .                                     # open current folder in VS Code
code --list-extensions                     # list installed extensions
code --install-extension <publisher.ext>   # install extension from CLI
code --disable-extensions                  # launch without extensions (debug)
```

**Essential VS Code extensions for Mac devs:**
```zsh
code --install-extension ms-vscode.vscode-typescript-next
code --install-extension dbaeumer.vscode-eslint
code --install-extension esbenp.prettier-vscode
code --install-extension ms-python.python
code --install-extension ms-python.vscode-pylance
code --install-extension vscjava.vscode-java-pack       # Java Extension Pack
code --install-extension redhat.java
code --install-extension ms-azuretools.vscode-docker
code --install-extension GitHub.copilot
code --install-extension GitHub.copilot-chat
code --install-extension eamodio.gitlens
```

### IntelliJ IDEA — Power Shortcuts

| Action | macOS Shortcut |
|---|---|
| Run | `Ctrl+R` |
| Debug | `Ctrl+D` |
| Find everywhere | `Double Shift` |
| Run anything | `Ctrl+Ctrl` (double Ctrl) |
| Refactor → Rename | `Shift+F6` |
| Generate | `Cmd+N` |
| Optimize imports | `Ctrl+Alt+O` |
| Reformat code | `Cmd+Alt+L` |
| Show class hierarchy | `Ctrl+H` |
| Go to declaration | `Cmd+B` |
| Quick fix | `Alt+Enter` |
| Commit | `Cmd+K` |
| Push | `Cmd+Shift+K` |

### JDK in IntelliJ IDEA

1. **File → Project Structure** (`Cmd+;`)
2. **Project → SDK → +** → JDK
3. Navigate to `/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home`
4. Click **OK**

Or in **Preferences → Build, Execution, Deployment → Build Tools → Maven → JDK for Importer**

---

## 🍺 IDEs Deep Dive

### IntelliJ IDEA

**Community vs Ultimate:**

| Feature | Community (Free) | Ultimate (Paid/Trial) |
|---|---|---|
| Java, Kotlin | ✅ | ✅ |
| Gradle/Maven | ✅ | ✅ |
| Spring | ❌ | ✅ |
| JavaScript/TS | ❌ | ✅ |
| Databases | ❌ | ✅ |
| Docker | ❌ | ✅ |
| HTTP Client | ❌ | ✅ |

**Useful IDEA Plugins:**
- `.ignore` — Gitignore + .dockerignore editing
- `Rainbow Brackets` — color-matched brackets
- `String Manipulation` — case conversion, sorting
- `SonarLint` — static analysis
- `Docker` — container management inside IDE
- `GitHub Copilot` — AI coding

### VS Code for Java

Use the **Java Extension Pack** (`vscjava.vscode-java-pack`) which bundles:
- Language Support for Java (Red Hat)
- Debugger for Java
- Test Runner for Java
- Maven for Java
- Project Manager for Java
- IntelliCode

```zsh
code --install-extension vscjava.vscode-java-pack
```

---

## 🐳 Docker on macOS

### Installation

```zsh
brew install --cask docker       # Docker Desktop for Mac (M1/M2/M3/M4 native)
```

After installation, launch **Docker Desktop** from Applications. It installs the `docker` and `docker compose` CLI commands.

### Essential Docker Commands

```zsh
# Verify installation
docker --version
docker compose version

# Pull and run a container
docker run hello-world                             # smoke test
docker run -it ubuntu bash                         # interactive Ubuntu shell

# Images
docker images                                      # list local images
docker pull postgres:16                            # pull an image
docker rmi <image-id>                              # remove an image

# Containers
docker ps                                          # list running containers
docker ps -a                                       # all containers (including stopped)
docker stop <container-id>                         # stop a container
docker rm <container-id>                           # remove a container
docker logs <container-id>                         # view logs
docker exec -it <container-id> bash                # shell into running container

# Cleanup
docker system prune                                # remove stopped containers + dangling images
docker system prune -a                             # remove EVERYTHING not in use
docker volume prune                                # remove unused volumes
```

### Common Dev Services via Docker

```zsh
# PostgreSQL (local dev database)
docker run -d \
  --name postgres-dev \
  -e POSTGRES_PASSWORD=devpass \
  -e POSTGRES_DB=mydb \
  -p 5432:5432 \
  postgres:16

# MySQL
docker run -d \
  --name mysql-dev \
  -e MYSQL_ROOT_PASSWORD=devpass \
  -e MYSQL_DATABASE=mydb \
  -p 3306:3306 \
  mysql:8

# Redis
docker run -d \
  --name redis-dev \
  -p 6379:6379 \
  redis:7

# MongoDB
docker run -d \
  --name mongo-dev \
  -p 27017:27017 \
  mongo:7
```

### Docker Compose (multi-container)

```yaml
# docker-compose.yml
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_PASSWORD: devpass
      POSTGRES_DB: mydb
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data

  redis:
    image: redis:7
    ports:
      - "6379:6379"

volumes:
  postgres-data:
```

```zsh
docker compose up -d             # start all services in background
docker compose down              # stop and remove containers
docker compose down -v           # also remove volumes
docker compose logs -f           # follow logs
docker compose ps                # list services
```

---

## 🗄️ Database Tools

### DBngin — Zero-Config Local Databases

DBngin lets you start/stop Postgres, MySQL, and Redis from a menu bar app — no Docker needed.

```zsh
brew install --cask dbngin
```

Supported engines: PostgreSQL, MySQL, MariaDB, Redis, Memcached.

### TablePlus — Universal Database GUI

```zsh
brew install --cask tableplus       # connects to Postgres, MySQL, SQLite, Redis, MongoDB...
```

### PostgreSQL CLI

```zsh
brew install libpq                  # installs psql without the full server
brew link --force libpq

# Connect to a running instance
psql -h localhost -U postgres -d mydb
```

---

## 🌐 API & Network Tools

```zsh
# HTTPie — human-friendly curl
brew install httpie
http GET https://api.github.com/users/github   # nicer than curl
http POST https://httpbin.org/post name=John

# curl basics (already installed on macOS)
curl https://api.github.com/users/github
curl -X POST -H "Content-Type: application/json" -d '{"key":"val"}' https://httpbin.org/post

# grpcurl — gRPC CLI
brew install grpcurl
grpcurl -plaintext localhost:50051 list

# ngrok — expose localhost to the internet (webhooks, mobile testing)
brew install --cask ngrok
ngrok http 8080
```

---

## 📋 All Tools Quick Reference

| Category | Tool | Brew Command |
|---|---|---|
| Editor | VS Code | `brew install --cask visual-studio-code` |
| Java IDE | IntelliJ IDEA | `brew install --cask intellij-idea` |
| Python IDE | PyCharm CE | `brew install --cask pycharm-ce` |
| JS IDE | WebStorm | `brew install --cask webstorm` |
| DB IDE | DataGrip | `brew install --cask datagrip` |
| Terminal | iTerm2 | `brew install --cask iterm2` |
| Terminal | Warp | `brew install --cask warp` |
| Containers | Docker Desktop | `brew install --cask docker` |
| API Client | Postman | `brew install --cask postman` |
| API CLI | HTTPie | `brew install httpie` |
| DB GUI | TablePlus | `brew install --cask tableplus` |
| DB local | DBngin | `brew install --cask dbngin` |
| Window Mgr | Rectangle | `brew install --cask rectangle` |
| Launcher | Raycast | `brew install --cask raycast` |
| Browser | Chrome | `brew install --cask google-chrome` |
| VPN | Wireguard | `brew install --cask wireguard-tools` |

---

**Navigation:** [← npm on Mac](npm-on-mac.md) · [Full Environment →](mac-dev-environment.md) · [← START-HERE](START-HERE.md)
