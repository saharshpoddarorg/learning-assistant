```prompt
---
name: system-design
description: 'System design learning — HLD (scaling, data, communication) and LLD (OOP modeling, API design, patterns) with internal hierarchy'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## Topic
${input:topic:What to design or learn? (e.g., url-shortener, rate-limiter, chat-system, LRU-cache, parking-lot, or a concept like caching, load-balancing, sharding, SOLID)}

## Level
${input:level:HLD or LLD? (hld / lld / both / concept / interview-practice)}

## Depth
${input:depth:How deep? (overview / detailed / exhaustive)}

## Instructions

### System Design Hierarchy
```
System Design
│
├── HLD — High-Level Design
│   │
│   ├── 1. Requirements & Estimation
│   │   ├── Functional requirements (what the system does)
│   │   ├── Non-functional requirements (scale, latency, availability)
│   │   ├── Back-of-envelope estimation (QPS, storage, bandwidth)
│   │   └── Constraints & assumptions
│   │
│   ├── 2. Architecture Components
│   │   ├── Scaling
│   │   │   ├── Horizontal vs vertical scaling
│   │   │   ├── Load balancing (L4/L7, algorithms, health checks)
│   │   │   ├── Caching (client, CDN, server, database-level)
│   │   │   ├── CDN (static content delivery)
│   │   │   ├── Database sharding (horizontal partitioning)
│   │   │   ├── Read replicas & write scaling
│   │   │   └── Auto-scaling & elasticity
│   │   │
│   │   ├── Data Layer
│   │   │   ├── SQL vs NoSQL selection criteria
│   │   │   ├── Replication (leader-follower, multi-leader, leaderless)
│   │   │   ├── Partitioning (range, hash, consistent hashing)
│   │   │   ├── Consistency models (strong, eventual, causal)
│   │   │   ├── CAP / PACELC theorem
│   │   │   ├── Distributed transactions (2PC, Saga)
│   │   │   └── Data pipelines & ETL
│   │   │
│   │   ├── Communication
│   │   │   ├── Synchronous (REST, gRPC, GraphQL)
│   │   │   ├── Asynchronous (message queues, event streaming)
│   │   │   ├── Event-driven architecture
│   │   │   ├── Pub/Sub vs point-to-point
│   │   │   ├── API Gateway & service mesh
│   │   │   └── WebSocket & SSE (real-time)
│   │   │
│   │   └── Reliability & Operations
│   │       ├── Rate limiting (token bucket, sliding window)
│   │       ├── Circuit breakers & bulkheads
│   │       ├── Consensus algorithms (Raft, Paxos, ZAB)
│   │       ├── Leader election
│   │       ├── Heartbeats & failure detection
│   │       ├── Idempotency & retry strategies
│   │       └── Disaster recovery (backup, failover, RTO/RPO)
│   │
│   └── 3. Classic HLD Case Studies
│       ├── URL Shortener (TinyURL)
│       ├── Rate Limiter
│       ├── Chat System (WhatsApp/Slack)
│       ├── News Feed (Facebook/Twitter)
│       ├── Video Streaming (YouTube/Netflix)
│       ├── Web Crawler
│       ├── Notification System
│       ├── Search Autocomplete
│       ├── Distributed Cache
│       ├── Distributed File Storage (S3/GFS)
│       ├── Payment System
│       └── Ride-Sharing (Uber/Lyft)
│
└── LLD — Low-Level Design
    │
    ├── 1. OOP Modeling
    │   ├── Identifying entities & relationships
    │   ├── Class diagrams (UML)
    │   ├── Inheritance vs composition
    │   ├── Interface segregation
    │   ├── SOLID principles in practice
    │   └── Design pattern selection
    │
    ├── 2. API & Contract Design
    │   ├── REST endpoint design (resources, verbs, status codes)
    │   ├── gRPC service definitions (protobuf)
    │   ├── Request/response schemas
    │   ├── Pagination, filtering, sorting
    │   ├── Versioning strategies
    │   └── Error response standards
    │
    ├── 3. Internal Architecture
    │   ├── State machines & transitions
    │   ├── Concurrency control (locks, optimistic/pessimistic)
    │   ├── Event sourcing & CQRS
    │   ├── Repository / DAO pattern
    │   ├── Service layer design
    │   └── Dependency injection
    │
    ├── 4. Schema & Data Modeling
    │   ├── Database schema (normalization, denormalization)
    │   ├── Indexing strategy
    │   ├── Query optimization
    │   └── Migration strategy
    │
    └── 5. Classic LLD Case Studies
        ├── Parking Lot System
        ├── LRU Cache
        ├── Elevator System
        ├── Library Management
        ├── Tic-Tac-Toe / Chess
        ├── Vending Machine
        ├── File System (in-memory)
        ├── Hotel Booking System
        ├── Snake & Ladder Game
        └── Online Shopping Cart
```

### Response Structure

#### If level = hld:
1. **Requirements** — Functional + non-functional, clarifying questions
2. **Estimation** — QPS, storage, bandwidth calculations
3. **High-level architecture** — ASCII diagram showing all components
4. **Component deep-dive** — 2-3 most critical components explained
5. **Data model** — Key entities and storage decisions
6. **API design** — Core endpoints
7. **Scaling strategy** — How to handle 10x/100x growth
8. **Trade-offs** — Key decisions and alternatives
9. **Bottlenecks & mitigations** — What could fail and how to handle it

#### If level = lld:
1. **Requirements** — Use cases and actors
2. **Core entities** — Class diagram with attributes and methods
3. **Design patterns** — Which patterns apply and why
4. **SOLID analysis** — How the design follows each principle
5. **Code skeleton** — Key classes/interfaces in the chosen language
6. **State transitions** — For stateful objects
7. **Extensibility** — How easy to add features
8. **Testing strategy** — What and how to test

#### If level = concept:
Teach the specific system design concept using the standard learning format (what, why, how, example, common mistakes, what next).

#### If level = interview-practice:
1. Present the problem as an interviewer would
2. Walk through the framework step by step
3. Show a model answer with diagrams
4. Highlight what interviewers look for at each step
5. Common follow-up questions

### Rules
- Always draw ASCII architecture diagrams for HLD
- Always show class diagrams (text-based) for LLD
- Cite DDIA or System Design Interview books where relevant
- For case studies, follow the exact framework: requirements → estimation → design → trade-offs
- Be explicit about trade-offs — every design decision has a cost
- If the user asks about a concept (e.g., "caching"), teach it first, then show how it fits in system design
```
