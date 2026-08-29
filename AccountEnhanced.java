public class AccountEnhanced {
    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin;

    public AccountEnhanced(int accountNumber, String name, int age, double initialBalance, String accountType) {
        this.accountNumber = accountNumber;
        this.name = name;
        
        if (age < 18) {
            this.age = 18;
        } else {
            this.age = age;
        }
        
        if (accountType == null || (!accountType.equals("Savings") && !accountType.equals("Current"))) {
            this.accountType = "Savings";
        } else {
            this.accountType = accountType;
        }
        
        double minBalance = this.accountType.equals("Savings") ? 500.0 : 1000.0;
        if (initialBalance < minBalance) {
            this.balance = minBalance;
        } else {
            this.balance = initialBalance;
        }
        
        this.status = "Active";
        this.pin = null;
    }

    public boolean deposit(double amount) {
        if (!"Active".equals(status)) {
            return false;
        }
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        return true;
    }

    public boolean withdraw(double amount, int pin) {
        if (!"Active".equals(status)) {
            return false;
        }
        if (!verifyPin(pin)) {
            return false;
        }
        if (amount <= 0) {
            return false;
        }
        
        double minBalance = this.accountType.equals("Savings") ? 500.0 : 1000.0;
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
        this.age = age;
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
        if (pin >= 0 && pin <= 9999) {
            this.pin = pin;
            return true;
        }
        return false;
    }

    public boolean verifyPin(int pin) {
        if (this.pin == null) {
            return false;
        }
        return this.pin == pin;
    }

    public boolean hasPin() {
        return this.pin != null;
    }
}
