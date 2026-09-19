package com.gdb.accounts;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class SavingsAccount extends AbstractAccount {

    private static final String ACCOUNT_TYPE   = "SAVINGS";
    private int tenureYears = 0;
    private double minBalance = 1000.0;
    private double interestRate = 4.0;

    public SavingsAccount(String accountNumber, String name, int age,
                          double initialBalance, String pin) {
        super(accountNumber, name, age, initialBalance, pin);
        this.minBalance = 1000.0;
        this.interestRate = 4.0;
        this.tenureYears = 0;
        if (initialBalance < this.minBalance) {
            throw new IllegalArgumentException(
                "Savings account requires a minimum balance of Rs " + this.minBalance +
                ". Provided: Rs " + initialBalance
            );
        }
    }

    public SavingsAccount(String accountNumber, String name, int age, double balance, String status, String pin, int tenureYears) {
        super(accountNumber, name, age, balance, pin);
        this.tenureYears = tenureYears;
        this.minBalance = AccountRulesEngine.getSavingsMinBalance(tenureYears);
        this.interestRate = AccountRulesEngine.getSavingsInterestRate(tenureYears);
    }

    public SavingsAccount(String accountNumber, String name, int age, double balance, String status, String pin, double minBalance, double interestRate) {
        super(accountNumber, name, age, balance, pin);
        this.tenureYears = 0;
        this.minBalance = minBalance;
        this.interestRate = interestRate;
    }

    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }

    @Override
    protected void processDebit(double amount) throws AccountException {
        double remainingBalance = getBalance() - amount;
        if (remainingBalance < this.minBalance) {
            throw new MinimumBalanceViolationException(
                "Cannot withdraw Rs " + amount +
                ". Minimum balance of Rs " + this.minBalance + " must be maintained." +
                " Balance after withdrawal would be: Rs " + remainingBalance
            );
        }
        setBalance(remainingBalance);
    }

    public double applyInterest() {
        double interest = getBalance() * (this.interestRate / 100.0);
        setBalance(getBalance() + interest);
        return interest;
    }

    public double calculateInterest(int years) {
        if (years < 0) {
            throw new IllegalArgumentException("Years must be non-negative. Provided: " + years);
        }
        return getBalance() * (this.interestRate / 100.0) * years;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public double getMinBalance() {
        return this.minBalance;
    }

    public int getTenureYears() {
        return this.tenureYears;
    }
}
