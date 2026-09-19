package com.gdb.domain;

import com.gdb.exceptions.*;

public interface IAccount {
    void deposit(double amount) throws InactiveAccountException, InvalidAmountException;
    void withdraw(double amount, int pin) throws InactiveAccountException, InvalidPinException, InvalidAmountException, InsufficientBalanceException;
    void closeAccount() throws InactiveAccountException;
    void reopenAccount() throws InactiveAccountException;
    void setPin(int pin) throws InvalidPinException;
    boolean verifyPin(int pin);
    boolean hasPin();
    boolean isActive();
    boolean canWithdraw(double amount);
    String getAccountInfo();
    int getAccountNumber();
    String getAccountHolderName();
    double getBalance();
    String getOpeningDate();
    int getTenureYears();
    void setTenureYears(int tenureYears);
    String getAccountType();
    double getMinimumBalance();
    double getInterestRate();
}
