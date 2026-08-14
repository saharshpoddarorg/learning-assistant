<#
    Converts a document from one format to another, preserving structural formatting
    (headings, bold/italic, lists, links, tables) as much as each format allows.

    Routing strategy (direct hop where possible, Markdown as the bridge otherwise):

    - Same extension in and out                -> byte-for-byte copy.
    - Both formats natively opened/saved by
      Word (.docx/.doc/.rtf/.pdf/.html/.htm/
      .txt/.text/.log/unknown)                  -> single direct hop via Word COM
                                                    (Open source format, SaveAs2 target
                                                    format) — this is the highest-fidelity
                                                    path Word can offer for that pair, no
                                                    Markdown involved at all.
    - Markdown (.md/.markdown) on either side,
      other side is Word-openable                -> bridged via Word's own Document object
                                                    model (WordDocumentBridge.psm1), NOT
                                                    Word's HTML export. Word's Filtered HTML
                                                    wraps content in noisy MSO markup and
                                                    renders bullets as symbol-font glyphs,
                                                    which corrupts a regex-based bridge
                                                    (misplaced bold markers, garbled bullets).
                                                    Reading/writing paragraph styles, list
                                                    formatting, and run-level bold/italic
                                                    directly is far more reliable.
    - Markdown <-> plain text                   -> verbatim copy (Markdown IS plain text;
                                                    this keeps 100% of the source content).
    - HTML <-> Markdown, explicitly without Word
      (-NoWord switch)                          -> lightweight regex bridge
                                                    (HtmlMarkdownConvert.psm1). Lower
                                                    fidelity than the Word-based bridge but
                                                    needs no Word installation — useful for
                                                    clean app-exported HTML (Notion, Google
                                                    Keep/Takeout, Confluence "export to HTML").

    No network calls. Word is only launched for pairs that actually need it.

    Usage:
      .\convert-document.ps1 -InputPath <source> -OutputPath <target>
      .\convert-document.ps1 -InputPath <source> -OutputPath <target> -TargetFormat pdf
      .\convert-document.ps1 -InputPath fixture.html -OutputPath fixture.md -NoWord
#>

param(
    [Parameter(Mandatory = $true)]
    [string]$InputPath,

    [Parameter(Mandatory = $true)]
    [string]$OutputPath,

    # Optional override — otherwise the target format is inferred from -OutputPath's extension.
    [ValidateSet('docx', 'doc', 'rtf', 'pdf', 'html', 'htm', 'md', 'markdown', 'txt', 'text', 'log')]
    [string]$TargetFormat,

    # Force the lightweight regex HTML<->Markdown bridge instead of the Word object model.
    # Only meaningful for html/htm <-> md/markdown; ignored for every other pair.
    [switch]$NoWord
)

$ErrorActionPreference = "Stop"
Import-Module (Join-Path $PSScriptRoot "HtmlMarkdownConvert.psm1") -Force
Import-Module (Join-Path $PSScriptRoot "WordDocumentBridge.psm1") -Force

# Extensions Word's Documents.Open() auto-detects by file extension.
$wordOpenableExtensions = @(".docx", ".doc", ".rtf", ".pdf", ".html", ".htm", ".txt")

$saveFormatCodes = @{
    ".docx" = 16  # wdFormatXMLDocument (wdFormatDocumentDefault)
    ".doc"  = 0   # wdFormatDocument
    ".rtf"  = 6   # wdFormatRTF
    ".pdf"  = 17  # wdFormatPDF
    ".html" = 10  # wdFormatFilteredHTML — cleaner markup, better for an HTML<->MD round trip
    ".htm"  = 10
    ".txt"  = 7   # wdFormatUnicodeText — safer than wdFormatText(2) for non-ASCII content
    ".text" = 7
    ".log"  = 7
}

function Get-Family([string]$ext) {
    switch -Regex ($ext) {
        '^\.(docx|doc|rtf)$' { return "word" }
        '^\.pdf$' { return "pdf" }
        '^\.(html|htm)$' { return "html" }
        '^\.(md|markdown)$' { return "markdown" }
        default { return "text" }  # .txt/.text/.log and any unrecognized/npp-style extension
    }
}

$tempFiles = New-Object System.Collections.Generic.List[string]

function New-TempFile([string]$extension) {
    $path = Join-Path ([System.IO.Path]::GetTempPath()) ([System.IO.Path]::GetRandomFileName() + $extension)
    $tempFiles.Add($path)
    return $path
}

function Open-WordDocument([string]$Path, [string]$Extension) {
    $openablePath = $Path
    if ($wordOpenableExtensions -notcontains $Extension) {
        $openablePath = New-TempFile ".txt"
        Copy-Item -LiteralPath $Path -Destination $openablePath
    }

    $word = New-Object -ComObject Word.Application
    $word.Visible = $false
    $word.DisplayAlerts = 0  # wdAlertsNone
    $doc = $word.Documents.Open($openablePath)
    return @{ Word = $word; Document = $doc }
}

function Close-WordSession($Session) {
    if ($Session.Document) { $Session.Document.Close([ref]$false) }
    $Session.Word.Quit()
    [System.Runtime.Interopservices.Marshal]::ReleaseComObject($Session.Word) | Out-Null
    [GC]::Collect()
    [GC]::WaitForPendingFinalizers()
}

function Copy-AsPlainText([string]$SourcePath, [string]$DestPath) {
    $bytes = [System.IO.File]::ReadAllBytes($SourcePath)
    if ($bytes -contains 0) {
        throw "'$SourcePath' looks like a binary file (contains null bytes), not plain text or Markdown."
    }
    $content = [System.IO.File]::ReadAllText($SourcePath)
    [System.IO.File]::WriteAllText($DestPath, $content, (New-Object System.Text.UTF8Encoding($false)))
}

$resolvedInput = Resolve-Path -LiteralPath $InputPath -ErrorAction Stop
$srcExt = [System.IO.Path]::GetExtension($resolvedInput.Path).ToLowerInvariant()

if ($TargetFormat) {
    $dstExt = "." + $TargetFormat.ToLowerInvariant()
}
else {
    $dstExt = [System.IO.Path]::GetExtension($OutputPath).ToLowerInvariant()
    if (-not $dstExt) { throw "Cannot infer target format: -OutputPath has no extension and -TargetFormat was not given." }
}

$outputDir = Split-Path -Parent $OutputPath
if ($outputDir -and -not (Test-Path -LiteralPath $outputDir)) {
    New-Item -ItemType Directory -Force -Path $outputDir | Out-Null
}
$outputDirResolved = if ($outputDir) { (Resolve-Path -LiteralPath $outputDir -ErrorAction Stop).Path } else { (Get-Location).Path }
$outputFile = Join-Path $outputDirResolved ([System.IO.Path]::GetFileName($OutputPath))

$srcFamily = Get-Family $srcExt
$dstFamily = Get-Family $dstExt

try {
    if ($srcExt -eq $dstExt) {
        Copy-Item -LiteralPath $resolvedInput.Path -Destination $outputFile -Force
    }
    elseif ($srcFamily -in @("word", "pdf", "html", "text") -and $dstFamily -in @("word", "pdf", "html", "text")) {
        # Direct Word-native hop — no Markdown involved, maximum fidelity Word can offer.
        $formatCode = $saveFormatCodes[$dstExt]
        if ($null -eq $formatCode) { $formatCode = 7 }
        $session = Open-WordDocument -Path $resolvedInput.Path -Extension $srcExt
        try { $session.Document.SaveAs2($outputFile, $formatCode) } finally { Close-WordSession $session }
    }
    elseif ($srcFamily -eq "markdown" -and $dstFamily -in @("word", "pdf", "html")) {
        $markdown = [System.IO.File]::ReadAllText($resolvedInput.Path)
        if ($NoWord -and $dstFamily -eq "html") {
            $htmlText = ConvertTo-HtmlFromMarkdown -Markdown $markdown
            [System.IO.File]::WriteAllText($outputFile, $htmlText, (New-Object System.Text.UTF8Encoding($false)))
        }
        else {
            $word = New-Object -ComObject Word.Application
            $word.Visible = $false
            $word.DisplayAlerts = 0
            try {
                $doc = New-WordDocumentFromMarkdown -Word $word -Markdown $markdown
                $formatCode = $saveFormatCodes[$dstExt]
                if ($null -eq $formatCode) { $formatCode = 7 }
                $doc.SaveAs2($outputFile, $formatCode)
                $doc.Close([ref]$false)
            }
            finally {
                $word.Quit()
                [System.Runtime.Interopservices.Marshal]::ReleaseComObject($word) | Out-Null
                [GC]::Collect()
                [GC]::WaitForPendingFinalizers()
            }
        }
    }
    elseif ($srcFamily -in @("word", "pdf", "html") -and $dstFamily -eq "markdown") {
        if ($NoWord -and $srcFamily -eq "html") {
            $htmlText = [System.IO.File]::ReadAllText($resolvedInput.Path)
            $markdownText = ConvertTo-MarkdownFromHtml -Html $htmlText
        }
        else {
            $session = Open-WordDocument -Path $resolvedInput.Path -Extension $srcExt
            try { $markdownText = ConvertFrom-WordDocumentToMarkdown -Document $session.Document } finally { Close-WordSession $session }
        }
        [System.IO.File]::WriteAllText($outputFile, $markdownText, (New-Object System.Text.UTF8Encoding($false)))
    }
    elseif (($srcFamily -eq "markdown" -and $dstFamily -eq "text") -or
        ($srcFamily -eq "text" -and $dstFamily -eq "markdown") -or
        ($srcFamily -eq "markdown" -and $dstFamily -eq "markdown")) {
        # Markdown IS plain text — verbatim copy preserves 100% of the source content.
        Copy-AsPlainText -SourcePath $resolvedInput.Path -DestPath $outputFile
    }
    else {
        throw "Unsupported conversion: '$srcExt' -> '$dstExt'."
    }
}
finally {
    foreach ($t in $tempFiles) { Remove-Item -LiteralPath $t -Force -ErrorAction SilentlyContinue }
}

Write-Output "Converted: $($resolvedInput.Path) -> $outputFile"
