package com.gdb.accounts;

import com.gdb.exceptions.AccountException;

/**
 * FixedDepositAccount — a bank account that does not allow premature withdrawal.
 *
 * Any attempt to call processDebit() always throws AccountException,
 * demonstrating that the Template Method Pattern still enforces the common
 * withdrawal sequence (PIN → status → amount), but the account-specific rule
 * simply blocks the debit entirely.
 *
 * Activity 9: Abstract Classes & Template Method Pattern
 */
public class FixedDepositAccount extends AbstractAccount {

    // ===== Constants =====
    private static final String ACCOUNT_TYPE = "FIXED_DEPOSIT";

    // ===== Fixed-Deposit-Specific Fields =====
    private final int    tenureMonths;
    private final double interestRate;

    // ===== Constructor =====

    /**
     * Creates a FixedDepositAccount.
     *
     * @param accountNumber  unique account identifier
     * @param name           account holder name
     * @param age            account holder age (>= 18)
     * @param depositAmount  principal deposit amount (>= 0)
     * @param pin            4-digit PIN string
     * @param tenureMonths   tenure of the fixed deposit in months
     * @param interestRate   annual interest rate (%)
     */
    public FixedDepositAccount(String accountNumber, String name, int age,
                               double depositAmount, String pin,
                               int tenureMonths, double interestRate) {
        super(accountNumber, name, age, depositAmount, pin);
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
    }

    // ===== Abstract Method Implementations =====

    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }

    /**
     * Debit rule for Fixed Deposit Account:
     *   Premature withdrawal is NEVER allowed.
     *   Always throws AccountException regardless of amount or balance.
     *
     * @throws AccountException always — premature withdrawal not permitted
     */
    @Override
    protected void processDebit(double amount) throws AccountException {
        throw new AccountException(
            "Premature withdrawal not allowed on Fixed Deposit account " +
            getAccountNumber() + ". Tenure: " + tenureMonths + " months."
        );
    }

    // ===== Fixed-Deposit-Specific Methods =====

    /**
     * Calculates the maturity amount at the end of the tenure.
     * Uses simple interest: maturity = principal + (principal * rate * years / 100)
     *
     * @return maturity amount in Rs
     */
    public double calculateMaturityAmount() {
        double years = tenureMonths / 12.0;
        double interest = getBalance() * (interestRate / 100.0) * years;
        return getBalance() + interest;
    }

    /** Returns the tenure in months. */
    public int getTenureMonths() {
        return tenureMonths;
    }

    /** Returns the annual interest rate (%). */
    public double getInterestRate() {
        return interestRate;
    }
}
