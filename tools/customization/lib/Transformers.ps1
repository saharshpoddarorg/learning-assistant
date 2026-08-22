Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Split-Frontmatter {
    param([string]$Content)

    if ($Content -notmatch "(?s)^---\r?\n(.*?)\r?\n---\r?\n(.*)$") {
        return @{ Frontmatter = ""; Body = $Content }
    }

    return @{
        Frontmatter = $Matches[1]
        Body = $Matches[2]
    }
}

function Convert-ForClaudeFamily {
    param([string]$Content)

    $parts = Split-Frontmatter -Content $Content
    if ([string]::IsNullOrWhiteSpace($parts.Frontmatter)) {
        return $parts.Body
    }

    $applyTo = ""
    foreach ($line in ($parts.Frontmatter -split "`r?`n")) {
        if ($line -match "^\s*applyTo\s*:\s*(.+)$") {
            $applyTo = $Matches[1].Trim().Trim('"')
        }
    }

    $meta = "<claude-config>`n<applies-to>$applyTo</applies-to>`n</claude-config>`n`n"
    return $meta + $parts.Body
}

function Convert-ForGemini {
    param(
        [string]$Content,
        [string]$RelativePath
    )

    $parts = Split-Frontmatter -Content $Content
    $payload = @{
        id = ($RelativePath -replace "[/.]", "-").ToLowerInvariant()
        sourcePath = $RelativePath
        content = $parts.Body
    }

    return $payload | ConvertTo-Json -Depth 5
}

function Convert-ForChatGpt {
    param([string]$Content)

    $parts = Split-Frontmatter -Content $Content
    return $parts.Body
}

function Convert-ForAntigravity {
    param(
        [string]$Content,
        [string]$RelativePath
    )

    $parts = Split-Frontmatter -Content $Content
    $payload = @{
        schema = "antigravity-customization-v1"
        sourcePath = $RelativePath
        body = $parts.Body
    }

    return $payload | ConvertTo-Json -Depth 5
}

function Convert-ForProfile {
    param(
        [string]$ProfileKey,
        [string]$Content,
        [string]$RelativePath
    )

    switch ($ProfileKey) {
        "github-copilot" { return $Content }
        "claude-family" { return Convert-ForClaudeFamily -Content $Content }
        "gemini" { return Convert-ForGemini -Content $Content -RelativePath $RelativePath }
        "chatgpt" { return Convert-ForChatGpt -Content $Content }
        "antigravity" { return Convert-ForAntigravity -Content $Content -RelativePath $RelativePath }
        default { throw "Unknown profile key: $ProfileKey" }
    }
}
