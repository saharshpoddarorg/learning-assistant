# =============================================================================
# launch-browser.ps1 — Launch an isolated browser for MCP server use (Windows)
# =============================================================================
# SAFETY: Always uses a dedicated user-data-dir (%LOCALAPPDATA%\mcp\browser-data)
# so the user's existing tabs, windows, profiles, cookies, and accounts are
# NEVER touched — even with zero configuration.
#
# Usage:
#   .\scripts\common\browser\launch-browser.ps1 [-Url <url>] [-Headless] [-Ephemeral]
#
# Parameters:
#   -Url <url>         URL to open (default: about:blank)
#   -Headless          Override to headless mode
#   -Profile <name>    Profile within MCP data dir
#   -DebugPort <n>     Override remote debugging port
#   -Ephemeral         Use a temp dir (no cookie/session persistence)
#   -DataDir <path>    Override MCP browser data directory
# =============================================================================

param(
    [string]$Url = "about:blank",
    [switch]$Headless,
    [string]$Profile = "",
    [int]$DebugPort = -1,
    [switch]$Ephemeral,
    [string]$DataDir = ""
)

$ErrorActionPreference = "Stop"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$ProjectRoot = Resolve-Path (Join-Path $ScriptDir "..\..\..") | Select-Object -ExpandProperty Path

# Source shared config reader (layered: env > local > base)
. (Join-Path $ProjectRoot "scripts\common\utils\Read-Config.ps1")

# --- Load browser config ------------------------------------------------------
$browserExec = Read-McpConfig -Key "browser.executable" -Default ""
$browserDataDir = Read-McpConfig -Key "browser.dataDir" -Default ""
$browserProfile = Read-McpConfig -Key "browser.profile" -Default ""
$browserLaunchMode = Read-McpConfig -Key "browser.launchMode" -Default "new-window"
$browserHeadless = Read-McpConfig -Key "browser.headless" -Default "false"
$browserDebugPort = [int](Read-McpConfig -Key "browser.remoteDebuggingPort" -Default "0")
$browserWidth = [int](Read-McpConfig -Key "browser.windowWidth" -Default "0")
$browserHeight = [int](Read-McpConfig -Key "browser.windowHeight" -Default "0")

# Apply CLI overrides
if ($Profile) { $browserProfile = $Profile }
if ($Headless) { $browserHeadless = "true" }
if ($DebugPort -ge 0) { $browserDebugPort = $DebugPort }
if ($DataDir) { $browserDataDir = $DataDir }

# --- Resolve browser data directory (auto-isolation) --------------------------
function Get-McpDataDir {
    if ($Ephemeral) {
        $tempDir = Join-Path ([System.IO.Path]::GetTempPath()) "mcp-browser-$([guid]::NewGuid().ToString('N').Substring(0,8))"
        New-Item -Path $tempDir -ItemType Directory -Force | Out-Null
        return $tempDir
    }

    if ($browserDataDir) { return $browserDataDir }

    # Windows default
    return Join-Path $env:LOCALAPPDATA "mcp\browser-data"
}

$mcpDataDir = Get-McpDataDir
if (-not (Test-Path $mcpDataDir)) {
    New-Item -Path $mcpDataDir -ItemType Directory -Force | Out-Null
}

# --- Detect browser -----------------------------------------------------------
function Find-Browser {
    if ($browserExec) { return $browserExec }

    $candidates = @(
        "${env:ProgramFiles}\Google\Chrome\Application\chrome.exe",
        "${env:ProgramFiles(x86)}\Google\Chrome\Application\chrome.exe",
        "${env:ProgramFiles(x86)}\Microsoft\Edge\Application\msedge.exe",
        "${env:ProgramFiles}\Microsoft\Edge\Application\msedge.exe",
        "${env:ProgramFiles}\Mozilla Firefox\firefox.exe",
        "${env:ProgramFiles}\BraveSoftware\Brave-Browser\Application\brave.exe"
    )

    foreach ($candidate in $candidates) {
        if (Test-Path $candidate) { return $candidate }
    }

    foreach ($name in @("chrome", "msedge", "firefox", "brave")) {
        $found = Get-Command $name -ErrorAction SilentlyContinue
        if ($found) { return $found.Source }
    }

    return $null
}

$browser = Find-Browser

if (-not $browser) {
    Write-Error "No browser found. Set browser.executable in config or MCP_BROWSER_EXECUTABLE env var."
    exit 1
}

Write-Host "=== MCP Browser Launch ===" -ForegroundColor Cyan
Write-Host "Browser    : $browser"
Write-Host "Data Dir   : $mcpDataDir $(if ($Ephemeral) { '(ephemeral)' })"
Write-Host "Profile    : $(if ($browserProfile) { $browserProfile } else { 'Default' })"
Write-Host "Mode       : $browserLaunchMode"
Write-Host "Headless   : $browserHeadless"
Write-Host "Debug Port : $browserDebugPort"
Write-Host "URL        : $Url"
Write-Host "==========================" -ForegroundColor Cyan

# --- Build command arguments ---------------------------------------------------
$launchArgs = @()
$browserBase = (Split-Path -Leaf $browser).ToLower()

if ($browserBase -match "chrome|edge|chromium|brave") {
    # --- ISOLATION: dedicated user-data-dir (separate from personal browser) ---
    $launchArgs += "--user-data-dir=$mcpDataDir"

    if ($browserProfile) { $launchArgs += "--profile-directory=$browserProfile" }
    if ($browserHeadless -eq "true") { $launchArgs += "--headless=new" }
    if ($browserDebugPort -ne 0) { $launchArgs += "--remote-debugging-port=$browserDebugPort" }
    if ($browserWidth -ne 0 -and $browserHeight -ne 0) { $launchArgs += "--window-size=${browserWidth},${browserHeight}" }

    switch ($browserLaunchMode) {
        "new-window"  { $launchArgs += "--new-window" }
        "incognito"   { $launchArgs += "--incognito" }
        "app-mode"    { $launchArgs += "--app=$Url"; $Url = "" }
    }

    $launchArgs += "--no-first-run"
    $launchArgs += "--no-default-browser-check"

} elseif ($browserBase -match "firefox") {
    # --- ISOLATION: dedicated profile directory + no-remote ---
    $ffProfileDir = Join-Path $mcpDataDir "firefox-mcp"
    if (-not (Test-Path $ffProfileDir)) {
        New-Item -Path $ffProfileDir -ItemType Directory -Force | Out-Null
    }
    $launchArgs += @("-profile", $ffProfileDir, "--no-remote")

    if ($browserHeadless -eq "true") { $launchArgs += "--headless" }
    if ($browserDebugPort -ne 0) { $launchArgs += @("--start-debugger-server", "$browserDebugPort") }
    if ($browserWidth -ne 0 -and $browserHeight -ne 0) { $launchArgs += @("--width", "$browserWidth", "--height", "$browserHeight") }

    switch ($browserLaunchMode) {
        "new-window"  { $launchArgs += "--new-window" }
        "incognito"   { $launchArgs += "--private-window" }
    }
}

if ($Url) { $launchArgs += $Url }

# --- Launch -------------------------------------------------------------------
Write-Host "Launching: $browser $($launchArgs -join ' ')" -ForegroundColor Green
$process = Start-Process -FilePath $browser -ArgumentList $launchArgs -PassThru
Write-Host "Browser PID: $($process.Id)"

# Save PID for cleanup scripts
$pidFile = Join-Path $ProjectRoot "scripts\.browser-pid"
$process.Id | Out-File -FilePath $pidFile -Encoding ascii -Force
Write-Host "PID saved to: $pidFile"
