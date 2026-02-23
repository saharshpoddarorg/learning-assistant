<#
.SYNOPSIS
    Compile the search-engine module (self-contained search library).

.DESCRIPTION
    Compiles all sources under src/ in a single pass:
      - search.api.*    -- contracts (interfaces, records, enums, functional types)
      - search.engine.* -- algorithm implementations (BM25, TextMatch, pipeline, etc.)

    Both packages live in the same src/ tree. Zero external dependencies.

    Build hierarchy:
        search-engine  (this)   ← self-contained; search.api.* + search.engine.*
            ↑
        mcp-servers             ← depends on search-engine; adds domain engines

    For a full build that includes mcp-servers, run:
        cd ../mcp-servers && .\build.ps1

.PARAMETER OutDir
    Output directory for compiled .class files. Default: out

.PARAMETER Clean
    Delete the output directory before compiling.

.EXAMPLE
    .\build.ps1
    .\build.ps1 -Clean
#>
[CmdletBinding()]
param(
    [string] $OutDir = "out",
    [switch] $Clean
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $ScriptDir

# Re-use JAVA_HOME resolution from the sibling mcp-servers module.
$MpcBuildEnv = Join-Path $ScriptDir '..\mcp-servers\build.env.local'
if (Test-Path $MpcBuildEnv) {
    Get-Content $MpcBuildEnv | ForEach-Object {
        if ($_ -match '^\s*([^#=]+?)\s*=\s*(.+?)\s*$') {
            Set-Item "env:$($Matches[1])" $Matches[2]
        }
    }
}

function Find-Javac {
    if ($env:JAVA_HOME) {
        $candidate = Join-Path $env:JAVA_HOME 'bin\javac.exe'
        if (Test-Path $candidate) { return $candidate }
    }
    $vsCodeExt = Join-Path $env:USERPROFILE '.vscode\extensions'
    if (Test-Path $vsCodeExt) {
        $jdkDirs = Get-ChildItem $vsCodeExt -Filter 'redhat.java-*' -Directory | Sort-Object Name -Descending
        foreach ($dir in $jdkDirs) {
            $jreRoot = Join-Path $dir.FullName 'jre'
            if (Test-Path $jreRoot) {
                $hit = Get-ChildItem $jreRoot -Recurse -Filter 'javac.exe' -ErrorAction SilentlyContinue | Select-Object -First 1
                if ($hit) { return $hit.FullName }
            }
        }
    }
    $onPath = Get-Command javac -ErrorAction SilentlyContinue
    if ($onPath) { return $onPath.Source }
    return $null
}

Write-Host ""
Write-Host "=== search-engine standalone build ===" -ForegroundColor Cyan
$Javac = Find-Javac
if (-not $Javac) {
    Write-Host "ERROR: javac not found. Set JAVA_HOME or install the VS Code Java Extension Pack." -ForegroundColor Red
    exit 1
}

$VersionLine = (& $Javac -version 2>&1) -join ' '
Write-Host "  javac  : $Javac  ($VersionLine)" -ForegroundColor Cyan
Write-Host ""

if ($Clean -and (Test-Path $OutDir)) {
    Write-Host "Cleaning $OutDir/ ..." -ForegroundColor Yellow
    Remove-Item $OutDir -Recurse -Force
}
New-Item -ItemType Directory -Path $OutDir -Force | Out-Null

# search-engine is self-contained: search.api.* (contracts) and search.engine.* (impls)
# live in the same src/ tree — no external search-api dependency needed.
$Sources = Get-ChildItem -Path 'src' -Recurse -Filter '*.java' |
           Select-Object -ExpandProperty FullName

if (-not $Sources) {
    Write-Host "ERROR: No .java files found under src/" -ForegroundColor Red
    exit 1
}

Write-Host "Compiling $($Sources.Count) source files  ->  $OutDir/" -ForegroundColor Cyan

$SourceList = [System.IO.Path]::GetTempFileName()
[System.IO.File]::WriteAllLines($SourceList, $Sources)

$ExitCode = 0
try {
    & $Javac -d $OutDir --release 21 "@$SourceList"
    $ExitCode = $LASTEXITCODE
} finally {
    Remove-Item $SourceList -ErrorAction SilentlyContinue
}

Write-Host ""
if ($ExitCode -eq 0) {
    Write-Host "BUILD SUCCESS -- compiled $($Sources.Count) files" -ForegroundColor Green
} else {
    Write-Host "BUILD FAILED  (exit code $ExitCode)" -ForegroundColor Red
    exit $ExitCode
}
