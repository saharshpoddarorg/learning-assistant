#!/usr/bin/env bash
# clear-inbox.sh -- Clear files from brain/inbox/ (preserves README.md)
#
# Usage:
#   ./brain/ai-brain/scripts/clear-inbox.sh              Preview only -- lists files, no delete
#   ./brain/ai-brain/scripts/clear-inbox.sh --confirm    List then prompt before deleting
#   ./brain/ai-brain/scripts/clear-inbox.sh --force      Delete without prompting
#
# Run from any location -- resolves path relative to this script's directory.

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"
INBOX_DIR="$REPO_ROOT/brain/ai-brain/inbox"

MODE="preview"
for arg in "$@"; do
    case "$arg" in
        --confirm) MODE="confirm" ;;
        --force)   MODE="force" ;;
        -h|--help)
            sed -n '2,10p' "$0" | sed 's/^# \?//'
            exit 0
            ;;
        *)
            echo "Unknown option: $arg" >&2
            exit 1
            ;;
    esac
done

if [[ ! -d "$INBOX_DIR" ]]; then
    echo "inbox/ does not exist: $INBOX_DIR"
    exit 0
fi

mapfile -t FILES < <(find "$INBOX_DIR" -type f ! -name "README.md")

if [[ ${#FILES[@]} -eq 0 ]]; then
    echo "inbox/ is already empty."
    exit 0
fi

echo ""
echo "Files in brain/ai-brain/inbox/:"
for f in "${FILES[@]}"; do
    echo "  ${f#"$INBOX_DIR/"}"
done
echo ""

if [[ "$MODE" == "force" ]]; then
    for f in "${FILES[@]}"; do rm -f "$f"; done
    echo "Cleared ${#FILES[@]} file(s) from inbox/."
    exit 0
fi

if [[ "$MODE" == "confirm" ]]; then
    read -r -p "Delete these ${#FILES[@]} file(s)? [y/N] " answer
    if [[ "$answer" =~ ^[yY] ]]; then
        for f in "${FILES[@]}"; do rm -f "$f"; done
        echo "Cleared ${#FILES[@]} file(s) from inbox/."
    else
        echo "Cancelled."
    fi
    exit 0
fi

# preview only
echo "Preview only (no files deleted)."
echo "Run with --confirm to delete interactively, or --force to delete immediately."
