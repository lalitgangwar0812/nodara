import os
import unittest

from nodara_agent.registration import get_heartbeat_interval_seconds, get_system_info


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

    def test_system_info_contains_expected_fields(self):
        info = get_system_info()
        self.assertIn("hostname", info)
        self.assertIn("osName", info)
        self.assertIn("osVersion", info)
        self.assertIn("agentVersion", info)


if __name__ == "__main__":
    unittest.main()
