<#
.SYNOPSIS
    PSI Tools CLI — Lightweight PowerShell client for the PSI Tools IntelliJ IDEA plugin.

.DESCRIPTION
    Calls the PSI Tools MCP server hosted by the IntelliJ plugin via simple JSON-RPC
    over HTTP. No handshake, no session management, no external dependencies.

.USAGE
    $env:CLI_JSON_ARGS = '{"symbol":"com.example.MyClass"}'; & "<skill>/scripts/psi_tools_cli.ps1" find_usages

.PREREQUISITES
    - IntelliJ IDEA running with the PSI Tools plugin active (hosts server on localhost:3000)
    - PowerShell 5.1+ (built-in on Windows)

.OUTPUTS
    JSON to stdout:
      { "success": true,  "data": {...} }
      { "success": false, "error": "..." }

.VERSION 3.0.0
#>

param(
    [Parameter(Position = 0)]
    [string]$Action
)

$ErrorActionPreference = 'Stop'

# ═══════════════════════════════════════════════════════════════════════════════
# Configuration — env vars override defaults
# ═══════════════════════════════════════════════════════════════════════════════

$PSI_HOST = if ($env:PSI_TOOLS_HOST) { $env:PSI_TOOLS_HOST } else { 'localhost' }
$PSI_PORT = if ($env:PSI_TOOLS_PORT) { [int]$env:PSI_TOOLS_PORT } else { 3000 }
$INSPECTION_PORT = if ($env:PSI_TOOLS_INSPECTION_PORT) { [int]$env:PSI_TOOLS_INSPECTION_PORT } else { 3001 }
$TIMEOUT_SEC = if ($env:PSI_TOOLS_TIMEOUT) { [int]$env:PSI_TOOLS_TIMEOUT / 1000 } else { 120 }

$BASE_URL = "http://${PSI_HOST}:${PSI_PORT}/mcp"
$INSPECTION_URL = "http://${PSI_HOST}:${INSPECTION_PORT}/mcp"

# Actions that route to the inspection server
$INSPECTION_ACTIONS = @('get_file_inspections', 'get_changeset_inspections')

# ═══════════════════════════════════════════════════════════════════════════════
# Argument Parsing
# ═══════════════════════════════════════════════════════════════════════════════

if (-not $Action) {
    $result = @{ success = $false; error = "No action specified. Usage: psi_tools_cli.ps1 <action>" }
    $result | ConvertTo-Json -Depth 2 -Compress
    exit 1
}

$jsonArgs = if ($env:CLI_JSON_ARGS) { $env:CLI_JSON_ARGS } else { '{}' }
try {
    $parsedArgs = $jsonArgs | ConvertFrom-Json -AsHashtable
}
catch {
    $result = @{ success = $false; error = "Invalid JSON arguments: $($_.Exception.Message)" }
    $result | ConvertTo-Json -Depth 2 -Compress
    exit 1
}

# ═══════════════════════════════════════════════════════════════════════════════
# Core: Call a PSI Tools action via JSON-RPC
# ═══════════════════════════════════════════════════════════════════════════════

function Invoke-PsiTool {
    param(
        [string]$ToolName,
        [hashtable]$Arguments,
        [string]$Url
    )

    $body = @{
        jsonrpc = '2.0'
        id      = 1
        method  = 'tools/call'
        params  = @{
            name      = $ToolName
            arguments = $Arguments
        }
    } | ConvertTo-Json -Depth 10 -Compress

    try {
        $response = Invoke-RestMethod -Uri $Url -Method POST -ContentType 'application/json' -Body $body -TimeoutSec $TIMEOUT_SEC
    }
    catch {
        return @{ success = $false; error = $_.Exception.Message }
    }

    # Check for JSON-RPC error
    if ($response.error) {
        return @{ success = $false; error = $response.error.message }
    }

    # Parse the wrapped MCP content
    $content = $response.result.content
    if ($content -and $content.Count -gt 0 -and $content[0].text) {
        try {
            $parsed = $content[0].text | ConvertFrom-Json
            return @{ success = $true; data = $parsed }
        }
        catch {
            return @{ success = $true; data = $content[0].text }
        }
    }

    return @{ success = $true; data = $response.result }
}

function Invoke-ListTools {
    $body = @{
        jsonrpc = '2.0'
        id      = 1
        method  = 'tools/list'
        params  = @{}
    } | ConvertTo-Json -Depth 5 -Compress

    try {
        $response = Invoke-RestMethod -Uri $BASE_URL -Method POST -ContentType 'application/json' -Body $body -TimeoutSec $TIMEOUT_SEC
    }
    catch {
        return @{ success = $false; error = $_.Exception.Message }
    }

    if ($response.error) {
        return @{ success = $false; error = $response.error.message }
    }

    $tools = @($response.result.tools | ForEach-Object {
        @{
            name        = $_.name
            description = $_.description
        }
    })

    return @{ success = $true; data = $tools }
}

# ═══════════════════════════════════════════════════════════════════════════════
# Special Actions
# ═══════════════════════════════════════════════════════════════════════════════

function Invoke-HealthCheck {
    $results = @{}

    # Check PSI server
    try {
        $echoBody = @{
            jsonrpc = '2.0'; id = 1; method = 'tools/call'
            params = @{ name = 'echo'; arguments = @{ message = 'health-check' } }
        } | ConvertTo-Json -Depth 5 -Compress

        $echoResp = Invoke-RestMethod -Uri $BASE_URL -Method POST -ContentType 'application/json' -Body $echoBody -TimeoutSec 10
        $results['psiServer'] = @{ status = 'up'; port = $PSI_PORT }
    }
    catch {
        $results['psiServer'] = @{ status = 'down'; port = $PSI_PORT; error = $_.Exception.Message }
    }

    # Check inspection server
    try {
        $inspBody = @{
            jsonrpc = '2.0'; id = 2; method = 'tools/call'
            params = @{ name = 'echo'; arguments = @{ message = 'health-check' } }
        } | ConvertTo-Json -Depth 5 -Compress

        $inspResp = Invoke-RestMethod -Uri $INSPECTION_URL -Method POST -ContentType 'application/json' -Body $inspBody -TimeoutSec 10
        $results['inspectionServer'] = @{ status = 'up'; port = $INSPECTION_PORT }
    }
    catch {
        $results['inspectionServer'] = @{ status = 'down'; port = $INSPECTION_PORT; error = $_.Exception.Message }
    }

    $psiUp = $results['psiServer'].status -eq 'up'
    $inspUp = $results['inspectionServer'].status -eq 'up'

    $summary = if ($psiUp -and $inspUp) { 'All servers operational' }
               elseif ($psiUp) { 'PSI server up, inspection server down' }
               else { 'PSI server down - is IntelliJ running with the PSI Tools plugin?' }

    $results['summary'] = $summary
    return @{ success = $psiUp; data = $results }
}

function Invoke-Ping {
    try {
        $r = Invoke-PsiTool -ToolName 'echo' -Arguments @{ message = 'ping' } -Url $BASE_URL
        if ($r.success) {
            return @{ success = $true; data = @{ status = 'ok'; response = $r.data } }
        }
        return $r
    }
    catch {
        return @{ success = $false; error = "Server not reachable: $($_.Exception.Message)" }
    }
}

function Get-Help {
    $actions = @(
        'echo', 'server_info', 'list_tools',
        'find_usages', 'get_class_structure', 'symbol_search', 'file_search', 'text_search', 'get_call_graph',
        'show_subclasses', 'show_superclasses', 'find_implementations', 'get_type_hierarchy',
        'get_method_body', 'get_imports', 'get_annotations', 'explore_class_dependencies',
        'get_method_hierarchy', 'get_changed_line_ranges',
        'get_file_inspections', 'get_changeset_inspections',
        'health', 'ping', 'help'
    ) | Sort-Object

    return @{
        success = $true
        data    = @{
            version       = '3.0.0'
            usage         = '$env:CLI_JSON_ARGS = ''<json>''; & "<skill>/scripts/psi_tools_cli.ps1" <action>'
            actions       = $actions
            configuration = @{
                PSI_TOOLS_HOST            = "$PSI_HOST (default: localhost)"
                PSI_TOOLS_PORT            = "$PSI_PORT (default: 3000)"
                PSI_TOOLS_INSPECTION_PORT = "$INSPECTION_PORT (default: 3001)"
                PSI_TOOLS_TIMEOUT         = "$($TIMEOUT_SEC * 1000)ms (default: 120000)"
            }
        }
    }
}

# ═══════════════════════════════════════════════════════════════════════════════
# Main Dispatch
# ═══════════════════════════════════════════════════════════════════════════════

$result = switch ($Action) {
    'health' { Invoke-HealthCheck }
    'ping'   { Invoke-Ping }
    'help'   { Get-Help }
    'list_tools' { Invoke-ListTools }
    default  {
        # Route to correct server
        $url = if ($Action -in $INSPECTION_ACTIONS) { $INSPECTION_URL } else { $BASE_URL }
        Invoke-PsiTool -ToolName $Action -Arguments $parsedArgs -Url $url
    }
}

$result | ConvertTo-Json -Depth 20
if (-not $result.success) { exit 1 }
