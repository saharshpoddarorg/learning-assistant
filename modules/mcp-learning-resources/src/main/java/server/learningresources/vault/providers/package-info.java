/**
 * Category-specific resource providers for the learning resources vault.
 *
 * <p>Each provider implements {@link server.learningresources.vault.ResourceProvider}
 * and supplies a curated list of resources for a specific domain (Java, Python,
 * AI/ML, DevOps, etc.). Providers are composed by
 * {@link server.learningresources.vault.BuiltInResources} to form the complete
 * built-in resource library.
 *
 * <h2>Adding a New Provider</h2>
 * <ol>
 *   <li>Create a final class implementing {@code ResourceProvider}</li>
 *   <li>Return resources via {@code List.of(...)} in the {@code resources()} method</li>
 *   <li>Register the provider in {@code BuiltInResources.PROVIDERS}</li>
 * </ol>
 *
 * @see server.learningresources.vault.ResourceProvider
 * @see server.learningresources.vault.BuiltInResources
 */
package server.learningresources.vault.providers;
