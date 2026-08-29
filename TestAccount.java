public class TestAccount {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" GLOBAL DIGITAL BANK - ACCOUNT TEST");
        System.out.println("==================================================");
        
        System.out.println(">>> 1. Creating Account");
        Account acc1 = new Account(1001, "John Doe", 25, 1000.0, "Savings");
        System.out.println("Account created!");
        printAccountInfo(acc1);
        
        System.out.println(">>> 2. Deposit Money");
        if (acc1.deposit(500.0)) {
            System.out.println("Depositing ₹500.0: SUCCESS");
            System.out.println("New balance: ₹" + acc1.getBalance());
        } else {
            System.out.println("Depositing ₹500.0: FAILED (Invalid amount)");
        }
        
        if (acc1.deposit(-100.0)) {
            System.out.println("Depositing ₹-100.0: SUCCESS");
            System.out.println("New balance: ₹" + acc1.getBalance());
        } else {
            System.out.println("Depositing ₹-100.0: FAILED (Invalid amount)");
        }
        
        System.out.println(">>> 3. Withdraw Money");
        if (acc1.withdraw(200.0)) {
            System.out.println("Withdrawing ₹200.0: SUCCESS");
            System.out.println("New balance: ₹" + acc1.getBalance());
        } else {
            System.out.println("Withdrawing ₹200.0: FAILED (Insufficient balance)");
        }
        
        if (acc1.withdraw(2000.0)) {
            System.out.println("Withdrawing ₹2000.0: SUCCESS");
            System.out.println("New balance: ₹" + acc1.getBalance());
        } else {
            System.out.println("Withdrawing ₹2000.0: FAILED (Insufficient balance)");
            System.out.println("Current balance: ₹" + acc1.getBalance());
        }
        
        System.out.println(">>> 4. Creating Another Account");
        Account acc2 = new Account(1002, "Jane Smith", 30, 2000.0, "Current");
        printAccountInfo(acc2);
        
        System.out.println(">>> 5. All Accounts");
        printAccountInfo(acc1);
        printAccountInfo(acc2);
        
        System.out.println("==================================================");
        System.out.println(" TEST COMPLETED!");
        System.out.println("==================================================");
    }
    
    private static void printAccountInfo(Account acc) {
        System.out.println("Account #" + acc.getAccountNumber() + " | " + acc.getName() + " (" + acc.getAge() + " yrs) | " + acc.getAccountType() + " | ₹" + acc.getBalance() + " | " + acc.getStatus());
    }
}
