package com.gdb.domain;

import com.gdb.exceptions.InvalidAgeException;

public class FixedDepositAccount extends Account {
    public FixedDepositAccount(int accountNumber, String name, int age, double initialBalance, int tenureYears) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, tenureYears);
    }

    public FixedDepositAccount(int accountNumber, String name, int age, double initialBalance) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, 0);
    }

    @Override
    public String getAccountType() {
        return "FixedDeposit";
    }

    @Override
    public double getMinimumBalance() {
        return AccountRulesEngine.getInstance().getMinimumBalance("FIXEDDEPOSIT", tenureYears);
    }

    @Override
    public double getInterestRate() {
        return AccountRulesEngine.getInstance().getInterestRate("FIXEDDEPOSIT", tenureYears);
    }

    @Override
    public boolean canWithdraw(double amount) {
        return false;
    }
}
