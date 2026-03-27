package server.learningresources.vault.providers;

import server.learningresources.model.*;
import server.learningresources.vault.ResourceProvider;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * Curated messaging and security learning resources — message brokers,
 * async messaging patterns, OWASP, and security best practices.
 *
 * <p>Database resources (SQL, NoSQL, search engines) have been moved to
 * {@link SystemDesignResources.DatabaseResources}.
 *
 * @see SystemDesignResources
 */
public final class DataAndSecurityResources implements ResourceProvider
{

    @Override
    public List<LearningResource> resources()
    {
        final var now = Instant.now();
        return List.of(

                new LearningResource(
                        "owasp-top-ten",
                        "OWASP Top Ten — Web Application Security Risks",
                        "https://owasp.org/www-project-top-ten/",
                        "The definitive list of the most critical web application security "
                                + "risks. Each risk includes description, examples, prevention "
                                + "techniques, and references. Standard for security audits.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.SECURITY, ResourceCategory.WEB),
                        Set.of(ConceptArea.WEB_SECURITY, ConceptArea.API_DESIGN),
                        Set.of("official", "owasp", "security", "injection", "xss", "csrf",
                                "authentication", "authorization"),
                        "OWASP Foundation",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.PERIODICALLY_UPDATED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "owasp-cheat-sheets",
                        "OWASP Cheat Sheet Series",
                        "https://cheatsheetseries.owasp.org/",
                        "Collection of concise, actionable security cheat sheets. Covers "
                                + "authentication, session management, input validation, "
                                + "cryptographic storage, REST security, and more.",
                        ResourceType.CHEAT_SHEET,
                        Set.of(ResourceCategory.SECURITY),
                        Set.of(ConceptArea.WEB_SECURITY, ConceptArea.CRYPTOGRAPHY,
                                ConceptArea.API_DESIGN),
                        Set.of("official", "owasp", "security", "cheat-sheet", "authentication",
                                "session-management", "cryptography"),
                        "OWASP Foundation",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                // ─── Messaging & Streaming ──────────────────────────────────

                new LearningResource(
                        "kafka-docs",
                        "Apache Kafka Documentation",
                        "https://kafka.apache.org/documentation/",
                        "Official Apache Kafka documentation — the distributed event streaming "
                                + "platform. Topics, partitions, consumer groups, producers, Kafka "
                                + "Streams, Kafka Connect, Schema Registry, exactly-once semantics, "
                                + "and cluster management.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.DEVOPS, ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.ASYNC_MESSAGING,
                                ConceptArea.DISTRIBUTED_SYSTEMS,
                                ConceptArea.SYSTEM_DESIGN,
                                ConceptArea.ARCHITECTURE),
                        Set.of("official", "kafka", "event-streaming", "pub-sub", "topics",
                                "partitions", "consumer-groups", "kafka-streams",
                                "message-broker", "async-communication"),
                        "Apache Software Foundation",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "rabbitmq-docs",
                        "RabbitMQ Documentation",
                        "https://www.rabbitmq.com/docs",
                        "Official RabbitMQ documentation — the most widely-deployed open-source "
                                + "message broker. Covers AMQP 0-9-1 protocol, exchanges (direct, "
                                + "fanout, topic, headers), queues, bindings, acknowledgements, "
                                + "publisher confirms, dead-letter exchanges, clustering, quorum "
                                + "queues, streams, and shovel/federation for multi-site setups. "
                                + "Supported by Broadcom/VMware Tanzu.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING, ResourceCategory.DEVOPS),
                        Set.of(ConceptArea.ASYNC_MESSAGING,
                                ConceptArea.DISTRIBUTED_SYSTEMS,
                                ConceptArea.ARCHITECTURE),
                        Set.of("official", "rabbitmq", "amqp", "message-broker", "queues",
                                "exchanges", "pub-sub", "dead-letter", "clustering",
                                "async-communication", "event-driven"),
                        "Broadcom / VMware Tanzu",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "enterprise-integration-patterns",
                        "Enterprise Integration Patterns — Gregor Hohpe & Bobby Woolf",
                        "https://www.enterpriseintegrationpatterns.com/",
                        "The foundational pattern language for asynchronous messaging — 65 "
                                + "integration patterns covering message channels, message "
                                + "construction, routing, transformation, and endpoints. Patterns "
                                + "like Publish-Subscribe Channel, Content-Based Router, Message "
                                + "Filter, Aggregator, and Splitter are used in Apache Camel, "
                                + "Spring Integration, MuleSoft, and modern event-driven "
                                + "microservices. Now includes modern examples with AWS Step "
                                + "Functions and GCP Workflows.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.ASYNC_MESSAGING,
                                ConceptArea.ARCHITECTURE,
                                ConceptArea.DESIGN_PATTERNS,
                                ConceptArea.DISTRIBUTED_SYSTEMS),
                        Set.of("eip", "enterprise-integration", "messaging-patterns",
                                "pub-sub", "content-based-router", "aggregator", "splitter",
                                "gregor-hohpe", "async-messaging", "event-driven"),
                        "Gregor Hohpe & Bobby Woolf",
                        DifficultyLevel.ADVANCED,
                        ContentFreshness.EVERGREEN,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "microservices-io-messaging",
                        "Microservices.io — Messaging & Event-Driven Patterns",
                        "https://microservices.io/patterns/communication-style/messaging.html",
                        "Chris Richardson's pattern catalog for asynchronous inter-service "
                                + "communication. Covers messaging styles (request/response, "
                                + "notifications, publish/subscribe, request/async response), "
                                + "Saga pattern for distributed transactions, CQRS for query "
                                + "separation, Transactional Outbox for reliable messaging, and "
                                + "Domain Events. Essential reference for microservice architects.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING),
                        Set.of(ConceptArea.ASYNC_MESSAGING,
                                ConceptArea.ARCHITECTURE,
                                ConceptArea.DISTRIBUTED_SYSTEMS,
                                ConceptArea.DESIGN_PATTERNS),
                        Set.of("microservices", "messaging", "saga", "cqrs", "event-sourcing",
                                "transactional-outbox", "domain-events", "pub-sub",
                                "chris-richardson", "async-communication"),
                        "Chris Richardson",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.UNIVERSAL, now
                ),

                new LearningResource(
                        "confluent-developer",
                        "Confluent Developer — Kafka & Flink Learning",
                        "https://developer.confluent.io/",
                        "Confluent's developer learning hub for Apache Kafka and Apache Flink. "
                                + "Free video courses (Kafka 101, Kafka Connect 101, Kafka Streams "
                                + "101, Flink 101), event streaming design patterns, full-code "
                                + "tutorials in Java/Python/Go/.NET/Node.js, and Kafka internals "
                                + "deep-dives. Built by the Kafka co-creators.",
                        ResourceType.VIDEO_COURSE,
                        Set.of(ResourceCategory.SOFTWARE_ENGINEERING, ResourceCategory.DEVOPS),
                        Set.of(ConceptArea.ASYNC_MESSAGING,
                                ConceptArea.DISTRIBUTED_SYSTEMS,
                                ConceptArea.SYSTEM_DESIGN),
                        Set.of("confluent", "kafka", "flink", "event-streaming", "kafka-streams",
                                "kafka-connect", "schema-registry", "free-courses",
                                "async-communication", "message-broker"),
                        "Confluent / Kafka Co-Creators",
                        DifficultyLevel.BEGINNER,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        false, true, LanguageApplicability.MULTI_LANGUAGE, now
                )
        );
    }
}
