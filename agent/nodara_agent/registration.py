"""Device registration logic for the Nodara endpoint agent."""

import os
import platform
import socket
import time
import uuid
from pathlib import Path
from typing import Dict, Optional
from nodara_agent.client import BackendClient
from nodara_agent.telemetry import collect_system_telemetry, get_telemetry_interval_seconds


AGENT_VERSION = "0.1.0"


def get_agent_state_path() -> Path:
    """Return the path used to persist the stable machine identity."""
    home_dir = Path.home()
    state_dir = home_dir / ".nodara"
    state_dir.mkdir(exist_ok=True, parents=True)
    return state_dir / "agent_id.txt"


def get_or_create_device_uuid() -> str:
    """Return a stable UUID for this machine, persisted locally."""
    state_path = get_agent_state_path()

    if state_path.exists():
        stored_value = state_path.read_text(encoding="utf-8").strip()
        if stored_value:
            return stored_value

    new_uuid = str(uuid.uuid4())
    state_path.write_text(new_uuid, encoding="utf-8")
    return new_uuid


def get_system_info() -> Dict[str, str]:
    """
    Collect basic system information for device registration.
    
    Returns:
        Dictionary with hostname, osName, osVersion, agentVersion, deviceUuid
    """
    hostname = socket.gethostname()
    system = platform.system()
    release = platform.release()

    return {
        "hostname": hostname,
        "osName": system,
        "osVersion": release,
        "agentVersion": AGENT_VERSION,
        "deviceUuid": get_or_create_device_uuid(),
    }


def get_heartbeat_interval_seconds() -> int:
    """Read heartbeat interval from environment or default to 30 seconds."""
    raw_value = os.getenv("NODARA_HEARTBEAT_INTERVAL_SECONDS", "30")
    try:
        interval = int(raw_value)
    except (TypeError, ValueError):
        return 30
    return max(1, interval)


def register_with_backend(backend_url: str = None) -> Optional[Dict[str, object]]:
    """
    Register this endpoint with the Nodara backend and return the response.
    
    Args:
        backend_url: Optional backend URL. If not provided, uses environment variable
                    or defaults to http://localhost:8080
        
    Returns:
        Backend device payload on success, otherwise None.
    """
    try:
        system_info = get_system_info()
        client = BackendClient(backend_url)
        
        print(f"Registering device: {system_info['hostname']}")
        print(f"  OS: {system_info['osName']} {system_info['osVersion']}")
        print(f"  Agent version: {system_info['agentVersion']}")
        
        response = client.register_device(system_info)
        print(f"✓ Registration successful (device ID: {response.get('id')})")
        print(f"  Last heartbeat: {response.get('lastHeartbeat')}")
        return response
        
    except RuntimeError as e:
        print(f"✗ Registration failed: {str(e)}")
        return None
    except Exception as e:
        print(f"✗ Unexpected error during registration: {str(e)}")
        return None


def run_heartbeat_loop(backend_url: str = None, device_id: Optional[int] = None) -> None:
    """Send periodic heartbeats until interrupted by the user."""
    if device_id is None:
        raise ValueError("A registered device ID is required before starting heartbeats.")

    interval_seconds = get_heartbeat_interval_seconds()
    client = BackendClient(backend_url)

    print(f"Starting heartbeat loop every {interval_seconds} seconds.")
    while True:
        try:
            response = client.heartbeat_device(device_id)
            print(f"✓ Heartbeat successful for device ID {device_id} at {response.get('lastHeartbeat')}")
        except RuntimeError as e:
            print(f"✗ Heartbeat failed: {str(e)}")
        time.sleep(interval_seconds)


def run_telemetry_loop(backend_url: str = None, device_id: Optional[int] = None) -> None:
    """Send periodic telemetry payloads until interrupted by the user."""
    if device_id is None:
        raise ValueError("A registered device ID is required before starting telemetry collection.")

    interval_seconds = get_telemetry_interval_seconds()
    client = BackendClient(backend_url)

    print(f"Starting telemetry loop every {interval_seconds} seconds.")
    while True:
        try:
            telemetry = collect_system_telemetry()
            response = client.send_telemetry(device_id, telemetry)
            print(
                "✓ Telemetry successful for device ID "
                f"{device_id} at {response.get('recordedAt')} "
                f"(CPU {response.get('cpuUsage')}%, RAM {response.get('ramUsage')}%, "
                f"Disk {response.get('diskUsage')}%)"
            )
        except RuntimeError as e:
            print(f"✗ Telemetry failed: {str(e)}")
        time.sleep(interval_seconds)
