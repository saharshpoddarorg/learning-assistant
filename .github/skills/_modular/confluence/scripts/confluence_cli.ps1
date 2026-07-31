<#
.SYNOPSIS
  Confluence CLI - PowerShell PAT-authenticated REST API client.
  Supports all Confluence read operations and safe (non-HTML) write operations.
  For HTML-write operations, use Node.js 18+ with confluence_cli.js instead.

.USAGE
  $env:CLI_JSON_ARGS = '{"pageId":"123456"}'
  & "skills/_modular/confluence/scripts/confluence_cli.ps1" fetch_confluence_page
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

function Invoke-RawWeb { param([hashtable]$Params) if($script:SkipCert){$Params['SkipCertificateCheck']=$true}; Invoke-WebRequest @Params -ErrorAction Stop }

# ═══════════════════════════════════════════════════════════════════════════════
# Blocked Actions (require Node.js for HTML content)
# ═══════════════════════════════════════════════════════════════════════════════

$BLOCKED_ACTIONS = @(
    'create_confluence_page','update_confluence_page','append_to_confluence_page',
    'add_confluence_comment','reply_to_confluence_comment','add_confluence_inline_comment',
    'create_confluence_blog_post','copy_confluence_page','move_confluence_page'
)

# ═══════════════════════════════════════════════════════════════════════════════
# Action Aliases
# ═══════════════════════════════════════════════════════════════════════════════

$ALIASES = @{
    search_confluence_pages='search_confluence'; search_confluence_page='search_confluence'
    confluence_search='search_confluence'; confluence_cql_search='search_confluence_cql'
    search_confluence_cql_pages='search_confluence_cql'
    get_confluence_page='fetch_confluence_page'; read_confluence_page='fetch_confluence_page'
    fetch_confluence='fetch_confluence_page'; comment_confluence='add_confluence_comment'
    blog_posts='get_confluence_blog_posts'; add_labels='add_confluence_page_labels'
    page_labels='get_confluence_page_labels'; remove_label='remove_confluence_page_label'
    page_tree='get_confluence_page_tree'; page_ancestors='get_confluence_page_ancestors'
    page_versions='get_confluence_page_versions'; confluence_space='get_confluence_space'
    space_content='get_space_content'; confluence_templates='get_confluence_templates'
    export_pdf='export_confluence_page_pdf'; confluence_myself='get_current_confluence_user'
}

# ═══════════════════════════════════════════════════════════════════════════════
# Confluence Actions
# ═══════════════════════════════════════════════════════════════════════════════

$ACTIONS = @{
    # ── Read ──
    fetch_confluence_page       = { param($b,$t,$a) Rest-Get $b $t "/rest/api/content/$($a.pageId)?expand=version,space,body.storage,metadata.labels" }
    search_confluence           = { param($b,$t,$a) $q=$a.query -replace '"','\"'; $cql="text ~ `"$q`" OR title ~ `"$q`""; $m=if($a.maxResults){$a.maxResults}else{10}; $sf=if($a.spaceKey){" AND space = `"$($a.spaceKey)`""}else{''}; Rest-Get $b $t "/rest/api/content/search?cql=$([System.Uri]::EscapeDataString($cql+$sf))&limit=$m&expand=space,version" }
    search_confluence_cql       = { param($b,$t,$a) $m=if($a.maxResults){$a.maxResults}else{10}; Rest-Get $b $t "/rest/api/content/search?cql=$([System.Uri]::EscapeDataString($a.cql))&limit=$m&expand=space,version,history.lastUpdated" }
    list_confluence_pages       = { param($b,$t,$a) $m=if($a.maxResults){$a.maxResults}else{50}; Rest-Get $b $t "/rest/api/content/$($a.parentPageId)/child/page?limit=$m&expand=version" }
    get_confluence_comments     = { param($b,$t,$a) $m=if($a.maxResults){$a.maxResults}else{25}; Rest-Get $b $t "/rest/api/content/$($a.pageId)/child/comment?expand=body.storage,version,ancestors&limit=$m&depth=all" }
    get_confluence_page_labels  = { param($b,$t,$a) Rest-Get $b $t "/rest/api/content/$($a.pageId)/label" }
    search_confluence_by_label  = { param($b,$t,$a) $m=if($a.maxResults){$a.maxResults}else{25}; $cql="label = `"$($a.label)`""; if($a.spaceKey){$cql+=" AND space = `"$($a.spaceKey)`""}; Rest-Get $b $t "/rest/api/content/search?cql=$([System.Uri]::EscapeDataString($cql))&limit=$m&expand=space,version" }
    get_confluence_page_property = { param($b,$t,$a) if($a.propertyKey){Rest-Get $b $t "/rest/api/content/$($a.pageId)/property/$(Enc $a.propertyKey)"}else{Rest-Get $b $t "/rest/api/content/$($a.pageId)/property"} }
    get_confluence_page_versions = { param($b,$t,$a) $m=if($a.maxResults){$a.maxResults}else{25}; Rest-Get $b $t "/rest/experimental/content/$($a.pageId)/version?limit=$m" }
    get_confluence_page_tree    = { param($b,$t,$a)
        $depth=if($a.depth){$a.depth}else{3}
        function Get-Children($pid,$lvl){
            if($lvl -ge $depth){return @()}
            $d=Rest-Get $b $t "/rest/api/content/$pid/child/page?limit=100&expand=version"
            $r=@($d.results)
            foreach($c in $r){ $c | Add-Member -NotePropertyName children -NotePropertyValue (Get-Children $c.id ($lvl+1)) -Force }
            $r
        }
        @{pageId=$a.pageId;depth=$depth;tree=(Get-Children $a.pageId 0)}
    }
    get_confluence_page_ancestors = { param($b,$t,$a) $p=Rest-Get $b $t "/rest/api/content/$($a.pageId)?expand=ancestors"; @{pageId=$a.pageId;title=$p.title;ancestors=@($p.ancestors)} }
    get_confluence_space        = { param($b,$t,$a) Rest-Get $b $t "/rest/api/space/$(Enc $a.spaceKey)?expand=description.plain,homepage,metadata.labels" }
    get_space_content           = { param($b,$t,$a) $m=if($a.maxResults){$a.maxResults}else{50}; $ct=if($a.contentType){$a.contentType}else{'page'}; Rest-Get $b $t "/rest/api/space/$(Enc $a.spaceKey)/content/${ct}?limit=$m&expand=version" }
    get_confluence_templates    = { param($b,$t,$a) if($a.spaceKey){Rest-Get $b $t "/rest/api/template/page?spaceKey=$(Enc $a.spaceKey)&expand=body"}else{Rest-Get $b $t '/rest/api/template/blueprint?expand=body'} }
    export_confluence_page_pdf  = { param($b,$t,$a) @{pdfUrl="$($b.TrimEnd('/'))/spaces/flyingpdf/pdfpageexport.action?pageId=$($a.pageId)";note='Open this URL in a browser to download the PDF.'} }
    get_current_confluence_user = { param($b,$t,$a) Rest-Get $b $t '/rest/api/user/current' }
    get_confluence_blog_posts   = { param($b,$t,$a) $m=if($a.maxResults){$a.maxResults}else{10}; $cql="type = blogpost AND space = `"$($a.spaceKey)`" ORDER BY created DESC"; Rest-Get $b $t "/rest/api/content/search?cql=$([System.Uri]::EscapeDataString($cql))&limit=$m&expand=space,version,history.lastUpdated" }

    # ── Safe Write (no HTML body) ──
    delete_confluence_page      = { param($b,$t,$a) Rest-Delete $b $t "/rest/api/content/$($a.pageId)" }
    add_confluence_page_labels  = { param($b,$t,$a) $labels=@($a.labels|ForEach-Object{@{prefix='global';name=$_}}); Rest-Post $b $t "/rest/api/content/$($a.pageId)/label" $labels }
    remove_confluence_page_label = { param($b,$t,$a) Rest-Delete $b $t "/rest/api/content/$($a.pageId)/label/$(Enc $a.label)" }
    set_confluence_page_property = { param($b,$t,$a)
        try{ $ex=Rest-Get $b $t "/rest/api/content/$($a.pageId)/property/$(Enc $a.propertyKey)"; Rest-Put $b $t "/rest/api/content/$($a.pageId)/property/$(Enc $a.propertyKey)" @{key=$a.propertyKey;value=$a.value;version=@{number=$ex.version.number+1}} }
        catch{ Rest-Post $b $t "/rest/api/content/$($a.pageId)/property" @{key=$a.propertyKey;value=$a.value} }
    }
    restore_confluence_page_version = { param($b,$t,$a) Rest-Post $b $t "/rest/experimental/content/$($a.pageId)/version" @{operationKey='restore';params=@{versionNumber=$a.versionNumber}} }
    like_confluence_page = { param($b,$t,$a)
        $uri="$($b.TrimEnd('/'))/rest/api/content/$($a.pageId)/likes"
        $meth=if($a.unlike){'DELETE'}else{'POST'}
        Invoke-RawWeb @{Uri=$uri;Method=$meth;Headers=@{Authorization="Bearer $t";Accept='application/json';'X-Atlassian-Token'='no-check'};UseBasicParsing=$true} | Out-Null
        @{liked=(-not $a.unlike);pageId=$a.pageId}
    }
    watch_confluence_page   = { param($b,$t,$a) $uri="$($b.TrimEnd('/'))/rest/api/user/watch/content/$($a.pageId)"; Invoke-RawWeb @{Uri=$uri;Method='POST';Headers=@{Authorization="Bearer $t";Accept='application/json';'X-Atlassian-Token'='no-check'};UseBasicParsing=$true} | Out-Null; @{watching=$true;pageId=$a.pageId} }
    unwatch_confluence_page = { param($b,$t,$a) $uri="$($b.TrimEnd('/'))/rest/api/user/watch/content/$($a.pageId)"; Invoke-RawWeb @{Uri=$uri;Method='DELETE';Headers=@{Authorization="Bearer $t";Accept='application/json';'X-Atlassian-Token'='no-check'};UseBasicParsing=$true} | Out-Null; @{watching=$false;pageId=$a.pageId} }
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
        Out-Result $false 'Usage: confluence_cli.ps1 <action>. Set $env:CLI_JSON_ARGS with JSON arguments.'
        exit 1
    }

    if ($BLOCKED_ACTIONS -contains $Action) {
        Out-Result $false "Action `"$Action`" requires Confluence HTML content upload which is not supported in the PowerShell fallback due to encoding risks. Install Node.js 18+ and use confluence_cli.js instead."
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
    if (-not $envVars['CONFLUENCE_PAT_TOKEN']) { Out-Result $false 'Missing CONFLUENCE_PAT_TOKEN in .env file.'; exit 1 }
    if (-not $envVars['CONFLUENCE_BASE_URL'])  { Out-Result $false 'Missing CONFLUENCE_BASE_URL in .env file.'; exit 1 }

    $baseUrl = $envVars['CONFLUENCE_BASE_URL']
    $token   = $envVars['CONFLUENCE_PAT_TOKEN']

    $result = & $ACTIONS[$Action] $baseUrl $token $ht
    Out-Result $true $result

} catch {
    Out-Result $false $_.Exception.Message
    exit 1
}
