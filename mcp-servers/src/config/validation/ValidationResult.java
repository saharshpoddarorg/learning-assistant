package config.validation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Holds the outcome of a configuration validation pass.
 *
 * <p>Contains a list of error messages describing any issues found.
 * An empty error list means the configuration is valid.
 *
 * @param errors list of validation error messages (empty if valid)
 */
public record ValidationResult(List<String> errors) {

    /** A reusable valid result with no errors. */
    public static final ValidationResult VALID = new ValidationResult(List.of());

    /**
     * Creates a {@link ValidationResult} with defensive copying.
     *
     * @param errors the validation error messages
     */
    public ValidationResult {
        Objects.requireNonNull(errors, "Errors list must not be null");
        errors = Collections.unmodifiableList(List.copyOf(errors));
    }

    /**
     * Checks whether the configuration passed validation.
     *
     * @return {@code true} if there are no errors
     */
    public boolean isValid() {
        return errors.isEmpty();
    }

    /**
     * Returns a single-line summary of all errors, joined by semicolons.
     *
     * @return the error summary, or "valid" if no errors
     */
    public String errorSummary() {
        if (isValid()) {
            return "valid";
        }
        return String.join("; ", errors);
    }

    /**
     * Returns a multi-line formatted report of all errors.
     *
     * @return the formatted error report
     */
    public String formatReport() {
        if (isValid()) {
            return "Configuration is valid.";
        }

        final var builder = new StringBuilder("Configuration validation failed with ")
                .append(errors.size())
                .append(" error(s):\n");

        for (int index = 0; index < errors.size(); index++) {
            builder.append("  ").append(index + 1).append(". ").append(errors.get(index)).append("\n");
        }

        return builder.toString();
    }
}
