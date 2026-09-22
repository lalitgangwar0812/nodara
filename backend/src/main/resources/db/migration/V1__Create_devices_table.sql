-- V1__Create_devices_table.sql
-- Initial migration: create the devices table for endpoint registration.

CREATE TABLE IF NOT EXISTS devices (
    id BIGSERIAL PRIMARY KEY,
    hostname VARCHAR(255) NOT NULL UNIQUE,
    os_name VARCHAR(255) NOT NULL,
    os_version VARCHAR(255) NOT NULL,
    agent_version VARCHAR(50) NOT NULL,
    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_heartbeat TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_devices_hostname ON devices(hostname);
CREATE INDEX idx_devices_last_heartbeat ON devices(last_heartbeat);
