import java.util.ArrayList;
import java.util.List;

/**
 * Test class that handles exceptions thrown by the enhanced Account class.
 */
public class TestAccountExceptions {
    
    private static void printAccountInfo(Account acc) {
        String pinStatus = acc.hasPin() ? "Yes" : "No";
        System.out.println("Account #" + acc.getAccountNumber() + " | " + acc.getName() + 
                           " (" + acc.getAge() + " yrs) | " + acc.getAccountType() + 
                           " | ₹" + acc.getBalance() + " | " + acc.getStatus() + 
                           " | PIN: " + pinStatus);
    }
    
    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();

        System.out.println("============================================================");
        System.out.println("ACCOUNT TEST WITH EXCEPTIONS");
        System.out.println("============================================================");
        System.out.println();

        // >>> Test 1: Valid Account Creation
        System.out.println(">>> Test 1: Valid Account Creation");
        try {
            Account acc1 = new Account(1001, "John Doe", 25, 1000.0, "Savings");
            accounts.add(acc1);
            System.out.print("SUCCESS: ");
            printAccountInfo(acc1);
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 2: Invalid Age (under 18)
        System.out.println(">>> Test 2: Invalid Age (under 18)");
        try {
            new Account(1002, "Young Kid", 16, 500.0, "Savings");
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 3: Invalid Account Type
        System.out.println(">>> Test 3: Invalid Account Type");
        try {
            new Account(1003, "Test User", 25, 500.0, "Invalid");
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 4: Minimum Balance on Creation
        System.out.println(">>> Test 4: Minimum Balance on Creation");
        System.out.println();
        System.out.println("Creating Savings account with ₹300");
        try {
            new Account(1004, "Bob Wilson", 25, 300.0, "Savings");
        } catch (IllegalArgumentException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 5: Valid Deposit and Withdrawal
        System.out.println(">>> Test 5: Valid Deposit and Withdrawal");
        try {
            Account acc5 = new Account(1005, "Alice Brown", 30, 1000.0, "Current");
            accounts.add(acc5);
            System.out.print("Account: ");
            printAccountInfo(acc5);
            
            System.out.print("Setting PIN 1234: ");
            acc5.setPin(1234);
            System.out.println("SUCCESS");
            
            System.out.print("Depositing ₹500.0: ");
            acc5.deposit(500.0);
            System.out.println("SUCCESS");
            System.out.println("Balance after deposit: ₹" + acc5.getBalance());
            
            System.out.print("Withdrawing ₹200.0: ");
            acc5.withdraw(200.0, 1234);
            System.out.println("SUCCESS");
            System.out.println("Balance after withdrawal: ₹" + acc5.getBalance());
            
            printAccountInfo(acc5);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 6: Invalid Deposit (Negative Amount)
        System.out.println(">>> Test 6: Invalid Deposit (Negative Amount)");
        System.out.println("Attempting to deposit ₹-100.0");
        try {
            Account acc5 = accounts.get(accounts.size() - 1); // Get Alice's account
            acc5.deposit(-100.0);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 7: Insufficient Balance
        System.out.println(">>> Test 7: Insufficient Balance");
        try {
            Account acc6 = new Account(1006, "Charlie Green", 35, 500.0, "Savings");
            acc6.setPin(1234); // Silently set PIN so we can test withdrawal
            accounts.add(acc6);
            System.out.print("Account: ");
            printAccountInfo(acc6);
            
            System.out.println("Attempting to withdraw ₹1000.0");
            acc6.withdraw(1000.0, 1234);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 8: Minimum Balance Violation
        System.out.println(">>> Test 8: Minimum Balance Violation");
        try {
            Account acc7 = new Account(1007, "Diana Prince", 28, 1000.0, "Savings");
            acc7.setPin(1234); // Silently set PIN
            accounts.add(acc7);
            System.out.print("Account: ");
            printAccountInfo(acc7);
            
            System.out.println("Attempting to withdraw ₹600.0");
            acc7.withdraw(600.0, 1234);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 9: Inactive Account Operations
        System.out.println(">>> Test 9: Inactive Account Operations");
        try {
            Account acc8 = new Account(1008, "Eve Wilson", 32, 2000.0, "Current");
            accounts.add(acc8);
            System.out.print("Account: ");
            printAccountInfo(acc8);
            
            System.out.print("Closing account: ");
            acc8.closeAccount();
            System.out.println("SUCCESS");
            
            System.out.println("Attempting to deposit ₹100.0 on closed account");
            acc8.deposit(100.0);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        
        // Reopening and depositing
        try {
            Account acc8 = accounts.get(accounts.size() - 1);
            System.out.print("Reopening account: ");
            acc8.reopenAccount();
            System.out.println("SUCCESS");
            
            System.out.print("Depositing ₹100.0 after reopen: ");
            acc8.deposit(100.0);
            System.out.println("SUCCESS");
            System.out.println("Balance after deposit: ₹" + acc8.getBalance());
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 10: PIN Verification
        System.out.println(">>> Test 10: PIN Verification");
        try {
            Account acc9 = new Account(1009, "Frank Miller", 40, 1500.0, "Savings");
            accounts.add(acc9);
            System.out.print("Account: ");
            printAccountInfo(acc9);
            
            System.out.print("Setting PIN 1234: ");
            acc9.setPin(1234);
            System.out.println("SUCCESS");
            
            System.out.print("Withdrawing ₹200.0 with correct PIN: ");
            acc9.withdraw(200.0, 1234);
            System.out.println("SUCCESS");
            System.out.println();
            System.out.println("Balance: ₹" + acc9.getBalance());
            
            System.out.println("Attempting to withdraw ₹100.0 with incorrect PIN (9999)");
            acc9.withdraw(100.0, 9999);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        
        // Attempting to withdraw without PIN set (using acc1 which has no PIN set)
        try {
            Account acc1 = accounts.get(0); // John Doe
            System.out.println("Attempting to withdraw ₹100.0 without PIN set");
            acc1.withdraw(100.0, 1234);
        } catch (AccountException e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }

        // >>> Test 11: All Accounts Summary
        System.out.println(">>> Test 11: All Accounts Summary");
        for (Account acc : accounts) {
            printAccountInfo(acc);
        }

        System.out.println("============================================================");
        System.out.println("TEST COMPLETED!");
        System.out.println("============================================================");
    }
}
