param(
    [Parameter(Mandatory = $true)]
    [string]$InputFile,
    [Parameter(Mandatory = $true)]
    [string]$TargetTool,
    [string]$OutputFile
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

. "$PSScriptRoot/lib/Common.ps1"
. "$PSScriptRoot/lib/Transformers.ps1"

if (-not (Test-Path $InputFile)) {
    throw "Input file not found: $InputFile"
}

$content = Get-Content -Raw -Path $InputFile
$repoRoot = Resolve-RepositoryRoot -StartPath $PSScriptRoot
$relative = Get-RelativePath -BasePath $repoRoot -FullPath (Resolve-Path $InputFile)

$converted = Convert-ForProfile -ProfileKey $TargetTool -Content $content -RelativePath $relative

if ($OutputFile) {
    $dir = Split-Path $OutputFile -Parent
    if ($dir -and -not (Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
    }
    Set-Content -Path $OutputFile -Value $converted -Encoding UTF8
    Write-Host "Written: $OutputFile"
} else {
    Write-Output $converted
}
