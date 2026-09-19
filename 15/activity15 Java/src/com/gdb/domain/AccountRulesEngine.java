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
