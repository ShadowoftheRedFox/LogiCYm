package com.pjava.src.errors;

import com.pjava.src.utils.Utils;

/**
 * This exception should be raised when the given bus size is not the one
 * expected.
 */
public class BusSizeException extends Exception {
    /**
     * Default contructor, with null as the message.
     *
     * @see java.lang.Exception#Exception()
     */
    public BusSizeException() {
    }

    /**
     * New exception with the given error message.
     *
     * @param message The error message to further specify the error.
     * @see java.lang.Exception#Exception(String)
     */
    public BusSizeException(String message) {
        super(message);
    }

    /**
     * New exception with the given error message and a cause.
     *
     * @param message The error message to further specify the error.
     * @param cause   The cause of this exception.
     * @see java.lang.Exception#Exception(String, Throwable)
     */
    public BusSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * New exception with the cause. The error message from the cause may be used
     * for this exception.
     *
     * @param cause The cause of this exception.
     * @see java.lang.Exception#Exception(Throwable)
     */
    public BusSizeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message, cause,
     * suppression enabled or disabled, and writable stack trace enabled or
     * disabled.
     *
     * @param message            The error message to further specify the error.
     * @param cause              The cause of this exception.
     * @param enableSuppression  Whether or not suppression is enabled or disabled.
     * @param writableStackTrace Whether or not the stack trace should be writable.
     * @see java.lang.Exception#Exception(String, Throwable, boolean, boolean)
     */
    protected BusSizeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * CHeck whether the given number is a valid case to throw this exception.
     *
     * @param x The number to check.
     * @return False if x is a valid bus size, true otherwise.
     */
    public static boolean isBusSizeException(Integer x) {
        return x == null || x <= 0 || x > 32 || !Utils.isPower2(x);
    }

    /**
     * Create a new instance of BusSizeException with a prefilled message:
     * "Expected " + name + " to be a valid bus size, received: " + x
     *
     * @param name Name of the variable. If null, default is "variable".
     * @param x    Value of the integer checked.
     * @return The new instance of BusSizeException with the prefilled message.
     */
    public static BusSizeException fromName(String name, Integer x) {
        if (name == null) {
            name = "variable";
        }

        return new BusSizeException("Expected " + name + " to be a valid bus size, received: " + x
                + "\nValid bus size are between 1 and 32 included, and a power of 2");
    }
}
