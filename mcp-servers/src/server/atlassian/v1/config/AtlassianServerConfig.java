package server.atlassian.v1.config;

import server.atlassian.v1.model.AtlassianCredentials;

/**
 * Immutable configuration record for one Atlassian server instance.
 *
 * <p>Holds all information needed to connect to a specific Atlassian deployment —
 * its variant, per-product base URLs, credentials, and HTTP settings.
 *
 * <p>Per-product URLs are kept separate because the products may live at
 * different hosts or path prefixes:
 * <ul>
 *   <li>Jira Cloud: {@code https://workspace.atlassian.net}</li>
 *   <li>Confluence Cloud: {@code https://workspace.atlassian.net/wiki}</li>
 *   <li>Bitbucket Cloud: {@code https://api.bitbucket.org} (entirely different host)</li>
 *   <li>Data Center: all three products share the same host but with different paths</li>
 * </ul>
 *
 * <p>Instances are created via {@link AtlassianConfigLoader#load()}.
 *
 * @param instanceName      a human-readable name for this instance (e.g., "work-cloud", "colleague-dc")
 * @param variant           the deployment type ({@link AtlassianVariant})
 * @param jiraBaseUrl       base URL for Jira API calls (e.g., {@code https://workspace.atlassian.net})
 * @param confluenceBaseUrl base URL for Confluence API calls
 *                          (e.g., {@code https://workspace.atlassian.net/wiki})
 * @param bitbucketBaseUrl  base URL for Bitbucket API calls
 *                          (e.g., {@code https://api.bitbucket.org})
 * @param credentials       authentication credentials (API token or PAT)
 * @param connectTimeoutMs  HTTP connection timeout in milliseconds
 * @param readTimeoutMs     HTTP read timeout in milliseconds
 * @param jiraEnabled       whether the Jira tool set is active on this server
 * @param confluenceEnabled whether the Confluence tool set is active on this server
 * @param bitbucketEnabled  whether the Bitbucket tool set is active on this server
 */
public record AtlassianServerConfig(
        String instanceName,
        AtlassianVariant variant,
        String jiraBaseUrl,
        String confluenceBaseUrl,
        String bitbucketBaseUrl,
        AtlassianCredentials credentials,
        int connectTimeoutMs,
        int readTimeoutMs,
        boolean jiraEnabled,
        boolean confluenceEnabled,
        boolean bitbucketEnabled
) {

    /** Default HTTP connection timeout (10 seconds). */
    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 10_000;

    /** Default HTTP read timeout (30 seconds). */
    public static final int DEFAULT_READ_TIMEOUT_MS = 30_000;

    /**
     * Compact constructor — validates required fields.
     */
    public AtlassianServerConfig {
        java.util.Objects.requireNonNull(instanceName, "instanceName must not be null");
        java.util.Objects.requireNonNull(variant, "variant must not be null");
        java.util.Objects.requireNonNull(credentials, "credentials must not be null");

        // At least one product must have a URL if it is enabled
        if (jiraEnabled) {
            java.util.Objects.requireNonNull(jiraBaseUrl,
                    "jiraBaseUrl must not be null when jiraEnabled=true");
        }
        if (confluenceEnabled) {
            java.util.Objects.requireNonNull(confluenceBaseUrl,
                    "confluenceBaseUrl must not be null when confluenceEnabled=true");
        }
        if (bitbucketEnabled) {
            java.util.Objects.requireNonNull(bitbucketBaseUrl,
                    "bitbucketBaseUrl must not be null when bitbucketEnabled=true");
        }
    }

    /**
     * Returns the Jira base URL (trimmed, no trailing slash).
     *
     * @return normalized Jira base URL, or {@code null} when Jira is not configured
     */
    @Override
    public String jiraBaseUrl() {
        return normalize(jiraBaseUrl);
    }

    /**
     * Returns the Confluence base URL (trimmed, no trailing slash).
     *
     * @return normalized Confluence base URL, or {@code null} when Confluence is not configured
     */
    @Override
    public String confluenceBaseUrl() {
        return normalize(confluenceBaseUrl);
    }

    /**
     * Returns the Bitbucket base URL (trimmed, no trailing slash).
     *
     * @return normalized Bitbucket base URL, or {@code null} when Bitbucket is not configured
     */
    @Override
    public String bitbucketBaseUrl() {
        return normalize(bitbucketBaseUrl);
    }

    /**
     * Convenience factory for a minimal cloud configuration using the default timeouts.
     *
     * <p>For Cloud, Confluence URL is derived as {@code jiraBaseUrl + "/wiki"} and
     * Bitbucket uses the standard {@code https://api.bitbucket.org} host.
     *
     * @param instanceName  a label for this instance (e.g., "my-company-cloud")
     * @param jiraBaseUrl   the Jira Cloud base URL (e.g., {@code https://myco.atlassian.net})
     * @param credentials   the API token credentials
     * @return a fully configured cloud instance with all three products enabled
     */
    public static AtlassianServerConfig cloudDefaults(
            final String instanceName,
            final String jiraBaseUrl,
            final AtlassianCredentials credentials) {

        final String normalizedJira = normalize(jiraBaseUrl);
        return new AtlassianServerConfig(
                instanceName,
                AtlassianVariant.CLOUD,
                normalizedJira,
                normalizedJira + "/wiki",
                "https://api.bitbucket.org",
                credentials,
                DEFAULT_CONNECT_TIMEOUT_MS,
                DEFAULT_READ_TIMEOUT_MS,
                true,
                true,
                true
        );
    }

    /**
     * Convenience factory for a Data Center or Server instance using the default timeouts.
     *
     * <p>For Data Center, all products share the same host. Jira, Confluence, and
     * Bitbucket base URLs must be supplied explicitly (they may share a host but
     * have different context paths, e.g., {@code /jira}, {@code /confluence}).
     *
     * @param instanceName      a label for this instance
     * @param jiraBaseUrl       the Jira DC base URL
     * @param confluenceBaseUrl the Confluence DC base URL (may be on a different port or path)
     * @param bitbucketBaseUrl  the Bitbucket DC base URL (may be on a different port or path)
     * @param credentials       the PAT credentials
     * @param variant           {@link AtlassianVariant#DATA_CENTER} or {@link AtlassianVariant#SERVER}
     * @return a fully configured self-managed instance with all three products enabled
     */
    public static AtlassianServerConfig selfManagedDefaults(
            final String instanceName,
            final String jiraBaseUrl,
            final String confluenceBaseUrl,
            final String bitbucketBaseUrl,
            final AtlassianCredentials credentials,
            final AtlassianVariant variant) {

        return new AtlassianServerConfig(
                instanceName,
                variant,
                jiraBaseUrl,
                confluenceBaseUrl,
                bitbucketBaseUrl,
                credentials,
                DEFAULT_CONNECT_TIMEOUT_MS,
                DEFAULT_READ_TIMEOUT_MS,
                true,
                true,
                true
        );
    }

    private static String normalize(final String url) {
        if (url == null) return null;
        final var trimmed = url.strip();
        return trimmed.endsWith("/") ? trimmed.substring(0, trimmed.length() - 1) : trimmed;
    }

    @Override
    public String toString() {
        // Deliberately omit credential details from toString()
        return "AtlassianServerConfig["
                + "instanceName=" + instanceName
                + ", variant=" + variant
                + ", jiraUrl=" + jiraBaseUrl()
                + ", confluenceUrl=" + confluenceBaseUrl()
                + ", bitbucketUrl=" + bitbucketBaseUrl()
                + ", jiraEnabled=" + jiraEnabled
                + ", confluenceEnabled=" + confluenceEnabled
                + ", bitbucketEnabled=" + bitbucketEnabled
                + "]";
    }
}
