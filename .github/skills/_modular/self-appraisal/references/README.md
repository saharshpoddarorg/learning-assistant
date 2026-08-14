# Self-Appraisal Reference Documents

This folder holds the developer's own past growth-talk and self-appraisal submissions,
used by Phase 3 to refine narrative style, structure, and quantification patterns. It is
personal HR data, not project documentation, so the actual files never enter git history.

## Layout

```text
references/
  README.md                        ← this file (tracked)
  private/                         ← gitignored entirely — never committed
    raw/
      growth-talk/                 ← original .docx/.pdf growth-talk exports
      self-appraisal-form/         ← original .docx/.pdf self-appraisal submissions
    converted/
      growth-talk/                 ← .md output from the document-converter skill
      self-appraisal-form/         ← .md output from the document-converter skill
```

## Usage

1. Drop 2-3 years of past submissions into `private/raw/growth-talk/` and
   `private/raw/self-appraisal-form/`, named by cycle (e.g., `FY2023-24.docx`). **Prefer
   `.docx` over `.pdf`** if you have a choice — opening a `.pdf` via the converter can hang
   Word COM automation indefinitely (a confirmed, environment-level limitation, not a bug);
   `.docx` has no such risk.
2. Convert each file to Markdown using the
   [`document-converter`](../../document-converter/SKILL.md) skill (target `.md`, not
   `.txt` — Markdown preserves headings/bold/lists/tables, plain text does not); save the
   output to the matching `private/converted/<category>/` folder with the same base filename.
3. Ask the `self-appraisal` skill to run its Phase 3 workflow — it reads only the
   `private/converted/` Markdown files, treating `growth-talk` and `self-appraisal-form` as
   separate style references.

## Guardrails

- Never commit anything under `private/` — it is excluded in the repo `.gitignore`.
- Never paste this content into a public issue, PR, or chat outside this workspace.
- Treat the converted text the same as the original documents: personal HR data.
