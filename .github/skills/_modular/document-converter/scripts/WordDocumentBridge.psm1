<#
    Bridges Word's Document object model directly to/from Markdown, without going
    through Word's "Filtered HTML" export. Word's own HTML export wraps content in noisy
    MSO conditional markup and renders bullets as symbol-font characters, which breaks a
    lightweight regex bridge (bold markers land in the wrong place, bullets become
    garbage characters). Reading/writing the Document object's paragraphs, styles, list
    formatting, and run-level Bold/Italic directly is far more reliable and preserves
    more structure.

    Used for any format Word can open (.docx/.doc/.rtf/.pdf/.html/.htm/.txt) <-> Markdown.
#>

function ConvertFrom-WordDocumentToMarkdown {
    param([Parameter(Mandatory = $true)]$Document)

    $wdWithInTable = 12

    # Collect paragraphs (excluding ones that live inside a table — those are walked
    # via the table itself, below) and tables, then interleave by document position.
    $blocks = New-Object System.Collections.Generic.List[psobject]

    foreach ($para in $Document.Paragraphs) {
        if ($para.Range.Information($wdWithInTable)) { continue }
        $blocks.Add([pscustomobject]@{ Start = $para.Range.Start; Kind = 'para'; Item = $para })
    }
    foreach ($tbl in $Document.Tables) {
        $blocks.Add([pscustomobject]@{ Start = $tbl.Range.Start; Kind = 'table'; Item = $tbl })
    }

    $ordered = $blocks | Sort-Object Start
    $lines = New-Object System.Collections.Generic.List[string]

    foreach ($block in $ordered) {
        if ($block.Kind -eq 'para') {
            $lines.Add((Convert-WordParagraphToMarkdownLine $block.Item))
        }
        else {
            foreach ($tableLine in (Convert-WordTableToMarkdownLines $block.Item)) { $lines.Add($tableLine) }
        }
    }

    # Ensure headings and tables get a surrounding blank line, then collapse extra blanks.
    $withSpacing = New-Object System.Collections.Generic.List[string]
    for ($i = 0; $i -lt $lines.Count; $i++) {
        $isHeading = $lines[$i] -match '^#{1,6}\s'
        if ($isHeading -and $withSpacing.Count -gt 0 -and $withSpacing[$withSpacing.Count - 1] -ne '') {
            $withSpacing.Add('')
        }
        $withSpacing.Add($lines[$i])
        if ($isHeading) { $withSpacing.Add('') }
    }

    $text = ($withSpacing -join "`n")
    $text = [regex]::Replace($text, '\n{3,}', "`n`n")
    return $text.Trim() + "`n"
}

function Convert-WordParagraphToMarkdownLine {
    param([Parameter(Mandatory = $true)]$Paragraph)

    $range = $Paragraph.Range
    $rawText = ($range.Text -replace '[\r\a\x0b\f]+$', '')
    if ($rawText.Trim() -eq '') { return '' }

    $line = Get-InlineMarkdownFromRange -Range $range

    $headingLevel = 0
    if ($range.Style.NameLocal -match '^Heading (\d)$') { $headingLevel = [int]$Matches[1] }
    if ($headingLevel -gt 0) {
        return ('#' * $headingLevel) + ' ' + $line
    }

    $listString = $range.ListFormat.ListString
    if ($listString) {
        if ($listString -match '^\s*(\d+)[\.\)]') { return "$($Matches[1]). $line" }
        return "- $line"
    }

    return $line
}

function Get-InlineMarkdownFromRange {
    param([Parameter(Mandatory = $true)]$Range)

    $sb = New-Object System.Text.StringBuilder
    $curBold = $false
    $curItalic = $false
    $buffer = ""

    function Write-Run([string]$Text, [bool]$Bold, [bool]$Italic) {
        if ($Text -eq "") { return "" }
        # Keep leading/trailing whitespace OUTSIDE the emphasis markers — Word's Words
        # collection often attaches a trailing space to the last formatted word, and
        # markers wrapped around it (e.g. "**bold **") would render incorrectly in MD.
        if ($Text -match '^(\s*)(.*?)(\s*)$' -and $Matches[2] -ne '') {
            $core = $Matches[2]
            if ($Bold) { $core = "**$core**" }
            if ($Italic) { $core = "*$core*" }
            return $Matches[1] + $core + $Matches[3]
        }
        return $Text
    }

    foreach ($word in $Range.Words) {
        $isBold = [bool]$word.Font.Bold
        $isItalic = [bool]$word.Font.Italic
        if (($isBold -ne $curBold -or $isItalic -ne $curItalic) -and $buffer -ne "") {
            [void]$sb.Append((Write-Run $buffer $curBold $curItalic))
            $buffer = ""
        }
        $curBold = $isBold
        $curItalic = $isItalic
        $buffer += $word.Text
    }
    if ($buffer -ne "") {
        [void]$sb.Append((Write-Run $buffer $curBold $curItalic))
    }

    return $sb.ToString().Trim()
}

function Convert-WordTableToMarkdownLines {
    param([Parameter(Mandatory = $true)]$Table)

    $lines = New-Object System.Collections.Generic.List[string]
    $rowIndex = 0
    foreach ($row in $Table.Rows) {
        $cellText = foreach ($cell in $row.Cells) {
            ($cell.Range.Text -replace '[\r\a\x0b\f\t]+$', '').Trim()
        }
        $lines.Add('| ' + ($cellText -join ' | ') + ' |')
        if ($rowIndex -eq 0) {
            $lines.Add('|' + (' --- |' * $cellText.Count))
        }
        $rowIndex++
    }
    return $lines
}

function New-WordDocumentFromMarkdown {
    param(
        [Parameter(Mandatory = $true)]$Word,
        [Parameter(Mandatory = $true)][string]$Markdown
    )

    $doc = $Word.Documents.Add()
    $sel = $Word.Selection
    $lines = $Markdown -split "`r?`n"
    $inCodeBlock = $false
    $currentListType = $null  # $null, 'ul', 'ol' — tracks state across lines
    $i = 0

    while ($i -lt $lines.Count) {
        $line = $lines[$i]

        if ($line -match '^\s*```') {
            $inCodeBlock = -not $inCodeBlock
            $i++
            continue
        }
        if ($inCodeBlock) {
            $sel.Style = $doc.Styles.Item("Normal")
            $sel.Font.Name = "Consolas"
            $sel.TypeText($line)
            $sel.TypeParagraph()
            $sel.Font.Name = "Calibri"
            $i++
            continue
        }

        if ($line -match '^(#{1,6})\s+(.*)$') {
            $level = $Matches[1].Length
            $sel.Style = $doc.Styles.Item("Heading $level")
            Add-InlineMarkdownRun -Selection $sel -Text $Matches[2]
            $sel.TypeParagraph()
            $sel.Style = $doc.Styles.Item("Normal")
            $i++
            continue
        }

        # Pipe table: header row + separator row + body rows.
        $headerMatch = [regex]::Match($line, '^\s*\|(.+)\|\s*$')
        if ($headerMatch.Success -and ($i + 1) -lt $lines.Count -and $lines[$i + 1] -match '^\s*\|?[\s:|-]+\|?\s*$') {
            $tableRows = New-Object System.Collections.Generic.List[string[]]
            $tableRows.Add(($headerMatch.Groups[1].Value -split '\|' | ForEach-Object { $_.Trim() }))
            $i += 2  # header + separator
            while ($i -lt $lines.Count) {
                $rowMatch = [regex]::Match($lines[$i], '^\s*\|(.+)\|\s*$')
                if (-not $rowMatch.Success) { break }
                $tableRows.Add(($rowMatch.Groups[1].Value -split '\|' | ForEach-Object { $_.Trim() }))
                $i++
            }
            Add-WordTable -Document $doc -Selection $sel -Rows $tableRows
            continue
        }

        if ($line -match '^\s*[-*+]\s+(.*)$') {
            # ApplyBulletDefault() toggles bullets off if called again on an already-
            # bulleted paragraph, so only apply it when transitioning INTO a bullet list.
            if ($currentListType -ne 'ul') { $sel.Range.ListFormat.ApplyBulletDefault() }
            $currentListType = 'ul'
            Add-InlineMarkdownRun -Selection $sel -Text $Matches[1]
            $sel.TypeParagraph()
            $i++
            continue
        }
        if ($line -match '^\s*\d+\.\s+(.*)$') {
            if ($currentListType -ne 'ol') { $sel.Range.ListFormat.ApplyNumberDefault() }
            $currentListType = 'ol'
            Add-InlineMarkdownRun -Selection $sel -Text $Matches[1]
            $sel.TypeParagraph()
            $i++
            continue
        }
        if ($line -match '^\s*$') {
            if ($currentListType) { $sel.Range.ListFormat.RemoveNumbers() }
            $currentListType = $null
            $i++
            continue
        }

        if ($currentListType) { $sel.Range.ListFormat.RemoveNumbers() }
        $currentListType = $null
        Add-InlineMarkdownRun -Selection $sel -Text $line
        $sel.TypeParagraph()
        $i++
    }

    return $doc
}

function Add-InlineMarkdownRun {
    param($Selection, [string]$Text)

    # Tokenize on **bold**, *italic*, and `code` spans and toggle formatting per span.
    $pattern = '\*\*(.+?)\*\*|\*(.+?)\*|`([^`]+)`|([^*`]+)'
    foreach ($m in [regex]::Matches($Text, $pattern)) {
        if ($m.Groups[1].Success) {
            $Selection.Font.Bold = 1
            $Selection.TypeText($m.Groups[1].Value)
            $Selection.Font.Bold = 0
        }
        elseif ($m.Groups[2].Success) {
            $Selection.Font.Italic = 1
            $Selection.TypeText($m.Groups[2].Value)
            $Selection.Font.Italic = 0
        }
        elseif ($m.Groups[3].Success) {
            $Selection.Font.Name = "Consolas"
            $Selection.TypeText($m.Groups[3].Value)
            $Selection.Font.Name = "Calibri"
        }
        else {
            $Selection.TypeText($m.Groups[4].Value)
        }
    }
}

function Add-WordTable {
    param($Document, $Selection, [System.Collections.Generic.List[string[]]]$Rows)

    $rowCount = $Rows.Count
    $colCount = $Rows[0].Count
    $table = $Document.Tables.Add($Selection.Range, $rowCount, $colCount)
    for ($r = 1; $r -le $rowCount; $r++) {
        for ($c = 1; $c -le $colCount; $c++) {
            $table.Cell($r, $c).Range.Text = $Rows[$r - 1][$c - 1]
        }
    }
    $Selection.EndOf(6) | Out-Null  # wdStory — move past the inserted table
    $Selection.TypeParagraph()
}

Export-ModuleMember -Function ConvertFrom-WordDocumentToMarkdown, New-WordDocumentFromMarkdown
