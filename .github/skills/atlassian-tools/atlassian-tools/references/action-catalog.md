# Action Catalog

Load this file only when you need the exact action name, required arguments, optional arguments, or response shape.

## Available Actions

### JIRA — Core (10 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `fetch_jira_issue` | `issueKey` | `fields` | Fetch issue details (summary, status, assignee, comments, links) |
| `fetch_jira_comments` | `issueKey` | `maxResults` | Fetch issue comments (newest first) |
| `search_jira_issues` | `jql` | `maxResults`, `startAt`, `fields` | Search via JQL query |
| `list_jira_issues_by_project` | `projectKey` | `maxResults`, `startAt` | List issues in project |
| `create_jira_issue` | `projectKey`, `issueType`, `summary` | `description`, `assignee`, `labels`, `priority`, `components`, `parentKey` | Create new issue (also creates subtasks via `parentKey`) |
| `update_jira_issue` | `issueKey` | `summary`, `description`, `assignee`, `labels`, `priority` | Update issue fields |
| `transition_jira_issue` | `issueKey`, `transition` | — | Transition issue status. `transition` accepts the display name and the CLI resolves it to the numeric ID |
| `add_jira_comment` | `issueKey`, `comment` | — | Add comment to issue |
| `add_jira_worklog` | `issueKey`, `timeSpent` | `comment`, `started` | Log work (for example `2h` or `1d 4h`) |
| `delete_jira_issue` | `issueKey` | — | Delete issue (requires admin permissions) |

### JIRA — Links & Relations (4 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `link_jira_issues` | `inwardIssueKey`, `outwardIssueKey` | `linkType` | Link two issues. Default link type is `Relates` |
| `get_jira_issue_links` | `issueKey` | — | Get all links for an issue |
| `get_jira_subtasks` | `issueKey` | `maxResults` | Get all subtasks of an issue via JQL `parent = KEY` |
| `clone_jira_issue` | `issueKey` | `summary` | Clone issue with all fields |

### JIRA — Bulk Operations (2 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `bulk_create_jira_issues` | `issues` | — | Create multiple issues at once |
| `bulk_transition_jira_issues` | `issueKeys`, `transition` | — | Transition multiple issues to a target status |

### JIRA — Labels (2 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `add_jira_labels` | `issueKey`, `labels` | — | Add labels to an issue |
| `remove_jira_labels` | `issueKey`, `labels` | — | Remove labels from an issue |

### JIRA — Watchers (3 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_jira_watchers` | `issueKey` | — | List all watchers on an issue |
| `add_jira_watcher` | `issueKey`, `username` | — | Add a user as watcher |
| `remove_jira_watcher` | `issueKey`, `username` | — | Remove a watcher |

### JIRA — Metadata (7 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_jira_issue_changelog` | `issueKey` | `maxResults` | Full change history for an issue |
| `get_jira_issue_types` | — | `projectKey` | List all issue types |
| `get_jira_statuses` | — | `projectKey` | List all statuses |
| `get_jira_components` | `projectKey` | — | List all components in a project |
| `get_jira_versions` | `projectKey` | — | List all versions/releases in a project |
| `search_jira_users` | `query` | `maxResults` | Search users by name/email |
| `get_current_jira_user` | — | — | Get the authenticated user's profile |

### JIRA — Agile / Sprint (6 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_epic_issues` | `epicKey` | `maxResults` | Get all issues linked to an epic |
| `add_issues_to_epic` | `epicKey`, `issueKeys` | — | Move issues into an epic |
| `get_jira_sprints` | `boardId` | `state` | List sprints for a board |
| `get_sprint_issues` | `sprintId` | `maxResults` | List all issues in a sprint |
| `move_to_sprint` | `sprintId`, `issueKeys` | — | Move issues into a sprint |
| `move_to_backlog` | `issueKeys` | — | Move issues to the backlog |

### Confluence — Core (11 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `fetch_confluence_page` | `pageId` | — | Fetch page content |
| `search_confluence` | `query` | `maxResults`, `spaceKey` | Text search across spaces |
| `search_confluence_cql` | `cql` | `maxResults` | Search via CQL query |
| `list_confluence_pages` | `parentPageId` | `maxResults` | List child pages |
| `create_confluence_page` | `title`, `spaceKey` | `parentPageId`, `contentFromEnv`, `contentFile` | Create new page |
| `update_confluence_page` | `pageId` | `title`, `contentFromEnv`, `contentFile` | Replace page content |
| `append_to_confluence_page` | `pageId` | `contentFromEnv`, `contentFile` | Append HTML to end of existing page |
| `add_confluence_comment` | `pageId`, `comment` | — | Add page-level comment |
| `get_confluence_comments` | `pageId` | `maxResults` | Get all comments on a page |
| `reply_to_confluence_comment` | `pageId`, `parentCommentId`, `reply` | — | Reply to a comment |
| `delete_confluence_page` | `pageId` | — | Delete page |

### Confluence — Content Management (12 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `like_confluence_page` | `pageId` | `unlike` | Like or unlike a page |
| `add_confluence_inline_comment` | `pageId`, `comment` | `originalSelection` | Add inline comment |
| `copy_confluence_page` | `pageId` | `newTitle`, `targetSpaceKey`, `targetParentId` | Copy a page |
| `move_confluence_page` | `pageId`, `targetParentId` | `targetSpaceKey` | Move a page |
| `add_confluence_page_labels` | `pageId`, `labels` | — | Add labels to a page |
| `get_confluence_page_labels` | `pageId` | — | Get all labels on a page |
| `remove_confluence_page_label` | `pageId`, `label` | — | Remove a specific label from a page |
| `search_confluence_by_label` | `label` | `spaceKey`, `maxResults` | Find all pages with a given label |
| `get_confluence_page_property` | `pageId` | `propertyKey` | Get page properties |
| `set_confluence_page_property` | `pageId`, `propertyKey`, `value` | — | Create or update a page property |
| `get_confluence_page_versions` | `pageId` | `maxResults` | Version history of a page |
| `restore_confluence_page_version` | `pageId`, `versionNumber` | — | Restore a page to a previous version |

### Confluence — Navigation & Structure (4 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_confluence_page_tree` | `pageId` | `depth` | Recursive child page listing |
| `get_confluence_page_ancestors` | `pageId` | — | Get the full ancestor chain for a page |
| `watch_confluence_page` | `pageId` | — | Start watching a page |
| `unwatch_confluence_page` | `pageId` | — | Stop watching a page |

### Confluence — Spaces, Blogs & More (7 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_confluence_space` | `spaceKey` | — | Get space details |
| `get_space_content` | `spaceKey` | `contentType`, `maxResults` | List content in a space |
| `create_confluence_blog_post` | `title`, `spaceKey` | `content`, `contentFromEnv`, `contentFile` | Create a blog post |
| `get_confluence_blog_posts` | `spaceKey` | `maxResults` | List recent blog posts |
| `get_confluence_templates` | — | `spaceKey` | List page templates |
| `export_confluence_page_pdf` | `pageId` | — | Get PDF export URL |
| `get_current_confluence_user` | — | — | Get the authenticated Confluence user |

### Bitbucket — Core (7 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `fetch_bitbucket_pr` | `project`, `repo`, `prId` | — | Fetch PR details |
| `fetch_bitbucket_pr_files` | `project`, `repo`, `prId` | `filterExtension` | List changed files |
| `fetch_bitbucket_pr_diff` | `project`, `repo`, `prId` | `filePath`, `contextLines` | Get PR diff |
| `fetch_bitbucket_pr_activities` | `project`, `repo`, `prId` | — | PR activity feed |
| `search_bitbucket_prs` | `project`, `repo` | `state`, `author`, `maxResults`, `query` | Search PRs |
| `fetch_bitbucket_file` | `project`, `repo`, `filePath` | `branch` | Fetch file content |
| `summarize_bitbucket_contributions` | `project`, `repo`, `person` | `months` | Contribution summary |

### Bitbucket — PR Comments (5 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_bitbucket_pr_comments` | `project`, `repo`, `prId` | — | Get all PR comments |
| `add_bitbucket_pr_comment` | `project`, `repo`, `prId`, `comment` | `filePath`, `line`, `lineType`, `fileType`, `parentId` | Add general or inline comment |
| `update_bitbucket_pr_comment` | `project`, `repo`, `prId`, `commentId`, `comment` | `version` | Update an existing comment |
| `delete_bitbucket_pr_comment` | `project`, `repo`, `prId`, `commentId` | `version` | Delete a comment |
| `reply_bitbucket_pr_comment` | `project`, `repo`, `prId`, `parentCommentId`, `comment` | — | Reply to a comment |

### Bitbucket — Tasks (6 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `create_bitbucket_task` | `commentId`, `text` | — | Create a task anchored to a PR comment |
| `list_bitbucket_tasks` | `project`, `repo`, `prId` | — | List all tasks on a PR |
| `update_bitbucket_task` | `taskId`, `text` | — | Update task text |
| `delete_bitbucket_task` | `taskId` | — | Delete a task |
| `resolve_bitbucket_task` | `taskId` | — | Mark task as resolved |
| `reopen_bitbucket_task` | `taskId` | — | Reopen a resolved task |

### Bitbucket — File Operations (3 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_bitbucket_file_diff` | `project`, `repo`, `filePath` | `contextLines`, `since`, `until` | Get diff for a specific file path |
| `check_file_in_bitbucket_pr` | `project`, `repo`, `prId`, `filePath` | — | Check if a file was changed in a PR |
| `get_bitbucket_pr_file` | `project`, `repo`, `prId`, `filePath` | — | Get file content at the PR source branch |

## Response Shape

All actions return JSON: `{ "success": true, "data": { ... } }` or `{ "success": false, "error": "..." }`

### Non-Obvious Response Shapes

| Action | Key fields |
|---|---|
| `fetch_confluence_page` | `data.body.storage.value`, `data.version.number`, `data._links.webui` |
| `bulk_create_jira_issues` | `data.created`, `data.failed`, `data.results[]` |
| `bulk_transition_jira_issues` | `data.transitioned`, `data.failed`, `data.results[]` |
| `get_jira_issue_changelog` | `data.changelog.histories[]` |
| `get_confluence_page_tree` | `data.tree[]` |
| `check_file_in_bitbucket_pr` | `data.found`, `data.matchingFiles[]` |
| `summarize_bitbucket_contributions` | `data.totalPRs`, `data.merged`, `data.open`, `data.declined`, `data.prs[]` |
| `get_jira_sprints` | `data.values[]` |
| `export_confluence_page_pdf` | `data.pdfUrl` |