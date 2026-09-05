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

## 📅 Activity 4: Testing the Enhanced Account Class

### 📄 File: [TestAccountEnhanced.java](file:///c:/Users/Admin/Downloads/activity/TestAccountEnhanced.java)

### 🔍 Description
A test runner (`TestAccountEnhanced`) designed to verify all validation, status management, and PIN protection rules implemented in `AccountEnhanced.java` using boolean return values.

### 🛠️ Features & Requirements
*   Tests valid account creation.
*   Tests invalid age correction (under 18 auto-corrected to 18).
*   Tests invalid account type default ("Invalid" defaulting to "Savings").
*   Tests minimum balance enforcement on creation (auto-corrected to minimum).
*   Tests minimum balance enforcement on withdrawal (returning `false` on violations).
*   Tests status management (closing and reopening accounts).
*   Tests PIN protection (setting, verifying, and checking operations with correct/incorrect/uninitialized PINs).
*   Displays a comprehensive summary of all accounts at the end of the test.

---

## 📅 Activity 5: Introducing Exceptions in the Account Class

### 📄 File: [Account.java](file:///c:/Users/Admin/Downloads/activity/Account.java)

### 🔍 Description
In this activity, the `Account` class is updated to use robust Exception Handling rather than simple boolean return values for operations. Custom checked exceptions are introduced, and the constructor now throws `IllegalArgumentException` on invalid values instead of self-correcting them.

### 🛠️ Custom Exceptions Created
*   [`AccountException.java`](file:///c:/Users/Admin/Downloads/activity/AccountException.java) (Base class)
*   [`InvalidAmountException.java`](file:///c:/Users/Admin/Downloads/activity/InvalidAmountException.java) (For negative/zero deposit/withdrawal amounts)
*   [`InsufficientBalanceException.java`](file:///c:/Users/Admin/Downloads/activity/InsufficientBalanceException.java) (For withdrawals exceeding balance)
*   [`MinimumBalanceViolationException.java`](file:///c:/Users/Admin/Downloads/activity/MinimumBalanceViolationException.java) (For withdrawals that would drop balance below minimum limit)
*   [`InactiveAccountException.java`](file:///c:/Users/Admin/Downloads/activity/InactiveAccountException.java) (For operations attempted on inactive accounts)
*   [`InvalidPinException.java`](file:///c:/Users/Admin/Downloads/activity/InvalidPinException.java) (For incorrect or missing PIN verification)

---

## 📅 Activity 6: Testing the Account Class with Exceptions

### 📄 File: [TestAccountExceptions.java](file:///c:/Users/Admin/Downloads/activity/TestAccountExceptions.java)

### 🔍 Description
A comprehensive test suite (`TestAccountExceptions`) that validates the exception-handling model built in Activity 5. It runs scenarios with structured `try-catch` blocks and logs detailed, user-friendly messages for both validation failures and operational exception states.

### 🛠️ Features & Requirements
*   Uses `try-catch` blocks for all error-testing paths.
*   Asserts constructor exception throws (underage checks, invalid types, below minimum balance bounds).
*   Validates deposit and withdrawal exceptions (`InvalidAmountException`, `InsufficientBalanceException`, `MinimumBalanceViolationException`, `InactiveAccountException`, `InvalidPinException`).
*   Displays formatted console logs detailing both `SUCCESS` flows and `EXCEPTION` events with parameter dumps.
*   Prints a complete successful account summary at the conclusion of execution.

---

## 🚀 Compilation & Running

To compile and run the application tests, execute the following commands in your terminal:

```powershell
# Compile all source files (including exceptions and tests)
javac AccountException.java InactiveAccountException.java InsufficientBalanceException.java InvalidAmountException.java InvalidPinException.java MinimumBalanceViolationException.java Account.java SavingsAccount.java CurrentAccount.java TestAccountExceptions.java AccountEnhanced.java TestAccountEnhanced.java

# Run the exceptions test runner (Activity 6)
java TestAccountExceptions

# Run the subclasses test runner (Activity 8) — self-contained single file
javac TestAccountSubclasses.java
java TestAccountSubclasses
```

---

## 📅 Activity 7: Creating Account Subclasses (Savings & Current)

### 📄 Files
- [`Account.java`](file:///c:/Users/Admin/Downloads/activity/Account.java) — converted to **abstract class**
- [`SavingsAccount.java`](file:///c:/Users/Admin/Downloads/activity/SavingsAccount.java) — **[NEW]**
- [`CurrentAccount.java`](file:///c:/Users/Admin/Downloads/activity/CurrentAccount.java) — **[NEW]**

### 🏗️ Architecture
```
Account (Abstract Parent)
├── SavingsAccount (Child)
└── CurrentAccount (Child)
```

### 🔍 Description
Introduces inheritance by converting `Account` to an abstract class and creating two specialized subclasses.

### 🛠️ Features & Requirements
1. **Abstract `Account` class**:
   - Removed `accountType` field and `accountType` constructor param — each subclass defines its own type
   - Two new abstract methods: `getMinimumBalance()` and `getAccountType()`
   - Protected `setBalance()` helper for subclass use
   - Constructor validates age and minimum balance (delegated to the subclass via abstract methods)
2. **`SavingsAccount`**:
   - Minimum balance: ₹500
   - Interest rate: 4% per annum
   - Extra methods: `calculateInterest(int years)`, `getInterestRate()`
3. **`CurrentAccount`**:
   - Minimum balance: ₹1000
   - Overdraft limit: ₹5000
   - Overrides `withdraw()` to allow balance to go below minimum using the overdraft facility
   - Extra methods: `getOverdraftLimit()`, `getOverdraftUsed()`, `getAvailableOverdraft()`, `isUsingOverdraft()`, `repayOverdraft(double)`

---

## 📅 Activity 8: Testing Account Subclasses

### 📄 File: [`TestAccountSubclasses.java`](file:///c:/Users/Admin/Downloads/activity/TestAccountSubclasses.java)

### 🔍 Description
A comprehensive self-contained test class (`TestAccountSubclasses`) that verifies the specialized behaviour of `SavingsAccount` and `CurrentAccount`. Follows the same single-file OnlineGDB-compatible pattern as `TestAccountExceptions.java`.

### 🛠️ Test Coverage
1. **Test 1** — Create `SavingsAccount` and `CurrentAccount` instances
2. **Test 2** — Verify polymorphic `getAccountType()` and `getMinimumBalance()` calls
3. **Test 3** — Savings-specific interest calculation (1, 2, 5 years)
4. **Test 4** — Current-specific overdraft: below-minimum withdrawal, exceeding overdraft, repayment
5. **Test 5** — Polymorphism: treating both account types uniformly through `Account` references
6. **Test 6** — Validation: invalid creation (below min balance, underage)
7. **Test 7** — Savings account PIN and operation exceptions (MinimumBalanceViolation)
8. **Test 8** — Current account status management (close, deposit on closed, reopen, deposit)
9. **Test 9** — All accounts summary

