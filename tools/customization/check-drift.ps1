param(
    [int]$Days = 14,
    [string]$Tool,
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

$sourceDef = @($spec.targets | Where-Object { $_.isSource })[0]
$sourceArtifacts = Get-SourceArtifacts -RepoRoot $repoRoot -SourceDef $sourceDef -Rules $rules

$targets = @($spec.targets | Where-Object { -not $_.isSource })
if ($Tool) {
    $targets = @($targets | Where-Object { $_.key -eq $Tool })
    if ($targets.Count -eq 0) {
        throw "Unknown target profile: $Tool"
    }
}

$threshold = (Get-Date).AddDays(-$Days)
$drifts = @()

foreach ($target in $targets) {
    $targetRoot = Join-Path $repoRoot $target.directory

    foreach ($artifact in $sourceArtifacts) {
        $targetFile = Join-Path $targetRoot $artifact.RelativePath
        if (-not (Test-Path $targetFile)) {
            $drifts += [ordered]@{
                profile = $target.key
                type = "missing"
                file = $artifact.RelativePath
                details = "Target file missing"
            }
            continue
        }

        $srcTime = (Get-Item $artifact.FullPath).LastWriteTime
        $targetTime = (Get-Item $targetFile).LastWriteTime

        if ($targetTime -lt $srcTime -or $targetTime -lt $threshold) {
            $drifts += [ordered]@{
                profile = $target.key
                type = "stale"
                file = $artifact.RelativePath
                sourceModified = $srcTime
                targetModified = $targetTime
                details = "Run sync-customization.ps1"
            }
        }
    }
}

$summary = [ordered]@{
    generatedAt = (Get-Date).ToString("s")
    thresholdDays = $Days
    driftCount = $drifts.Count
    drifts = $drifts
}

Write-Host "Drift summary: $($drifts.Count) issue(s)"

if ($Report) {
    Save-ReportJson -Report $summary -Path $Report
    Write-Host "Report written: $Report"
}

if ($drifts.Count -gt 0) {
    exit 1
}

exit 0
