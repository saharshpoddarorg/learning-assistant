<#
.SYNOPSIS
  Bitbucket Server CLI - PowerShell PAT-authenticated REST API client.
  Supports all Bitbucket read and write operations.

.USAGE
  $env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905"}'
  & "skills/_modular/bitbucket/scripts/bitbucket_cli.ps1" fetch_bitbucket_pr
#>
param([string]$Action)

$ErrorActionPreference = 'Stop'
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

# ── TLS Setup ──
if ($PSVersionTable.PSVersion.Major -le 5) {
    try { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12 -bor [Net.SecurityProtocolType]::Tls13 } catch {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    }
    if (-not ([System.Management.Automation.PSTypeName]'TrustAll').Type) {
        Add-Type @"
using System.Net;using System.Net.Security;using System.Security.Cryptography.X509Certificates;
public class TrustAll{public static void Init(){ServicePointManager.ServerCertificateValidationCallback=delegate{return true;};}}
"@
    }
    [TrustAll]::Init()
    $script:SkipCert = $false
} else {
    $script:SkipCert = $true
}

# ── .env Loader ──
function Load-Env {
    $wsRoot = (Resolve-Path (Join-Path $PSScriptRoot '..\..\..\..')  -ErrorAction SilentlyContinue).Path
    $skillRoot = (Resolve-Path (Join-Path $PSScriptRoot '..') -ErrorAction SilentlyContinue).Path
    $candidates = @()
    if ($wsRoot) { $candidates += (Join-Path $wsRoot '.env') }
    if ($skillRoot) { $candidates += (Join-Path $skillRoot '.env') }
    if ($env:ENV_FILE -and (Test-Path $env:ENV_FILE)) { $candidates += $env:ENV_FILE }
    $result = @{}
    foreach ($f in $candidates) {
        if (-not (Test-Path $f)) { continue }
        foreach ($line in (Get-Content $f -Encoding UTF8)) {
            $t = $line.Trim()
            if (-not $t -or $t.StartsWith('#')) { continue }
            $eq = $t.IndexOf('=')
            if ($eq -lt 1) { continue }
            $k = $t.Substring(0, $eq).Trim()
            $v = $t.Substring($eq + 1).Trim()
            if (($v.StartsWith('"') -and $v.EndsWith('"')) -or ($v.StartsWith("'") -and $v.EndsWith("'"))) {
                $v = $v.Substring(1, $v.Length - 2)
            }
            if ($v) { $result[$k] = $v }
        }
    }
    $result
}

# ── REST Helper ──
function Invoke-AtlassianRest {
    param([string]$Method, [string]$BaseUrl, [string]$Token, [string]$Path, $Body)
    $uri = "$($BaseUrl.TrimEnd('/'))$Path"
    $headers = @{
        'Authorization'      = "Bearer $Token"
        'Accept'             = 'application/json'
        'X-Atlassian-Token'  = 'no-check'
    }
    $params = @{ Uri = $uri; Method = $Method; Headers = $headers; UseBasicParsing = $true }
    if ($script:SkipCert) { $params['SkipCertificateCheck'] = $true }
    if ($Body) {
        $json = if ($Body -is [string]) { $Body } else { $Body | ConvertTo-Json -Depth 20 -Compress }
        $bodyBytes = [System.Text.Encoding]::UTF8.GetBytes($json)
        $params['Body'] = $bodyBytes
        $params['ContentType'] = 'application/json; charset=utf-8'
    }
    $resp = Invoke-WebRequest @params -ErrorAction Stop
    $text = [System.Text.Encoding]::UTF8.GetString($resp.RawContentStream.ToArray())
    if (-not $text.Trim()) { return @{} }
    $text | ConvertFrom-Json
}

function Rest-Get    ($b,$t,$p)       { Invoke-AtlassianRest 'GET'    $b $t $p }
function Rest-Post   ($b,$t,$p,$body) { Invoke-AtlassianRest 'POST'   $b $t $p $body }
function Rest-Put    ($b,$t,$p,$body) { Invoke-AtlassianRest 'PUT'    $b $t $p $body }
function Rest-Delete ($b,$t,$p)       { Invoke-AtlassianRest 'DELETE' $b $t $p }

function Enc([string]$s) { [System.Uri]::EscapeDataString($s) }

# ── Paginated GET (Bitbucket Server) ──
function Rest-GetPaginated ($b,$t,$path,[int]$maxPages=5) {
    $all = @(); $start = 0
    for ($i = 0; $i -lt $maxPages; $i++) {
        $sep = if ($path.Contains('?')) { '&' } else { '?' }
        $data = Rest-Get $b $t "$path${sep}start=$start&limit=500"
        if ($null -eq $data.values -and $null -eq $data.isLastPage) { return $data }
        if ($data.values) { $all += $data.values }
        if ($data.isLastPage -ne $false) { break }
        $start = if ($data.nextPageStart) { $data.nextPageStart } else { $start + 500 }
    }
    @{ values = $all; size = $all.Count }
}

# ═══════════════════════════════════════════════════════════════════════════════
# Action Aliases
# ═══════════════════════════════════════════════════════════════════════════════

$ALIASES = @{
    bitbucket_pr='fetch_bitbucket_pr'; bitbucket_pr_files='fetch_bitbucket_pr_files'
    bitbucket_files='fetch_bitbucket_pr_files'; bitbucket_diff='fetch_bitbucket_pr_diff'
    bitbucket_pr_activities='fetch_bitbucket_pr_activities'
    bitbucket_search='search_bitbucket_prs'; search_bitbucket='search_bitbucket_prs'
    bitbucket_file='fetch_bitbucket_file'; fetch_bitbucket='fetch_bitbucket_file'
    bitbucket_contribution_summary='summarize_bitbucket_contributions'
    summarize_bitbucket_contribution='summarize_bitbucket_contributions'
    pr_comments='get_bitbucket_pr_comments'; add_pr_comment='add_bitbucket_pr_comment'
    bitbucket_tasks='list_bitbucket_tasks'; pr_tasks='list_bitbucket_tasks'
    file_diff='get_bitbucket_file_diff'; check_file_in_pr='check_file_in_bitbucket_pr'
    pr_file='get_bitbucket_pr_file'
}

# ═══════════════════════════════════════════════════════════════════════════════
# Bitbucket Actions
# ═══════════════════════════════════════════════════════════════════════════════

$ACTIONS = @{
    # ── Read ──
    fetch_bitbucket_pr          = { param($b,$t,$a) Rest-Get $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests/$($a.prId)" }
    fetch_bitbucket_pr_files    = { param($b,$t,$a) Rest-GetPaginated $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests/$($a.prId)/changes" }
    fetch_bitbucket_pr_diff     = { param($b,$t,$a) $ctx=if($a.contextLines){$a.contextLines}else{5}; $p="/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests/$($a.prId)/diff"; if($a.filePath){$p+="/$($a.filePath)"}; Rest-Get $b $t "$p`?contextLines=$ctx" }
    fetch_bitbucket_pr_activities = { param($b,$t,$a) Rest-GetPaginated $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests/$($a.prId)/activities" }
    search_bitbucket_prs        = { param($b,$t,$a) $p="/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests?state=$(if($a.state){$a.state}else{'ALL'})"; if($a.author){$p+="&author=$([System.Uri]::EscapeDataString($a.author))"}; if($a.maxResults){$p+="&limit=$($a.maxResults)"}; Rest-GetPaginated $b $t $p 3 }
    fetch_bitbucket_file        = { param($b,$t,$a) $p="/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/browse/$($a.filePath)"; if($a.branch){$p+="?at=$([System.Uri]::EscapeDataString($a.branch))"}; Rest-Get $b $t $p }
    summarize_bitbucket_contributions = { param($b,$t,$a)
        $months=if($a.months){$a.months}else{2}
        $data=Rest-GetPaginated $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests?state=ALL&limit=100" 3
        $needle=$a.person.ToLower()
        $prs=@($data.values|Where-Object{$author=$_.author.user.displayName;if(-not $author){$author=$_.author.user.name};$author -and $author.ToLower().Contains($needle)})
        $cutoff=(Get-Date).AddDays(-($months*30))
        $recent=@($prs|Where-Object{$_.createdDate -and ([DateTimeOffset]::FromUnixTimeMilliseconds($_.createdDate).DateTime -ge $cutoff)})
        @{person=$a.person;totalPRs=$recent.Count;merged=@($recent|Where-Object{$_.state -eq 'MERGED'}).Count;open=@($recent|Where-Object{$_.state -eq 'OPEN'}).Count;declined=@($recent|Where-Object{$_.state -eq 'DECLINED'}).Count;prs=@($recent|ForEach-Object{@{id=$_.id;title=$_.title;state=$_.state;created=if($_.createdDate){[DateTimeOffset]::FromUnixTimeMilliseconds($_.createdDate).ToString('yyyy-MM-dd')}else{''};fromBranch=if($_.fromRef){$_.fromRef.displayId}else{''};toBranch=if($_.toRef){$_.toRef.displayId}else{''}}})}
    }
    get_bitbucket_pr_comments = { param($b,$t,$a)
        $data=Rest-GetPaginated $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests/$($a.prId)/activities"
        $comments=@($data.values|Where-Object{$_.action -eq 'COMMENTED' -and $_.comment}|ForEach-Object{$_.comment})
        @{values=$comments;size=$comments.Count}
    }
    get_bitbucket_file_diff   = { param($b,$t,$a) $ctx=if($a.contextLines){$a.contextLines}else{5}; $p="/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/diff/$($a.filePath)?contextLines=$ctx"; if($a.since){$p+="&since=$(Enc $a.since)"}; if($a.until){$p+="&until=$(Enc $a.until)"}; Rest-Get $b $t $p }
    check_file_in_bitbucket_pr = { param($b,$t,$a)
        $data=Rest-GetPaginated $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests/$($a.prId)/changes"
        $files=@($data.values|ForEach-Object{if($_.path.toString){$_.path.toString}else{"$($_.path.parent)/$($_.path.name)"}})
        $found=$files|Where-Object{$_ -like "*$($a.filePath)*"}
        @{found=[bool]$found;filePath=$a.filePath;totalChangedFiles=$files.Count;matchingFiles=@($found)}
    }
    get_bitbucket_pr_file = { param($b,$t,$a)
        $pr=Rest-Get $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests/$($a.prId)"
        $branch=if($pr.fromRef.id){$pr.fromRef.id}else{$pr.fromRef.displayId}
        if(-not $branch){throw 'Could not determine PR source branch'}
        Rest-Get $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/browse/$($a.filePath)?at=$([System.Uri]::EscapeDataString($branch))"
    }

    # ── Write ──
    add_bitbucket_pr_comment = { param($b,$t,$a)
        $p=@{text=$a.comment}
        if($a.parentId){$p.parent=@{id=$a.parentId}}
        if($a.filePath){$p.anchor=@{path=$a.filePath;lineType=if($a.lineType){$a.lineType}else{'ADDED'};line=if($a.line){$a.line}else{1};fileType=if($a.fileType){$a.fileType}else{'TO'}}}
        Rest-Post $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests/$($a.prId)/comments" $p
    }
    update_bitbucket_pr_comment = { param($b,$t,$a) Rest-Put $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests/$($a.prId)/comments/$($a.commentId)" @{text=$a.comment;version=if($a.version){$a.version}else{0}} }
    delete_bitbucket_pr_comment = { param($b,$t,$a) $v=if($a.version){$a.version}else{0}; Rest-Delete $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests/$($a.prId)/comments/$($a.commentId)?version=$v" }
    reply_bitbucket_pr_comment  = { param($b,$t,$a) Rest-Post $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests/$($a.prId)/comments" @{text=$a.comment;parent=@{id=$a.parentCommentId}} }
    create_bitbucket_task       = { param($b,$t,$a) Rest-Post $b $t '/rest/api/1.0/tasks' @{anchor=@{id=$a.commentId;type='COMMENT'};text=$a.text} }
    list_bitbucket_tasks        = { param($b,$t,$a) Rest-Get $b $t "/rest/api/1.0/projects/$(Enc $a.project)/repos/$(Enc $a.repo)/pull-requests/$($a.prId)/tasks" }
    update_bitbucket_task       = { param($b,$t,$a) Rest-Put $b $t "/rest/api/1.0/tasks/$($a.taskId)" @{text=$a.text} }
    delete_bitbucket_task       = { param($b,$t,$a) Rest-Delete $b $t "/rest/api/1.0/tasks/$($a.taskId)" }
    resolve_bitbucket_task      = { param($b,$t,$a) Rest-Put $b $t "/rest/api/1.0/tasks/$($a.taskId)" @{state='RESOLVED'} }
    reopen_bitbucket_task       = { param($b,$t,$a) Rest-Put $b $t "/rest/api/1.0/tasks/$($a.taskId)" @{state='OPEN'} }
}

# ═══════════════════════════════════════════════════════════════════════════════
# Main
# ═══════════════════════════════════════════════════════════════════════════════

function Out-Result($success, $data) {
    $obj = @{ success = $success }
    if ($success) { $obj.data = $data } else { $obj.error = $data }
    $json = $obj | ConvertTo-Json -Depth 20 -Compress
    [Console]::Out.Write($json)
}

try {
    if ($ALIASES.ContainsKey($Action)) { $Action = $ALIASES[$Action] }

    if (-not $Action) {
        Out-Result $false 'Usage: bitbucket_cli.ps1 <action>. Set $env:CLI_JSON_ARGS with JSON arguments.'
        exit 1
    }

    if (-not $ACTIONS.ContainsKey($Action)) {
        Out-Result $false "Unknown action: `"$Action`". Available: $($ACTIONS.Keys -join ', ')"
        exit 1
    }

    $rawArgs = if ($env:CLI_JSON_ARGS) { $env:CLI_JSON_ARGS } else { '{}' }
    $env:CLI_JSON_ARGS = $null
    try { $args2 = $rawArgs | ConvertFrom-Json } catch {
        Out-Result $false "Invalid JSON arguments: $($_.Exception.Message)"
        exit 1
    }
    $ht = @{}
    $args2.PSObject.Properties | ForEach-Object { $ht[$_.Name] = $_.Value }

    $envVars = Load-Env
    if (-not $envVars['BITBUCKET_PAT_TOKEN']) { Out-Result $false 'Missing BITBUCKET_PAT_TOKEN in .env file.'; exit 1 }
    if (-not $envVars['BITBUCKET_BASE_URL'])  { Out-Result $false 'Missing BITBUCKET_BASE_URL in .env file.'; exit 1 }

    $baseUrl = $envVars['BITBUCKET_BASE_URL']
    $token   = $envVars['BITBUCKET_PAT_TOKEN']

    $result = & $ACTIONS[$Action] $baseUrl $token $ht
    Out-Result $true $result

} catch {
    Out-Result $false $_.Exception.Message
    exit 1
}
