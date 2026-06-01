---
name: import-skill
description: Import and enhance an external skill (folder, file, GitHub URL, ZIP, or pasted content) into the skills library — pair-programming workflow with sanitisation, overlap detection, enhancements, and 3-way merge on re-import.
agent: agent
tools: ['editFiles', 'codebase', 'runInTerminal', 'fetch']
---

## Source

${input:source:Path, URL, ZIP file, or paste content (e.g. "C:\\Users\\me\\Downloads\\confluence-skill", "https://github.com/owner/repo/tree/main/skill", or paste SKILL.md content)}

## Mode

${input:mode:full | overlap-only | sanitise-only | re-import | enhance-existing  (default: full)}

---

Run the `skill-importer` skill at
`.github/skills/_modular/skill-importer/SKILL.md` exactly. Treat `${input:source}`
as the import source and `${input:mode}` as the mode (default `full`).

All operating principles in that skill are non-negotiable:

- Pair-programmed: checkpoint at every phase boundary, never auto-advance
- Dry-run every write, approve per file
- Always stash the untouched original under `.github/skills/_imported-originals/<name>/<YYYY-MM-DD>/`
- Always add provenance frontmatter
- Block on unsanitised secrets and on confidentiality markers
- Always classify overlap against existing skills before writing

Begin with Phase 1.
