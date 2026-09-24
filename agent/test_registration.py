import os
import unittest

from nodara_agent.registration import get_heartbeat_interval_seconds, get_system_info
from nodara_agent.telemetry import collect_system_telemetry, get_telemetry_interval_seconds


class RegistrationTests(unittest.TestCase):
    def test_heartbeat_interval_defaults_to_30_seconds(self):
        os.environ.pop("NODARA_HEARTBEAT_INTERVAL_SECONDS", None)
        self.assertEqual(get_heartbeat_interval_seconds(), 30)

    def test_heartbeat_interval_respects_environment(self):
        os.environ["NODARA_HEARTBEAT_INTERVAL_SECONDS"] = "45"
        try:
            self.assertEqual(get_heartbeat_interval_seconds(), 45)
        finally:
            os.environ.pop("NODARA_HEARTBEAT_INTERVAL_SECONDS", None)

    def test_telemetry_interval_defaults_to_60_seconds(self):
        os.environ.pop("NODARA_TELEMETRY_INTERVAL_SECONDS", None)
        self.assertEqual(get_telemetry_interval_seconds(), 60)

    def test_telemetry_interval_respects_environment(self):
        os.environ["NODARA_TELEMETRY_INTERVAL_SECONDS"] = "75"
        try:
            self.assertEqual(get_telemetry_interval_seconds(), 75)
        finally:
            os.environ.pop("NODARA_TELEMETRY_INTERVAL_SECONDS", None)

    def test_system_info_contains_expected_fields(self):
        info = get_system_info()
        self.assertIn("hostname", info)
        self.assertIn("osName", info)
        self.assertIn("osVersion", info)
        self.assertIn("agentVersion", info)

    def test_collect_system_telemetry_returns_expected_fields(self):
        telemetry = collect_system_telemetry()
        self.assertIn("cpuUsage", telemetry)
        self.assertIn("ramUsage", telemetry)
        self.assertIn("diskUsage", telemetry)
        self.assertIn("uptimeSeconds", telemetry)
        self.assertGreaterEqual(telemetry["cpuUsage"], 0)
        self.assertLessEqual(telemetry["cpuUsage"], 100)
        self.assertGreaterEqual(telemetry["ramUsage"], 0)
        self.assertLessEqual(telemetry["ramUsage"], 100)
        self.assertGreaterEqual(telemetry["diskUsage"], 0)
        self.assertLessEqual(telemetry["diskUsage"], 100)
        self.assertGreaterEqual(telemetry["uptimeSeconds"], 0)


if __name__ == "__main__":
    unittest.main()
