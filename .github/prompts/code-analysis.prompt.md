---
name: code-analysis
description: 'Analyse code for structure, patterns, smells, and improvements — with auto session capture'
agent: ''
tools: ['codebase', 'fetch']
---

## Target

${input:target:What code to analyse? (e.g., OrderService, PaymentGateway.processPayment, checkout flow)}

## Goal

${input:goal:What's the goal? (review — find issues / understand — learn the code / refactor — plan changes / patterns — identify design patterns)}

## Level

${input:level:Depth? (quick — high-level overview / standard — structure + findings / thorough — full analysis with recommendations)}

## Instructions

Perform a **code analysis** on the target code. Adapt depth based on the level selected.

### Analysis Structure

#### 1. Target Summary

| Property | Value |
|---|---|
| Class | `ClassName` |
| Method | `methodName` (or "class-level") |
| Package | `package.path` |
| File | `src/path/to/File.java` |
| Purpose | One-sentence description |

#### 2. Code Structure Overview

- How the code is organized (classes, methods, inheritance, interfaces)
- Key dependencies (what it uses, what uses it)
- Design patterns in play (Service, Repository, Strategy, Factory, etc.)
- Responsibility assessment — does it do one thing well, or too many?

#### 3. Code Block Breakdown

Split the code into **functional blocks by cohesion**. For each block:

- **Name** — descriptive label for what this block does
- **Lines** — line range in the source file
- **Code** — show the actual code snippet
- **Purpose** — what this block accomplishes
- **Connection** — how it relates to the blocks before/after it

Aim for 3-8 blocks per method. This is the most valuable section — be thorough.

#### 4. Findings Table

| # | Finding | Severity | Category | Line(s) |
|---|---|---|---|---|
| 1 | Description | high/medium/low | smell/bug/pattern/performance/security | L42-45 |

**Categories:**

- `smell` — code smell (long method, god class, feature envy, etc.)
- `bug` — actual or potential bug
- `pattern` — design pattern observation (good or misused)
- `performance` — performance concern
- `security` — security issue (injection, validation, auth)

#### 5. Proposed Changes (if goal is review or refactor)

For each finding, suggest a concrete improvement:

| # | Finding | Proposed Change | Impact | Effort |
|---|---|---|---|---|
| 1 | Ref to finding | What to do | high/medium/low | small/medium/large |

#### 6. Key Takeaways

- 3-5 bullet points summarizing the analysis
- Overall quality assessment (good/needs-work/concerning)
- One recommended next action

### Level Adaptation

| Level | Sections | Depth |
|---|---|---|
| `quick` | 1, 2, 6 | Overview only — skip block breakdown and findings |
| `standard` | 1, 2, 3, 4, 6 | Full structure + findings, skip proposals |
| `thorough` | All (1-6) | Complete analysis with proposals |

### Output Rules

- Show actual code blocks — not just descriptions
- Include line numbers when referencing specific code
- Be honest — if the code is good, say so; if it has problems, be direct
- For `understand` goal, focus on explanation over criticism
- For `review` goal, focus on findings and severity
- For `refactor` goal, focus on proposals with effort estimates

### Session Capture — Auto-Save to Brain

After completing the analysis, **automatically capture** the full output as a session file.

**Capture steps:**

1. **Get the current timestamp** — run `Get-Date -Format "yyyy-MM-dd_hh-mmtt"` and
   `Get-Date -Format "yyyy-MM-dd"` and `Get-Date -Format "hh:mm tt"`
2. **Determine the domain:**
   - If the code is in this repo or a work project → `work`
   - If the code is a personal/side project → `personal`
3. **Build the file path:**
   - Work domain: `brain/ai-brain/sessions/work/code-analysis/`
   - Personal domain: `brain/ai-brain/sessions/personal/software-dev/code-review/`
4. **Build the filename:** `<date>_<time>_code-analysis_<class-method-kebab>.md`
   - Example: `2026-04-20_09-21pm_code-analysis_order-service-calculate-total.md`
5. **Use the `code-analysis-capture.md` template** from
   `brain/ai-brain/sessions/_templates/` — fill in ALL sections:
   - Frontmatter: date, time, domain, category, project, subject, tags, code-target
   - Analysis content from the sections above
   - Key Outcomes, Follow-Up, Session Metadata
6. **Write the file** to the path determined in step 3
7. **Check escalation** — count files in the target folder; if 3+ files share the same
   class prefix, trigger sub-package escalation per chat-capture instructions
8. **Append to SESSION-LOG.md** — add a row to `brain/ai-brain/sessions/SESSION-LOG.md`
9. **Report** — tell the user: "Analysis captured to `sessions/<path>`"

**Content emphasis for the captured file:**

- **Code Block Breakdown** must include actual code snippets — a developer reading
  the file should see the code alongside the explanation
- **Findings Table** must be actionable — each finding has severity and category
- The file must be **self-contained** — readable without opening the source code
- High-level overview should be understandable in 30 seconds
