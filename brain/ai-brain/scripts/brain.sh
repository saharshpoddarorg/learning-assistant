#!/usr/bin/env bash
# brain -- personal knowledge workspace dispatcher
#
# Usage: ./brain/ai-brain/scripts/brain.sh <command> [options]
#
# Commands:
#   new       Create a new note with frontmatter template
#   publish   Promote to archive/ with project+date hierarchy, tagging, git commit
#   move      Move a file between tiers (inbox -> notes -> archive)
#   search    Search notes by frontmatter or full text
#   list      List notes with frontmatter summary
#   clear     Clear files from inbox/
#   status    Show tier summary
#   help      Show this help
#
# Options:
#   --tier     inbox | notes | archive
#   --project  project bucket name
#   --title    note title for 'new'
#   --kind     note | decision | session | resource | snippet | ref
#   --tag      tag filter for search/list
#   --date     YYYY-MM filter
#   --status   draft | final | archived
#   --force    skip confirmation prompts
#   --commit   auto-commit after save
#   --no-edit  don't open editor after creating note
#
# Examples:
#   ./brain/ai-brain/scripts/brain.sh new
#   ./brain/ai-brain/scripts/brain.sh new --tier inbox --project mcp-servers --title "SSE transport"
#   ./brain/ai-brain/scripts/brain.sh publish brain/ai-brain/inbox/draft.md --project mcp-servers
#   ./brain/ai-brain/scripts/brain.sh search java --tag generics
#   ./brain/ai-brain/scripts/brain.sh list --tier archive --project mcp-servers
#   ./brain/ai-brain/scripts/brain.sh clear --force
#   ./brain/ai-brain/scripts/brain.sh status

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"
BRAIN_ROOT="$REPO_ROOT/brain/ai-brain"

VALID_KINDS=("note" "decision" "session" "resource" "snippet" "ref")
VALID_TIERS=("inbox" "notes" "archive")
VALID_STATUS=("draft" "final" "archived")

# ── Colours ────────────────────────────────────────────────────────────────────
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'
CYAN='\033[0;36m'; WHITE='\033[1;37m'; GRAY='\033[0;37m'; RESET='\033[0m'

ok()   { echo -e "  ${GREEN}$*${RESET}"; }
info() { echo -e "  ${WHITE}$*${RESET}"; }
warn() { echo -e "  ${YELLOW}$*${RESET}"; }
err()  { echo -e "${RED}ERROR: $*${RESET}" >&2; exit 1; }
header() { echo; echo -e "${CYAN}$*${RESET}"; printf '%0.s-' $(seq 1 ${#1}); echo; }

# ── Argument parsing ───────────────────────────────────────────────────────────
COMMAND="${1:-help}"; shift || true
ARG1=""
OPT_TIER=""; OPT_PROJECT=""; OPT_TITLE=""; OPT_KIND=""
OPT_TAG=""; OPT_DATE=""; OPT_STATUS=""
OPT_FORCE=false; OPT_COMMIT=false; OPT_NO_EDIT=false

while [[ $# -gt 0 ]]; do
    case "$1" in
        --tier)    OPT_TIER="$2";    shift 2 ;;
        --project) OPT_PROJECT="$2"; shift 2 ;;
        --title)   OPT_TITLE="$2";   shift 2 ;;
        --kind)    OPT_KIND="$2";    shift 2 ;;
        --tag)     OPT_TAG="$2";     shift 2 ;;
        --date)    OPT_DATE="$2";    shift 2 ;;
        --status)  OPT_STATUS="$2";  shift 2 ;;
        --force)   OPT_FORCE=true;   shift ;;
        --commit)  OPT_COMMIT=true;  shift ;;
        --no-edit) OPT_NO_EDIT=true; shift ;;
        -*)        err "Unknown option: $1" ;;
        *)
            if [[ -z "$ARG1" ]]; then ARG1="$1"; else err "Unexpected argument: $1"; fi
            shift ;;
    esac
done

# ── Helpers ────────────────────────────────────────────────────────────────────

prompt_input() {
    local prompt="$1" default="${2:-}"
    local display="$prompt"
    [[ -n "$default" ]] && display="$prompt [$default]"
    read -r -p "$display: " value
    echo "${value:-$default}"
}

prompt_confirm() {
    local prompt="$1" default_yes="${2:-true}"
    local hint="[Y/n]"; [[ "$default_yes" == "false" ]] && hint="[y/N]"
    read -r -p "$prompt $hint " answer
    [[ -z "$answer" ]] && { [[ "$default_yes" == "true" ]] && return 0 || return 1; }
    [[ "$answer" =~ ^[yY] ]]
}

make_slug() {
    echo "$1" | tr '[:upper:]' '[:lower:]' | sed 's/[^a-z0-9]/-/g' | sed 's/-\+/-/g' | sed 's/^-\|-$//g'
}

contains() {
    local item="$1"; shift
    for el; do [[ "$el" == "$item" ]] && return 0; done
    return 1
}

get_frontmatter_field() {
    local file="$1" field="$2"
    # Extract frontmatter block then grep the field
    awk '/^---$/{if(++c==2)exit} c==1 && /^'"$field"':/{print; exit}' "$file" \
        | sed "s/^$field: *//"
}

get_frontmatter_tags() {
    local file="$1"
    get_frontmatter_field "$file" "tags" \
        | tr -d '[]' | tr ',' '\n' | awk '{$1=$1;print}' | grep -v '^$'
}

open_editor() {
    local file="$1"
    if command -v code &>/dev/null; then
        code "$file"
    elif [[ -n "${EDITOR:-}" ]]; then
        "$EDITOR" "$file"
    else
        echo "  Could not detect editor. Open manually: $file"
    fi
}

resolve_source() {
    local src="$1"
    if [[ -f "$src" ]]; then               echo "$src";               return; fi
    if [[ -f "$BRAIN_ROOT/$src" ]];  then  echo "$BRAIN_ROOT/$src"; return; fi
    if [[ -f "$PWD/$src" ]];         then  echo "$PWD/$src";       return; fi
    err "File not found: $src"
}

get_notes() {
    local tiers=("$@")
    [[ ${#tiers[@]} -eq 0 ]] && tiers=("inbox" "notes" "archive")
    for tier in "${tiers[@]}"; do
        local dir="$BRAIN_ROOT/$tier"
        find "$dir" -name "*.md" ! -name "README.md" | while read -r f; do
            echo "$tier|$f"
        done
    done
}

# ── Command: help ──────────────────────────────────────────────────────────────

cmd_help() {
    echo
    echo -e "${CYAN}brain -- personal knowledge workspace${RESET}"
    echo
    echo -e "${YELLOW}USAGE${RESET}"
    echo "  ./brain/ai-brain/scripts/brain.sh <command> [options]"
    echo
    echo -e "${YELLOW}COMMANDS${RESET}"
    printf "  %-10s %s\n" \
        "new"     "Create a new note with frontmatter template" \
        "publish" "Promote to archive/ with project+date hierarchy, tagging, git commit" \
        "move"    "Move a file between tiers (inbox -> notes -> archive)" \
        "search"  "Search notes by frontmatter (--tag, --project, --kind, --date) or text" \
        "list"    "List notes with frontmatter summary" \
        "clear"   "Clear files from inbox/" \
        "status"  "Show tier summary" \
        "help"    "Show this help"
    echo
    echo -e "${YELLOW}OPTIONS${RESET}"
    printf "  %-12s %s\n" \
        "--tier"    "inbox | notes | archive" \
        "--project" "project bucket name (e.g. mcp-servers, java, general)" \
        "--title"   "note title for 'new'" \
        "--kind"    "note | decision | session | resource | snippet | ref" \
        "--tag"     "tag filter for search/list" \
        "--date"    "YYYY-MM filter for search/list" \
        "--status"  "draft | final | archived" \
        "--force"   "skip confirmation prompts" \
        "--commit"  "auto-commit after save without prompting" \
        "--no-edit" "don't open editor after creating note"
    echo
    echo -e "${YELLOW}EXAMPLES${RESET}"
    echo "  ./brain/ai-brain/scripts/brain.sh new"
    echo "  ./brain/ai-brain/scripts/brain.sh new --tier inbox --project mcp-servers --title \"SSE transport\""
    echo "  ./brain/ai-brain/scripts/brain.sh publish brain/ai-brain/inbox/draft.md --project mcp-servers"
    echo "  ./brain/ai-brain/scripts/brain.sh search java --tag generics --tier archive"
    echo "  ./brain/ai-brain/scripts/brain.sh list --tier archive --project mcp-servers"
    echo "  ./brain/ai-brain/scripts/brain.sh clear --force"
    echo "  ./brain/ai-brain/scripts/brain.sh status"
    echo
    echo -e "${YELLOW}ALIASES  (after sourcing .brain-aliases.sh)${RESET}"
    echo "  brain, brain-new, brain-publish, brain-move, brain-search, brain-list, brain-clear, brain-status"
    echo
}

# ── Command: new ──────────────────────────────────────────────────────────────

cmd_new() {
    header "Create new note"

    local tier project title kind tags
    tier="${OPT_TIER:-}"
    [[ -z "$tier" ]] && tier="$(prompt_input "Tier (inbox/notes/archive)" "inbox")"

    project="${OPT_PROJECT:-}"
    [[ -z "$project" ]] && project="$(prompt_input "Project" "general")"

    title="${OPT_TITLE:-}"
    [[ -z "$title" ]] && title="$(prompt_input "Title" "untitled")"

    kind="${OPT_KIND:-}"
    [[ -z "$kind" ]] && kind="$(prompt_input "Kind ($(IFS='|'; echo "${VALID_KINDS[*]}"))" "note")"
    contains "$kind" "${VALID_KINDS[@]}" || { warn "Unrecognised kind, using 'note'"; kind="note"; }

    tags="$(prompt_input "Tags (comma-separated, or Enter to skip)" "")"

    local today slug filename dest_dir dest_path
    today="$(date +%Y-%m-%d)"
    slug="$(make_slug "$title")"
    filename="${today}_${slug}.md"
    dest_dir="$BRAIN_ROOT/$tier"
    dest_path="$dest_dir/$filename"

    if [[ -f "$dest_path" ]]; then
        prompt_confirm "File already exists. Overwrite?" false || { warn "Cancelled."; return; }
    fi

    local tag_list="[]"
    [[ -n "$tags" ]] && tag_list="[$(echo "$tags" | sed 's/\s*,\s*/, /g')]"

    mkdir -p "$dest_dir"
    cat > "$dest_path" <<EOF
---
date: $today
kind: $kind
project: $project
tags: $tag_list
status: draft
source: copilot
---

# $title

<!-- Notes here -->
EOF

    ok "Created: brain/ai-brain/$tier/$filename"

    if [[ "$OPT_NO_EDIT" != "true" ]]; then
        local do_open=true
        [[ "$OPT_FORCE" != "true" ]] && prompt_confirm "Open in editor?" true || do_open=false
        [[ "$do_open" == "true" ]] && open_editor "$dest_path"
    fi
}

# ── Command: publish ─────────────────────────────────────────────────────────────

cmd_publish() {
    header "Save note to repo"

    local source_path="$ARG1"
    [[ -z "$source_path" ]] && source_path="$(prompt_input "Source file (relative to brain/ai-brain/ or absolute)")"
    source_path="$(resolve_source "$source_path")"

    echo
    echo -e "${GRAY}Current frontmatter:${RESET}"
    local fm_date fm_kind fm_project fm_tags fm_status
    fm_date="$(get_frontmatter_field "$source_path" "date" 2>/dev/null || true)"
    fm_kind="$(get_frontmatter_field "$source_path" "kind" 2>/dev/null || true)"
    fm_project="$(get_frontmatter_field "$source_path" "project" 2>/dev/null || true)"
    fm_tags="$(get_frontmatter_field "$source_path" "tags" 2>/dev/null || true)"
    fm_status="$(get_frontmatter_field "$source_path" "status" 2>/dev/null || true)"
    [[ -n "$fm_date" ]]    && echo -e "  ${GRAY}date: $fm_date${RESET}"
    [[ -n "$fm_kind" ]]    && echo -e "  ${GRAY}kind: $fm_kind${RESET}"
    [[ -n "$fm_project" ]] && echo -e "  ${GRAY}project: $fm_project${RESET}"
    [[ -n "$fm_tags" ]]    && echo -e "  ${GRAY}tags: $fm_tags${RESET}"
    echo

    # Project
    local project="${OPT_PROJECT:-${fm_project:-general}}"
    [[ "$OPT_FORCE" != "true" ]] && project="$(prompt_input "Project bucket" "$project")"

    # Kind
    local kind="${OPT_KIND:-${fm_kind:-note}}"
    [[ "$OPT_FORCE" != "true" ]] && kind="$(prompt_input "Kind" "$kind")"
    contains "$kind" "${VALID_KINDS[@]}" || kind="note"

    # Tags -- suggest from existing + filename slug
    local file_slug basename
    basename="$(basename "$source_path" .md)"
    file_slug="${basename#????-??-??_}"  # strip date prefix
    local existing_tags=()
    [[ -n "$fm_tags" ]] && mapfile -t existing_tags < <(echo "$fm_tags" | tr -d '[]' | tr ',' '\n' | awk '{$1=$1;print}' | grep -v '^$')
    local auto_tags=()
    IFS='-_' read -ra parts <<< "$file_slug"
    for p in "${parts[@]}"; do [[ ${#p} -gt 2 ]] && auto_tags+=("$p"); done
    local all_suggested=(); declare -A _seen
    for t in "${existing_tags[@]}" "${auto_tags[@]}"; do
        [[ -z "${_seen[$t]+x}" ]] && all_suggested+=("$t") && _seen[$t]=1
    done
    local suggested_str
    suggested_str="$(IFS=', '; echo "${all_suggested[*]}")"
    echo -e "  ${GRAY}Suggested tags: $suggested_str${RESET}"
    local tag_input
    tag_input="$(prompt_input "Tags (comma-separated)" "$suggested_str")"
    local final_tags="[]"
    [[ -n "$tag_input" ]] && final_tags="[$(echo "$tag_input" | sed 's/\s*,\s*/, /g')]"

    # Status
    local status_val="${OPT_STATUS:-${fm_status:-final}}"
    [[ "$OPT_FORCE" != "true" ]] && status_val="$(prompt_input "Status (draft|final|archived)" "$status_val")"
    contains "$status_val" "${VALID_STATUS[@]}" || status_val="final"

    # Date for folder
    local today; today="$(date +%Y-%m-%d)"
    local note_date="${fm_date:-$today}"
    local year_month="${note_date:0:7}"

    local filename; filename="$(basename "$source_path")"
    local dest_dir="$BRAIN_ROOT/archive/$project/$year_month"
    local dest_path="$dest_dir/$filename"
    local git_rel="brain/ai-brain/archive/$project/$year_month/$filename"

    echo
    echo -e "  ${CYAN}Destination: $git_rel${RESET}"

    if [[ "$OPT_FORCE" != "true" ]]; then
        prompt_confirm "Proceed?" true || { warn "Cancelled."; return; }
    fi

    # Update frontmatter in source file
    local tmp; tmp="$(mktemp)"
    awk -v d="$note_date" -v k="$kind" -v p="$project" -v t="$final_tags" -v s="$status_val" '
        /^---$/ { fc++; print; next }
        fc==1 && /^date:/    { print "date: " d; next }
        fc==1 && /^kind:/    { print "kind: " k; next }
        fc==1 && /^project:/ { print "project: " p; next }
        fc==1 && /^tags:/    { print "tags: " t; next }
        fc==1 && /^status:/  { print "status: " s; next }
        fc==2 && !wrote_extras {
            if (d && !had_date)    print "date: " d
            if (k && !had_kind)    print "kind: " k
            if (p && !had_proj)    print "project: " p
            if (t && !had_tags)    print "tags: " t
            if (s && !had_stat)    print "status: " s
            wrote_extras=1
        }
        { print }
    ' "$source_path" > "$tmp"
    mv "$tmp" "$source_path"

    # Move to archive/
    mkdir -p "$dest_dir"
    if [[ -f "$dest_path" && "$OPT_FORCE" != "true" ]]; then
        prompt_confirm "Destination exists. Overwrite?" false || { warn "Cancelled."; return; }
    fi
    mv "$source_path" "$dest_path"
    ok "Moved: brain/ai-brain/archive/$project/$year_month/$filename"

    # Git
    cd "$REPO_ROOT"
    git add "$git_rel" 2>/dev/null
    ok "Staged: $git_rel"

    local do_commit="$OPT_COMMIT"
    [[ "$do_commit" != "true" ]] && prompt_confirm "Commit now?" true && do_commit=true

    if [[ "$do_commit" == "true" ]]; then
        local slug_title="${filename%.md}"; slug_title="${slug_title#????-??-??_}"
        local commit_msg="brain: publish $slug_title [$tag_input]

Project: $project  Kind: $kind  Status: $status_val

-- created by gpt"
        git commit -m "$commit_msg"
        ok "Committed."
    else
        info "Run: git commit -m \"brain: publish $filename\""
    fi
}

# ── Command: move ──────────────────────────────────────────────────────────

cmd_move() {
    header "Promote file between tiers"

    local source_path="$ARG1"
    [[ -z "$source_path" ]] && source_path="$(prompt_input "Source (relative to brain/ai-brain/)")"
    source_path="$(resolve_source "$source_path")"

    local target_tier="${OPT_TIER:-}"
    [[ -z "$target_tier" ]] && target_tier="$(prompt_input "Target tier (notes|archive)" "notes")"

    local subdir="${OPT_PROJECT:-}"
    [[ -z "$subdir" && "$OPT_FORCE" != "true" ]] && subdir="$(prompt_input "Subdirectory (optional)" "")"

    local filename; filename="$(basename "$source_path")"
    local dest_dir
    [[ -n "$subdir" ]] && dest_dir="$BRAIN_ROOT/$target_tier/$subdir" || dest_dir="$BRAIN_ROOT/$target_tier"
    local dest_path="$dest_dir/$filename"

    mkdir -p "$dest_dir"
    if [[ -f "$dest_path" && "$OPT_FORCE" != "true" ]]; then
        prompt_confirm "Destination exists. Overwrite?" false || { warn "Cancelled."; return; }
    fi

    mv "$source_path" "$dest_path"
    ok "Moved: brain/ai-brain/$target_tier/$subdir${subdir:+/}$filename"

    if [[ "$target_tier" == "archive" ]]; then
        cd "$REPO_ROOT"
        local git_rel="brain/ai-brain/$target_tier/${subdir:+$subdir/}$filename"
        git add "$git_rel" 2>/dev/null
        ok "Staged: $git_rel"
        info "Run 'git commit' when ready."
    fi
}

# ── Command: search ────────────────────────────────────────────────────────────

cmd_search() {
    local query="$ARG1"

    local tiers=("inbox" "notes" "archive")
    [[ -n "$OPT_TIER" ]] && tiers=("$OPT_TIER")

    header "Search results"

    local filters=()
    [[ -n "$query" ]]       && filters+=("text:\"$query\"")
    [[ -n "$OPT_TAG" ]]     && filters+=("tag:$OPT_TAG")
    [[ -n "$OPT_PROJECT" ]] && filters+=("project:$OPT_PROJECT")
    [[ -n "$OPT_KIND" ]]    && filters+=("kind:$OPT_KIND")
    [[ -n "$OPT_DATE" ]]    && filters+=("date:$OPT_DATE")
    [[ -n "$OPT_STATUS" ]]  && filters+=("status:$OPT_STATUS")
    [[ ${#filters[@]} -gt 0 ]] && info "Filters: ${filters[*]}"

    local count=0
    while IFS='|' read -r tier filepath; do
        local match=true
        local fm_date fm_kind fm_project fm_tags fm_status
        fm_date="$(get_frontmatter_field "$filepath" "date" 2>/dev/null || true)"
        fm_kind="$(get_frontmatter_field "$filepath" "kind" 2>/dev/null || true)"
        fm_project="$(get_frontmatter_field "$filepath" "project" 2>/dev/null || true)"
        fm_tags="$(get_frontmatter_field "$filepath" "tags" 2>/dev/null || true)"
        fm_status="$(get_frontmatter_field "$filepath" "status" 2>/dev/null || true)"

        [[ -n "$OPT_TAG"     ]] && [[ "$fm_tags"    != *"$OPT_TAG"*     ]] && match=false
        [[ -n "$OPT_PROJECT" ]] && [[ "$fm_project" != "$OPT_PROJECT"   ]] && match=false
        [[ -n "$OPT_KIND"    ]] && [[ "$fm_kind"    != "$OPT_KIND"      ]] && match=false
        [[ -n "$OPT_DATE"    ]] && [[ "$fm_date"    != *"$OPT_DATE"*    ]] && match=false
        [[ -n "$OPT_STATUS"  ]] && [[ "$fm_status"  != "$OPT_STATUS"    ]] && match=false

        if [[ -n "$query" ]] && [[ "$match" == "true" ]]; then
            grep -qi "$query" "$filepath" || [[ "$(basename "$filepath")" == *"$query"* ]] || match=false
        fi

        if [[ "$match" == "true" ]]; then
            local rel="${filepath#"$BRAIN_ROOT/$tier/"}"
            echo
            echo -e "  ${WHITE}[$tier] $rel${RESET}"
            local meta=()
            [[ -n "$fm_date" ]]    && meta+=("$fm_date")
            [[ -n "$fm_kind" ]]    && meta+=("$fm_kind")
            [[ -n "$fm_project" ]] && meta+=("$fm_project")
            [[ -n "$fm_tags" ]]    && meta+=("$fm_tags")
            [[ ${#meta[@]} -gt 0 ]] && echo -e "         ${GRAY}$(IFS='  |  '; echo "${meta[*]}")${RESET}"
            count=$((count + 1))
        fi
    done < <(get_notes "${tiers[@]}")

    echo
    [[ $count -eq 0 ]] && warn "No results found." || info "$count result(s)"
}

# ── Command: list ──────────────────────────────────────────────────────────────

cmd_list() {
    local tiers=("inbox" "notes" "archive")
    [[ -n "$OPT_TIER" ]] && tiers=("$OPT_TIER")

    local count=0 current_tier=""
    while IFS='|' read -r tier filepath; do
        local fm_project fm_kind fm_tags
        fm_project="$(get_frontmatter_field "$filepath" "project" 2>/dev/null || true)"
        fm_kind="$(get_frontmatter_field "$filepath" "kind" 2>/dev/null || true)"
        fm_tags="$(get_frontmatter_field "$filepath" "tags" 2>/dev/null || true)"

        [[ -n "$OPT_PROJECT" ]] && [[ "$fm_project" != "$OPT_PROJECT" ]] && continue
        [[ -n "$OPT_KIND"    ]] && [[ "$fm_kind"    != "$OPT_KIND"    ]] && continue
        [[ -n "$OPT_TAG"     ]] && [[ "$fm_tags"    != *"$OPT_TAG"*   ]] && continue

        [[ "$tier" != "$current_tier" ]] && { header "$tier"; current_tier="$tier"; }
        local rel="${filepath#"$BRAIN_ROOT/$tier/"}"
        printf "  %-44s %-10s %-14s %s\n" "$rel" "${fm_kind:--}" "${fm_project:--}" "$fm_tags"
        count=$((count + 1))
    done < <(get_notes "${tiers[@]}" | sort)

    echo
    [[ $count -eq 0 ]] && warn "No notes found." || info "$count note(s)"
}

# ── Command: clear ─────────────────────────────────────────────────────────────

cmd_clear() {
    local scratch_dir="$BRAIN_ROOT/inbox"
    [[ -d "$scratch_dir" ]] || { warn "inbox/ does not exist."; return; }

    mapfile -t files < <(find "$scratch_dir" -type f ! -name "README.md")
    [[ ${#files[@]} -eq 0 ]] && { ok "inbox/ is already empty."; return; }

    header "Clear inbox/"
    for f in "${files[@]}"; do info "${f#"$scratch_dir/"}"; done
    echo

    local do_delete=false
    [[ "$OPT_FORCE" == "true" ]] && do_delete=true
    [[ "$do_delete" != "true" ]] && prompt_confirm "Delete these ${#files[@]} file(s)?" false && do_delete=true

    if [[ "$do_delete" == "true" ]]; then
        for f in "${files[@]}"; do rm -f "$f"; done
        ok "Cleared ${#files[@]} file(s) from inbox/."
    else
        warn "Cancelled."
    fi
}

# ── Command: status ────────────────────────────────────────────────────────────

cmd_status() {
    header "brain/ai-brain/ workspace status"

    for tier in inbox notes archive; do
        local dir="$BRAIN_ROOT/$tier"
        local git_status="[gitignored]"
        [[ "$tier" == "archive" ]] && git_status="[tracked]  "
        if [[ ! -d "$dir" ]]; then
            info "  $(printf '%-10s' "$tier") $git_status  (does not exist)"
            continue
        fi
        local count; count=$(find "$dir" -name "*.md" ! -name "README.md" | wc -l | tr -d ' ')
        echo -e "  ${WHITE}$(printf '%-10s' "$tier") $git_status  $count note(s)${RESET}"
        find "$dir" -name "*.md" ! -name "README.md" -printf "%T@ %p\n" 2>/dev/null \
            | sort -rn | head -3 \
            | while IFS=' ' read -r _ f; do
                echo -e "             ${GRAY}${f#"$dir/"}${RESET}"
            done
    done

    echo
    local staged; staged="$(git -C "$REPO_ROOT" diff --cached --name-only -- brain/ai-brain/ 2>/dev/null || true)"
    if [[ -n "$staged" ]]; then
        echo -e "  ${YELLOW}Staged (ready to commit):${RESET}"
        echo "$staged" | while read -r f; do echo -e "    ${YELLOW}$f${RESET}"; done
    fi
}

# ── Dispatch ───────────────────────────────────────────────────────────────────

case "$COMMAND" in
    new)     cmd_new ;;
    publish) cmd_publish ;;
    move)    cmd_move ;;
    search)  cmd_search ;;
    list)    cmd_list ;;
    clear)   cmd_clear ;;
    status)  cmd_status ;;
    help|-h|--help) cmd_help ;;
    *)
        # Treat as a search query for convenience
        ARG1="$COMMAND"
        cmd_search
        ;;
esac
