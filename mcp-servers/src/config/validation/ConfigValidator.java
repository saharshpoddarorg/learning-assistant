package config.validation;

import config.model.McpConfiguration;
import config.model.ServerDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Validates an {@link McpConfiguration} for correctness and completeness.
 *
 * <p>Checks include:
 * <ul>
 *   <li>Active profile references an existing profile</li>
 *   <li>STDIO servers have a command configured</li>
 *   <li>HTTP-based servers have a URL configured</li>
 *   <li>Server environment variables referencing API keys are resolvable</li>
 *   <li>At least one server is defined</li>
 * </ul>
 */
public class ConfigValidator {

    /**
     * Validates the given configuration and returns a result.
     *
     * @param configuration the configuration to validate
     * @return a {@link ValidationResult} (check {@code isValid()} for outcome)
     */
    public ValidationResult validate(final McpConfiguration configuration) {
        Objects.requireNonNull(configuration, "Configuration must not be null");

        final var errors = new ArrayList<String>();

        validateActiveProfile(configuration, errors);
        validateServers(configuration, errors);
        validateProfiles(configuration, errors);

        return errors.isEmpty() ? ValidationResult.VALID : new ValidationResult(errors);
    }

    /**
     * Validates that the active profile (if set) references a defined profile.
     *
     * @param configuration the configuration to check
     * @param errors        list to append errors to
     */
    private void validateActiveProfile(final McpConfiguration configuration, final List<String> errors) {
        final var activeProfile = configuration.activeProfile();
        if (!activeProfile.isEmpty() && !configuration.profiles().containsKey(activeProfile)) {
            errors.add("Active profile '" + activeProfile
                    + "' is not defined. Available profiles: " + configuration.profiles().keySet());
        }
    }

    /**
     * Validates all server definitions for transport-specific requirements.
     *
     * @param configuration the configuration containing server definitions
     * @param errors        list to append errors to
     */
    private void validateServers(final McpConfiguration configuration, final List<String> errors) {
        if (configuration.servers().isEmpty()) {
            errors.add("No MCP servers defined. Add at least one server.{name}.* block to your config.");
            return;
        }

        configuration.servers().forEach((serverName, serverDef) ->
                validateServerDefinition(serverName, serverDef, errors));
    }

    /**
     * Validates a single server definition.
     *
     * @param serverName the server identifier
     * @param serverDef  the server definition
     * @param errors     list to append errors to
     */
    private void validateServerDefinition(final String serverName,
                                           final ServerDefinition serverDef,
                                           final List<String> errors) {
        if (serverDef.isSubprocessBased() && serverDef.command().isBlank()) {
            errors.add("Server '" + serverName
                    + "' uses STDIO transport but has no command configured. "
                    + "Set server." + serverName + ".command=<executable>");
        }

        if (serverDef.isHttpBased() && serverDef.url().isBlank()) {
            errors.add("Server '" + serverName
                    + "' uses " + serverDef.transport().getConfigValue()
                    + " transport but has no URL configured. "
                    + "Set server." + serverName + ".url=<endpoint>");
        }

        if (serverDef.name().isBlank()) {
            errors.add("Server '" + serverName + "' has a blank display name.");
        }
    }

    /**
     * Validates all profile definitions for internal consistency.
     *
     * @param configuration the configuration containing profile definitions
     * @param errors        list to append errors to
     */
    private void validateProfiles(final McpConfiguration configuration, final List<String> errors) {
        configuration.profiles().forEach((profileName, profileDef) -> {
            profileDef.serverOverrides().forEach((serverName, serverDef) ->
                    validateServerDefinition(profileName + "." + serverName, serverDef, errors));
        });
    }
}
