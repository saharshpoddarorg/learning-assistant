# =============================================================================
# close-browser.ps1 — Gracefully stop the MCP-managed browser instance (Windows)
# =============================================================================
# Reads the saved PID from .browser-pid and terminates the process.
#
# Usage:
#   .\scripts\common\browser\close-browser.ps1 [-Force] [-Timeout <seconds>]
# =============================================================================

param(
    [switch]$Force,
    [int]$Timeout = 5
)

$ErrorActionPreference = "Stop"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$ProjectRoot = Resolve-Path (Join-Path $ScriptDir "..\..\..") | Select-Object -ExpandProperty Path
$PidFile = Join-Path $ProjectRoot "scripts\.browser-pid"

# --- Check PID file -----------------------------------------------------------
if (-not (Test-Path $PidFile)) {
    Write-Host "No .browser-pid file found. Browser may not have been launched by MCP scripts."
    exit 0
}

$BrowserPid = (Get-Content $PidFile -Raw).Trim()

if (-not $BrowserPid) {
    Write-Host "PID file is empty. Cleaning up."
    Remove-Item $PidFile -Force
    exit 0
}

$BrowserPid = [int]$BrowserPid

# --- Check if process is running ---------------------------------------------
$process = Get-Process -Id $BrowserPid -ErrorAction SilentlyContinue
if (-not $process) {
    Write-Host "Browser (PID $BrowserPid) is not running. Cleaning up PID file."
    Remove-Item $PidFile -Force
    exit 0
}

Write-Host "=== MCP Browser Shutdown ===" -ForegroundColor Cyan
Write-Host "PID     : $BrowserPid"
Write-Host "Force   : $Force"
Write-Host "Timeout : ${Timeout}s"
Write-Host "============================" -ForegroundColor Cyan

# --- Terminate ----------------------------------------------------------------
if ($Force) {
    Write-Host "Force-killing browser (PID $BrowserPid)..."
    Stop-Process -Id $BrowserPid -Force -ErrorAction SilentlyContinue
} else {
    Write-Host "Requesting graceful shutdown (PID $BrowserPid)..."
    # CloseMainWindow for graceful exit
    try {
        $process.CloseMainWindow() | Out-Null
    } catch {
        # Ignore — process may not have a main window
    }

    $elapsed = 0
    while ($elapsed -lt $Timeout) {
        Start-Sleep -Seconds 1
        $elapsed++
        $still = Get-Process -Id $BrowserPid -ErrorAction SilentlyContinue
        if (-not $still) { break }
        Write-Host "  Waiting... (${elapsed}s / ${Timeout}s)"
    }

    $still = Get-Process -Id $BrowserPid -ErrorAction SilentlyContinue
    if ($still) {
        Write-Host "Graceful shutdown timed out. Force-killing..."
        Stop-Process -Id $BrowserPid -Force -ErrorAction SilentlyContinue
    }
}

Write-Host "Browser stopped."
Remove-Item $PidFile -Force -ErrorAction SilentlyContinue
Write-Host "PID file cleaned up."
