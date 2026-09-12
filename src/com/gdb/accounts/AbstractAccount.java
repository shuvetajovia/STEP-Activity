package com.gdb.accounts;

import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InactiveAccountException;
import com.gdb.exceptions.InvalidAmountException;
import com.gdb.exceptions.InvalidPinException;

/**
 * AbstractAccount — the common base for all bank account types.
 *
 * Implements the Template Method Pattern for the withdrawal process:
 *   withdraw() defines the fixed sequence (validate PIN → check status → validate amount → debit)
 *   processDebit() is abstract, letting each subclass enforce its own debit rule.
 *
 * Activity 9: Abstract Classes & Template Method Pattern
 */
public abstract class AbstractAccount {

    // ===== Constants =====
    private static final int MIN_AGE    = 18;
    private static final int PIN_LENGTH = 4;

    // ===== Fields =====
    private final String accountNumber;
    private       String name;
    private       int    age;
    private       double balance;
    private       String status;   // "ACTIVE" or "INACTIVE"
    private       String pin;      // 4-digit string; null if not set

    // ===== Abstract Methods =====

    /**
     * Returns the account type label (e.g. "SAVINGS", "CURRENT", "SALARY").
     * Subclasses must provide their own label.
     */
    public abstract String getAccountType();

    /**
     * Performs the actual balance deduction according to the account-specific rule.
     * Called only after all common validations pass inside withdraw().
     *
     * @param amount the amount to debit (guaranteed > 0 at this point)
     * @throws AccountException if the account-specific debit rule is violated
     */
    protected abstract void processDebit(double amount) throws AccountException;

    // ===== Constructor =====

    /**
     * Initialises all common account fields with validation.
     *
     * @param accountNumber  unique account identifier
     * @param name           account holder name
     * @param age            account holder age (must be >= 18)
     * @param initialBalance starting balance (must be >= 0)
     * @param pin            4-digit PIN string
     * @throws IllegalArgumentException if any validation fails
     */
    public AbstractAccount(String accountNumber, String name, int age,
                           double initialBalance, String pin)
            throws IllegalArgumentException {

        // Age validation
        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                "Customer must be at least " + MIN_AGE + " years old. Provided: " + age
            );
        }

        // Balance validation
        if (initialBalance < 0) {
            throw new IllegalArgumentException(
                "Initial balance cannot be negative. Provided: " + initialBalance
            );
        }

        // PIN validation
        if (!isValidPin(pin)) {
            throw new IllegalArgumentException(
                "PIN must be exactly " + PIN_LENGTH + " digits. Provided: " + pin
            );
        }

        this.accountNumber = accountNumber;
        this.name          = name;
        this.age           = age;
        this.balance       = initialBalance;
        this.status        = "ACTIVE";
        this.pin           = pin;
    }

    // ===== Template Method – Withdrawal =====

    /**
     * Withdrawal Template Method — enforces the same sequence for every account type:
     *   Step 1: Validate PIN
     *   Step 2: Check account status
     *   Step 3: Validate amount (> 0)
     *   Step 4: Delegate to processDebit() for account-specific debit rule
     *
     * @param amount      amount to withdraw
     * @param enteredPin  PIN entered by the customer
     * @throws InvalidPinException      if PIN is incorrect
     * @throws InactiveAccountException if account is inactive
     * @throws InvalidAmountException   if amount <= 0
     * @throws AccountException         if account-specific debit rule is violated
     */
    public final void withdraw(double amount, String enteredPin) throws AccountException {

        // Step 1 – Validate PIN
        if (!validatePin(enteredPin)) {
            throw new InvalidPinException("Incorrect PIN. Withdrawal denied.");
        }

        // Step 2 – Check account status
        if (!"ACTIVE".equals(this.status)) {
            throw new InactiveAccountException(
                "Account " + accountNumber + " is inactive. Cannot withdraw."
            );
        }

        // Step 3 – Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Withdrawal amount must be greater than 0. Provided: " + amount
            );
        }

        // Step 4 – Account-specific debit rule
        processDebit(amount);
    }

    // ===== Common Business Methods =====

    /**
     * Deposits money into the account.
     *
     * @param amount amount to deposit (must be > 0)
     * @throws InvalidAmountException if amount <= 0
     */
    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(
                "Deposit amount must be greater than 0. Provided: " + amount
            );
        }
        this.balance += amount;
    }

    /**
     * Returns true if the entered PIN matches the stored PIN.
     *
     * @param enteredPin PIN string entered by the customer
     * @return true if correct, false otherwise
     */
    public boolean validatePin(String enteredPin) {
        return this.pin != null && this.pin.equals(enteredPin);
    }

    /**
     * Changes the PIN after verifying the old PIN.
     *
     * @param oldPin current PIN
     * @param newPin new 4-digit PIN
     * @return true if changed successfully, false otherwise
     */
    public boolean changePin(String oldPin, String newPin) {
        if (!validatePin(oldPin)) {
            return false;
        }
        if (!isValidPin(newPin)) {
            return false;
        }
        this.pin = newPin;
        return true;
    }

    /** Displays a formatted summary of the account information. */
    public void displayAccountInfo() {
        System.out.println("-------------------------------");
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Name           : " + name);
        System.out.println("Age            : " + age);
        System.out.println("Balance        : Rs " + balance);
        System.out.println("Account Type   : " + getAccountType());
        System.out.println("Status         : " + status);
        System.out.println("-------------------------------");
    }

    // ===== Status Management =====

    /** Closes (deactivates) the account. */
    public void closeAccount() {
        this.status = "INACTIVE";
    }

    /** Reactivates a closed account. */
    public void reopenAccount() {
        this.status = "ACTIVE";
    }

    // ===== Private Helpers =====

    /** Returns true if the pin is exactly PIN_LENGTH digits. */
    private boolean isValidPin(String pin) {
        if (pin == null || pin.length() != PIN_LENGTH) return false;
        for (char c : pin.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    // ===== Getters =====

    public String getAccountNumber() { return accountNumber; }
    public String getName()          { return name;          }
    public int    getAge()           { return age;           }
    public double getBalance()       { return balance;       }
    public String getStatus()        { return status;        }

    // ===== Protected Setters (for subclass use) =====

    /** Allows subclasses to update the balance after a debit or credit. */
    protected void setBalance(double balance) {
        this.balance = balance;
    }

    // ===== Standard setters =====

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                "Age must be at least " + MIN_AGE + ". Provided: " + age
            );
        }
        this.age = age;
    }
}
