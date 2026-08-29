import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Account with exceptions.
 * Compatible with OnlineGDB.
 */
public class TestAccountExceptions {

    private static void printAccountInfo(Account acc) {
        String pinStatus = acc.hasPin() ? "Yes" : "No";

        System.out.println(
            "Account #" + acc.getAccountNumber() +
            " | " + acc.getName() +
            " (" + acc.getAge() + " yrs)" +
            " | " + acc.getAccountType() +
            " | ₹" + acc.getBalance() +
            " | " + acc.getStatus() +
            " | PIN: " + pinStatus
        );
    }

    private static void printException(Exception e) {
        // Modified to match the expected output format exactly
        System.out.println("EXCEPTION: " + e.getMessage());
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
            printException(e);
        }

        // >>> Test 2: Invalid Age (under 18)
        System.out.println(">>> Test 2: Invalid Age (under 18)");
        try {
            new Account(1002, "Young Kid", 16, 500.0, "Savings");
        } catch (IllegalArgumentException e) {
            printException(e);
        }

        // >>> Test 3: Invalid Account Type
        System.out.println(">>> Test 3: Invalid Account Type");
        try {
            new Account(1003, "Test User", 25, 500.0, "Invalid");
        } catch (IllegalArgumentException e) {
            printException(e);
        }

        // >>> Test 4: Minimum Balance on Creation
        System.out.println(">>> Test 4: Minimum Balance on Creation");
        System.out.println();
        System.out.println("Creating Savings account with ₹300");
        try {
            new Account(1004, "Bob Wilson", 25, 300.0, "Savings");
        } catch (IllegalArgumentException e) {
            printException(e);
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
            printException(e);
        }

        // >>> Test 6: Invalid Deposit (Negative Amount)
        System.out.println(">>> Test 6: Invalid Deposit (Negative Amount)");
        System.out.println("Attempting to deposit ₹-100.0");
        try {
            // Find Alice's account from list
            Account acc5 = accounts.stream().filter(a -> a.getAccountNumber() == 1005).findFirst().orElse(null);
            if (acc5 != null) {
                acc5.deposit(-100.0);
            }
        } catch (Exception e) {
            printException(e);
        }

        // >>> Test 7: Insufficient Balance
        System.out.println(">>> Test 7: Insufficient Balance");
        try {
            Account acc6 = new Account(1006, "Charlie Green", 35, 500.0, "Savings");
            acc6.setPin(1234);
            accounts.add(acc6);
            System.out.print("Account: ");
            printAccountInfo(acc6);
            
            System.out.println("Attempting to withdraw ₹1000.0");
            acc6.withdraw(1000.0, 1234);
        } catch (Exception e) {
            printException(e);
        }

        // >>> Test 8: Minimum Balance Violation
        System.out.println(">>> Test 8: Minimum Balance Violation");
        try {
            Account acc7 = new Account(1007, "Diana Prince", 28, 1000.0, "Savings");
            acc7.setPin(1234);
            accounts.add(acc7);
            System.out.print("Account: ");
            printAccountInfo(acc7);
            
            System.out.println("Attempting to withdraw ₹600.0");
            acc7.withdraw(600.0, 1234);
        } catch (Exception e) {
            printException(e);
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
        } catch (Exception e) {
            printException(e);
        }
        
        // Reopen and deposit successfully
        try {
            Account acc8 = accounts.stream().filter(a -> a.getAccountNumber() == 1008).findFirst().orElse(null);
            if (acc8 != null) {
                System.out.print("Reopening account: ");
                acc8.reopenAccount();
                System.out.println("SUCCESS");
                
                System.out.print("Depositing ₹100.0 after reopen: ");
                acc8.deposit(100.0);
                System.out.println("SUCCESS");
                System.out.println("Balance after deposit: ₹" + acc8.getBalance());
            }
        } catch (Exception e) {
            printException(e);
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
        } catch (Exception e) {
            printException(e);
        }
        
        // Attempting to withdraw without PIN set (using acc1 which has no PIN set)
        try {
            Account acc1 = accounts.get(0);
            System.out.println("Attempting to withdraw ₹100.0 without PIN set");
            acc1.withdraw(100.0, 1234);
        } catch (Exception e) {
            printException(e);
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


/*
 * ============================================================
 * ACCOUNT CLASS
 * ============================================================
 */

class Account {

    private static final double MIN_BALANCE_SAVINGS = 500.0;
    private static final double MIN_BALANCE_CURRENT = 1000.0;

    private static final int MIN_AGE = 18;
    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;

    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin;


    public Account(
            int accountNumber,
            String name,
            int age,
            double initialBalance,
            String accountType)
            throws IllegalArgumentException {

        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                "Customer must be at least " +
                MIN_AGE +
                " years old. Provided: " +
                age
            );
        }


        if (!"Savings".equals(accountType)
                && !"Current".equals(accountType)) {

            throw new IllegalArgumentException(
                "Account type must be 'Savings' or 'Current'. " +
                "Provided: " +
                accountType
            );
        }


        double minBalance =
            getMinimumBalance(accountType);


        if (initialBalance < minBalance) {

            throw new IllegalArgumentException(
                accountType +
                " account requires minimum balance of ₹" +
                minBalance +
                ". Provided: ₹" +
                initialBalance
            );
        }


        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = null;
    }


    private double getMinimumBalance(String accountType) {

        if ("Savings".equals(accountType)) {
            return MIN_BALANCE_SAVINGS;
        }

        return MIN_BALANCE_CURRENT;
    }


    private double getMinimumBalance() {
        return getMinimumBalance(this.accountType);
    }


    private void validateActive()
            throws InactiveAccountException {

        if (!"Active".equals(this.status)) {

            throw new InactiveAccountException(
                "Account is inactive. " +
                "Please reopen the account or contact support."
            );
        }
    }


    public void deposit(double amount)
            throws InvalidAmountException,
                   InactiveAccountException {

        validateActive();


        if (amount <= 0) {

            throw new InvalidAmountException(
                "Deposit amount must be positive. " +
                "Provided: ₹" +
                amount
            );
        }


        this.balance += amount;
    }


    public void withdraw(
            double amount,
            int pin)
            throws InvalidAmountException,
                   InsufficientBalanceException,
                   MinimumBalanceViolationException,
                   InactiveAccountException,
                   InvalidPinException {

        validateActive();


        if (this.pin == null) {

            throw new InvalidPinException(
                "PIN not set for this account."
            );
        }


        if (!this.pin.equals(pin)) {

            throw new InvalidPinException(
                "Incorrect PIN."
            );
        }


        if (amount <= 0) {

            throw new InvalidAmountException(
                "Withdrawal amount must be positive. " +
                "Provided: ₹" +
                amount
            );
        }


        if (amount > this.balance) {

            throw new InsufficientBalanceException(
                "Insufficient balance. Available: ₹" +
                this.balance +
                ", Requested: ₹" +
                amount
            );
        }


        double minBalance =
            getMinimumBalance();


        if (this.balance - amount < minBalance) {

            throw new MinimumBalanceViolationException(
                "Cannot withdraw. Minimum balance of ₹" +
                minBalance +
                " required. " +
                "Available after withdrawal: ₹" +
                (this.balance - amount)
            );
        }


        this.balance -= amount;
    }


    public void closeAccount()
            throws IllegalStateException {

        if (!"Active".equals(this.status)) {

            throw new IllegalStateException(
                "Account is already closed."
            );
        }

        this.status = "Inactive";
    }


    public void reopenAccount()
            throws IllegalStateException {

        if ("Active".equals(this.status)) {

            throw new IllegalStateException(
                "Account is already active."
            );
        }

        this.status = "Active";
    }


    public void setPin(int pin)
            throws IllegalArgumentException {

        if (pin < MIN_PIN || pin > MAX_PIN) {

            throw new IllegalArgumentException(
                "PIN must be a 4-digit number (" +
                MIN_PIN +
                "-" +
                MAX_PIN +
                ")"
            );
        }

        this.pin = pin;
    }


    public boolean verifyPin(int pin) {

        if (this.pin == null) {
            return false;
        }

        return this.pin.equals(pin);
    }


    public boolean hasPin() {
        return this.pin != null;
    }


    public int getAccountNumber() {
        return accountNumber;
    }


    public String getName() {
        return name;
    }


    public int getAge() {
        return age;
    }


    public double getBalance() {
        return balance;
    }


    public String getAccountType() {
        return accountType;
    }


    public String getStatus() {
        return status;
    }


    public Integer getPin() {
        return pin;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setAge(int age) {

        if (age < MIN_AGE) {

            throw new IllegalArgumentException(
                "Customer must be at least " +
                MIN_AGE +
                " years old."
            );
        }

        this.age = age;
    }
}


/*
 * ============================================================
 * EXCEPTION CLASSES
 * ============================================================
 */

class InvalidAmountException extends Exception {

    public InvalidAmountException(String message) {
        super(message);
    }
}


class InsufficientBalanceException extends Exception {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}


class MinimumBalanceViolationException extends Exception {

    public MinimumBalanceViolationException(String message) {
        super(message);
    }
}


class InactiveAccountException extends Exception {

    public InactiveAccountException(String message) {
        super(message);
    }
}


class InvalidPinException extends Exception {

    public InvalidPinException(String message) {
        super(message);
    }
}
