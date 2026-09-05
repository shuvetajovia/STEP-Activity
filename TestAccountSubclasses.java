import java.util.ArrayList;
import java.util.List;

/**
 * Activity 8 – Testing Account Subclasses (SavingsAccount & CurrentAccount).
 *
 * Self-contained single-file version for OnlineGDB.
 * All classes (Account, SavingsAccount, CurrentAccount, exceptions) are
 * embedded below the test class so that the file compiles and runs without
 * any additional source files.
 */
public class TestAccountSubclasses {

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

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

    private static void printAccountInfoNoPin(Account acc) {
        System.out.println(
            "Account #" + acc.getAccountNumber() +
            " | " + acc.getName() +
            " (" + acc.getAge() + " yrs)" +
            " | " + acc.getAccountType() +
            " | ₹" + acc.getBalance() +
            " | " + acc.getStatus()
        );
    }

    private static void printException(Exception e) {
        System.out.println("EXCEPTION: " + e.getMessage());
    }

    // -------------------------------------------------------------------------
    // Main
    // -------------------------------------------------------------------------

    public static void main(String[] args) {

        List<Account> allAccounts = new ArrayList<>();

        System.out.println("============================================================");
        System.out.println("ACCOUNT SUBCLASSES TEST (SAVINGS & CURRENT)");
        System.out.println("============================================================");

        // =====================================================================
        // Test 1: Creating Accounts
        // =====================================================================
        System.out.println();
        System.out.println(">>> Test 1: Creating Accounts");

        SavingsAccount savings1 = null;
        CurrentAccount current1 = null;

        try {
            savings1 = new SavingsAccount(1001, "John Doe", 25, 1000.0);
            allAccounts.add(savings1);
            System.out.print("Savings Account: ");
            printAccountInfo(savings1);
        } catch (IllegalArgumentException e) {
            printException(e);
        }

        try {
            current1 = new CurrentAccount(1002, "Jane Smith", 30, 2000.0);
            allAccounts.add(current1);
            System.out.print("Current Account: ");
            printAccountInfo(current1);
        } catch (IllegalArgumentException e) {
            printException(e);
        }

        // =====================================================================
        // Test 2: Account Type and Minimum Balance
        // =====================================================================
        System.out.println();
        System.out.println(">>> Test 2: Account Type and Minimum Balance");

        if (savings1 != null) {
            System.out.println(
                "Savings Account - Type: " + savings1.getAccountType() +
                ", Minimum Balance: ₹" + savings1.getMinimumBalance()
            );
        }
        if (current1 != null) {
            System.out.println(
                "Current Account - Type: " + current1.getAccountType() +
                ", Minimum Balance: ₹" + current1.getMinimumBalance()
            );
        }

        // =====================================================================
        // Test 3: Savings Account – Interest Calculation
        // =====================================================================
        System.out.println();
        System.out.println(">>> Test 3: Savings Account - Interest Calculation");

        if (savings1 != null) {
            printAccountInfoNoPin(savings1);
            System.out.println("Interest Rate: " + savings1.getInterestRate() + "% per annum");
            System.out.println("Interest for 1 year: ₹"  + savings1.calculateInterest(1));
            System.out.println("Interest for 2 years: ₹" + savings1.calculateInterest(2));
            System.out.println("Interest for 5 years: ₹" + savings1.calculateInterest(5));
            double balanceAfter2Years = savings1.getBalance() + savings1.calculateInterest(2);
            System.out.println("After 2 years with interest: Balance would be ₹" + balanceAfter2Years);
        }

        // =====================================================================
        // Test 4: Current Account – Overdraft Feature
        // =====================================================================
        System.out.println();
        System.out.println(">>> Test 4: Current Account - Overdraft Feature");

        if (current1 != null) {
            printAccountInfoNoPin(current1);
            System.out.println("Overdraft Limit: ₹"     + current1.getOverdraftLimit());
            System.out.println("Available Overdraft: ₹" + current1.getAvailableOverdraft());
            System.out.println("Overdraft Used: ₹"      + current1.getOverdraftUsed());
            System.out.println("Is Using Overdraft: "   + current1.isUsingOverdraft());

            // Set PIN so we can withdraw
            try {
                current1.setPin(4321);
            } catch (IllegalArgumentException e) {
                printException(e);
            }

            // --- Withdraw ₹1500 (goes below min balance, uses overdraft) ---
            System.out.println();
            System.out.println("Withdrawing ₹1500.0 (goes below minimum balance of ₹1000)");
            System.out.println("Balance before: ₹" + current1.getBalance());
            try {
                System.out.print("Withdrawing: ₹1500.0 - ");
                current1.withdraw(1500.0, 4321);
                System.out.println("SUCCESS");
                System.out.println("Balance after: ₹"          + current1.getBalance());
                System.out.println("Overdraft Used: ₹"         + current1.getOverdraftUsed());
                System.out.println("Available Overdraft: ₹"    + current1.getAvailableOverdraft());
                System.out.println("Is Using Overdraft: "      + current1.isUsingOverdraft());
            } catch (Exception e) {
                printException(e);
            }

            // --- Attempt ₹4000 (would exceed available funds) ---
            System.out.println();
            System.out.println("Attempting to withdraw ₹4000.0 (would exceed overdraft)");
            double displayAvail = current1.getBalance() + current1.getAvailableOverdraft();
            System.out.println(
                "Available funds: ₹" + current1.getBalance() +
                " (balance) + ₹" + current1.getAvailableOverdraft() +
                " (overdraft) = ₹" + displayAvail
            );
            try {
                current1.withdraw(4000.0, 4321);
                System.out.println("SUCCESS");
            } catch (Exception e) {
                printException(e);
            }

            // --- Repay overdraft ---
            System.out.println();
            System.out.println("Repaying overdraft of ₹500.0");
            System.out.println("Balance before repayment: ₹"   + current1.getBalance());
            System.out.println("Overdraft Used before: ₹"      + current1.getOverdraftUsed());
            try {
                System.out.print("Repaying ₹500.0 - ");
                current1.repayOverdraft(500.0);
                System.out.println("SUCCESS");
                System.out.println("Balance after repayment: ₹" + current1.getBalance());
                System.out.println("Overdraft Used after: ₹"    + current1.getOverdraftUsed());
                System.out.println("Is Using Overdraft: "        + current1.isUsingOverdraft());
            } catch (Exception e) {
                printException(e);
            }
        }

        // =====================================================================
        // Test 5: Polymorphism – Treating Accounts Uniformly
        // =====================================================================
        System.out.println();
        System.out.println(">>> Test 5: Polymorphism - Treating Accounts Uniformly");

        List<Account> polyAccounts = new ArrayList<>();

        // Re-use already-created accounts
        if (savings1 != null) polyAccounts.add(savings1);
        if (current1 != null) polyAccounts.add(current1);

        // Add two more accounts
        try {
            SavingsAccount acc3 = new SavingsAccount(1003, "Bob Wilson", 35, 500.0);
            polyAccounts.add(acc3);
            allAccounts.add(acc3);
        } catch (IllegalArgumentException e) {
            printException(e);
        }

        try {
            CurrentAccount acc4 = new CurrentAccount(1004, "Alice Brown", 28, 1500.0);
            polyAccounts.add(acc4);
            allAccounts.add(acc4);
        } catch (IllegalArgumentException e) {
            printException(e);
        }

        System.out.println("Processing accounts polymorphically:");
        double totalBalance = 0.0;
        for (Account acc : polyAccounts) {
            System.out.println(
                "Account #" + acc.getAccountNumber() +
                " | " + acc.getName() +
                " (" + acc.getAge() + " yrs)" +
                " | " + acc.getAccountType() +
                " | ₹" + acc.getBalance() +
                " | " + acc.getStatus() +
                " | Type: " + acc.getAccountType() +
                ", Min Balance: ₹" + acc.getMinimumBalance()
            );
            totalBalance += acc.getBalance();
        }
        System.out.println("Total accounts: " + polyAccounts.size());
        System.out.println("Total balance across all accounts: ₹" + totalBalance);

        // =====================================================================
        // Test 6: Validation – Invalid Creation Attempts
        // =====================================================================
        System.out.println();
        System.out.println(">>> Test 6: Validation - Invalid Creation Attempts");

        System.out.println("Attempting to create SavingsAccount with ₹300 (below minimum)");
        try {
            new SavingsAccount(9001, "Bad Balance", 25, 300.0);
        } catch (IllegalArgumentException e) {
            printException(e);
        }

        System.out.println("Attempting to create CurrentAccount with ₹500 (below minimum)");
        try {
            new CurrentAccount(9002, "Bad Balance", 25, 500.0);
        } catch (IllegalArgumentException e) {
            printException(e);
        }

        System.out.println("Attempting to create SavingsAccount with age 16");
        try {
            new SavingsAccount(9003, "Too Young", 16, 1000.0);
        } catch (IllegalArgumentException e) {
            printException(e);
        }

        // =====================================================================
        // Test 7: Savings Account – PIN and Operations
        // =====================================================================
        System.out.println();
        System.out.println(">>> Test 7: Savings Account - PIN and Operations");

        SavingsAccount savings5 = null;
        try {
            savings5 = new SavingsAccount(1005, "Charlie Green", 40, 2000.0);
            allAccounts.add(savings5);
            System.out.print("Savings Account: ");
            printAccountInfo(savings5);
        } catch (IllegalArgumentException e) {
            printException(e);
        }

        if (savings5 != null) {
            // Set PIN
            try {
                System.out.print("Setting PIN 1234: ");
                savings5.setPin(1234);
                System.out.println("SUCCESS");
            } catch (IllegalArgumentException e) {
                printException(e);
            }

            // Deposit
            try {
                System.out.print("Depositing ₹500.0: ");
                savings5.deposit(500.0);
                System.out.println("SUCCESS");
                System.out.println("Balance after deposit: ₹" + savings5.getBalance());
            } catch (Exception e) {
                printException(e);
            }

            // Valid withdrawal
            try {
                System.out.print("Withdrawing ₹300.0 with correct PIN: ");
                savings5.withdraw(300.0, 1234);
                System.out.println("SUCCESS");
                System.out.println("Balance after withdrawal: ₹" + savings5.getBalance());
            } catch (Exception e) {
                printException(e);
            }

            // Withdrawal that would violate minimum balance
            System.out.println("Attempting to withdraw ₹2000.0 (would violate minimum balance)");
            try {
                savings5.withdraw(2000.0, 1234);
            } catch (Exception e) {
                printException(e);
            }
        }

        // =====================================================================
        // Test 8: Current Account – Active Status Operations
        // =====================================================================
        System.out.println();
        System.out.println(">>> Test 8: Current Account - Active Status Operations");

        CurrentAccount current6 = null;
        try {
            current6 = new CurrentAccount(1006, "Diana Prince", 35, 3000.0);
            allAccounts.add(current6);
            System.out.print("Current Account: ");
            printAccountInfo(current6);
        } catch (IllegalArgumentException e) {
            printException(e);
        }

        if (current6 != null) {
            // Close account
            try {
                System.out.print("Closing account: ");
                current6.closeAccount();
                System.out.println("SUCCESS");
            } catch (IllegalStateException e) {
                printException(e);
            }

            // Attempt deposit on closed account
            System.out.println("Attempting to deposit ₹100.0 on closed account");
            try {
                current6.deposit(100.0);
            } catch (Exception e) {
                printException(e);
            }

            // Reopen
            try {
                System.out.print("Reopening account: ");
                current6.reopenAccount();
                System.out.println("SUCCESS");
            } catch (IllegalStateException e) {
                printException(e);
            }

            // Deposit after reopen
            try {
                System.out.print("Depositing ₹100.0 after reopen: ");
                current6.deposit(100.0);
                System.out.println("SUCCESS");
                System.out.println("Balance after deposit: ₹" + current6.getBalance());
            } catch (Exception e) {
                printException(e);
            }
        }

        // =====================================================================
        // Test 9: All Accounts Summary
        // =====================================================================
        System.out.println();
        System.out.println(">>> Test 9: All Accounts Summary");
        for (Account acc : allAccounts) {
            printAccountInfo(acc);
        }

        System.out.println("============================================================");
        System.out.println("TEST COMPLETED!");
        System.out.println("============================================================");
    }
}


// =============================================================================
// ABSTRACT ACCOUNT CLASS
// =============================================================================

abstract class Account {

    private static final int MIN_AGE = 18;
    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;

    private int     accountNumber;
    private String  name;
    private int     age;
    private double  balance;
    private String  status;
    private Integer pin;

    // ----- Abstract Methods -----
    public abstract double getMinimumBalance();
    public abstract String getAccountType();

    // ----- Constructor -----
    public Account(int accountNumber, String name, int age, double initialBalance)
            throws IllegalArgumentException {

        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                "Customer must be at least " + MIN_AGE + " years old. Provided: " + age
            );
        }

        double minBalance = getMinimumBalance();
        if (initialBalance < minBalance) {
            throw new IllegalArgumentException(
                getAccountType() +
                " account requires minimum balance of ₹" + minBalance +
                ". Provided: ₹" + initialBalance
            );
        }

        this.accountNumber = accountNumber;
        this.name          = name;
        this.age           = age;
        this.balance       = initialBalance;
        this.status        = "Active";
        this.pin           = null;
    }

    // ----- Internal Helpers -----
    protected void validateActive() throws InactiveAccountException {
        if (!"Active".equals(this.status)) {
            throw new InactiveAccountException(
                "Account is inactive. Please reopen the account or contact support."
            );
        }
    }

    // ----- Business Methods -----
    public void deposit(double amount)
            throws InvalidAmountException, InactiveAccountException {
        validateActive();
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Deposit amount must be positive. Provided: ₹" + amount
            );
        }
        this.balance += amount;
    }

    public void withdraw(double amount, int pin)
            throws InvalidAmountException,
                   InsufficientBalanceException,
                   MinimumBalanceViolationException,
                   InactiveAccountException,
                   InvalidPinException {
        validateActive();

        if (this.pin == null) {
            throw new InvalidPinException("PIN not set for this account.");
        }
        if (!this.pin.equals(pin)) {
            throw new InvalidPinException("Incorrect PIN.");
        }
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Withdrawal amount must be positive. Provided: ₹" + amount
            );
        }
        if (amount > this.balance) {
            throw new InsufficientBalanceException(
                "Insufficient balance. Available: ₹" + this.balance +
                ", Requested: ₹" + amount
            );
        }

        double minBalance = getMinimumBalance();
        if (this.balance - amount < minBalance) {
            throw new MinimumBalanceViolationException(
                "Cannot withdraw. Minimum balance of ₹" + minBalance +
                " required. Available after withdrawal: ₹" + (this.balance - amount)
            );
        }

        this.balance -= amount;
    }

    public void closeAccount() throws IllegalStateException {
        if (!"Active".equals(this.status)) {
            throw new IllegalStateException("Account is already closed.");
        }
        this.status = "Inactive";
    }

    public void reopenAccount() throws IllegalStateException {
        if ("Active".equals(this.status)) {
            throw new IllegalStateException("Account is already active.");
        }
        this.status = "Active";
    }

    public void setPin(int pin) throws IllegalArgumentException {
        if (pin < MIN_PIN || pin > MAX_PIN) {
            throw new IllegalArgumentException(
                "PIN must be a 4-digit number (" + MIN_PIN + "-" + MAX_PIN + ")"
            );
        }
        this.pin = pin;
    }

    public boolean verifyPin(int pin) {
        if (this.pin == null) return false;
        return this.pin.equals(pin);
    }

    public boolean hasPin()            { return this.pin != null; }
    public int     getAccountNumber()  { return accountNumber; }
    public String  getName()           { return name; }
    public int     getAge()            { return age; }
    public double  getBalance()        { return balance; }
    public String  getStatus()         { return status; }
    public Integer getPin()            { return pin; }

    public void setName(String name)   { this.name = name; }
    public void setAge(int age) {
        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                "Customer must be at least " + MIN_AGE + " years old. Provided: " + age
            );
        }
        this.age = age;
    }

    protected void setBalance(double balance) { this.balance = balance; }
}


// =============================================================================
// SAVINGSACCOUNT
// =============================================================================

class SavingsAccount extends Account {

    private static final double MINIMUM_BALANCE = 500.0;
    private static final String ACCOUNT_TYPE    = "Savings";
    private static final double INTEREST_RATE   = 4.0;

    public SavingsAccount(int accountNumber, String name, int age, double initialBalance)
            throws IllegalArgumentException {
        super(accountNumber, name, age, initialBalance);
    }

    @Override public double getMinimumBalance() { return MINIMUM_BALANCE; }
    @Override public String getAccountType()    { return ACCOUNT_TYPE; }

    public double calculateInterest(int years) {
        if (years < 0) {
            throw new IllegalArgumentException("Years must be non-negative. Provided: " + years);
        }
        return getBalance() * (INTEREST_RATE / 100.0) * years;
    }

    public double getInterestRate() { return INTEREST_RATE; }
}


// =============================================================================
// CURRENTACCOUNT
// =============================================================================

class CurrentAccount extends Account {

    private static final double MINIMUM_BALANCE = 1000.0;
    private static final String ACCOUNT_TYPE    = "Current";
    private static final double OVERDRAFT_LIMIT = 5000.0;

    private double overdraftUsed;

    public CurrentAccount(int accountNumber, String name, int age, double initialBalance)
            throws IllegalArgumentException {
        super(accountNumber, name, age, initialBalance);
        this.overdraftUsed = 0.0;
    }

    @Override public double getMinimumBalance() { return MINIMUM_BALANCE; }
    @Override public String getAccountType()    { return ACCOUNT_TYPE; }

    @Override
    public void withdraw(double amount, int pin)
            throws InvalidAmountException,
                   InsufficientBalanceException,
                   MinimumBalanceViolationException,
                   InactiveAccountException,
                   InvalidPinException {

        validateActive();

        if (!hasPin()) {
            throw new InvalidPinException("PIN not set for this account.");
        }
        if (!verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN.");
        }
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Withdrawal amount must be positive. Provided: ₹" + amount
            );
        }

        double availableOverdraft = OVERDRAFT_LIMIT - overdraftUsed;
        // Usable funds = above-minimum balance + remaining overdraft headroom
        double usableFunds  = (getBalance() - MINIMUM_BALANCE) + availableOverdraft;
        // User-friendly display total = balance + remaining overdraft
        double displayFunds = getBalance() + availableOverdraft;

        // Reject if amount would exhaust or exceed the available overdraft facility
        if (amount >= usableFunds) {
            throw new InsufficientBalanceException(
                "Insufficient funds. Available: ₹" + displayFunds +
                " (including ₹" + OVERDRAFT_LIMIT + " overdraft), Requested: ₹" + amount
            );
        }

        double newBalance = getBalance() - amount;
        if (newBalance < MINIMUM_BALANCE) {
            this.overdraftUsed += (MINIMUM_BALANCE - newBalance);
        }
        setBalance(newBalance);
    }

    public double  getOverdraftLimit()     { return OVERDRAFT_LIMIT; }
    public double  getOverdraftUsed()      { return overdraftUsed; }
    public double  getAvailableOverdraft() { return OVERDRAFT_LIMIT - overdraftUsed; }
    public boolean isUsingOverdraft()      { return overdraftUsed > 0; }

    public void repayOverdraft(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Repayment amount must be positive. Provided: ₹" + amount
            );
        }
        if (amount > overdraftUsed) {
            throw new IllegalArgumentException(
                "Amount exceeds overdraft used (₹" + overdraftUsed + ")"
            );
        }
        this.overdraftUsed -= amount;
        setBalance(getBalance() + amount);
    }
}


// =============================================================================
// EXCEPTION CLASSES
// =============================================================================

class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) { super(message); }
}

class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) { super(message); }
}

class MinimumBalanceViolationException extends Exception {
    public MinimumBalanceViolationException(String message) { super(message); }
}

class InactiveAccountException extends Exception {
    public InactiveAccountException(String message) { super(message); }
}

class InvalidPinException extends Exception {
    public InvalidPinException(String message) { super(message); }
}
