# Architecture

Nodara is planned as a modular monolith: one Spring Boot backend organized by domain boundaries, with a separate React dashboard and a Python endpoint agent. This avoids distributed-system complexity while preserving clear seams for future growth.

## Intended components

- **Endpoint agent:** runs on managed Windows and later Linux hosts; sends identity, heartbeat, and approved telemetry.
- **Backend:** exposes versioned REST APIs, validates and stores data, and later manages authorization, alerts, incidents, automation, and audit records.
- **Dashboard:** presents inventory, system health, and operational workflows.
- **PostgreSQL:** durable store for platform data.

## Backend boundaries

The Java root package is `com.nodara.platform`. Planned modules are `auth`, `user`, `device`, `asset`, `telemetry`, `monitoring`, `alert`, `incident`, `automation`, `software`, `audit`, `notification`, `common`, and `config`. Only the health endpoint and configuration foundation currently contain implementation.

## Data flow (planned)

`Endpoint agent → REST API → backend domain modules → PostgreSQL → dashboard`

Authentication, telemetry processing, and automation have deliberately not been implemented in this phase.
