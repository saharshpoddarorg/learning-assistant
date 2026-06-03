# setup.sh — Load learning-assistant environment variables
#
# USAGE (bash / zsh):
#   source ./setup.sh          # loads .env from repo root
#   source ./setup.sh --check  # validate only, do not export
#
# Reads .env from the same directory as this script, validates required
# variables, then exports them into the current shell session.
#
# Safe to source multiple times (idempotent).

# ---- locate repo root --------------------------------------------------------
SETUP_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="$SETUP_DIR/.env"

# ---- load .env ---------------------------------------------------------------
if [ ! -f "$ENV_FILE" ]; then
    echo ""
    echo "  [setup.sh] ERROR: .env not found at $ENV_FILE"
    echo "  Copy .env.example to .env and fill in your machine-specific paths:"
    echo "    cp .env.example .env"
    echo ""
    return 1 2>/dev/null || exit 1
fi

# Export every non-comment, non-empty line from .env
while IFS='=' read -r key value; do
    # skip comments and blank lines
    [[ "$key" =~ ^#.*$ || -z "$key" ]] && continue
    # strip inline comments and surrounding whitespace
    value="${value%%#*}"
    value="${value#"${value%%[![:space:]]*}"}"
    value="${value%"${value##*[![:space:]]}"}"
    [[ -n "$value" ]] && export "$key=$value"
done < "$ENV_FILE"

# ---- validate required variables ---------------------------------------------
_SETUP_OK=true

_check_required() {
    local var_name="$1"
    local example="$2"
    local value="${!var_name}"
    if [ -z "$value" ]; then
        echo "  [setup.sh] MISSING: $var_name is not set in .env"
        echo "             Example: $var_name=$example"
        _SETUP_OK=false
    elif [ ! -d "$value" ]; then
        echo "  [setup.sh] INVALID: $var_name=$value — directory does not exist"
        _SETUP_OK=false
    fi
}

echo ""
echo "  [setup.sh] Validating environment..."
_check_required "JAVA_HOME" "/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home"
_check_required "PROJ_HOME" "/Users/yourname/dev/learning-assistant"

# Verify JAVA_HOME is a JDK (has a compiler), not a JRE
if [ -n "$JAVA_HOME" ] && [ -d "$JAVA_HOME" ]; then
    if [ ! -f "$JAVA_HOME/bin/javac" ] && [ ! -f "$JAVA_HOME/bin/javac.exe" ]; then
        echo "  [setup.sh] WARNING: JAVA_HOME=$JAVA_HOME does not contain bin/javac"
        echo "             Gradle requires a JDK — a JRE-only install will not work."
        _SETUP_OK=false
    fi
fi

# ---- set derived defaults ----------------------------------------------------
if [ -z "$PROJ_TEMP" ]; then
    export PROJ_TEMP="$PROJ_HOME/temp"
fi

# ---- update PATH and report --------------------------------------------------
if [ "$_SETUP_OK" = true ]; then
    # Prepend JDK bin to PATH so 'java', 'javac' resolve to the right JDK
    case ":$PATH:" in
        *":$JAVA_HOME/bin:"*) ;;   # already present
        *) export PATH="$JAVA_HOME/bin:$PATH" ;;
    esac

    echo "  [setup.sh] OK — environment ready:"
    echo "    JAVA_HOME     = $JAVA_HOME"
    echo "    PROJ_HOME     = $PROJ_HOME"
    echo "    PROJ_TEMP     = $PROJ_TEMP"
    [ -n "$GRADLE_USER_HOME" ] && echo "    GRADLE_USER_HOME = $GRADLE_USER_HOME"
    echo "    PATH          = (java/javac prepended)"
    echo ""
else
    echo ""
    echo "  [setup.sh] FAILED — fix the issues above, then re-run: source ./setup.sh"
    echo ""
    return 1 2>/dev/null || exit 1
fi
