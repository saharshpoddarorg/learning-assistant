# ai-brain/scripts/ -- Workspace Utility Scripts

All scripts here manage the `ai-brain/` content directory. Use them from the repo root.

---

## Main Dispatcher

Everything goes through one entry point:

```powershell
# Windows PowerShell
.\ai-brain\scripts\brain.ps1 <command> [options]

# Bash (Linux / macOS / Git Bash)
./ai-brain/scripts/brain.sh <command> [options]
```

### Commands

| Command | Description |
|---------|-------------|
| `new`   | Create a new note with frontmatter template |
| `publish` | Promote to `archive/` with project+date hierarchy, tag prompting, git commit |
| `move`    | Move between tiers without the full publish workflow |
| `search` | Search by frontmatter or full text |
| `list`  | List notes with frontmatter summary |
| `clear` | Clear files from `inbox/` |
| `status` | Show tier summary (counts, recent files, staged) |
| `help`  | Show full help |

### Options

| Option | Description |
|--------|-------------|
| `--tier inbox\|notes\|archive` | Which tier |
| `--project <name>` | Project bucket (e.g. `mcp-servers`, `java`) |
| `--title <title>` | Note title (for `new`) |
| `--kind <kind>` | `note\|decision\|session\|resource\|snippet\|ref` |
| `--tag <tag>` | Tag filter (for `search`/`list`) |
| `--date <YYYY-MM>` | Date filter (for `search`/`list`) |
| `--status <status>` | `draft\|final\|archived` |
| `--force` | Skip confirmation prompts |
| `--commit` | Auto-commit after save |
| `--no-edit` | Don't open editor after `new` |

---

## Quick Examples

```powershell
# Create a new inbox note interactively
.\ai-brain\scripts\brain.ps1 new

# Create in notes tier for a specific project, non-interactively
.\ai-brain\scripts\brain.ps1 new --tier inbox --project mcp-servers --title "SSE transport notes" --kind note

# Search across all tiers
.\ai-brain\scripts\brain.ps1 search java
.\ai-brain\scripts\brain.ps1 search --tag generics --project java
.\ai-brain\scripts\brain.ps1 search --kind decision --tier archive

# Publish a note to the repo (interactive: prompts for project, confirms tags, commits)
.\ai-brain\scripts\brain.ps1 publish ai-brain\inbox\2026-02-21_draft.md
.\ai-brain\scripts\brain.ps1 publish ai-brain\inbox\2026-02-21_draft.md --project mcp-servers --commit

# Move between tiers
.\ai-brain\scripts\brain.ps1 move ai-brain\inbox\draft.md --tier notes

# List notes
.\ai-brain\scripts\brain.ps1 list
.\ai-brain\scripts\brain.ps1 list --tier archive --project mcp-servers

# Status overview
.\ai-brain\scripts\brain.ps1 status

# Clear inbox
.\ai-brain\scripts\brain.ps1 clear           # preview only
.\ai-brain\scripts\brain.ps1 clear --force   # delete without prompt
```

---

## PowerShell Module (short aliases)

Dot-source or import to get `brain`, `brain-new`, `brain-publish`, `brain-move`, `brain-search`, etc.:

```powershell
# One-time in this terminal session
. .\ai-brain\scripts\brain-module.psm1

# Then use short names
brain status
brain-new --project mcp-servers --title "Transport decisions"
brain-search --tag generics
brain-publish ai-brain\inbox\draft.md --project java
brain-move ai-brain\inbox\draft.md --tier notes
brain-clear
```

**To make aliases permanent**, add to your `$PROFILE`:

```powershell
Import-Module E:\path\to\repo\ai-brain\scripts\brain-module.psm1
```

---

## Bash Aliases

```bash
# One-time in this terminal session
source ./ai-brain/scripts/.brain-aliases.sh

# Then use short names
brain status
brain-new --project mcp-servers
brain-search --tag generics
brain-publish ai-brain/inbox/draft.md --project java
brain-move ai-brain/inbox/draft.md --tier notes
brain-clear --force
```

**To make aliases permanent**, add to `~/.bashrc` or `~/.zshrc`:

```bash
source /path/to/repo/ai-brain/scripts/.brain-aliases.sh
```

---

## VS Code Tasks

Open the Command Palette → **Tasks: Run Task** → choose a `brain:` task:

| Task label | What it does |
|------------|--------------|
| `brain: new note` | Interactive new note in inbox/ |
| `brain: new note (notes tier)` | New note in notes/ |
| `brain: publish note` | Promote to archive/, tag, commit |
| `brain: search notes` | Interactive search |
| `brain: list notes` | List all notes |
| `brain: list archive` | List only archive/ notes |
| `brain: status` | Tier summary |
| `brain: clear inbox (preview)` | Show what would be cleared |
| `brain: clear inbox (force)` | Delete inbox without prompt |
| `brain: help` | Show dispatcher help |

---

## Copilot Slash Commands

Use from the Copilot Chat panel (`@workspace` or agent mode):

| Prompt file | Slash command | What it does |
|-------------|---------------|--------------|
| `brain-new.prompt.md` | `/brain-new` | Create a new note with Copilot writing the content |
| `brain-publish.prompt.md` | `/brain-publish` | Promote a file: tag enrichment + git commit |
| `brain-search.prompt.md` | `/brain-search` | AI-assisted search with natural language |

---

## Script Files

| File | Purpose |
|------|---------|
| `brain.ps1` | Main dispatcher (PowerShell) |
| `brain.sh` | Main dispatcher (Bash) |
| `brain-module.psm1` | PowerShell module — imports alias functions |
| `.brain-aliases.sh` | Bash aliases — source to get `brain-*` functions |
| `clear-inbox.ps1` | Standalone scratch clearer (PowerShell) |
| `clear-inbox.sh` | Standalone scratch clearer (Bash) |
| `promote.ps1` | Standalone tier promoter (PowerShell) |
| `promote.sh` | Standalone tier promoter (Bash) |

The standalone scripts (`clear-inbox.*`, `promote.*`) are kept for backward compatibility.
Prefer the `brain.ps1` / `brain.sh` dispatcher for all new usage.
