import java.util.Objects;

/**
 * Abstract base Account class.
 *
 * Activity 7 change: converted to abstract class.
 * Subclasses must implement:
 *   - getMinimumBalance()  – returns the minimum balance rule
 *   - getAccountType()     – returns the account-type string
 *
 * All other behaviour (deposit, withdraw, PIN management, status) is unchanged.
 */
public abstract class Account {

    // ===== Constants =====
    private static final int MIN_AGE = 18;
    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;

    // ===== Fields =====
    private int    accountNumber;
    private String name;
    private int    age;
    private double balance;
    private String status;
    private Integer pin; // null if not set

    // ===== Abstract Methods =====

    /** Returns the minimum balance required for this account type. */
    public abstract double getMinimumBalance();

    /** Returns the account-type label (e.g. "Savings", "Current"). */
    public abstract String getAccountType();

    // ===== Constructor =====

    /**
     * Common constructor used by all subclasses.
     *
     * Validates:
     *   1. Age must be >= 18
     *   2. initialBalance must be >= getMinimumBalance() (delegated to subclass)
     */
    public Account(int accountNumber, String name, int age,
                   double initialBalance)
            throws IllegalArgumentException {

        // Validate age
        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                "Customer must be at least " + MIN_AGE +
                " years old. Provided: " + age
            );
        }

        // Validate minimum balance (rule defined by subclass)
        double minBalance = getMinimumBalance();
        if (initialBalance < minBalance) {
            throw new IllegalArgumentException(
                getAccountType() +
                " account requires minimum balance of ₹" + minBalance +
                ". Provided: ₹" + initialBalance
            );
        }

        // Initialise fields
        this.accountNumber = accountNumber;
        this.name          = name;
        this.age           = age;
        this.balance       = initialBalance;
        this.status        = "Active";
        this.pin           = null;
    }

    // ===== Internal Helpers =====

    /** Throws InactiveAccountException if the account is not Active. */
    protected void validateActive() throws InactiveAccountException {
        if (!"Active".equals(this.status)) {
            throw new InactiveAccountException(
                "Account is inactive. Please reopen the account or contact support."
            );
        }
    }

    // ===== Business Methods =====

    /**
     * Deposits the given amount into the account.
     *
     * @throws InvalidAmountException   if amount <= 0
     * @throws InactiveAccountException if account is inactive
     */
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

    /**
     * Withdraws the given amount from the account after PIN verification.
     *
     * Subclasses (e.g. CurrentAccount) may override this to add overdraft logic.
     *
     * @throws InvalidAmountException          if amount <= 0
     * @throws InsufficientBalanceException    if amount > balance
     * @throws MinimumBalanceViolationException if withdrawal would drop balance below minimum
     * @throws InactiveAccountException        if account is inactive
     * @throws InvalidPinException             if PIN is unset or incorrect
     */
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

    // ===== Account Status Management =====

    /** Closes (deactivates) the account. */
    public void closeAccount() throws IllegalStateException {
        if (!"Active".equals(this.status)) {
            throw new IllegalStateException("Account is already closed.");
        }
        this.status = "Inactive";
    }

    /** Re-activates a previously closed account. */
    public void reopenAccount() throws IllegalStateException {
        if ("Active".equals(this.status)) {
            throw new IllegalStateException("Account is already active.");
        }
        this.status = "Active";
    }

    // ===== PIN Management =====

    /** Sets a 4-digit PIN (1000–9999). */
    public void setPin(int pin) throws IllegalArgumentException {
        if (pin < MIN_PIN || pin > MAX_PIN) {
            throw new IllegalArgumentException(
                "PIN must be a 4-digit number (" + MIN_PIN + "-" + MAX_PIN + ")"
            );
        }
        this.pin = pin;
    }

    /** Returns true if the supplied PIN matches the stored PIN. */
    public boolean verifyPin(int pin) {
        return Objects.equals(this.pin, pin);
    }

    /** Returns true if a PIN has been set on this account. */
    public boolean hasPin() {
        return this.pin != null;
    }

    // ===== Getters =====

    public int     getAccountNumber() { return accountNumber; }
    public String  getName()          { return name; }
    public int     getAge()           { return age; }
    public double  getBalance()       { return balance; }
    public String  getStatus()        { return status; }
    public Integer getPin()           { return pin; }

    // ===== Setters =====

    public void setName(String name) { this.name = name; }

    public void setAge(int age) throws IllegalArgumentException {
        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                "Customer must be at least " + MIN_AGE + " years old. Provided: " + age
            );
        }
        this.age = age;
    }

    /**
     * Protected balance setter for use by subclasses that need to
     * manipulate the balance directly (e.g. overdraft repayment).
     */
    protected void setBalance(double balance) {
        this.balance = balance;
    }
}
