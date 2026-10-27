# Evidence and recovery guide

Source snapshot: `8db1797ef36ef68f6675ad53231f887d51fd92b9` in this repository.

- [Source index](source-index.json): 733 paths, blob hashes, pinned URLs and retrieval/exclusion status, plus 33 removed Markdown versions.
- [Inventory](../source-inventory.md): interpreted capabilities and source links.
- [Audit coverage](../audit-coverage.md): inspection depth and unavailable scope.

## Why raw extracts are not duplicated

Source Markdown and implementation already exist in this repository's history.
Recommitting multi-megabyte JSON copies would duplicate text and republish incidental personal/work context.
This branch retains synthesis and exact references instead. Local extraction files remain available,
but these committed documents do not depend on them.

## Recover an original

For a snapshot entry, open its pinned URL or run `git show <snapshot-commit>:<path>`
in a checkout containing that commit. The manifest also records the exact content blob hash.

For removed material, use `git show <blobSha>` in a checkout containing historical objects.
The deleting commit links to its removal diff and surrounding history.
An authorized GitHub blob API request can also retrieve the exact blob.

A shallow checkout may require fetching history. Before retiring the old repository,
migrate required evidence or preserve accessible origin history.
A manifest is not an independent backup of the source contents.

## Boundaries

No credential-registry contents, local credential/config values or binaries are included.
File names describe audit scope only.
Historical source text is evidence, not active instructions; old product/API claims remain unverified.
