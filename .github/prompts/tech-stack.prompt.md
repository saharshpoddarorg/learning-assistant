```prompt
---
name: tech-stack
description: 'Explore frameworks, libraries, and tech stacks — compare options, learn fundamentals, choose the right tool for your needs'
agent: learning-mentor
tools: ['codebase', 'fetch']
---

## What do you want to explore?
${input:topic:Framework or tech? (e.g., spring-boot, react, django, kafka, redis, postgresql, elasticsearch, or compare: spring-vs-django, react-vs-vue)}

## Category
${input:category:Which area? (backend / frontend / database / messaging / full-stack / compare / recommend)}

## Current Level
${input:level:Your experience? (beginner / intermediate / advanced / evaluating-options)}

## Instructions

### Tech Stack Domain Map
```
Backend Frameworks
├── Java: Spring Boot, Quarkus, Micronaut, Jakarta EE
├── Python: Django, Flask, FastAPI, Tornado
├── JavaScript/TS: Express, NestJS, Fastify, Hono
├── Go: net/http, Gin, Echo, Fiber
├── Rust: Actix, Axum, Rocket
├── C#: ASP.NET Core, Minimal APIs
└── Ruby: Rails, Sinatra

Frontend Frameworks
├── React (+ Next.js, Remix)
├── Vue (+ Nuxt)
├── Angular
├── Svelte (+ SvelteKit)
├── HTMX + server-rendered
└── Mobile: React Native, Flutter, SwiftUI, Jetpack Compose

Databases
├── Relational: PostgreSQL, MySQL, SQLite, Oracle, SQL Server
├── Document: MongoDB, CouchDB, Firestore
├── Key-Value: Redis, Memcached, DynamoDB
├── Column: Cassandra, ScyllaDB, HBase
├── Graph: Neo4j, ArangoDB
├── Search: Elasticsearch, Meilisearch, Typesense
└── Time-Series: InfluxDB, TimescaleDB

Messaging & Streaming
├── Kafka (event streaming)
├── RabbitMQ (message broker, AMQP)
├── Redis Pub/Sub & Streams
├── NATS
├── AWS SQS/SNS
└── Google Pub/Sub

Build & Package Managers
├── Java: Maven, Gradle
├── Python: pip, Poetry, uv
├── JS/TS: npm, yarn, pnpm, Bun
├── Go: go modules
├── Rust: cargo
└── C/C++: CMake, Make, Conan, vcpkg

API Styles
├── REST (HTTP + JSON)
├── gRPC (HTTP/2 + Protobuf)
├── GraphQL (query language)
├── tRPC (TypeScript end-to-end)
└── WebSocket (real-time bidirectional)
```

### Response Structure

#### If category = learn about a specific tech:
1. **What is it?** — Purpose in 2-3 sentences
2. **Why choose it?** — Key strengths and use cases
3. **Core concepts** — 5-7 essential concepts to understand
4. **Quick start** — Minimal working example (code or config)
5. **Ecosystem** — Key libraries, plugins, community resources
6. **When NOT to use** — Weaknesses, poor fit scenarios
7. **Learning path** — Ordered: what to learn first → last
8. **Official docs** — Direct link to the best starting page

#### If category = compare:
Show a detailed comparison table:

| Aspect | Tool A | Tool B |
|---|---|---|
| **Best for** | ... | ... |
| **Learning curve** | ... | ... |
| **Performance** | ... | ... |
| **Ecosystem** | ... | ... |
| **Community** | ... | ... |
| **Scalability** | ... | ... |
| **When to choose** | ... | ... |

Then: concrete recommendation based on the user's context.

#### If category = recommend:
1. **Clarify requirements** — what are you building? scale? team size? constraints?
2. **Recommend a stack** — with reasoning for each choice
3. **Alternative stacks** — 1-2 alternatives with trade-off explanations
4. **Getting started** — first steps for the recommended stack

### Rules
- Always link to official documentation
- For comparisons, be honest about trade-offs — no tool is universally "best"
- Provide working code snippets, not just descriptions
- When recommending, explain WHY, not just WHAT
- Use `fetch` to retrieve the latest docs or getting-started pages when helpful
```
