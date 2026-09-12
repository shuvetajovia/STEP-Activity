package com.gdb.accounts;

public class AccountFactory {
    public static IAccount createAccount(String type, String accNum, String name, int age, double balance, String pin) {
        if (type == null) return null;
        switch (type.toUpperCase()) {
            case "SAVINGS":
                return new SavingsAccount(accNum, name, age, balance, pin);
            case "CURRENT":
                return new CurrentAccount(accNum, name, age, balance, pin);
            case "FIXED_DEPOSIT":
            case "FD":
                return new FixedDepositAccount(accNum, name, age, balance, pin, 12, 6.5);
            case "SALARY":
                return new SalaryAccount(accNum, name, age, balance, pin, "TechCorp");
            default:
                throw new IllegalArgumentException("Unknown account type: " + type);
        }
    }
}