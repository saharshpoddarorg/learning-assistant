# Historical alternatives and open design space

Status: reference only; no historical decision below binds the new platform.

| ID | Alternatives / rationale in old repo | Evidence |
|---|---|---|
| H01 | Pure skills, pure MCP, hybrid, agent-centric and broader platform approaches; static knowledge and executable tools treated differently. | Master architecture session; BLI-002 |
| H02 | Single vs specialized orchestrators; shared skills and agent handoffs. | BLI-005; agents/prompts |
| H03 | Java familiarity and existing code vs Node/Python and other runtime choices; startup, packaging and portability trade-offs. | skill-architecture-guide.md |
| H04 | Controlled enum vocabulary vs tags/hybrid; generated taxonomy/schema versus duplicated manual inventories. | BLI-087 |
| H05 | Delay shared-model extraction until a real consumer needs it; alternative module/package splits. | Master architecture session |
| H06 | Brain workspace separate from Java modules and relocatable through configuration; earlier 3/4/6-tier structures. | brain README; configuration reference; history |
| H07 | Flattened modular skills, split large skills, move workflows to prompts; historical trimming must not imply educational material has no value. | PRs 49–72 |
| H08 | MCP retirement, preserving Java reference, or reviving hybrid runtime. | Migration sessions and later backlog; conflicting historical directions |
| H09 | Obsidian/Notion/Logseq/other tools and PARA/CODE/Zettelkasten; old tool rejection is not a current decision. | Digital-notetaking guides; PKM philosophy |
| H10 | Build versus adopt for ebook reader and other personal tools. | BLI-016 and adjacent project BLIs |
| H11 | Separate backlog notes were removed to avoid overlap; lightweight raw capture remains a useful capability. | Commit e0b8571ceeec3846ccdeedd0fe6b9c69f18af354 |
| H12 | Multi-account profile resolution and shared HTTP clients; source-specific adapters vs universal handler. | Atlassian sessions, PRs 82–83 |

For original wording and evidence links, see the [evidence guide](../../discovery/evidence/README.md) and [inventory](../../discovery/source-inventory.md). No H entry is an approved new-platform decision.

## Disposition ledger

Every inventory entry currently has disposition: **undecided**.
No old feature has been approved for silent removal; no old architectural claim is carried forward as binding.
Record future dispositions against inventory IDs with reason, replacement, affected data/dependencies, and user review.
