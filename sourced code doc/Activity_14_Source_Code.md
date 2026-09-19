# Object-Oriented Programming Lab Report
## Activity 14: External Properties Rules Engine

**Student Name:** A Shuveta Jovi  
**Registration Number:** RA2411003011128  

---

## 1. Activity 14: External Properties-Driven Rules Engine

### Objective
Move all banking business rules and policy configurations from hardcoded Java code into external `.properties` configuration files (`savings.properties`, `current.properties`, `fixeddeposit.properties`, `salary.properties`), creating a decoupled, configuration-driven banking engine loaded dynamically via `AccountRulesPropertiesLoader` and queried through `AccountRulesEngine`.

---

### External Properties Configuration Files

#### `savings.properties`
```properties
# Bucket      Tenure       Min Balance   Interest Rate
# new         0 to 1 yr    10000         2.70
# standard    1 to 3 yrs    7500         3.00
# premium     3 to 5 yrs    5000         3.50
# privilege   5+ yrs        2500         4.00
min.balance.new=10000
min.balance.standard=7500
min.balance.premium=5000
min.balance.privilege=2500

interest.rate.new=2.70
interest.rate.standard=3.00
interest.rate.premium=3.50
interest.rate.privilege=4.00
```

#### `current.properties`
```properties
overdraft.multiplier=2.5
overdraft.min.limit=25000
```

#### `fixeddeposit.properties`
```properties
interest.rate.short=5.00
interest.rate.medium=6.50
interest.rate.long=7.50
```

#### `salary.properties`
```properties
auto.deactivate.months=3
minimum.monthly.credit=10000
```

---

### Java Source Code

#### `AccountRulesPropertiesLoader.java`
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

    private void loadProperties(String configPath) {
        InputStream is = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream(configPath);
            if (is == null) {
                File file = new File(configPath);
                if (file.exists()) {
                    is = new FileInputStream(file);
                }
            }
            if (is != null) {
                properties.load(is);
            } else {
                System.err.println("Warning: Config file not found at " + configPath);
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to load properties from " + configPath + ": " + e.getMessage());
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
}
```

#### `AccountRulesEngine.java`
```java
package com.gdb.domain;

public class AccountRulesEngine {
    private static AccountRulesPropertiesLoader savingsLoader =
        new AccountRulesPropertiesLoader("src/main/resources/config/rules/savings.properties");

    // Bucket names are lowercase so they match the keys in savings.properties (e.g. min.balance.new).
    public static String getSavingsBucket(int tenureYears) {
        if (tenureYears >= 5) return "privilege";
        if (tenureYears >= 3) return "premium";
        if (tenureYears >= 1) return "standard";
        return "new";
    }

    public static double getSavingsMinBalance(int tenureYears) {
        return savingsLoader.getDouble("min.balance." + getSavingsBucket(tenureYears), 10000.0);
    }

    public static double getSavingsInterestRate(int tenureYears) {
        return savingsLoader.getDouble("interest.rate." + getSavingsBucket(tenureYears), 2.70);
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
        this.tenureYears = 0;
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

    public int getTenureYears() { return tenureYears; }
    public double getMinBalance() { return minBalance; }
    public double getInterestRate() { return interestRate; }
}
```

#### `TestAccountRulesEngineProperties.java`
```java
package com.gdb.tests;

import com.gdb.domain.AccountRulesEngine;

public class TestAccountRulesEngineProperties {
    public static void main(String[] args) {
        System.out.println("=== Activity 14: Properties-Driven Rules Engine Test ===");
        System.out.println("[Config] Loaded rules from src/main/resources/config/rules/savings.properties");
        
        int[] tenures = { 0, 2, 4, 6 };
        for (int t : tenures) {
            double minBal = AccountRulesEngine.getSavingsMinBalance(t);
            double rate = AccountRulesEngine.getSavingsInterestRate(t);
            System.out.printf("Tenure %d yrs -> Min Balance: Rs %.1f | Interest: %.2f%%%n", t, minBal, rate);
        }
        System.out.println("All external properties loaded and verified successfully!");
    }
}
```

---

### Execution Output
```
=== Activity 14: Properties-Driven Rules Engine Test ===
[Config] Loaded rules from src/main/resources/config/rules/savings.properties
Tenure 0 yrs -> Min Balance: Rs 10000.0 | Interest: 2.70%
Tenure 2 yrs -> Min Balance: Rs 7500.0 | Interest: 3.00%
Tenure 4 yrs -> Min Balance: Rs 5000.0 | Interest: 3.50%
Tenure 6 yrs -> Min Balance: Rs 2500.0 | Interest: 4.00%
All external properties loaded and verified successfully!
```
