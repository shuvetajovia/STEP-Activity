/**
 * Test class to verify exception throwing and catch blocks for Activity 5.
 */
public class TestAccountExceptions {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" GLOBAL DIGITAL BANK - ACCOUNT EXCEPTIONS TEST");
        System.out.println("==================================================");

        // Test 1: Invalid Age
        try {
            System.out.println(">>> Test 1: Creating account with age 16...");
            new Account(1001, "Kid", 16, 500.0, "Savings");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        // Test 2: Invalid Account Type
        try {
            System.out.println("\n>>> Test 2: Creating account with invalid type...");
            new Account(1002, "User", 25, 500.0, "InvalidType");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        // Test 3: Below Minimum Balance
        try {
            System.out.println("\n>>> Test 3: Creating Current account with ₹500...");
            new Account(1003, "Alice", 25, 500.0, "Current");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        // Create a valid account for further testing
        Account acc = null;
        try {
            System.out.println("\n>>> Test 4: Creating valid Current account with ₹2000...");
            acc = new Account(1004, "John Doe", 25, 2000.0, "Current");
            System.out.println("Account created successfully!");
            printAccountInfo(acc);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create valid account: " + e.getMessage());
            return;
        }

        // Test 5: Deposit Positive vs Invalid
        try {
            System.out.println("\n>>> Test 5: Depositing ₹500...");
            acc.deposit(500.0);
            System.out.println("Success! New Balance: ₹" + acc.getBalance());

            System.out.println("Depositing ₹-100...");
            acc.deposit(-100.0);
        } catch (AccountException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        // Test 6: Withdrawal without PIN set
        try {
            System.out.println("\n>>> Test 6: Withdrawing ₹200 without PIN set...");
            acc.withdraw(200.0, 1234);
        } catch (AccountException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        // Set PIN
        try {
            System.out.println("\nSetting PIN 1234...");
            acc.setPin(1234);
            System.out.println("PIN set successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to set PIN: " + e.getMessage());
        }

        // Test 7: Withdrawal with incorrect PIN
        try {
            System.out.println("\n>>> Test 7: Withdrawing ₹200 with incorrect PIN (9999)...");
            acc.withdraw(200.0, 9999);
        } catch (AccountException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        // Test 8: Withdrawal with correct PIN (Success)
        try {
            System.out.println("\n>>> Test 8: Withdrawing ₹500 with correct PIN (1234)...");
            acc.withdraw(500.0, 1234);
            System.out.println("Success! New Balance: ₹" + acc.getBalance());
        } catch (AccountException e) {
            System.out.println("Unexpected failure: " + e.getMessage());
        }

        // Test 9: Withdrawal exceeding balance
        try {
            System.out.println("\n>>> Test 9: Withdrawing ₹3000 (exceeds balance)...");
            acc.withdraw(3000.0, 1234);
        } catch (AccountException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        // Test 10: Withdrawal violating minimum balance (Current min balance is 1000)
        try {
            System.out.println("\n>>> Test 10: Withdrawing ₹1200 (would leave ₹800, min is 1000)...");
            acc.withdraw(1200.0, 1234);
        } catch (AccountException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        // Test 11: Account Inactivity
        try {
            System.out.println("\n>>> Test 11: Closing account and attempting deposit...");
            acc.closeAccount();
            System.out.println("Account closed successfully.");
            acc.deposit(100.0);
        } catch (IllegalStateException e) {
            System.out.println("State error: " + e.getMessage());
        } catch (AccountException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
        }

        System.out.println("\n==================================================");
        System.out.println(" TESTS COMPLETED SUCCESSFULLY");
        System.out.println("==================================================");
    }

    private static void printAccountInfo(Account acc) {
        System.out.println("Account #" + acc.getAccountNumber() + " | " + acc.getName() + " (" + acc.getAge() + " yrs) | " + acc.getAccountType() + " | ₹" + acc.getBalance() + " | " + acc.getStatus());
    }
}
