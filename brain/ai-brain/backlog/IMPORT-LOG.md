# Import Log — File-to-Backlog Import History

> **Purpose:** Track every `/read-file-jot` invocation — which files were imported,
> when, how many items were extracted, and the resulting batch ID.
>
> **Usage:** Check this log before importing a file to detect re-imports. Use the
> `IMP-NNN` batch ID to find all items created from a specific file.

---

## Import Batches

| IMP-ID | Date | Time | Source File | BLIs Created | IDEAs Created | Merged | Skipped | Notes |
|---|---|---|---|---|---|---|---|---|

---

## Statistics

```text
Total imports:        0
Total BLIs created:   0
Total IDEAs created:  0
Total merged:         0
Total skipped:        0
Last import:          —
```

---

## Quick Reference

- **Find items from an import:** search for `import-batch: IMP-NNN` in frontmatter
- **View by source:** see `views/by-source.md` for items grouped by import batch
- **Re-import detection:** this log is checked in Phase 0 of `/read-file-jot`
- **IMP-NNN IDs are sequential** and never reused
