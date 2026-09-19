import os
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls

def set_cell_background(cell, fill_hex):
    tcPr = cell._element.get_or_add_tcPr()
    shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    tcPr.append(shd)

def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
    tcPr = cell._element.get_or_add_tcPr()
    tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
    tcPr.append(tcMar)

def add_code_block(doc, code_text):
    table = doc.add_table(rows=1, cols=1)
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.autofit = False
    
    cell = table.cell(0, 0)
    cell.width = Inches(6.5)
    set_cell_background(cell, "F5F6F8")
    set_cell_margins(cell, top=140, bottom=140, left=200, right=200)
    
    tcPr = cell._element.get_or_add_tcPr()
    borders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:top w:val="single" w:sz="4" w:space="0" w:color="D1D5DB"/><w:left w:val="single" w:sz="4" w:space="0" w:color="D1D5DB"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="D1D5DB"/><w:right w:val="single" w:sz="4" w:space="0" w:color="D1D5DB"/></w:tcBorders>')
    tcPr.append(borders)
    
    p = cell.paragraphs[0]
    p.paragraph_format.space_before = Pt(2)
    p.paragraph_format.space_after = Pt(2)
    p.paragraph_format.line_spacing = Pt(13)
    
    lines = code_text.strip().split('\n')
    for i, line in enumerate(lines):
        run = p.add_run(line)
        run.font.name = 'Consolas'
        run.font.size = Pt(9.5)
        run.font.color.rgb = RGBColor(30, 41, 59)
        if i < len(lines) - 1:
            p.add_run('\n')

    p_after = doc.add_paragraph()
    p_after.paragraph_format.space_before = Pt(0)
    p_after.paragraph_format.space_after = Pt(6)

def add_output_block(doc, output_text):
    table = doc.add_table(rows=1, cols=1)
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.autofit = False
    
    cell = table.cell(0, 0)
    cell.width = Inches(6.5)
    set_cell_background(cell, "1E1E2E")
    set_cell_margins(cell, top=140, bottom=140, left=200, right=200)
    
    tcPr = cell._element.get_or_add_tcPr()
    borders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:top w:val="none"/><w:left w:val="none"/><w:bottom w:val="none"/><w:right w:val="none"/></w:tcBorders>')
    tcPr.append(borders)
    
    p = cell.paragraphs[0]
    p.paragraph_format.space_before = Pt(2)
    p.paragraph_format.space_after = Pt(2)
    p.paragraph_format.line_spacing = Pt(13)
    
    lines = output_text.strip().split('\n')
    for i, line in enumerate(lines):
        run = p.add_run(line)
        run.font.name = 'Consolas'
        run.font.size = Pt(9.5)
        run.font.color.rgb = RGBColor(166, 227, 161)
        if i < len(lines) - 1:
            p.add_run('\n')

    p_after = doc.add_paragraph()
    p_after.paragraph_format.space_before = Pt(0)
    p_after.paragraph_format.space_after = Pt(6)

def create_activity15_doc(output_path):
    doc = docx.Document()
    
    for section in doc.sections:
        section.top_margin = Inches(0.8)
        section.bottom_margin = Inches(0.8)
        section.left_margin = Inches(0.8)
        section.right_margin = Inches(0.8)

    # Title
    title = doc.add_paragraph()
    run_title = title.add_run("Object-Oriented Programming Lab Report")
    run_title.font.name = 'Calibri'
    run_title.font.size = Pt(22)
    run_title.font.bold = True
    run_title.font.color.rgb = RGBColor(15, 23, 42)
    title.paragraph_format.space_after = Pt(2)

    subtitle = doc.add_paragraph()
    run_sub = subtitle.add_run("Activity 15: Funds Transfer with Daily Limits")
    run_sub.font.name = 'Calibri'
    run_sub.font.size = Pt(16)
    run_sub.font.bold = True
    run_sub.font.color.rgb = RGBColor(37, 99, 235)
    subtitle.paragraph_format.space_after = Pt(10)

    # Metadata Box
    meta_table = doc.add_table(rows=2, cols=2)
    meta_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    for row in meta_table.rows:
        for cell in row.cells:
            set_cell_background(cell, "F1F5F9")
            set_cell_margins(cell, top=60, bottom=60, left=120, right=120)
            cell.width = Inches(3.25)
            tcPr = cell._element.get_or_add_tcPr()
            tcBorders = parse_xml(f'<w:tcBorders {nsdecls("w")}><w:top w:val="none"/><w:left w:val="none"/><w:bottom w:val="none"/><w:right w:val="none"/></w:tcBorders>')
            tcPr.append(tcBorders)

    c00 = meta_table.cell(0, 0).paragraphs[0]
    r = c00.add_run("Student Name: ")
    r.font.bold = True
    r.font.name = 'Calibri'
    r.font.size = Pt(10.5)
    r2 = c00.add_run("A Shuveta Jovi")
    r2.font.name = 'Calibri'
    r2.font.size = Pt(10.5)

    c01 = meta_table.cell(0, 1).paragraphs[0]
    r = c01.add_run("Registration No: ")
    r.font.bold = True
    r.font.name = 'Calibri'
    r.font.size = Pt(10.5)
    r2 = c01.add_run("RA2411003011128")
    r2.font.name = 'Calibri'
    r2.font.size = Pt(10.5)

    c10 = meta_table.cell(1, 0).paragraphs[0]
    r = c10.add_run("Language: ")
    r.font.bold = True
    r.font.name = 'Calibri'
    r.font.size = Pt(10.5)
    r2 = c10.add_run("Java (JDK 8+)")
    r2.font.name = 'Calibri'
    r2.font.size = Pt(10.5)

    c11 = meta_table.cell(1, 1).paragraphs[0]
    r = c11.add_run("Topic: ")
    r.font.bold = True
    r.font.name = 'Calibri'
    r.font.size = Pt(10.5)
    r2 = c11.add_run("Funds Transfer Service & Daily Limits")
    r2.font.name = 'Calibri'
    r2.font.size = Pt(10.5)

    # 1. Objective
    h1 = doc.add_heading(level=1)
    run_h1 = h1.add_run("1. Objective")
    run_h1.font.name = 'Calibri'
    run_h1.font.size = Pt(14)
    run_h1.font.bold = True
    run_h1.font.color.rgb = RGBColor(15, 23, 42)
    h1.paragraph_format.space_before = Pt(10)
    h1.paragraph_format.space_after = Pt(4)

    p_obj = doc.add_paragraph()
    p_obj.paragraph_format.space_before = Pt(2)
    p_obj.paragraph_format.space_after = Pt(8)
    p_obj.paragraph_format.line_spacing = Pt(14)
    r = p_obj.add_run("Implement a secure, transactional funds transfer service (TransferService) that facilitates money movement between accounts with real-time daily transfer limits. Enforce comprehensive multi-step validation (account presence, active status, PIN verification, positive amount, and daily limit thresholds), dynamic rule assignment based on account type/tenure tier, date-aware limit reset, and atomic balance debit/credit execution.")
    r.font.name = 'Calibri'
    r.font.size = Pt(11)

    # 2. Source Code
    h2 = doc.add_heading(level=1)
    run_h2 = h2.add_run("2. Source Code Implementation")
    run_h2.font.name = 'Calibri'
    run_h2.font.size = Pt(14)
    run_h2.font.bold = True
    run_h2.font.color.rgb = RGBColor(15, 23, 42)
    h2.paragraph_format.space_before = Pt(12)
    h2.paragraph_format.space_after = Pt(4)

    # TransferService.java
    p_fn = doc.add_paragraph()
    r = p_fn.add_run("TransferService.java")
    r.font.name = 'Consolas'
    r.font.size = Pt(11)
    r.font.bold = True
    r.font.color.rgb = RGBColor(37, 99, 235)
    p_fn.paragraph_format.space_before = Pt(4)
    p_fn.paragraph_format.space_after = Pt(2)

    transfer_service_code = """package com.gdb.domain;

import com.gdb.exceptions.*;

public class TransferService {

    public static boolean transfer(IAccount fromAccount, IAccount toAccount, double amount, String pin) throws AccountException {
        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Source and destination accounts must not be null");
        }

        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer funds to the same account: " + fromAccount.getAccountNumber());
        }

        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive. Provided: " + amount);
        }

        if (!fromAccount.validatePin(pin)) {
            throw new InvalidPinException("Invalid PIN entered for account: " + fromAccount.getAccountNumber());
        }

        if (!"ACTIVE".equalsIgnoreCase(fromAccount.getStatus())) {
            throw new InactiveAccountException("Source account " + fromAccount.getAccountNumber() + " is inactive");
        }

        if (!"ACTIVE".equalsIgnoreCase(toAccount.getStatus())) {
            throw new InactiveAccountException("Destination account " + toAccount.getAccountNumber() + " is inactive");
        }

        // Daily limit check
        if (!fromAccount.canTransfer(amount)) {
            throw new DailyLimitExceededException(
                "Transfer of Rs " + amount + " exceeds remaining daily limit of Rs " +
                fromAccount.getRemainingDailyLimit() + " (Daily Limit: Rs " + fromAccount.getDailyLimit() +
                ", Transferred Today: Rs " + fromAccount.getTransferredToday() + ")"
            );
        }

        // Transactional execution: withdraw -> update daily usage -> deposit
        fromAccount.withdraw(amount, pin);
        fromAccount.updateDailyTransferred(amount);
        toAccount.deposit(amount);

        return true;
    }
}"""
    add_code_block(doc, transfer_service_code)

    # DailyLimitExceededException.java
    p_fn = doc.add_paragraph()
    r = p_fn.add_run("DailyLimitExceededException.java")
    r.font.name = 'Consolas'
    r.font.size = Pt(11)
    r.font.bold = True
    r.font.color.rgb = RGBColor(37, 99, 235)
    p_fn.paragraph_format.space_before = Pt(4)
    p_fn.paragraph_format.space_after = Pt(2)

    ex_code = """package com.gdb.exceptions;

public class DailyLimitExceededException extends AccountException {
    public DailyLimitExceededException(String message) {
        super(message);
    }
}"""
    add_code_block(doc, ex_code)

    # IAccount.java
    p_fn = doc.add_paragraph()
    r = p_fn.add_run("IAccount.java")
    r.font.name = 'Consolas'
    r.font.size = Pt(11)
    r.font.bold = True
    r.font.color.rgb = RGBColor(37, 99, 235)
    p_fn.paragraph_format.space_before = Pt(4)
    p_fn.paragraph_format.space_after = Pt(2)

    iaccount_code = """package com.gdb.domain;

import com.gdb.exceptions.*;

public interface IAccount {
    String getAccountNumber();
    String getName();
    int getAge();
    double getBalance();
    String getAccountType();
    String getStatus();
    boolean validatePin(String enteredPin);
    boolean changePin(String oldPin, String newPin);
    void deposit(double amount) throws InvalidAmountException;
    void withdraw(double amount, String enteredPin) throws AccountException;
    
    // Daily Transfer Limit Methods
    double getDailyLimit();
    double getRemainingDailyLimit();
    boolean canTransfer(double amount);
    void updateDailyTransferred(double amount);
    void resetDailyLimitIfNewDay();
    double getTransferredToday();
    
    void displayAccountInfo();
}"""
    add_code_block(doc, iaccount_code)

    # AbstractAccount.java
    p_fn = doc.add_paragraph()
    r = p_fn.add_run("AbstractAccount.java")
    r.font.name = 'Consolas'
    r.font.size = Pt(11)
    r.font.bold = True
    r.font.color.rgb = RGBColor(37, 99, 235)
    p_fn.paragraph_format.space_before = Pt(4)
    p_fn.paragraph_format.space_after = Pt(2)

    abstract_acc_code = """package com.gdb.domain;

import com.gdb.exceptions.*;
import java.time.LocalDate;

public abstract class AbstractAccount implements IAccount {
    protected String accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected String pin;
    
    protected double dailyLimit;
    protected double transferredToday;
    protected LocalDate lastTransferDate;

    public AbstractAccount(String accountNumber, String name, int age, double balance, String accountType, String status, String pin, double dailyLimit) {
        if (age < 18) throw new IllegalArgumentException("Customer age must be 18 or above");
        if (balance < 0) throw new IllegalArgumentException("Initial balance cannot be negative");
        if (pin == null || !pin.matches("\\\\d{4}")) throw new IllegalArgumentException("PIN must be 4 digits");
        
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.accountType = accountType;
        this.status = status;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
        this.transferredToday = 0.0;
        this.lastTransferDate = LocalDate.now();
    }

    public boolean validatePin(String enteredPin) {
        return this.pin != null && this.pin.equals(enteredPin);
    }

    public boolean changePin(String oldPin, String newPin) {
        if (!validatePin(oldPin)) return false;
        if (newPin == null || !newPin.matches("\\\\d{4}")) return false;
        this.pin = newPin;
        return true;
    }

    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be positive");
        this.balance += amount;
    }

    public void withdraw(double amount, String enteredPin) throws AccountException {
        if (!validatePin(enteredPin)) throw new InvalidPinException("Invalid PIN entered");
        if (!"ACTIVE".equalsIgnoreCase(this.status)) throw new InactiveAccountException("Account is not active");
        if (amount <= 0) throw new InvalidAmountException("Withdrawal amount must be positive");
        processDebit(amount);
    }

    public abstract void processDebit(double amount) throws AccountException;

    public void resetDailyLimitIfNewDay() {
        LocalDate today = LocalDate.now();
        if (lastTransferDate == null || !lastTransferDate.isEqual(today)) {
            transferredToday = 0.0;
            lastTransferDate = today;
        }
    }

    public double getDailyLimit() { return dailyLimit; }

    public double getTransferredToday() {
        resetDailyLimitIfNewDay();
        return transferredToday;
    }

    public double getRemainingDailyLimit() {
        resetDailyLimitIfNewDay();
        return Math.max(0.0, dailyLimit - transferredToday);
    }

    public boolean canTransfer(double amount) {
        resetDailyLimitIfNewDay();
        return (transferredToday + amount) <= dailyLimit;
    }

    public void updateDailyTransferred(double amount) {
        resetDailyLimitIfNewDay();
        this.transferredToday += amount;
    }

    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Balance: Rs " + balance);
        System.out.println("Account Type: " + accountType);
        System.out.println("Status: " + status);
        System.out.println("Daily Limit: Rs " + dailyLimit + " (Remaining: Rs " + getRemainingDailyLimit() + ")");
    }

    public String getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public String getStatus() { return status; }
}"""
    add_code_block(doc, abstract_acc_code)

    # TestTransferService.java
    p_fn = doc.add_paragraph()
    r = p_fn.add_run("TestTransferService.java")
    r.font.name = 'Consolas'
    r.font.size = Pt(11)
    r.font.bold = True
    r.font.color.rgb = RGBColor(37, 99, 235)
    p_fn.paragraph_format.space_before = Pt(4)
    p_fn.paragraph_format.space_after = Pt(2)

    test_code = """package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestTransferService {
    public static void main(String[] args) {
        System.out.println("=== Activity 15: Funds Transfer with Daily Limits Test ===");

        IAccount sender = AccountFactory.createAccount("SAVINGS", "SAV1001", "Rajesh Sharma", 28, 80000.0, "ACTIVE", "1234", 2);
        IAccount receiver = AccountFactory.createAccount("CURRENT", "CUR1001", "Priya Patel", 34, 20000.0, "ACTIVE", "5678");

        System.out.println("\\n--- Initial Account States ---");
        System.out.println("Sender Balance  : Rs " + sender.getBalance() + " | Daily Limit: Rs " + sender.getDailyLimit() + " | Remaining: Rs " + sender.getRemainingDailyLimit());
        System.out.println("Receiver Balance: Rs " + receiver.getBalance());

        // Test 1: Successful Transfer within daily limit
        System.out.println("\\n[Test 1] Transfer Rs 20,000 within daily limit");
        try {
            boolean success = TransferService.transfer(sender, receiver, 20000.0, "1234");
            if (success && sender.getBalance() == 60000.0 && receiver.getBalance() == 40000.0 && sender.getRemainingDailyLimit() == 30000.0) {
                System.out.println(" -> Result: SUCCESS [PASS]");
                System.out.println(" -> Sender Balance: Rs " + sender.getBalance() + " | Remaining Limit: Rs " + sender.getRemainingDailyLimit());
                System.out.println(" -> Receiver Balance: Rs " + receiver.getBalance());
            }
        } catch (Exception e) {
            System.out.println(" -> Result: FAILED [FAIL] - " + e.getMessage());
        }

        // Test 2: Transfer exceeding remaining daily limit
        System.out.println("\\n[Test 2] Transfer Rs 35,000 exceeding remaining daily limit of Rs 30,000");
        try {
            TransferService.transfer(sender, receiver, 35000.0, "1234");
        } catch (DailyLimitExceededException e) {
            System.out.println(" -> Result: Caught DailyLimitExceededException [PASS]");
            System.out.println("    Message: " + e.getMessage());
            System.out.println(" -> Sender Balance: Rs " + sender.getBalance() + " (Unchanged) [PASS]");
        }

        // Test 3: Successful Transfer 2 utilizing remaining limit
        System.out.println("\\n[Test 3] Transfer Rs 30,000 fully utilizing remaining daily limit");
        try {
            boolean success = TransferService.transfer(sender, receiver, 30000.0, "1234");
            if (success && sender.getBalance() == 30000.0 && sender.getRemainingDailyLimit() == 0.0) {
                System.out.println(" -> Result: SUCCESS [PASS]");
                System.out.println(" -> Sender Balance: Rs " + sender.getBalance() + " | Remaining Limit: Rs " + sender.getRemainingDailyLimit());
                System.out.println(" -> Receiver Balance: Rs " + receiver.getBalance());
            }
        } catch (Exception e) {
            System.out.println(" -> Result: FAILED [FAIL] - " + e.getMessage());
        }

        // Test 4: Daily limit exhausted
        System.out.println("\\n[Test 4] Transfer when daily limit is exhausted (Remaining: Rs 0.0)");
        try {
            TransferService.transfer(sender, receiver, 1000.0, "1234");
        } catch (DailyLimitExceededException e) {
            System.out.println(" -> Result: Caught DailyLimitExceededException [PASS]");
            System.out.println("    Message: " + e.getMessage());
        }

        // Test 5: Invalid PIN
        System.out.println("\\n[Test 5] Transfer with Invalid PIN");
        try {
            TransferService.transfer(sender, receiver, 5000.0, "9999");
        } catch (InvalidPinException e) {
            System.out.println(" -> Result: Caught InvalidPinException [PASS]");
            System.out.println("    Message: " + e.getMessage());
        }

        // Test 6: Self-Transfer
        System.out.println("\\n[Test 6] Self-Transfer Rejection");
        try {
            TransferService.transfer(sender, sender, 5000.0, "1234");
        } catch (IllegalArgumentException e) {
            System.out.println(" -> Result: Caught IllegalArgumentException [PASS]");
            System.out.println("    Message: " + e.getMessage());
        }

        System.out.println("\\nAll Activity 15 funds transfer and daily limit tests verified successfully!");
    }
}"""
    add_code_block(doc, test_code)

    # 3. Output
    h3 = doc.add_heading(level=1)
    run_h3 = h3.add_run("3. Execution Output")
    run_h3.font.name = 'Calibri'
    run_h3.font.size = Pt(14)
    run_h3.font.bold = True
    run_h3.font.color.rgb = RGBColor(15, 23, 42)
    h3.paragraph_format.space_before = Pt(12)
    h3.paragraph_format.space_after = Pt(4)

    output_text = """=== Activity 15: Funds Transfer with Daily Limits Test ===

--- Initial Account States ---
Sender Balance  : Rs 80000.0 | Daily Limit: Rs 50000.0 | Remaining: Rs 50000.0
Receiver Balance: Rs 20000.0

[Test 1] Transfer Rs 20,000 within daily limit
 -> Result: SUCCESS [PASS]
 -> Sender Balance: Rs 60000.0 | Remaining Limit: Rs 30000.0
 -> Receiver Balance: Rs 40000.0

[Test 2] Transfer Rs 35,000 exceeding remaining daily limit of Rs 30,000
 -> Result: Caught DailyLimitExceededException [PASS]
    Message: Transfer of Rs 35000.0 exceeds remaining daily limit of Rs 30000.0 (Daily Limit: Rs 50000.0, Transferred Today: Rs 20000.0)
 -> Sender Balance: Rs 60000.0 (Unchanged) [PASS]

[Test 3] Transfer Rs 30,000 fully utilizing remaining daily limit
 -> Result: SUCCESS [PASS]
 -> Sender Balance: Rs 30000.0 | Remaining Limit: Rs 0.0
 -> Receiver Balance: Rs 70000.0

[Test 4] Transfer when daily limit is exhausted (Remaining: Rs 0.0)
 -> Result: Caught DailyLimitExceededException [PASS]
    Message: Transfer of Rs 1000.0 exceeds remaining daily limit of Rs 0.0 (Daily Limit: Rs 50000.0, Transferred Today: Rs 50000.0)

[Test 5] Transfer with Invalid PIN
 -> Result: Caught InvalidPinException [PASS]
    Message: Invalid PIN entered for account: SAV1001

[Test 6] Self-Transfer Rejection
 -> Result: Caught IllegalArgumentException [PASS]
    Message: Cannot transfer funds to the same account: SAV1001

All Activity 15 funds transfer and daily limit tests verified successfully!"""
    add_output_block(doc, output_text)

    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    doc.save(output_path)
    print(f"Generated: {output_path}")

if __name__ == "__main__":
    targets = [
        r"C:\Users\Admin\Downloads\activity\sourced code doc\Activity_15_Source_Code.docx",
        r"c:\Users\Admin\Downloads\java-gdb-activities-10\java-gdb-activities-10\sourced code doc\Activity_15_Source_Code.docx",
        r"c:\Users\Admin\Downloads\java-gdb-activities-10\java-gdb-activities-10\source code doc\RA2411003011128 Activity_15_Source_Code.docx",
        r"c:\Users\Admin\Downloads\java-gdb-activities-10\15\Activity_15_Source_Code.docx",
    ]
    for target in targets:
        create_activity15_doc(target)
