# =============================================================================
# Token-Check.ps1 — Verify if current API credentials/tokens are valid (Windows)
# =============================================================================
# Checks configured API keys by attempting lightweight API calls.
#
# Usage:
#   .\scripts\common\auth\Token-Check.ps1 [-Service <name>] [-Verbose]
# =============================================================================

param(
    [string]$Service = "",
    [switch]$ShowVerbose
)

$ErrorActionPreference = "Stop"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$ProjectRoot = Resolve-Path (Join-Path $ScriptDir "..\..\..") | Select-Object -ExpandProperty Path

# Source shared config reader
. (Join-Path $ProjectRoot "scripts\common\utils\Read-Config.ps1")

$Total = 0
$Valid = 0
$Invalid = 0

Write-Host "=== MCP Token Validation ===" -ForegroundColor Cyan
Write-Host ""

# --- Service-specific checks --------------------------------------------------

function Test-GithubToken {
    $token = Read-McpConfig -Key "apiKeys.github"

    if (-not $token) {
        Write-Host "  [github] SKIP - no API key configured"
        return
    }

    $script:Total++
    Write-Host -NoNewline "  [github] Checking... "

    try {
        $headers = @{
            "Authorization" = "token $token"
            "Accept" = "application/vnd.github.v3+json"
            "User-Agent" = "MCP-Config-Validator"
        }

        $response = Invoke-WebRequest -Uri "https://api.github.com/user" -Headers $headers -UseBasicParsing -ErrorAction Stop
        $body = $response.Content | ConvertFrom-Json

        if ($ShowVerbose) {
            Write-Host ""
            Write-Host "    HTTP Status: $($response.StatusCode)"
            Write-Host "    Login: $($body.login)"
        }

        Write-Host "VALID (authenticated as: $($body.login))" -ForegroundColor Green
        $script:Valid++
    }
    catch {
        $statusCode = 0
        if ($_.Exception.Response) {
            $statusCode = [int]$_.Exception.Response.StatusCode
        }

        switch ($statusCode) {
            401 {
                Write-Host "INVALID (bad credentials or expired token)" -ForegroundColor Red
                $script:Invalid++
            }
            403 {
                Write-Host "WARN (rate limited or forbidden)" -ForegroundColor Yellow
                $script:Valid++
            }
            default {
                Write-Host "UNKNOWN (HTTP $statusCode)" -ForegroundColor Yellow
                $script:Invalid++
            }
        }
    }
}

function Test-GenericToken {
    param([string]$ServiceName)

    $token = Read-McpConfig -Key "apikey.$ServiceName"
    if (-not $token) { return }

    $script:Total++
    Write-Host "  [$ServiceName] API key present (length: $($token.Length)) - no specific validation available"
    $script:Valid++
}

# --- Run checks ---------------------------------------------------------------
if ($Service) {
    switch ($Service) {
        "github" { Test-GithubToken }
        default  { Test-GenericToken -ServiceName $Service }
    }
} else {
    Test-GithubToken

    # Discover other API keys
    if (Test-Path $global:McpConfigFile) {
        $otherKeys = Get-Content $global:McpConfigFile | Where-Object { $_ -match '^apikey\.' -and $_ -notmatch '^apikey\.github=' } | ForEach-Object {
            ($_ -split '\.')[1] -split '=' | Select-Object -First 1
        } | Sort-Object -Unique

        foreach ($svc in $otherKeys) {
            Test-GenericToken -ServiceName $svc
        }
    }
}

# --- Summary ------------------------------------------------------------------
Write-Host ""
Write-Host "=== Token Check Summary ===" -ForegroundColor Cyan
Write-Host "Total   : $Total"
Write-Host "Valid   : $Valid"
Write-Host "Invalid : $Invalid"

if ($Invalid -gt 0) {
    Write-Host "Status  : ISSUES FOUND" -ForegroundColor Red
    exit 1
} else {
    Write-Host "Status  : ALL VALID" -ForegroundColor Green
    exit 0
}
