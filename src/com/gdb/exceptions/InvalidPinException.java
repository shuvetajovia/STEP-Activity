package com.gdb.exceptions;

/** Thrown when an invalid PIN is entered or no PIN is set. */
public class InvalidPinException extends AccountException {
    public InvalidPinException(String message) {
        super(message);
    }
}
