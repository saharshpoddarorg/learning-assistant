#!/usr/bin/env bash
# =============================================================================
# oauth-flow.sh — Generic OAuth2 Authorization Code flow helper
# =============================================================================
# Opens the browser to the OAuth authorization endpoint, starts a local
# callback server (via netcat or Python), captures the authorization code,
# and exchanges it for an access token.
#
# This is a FRAMEWORK TEMPLATE — customize the endpoints and parameters
# for each specific OAuth provider.
#
# Usage:
#   ./scripts/common/auth/oauth-flow.sh --provider <name> [options]
#
# Options:
#   --provider <name>       Provider name matching config keys (e.g., github)
#   --client-id <id>        Override client ID from config
#   --redirect-port <port>  Local callback port (default: 8484)
#   --scopes <scopes>       OAuth scopes (space-separated, default from config)
#
# Config keys read (prefix: oauth.<provider>):
#   oauth.<provider>.authorizeUrl    Authorization endpoint
#   oauth.<provider>.tokenUrl        Token exchange endpoint
#   oauth.<provider>.clientId        Client ID
#   oauth.<provider>.scopes          Default scopes
#
# NOTE: Client secrets should be stored in environment variables, NOT in the
# config file. Use: MCP_OAUTH_<PROVIDER>_CLIENTSECRET
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

# Source shared config reader
source "$PROJECT_ROOT/scripts/common/utils/read-config.sh"

PROVIDER=""
CLIENT_ID_OVERRIDE=""
REDIRECT_PORT=8484
SCOPES_OVERRIDE=""

# --- Parse arguments ---------------------------------------------------------
while [[ $# -gt 0 ]]; do
    case "$1" in
        --provider)       PROVIDER="$2"; shift 2 ;;
        --client-id)      CLIENT_ID_OVERRIDE="$2"; shift 2 ;;
        --redirect-port)  REDIRECT_PORT="$2"; shift 2 ;;
        --scopes)         SCOPES_OVERRIDE="$2"; shift 2 ;;
        *)                echo "Unknown option: $1"; exit 1 ;;
    esac
done

if [[ -z "$PROVIDER" ]]; then
    echo "ERROR: --provider is required"
    echo "Usage: $0 --provider <name>"
    exit 1
fi

# --- Load OAuth config -------------------------------------------------------
AUTHORIZE_URL=$(read_config "oauth.${PROVIDER}.authorizeUrl" "")
TOKEN_URL=$(read_config "oauth.${PROVIDER}.tokenUrl" "")
CLIENT_ID="${CLIENT_ID_OVERRIDE:-$(read_config "oauth.${PROVIDER}.clientId" "")}"
CLIENT_SECRET=$(read_config "oauth.${PROVIDER}.clientSecret" "")
SCOPES="${SCOPES_OVERRIDE:-$(read_config "oauth.${PROVIDER}.scopes" "")}"

REDIRECT_URI="http://localhost:${REDIRECT_PORT}/callback"

# --- Validate required fields ------------------------------------------------
missing=()
[[ -z "$AUTHORIZE_URL" ]] && missing+=("oauth.${PROVIDER}.authorizeUrl")
[[ -z "$TOKEN_URL" ]]     && missing+=("oauth.${PROVIDER}.tokenUrl")
[[ -z "$CLIENT_ID" ]]     && missing+=("oauth.${PROVIDER}.clientId")

if [[ ${#missing[@]} -gt 0 ]]; then
    echo "ERROR: Missing OAuth configuration for provider '$PROVIDER':"
    for key in "${missing[@]}"; do
        echo "  - $key"
    done
    echo ""
    echo "Add these to your config file or set as environment variables."
    exit 1
fi

if [[ -z "$CLIENT_SECRET" ]]; then
    echo "WARNING: No client secret found."
    echo "  Set env var: MCP_OAUTH_$(echo "${PROVIDER}" | tr '[:lower:]' '[:upper:]')_CLIENTSECRET"
    echo "  Or add oauth.${PROVIDER}.clientSecret to config (NOT recommended for secrets)."
    echo ""
fi

# --- Generate state parameter for CSRF protection ----------------------------
STATE=$(head -c 32 /dev/urandom | base64 | tr -dc 'a-zA-Z0-9' | head -c 32)

echo "=== OAuth2 Authorization Code Flow ==="
echo "Provider     : $PROVIDER"
echo "Authorize URL: $AUTHORIZE_URL"
echo "Redirect URI : $REDIRECT_URI"
echo "Scopes       : ${SCOPES:-"(none)"}"
echo "======================================="

# --- Build authorization URL --------------------------------------------------
AUTH_PARAMS="response_type=code"
AUTH_PARAMS+="&client_id=$(python3 -c "import urllib.parse; print(urllib.parse.quote('$CLIENT_ID'))" 2>/dev/null || echo "$CLIENT_ID")"
AUTH_PARAMS+="&redirect_uri=$(python3 -c "import urllib.parse; print(urllib.parse.quote('$REDIRECT_URI'))" 2>/dev/null || echo "$REDIRECT_URI")"
AUTH_PARAMS+="&state=$STATE"
[[ -n "$SCOPES" ]] && AUTH_PARAMS+="&scope=$(python3 -c "import urllib.parse; print(urllib.parse.quote('$SCOPES'))" 2>/dev/null || echo "$SCOPES")"

FULL_AUTH_URL="${AUTHORIZE_URL}?${AUTH_PARAMS}"

# --- Open browser for authorization ------------------------------------------
echo ""
echo "Opening browser for authorization..."
echo "URL: $FULL_AUTH_URL"
echo ""

# Try to use MCP browser launcher, fall back to system open
if [[ -x "$PROJECT_ROOT/scripts/common/browser/launch-browser.sh" ]]; then
    "$PROJECT_ROOT/scripts/common/browser/launch-browser.sh" --url "$FULL_AUTH_URL" &
elif command -v xdg-open &>/dev/null; then
    xdg-open "$FULL_AUTH_URL" &
elif command -v open &>/dev/null; then
    open "$FULL_AUTH_URL" &
else
    echo "Please open this URL in your browser manually:"
    echo "  $FULL_AUTH_URL"
fi

# --- Start local callback server ----------------------------------------------
echo "Waiting for OAuth callback on port $REDIRECT_PORT..."
echo "(Press Ctrl+C to cancel)"
echo ""

# Use Python for a simple HTTP callback listener
CALLBACK_RESPONSE=$(python3 -c "
import http.server, urllib.parse, sys

class Handler(http.server.BaseHTTPRequestHandler):
    def do_GET(self):
        query = urllib.parse.urlparse(self.path).query
        params = urllib.parse.parse_qs(query)
        code = params.get('code', [''])[0]
        state = params.get('state', [''])[0]

        self.send_response(200)
        self.send_header('Content-Type', 'text/html')
        self.end_headers()
        self.wfile.write(b'<html><body><h1>Authorization successful!</h1><p>You can close this tab.</p></body></html>')

        print(f'CODE={code}')
        print(f'STATE={state}')
        sys.stdout.flush()

        # Shutdown after receiving callback
        import threading
        threading.Thread(target=self.server.shutdown).start()

    def log_message(self, format, *args):
        pass  # Suppress access logs

server = http.server.HTTPServer(('127.0.0.1', $REDIRECT_PORT), Handler)
server.handle_request()
server.server_close()
" 2>/dev/null) || {
    echo "ERROR: Failed to start callback server. Is Python 3 installed?"
    echo "  Alternatively, handle the callback manually and provide the code."
    exit 1
}

# Extract code and state from callback
AUTH_CODE=$(echo "$CALLBACK_RESPONSE" | grep "^CODE=" | cut -d'=' -f2-)
RETURN_STATE=$(echo "$CALLBACK_RESPONSE" | grep "^STATE=" | cut -d'=' -f2-)

# Verify state to prevent CSRF
if [[ "$RETURN_STATE" != "$STATE" ]]; then
    echo "ERROR: State mismatch! Possible CSRF attack."
    echo "  Expected: $STATE"
    echo "  Received: $RETURN_STATE"
    exit 1
fi

if [[ -z "$AUTH_CODE" ]]; then
    echo "ERROR: No authorization code received."
    exit 1
fi

echo "Authorization code received."

# --- Exchange code for token --------------------------------------------------
if [[ -n "$CLIENT_SECRET" ]]; then
    echo "Exchanging authorization code for access token..."

    TOKEN_RESPONSE=$(curl -s -X POST "$TOKEN_URL" \
        -H "Accept: application/json" \
        -d "grant_type=authorization_code" \
        -d "code=$AUTH_CODE" \
        -d "redirect_uri=$REDIRECT_URI" \
        -d "client_id=$CLIENT_ID" \
        -d "client_secret=$CLIENT_SECRET" \
        2>/dev/null)

    echo ""
    echo "Token response (store securely!):"
    echo "$TOKEN_RESPONSE" | python3 -m json.tool 2>/dev/null || echo "$TOKEN_RESPONSE"
    echo ""
    echo "IMPORTANT: Store the access_token securely (env var or secrets manager)."
    echo "  Never commit tokens to version control."
else
    echo ""
    echo "Authorization code: $AUTH_CODE"
    echo ""
    echo "No client secret available for token exchange."
    echo "Exchange manually or set MCP_OAUTH_$(echo "${PROVIDER}" | tr '[:lower:]' '[:upper:]')_CLIENTSECRET"
fi
