#!/usr/bin/env bash
# .brain-aliases.sh -- Bash aliases for the brain/ workspace dispatcher
#
# Source this file in your shell to get short brain commands:
#   source ./ai-brain/scripts/.brain-aliases.sh
#
# To load automatically, add to ~/.bashrc or ~/.zshrc:
#   source /path/to/repo/ai-brain/scripts/.brain-aliases.sh
#
# Available aliases:
#   brain         Full dispatcher  (same as ./ai-brain/scripts/brain.sh directly)
#   brain-new        Create a new note
#   brain-publish       Promote to archive/ with tagging and git commit
#   brain-search     Search notes by frontmatter or full text
#   brain-list       List notes
#   brain-clear      Clear inbox/
#   brain-status     Show tier summary
#   brain-move    Move a file between tiers

_BRAIN_SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
_BRAIN_SCRIPT="$_BRAIN_SCRIPT_DIR/brain.sh"

if [[ ! -f "$_BRAIN_SCRIPT" ]]; then
    echo "brain-aliases: dispatcher not found at $_BRAIN_SCRIPT" >&2
    return 1
fi

brain()         { bash "$_BRAIN_SCRIPT" "$@"; }
brain-new()     { bash "$_BRAIN_SCRIPT" new "$@"; }
brain-publish() { bash "$_BRAIN_SCRIPT" publish "$@"; }
brain-search()  { bash "$_BRAIN_SCRIPT" search "$@"; }
brain-list()    { bash "$_BRAIN_SCRIPT" list "$@"; }
brain-clear()   { bash "$_BRAIN_SCRIPT" clear "$@"; }
brain-status()  { bash "$_BRAIN_SCRIPT" status "$@"; }
brain-move()    { bash "$_BRAIN_SCRIPT" move "$@"; }

export -f brain brain-new brain-publish brain-search brain-list brain-clear brain-status brain-move

echo "brain-aliases loaded. Commands: brain, brain-new, brain-publish, brain-move, brain-search, brain-list, brain-clear, brain-status"
