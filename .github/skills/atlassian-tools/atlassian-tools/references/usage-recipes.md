# Usage Recipes

Load this file when you need concrete examples, bulk-operation patterns, server quirks, or troubleshooting.

---

## 1. Quick Examples

### Fetch a Jira issue

```powershell
$env:CLI_JSON_ARGS = '{"issueKey":"PROJ-123"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" fetch_jira_issue
```

### Create a Confluence page with env-var content

```powershell
$env:CLI_CONTENT = @'
<h2>Design Decisions</h2>
<table><tr><th>Decision</th><th>Status</th></tr><tr><td>Adopt gRPC</td><td>Approved</td></tr></table>
'@
$env:CLI_JSON_ARGS = '{"title":"Sprint 24 Review","spaceKey":"ENG","parentPageId":"602112114","contentFromEnv":true}'
node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" create_confluence_page
```

### Add a Bitbucket inline comment

```powershell
$env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905","comment":"This null check is missing","filePath":"src/main/java/App.java","line":42,"lineType":"ADDED"}'; node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" add_bitbucket_pr_comment
```

---

## 2. Workflow Patterns

### Sprint planning

1. `get_jira_sprints` → list sprints for a board
2. `get_sprint_issues` → see what is already in the sprint
3. `search_jira_issues` → find candidates via JQL
4. `move_to_sprint` → assign issues to the sprint

### Code review documentation

1. `fetch_bitbucket_pr` + `fetch_bitbucket_pr_files` → gather PR context
2. `get_bitbucket_pr_comments` → review existing feedback
3. `create_confluence_page` → publish the review summary
4. `add_bitbucket_pr_comment` → post findings back to the PR

---

## 3. Pagination

For actions like `search_jira_issues`, `list_jira_issues_by_project`, and `search_confluence_cql`:

- Set `maxResults` to control page size (default is 25).
- Inspect `data.total` in the response to determine if more pages remain.
- Increment `startAt` by `maxResults` for the next page.

---

## 4. Bulk Operations

```powershell
$env:CLI_JSON_ARGS = '{"jql":"project = PROJ AND status = Open AND assignee = currentUser()","maxResults":50}'
$result = (node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" search_jira_issues) | ConvertFrom-Json

if ($result.success) {
    foreach ($issue in $result.data.issues) {
        $key = $issue.key
        $env:CLI_JSON_ARGS = "{`"issueKey`":`"$key`",`"comment`":`"Triaged in sprint planning`"}"
        node "<workspace>/.github/skills/atlassian-tools/scripts/atlassian_cli.js" add_jira_comment | Out-Null
    }
}
```

Complete the full loop first, then summarize successes and failures.

---

## 5. Operating Rules

| Rule | Reason |
|---|---|
| Prefer page ID over URL for Confluence | Page IDs are stable; URLs can change |
| Retry once before asking the user | Reduces friction on transient failures |
| Always parse the `success` field | Prevents presenting error JSON as data |
| Finish the full loop for bulk operations | The user needs the complete result set |
| Escape double quotes as `\"` inside JSON | Prevents JSON parse failures in JQL/CQL |
| Set `maxResults` intentionally | Controls response size and avoids truncation |
| Default Bitbucket: `project=IESD`, `repo=iesd-26` | Keeps calls consistent for this workspace |
| Get comment ID before creating a task | Bitbucket tasks are anchored to comments |
| Keep `depth ≤ 3` for `get_confluence_page_tree` | Deep recursion is expensive |
| Scratch files go in `temp-atlassian-tools/` | Keeps the skill folder clean |

---

## 6. Server-Specific Quirks

| Area | Detail |
|---|---|
| Confluence page versions | Server uses `/rest/experimental/content/{id}/version` (not `/rest/api/`) |
| Bitbucket PR comments | CLI uses the activities endpoint and filters for comment actions |
| Jira Agile API prefix | Sprint and board operations use `/rest/agile/1.0/` |
| Epic link field | CLI tries `customfield_10008` first, falls back to `Epic Link` |
| Content delivery | Use `contentFromEnv` for short content; `contentFile` for large HTML |
| Mermaid rendering | Use the `html` macro with CDATA and a `div.mermaid` wrapper |
| Action aliases | CLI maps common hallucinated action names to real ones |

---

## 7. Troubleshooting

### Authentication

| Symptom | Likely Cause | Fix |
|---|---|---|
| `401 Unauthorized` | Invalid, expired, or wrong PAT token | Regenerate the token and update `.env` |
| `401` despite valid token | Token wrapped in quotes in `.env` | Remove surrounding `"` or `'` — store raw value only |
| Token with `+` `/` `=` fails | Token was URL-encoded or quoted | Store the raw Base64 token as-is — these characters are valid |
| TLS / certificate error | Corporate CA not trusted by Node.js | Set `NODE_EXTRA_CA_CERTS` env var pointing to CA bundle |

### Setup

| Symptom | Likely Cause | Fix |
|---|---|---|
| `Missing .env file` | No `.env` in workspace root | Create `<workspace>/.env` with required variables (see SKILL.md → Setup) |
| `Missing *_PAT_TOKEN` | Service token line missing | Add the token to `.env` |
| `ECONNREFUSED` or timeout | Network / VPN issue | Check VPN connection and verify base URLs in `.env` |

### Jira

| Symptom | Likely Cause | Fix |
|---|---|---|
| `Transition not found` | Name mismatch with available transitions | Fetch the issue first; use the exact transition name shown |
| Empty search results | Wrong status names or restrictive filters | Try alternate statuses or broaden the JQL query |
| `search_jira_issues` truncated | Default `maxResults` is 25 | Set `maxResults` to a higher value |
| `Epic Link` field not found | Instance-specific custom field config | Consult Jira admin for the correct field name |
| Sprint or board `404` | Wrong board or sprint ID | Fetch valid IDs with `get_jira_sprints` first |

### Confluence

| Symptom | Likely Cause | Fix |
|---|---|---|
| `404 Not Found` | Wrong page ID | Verify the ID via search or URL |
| `409 Conflict` on update | Version conflict (concurrent edit) | Re-fetch the page to get latest version, then retry |
| `export_confluence_page_pdf` — no download | Requires browser session | Open the returned URL in an authenticated browser |

### Bitbucket

| Symptom | Likely Cause | Fix |
|---|---|---|
| `403 Forbidden` | Missing repository or project permissions | Check access with repo admin |
| `get_bitbucket_pr_comments` empty | PR has no comments | Confirm the activity feed is actually empty |
| `create_bitbucket_task` fails | Invalid `commentId` | Fetch PR comments first to get valid comment IDs |
| `bulk_create` partial failure | Some items failed | Inspect `data.results[]` and retry only the failed items |

### PowerShell

| Symptom | Likely Cause | Fix |
|---|---|---|
| `JSON parse error` | PowerShell mangled JSON arguments | Always use single quotes around static JSON |
| Mojibake (`ΓÇö` instead of `—`) | PowerShell corrupted non-ASCII on round-trip | Generate HTML in a UTF-8 file; avoid piping CLI JSON through text cmdlets |