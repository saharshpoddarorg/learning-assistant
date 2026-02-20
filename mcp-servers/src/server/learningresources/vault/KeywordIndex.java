package server.learningresources.vault;

import server.learningresources.model.ConceptArea;
import server.learningresources.model.DifficultyLevel;
import server.learningresources.model.ResourceCategory;

import java.util.HashMap;
import java.util.Map;

/**
 * Static keyword-to-enum mappings used by the discovery engine for intent inference.
 *
 * <p>When a user types a vague query like "java concurrency beginner", the keyword
 * index resolves individual words and phrases into structured enums:
 * <ul>
 *   <li>{@code "concurrency"} → {@link ConceptArea#CONCURRENCY}</li>
 *   <li>{@code "java"} → {@link ResourceCategory#JAVA}</li>
 *   <li>{@code "beginner"} → {@link DifficultyLevel#BEGINNER}</li>
 * </ul>
 *
 * <p>Extracted from {@link ResourceDiscovery} for maintainability — this file is
 * the single place to add new keyword synonyms without touching the scoring or
 * query-handling logic.
 */
public final class KeywordIndex {

    private KeywordIndex() {
        // Static utility — no instances
    }

    // ─── Keyword → Concept ──────────────────────────────────────────

    /** Immutable keyword-to-concept map (80+ entries). */
    private static final Map<String, ConceptArea> CONCEPT_MAP = buildConceptMap();

    /**
     * Returns the keyword-to-concept mapping.
     *
     * @return unmodifiable map from lowercase keyword to {@link ConceptArea}
     */
    public static Map<String, ConceptArea> conceptMap() {
        return CONCEPT_MAP;
    }

    private static Map<String, ConceptArea> buildConceptMap() {
        final var map = new HashMap<String, ConceptArea>();

        // Programming fundamentals
        map.put("oop", ConceptArea.OOP);
        map.put("object-oriented", ConceptArea.OOP);
        map.put("classes", ConceptArea.OOP);
        map.put("inheritance", ConceptArea.OOP);
        map.put("polymorphism", ConceptArea.OOP);
        map.put("encapsulation", ConceptArea.OOP);
        map.put("functional", ConceptArea.FUNCTIONAL_PROGRAMMING);
        map.put("lambda", ConceptArea.FUNCTIONAL_PROGRAMMING);
        map.put("lambdas", ConceptArea.FUNCTIONAL_PROGRAMMING);
        map.put("streams", ConceptArea.FUNCTIONAL_PROGRAMMING);
        map.put("generics", ConceptArea.GENERICS);
        map.put("type-system", ConceptArea.GENERICS);
        map.put("wildcards", ConceptArea.GENERICS);

        // Core CS
        map.put("concurrency", ConceptArea.CONCURRENCY);
        map.put("threads", ConceptArea.CONCURRENCY);
        map.put("async", ConceptArea.CONCURRENCY);
        map.put("parallel", ConceptArea.CONCURRENCY);
        map.put("virtual threads", ConceptArea.CONCURRENCY);
        map.put("synchronization", ConceptArea.CONCURRENCY);
        map.put("data structures", ConceptArea.DATA_STRUCTURES);
        map.put("collections", ConceptArea.DATA_STRUCTURES);
        map.put("list", ConceptArea.DATA_STRUCTURES);
        map.put("map", ConceptArea.DATA_STRUCTURES);
        map.put("algorithms", ConceptArea.ALGORITHMS);
        map.put("sorting", ConceptArea.ALGORITHMS);
        map.put("searching", ConceptArea.ALGORITHMS);
        map.put("dynamic programming", ConceptArea.ALGORITHMS);
        map.put("big-o", ConceptArea.COMPLEXITY_ANALYSIS);
        map.put("complexity", ConceptArea.COMPLEXITY_ANALYSIS);
        map.put("time complexity", ConceptArea.COMPLEXITY_ANALYSIS);
        map.put("memory", ConceptArea.MEMORY_MANAGEMENT);
        map.put("garbage collection", ConceptArea.MEMORY_MANAGEMENT);
        map.put("jvm", ConceptArea.MEMORY_MANAGEMENT);
        map.put("heap", ConceptArea.MEMORY_MANAGEMENT);

        // Software engineering
        map.put("design patterns", ConceptArea.DESIGN_PATTERNS);
        map.put("patterns", ConceptArea.DESIGN_PATTERNS);
        map.put("singleton", ConceptArea.DESIGN_PATTERNS);
        map.put("factory", ConceptArea.DESIGN_PATTERNS);
        map.put("observer", ConceptArea.DESIGN_PATTERNS);
        map.put("strategy", ConceptArea.DESIGN_PATTERNS);
        map.put("clean code", ConceptArea.CLEAN_CODE);
        map.put("refactoring", ConceptArea.CLEAN_CODE);
        map.put("best practices", ConceptArea.CLEAN_CODE);
        map.put("solid", ConceptArea.CLEAN_CODE);
        map.put("testing", ConceptArea.TESTING);
        map.put("unit test", ConceptArea.TESTING);
        map.put("tdd", ConceptArea.TESTING);
        map.put("junit", ConceptArea.TESTING);
        map.put("mocking", ConceptArea.TESTING);
        map.put("api", ConceptArea.API_DESIGN);
        map.put("rest", ConceptArea.API_DESIGN);
        map.put("graphql", ConceptArea.API_DESIGN);
        map.put("architecture", ConceptArea.ARCHITECTURE);
        map.put("microservices", ConceptArea.ARCHITECTURE);
        map.put("hexagonal", ConceptArea.ARCHITECTURE);

        // System design & infra
        map.put("system design", ConceptArea.SYSTEM_DESIGN);
        map.put("scalability", ConceptArea.SYSTEM_DESIGN);
        map.put("load balancing", ConceptArea.SYSTEM_DESIGN);
        map.put("database", ConceptArea.DATABASES);
        map.put("sql", ConceptArea.DATABASES);
        map.put("postgresql", ConceptArea.DATABASES);
        map.put("indexing", ConceptArea.DATABASES);
        map.put("distributed", ConceptArea.DISTRIBUTED_SYSTEMS);
        map.put("consensus", ConceptArea.DISTRIBUTED_SYSTEMS);
        map.put("networking", ConceptArea.NETWORKING);
        map.put("http", ConceptArea.NETWORKING);
        map.put("tcp", ConceptArea.NETWORKING);
        map.put("dns", ConceptArea.NETWORKING);
        map.put("operating systems", ConceptArea.OPERATING_SYSTEMS);
        map.put("processes", ConceptArea.OPERATING_SYSTEMS);

        // DevOps & tooling
        map.put("ci/cd", ConceptArea.CI_CD);
        map.put("ci-cd", ConceptArea.CI_CD);
        map.put("pipeline", ConceptArea.CI_CD);
        map.put("github actions", ConceptArea.CI_CD);
        map.put("docker", ConceptArea.CONTAINERS);
        map.put("kubernetes", ConceptArea.CONTAINERS);
        map.put("containers", ConceptArea.CONTAINERS);
        map.put("k8s", ConceptArea.CONTAINERS);
        map.put("git", ConceptArea.VERSION_CONTROL);
        map.put("version control", ConceptArea.VERSION_CONTROL);
        map.put("branching", ConceptArea.VERSION_CONTROL);
        map.put("gradle", ConceptArea.BUILD_TOOLS);
        map.put("maven", ConceptArea.BUILD_TOOLS);
        map.put("build", ConceptArea.BUILD_TOOLS);
        map.put("monitoring", ConceptArea.OBSERVABILITY);
        map.put("logging", ConceptArea.OBSERVABILITY);
        map.put("tracing", ConceptArea.OBSERVABILITY);

        // Security
        map.put("security", ConceptArea.WEB_SECURITY);
        map.put("owasp", ConceptArea.WEB_SECURITY);
        map.put("xss", ConceptArea.WEB_SECURITY);
        map.put("injection", ConceptArea.WEB_SECURITY);
        map.put("authentication", ConceptArea.WEB_SECURITY);
        map.put("cryptography", ConceptArea.CRYPTOGRAPHY);
        map.put("encryption", ConceptArea.CRYPTOGRAPHY);
        map.put("hashing", ConceptArea.CRYPTOGRAPHY);

        // AI & data
        map.put("machine learning", ConceptArea.MACHINE_LEARNING);
        map.put("deep learning", ConceptArea.MACHINE_LEARNING);
        map.put("neural", ConceptArea.MACHINE_LEARNING);
        map.put("ai", ConceptArea.MACHINE_LEARNING);
        map.put("llm", ConceptArea.LLM_AND_PROMPTING);
        map.put("prompt", ConceptArea.LLM_AND_PROMPTING);
        map.put("gpt", ConceptArea.LLM_AND_PROMPTING);
        map.put("rag", ConceptArea.LLM_AND_PROMPTING);

        // Career & meta
        map.put("interview", ConceptArea.INTERVIEW_PREP);
        map.put("leetcode", ConceptArea.INTERVIEW_PREP);
        map.put("career", ConceptArea.CAREER_DEVELOPMENT);
        map.put("roadmap", ConceptArea.CAREER_DEVELOPMENT);
        map.put("getting started", ConceptArea.GETTING_STARTED);
        map.put("beginner", ConceptArea.GETTING_STARTED);
        map.put("hello world", ConceptArea.GETTING_STARTED);

        return Map.copyOf(map);
    }

    // ─── Keyword → Category ─────────────────────────────────────────

    /** Immutable keyword-to-category map. */
    private static final Map<String, ResourceCategory> CATEGORY_MAP = buildCategoryMap();

    /**
     * Returns the keyword-to-category mapping.
     *
     * @return unmodifiable map from lowercase keyword to {@link ResourceCategory}
     */
    public static Map<String, ResourceCategory> categoryMap() {
        return CATEGORY_MAP;
    }

    private static Map<String, ResourceCategory> buildCategoryMap() {
        final var map = new HashMap<String, ResourceCategory>();
        map.put("java", ResourceCategory.JAVA);
        map.put("spring", ResourceCategory.JAVA);
        map.put("jdk", ResourceCategory.JAVA);
        map.put("jvm", ResourceCategory.JAVA);
        map.put("python", ResourceCategory.PYTHON);
        map.put("django", ResourceCategory.PYTHON);
        map.put("flask", ResourceCategory.PYTHON);
        map.put("javascript", ResourceCategory.JAVASCRIPT);
        map.put("typescript", ResourceCategory.JAVASCRIPT);
        map.put("node", ResourceCategory.JAVASCRIPT);
        map.put("react", ResourceCategory.JAVASCRIPT);
        map.put("web", ResourceCategory.WEB);
        map.put("html", ResourceCategory.WEB);
        map.put("css", ResourceCategory.WEB);
        map.put("frontend", ResourceCategory.WEB);
        map.put("devops", ResourceCategory.DEVOPS);
        map.put("docker", ResourceCategory.DEVOPS);
        map.put("kubernetes", ResourceCategory.DEVOPS);
        map.put("security", ResourceCategory.SECURITY);
        map.put("owasp", ResourceCategory.SECURITY);
        map.put("data", ResourceCategory.DATABASE);
        map.put("database", ResourceCategory.DATABASE);
        map.put("sql", ResourceCategory.DATABASE);
        map.put("ai", ResourceCategory.AI_ML);
        map.put("ml", ResourceCategory.AI_ML);
        map.put("machine learning", ResourceCategory.AI_ML);
        map.put("deep learning", ResourceCategory.AI_ML);
        map.put("algorithm", ResourceCategory.ALGORITHMS);
        map.put("algorithms", ResourceCategory.ALGORITHMS);
        map.put("testing", ResourceCategory.TESTING);
        map.put("junit", ResourceCategory.TESTING);
        map.put("engineering", ResourceCategory.SOFTWARE_ENGINEERING);
        return Map.copyOf(map);
    }

    // ─── Keyword → Difficulty ───────────────────────────────────────

    /** Immutable keyword-to-difficulty map. */
    private static final Map<String, DifficultyLevel> DIFFICULTY_MAP = buildDifficultyMap();

    /**
     * Returns the keyword-to-difficulty mapping.
     *
     * @return unmodifiable map from lowercase keyword to {@link DifficultyLevel}
     */
    public static Map<String, DifficultyLevel> difficultyMap() {
        return DIFFICULTY_MAP;
    }

    private static Map<String, DifficultyLevel> buildDifficultyMap() {
        final var map = new HashMap<String, DifficultyLevel>();
        map.put("beginner", DifficultyLevel.BEGINNER);
        map.put("new", DifficultyLevel.BEGINNER);
        map.put("start", DifficultyLevel.BEGINNER);
        map.put("easy", DifficultyLevel.BEGINNER);
        map.put("intro", DifficultyLevel.BEGINNER);
        map.put("basic", DifficultyLevel.BEGINNER);
        map.put("intermediate", DifficultyLevel.INTERMEDIATE);
        map.put("moderate", DifficultyLevel.INTERMEDIATE);
        map.put("mid", DifficultyLevel.INTERMEDIATE);
        map.put("advanced", DifficultyLevel.ADVANCED);
        map.put("deep", DifficultyLevel.ADVANCED);
        map.put("hard", DifficultyLevel.ADVANCED);
        map.put("expert", DifficultyLevel.EXPERT);
        map.put("master", DifficultyLevel.EXPERT);
        return Map.copyOf(map);
    }
}
