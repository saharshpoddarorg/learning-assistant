package config.exception;

import config.validation.ValidationResult;

import java.util.Objects;

/**
 * Thrown when configuration validation fails.
 *
 * <p>Contains the {@link ValidationResult} with detailed information
 * about which validation rules were violated.
 */
public class ConfigValidationException extends RuntimeException {

    private final ValidationResult validationResult;

    /**
     * Creates a new config validation exception with the failed result.
     *
     * @param validationResult the result containing all validation errors
     */
    public ConfigValidationException(final ValidationResult validationResult) {
        super("Configuration validation failed: " + validationResult.errorSummary());
        this.validationResult = Objects.requireNonNull(validationResult,
                "Validation result must not be null");
    }

    /**
     * Returns the detailed validation result.
     *
     * @return the validation result containing all errors
     */
    public ValidationResult getValidationResult() {
        return validationResult;
    }
}
