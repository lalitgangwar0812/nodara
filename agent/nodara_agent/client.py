"""HTTP client for communicating with the Nodara backend."""

import os
import requests
from typing import Dict, Any, Optional


class BackendClient:
    """Handles HTTP communication with the Nodara backend."""

    def __init__(self, base_url: Optional[str] = None):
        """
        Initialize the backend client.
        
        Args:
            base_url: The base URL of the Nodara backend. If not provided,
                     uses NODARA_BACKEND_URL environment variable, defaulting to
                     http://localhost:8080
        """
        self.base_url = (
            base_url
            or os.getenv("NODARA_BACKEND_URL", "http://localhost:8080")
        ).rstrip("/")
        self.timeout = 10

    def register_device(self, device_info: Dict[str, str]) -> Dict[str, Any]:
        """
        Register the endpoint device with the backend.
        
        Args:
            device_info: Dictionary containing hostname, osName, osVersion, agentVersion
            
        Returns:
            The device response from the backend
            
        Raises:
            requests.exceptions.RequestException: If the request fails
        """
        url = f"{self.base_url}/api/v1/devices/register"
        
        try:
            response = requests.post(
                url,
                json=device_info,
                timeout=self.timeout,
                headers={"Content-Type": "application/json"},
            )
            response.raise_for_status()
            return response.json()
        except requests.exceptions.Timeout:
            raise RuntimeError(f"Request to {url} timed out after {self.timeout} seconds")
        except requests.exceptions.ConnectionError:
            raise RuntimeError(f"Failed to connect to backend at {url}")
        except requests.exceptions.HTTPError as e:
            raise RuntimeError(f"Backend returned error {response.status_code}: {response.text}")
        except Exception as e:
            raise RuntimeError(f"Unexpected error during registration: {str(e)}")

    def heartbeat_device(self, device_id: int) -> Dict[str, Any]:
        """
        Send a heartbeat for a previously registered device.

        Args:
            device_id: The backend device identifier.

        Returns:
            The updated device response from the backend

        Raises:
            RuntimeError: If the request fails or the backend returns an error.
        """
        url = f"{self.base_url}/api/v1/devices/{device_id}/heartbeat"

        try:
            response = requests.post(
                url,
                timeout=self.timeout,
                headers={"Content-Type": "application/json"},
            )
            response.raise_for_status()
            return response.json()
        except requests.exceptions.Timeout:
            raise RuntimeError(f"Heartbeat request to {url} timed out after {self.timeout} seconds")
        except requests.exceptions.ConnectionError:
            raise RuntimeError(f"Failed to connect to backend at {url}")
        except requests.exceptions.HTTPError:
            raise RuntimeError(f"Backend returned error {response.status_code}: {response.text}")
        except Exception as e:
            raise RuntimeError(f"Unexpected error during heartbeat: {str(e)}")

    def send_telemetry(self, device_id: int, telemetry: Dict[str, Any]) -> Dict[str, Any]:
        """
        Send telemetry for a previously registered device.

        Args:
            device_id: The backend device identifier.
            telemetry: A dictionary containing cpuUsage, ramUsage, diskUsage, uptimeSeconds.

        Returns:
            The telemetry response from the backend.
        """
        url = f"{self.base_url}/api/v1/devices/{device_id}/telemetry"

        try:
            response = requests.post(
                url,
                json=telemetry,
                timeout=self.timeout,
                headers={"Content-Type": "application/json"},
            )
            response.raise_for_status()
            return response.json()
        except requests.exceptions.Timeout:
            raise RuntimeError(f"Telemetry request to {url} timed out after {self.timeout} seconds")
        except requests.exceptions.ConnectionError:
            raise RuntimeError(f"Failed to connect to backend at {url}")
        except requests.exceptions.HTTPError:
            raise RuntimeError(f"Backend returned error {response.status_code}: {response.text}")
        except Exception as e:
            raise RuntimeError(f"Unexpected error during telemetry send: {str(e)}")

    def get_devices(self) -> list:
        """
        Retrieve all registered devices from the backend.
        
        Returns:
            List of device objects
            
        Raises:
            requests.exceptions.RequestException: If the request fails
        """
        url = f"{self.base_url}/api/v1/devices"
        
        try:
            response = requests.get(
                url,
                timeout=self.timeout,
                headers={"Accept": "application/json"},
            )
            response.raise_for_status()
            return response.json()
        except requests.exceptions.Timeout:
            raise RuntimeError(f"Request to {url} timed out after {self.timeout} seconds")
        except requests.exceptions.ConnectionError:
            raise RuntimeError(f"Failed to connect to backend at {url}")
        except requests.exceptions.HTTPError as e:
            raise RuntimeError(f"Backend returned error {response.status_code}: {response.text}")
        except Exception as e:
            raise RuntimeError(f"Unexpected error retrieving devices: {str(e)}")
