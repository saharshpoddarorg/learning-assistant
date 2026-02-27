#!/usr/bin/env bash
# promote.sh -- Move a file between brain/ tiers (inbox, notes, archive)
#
# Usage:
#   ./brain/scripts/promote.sh <source> <tier> [subdir]
#
# Arguments:
#   source   Path relative to brain/   (e.g. inbox/draft.md  or  notes/java/note.md)
#   tier     Destination: notes | archive
#   subdir   Optional subdirectory within tier  (e.g. java -> brain/<tier>/java/<file>)
#
# Examples:
#   ./brain/scripts/promote.sh inbox/draft.md notes
#   ./brain/scripts/promote.sh inbox/draft.md notes java
#   ./brain/scripts/promote.sh notes/note.md archive

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"
BRAIN_ROOT="$REPO_ROOT/brain/ai-brain"

SOURCE="${1:-}"
TIER="${2:-}"
SUBDIR="${3:-}"

if [[ -z "$SOURCE" || -z "$TIER" ]]; then
    echo "Usage: $0 <source> <tier> [subdir]" >&2
    echo "  tier: notes | archive" >&2
    exit 1
fi

if [[ "$TIER" != "notes" && "$TIER" != "archive" ]]; then
    echo "Error: tier must be 'notes' or 'archive', got: $TIER" >&2
    exit 1
fi

# Resolve source
if [[ "$SOURCE" = /* ]]; then
    SOURCE_PATH="$SOURCE"
else
    SOURCE_PATH="$BRAIN_ROOT/$SOURCE"
fi

if [[ ! -f "$SOURCE_PATH" ]]; then
    echo "Error: source not found: $SOURCE_PATH" >&2
    exit 1
fi

FILENAME="$(basename "$SOURCE_PATH")"

if [[ -n "$SUBDIR" ]]; then
    DEST_DIR="$BRAIN_ROOT/$TIER/$SUBDIR"
else
    DEST_DIR="$BRAIN_ROOT/$TIER"
fi

DEST_PATH="$DEST_DIR/$FILENAME"
mkdir -p "$DEST_DIR"

if [[ -f "$DEST_PATH" ]]; then
    read -r -p "Destination already exists: ${DEST_PATH#"$BRAIN_ROOT/"} -- Overwrite? [y/N] " answer
    if [[ ! "$answer" =~ ^[yY] ]]; then
        echo "Cancelled."
        exit 0
    fi
fi

mv "$SOURCE_PATH" "$DEST_PATH"

SOURCE_REL="${SOURCE_PATH#"$BRAIN_ROOT/"}"
DEST_REL="${DEST_PATH#"$BRAIN_ROOT/"}"
echo "Moved: $SOURCE_REL -> $DEST_REL"

if [[ "$TIER" == "archive" ]]; then
    echo ""
    read -r -p "Run 'git add' on the file? [Y/n] " add
    if [[ ! "$add" =~ ^[nN] ]]; then
        GIT_PATH="brain/ai-brain/archive/${DEST_PATH#"$BRAIN_ROOT/archive/"}"
        git -C "$REPO_ROOT" add "$GIT_PATH"
        echo "Staged: $GIT_PATH"
        echo "Next: git commit -m \"brain: publish <topic>\""
    fi
fi
