package server.learningresources.vault;

import server.learningresources.model.LearningResource;
import server.learningresources.vault.providers.AiMlResources;
import server.learningresources.vault.providers.AlgorithmsResources;
import server.learningresources.vault.providers.BuildToolsResources;
import server.learningresources.vault.providers.DataAndSecurityResources;
import server.learningresources.vault.providers.DataStructuresResources;
import server.learningresources.vault.providers.DevOpsResources;
import server.learningresources.vault.providers.DigitalNotetakingResources;
import server.learningresources.vault.providers.EngineeringResources;
import server.learningresources.vault.providers.GeneralResources;
import server.learningresources.vault.providers.JavaResources;
import server.learningresources.vault.providers.PythonResources;
import server.learningresources.vault.providers.VcsResources;
import server.learningresources.vault.providers.WebResources;

import java.util.Collections;
import java.util.List;

/**
 * Pre-loaded library of famous, useful, worldwide learning resources.
 *
 * <p>This is the "vault" — a curated collection of high-quality resources
 * organized by topic and type. These are well-known, authoritative sources
 * that every developer should know about.
 *
 * <p>Categories include official documentation, community tutorials, blogs,
 * open-source project guides, video courses, and reference materials.
 *
 * <p>Resources are organized into domain-specific providers in the
 * {@link server.learningresources.vault.providers} package. Each provider
 * implements {@link ResourceProvider} and contributes resources for a
 * specific category (Java, Python, AI/ML, DevOps, etc.).
 *
 * @see ResourceProvider
 * @see server.learningresources.vault.providers
 */
public final class BuiltInResources {

    /** All registered resource providers, ordered by domain. */
    private static final List<ResourceProvider> PROVIDERS = List.of(
            new JavaResources(),
            new WebResources(),
            new PythonResources(),
            new AlgorithmsResources(),
            new DataStructuresResources(),
            new EngineeringResources(),
            new DevOpsResources(),
            new VcsResources(),
            new BuildToolsResources(),
            new DataAndSecurityResources(),
            new AiMlResources(),
            new DigitalNotetakingResources(),
            new GeneralResources()
    );

    private BuiltInResources() {
        // Utility class — no instantiation
    }

    /**
     * Returns all built-in learning resources from every registered provider.
     *
     * @return an unmodifiable list of curated resources
     */
    public static List<LearningResource> all() {
        return Collections.unmodifiableList(
                PROVIDERS.stream()
                        .map(ResourceProvider::resources)
                        .flatMap(List::stream)
                        .toList()
        );
    }

    /**
     * Returns the registered resource providers.
     *
     * @return an unmodifiable list of providers
     */
    public static List<ResourceProvider> providers() {
        return PROVIDERS;
    }
}
