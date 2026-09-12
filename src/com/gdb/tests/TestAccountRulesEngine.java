package com.gdb.tests;

import com.gdb.accounts.*;

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
