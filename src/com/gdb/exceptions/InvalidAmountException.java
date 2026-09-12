package com.gdb.exceptions;

/** Thrown when an invalid amount (zero or negative) is provided. */
public class InvalidAmountException extends AccountException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
