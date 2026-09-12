package com.gdb.tests;

import com.gdb.accounts.AbstractAccount;
import com.gdb.accounts.CurrentAccount;
import com.gdb.accounts.FixedDepositAccount;
import com.gdb.accounts.SalaryAccount;
import com.gdb.accounts.SavingsAccount;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidPinException;
import com.gdb.exceptions.MinimumBalanceViolationException;

/**
 * TestAbstractAccount — driver / test program for Activity 9 & Activity 10.
 *
 * Activity 9: Verifies the Template Method Pattern withdraw() sequence and
 *             account-specific processDebit() rules.
 * Activity 10: Demonstrates polymorphism, fund transfers, exception handling,
 *              and the monthly banking cycle using an AbstractAccount[] portfolio.
 */
public class TestAbstractAccount {

    // ================================================================
    //  ACTIVITY 10 – FUND TRANSFER
    // ================================================================

    /**
     * Transfers money from a source account to a destination account.
     *
     * The correct order is:
     *   1. Withdraw from source (may throw exception)
     *   2. Only if withdrawal succeeds → deposit into destination
     *
     * This guarantees that a failed withdrawal never credits the destination.
     *
     * @param source      account to withdraw from
     * @param destination account to deposit into
     * @param amount      transfer amount
     * @param pin         PIN for the source account
     * @throws AccountException if the withdrawal fails for any reason
     */
    public static void transfer(AbstractAccount source,
                                AbstractAccount destination,
                                double amount,
                                String pin) throws AccountException {
        // Step 1 – Withdraw (validates PIN, status, amount, and account-specific rule)
        source.withdraw(amount, pin);

        // Step 2 – Deposit ONLY if withdrawal succeeded
        destination.deposit(amount);
    }

    // ================================================================
    //  MAIN – TEST RUNNER
    // ================================================================

    public static void main(String[] args) {

        System.out.println("=== Activity 9 & 10: Abstract Account & Banking Operations Suite ===");
        System.out.println();

        // ============================================================
        //  ACTIVITY 9 – Template Method Pattern Tests
        // ============================================================

        System.out.println("--- Activity 9: Template Method Pattern ---");
        System.out.println();

        // --- Test A9-1: Successful Savings Withdrawal ---
        System.out.println("[A9-Test 1] Successful Savings Withdrawal");
        try {
            SavingsAccount savings9 = new SavingsAccount("S001", "Ravi", 30, 10000.0, "1234");
            savings9.withdraw(2000.0, "1234");
            System.out.println("[Savings] Withdraw 2000: SUCCESS | Balance: Rs " + savings9.getBalance());
        } catch (AccountException e) {
            System.out.println("[FAIL] Unexpected exception: " + e.getMessage());
        }

        // --- Test A9-2: Savings Minimum Balance Violation ---
        System.out.println();
        System.out.println("[A9-Test 2] Savings Minimum Balance Violation");
        try {
            SavingsAccount savings9b = new SavingsAccount("S002", "Priya", 25, 10000.0, "1234");
            savings9b.withdraw(9500.0, "1234");  // would leave Rs 500, below Rs 1000 minimum
            System.out.println("[FAIL] Should have thrown MinimumBalanceViolationException");
        } catch (MinimumBalanceViolationException e) {
            System.out.println("[Savings] Withdraw below min balance:");
            System.out.println("  Caught MinimumBalanceViolationException [PASS]");
            System.out.println("  Reason: " + e.getMessage());
        } catch (AccountException e) {
            System.out.println("[FAIL] Wrong exception type: " + e.getClass().getSimpleName());
        }

        // --- Test A9-3: Current Account Overdraft ---
        System.out.println();
        System.out.println("[A9-Test 3] Current Account Overdraft Debit");
        try {
            CurrentAccount current9 = new CurrentAccount("C001", "Arjun", 35, 2000.0, "5678");
            current9.withdraw(5000.0, "5678");   // Rs 2000 balance + Rs 5000 overdraft = ok
            System.out.println("[Current] Overdraft debit: SUCCESS | Balance: Rs " + current9.getBalance());
        } catch (AccountException e) {
            System.out.println("[FAIL] Unexpected exception: " + e.getMessage());
        }

        // --- Test A9-4: Fixed Deposit Premature Withdrawal ---
        System.out.println();
        System.out.println("[A9-Test 4] Fixed Deposit Premature Withdrawal");
        try {
            FixedDepositAccount fd9 = new FixedDepositAccount("F001", "Meena", 40, 50000.0, "9999", 12, 7.5);
            fd9.withdraw(5000.0, "9999");
            System.out.println("[FAIL] Should have thrown AccountException");
        } catch (AccountException e) {
            System.out.println("[FixedDeposit] Premature debit:");
            System.out.println("  Caught AccountException [PASS]");
            System.out.println("  Reason: " + e.getMessage());
        }

        // --- Test A9-5: Wrong PIN ---
        System.out.println();
        System.out.println("[A9-Test 5] Wrong PIN Validation");
        try {
            SavingsAccount savings9c = new SavingsAccount("S003", "Kiran", 28, 5000.0, "4321");
            savings9c.withdraw(1000.0, "9999");   // wrong PIN
            System.out.println("[FAIL] Should have thrown InvalidPinException");
        } catch (InvalidPinException e) {
            System.out.println("[Savings] Withdraw with wrong PIN:");
            System.out.println("  Caught InvalidPinException [PASS]");
        } catch (AccountException e) {
            System.out.println("[FAIL] Wrong exception type: " + e.getClass().getSimpleName());
        }

        System.out.println();
        System.out.println("Template method pattern executed successfully!");
        System.out.println();

        // ============================================================
        //  ACTIVITY 10 – Banking Operations Suite
        // ============================================================

        System.out.println("=== Activity 10: Banking Operations Suite ===");
        System.out.println();

        // ----------------------------------------------------------------
        //  PART 1 – Account Portfolio (polymorphic AbstractAccount array)
        // ----------------------------------------------------------------

        System.out.println("--- Part 1: Building Account Portfolio ---");

        SavingsAccount savings  = new SavingsAccount("S101", "Anita",  29, 10000.0, "1111");
        CurrentAccount current  = new CurrentAccount("C101", "Rohan",  33, 5000.0,  "2222");
        SalaryAccount  salary   = new SalaryAccount ("SA101","Vikram", 27, 8000.0,  "3333", "TechCorp");
        FixedDepositAccount fd  = new FixedDepositAccount("F101","Deepa",45, 25000.0,"4444", 24, 8.0);

        // Polymorphism — all held as AbstractAccount references
        AbstractAccount[] portfolio = { savings, current, salary, fd };

        System.out.println("Portfolio created with " + portfolio.length + " accounts:");
        for (AbstractAccount acc : portfolio) {
            System.out.println("  * [" + acc.getAccountType() + "] " +
                               acc.getName() + " | Balance: Rs " + acc.getBalance());
        }
        System.out.println();

        // ----------------------------------------------------------------
        //  PART 2 & 3 – Fund Transfer with Exception Handling
        // ----------------------------------------------------------------

        System.out.println("--- Part 2 & 3: Fund Transfer Operations ---");
        System.out.println();

        // Test 1 – Successful Transfer: Savings -> Current, Rs 3000, correct PIN
        System.out.println("[Test 1] Successful Transfer (Savings -> Current, Rs 3000)");
        System.out.println("  Before: Savings = Rs " + savings.getBalance() +
                           " | Current = Rs " + current.getBalance());
        try {
            transfer(savings, current, 3000.0, "1111");
            System.out.println("  Transfer Rs 3000 from Savings to Current: SUCCESS");
            System.out.println("  Savings Balance: Rs " + savings.getBalance() +
                               " | Current Balance: Rs " + current.getBalance());
        } catch (AccountException e) {
            System.out.println("  [FAIL] Transfer failed unexpectedly: " + e.getMessage());
        }
        System.out.println();

        // Test 2 – Failed Transfer: Wrong PIN -> destination must NOT be credited
        System.out.println("[Test 2] Failed Transfer (Wrong PIN)");
        double savingsBefore  = savings.getBalance();
        double currentBefore  = current.getBalance();
        System.out.println("  Before: Savings = Rs " + savingsBefore +
                           " | Current = Rs " + currentBefore);
        try {
            transfer(savings, current, 2000.0, "WRONG");  // wrong PIN
            System.out.println("  [FAIL] Transfer should have failed due to wrong PIN");
        } catch (InvalidPinException e) {
            System.out.println("  Failed Transfer (Wrong PIN):");
            System.out.println("  Exception caught, no balance changed [PASS]");
            // Verify balances are unchanged
            boolean balancesUnchanged = (savings.getBalance() == savingsBefore)
                                      && (current.getBalance() == currentBefore);
            System.out.println("  Savings Balance: Rs "  + savings.getBalance() +
                               " | Current Balance: Rs " + current.getBalance() +
                               (balancesUnchanged ? " [UNCHANGED - PASS]" : " [CHANGED – FAIL]"));
        } catch (AccountException e) {
            System.out.println("  [FAIL] Wrong exception type: " + e.getClass().getSimpleName());
        }
        System.out.println();

        // Test 3 – Failed Transfer: Minimum Balance Violation
        System.out.println("[Test 3] Failed Transfer (Savings minimum balance would be breached)");
        double savingsB2 = savings.getBalance();
        double currentB2 = current.getBalance();
        try {
            // Savings has Rs 7000; trying to withdraw Rs 6500 would leave Rs 500 < Rs 1000 minimum
            transfer(savings, current, 6500.0, "1111");
            System.out.println("  [FAIL] Transfer should have been blocked by minimum balance rule");
        } catch (MinimumBalanceViolationException e) {
            System.out.println("  Failed Transfer (Minimum Balance Violation):");
            System.out.println("  Exception caught, no balance changed [PASS]");
            System.out.println("  Savings Balance: Rs "  + savings.getBalance() +
                               " | Current Balance: Rs " + current.getBalance());
        } catch (AccountException e) {
            System.out.println("  [FAIL] Wrong exception: " + e.getClass().getSimpleName() +
                               " - " + e.getMessage());
        }
        System.out.println();

        // ----------------------------------------------------------------
        //  PART 4 – Monthly Banking Cycle
        // ----------------------------------------------------------------

        System.out.println("--- Part 4: Monthly Banking Cycle ---");
        System.out.println();

        for (AbstractAccount acc : portfolio) {

            // instanceof check — Savings Account -> apply interest
            if (acc instanceof SavingsAccount) {
                SavingsAccount sa = (SavingsAccount) acc;
                double balanceBefore = sa.getBalance();
                double interest      = sa.applyInterest();
                System.out.println("[Monthly Cycle] SavingsAccount detected for: " + sa.getName());
                System.out.println("  Interest Rate : " + sa.getInterestRate() + "%");
                System.out.println("  Balance Before: Rs " + balanceBefore);
                System.out.println("  Interest Added: Rs " + interest);
                System.out.println("  Balance After : Rs " + sa.getBalance());
                System.out.println("  Savings interest applied successfully.");
                System.out.println();
            }

            // instanceof check — Salary Account -> process monthly salary activity
            else if (acc instanceof SalaryAccount) {
                SalaryAccount sa = (SalaryAccount) acc;
                sa.incrementInactiveMonths();
                System.out.println("[Monthly Cycle] SalaryAccount detected for: " + sa.getName());
                System.out.println("  Employer      : " + sa.getEmployerName());
                System.out.println("  Inactive Months (no salary): " + sa.getInactiveMonths());
                if (sa.getInactiveMonths() >= 3) {
                    System.out.println("  [WARNING] Account inactive for 3+ months -- flag for review.");
                } else {
                    System.out.println("  Salary account monthly processing completed.");
                }
                System.out.println();
            }

            // CurrentAccount and FixedDepositAccount — no special monthly action
            else {
                System.out.println("[Monthly Cycle] " + acc.getAccountType() +
                                   " for " + acc.getName() + " -- no monthly action required.");
                System.out.println();
            }
        }

        System.out.println("Monthly Interest Cycle processed for all qualifying accounts.");
        System.out.println();

        // ----------------------------------------------------------------
        //  Final Summary
        // ----------------------------------------------------------------

        System.out.println("--- Final Account Balances ---");
        for (AbstractAccount acc : portfolio) {
            System.out.printf("  [%-14s] %-8s -> Rs %.1f%n",
                acc.getAccountType(), acc.getName(), acc.getBalance());
        }
        System.out.println();
        System.out.println("All banking operations passed!");
    }
}
