
# DevMatch.AI — AI-Driven Job Matching Platform

## 📅 Start Date: 2025-07-24
## 👨‍💻 Author: Nicolay Marunevici

---

## 🎯 Project Goal:
Build an intelligent platform for automatically matching IT professionals with job opportunities using AI, a microservice architecture, and modern frontend/backend/DevOps technologies.

---

## ⚙️ Tech Stack

### Backend:
- Java 17+, Spring Boot 3
- Spring Security (JWT + OAuth2)
- Spring AI (LLM: OpenAI, Together.ai)
- Spring Data JPA + PostgreSQL
- Redis
- Apache Kafka
- Elasticsearch
- Docker, Kubernetes
- MapStruct, Lombok, Validation

### Frontend:
- React.js + TypeScript + Vite
- TailwindCSS or Material UI
- Zustand / Redux Toolkit
- React Query / SWR
- WebSocket
- CodeMirror / Monaco Editor

### DevOps:
- GitHub Actions / CircleCI
- Docker + Docker Compose
- Kubernetes + Helm + ArgoCD
- Prometheus + Grafana
- ELK Stack or Loki
- Secrets: K8s Secrets or HashiCorp Vault

---

## 🧱 Modules & Microservices

| Microservice        | Description |
|---------------------|-------------|
| API Gateway         | Entry point, JWT validation, routing |
| Auth Service        | Registration, login, JWT, OAuth2 |
| User Service        | Stores user and company profiles |
| Job Service         | Job CRUD, AI description generation |
| Resume Parser (AI)  | Resume parsing and LLM-based analysis |
| Matching Service    | AI/Rule-based job-candidate matching |
| Interview Bot       | LLM chatbot for mock interviews |
| Code Submission     | Online IDE, code evaluation, AI feedback |
| Search Service      | Elasticsearch-powered search |
| Notification Service| Email and WebSocket notifications |
| Frontend            | React UI for candidates and employers |

---

## 🔁 Service Interactions

- All external calls go through API Gateway.
- Internal services (AI, Matching, Interview) communicate via Kafka and internal REST.
- Redis is used for caching matches and real-time notifications.
- Elasticsearch indexes users and job posts.
- Events like `user.created`, `job.updated`, `submission.done` are sent via Kafka.

---

## 📄 Extras

- Extensions: ML model for candidate ranking, PWA, WebRTC video interviews.
- Monetization strategy: Freemium / Subscription.
- Mobile support via responsive UI.

---

## 🛠 Development Modes

- `local-dev`: Docker Compose with mock services
- `staging`: K8s with ArgoCD
- `prod`: Helm + Secrets + ACR

---

