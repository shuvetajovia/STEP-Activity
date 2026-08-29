# Java Banking Account System Activities

This project contains Java classes developed across different activities (representing week-wise progression) to build a basic banking account management system.

---

## 📅 Activity 1: Creating the Account Class (Entity/Model Class)

### 📄 File: [Account.java](file:///c:/Users/Admin/Downloads/activity/Account.java)

### 🔍 Description
This activity focused on creating a pure entity/model class (`Account`) that only manages account data using boolean returns to indicate success or failure of operations. There are no display methods or custom exceptions thrown in this class.

### 🛠️ Features & Requirements
*   **Private Fields**:
    *   `accountNumber` (int) - Unique account number
    *   `name` (String) - Account holder's full name
    *   `age` (int) - Account holder's age
    *   `balance` (double) - Current account balance
    *   `accountType` (String) - Type of account ("Savings" or "Current")
    *   `status` (String) - Account status ("Active" by default, or "Inactive")
*   **Constructor**: Sets all fields and initializes `status` to `"Active"` by default.
*   **Methods**:
    *   `deposit(double amount)`: Returns `true` if successful, `false` if amount <= 0.
    *   `withdraw(double amount)`: Returns `true` if successful, `false` if insufficient balance or invalid amount.
    *   **Getters & Setters**: Standard getters for all fields, and setters for `name` and `age`.

---

## 📅 Activity 2: Testing the Account Class

### 📄 File: [TestAccount.java](file:///c:/Users/Admin/Downloads/activity/TestAccount.java)

### 🔍 Description
This activity created a driver/test class (`TestAccount`) that interacts with the `Account` class and handles all display/formatting logic.

### 🛠️ Features & Requirements
*   Creates two separate accounts.
*   Tests deposit functionality (success and invalid negative amounts).
*   Tests withdrawal functionality (success and insufficient balance failure).
*   Prints structured information for all accounts with custom console layout.
*   Standardized console logging output with separator lines (`===`).

---

## 📅 Activity 3: Enhancing the Account Class

### 📄 File: [AccountEnhanced.java](file:///c:/Users/Admin/Downloads/activity/AccountEnhanced.java)

### 🔍 Description
An advanced implementation (`AccountEnhanced`) implementing validation, business rules, and security features using boolean returns.

### 🛠️ Features & Requirements
1.  **Enhancement 1: Age Validation**:
    *   Account holders must be at least 18 years old.
    *   If age < 18, the constructor automatically sets it to 18.
2.  **Enhancement 2: Account Type Validation**:
    *   Only `"Savings"` and `"Current"` types are allowed.
    *   If an invalid value is provided, it defaults to `"Savings"`.
3.  **Enhancement 3: Minimum Balance Rules on Creation**:
    *   Savings accounts: Minimum balance of ₹500
    *   Current accounts: Minimum balance of ₹1000
    *   If initial balance is below these minimums, it is automatically corrected to the minimum.
4.  **Enhancement 4: Minimum Balance Enforcement on Withdrawal**:
    *   Balance is not allowed to fall below the minimum limit after any withdrawal. If it would, `withdraw()` returns `false`.
5.  **Enhancement 5: Account Status Management**:
    *   `closeAccount()`: Sets status to `"Inactive"` (returns `true` if successful, `false` if already inactive).
    *   `reopenAccount()`: Sets status to `"Active"` (returns `true` if successful, `false` if already active).
    *   `deposit()` and `withdraw()` operations instantly return `false` if the account status is inactive.
6.  **Enhancement 6: PIN Protection**:
    *   `pin` field (Integer, 4-digit, can be null).
    *   `setPin(int pin)`: Sets the pin (returns `true` if valid 4-digit number, `false` otherwise).
    *   `verifyPin(int pin)`: Verifies if the input matches.
    *   `hasPin()`: Checks if the pin has been initialized.
    *   `withdraw(double amount, int pin)`: Overloaded/modified withdrawal method that requires PIN validation, checking status, and minimum balance enforcement.

---

## 🚀 Compilation & Running

To compile and run the basic test demonstration (Activity 2), execute the following commands in your terminal:

```powershell
# Compile all source files
javac Account.java TestAccount.java AccountEnhanced.java

# Run the test runner
java TestAccount
```
