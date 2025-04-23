package com.pjava.src.errors;

public class BusDifferentSizeException extends Exception {
    public BusDifferentSizeException() {
    }

    public BusDifferentSizeException(String message) {
        super(message);
    }

    public BusDifferentSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusDifferentSizeException(Throwable cause) {
        super(cause);
    }

    protected BusDifferentSizeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
