# PSI Tools Skill

Java code intelligence via IntelliJ PSI Tools plugin (class structure, method bodies, usages, call graphs, inspections).

## Adding a New Capability

A "capability" is a new CLI action (e.g. `get_class_structure`, `find_usages`).

1. **Define the action** in `scripts/psi_tools_cli.ps1`:
   - Add a case to the action dispatcher with the action name.
   - Implement the HTTP call to the PSI Tools plugin endpoint.
   - Return `{ success: true, data: {...} }` on success.

2. **Document in `references/action-catalog.md`**:
   - Action name, required args, optional args, response shape.
   - Follow the existing table format.

3. **Add usage examples** in `references/usage-recipes.md`:
   - Show the `CLI_JSON_ARGS` + invocation pattern.
   - Include expected output snippets.

4. **Update SKILL.md** if the new capability changes the skill description or architecture.

## Updating This Skill

- **Fix a bug:** Edit `scripts/psi_tools_cli.ps1`. Test with IntelliJ running and the PSI Tools plugin active.
- **Change server port or connection logic:** Edit the HTTP base URL section in the CLI script.
- **Update documentation:** Edit files in `references/` and/or `SKILL.md`.

## Testing Locally

```powershell
$env:CLI_JSON_ARGS = '{"className":"com.example.MyClass"}'
pwsh scripts/psi_tools_cli.ps1 get_class_structure
```

Verify the JSON output has `"success": true` and the expected data shape. Requires IntelliJ with the PSI Tools plugin running on localhost.
