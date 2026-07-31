# Usage Recipes - Jira Tools

Load this file when you need concrete examples, bulk-operation patterns, or troubleshooting.

---

## 1. Quick Examples

### Fetch a Jira issue

```powershell
$env:CLI_JSON_ARGS = '{"issueKey":"PROJ-123"}'; node "<workspace>/skills/_modular/jira/scripts/jira_cli.js" fetch_jira_issue
```

### Search via JQL

```powershell
$env:CLI_JSON_ARGS = '{"jql":"project = PROJ AND status = Open AND assignee = currentUser()","maxResults":50}'; node "<workspace>/skills/_modular/jira/scripts/jira_cli.js" search_jira_issues
```

### Create an issue

```powershell
$env:CLI_JSON_ARGS = '{"projectKey":"PROJ","issueType":"Task","summary":"Implement login flow","description":"Add OAuth2 login","assignee":"john.doe"}'; node "<workspace>/skills/_modular/jira/scripts/jira_cli.js" create_jira_issue
```

### Transition an issue

```powershell
$env:CLI_JSON_ARGS = '{"issueKey":"PROJ-123","transition":"In Progress"}'; node "<workspace>/skills/_modular/jira/scripts/jira_cli.js" transition_jira_issue
```

---

## 2. Workflow Patterns

### Sprint planning

1. `get_jira_sprints` - list sprints for a board
2. `get_sprint_issues` - see what is already in the sprint
3. `search_jira_issues` - find candidates via JQL
4. `move_to_sprint` - assign issues to the sprint

### Bulk label update

1. `search_jira_issues` - find issues matching criteria
2. Loop: `add_jira_labels` on each issue
3. Summarize successes and failures

---

## 3. Pagination

For `search_jira_issues` and `list_jira_issues_by_project`:

- Set `maxResults` to control page size (default is 25).
- Inspect `data.total` in the response to determine if more pages remain.
- Increment `startAt` by `maxResults` for the next page.

---

## 4. Bulk Operations

```powershell
$env:CLI_JSON_ARGS = '{"jql":"project = PROJ AND status = Open AND assignee = currentUser()","maxResults":50}'
$result = (node "<workspace>/skills/_modular/jira/scripts/jira_cli.js" search_jira_issues) | ConvertFrom-Json

if ($result.success) {
    foreach ($issue in $result.data.issues) {
        $key = $issue.key
        $env:CLI_JSON_ARGS = "{`"issueKey`":`"$key`",`"comment`":`"Triaged in sprint planning`"}"
        node "<workspace>/skills/_modular/jira/scripts/jira_cli.js" add_jira_comment | Out-Null
    }
}
```

Complete the full loop first, then summarize successes and failures.

---

## 5. Operating Rules

| Rule | Reason |
|---|---|
| Retry once before asking the user | Reduces friction on transient failures |
| Always parse the `success` field | Prevents presenting error JSON as data |
| Finish the full loop for bulk operations | The user needs the complete result set |
| Escape double quotes as `\"` inside JSON | Prevents JSON parse failures in JQL |
| Set `maxResults` intentionally | Controls response size and avoids truncation |

---

## 6. Server-Specific Quirks

| Area | Detail |
|---|---|
| Jira Agile API prefix | Sprint and board operations use `/rest/agile/1.0/` |
| Epic link field | CLI tries `customfield_10008` first, falls back to `Epic Link` |
| Action aliases | CLI maps common hallucinated action names to real ones |

---

## 7. Troubleshooting

### Authentication

| Symptom | Likely Cause | Fix |
|---|---|---|
| `401 Unauthorized` | Invalid, expired, or wrong PAT token | Regenerate the token and update `.env` |
| `401` despite valid token | Token wrapped in quotes in `.env` | Remove surrounding `"` or `'` - store raw value only |
| Token with `+` `/` `=` fails | Token was URL-encoded or quoted | Store the raw Base64 token as-is |
| TLS / certificate error | Corporate CA not trusted by Node.js | Set `NODE_EXTRA_CA_CERTS` env var pointing to CA bundle |

### Common Issues

| Symptom | Likely Cause | Fix |
|---|---|---|
| `Transition not found` | Name mismatch with available transitions | Fetch the issue first; use the exact transition name shown |
| Empty search results | Wrong status names or restrictive filters | Try alternate statuses or broaden the JQL query |
| `search_jira_issues` truncated | Default `maxResults` is 25 | Set `maxResults` to a higher value |
| `Epic Link` field not found | Instance-specific custom field config | Consult Jira admin for the correct field name |
| Sprint or board `404` | Wrong board or sprint ID | Fetch valid IDs with `get_jira_sprints` first |
