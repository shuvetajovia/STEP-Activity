package com.gdb.exceptions;

/**
 * Base exception for all account-related errors.
 * Activity 9: Abstract Classes & Template Method Pattern
 */
public class AccountException extends Exception {
    public AccountException(String message) {
        super(message);
    }
}
