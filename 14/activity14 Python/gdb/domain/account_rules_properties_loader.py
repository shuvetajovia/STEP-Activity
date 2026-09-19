# gdb/domain/account_rules_properties_loader.py
import os

class AccountRulesPropertiesLoader:
    """Utility loading external .properties files."""

    @staticmethod
    def load_rules(account_type: str) -> dict:
        config_path = os.path.join(
            os.path.dirname(os.path.abspath(__file__)),
            "..",
            "resources",
            "config",
            "rules",
            f"{account_type.lower()}.properties"
        )
        if not os.path.exists(config_path):
            return {}
        
        rules = {}
        with open(config_path, "r", encoding="utf-8") as f:
            for line in f:
                line = line.strip()
                if not line or line.startswith("#"):
                    continue
                if "=" in line:
                    key, value = line.split("=", 1)
                    rules[key.strip()] = value.strip()
        return rules
