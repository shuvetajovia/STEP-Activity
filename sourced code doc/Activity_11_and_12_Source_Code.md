# Object-Oriented Programming Lab Report
## Activity 11 & Activity 12: Interface, Factory Pattern & System Verification

**Student Name:** A Shuveta Jovi  
**Registration Number:** RA2411003011128  

---

## 1. Activity 11: Interface & Factory Pattern

### Objective
Decouple banking operations using the `IAccount` interface and implement the Factory Design Pattern in `AccountFactory` for centralized, secure object creation.

### Source Code

#### `IAccount.java`
```java
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
    void displayAccountInfo();
}
```

#### `AbstractAccount.java`
```java
package com.gdb.domain;

import com.gdb.exceptions.*;

public abstract class AbstractAccount implements IAccount {
    protected String accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected String pin;

    public AbstractAccount(String accountNumber, String name, int age, double balance, String accountType, String status, String pin) {
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

    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Balance: Rs " + balance);
        System.out.println("Account Type: " + accountType);
        System.out.println("Status: " + status);
    }

    public String getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public String getStatus() { return status; }
}
```

#### `AccountFactory.java`
```java
package com.gdb.domain;

public class AccountFactory {
    public static IAccount createAccount(String type, String accNum, String name, int age, double balance, String status, String pin) {
        if (type == null) return null;
        switch (type.toUpperCase()) {
            case "SAVINGS":
                return new SavingsAccount(accNum, name, age, balance, status, pin, 1000.0, 4.0);
            case "CURRENT":
                return new CurrentAccount(accNum, name, age, balance, status, pin, 25000.0);
            case "FIXED_DEPOSIT":
            case "FD":
                return new FixedDepositAccount(accNum, name, age, balance, status, pin, 12, 6.5);
            case "SALARY":
                return new SalaryAccount(accNum, name, age, balance, status, pin, "TechCorp");
            default:
                throw new IllegalArgumentException("Unknown account type: " + type);
        }
    }
}
```

#### `TestInterfaceFactory.java`
```java
package com.gdb.tests;

import com.gdb.domain.*;

public class TestInterfaceFactory {
    public static void main(String[] args) {
        System.out.println("=== Activity 11: Interface & Factory Pattern Test ===");

        IAccount acc1 = AccountFactory.createAccount("SAVINGS", "SAV1001", "Rajesh Sharma", 28, 5000.0, "ACTIVE", "1234");
        System.out.println("Factory created: " + acc1.getAccountType() + " account for " + acc1.getName());

        IAccount acc2 = AccountFactory.createAccount("CURRENT", "CUR1001", "Priya Patel", 34, 10000.0, "ACTIVE", "5678");
        System.out.println("Factory created: " + acc2.getAccountType() + " account for " + acc2.getName());

        IAccount acc3 = AccountFactory.createAccount("FIXED_DEPOSIT", "FD1001", "Amit Kumar", 45, 50000.0, "ACTIVE", "1111");
        System.out.println("Factory created: " + acc3.getAccountType() + " account for " + acc3.getName());

        IAccount acc4 = AccountFactory.createAccount("SALARY", "SAL1001", "Sneha Verma", 26, 30000.0, "ACTIVE", "2222");
        System.out.println("Factory created: " + acc4.getAccountType() + " account for " + acc4.getName());

        System.out.println("All accounts successfully created through AccountFactory!");
    }
}
```

### Execution Output
```
=== Activity 11: Interface & Factory Pattern Test ===
Factory created: SAVINGS account for Rajesh Sharma
Factory created: CURRENT account for Priya Patel
Factory created: FIXED_DEPOSIT account for Amit Kumar
Factory created: SALARY account for Sneha Verma
All accounts successfully created through AccountFactory!
```

---

## 2. Activity 12: Factory-Driven Banking System

### Objective
Build a comprehensive test suite where accounts are instantiated solely through `AccountFactory` and transactions are executed exclusively through `IAccount` interface references.

### Source Code

#### `TestInterfaceFactory.java` (Full Suite)
```java
package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestInterfaceFactory {
    public static void main(String[] args) {
        System.out.println("=== Activity 12: Factory-Driven System Suite ===");

        // Step 1, 2, 3: Savings Account Creation & Deposit
        try {
            IAccount savings = AccountFactory.createAccount("SAVINGS", "SAV1001", "Rajesh Sharma", 28, 5000.0, "ACTIVE", "1234");
            savings.deposit(2000.0);
            if (savings.getBalance() == 7000.0) {
                System.out.println("[Test 1] Savings Account Creation & Deposit: [PASS]");
            } else {
                System.out.println("[Test 1] Savings Account Creation & Deposit: [FAIL]");
            }
        } catch (Exception e) {
            System.out.println("[Test 1] Savings Account Creation & Deposit: [FAIL] - " + e.getMessage());
        }

        // Step 4: Current Account Overdraft Withdrawal
        try {
            IAccount current = AccountFactory.createAccount("CURRENT", "CUR1001", "Priya Patel", 34, 10000.0, "ACTIVE", "5678");
            current.withdraw(20000.0, "5678");
            if (current.getBalance() == -10000.0) {
                System.out.println("[Test 2] Current Account Overdraft Withdrawal: [PASS]");
            } else {
                System.out.println("[Test 2] Current Account Overdraft Withdrawal: [FAIL]");
            }
        } catch (Exception e) {
            System.out.println("[Test 2] Current Account Overdraft Withdrawal: [FAIL] - " + e.getMessage());
        }

        // Step 5: Fixed Deposit Premature Withdrawal Block
        try {
            IAccount fd = AccountFactory.createAccount("FIXED_DEPOSIT", "FD1001", "Amit Kumar", 45, 50000.0, "ACTIVE", "1111");
            fd.withdraw(10000.0, "1111");
            System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL]");
        } catch (AccountException e) {
            System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [PASS]");
        } catch (Exception e) {
            System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL] - Unexpected exception: " + e.getMessage());
        }

        // Step 6: Invalid Type Rejection
        try {
            AccountFactory.createAccount("INVESTMENT", "INV1001", "Vikram Singh", 30, 10000.0, "ACTIVE", "9999");
            System.out.println("[Test 4] Invalid Type Rejection: [FAIL]");
        } catch (IllegalArgumentException e) {
            System.out.println("[Test 4] Invalid Type Rejection: [PASS]");
        } catch (Exception e) {
            System.out.println("[Test 4] Invalid Type Rejection: [FAIL] - Unexpected exception: " + e.getMessage());
        }

        System.out.println("Factory-driven architecture successfully verified!");
    }
}
```

### Execution Output
```
=== Activity 12: Factory-Driven System Suite ===
[Test 1] Savings Account Creation & Deposit: [PASS]
[Test 2] Current Account Overdraft Withdrawal: [PASS]
[Test 3] Fixed Deposit Premature Withdrawal Block: [PASS]
[Test 4] Invalid Type Rejection: [PASS]
Factory-driven architecture successfully verified!
```