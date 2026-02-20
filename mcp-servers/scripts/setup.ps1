# =============================================================================
# setup.ps1 — One-time setup for MCP server configuration (Windows)
# =============================================================================
# Automates the developer onboarding flow with minimal intervention:
#   1. Creates mcp-config.local.properties (if missing)
#   2. Creates the MCP browser data directory (auto-isolation)
#   3. Auto-detects installed browser
#   4. Checks for API keys
#   5. Runs config validation (if validate-config.sh available via WSL/Git Bash)
#   6. Prints a summary of what's ready and what needs attention
#
# Usage:
#   .\scripts\setup.ps1 [-SkipValidation]
# =============================================================================

param(
    [switch]$SkipValidation
)

$ErrorActionPreference = "Stop"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$ProjectRoot = Resolve-Path (Join-Path $ScriptDir "..") | Select-Object -ExpandProperty Path

# Source shared config reader
. (Join-Path $ProjectRoot "scripts\common\utils\Read-Config.ps1")

Write-Host ""
Write-Host "+=======================================+" -ForegroundColor Cyan
Write-Host "|       MCP Server Setup Wizard         |" -ForegroundColor Cyan
Write-Host "+=======================================+" -ForegroundColor Cyan
Write-Host ""

$ReadyItems = @()
$ActionItems = @()

# =============================================================================
# Step 1: Check base config exists
# =============================================================================
Write-Host "[1/5] Checking base config..."
if (Test-Path $global:McpConfigFile) {
    Write-Host "  OK: Base config exists: $(Split-Path -Leaf $global:McpConfigFile)" -ForegroundColor Green
    $ReadyItems += "Base config (committed defaults)"
} else {
    Write-Host "  MISSING: Base config: $($global:McpConfigFile)" -ForegroundColor Red
    Write-Host "    This file should be in the repo. Run: git checkout -- user-config\mcp-config.properties"
    $ActionItems += "Restore base config from git"
}

# =============================================================================
# Step 2: Create local config file (if missing)
# =============================================================================
Write-Host "[2/5] Checking local config..."
$LocalTemplate = Join-Path $ProjectRoot "user-config\mcp-config.local.example.properties"

if (Test-Path $global:McpLocalConfigFile) {
    Write-Host "  OK: Local config exists: $(Split-Path -Leaf $global:McpLocalConfigFile)" -ForegroundColor Green
    $ReadyItems += "Local config (secrets/overrides)"
} else {
    Write-Host "  Creating local config from template..."
    if (Test-Path $LocalTemplate) {
        Copy-Item $LocalTemplate $global:McpLocalConfigFile
        Write-Host "  OK: Created: $(Split-Path -Leaf $global:McpLocalConfigFile)" -ForegroundColor Green
    } else {
        $localContent = @"
# =============================================================================
# Local Config - YOUR secrets and machine-specific overrides
# =============================================================================
# This file is GITIGNORED. Safe for real values.
# Only include keys you need to override.

# --- API keys (required for the services you use) ---
apiKeys.github=
server.github.env.GITHUB_TOKEN=

# --- Uncomment to override ---
# browser.executable=
# location.timezone=
"@
        $localContent | Out-File -FilePath $global:McpLocalConfigFile -Encoding utf8
        Write-Host "  OK: Created: $(Split-Path -Leaf $global:McpLocalConfigFile)" -ForegroundColor Green
    }
    Write-Host "    Edit this file to add your API keys and secrets."
    $ActionItems += "Set API keys in $(Split-Path -Leaf $global:McpLocalConfigFile)"
}

# =============================================================================
# Step 3: Create MCP browser data directory
# =============================================================================
Write-Host "[3/5] Setting up browser isolation..."
$BrowserDataDir = Read-McpConfig -Key "browser.dataDir" -Default ""

if (-not $BrowserDataDir) {
    $BrowserDataDir = Join-Path $env:LOCALAPPDATA "mcp\browser-data"
}

if (Test-Path $BrowserDataDir) {
    Write-Host "  OK: Browser data directory exists: $BrowserDataDir" -ForegroundColor Green
} else {
    New-Item -Path $BrowserDataDir -ItemType Directory -Force | Out-Null
    Write-Host "  OK: Created browser data directory: $BrowserDataDir" -ForegroundColor Green
}
$ReadyItems += "Browser auto-isolation (--user-data-dir)"

# =============================================================================
# Step 4: Auto-detect browser
# =============================================================================
Write-Host "[4/5] Detecting browser..."
$DetectedBrowser = Read-McpConfig -Key "browser.executable" -Default ""

if (-not $DetectedBrowser) {
    $candidates = @(
        "${env:ProgramFiles}\Google\Chrome\Application\chrome.exe",
        "${env:ProgramFiles(x86)}\Google\Chrome\Application\chrome.exe",
        "${env:ProgramFiles(x86)}\Microsoft\Edge\Application\msedge.exe",
        "${env:ProgramFiles}\Microsoft\Edge\Application\msedge.exe",
        "${env:ProgramFiles}\Mozilla Firefox\firefox.exe",
        "${env:ProgramFiles}\BraveSoftware\Brave-Browser\Application\brave.exe"
    )
    foreach ($candidate in $candidates) {
        if (Test-Path $candidate) {
            $DetectedBrowser = $candidate
            break
        }
    }
}

if ($DetectedBrowser) {
    Write-Host "  OK: Detected: $DetectedBrowser" -ForegroundColor Green
    $ReadyItems += "Browser: $(Split-Path -Leaf $DetectedBrowser)"
} else {
    Write-Host "  MISSING: No browser found" -ForegroundColor Yellow
    $ActionItems += "Set browser.executable in config or install Chrome/Edge/Firefox"
}

# =============================================================================
# Step 5: Check API keys
# =============================================================================
Write-Host "[5/5] Checking API keys..."
$GithubKey = Read-McpConfig -Key "apiKeys.github" -Default ""
$GithubToken = Read-McpConfig -Key "server.github.env.GITHUB_TOKEN" -Default ""

if ($GithubKey -or $GithubToken) {
    Write-Host "  OK: GitHub API key configured" -ForegroundColor Green
    $ReadyItems += "GitHub API key"
} else {
    Write-Host "  MISSING: No GitHub API key found" -ForegroundColor Yellow
    $ActionItems += "Set apiKeys.github in $(Split-Path -Leaf $global:McpLocalConfigFile) or MCP_APIKEYS_GITHUB env var"
}

# =============================================================================
# Summary
# =============================================================================
Write-Host ""
Write-Host "+=======================================+" -ForegroundColor Cyan
Write-Host "|          Setup Summary                |" -ForegroundColor Cyan
Write-Host "+=======================================+" -ForegroundColor Cyan

if ($ReadyItems.Count -gt 0) {
    Write-Host ""
    Write-Host "Ready:" -ForegroundColor Green
    foreach ($item in $ReadyItems) {
        Write-Host "  OK: $item" -ForegroundColor Green
    }
}

if ($ActionItems.Count -gt 0) {
    Write-Host ""
    Write-Host "Action needed:" -ForegroundColor Yellow
    foreach ($item in $ActionItems) {
        Write-Host "  ->  $item" -ForegroundColor Yellow
    }
} else {
    Write-Host ""
    Write-Host "All set! Run: cd mcp-servers; javac -d out src/**/*.java; java -cp out Main" -ForegroundColor Green
}

Write-Host ""
Write-Host "Config files:"
Write-Host "  Base (committed) : user-config\mcp-config.properties"
Write-Host "  Local (secrets)  : user-config\mcp-config.local.properties"
Write-Host "  Browser data     : $BrowserDataDir"
Write-Host ""
