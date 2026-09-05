/**
 * CurrentAccount — a specialised Account with:
 *   - Minimum balance of ₹1000
 *   - Overdraft facility up to ₹5000
 *
 * Activity 7 (Inheritance / Subclasses)
 */
public class CurrentAccount extends Account {

    // ===== Constants =====
    private static final double MINIMUM_BALANCE = 1000.0;
    private static final String ACCOUNT_TYPE    = "Current";
    private static final double OVERDRAFT_LIMIT = 5000.0;

    // ===== Fields =====
    private double overdraftUsed;

    // ===== Constructor =====
    public CurrentAccount(int accountNumber, String name, int age,
                          double initialBalance)
            throws IllegalArgumentException {
        super(accountNumber, name, age, initialBalance);
        this.overdraftUsed = 0.0;
    }

    // ===== Abstract Method Implementations =====

    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    @Override
    public String getAccountType() {
        return ACCOUNT_TYPE;
    }

    // ===== Override Withdraw to Support Overdraft =====

    /**
     * Withdraws the given amount, utilising the overdraft facility if the
     * account balance would fall below the minimum balance.
     *
     * Overdraft availability = OVERDRAFT_LIMIT - overdraftUsed
     * Total available funds  = (balance - minimumBalance) + availableOverdraft
     *
     * @throws InvalidAmountException          if amount <= 0
     * @throws InsufficientBalanceException    if amount exceeds total available funds
     * @throws InactiveAccountException        if account is inactive
     * @throws InvalidPinException             if PIN is unset or incorrect
     */
    @Override
    public void withdraw(double amount, int pin)
            throws InvalidAmountException,
                   InsufficientBalanceException,
                   MinimumBalanceViolationException,
                   InactiveAccountException,
                   InvalidPinException {

        // Delegate active + PIN + amount validation to parent
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

        // Total funds available including overdraft
        double availableOverdraft  = OVERDRAFT_LIMIT - overdraftUsed;
        double availableFunds      = (getBalance() - MINIMUM_BALANCE) + availableOverdraft;

        if (amount > availableFunds) {
            throw new InsufficientBalanceException(
                "Insufficient funds. Available: ₹" + availableFunds +
                " (including ₹" + OVERDRAFT_LIMIT + " overdraft), Requested: ₹" + amount
            );
        }

        // Calculate new balance
        double newBalance = getBalance() - amount;

        // If balance drops below minimum, record how much overdraft is consumed
        if (newBalance < MINIMUM_BALANCE) {
            double additionalOverdraft = MINIMUM_BALANCE - newBalance;
            this.overdraftUsed += additionalOverdraft;
        }

        setBalance(newBalance);
    }

    // ===== Current-Specific Methods =====

    /** Returns the fixed overdraft limit. */
    public double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }

    /** Returns the amount of overdraft currently in use. */
    public double getOverdraftUsed() {
        return overdraftUsed;
    }

    /** Returns the remaining overdraft available. */
    public double getAvailableOverdraft() {
        return OVERDRAFT_LIMIT - overdraftUsed;
    }

    /** Returns true if any overdraft is currently being used. */
    public boolean isUsingOverdraft() {
        return overdraftUsed > 0;
    }

    /**
     * Repays a portion (or all) of the outstanding overdraft.
     *
     * @param amount amount to repay (must be positive and <= overdraftUsed)
     */
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
