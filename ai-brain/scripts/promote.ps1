<#
.SYNOPSIS
    Move a file between brain/ tiers (inbox, notes, archive).

.DESCRIPTION
    Moves a file from its current tier to a target tier.
    Source path is relative to the brain/ directory.
    If -SubDir is provided, the file lands in brain/<tier>/<subdir>/.
    When moving to archive/, the script offers to run `git add` on the file.

.PARAMETER Source
    Source path relative to the brain/ directory.
    Examples: inbox\draft.md   notes\java\note.md

.PARAMETER Tier
    Destination tier: notes | archive

.PARAMETER SubDir
    Optional subdirectory within the destination tier.
    Example: java  ->  brain/<tier>/java/<filename>

.EXAMPLE
    .\brain\scripts\promote.ps1 inbox\draft.md notes
    Moves brain/inbox/draft.md to brain/notes/draft.md

.EXAMPLE
    .\brain\scripts\promote.ps1 inbox\draft.md notes java
    Moves brain/inbox/draft.md to brain/notes/java/draft.md

.EXAMPLE
    .\brain\scripts\promote.ps1 notes\note.md archive
    Moves brain/notes/note.md to brain/archive/note.md and offers git add.
#>
[CmdletBinding()]
param(
    [Parameter(Mandatory)]
    [string]$Source,

    [Parameter(Mandatory)]
    [ValidateSet("notes", "archive")]
    [string]$Tier,

    [string]$SubDir = ""
)

$repoRoot  = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
$brainRoot = Join-Path $repoRoot "ai-brain"

# Resolve source -- allow bare filename (searched across tiers), relative, or absolute
$sourcePath = $Source
if (-not [System.IO.Path]::IsPathRooted($sourcePath)) {
    $sourcePath = Join-Path $brainRoot $sourcePath
}

if (-not (Test-Path $sourcePath)) {
    Write-Error "Source not found: $sourcePath"
    exit 1
}

$fileName   = Split-Path $sourcePath -Leaf
$destDir    = if ($SubDir) { Join-Path $brainRoot "$Tier\$SubDir" } else { Join-Path $brainRoot $Tier }
$destPath   = Join-Path $destDir $fileName

if (-not (Test-Path $destDir)) {
    New-Item -ItemType Directory -Path $destDir | Out-Null
}

if (Test-Path $destPath) {
    $answer = Read-Host "Destination already exists: $destPath`nOverwrite? [y/N]"
    if ($answer -notmatch '^[yY]') {
        Write-Host "Cancelled." -ForegroundColor Yellow
        exit 0
    }
}

Move-Item -Path $sourcePath -Destination $destPath -Force
Write-Host "Moved: $($sourcePath.Substring($brainRoot.Length + 1)) -> $($destPath.Substring($brainRoot.Length + 1))" -ForegroundColor Green

if ($Tier -eq "archive") {
    Write-Host ""
    $add = Read-Host "Run 'git add' on the file? [Y/n]"
    if ($add -notmatch '^[nN]') {
        $relativePath = "ai-brain/archive/" + ($destPath.Substring((Join-Path $brainRoot "archive").Length + 1) -replace '\\', '/')
        Push-Location $repoRoot
        git add $relativePath
        Pop-Location
        Write-Host "Staged: $relativePath" -ForegroundColor Cyan
        Write-Host "Next: git commit -m `"brain: publish <topic>`""
    }
}
