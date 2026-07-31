<#
.SYNOPSIS
  Jira CLI - PowerShell PAT-authenticated REST API client.
  Supports all Jira read and write operations.

.USAGE
  $env:CLI_JSON_ARGS = '{"issueKey":"PROJ-123"}'
  & "skills/_modular/jira/scripts/jira_cli.ps1" fetch_jira_issue
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

# ── JQL Normalize ──
function Normalize-Jql([string]$jql) {
    [regex]::Replace($jql, '\b(assignee|reporter)\s*(=|!=|~|!~)\s*([^\s()"'']+@[^\s()"'']+)', { param($m) "$($m.Groups[1].Value) $($m.Groups[2].Value) `"$($m.Groups[3].Value)`"" }, 'IgnoreCase')
}

# ═══════════════════════════════════════════════════════════════════════════════
# Action Aliases
# ═══════════════════════════════════════════════════════════════════════════════

$ALIASES = @{
    search_jira='search_jira_issues'; jira_search='search_jira_issues'
    get_jira_issue='fetch_jira_issue'; comment_jira='add_jira_comment'
    log_work='add_jira_worklog'; jira_worklog='add_jira_worklog'
    add_worklog='add_jira_worklog'; add_work_log='add_jira_worklog'
    jira_work_log='add_jira_worklog'; log_jira_work='add_jira_worklog'
    create_jira_subtask='create_jira_issue'; get_subtasks='get_jira_subtasks'
    jira_subtasks='get_jira_subtasks'; clone_issue='clone_jira_issue'
    link_issues='link_jira_issues'; get_issue_links='get_jira_issue_links'
    jira_changelog='get_jira_issue_changelog'; issue_changelog='get_jira_issue_changelog'
    jira_issue_types='get_jira_issue_types'; issue_types='get_jira_issue_types'
    jira_statuses='get_jira_statuses'; jira_components='get_jira_components'
    jira_versions='get_jira_versions'; find_jira_user='search_jira_users'
    jira_users='search_jira_users'; jira_myself='get_current_jira_user'
    current_user='get_current_jira_user'; epic_issues='get_epic_issues'
    jira_sprints='get_jira_sprints'; sprint_issues='get_sprint_issues'
}

# ═══════════════════════════════════════════════════════════════════════════════
# Jira Actions
# ═══════════════════════════════════════════════════════════════════════════════

$ACTIONS = @{
    # ── Read ──
    fetch_jira_issue         = { param($b,$t,$a) $f=$a.fields; if(-not $f){$f='summary,status,assignee,reporter,priority,issuetype,created,updated,description,comment,parent,issuelinks,subtasks,labels,components,fixVersions'}; Rest-Get $b $t "/rest/api/2/issue/$(Enc $a.issueKey)?fields=$f" }
    fetch_jira_comments      = { param($b,$t,$a) $m=if($a.maxResults){$a.maxResults}else{25}; Rest-Get $b $t "/rest/api/2/issue/$(Enc $a.issueKey)/comment?maxResults=$m&orderBy=-created" }
    search_jira_issues       = { param($b,$t,$a) $jql=Normalize-Jql $a.jql; $m=if($a.maxResults){$a.maxResults}else{25}; $s=if($a.startAt){$a.startAt}else{0}; $f=if($a.fields){$a.fields}else{'summary,status,assignee,priority,issuetype,created,updated,reporter,labels'}; Rest-Get $b $t "/rest/api/2/search?jql=$([System.Uri]::EscapeDataString($jql))&maxResults=$m&startAt=$s&fields=$f" }
    list_jira_issues_by_project = { param($b,$t,$a) $jql="project = $($a.projectKey) ORDER BY updated DESC"; $m=if($a.maxResults){$a.maxResults}else{25}; $s=if($a.startAt){$a.startAt}else{0}; Rest-Get $b $t "/rest/api/2/search?jql=$([System.Uri]::EscapeDataString($jql))&maxResults=$m&startAt=$s&fields=summary,status,assignee,priority,issuetype" }
    get_jira_issue_links     = { param($b,$t,$a) $d=Rest-Get $b $t "/rest/api/2/issue/$(Enc $a.issueKey)?fields=issuelinks"; @{issuelinks=@($d.fields.issuelinks)} }
    get_jira_subtasks        = { param($b,$t,$a) $jql="parent = $($a.issueKey) ORDER BY created ASC"; $m=if($a.maxResults){$a.maxResults}else{50}; Rest-Get $b $t "/rest/api/2/search?jql=$([System.Uri]::EscapeDataString($jql))&maxResults=$m&fields=summary,status,assignee,issuetype,priority" }
    get_jira_issue_changelog = { param($b,$t,$a) $m=if($a.maxResults){$a.maxResults}else{50}; Rest-Get $b $t "/rest/api/2/issue/$(Enc $a.issueKey)?expand=changelog&fields=summary&maxResults=$m" }
    get_jira_issue_types     = { param($b,$t,$a) if($a.projectKey){$p=Rest-Get $b $t "/rest/api/2/project/$(Enc $a.projectKey)";@{issueTypes=$p.issueTypes}}else{Rest-Get $b $t '/rest/api/2/issuetype'} }
    get_jira_statuses        = { param($b,$t,$a) if($a.projectKey){Rest-Get $b $t "/rest/api/2/project/$(Enc $a.projectKey)/statuses"}else{Rest-Get $b $t '/rest/api/2/status'} }
    get_jira_components      = { param($b,$t,$a) Rest-Get $b $t "/rest/api/2/project/$(Enc $a.projectKey)/components" }
    get_jira_versions        = { param($b,$t,$a) Rest-Get $b $t "/rest/api/2/project/$(Enc $a.projectKey)/versions" }
    search_jira_users        = { param($b,$t,$a) $m=if($a.maxResults){$a.maxResults}else{20}; Rest-Get $b $t "/rest/api/2/user/search?username=$(Enc $a.query)&maxResults=$m" }
    get_current_jira_user    = { param($b,$t,$a) Rest-Get $b $t '/rest/api/2/myself' }
    get_epic_issues          = { param($b,$t,$a) $jql="`"Epic Link`" = $($a.epicKey) ORDER BY rank ASC"; $m=if($a.maxResults){$a.maxResults}else{50}; Rest-Get $b $t "/rest/api/2/search?jql=$([System.Uri]::EscapeDataString($jql))&maxResults=$m&fields=summary,status,assignee,issuetype,priority" }
    get_jira_sprints         = { param($b,$t,$a) $st=if($a.state){$a.state}else{'active,future'}; Rest-Get $b $t "/rest/agile/1.0/board/$($a.boardId)/sprint?state=$st" }
    get_sprint_issues        = { param($b,$t,$a) $m=if($a.maxResults){$a.maxResults}else{50}; Rest-Get $b $t "/rest/agile/1.0/sprint/$($a.sprintId)/issue?maxResults=$m&fields=summary,status,assignee,issuetype,priority" }
    get_jira_watchers        = { param($b,$t,$a) Rest-Get $b $t "/rest/api/2/issue/$(Enc $a.issueKey)/watchers" }

    # ── Write ──
    create_jira_issue = { param($b,$t,$a)
        $fields = @{ project=@{key=$a.projectKey}; issuetype=@{name=$a.issueType}; summary=$a.summary }
        if($a.description){$fields.description=$a.description}
        if($a.priority){$fields.priority=@{name=$a.priority}}
        if($a.assignee){$fields.assignee=@{name=$a.assignee}}
        if($a.labels){$fields.labels=$a.labels}
        if($a.components){$fields.components=@($a.components|ForEach-Object{@{name=$_}})}
        if($a.parentKey){$fields.parent=@{key=$a.parentKey}}
        Rest-Post $b $t '/rest/api/2/issue' @{fields=$fields}
    }
    update_jira_issue = { param($b,$t,$a)
        $fields = @{}
        if($a.summary){$fields.summary=$a.summary}
        if($a.description){$fields.description=$a.description}
        if($a.priority){$fields.priority=@{name=$a.priority}}
        if($a.assignee){$fields.assignee=@{name=$a.assignee}}
        if($a.labels){$fields.labels=$a.labels}
        Rest-Put $b $t "/rest/api/2/issue/$(Enc $a.issueKey)" @{fields=$fields}
    }
    transition_jira_issue = { param($b,$t,$a)
        $tr = Rest-Get $b $t "/rest/api/2/issue/$(Enc $a.issueKey)/transitions"
        $match = $tr.transitions | Where-Object { $_.name -ieq $a.transition } | Select-Object -First 1
        if(-not $match){ $avail=($tr.transitions|ForEach-Object{$_.name}) -join ', '; throw "Transition `"$($a.transition)`" not found. Available: $avail" }
        Rest-Post $b $t "/rest/api/2/issue/$(Enc $a.issueKey)/transitions" @{transition=@{id=$match.id}}
    }
    add_jira_comment   = { param($b,$t,$a) Rest-Post $b $t "/rest/api/2/issue/$(Enc $a.issueKey)/comment" @{body=$a.comment} }
    add_jira_worklog   = { param($b,$t,$a) $p=@{timeSpent=$a.timeSpent}; if($a.comment){$p.comment=$a.comment}; if($a.started){$p.started=$a.started}; Rest-Post $b $t "/rest/api/2/issue/$(Enc $a.issueKey)/worklog" $p }
    delete_jira_issue  = { param($b,$t,$a) Rest-Delete $b $t "/rest/api/2/issue/$(Enc $a.issueKey)" }
    link_jira_issues   = { param($b,$t,$a) $lt=if($a.linkType){$a.linkType}else{'Relates'}; Rest-Post $b $t '/rest/api/2/issueLink' @{type=@{name=$lt};inwardIssue=@{key=$a.inwardIssueKey};outwardIssue=@{key=$a.outwardIssueKey}} }
    clone_jira_issue   = { param($b,$t,$a)
        $src = Rest-Get $b $t "/rest/api/2/issue/$(Enc $a.issueKey)?fields=summary,description,issuetype,priority,labels,components,assignee,project"
        $f = $src.fields; $fields = @{project=@{key=$f.project.key};issuetype=@{name=$f.issuetype.name};summary=if($a.summary){$a.summary}else{"[Clone] $($f.summary)"};description=if($f.description){$f.description}else{''}}
        if($f.priority){$fields.priority=@{name=$f.priority.name}}
        if($f.labels.Count){$fields.labels=$f.labels}
        if($f.assignee){$fields.assignee=@{name=$f.assignee.name}}
        Rest-Post $b $t '/rest/api/2/issue' @{fields=$fields}
    }
    bulk_create_jira_issues = { param($b,$t,$a)
        $results = @()
        foreach($issue in $a.issues){
            try{
                $fields=@{project=@{key=$issue.projectKey};issuetype=@{name=$issue.issueType};summary=$issue.summary}
                if($issue.description){$fields.description=$issue.description}
                if($issue.priority){$fields.priority=@{name=$issue.priority}}
                if($issue.assignee){$fields.assignee=@{name=$issue.assignee}}
                if($issue.labels){$fields.labels=$issue.labels}
                if($issue.parentKey){$fields.parent=@{key=$issue.parentKey}}
                $r=Rest-Post $b $t '/rest/api/2/issue' @{fields=$fields}
                $results+=@{success=$true;key=$r.key;id=$r.id;summary=$issue.summary}
            }catch{ $results+=@{success=$false;summary=$issue.summary;error=$_.Exception.Message} }
        }
        @{created=@($results|Where-Object{$_.success}).Count;failed=@($results|Where-Object{-not $_.success}).Count;results=$results}
    }
    bulk_transition_jira_issues = { param($b,$t,$a)
        $results=@()
        foreach($key in $a.issueKeys){
            try{
                $tr=Rest-Get $b $t "/rest/api/2/issue/$(Enc $key)/transitions"
                $match=$tr.transitions|Where-Object{$_.name -ieq $a.transition}|Select-Object -First 1
                if(-not $match){$results+=@{success=$false;key=$key;error="Transition not available"};continue}
                Rest-Post $b $t "/rest/api/2/issue/$(Enc $key)/transitions" @{transition=@{id=$match.id}}
                $results+=@{success=$true;key=$key;transition=$a.transition}
            }catch{$results+=@{success=$false;key=$key;error=$_.Exception.Message}}
        }
        @{transitioned=@($results|Where-Object{$_.success}).Count;failed=@($results|Where-Object{-not $_.success}).Count;results=$results}
    }
    add_jira_labels    = { param($b,$t,$a) Rest-Put $b $t "/rest/api/2/issue/$(Enc $a.issueKey)" @{update=@{labels=@($a.labels|ForEach-Object{@{add=$_}})}} }
    remove_jira_labels = { param($b,$t,$a) Rest-Put $b $t "/rest/api/2/issue/$(Enc $a.issueKey)" @{update=@{labels=@($a.labels|ForEach-Object{@{remove=$_}})}} }
    add_jira_watcher   = { param($b,$t,$a)
        $uri="$($b.TrimEnd('/'))/rest/api/2/issue/$(Enc $a.issueKey)/watchers"
        $bodyBytes=[System.Text.Encoding]::UTF8.GetBytes(('"'+$a.username+'"'))
        Invoke-RawWeb @{Uri=$uri;Method='POST';Headers=@{Authorization="Bearer $t";Accept='application/json';'X-Atlassian-Token'='no-check'};Body=$bodyBytes;ContentType='application/json; charset=utf-8';UseBasicParsing=$true} | Out-Null
        @{added=$a.username}
    }
    remove_jira_watcher = { param($b,$t,$a) Rest-Delete $b $t "/rest/api/2/issue/$(Enc $a.issueKey)/watchers?username=$(Enc $a.username)" }
    add_issues_to_epic = { param($b,$t,$a)
        $results=@()
        foreach($key in $a.issueKeys){
            try{ Rest-Put $b $t "/rest/api/2/issue/$(Enc $key)" @{fields=@{customfield_10008=$a.epicKey}}; $results+=@{success=$true;key=$key} }
            catch{ try{ Rest-Put $b $t "/rest/api/2/issue/$(Enc $key)" @{fields=@{'Epic Link'=$a.epicKey}}; $results+=@{success=$true;key=$key} }catch{ $results+=@{success=$false;key=$key;error=$_.Exception.Message} } }
        }
        @{results=$results}
    }
    move_to_sprint  = { param($b,$t,$a) Rest-Post $b $t "/rest/agile/1.0/sprint/$($a.sprintId)/issue" @{issues=$a.issueKeys} }
    move_to_backlog = { param($b,$t,$a) Rest-Post $b $t '/rest/agile/1.0/backlog/issue' @{issues=$a.issueKeys} }
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
        Out-Result $false 'Usage: jira_cli.ps1 <action>. Set $env:CLI_JSON_ARGS with JSON arguments.'
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
    if (-not $envVars['JIRA_PAT_TOKEN']) { Out-Result $false 'Missing JIRA_PAT_TOKEN in .env file.'; exit 1 }
    if (-not $envVars['JIRA_BASE_URL'])  { Out-Result $false 'Missing JIRA_BASE_URL in .env file.'; exit 1 }

    $baseUrl = $envVars['JIRA_BASE_URL']
    $token   = $envVars['JIRA_PAT_TOKEN']

    $result = & $ACTIONS[$Action] $baseUrl $token $ht
    Out-Result $true $result

} catch {
    Out-Result $false $_.Exception.Message
    exit 1
}
