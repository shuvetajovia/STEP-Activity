package com.gdb.accounts;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.MinimumBalanceViolationException;

/**
 * SavingsAccount — a bank account with:
 *   - A minimum balance requirement (Rs 1000)
 *   - An annual interest rate (4%)
 *
 * processDebit() ensures the balance never drops below the minimum.
 *
 * Activity 9: Abstract Classes & Template Method Pattern
 */
public class SavingsAccount extends AbstractAccount {

    // ===== Constants =====
    private static final String ACCOUNT_TYPE   = "SAVINGS";
    private static final double MIN_BALANCE    = 1000.0;
    private static final double INTEREST_RATE  = 4.0;   // 4% per annum

    // ===== Constructor =====

    /**
     * Creates a SavingsAccount.
     *
     * @param accountNumber  unique account identifier
     * @param name           account holder name
     * @param age            account holder age (>= 18)
     * @param initialBalance starting balance (>= MIN_BALANCE)
     * @param pin            4-digit PIN string
     */
    public SavingsAccount(String accountNumber, String name, int age,
                          double initialBalance, String pin) {
        super(accountNumber, name, age, initialBalance, pin);

        if (initialBalance < MIN_BALANCE) {
            throw new IllegalArgumentException(
                "Savings account requires a minimum balance of Rs " + MIN_BALANCE +
                ". Provided: Rs " + initialBalance
            );
        }
    }

    // ===== Abstract Method Implementations =====

    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }

    /**
     * Debit rule for Savings Account:
     *   Withdrawal is rejected if the remaining balance would fall below the minimum balance.
     *
     * @throws MinimumBalanceViolationException if withdrawal would breach minimum balance
     */
    @Override
    protected void processDebit(double amount) throws AccountException {
        double remainingBalance = getBalance() - amount;
        if (remainingBalance < MIN_BALANCE) {
            throw new MinimumBalanceViolationException(
                "Cannot withdraw Rs " + amount +
                ". Minimum balance of Rs " + MIN_BALANCE + " must be maintained." +
                " Balance after withdrawal would be: Rs " + remainingBalance
            );
        }
        setBalance(remainingBalance);
    }

    // ===== Savings-Specific Methods =====

    /**
     * Applies simple interest to the current balance and updates the balance.
     * Interest = balance * (rate / 100)
     *
     * @return the interest amount added
     */
    public double applyInterest() {
        double interest = getBalance() * (INTEREST_RATE / 100.0);
        setBalance(getBalance() + interest);
        return interest;
    }

    /**
     * Calculates interest for a given number of years without applying it.
     *
     * @param years number of years (must be >= 0)
     * @return interest amount in Rs
     */
    public double calculateInterest(int years) {
        if (years < 0) {
            throw new IllegalArgumentException("Years must be non-negative. Provided: " + years);
        }
        return getBalance() * (INTEREST_RATE / 100.0) * years;
    }

    /** Returns the annual interest rate (%). */
    public double getInterestRate() {
        return INTEREST_RATE;
    }

    /** Returns the minimum balance requirement. */
    public double getMinBalance() {
        return MIN_BALANCE;
    }
}
