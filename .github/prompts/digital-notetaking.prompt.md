---
name: digital-notetaking
description: >
  Learn digital note-taking systems (PARA, CODE, Zettelkasten), compare and migrate
  between tools (Notion, Obsidian, Logseq, OneNote), set up developer-specific PKM
  workflows, manage todos, and upgrade JDK versions cross-platform.
agent: Learning-Mentor
tools:
  - codebase
  - fetch
---

# /digital-notetaking — Digital Note-Taking & PKM Assistant

## Input Variables
- `${input:topic}`: What you want to learn or do
  (e.g., `set up Obsidian`, `PARA method`, `migrate Notion to Obsidian`, `upgrade to JDK 25`)
- `${input:tool}`: Your current or target tool
  (e.g., `notion`, `obsidian`, `logseq`, `onenote`, `google-docs`, `any`)
- `${input:level}`: Your familiarity level
  (e.g., `newbie`, `intermediate`, `advanced`)
- `${input:os}`: Your operating system
  (e.g., `windows`, `macos`, `linux`, `cross-platform`)

---

## Domain Map

```
Digital Note-Taking & PKM
├── Methodologies (tool-agnostic)
│   ├── CODE  → Capture, Organize, Distill, Express (Tiago Forte)
│   ├── PARA  → Projects, Areas, Resources, Archives (Tiago Forte)
│   ├── Zettelkasten → atomic notes + links (Luhmann)
│   └── GTD   → Getting Things Done + PARA actions
├── Tools
│   ├── Cloud-first   → Notion, Google Docs / Keep, OneNote (Microsoft 365)
│   ├── Local-first   → Obsidian, Logseq (open-source), Roam Research
│   └── Task-focused  → Todoist, Notion databases, Linear
├── Developer-Specific Workflows
│   ├── Architecture Decision Records (ADRs)
│   ├── Learning journals (daily/sprint logs)
│   ├── Code snippet vaults
│   └── Reading + research notes
├── Migration & Tool Switching
│   ├── Notion → Obsidian
│   ├── Logseq → Obsidian
│   ├── OneNote → Notion
│   └── Any → Markdown export / Pandoc
└── JDK Version Management (cross-platform)
    ├── SDKMAN! (Linux / macOS / WSL)
    ├── Eclipse Temurin / Adoptium (Windows installer)
    ├── JDK 25 (LTS, Sep 2025) migration from JDK 21
    └── .sdkmanrc for per-project JDK pinning
```

---

## Tier 1 — Newbie (I'm just starting)

When `level` = `newbie`, respond with:

1. **One-sentence answer** to the immediate question
2. **Recommended tool** based on their OS and goals
3. **5-step quick-start** (copy-pasteable setup commands or UI steps)
4. **PARA folder structure** to create immediately

```
Example: User asks "I want to start taking notes as a developer"

→ Recommend: Obsidian (Windows/macOS/Linux) or Notion (iOS/Android priority)
→ Quick-start:
  1. Download obsidian.md (free)
  2. Create vault → pick a synced folder
  3. Create folders: Inbox, Projects, Areas, Resources, Archives
  4. Install Dataview plugin
  5. Write your first daily note today
```

---

## Tier 2 — Amateur (I have a system, want to improve)

When `level` = `intermediate`, respond with:

1. **Diagnosis** — what's likely missing in their current setup
2. **Methodology deep-dive** (CODE / PARA / Zettelkasten)
3. **Template** for their specific use case (ADR, sprint log, snippet vault)
4. **Integration tips** — how to connect their tool with their dev workflow

```
Example: User asks "How do I apply PARA to my Obsidian vault?"

→ Explain PARA in developer context
→ Show folder structure:
   vault/
   ├── Inbox/           ← daily capture
   ├── Projects/        ← one folder per active codebase / goal
   ├── Areas/           ← Java/, Architecture/, Career/
   ├── Resources/       ← book summaries, docs, articles
   └── Archives/        ← completed projects, old roles
→ Show Dataview query to list all Project notes
```

---

## Tier 3 — Pro (I want advanced automation & migration)

When `level` = `advanced`, respond with:

1. **Tool migration guide** (step-by-step, with commands)
2. **Automation** — Dataview queries, Templater templates, SDKMAN! setup
3. **Cross-tool integration** — Notion ↔ GitHub, Obsidian Git sync, Todoist API
4. **JDK version management** — SDKMAN!, .sdkmanrc, upgrade from JDK 21 to 25

```
Example: User asks "How do I migrate from Notion to Obsidian?"

→ Export from Notion (Settings → Export as Markdown & CSV)
→ Use Obsidian's built-in Notion importer plugin
→ Set up Git plugin for automatic vault commits
→ Rebuild databases as Dataview tables (TABLE + FROM)
```

---

## Quick Reference — Tool Comparison

| Feature | Notion | Obsidian | Logseq | OneNote |
|---|---|---|---|---|
| Storage | Cloud | Local | Local/Cloud | Cloud (OneDrive) |
| Offline | Limited | ✅ Full | ✅ Full | Limited |
| Price | Free/paid | Free personal | Free + open-source | Free w/ Microsoft |
| Windows | ✅ | ✅ | ✅ | ✅ Best |
| macOS | ✅ | ✅ | ✅ | ✅ |
| iOS/Android | ✅ Best | ✅ | ✅ | ✅ |
| Format | Proprietary | Markdown | Markdown/Org | Proprietary |
| Dev-friendly | Medium | High | High | Low |
| Migration | Export MD | Open spec | Open spec | Export to Word |

---

## OS-Specific Notes

**Windows:** Use PowerShell for JDK management. Download Temurin from adoptium.net.
Use `%APPDATA%\Obsidian` for vault storage, or OneDrive for sync.

**macOS:** Use Homebrew (`brew install --cask obsidian`) or direct download.
Use SDKMAN! for JDK (`curl -s "https://get.sdkman.io" | bash`).
iCloud Drive recommended for Obsidian sync.

**Linux:** SDKMAN! is the primary JDK manager. Use Logseq's AppImage or .deb package.
Obsidian available as AppImage, snap, or flatpak.

**Cross-platform:** Notion web app works everywhere. Obsidian Sync (paid) works
across all platforms. Logseq + GitHub repo is a free, developer-friendly alternative.

---

## JDK Upgrade Quick Reference

```bash
# Check current Java version
java --version

# Install SDKMAN! (Linux / macOS / WSL)
curl -s "https://get.sdkman.io" | bash && source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install JDK 25 (Eclipse Temurin LTS)
sdk install java 25-tem

# Pin this project to JDK 25
echo "java=25-tem" > .sdkmanrc

# Apply pinned version in current shell
sdk env

# Verify
java --version    # should show 25.x.x
```

---

## Curated Vault Resources

| Resource | Category | Tags |
|---|---|---|
| Building a Second Brain (BASB) | `building-a-second-brain` | pkm, tiago-forte, second-brain |
| PARA Method | `para-method` | para, folder-structure, organization |
| CODE Method | `code-method-four-levels-pkm` | code-method, distill, express |
| Zettelkasten Introduction | `zettelkasten-introduction` | zettelkasten, permanent-notes |
| Obsidian Documentation | `obsidian-official` | obsidian, markdown, local-first |
| Notion Help Center | `notion-help-center` | notion, cloud-based, databases |
| Logseq Documentation | `logseq-official` | logseq, open-source, outliner |
| PKM for Software Engineers | `pkm-for-software-engineers` | developer, adr, learning-journal |
| Notion → Obsidian Migration | `notion-to-obsidian-migration` | migration, import, tool-switch |
| SDKMAN! | `sdkman-jdk-manager` | jdk, version-manager, sdkman |
| Eclipse Temurin (Adoptium) | `eclipse-temurin` | open-source-jdk, windows, macos |
| JDK 25 Release Notes | `jdk25-release-notes` | jdk25, upgrade, lts |
| JDK 25 Migration Guide | `jdk25-migration-guide` | migration, upgrade, jdk25 |
