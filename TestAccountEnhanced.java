import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Enhanced Account entity with boolean returns.
 * All display logic is handled here, not in the Account class.
 */
public class TestAccountEnhanced {
    
    private static void printAccountInfo(AccountEnhanced acc) {
        String pinStatus = acc.hasPin() ? "Yes" : "No";
        System.out.println("Account #" + acc.getAccountNumber() + " | " + acc.getName() + 
                           " (" + acc.getAge() + " yrs) | " + acc.getAccountType() + 
                           " | ₹" + acc.getBalance() + " | " + acc.getStatus() + 
                           " | PIN: " + pinStatus);
    }
    
    public static void main(String[] args) {
        List<AccountEnhanced> accounts = new ArrayList<>();

        System.out.println("============================================================");
        System.out.println("ENHANCED ACCOUNT TEST (BOOLEAN RETURNS)");
        System.out.println("============================================================");
        System.out.println();

        // >>> Test 1: Valid Account Creation
        System.out.println(">>> Test 1: Valid Account Creation");
        AccountEnhanced acc1 = new AccountEnhanced(1001, "John Doe", 25, 1000.0, "Savings");
        accounts.add(acc1);
        printAccountInfo(acc1);

        // >>> Test 2: Invalid Age (under 18)
        System.out.println(">>> Test 2: Invalid Age (under 18)");
        System.out.println("Creating account with age 16");
        AccountEnhanced acc2 = new AccountEnhanced(1002, "Young Kid", 16, 500.0, "Savings");
        accounts.add(acc2);
        System.out.println("Age auto-corrected to: " + acc2.getAge());
        printAccountInfo(acc2);

        // >>> Test 3: Invalid Account Type
        System.out.println(">>> Test 3: Invalid Account Type");
        System.out.println("Creating account with type \"Invalid\"");
        AccountEnhanced acc3 = new AccountEnhanced(1003, "Test User", 25, 500.0, "Invalid");
        accounts.add(acc3);
        System.out.println("Account type defaulted to: " + acc3.getAccountType());
        printAccountInfo(acc3);

        // >>> Test 4: Minimum Balance Enforcement on Creation
        System.out.println(">>> Test 4: Minimum Balance Enforcement on Creation");
        System.out.println("Creating Savings account with ₹300 (below minimum)");
        AccountEnhanced acc4 = new AccountEnhanced(1004, "Bob Wilson", 25, 300.0, "Savings");
        accounts.add(acc4);
        System.out.println("Balance auto-corrected to minimum: ₹" + acc4.getBalance());
        printAccountInfo(acc4);

        // >>> Test 5: Withdrawal with Minimum Balance
        System.out.println(">>> Test 5: Withdrawal with Minimum Balance");
        AccountEnhanced acc5 = new AccountEnhanced(1005, "Alice Brown", 30, 1000.0, "Current");
        acc5.setPin(1234); // Set PIN so we can test withdrawal with PIN
        accounts.add(acc5);
        System.out.print("Initial: ");
        printAccountInfo(acc5);
        
        System.out.print("Withdrawing ₹200.0: ");
        if (acc5.withdraw(200.0, 1234)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED");
        }
        System.out.println("New balance: ₹" + acc5.getBalance());
        System.out.print("After withdrawal: ");
        printAccountInfo(acc5);

        System.out.print("Withdrawing ₹900.0 (would leave ₹-100): ");
        if (acc5.withdraw(900.0, 1234)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED (Minimum balance violation)");
        }
        System.out.println("Current balance: ₹" + acc5.getBalance());

        // >>> Test 6: Account Status Management
        System.out.println(">>> Test 6: Account Status Management");
        AccountEnhanced acc6 = new AccountEnhanced(1006, "Charlie Green", 35, 2000.0, "Savings");
        accounts.add(acc6);
        System.out.print("Initial: ");
        printAccountInfo(acc6);

        System.out.print("Closing account: ");
        if (acc6.closeAccount()) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED");
        }
        System.out.print("After close: ");
        printAccountInfo(acc6);
        System.out.println();

        System.out.print("Depositing ₹500.0 to closed account: ");
        if (acc6.deposit(500.0)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED (Account inactive)");
        }

        System.out.print("Reopening account: ");
        if (acc6.reopenAccount()) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED");
        }
        System.out.print("After reopen: ");
        printAccountInfo(acc6);

        // >>> Test 7: PIN Protection
        System.out.println(">>> Test 7: PIN Protection");
        AccountEnhanced acc7 = new AccountEnhanced(1007, "Diana Prince", 28, 1500.0, "Savings");
        accounts.add(acc7);

        // Setting PIN
        System.out.print("Setting PIN 1234: ");
        if (acc7.setPin(1234)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED");
        }

        // Correct PIN
        System.out.print("Withdrawing ₹200.0 with correct PIN (1234): ");
        if (acc7.withdraw(200.0, 1234)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED");
        }
        System.out.println("New balance: ₹" + acc7.getBalance());

        // Incorrect PIN
        System.out.print("Withdrawing ₹100.0 with incorrect PIN (9999): ");
        if (!acc7.hasPin()) {
            System.out.println("FAILED (PIN not set)");
        } else if (!acc7.verifyPin(9999)) {
            System.out.println("FAILED (Incorrect PIN)");
        } else if (acc7.withdraw(100.0, 9999)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED");
        }

        // Test withdrawal with PIN not set (on acc1 which has no PIN set)
        System.out.print("Withdrawing ₹100.0 with PIN not set: ");
        if (!acc1.hasPin()) {
            System.out.println("FAILED (PIN not set)");
        } else if (acc1.withdraw(100.0, 1234)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED");
        }

        // >>> Test 8: All Accounts Summary
        System.out.println(">>> Test 8: All Accounts Summary");
        for (AccountEnhanced acc : accounts) {
            printAccountInfo(acc);
        }

        System.out.println("============================================================");
        System.out.println("ENHANCED TEST COMPLETED!");
        System.out.println("============================================================");
    }
}
