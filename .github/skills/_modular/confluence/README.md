# Confluence Skill

PAT-authenticated Confluence REST API operations (pages, blogs, CQL search, templates, comments, labels, page trees, PDF export, version management).

## Adding a New Capability

A "capability" is a new CLI action (e.g. `fetch_confluence_page`, `create_confluence_page`).

1. **Define the action** in `scripts/confluence_cli.js`:
   - Add a case to the action dispatcher with the action name.
   - Implement the REST call using the existing `apiFetch()` helper.
   - Return `{ success: true, data: {...} }` on success.

2. **Add PowerShell fallback** (read-only actions only) in `scripts/confluence_cli.ps1`:
   - Add a matching case for the same action name.
   - Use `Invoke-RestMethod` with the same endpoint and parameters.
   - Note: HTML-write actions must remain Node.js-only due to PowerShell 5.1 encoding limitations.

3. **Document in `references/action-catalog.md`**:
   - Action name, required args, optional args, response shape.
   - Follow the existing table format.

4. **Add usage examples** in `references/usage-recipes.md`:
   - Show the `CLI_JSON_ARGS` + invocation pattern.
   - Include expected output snippets.

5. **Document formatting rules** (if HTML/storage-format is involved) in `references/confluence-formatting.md`.

6. **Update SKILL.md** if the new capability changes the skill description or architecture.

## Updating This Skill

- **Fix a bug:** Edit `scripts/confluence_cli.js` (and `.ps1` if applicable). Test locally with a `.env` file.
- **Change auth or base URL logic:** Edit the `.env` lookup section in the CLI scripts.
- **Update documentation:** Edit files in `references/` and/or `SKILL.md`.
- **Tone/disclaimer changes:** Edit `references/tone-and-disclaimer.md`.
- **Formatting rules:** Edit `references/confluence-formatting.md`.

## Testing Locally

```powershell
$env:CLI_JSON_ARGS = '{"pageId":"123456"}'
node scripts/confluence_cli.js fetch_confluence_page
```

Verify the JSON output has `"success": true` and the expected data shape.
