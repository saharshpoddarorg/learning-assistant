# =============================================================================
# create-profile.ps1 — Create a browser profile within the MCP data directory
# =============================================================================
# Creates a named profile inside the MCP browser data directory.
#
# NOTE: With auto-isolation (--user-data-dir), launch-browser.ps1 already
# creates an isolated Default profile automatically. This script is only needed
# if you want ADDITIONAL named profiles within the MCP data dir.
#
# Usage:
#   .\scripts\common\browser\create-profile.ps1 [-Name <profile-name>] [-DataDir <path>]
# =============================================================================

param(
    [string]$Name = "MCP-Extra",
    [string]$DataDir = ""
)

$ErrorActionPreference = "Stop"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$ProjectRoot = Resolve-Path (Join-Path $ScriptDir "..\..\..") | Select-Object -ExpandProperty Path

# Source shared config reader
. (Join-Path $ProjectRoot "scripts\common\utils\Read-Config.ps1")

# --- Resolve MCP data directory -----------------------------------------------
function Get-McpDataDir {
    if ($DataDir) { return $DataDir }

    $configured = Read-McpConfig -Key "browser.dataDir" -Default ""
    if ($configured) { return $configured }

    return Join-Path $env:LOCALAPPDATA "mcp\browser-data"
}

$McpDataDir = Get-McpDataDir

# --- Detect browser type from config -----------------------------------------
function Find-BrowserType {
    $exec = Read-McpConfig -Key "browser.executable" -Default ""
    if ($exec) {
        $base = (Split-Path -Leaf $exec).ToLower()
        if ($base -match "chrome|edge|chromium|brave") { return "chromium" }
        if ($base -match "firefox") { return "firefox" }
    }

    if (Test-Path "${env:ProgramFiles}\Google\Chrome\Application\chrome.exe") { return "chromium" }
    if (Test-Path "${env:ProgramFiles(x86)}\Microsoft\Edge\Application\msedge.exe") { return "chromium" }
    if (Test-Path "${env:ProgramFiles}\Mozilla Firefox\firefox.exe") { return "firefox" }

    return "chromium"
}

$BrowserType = Find-BrowserType

Write-Host "=== MCP Browser Profile Setup ===" -ForegroundColor Cyan
Write-Host "Browser Type : $BrowserType"
Write-Host "Data Dir     : $McpDataDir"
Write-Host "Profile      : $Name"
Write-Host "==================================" -ForegroundColor Cyan

# --- Create profile within MCP data dir ----------------------------------------
$ProfileDir = Join-Path $McpDataDir $Name

if (Test-Path $ProfileDir) {
    Write-Host "Profile directory already exists: $ProfileDir"
    Write-Host "Use a different -Name or delete the existing profile first."
    exit 0
}

Write-Host "Creating profile directory: $ProfileDir"
New-Item -Path $ProfileDir -ItemType Directory -Force | Out-Null

switch ($BrowserType) {
    "chromium" {
        New-Item -Path (Join-Path $ProfileDir "First Run") -ItemType File -Force | Out-Null
        Write-Host "Created Chromium profile directory with First Run sentinel."
    }
    "firefox" {
        Write-Host "Created Firefox profile directory."
    }
}

Write-Host ""
Write-Host "Profile created successfully." -ForegroundColor Green
Write-Host ""
Write-Host "To use this profile with launch-browser:"
Write-Host "  .\scripts\common\browser\launch-browser.ps1 -Profile `"$Name`""
