import java.util.Objects;
import java.util.Set;

/**
 * Enhanced Account entity class with validation and exceptions.
 */
public class Account {
    
    // ===== Constants =====
    private static final double MIN_BALANCE_SAVINGS = 500.0;
    private static final double MIN_BALANCE_CURRENT = 1000.0;
    private static final int MIN_AGE = 18;
    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;
    
    private static final Set<String> VALID_TYPES = Set.of("Savings", "Current");
    
    // ===== Fields =====
    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin; // null if not set
    
    // ===== Constructor =====
    public Account(int accountNumber, String name, int age, 
                   double initialBalance, String accountType) 
                   throws IllegalArgumentException {
        this.accountNumber = accountNumber;
        this.name = name;
        
        // Validate age
        if (age < MIN_AGE) {
            throw new IllegalArgumentException("Customer must be at least " + MIN_AGE + " years old. Provided: " + age);
        }
        this.age = age;
        
        // Validate account type
        if (accountType == null || !VALID_TYPES.contains(accountType)) {
            throw new IllegalArgumentException("Account type must be 'Savings' or 'Current'. Provided: " + accountType);
        }
        this.accountType = accountType;
        
        // Validate minimum balance
        double minBalance = getMinimumBalance();
        if (initialBalance < minBalance) {
            throw new IllegalArgumentException(accountType + " account requires minimum balance of ₹" + minBalance + ". Provided: ₹" + initialBalance);
        }
        this.balance = initialBalance;
        
        this.status = "Active";
        this.pin = null;
    }
    
    // ===== Helper Methods =====
    private double getMinimumBalance() {
        return "Savings".equals(this.accountType) ? MIN_BALANCE_SAVINGS : MIN_BALANCE_CURRENT;
    }
    
    private void validateActive() throws InactiveAccountException {
        if (!"Active".equals(this.status)) {
            throw new InactiveAccountException("Account is inactive. Please reopen the account or contact support.");
        }
    }
    
    // ===== Business Methods =====
    
    public void deposit(double amount) 
            throws InvalidAmountException, InactiveAccountException {
        validateActive();
        
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive. Provided: ₹" + amount);
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
            throw new InvalidPinException("PIN not set for this account");
        }
        
        if (this.pin != pin) {
            throw new InvalidPinException("Incorrect PIN");
        }
        
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive. Provided: ₹" + amount);
        }
        
        if (amount > this.balance) {
            throw new InsufficientBalanceException("Insufficient balance. Available: ₹" + this.balance + ", Requested: ₹" + amount);
        }
        
        double minBalance = getMinimumBalance();
        if (this.balance - amount < minBalance) {
            throw new MinimumBalanceViolationException("Cannot withdraw. Minimum balance of ₹" + minBalance + " required. Available after withdrawal: ₹" + (this.balance - amount));
        }
        
        this.balance -= amount;
    }
    
    // ===== Account Status Management =====
    
    public void closeAccount() throws IllegalStateException {
        if (!"Active".equals(this.status)) {
            throw new IllegalStateException("Account is already inactive");
        }
        this.status = "Inactive";
    }
    
    public void reopenAccount() throws IllegalStateException {
        if ("Active".equals(this.status)) {
            throw new IllegalStateException("Account is already active");
        }
        this.status = "Active";
    }
    
    // ===== PIN Management =====
    
    public void setPin(int pin) throws IllegalArgumentException {
        if (pin < MIN_PIN || pin > MAX_PIN) {
            throw new IllegalArgumentException("PIN must be a 4-digit number");
        }
        this.pin = pin;
    }
    
    public boolean verifyPin(int pin) {
        return Objects.equals(this.pin, pin);
    }
    
    public boolean hasPin() {
        return Objects.nonNull(this.pin);
    }
    
    // ===== Getters =====
    
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
    
    // ===== Setters =====
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAge(int age) throws IllegalArgumentException {
        if (age < MIN_AGE) {
            throw new IllegalArgumentException("Customer must be at least " + MIN_AGE + " years old. Provided: " + age);
        }
        this.age = age;
    }
}
