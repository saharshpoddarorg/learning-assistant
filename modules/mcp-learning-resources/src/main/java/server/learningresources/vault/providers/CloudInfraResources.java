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
import java.util.Set;

/**
 * Curated resources for cloud platforms, infrastructure as code, and observability.
 *
 * <p>Covers the full cloud-native stack:
 * <ul>
 *   <li>Cloud platforms: AWS, Google Cloud, Microsoft Azure</li>
 *   <li>Infrastructure as Code: Terraform, Ansible</li>
 *   <li>Observability: Prometheus, Grafana</li>
 * </ul>
 *
 * <p>Resources span three difficulty tiers:
 * {@link DifficultyLevel#BEGINNER} (getting-started, free-tier guides),
 * {@link DifficultyLevel#INTERMEDIATE} (service-level docs, IaC modules),
 * {@link DifficultyLevel#ADVANCED} (multi-cloud architecture, advanced monitoring).
 *
 * @see DevOpsResources
 * @see EngineeringResources
 */
public final class CloudInfraResources implements ResourceProvider {

    @Override
    public List<LearningResource> resources() {
        final var now = Instant.now();
        return List.of(

                // ─── Cloud Platforms ────────────────────────────────────────

                new LearningResource(
                        "aws-docs",
                        "AWS Documentation",
                        "https://docs.aws.amazon.com/",
                        "Official Amazon Web Services documentation — the largest cloud platform. "
                                + "Covers EC2, S3, Lambda, RDS, DynamoDB, CloudFormation, IAM, VPC, "
                                + "ECS/EKS, SQS/SNS, and 200+ services with getting-started guides, "
                                + "API references, and architecture best practices.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.CLOUD, ResourceCategory.DEVOPS),
                        Set.of(ConceptArea.INFRASTRUCTURE, ConceptArea.SYSTEM_DESIGN,
                                ConceptArea.DISTRIBUTED_SYSTEMS, ConceptArea.NETWORKING),
                        Set.of("official", "aws", "amazon", "ec2", "s3", "lambda", "rds",
                                "dynamodb", "iam", "vpc", "cloud"),
                        "Amazon Web Services",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "gcp-docs",
                        "Google Cloud Documentation",
                        "https://cloud.google.com/docs",
                        "Official Google Cloud Platform documentation. Covers Compute Engine, "
                                + "Cloud Storage, BigQuery, Cloud Run, GKE, Cloud Functions, "
                                + "Pub/Sub, Spanner, Firebase, and AI/ML services (Vertex AI). "
                                + "Includes quickstarts, tutorials, and architecture guides.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.CLOUD, ResourceCategory.DEVOPS),
                        Set.of(ConceptArea.INFRASTRUCTURE, ConceptArea.SYSTEM_DESIGN,
                                ConceptArea.CONTAINERS, ConceptArea.MACHINE_LEARNING),
                        Set.of("official", "gcp", "google-cloud", "bigquery", "gke",
                                "cloud-run", "cloud-functions", "vertex-ai"),
                        "Google Cloud",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "azure-docs",
                        "Microsoft Azure Documentation",
                        "https://learn.microsoft.com/en-us/azure/",
                        "Official Microsoft Azure documentation. Covers Azure VMs, App Service, "
                                + "Azure Functions, Azure SQL, Cosmos DB, AKS, Azure DevOps, "
                                + "Azure Active Directory (Entra ID), and AI services. Includes "
                                + "learning paths, quickstarts, and architecture center.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.CLOUD, ResourceCategory.DEVOPS),
                        Set.of(ConceptArea.INFRASTRUCTURE, ConceptArea.SYSTEM_DESIGN,
                                ConceptArea.CI_CD, ConceptArea.DATABASES),
                        Set.of("official", "azure", "microsoft", "app-service", "azure-functions",
                                "cosmos-db", "aks", "azure-devops"),
                        "Microsoft",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                // ─── Infrastructure as Code ─────────────────────────────────

                new LearningResource(
                        "terraform-docs",
                        "Terraform Documentation",
                        "https://developer.hashicorp.com/terraform/docs",
                        "Official Terraform documentation — the leading Infrastructure as Code "
                                + "tool. HCL language, providers (AWS, GCP, Azure), modules, state "
                                + "management, workspaces, Terraform Cloud, import, and best practices "
                                + "for managing cloud infrastructure declaratively.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.DEVOPS, ResourceCategory.CLOUD),
                        Set.of(ConceptArea.INFRASTRUCTURE, ConceptArea.SYSTEM_DESIGN),
                        Set.of("official", "terraform", "iac", "infrastructure-as-code",
                                "hcl", "providers", "modules", "state", "hashicorp"),
                        "HashiCorp",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "ansible-docs",
                        "Ansible Documentation",
                        "https://docs.ansible.com/ansible/latest/",
                        "Official Ansible documentation — agentless IT automation. Playbooks, "
                                + "inventory, roles, modules, collections, Ansible Galaxy, "
                                + "and Ansible Vault for secrets. Used for configuration management, "
                                + "application deployment, and orchestration.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.DEVOPS, ResourceCategory.CLOUD),
                        Set.of(ConceptArea.INFRASTRUCTURE, ConceptArea.CI_CD),
                        Set.of("official", "ansible", "automation", "playbooks", "roles",
                                "configuration-management", "iac", "red-hat"),
                        "Red Hat / Ansible Community",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                // ─── Observability ──────────────────────────────────────────

                new LearningResource(
                        "prometheus-docs",
                        "Prometheus Documentation",
                        "https://prometheus.io/docs/introduction/overview/",
                        "Official Prometheus documentation — the cloud-native monitoring and "
                                + "alerting toolkit. PromQL query language, metric types (counter, "
                                + "gauge, histogram, summary), scraping, alerting rules, service "
                                + "discovery, and federation. CNCF graduated project.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.DEVOPS, ResourceCategory.CLOUD),
                        Set.of(ConceptArea.OBSERVABILITY, ConceptArea.INFRASTRUCTURE,
                                ConceptArea.DISTRIBUTED_SYSTEMS),
                        Set.of("official", "prometheus", "monitoring", "promql", "metrics",
                                "alerting", "cncf", "cloud-native"),
                        "CNCF / Prometheus Authors",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                ),

                new LearningResource(
                        "grafana-docs",
                        "Grafana Documentation",
                        "https://grafana.com/docs/grafana/latest/",
                        "Official Grafana documentation — the leading open-source dashboarding "
                                + "and visualization platform. Dashboards, panels, data sources "
                                + "(Prometheus, Loki, Elasticsearch), alerting, variables, and "
                                + "annotations. Pairs with Prometheus for full observability.",
                        ResourceType.DOCUMENTATION,
                        Set.of(ResourceCategory.DEVOPS, ResourceCategory.TOOLS),
                        Set.of(ConceptArea.OBSERVABILITY, ConceptArea.INFRASTRUCTURE),
                        Set.of("official", "grafana", "dashboards", "visualization",
                                "monitoring", "data-sources", "alerting"),
                        "Grafana Labs",
                        DifficultyLevel.INTERMEDIATE,
                        ContentFreshness.ACTIVELY_MAINTAINED,
                        true, true, LanguageApplicability.MULTI_LANGUAGE, now
                )
        );
    }
}
