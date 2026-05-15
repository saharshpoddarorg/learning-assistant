package server.learningresources.vault;

import server.learningresources.model.LearningResource;

import java.util.List;

/**
 * Provides a collection of learning resources for a specific domain or category.
 *
 * <p>Implementations supply a focused, curated list of resources (e.g., Java resources,
 * DevOps resources, AI/ML resources). The {@link BuiltInResources} class composes
 * all providers to build the complete vault.
 *
 * <p>This interface enables modular resource management — new categories can be
 * added by implementing this interface and registering the provider in
 * {@link BuiltInResources#all()}.
 *
 * @see BuiltInResources
 * @see ResourceVault
 */
@FunctionalInterface
public interface ResourceProvider {

    /**
     * Returns all learning resources managed by this provider.
     *
     * @return an unmodifiable list of curated resources
     */
    List<LearningResource> resources();
}
