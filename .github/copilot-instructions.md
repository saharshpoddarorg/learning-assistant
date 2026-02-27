# Project Instructions — Learning Assistant

## Overview
- **Project:** Learning Assistant — a simple Java project for learning Copilot customization
- **Language:** Java 21+
- **Build:** Manual compilation (no build tool yet)
- **Purpose:** Hands-on experimentation with GitHub Copilot's customization features

## Project Structure
```
learning-assistant/
├── .github/              ← Copilot customization files (you're learning this!)
├── src/
│   └── Main.java         ← Entry point
└── .gitignore
```

## Coding Conventions

### Naming
- **Classes:** `UpperCamelCase` (e.g., `StudentManager`, `OrderService`)
- **Methods:** `lowerCamelCase` (e.g., `calculateTotal`, `getStudentName`)
- **Variables:** `lowerCamelCase`, descriptive (e.g., `totalPrice`, not `tp`)
- **Constants:** `UPPER_SNAKE_CASE` (e.g., `MAX_RETRY_COUNT`)
- **Packages:** all lowercase (e.g., `com.learning.service`)

### Code Style
- Use `final` for variables that don't change
- Prefer `var` for local variables when the type is obvious (Java 10+)
- One public class per file, class name matches filename
- Use `Logger` instead of `System.out.println` (except in learning examples)

### Methods
- Keep methods under 30 lines
- Each method should do exactly one thing
- Add Javadoc to all public methods:
  ```java
  /**
   * Calculates the total price including tax.
   *
   * @param price    the base price
   * @param taxRate  the tax rate as a decimal (e.g., 0.10 for 10%)
   * @return the total price with tax
   */
  public double calculateTotal(double price, double taxRate) { ... }
  ```

### Error Handling
- Catch specific exceptions, never generic `Exception`
- Always include a helpful error message
- Use try-with-resources for closeable resources

### File Organization (order within a class)
1. Static fields
2. Instance fields
3. Constructors
4. Public methods
5. Private/helper methods
6. `toString()`, `equals()`, `hashCode()`

## Do's and Don'ts

### Do:
- ✅ Use descriptive variable names (minimum 3 characters, except loop counters)
- ✅ Add comments explaining WHY, not WHAT
- ✅ Use `Objects.requireNonNull()` for null-checking constructor parameters
- ✅ Include `@Override` on all overridden methods
- ✅ Close resources with try-with-resources

### Don't:
- ❌ Don't use single-letter variables (except `i`, `j`, `k` in loops)
- ❌ Don't leave empty catch blocks
- ❌ Don't use `==` to compare Strings (use `.equals()`)
- ❌ Don't hardcode values — extract to constants
- ❌ Don't write methods longer than 30 lines

## Commit Guidelines

> This project follows [Conventional Commits](https://www.conventionalcommits.org/) (v1.0.0).
> Every commit must be a clean, self-contained logical unit.

### Commit Message Format

```
<type>(<scope>): <subject>          ← subject: ≤ 50 chars (hard limit 72)
<blank line>
<body>                               ← wrap at 72 chars; explain WHY, not WHAT
<blank line>
<footer(s)>                          ← BREAKING CHANGE, closes #N, attribution
```

**The subject line is mandatory. Body and footers are optional but strongly encouraged**
for non-trivial changes.

### Type Reference

| Type | When to use |
|------|-------------|
| `feat` | New feature or capability visible to users / callers |
| `fix` | Bug fix — corrects incorrect behaviour |
| `refactor` | Code restructuring with no behaviour change (split, rename, extract) |
| `docs` | Documentation only (README, Javadoc, prompt/skill files, comments) |
| `style` | Formatting, whitespace, import ordering — no logic change |
| `test` | Adding or updating tests |
| `chore` | Build scripts, CI config, dependency bumps, tooling |
| `perf` | Performance improvement |
| `ci` | CI/CD pipeline changes |
| `revert` | Reverts a previous commit (reference original SHA in body) |

### Subject Line Rules

- **Imperative mood**: "Add", "Fix", "Remove" — not "Added", "Fixed", "Removes"
- **≤ 50 characters** (soft target); **72-character hard limit**
- **No period** at the end
- **Capital first letter** after the colon
- **Scope is optional** but helps when the change touches one module: `feat(vault): …`

### Body Rules

- Separate from subject with a **blank line**
- Wrap lines at **72 characters**
- Explain **WHY** this change was made (motivation, context, trade-offs)
- Explain **WHAT** changed if it isn't obvious from the subject
- Use bullet points (`-`) for lists of changes

### Footers

```
BREAKING CHANGE: <description>   ← required if API/interface changes
Closes #<issue-number>           ← auto-closes linked GitHub issue
Co-authored-by: name <email>     ← for pair/mob contributions
— created by gpt                 ← AI attribution (see below)
```

**Breaking changes** can also be marked with `!` after the type:
`refactor!(vault): rename ResourceVault to LearningVault`

### Atomic Commit Principle

- **One logical change per commit** — reviewers should be able to understand, revert, or
  cherry-pick any commit in isolation
- Don't mix a bug fix with a new feature in one commit
- Don't mix code changes with formatting/whitespace changes
- When a change spans many files, it can still be one commit — if it implements a single idea
- Stage and review before committing: `git diff --staged`

### Author Attribution

Always append as the last footer line:

| Who wrote it | Suffix |
|---|---|
| AI-generated code | `— created by gpt` |
| Human-edited AI output | `— assisted by gpt` |
| Human only | *(no suffix)* |

### Do ✅ / Don't ❌

**Do:**
```
feat(vault): Add VcsResources provider with 9 curated resources

Git foundations, branching strategies (GitFlow, GitHub Flow, TBD),
Conventional Commits, SemVer, and git internals in a dedicated
provider — keeping VCS concerns separate from build tooling.

— created by gpt
```

```
fix(config): Null-check API key before making HTTP request

Without the guard, a missing key caused a NullPointerException
deep in the HTTP client rather than a clear ConfigValidationException
at startup. Added Objects.requireNonNull with an explicit message.

— assisted by gpt
```

```
refactor(vault): Split GitAndBuildResources into VcsResources + BuildToolsResources

VCS (version control workflows) and build automation (Maven, Gradle,
Make) belong to different conceptual domains and have different
audiences. Keeping them in one provider violated single-responsibility
and made the vault harder to extend incrementally.

- VcsResources: 9 resources — git foundations, branching, conventions
- BuildToolsResources: 11 resources — Maven (5), Gradle (3), Make/Bazel/npm
- BuiltInResources: imports sorted; PROVIDERS updated

— created by gpt
```

**Don't:**
```
# ❌ vague subject
update stuff

# ❌ past tense
fixed the bug in config loader

# ❌ mixing unrelated changes
feat: Add VcsResources; also fix typo in README and reformat imports

# ❌ subject too long (> 72 chars)
feat(learningresources): Add a comprehensive vault of resources about version control systems and git

# ❌ missing type prefix
Add new provider class for VCS resources
```

### Examples with Breaking Changes

```
feat(api)!: Replace List<String> tags with Set<String> in LearningResource

Using Set enforces uniqueness at the model level and improves
look-up performance from O(n) to O(1). Callers that relied on
tag insertion order must update accordingly.

BREAKING CHANGE: LearningResource.tags() now returns Set<String>
instead of List<String>. Existing providers that use List.of(...)
must be updated to Set.of(...).

— created by gpt
```

### Reference

- Specification: [conventionalcommits.org](https://www.conventionalcommits.org/en/v1.0.0/)
- Related: [Semantic Versioning](https://semver.org/) — patch/minor/major maps to fix/feat/BREAKING CHANGE
