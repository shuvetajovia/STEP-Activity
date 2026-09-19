# gdb/service/transfer_service.py
from gdb.domain.iaccount import IAccount
from gdb.domain.account import Account
from gdb.exceptions.account_exception import AccountException
from gdb.exceptions.inactive_account_exception import InactiveAccountException
from gdb.exceptions.insufficient_balance_exception import InsufficientBalanceException
from gdb.exceptions.invalid_pin_exception import InvalidPinException

class TransferService:
    """Service managing funds transfer operations, validations, and daily tier limits."""

    def __init__(self) -> None:
        pass

    def transfer(self, from_acc: IAccount, to_acc: IAccount, amount: float, pin: int) -> None:
        # ============================================================
        # 📝 STEP 1: Transfer Funds With Daily Limit
        # ============================================================
        if from_acc is None or to_acc is None:
            raise AccountException("Source and destination accounts are required")

        if not from_acc.is_active() or not to_acc.is_active():
            raise InactiveAccountException("Both accounts must be active to transfer funds")

        if not from_acc.verify_pin(pin):
            raise InvalidPinException("Incorrect PIN")

        if not from_acc.can_withdraw(amount):
            raise InsufficientBalanceException(f"Insufficient balance for transfer of Rs. {amount}")

        if isinstance(from_acc, Account):
            from_acc.reset_daily_transfer_if_needed()
            if not from_acc.can_transfer(amount):
                raise AccountException(f"Daily transfer limit exceeded. Remaining today: Rs. {from_acc.get_remaining_daily_transfer_limit()}")

        from_acc.withdraw(amount, pin)
        to_acc.deposit(amount)

        if isinstance(from_acc, Account):
            from_acc.update_daily_transfer_total(amount)
