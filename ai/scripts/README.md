# ai/scripts/ -- Workspace Utility Scripts

All scripts here manage the `ai/` content directory. Use them from the repo root.

---

## Main Dispatcher

Everything goes through one entry point:

```powershell
# Windows PowerShell
.\ai\scripts\ai.ps1 <command> [options]

# Bash (Linux / macOS / Git Bash)
./ai/scripts/ai.sh <command> [options]
```

### Commands

| Command | Description |
|---------|-------------|
| `new`   | Create a new note with frontmatter template |
| `save`  | Promote to `saved/` with project+date hierarchy, tag prompting, git commit |
| `promote` | Move between tiers without the full save workflow |
| `search` | Search by frontmatter or full text |
| `list`  | List notes with frontmatter summary |
| `clear` | Clear files from `scratch/` |
| `status` | Show tier summary (counts, recent files, staged) |
| `help`  | Show full help |

### Options

| Option | Description |
|--------|-------------|
| `--tier scratch\|local\|saved` | Which tier |
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
# Create a new scratch note interactively
.\ai\scripts\ai.ps1 new

# Create in local tier for a specific project, non-interactively
.\ai\scripts\ai.ps1 new --tier local --project mcp-servers --title "SSE transport notes" --kind note

# Search across all tiers
.\ai\scripts\ai.ps1 search java
.\ai\scripts\ai.ps1 search --tag generics --project java
.\ai\scripts\ai.ps1 search --kind decision --tier saved

# Save a note to the repo (interactive: prompts for project, confirms tags, commits)
.\ai\scripts\ai.ps1 save ai\scratch\2026-02-21_draft.md
.\ai\scripts\ai.ps1 save ai\scratch\2026-02-21_draft.md --project mcp-servers --commit

# List notes
.\ai\scripts\ai.ps1 list
.\ai\scripts\ai.ps1 list --tier saved --project mcp-servers

# Status overview
.\ai\scripts\ai.ps1 status

# Clear scratch
.\ai\scripts\ai.ps1 clear           # preview only
.\ai\scripts\ai.ps1 clear --force   # delete without prompt
```

---

## PowerShell Module (short aliases)

Dot-source or import to get `ai`, `ai-new`, `ai-save`, `ai-search`, etc.:

```powershell
# One-time in this terminal session
. .\ai\scripts\ai-module.psm1

# Then use short names
ai-status
ai-new --project mcp-servers --title "Transport decisions"
ai-search --tag generics
ai-save scratch\draft.md --project java
ai-clear
```

**To make aliases permanent**, add to your `$PROFILE`:

```powershell
Import-Module E:\path\to\repo\ai\scripts\ai-module.psm1
```

---

## Bash Aliases

```bash
# One-time in this terminal session
source ./ai/scripts/.ai-aliases.sh

# Then use short names
ai-status
ai-new --project mcp-servers
ai-search --tag generics
ai-save scratch/draft.md --project java
ai-clear --force
```

**To make aliases permanent**, add to `~/.bashrc` or `~/.zshrc`:

```bash
source /path/to/repo/ai/scripts/.ai-aliases.sh
```

---

## VS Code Tasks

Open the Command Palette → **Tasks: Run Task** → choose an `ai:` task:

| Task label | What it does |
|------------|--------------|
| `ai: new note` | Interactive new note in scratch/ |
| `ai: new note (local tier)` | New note in local/ |
| `ai: save note to repo` | Promote to saved/, tag, commit |
| `ai: search notes` | Interactive search |
| `ai: list notes` | List all notes |
| `ai: list saved notes` | List only saved/ notes |
| `ai: status` | Tier summary |
| `ai: clear scratch (preview)` | Show what would be cleared |
| `ai: clear scratch (force)` | Delete scratch without prompt |
| `ai: help` | Show dispatcher help |

---

## Copilot Slash Commands

Use from the Copilot Chat panel (`@workspace` or agent mode):

| Prompt file | Slash command | What it does |
|-------------|---------------|--------------|
| `ai-new.prompt.md` | `/ai-new` | Create a new note with Copilot writing the content |
| `ai-save.prompt.md` | `/ai-save` | Promote a file: tag enrichment + git commit |
| `ai-search.prompt.md` | `/ai-search` | AI-assisted search with natural language |

---

## Script Files

| File | Purpose |
|------|---------|
| `ai.ps1` | Main dispatcher (PowerShell) |
| `ai.sh` | Main dispatcher (Bash) |
| `ai-module.psm1` | PowerShell module — imports alias functions |
| `.ai-aliases.sh` | Bash aliases — source to get `ai-*` functions |
| `clear-scratch.ps1` | Standalone scratch clearer (PowerShell) |
| `clear-scratch.sh` | Standalone scratch clearer (Bash) |
| `promote.ps1` | Standalone tier promoter (PowerShell) |
| `promote.sh` | Standalone tier promoter (Bash) |

The standalone scripts (`clear-scratch.*`, `promote.*`) are kept for backward compatibility.
Prefer the `ai.ps1` / `ai.sh` dispatcher for all new usage.
