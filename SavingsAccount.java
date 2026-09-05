import java.util.Objects;

/**
 * SavingsAccount — a specialised Account with:
 *   - Minimum balance of ₹500
 *   - 4% per-annum interest calculation
 *
 * Activity 7 (Inheritance / Subclasses)
 */
public class SavingsAccount extends Account {

    // ===== Constants =====
    private static final double MINIMUM_BALANCE = 500.0;
    private static final String ACCOUNT_TYPE    = "Savings";
    private static final double INTEREST_RATE   = 4.0; // 4% per annum

    // ===== Constructor =====
    public SavingsAccount(int accountNumber, String name, int age,
                          double initialBalance)
            throws IllegalArgumentException {
        super(accountNumber, name, age, initialBalance);
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

    // ===== Savings-Specific Methods =====

    /**
     * Calculates simple interest for the given number of years
     * based on the current balance.
     *
     * @param years number of years (must be >= 0)
     * @return interest amount in ₹
     */
    public double calculateInterest(int years) {
        if (years < 0) {
            throw new IllegalArgumentException("Years must be non-negative. Provided: " + years);
        }
        return getBalance() * (INTEREST_RATE / 100.0) * years;
    }

    /** Returns the annual interest rate (%). */
    public double getInterestRate() {
        return INTEREST_RATE;
    }
}
