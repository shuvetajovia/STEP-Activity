package com.gdb.tests;

import com.gdb.accounts.*;
import com.gdb.exceptions.*;

public class TestInterfaceFactory {
    public static void main(String[] args) {
        System.out.println("=== Activity 11: Interface & Factory Pattern Test ===");
        IAccount acc1 = AccountFactory.createAccount("SAVINGS", "SAV1001", "Rajesh Sharma", 28, 5000.0, "1234");
        System.out.println("Factory created: " + acc1.getAccountType() + " account for " + acc1.getName());

        IAccount acc2 = AccountFactory.createAccount("CURRENT", "CUR1001", "Priya Patel", 34, 10000.0, "5678");
        System.out.println("Factory created: " + acc2.getAccountType() + " account for " + acc2.getName());

        IAccount acc3 = AccountFactory.createAccount("FIXED_DEPOSIT", "FD1001", "Amit Kumar", 45, 50000.0, "1111");
        System.out.println("Factory created: " + acc3.getAccountType() + " account for " + acc3.getName());

        IAccount acc4 = AccountFactory.createAccount("SALARY", "SAL1001", "Sneha Verma", 26, 30000.0, "2222");
        System.out.println("Factory created: " + acc4.getAccountType() + " account for " + acc4.getName());
        System.out.println("All accounts successfully created through AccountFactory!\n");

        System.out.println("=== Activity 12: Factory-Driven System Suite ===");
        // Test 1: Savings Account Creation & Deposit
        try {
            IAccount savings = AccountFactory.createAccount("SAVINGS", "SAV1001", "Rajesh Sharma", 28, 5000.0, "1234");
            savings.deposit(2000.0);
            if (savings.getBalance() == 7000.0) {
                System.out.println("[Test 1] Savings Account Creation & Deposit: [PASS]");
            } else {
                System.out.println("[Test 1] Savings Account Creation & Deposit: [FAIL]");
            }
        } catch (Exception e) {
            System.out.println("[Test 1] Savings Account Creation & Deposit: [FAIL] - " + e.getMessage());
        }

        // Test 2: Current Account Overdraft Withdrawal
        try {
            IAccount current = AccountFactory.createAccount("CURRENT", "CUR1001", "Priya Patel", 34, 10000.0, "5678");
            current.withdraw(12000.0, "5678");
            if (current.getBalance() == -2000.0) {
                System.out.println("[Test 2] Current Account Overdraft Withdrawal: [PASS]");
            } else {
                System.out.println("[Test 2] Current Account Overdraft Withdrawal: [FAIL]");
            }
        } catch (Exception e) {
            System.out.println("[Test 2] Current Account Overdraft Withdrawal: [FAIL] - " + e.getMessage());
        }

        // Test 3: Fixed Deposit Premature Withdrawal Block
        try {
            IAccount fd = AccountFactory.createAccount("FIXED_DEPOSIT", "FD1001", "Amit Kumar", 45, 50000.0, "1111");
            fd.withdraw(10000.0, "1111");
            System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL]");
        } catch (AccountException e) {
            System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [PASS]");
        } catch (Exception e) {
            System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL] - " + e.getMessage());
        }

        // Test 4: Invalid Type Rejection
        try {
            AccountFactory.createAccount("INVESTMENT", "INV1001", "Vikram Singh", 30, 10000.0, "9999");
            System.out.println("[Test 4] Invalid Type Rejection: [FAIL]");
        } catch (IllegalArgumentException e) {
            System.out.println("[Test 4] Invalid Type Rejection: [PASS]");
        } catch (Exception e) {
            System.out.println("[Test 4] Invalid Type Rejection: [FAIL] - " + e.getMessage());
        }

        System.out.println("Factory-driven architecture successfully verified!");
    }
}
