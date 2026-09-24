"""Executable entry point for the Nodara endpoint agent."""

import os
import sys
import time
from threading import Thread

from nodara_agent.registration import register_with_backend, run_heartbeat_loop, run_telemetry_loop


def main() -> None:
    """Main entry point for the Nodara agent."""
    print("Starting Nodara endpoint agent...")
    print()

    response = register_with_backend()
    if response is None or response.get("id") is None:
        print()
        print("Agent registration failed. Please ensure the backend is running.")
        sys.exit(1)

    device_id = response.get("id")
    heartbeat_interval = os.getenv("NODARA_HEARTBEAT_INTERVAL_SECONDS", "30")
    telemetry_interval = os.getenv("NODARA_TELEMETRY_INTERVAL_SECONDS", "60")
    print()
    print(
        "Agent is ready. Heartbeats will be sent every "
        f"{heartbeat_interval} seconds and telemetry every {telemetry_interval} seconds."
    )

    heartbeat_thread = Thread(target=run_heartbeat_loop, kwargs={"device_id": device_id}, daemon=True)
    telemetry_thread = Thread(target=run_telemetry_loop, kwargs={"device_id": device_id}, daemon=True)

    heartbeat_thread.start()
    telemetry_thread.start()

    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print()
        print("Stopping Nodara endpoint agent.")


if __name__ == "__main__":
    main()
