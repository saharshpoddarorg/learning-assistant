param(
    [switch]$All,
    [string]$Tool,
    [switch]$Deep,
    [switch]$Strict,
    [string]$Report,
    [switch]$Verbose
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
$script:VerboseEnabled = $Verbose

. "$PSScriptRoot/lib/Common.ps1"
. "$PSScriptRoot/lib/Validators.ps1"

$repoRoot = Resolve-RepositoryRoot -StartPath $PSScriptRoot
$spec = Load-JsonFile -Path (Join-Path $PSScriptRoot "config/tool-specs.json")
$rules = Load-JsonFile -Path (Join-Path $PSScriptRoot "config/transformation-rules.json")

$targetDefs = @($spec.targets)
if ($Tool) {
    $targetDefs = @($targetDefs | Where-Object { $_.key -eq $Tool })
    if ($targetDefs.Count -eq 0) {
        throw "Unknown tool profile: $Tool"
    }
} elseif (-not $All) {
    throw "Specify -All or -Tool <profile>."
}

$sourceDef = @($spec.targets | Where-Object { $_.isSource })[0]
$sourceArtifacts = Get-SourceArtifacts -RepoRoot $repoRoot -SourceDef $sourceDef -Rules $rules

$errorCount = 0
$warningCount = 0
$profileReports = @()

foreach ($targetDef in $targetDefs) {
    Write-Log -Level INFO -Message "Validating profile: $($targetDef.key)"

    $issues = @()
    $issues += Test-TargetStructure -RepoRoot $repoRoot -TargetDef $targetDef

    $targetRootPath = Join-Path $repoRoot $targetDef.directory
    if ($Deep -and -not $targetDef.isSource -and (Test-Path $targetRootPath)) {
        $issues += Compare-TargetParity -RepoRoot $repoRoot -TargetDef $targetDef -SourceArtifacts $sourceArtifacts -HashFn ${function:Get-FileHashSafe}
    }

    foreach ($issue in $issues) {
        if ($issue.level -eq "error") { $errorCount++ }
        if ($issue.level -eq "warn") { $warningCount++ }
    }

    $profileReports += [ordered]@{
        profile = $targetDef.key
        issueCount = $issues.Count
        issues = $issues
    }
}

$summary = [ordered]@{
    generatedAt = (Get-Date).ToString("s")
    deep = [bool]$Deep
    strict = [bool]$Strict
    errors = $errorCount
    warnings = $warningCount
    profiles = $profileReports
}

Write-Host "Validation summary: errors=$errorCount warnings=$warningCount"

if ($Report) {
    Save-ReportJson -Report $summary -Path $Report
    Write-Host "Report written: $Report"
}

if ($errorCount -gt 0) {
    exit 1
}

if ($Strict -and $warningCount -gt 0) {
    exit 2
}

exit 0
