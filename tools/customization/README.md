# Customization Compliance Scripts

This folder provides a starter framework for cross-agent customization compliance.

## Scripts

- `validate-customization.ps1` : validates profile structure and optional deep parity.
- `check-drift.ps1` : detects stale or missing target artifacts relative to source.
- `sync-customization.ps1` : syncs source customization files to target profiles.
- `transform-for-tool.ps1` : transforms one file for a specific target profile.

## Profiles

Profiles are defined in `config/tool-specs.json`.

Current profiles:

- `github-copilot` (source)
- `claude-family`
- `gemini`
- `chatgpt`
- `antigravity` (validation-first profile)

## Quick Start

```powershell
# Validate all profiles (structure checks)
.\tools\customization\validate-customization.ps1 -All

# Deep validation with JSON report
.\tools\customization\validate-customization.ps1 -All -Deep -Report .\out\customization-validate.json

# Drift check
.\tools\customization\check-drift.ps1 -Days 14 -Report .\out\customization-drift.json

# Dry-run sync to all targets
.\tools\customization\sync-customization.ps1 -DryRun

# Transform one file for Claude-family
.\tools\customization\transform-for-tool.ps1 -InputFile .github\copilot-instructions.md -TargetTool claude-family
```

## Notes

- Source defaults to `.github`.
- Deep validation and drift checks become more useful after target profile directories exist.
- This scaffold is intentionally minimal and intended to be extended under BLI-101..105.

## CI Integration

GitHub Actions workflow:

- `.github/workflows/customization-compliance.yml`

Current CI behavior:

1. Required gate: deep validation of source profile (`github-copilot`).
2. Full profile validation in non-strict mode for phased rollout visibility.
3. Drift check runs as informational (`continue-on-error`).
4. Sync runs in dry-run mode to show pending adaptation changes.
5. Reports are uploaded as CI artifacts.
