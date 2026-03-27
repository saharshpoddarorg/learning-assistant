---
id: BLI-018
title: Code snippet & template manager
status: todo
priority: low
type: feature
created: 2026-03-28
updated: 2026-03-28
epic: EPIC-003
parent: null
sub-items: []
origin: null
tags: [project:snippet-manager, code-snippets, templates, boilerplate, reuse, gist]
---

# BLI-018: Code snippet & template manager

## Description

Build a personal code snippet and project template manager that collects reusable
code patterns, boilerplate templates, configuration snippets, and shell commands
into a searchable, taggable library. Integrate with GitHub Gists for cloud sync,
VS Code snippets for IDE integration, and the brain workspace for cross-referencing
with learning resources.

## Acceptance Criteria

- [ ] Snippet data model — language, title, description, code, tags, source URL
- [ ] GitHub Gist sync — push/pull snippets to/from personal Gists
- [ ] VS Code snippet export — generate `.code-snippets` files from managed snippets
- [ ] IntelliJ live template export — generate live templates for IntelliJ IDEA
- [ ] Language-specific libraries — organize snippets by Java, Python, Bash, SQL, etc.
- [ ] Template scaffolding — full project templates (Spring Boot starter, React app, CLI tool)
- [ ] Tag-based search — find snippets by tags, language, or free-text search
- [ ] CLI interface — `snippet add`, `snippet search`, `snippet copy` from terminal
- [ ] Version tracking — keep history of snippet changes over time
- [ ] Import from existing sources — import from VS Code snippets, IntelliJ templates, Gists
- [ ] Brain cross-reference — link snippets to vault learning resources and skill files
- [ ] Sharing — export snippet collections as Markdown or JSON for sharing

## Notes

- GitHub Gists API — easy CRUD for snippets, supports multi-file gists
- VS Code snippet format: JSON with `$1`, `$2` placeholders, `prefix` trigger
- IntelliJ live templates: XML format in `.idea/templates/`
- Consider Ray.so or Carbon.sh for pretty-printing snippets for sharing
- Alternative to building: evaluate existing tools (Lepton, massCode, SnippetStore)
- Integration with Copilot — "give me my Java stream snippet for filtering nulls"
