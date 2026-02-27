package server.learningresources.vault.providers;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.ContentFreshness;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.LanguageApplicability;
import server.learningresources.model.LearningResource;
import server.learningresources.model.ResourceCategory;
import server.learningresources.model.ResourceType;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;

/**
 * Curated AI, machine learning, and LLM/prompt-engineering learning resources.
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
                        "Free courses teaching deep learning with a top-down, code-first "
                                + "approach. Covers image classification, NLP, tabular data, "
                                + "and collaborative filtering using PyTorch and the fastai library.",
                        ResourceType.VIDEO_COURSE,
                        List.of(ResourceCategory.AI_ML),
                        List.of(ConceptArea.MACHINE_LEARNING, ConceptArea.GETTING_STARTED),
                        List.of("deep-learning", "pytorch", "nlp", "image-classification",
                                "practical", "free-course"),
                        "Jeremy Howard & Rachel Thomas",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "prompt-engineering-guide",
                        "Prompt Engineering Guide",
                        "https://www.promptingguide.ai/",
                        "Comprehensive guide to prompt engineering techniques for LLMs. "
                                + "Covers zero-shot, few-shot, chain-of-thought, self-consistency, "
                                + "ReAct, and model-specific strategies.",
                        ResourceType.TUTORIAL,
                        List.of(ResourceCategory.AI_ML),
                        List.of(ConceptArea.LLM_AND_PROMPTING),
                        List.of("prompt-engineering", "llm", "gpt", "chain-of-thought",
                                "few-shot", "zero-shot", "techniques"),
                        "DAIR.AI",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "openai-api-docs",
                        "OpenAI API Documentation",
                        "https://platform.openai.com/docs/",
                        "Official OpenAI API reference — models, chat completions, embeddings, "
                                + "fine-tuning, function calling, assistants API, and best practices "
                                + "for building LLM-powered applications.",
                        ResourceType.API_REFERENCE,
                        List.of(ResourceCategory.AI_ML),
                        List.of(ConceptArea.LLM_AND_PROMPTING, ConceptArea.API_DESIGN),
                        List.of("official", "openai", "gpt", "api", "function-calling",
                                "embeddings", "fine-tuning", "assistants"),
                        "OpenAI",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "3b1b-neural-networks",
                        "3Blue1Brown — Neural Networks (Video Series)",
                        "https://www.3blue1brown.com/topics/neural-networks",
                        "Grant Sanderson's beautifully animated video series on neural networks "
                                + "and deep learning. Covers: what a neural network is, gradient "
                                + "descent, backpropagation, and — in later episodes — transformers, "
                                + "attention mechanisms, LLMs, and diffusion models. Uses visual "
                                + "intuition instead of dense math. Ideal starting point before "
                                + "diving into fast.ai or a full ML course.",
                        ResourceType.PLAYLIST,
                        List.of(ResourceCategory.AI_ML),
                        List.of(ConceptArea.MACHINE_LEARNING, ConceptArea.DEEP_LEARNING),
                        List.of("3blue1brown", "neural-networks", "deep-learning",
                                "gradient-descent", "backpropagation", "transformers",
                                "attention", "llm", "visualizations", "intuition",
                                "beginner", "grant-sanderson"),
                        "Grant Sanderson (3Blue1Brown)",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                )
        );
    }
}
