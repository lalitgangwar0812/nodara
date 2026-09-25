-- V3__Add_device_uuid_unique_identity.sql
-- Ensure each physical machine keeps a stable endpoint identity across agent restarts.

ALTER TABLE devices ADD COLUMN IF NOT EXISTS device_uuid VARCHAR(36);

UPDATE devices
SET device_uuid = (
    substr(md5(random()::text || id::text || clock_timestamp()::text), 1, 8) || '-' ||
    substr(md5(random()::text || id::text || clock_timestamp()::text), 9, 4) || '-' ||
    substr(md5(random()::text || id::text || clock_timestamp()::text), 13, 4) || '-' ||
    substr(md5(random()::text || id::text || clock_timestamp()::text), 17, 4) || '-' ||
    substr(md5(random()::text || id::text || clock_timestamp()::text), 21, 12)
)
WHERE device_uuid IS NULL;

ALTER TABLE devices ALTER COLUMN device_uuid SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uk_devices_device_uuid ON devices(device_uuid);
