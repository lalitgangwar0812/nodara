"""Device registration logic for the Nodara endpoint agent."""

import os
import platform
import socket
import time
from typing import Dict, Optional
from nodara_agent.client import BackendClient


AGENT_VERSION = "0.1.0"


def get_system_info() -> Dict[str, str]:
    """
    Collect basic system information for device registration.
    
    Returns:
        Dictionary with hostname, osName, osVersion, agentVersion
    """
    hostname = socket.gethostname()
    system = platform.system()
    release = platform.release()
    
    return {
        "hostname": hostname,
        "osName": system,
        "osVersion": release,
        "agentVersion": AGENT_VERSION,
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
