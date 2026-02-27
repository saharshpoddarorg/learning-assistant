<#
.SYNOPSIS
    Lifecycle management for MCP servers (start / stop / restart / status / reset / demo).

.DESCRIPTION
    Controls the two Java MCP server processes in this workspace:
      learning-resources  →  server.learningresources.LearningResourcesServer
      atlassian           →  server.atlassian.AtlassianServer

    Each running process is tracked by a PID file under scripts/.pids/.
    The servers run as background processes; their stdout/stderr are piped to
    a log file under scripts/.logs/ so you can inspect them at any time.

    NOTE: VS Code auto-manages these servers via .vscode/mcp.json when acting
    as an MCP host (GitHub Copilot, etc.).  Use this script for:
      • Local smoke-testing and interactive demos
      • Verifying a server starts cleanly before committing
      • Manual process control outside of VS Code

.PARAMETER Command
    Required. One of:
      start        Start the named server as a background process.
      stop         Stop the named server (or all, if name is "all").
      restart      Stop then start the named server.
      status       Print running / stopped state for all servers (or the named one).
      reset        Stop, clean compiled output, rebuild, then start the named server.
      demo         Run the named server in foreground demo mode (Ctrl-C to quit).
      list-tools   Print all MCP tools exposed by the named server and exit.
      validate     Run config validation and report any issues.
      logs         Tail the log file for the named server (Ctrl-C to stop).

.PARAMETER Server
    Optional. Server name: "learning-resources" | "atlassian" | "all".
    Defaults to "all" for status/stop/reset, required for the rest.

.PARAMETER Follow
    When used with "logs", keep tailing (like tail -f). Default: true.

.EXAMPLE
    .\scripts\server.ps1 status
    .\scripts\server.ps1 start  learning-resources
    .\scripts\server.ps1 stop   all
    .\scripts\server.ps1 restart atlassian
    .\scripts\server.ps1 reset  learning-resources
    .\scripts\server.ps1 demo   learning-resources
    .\scripts\server.ps1 list-tools atlassian
    .\scripts\server.ps1 logs   atlassian
    .\scripts\server.ps1 validate
#>

[CmdletBinding()]
param(
    [Parameter(Mandatory, Position = 0)]
    [ValidateSet("start","stop","restart","status","reset","demo","list-tools","validate","logs","help")]
    [string] $Command,

    [Parameter(Position = 1)]
    [string] $Server = "all",

    [switch] $Follow = $true
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

# ── Paths ─────────────────────────────────────────────────────────────────────
$ScriptDir    = Split-Path -Parent $MyInvocation.MyCommand.Path
$McpRoot      = Resolve-Path (Join-Path $ScriptDir "..") | Select-Object -ExpandProperty Path
$OutDir       = Join-Path $McpRoot "out"
$PidsDir      = Join-Path $ScriptDir ".pids"
$LogsDir      = Join-Path $ScriptDir ".logs"
$BuildScript  = Join-Path $McpRoot "build.ps1"
$ValidateScript = Join-Path $ScriptDir "common\utils\validate-config.sh"

# Create tracking dirs
New-Item -ItemType Directory -Path $PidsDir -Force | Out-Null
New-Item -ItemType Directory -Path $LogsDir -Force | Out-Null

# ── Server registry ───────────────────────────────────────────────────────────
$SERVERS = [ordered]@{
    "learning-resources" = @{
        MainClass   = "server.learningresources.LearningResourcesServer"
        Description = "Learning Resources — curated vault + smart search + web scraper"
        RequiresCredentials = $false
    }
    "atlassian" = @{
        MainClass   = "server.atlassian.AtlassianServer"
        Description = "Atlassian — Jira + Confluence + Bitbucket (27 tools)"
        RequiresCredentials = $true
    }
}

# ── Helpers ───────────────────────────────────────────────────────────────────

function Write-Header([string] $text) {
    Write-Host ""
    Write-Host "  $text" -ForegroundColor Cyan
    Write-Host "  $('─' * $text.Length)" -ForegroundColor DarkCyan
}

function Find-Java {
    # Load build.env.local overrides
    $EnvLocal = Join-Path $McpRoot "build.env.local"
    if (Test-Path $EnvLocal) {
        Get-Content $EnvLocal | ForEach-Object {
            if ($_ -match "^\s*([^#=]+?)\s*=\s*(.+?)\s*$") {
                Set-Item "env:$($Matches[1])" $Matches[2]
            }
        }
    }

    if ($env:JAVA_HOME) {
        $candidate = Join-Path $env:JAVA_HOME "bin\java.exe"
        if (Test-Path $candidate) { return $candidate }
    }

    $vsCodeExt = Join-Path $env:USERPROFILE ".vscode\extensions"
    if (Test-Path $vsCodeExt) {
        $jdkDirs = Get-ChildItem $vsCodeExt -Filter "redhat.java-*" -Directory |
                   Sort-Object Name -Descending
        foreach ($dir in $jdkDirs) {
            $hit = Get-ChildItem (Join-Path $dir.FullName "jre") -Recurse -Filter "java.exe" -ErrorAction SilentlyContinue |
                   Select-Object -First 1
            if ($hit) { return $hit.FullName }
        }
    }

    $onPath = Get-Command java -ErrorAction SilentlyContinue
    if ($onPath) { return $onPath.Source }
    return $null
}

function Get-PidFile([string] $name) { Join-Path $PidsDir "$name.pid" }
function Get-LogFile([string] $name) { Join-Path $LogsDir "$name.log" }

function Get-ServerPid([string] $name) {
    $pidFile = Get-PidFile $name
    if (-not (Test-Path $pidFile)) { return $null }
    $raw = (Get-Content $pidFile -Raw).Trim()
    if (-not $raw) { return $null }
    try { return [int]$raw } catch { return $null }
}

function Test-ServerRunning([string] $name) {
    $pid = Get-ServerPid $name
    if ($null -eq $pid) { return $false }
    try {
        $proc = Get-Process -Id $pid -ErrorAction SilentlyContinue
        return ($null -ne $proc)
    } catch { return $false }
}

function Assert-Built {
    if (-not (Test-Path $OutDir)) {
        Write-Host "  [!] No compiled output found. Building first..." -ForegroundColor Yellow
        Invoke-Build
    }
}

function Invoke-Build([switch] $Clean) {
    Write-Host "  Building MCP servers..." -ForegroundColor Cyan
    $args = @()
    if ($Clean) { $args += "-Clean" }
    & powershell -NoProfile -ExecutionPolicy Bypass -File $BuildScript @args
    if ($LASTEXITCODE -ne 0) {
        Write-Host "  [ERROR] Build failed." -ForegroundColor Red
        exit 1
    }
    Write-Host "  Build complete." -ForegroundColor Green
}

# ── Commands ───────────────────────────────────────────────────────────────────

function Invoke-Status([string] $serverFilter = "all") {
    Write-Header "MCP Server Status"
    $java = Find-Java
    Write-Host "  java : $( if ($java) { $java } else { 'NOT FOUND' } )" -ForegroundColor DarkGray
    Write-Host "  out/ : $( if (Test-Path $OutDir) { 'exists' } else { 'missing — run build first' } )" -ForegroundColor DarkGray
    Write-Host ""

    foreach ($name in $SERVERS.Keys) {
        if ($serverFilter -ne "all" -and $serverFilter -ne $name) { continue }

        $info    = $SERVERS[$name]
        $running = Test-ServerRunning $name
        $pid     = Get-ServerPid $name
        $logFile = Get-LogFile $name

        $indicator = if ($running) { "[RUNNING]" } else { "[STOPPED]" }
        $color     = if ($running) { "Green" } else { "DarkGray" }

        Write-Host "  $indicator  $name" -ForegroundColor $color
        Write-Host "    $($info.Description)" -ForegroundColor DarkGray
        if ($running) {
            Write-Host "    PID : $pid" -ForegroundColor DarkGray
            Write-Host "    Log : $logFile" -ForegroundColor DarkGray
        }
        Write-Host ""
    }
}

function Invoke-Start([string] $name) {
    if (-not $SERVERS.Contains($name)) {
        Write-Host "  [ERROR] Unknown server '$name'. Known: $($SERVERS.Keys -join ', ')" -ForegroundColor Red
        exit 1
    }

    if (Test-ServerRunning $name) {
        $pid = Get-ServerPid $name
        Write-Host "  [$name] Already running (PID $pid). Use 'restart' to bounce it." -ForegroundColor Yellow
        return
    }

    Assert-Built

    $java = Find-Java
    if (-not $java) {
        Write-Host "  [ERROR] java not found. Install JDK 21+ and set JAVA_HOME." -ForegroundColor Red
        exit 1
    }

    $mainClass = $SERVERS[$name].MainClass
    $logFile   = Get-LogFile $name
    $pidFile   = Get-PidFile $name

    Write-Host "  Starting $name..." -ForegroundColor Cyan

    # Launch as a background job; redirect output to log
    $proc = Start-Process -FilePath $java `
                          -ArgumentList @("-cp", "out", $mainClass) `
                          -WorkingDirectory $McpRoot `
                          -RedirectStandardOutput $logFile `
                          -RedirectStandardError  "$logFile.err" `
                          -PassThru `
                          -WindowStyle Hidden

    $proc.Id | Out-File -FilePath $pidFile -Encoding utf8 -NoNewline

    Start-Sleep -Milliseconds 800
    if (-not (Get-Process -Id $proc.Id -ErrorAction SilentlyContinue)) {
        Write-Host "  [ERROR] $name failed to start. Check log: $logFile" -ForegroundColor Red
        exit 1
    }

    Write-Host "  [$name] Started  (PID $($proc.Id))" -ForegroundColor Green
    Write-Host "  Log : $logFile" -ForegroundColor DarkGray
}

function Invoke-Stop([string] $serverFilter) {
    $targets = if ($serverFilter -eq "all") { @($SERVERS.Keys) } else { @($serverFilter) }

    foreach ($name in $targets) {
        if (-not $SERVERS.Contains($name)) {
            Write-Host "  [ERROR] Unknown server '$name'." -ForegroundColor Red
            continue
        }

        if (-not (Test-ServerRunning $name)) {
            Write-Host "  [$name] Not running." -ForegroundColor DarkGray
            continue
        }

        $pid = Get-ServerPid $name
        try {
            Stop-Process -Id $pid -Force -ErrorAction SilentlyContinue
            Write-Host "  [$name] Stopped (PID $pid)." -ForegroundColor Yellow
        } catch {
            Write-Host "  [$name] Could not stop PID $pid: $_" -ForegroundColor Red
        }

        Remove-Item (Get-PidFile $name) -Force -ErrorAction SilentlyContinue
    }
}

function Invoke-Restart([string] $name) {
    Write-Host "  Restarting $name..." -ForegroundColor Cyan
    Invoke-Stop $name
    Start-Sleep -Milliseconds 400
    Invoke-Start $name
}

function Invoke-Reset([string] $serverFilter) {
    $targets = if ($serverFilter -eq "all") { @($SERVERS.Keys) } else { @($serverFilter) }
    Write-Host "  Resetting: $($targets -join ', ')..." -ForegroundColor Cyan
    Invoke-Stop $serverFilter
    Invoke-Build -Clean
    foreach ($name in $targets) { Invoke-Start $name }
}

function Invoke-Demo([string] $name) {
    if (-not $SERVERS.Contains($name)) {
        Write-Host "  [ERROR] Unknown server '$name'." -ForegroundColor Red
        exit 1
    }
    Assert-Built
    $java = Find-Java
    $mainClass = $SERVERS[$name].MainClass
    Write-Host "  Running $name in demo mode (Ctrl-C to stop)..." -ForegroundColor Cyan
    Write-Host ""
    & $java -cp "out" $mainClass --demo
}

function Invoke-ListTools([string] $name) {
    if (-not $SERVERS.Contains($name)) {
        Write-Host "  [ERROR] Unknown server '$name'." -ForegroundColor Red
        exit 1
    }
    Assert-Built
    $java = Find-Java
    $mainClass = $SERVERS[$name].MainClass
    Write-Host "  Tools for $name:" -ForegroundColor Cyan
    Write-Host ""
    & $java -cp "out" $mainClass --list-tools
}

function Invoke-Validate {
    Write-Header "Config Validation"

    # Try PowerShell config reader first
    $readConfig = Join-Path $ScriptDir "common\utils\Read-Config.ps1"
    if (Test-Path $readConfig) {
        . $readConfig
        Write-Host "  Config loaded from layered sources." -ForegroundColor Green
    }

    # Try the shell validator via Git Bash / WSL
    $wsl     = Get-Command wsl -ErrorAction SilentlyContinue
    $gitBash = "C:\Program Files\Git\bin\bash.exe"

    $shScript = $ValidateScript -replace "\\", "/"

    if (Test-Path $gitBash) {
        Write-Host "  Running validate-config.sh via Git Bash..." -ForegroundColor Cyan
        & $gitBash -c "$shScript --fix-suggestions"
    } elseif ($wsl) {
        Write-Host "  Running validate-config.sh via WSL..." -ForegroundColor Cyan
        & wsl bash $shScript --fix-suggestions
    } else {
        Write-Host "  [SKIP] validate-config.sh not runnable (no Git Bash / WSL found)." -ForegroundColor Yellow
        Write-Host "         Install Git for Windows (https://git-scm.com) and re-run." -ForegroundColor DarkGray
    }

    # Also verify Java is present and out/ exists
    $java = Find-Java
    if ($java) {
        Write-Host "  java    : $java  [OK]" -ForegroundColor Green
    } else {
        Write-Host "  java    : NOT FOUND  [ERROR] — Install JDK 21+ and set JAVA_HOME" -ForegroundColor Red
    }

    if (Test-Path $OutDir) {
        Write-Host "  out/    : exists  [OK]" -ForegroundColor Green
    } else {
        Write-Host "  out/    : missing — run 'mcp-servers: build' task  [WARN]" -ForegroundColor Yellow
    }
}

function Invoke-Logs([string] $name, [bool] $follow) {
    if (-not $SERVERS.Contains($name)) {
        Write-Host "  [ERROR] Unknown server '$name'." -ForegroundColor Red
        exit 1
    }
    $logFile = Get-LogFile $name
    if (-not (Test-Path $logFile)) {
        Write-Host "  No log file found for $name. Start the server first." -ForegroundColor Yellow
        return
    }
    Write-Host "  Log: $logFile" -ForegroundColor DarkGray
    if ($follow) {
        Write-Host "  (Ctrl-C to stop tailing)" -ForegroundColor DarkGray
        Get-Content $logFile -Wait -Tail 40
    } else {
        Get-Content $logFile
    }
}

function Invoke-Help {
    Write-Host @"

  MCP Server Lifecycle Manager
  ─────────────────────────────────────────────────────────────

  USAGE
    .\scripts\server.ps1 <command> [server]

  COMMANDS
    status  [server|all]        Show which servers are running
    start    <server>           Start server as background process
    stop    [server|all]        Stop running server(s)
    restart  <server>           Stop then start
    reset   [server|all]        Stop → clean build → rebuild → start
    demo     <server>           Run server in foreground demo mode
    list-tools <server>         Print all MCP tools and exit
    validate                    Check config + environment
    logs     <server>           Tail the server log file
    help                        Show this message

  SERVERS
    learning-resources          Curated vault + smart search (no credentials needed)
    atlassian                   Jira + Confluence + Bitbucket (credentials required)
    all                         All servers (for stop / status / reset)

  EXAMPLES
    .\scripts\server.ps1 status
    .\scripts\server.ps1 start  learning-resources
    .\scripts\server.ps1 stop   all
    .\scripts\server.ps1 restart atlassian
    .\scripts\server.ps1 reset  all
    .\scripts\server.ps1 demo   learning-resources
    .\scripts\server.ps1 list-tools atlassian
    .\scripts\server.ps1 logs   learning-resources
    .\scripts\server.ps1 validate

  NOTE
    VS Code auto-manages these servers via .vscode/mcp.json.
    Use this script for local testing and smoke-checking.
"@
}

# ── Dispatch ──────────────────────────────────────────────────────────────────
Push-Location $McpRoot
try {
    switch ($Command) {
        "status"     { Invoke-Status $Server }
        "start"      { Invoke-Start  $Server }
        "stop"       { Invoke-Stop   $Server }
        "restart"    { Invoke-Restart $Server }
        "reset"      { Invoke-Reset  $Server }
        "demo"       { Invoke-Demo   $Server }
        "list-tools" { Invoke-ListTools $Server }
        "validate"   { Invoke-Validate }
        "logs"       { Invoke-Logs $Server $Follow.IsPresent }
        "help"       { Invoke-Help }
    }
} finally {
    Pop-Location
}
