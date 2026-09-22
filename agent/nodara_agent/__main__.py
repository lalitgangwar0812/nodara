"""Executable entry point for the Nodara endpoint agent."""

import sys
from nodara_agent.registration import register_with_backend


def main() -> None:
    """Main entry point for the Nodara agent."""
    print("Starting Nodara endpoint agent...")
    print()
    
    success = register_with_backend()
    
    if not success:
        print()
        print("Agent registration failed. Please ensure the backend is running.")
        sys.exit(1)
    
    print()
    print("Agent is ready. Scheduled heartbeats and telemetry collection are not yet implemented.")


if __name__ == "__main__":
    main()
