package com.gdb.domain;

import com.gdb.exceptions.*;

public class CurrentAccount extends AbstractAccount {
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String name, int age, double balance, String status, String pin, double overdraftLimit) {
        super(accountNumber, name, age, balance, "CURRENT", status, pin, AccountRulesEngine.getCurrentDailyLimit());
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void processDebit(double amount) throws AccountException {
        if ((this.balance - amount) < -this.overdraftLimit) {
            throw new InsufficientBalanceException("Overdraft limit of Rs " + overdraftLimit + " exceeded");
        }
        this.balance -= amount;
    }

    public double getOverdraftLimit() { return overdraftLimit; }
}
