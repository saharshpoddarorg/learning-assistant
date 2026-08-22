Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Resolve-RepositoryRoot {
    param([string]$StartPath = $PSScriptRoot)

    $current = Resolve-Path $StartPath
    while ($true) {
        if (Test-Path (Join-Path $current ".git")) {
            return $current.Path
        }

        $parent = Split-Path $current -Parent
        if ($parent -eq $current) {
            throw "Repository root not found from path: $StartPath"
        }
        $current = Resolve-Path $parent
    }
}

function Write-Log {
    param(
        [ValidateSet("INFO", "WARN", "ERROR", "DEBUG")]
        [string]$Level,
        [string]$Message,
        [switch]$VerboseOnly
    )

    if ($VerboseOnly -and -not $script:VerboseEnabled) {
        return
    }

    $prefix = "[$Level]"
    Write-Host "$prefix $Message"
}

function Load-JsonFile {
    param([string]$Path)

    if (-not (Test-Path $Path)) {
        throw "Missing JSON file: $Path"
    }

    return Get-Content -Raw -Path $Path | ConvertFrom-Json
}

function Save-ReportJson {
    param(
        [hashtable]$Report,
        [string]$Path
    )

    $dir = Split-Path $Path -Parent
    if ($dir -and -not (Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
    }

    $Report | ConvertTo-Json -Depth 10 | Set-Content -Path $Path -Encoding UTF8
}

function Get-FileHashSafe {
    param([string]$Path)

    if (-not (Test-Path $Path)) {
        return $null
    }

    return (Get-FileHash -Path $Path -Algorithm SHA256).Hash
}

function Get-RelativePath {
    param(
        [string]$BasePath,
        [string]$FullPath
    )

    $base = [System.IO.Path]::GetFullPath($BasePath)
    $full = [System.IO.Path]::GetFullPath($FullPath)
    $relative = [System.IO.Path]::GetRelativePath($base, $full)
    return $relative -replace "\\", "/"
}
