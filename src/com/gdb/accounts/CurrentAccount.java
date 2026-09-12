package com.gdb.accounts;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;

/**
 * CurrentAccount — a bank account with an overdraft facility.
 *
 * The customer can withdraw up to: balance + overdraftLimit.
 * If the withdrawal exceeds that ceiling, InsufficientBalanceException is thrown.
 *
 * Activity 9: Abstract Classes & Template Method Pattern
 */
public class CurrentAccount extends AbstractAccount {

    // ===== Constants =====
    private static final String ACCOUNT_TYPE    = "CURRENT";
    private static final double OVERDRAFT_LIMIT = 5000.0;

    // ===== Constructor =====

    /**
     * Creates a CurrentAccount.
     *
     * @param accountNumber  unique account identifier
     * @param name           account holder name
     * @param age            account holder age (>= 18)
     * @param initialBalance starting balance (>= 0)
     * @param pin            4-digit PIN string
     */
    public CurrentAccount(String accountNumber, String name, int age,
                          double initialBalance, String pin) {
        super(accountNumber, name, age, initialBalance, pin);
    }

    // ===== Abstract Method Implementations =====

    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }

    /**
     * Debit rule for Current Account:
     *   The customer may withdraw up to (balance + overdraftLimit).
     *   Exceeding that ceiling throws InsufficientBalanceException.
     *
     * @throws InsufficientBalanceException if amount exceeds balance + overdraft limit
     */
    @Override
    protected void processDebit(double amount) throws AccountException {
        double maxWithdrawable = getBalance() + OVERDRAFT_LIMIT;
        if (amount > maxWithdrawable) {
            throw new InsufficientBalanceException(
                "Cannot withdraw Rs " + amount +
                ". Maximum available (balance + overdraft): Rs " + maxWithdrawable
            );
        }
        setBalance(getBalance() - amount);
    }

    // ===== Current-Specific Methods =====

    /** Returns the fixed overdraft limit for this account. */
    public double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }

    /**
     * Returns the total amount the customer can currently withdraw
     * (balance + overdraft limit).
     */
    public double getTotalAvailableFunds() {
        return getBalance() + OVERDRAFT_LIMIT;
    }
}
