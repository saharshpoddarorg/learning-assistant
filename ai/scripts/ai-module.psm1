<#
.SYNOPSIS
    ai-module.psm1 -- PowerShell module providing ai/ workspace functions.

.DESCRIPTION
    Dot-source or import this module from your PowerShell profile to get
    short alias functions for the ai dispatcher.

    To load automatically, add to your $PROFILE:
        Import-Module E:\path\to\repo\ai\scripts\ai-module.psm1

    Or to load only for this repo session (in a shell init script):
        . .\ai\scripts\ai-module.psm1

.EXPORTED FUNCTIONS
    ai            Full dispatcher (same as ai.ps1 directly)
    ai-new        Create a new note
    ai-save       Save/promote a note to saved/ with tagging and git commit
    ai-search     Search notes
    ai-list       List notes
    ai-clear      Clear scratch/
    ai-status     Show tier summary
    ai-promote    Move a file between tiers
#>

$script:AiScript = Join-Path $PSScriptRoot "ai.ps1"

function Invoke-Ai {
    <#
    .SYNOPSIS Full ai dispatcher. Usage: ai <command> [options]
    .EXAMPLE ai new --project mcp-servers --title "SSE notes"
    .EXAMPLE ai search java --tag generics
    .EXAMPLE ai status
    #>
    & $script:AiScript @args
}

function Invoke-AiNew {
    <#
    .SYNOPSIS Create a new AI note.
    .EXAMPLE ai-new
    .EXAMPLE ai-new --tier local --project mcp-servers --title "Transport decisions"
    #>
    & $script:AiScript new @args
}

function Invoke-AiSave {
    <#
    .SYNOPSIS Promote a note to saved/ with tagging and git commit.
    .EXAMPLE ai-save
    .EXAMPLE ai-save ai\scratch\2026-02-21_draft.md --project java --commit
    #>
    & $script:AiScript save @args
}

function Invoke-AiSearch {
    <#
    .SYNOPSIS Search notes by frontmatter or full text.
    .EXAMPLE ai-search java
    .EXAMPLE ai-search --tag generics --project mcp-servers --tier saved
    .EXAMPLE ai-search --kind decision
    #>
    & $script:AiScript search @args
}

function Invoke-AiList {
    <#
    .SYNOPSIS List notes with frontmatter summary.
    .EXAMPLE ai-list
    .EXAMPLE ai-list --tier saved --project mcp-servers
    .EXAMPLE ai-list --kind decision
    #>
    & $script:AiScript list @args
}

function Invoke-AiClear {
    <#
    .SYNOPSIS Clear files from scratch/.
    .EXAMPLE ai-clear             # preview
    .EXAMPLE ai-clear --force     # clear immediately
    #>
    & $script:AiScript clear @args
}

function Invoke-AiStatus {
    <#
    .SYNOPSIS Show tier summary (file counts, recent files, staged files).
    .EXAMPLE ai-status
    #>
    & $script:AiScript status @args
}

function Invoke-AiPromote {
    <#
    .SYNOPSIS Move a file between tiers.
    .EXAMPLE ai-promote scratch\draft.md --tier local
    .EXAMPLE ai-promote local\note.md --tier saved --project mcp-servers
    #>
    & $script:AiScript promote @args
}

# ── Aliases ────────────────────────────────────────────────────────────────────
Set-Alias -Name ai         -Value Invoke-Ai        -Scope Global -Force
Set-Alias -Name ai-new     -Value Invoke-AiNew     -Scope Global -Force
Set-Alias -Name ai-save    -Value Invoke-AiSave    -Scope Global -Force
Set-Alias -Name ai-search  -Value Invoke-AiSearch  -Scope Global -Force
Set-Alias -Name ai-list    -Value Invoke-AiList    -Scope Global -Force
Set-Alias -Name ai-clear   -Value Invoke-AiClear   -Scope Global -Force
Set-Alias -Name ai-status  -Value Invoke-AiStatus  -Scope Global -Force
Set-Alias -Name ai-promote -Value Invoke-AiPromote -Scope Global -Force

Export-ModuleMember -Function Invoke-Ai, Invoke-AiNew, Invoke-AiSave, Invoke-AiSearch,
                               Invoke-AiList, Invoke-AiClear, Invoke-AiStatus, Invoke-AiPromote
Export-ModuleMember -Alias    ai, ai-new, ai-save, ai-search, ai-list, ai-clear, ai-status, ai-promote

Write-Host "ai-module loaded. Commands: ai, ai-new, ai-save, ai-search, ai-list, ai-clear, ai-status, ai-promote" -ForegroundColor DarkGray
