"""Device registration logic for the Nodara endpoint agent."""

import platform
import socket
from typing import Dict
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


def register_with_backend(backend_url: str = None) -> bool:
    """
    Register this endpoint with the Nodara backend.
    
    Args:
        backend_url: Optional backend URL. If not provided, uses environment variable
                    or defaults to http://localhost:8080
        
    Returns:
        True if registration was successful, False otherwise
    """
    try:
        system_info = get_system_info()
        client = BackendClient(backend_url)
        
        print(f"Registering device: {system_info['hostname']}")
        print(f"  OS: {system_info['osName']} {system_info['osVersion']}")
        print(f"  Agent version: {system_info['agentVersion']}")
        
        response = client.register_device(system_info)
        
        print(f"✓ Device registered successfully (ID: {response.get('id')})")
        print(f"  Last heartbeat: {response.get('lastHeartbeat')}")
        return True
        
    except RuntimeError as e:
        print(f"✗ Registration failed: {str(e)}")
        return False
    except Exception as e:
        print(f"✗ Unexpected error during registration: {str(e)}")
        return False
