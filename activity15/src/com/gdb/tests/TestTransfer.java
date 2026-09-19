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
