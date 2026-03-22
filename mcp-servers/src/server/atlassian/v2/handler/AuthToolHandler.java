package server.atlassian.v2.handler;

import server.atlassian.v2.auth.AuthException;
import server.atlassian.v2.auth.AuthManager;
import server.atlassian.v2.model.AuthMethod;
import server.atlassian.v2.model.Product;
import server.atlassian.v2.model.ToolResult;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles authentication management tools (login, status, logout, switch).
 */
public final class AuthToolHandler {

    private static final Logger LOGGER = Logger.getLogger(AuthToolHandler.class.getName());
    private static final Product P = Product.CROSS_PRODUCT;

    private final AuthManager authManager;

    public AuthToolHandler(final AuthManager authManager) {
        this.authManager = Objects.requireNonNull(authManager);
    }

    public ToolResult handle(final String tool, final Map<String, String> args) {
        return switch (tool) {
            case "auth_login_browser"  -> loginBrowser();
            case "auth_status"         -> authStatus();
            case "auth_logout"         -> logout();
            case "auth_switch_method"  -> switchMethod(args);
            default -> ToolResult.error(P, tool, "Unknown auth tool: " + tool);
        };
    }

    /**
     * Initiates the browser-based OAuth 2.0 login flow. Opens the user's
     * default browser to the Atlassian consent page and waits for the
     * callback on a localhost server.
     */
    private ToolResult loginBrowser() {
        if (authManager.getActiveMethod() != AuthMethod.OAUTH2_BROWSER) {
            return ToolResult.error(P, "auth_login_browser",
                "OAuth 2.0 browser auth is not the active method. Current method: "
                    + authManager.getActiveMethod().configKey()
                    + ". Use auth_switch_method to switch, or configure oauth2_browser in your config.");
        }
        try {
            authManager.loginViaBrowser();
            return ToolResult.success(P, "auth_login_browser",
                "{\"status\":\"authenticated\",\"message\":\"Browser OAuth login successful.\"}");
        } catch (AuthException ex) {
            LOGGER.log(Level.WARNING, "Browser login failed", ex);
            return ToolResult.error(P, "auth_login_browser", "Login failed: " + ex.getMessage());
        }
    }

    /**
     * Returns the current authentication status.
     */
    private ToolResult authStatus() {
        final var sb = new StringBuilder(256);
        sb.append("{\"method\":\"").append(HandlerUtils.escapeJson(authManager.getActiveMethod().configKey())).append("\"");
        sb.append(",\"authenticated\":").append(authManager.isAuthenticated());
        sb.append(",\"description\":\"").append(HandlerUtils.escapeJson(authManager.statusDescription())).append("\"");
        sb.append('}');
        return ToolResult.success(P, "auth_status", sb.toString());
    }

    /**
     * Clears stored credentials (tokens, refresh tokens, etc.).
     */
    private ToolResult logout() {
        try {
            authManager.clearCredentials();
            return ToolResult.success(P, "auth_logout",
                "{\"status\":\"logged_out\",\"message\":\"Credentials cleared.\"}");
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Logout failed", ex);
            return ToolResult.error(P, "auth_logout", "Logout failed: " + ex.getMessage());
        }
    }

    /**
     * Switches the active auth method (e.g., from api_token to oauth2_browser).
     */
    private ToolResult switchMethod(final Map<String, String> args) {
        final var method = args.get("method");
        if (method == null || method.isBlank()) {
            return ToolResult.error(P, "auth_switch_method", "Missing required: 'method'. Valid values: api_token, pat, oauth2_browser");
        }
        final var parsed = AuthMethod.fromConfigKey(method);
        if (parsed == null) {
            return ToolResult.error(P, "auth_switch_method", "Invalid method: '" + method + "'. Valid values: api_token, pat, oauth2_browser");
        }
        try {
            authManager.switchMethod(parsed);
            return ToolResult.success(P, "auth_switch_method",
                "{\"method\":\"" + HandlerUtils.escapeJson(parsed.configKey()) + "\",\"message\":\"Switched auth method.\"}");
        } catch (AuthException ex) {
            LOGGER.log(Level.WARNING, "Auth switch failed", ex);
            return ToolResult.error(P, "auth_switch_method", "Switch failed: " + ex.getMessage());
        }
    }
}
