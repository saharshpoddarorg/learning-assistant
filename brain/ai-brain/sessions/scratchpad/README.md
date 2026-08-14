# scratchpad/ — Temporary Session Recordings

Use this folder for session recordings you want to keep only temporarily while working.

Unlike the permanent `work/` and `personal/` session trees, `scratchpad/` is for captures
that do not yet need full classification, templates, append-only logs, or git history.

---

## Rules

- Files in this folder are **gitignored** by the local `.gitignore` file.
- Do **not** append scratchpad entries to `../SESSION-LOG.md` or `../CAPTURE-LOG.md`.
- Do **not** use `_templates/` for scratchpad unless you later promote the file.
- Keep files flat in this folder. No escalation or sub-packages.

---

## Naming

```text
YYYY-MM-DD_HH-MMtt_scratchpad_<subject>.md
```

Example:

```text
2026-07-31_09-45am_scratchpad_temp-api-review.md
```

---

## Promotion

If a scratchpad recording becomes worth keeping:

1. Move it into the correct `work/` or `personal/` category.
2. Apply the normal session frontmatter and content structure.
3. Append the corresponding entries to `SESSION-LOG.md` and `CAPTURE-LOG.md`.

At that point it becomes a standard persistent session capture.
