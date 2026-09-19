package com.gdb.domain;

import com.gdb.exceptions.*;
import java.time.LocalDate;

public abstract class AbstractAccount implements IAccount {
    protected String accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected String pin;
    
    // Daily Transfer Tracking
    protected double dailyLimit;
    protected double transferredToday;
    protected LocalDate lastTransferDate;

    public AbstractAccount(String accountNumber, String name, int age, double balance, String accountType, String status, String pin, double dailyLimit) {
        if (age < 18) throw new IllegalArgumentException("Customer age must be 18 or above");
        if (balance < 0) throw new IllegalArgumentException("Initial balance cannot be negative");
        if (pin == null || !pin.matches("\\d{4}")) throw new IllegalArgumentException("PIN must be 4 digits");
        
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.accountType = accountType;
        this.status = status;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
        this.transferredToday = 0.0;
        this.lastTransferDate = LocalDate.now();
    }

    public boolean validatePin(String enteredPin) {
        return this.pin != null && this.pin.equals(enteredPin);
    }

    public boolean changePin(String oldPin, String newPin) {
        if (!validatePin(oldPin)) return false;
        if (newPin == null || !newPin.matches("\\d{4}")) return false;
        this.pin = newPin;
        return true;
    }

    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be positive");
        this.balance += amount;
    }

    public void withdraw(double amount, String enteredPin) throws AccountException {
        if (!validatePin(enteredPin)) throw new InvalidPinException("Invalid PIN entered");
        if (!"ACTIVE".equalsIgnoreCase(this.status)) throw new InactiveAccountException("Account is not active");
        if (amount <= 0) throw new InvalidAmountException("Withdrawal amount must be positive");
        processDebit(amount);
    }

    public abstract void processDebit(double amount) throws AccountException;

    // Daily Limit Management
    public void resetDailyLimitIfNewDay() {
        LocalDate today = LocalDate.now();
        if (lastTransferDate == null || !lastTransferDate.isEqual(today)) {
            transferredToday = 0.0;
            lastTransferDate = today;
        }
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    public double getTransferredToday() {
        resetDailyLimitIfNewDay();
        return transferredToday;
    }

    public double getRemainingDailyLimit() {
        resetDailyLimitIfNewDay();
        return Math.max(0.0, dailyLimit - transferredToday);
    }

    public boolean canTransfer(double amount) {
        resetDailyLimitIfNewDay();
        return (transferredToday + amount) <= dailyLimit;
    }

    public void updateDailyTransferred(double amount) {
        resetDailyLimitIfNewDay();
        this.transferredToday += amount;
    }

    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Balance: Rs " + balance);
        System.out.println("Account Type: " + accountType);
        System.out.println("Status: " + status);
        System.out.println("Daily Limit: Rs " + dailyLimit + " (Remaining: Rs " + getRemainingDailyLimit() + ")");
    }

    public String getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public String getStatus() { return status; }
}
