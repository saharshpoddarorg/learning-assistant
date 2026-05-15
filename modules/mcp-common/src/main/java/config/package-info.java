/**
 * Configuration module — loading, parsing, validating, and managing MCP server configuration.
 *
 * <p>This is the root package of the configuration system. The primary entry point is
 * {@link config.ConfigManager}, a facade that orchestrates the full configuration lifecycle:
 * load → merge → parse → validate → resolve.
 *
 * <p>Sub-packages:
 * <ul>
 *   <li>{@code config.model} — immutable configuration records ({@link config.model.McpConfiguration} etc.)</li>
 *   <li>{@code config.loader} — strategy-based config loading ({@link config.loader.ConfigSource} implementations)</li>
 *   <li>{@code config.validation} — structural and semantic validation</li>
 *   <li>{@code config.exception} — typed exceptions for load and validation failures</li>
 * </ul>
 *
 * @see config.ConfigManager
 * @see config.model.McpConfiguration
 */
package config;
