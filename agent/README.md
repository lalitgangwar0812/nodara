# Nodara endpoint agent

The endpoint agent will collect approved host information, emit heartbeats, and eventually execute controlled remediation actions. Windows is the initial target; its design will remain cross-platform where practical.

This foundation contains only an executable entry point. It does not collect telemetry, call the backend, or perform system changes.

Run it with `python -m nodara_agent` from this directory.
