# Nodara

Nodara is an IT Infrastructure Visibility & Automation Platform under active development. It is intended to give IT teams a reliable view of managed endpoints and, later, an auditable way to respond to operational issues.

## Problem it will address

Infrastructure information is frequently fragmented across machines, tools, and manual processes. Nodara will bring endpoint inventory, health signals, alerts, incidents, and tightly controlled automation into one platform. These capabilities are planned; this repository currently provides only the project foundation.

## Intended architecture

Nodara will use a modular Spring Boot monolith, a React dashboard, a Python endpoint agent, and PostgreSQL. See [architecture documentation](docs/architecture.md) for component boundaries and data flow.

## Technology stack

- Backend: Java 17, Spring Boot, Spring Security, Spring Data JPA/Hibernate, PostgreSQL, Maven, OpenAPI, JUnit, Mockito
- Frontend: React, Vite, Axios
- Agent: Python
- Infrastructure: Docker and Docker Compose

## Repository layout

```text
backend/            Spring Boot API and domain-module foundation
frontend/           React/Vite application shell
agent/              Python endpoint-agent foundation
infrastructure/     Docker resources and operational scripts
docs/               Architecture, development, and roadmap documentation
docker-compose.yml  PostgreSQL service for local development
```

## Development prerequisites

Java 17, Maven, Node.js/npm, Python 3.12+, and Docker Desktop (for PostgreSQL) are required. Details are in the [development guide](docs/development.md).

## Start locally

Start the backend: `cd backend; mvn spring-boot:run`. Check `http://localhost:8080/api/v1/health`; it returns `{"status":"UP","service":"nodara-backend"}`.

Start the frontend: `cd frontend; npm install; npm run dev`.

Start PostgreSQL: copy `.env.example` to `.env`, choose a non-example password, then run `docker compose up -d postgres`.

Start the agent from `agent`: `python -m nodara_agent`.

## Current status

Phase 1 foundation is implemented: a bootable backend health endpoint, frontend placeholder, agent entry point, PostgreSQL Compose definition, and project documentation. Device monitoring, authentication workflows, telemetry, alerts, incidents, automation, CI/CD, cloud deployment, and observability are not implemented.

## Planned phases

The ordered plan is in [docs/roadmap.md](docs/roadmap.md), beginning with the endpoint agent and device inventory.
