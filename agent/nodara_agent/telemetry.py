"""System telemetry collection for the Nodara endpoint agent."""

import time
from typing import Dict


def get_telemetry_interval_seconds() -> int:
    """Read telemetry interval from environment or default to 60 seconds."""
    import os

    raw_value = os.getenv("NODARA_TELEMETRY_INTERVAL_SECONDS", "60")
    try:
        interval = int(raw_value)
    except (TypeError, ValueError):
        return 60
    return max(1, interval)


def collect_system_telemetry() -> Dict[str, float | int]:
    """Collect CPU, RAM, disk usage, and system uptime for this endpoint."""
    try:
        import psutil
    except ImportError as exc:
        raise RuntimeError(
            "psutil is required for telemetry collection. Install it with 'pip install -r requirements.txt'."
        ) from exc

    cpu_usage = psutil.cpu_percent(interval=1)
    memory = psutil.virtual_memory()
    disk = psutil.disk_usage("/")
    boot_time = psutil.boot_time()
    uptime_seconds = max(0, int(time.time() - boot_time))

    cpu_percent = round(min(max(cpu_usage, 0.0), 100.0), 2)
    ram_percent = round(min(max((memory.used / memory.total) * 100, 0.0), 100.0), 2)
    disk_percent = round(min(max((disk.used / disk.total) * 100, 0.0), 100.0), 2)

    return {
        "cpuUsage": cpu_percent,
        "ramUsage": ram_percent,
        "diskUsage": disk_percent,
        "uptimeSeconds": int(uptime_seconds),
    }
