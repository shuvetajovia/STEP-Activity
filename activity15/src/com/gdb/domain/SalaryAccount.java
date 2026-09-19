package com.gdb.domain;

import com.gdb.exceptions.InvalidAgeException;

public class SalaryAccount extends Account {
    public SalaryAccount(int accountNumber, String name, int age, double initialBalance, int tenureYears) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, tenureYears);
    }

    public SalaryAccount(int accountNumber, String name, int age, double initialBalance) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, 0);
    }

    @Override
    public String getAccountType() {
        return "Salary";
    }

    @Override
    public double getMinimumBalance() {
        return AccountRulesEngine.getInstance().getMinimumBalance("SALARY", tenureYears);
    }

    @Override
    public double getInterestRate() {
        return AccountRulesEngine.getInstance().getInterestRate("SALARY", tenureYears);
    }

    @Override
    public boolean canWithdraw(double amount) {
        return (this.balance - amount) >= getMinimumBalance();
    }
}
