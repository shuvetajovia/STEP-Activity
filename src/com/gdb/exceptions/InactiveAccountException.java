package com.gdb.exceptions;

/** Thrown when an operation is attempted on an inactive account. */
public class InactiveAccountException extends AccountException {
    public InactiveAccountException(String message) {
        super(message);
    }
}
