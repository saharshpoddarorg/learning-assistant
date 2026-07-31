# Action Catalog - Confluence Tools

Load this file only when you need the exact action name, required arguments, optional arguments, or response shape.

## Confluence - Core (11 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `fetch_confluence_page` | `pageId` | - | Fetch page content |
| `search_confluence` | `query` | `maxResults`, `spaceKey` | Text search across spaces |
| `search_confluence_cql` | `cql` | `maxResults` | Search via CQL query. Note: `lastmodifier`, `updatedBy`, `modifiedBy` are NOT valid CQL fields - use `contributor` + client-side `version.by` filter instead. |
| `list_confluence_pages` | `parentPageId` | `maxResults` | List child pages |
| `create_confluence_page` | `title`, `spaceKey` | `parentPageId`, `contentFromEnv`, `contentFile` | Create new page |
| `update_confluence_page` | `pageId` | `title`, `contentFromEnv`, `contentFile` | Replace page content |
| `append_to_confluence_page` | `pageId` | `contentFromEnv`, `contentFile` | Append HTML to end of existing page |
| `add_confluence_comment` | `pageId`, `comment` | - | Add page-level comment |
| `get_confluence_comments` | `pageId` | `maxResults` | Get all comments on a page |
| `reply_to_confluence_comment` | `pageId`, `parentCommentId`, `reply` | - | Reply to a comment |
| `delete_confluence_page` | `pageId` | - | Delete page |

## Confluence - Content Management (12 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `like_confluence_page` | `pageId` | `unlike` | Like or unlike a page |
| `add_confluence_inline_comment` | `pageId`, `comment` | `originalSelection` | Add inline comment |
| `copy_confluence_page` | `pageId` | `newTitle`, `targetSpaceKey`, `targetParentId` | Copy a page |
| `move_confluence_page` | `pageId`, `targetParentId` | `targetSpaceKey` | Move a page |
| `add_confluence_page_labels` | `pageId`, `labels` | - | Add labels to a page |
| `get_confluence_page_labels` | `pageId` | - | Get all labels on a page |
| `remove_confluence_page_label` | `pageId`, `label` | - | Remove a specific label from a page |
| `search_confluence_by_label` | `label` | `spaceKey`, `maxResults` | Find all pages with a given label |
| `get_confluence_page_property` | `pageId` | `propertyKey` | Get page properties |
| `set_confluence_page_property` | `pageId`, `propertyKey`, `value` | - | Create or update a page property |
| `get_confluence_page_versions` | `pageId` | `maxResults` | Version history of a page |
| `restore_confluence_page_version` | `pageId`, `versionNumber` | - | Restore a page to a previous version |

## Confluence - Navigation & Structure (4 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_confluence_page_tree` | `pageId` | `depth` | Recursive child page listing |
| `get_confluence_page_ancestors` | `pageId` | - | Get the full ancestor chain for a page |
| `watch_confluence_page` | `pageId` | - | Start watching a page |
| `unwatch_confluence_page` | `pageId` | - | Stop watching a page |

## Confluence - Spaces, Blogs & More (7 actions)

| Action | Required Args | Optional Args | Description |
|---|---|---|---|
| `get_confluence_space` | `spaceKey` | - | Get space details |
| `get_space_content` | `spaceKey` | `contentType`, `maxResults` | List content in a space |
| `create_confluence_blog_post` | `title`, `spaceKey` | `content`, `contentFromEnv`, `contentFile` | Create a blog post |
| `get_confluence_blog_posts` | `spaceKey` | `maxResults` | List recent blog posts |
| `get_confluence_templates` | - | `spaceKey` | List page templates |
| `export_confluence_page_pdf` | `pageId` | - | Get PDF export URL |
| `get_current_confluence_user` | - | - | Get the authenticated Confluence user |

## Response Shape

All actions return JSON: `{ "success": true, "data": { ... } }` or `{ "success": false, "error": "..." }`

### Non-Obvious Response Shapes

| Action | Key fields |
|---|---|
| `fetch_confluence_page` | `data.body.storage.value`, `data.version.number`, `data._links.webui` |
| `get_confluence_page_tree` | `data.tree[]` |
| `export_confluence_page_pdf` | `data.pdfUrl` |
| `search_confluence_cql` | `data.results[].id`, `.title`, `.space.key`, `.version.by.username`, `.version.when`, `.history.createdBy.username`, `.history.createdDate`, `._links.webui` (relative - prepend base URL) |
