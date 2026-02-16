```prompt
---
name: devops
description: 'Learn DevOps tools & practices — CI/CD, Docker, Kubernetes, cloud, IaC, monitoring, and deployment pipelines'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Topic
${input:topic:What DevOps topic? (e.g., docker, kubernetes, ci-cd, github-actions, terraform, aws, monitoring, jenkins, docker-compose, helm, ansible)}

## Goal
${input:goal:What's your goal? (learn-concept / setup-pipeline / compare-tools / troubleshoot / interview-prep)}

## Current Level
${input:level:Your experience level? (beginner / know-the-basics / intermediate / advanced)}

## Instructions

Teach or guide the user on the selected DevOps topic. Adapt your response based on the goal.

### DevOps Domain Map
```
CI/CD (Continuous Integration / Continuous Delivery)
├── Concepts: pipeline, stages, artifacts, triggers, environments
├── Tools: GitHub Actions, GitLab CI, Jenkins, CircleCI, Travis CI
├── Practices: automated testing, build verification, deploy gates
└── Advanced: blue-green deploy, canary releases, feature flags

Containers & Orchestration
├── Docker: images, containers, Dockerfile, layers, volumes, networking
├── Docker Compose: multi-container apps, service definitions
├── Kubernetes: pods, deployments, services, ingress, ConfigMaps, Secrets
├── Helm: charts, templating, release management
└── Registry: Docker Hub, ECR, GCR, ACR

Infrastructure as Code (IaC)
├── Terraform: providers, resources, state, modules, plan/apply
├── Ansible: playbooks, roles, inventory, idempotency
├── CloudFormation: stacks, templates, drift detection
└── Pulumi: programming language-based IaC

Cloud Platforms
├── AWS: EC2, S3, RDS, Lambda, ECS/EKS, IAM, VPC
├── GCP: Compute Engine, Cloud Run, GKE, BigQuery
├── Azure: App Service, AKS, Azure Functions, Cosmos DB
└── Fundamentals: regions, availability zones, auto-scaling, CDN

Monitoring & Observability
├── Metrics: Prometheus, Grafana, CloudWatch, Datadog
├── Logging: ELK Stack (Elasticsearch, Logstash, Kibana), Fluentd
├── Tracing: Jaeger, Zipkin, OpenTelemetry
├── Alerting: PagerDuty, Opsgenie, Alertmanager
└── SRE Practices: SLOs, SLIs, SLAs, error budgets

Version Control & Collaboration
├── Git: branching strategies (GitFlow, trunk-based), rebasing, cherry-pick
├── GitHub: PRs, Actions, branch protection, CODEOWNERS
├── Code Review: best practices, automated checks, review checklists
└── Monorepo vs Polyrepo: trade-offs, tools (Nx, Turborepo, Bazel)
```

### Response Structure by Goal

#### If goal = learn-concept:
1. **What is it?** — Plain-language definition
2. **Why does it exist?** — Problem it solves, what was painful before
3. **How it works** — Core mechanics with diagram
4. **Hands-on example** — Minimal working config/script (e.g., a Dockerfile, a GitHub Actions workflow, a Terraform resource)
5. **Common mistakes** — What beginners get wrong
6. **Where it fits** — How it connects to the broader DevOps pipeline
7. **What to learn next** — One logical next topic

#### If goal = setup-pipeline:
1. **Requirements check** — What you need (language, framework, deploy target)
2. **Step-by-step pipeline** — Complete working config with comments
3. **Stages explained** — Build → Test → Lint → Deploy (each explained)
4. **Best practices** — Caching, secrets management, parallel jobs
5. **Troubleshooting** — Common errors and fixes

#### If goal = compare-tools:
1. **Side-by-side table** — Features, pricing, ecosystem, learning curve
2. **When to use each** — Concrete scenarios
3. **Migration considerations** — Switching costs
4. **Recommendation** — Based on the user's context

#### If goal = interview-prep:
1. **Common questions** — Top 10-15 DevOps interview questions for the topic
2. **Conceptual answers** — Clear, structured answers
3. **Scenario questions** — "How would you design..."
4. **Hands-on challenges** — Practical tasks interviewers might ask

### Rules
- Always provide working, copy-paste-ready config files (Dockerfile, YAML, HCL, etc.)
- Explain each line in config files — don't assume the user knows the syntax
- Use `fetch` to retrieve official documentation when beneficial
- For cloud topics, be vendor-aware but focus on concepts that transfer across providers
- Security best practices: never hardcode secrets, use least privilege, scan images
- End with one clear "what to learn next" recommendation
```
