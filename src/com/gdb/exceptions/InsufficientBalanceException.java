package com.gdb.exceptions;

/** Thrown when a withdrawal exceeds the available balance (or overdraft limit). */
public class InsufficientBalanceException extends AccountException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
