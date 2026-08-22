param(
    [string]$Source = ".github",
    [string]$Target,
    [switch]$DryRun,
    [switch]$Force,
    [switch]$Verbose
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
$script:VerboseEnabled = $Verbose

. "$PSScriptRoot/lib/Common.ps1"
. "$PSScriptRoot/lib/Transformers.ps1"

$repoRoot = Resolve-RepositoryRoot -StartPath $PSScriptRoot
$spec = Load-JsonFile -Path (Join-Path $PSScriptRoot "config/tool-specs.json")
$rules = Load-JsonFile -Path (Join-Path $PSScriptRoot "config/transformation-rules.json")

$sourceRoot = Join-Path $repoRoot $Source
if (-not (Test-Path $sourceRoot)) {
    throw "Source path does not exist: $Source"
}

$targets = @($spec.targets | Where-Object { -not $_.isSource })
if ($Target) {
    $targets = @($targets | Where-Object { $_.key -eq $Target -or $_.directory -eq $Target })
    if ($targets.Count -eq 0) {
        throw "Unknown target profile: $Target"
    }
}

$sourceFiles = @()
foreach ($map in $rules.pathMappings) {
    $path = Join-Path $sourceRoot $map.from
    if (-not (Test-Path $path)) { continue }
    $sourceFiles += Get-ChildItem -Path $path -Recurse -File
}

$includeExtensions = @()
if ($rules.includeExtensions) {
    $includeExtensions = @($rules.includeExtensions | ForEach-Object { $_.ToLowerInvariant() })
}

$excludePathContains = @()
if ($rules.excludePathContains) {
    $excludePathContains = @($rules.excludePathContains | ForEach-Object { $_.ToLowerInvariant() })
}

$updated = 0
$created = 0
$skipped = 0

foreach ($targetDef in $targets) {
    $targetRoot = Join-Path $repoRoot $targetDef.directory
    if (-not (Test-Path $targetRoot)) {
        if ($DryRun) {
            Write-Log -Level INFO -Message "[dry-run] would create $($targetDef.directory)"
        } else {
            New-Item -ItemType Directory -Path $targetRoot -Force | Out-Null
        }
    }

    foreach ($file in $sourceFiles) {
        $relative = Get-RelativePath -BasePath $sourceRoot -FullPath $file.FullName
        $normalizedRelative = "/" + $relative.ToLowerInvariant()

        $skipByExclude = $false
        foreach ($excludePattern in $excludePathContains) {
            if ($normalizedRelative.Contains($excludePattern)) {
                $skipByExclude = $true
                break
            }
        }
        if ($skipByExclude) {
            continue
        }

        if ($includeExtensions.Count -gt 0) {
            $matchesExtension = $false
            foreach ($ext in $includeExtensions) {
                if ($normalizedRelative.EndsWith($ext)) {
                    $matchesExtension = $true
                    break
                }
            }
            if (-not $matchesExtension) {
                continue
            }
        }

        $sourceContent = Get-Content -Raw -Path $file.FullName
        $converted = Convert-ForProfile -ProfileKey $targetDef.key -Content $sourceContent -RelativePath $relative

        $targetPath = Join-Path $targetRoot $relative
        $targetDir = Split-Path $targetPath -Parent
        if (-not (Test-Path $targetDir)) {
            if (-not $DryRun) {
                New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
            }
        }

        $exists = Test-Path $targetPath
        if ($exists) {
            $current = Get-Content -Raw -Path $targetPath
            if ($current -eq $converted) {
                $skipped++
                continue
            }
        }

        if ($DryRun) {
            Write-Log -Level INFO -Message "[dry-run] would write $($targetDef.directory)/$relative"
            continue
        }

        Set-Content -Path $targetPath -Value $converted -Encoding UTF8
        if ($exists) { $updated++ } else { $created++ }
    }
}

Write-Host "Sync summary: created=$created updated=$updated skipped=$skipped dryRun=$DryRun"
exit 0
