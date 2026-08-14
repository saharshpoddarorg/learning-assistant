---
id: BLI-098
title: Add tool-specific configuration validation tests
status: todo
priority: high
type: testing
created: 2026-07-31
updated: 2026-07-31
started: null
completed: null
blocked-since: null
review-since: null
epic: BLI-090
sprint: null
parent: null
sub-items: []
origin: null
estimated-effort: M
actual-effort: null
tags: [testing, validation, ci-cd, quality-assurance, tool-configs, pester]
origin-type: manual
import-batch: null
source-file: null
---

# BLI-098: Testing — Add tool-specific configuration validation tests

## Description

Create a comprehensive test suite that validates all tool-specific configurations
(`.github/`, `.claude/`, `.gemini/`, `.chatgpt/`) are correct, consistent, and ready
for production use. Tests run as part of the build/CI-CD pipeline and gate merges to main.

### Goals

1. Ensure all tool customization directories are valid before committing
2. Catch configuration errors (typos, broken links, format issues) early
3. Verify consistency across all tools
4. Support both local development and CI/CD pipeline
5. Provide clear test failure messages for debugging

### Scope

- ✅ Unit tests for sync and validation scripts (BLI-097)
- ✅ Integration tests for full workflows
- ✅ Configuration validation tests (directory structure, file format, metadata)
- ✅ Cross-tool consistency tests
- ✅ Performance tests (scripts complete in reasonable time)
- ✅ CI/CD integration (pre-commit hook, pipeline stage)
- ✅ Test fixtures and sample configurations

---

## Test Framework

**Technology:** PowerShell Pester v5 (built-in test framework)

**Why Pester?**
- Native to PowerShell
- Works cross-platform (Windows, macOS, Linux)
- Integrates with CI/CD (GitHub Actions, etc.)
- Good reporting and failure messages

---

## Test Suite Structure

```text
tools/tests/
├── README.md                           (test documentation)
├── unit/
│   ├── Transform.Tests.ps1             (transform-for-tool.ps1 tests)
│   ├── Validation.Tests.ps1            (validate-customization.ps1 tests)
│   ├── Sync.Tests.ps1                  (sync-customization.ps1 tests)
│   └── Drift.Tests.ps1                 (check-drift.ps1 tests)
├── integration/
│   ├── SyncWorkflow.Tests.ps1          (end-to-end sync tests)
│   ├── ValidateWorkflow.Tests.ps1      (end-to-end validation tests)
│   └── ConsistencyWorkflow.Tests.ps1   (cross-tool consistency tests)
├── fixtures/
│   ├── valid-github/                   (sample .github/ directory structure)
│   ├── valid-claude/                   (sample .claude/ directory structure)
│   ├── invalid-configs/                (broken configs for testing error detection)
│   ├── sample-instructions.md          (test data)
│   └── sample-prompts.md               (test data)
└── CI/
    ├── run-all-tests.ps1               (orchestrator for CI/CD)
    ├── generate-report.ps1             (test report generator)
    └── ci.yml                          (GitHub Actions workflow)
```

---

## Unit Tests Examples

### Transform Tests (`unit/Transform.Tests.ps1`)

```powershell
Describe 'Transform-for-Tool: Claude Format' {
  Context 'When transforming instruction to Claude' {
    It 'Should add Claude XML metadata' {
      # Arrange
      $input = @"
---
applyTo: "**/*.java"
---
# Java Style
Follow this style...
"@

      # Act
      $output = Transform-for-Tool -InputFile $input -TargetTool 'claude'

      # Assert
      $output | Should -Match '<claude-config>'
      $output | Should -Match '<applies-to>'
    }

    It 'Should preserve content' {
      # ... test that content is unchanged
    }

    It 'Should handle YAML with special characters' {
      # ... test edge cases
    }
  }
}

Describe 'Transform-for-Tool: Gemini Format' {
  Context 'When transforming prompt to Gemini' {
    It 'Should convert to JSON structure' {
      # ...
    }

    It 'Should truncate to Gemini limits' {
      # Verify content fits within Gemini's instruction limit
    }
  }
}

Describe 'Transform-for-Tool: ChatGPT Format' {
  Context 'When transforming to ChatGPT' {
    It 'Should extract text from markdown' {
      # ...
    }

    It 'Should be compatible with Custom Instructions format' {
      # ...
    }
  }
}
```

### Validation Tests (`unit/Validation.Tests.ps1`)

```powershell
Describe 'Validate-Customization: Structure' {
  Context 'When validating .github/ structure' {
    It 'Should pass with correct directory structure' {
      # Arrange: create valid structure with fixtures
      # Act: run validation
      # Assert: expect pass
    }

    It 'Should fail when instructions/ directory missing' {
      # Arrange: create structure without instructions/
      # Act: run validation
      # Assert: expect failure with specific error
    }

    It 'Should warn when optional directories are missing' {
      # ...
    }
  }
}

Describe 'Validate-Customization: Metadata' {
  Context 'When checking frontmatter' {
    It 'Should detect missing YAML frontmatter' {
      # Create file without frontmatter, expect validation failure
    }

    It 'Should validate required fields (name, type, etc.)' {
      # Create file with incomplete frontmatter, expect error
    }

    It 'Should accept all valid field combinations' {
      # Test various valid frontmatter combinations
    }
  }
}

Describe 'Validate-Customization: Links' {
  Context 'When checking markdown links' {
    It 'Should detect broken links' {
      # Create file with link to non-existent file, expect error
    }

    It 'Should accept valid internal links' {
      # Test various valid link formats
    }

    It 'Should accept external links' {
      # External https:// links should always pass
    }
  }
}

Describe 'Validate-Customization: File Size' {
  Context 'When checking tool-specific limits' {
    It 'Should fail if .claude/ file exceeds Claude limit' {
      # Verify size validation uses tool-specific limits from config
    }

    It 'Should warn if .gemini/ instruction exceeds 1500 chars' {
      # Gemini has strict character limit
    }
  }
}
```

### Sync Tests (`unit/Sync.Tests.ps1`)

```powershell
Describe 'Sync-Customization: File Propagation' {
  Context 'When syncing .github/ to .claude/' {
    It 'Should copy file if source newer' {
      # Create source, run sync, verify file appears in target with correct format
    }

    It 'Should skip file if target already current' {
      # Run sync twice, verify second run is idempotent
    }

    It 'Should apply format transformation' {
      # Verify YAML is transformed to Claude XML
    }
  }
}

Describe 'Sync-Customization: Multiple Targets' {
  Context 'When syncing to all tools' {
    It 'Should sync to all targets in one run' {
      # Verify .claude/, .gemini/, .chatgpt/ all updated in single run
    }

    It 'Should use appropriate transformation for each tool' {
      # Verify each tool gets its format adaptation
    }
  }
}
```

---

## Integration Tests Examples

### SyncWorkflow.Tests.ps1

```powershell
Describe 'Sync Workflow: End-to-End' {
  Context 'When syncing modified .github/ content' {
    It 'Should update all tool directories consistently' {
      # Arrange: create/modify file in .github/
      # Act: run sync-customization.ps1
      # Assert: verify file appears in .claude/, .gemini/, .chatgpt/ with correct formats
    }

    It 'Should maintain cross-tool consistency' {
      # After sync, run validation on all tools
      # Verify all pass validation
    }

    It 'Should not require manual intervention after sync' {
      # Run sync, then validate without any manual fixes needed
    }
  }
}

Describe 'Sync Workflow: Large Batch' {
  Context 'When syncing many files' {
    It 'Should handle bulk updates efficiently' {
      # Modify multiple files in .github/
      # Run sync on entire directory tree
      # Verify all files synced correctly
    }

    It 'Should complete in reasonable time' {
      # Measure performance; sync should be < 30 seconds
    }
  }
}
```

### ConsistencyWorkflow.Tests.ps1

```powershell
Describe 'Cross-Tool Consistency: Semantic' {
  Context 'When instructions exist in multiple tools' {
    It 'Should have same core message in all formats' {
      # Read instruction from .github/, .claude/, .gemini/, .chatgpt/
      # Verify they all convey the same core guidance (within tool limits)
    }

    It 'Should not contradict itself across tools' {
      # Verify no conflicting instructions exist
      # E.g., if .github/ says "use Streams", .claude shouldn't say "avoid Streams"
    }
  }
}

Describe 'Cross-Tool Consistency: Completeness' {
  Context 'When all tools should have same content' {
    It 'Should have equivalent instructions in all tools' {
      # Count files in .github/instructions/
      # Count files in .claude/instructions/ (should match, accounting for format differences)
      # Repeat for all tools
    }

    It 'Should flag missing content' {
      # If .claude/instructions/ has fewer files than .github/, flag as warning
    }
  }
}
```

---

## Configuration Validation Tests

### Fixture-Based Tests

Create sample configurations in `fixtures/`:

```text
fixtures/
├── valid-github/                    (passing validation)
│   ├── .github/
│   │   ├── instructions/
│   │   │   └── sample.md
│   │   ├── prompts/
│   │   │   └── sample.prompt.md
│   │   └── skills/
│   │       └── SKILL.md
│   ├── .claude/
│   ├── .gemini/
│   └── .chatgpt/
└── invalid-configs/                 (should fail validation)
    ├── missing-metadata/
    ├── broken-links/
    ├── oversized-files/
    └── format-errors/
```

**Test:**

```powershell
Describe 'Validation with Fixtures' {
  Context 'When validating fixtures/valid-github/' {
    It 'Should pass all checks' {
      $result = Validate-Customization -Tool 'github' -Path 'fixtures/valid-github'
      $result.Passed | Should -Be $true
      $result.Errors | Should -HaveCount 0
    }
  }

  Context 'When validating fixtures/invalid-configs/missing-metadata' {
    It 'Should detect missing frontmatter' {
      $result = Validate-Customization -Path 'fixtures/invalid-configs/missing-metadata'
      $result.Passed | Should -Be $false
      $result.Errors | Should -Contain (
        'Missing YAML frontmatter in file: instructions/bad.md'
      )
    }
  }
}
```

---

## CI/CD Integration

### GitHub Actions Workflow (`.github/workflows/validate-customization.yml`)

```yaml
name: Validate Customization

on:
  push:
    paths:
      - '.github/**'
      - '.claude/**'
      - '.gemini/**'
      - '.chatgpt/**'
      - 'tools/tests/**'
  pull_request:
    paths:
      - '.github/**'
      - '.claude/**'
      - '.gemini/**'
      - '.chatgpt/**'

jobs:
  validate:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Install PowerShell
        run: |
          sudo apt-get update
          sudo apt-get install -y powershell

      - name: Run Validation Tests
        run: |
          pwsh -Command "& { Invoke-Pester -Path 'tools/tests' -OutputFormat JunitXml -OutputFile test-results.xml }"

      - name: Run Validation Scripts
        run: |
          pwsh -Command "& { .\tools\validate-customization.ps1 -All -Strict }"

      - name: Upload Test Results
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: test-results
          path: test-results.xml

      - name: Comment PR with Results
        if: github.event_name == 'pull_request'
        uses: actions/github-script@v6
        with:
          script: |
            // Parse test results and comment on PR
            const fs = require('fs');
            const junit = fs.readFileSync('test-results.xml', 'utf8');
            // ... parse and comment
```

### Pre-Commit Hook (`.githooks/pre-commit`)

```bash
#!/bin/bash
# Run validation before allowing commit

pwsh -Command "& {
    .\tools\validate-customization.ps1 -All -Strict
    if ($LASTEXITCODE -ne 0) {
        exit 1
    }
}"

if [ $? -ne 0 ]; then
    echo "❌ Customization validation failed. Fix errors and try again."
    exit 1
fi

echo "✅ Customization validation passed."
```

---

## Test Run Command

```powershell
# Run all tests with detailed output
.\tools\tests\CI\run-all-tests.ps1 -Verbose

# Run specific test file
Invoke-Pester -Path 'tools/tests/unit/Transform.Tests.ps1' -Show All

# Generate HTML report
Invoke-Pester -Path 'tools/tests' -OutputFormat NunitXml -OutputFile results.xml
.\tools\tests\CI\generate-report.ps1 -InputFile results.xml -OutputFile test-report.html
```

---

## Expected Test Coverage

| Component | Coverage | Goal |
|---|---|---|
| Transform functions | All branches | 100% |
| Validation checks | All checks + error paths | 100% |
| Sync logic | Add, update, delete, skip cases | 100% |
| Drift detection | Various time thresholds | 95%+ |
| Integration workflows | Happy path + error cases | 90%+ |

---

## Acceptance Criteria

- [ ] All unit tests written and passing
- [ ] All integration tests written and passing
- [ ] Test fixtures created (valid + invalid configs)
- [ ] At least 30 test cases total
- [ ] Coverage report shows ≥90% coverage
- [ ] CI/CD workflow configured and working
- [ ] Pre-commit hook created and documented
- [ ] Test documentation written (`tools/tests/README.md`)
- [ ] All tests run in < 2 minutes (performance acceptable)
- [ ] Test failures provide actionable error messages
- [ ] Tests can run on Windows, macOS, and Linux

---

## Testing Checklist

### Local Development

- [ ] Run `Invoke-Pester tools/tests -Show All` — all tests pass
- [ ] Run `.\tools\validate-customization.ps1 -All` — validation passes
- [ ] Modify `.github/`, run sync, run validation — all pass
- [ ] Create intentionally broken config, run validation — errors detected correctly

### CI/CD

- [ ] GitHub Actions workflow runs on push to any .github/ or tool file
- [ ] Workflow runs on PR, reports results
- [ ] Workflow fails if validation fails (blocks merge)
- [ ] Test report generated and uploaded
- [ ] Pre-commit hook blocks commits with invalid configs

---

## Future Enhancements (Phase 2)

- [ ] Performance benchmarks (track sync time over releases)
- [ ] Coverage reports published to PR comments
- [ ] Automated regression testing against real-world configs
- [ ] Visual test report dashboard
- [ ] Slack notifications on test failures

---

## Dependencies & Blockers

- **Blocked by:** BLI-093, BLI-094, BLI-095, BLI-097 (need implementations to test)
- **Unblocks:** Merging BLI changes to main (tests gate the pipeline)

---

## Notes

- Keep tests independent (can run in any order)
- Use fixtures for reproducible test data
- Mock external resources (API calls, file systems) where possible
- Document any platform-specific test skips
- Consider performance impact of validation in hot path (e.g., pre-commit)
