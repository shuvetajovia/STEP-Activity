package com.gdb.domain;

import com.gdb.exceptions.*;

public interface IAccount {
    String getAccountNumber();
    String getName();
    int getAge();
    double getBalance();
    String getAccountType();
    String getStatus();
    boolean validatePin(String enteredPin);
    boolean changePin(String oldPin, String newPin);
    void deposit(double amount) throws InvalidAmountException;
    void withdraw(double amount, String enteredPin) throws AccountException;
    
    // Daily Transfer Limit Methods (Activity 15)
    double getDailyLimit();
    double getRemainingDailyLimit();
    boolean canTransfer(double amount);
    void updateDailyTransferred(double amount);
    void resetDailyLimitIfNewDay();
    double getTransferredToday();
    
    void displayAccountInfo();
}
