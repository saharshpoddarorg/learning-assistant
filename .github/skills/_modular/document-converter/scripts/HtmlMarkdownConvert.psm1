<#
    Best-effort structural conversion between HTML and Markdown.

    This is a lightweight, regex-based converter — not a full CommonMark or HTML5
    parser. It targets the common structures found in typical exports (Notion, Google
    Keep/Takeout, Word's "Filtered HTML", Confluence exports): headings, bold/italic,
    lists, links, inline/fenced code, simple tables, and paragraphs. Deeply nested or
    unusual markup may not round-trip perfectly — that is an accepted trade-off for
    avoiding any external dependency (no pandoc, no HTML parser library).
#>

function ConvertTo-MarkdownFromHtml {
    param([Parameter(Mandatory = $true)][string]$Html)

    $text = $Html

    # Drop non-content blocks entirely.
    $text = [regex]::Replace($text, '<script[\s\S]*?</script>', '', 'IgnoreCase')
    $text = [regex]::Replace($text, '<style[\s\S]*?</style>', '', 'IgnoreCase')
    $text = [regex]::Replace($text, '<!--[\s\S]*?-->', '')

    # Fenced code blocks first, so inner text isn't touched by other rules.
    $text = [regex]::Replace($text, '<pre[^>]*>\s*<code[^>]*>([\s\S]*?)</code>\s*</pre>', {
            param($m)
            $code = [System.Net.WebUtility]::HtmlDecode($m.Groups[1].Value)
            $code = [regex]::Replace($code, '<[^>]+>', '')
            "`n```````n$code`n```````n"
        }, 'IgnoreCase')

    # Headings.
    for ($level = 6; $level -ge 1; $level--) {
        $hashes = '#' * $level
        $text = [regex]::Replace($text, "<h$level[^>]*>([\s\S]*?)</h$level>", "`n$hashes " + '$1' + "`n", 'IgnoreCase')
    }

    # Tables (best-effort: header row from first <tr>, rest as body rows).
    $text = [regex]::Replace($text, '<table[^>]*>([\s\S]*?)</table>', {
            param($m)
            $rows = [regex]::Matches($m.Groups[1].Value, '<tr[^>]*>([\s\S]*?)</tr>', 'IgnoreCase')
            if ($rows.Count -eq 0) { return '' }
            $lines = New-Object System.Collections.Generic.List[string]
            $rowIndex = 0
            foreach ($row in $rows) {
                $cells = [regex]::Matches($row.Groups[1].Value, '<t[dh][^>]*>([\s\S]*?)</t[dh]>', 'IgnoreCase')
                $cellText = @()
                foreach ($cell in $cells) {
                    $c = [regex]::Replace($cell.Groups[1].Value, '<[^>]+>', '').Trim()
                    $cellText += [System.Net.WebUtility]::HtmlDecode($c)
                }
                $lines.Add('| ' + ($cellText -join ' | ') + ' |')
                if ($rowIndex -eq 0) {
                    $lines.Add('|' + (' --- |' * $cellText.Count))
                }
                $rowIndex++
            }
            "`n" + ($lines -join "`n") + "`n"
        }, 'IgnoreCase')

    # Lists — unordered.
    $text = [regex]::Replace($text, '<ul[^>]*>([\s\S]*?)</ul>', {
            param($m)
            $items = [regex]::Matches($m.Groups[1].Value, '<li[^>]*>([\s\S]*?)</li>', 'IgnoreCase')
            $lines = foreach ($item in $items) { '- ' + ($item.Groups[1].Value.Trim()) }
            "`n" + ($lines -join "`n") + "`n"
        }, 'IgnoreCase')

    # Lists — ordered.
    $text = [regex]::Replace($text, '<ol[^>]*>([\s\S]*?)</ol>', {
            param($m)
            $items = [regex]::Matches($m.Groups[1].Value, '<li[^>]*>([\s\S]*?)</li>', 'IgnoreCase')
            $n = 0
            $lines = foreach ($item in $items) { $n++; "$n. " + ($item.Groups[1].Value.Trim()) }
            "`n" + ($lines -join "`n") + "`n"
        }, 'IgnoreCase')

    # Inline formatting.
    $text = [regex]::Replace($text, '<(strong|b)[^>]*>([\s\S]*?)</\1>', '**$2**', 'IgnoreCase')
    $text = [regex]::Replace($text, '<(em|i)[^>]*>([\s\S]*?)</\1>', '*$2*', 'IgnoreCase')
    $text = [regex]::Replace($text, '<code[^>]*>([\s\S]*?)</code>', '`$1`', 'IgnoreCase')
    $text = [regex]::Replace($text, '<a[^>]+href="([^"]*)"[^>]*>([\s\S]*?)</a>', '[$2]($1)', 'IgnoreCase')

    # Block/line breaks and paragraphs.
    $text = [regex]::Replace($text, '<br\s*/?>', "`n", 'IgnoreCase')
    $text = [regex]::Replace($text, '</p>|</div>', "`n`n", 'IgnoreCase')
    $text = [regex]::Replace($text, '<p[^>]*>|<div[^>]*>', '', 'IgnoreCase')

    # Strip anything left, decode entities, collapse blank lines.
    $text = [regex]::Replace($text, '<[^>]+>', '')
    $text = [System.Net.WebUtility]::HtmlDecode($text)
    $text = [regex]::Replace($text, '\n{3,}', "`n`n")
    $text = $text.Trim() + "`n"

    return $text
}

function ConvertTo-HtmlFromMarkdown {
    param([Parameter(Mandatory = $true)][string]$Markdown)

    $lines = $Markdown -split "`r?`n"
    $html = New-Object System.Collections.Generic.List[string]
    $inCodeBlock = $false
    $codeBuffer = New-Object System.Collections.Generic.List[string]
    $listBuffer = New-Object System.Collections.Generic.List[string]
    $listType = $null  # 'ul' or 'ol'

    function Convert-Inline([string]$s) {
        $s = [System.Net.WebUtility]::HtmlEncode($s)
        $s = [regex]::Replace($s, '\*\*(.+?)\*\*', '<strong>$1</strong>')
        $s = [regex]::Replace($s, '__(.+?)__', '<strong>$1</strong>')
        $s = [regex]::Replace($s, '(?<!\*)\*(?!\*)(.+?)(?<!\*)\*(?!\*)', '<em>$1</em>')
        $s = [regex]::Replace($s, '`([^`]+)`', '<code>$1</code>')
        $s = [regex]::Replace($s, '\[([^\]]+)\]\(([^)]+)\)', '<a href="$2">$1</a>')
        return $s
    }

    function Close-List {
        if ($listBuffer.Count -gt 0) {
            $html.Add("<$listType>")
            foreach ($item in $listBuffer) { $html.Add("<li>$(Convert-Inline $item)</li>") }
            $html.Add("</$listType>")
            $listBuffer.Clear()
        }
        $script:listType = $null
    }

    for ($i = 0; $i -lt $lines.Count; $i++) {
        $line = $lines[$i]

        if ($line -match '^\s*```') {
            if ($inCodeBlock) {
                $html.Add('<pre><code>' + [System.Net.WebUtility]::HtmlEncode(($codeBuffer -join "`n")) + '</code></pre>')
                $codeBuffer.Clear()
                $inCodeBlock = $false
            }
            else {
                Close-List
                $inCodeBlock = $true
            }
            continue
        }
        if ($inCodeBlock) { $codeBuffer.Add($line); continue }

        if ($line -match '^(#{1,6})\s+(.*)$') {
            Close-List
            $level = $Matches[1].Length
            $html.Add("<h$level>$(Convert-Inline $Matches[2])</h$level>")
            continue
        }

        # Pipe table: a row followed by a separator row (---/:--/--:) starts a table.
        $headerMatch = [regex]::Match($line, '^\s*\|(.+)\|\s*$')
        if ($headerMatch.Success -and ($i + 1) -lt $lines.Count -and
            $lines[$i + 1] -match '^\s*\|?[\s:|-]+\|?\s*$') {
            Close-List
            $headerCells = $headerMatch.Groups[1].Value -split '\|' | ForEach-Object { $_.Trim() }
            $headerHtml = ($headerCells | ForEach-Object { "<th>$(Convert-Inline $_)</th>" }) -join ''
            $html.Add("<table><tr>$headerHtml</tr>")
            $i++  # consume the separator row
            while (($i + 1) -lt $lines.Count) {
                $rowMatch = [regex]::Match($lines[$i + 1], '^\s*\|(.+)\|\s*$')
                if (-not $rowMatch.Success) { break }
                $i++
                $rowCells = $rowMatch.Groups[1].Value -split '\|' | ForEach-Object { $_.Trim() }
                $rowHtml = ($rowCells | ForEach-Object { "<td>$(Convert-Inline $_)</td>" }) -join ''
                $html.Add("<tr>$rowHtml</tr>")
            }
            $html.Add('</table>')
            continue
        }

        if ($line -match '^\s*[-*+]\s+(.*)$') {
            if ($listType -ne 'ul') { Close-List; $listType = 'ul' }
            $listBuffer.Add($Matches[1])
            continue
        }
        if ($line -match '^\s*\d+\.\s+(.*)$') {
            if ($listType -ne 'ol') { Close-List; $listType = 'ol' }
            $listBuffer.Add($Matches[1])
            continue
        }
        if ($line -match '^\s*$') {
            Close-List
            continue
        }
        Close-List
        $html.Add("<p>$(Convert-Inline $line)</p>")
    }
    Close-List

    return "<html><body>`n" + ($html -join "`n") + "`n</body></html>"
}

Export-ModuleMember -Function ConvertTo-MarkdownFromHtml, ConvertTo-HtmlFromMarkdown
