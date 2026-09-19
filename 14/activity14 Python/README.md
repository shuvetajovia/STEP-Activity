# Activity 14: External Properties Rules Engine

## Objective
Externalize all business rules and policy thresholds into `.properties` files loaded dynamically via `AccountRulesPropertiesLoader` without modifying or restarting application code.

---

## Target Files to Complete
- `gdb/resources/config/rules/savings.properties`
- `gdb/resources/config/rules/current.properties`
- `gdb/resources/config/rules/fixeddeposit.properties`
- `gdb/resources/config/rules/salary.properties`
- `gdb/domain/account_rules_properties_loader.py`
- `gdb/domain/account_rules_engine.py`

---

## Plain English Step-by-Step Instructions

### Step 1: Define Key-Value Rules in Properties Files
1. Configure `minBalance` and `interestRate` in `savings.properties`.
2. Configure `overdraftLimit` in `current.properties`.
3. Configure `interestRate` in `fixeddeposit.properties`.

### Step 2: Implement Properties Loader
1. Read `.properties` files and parse `key=value` lines into Python dictionaries.

---

## How to Run (Multi-OS Guide)
Run these commands from inside this activity folder (the folder that contains `gdb/`).

### Windows (PowerShell)
```powershell
python -m gdb.tests.test_account_rules_engine_properties
```

### Windows (Command Prompt - CMD)
```cmd
python -m gdb.tests.test_account_rules_engine_properties
```

### Linux & macOS (Terminal / Bash / Zsh)
```bash
python3 -m gdb.tests.test_account_rules_engine_properties
```
