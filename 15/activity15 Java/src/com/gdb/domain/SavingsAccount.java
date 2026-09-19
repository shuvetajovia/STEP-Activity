package com.gdb.domain;

import com.gdb.exceptions.InvalidAgeException;

public class SavingsAccount extends Account {
    public SavingsAccount(int accountNumber, String name, int age, double initialBalance, int tenureYears) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, tenureYears);
    }

    public SavingsAccount(int accountNumber, String name, int age, double initialBalance) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, 0);
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    @Override
    public double getMinimumBalance() {
        return AccountRulesEngine.getInstance().getMinimumBalance("SAVINGS", tenureYears);
    }

    @Override
    public double getInterestRate() {
        return AccountRulesEngine.getInstance().getInterestRate("SAVINGS", tenureYears);
    }

    @Override
    public boolean canWithdraw(double amount) {
        return (this.balance - amount) >= getMinimumBalance();
    }
}
