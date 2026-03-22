package server.atlassian.v2.config;

import server.atlassian.v2.model.AuthMethod;
import server.atlassian.v2.model.DeploymentVariant;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Immutable configuration for the Atlassian MCP Server v2.
 *
 * <p>Extends v1 config with OAuth credentials, credential store path,
 * and versioning info. Created by {@link ConfigLoaderV2}.
 *
 * @param instanceName      human-readable name for this instance
 * @param variant           deployment type (Cloud, Data Center, Server, Custom)
 * @param jiraBaseUrl       Jira API base URL (null if disabled)
 * @param confluenceBaseUrl Confluence API base URL (null if disabled)
 * @param bitbucketBaseUrl  Bitbucket API base URL (null if disabled)
 * @param authMethod        the configured auth method (null = auto-detect)
 * @param authEmail         user email (for API token auth)
 * @param authToken         API token or PAT value
 * @param oauthClientId     OAuth 2.0 client ID (null if not configured)
 * @param oauthClientSecret OAuth 2.0 client secret (null if not configured)
 * @param credentialDir     directory for storing OAuth tokens
 * @param connectTimeoutMs  HTTP connection timeout in milliseconds
 * @param readTimeoutMs     HTTP read timeout in milliseconds
 * @param jiraEnabled       whether Jira tools are active
 * @param confluenceEnabled whether Confluence tools are active
 * @param bitbucketEnabled  whether Bitbucket tools are active
 */
public record ServerConfigV2(
        String instanceName,
        DeploymentVariant variant,
        String jiraBaseUrl,
        String confluenceBaseUrl,
        String bitbucketBaseUrl,
        AuthMethod authMethod,
        String authEmail,
        String authToken,
        String oauthClientId,
        String oauthClientSecret,
        Path credentialDir,
        int connectTimeoutMs,
        int readTimeoutMs,
        boolean jiraEnabled,
        boolean confluenceEnabled,
        boolean bitbucketEnabled
) {

    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 10_000;
    public static final int DEFAULT_READ_TIMEOUT_MS = 30_000;

    public ServerConfigV2 {
        Objects.requireNonNull(instanceName, "instanceName must not be null");
        Objects.requireNonNull(variant, "variant must not be null");
        Objects.requireNonNull(credentialDir, "credentialDir must not be null");

        if (jiraEnabled) {
            Objects.requireNonNull(jiraBaseUrl, "jiraBaseUrl required when Jira is enabled");
        }
        if (confluenceEnabled) {
            Objects.requireNonNull(confluenceBaseUrl, "confluenceBaseUrl required when Confluence is enabled");
        }
        if (bitbucketEnabled) {
            Objects.requireNonNull(bitbucketBaseUrl, "bitbucketBaseUrl required when Bitbucket is enabled");
        }
    }

    /** Normalizes a URL by stripping trailing slashes. */
    public static String normalize(final String url) {
        if (url == null) return null;
        var result = url.strip();
        while (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result.isEmpty() ? null : result;
    }
}
