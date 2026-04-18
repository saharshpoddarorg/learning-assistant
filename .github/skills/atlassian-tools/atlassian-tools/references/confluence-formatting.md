# Confluence Formatting

Load this file when creating or updating Confluence pages, especially if HTML, macros, or Mermaid diagrams are involved.

## HTML Content Patterns

Use one of these two patterns for Confluence create, update, and append actions.

### Pattern A — env var

Use this for short HTML payloads.

```powershell
$env:CLI_CONTENT = @'
<h2>Section Title</h2><p>Content here</p>
'@
$env:CLI_JSON_ARGS = '{"pageId":"123456","contentFromEnv":true}'
node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" append_to_confluence_page
```

### Pattern B — workspace scratch file

Use this for larger HTML payloads or content that is awkward to embed inline.

```powershell
$workspaceTempDir = Join-Path (Get-Location) "temp-atlassian-tools"
New-Item -ItemType Directory -Path $workspaceTempDir -Force | Out-Null
$htmlFile = Join-Path $workspaceTempDir "cli_content.html"
'<h2>Section</h2><p>Content</p>' | Set-Content $htmlFile -Encoding UTF8
$env:CLI_JSON_ARGS = "{`"pageId`":`"123456`",`"contentFile`":`"$htmlFile`"}"
node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" update_confluence_page
```

## Windows PowerShell UTF-8 Warning

If you fetch Confluence page JSON from the CLI and then round-trip it through Windows PowerShell text cmdlets before re-uploading it, you can corrupt non-ASCII punctuation in `body.storage.value`.

Typical bad path:

```powershell
node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_confluence_page | Out-File -Encoding utf8 page.json
$page = Get-Content page.json -Raw | ConvertFrom-Json
$page.data.body.storage.value | Set-Content page.html -Encoding UTF8
```

Symptoms: `—` becomes `ΓÇö`, `→` becomes `ΓåÆ`, `↓` becomes `Γåô`, and `’` becomes `ΓÇÖ`.

Safe patterns:

- Prefer generating the HTML directly in a UTF-8 file and passing it via `contentFile`.
- If you must reuse existing page HTML, fetch it with `Invoke-RestMethod` or another UTF-8-safe path, edit the resulting string in memory, then write it with `Set-Content -Encoding UTF8`.
- Avoid using PowerShell text capture of external CLI JSON as the source of truth for a later Confluence page update when the page contains non-ASCII characters.

## Critical Macro Rules

| Macro | Body tag | Wrapping | If violated |
|---|---|---|---|
| `ac:name="code"` | `<ac:plain-text-body>` | `<![CDATA[...]]>` | Empty code block or code rendered as HTML |
| `ac:name="html"` | `<ac:plain-text-body>` | `<![CDATA[...]]>` | Mermaid or custom HTML renders as plain text or does not render |
| `ac:name="info"`, `warning`, `tip`, `note` | `<ac:rich-text-body>` | Standard HTML | Panel will not render |
| `ac:name="status"` | N/A | `colour` and `title` params | Missing status rendering |
| `ac:name="toc"` | N/A | `maxLevel` param | Missing table of contents |
| `ac:name="expand"` | `<ac:rich-text-body>` | Standard HTML | Expand block will not render |

### Code block example

```html
<ac:structured-macro ac:name="code">
  <ac:parameter ac:name="language">java</ac:parameter>
  <ac:plain-text-body><![CDATA[// Do NOT HTML-escape inside CDATA
public void example() { }]]></ac:plain-text-body>
</ac:structured-macro>
```

### Table Header Styling

Use the following style for table headers. The colour provides good contrast and readability:

```html
style="background-color: #205081; color: #ffffff; padding: 8px 12px; font-weight: bold;"
```
For alternating row shading (optional):

```html
<tr style="background-color: #f5f5f5;">...</tr>
```

## Mermaid Diagrams

If the user wants Mermaid rendered as graphics in browse mode, use the `html` macro, not the `code` macro.

Hard rules:

- Use `ac:name="html"` with `<ac:plain-text-body><![CDATA[...]]></ac:plain-text-body>`
- Prefer `<div class="mermaid">...</div>`
- Load Mermaid with the ESM CDN import inside `<script type="module">`
- For class diagrams, replace `<<` with `&lt;&lt;` and `>>` with `&gt;&gt;`
- Use `contentFile` for larger payloads instead of embedding huge HTML blobs directly in JSON
- If the user wants source only, use the `code` macro instead

### Working Mermaid example

```powershell
$workspaceTempDir = Join-Path (Get-Location) "temp-atlassian-tools"
New-Item -ItemType Directory -Path $workspaceTempDir -Force | Out-Null
$htmlFile = Join-Path $workspaceTempDir "confluence-mermaid.html"
@'
<h2>Workflow</h2>
<ac:structured-macro ac:name="html" ac:schema-version="1">
  <ac:plain-text-body><![CDATA[<div style="width: 100%; overflow: auto; padding: 20px;">
  <div class="mermaid">
flowchart TD
  A[Input] --> B[Process]
  B --> C[Output]
  </div>
</div>
<script type="module">
  import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@11/dist/mermaid.esm.min.mjs';
  mermaid.initialize({
    startOnLoad: true,
    theme: 'default'
  });
</script>]]></ac:plain-text-body>
</ac:structured-macro>
'@ | Set-Content -Path $htmlFile -Encoding UTF8

$env:CLI_JSON_ARGS = "{\"title\":\"Mermaid Demo\",\"spaceKey\":\"ENG\",\"parentPageId\":\"602112114\",\"contentFile\":\"$htmlFile\"}"
node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" create_confluence_page
```

## Special Characters in Content

When generating Confluence HTML content, avoid text that contains characters that are problematic in PowerShell or HTML contexts:

| Character | Problem | Fix |
|---|---|---|
| `;` (semicolon) | PowerShell treats as statement separator in env vars and here-strings in some contexts | Use `contentFile` for content that contains semicolons (e.g., `TL;DR`) |
| `—` (em dash) | Mojibake risk in PowerShell round-trips | Write directly to UTF-8 file, never pipe through PowerShell text cmdlets |
| `→`, `↓`, `'` | Same mojibake risk as em dash | Same fix — generate HTML in a UTF-8 `contentFile` |
| `&` in text | Must be `&amp;` in Confluence storage format (outside CDATA) | Always HTML-encode ampersands in body text |
| `<`, `>` in text | Must be `&lt;` / `&gt;` outside CDATA | Always HTML-encode angle brackets in body text |

**Rule:** For any content containing non-ASCII characters or semicolons, always use Pattern B (`contentFile`) instead of Pattern A (`contentFromEnv`).

## Page Formatting Best Practices

When creating Confluence pages, follow these visual and structural patterns for readability:

### Executive Summary / TL;DR Section

Place a high-visibility summary at the top of every generated page using the `info` panel:

```html
<ac:structured-macro ac:name="info" ac:schema-version="1">
  <ac:parameter ac:name="title">TL;DR &mdash; What You Need to Know</ac:parameter>
  <ac:rich-text-body>
    <ul>
      <li><strong>Key point 1:</strong> Brief summary</li>
      <li><strong>Key point 2:</strong> Brief summary</li>
    </ul>
  </ac:rich-text-body>
</ac:structured-macro>
```

Do **not** use plain `TL;DR` as an `<h2>` heading — it looks like junk text to non-technical readers. Wrap it in an info panel with a descriptive title.

### Table Formatting

Use the standardised header style for all tables:

```html
<table>
  <tbody>
    <tr>
      <th style="background-color: #205081; color: #ffffff; padding: 8px 12px; font-weight: bold;">Column 1</th>
      <th style="background-color: #205081; color: #ffffff; padding: 8px 12px; font-weight: bold;">Column 2</th>
    </tr>
    <tr><td style="padding: 6px 12px;">Data</td><td style="padding: 6px 12px;">Data</td></tr>
    <tr style="background-color: #f5f5f5;"><td style="padding: 6px 12px;">Data</td><td style="padding: 6px 12px;">Data</td></tr>
  </tbody>
</table>
```

Rules:
- Header colour: `#205081` (readable contrast with white text)
- Header text colour: **always use `color: #ffffff`** — never use `color:var(--ds-text,#000000)` or any Confluence DS variable on dark backgrounds; DS variables resolve to black in most themes and make text invisible on dark headers
- Body rows: alternate between white and `#f5f5f5` for zebra striping
- Cell padding: `6px 12px` minimum for readability
- Never use `rgb(0,82,155)` — it is too dark and makes white text hard to read

### Page Structure Template

Every generated Confluence page should follow this structure:

1. **AI Disclaimer** — `note` macro at the top (see `tone-and-disclaimer.md`)
2. **Table of Contents** — `toc` macro with `maxLevel=3`
3. **Executive Summary** — `info` panel with key takeaways
4. **Body sections** — `<h2>` headings for major sections, `<h3>` for subsections
5. **Tables** — Use the standardised header style above
6. **Callouts** — Use `info`, `warning`, `tip`, `note` macros for emphasis (not bold paragraphs)
7. **Status badges** — Use the `status` macro for states (colour-coded)