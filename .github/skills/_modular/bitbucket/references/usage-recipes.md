# Usage Recipes - Bitbucket Tools

Load this file when you need concrete examples, workflow patterns, or troubleshooting.

---

## 1. Quick Examples

### Fetch a PR

```powershell
$env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905"}'; node "<workspace>/skills/shared/atlassian-cli/atlassian_cli.js" fetch_bitbucket_pr
```

### Get PR diff for a specific file

```powershell
$env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905","filePath":"src/main/java/App.java","contextLines":5}'; node "<workspace>/skills/shared/atlassian-cli/atlassian_cli.js" fetch_bitbucket_pr_diff
```

### Add an inline comment

```powershell
$env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905","comment":"This null check is missing","filePath":"src/main/java/App.java","line":42,"lineType":"ADDED"}'; node "<workspace>/skills/shared/atlassian-cli/atlassian_cli.js" add_bitbucket_pr_comment
```

### Search open PRs by author

```powershell
$env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","state":"OPEN","author":"john.doe","maxResults":20}'; node "<workspace>/skills/shared/atlassian-cli/atlassian_cli.js" search_bitbucket_prs
```

---

## 2. Workflow Patterns

### Code review documentation

1. `fetch_bitbucket_pr` + `fetch_bitbucket_pr_files` - gather PR context
2. `get_bitbucket_pr_comments` - review existing feedback
3. `add_bitbucket_pr_comment` - post findings back to the PR

### Create review tasks from comments

1. `get_bitbucket_pr_comments` - get comment IDs
2. `create_bitbucket_task` - create tasks anchored to specific comments
3. `list_bitbucket_tasks` - verify tasks were created

### Check if a file was modified in a PR

```powershell
$env:CLI_JSON_ARGS = '{"project":"IESD","repo":"iesd-26","prId":"17905","filePath":"build.gradle"}'; node "<workspace>/skills/shared/atlassian-cli/atlassian_cli.js" check_file_in_bitbucket_pr
```

---

## 3. Operating Rules

| Rule | Reason |
|---|---|
| Default `project=IESD`, `repo=iesd-26` | Consistent defaults for this workspace |
| Get comment ID before creating a task | Bitbucket tasks are anchored to comments |
| Retry once before asking the user | Reduces friction on transient failures |
| Always parse the `success` field | Prevents presenting error JSON as data |

---

## 4. Server-Specific Quirks

| Area | Detail |
|---|---|
| Bitbucket PR comments | CLI uses the activities endpoint and filters for comment actions |
| Inline comments | Require `filePath`, `line`, and `lineType` (ADDED, REMOVED, or CONTEXT) |
| Task anchoring | Tasks must reference a valid `commentId` from an existing PR comment |

---

## 5. Troubleshooting

| Symptom | Likely Cause | Fix |
|---|---|---|
| `401 Unauthorized` | Invalid or expired PAT token | Regenerate the token and update `.env` |
| `403 Forbidden` | Missing repository or project permissions | Check access with repo admin |
| `get_bitbucket_pr_comments` empty | PR has no comments | Confirm the activity feed is actually empty |
| `create_bitbucket_task` fails | Invalid `commentId` | Fetch PR comments first to get valid comment IDs |
| TLS / certificate error | Corporate CA not trusted by Node.js | Set `NODE_EXTRA_CA_CERTS` env var pointing to CA bundle |
