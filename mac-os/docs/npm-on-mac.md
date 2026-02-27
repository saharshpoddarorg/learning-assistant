# 📦 npm on macOS — Node.js Package Management

> Node.js + npm is essential even if you're primarily a Java/Python developer.  
> Modern dev tools (ESLint, Prettier, TypeScript, Vite, Next.js, the GitHub CLI wrapper, etc.)
> all run on Node. This guide covers installing Node.js, using npm, managing versions with nvm,
> and working productively with the npm ecosystem on macOS.

---

## 📑 Table of Contents

- [Install Node.js on macOS](#-install-nodejs-on-macos)
- [🟢 Newbie — npm Basics](#-newbie--npm-basics)
- [🟡 Amateur — nvm, Global Tools & Scripts](#-amateur--nvm-global-tools--scripts)
- [🔴 Pro — Workspaces, Publishing & Automation](#-pro--workspaces-publishing--automation)
- [Quick Reference Table](#-quick-reference-table)
- [Common Global Packages for Devs](#-common-global-packages-for-devs)
- [Troubleshooting](#-troubleshooting)

---

## ⬇️ Install Node.js on macOS

### Option A — nvm (Recommended)
Manages multiple Node.js versions like `jenv` does for Java.

```zsh
# Install nvm via Homebrew
brew install nvm

# Add to ~/.zshrc
export NVM_DIR="$HOME/.nvm"
[ -s "/opt/homebrew/opt/nvm/nvm.sh" ] && \. "/opt/homebrew/opt/nvm/nvm.sh"
[ -s "/opt/homebrew/opt/nvm/etc/bash_completion.d/nvm" ] && \. "/opt/homebrew/opt/nvm/etc/bash_completion.d/nvm"

# Reload shell
source ~/.zshrc

# Install the latest LTS Node.js
nvm install --lts
nvm use --lts
nvm alias default 'lts/*'

# Verify
node --version     # e.g., v22.x.x
npm --version      # e.g., 10.x.x
```

### Option B — Direct Homebrew (simpler, single version)

```zsh
brew install node            # installs Node.js latest stable
node --version
npm --version
```

---

## 🟢 Newbie — npm Basics

### What is npm?
`npm` = **Node Package Manager**. It:
- Installs JavaScript libraries into your project (`node_modules/`)
- Runs scripts defined in `package.json`
- Publishes packages to the npm registry

### Initialize a new project

```zsh
mkdir my-project && cd my-project
npm init                            # interactive wizard
npm init -y                         # skip questions, use defaults
```

### Install packages

```zsh
npm install lodash                  # add to dependencies
npm install --save-dev jest         # add to devDependencies (test tools, build tools)
npm install -g typescript           # install globally (available everywhere)
npm install                         # install everything in package.json
```

### Run scripts

```zsh
npm run build                       # run the "build" script from package.json
npm run test                        # run tests
npm run start                       # start the application
npm test                            # shorthand (no "run" needed for test/start)
npm start
```

### Remove packages

```zsh
npm uninstall lodash                # remove from local project
npm uninstall -g typescript         # remove global package
```

---

## 🟡 Amateur — nvm, Global Tools & Scripts

### nvm — Managing Multiple Node.js Versions

```zsh
nvm install 22                      # install Node 22
nvm install 20                      # install Node 20 LTS
nvm install --lts                   # install latest LTS
nvm list                            # list all locally installed versions
nvm list-remote                     # list all available versions
nvm use 20                          # switch to Node 20 in current session
nvm use --lts                       # switch to LTS
nvm alias default 22                # set default version for new terminals
nvm current                         # show active version

# Per-project version file
echo "20" > .nvmrc                  # create .nvmrc in project root
nvm use                             # auto-reads .nvmrc and switches
```

### Auto-switch Node version on `cd` (add to `~/.zshrc`)

```zsh
# Auto-switch to .nvmrc when entering a directory
autoload -U add-zsh-hook
load-nvmrc() {
  local nvmrc_path="$(nvm_find_nvmrc)"
  if [ -n "$nvmrc_path" ]; then
    local nvmrc_node_version=$(nvm version "$(cat "${nvmrc_path}")")
    if [ "$nvmrc_node_version" = "N/A" ]; then
      nvm install
    elif [ "$nvmrc_node_version" != "$(nvm version)" ]; then
      nvm use
    fi
  fi
}
add-zsh-hook chpwd load-nvmrc
load-nvmrc
```

### Global Tools Worth Having

```zsh
npm install -g typescript            # TypeScript compiler (tsc)
npm install -g ts-node               # run TypeScript directly
npm install -g nodemon               # auto-restart Node on file changes
npm install -g eslint                # JavaScript/TypeScript linter
npm install -g prettier              # code formatter
npm install -g http-server           # quick static file server
npm install -g @angular/cli          # Angular CLI
npm install -g create-react-app      # React app scaffolding
npm install -g next                  # Next.js CLI
npm install -g vercel                # Vercel deployment CLI
npm install -g serve                 # serve static files
```

### npx — Run Without Installing

```zsh
npx create-react-app my-app          # scaffold React without installing globally
npx tsc --init                       # create tsconfig.json without global tsc
npx prettier --write src/            # run prettier once without installing globally
npx http-server ./dist               # serve a dist folder on-the-fly
```

### Package.json Scripts — Standard Patterns

```json
{
  "scripts": {
    "start":   "node dist/index.js",
    "dev":     "nodemon src/index.ts",
    "build":   "tsc",
    "test":    "jest",
    "lint":    "eslint src/ --ext .ts",
    "format":  "prettier --write 'src/**/*.ts'",
    "clean":   "rm -rf dist/ node_modules/",
    "prepare": "npm run build"
  }
}
```

---

## 🔴 Pro — Workspaces, Publishing & Automation

### npm Workspaces (Monorepo)

```json
// Root package.json
{
  "name": "my-monorepo",
  "workspaces": [
    "packages/*",
    "apps/*"
  ]
}
```

```zsh
# Install deps for all workspaces from root
npm install

# Run a script in a specific workspace
npm run build --workspace=packages/ui
npm -w packages/ui run build

# Add a dep to a specific workspace
npm install lodash --workspace=packages/utils
```

### Lock File Best Practices

```zsh
# Always commit package-lock.json (or yarn.lock / pnpm-lock.yaml)
# Use `npm ci` in CI instead of `npm install` — it's faster and respects lockfile exactly
npm ci                              # clean install — uses lockfile
npm ci --ignore-scripts             # skip postinstall hooks (CI security)
```

### Publishing to npm Registry

```zsh
npm login                           # authenticate
npm version patch                   # bump version (patch = 1.0.0 → 1.0.1)
npm version minor                   # 1.0.0 → 1.1.0
npm version major                   # 1.0.0 → 2.0.0
npm publish                         # publish to registry
npm publish --access public         # for scoped packages (@org/pkg)
npm pack                            # dry run — see what would be published
npm unpublish my-pkg@1.0.0          # unpublish (72h window)
```

### Dependency Audit & Maintenance

```zsh
npm audit                           # check for known vulnerabilities
npm audit fix                       # auto-fix safe updates
npm audit fix --force               # fix even breaking changes (careful!)
npm outdated                        # show which packages have newer versions
npm update                          # update all to compatible versions
npm dedupe                          # deduplicate node_modules (reduce size)
```

### pnpm & yarn (Alternatives)

```zsh
# pnpm — fastest, disk-efficient (symlinks shared across projects)
brew install pnpm
pnpm install
pnpm add lodash
pnpm run build

# yarn (classic v1)
brew install yarn
yarn install
yarn add lodash
yarn run build

# yarn (berry / v4)
corepack enable
corepack prepare yarn@4.x.x --activate
```

---

## 📊 Quick Reference Table

| Goal | npm Command | yarn | pnpm |
|---|---|---|---|
| Install all deps | `npm install` | `yarn` | `pnpm install` |
| Add dep | `npm install <pkg>` | `yarn add <pkg>` | `pnpm add <pkg>` |
| Add dev dep | `npm install -D <pkg>` | `yarn add -D <pkg>` | `pnpm add -D <pkg>` |
| Remove dep | `npm uninstall <pkg>` | `yarn remove <pkg>` | `pnpm remove <pkg>` |
| Run script | `npm run <script>` | `yarn <script>` | `pnpm run <script>` |
| Clean install | `npm ci` | `yarn --frozen-lockfile` | `pnpm install --frozen-lockfile` |
| List deps | `npm ls` | `yarn list` | `pnpm list` |
| Global install | `npm install -g <pkg>` | `yarn global add <pkg>` | `pnpm add -g <pkg>` |
| Run without install | `npx <pkg>` | `yarn dlx <pkg>` | `pnpm dlx <pkg>` |

---

## 🛠️ Common Global Packages for Devs

| Package | Purpose | Install |
|---|---|---|
| `typescript` | TypeScript compiler | `npm i -g typescript` |
| `ts-node` | Run TypeScript directly | `npm i -g ts-node` |
| `nodemon` | Auto-restart on file changes | `npm i -g nodemon` |
| `eslint` | JavaScript/TypeScript linting | `npm i -g eslint` |
| `prettier` | Code formatting | `npm i -g prettier` |
| `http-server` | Quick static file server | `npm i -g http-server` |
| `serve` | Serve static/SPA files | `npm i -g serve` |
| `vercel` | Deploy to Vercel | `npm i -g vercel` |
| `@modelcontextprotocol/inspector` | Debug MCP servers | `npx @modelcontextprotocol/inspector` |

---

## 🔧 Troubleshooting

| Symptom | Fix |
|---|---|
| `npm: command not found` | Node.js not on PATH; use nvm: `nvm use --lts` |
| `EACCES permission denied` | Never use `sudo npm` — use nvm or fix npm prefix: `npm config set prefix ~/.npm-global` |
| `npm install` very slow | Check proxy settings; try `npm install --prefer-offline` |
| `node_modules` corrupted | Delete `node_modules/` and `package-lock.json`, then `npm install` |
| Wrong Node version | `nvm use` or `nvm use <ver>`; check `.nvmrc` |
| `npm ci` fails | Mismatched `package-lock.json`; run `npm install` to regenerate |
| `ENOENT package.json` | Not in a project directory; run `npm init -y` |

---

**Navigation:** [← JDK Setup](jdk-setup.md) · [Dev Tools →](dev-tools-guide.md) · [← START-HERE](START-HERE.md)
