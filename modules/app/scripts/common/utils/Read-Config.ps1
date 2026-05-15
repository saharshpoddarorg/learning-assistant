# =============================================================================
# Read-Config.ps1 — Shared config-reading module for MCP scripts (Windows)
# =============================================================================
# Provides config-reading functions used by other scripts.
#
# Usage (dot-source from another script):
#   . "$PSScriptRoot\..\..\common\utils\Read-Config.ps1"
#   $value = Read-McpConfig "browser.executable" "default"
#
# Priority order (highest wins):
#   1. Environment variable (MCP_ prefix, dots->underscores, UPPERCASE)
#   2. Local config (mcp-config.local.properties — gitignored, secrets/overrides)
#   3. Base config  (mcp-config.properties — committed, safe defaults)
#   4. Default value
# =============================================================================

# Prevent double-loading
if ($global:_MCP_READ_CONFIG_LOADED) { return }
$global:_MCP_READ_CONFIG_LOADED = $true

$_RcScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$_RcProjectRoot = Resolve-Path (Join-Path $_RcScriptDir "..\..\..") | Select-Object -ExpandProperty Path

# Allow override via environment
if (-not $env:MCP_CONFIG_FILE) {
    $global:McpConfigFile = Join-Path $_RcProjectRoot "user-config\mcp-config.properties"
} else {
    $global:McpConfigFile = $env:MCP_CONFIG_FILE
}

# Auto-resolve local config file (sibling of base config)
$global:McpLocalConfigFile = Join-Path (Split-Path $global:McpConfigFile) "mcp-config.local.properties"

# =============================================================================
# Read-FromFile <file> <key>  — Internal helper
# =============================================================================
function _Read-FromFile {
    param([string]$File, [string]$Key)

    if (Test-Path $File) {
        $line = Get-Content $File | Where-Object {
            $_ -match "^$([regex]::Escape($Key))="
        } | Select-Object -First 1

        if ($line) {
            $val = ($line -split '=', 2)[1].Trim()
            if ($val -and $val -notmatch '<<<PLACEHOLDER') {
                return $val
            }
        }
    }
    return $null
}

# =============================================================================
# Read-McpConfig <key> [default]
#   Layered read: env var > local config > base config > default.
# =============================================================================
function Read-McpConfig {
    param(
        [Parameter(Mandatory)]
        [string]$Key,
        [string]$Default = ""
    )

    # 1. Environment variable (highest priority)
    $envKey = "MCP_" + ($Key -replace '\.', '_').ToUpper()
    $envVal = [System.Environment]::GetEnvironmentVariable($envKey)
    if ($envVal) { return $envVal }

    # 2. Local config (secrets/overrides — gitignored)
    $localVal = _Read-FromFile -File $global:McpLocalConfigFile -Key $Key
    if ($localVal) { return $localVal }

    # 3. Base config (committed safe defaults)
    $baseVal = _Read-FromFile -File $global:McpConfigFile -Key $Key
    if ($baseVal) { return $baseVal }

    # 4. Default
    return $Default
}

# =============================================================================
# Read-McpConfigSection <prefix>
#   Reads all keys matching a prefix. Local values override base for same key.
# =============================================================================
function Read-McpConfigSection {
    param(
        [Parameter(Mandatory)]
        [string]$Prefix
    )

    $results = @{}

    # Base config first
    if (Test-Path $global:McpConfigFile) {
        Get-Content $global:McpConfigFile | Where-Object {
            $_ -match "^$([regex]::Escape($Prefix))\." -and $_ -notmatch '^#'
        } | ForEach-Object {
            $parts = $_ -split '=', 2
            $key = $parts[0].Trim()
            $val = $parts[1].Trim()
            if ($val -notmatch '<<<PLACEHOLDER') {
                $results[$key] = $val
            }
        }
    }

    # Local config overrides (higher priority)
    if (Test-Path $global:McpLocalConfigFile) {
        Get-Content $global:McpLocalConfigFile | Where-Object {
            $_ -match "^$([regex]::Escape($Prefix))\." -and $_ -notmatch '^#'
        } | ForEach-Object {
            $parts = $_ -split '=', 2
            $key = $parts[0].Trim()
            $val = $parts[1].Trim()
            if ($val) {
                $results[$key] = $val
            }
        }
    }

    return $results
}

# =============================================================================
# Test-McpConfig <key>
# =============================================================================
function Test-McpConfig {
    param([Parameter(Mandatory)][string]$Key)
    $val = Read-McpConfig -Key $Key
    return [bool]$val
}

# =============================================================================
# Assert-McpConfig <key> <description>
# =============================================================================
function Assert-McpConfig {
    param(
        [Parameter(Mandatory)][string]$Key,
        [string]$Description = $Key
    )

    $val = Read-McpConfig -Key $Key
    if (-not $val) {
        $envKey = "MCP_" + ($Key -replace '\.', '_').ToUpper()
        Write-Error "Required config missing: $Description ($Key)`n  Set it in: $($global:McpLocalConfigFile) (secrets/overrides)`n  Or in:     $($global:McpConfigFile) (defaults)`n  Or set env var: $envKey"
        exit 1
    }

    return $val
}
