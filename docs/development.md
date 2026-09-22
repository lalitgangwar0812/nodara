# Development

## Prerequisites

- Java 17 and Maven 3.9+
- Node.js 20+ and npm
- Python 3.12+
- Docker Desktop with Docker Compose (for PostgreSQL)

## Backend

Run `mvn spring-boot:run` from `backend`. The default `local` profile intentionally excludes database auto-configuration so the health endpoint can run before PostgreSQL. To connect to PostgreSQL, start Compose and set `SPRING_PROFILES_ACTIVE=postgres`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD`.

## Frontend

Run `npm install` and then `npm run dev` from `frontend`.

## Database

Copy `.env.example` to `.env`, replace the example password, then run `docker compose up -d postgres`.

## Agent

Run `python -m nodara_agent` from `agent`.
