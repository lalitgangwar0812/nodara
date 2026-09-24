-- V2__Create_device_telemetry_table.sql
-- Create telemetry records tied to registered devices.

CREATE TABLE IF NOT EXISTS device_telemetry (
    id BIGSERIAL PRIMARY KEY,
    device_id BIGINT NOT NULL REFERENCES devices(id) ON DELETE CASCADE,
    cpu_usage DECIMAL(5,2) NOT NULL CHECK (cpu_usage >= 0 AND cpu_usage <= 100),
    ram_usage DECIMAL(5,2) NOT NULL CHECK (ram_usage >= 0 AND ram_usage <= 100),
    disk_usage DECIMAL(5,2) NOT NULL CHECK (disk_usage >= 0 AND disk_usage <= 100),
    uptime_seconds BIGINT NOT NULL CHECK (uptime_seconds >= 0),
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_device_telemetry_device_id ON device_telemetry(device_id);
CREATE INDEX idx_device_telemetry_recorded_at ON device_telemetry(recorded_at);
