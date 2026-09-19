package com.gdb.domain;

import com.gdb.exceptions.InvalidAccountTypeException;
import com.gdb.exceptions.InvalidAgeException;
import com.gdb.exceptions.MinimumBalanceViolationException;

public class AccountFactory {
    public static IAccount createAccount(String accountType, int accountNumber, String name, int age, double initialBalance, int tenureYears)
            throws InvalidAgeException, InvalidAccountTypeException, MinimumBalanceViolationException {
        if (accountType == null) {
            throw new InvalidAccountTypeException("Account type cannot be null");
        }
        String t = accountType.toUpperCase().replace(" ", "").replace("_", "");
        IAccount acc;
        if ("SAVINGS".equals(t)) {
            acc = new SavingsAccount(accountNumber, name, age, initialBalance, tenureYears);
        } else if ("CURRENT".equals(t)) {
            acc = new CurrentAccount(accountNumber, name, age, initialBalance, tenureYears);
        } else if ("FIXEDDEPOSIT".equals(t) || "FD".equals(t)) {
            acc = new FixedDepositAccount(accountNumber, name, age, initialBalance, tenureYears);
        } else if ("SALARY".equals(t)) {
            acc = new SalaryAccount(accountNumber, name, age, initialBalance, tenureYears);
        } else {
            throw new InvalidAccountTypeException("Unsupported account type: " + accountType);
        }

        if (initialBalance < acc.getMinimumBalance()) {
            throw new MinimumBalanceViolationException(
                String.format("Initial balance Rs. %,.2f is below minimum required Rs. %,.2f",
                    initialBalance, acc.getMinimumBalance())
            );
        }
        return acc;
    }

    public static IAccount createAccount(String accountType, int accountNumber, String name, int age, double initialBalance)
            throws InvalidAgeException, InvalidAccountTypeException, MinimumBalanceViolationException {
        return createAccount(accountType, accountNumber, name, age, initialBalance, 0);
    }
}
