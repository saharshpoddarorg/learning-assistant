<#
.SYNOPSIS
    brain -- personal knowledge workspace dispatcher.

.DESCRIPTION
    Manages the ai-brain/ directory: create notes, promote between tiers, save to repo,
    search by frontmatter tags, list contents, and clear scratch.

    Run from any location -- all paths resolve relative to the repo root.

.COMMANDS
    new       Create a new note with frontmatter template
    publish   Publish a file to archive/ with project+date hierarchy, tagging, and git commit
    move      Move a file between tiers (inbox -> notes -> archive)
    search    Search notes by content or frontmatter (tag, project, kind, date)
    list      List notes with frontmatter summary
    clear     Clear files from inbox/
    status    Show tier summary
    help      Show this help

.EXAMPLE
    .\ai-brain\scripts\brain.ps1 new
    .\ai-brain\scripts\brain.ps1 new --tier inbox --project mcp-servers --title "SSE transport notes"
    .\ai-brain\scripts\brain.ps1 publish ai-brain\inbox\draft.md --project mcp-servers
    .\ai-brain\scripts\brain.ps1 search java --tag generics
    .\ai-brain\scripts\brain.ps1 search --project mcp-servers --kind decision
    .\ai-brain\scripts\brain.ps1 list --tier archive --project mcp-servers
    .\ai-brain\scripts\brain.ps1 clear --force
    .\ai-brain\scripts\brain.ps1 status
#>

[CmdletBinding(PositionalBinding = $false)]
param(
    [Parameter(Position = 0)]
    [string]$Command = "help",

    [Parameter(Position = 1)]
    [string]$Arg1 = "",      # file path for save/promote, query for search

    [string]$Tier     = "",  # inbox | notes | archive
    [string]$Project  = "",  # project bucket
    [string]$Title    = "",  # note title for 'new'
    [string]$Kind     = "",  # note | decision | session | resource | snippet | ref
    [string]$Tag      = "",  # tag filter for search/list
    [string]$Date     = "",  # YYYY-MM filter for search/list
    [string]$Status   = "",  # draft | final | archived
    [switch]$Force,          # skip confirmation prompts
    [switch]$Commit,         # auto-commit after save (default: ask)
    [switch]$NoEdit          # don't open editor after creating note
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

# ── Resolve repo root ──────────────────────────────────────────────────────────
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$RepoRoot  = Split-Path -Parent (Split-Path -Parent $ScriptDir)
$BrainRoot    = Join-Path $RepoRoot "ai-brain"

# ── Valid values ───────────────────────────────────────────────────────────────
$ValidKinds   = @("note", "decision", "session", "resource", "snippet", "ref")
$ValidTiers   = @("inbox", "notes", "archive")
$ValidStatus  = @("draft", "final", "archived")

# ── Helpers ────────────────────────────────────────────────────────────────────

function Write-Header([string]$Text) {
    Write-Host ""
    Write-Host $Text -ForegroundColor Cyan
    Write-Host ("-" * $Text.Length) -ForegroundColor DarkGray
}

function Write-Ok([string]$Text)   { Write-Host "  $Text" -ForegroundColor Green }
function Write-Info([string]$Text) { Write-Host "  $Text" -ForegroundColor White }
function Write-Warn([string]$Text) { Write-Host "  $Text" -ForegroundColor Yellow }
function Write-Err([string]$Text)  { Write-Host "ERROR: $Text" -ForegroundColor Red; exit 1 }

function Prompt-Input([string]$Prompt, [string]$Default = "") {
    $display = if ($Default) { "$Prompt [$Default]" } else { $Prompt }
    $value = Read-Host $display
    if ([string]::IsNullOrWhiteSpace($value)) { $Default } else { $value.Trim() }
}

function Prompt-Confirm([string]$Prompt, [bool]$DefaultYes = $true) {
    $hint = if ($DefaultYes) { "[Y/n]" } else { "[y/N]" }
    $answer = Read-Host "$Prompt $hint"
    if ([string]::IsNullOrWhiteSpace($answer)) { return $DefaultYes }
    return $answer -match '^[yY]'
}

function Get-Frontmatter([string]$FilePath) {
    if (-not (Test-Path $FilePath)) { return $null }
    $content = Get-Content $FilePath -Raw -Encoding UTF8
    if ($content -notmatch '(?s)^---\r?\n(.*?)\r?\n---') { return $null }
    $yaml = $Matches[1]
    $fm = [ordered]@{}
    foreach ($line in ($yaml -split '\r?\n')) {
        if ($line -match '^(\w+):\s*(.*)$') {
            $key = $Matches[1]
            $val = $Matches[2].Trim()
            if ($val -match '^\[(.+)\]$') {
                $fm[$key] = @($Matches[1] -split '\s*,\s*' | ForEach-Object { $_.Trim().Trim('"').Trim("'") })
            } else {
                $fm[$key] = $val
            }
        }
    }
    return $fm
}

function Set-Frontmatter([string]$FilePath, [hashtable]$Fm) {
    $content = Get-Content $FilePath -Raw -Encoding UTF8
    # Remove existing frontmatter block
    $body = $content -replace '(?s)^---\r?\n.*?\r?\n---\r?\n?', ''
    # Build new frontmatter
    $lines = @("---")
    foreach ($key in @("date","kind","project","tags","status","source")) {
        if ($Fm.Contains($key)) {
            $val = $Fm[$key]
            if ($val -is [array]) {
                $lines += "${key}: [$(($val | ForEach-Object { $_ }) -join ', ')]"
            } else {
                $lines += "${key}: $val"
            }
        }
    }
    # Any extra keys
    foreach ($key in $Fm.Keys) {
        if ($key -notin @("date","kind","project","tags","status","source")) {
            $lines += "${key}: $($Fm[$key])"
        }
    }
    $lines += "---"
    $newContent = ($lines -join "`n") + "`n" + $body.TrimStart("`r", "`n")
    Set-Content $FilePath -Value $newContent -Encoding UTF8 -NoNewline
}

function Make-Slug([string]$Text) {
    $Text.ToLower() -replace '[^a-z0-9]+', '-' -replace '^-|-$', ''
}

function Get-TierDir([string]$TierName) {
    Join-Path $BrainRoot $TierName
}

function Get-AllNotes([string[]]$Tiers = @("inbox","notes","archive")) {
    $notes = @()
    foreach ($tier in $Tiers) {
        $dir = Get-TierDir $tier
        if (Test-Path $dir) {
            $files = Get-ChildItem $dir -Recurse -Filter "*.md" |
                     Where-Object { $_.Name -ne "README.md" }
            foreach ($f in $files) {
                $notes += [PSCustomObject]@{
                    File    = $f
                    Tier    = $tier
                    RelPath = $f.FullName.Substring($dir.Length + 1)
                    Fm      = (Get-Frontmatter $f.FullName)
                }
            }
        }
    }
    return $notes
}

function Open-Editor([string]$FilePath) {
    # Try code (VS Code), then notepad
    $editor = if (Get-Command code -ErrorAction SilentlyContinue) { "code" }
              elseif ($env:EDITOR) { $env:EDITOR }
              else { "notepad" }
    Start-Process $editor -ArgumentList "`"$FilePath`""
}

# ── Command: help ──────────────────────────────────────────────────────────────

function Invoke-Help {
    Write-Host ""
    Write-Host "brain -- personal knowledge workspace" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "USAGE" -ForegroundColor Yellow
    Write-Host "  .\ai-brain\scripts\brain.ps1 <command> [options]"
    Write-Host ""
    Write-Host "COMMANDS" -ForegroundColor Yellow
    $cmds = @(
        @("new",     "Create a new note with frontmatter template"),
        @("publish", "Publish a file to archive/ with project+date hierarchy, tagging, and git commit"),
        @("move",    "Move a file between tiers  (inbox -> notes -> archive)"),
        @("search",  "Search notes by frontmatter (--tag, --project, --kind, --date) or full-text"),
        @("list",    "List notes with frontmatter summary"),
        @("clear",   "Clear files from inbox/"),
        @("status",  "Show tier summary (file counts and recent files)"),
        @("help",    "Show this help")
    )
    foreach ($c in $cmds) {
        Write-Host ("  {0,-10} {1}" -f $c[0], $c[1])
    }
    Write-Host ""
    Write-Host "OPTIONS (not all apply to every command)" -ForegroundColor Yellow
    $opts = @(
        @("--tier",    "inbox | notes | archive"),
        @("--project", "project bucket name  (e.g. mcp-servers, java, general)"),
        @("--title",   "note title for 'new'"),
        @("--kind",    "note | decision | session | resource | snippet | ref"),
        @("--tag",     "tag filter for search/list"),
        @("--date",    "YYYY-MM filter for search/list"),
        @("--status",  "draft | final | archived"),
        @("--force",   "skip confirmation prompts"),
        @("--commit",  "auto-commit after save without prompting"),
        @("--no-edit", "don't open editor after creating note")
    )
    foreach ($o in $opts) {
        Write-Host ("  {0,-12} {1}" -f $o[0], $o[1])
    }
    Write-Host ""
    Write-Host "EXAMPLES" -ForegroundColor Yellow
    Write-Host "  .\ai-brain\scripts\brain.ps1 new"
    Write-Host "  .\ai-brain\scripts\brain.ps1 new --tier inbox --project mcp-servers --title `"SSE transport`""
    Write-Host "  .\ai-brain\scripts\brain.ps1 publish ai-brain\inbox\draft.md --project mcp-servers"
    Write-Host "  .\ai-brain\scripts\brain.ps1 search java --tag generics --tier archive"
    Write-Host "  .\ai-brain\scripts\brain.ps1 list --tier archive --project mcp-servers"
    Write-Host "  .\ai-brain\scripts\brain.ps1 clear --force"
    Write-Host "  .\ai-brain\scripts\brain.ps1 status"
    Write-Host ""
    Write-Host "ALIASES  (after dot-sourcing brain-module.psm1)" -ForegroundColor Yellow
    Write-Host "  brain, brain-new, brain-publish, brain-move, brain-search, brain-list, brain-clear, brain-status"
    Write-Host ""
}

# ── Command: new ──────────────────────────────────────────────────────────────

function Invoke-New {
    Write-Header "Create new note"

    $tier    = if ($Tier)    { $Tier }    else { Prompt-Input "Tier (inbox/notes/archive)" "inbox" }
    $project = if ($Project) { $Project } else { Prompt-Input "Project" "general" }
    $title   = if ($Title)   { $Title }   else { Prompt-Input "Title" "untitled" }
    $kind    = if ($Kind)    { $Kind }    else { Prompt-Input "Kind ($(($ValidKinds -join '|')))" "note" }
    $tags    = Prompt-Input "Tags (comma-separated, or Enter to skip)" ""

    if ($tier -notin $ValidTiers) { Write-Err "Invalid tier: $tier. Must be one of: $($ValidTiers -join ', ')" }
    if ($kind -notin $ValidKinds) { Write-Warn "Unrecognised kind '$kind' -- using 'note'"; $kind = "note" }

    $today    = (Get-Date).ToString("yyyy-MM-dd")
    $slug     = Make-Slug $title
    $filename = "${today}_${slug}.md"
    $destDir  = Get-TierDir $tier
    $destPath = Join-Path $destDir $filename

    if (Test-Path $destPath) {
        if (-not (Prompt-Confirm "File already exists. Overwrite?" $false)) {
            Write-Warn "Cancelled."
            return
        }
    }

    $tagList = if ($tags) {
        "[$(($tags -split '\s*,\s*' | ForEach-Object { $_.Trim() }) -join ', ')]"
    } else { "[]" }

    $template = @"
---
date: $today
kind: $kind
project: $project
tags: $tagList
status: draft
source: copilot
---

# $title

<!-- Notes here -->
"@

    if (-not (Test-Path $destDir)) { New-Item -ItemType Directory -Path $destDir | Out-Null }
    Set-Content $destPath -Value $template -Encoding UTF8

    Write-Ok "Created: ai-brain/$tier/$filename"

    if (-not $NoEdit) {
        $open = if ($Force) { $true } else { Prompt-Confirm "Open in editor?" $true }
        if ($open) { Open-Editor $destPath }
    }
}

# ── Command: publish ──────────────────────────────────────────────────────────

function Invoke-Publish {
    Write-Header "Save note to repo"

    $sourcePath = $Arg1
    if (-not $sourcePath) {
        $sourcePath = Prompt-Input "Source file (relative to ai-brain/ or absolute)"
    }

    # Resolve path
    if (-not [System.IO.Path]::IsPathRooted($sourcePath)) {
        $tryBrain = Join-Path $BrainRoot $sourcePath
        $tryCwd   = Join-Path (Get-Location) $sourcePath
        if     (Test-Path $tryBrain) { $sourcePath = $tryBrain }
        elseif (Test-Path $tryCwd)   { $sourcePath = $tryCwd }
        else   { Write-Err "File not found: $sourcePath" }
    }
    if (-not (Test-Path $sourcePath)) { Write-Err "File not found: $sourcePath" }

    # Read existing frontmatter
    $fm = Get-Frontmatter $sourcePath
    if (-not $fm) { $fm = [ordered]@{} }

    Write-Host ""
    Write-Host "Current frontmatter:" -ForegroundColor DarkGray
    if ($fm.Count) {
        $fm.GetEnumerator() | ForEach-Object { Write-Host ("  {0}: {1}" -f $_.Key, $_.Value) -ForegroundColor DarkGray }
    } else {
        Write-Host "  (none)" -ForegroundColor DarkGray
    }
    Write-Host ""

    # Project
    $existingProject = if ($fm["project"]) { $fm["project"] } else { "general" }
    $project = if ($Project) { $Project } else { Prompt-Input "Project bucket" $existingProject }

    # Kind
    $existingKind = if ($fm["kind"]) { $fm["kind"] } else { "note" }
    $kind = if ($Kind) { $Kind } else { Prompt-Input "Kind ($(($ValidKinds -join '|')))" $existingKind }
    if ($kind -notin $ValidKinds) { $kind = "note" }

    # Tags -- suggest from existing + filename keywords
    $existingTags = if ($fm["tags"] -is [array]) { $fm["tags"] } else { @() }
    $fileSlug     = [System.IO.Path]::GetFileNameWithoutExtension($sourcePath) -replace '^\d{4}-\d{2}-\d{2}_', ''
    $autoTags     = ($fileSlug -split '[-_]') | Where-Object { $_.Length -gt 2 } | Select-Object -Unique
    $suggestedTags = ($existingTags + $autoTags) | Select-Object -Unique
    $suggestedStr  = $suggestedTags -join ', '

    Write-Host "  Suggested tags: $suggestedStr" -ForegroundColor DarkGray
    $tagInput = Prompt-Input "Tags (comma-separated)" $suggestedStr
    $finalTags = @($tagInput -split '\s*,\s*' | Where-Object { $_ } | ForEach-Object { $_.Trim() } | Select-Object -Unique)

    # Status
    $existingStatus = if ($fm["status"]) { $fm["status"] } else { "final" }
    $statusVal = if ($Status) { $Status } else { Prompt-Input "Status (draft|final|archived)" $existingStatus }
    if ($statusVal -notin $ValidStatus) { $statusVal = "final" }

    # Update frontmatter in file
    $fm["date"]    = if ($fm["date"]) { $fm["date"] } else { (Get-Date).ToString("yyyy-MM-dd") }
    $fm["kind"]    = $kind
    $fm["project"] = $project
    $fm["tags"]    = $finalTags
    $fm["status"]  = $statusVal

    # Destination: archive/<project>/<YYYY-MM>/filename
    $yearMonth = if ($fm["date"] -match '(\d{4}-\d{2})') { $Matches[1] } else { (Get-Date).ToString("yyyy-MM") }
    $filename  = Split-Path $sourcePath -Leaf
    $destDir   = Join-Path $BrainRoot "archive\$project\$yearMonth"
    $destPath  = Join-Path $destDir $filename

    Write-Host ""
    Write-Host "  Destination: brain/archive/$project/$yearMonth/$filename" -ForegroundColor Cyan

    if (-not $Force) {
        if (-not (Prompt-Confirm "Proceed?" $true)) {
            Write-Warn "Cancelled."
            return
        }
    }

    # Write updated frontmatter to source first
    Set-Frontmatter $sourcePath $fm

    # Move to archive/
    if (-not (Test-Path $destDir)) { New-Item -ItemType Directory -Path $destDir -Force | Out-Null }
    if ((Test-Path $destPath) -and ($sourcePath -ne $destPath)) {
        if (-not $Force) {
            if (-not (Prompt-Confirm "Destination already exists. Overwrite?" $false)) {
                Write-Warn "Cancelled."
                return
            }
        }
    }
    Move-Item $sourcePath $destPath -Force
    Write-Ok "Moved: brain/archive/$project/$yearMonth/$filename"

    # Git operations
    $gitRelPath = "ai-brain/archive/$project/$yearMonth/$filename" -replace '\\', '/'

    Push-Location $RepoRoot
    try {
        git add $gitRelPath 2>&1 | Out-Null
        Write-Ok "Staged: $gitRelPath"

        $doCommit = if ($Commit) { $true } else { Prompt-Confirm "Commit now?" $true }
        if ($doCommit) {
            $tagStr      = if ($finalTags) { " [$($finalTags -join ', ')]" } else { "" }
            $commitTitle = $filename -replace '\.md$', '' -replace '^\d{4}-\d{2}-\d{2}_', ''
            $commitMsg   = "brain: publish $commitTitle$tagStr`n`nProject: $project  Kind: $kind  Status: $statusVal`n`n-- created by gpt"
            git commit -m $commitMsg | Out-Null
            Write-Ok "Committed."
        } else {
            Write-Info "Run: git commit -m `"brain: publish $filename`""
        }
    } finally {
        Pop-Location
    }
}

# ── Command: move ────────────────────────────────────────────────────────────────

function Invoke-Move {
    Write-Header "Promote file between tiers"

    $sourcePath = $Arg1
    if (-not $sourcePath) { $sourcePath = Prompt-Input "Source (relative to brain/)" }
    $targetTier = if ($Tier) { $Tier } else { Prompt-Input "Target tier (notes|archive)" "notes" }
    $subDir     = if ($Project) { $Project } else { Prompt-Input "Subdirectory (optional, Enter for none)" "" }

    if (-not [System.IO.Path]::IsPathRooted($sourcePath)) {
        $tryBrain = Join-Path $BrainRoot $sourcePath
        if (Test-Path $tryBrain) { $sourcePath = $tryBrain }
        else { Write-Err "File not found: $sourcePath" }
    }

    $filename = Split-Path $sourcePath -Leaf
    $destDir  = if ($subDir) { Join-Path $BrainRoot "$targetTier\$subDir" } else { Join-Path $BrainRoot $targetTier }
    $destPath = Join-Path $destDir $filename

    if (-not (Test-Path $destDir)) { New-Item -ItemType Directory -Path $destDir -Force | Out-Null }

    if ((Test-Path $destPath) -and -not $Force) {
        if (-not (Prompt-Confirm "Destination exists. Overwrite?" $false)) {
            Write-Warn "Cancelled."
            return
        }
    }

    Move-Item $sourcePath $destPath -Force
    $aiRelSrc  = $sourcePath.Substring($BrainRoot.Length + 1)
    $aiRelDest = $destPath.Substring($BrainRoot.Length + 1)
    Write-Ok "Moved: brain/$aiRelSrc -> brain/$aiRelDest"

    if ($targetTier -eq "archive") {
        Push-Location $RepoRoot
        try {
            $gitPath = "ai-brain/$aiRelDest" -replace '\\', '/'
            git add $gitPath 2>&1 | Out-Null
            Write-Ok "Staged: $gitPath"
            Write-Info "Run 'git commit' when ready."
        } finally {
            Pop-Location
        }
    }
}

# ── Command: search ────────────────────────────────────────────────────────────

function Invoke-Search {
    $query = $Arg1

    $tiers = if ($Tier) { @($Tier) } else { @("inbox","notes","archive") }
    $notes = Get-AllNotes $tiers

    $results = @($notes | Where-Object {
        $fm = $_.Fm
        $match = $true

        if ($Tag -and $fm) {
            $noteTags = if ($fm["tags"]) { $fm["tags"] } else { @() }
            if ($noteTags -is [array]) {
                if (-not ($noteTags -contains $Tag)) { $match = $false }
            } else {
                if ($noteTags -notmatch $Tag) { $match = $false }
            }
        }

        if ($Project -and $fm) {
            if ($fm["project"] -ne $Project) { $match = $false }
        }

        if ($Kind -and $fm) {
            if ($fm["kind"] -ne $Kind) { $match = $false }
        }

        if ($Date -and $fm) {
            if ($fm["date"] -notmatch $Date) { $match = $false }
        }

        if ($Status -and $fm) {
            if ($fm["status"] -ne $Status) { $match = $false }
        }

        if ($query -and $match) {
            $content = Get-Content $_.File.FullName -Raw -ErrorAction SilentlyContinue
            if ($content -notmatch [regex]::Escape($query) -and
                $_.File.Name -notmatch [regex]::Escape($query)) {
                $match = $false
            }
        }

        $match
    })

    Write-Header "Search results"

    $filters = @()
    if ($query)   { $filters += "text:`"$query`"" }
    if ($Tag)     { $filters += "tag:$Tag" }
    if ($Project) { $filters += "project:$Project" }
    if ($Kind)    { $filters += "kind:$Kind" }
    if ($Date)    { $filters += "date:$Date" }
    if ($Status)  { $filters += "status:$Status" }
    if ($filters) { Write-Info "Filters: $($filters -join '  ')" }

    if (-not $results) {
        Write-Warn "No results found."
        return
    }

    foreach ($r in $results) {
        $fm  = $r.Fm
        $tags    = if ($fm -and $fm["tags"] -is [array]) { $fm["tags"] -join ', ' } elseif ($fm -and $fm["tags"]) { $fm["tags"] } else { "" }
        $project = if ($fm -and $fm["project"]) { $fm["project"] } else { "" }
        $kind    = if ($fm -and $fm["kind"])    { $fm["kind"] }    else { "" }
        $date    = if ($fm -and $fm["date"])    { $fm["date"] }    else { "" }

        Write-Host ""
        Write-Host "  [$($r.Tier)] $($r.RelPath)" -ForegroundColor White
        $meta = @($date, $kind, $project, $tags) | Where-Object { $_ }
        if ($meta) {
            Write-Host "         $($meta -join '  |  ')" -ForegroundColor DarkGray
        }
    }
    Write-Host ""
    Write-Info "$($results.Count) result(s)"
}

# ── Command: list ──────────────────────────────────────────────────────────────

function Invoke-List {
    $tiers = if ($Tier) { @($Tier) } else { @("inbox","notes","archive") }
    $notes = Get-AllNotes $tiers

    $filtered = @($notes | Where-Object {
        $fm   = $_.Fm
        $keep = $true
        if ($Project -and ($fm -and $fm["project"] -ne $Project)) { $keep = $false }
        if ($Tag) {
            $noteTags = if ($fm -and $fm["tags"] -is [array]) { $fm["tags"] } else { @() }
            if (-not ($noteTags -contains $Tag)) { $keep = $false }
        }
        if ($Kind -and ($fm -and $fm["kind"] -ne $Kind)) { $keep = $false }
        $keep
    })

    if (-not $filtered) {
        Write-Warn "No notes found."
        return
    }

    $currentTier = ""
    foreach ($r in ($filtered | Sort-Object { $_.Tier }, { $_.RelPath })) {
        if ($r.Tier -ne $currentTier) {
            Write-Header $r.Tier
            $currentTier = $r.Tier
        }
        $fm      = $r.Fm
        $tags    = if ($fm -and $fm["tags"] -is [array]) { $fm["tags"] -join ', ' } else { "" }
        $project = if ($fm -and $fm["project"]) { $fm["project"] } else { "-" }
        $kind    = if ($fm -and $fm["kind"])    { $fm["kind"] }    else { "-" }
        $date    = if ($fm -and $fm["date"])    { $fm["date"] }    else { "-" }
        Write-Host ("  {0,-42} {1,-10} {2,-12} {3}" -f $r.RelPath, $kind, $project, $tags) -ForegroundColor White
    }
    Write-Host ""
    Write-Info "$($filtered.Count) note(s)"
}

# ── Command: clear ─────────────────────────────────────────────────────────────

function Invoke-Clear {
    $scratchDir = Get-TierDir "inbox"
    if (-not (Test-Path $scratchDir)) { Write-Warn "inbox/ does not exist."; return }

    $files = @(Get-ChildItem $scratchDir -Recurse -File | Where-Object { $_.Name -ne "README.md" })

    if ($files.Count -eq 0) { Write-Ok "inbox/ is already empty."; return }

    Write-Header "Clear inbox/"
    $files | ForEach-Object { Write-Info $_.FullName.Substring($scratchDir.Length + 1) }
    Write-Host ""

    $doDelete = if ($Force) { $true } else { Prompt-Confirm "Delete these $($files.Count) file(s)?" $false }
    if ($doDelete) {
        $files | Remove-Item -Force
        Write-Ok "Cleared $($files.Count) file(s) from inbox/."
    } else {
        Write-Warn "Cancelled."
    }
}

# ── Command: status ────────────────────────────────────────────────────────────

function Invoke-Status {
    Write-Header "brain/ workspace status"

    foreach ($tier in @("inbox","notes","archive")) {
        $dir   = Get-TierDir $tier
        $gitStatus = if ($tier -eq "archive") { "[tracked]" } else { "[gitignored]" }
        if (-not (Test-Path $dir)) {
            Write-Info ("  {0,-10} {1}  (does not exist)" -f $tier, $gitStatus)
            continue
        }
        $files = @(Get-ChildItem $dir -Recurse -Filter "*.md" | Where-Object { $_.Name -ne "README.md" })
        Write-Host ("  {0,-10} {1}  {2} note(s)" -f $tier, $gitStatus, $files.Count) -ForegroundColor White
        $recent = $files | Sort-Object LastWriteTime -Descending | Select-Object -First 3
        foreach ($f in $recent) {
            $rel = $f.FullName.Substring($dir.Length + 1)
            Write-Host ("             {0}" -f $rel) -ForegroundColor DarkGray
        }
    }

    Write-Host ""
    Push-Location $RepoRoot
    try {
        $staged = git diff --cached --name-only -- "ai-brain/" 2>&1
        if ($staged) {
            Write-Host "  Staged (ready to commit):" -ForegroundColor Yellow
            $staged | ForEach-Object { Write-Host "    $_" -ForegroundColor Yellow }
        }
    } finally {
        Pop-Location
    }
}

# ── Dispatch ───────────────────────────────────────────────────────────────────

switch ($Command.ToLower()) {
    "new"     { Invoke-New }
    "publish" { Invoke-Publish }
    "move"    { Invoke-Move }
    "search"  { Invoke-Search }
    "list"    { Invoke-List }
    "clear"   { Invoke-Clear }
    "status"  { Invoke-Status }
    "help"    { Invoke-Help }
    default   {
        # Treat unknown command as a search query for convenience
        if ($Command) {
            $Arg1 = $Command
            Invoke-Search
        } else {
            Invoke-Help
        }
    }
}
