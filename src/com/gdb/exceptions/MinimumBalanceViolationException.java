package com.gdb.exceptions;

/** Thrown when a withdrawal would violate the minimum balance requirement. */
public class MinimumBalanceViolationException extends AccountException {
    public MinimumBalanceViolationException(String message) {
        super(message);
    }
}
