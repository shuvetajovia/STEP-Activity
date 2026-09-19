package com.gdb.domain;

import com.gdb.exceptions.*;

public class SalaryAccount extends AbstractAccount {
    private String employer;
    private int inactiveMonths;

    public SalaryAccount(String accountNumber, String name, int age, double balance, String status, String pin, String employer) {
        super(accountNumber, name, age, balance, "SALARY", status, pin, AccountRulesEngine.getSalaryDailyLimit());
        this.employer = employer;
        this.inactiveMonths = 0;
    }

    @Override
    public void processDebit(double amount) throws AccountException {
        if ((this.balance - amount) < 0) {
            throw new InsufficientBalanceException("Insufficient funds in salary account: available Rs " + balance);
        }
        this.balance -= amount;
    }

    public void incrementInactiveMonths() {
        this.inactiveMonths++;
        if (this.inactiveMonths >= 3) {
            this.status = "INACTIVE";
        }
    }

    public String getEmployer() { return employer; }
    public int getInactiveMonths() { return inactiveMonths; }
}
