# Action Catalog - Jira Tools

Load this file only when you need the exact action name, required arguments, optional arguments, or response shape.

## JIRA - Core (10 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `fetch_jira_issue` | `issueKey` | `fields` | Fetch issue details (summary, status, assignee, comments, links) |
| `fetch_jira_comments` | `issueKey` | `maxResults` | Fetch issue comments (newest first) |
| `search_jira_issues` | `jql` | `maxResults`, `startAt`, `fields` | Search via JQL query |
| `list_jira_issues_by_project` | `projectKey` | `maxResults`, `startAt` | List issues in project |
| `create_jira_issue` | `projectKey`, `issueType`, `summary` | `description`, `assignee`, `labels`, `priority`, `components`, `parentKey` | Create new issue (also creates subtasks via `parentKey`) |
| `update_jira_issue` | `issueKey` | `summary`, `description`, `assignee`, `labels`, `priority` | Update issue fields |
| `transition_jira_issue` | `issueKey`, `transition` | - | Transition issue status. `transition` accepts the display name and the CLI resolves it to the numeric ID |
| `add_jira_comment` | `issueKey`, `comment` | - | Add comment to issue |
| `add_jira_worklog` | `issueKey`, `timeSpent` | `comment`, `started` | Log work (for example `2h` or `1d 4h`) |
| `delete_jira_issue` | `issueKey` | - | Delete issue (requires admin permissions) |

## JIRA - Links & Relations (4 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `link_jira_issues` | `inwardIssueKey`, `outwardIssueKey` | `linkType` | Link two issues. Default link type is `Relates` |
| `get_jira_issue_links` | `issueKey` | - | Get all links for an issue |
| `get_jira_subtasks` | `issueKey` | `maxResults` | Get all subtasks of an issue via JQL `parent = KEY` |
| `clone_jira_issue` | `issueKey` | `summary` | Clone issue with all fields |

## JIRA - Bulk Operations (2 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `bulk_create_jira_issues` | `issues` | - | Create multiple issues at once |
| `bulk_transition_jira_issues` | `issueKeys`, `transition` | - | Transition multiple issues to a target status |

## JIRA - Labels (2 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `add_jira_labels` | `issueKey`, `labels` | - | Add labels to an issue |
| `remove_jira_labels` | `issueKey`, `labels` | - | Remove labels from an issue |

## JIRA - Watchers (3 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_jira_watchers` | `issueKey` | - | List all watchers on an issue |
| `add_jira_watcher` | `issueKey`, `username` | - | Add a user as watcher |
| `remove_jira_watcher` | `issueKey`, `username` | - | Remove a watcher |

## JIRA - Metadata (7 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_jira_issue_changelog` | `issueKey` | `maxResults` | Full change history for an issue |
| `get_jira_issue_types` | - | `projectKey` | List all issue types |
| `get_jira_statuses` | - | `projectKey` | List all statuses |
| `get_jira_components` | `projectKey` | - | List all components in a project |
| `get_jira_versions` | `projectKey` | - | List all versions/releases in a project |
| `search_jira_users` | `query` | `maxResults` | Search users by name/email |
| `get_current_jira_user` | - | - | Get the authenticated user's profile |

## JIRA - Agile / Sprint (6 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_epic_issues` | `epicKey` | `maxResults` | Get all issues linked to an epic |
| `add_issues_to_epic` | `epicKey`, `issueKeys` | - | Move issues into an epic |
| `get_jira_sprints` | `boardId` | `state` | List sprints for a board |
| `get_sprint_issues` | `sprintId` | `maxResults` | List all issues in a sprint |
| `move_to_sprint` | `sprintId`, `issueKeys` | - | Move issues into a sprint |
| `move_to_backlog` | `issueKeys` | - | Move issues to the backlog |

## Response Shape

All actions return JSON: `{ "success": true, "data": { ... } }` or `{ "success": false, "error": "..." }`

### Non-Obvious Response Shapes

| Action | Key fields |
|---|---|
| `bulk_create_jira_issues` | `data.created`, `data.failed`, `data.results[]` |
| `bulk_transition_jira_issues` | `data.transitioned`, `data.failed`, `data.results[]` |
| `get_jira_issue_changelog` | `data.changelog.histories[]` |
| `get_jira_sprints` | `data.values[]` |
