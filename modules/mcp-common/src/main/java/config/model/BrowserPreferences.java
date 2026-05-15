package config.model;

import java.util.Objects;
import java.util.Set;

/**
 * Browser preferences for MCP servers that launch or interact with a browser.
 *
 * <p>Configures which browser executable to use, which browser profile to load,
 * and how browser windows are managed — ensuring that MCP-launched browser
 * sessions do not interfere with the user's existing tabs, windows, or profiles.
 *
 * <p><strong>Why this matters:</strong> Many MCP servers (e.g., web scrapers, OAuth flows,
 * preview servers) need to open a browser. Without explicit browser config, the server
 * may hijack the user's default browser session, stealing focus, opening tabs in the
 * wrong profile, or conflicting with cookies/auth state.
 *
 * @param browserExecutable  path or name of the browser to launch (e.g., "chrome", "firefox",
 *                           "msedge", or a full path like "/usr/bin/google-chrome")
 * @param browserProfile     the browser profile/directory to use (e.g., "MCP-Isolated",
 *                           "Profile 2", or a path to a profile directory). An isolated
 *                           profile prevents cookie/session contamination with personal browsing.
 * @param browserAccount     the browser-synced account email (empty string if not applicable)
 * @param launchMode         how to open the browser: "new-window", "new-tab", "incognito",
 *                           "app-mode"
 * @param headless           whether to launch the browser in headless mode (no visible UI)
 * @param remoteDebuggingPort the Chrome DevTools Protocol port (0 = disabled). Used by
 *                            automation tools like Playwright/Puppeteer.
 * @param windowWidth        default browser window width in pixels (0 = use browser default)
 * @param windowHeight       default browser window height in pixels (0 = use browser default)
 */
public record BrowserPreferences(
        String browserExecutable,
        String browserProfile,
        String browserAccount,
        String launchMode,
        boolean headless,
        int remoteDebuggingPort,
        int windowWidth,
        int windowHeight
) {

    /** Default browser preferences — system default browser, no profile isolation. */
    public static final BrowserPreferences DEFAULT = new BrowserPreferences(
            "", "", "", "new-window", false, 0, 0, 0
    );

    private static final int MAX_PORT = 65535;
    private static final int MAX_DIMENSION = 7680;

    /** Allowed values for the {@code launchMode} field. */
    private static final Set<String> VALID_LAUNCH_MODES = Set.of(
            "", "new-window", "new-tab", "incognito", "app-mode"
    );

    /**
     * Creates a {@link BrowserPreferences} instance with validation.
     *
     * @param browserExecutable  browser command or path
     * @param browserProfile     browser profile name or directory
     * @param browserAccount     synced account email
     * @param launchMode         window launch strategy
     * @param headless           headless mode flag
     * @param remoteDebuggingPort debugging port (0–65535)
     * @param windowWidth        window width in pixels (0 = default)
     * @param windowHeight       window height in pixels (0 = default)
     */
    public BrowserPreferences {
        Objects.requireNonNull(browserExecutable,
                "Browser executable must not be null (use empty string for system default)");
        Objects.requireNonNull(browserProfile,
                "Browser profile must not be null (use empty string for default profile)");
        Objects.requireNonNull(browserAccount,
                "Browser account must not be null (use empty string if not applicable)");
        Objects.requireNonNull(launchMode,
                "Launch mode must not be null (use empty string for default)");

        if (!VALID_LAUNCH_MODES.contains(launchMode)) {
            throw new IllegalArgumentException(
                    "launchMode must be one of " + VALID_LAUNCH_MODES
                            + ", got: '" + launchMode + "'");
        }

        if (remoteDebuggingPort < 0 || remoteDebuggingPort > MAX_PORT) {
            throw new IllegalArgumentException(
                    "remoteDebuggingPort must be between 0 and " + MAX_PORT
                            + ", got: " + remoteDebuggingPort);
        }
        if (windowWidth < 0 || windowWidth > MAX_DIMENSION) {
            throw new IllegalArgumentException(
                    "windowWidth must be between 0 and " + MAX_DIMENSION + ", got: " + windowWidth);
        }
        if (windowHeight < 0 || windowHeight > MAX_DIMENSION) {
            throw new IllegalArgumentException(
                    "windowHeight must be between 0 and " + MAX_DIMENSION + ", got: " + windowHeight);
        }
    }

    /**
     * Checks whether a specific browser executable has been configured.
     *
     * @return {@code true} if a browser executable is set (not system default)
     */
    public boolean hasCustomBrowser() {
        return !browserExecutable.isBlank();
    }

    /**
     * Checks whether a dedicated browser profile is configured for isolation.
     *
     * @return {@code true} if a profile is configured
     */
    public boolean hasIsolatedProfile() {
        return !browserProfile.isBlank();
    }

    /**
     * Checks whether remote debugging is enabled.
     *
     * @return {@code true} if a non-zero debugging port is configured
     */
    public boolean isRemoteDebuggingEnabled() {
        return remoteDebuggingPort > 0;
    }
}
