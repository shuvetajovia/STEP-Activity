package com.gdb.accounts;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidAmountException;

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
    void displayAccountInfo();
}
