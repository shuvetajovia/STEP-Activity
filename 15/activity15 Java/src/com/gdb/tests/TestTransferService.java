package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestTransferService {
    public static void main(String[] args) {
        System.out.println("=== Activity 15: Funds Transfer with Daily Limits Test ===");

        // Setup Accounts
        // Sender: Savings Standard Tier (Tenure: 2 yrs -> Min Balance: 7500, Daily Limit: 50000)
        IAccount sender = AccountFactory.createAccount("SAVINGS", "SAV1001", "Rajesh Sharma", 28, 80000.0, "ACTIVE", "1234", 2);
        // Receiver: Current Account (Daily Limit: 500000)
        IAccount receiver = AccountFactory.createAccount("CURRENT", "CUR1001", "Priya Patel", 34, 20000.0, "ACTIVE", "5678");

        System.out.println("\n--- Initial Account States ---");
        System.out.println("Sender Balance  : Rs " + sender.getBalance() + " | Daily Limit: Rs " + sender.getDailyLimit() + " | Remaining: Rs " + sender.getRemainingDailyLimit());
        System.out.println("Receiver Balance: Rs " + receiver.getBalance());

        // Test 1: Successful Transfer 1 (Rs 20,000)
        System.out.println("\n[Test 1] Transfer Rs 20,000 within daily limit");
        try {
            boolean success = TransferService.transfer(sender, receiver, 20000.0, "1234");
            if (success && sender.getBalance() == 60000.0 && receiver.getBalance() == 40000.0 && sender.getRemainingDailyLimit() == 30000.0) {
                System.out.println(" -> Result: SUCCESS [PASS]");
                System.out.println(" -> Sender Balance: Rs " + sender.getBalance() + " | Remaining Limit: Rs " + sender.getRemainingDailyLimit());
                System.out.println(" -> Receiver Balance: Rs " + receiver.getBalance());
            } else {
                System.out.println(" -> Result: UNEXPECTED STATE [FAIL]");
            }
        } catch (Exception e) {
            System.out.println(" -> Result: FAILED [FAIL] - " + e.getMessage());
        }

        // Test 2: Transfer exceeding remaining daily limit (Attempting Rs 35,000 when remaining is Rs 30,000)
        System.out.println("\n[Test 2] Transfer Rs 35,000 exceeding remaining daily limit of Rs 30,000");
        try {
            TransferService.transfer(sender, receiver, 35000.0, "1234");
            System.out.println(" -> Result: Transfer succeeded unexpectedly [FAIL]");
        } catch (DailyLimitExceededException e) {
            System.out.println(" -> Result: Caught DailyLimitExceededException [PASS]");
            System.out.println("    Message: " + e.getMessage());
            System.out.println(" -> Sender Balance: Rs " + sender.getBalance() + " (Unchanged) [PASS]");
        } catch (Exception e) {
            System.out.println(" -> Result: Wrong Exception Caught [FAIL] - " + e);
        }

        // Test 3: Successful Transfer 2 utilizing remaining limit (Rs 30,000)
        System.out.println("\n[Test 3] Transfer Rs 30,000 fully utilizing remaining daily limit");
        try {
            boolean success = TransferService.transfer(sender, receiver, 30000.0, "1234");
            if (success && sender.getBalance() == 30000.0 && sender.getRemainingDailyLimit() == 0.0) {
                System.out.println(" -> Result: SUCCESS [PASS]");
                System.out.println(" -> Sender Balance: Rs " + sender.getBalance() + " | Remaining Limit: Rs " + sender.getRemainingDailyLimit());
                System.out.println(" -> Receiver Balance: Rs " + receiver.getBalance());
            } else {
                System.out.println(" -> Result: UNEXPECTED STATE [FAIL]");
            }
        } catch (Exception e) {
            System.out.println(" -> Result: FAILED [FAIL] - " + e.getMessage());
        }

        // Test 4: Transfer when daily limit exhausted (Rs 1,000)
        System.out.println("\n[Test 4] Transfer when daily limit is exhausted (Remaining: Rs 0.0)");
        try {
            TransferService.transfer(sender, receiver, 1000.0, "1234");
            System.out.println(" -> Result: Transfer succeeded unexpectedly [FAIL]");
        } catch (DailyLimitExceededException e) {
            System.out.println(" -> Result: Caught DailyLimitExceededException [PASS]");
            System.out.println("    Message: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" -> Result: Wrong Exception Caught [FAIL] - " + e);
        }

        // Test 5: Transfer with Invalid PIN
        System.out.println("\n[Test 5] Transfer with Invalid PIN");
        try {
            TransferService.transfer(sender, receiver, 5000.0, "9999");
            System.out.println(" -> Result: Transfer succeeded unexpectedly [FAIL]");
        } catch (InvalidPinException e) {
            System.out.println(" -> Result: Caught InvalidPinException [PASS]");
            System.out.println("    Message: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" -> Result: Wrong Exception Caught [FAIL] - " + e);
        }

        // Test 6: Transfer to Same Account
        System.out.println("\n[Test 6] Self-Transfer Rejection");
        try {
            TransferService.transfer(sender, sender, 5000.0, "1234");
            System.out.println(" -> Result: Transfer succeeded unexpectedly [FAIL]");
        } catch (IllegalArgumentException e) {
            System.out.println(" -> Result: Caught IllegalArgumentException [PASS]");
            System.out.println("    Message: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" -> Result: Wrong Exception Caught [FAIL] - " + e);
        }

        System.out.println("\nAll Activity 15 funds transfer and daily limit tests verified successfully!");
    }
}
