package com.gdb.domain;

import com.gdb.exceptions.*;

public class TransferService {

    /**
     * Executes an atomic, validated funds transfer between two accounts enforcing daily transfer limits.
     *
     * Transfer Flow:
     * 1. Validate accounts are non-null and not the same account.
     * 2. Validate transfer amount is strictly positive.
     * 3. Validate sender PIN before debit attempt.
     * 4. Validate both accounts are in ACTIVE state.
     * 5. Check sender's remaining daily transfer limit.
     * 6. Withdraw funds from sender.
     * 7. Update sender's daily limit usage upon successful withdrawal.
     * 8. Deposit funds into receiver account.
     *
     * @param fromAccount Sender account
     * @param toAccount   Receiver account
     * @param amount      Transfer amount
     * @param pin         Sender's 4-digit PIN
     * @return true if transfer completed successfully
     * @throws AccountException if any validation or banking rule fails
     */
    public static boolean transfer(IAccount fromAccount, IAccount toAccount, double amount, String pin) throws AccountException {
        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Source and destination accounts must not be null");
        }

        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer funds to the same account: " + fromAccount.getAccountNumber());
        }

        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive. Provided: " + amount);
        }

        if (!fromAccount.validatePin(pin)) {
            throw new InvalidPinException("Invalid PIN entered for account: " + fromAccount.getAccountNumber());
        }

        if (!"ACTIVE".equalsIgnoreCase(fromAccount.getStatus())) {
            throw new InactiveAccountException("Source account " + fromAccount.getAccountNumber() + " is inactive");
        }

        if (!"ACTIVE".equalsIgnoreCase(toAccount.getStatus())) {
            throw new InactiveAccountException("Destination account " + toAccount.getAccountNumber() + " is inactive");
        }

        // Daily limit check
        if (!fromAccount.canTransfer(amount)) {
            throw new DailyLimitExceededException(
                "Transfer of Rs " + amount + " exceeds remaining daily limit of Rs " +
                fromAccount.getRemainingDailyLimit() + " (Daily Limit: Rs " + fromAccount.getDailyLimit() +
                ", Transferred Today: Rs " + fromAccount.getTransferredToday() + ")"
            );
        }

        // Transactional execution: withdraw -> update daily usage -> deposit
        fromAccount.withdraw(amount, pin);
        fromAccount.updateDailyTransferred(amount);
        toAccount.deposit(amount);

        return true;
    }
}
