# Activity 15: Funds Transfer with Daily Limits

## Objective
Implement safe, transactional funds transfer between accounts in `TransferService` with real-time daily transfer limits enforced dynamically via the `AccountRulesEngine` and external `.properties` configuration.

---

## Target Files
- `src/main/resources/config/rules/savings.properties`
- `src/main/resources/config/rules/current.properties`
- `src/main/resources/config/rules/salary.properties`
- `src/main/resources/config/rules/fixeddeposit.properties`
- `src/com/gdb/domain/IAccount.java`
- `src/com/gdb/domain/AbstractAccount.java`
- `src/com/gdb/domain/AccountRulesEngine.java`
- `src/com/gdb/domain/TransferService.java`
- `src/com/gdb/exceptions/DailyLimitExceededException.java`
- `src/com/gdb/tests/TestTransferService.java`

---

## Transfer Validation Steps
1. Non-null accounts and self-transfer rejection.
2. Positive amount verification.
3. Sender PIN verification.
4. Active account status validation for both accounts.
5. Sender remaining daily transfer limit check (`canTransfer(amount)`).
6. Atomicity: withdraw from sender, update daily usage, deposit to receiver.

---

## How to Compile & Run

```powershell
javac -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
java -cp bin com.gdb.tests.TestTransferService
```
