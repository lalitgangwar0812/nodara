"""Executable entry point for the Nodara endpoint agent."""

import sys
from nodara_agent.registration import register_with_backend, run_heartbeat_loop


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
    print()
    print(f"Agent is ready. Heartbeats will be sent every {__import__('os').getenv('NODARA_HEARTBEAT_INTERVAL_SECONDS', '30')} seconds.")
    try:
        run_heartbeat_loop(device_id=device_id)
    except KeyboardInterrupt:
        print()
        print("Stopping Nodara endpoint agent.")


if __name__ == "__main__":
    main()
