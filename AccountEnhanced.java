import java.util.Objects;
import java.util.Set;

public class AccountEnhanced {
    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin;

    private static final Set<String> VALID_ACCOUNT_TYPES = Set.of("Savings", "Current");

    public AccountEnhanced(int accountNumber, String name, int age, double initialBalance, String accountType) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = Math.max(age, 18);
        this.accountType = VALID_ACCOUNT_TYPES.contains(accountType) ? accountType : "Savings";
        
        double minBalance = "Savings".equals(this.accountType) ? 500.0 : 1000.0;
        this.balance = Math.max(initialBalance, minBalance);
        
        this.status = "Active";
        this.pin = null;
    }

    public boolean deposit(double amount) {
        if (!"Active".equals(status) || amount <= 0) {
            return false;
        }
        this.balance += amount;
        return true;
    }

    public boolean withdraw(double amount, int pin) {
        if (!"Active".equals(status) || !verifyPin(pin) || amount <= 0) {
            return false;
        }
        
        double minBalance = "Savings".equals(this.accountType) ? 500.0 : 1000.0;
        if (this.balance - amount < minBalance) {
            return false;
        }
        
        this.balance -= amount;
        return true;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = Math.max(age, 18); // Use Math.max here too for consistency
    }

    public boolean closeAccount() {
        if ("Inactive".equals(this.status)) {
            return false;
        }
        this.status = "Inactive";
        return true;
    }

    public boolean reopenAccount() {
        if ("Active".equals(this.status)) {
            return false;
        }
        this.status = "Active";
        return true;
    }

    public boolean setPin(int pin) {
        if (pin >= 1000 && pin <= 9999) { // Ensure it is exactly a 4-digit positive integer
            this.pin = pin;
            return true;
        }
        return false;
    }

    public boolean verifyPin(int pin) {
        return Objects.equals(this.pin, pin); // Elegant null-safe comparison
    }

    public boolean hasPin() {
        return Objects.nonNull(this.pin); // Elegant built-in null check
    }
}
