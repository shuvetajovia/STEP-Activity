package com.gdb.exceptions;

public class DailyLimitExceededException extends AccountException {
    public DailyLimitExceededException(String message) {
        super(message);
    }
}
