# Object-Oriented Programming Lab Report
# Activity 15: Funds Transfer with Daily Limits

| Student Details | Value |
|---|---|
| **Student Name** | A Shuveta Jovi |
| **Registration Number** | RA2411003011128 |
| **Language** | Java (JDK 8+) |
| **Topic** | Funds Transfer Service & Daily Limits |

---

## 1. Objective & Architecture Overview

Activity 15 implements a secure, transactional funds transfer service (`TransferService`) with real-time daily transfer limits:
1. **TransferService Validation Pipeline**: Performs an 8-step validation sequence including null checks, active status verification for both sender and receiver accounts, PIN authentication, positive amount verification, sender balance sufficiency check, and daily limit compliance.
2. **Daily Limit Tracking & Auto-Reset**: Tracks cumulative daily transfer volume per account, checking against account-type specific limits (Rs 50,000 for Savings, Rs 100,000 for Current/Salary, Rs 0 for Fixed Deposit). Resets daily volume automatically on new calendar dates.
3. **Atomic Execution**: Debits the sender account and credits the recipient account only after all validation rules succeed, updating the sender's daily transferred total atomically.
4. **Decoupled Rules Engine**: Integrates seamlessly with the external `.properties` rules engine from Activity 14 for dynamic policy loading.

---

## 2. External Properties Configuration Files

### `savings.properties`
```properties
# Savings Account Rules
min.balance=10000.0
interest.rate=4.0
daily.transfer.limit.new=50000.0
daily.transfer.limit.standard=100000.0
daily.transfer.limit.premium=200000.0
daily.transfer.limit.privilege=500000.0
```

### `current.properties`
```properties
# Current Account Rules
min.balance=25000.0
interest.rate=0.0
daily.transfer.limit.new=100000.0
daily.transfer.limit.standard=250000.0
daily.transfer.limit.premium=500000.0
daily.transfer.limit.privilege=1000000.0
```

### `salary.properties`
```properties
# Salary Account Rules
min.balance=0.0
interest.rate=3.5
daily.transfer.limit.new=75000.0
daily.transfer.limit.standard=150000.0
daily.transfer.limit.premium=300000.0
daily.transfer.limit.privilege=750000.0
```

### `fixeddeposit.properties`
```properties
# Fixed Deposit Account Rules
min.balance=50000.0
interest.rate=6.5
daily.transfer.limit.new=0.0
daily.transfer.limit.standard=0.0
daily.transfer.limit.premium=0.0
daily.transfer.limit.privilege=0.0
```

---

## 3. Java Source Code Implementation

### `TransferService.java`
```java
package com.gdb.service;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TransferService {

    public TransferService() {
    }

    public void transfer(IAccount from, IAccount to, double amount, int pin)
            throws AccountException {
        // ============================================================
        // 📝 STEP 1: Transfer Funds With Daily Limit
        // ============================================================
        // 1. Null check
        if (from == null || to == null) {
            throw new AccountException("Source and destination accounts are required");
        }

        // 2. Active check
        if (!from.isActive() || !to.isActive()) {
            throw new InactiveAccountException("Both accounts must be active to transfer funds");
        }

        // 3. PIN verify
        if (!from.verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }

        // 4. Balance check
        if (!from.canWithdraw(amount)) {
            throw new InsufficientBalanceException("Insufficient balance for transfer of Rs. " + amount);
        }

        // 5. Daily-limit check
        Account source = (Account) from;
        source.resetDailyTransferIfNeeded();
        if (!source.canTransfer(amount)) {
            throw new AccountException("Daily transfer limit exceeded. Remaining today: Rs. " + source.getRemainingDailyTransferLimit());
        }

        // 6. Debit
        from.withdraw(amount, pin);

        // 7. Credit
        to.deposit(amount);

        // 8. Update total
        source.updateDailyTransferTotal(amount);
    }
}
```

### `Account.java`
```java
package com.gdb.domain;

import com.gdb.exceptions.*;
import java.time.LocalDateTime;

/**
 * Abstract class Account implementing default behavior for IAccount interface.
 * Encapsulates common state fields, customer tenure, and default validation routines.
 */
public abstract class Account implements IAccount {
    protected int accountNumber;
    protected String accountHolderName;
    protected int age;
    protected double balance;
    protected String status;
    protected Integer pin;
    protected String openingDate;
    protected int tenureYears;

    // ============================================================
    // 📝 STEP 2.1: Daily Transfer Tracking Fields
    // ============================================================
    protected double dailyTransferTotal = 0.0;
    protected LocalDateTime lastTransferDate = LocalDateTime.now();

    public Account(int accountNumber, String name, int age, double initialBalance) throws InvalidAgeException {
        this(accountNumber, name, age, initialBalance, 0);
    }

    public Account(int accountNumber, String name, int age, double initialBalance, int tenureYears) throws InvalidAgeException {
        if (age < 18) {
            throw new InvalidAgeException("Customer must be at least 18 years old. Provided: " + age);
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidAgeException("Name cannot be empty");
        }

        this.accountNumber = accountNumber;
        this.accountHolderName = name;
        this.age = age;
        this.balance = initialBalance;
        this.status = "Active";
        this.pin = null;
        this.openingDate = "2026-08-28";
        this.tenureYears = Math.max(0, tenureYears);
    }

    @Override
    public void deposit(double amount) throws InactiveAccountException, InvalidAmountException {
        if (!"Active".equals(status)) {
            throw new InactiveAccountException("Account is inactive.");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive. Provided: Rs. " + amount);
        }
        balance += amount;
    }

    @Override
    public void withdraw(double amount, int pin) throws InactiveAccountException, InvalidPinException, InvalidAmountException, InsufficientBalanceException {
        if (!"Active".equals(status)) {
            throw new InactiveAccountException("Account is inactive.");
        }
        if (this.pin == null) {
            throw new InvalidPinException("PIN not set for this account");
        }
        if (this.pin != pin) {
            throw new InvalidPinException("Incorrect PIN");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive. Provided: Rs. " + amount);
        }
        if (!canWithdraw(amount)) {
            throw new InsufficientBalanceException("Withdrawal not allowed");
        }
        balance -= amount;
    }

    @Override
    public void closeAccount() throws InactiveAccountException {
        if (!"Active".equals(status)) {
            throw new InactiveAccountException("Account is already closed / inactive.");
        }
        status = "Inactive";
    }

    @Override
    public void reopenAccount() throws InactiveAccountException {
        if ("Active".equals(status)) {
            throw new InactiveAccountException("Account is already active.");
        }
        status = "Active";
    }

    @Override
    public void setPin(int pin) throws InvalidPinException {
        if (pin < 1000 || pin > 9999) {
            throw new InvalidPinException("PIN must be a 4-digit number (1000-9999). Provided: " + pin);
        }
        this.pin = pin;
    }

    @Override public boolean verifyPin(int pin) { return this.pin != null && this.pin == pin; }
    @Override public boolean hasPin() { return pin != null; }
    @Override public boolean isActive() { return "Active".equals(status); }

    @Override
    public String getAccountInfo() {
        return "Account #" + accountNumber + " | " + accountHolderName + " (" + age + " yrs, Tenure: " + tenureYears + " yrs) | " +
               getAccountType() + " | Rs. " + balance + " | " + status;
    }

    @Override public int getAccountNumber() { return accountNumber; }
    @Override public String getAccountHolderName() { return accountHolderName; }
    @Override public double getBalance() { return balance; }
    @Override public String getOpeningDate() { return openingDate; }
    @Override public int getTenureYears() { return tenureYears; }
    @Override public void setTenureYears(int tenureYears) { this.tenureYears = Math.max(0, tenureYears); }

    public double getDailyTransferLimit() {
        // 📝 STEP 3: Get Daily Transfer Limit
        return AccountRulesEngine.getInstance().getDailyTransferLimit(getAccountType(), getTenureYears());
    }

    public double getRemainingDailyTransferLimit() {
        // 📝 STEP 4: Get Remaining Daily Limit
        resetDailyTransferIfNeeded();
        return Math.max(0.0, getDailyTransferLimit() - dailyTransferTotal);
    }

    public boolean canTransfer(double amount) {
        // 📝 STEP 5: Check Amount Against Daily Limit
        resetDailyTransferIfNeeded();
        return (dailyTransferTotal + amount) <= getDailyTransferLimit();
    }

    public void updateDailyTransferTotal(double amount) {
        // 📝 STEP 6: Record A Completed Transfer
        resetDailyTransferIfNeeded();
        this.dailyTransferTotal += amount;
        this.lastTransferDate = LocalDateTime.now();
    }

    public void resetDailyTransferIfNeeded() {
        // 📝 STEP 7: Reset The Total On A New Day
        if (lastTransferDate == null || !lastTransferDate.toLocalDate().isEqual(LocalDateTime.now().toLocalDate())) {
            this.dailyTransferTotal = 0.0;
            this.lastTransferDate = LocalDateTime.now();
        }
    }

    public double getDailyTransferTotal() { return dailyTransferTotal; }
    public LocalDateTime getLastTransferDate() { return lastTransferDate; }
}
```

### `IAccount.java`
```java
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
```

### `AccountFactory.java`
```java
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
```

### `SavingsAccount.java`
```java
package com.gdb.domain;

import com.gdb.exceptions.InvalidAgeException;

public class SavingsAccount extends Account {
    public SavingsAccount(int accountNumber, String name, int age, double initialBalance, int tenureYears) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, tenureYears);
    }

    public SavingsAccount(int accountNumber, String name, int age, double initialBalance) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, 0);
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    @Override
    public double getMinimumBalance() {
        return AccountRulesEngine.getInstance().getMinimumBalance("SAVINGS", tenureYears);
    }

    @Override
    public double getInterestRate() {
        return AccountRulesEngine.getInstance().getInterestRate("SAVINGS", tenureYears);
    }

    @Override
    public boolean canWithdraw(double amount) {
        return (this.balance - amount) >= getMinimumBalance();
    }
}
```

### `CurrentAccount.java`
```java
package com.gdb.domain;

import com.gdb.exceptions.InvalidAgeException;

public class CurrentAccount extends Account {
    public CurrentAccount(int accountNumber, String name, int age, double initialBalance, int tenureYears) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, tenureYears);
    }

    public CurrentAccount(int accountNumber, String name, int age, double initialBalance) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, 0);
    }

    @Override
    public String getAccountType() {
        return "Current";
    }

    @Override
    public double getMinimumBalance() {
        return AccountRulesEngine.getInstance().getMinimumBalance("CURRENT", tenureYears);
    }

    @Override
    public double getInterestRate() {
        return AccountRulesEngine.getInstance().getInterestRate("CURRENT", tenureYears);
    }

    @Override
    public boolean canWithdraw(double amount) {
        return (this.balance - amount) >= getMinimumBalance();
    }
}
```

### `FixedDepositAccount.java`
```java
package com.gdb.domain;

import com.gdb.exceptions.InvalidAgeException;

public class FixedDepositAccount extends Account {
    public FixedDepositAccount(int accountNumber, String name, int age, double initialBalance, int tenureYears) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, tenureYears);
    }

    public FixedDepositAccount(int accountNumber, String name, int age, double initialBalance) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, 0);
    }

    @Override
    public String getAccountType() {
        return "FixedDeposit";
    }

    @Override
    public double getMinimumBalance() {
        return AccountRulesEngine.getInstance().getMinimumBalance("FIXEDDEPOSIT", tenureYears);
    }

    @Override
    public double getInterestRate() {
        return AccountRulesEngine.getInstance().getInterestRate("FIXEDDEPOSIT", tenureYears);
    }

    @Override
    public boolean canWithdraw(double amount) {
        return false;
    }
}
```

### `SalaryAccount.java`
```java
package com.gdb.domain;

import com.gdb.exceptions.InvalidAgeException;

public class SalaryAccount extends Account {
    public SalaryAccount(int accountNumber, String name, int age, double initialBalance, int tenureYears) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, tenureYears);
    }

    public SalaryAccount(int accountNumber, String name, int age, double initialBalance) throws InvalidAgeException {
        super(accountNumber, name, age, initialBalance, 0);
    }

    @Override
    public String getAccountType() {
        return "Salary";
    }

    @Override
    public double getMinimumBalance() {
        return AccountRulesEngine.getInstance().getMinimumBalance("SALARY", tenureYears);
    }

    @Override
    public double getInterestRate() {
        return AccountRulesEngine.getInstance().getInterestRate("SALARY", tenureYears);
    }

    @Override
    public boolean canWithdraw(double amount) {
        return (this.balance - amount) >= getMinimumBalance();
    }
}
```

### `AccountRulesEngine.java`
```java
package com.gdb.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AccountRulesEngine {
    private static AccountRulesEngine instance;
    private Map<String, Map<String, Double>> rules = new HashMap<>();
    private LocalDateTime lastLoadTime;

    private AccountRulesEngine() {
        loadRules();
    }

    public static synchronized AccountRulesEngine getInstance() {
        if (instance == null) {
            instance = new AccountRulesEngine();
        }
        return instance;
    }

    public void loadRules() {
        String[] types = {"SAVINGS", "CURRENT", "FIXEDDEPOSIT", "SALARY"};
        String[] tiers = {"new", "standard", "premium", "privilege"};
        for (String t : types) {
            Properties props = AccountRulesPropertiesLoader.loadRules(t.toLowerCase());
            Map<String, Double> map = new HashMap<>();
            
            // Base values
            double baseMinBal = getPropDouble(props, "min.balance", "minBalance", 0.0);
            double baseInterest = getPropDouble(props, "interest.rate", "interestRate", 0.0);
            map.put("min_balance", baseMinBal);
            map.put("interest_rate", baseInterest);

            // Tier specific values
            for (String tier : tiers) {
                map.put("min_balance_" + tier, getPropDouble(props, "min.balance." + tier, "minBalance." + tier, baseMinBal > 0 ? baseMinBal : (t.equals("SAVINGS") ? (tier.equals("new") ? 10000.0 : tier.equals("standard") ? 7500.0 : tier.equals("premium") ? 5000.0 : 2500.0) : baseMinBal)));
                map.put("interest_rate_" + tier, getPropDouble(props, "interest.rate." + tier, "interestRate." + tier, baseInterest > 0 ? baseInterest : (t.equals("SAVINGS") ? (tier.equals("new") ? 2.70 : tier.equals("standard") ? 3.00 : tier.equals("premium") ? 3.50 : 4.00) : baseInterest)));
                
                double defaultLimit = 50000.0;
                if ("CURRENT".equals(t)) defaultLimit = tier.equals("new") ? 100000.0 : tier.equals("standard") ? 250000.0 : tier.equals("premium") ? 500000.0 : 1000000.0;
                else if ("FIXEDDEPOSIT".equals(t)) defaultLimit = 0.0;
                else if ("SALARY".equals(t)) defaultLimit = tier.equals("new") ? 75000.0 : tier.equals("standard") ? 150000.0 : tier.equals("premium") ? 300000.0 : 750000.0;
                else defaultLimit = tier.equals("new") ? 50000.0 : tier.equals("standard") ? 100000.0 : tier.equals("premium") ? 200000.0 : 500000.0;
                
                map.put("limit_" + tier, getPropDouble(props, "daily.transfer.limit." + tier, "daily.limit." + tier, defaultLimit));
            }
            
            rules.put(t, map);
        }
        lastLoadTime = LocalDateTime.now();
    }

    private double getPropDouble(Properties props, String key1, String key2, double defaultVal) {
        String val = props.getProperty(key1);
        if (val == null) {
            val = props.getProperty(key2);
        }
        if (val == null || val.trim().isEmpty()) return defaultVal;
        try {
            return Double.parseDouble(val.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public String getTier(int tenureYears) {
        if (tenureYears <= 1) {
            return "NEW";
        } else if (tenureYears <= 5) {
            return "STANDARD";
        } else if (tenureYears <= 10) {
            return "PREMIUM";
        } else {
            return "PRIVILEGE";
        }
    }

    public String getTenureBucketName(int tenureYears) {
        return getTier(tenureYears);
    }

    public double getMinimumBalance(String accountType, int tenureYears) {
        if (accountType == null) return 0.0;
        String normalized = accountType.toUpperCase().replace(" ", "").replace("_", "");
        String tier = getTier(tenureYears).toLowerCase();
        Map<String, Double> map = rules.get(normalized);
        if (map != null) {
            if (map.containsKey("min_balance_" + tier) && map.get("min_balance_" + tier) > 0) {
                return map.get("min_balance_" + tier);
            }
            if (map.containsKey("min_balance")) {
                return map.get("min_balance");
            }
        }
        return 0.0;
    }

    public double getMinimumBalance(String accountType) {
        return getMinimumBalance(accountType, 0);
    }

    public double getInterestRate(String accountType, int tenureYears) {
        if (accountType == null) return 0.0;
        String normalized = accountType.toUpperCase().replace(" ", "").replace("_", "");
        String tier = getTier(tenureYears).toLowerCase();
        Map<String, Double> map = rules.get(normalized);
        if (map != null) {
            if (map.containsKey("interest_rate_" + tier) && map.get("interest_rate_" + tier) > 0) {
                return map.get("interest_rate_" + tier);
            }
            if (map.containsKey("interest_rate")) {
                return map.get("interest_rate");
            }
        }
        return 0.0;
    }

    public double getInterestRate(String accountType) {
        return getInterestRate(accountType, 0);
    }

    public double getDailyTransferLimit(String accountType, int tenureYears) {
        if (accountType == null) return 0.0;
        String normalized = accountType.toUpperCase().replace(" ", "").replace("_", "");
        String tier = getTier(tenureYears).toLowerCase();
        String key = "limit_" + tier;
        Map<String, Double> map = rules.get(normalized);
        if (map != null && map.containsKey(key)) {
            return map.get(key);
        }
        return 0.0;
    }

    public String getFeatureName(String accountType, int tenureYears) {
        String tier = getTier(tenureYears);
        return tier + " Tier Benefits";
    }

    public Object getAdditionalFeature(String accountType, int tenureYears, String featureKey) {
        if ("overdraftLimit".equalsIgnoreCase(featureKey)) {
            return 25000.0;
        } else if ("lockinMonths".equalsIgnoreCase(featureKey)) {
            return 12;
        } else if ("penaltyPercentage".equalsIgnoreCase(featureKey)) {
            return 1.5;
        } else if ("privilegeStatus".equalsIgnoreCase(featureKey)) {
            return tenureYears >= 2 ? "Privilege" : "Regular";
        } else if ("benefits".equalsIgnoreCase(featureKey)) {
            return "Zero-balance payroll account";
        }
        return null;
    }

    public boolean isLoaded() {
        return !rules.isEmpty();
    }

    public LocalDateTime getLastLoadTime() {
        return lastLoadTime;
    }

    public int getAccountTypeCount() {
        return rules.size();
    }

    public void printAllRules() {
        System.out.println("  Loaded Rules for " + rules.size() + " account types.");
    }

    public void reloadRules() {
        loadRules();
    }
}
```

### `AccountRulesPropertiesLoader.java`
```java
package com.gdb.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AccountRulesPropertiesLoader {
    private Properties properties = new Properties();

    public AccountRulesPropertiesLoader(String configPath) {
        loadProperties(configPath);
    }

    public static Properties loadRules(String accountType) {
        String fileName = accountType.toLowerCase() + ".properties";
        AccountRulesPropertiesLoader loader = new AccountRulesPropertiesLoader(fileName);
        return loader.properties;
    }

    private void loadProperties(String configPath) {
        InputStream is = null;
        try {
            String cleanName = new File(configPath).getName();
            
            // 1. Classpath
            is = getClass().getClassLoader().getResourceAsStream(configPath);
            if (is == null) {
                is = getClass().getClassLoader().getResourceAsStream("config/rules/" + cleanName);
            }
            if (is == null) {
                is = getClass().getClassLoader().getResourceAsStream("main/resources/config/rules/" + cleanName);
            }

            // 2. Filesystem
            if (is == null) {
                String[] candidates = {
                    configPath,
                    cleanName,
                    "config/rules/" + cleanName,
                    "src/main/resources/config/rules/" + cleanName,
                    "../config/rules/" + cleanName,
                    "../../config/rules/" + cleanName,
                    "15/activity15 Java/config/rules/" + cleanName,
                    "15/activity15 Java/src/main/resources/config/rules/" + cleanName,
                    "15/activity15 Python/config/rules/" + cleanName,
                    "activity15/config/rules/" + cleanName,
                    "activity15/src/main/resources/config/rules/" + cleanName
                };
                for (String path : candidates) {
                    File file = new File(path);
                    if (file.exists() && file.isFile()) {
                        is = new FileInputStream(file);
                        break;
                    }
                }
            }

            if (is != null) {
                properties.load(is);
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to load properties for " + configPath + ": " + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public double getDouble(String key, double defaultValue) {
        String val = properties.getProperty(key);
        if (val == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(val.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        String val = properties.getProperty(key);
        if (val == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public Properties getProperties() {
        return properties;
    }
}
```

### `TestTransfer.java`
```java
package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.service.TransferService;
import com.gdb.exceptions.*;

public class TestTransfer {
    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 15 — TRANSFER WITH DAILY LIMITS");
        System.out.println("=".repeat(60));

        TransferService svc = new TransferService();
        AccountRulesEngine engine = AccountRulesEngine.getInstance();

        // ============================================================
        // 📝 STEP 9: Create Two Accounts And Set PIN
        // ============================================================
        Account acc1 = (Account) AccountFactory.createAccount("SAVINGS", 1001, "Rajesh Sharma", 30, 100000);
        Account acc2 = (Account) AccountFactory.createAccount("SAVINGS", 1002, "Priya Patel", 28, 20000);
        acc1.setPin(1234);
        acc2.setPin(5678);

        System.out.println("\n[Setup] Created accounts:");
        System.out.println("  acc1: " + acc1.getAccountInfo());
        System.out.println("  acc2: " + acc2.getAccountInfo());

        // ============================================================
        // 📝 STEP 10: Successful Transfer
        // ============================================================
        System.out.println("\n[Test 1] Successful Transfer of Rs. 5,000:");
        svc.transfer(acc1, acc2, 5000, 1234);
        System.out.println("  Transfer Rs. 5,000: SUCCESS | acc1 = Rs. " + acc1.getBalance() + " | acc2 = Rs. " + acc2.getBalance());

        // ============================================================
        // 📝 STEP 11: Insufficient Balance
        // ============================================================
        System.out.println("\n[Test 2] Insufficient Balance Check (Attempting Rs. 1,00,000):");
        try {
            svc.transfer(acc1, acc2, 100000, 1234);
            System.out.println("  Transfer Rs. 1,00,000: UNEXPECTED SUCCESS [FAIL]");
        } catch (InsufficientBalanceException e) {
            System.out.println("  Transfer Rs. 1,00,000: CAUGHT EXPECTED InsufficientBalanceException [PASS]");
            System.out.println("  Message: " + e.getMessage());
        }

        // ============================================================
        // 📝 STEP 12: Daily Limit Breach
        // ============================================================
        System.out.println("\n[Test 3] Daily Transfer Limit Breach:");
        System.out.println("  acc1 Daily Transfer Limit: Rs. " + acc1.getDailyTransferLimit());
        while (true) {
            try {
                svc.transfer(acc1, acc2, 20000, 1234);
                System.out.println("  Transfer Rs. 20,000: SUCCESS | acc1 = Rs. " + acc1.getBalance() + " | acc2 = Rs. " + acc2.getBalance());
            } catch (AccountException e) {
                System.out.println("  Transfer Rs. 20,000: CAUGHT EXPECTED AccountException [PASS]");
                System.out.println("  Message: " + e.getMessage());
                break;
            }
        }

        // ============================================================
        // 📝 STEP 13: Print Remaining Limit
        // ============================================================
        System.out.println("\n[Test 4] Remaining Daily Limit Verification:");
        System.out.println("  acc1 Daily Transfer Total     : Rs. " + acc1.getDailyTransferTotal());
        System.out.println("  acc1 Remaining Daily Limit    : Rs. " + acc1.getRemainingDailyTransferLimit());
        System.out.println("  Sum (Total + Remaining)       : Rs. " + (acc1.getDailyTransferTotal() + acc1.getRemainingDailyTransferLimit()));

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  ACTIVITY 15 VERIFICATION COMPLETED SUCCESSFULLY!");
        System.out.println("=".repeat(60));
    }
}
```

---

## 4. Test Execution Output

```
============================================================
  ACTIVITY 15 – TRANSFER WITH DAILY LIMITS
============================================================

[Setup] Created accounts:
  acc1: Account #1001 | Rajesh Sharma (30 yrs, Tenure: 0 yrs) | Savings | Rs. 100000.0 | Active
  acc2: Account #1002 | Priya Patel (28 yrs, Tenure: 0 yrs) | Savings | Rs. 20000.0 | Active

[Test 1] Successful Transfer of Rs. 5,000:
  Transfer Rs. 5,000: SUCCESS | acc1 = Rs. 95000.0 | acc2 = Rs. 25000.0

[Test 2] Insufficient Balance Check (Attempting Rs. 1,00,000):
  Transfer Rs. 1,00,000: CAUGHT EXPECTED InsufficientBalanceException [PASS]
  Message: Insufficient balance for transfer of Rs. 100000.0

[Test 3] Daily Transfer Limit Breach:
  acc1 Daily Transfer Limit: Rs. 50000.0
  Transfer Rs. 20,000: SUCCESS | acc1 = Rs. 75000.0 | acc2 = Rs. 45000.0
  Transfer Rs. 20,000: SUCCESS | acc1 = Rs. 55000.0 | acc2 = Rs. 65000.0
  Transfer Rs. 20,000: CAUGHT EXPECTED AccountException [PASS]
  Message: Daily transfer limit exceeded. Remaining today: Rs. 5000.0

[Test 4] Remaining Daily Limit Verification:
  acc1 Daily Transfer Total     : Rs. 45000.0
  acc1 Remaining Daily Limit    : Rs. 5000.0
  Sum (Total + Remaining)       : Rs. 50000.0

============================================================
  ACTIVITY 15 VERIFICATION COMPLETED SUCCESSFULLY!
============================================================
```
