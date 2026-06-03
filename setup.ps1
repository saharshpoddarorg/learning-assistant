# setup.ps1 -- Load learning-assistant environment variables (Windows PowerShell)
#
# USAGE:
#   . .\setup.ps1          # dot-source: exports vars into your current shell
#   . .\setup.ps1 -Check   # validate only, do not export
#
# Reads .env from the repo root, validates required variables,
# then sets them in the current PowerShell session.
#
# Safe to dot-source multiple times (idempotent).

[CmdletBinding()]
param(
    [switch]$Check   # validate without exporting
)

Set-StrictMode -Version Latest

# ---- locate repo root --------------------------------------------------------
$RepoRoot  = $PSScriptRoot
$EnvFile   = Join-Path $RepoRoot ".env"

# ---- load .env ---------------------------------------------------------------
if (-not (Test-Path $EnvFile)) {
    Write-Host ""
    Write-Host "  [setup.ps1] ERROR: .env not found at $EnvFile" -ForegroundColor Red
    Write-Host "  Copy .env.example to .env and fill in your machine-specific paths:"
    Write-Host "    copy .env.example .env"
    Write-Host ""
    return
}

$parsed = @{}

foreach ($line in (Get-Content $EnvFile)) {
    $trimmed = $line.Trim()
    # skip comments and blank lines
    if ($trimmed -match '^#' -or [string]::IsNullOrWhiteSpace($trimmed)) { continue }
    if ($trimmed -match '^([^=]+)=(.*)$') {
        $key   = $Matches[1].Trim()
        $value = $Matches[2].Trim()
        # strip inline comment
        $value = ($value -split '#')[0].Trim()
        if (-not [string]::IsNullOrWhiteSpace($value)) {
            $parsed[$key] = $value
        }
    }
}

# ---- validate required variables ---------------------------------------------
$setupOk = $true

function Test-Required {
    param([string]$VarName, [string]$Example)
    $value = $parsed[$VarName]
    if ([string]::IsNullOrWhiteSpace($value)) {
        Write-Host "  [setup.ps1] MISSING: $VarName is not set in .env" -ForegroundColor Red
        Write-Host "             Example : $VarName=$Example" -ForegroundColor Yellow
        Set-Variable -Name 'setupOk' -Value $false -Scope 1
    } elseif (-not (Test-Path $value -PathType Container)) {
        Write-Host "  [setup.ps1] INVALID: $VarName=$value -- directory does not exist" -ForegroundColor Red
        Set-Variable -Name 'setupOk' -Value $false -Scope 1
    }
}

Write-Host ""
Write-Host "  [setup.ps1] Validating environment..."

Test-Required "JAVA_HOME" "E:/mgcnoscan/JDKs/jdk-21.0.10"
Test-Required "PROJ_HOME" "E:/mgcnoscan/learning/learning-assistant"

# Verify JAVA_HOME points to a JDK (has javac), not a JRE
$javaHome = $parsed["JAVA_HOME"]
if (-not [string]::IsNullOrWhiteSpace($javaHome) -and (Test-Path $javaHome -PathType Container)) {
    $javac = Join-Path $javaHome "bin\javac.exe"
    if (-not (Test-Path $javac)) {
        Write-Host "  [setup.ps1] WARNING: JAVA_HOME=$javaHome does not contain bin\javac.exe" -ForegroundColor Yellow
        Write-Host "             Gradle requires a JDK -- a JRE-only install will not work." -ForegroundColor Yellow
        $setupOk = $false
    }
}

if (-not $setupOk) {
    Write-Host ""
    Write-Host "  [setup.ps1] FAILED -- fix the issues above, then re-run: . .\setup.ps1" -ForegroundColor Red
    Write-Host ""
    return
}

if ($Check) {
    Write-Host "  [setup.ps1] --Check only -- variables are valid but NOT exported." -ForegroundColor Cyan
    Write-Host ""
    return
}

# ---- export into current session --------------------------------------------
foreach ($key in $parsed.Keys) {
    [System.Environment]::SetEnvironmentVariable($key, $parsed[$key], "Process")
    Set-Item "Env:$key" $parsed[$key]
}

# Set derived default for PROJ_TEMP if not explicitly provided
if ([string]::IsNullOrWhiteSpace([System.Environment]::GetEnvironmentVariable("PROJ_TEMP"))) {
    $projTemp = Join-Path $parsed["PROJ_HOME"] "temp"
    [System.Environment]::SetEnvironmentVariable("PROJ_TEMP", $projTemp, "Process")
    Set-Item "Env:PROJ_TEMP" $projTemp
}

# Prepend JDK bin to PATH so 'java' / 'javac' resolve to the right JDK
$jdkBin = Join-Path $parsed["JAVA_HOME"] "bin"
if ($env:PATH -notlike "*$jdkBin*") {
    $env:PATH = "$jdkBin;$env:PATH"
}

# ---- report ------------------------------------------------------------------
Write-Host "  [setup.ps1] OK -- environment ready:" -ForegroundColor Green
Write-Host "    JAVA_HOME        = $env:JAVA_HOME"
Write-Host "    PROJ_HOME        = $env:PROJ_HOME"
Write-Host "    PROJ_TEMP        = $env:PROJ_TEMP"
if (-not [string]::IsNullOrWhiteSpace($env:GRADLE_USER_HOME)) {
    Write-Host "    GRADLE_USER_HOME = $env:GRADLE_USER_HOME"
}
Write-Host "    PATH             = (JDK bin prepended)"
Write-Host ""
