package com.pjava.src.errors;

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
}
