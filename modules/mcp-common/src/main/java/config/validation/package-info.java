/**
 * Configuration validation for the MCP system.
 *
 * <p>Validates a parsed {@link config.model.McpConfiguration} for structural
 * and semantic correctness:
 * <ul>
 *   <li>{@link config.validation.ConfigValidator} — validates active profile exists,
 *       STDIO servers have commands, HTTP servers have URLs, and other invariants</li>
 *   <li>{@link config.validation.ValidationResult} — immutable record holding the outcome
 *       of a validation pass (empty error list = valid)</li>
 * </ul>
 *
 * @see config.ConfigManager
 * @see config.exception.ConfigValidationException
 */
package config.validation;
