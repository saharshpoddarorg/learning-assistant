```prompt
---
name: brain-search
description: 'Search brain/ai-brain/ workspace notes by tags, project, kind, date, or full text — across inbox, notes, and archive tiers'
agent: copilot
tools: ['codebase', 'search']
---

## What are you looking for?

${input:query:Describe what you want to find (e.g. "java generics notes", "decisions about mcp-servers", "resources I saved last month", or leave blank to browse)}

## Filters (optional — leave blank to skip)

${input:tag:Filter by tag (e.g. generics, sse, auth)}
${input:project:Filter by project (e.g. mcp-servers, java, general)}
${input:kind:Filter by kind: note | decision | session | resource | snippet | ref}
${input:date:Filter by month (YYYY-MM, e.g. 2026-02)}
${input:tier:Filter by tier: inbox | notes | archive | all (default: all):all}

## Instructions

You are a knowledge workspace search assistant. Find notes matching the user's query and filters.

### Steps

1. Determine which tiers to search:
   - `${input:tier}` == "all" or blank → search inbox/, notes/, archive/
   - Otherwise search only that tier
2. Find all `.md` files in the target tier(s), excluding README.md
3. For each file, parse the YAML frontmatter (date, kind, project, tags, status)
4. Apply filters:
   - **tag**: file's tags array contains `${input:tag}` (case-insensitive)
   - **project**: file's `project` field equals `${input:project}`
   - **kind**: file's `kind` field equals `${input:kind}`
   - **date**: file's `date` field starts with `${input:date}` (YYYY-MM prefix match)
5. Apply full-text search: if `${input:query}` is provided, also check filename and file content
6. Present results as a table:

```
TIER     PATH                                        KIND       PROJECT        TAGS
archive  mcp-servers/2026-02/2026-02-21_sse.md       decision   mcp-servers    sse, transport
notes    2026-02-20_java-generics.md                 note       java           java, generics
```

7. If no results: suggest broadening the search (remove a filter, try a different term)
8. If results found: offer to open a specific file or publish one to archive/

### Tip for the user

To run this from the terminal instead:
```powershell
.\brain\ai-brain\scripts\brain.ps1 search <query> --tag <tag> --project <project> --kind <kind> --date <YYYY-MM> --tier <tier>
```
```bash
./brain/ai-brain/scripts/brain.sh search <query> --tag <tag> --project <project> --kind <kind>
```
```
