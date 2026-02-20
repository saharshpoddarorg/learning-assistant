package server.learningresources.vault.providers;

import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated AI and machine-learning learning resources.
 *
 * <p>Includes: fast.ai (practical deep learning), Prompt Engineering Guide.
 */
public final class AiMlResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "fast-ai",
                        "fast.ai — Practical Deep Learning for Coders",
                        "https://www.fast.ai/",
                        "Free courses teaching deep learning using a top-down, practical approach. "
                                + "Starts with training models immediately, then progressively deepens "
                                + "understanding. Built on PyTorch with the fastai library.",
                        ResourceType.COURSE,
                        List.of(ResourceCategory.AI_ML, ResourceCategory.PYTHON),
                        List.of("deep-learning", "pytorch", "machine-learning", "neural-networks", "fastai"),
                        "Jeremy Howard & Rachel Thomas",
                        "intermediate",
                        true, now
                ),

                new LearningResource(
                        "prompt-engineering-guide",
                        "Prompt Engineering Guide",
                        "https://www.promptingguide.ai/",
                        "Comprehensive guide to prompt engineering techniques for large language models. "
                                + "Covers zero-shot, few-shot, chain-of-thought, ReAct, and advanced "
                                + "prompting patterns with practical examples.",
                        ResourceType.ARTICLE,
                        List.of(ResourceCategory.AI_ML),
                        List.of("prompt-engineering", "llm", "gpt", "ai", "chatgpt"),
                        "DAIR.AI",
                        "beginner",
                        true, now
                )
        );
    }
}
