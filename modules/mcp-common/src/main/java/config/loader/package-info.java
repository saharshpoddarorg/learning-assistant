/**
 * Strategy-based configuration loading and parsing.
 *
 * <p>This package implements a pluggable loading pipeline:
 * <ul>
 *   <li>{@link config.loader.ConfigSource} — abstraction for a key-value configuration backend</li>
 *   <li>{@link config.loader.PropertiesConfigSource} — loads from {@code .properties} files on disk
 *       (supports optional files)</li>
 *   <li>{@link config.loader.EnvironmentConfigSource} — loads from {@code MCP_}-prefixed environment
 *       variables (highest precedence)</li>
 *   <li>{@link config.loader.ConfigParser} — transforms flat {@code Properties} into the structured
 *       {@link config.model.McpConfiguration} model using hierarchical key conventions</li>
 * </ul>
 *
 * <p>Sources are merged in precedence order by {@link config.ConfigManager} before parsing.
 *
 * @see config.ConfigManager
 * @see config.loader.ConfigSource
 */
package config.loader;
