<#
.SYNOPSIS
    brain-module.psm1 -- PowerShell module providing brain/ workspace functions.

.DESCRIPTION
    Dot-source or import this module from your PowerShell profile to get
    short alias functions for the brain dispatcher.

    To load automatically, add to your $PROFILE:
        Import-Module E:\path\to\repo\brain\ai-brain\scripts\brain-module.psm1

    Or to load only for this repo session (in a shell init script):
        . .\brain\ai-brain\scripts\brain-module.psm1

.EXPORTED FUNCTIONS
    brain         Full dispatcher (same as brain.ps1 directly)
    brain-new        Create a new note
    brain-publish       Save/promote a note to archive/ with tagging and git commit
    brain-search     Search notes
    brain-list       List notes
    brain-clear      Clear inbox/
    brain-status     Show tier summary
    brain-move    Move a file between tiers
#>

$script:BrainScript = Join-Path $PSScriptRoot "brain.ps1"

function Invoke-Brain {
    <#
    .SYNOPSIS Full brain dispatcher. Usage: brain <command> [options]
    .EXAMPLE brain new --project mcp-servers --title "SSE notes"
    .EXAMPLE brain search java --tag generics
    .EXAMPLE brain status
    #>
    & $script:BrainScript @args
}

function Invoke-BrainNew {
    <#
    .SYNOPSIS Create a new brain note.
    .EXAMPLE brain-new
    .EXAMPLE brain-new --tier inbox --project mcp-servers --title "Transport decisions"
    #>
    & $script:BrainScript new @args
}

function Invoke-BrainPublish {
    <#
    .SYNOPSIS Promote a note to archive/ with tagging and git commit.
    .EXAMPLE brain-publish
    .EXAMPLE brain-publish brain\inbox\2026-02-21_draft.md --project java --commit
    #>
    & $script:BrainScript publish @args
}

function Invoke-BrainSearch {
    <#
    .SYNOPSIS Search notes by frontmatter or full text.
    .EXAMPLE brain-search java
    .EXAMPLE brain-search --tag generics --project mcp-servers --tier archive
    .EXAMPLE brain-search --kind decision
    #>
    & $script:BrainScript search @args
}

function Invoke-BrainList {
    <#
    .SYNOPSIS List notes with frontmatter summary.
    .EXAMPLE brain-list
    .EXAMPLE brain-list --tier archive --project mcp-servers
    .EXAMPLE brain-list --kind decision
    #>
    & $script:BrainScript list @args
}

function Invoke-BrainClear {
    <#
    .SYNOPSIS Clear files from inbox/.
    .EXAMPLE brain-clear             # preview
    .EXAMPLE brain-clear --force     # clear immediately
    #>
    & $script:BrainScript clear @args
}

function Invoke-BrainStatus {
    <#
    .SYNOPSIS Show tier summary (file counts, recent files, staged files).
    .EXAMPLE brain-status
    #>
    & $script:BrainScript status @args
}

function Invoke-BrainMove {
    <#
    .SYNOPSIS Move a file between tiers.
    .EXAMPLE brain-move inbox\draft.md --tier notes
    .EXAMPLE brain-move notes\note.md --tier archive --project mcp-servers
    #>
    & $script:BrainScript move @args
}

# ── Aliases ────────────────────────────────────────────────────────────────────
Set-Alias -Name brain       -Value Invoke-Brain        -Scope Global -Force
Set-Alias -Name brain-new     -Value Invoke-BrainNew     -Scope Global -Force
Set-Alias -Name brain-publish    -Value Invoke-BrainPublish    -Scope Global -Force
Set-Alias -Name brain-search  -Value Invoke-BrainSearch  -Scope Global -Force
Set-Alias -Name brain-list    -Value Invoke-BrainList    -Scope Global -Force
Set-Alias -Name brain-clear   -Value Invoke-BrainClear   -Scope Global -Force
Set-Alias -Name brain-status  -Value Invoke-BrainStatus  -Scope Global -Force
Set-Alias -Name brain-move -Value Invoke-BrainMove -Scope Global -Force

Export-ModuleMember -Function Invoke-Brain, Invoke-BrainNew, Invoke-BrainPublish, Invoke-BrainSearch,
                               Invoke-BrainList, Invoke-BrainClear, Invoke-BrainStatus, Invoke-BrainMove
Export-ModuleMember -Alias    brain, brain-new, brain-publish, brain-search, brain-list, brain-clear, brain-status, brain-move

Write-Host "brain-module loaded. Commands: brain, brain-new, brain-publish, brain-move, brain-search, brain-list, brain-clear, brain-status" -ForegroundColor DarkGray
