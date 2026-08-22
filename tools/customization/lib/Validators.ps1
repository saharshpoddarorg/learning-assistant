Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Test-TargetStructure {
    param(
        [string]$RepoRoot,
        $TargetDef
    )

    $issues = @()
    $targetRootPath = Join-Path $RepoRoot $TargetDef.directory

    if (-not (Test-Path $targetRootPath)) {
        $level = "warn"
        if ($TargetDef.isSource) {
            $level = "error"
        }
        $issues += @{ level = $level; message = "Missing profile root: $($TargetDef.directory)" }
        return $issues
    }

    foreach ($dir in $TargetDef.artifactDirs) {
        $artifactPath = Join-Path $targetRootPath $dir
        if (-not (Test-Path $artifactPath)) {
            $issues += @{ level = "warn"; message = "Missing artifact directory: $($TargetDef.directory)/$dir" }
        }
    }

    return $issues
}

function Get-SourceArtifacts {
    param(
        [string]$RepoRoot,
        [pscustomobject]$SourceDef,
        [pscustomobject]$Rules
    )

    $result = @()
    $sourceRoot = Join-Path $RepoRoot $SourceDef.directory

    $includeExtensions = @()
    if ($Rules.includeExtensions) {
        $includeExtensions = @($Rules.includeExtensions | ForEach-Object { $_.ToLowerInvariant() })
    }

    $excludePathContains = @()
    if ($Rules.excludePathContains) {
        $excludePathContains = @($Rules.excludePathContains | ForEach-Object { $_.ToLowerInvariant() })
    }

    foreach ($map in $Rules.pathMappings) {
        $path = Join-Path $sourceRoot $map.from
        if (-not (Test-Path $path)) {
            continue
        }

        $files = Get-ChildItem -Path $path -Recurse -File
        foreach ($file in $files) {
            $relativePath = (Resolve-Path -Path $file.FullName).Path.Substring((Resolve-Path $sourceRoot).Path.Length + 1).Replace('\\', '/')
            $normalizedRelative = "/" + $relativePath.ToLowerInvariant()

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

            $result += [pscustomobject]@{
                FullPath = $file.FullName
                RelativePath = $relativePath
                MappingFrom = $map.from
                MappingTo = $map.to
            }
        }
    }

    return $result
}

function Compare-TargetParity {
    param(
        [string]$RepoRoot,
        $TargetDef,
        [object[]]$SourceArtifacts,
        [scriptblock]$HashFn
    )

    $issues = @()
    $targetRoot = Join-Path $RepoRoot $TargetDef.directory

    foreach ($artifact in $SourceArtifacts) {
        $relative = $artifact.RelativePath
        $targetPath = Join-Path $targetRoot $relative

        if (-not (Test-Path $targetPath)) {
            $issues += @{ level = "warn"; category = "parity"; message = "Missing target file: $($TargetDef.directory)/$relative" }
            continue
        }

        $sourceHash = & $HashFn $artifact.FullPath
        $targetHash = & $HashFn $targetPath

        if ($sourceHash -ne $targetHash) {
            $issues += @{ level = "warn"; category = "adaptation"; message = "Content differs for $relative (source vs $($TargetDef.directory))" }
        }
    }

    return $issues
}
