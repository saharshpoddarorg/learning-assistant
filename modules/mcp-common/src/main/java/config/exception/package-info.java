/**
 * Custom exceptions for the configuration module.
 *
 * <p>Both exceptions are unchecked ({@link RuntimeException}) and signal
 * distinct failure modes in the configuration lifecycle:
 * <ul>
 *   <li>{@link config.exception.ConfigLoadException} — configuration cannot be loaded
 *       (file not found, malformed content, I/O errors)</li>
 *   <li>{@link config.exception.ConfigValidationException} — configuration loaded but
 *       structurally or semantically invalid; carries a
 *       {@link config.validation.ValidationResult} with details</li>
 * </ul>
 *
 * @see config.ConfigManager
 */
package config.exception;
