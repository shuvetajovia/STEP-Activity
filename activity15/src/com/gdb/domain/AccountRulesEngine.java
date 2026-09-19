package com.gdb.domain;

public class AccountRulesEngine {
    private static AccountRulesPropertiesLoader savingsLoader =
        new AccountRulesPropertiesLoader("src/main/resources/config/rules/savings.properties");
    private static AccountRulesPropertiesLoader currentLoader =
        new AccountRulesPropertiesLoader("src/main/resources/config/rules/current.properties");
    private static AccountRulesPropertiesLoader salaryLoader =
        new AccountRulesPropertiesLoader("src/main/resources/config/rules/salary.properties");
    private static AccountRulesPropertiesLoader fdLoader =
        new AccountRulesPropertiesLoader("src/main/resources/config/rules/fixeddeposit.properties");

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

    public static double getSavingsDailyLimit(int tenureYears) {
        return savingsLoader.getDouble("daily.limit." + getSavingsBucket(tenureYears), 25000.0);
    }

    public static double getCurrentOverdraftLimit(double monthlyTurnover) {
        return Math.max(25000.0, monthlyTurnover * 2.5);
    }

    public static double getCurrentDailyLimit() {
        return currentLoader.getDouble("daily.limit", 500000.0);
    }

    public static double getSalaryDailyLimit() {
        return salaryLoader.getDouble("daily.limit", 100000.0);
    }

    public static double getFDDailyLimit() {
        return fdLoader.getDouble("daily.limit", 0.0);
    }

    public static double getFDInterestRate(int months) {
        if (months >= 36) return 7.50;
        if (months >= 12) return 6.50;
        return 5.00;
    }

    public static double getDailyLimit(String accountType, int tenureYears) {
        if (accountType == null) return 25000.0;
        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                return getSavingsDailyLimit(tenureYears);
            case "CURRENT":
                return getCurrentDailyLimit();
            case "SALARY":
                return getSalaryDailyLimit();
            case "FIXED_DEPOSIT":
            case "FD":
                return getFDDailyLimit();
            default:
                return 25000.0;
        }
    }
}
