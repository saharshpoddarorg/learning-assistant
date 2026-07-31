# Jira Skill

PAT-authenticated Jira REST API operations (issues, JQL, sprints, boards, worklogs, transitions, labels, watchers, bulk ops).

## Adding a New Capability

A "capability" is a new CLI action (e.g. `fetch_jira_issue`, `transition_issue`).

1. **Define the action** in `scripts/jira_cli.js`:
   - Add a case to the action dispatcher with the action name.
   - Implement the REST call using the existing `apiFetch()` helper.
   - Return `{ success: true, data: {...} }` on success.

2. **Add PowerShell fallback** (optional) in `scripts/jira_cli.ps1`:
   - Add a matching case for the same action name.
   - Use `Invoke-RestMethod` with the same endpoint and parameters.

3. **Document in `references/action-catalog.md`**:
   - Action name, required args, optional args, response shape.
   - Follow the existing table format.

4. **Add usage examples** in `references/usage-recipes.md`:
   - Show the `CLI_JSON_ARGS` + invocation pattern.
   - Include expected output snippets.

5. **Update SKILL.md** if the new capability changes the skill description or architecture.

## Updating This Skill

- **Fix a bug:** Edit `scripts/jira_cli.js` (and `.ps1` if applicable). Test locally with a `.env` file.
- **Change auth or base URL logic:** Edit the `.env` lookup section in the CLI scripts.
- **Update documentation:** Edit files in `references/` and/or `SKILL.md`.
- **Tone/disclaimer changes:** Edit `references/tone-and-disclaimer.md`.

## Testing Locally

```powershell
$env:CLI_JSON_ARGS = '{"issueKey":"PROJ-123"}'
node scripts/jira_cli.js fetch_jira_issue
```

Verify the JSON output has `"success": true` and the expected data shape.
