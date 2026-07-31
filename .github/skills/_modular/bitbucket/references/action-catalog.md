# Action Catalog - Bitbucket Tools

Load this file only when you need the exact action name, required arguments, optional arguments, or response shape.

## Bitbucket - Core (7 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `fetch_bitbucket_pr` | `project`, `repo`, `prId` | - | Fetch PR details |
| `fetch_bitbucket_pr_files` | `project`, `repo`, `prId` | - | List changed files |
| `fetch_bitbucket_pr_diff` | `project`, `repo`, `prId` | `filePath`, `contextLines` | Get PR diff |
| `fetch_bitbucket_pr_activities` | `project`, `repo`, `prId` | - | PR activity feed |
| `search_bitbucket_prs` | `project`, `repo` | `state`, `author`, `maxResults` | Search PRs |
| `fetch_bitbucket_file` | `project`, `repo`, `filePath` | `branch` | Fetch file content |
| `summarize_bitbucket_contributions` | `project`, `repo`, `person` | `months` | Contribution summary |

## Bitbucket - PR Comments (5 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_bitbucket_pr_comments` | `project`, `repo`, `prId` | - | Get all PR comments |
| `add_bitbucket_pr_comment` | `project`, `repo`, `prId`, `comment` | `filePath`, `line`, `lineType`, `fileType`, `parentId` | Add general or inline comment |
| `update_bitbucket_pr_comment` | `project`, `repo`, `prId`, `commentId`, `comment` | `version` | Update an existing comment |
| `delete_bitbucket_pr_comment` | `project`, `repo`, `prId`, `commentId` | `version` | Delete a comment |
| `reply_bitbucket_pr_comment` | `project`, `repo`, `prId`, `parentCommentId`, `comment` | - | Reply to a comment |

## Bitbucket - Tasks (6 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `create_bitbucket_task` | `commentId`, `text` | - | Create a task anchored to a PR comment |
| `list_bitbucket_tasks` | `project`, `repo`, `prId` | - | List all tasks on a PR |
| `update_bitbucket_task` | `taskId`, `text` | - | Update task text |
| `delete_bitbucket_task` | `taskId` | - | Delete a task |
| `resolve_bitbucket_task` | `taskId` | - | Mark task as resolved |
| `reopen_bitbucket_task` | `taskId` | - | Reopen a resolved task |

## Bitbucket - File Operations (3 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_bitbucket_file_diff` | `project`, `repo`, `filePath` | `contextLines`, `since`, `until` | Get diff for a specific file path |
| `check_file_in_bitbucket_pr` | `project`, `repo`, `prId`, `filePath` | - | Check if a file was changed in a PR |
| `get_bitbucket_pr_file` | `project`, `repo`, `prId`, `filePath` | - | Get file content at the PR source branch |

## Response Shape

All actions return JSON: `{ "success": true, "data": { ... } }` or `{ "success": false, "error": "..." }`

### Non-Obvious Response Shapes

| Action | Key fields |
|---|---|
| `check_file_in_bitbucket_pr` | `data.found`, `data.matchingFiles[]` |
| `summarize_bitbucket_contributions` | `data.totalPRs`, `data.merged`, `data.open`, `data.declined`, `data.prs[]` |
