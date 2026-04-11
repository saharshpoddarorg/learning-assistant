---
id: IDEA-001
title: Standardize purpose headers in prompts and skills
status: raw
type: idea
created: 2026-04-11
updated: 2026-04-11
tags: [templates, prompts, skills, standardisation, conventions]
origin-type: file-import
import-batch: IMP-001
source-file: "D:\\workdir\\MG_FTE\\notepad\\personal dev\\learning-assistant\\gpt.txt"
---

# IDEA-001: Standardize purpose headers in prompts and skills

## Raw Thought

> "purpose of each template" — Each `.prompt.md` and `SKILL.md` file should have a
> standardized purpose/description header that clearly states what the file does, when
> to invoke it, and what it covers. Some files already have this in frontmatter
> `description` or opening paragraphs, but the format is inconsistent.

## Context

- Many prompt files have varying levels of self-description
- Skills have `description` in frontmatter but the format and depth varies
- Standardizing would help both humans and Copilot understand when to activate each primitive
- Could be part of a broader "conventions audit" of all customization files

## Potential Directions

- Define a standard "Purpose" section template for all `.prompt.md` files
- Audit existing `description` fields in SKILL.md frontmatter for consistency
- Create a linting rule that checks for the presence and format of purpose headers
- Tie into BLI-021 (reusable content templates) — templates for templates

## Attachments & References

| Type | Path / URL | Added | Notes |
|---|---|---|---|
| Source file | `D:\workdir\MG_FTE\notepad\personal dev\learning-assistant\gpt.txt` | 2026-04-11 | Extracted from file import IMP-001 |

## Activity Log

| Date | Time | Actor | Action | Details |
|---|---|---|---|---|
| 2026-04-11 | 10:08 PM | system | created | Created via /read-file-jot from IMP-001 |

## Tags for Discovery

- Related items: [BLI-021](../features/BLI-021_reusable-content-templates.md) (template system)
- Concept: standardisation, consistency, conventions, meta-templates
