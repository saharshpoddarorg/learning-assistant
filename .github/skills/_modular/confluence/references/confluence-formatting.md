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
node "<workspace>/.github/skills/_modular/confluence/scripts/confluence_cli.js" append_to_confluence_page
```

### Pattern B — workspace scratch file

Use this for larger HTML payloads or content that is awkward to embed inline.

```powershell
$workspaceTempDir = Join-Path (Get-Location) "temp-atlassian-tools"
New-Item -ItemType Directory -Path $workspaceTempDir -Force | Out-Null
$htmlFile = Join-Path $workspaceTempDir "cli_content.html"
'<h2>Section</h2><p>Content</p>' | Set-Content $htmlFile -Encoding UTF8
$env:CLI_JSON_ARGS = "{`"pageId`":`"123456`",`"contentFile`":`"$htmlFile`"}"
node "<workspace>/.github/skills/_modular/confluence/scripts/confluence_cli.js" update_confluence_page
```

## Windows PowerShell UTF-8 Warning

If you fetch Confluence page JSON from the CLI and then round-trip it through Windows PowerShell text cmdlets before re-uploading it, you can corrupt non-ASCII punctuation in `body.storage.value`.

Typical bad path:

```powershell
node "<workspace>/.github/skills/_modular/confluence/scripts/confluence_cli.js" fetch_confluence_page | Out-File -Encoding utf8 page.json
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

### Modern Table Styling

Do not use dark saturated table headers such as `#205081`, `rgb(32,80,129)`, or similar navy fills. They feel heavy, dominate the page, and make large analytical pages look dated.

When the user asks for Siemens-aligned styling, use the palette already defined in the repo theme at `cextensions_src/servermanager_webapp/src/theme/theme-variables.scss`:

- Primary brand blue: `#005f87`
- Primary dark text: `#00344a`
- Accent yellow: `#ffb900`
- Accent light: `#ffedbd`
- Panel background: `#ebf0f5`
- Hover/selection tint: `#daecf0`
- Pale neutral: `#dfe6ed`

Use this lighter editorial style by default:

```html
<div style="margin: 12px 0 28px 0; border: 1px solid #dfe6ed; border-radius: 18px; overflow: hidden; background-color: #ffffff; box-shadow: 0 10px 24px rgba(0,95,135,0.08);">
  <table style="width: 100%; border-collapse: separate; border-spacing: 0; background-color: #ffffff;">
    <tbody>
      <tr>
        <th style="background-color: #ebf0f5; color: #005f87; padding: 10px 14px; font-weight: 700; border-bottom: 2px solid #ffb900; text-align: left; vertical-align: bottom;">Column 1</th>
        <th style="background-color: #ebf0f5; color: #005f87; padding: 10px 14px; font-weight: 700; border-bottom: 2px solid #ffb900; text-align: left; vertical-align: bottom;">Column 2</th>
      </tr>
      <tr>
        <td style="padding: 10px 14px; color: #00344a; border-bottom: 1px solid #dfe6ed; vertical-align: top;">Data</td>
        <td style="padding: 10px 14px; color: #00344a; border-bottom: 1px solid #dfe6ed; vertical-align: top;">Data</td>
      </tr>
      <tr style="background-color: #f8fbfd;">
        <td style="padding: 10px 14px; color: #00344a; border-bottom: 1px solid #dfe6ed; vertical-align: top;">Data</td>
        <td style="padding: 10px 14px; color: #00344a; border-bottom: 1px solid #dfe6ed; vertical-align: top;">Data</td>
      </tr>
    </tbody>
  </table>
</div>
```

Rules:
- Use an outer wrapper with `border-radius` and `overflow: hidden` to give tables a curved card-like look.
- Use light header fills with dark text, not dark headers with white text.
- Prefer Siemens blue text with Siemens yellow underline accents for headers and section dividers.
- Keep header padding at `10px 14px` or larger for analytical tables.
- Add a visible bottom border under headers instead of relying on colour blocks.
- Use `vertical-align: top` for multi-line analytical cells.
- Use zebra striping with subtle cool neutrals only, for example `#f8fbfd`.
- If a surface is fixed light, always set the cell text colour explicitly, for example `color: #00344a`; do not rely on Confluence theme defaults.
- For text that sits directly on the page background, prefer theme-aware colours such as `var(--ds-text, #00344a)` so headings remain readable in both light and dark mode.
- Keep table chrome quiet so the data, status badges, and diagrams remain the focal point.

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
    theme: 'base',
    themeVariables: {
      primaryColor: '#daecf0',
      primaryTextColor: '#00344a',
      primaryBorderColor: '#005f87',
      secondaryColor: '#ebf0f5',
      tertiaryColor: '#ffedbd',
      lineColor: '#005f87',
      noteBkgColor: '#ffedbd',
      noteTextColor: '#00344a',
      fontFamily: 'Segoe UI, Helvetica, sans-serif'
    }
  });
</script>]]></ac:plain-text-body>
</ac:structured-macro>
'@ | Set-Content -Path $htmlFile -Encoding UTF8

$env:CLI_JSON_ARGS = "{\"title\":\"Mermaid Demo\",\"spaceKey\":\"ENG\",\"parentPageId\":\"602112114\",\"contentFile\":\"$htmlFile\"}"
node "<workspace>/.github/skills/_modular/confluence/scripts/confluence_cli.js" create_confluence_page
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

## Modern Page System

When creating Confluence pages, follow this design system. The goal is a page that looks intentional, current, and easy to scan in the first 15 seconds.

### Above-the-fold layout

Every analytical or status-heavy page should open with this structure:

1. **Table of contents** — `toc` macro with `maxLevel=3`
2. **Executive snapshot** — `info` panel or styled summary block with the main takeaways
3. **Visual overview** — at least one Mermaid diagram near the top
4. **Primary evidence** — one or two high-value summary tables before detailed appendices
5. **Details on demand** — use `expand` macros for long area-by-area breakdowns or appendices
6. **AI disclaimer** — `note` macro at the bottom (see `tone-and-disclaimer.md`)

### Executive snapshot section

Do not use plain `TL;DR` as a heading. Use an `info` panel titled `Executive snapshot` or `What matters most` with concise bullets.

```html
<ac:structured-macro ac:name="info" ac:schema-version="1">
  <ac:parameter ac:name="title">Executive snapshot</ac:parameter>
  <ac:rich-text-body>
    <ul>
      <li><strong>Signal:</strong> The main conclusion.</li>
      <li><strong>Risk:</strong> The highest concern.</li>
      <li><strong>Action:</strong> What should happen next.</li>
    </ul>
  </ac:rich-text-body>
</ac:structured-macro>
```

### Section headers

Use clean section headings with restrained styling. Prefer inline heading styles such as:

```html
<h2 style="margin: 34px 0 10px 0; font-size: 24px; color: var(--ds-text, #00344a); padding-bottom: 8px; border-bottom: 2px solid #ffb900;">Section title</h2>
<h3 style="margin: 24px 0 8px 0; font-size: 18px; color: var(--ds-text-subtle, #005f87);">Subsection title</h3>
```

If you are using a transparent page background behind the heading, do not hard-code dark text colours there.

### Table usage rules

- Put summary tables first; move exhaustive evidence below the fold.
- For very long sections, wrap the detailed tables in `expand` macros.
- Use `status` macros or small text badges only where they add real signal.
- Keep row striping subtle and consistent across the page.
- Use the Modern Table Styling pattern above for all data tables.

### Detail collapse pattern

For long analytical pages, collapse verbose evidence rather than forcing readers through a wall of tables.

```html
<ac:structured-macro ac:name="expand" ac:schema-version="1">
  <ac:parameter ac:name="title">Detailed area breakdown</ac:parameter>
  <ac:rich-text-body>
    <p>Place the long appendix content here.</p>
  </ac:rich-text-body>
</ac:structured-macro>
```

### Visual Diagram Requirement

**Every Confluence page must include at least one Mermaid diagram.** Do not create text-only pages. Choose the most appropriate diagram type based on the content:

| Content type | Recommended diagram |
|---|---|
| Workflow, process, decision logic | `flowchart TD` or `flowchart LR` |
| Sequence of interactions between components | `sequenceDiagram` |
| Class/object relationships, data model | `classDiagram` |
| System architecture, module boundaries | `flowchart` with subgraphs |
| Performance analysis, call chains | `sequenceDiagram` or `flowchart TD` |
| Timeline, milestones, phases | `gantt` or `timeline` |
| State transitions | `stateDiagram-v2` |

Rules:
- Place the diagram near the top of the page, immediately after the Executive Summary, so readers get a visual overview first.
- If the page covers multiple topics (e.g., architecture + performance + risk), include a diagram for each major topic.
- Use clear, descriptive node labels — not single letters or abbreviations.
- Apply Mermaid styling (`style` or `classDef`) for emphasis where it adds clarity (e.g., red for errors, green for success paths).
- Use `theme: 'base'` with custom `themeVariables` so the diagrams align with the page palette rather than default Mermaid colours.
- Render using the `html` macro pattern documented in the Mermaid Diagrams section above.
