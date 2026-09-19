# gdb/domain/account.py
from datetime import datetime, date
from typing import Optional
from gdb.domain.iaccount import IAccount
from gdb.domain.account_rules_engine import AccountRulesEngine
from gdb.exceptions.invalid_age_exception import InvalidAgeException
from gdb.exceptions.invalid_amount_exception import InvalidAmountException
from gdb.exceptions.invalid_pin_exception import InvalidPinException
from gdb.exceptions.inactive_account_exception import InactiveAccountException
from gdb.exceptions.insufficient_balance_exception import InsufficientBalanceException

class Account(IAccount):
    """Abstract Base Class Account implementing core state, validations, and daily limit tracking."""

    def __init__(self, account_number: int, name: str, age: int, initial_balance: float, tenure_years: int = 0) -> None:
        if age < 18:
            raise InvalidAgeException(f"Customer must be at least 18 years old. Provided: {age}")
        if not name or not name.strip():
            raise InvalidAgeException("Name cannot be empty")

        self._account_number = int(account_number)
        self._name = name.strip()
        self._age = age
        self._balance = float(initial_balance)
        self._status = "Active"
        self._pin: Optional[int] = None
        self._opening_date = "2026-08-28"
        self._tenure_years = max(0, tenure_years)

        self._daily_transfer_total = 0.0
        self._last_transfer_date = datetime.now()

    def deposit(self, amount: float) -> None:
        if self._status != "Active":
            raise InactiveAccountException("Account is inactive.")
        if amount <= 0:
            raise InvalidAmountException(f"Deposit amount must be positive. Provided: Rs. {amount}")
        self._balance += amount

    def withdraw(self, amount: float, pin: int) -> None:
        if self._status != "Active":
            raise InactiveAccountException("Account is inactive.")
        if self._pin is None:
            raise InvalidPinException("PIN not set for this account")
        if self._pin != pin:
            raise InvalidPinException("Incorrect PIN")
        if amount <= 0:
            raise InvalidAmountException(f"Amount must be positive. Provided: Rs. {amount}")
        if not self.can_withdraw(amount):
            raise InsufficientBalanceException("Withdrawal not allowed")
        self._balance -= amount

    def close_account(self) -> None:
        if self._status != "Active":
            raise InactiveAccountException("Account is already closed / inactive.")
        self._status = "Inactive"

    def reopen_account(self) -> None:
        if self._status == "Active":
            raise InactiveAccountException("Account is already active.")
        self._status = "Active"

    def set_pin(self, pin: int) -> None:
        pin_int = int(pin)
        if pin_int < 1000 or pin_int > 9999:
            raise InvalidPinException(f"PIN must be a 4-digit number (1000-9999). Provided: {pin}")
        self._pin = pin_int

    def verify_pin(self, pin: int) -> bool:
        return self._pin is not None and self._pin == int(pin)

    def has_pin(self) -> bool:
        return self._pin is not None

    def is_active(self) -> bool:
        return self._status == "Active"

    def get_account_info(self) -> str:
        return f"Account #{self._account_number} | {self._name} ({self._age} yrs, Tenure: {self._tenure_years} yrs) | {self.get_account_type()} | Rs. {self._balance:.1f} | {self._status}"

    def get_account_number(self) -> int: return self._account_number
    def get_account_holder_name(self) -> str: return self._name
    def get_balance(self) -> float: return self._balance
    def get_opening_date(self) -> str: return self._opening_date
    def get_tenure_years(self) -> int: return self._tenure_years
    def set_tenure_years(self, tenure_years: int) -> None: self._tenure_years = max(0, tenure_years)

    def get_daily_transfer_limit(self) -> float:
        # 📝 STEP 3: Get Daily Transfer Limit
        return AccountRulesEngine.get_instance().get_daily_transfer_limit(self.get_account_type(), self.get_tenure_years())

    def get_remaining_daily_transfer_limit(self) -> float:
        # 📝 STEP 4: Get Remaining Daily Limit
        self.reset_daily_transfer_if_needed()
        return max(0.0, self.get_daily_transfer_limit() - self._daily_transfer_total)

    def can_transfer(self, amount: float) -> bool:
        # 📝 STEP 5: Check Amount Against Daily Limit
        self.reset_daily_transfer_if_needed()
        return (self._daily_transfer_total + amount) <= self.get_daily_transfer_limit()

    def update_daily_transfer_total(self, amount: float) -> None:
        # 📝 STEP 6: Record A Completed Transfer
        self.reset_daily_transfer_if_needed()
        self._daily_transfer_total += float(amount)
        self._last_transfer_date = datetime.now()

    def reset_daily_transfer_if_needed(self) -> None:
        # 📝 STEP 7: Reset The Total On A New Day
        if self._last_transfer_date.date() != date.today():
            self._daily_transfer_total = 0.0
            self._last_transfer_date = datetime.now()

    def get_daily_transfer_total(self) -> float:
        return self._daily_transfer_total

    def get_last_transfer_date(self) -> datetime:
        return self._last_transfer_date

    # CamelCase aliases
    def getDailyTransferLimit(self) -> float: return self.get_daily_transfer_limit()
    def getRemainingDailyTransferLimit(self) -> float: return self.get_remaining_daily_transfer_limit()
    def canTransfer(self, amount: float) -> bool: return self.can_transfer(amount)
    def updateDailyTransferTotal(self, amount: float) -> None: self.update_daily_transfer_total(amount)
    def resetDailyTransferIfNeeded(self) -> None: self.reset_daily_transfer_if_needed()
    def getDailyTransferTotal(self) -> float: return self.get_daily_transfer_total()
    def getLastTransferDate(self) -> datetime: return self.get_last_transfer_date()
