# __md_lint.ps1 — Markdown linter and auto-fixer for the learning-assistant repo
#
# Usage:
#   .\__md_lint.ps1                         Fix all .md files (default)
#   .\__md_lint.ps1 -Check                  Report only, no writes
#   .\__md_lint.ps1 -Target "README.md"     Fix/check a single file
#   .\__md_lint.ps1 -Target "README.md" -Check
#
# Exit codes:
#   0  All auto-fixable issues resolved; no remaining issues
#   1  Issues remain that need human attention

[CmdletBinding()]
param(
    [string]$Root = $PSScriptRoot,
    [string]$Target = '',
    [switch]$Check
)

# ─── Helpers ──────────────────────────────────────────────────────────────────

function Get-FenceLang {
    param([string[]]$ContentLines)
    # Guess language from content of a code block (first non-empty line)
    $first = ($ContentLines | Where-Object { $_.Trim() -ne '' } | Select-Object -First 1)
    if (-not $first) { return 'text' }
    $f = $first.Trim()

    # PowerShell
    if ($f -match '^(Get-|Set-|Write-|Read-|New-|Remove-|Test-|Import-|Export-|Invoke-|Select-|Where-|ForEach-|Sort-|Format-)' `
        -or $f -match '^(\$\w|powershell|pwsh|\.\\)' `
        -or $f -match 'Bypass|ExecutionPolicy|-NoProfile|-File \.\\') { return 'powershell' }

    # Java
    if ($f -match '^(public |private |protected |import |package |class |interface |enum |@|/\*\*?|//.*\w)' `
        -or $f -match '^(final |static |abstract |void |boolean |int |String |List |Map |var )') { return 'java' }

    # Bash / shell
    if ($f -match '^(#!.*sh|export |source |alias |chmod |curl |wget |apt |brew |sudo |yum |dnf )' `
        -or $f -match '^(cd |ls |mkdir |rm |cp |mv |cat |grep |find |echo |./|git |docker |kubectl |helm )') { return 'bash' }

    # JSON
    if ($f -match '^\s*\{' -or $f -match '^\s*"[^"]+"\s*:') { return 'json' }

    # YAML
    if ($f -match '^---$' -or ($f -match '^\w[\w-]*:(\s|$)' -and $f -notmatch '[{}\[\]]')) { return 'yaml' }

    # Properties / config
    if ($f -match '^\w[\w.-]*\s*=' -and $f -notmatch '[{}\[\]()]') { return 'properties' }

    # SQL
    if ($f -match '^(SELECT|INSERT|UPDATE|DELETE|CREATE|DROP|ALTER|WITH)\b') { return 'sql' }

    # Markdown (nested)
    if ($f -match '^#+ ' -or $f -match '^\|.+\|') { return 'markdown' }

    # XML / HTML
    if ($f -match '^<[a-zA-Z!]') { return 'xml' }

    # Default: plain text / file tree / command output
    return 'text'
}

function Write-Issue {
    param([string]$Path, [int]$Line, [string]$Kind, [string]$Detail)
    $rel = $Path.Substring($Root.Length).TrimStart('\', '/')
    Write-Host ("  [ISSUE] {0}:{1}  {2}  -- {3}" -f $rel, $Line, $Kind, $Detail) -ForegroundColor Yellow
}

function Write-Fixed {
    param([string]$Path)
    $rel = $Path.Substring($Root.Length).TrimStart('\', '/')
    Write-Host ("  [FIXED] {0}" -f $rel) -ForegroundColor Green
}

# ─── Per-file processing ──────────────────────────────────────────────────────

function Invoke-LintFile {
    param([string]$FilePath)

    $raw = [System.IO.File]::ReadAllText($FilePath)
    $raw = $raw -replace "`r`n", "`n" -replace "`r", "`n"
    $original = $raw

    $lines       = $raw -split "`n"
    $manualIssues = [System.Collections.Generic.List[string]]::new()

    # ── Pass 1: auto-fixable transforms ─────────────────────────────────────

    # 1. Strip trailing whitespace
    $lines = $lines | ForEach-Object { $_.TrimEnd() }

    # 2. Insert blank lines around headings and code fences
    $rebuilt   = [System.Collections.Generic.List[string]]::new()
    $inFence   = $false
    $fenceDepth = 0

    for ($i = 0; $i -lt $lines.Count; $i++) {
        $cur = $lines[$i]
        $isFence = $cur -match '^\s*(`{3,}|~{3,})'

        if ($isFence) {
            $tickCount = ($Matches[1]).Length
            # GFM spec: closing fence must have NO info string (bare ticks only)
            $hasInfoStr = ($cur -replace '^\s*[`~]+', '').Trim() -ne ''
            if (-not $inFence) {
                # opening fence
                $inFence    = $true
                $fenceDepth = $tickCount
                # blank before opening fence
                if ($rebuilt.Count -gt 0 -and $rebuilt[$rebuilt.Count - 1] -ne '') {
                    $rebuilt.Add('')
                }
                # Auto-add language tag if missing (only bare opening fences)
                if ($cur -match '^(\s*`{3,})\s*$') {
                    # Gather upcoming lines to guess language
                    $upcoming = @()
                    for ($j = $i + 1; $j -lt [Math]::Min($i + 6, $lines.Count); $j++) {
                        if ($lines[$j] -match '^\s*`{3,}') { break }
                        $upcoming += $lines[$j]
                    }
                    $lang = Get-FenceLang -ContentLines $upcoming
                    $rebuilt.Add(($cur -replace '```\s*$', ("``````" + $lang)))
                } else {
                    $rebuilt.Add($cur)
                }
            } elseif ($tickCount -ge $fenceDepth -and -not $hasInfoStr) {
                # closing fence: same/higher depth AND bare (no info string per GFM spec)
                $inFence = $false
                $rebuilt.Add($cur)
                # blank after closing fence
                if (($i + 1) -lt $lines.Count -and $lines[$i + 1].TrimEnd() -ne '') {
                    $rebuilt.Add('')
                }
            } else {
                # Fence-like line inside outer fence (literal content or lower-depth nested)
                $rebuilt.Add($cur)
            }
        } elseif ((-not $inFence) -and ($cur -match '^(#{1,6}) ')) {
            # heading
            if ($rebuilt.Count -gt 0 -and $rebuilt[$rebuilt.Count - 1] -ne '') {
                $rebuilt.Add('')
            }
            $rebuilt.Add($cur)
            if (($i + 1) -lt $lines.Count -and $lines[$i + 1].TrimEnd() -ne '') {
                $rebuilt.Add('')
            }
        } else {
            $rebuilt.Add($cur)
        }
    }

    # 3. Collapse 3+ consecutive blank lines → 1
    $collapsed = [System.Collections.Generic.List[string]]::new()
    $blankRun  = 0
    foreach ($line in $rebuilt) {
        if ($line -eq '') {
            $blankRun++
            if ($blankRun -le 1) { $collapsed.Add($line) }
        } else {
            $blankRun = 0
            $collapsed.Add($line)
        }
    }

    # 4. Strip trailing blank lines; ensure single trailing newline
    while ($collapsed.Count -gt 0 -and $collapsed[$collapsed.Count - 1] -eq '') {
        $collapsed.RemoveAt($collapsed.Count - 1)
    }

    $newContent = ($collapsed -join "`n") + "`n"

    # Write fixed content
    if (-not $Check) {
        if ($newContent -ne $original) {
            [System.IO.File]::WriteAllText($FilePath, $newContent, (New-Object System.Text.UTF8Encoding $false))
            Write-Fixed -Path $FilePath
        }
    }

    # ── Pass 2: static analysis for issues that need human judgment ──────────

    $checkLines  = ($newContent -split "`n")
    $inFence2    = $false
    $fenceDepth2 = 0
    $h1Count     = 0
    $prevLevel   = 0
    $inList      = $false
    $listMarker  = ''

    for ($i = 0; $i -lt $checkLines.Count; $i++) {
        $line = $checkLines[$i]
        $lineNum = $i + 1

        $isFence2 = $line -match '^\s*(`{3,}|~{3,})'
        if ($isFence2) {
            $tickCount2  = ($Matches[1]).Length
            $hasInfo2    = ($line -replace '^\s*[`~]+', '').Trim() -ne ''
            if (-not $inFence2) {
                $inFence2    = $true
                $fenceDepth2 = $tickCount2
            } elseif ($tickCount2 -ge $fenceDepth2 -and -not $hasInfo2) {
                # GFM spec: closing fence must have no info string
                $inFence2 = $false
            }
        }

        if ($inFence2) { $inList = $false; $listMarker = ''; continue }

        # Heading checks
        if ($line -match '^(#{1,6}) ') {
            $level = $Matches[1].Length
            if ($level -eq 1) { $h1Count++ }

            # Level skip
            if ($prevLevel -gt 0 -and $level -gt ($prevLevel + 1)) {
                $manualIssues.Add(("L{0}: Heading level skip H{1}→H{2}: {3}" -f $lineNum, $prevLevel, $level, $line.Trim()))
            }
            $prevLevel = $level
            $inList    = $false
            $listMarker = ''
        }

        # (Code fence language tag is auto-fixed in pass 1 — no check needed here)

        # Mixed list markers
        if ($line -match '^(\s*)([-*+]) ') {
            $marker = $Matches[2]
            if (-not $inList) {
                $inList     = $true
                $listMarker = $marker
            } elseif ($marker -ne $listMarker -and $marker -in @('-', '*', '+')) {
                $manualIssues.Add(("L{0}: Mixed list marker '{1}' (block started with '{2}')" -f $lineNum, $marker, $listMarker))
            }
        } elseif ($line.Trim() -eq '') {
            $inList     = $false
            $listMarker = ''
        }

        # Non-sequential ordered list
        if ($line -match '^\s*1\. ' -and $i -gt 0 -and $checkLines[$i - 1] -match '^\s*1\. ') {
            $manualIssues.Add(("L{0}: Ordered list uses '1.' instead of sequential numbering" -f $lineNum))
        }

# Broken link: [text] (url) -- space before ( where content looks like a link destination
        if ($line -match '\]\s+\((https?://|#[\w-]|\.\.?/|/)') {
                $manualIssues.Add(("L{0}: Broken link -- space between ']' and '(': {1}" -f $lineNum, $line.Trim()))
        }

        # Table: only flag the FIRST row of a table (prev line is not a pipe row)
        # when the next row is not a separator
        if ($line -match '^\|' -and ($i + 1) -lt $checkLines.Count) {
            $prev = if ($i -gt 0) { $checkLines[$i - 1] } else { '' }
            if (-not ($prev -match '^\|')) {
                # This is the opening row of a table
                $next = $checkLines[$i + 1]
                if (-not ($next -match '^\|\s*[-:|]+\s*\|')) {
                    $manualIssues.Add(("L{0}: Table header row not followed by separator (|---|) row" -f $lineNum))
                }
            }
        }
    }

    # Multiple H1
    # Exempt: .github/prompts/*.prompt.md  — template docs, each H1 is a document template title
    # Exempt: *-deck.md / *-slide*.md      — slide decks where each H1 is a slide section
    $isH1Exempt = ($FilePath -match '[/\\]\.github[/\\]prompts[/\\]') -or
                  ($FilePath -match '-deck\.md$') -or
                  ($FilePath -match '-slides?\.md$')
    if ($h1Count -gt 1 -and -not $isH1Exempt) {
        $manualIssues.Add(("{0} H1 headings found (only 1 allowed)" -f $h1Count))
    }

    return $manualIssues
}

# ─── Main ─────────────────────────────────────────────────────────────────────

$excludePatterns = @('[/\\]\.git[/\\]', '[/\\]out[/\\]', '__md_lint\.ps1')

if ($Target -ne '') {
    # Single-file mode
    $target = Join-Path $Root $Target
    if (-not (Test-Path $target)) {
        Write-Host "File not found: $target" -ForegroundColor Red
        exit 1
    }
    $mdFiles = @(Get-Item $target)
} else {
    $mdFiles = Get-ChildItem -Path $Root -Recurse -Filter '*.md' | Where-Object {
        $p = $_.FullName
        -not ($excludePatterns | Where-Object { $p -match $_ })
    }
}

$totalFiles    = 0
$remainingIssues = [System.Collections.Generic.List[string]]::new()

Write-Host ""
if ($Check) {
    Write-Host "  Mode: CHECK ONLY (no files will be written)" -ForegroundColor Cyan
} else {
    Write-Host "  Mode: FIX + CHECK" -ForegroundColor Cyan
}
Write-Host ""

foreach ($f in $mdFiles) {
    $totalFiles++
    $issues = Invoke-LintFile -FilePath $f.FullName
    foreach ($iss in $issues) {
        $rel = $f.FullName.Substring($Root.Length).TrimStart('\', '/')
        $remainingIssues.Add(("{0}: {1}" -f $rel, $iss))
    }
}

Write-Host ""
Write-Host ("  Scanned {0} files." -f $totalFiles)

if ($remainingIssues.Count -eq 0) {
    Write-Host "  No manual-review issues found." -ForegroundColor Green
    Write-Host ""
    exit 0
} else {
    Write-Host ("  {0} issue(s) need human attention:" -f $remainingIssues.Count) -ForegroundColor Yellow
    Write-Host ""
    foreach ($iss in $remainingIssues) {
        Write-Host ("    {0}" -f $iss) -ForegroundColor Yellow
    }
    Write-Host ""
    exit 1
}
