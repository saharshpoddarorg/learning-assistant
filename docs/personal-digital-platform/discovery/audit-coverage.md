# Repository extraction audit

Date: 2026-10-27. Source: [learning-assistant](https://github.com/saharshpoddarorg/learning-assistant).
Pinned main: `8db1797ef36ef68f6675ad53231f887d51fd92b9`; tree `9e478a881d2d8ff4b3dc8246f344d60d4dbff45b`. Main rechecked on 2026-10-27 and unchanged.

The original-text extracts described below are local audit working records, not files shipped in this documentation branch. Committed evidence references point to the same source versions already in Git history.

## Coverage

| Surface | Evidence / work completed | Limit |
|---|---|---|
| Pinned tree | 733 tracked blobs inventoried; 730 text files retrieved. | Retrieval is not a claim of line-by-line semantic review. |
| Implementation | Java/module/search/scraper/model/handler structure and selected method bodies, JS/PS helpers and shell dispatcher inspected. | No builds, tests or live integration verification. |
| Product ideas | Backlog descriptions, ACs, future considerations, epics, prompts, skills, source registry and architecture sessions mapped to inventory. | Candidate ideas, not validated requirements. |
| Durable source reference | 421 of 425 current Markdown files saved as original-text extracts: 97 design/backlog, 187 knowledge/workflow, 137 supporting docs. Also 33 unique removed Markdown versions saved. | Four account/source registry files are not duplicated; capabilities are summarized and paths retained. One credential registry was not read at all. |
| Main history | 350 commit metadata records; complete changed-file lists for all 256 non-merge commits, including pagination. | Detailed content review targeted capability-bearing changes/removals; not every revision read line by line. |
| Pull requests | All 90 PR metadata records and complete changed-file lists; review records requested for all 90. | Two submitted reviews, both with empty body. |
| Discussions | One issue comment and two inline review comments retrieved. | No separate requirement found; inline discussion concerned a sample test. |
| Branches | Eight branches including main; earlier compare checks showed all seven other branches ahead=0. | No local/unpushed branches available. |
| Tags/releases | Tag refs and release listing empty. | No additional version snapshots there. |
| Wiki | Repo setting enabled; connector/browser unavailable; read-only git ls-remote returned Repository not found. | No accessible wiki found; this does not prove no private/unavailable wiki exists. |
| Sensitive/non-text exclusions | accounts-and-credentials.md, mcp-config.local.properties, gradle-wrapper.jar. | No credential content copied. Wrapper binary not useful to idea extraction. |
| External references | Source links and original-file paths recorded. | Workstation-only source files and external connected accounts not accessed. |

## Findings to carry into design

1. Old features-inventory counts are stale: snapshot contains 67 prompt files, 40 SKILL.md files (including legacy/staging), 8 agent files and 10 instruction files.
2. Git-inspired import/sync command vocabulary is mostly prompt workflow. The shell dispatcher implements new/publish/move/search/list/clear/status/help, not a complete external sync engine.
3. InMemoryIndex uses a concurrent map; ConfigurableSearchEngine scans/filter/scores/ranks. Persistent inverted/full-text/semantic search remains a separate proposal.
4. FuzzyMatcher implements prefix/substring matching. Do not describe it as a verified Levenshtein typo-correction engine. Its termFrequency loop counts substrings despite a whole-word comment.
5. ContentSummarizer selects leading sentences (default five, capped length), not semantic synthesis.
6. Note-status vocabulary in workflow docs and Java models differs; schemas are not demonstrably unified.
7. Universal customization documentation has conflicting completion markers; tests README is a placeholder. Only a sample Java test was found, not meaningful feature coverage.
8. Historical skill trimming removed educational examples and paths. These are preserved as learning-content candidates, not automatically restored as runtime instructions.
9. A large fraction of deleted migration files match surviving blob hashes exactly. File deletion alone does not establish lost functionality.
10. The Atlassian session deleted then re-added under scratchpad appears as a rename in the net PR; don't count it as missing.
11. Old source registries combine capture preferences with access rules and placeholders. Their descriptions are historical, not evidence of present account state or permission.
12. Old API/product/version claims and code examples can be stale or wrong. Preserve the idea; verify technical claims when evaluating that idea.
13. A bot suggested using arr[-1]; as a standalone Java statement in a sample test review. That suggestion is invalid Java and must not be treated as verified guidance.
14. Current-source snapshots plus history show several incompatible architecture directions. Preserve alternatives and rationale, not just the latest confident prose.

## Extraction completion gate

- [x] Snapshot and file manifest pinned.
- [x] Capability inventory with evidence and maturity labels created.
- [x] Backlog/architecture reference extracts durably saved.
- [x] Current user agreements separated from historical proposals.
- [x] PR/commit/branch/comment surfaces checked as described above.
- [x] Historical removals and relocations examined for capability loss.
- [x] Known discrepancies and inaccessible scope recorded.
- [x] Saved JSON parsed successfully; 62 unique inventory IDs verified; document links checked; source links checked against pinned tree.
- [ ] User confirms that inventory coverage is sufficient and identifies any missing intended ideas.
- [ ] User authorizes proceeding to further requirements/design brainstorming.

This is a bounded extraction checkpoint, not proof that every useful sentence in every revision has been found.
This documentation branch is authorized. No new-platform implementation repository, IntelliJ configuration or migration has been created.
