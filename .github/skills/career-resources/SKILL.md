---
name: career-resources
description: >
  Curated career resources for tech job roles, skills matrices, compensation data, career roadmaps,
  interview processes, and role transitions. Covers SDE, MLE, DevOps, Architect, Tech Lead, EM,
  Data Engineer, Security Engineer, and all major tech career paths.
  Use when asked about tech careers, job roles, skills needed, salary ranges, career growth,
  role comparisons, interview processes, or career transition planning.
---

# Tech Career Resources

## Role Hierarchy — IC vs Management Tracks

```
               IC Track                              Management Track
         ─────────────────                        ─────────────────────
Level 1:  Intern / New Grad                       ─
Level 2:  Junior Engineer (SDE I)                 ─
Level 3:  Mid-Level Engineer (SDE II)             ─
Level 4:  Senior Engineer (SDE III)               Engineering Manager
Level 5:  Staff Engineer (SDE IV)                 Senior EM / Director
Level 6:  Principal Engineer (SDE V)              VP of Engineering
Level 7:  Distinguished Engineer / Fellow         CTO / SVP
```

### Level Expectations (General Industry Standard)

| Level | Scope | Independence | Design | Leadership |
|---|---|---|---|---|
| **Junior** | Tasks within a feature | Needs guidance | None expected | None |
| **Mid** | Full features | Semi-autonomous | Component-level | Mentors juniors |
| **Senior** | Multi-feature / system | Autonomous | System-level | Technical direction |
| **Staff** | Org-wide systems | Sets direction | Cross-system | Org-wide influence |
| **Principal** | Company-wide | Defines strategy | Architecture-level | Industry influence |

---

## Skills Matrices by Role

### Software Development Engineer (SDE / SWE)
| Level | Languages | System Design | Tools | Soft Skills |
|---|---|---|---|---|
| Junior | 1-2 languages, basic DSA | — | Git, IDE, basic CLI | Communication |
| Mid | 2+ languages, solid DSA | Component design | CI/CD, Docker, SQL | Collaboration |
| Senior | Language expertise, advanced DSA | System design (HLD/LLD) | K8s, IaC, monitoring | Mentoring, tech direction |
| Staff+ | Polyglot, architecture | Org-wide design | Full platform | Strategy, influence |

### Machine Learning Engineer (MLE)
| Skill Category | Required Skills |
|---|---|
| **Math/Stats** | Linear algebra, calculus, probability, statistics, optimization |
| **ML Frameworks** | PyTorch, TensorFlow, scikit-learn, Hugging Face, JAX |
| **Data** | Pandas, Spark, SQL, feature engineering, data pipelines |
| **MLOps** | MLflow, Kubeflow, model serving (TorchServe, TFServing), experiment tracking |
| **Languages** | Python (primary), C++ (for performance), SQL |
| **System Design** | ML system design (training infra, serving, A/B testing, monitoring) |

### DevOps / SRE
| Skill Category | Required Skills |
|---|---|
| **CI/CD** | GitHub Actions, Jenkins, GitLab CI, ArgoCD |
| **Containers** | Docker, Kubernetes, Helm, container security |
| **Cloud** | AWS / GCP / Azure (at least one deeply) |
| **IaC** | Terraform, Ansible, Pulumi, CloudFormation |
| **Monitoring** | Prometheus, Grafana, ELK, Datadog, PagerDuty |
| **Scripting** | Bash, Python, Go |
| **Networking** | DNS, load balancers, firewalls, VPCs, CDNs |
| **Reliability** | SLOs/SLIs/SLAs, incident response, chaos engineering |

### Software Architect
| Skill Category | Required Skills |
|---|---|
| **Architecture Patterns** | Microservices, event-driven, CQRS, hexagonal, layered |
| **System Design** | HLD mastery, trade-off analysis, scalability |
| **Cross-Cutting** | Security, performance, observability, compliance |
| **Communication** | ADRs, architecture diagrams, stakeholder presentations |
| **Breadth** | Multiple languages, databases, cloud platforms |
| **Experience** | 8-15+ years, led large-scale system designs |

---

## Compensation Data Sources

| Source | URL | Best For | Data Type |
|---|---|---|---|
| **Levels.fyi** | `https://www.levels.fyi/` | FAANG & large tech compensation by level | TC breakdown (base + bonus + stock) |
| **Glassdoor** | `https://www.glassdoor.com/Salaries/` | Broad market data, reviews | Salary ranges, company reviews |
| **PayScale** | `https://www.payscale.com/` | Market benchmarks, cost of living | Salary benchmarks |
| **Blind** | `https://www.teamblind.com/` | Anonymous tech worker discussions | Real TC data points |
| **Stack Overflow Survey** | `https://survey.stackoverflow.co/` | Global developer survey data | Tech + salary trends |
| **Comparably** | `https://www.comparably.com/` | Company culture + compensation | Culture scores + TC |
| **LinkedIn Salary** | `https://www.linkedin.com/salary/` | Role-based salary insights | Market rates |
| **H1B Salary Database** | `https://h1bdata.info/` | US visa-based salary data | Verified employer filings |

### Approximate Pay Ranges (USD, Total Compensation, 2025–2026)

| Role | Entry (0-2 yr) | Mid (3-5 yr) | Senior (6-10 yr) | Staff+ (10+ yr) |
|---|---|---|---|---|
| **SDE (FAANG)** | $150K–$220K | $220K–$350K | $350K–$550K | $500K–$900K+ |
| **SDE (Mid-Tier)** | $80K–$130K | $120K–$200K | $180K–$300K | $280K–$450K |
| **SDE (Startup)** | $70K–$120K + equity | $100K–$170K + equity | $150K–$250K + equity | $200K–$350K + equity |
| **MLE** | $130K–$200K | $200K–$350K | $350K–$550K | $500K–$800K+ |
| **DevOps/SRE** | $90K–$150K | $140K–$230K | $220K–$380K | $350K–$550K |
| **Data Engineer** | $90K–$140K | $130K–$220K | $200K–$350K | $320K–$500K |
| **Architect** | — | — | $200K–$350K | $350K–$600K |
| **EM** | — | — | $250K–$400K | $400K–$700K+ |

> ⚠️ Ranges vary significantly by geography, company tier, and negotiation. Use `fetch` on levels.fyi for current data.

---

## Career Roadmap Resources

| Resource | URL | Best For |
|---|---|---|
| **roadmap.sh** | `https://roadmap.sh/` | Visual learning roadmaps for every role |
| **Tech Interview Handbook** | `https://www.techinterviewhandbook.org/` | Interview prep, resume, negotiation |
| **System Design Primer** | `https://github.com/donnemartin/system-design-primer` | System design interview prep |
| **Coding Interview University** | `https://github.com/jwasham/coding-interview-university` | Self-study plan for SDE interviews |
| **Staff Engineer Book** | `https://staffeng.com/book` | Will Larson — staff+ IC career guide |
| **An Elegant Puzzle** | Book by Will Larson | Engineering management guide |
| **The Manager's Path** | Book by Camille Fournier | IC → Manager career transition |
| **Cracking the Coding Interview** | Book by Gayle McDowell | DSA-focused interview prep |
| **Designing Data-Intensive Applications** | Book by Martin Kleppmann | System design concepts |

---

## Interview Process by Role

| Role | Typical Rounds | Focus Areas |
|---|---|---|
| **SDE** | DSA (2-3), System Design (1-2), Behavioral (1) | LeetCode Medium/Hard, HLD, LLD |
| **MLE** | ML Theory (1), ML System Design (1), Coding (1-2), Behavioral (1) | ML concepts, feature engineering, model serving |
| **DevOps/SRE** | System Design (1-2), Coding (1), Troubleshooting (1), Behavioral (1) | Infrastructure design, incident response, automation |
| **Frontend** | UI Coding (2), System Design (1), Behavioral (1) | DOM, React/framework, web performance |
| **Architect** | Architecture Deep-Dive (2-3), Behavioral/Leadership (1-2) | Trade-off analysis, cross-system design |
| **EM** | System Design (1), Behavioral/Leadership (3-4) | Team management, conflict resolution, delivery |

---

## How to Use This Skill

1. **Identify the role** — what career path is the user exploring?
2. **Match to skills matrix** — what skills are needed at their target level?
3. **Recommend learning path** — use `/language-guide`, `/dsa`, `/system-design`, `/devops` for skill building
4. **Provide market data** — use `fetch` for current compensation data
5. **Suggest interview prep** — use `/interview-prep` for targeted practice
6. **Never guarantee compensation** — always note salary data is approximate and variable
