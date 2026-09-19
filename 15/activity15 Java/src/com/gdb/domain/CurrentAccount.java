package com.gdb.domain;

import com.gdb.exceptions.InvalidAgeException;

public class CurrentAccount extends Account {
    public CurrentAccount(int accountNumber, String name, int age, double initialBalance, int tenureYears) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, tenureYears);
    }

    public CurrentAccount(int accountNumber, String name, int age, double initialBalance) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, 0);
    }

    @Override
    public String getAccountType() {
        return "Current";
    }

    @Override
    public double getMinimumBalance() {
        return AccountRulesEngine.getInstance().getMinimumBalance("CURRENT", tenureYears);
    }

    @Override
    public double getInterestRate() {
        return AccountRulesEngine.getInstance().getInterestRate("CURRENT", tenureYears);
    }

    @Override
    public boolean canWithdraw(double amount) {
        return (this.balance - amount) >= getMinimumBalance();
    }
}
