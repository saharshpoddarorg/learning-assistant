# Usage Recipes - Confluence Tools

Load this file when you need concrete examples, CQL recipes, pagination, or troubleshooting.

---

## 1. Quick Examples

### Fetch a page

```powershell
$env:CLI_JSON_ARGS = '{"pageId":"602112114"}'; node "<workspace>/skills/_modular/confluence/scripts/confluence_cli.js" fetch_confluence_page
```

### Create a page with env-var content

```powershell
$env:CLI_CONTENT = @'
<h2>Design Decisions</h2>
<table><tr><th>Decision</th><th>Status</th></tr><tr><td>Adopt gRPC</td><td>Approved</td></tr></table>
'@
$env:CLI_JSON_ARGS = '{"title":"Sprint 24 Review","spaceKey":"ENG","parentPageId":"602112114","contentFromEnv":true}'
node "<workspace>/skills/_modular/confluence/scripts/confluence_cli.js" create_confluence_page
```

### Search via CQL

```powershell
$env:CLI_JSON_ARGS = '{"cql":"type = page AND space = ENG ORDER BY lastmodified DESC","maxResults":10}'; node "<workspace>/skills/_modular/confluence/scripts/confluence_cli.js" search_confluence_cql
```

---

## 2. Pagination

For `search_confluence_cql`, `list_confluence_pages`, and `get_space_content`:

- Set `maxResults` to control page size (default is 25).
- Inspect `data.total` in the response to determine if more pages remain.
- Increment `startAt` by `maxResults` for the next page.

---

## 3. CQL Recipes

### Pages recently updated by a specific user

CQL has no `lastmodifier` field. Use `contributor` and filter client-side:

```powershell
$env:CLI_JSON_ARGS = '{"cql":"type = page AND contributor = currentUser() ORDER BY lastmodified DESC","maxResults":20}'
$result = (node "<workspace>/skills/_modular/confluence/scripts/confluence_cli.js" search_confluence_cql) | ConvertFrom-Json
$myPages = $result.data.results | Where-Object { $_.version.by.username -eq 'your.username@company.com' }
$myPages | Select-Object -First 5 | ForEach-Object { "$($_.title) - $($_.version.when)" }
```

Over-fetch (`maxResults=20`) to compensate for filtered-out results.

### Pages created by me (no filter needed)

```powershell
$env:CLI_JSON_ARGS = '{"cql":"type = page AND creator = currentUser() ORDER BY created DESC","maxResults":10}'
node "<workspace>/skills/_modular/confluence/scripts/confluence_cli.js" search_confluence_cql
```

### CQL Fields That Do NOT Exist

| Attempted Field | Error | Correct Alternative |
|---|---|---|
| `lastmodifier` | HTTP 400 | `contributor` + `version.by` client filter |
| `updatedBy` | HTTP 400 | Same as above |
| `modifiedBy` | HTTP 400 | Same as above |

---

## 4. Operating Rules

| Rule | Reason |
|---|---|
| Prefer page ID over URL for Confluence | Page IDs are stable; URLs can change |
| Retry once before asking the user | Reduces friction on transient failures |
| Always parse the `success` field | Prevents presenting error JSON as data |
| Set `maxResults` intentionally | Controls response size and avoids truncation |
| Keep `depth <= 3` for `get_confluence_page_tree` | Deep recursion is expensive |
| Scratch files go in `temp-confluence-tools/` | Keeps the skill folder clean |
| Prefer a single CQL query + client-side filter over N sequential calls | One round-trip is always faster |
| Over-fetch when client filtering is needed (e.g. `maxResults=20` for top 5) | Compensates for results removed by filter |
| Use `currentUser()` in CQL instead of hardcoding a username | Works for any user running the query |

---

## 5. Server-Specific Quirks

| Area | Detail |
|---|---|
| Confluence page versions | Server uses `/rest/experimental/content/{id}/version` (not `/rest/api/`) |
| Content delivery | Use `contentFromEnv` for short content; `contentFile` for large HTML |
| Mermaid rendering | Use the `html` macro with CDATA and a `div.mermaid` wrapper |
| CQL `lastmodifier` field | Does NOT exist - returns HTTP 400. Use `contributor` + client filter |
| CQL `contributor` field | Matches anyone who ever edited the page, not just the latest editor |

---

## 6. Troubleshooting

| Symptom | Likely Cause | Fix |
|---|---|---|
| `401 Unauthorized` | Invalid or expired PAT token | Regenerate the token and update `.env` |
| `404 Not Found` | Wrong page ID | Verify the ID via search or URL |
| `409 Conflict` on update | Version conflict (concurrent edit) | Re-fetch the page to get latest version, then retry |
| `export_confluence_page_pdf` - no download | Requires browser session | Open the returned URL in an authenticated browser |
| HTTP 400 on CQL query | Invalid field name (e.g. `lastmodifier`) | Use `contributor` + client-side filter. See CQL Recipes above. |
| JSON parse error | PowerShell mangled JSON arguments | Always use single quotes around static JSON |
| Mojibake in content | PowerShell corrupted non-ASCII on round-trip | Generate HTML in a UTF-8 file; use `contentFile` |
