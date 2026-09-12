# Object-Oriented Programming Lab Report
## Activity 13: Rules Engine & Dynamic Account Policy Integration

**Student Name:** A Shuveta Jovi  
**Registration Number:** RA2411003011128  

---

## 1. Activity 13.1: Account Rules Engine (In-Memory Lookup)

### Objective
Create a centralized `AccountRulesEngine` class using in-memory lookup tables (`Map`) to determine minimum balance, interest rates, overdraft limits, and FD rates based on customer relationship tenure.

### Source Code

#### `AccountRulesEngine.java`
```java
package com.gdb.domain;

import java.util.HashMap;
import java.util.Map;

public class AccountRulesEngine {
    private static final Map<String, Double> SAVINGS_MIN_BALANCES = new HashMap<>();
    private static final Map<String, Double> SAVINGS_INTEREST_RATES = new HashMap<>();

    static {
        // Tiers: NEW (0-1 yr), STANDARD (1-3 yrs), PREMIUM (3-5 yrs), PRIVILEGE (5+ yrs)
        SAVINGS_MIN_BALANCES.put("NEW", 10000.0);
        SAVINGS_MIN_BALANCES.put("STANDARD", 7500.0);
        SAVINGS_MIN_BALANCES.put("PREMIUM", 5000.0);
        SAVINGS_MIN_BALANCES.put("PRIVILEGE", 2500.0);

        SAVINGS_INTEREST_RATES.put("NEW", 2.70);
        SAVINGS_INTEREST_RATES.put("STANDARD", 3.00);
        SAVINGS_INTEREST_RATES.put("PREMIUM", 3.50);
        SAVINGS_INTEREST_RATES.put("PRIVILEGE", 4.00);
    }

    public static String getSavingsBucket(int tenureYears) {
        if (tenureYears >= 5) return "PRIVILEGE";
        if (tenureYears >= 3) return "PREMIUM";
        if (tenureYears >= 1) return "STANDARD";
        return "NEW";
    }

    public static double getSavingsMinBalance(int tenureYears) {
        String bucket = getSavingsBucket(tenureYears);
        return SAVINGS_MIN_BALANCES.getOrDefault(bucket, 10000.0);
    }

    public static double getSavingsInterestRate(int tenureYears) {
        String bucket = getSavingsBucket(tenureYears);
        return SAVINGS_INTEREST_RATES.getOrDefault(bucket, 2.70);
    }

    public static double getCurrentOverdraftLimit(double monthlyTurnover) {
        return Math.max(25000.0, monthlyTurnover * 2.5);
    }

    public static double getFDInterestRate(int months) {
        if (months >= 36) return 7.50;
        if (months >= 12) return 6.50;
        return 5.00;
    }
}
```

---

## 2. Activity 13.2: Dynamic Account Rules Integration

### Objective
Connect account domain classes and `AccountFactory` to `AccountRulesEngine` so that minimum balances and interest rates are assigned dynamically at runtime based on customer relationship tenure.

### Source Code

#### `SavingsAccount.java`
```java
package com.gdb.domain;

import com.gdb.exceptions.*;

public class SavingsAccount extends AbstractAccount {
    private int tenureYears;
    private double minBalance;
    private double interestRate;

    public SavingsAccount(String accountNumber, String name, int age, double balance, String status, String pin, int tenureYears) {
        super(accountNumber, name, age, balance, "SAVINGS", status, pin);
        this.tenureYears = tenureYears;
        this.minBalance = AccountRulesEngine.getSavingsMinBalance(tenureYears);
        this.interestRate = AccountRulesEngine.getSavingsInterestRate(tenureYears);
    }

    public SavingsAccount(String accountNumber, String name, int age, double balance, String status, String pin, double minBalance, double interestRate) {
        super(accountNumber, name, age, balance, "SAVINGS", status, pin);
        this.minBalance = minBalance;
        this.interestRate = interestRate;
    }

    @Override
    public void processDebit(double amount) throws AccountException {
        if ((this.balance - amount) < this.minBalance) {
            throw new MinimumBalanceViolationException("Cannot breach minimum balance of Rs " + minBalance);
        }
        this.balance -= amount;
    }

    public void applyInterest() {
        double interest = this.balance * (interestRate / 100.0);
        this.balance += interest;
    }

    public int getTenureYears() {
        return tenureYears;
    }

    public double getMinBalance() { return minBalance; }
    public double getInterestRate() { return interestRate; }
}
```

#### `AccountFactory.java`
```java
package com.gdb.domain;

public class AccountFactory {
    public static IAccount createAccount(String type, String accNum, String name, int age, double balance, String status, String pin) {
        return createAccount(type, accNum, name, age, balance, status, pin, 0);
    }

    public static IAccount createAccount(String type, String accNum, String name, int age, double balance, String status, String pin, int tenureYears) {
        if (type == null) return null;
        switch (type.toUpperCase()) {
            case "SAVINGS":
                return new SavingsAccount(accNum, name, age, balance, status, pin, tenureYears);
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

#### `TestAccountRulesEngine.java`
```java
package com.gdb.tests;

import com.gdb.domain.*;

public class TestAccountRulesEngine {
    public static void main(String[] args) {
        System.out.println("=== Activity 13.1: Hardcoded Rules Engine Test ===");
        int[] tenures = { 0, 2, 4, 6 };
        for (int t : tenures) {
            double minBal = AccountRulesEngine.getSavingsMinBalance(t);
            double rate = AccountRulesEngine.getSavingsInterestRate(t);
            System.out.println("Tenure " + t + " yrs -> Min Balance: Rs " + minBal + " | Interest: " + rate + "%");
        }
        System.out.println("Rules Engine lookup completed successfully!\n");

        System.out.println("=== Activity 13.2: Dynamic Account Rules Test ===");
        SavingsAccount sa = (SavingsAccount) AccountFactory.createAccount("SAVINGS", "SAV1001", "Rajesh Sharma", 28, 50000.0, "ACTIVE", "1234", 4);
        System.out.println("Created Savings Account (Tenure: " + sa.getTenureYears() + " yrs):");
        System.out.println(" -> Min Balance: Rs " + sa.getMinBalance() + " (Dynamically fetched)");
        System.out.println(" -> Interest Rate: " + sa.getInterestRate() + "% (Dynamically fetched)");
        System.out.println("Dynamic rule integration verified!");
    }
}
```

### Execution Output
```
=== Activity 13.1: Hardcoded Rules Engine Test ===
Tenure 0 yrs -> Min Balance: Rs 10000.0 | Interest: 2.7%
Tenure 2 yrs -> Min Balance: Rs 7500.0 | Interest: 3.0%
Tenure 4 yrs -> Min Balance: Rs 5000.0 | Interest: 3.5%
Tenure 6 yrs -> Min Balance: Rs 2500.0 | Interest: 4.0%
Rules Engine lookup completed successfully!

=== Activity 13.2: Dynamic Account Rules Test ===
Created Savings Account (Tenure: 4 yrs):
 -> Min Balance: Rs 5000.0 (Dynamically fetched)
 -> Interest Rate: 3.5% (Dynamically fetched)
Dynamic rule integration verified!
```