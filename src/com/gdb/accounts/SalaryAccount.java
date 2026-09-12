package com.gdb.accounts;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InsufficientBalanceException;

/**
 * SalaryAccount — a bank account for salaried employees.
 *
 * Withdrawal rule: the customer cannot withdraw more than the available balance.
 * The account also tracks inactive months where no salary is credited.
 *
 * Activity 9: Abstract Classes & Template Method Pattern
 */
public class SalaryAccount extends AbstractAccount {

    // ===== Constants =====
    private static final String ACCOUNT_TYPE = "SALARY";

    // ===== Salary-Specific Fields =====
    private final String employerName;
    private       int    inactiveMonths;  // months without salary credit

    // ===== Constructor =====

    /**
     * Creates a SalaryAccount.
     *
     * @param accountNumber  unique account identifier
     * @param name           account holder name
     * @param age            account holder age (>= 18)
     * @param initialBalance starting balance (>= 0)
     * @param pin            4-digit PIN string
     * @param employerName   name of the employer
     */
    public SalaryAccount(String accountNumber, String name, int age,
                         double initialBalance, String pin, String employerName) {
        super(accountNumber, name, age, initialBalance, pin);
        this.employerName   = employerName;
        this.inactiveMonths = 0;
    }

    // ===== Abstract Method Implementations =====

    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }

    /**
     * Debit rule for Salary Account:
     *   Simple rule — the customer cannot withdraw more than the current balance.
     *
     * @throws InsufficientBalanceException if withdrawal amount exceeds balance
     */
    @Override
    protected void processDebit(double amount) throws AccountException {
        if (amount > getBalance()) {
            throw new InsufficientBalanceException(
                "Insufficient balance in Salary Account. " +
                "Available: Rs " + getBalance() + ", Requested: Rs " + amount
            );
        }
        setBalance(getBalance() - amount);
    }

    // ===== Salary-Specific Methods =====

    /**
     * Increments the inactive months counter by 1.
     * Called during the monthly banking cycle if no salary was credited.
     */
    public void incrementInactiveMonths() {
        this.inactiveMonths++;
    }

    /**
     * Resets the inactive months counter to 0.
     * Called when a salary credit is received.
     */
    public void resetInactiveMonths() {
        this.inactiveMonths = 0;
    }

    /** Returns the number of months without a salary credit. */
    public int getInactiveMonths() {
        return inactiveMonths;
    }

    /** Returns the employer name associated with this salary account. */
    public String getEmployerName() {
        return employerName;
    }
}
