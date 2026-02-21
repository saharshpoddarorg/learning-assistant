#!/usr/bin/env bash
# .ai-aliases.sh -- Bash aliases for the ai/ workspace dispatcher
#
# Source this file in your shell to get short ai-* commands:
#   source ./ai/scripts/.ai-aliases.sh
#
# To load automatically, add to ~/.bashrc or ~/.zshrc:
#   source /path/to/repo/ai/scripts/.ai-aliases.sh
#
# Available aliases:
#   ai            Full dispatcher  (same as ./ai/scripts/ai.sh directly)
#   ai-new        Create a new note
#   ai-save       Promote to saved/ with tagging and git commit
#   ai-search     Search notes by frontmatter or full text
#   ai-list       List notes
#   ai-clear      Clear scratch/
#   ai-status     Show tier summary
#   ai-promote    Move a file between tiers

_AI_SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
_AI_SCRIPT="$_AI_SCRIPT_DIR/ai.sh"

if [[ ! -f "$_AI_SCRIPT" ]]; then
    echo "ai-aliases: dispatcher not found at $_AI_SCRIPT" >&2
    return 1
fi

ai()         { bash "$_AI_SCRIPT" "$@"; }
ai-new()     { bash "$_AI_SCRIPT" new "$@"; }
ai-save()    { bash "$_AI_SCRIPT" save "$@"; }
ai-search()  { bash "$_AI_SCRIPT" search "$@"; }
ai-list()    { bash "$_AI_SCRIPT" list "$@"; }
ai-clear()   { bash "$_AI_SCRIPT" clear "$@"; }
ai-status()  { bash "$_AI_SCRIPT" status "$@"; }
ai-promote() { bash "$_AI_SCRIPT" promote "$@"; }

export -f ai ai-new ai-save ai-search ai-list ai-clear ai-status ai-promote

echo "ai-aliases loaded. Commands: ai, ai-new, ai-save, ai-search, ai-list, ai-clear, ai-status, ai-promote"
