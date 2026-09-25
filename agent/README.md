# Nodara endpoint agent

The endpoint agent collects approved host information, emits heartbeats, and sends CPU, RAM, disk, and uptime telemetry. Windows is the initial target; its design remains cross-platform where practical.

The agent persists its stable endpoint UUID in `~/.nodara/agent_id.txt` so restarting the agent reuses the same backend endpoint. Heartbeat and telemetry intervals are configurable with `NODARA_HEARTBEAT_INTERVAL_SECONDS` and `NODARA_TELEMETRY_INTERVAL_SECONDS`.

Run it with `python -m nodara_agent` from this directory.
