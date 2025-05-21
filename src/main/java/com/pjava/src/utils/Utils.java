package com.pjava.src.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An abstract class used to get access to useful static functions globally.
 */
public abstract class Utils {
    /**
     * The constructor of the Utils class.
     */
    protected Utils() {
    }

    /**
     * Check if the given number is even. Check with the bitwise operation.
     *
     * @param n The number to check.
     * @return True is n is even, false otherwise.
     * @see #isOdd
     */
    public static boolean isEven(int n) {
        return (n & 1) == 0;
    }

    /**
     * Check if the given number is odd. Check with the bitwise operation.
     *
     * @param n The number to check.
     * @return True is n is odd, false otherwise.
     * @see #isEven
     */
    public static boolean isOdd(int n) {
        return !isEven(n);
    }

    /**
     * Check if n is a power of 2.
     *
     * @param n The value to check.
     * @return true if n is a power of 2, false otherwise.
     */
    public static boolean isPower2(int n) {
        return (n != 0) && ((n & (n - 1)) == 0);
    }

    /**
     * Calculate 2 to the power of k.
     *
     * @param k The power. Must be between 1 and 32, inclusive.
     * @return 1 if k is less than 0, 2^32 if k is greater than 32, or 2^k.
     */
    public static int pow2(int k) {
        if (k < 0)
            return 1;
        if (k > 32)
            k = 32;
        return 1 << k;
    }

    /**
     * Tells whether the target is in the array.
     *
     * @param array  The array to look into.
     * @param target The target we want to know if its inside array.
     * @return True if target is inside array, false otherwise.
     */
    public static boolean includes(int[] array, int target) {
        if (array == null || array.length == 0) {
            return false;
        }
        for (int i : array) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tells whether the target is in the array.
     *
     * @param array  The array to look into.
     * @param target The target we want to know if its inside array.
     * @return True if target is inside array, false otherwise.
     */
    public static boolean includes(Integer[] array, int target) {
        if (array == null || array.length == 0) {
            return false;
        }
        for (int i : array) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used to generate unique ID at runtime.
     *
     * @see #runtimeID()
     */
    static int id = 1;

    /**
     * Create a unique id, though it's garanted to be unique during the same run.
     * Also can overflow if the id gets bigger than the {@code Integer.MAX_VALUE}
     *
     * @return The unique ID, starting from 1.
     */
    public static int runtimeID() {
        return id++;
    }

    /**
     * Gets the number of generated components. Since all components call
     * {@link #runtimeID()} when instantiating, returning this id without
     * incrementing it gives the amount of components created.
     *
     * @return The number of component generated.
     */
    public static int getComponentsCount() {
        return id;
    }

    /**
     * Execute the given runnable after waiting the given delay.
     *
     * @param exec    The main runnable function.
     * @param delay   The delay to wait, in miliseconds.
     * @param catcher A function in case {@link CompletableFuture} throws. Can be
     *                null.
     * @return The CompletableFuture created, or null if exec is null or if delay is
     *         smaller or equal to 0.
     */
    public static CompletableFuture<Void> timeout(Runnable exec, long delay, TimeoutCatch catcher) {
        if (delay < 0 || exec == null) {
            return null;
        }

        CompletableFuture<Void> future = CompletableFuture.runAsync(exec);
        if (catcher == null) {
            catcher = new TimeoutCatch() {
            };
        }

        try {
            // success
            future.get(delay, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            catcher.handleInterruption(ie);
        } catch (ExecutionException ee) {
            // exception was thrown by code
            catcher.handleExecution(ee);
        } catch (TimeoutException te) {
            // timeout occurred
            catcher.handleTimeout(te);
        }
        return future;
    }

    /**
     * Interface use as the catcher in
     * {@link #timeout(Runnable, long, TimeoutCatch)}.
     */
    public interface TimeoutCatch {
        /**
         * Default handler, that print the exception.
         *
         * @param e The exception to handle.
         */
        default void handle(Exception e) {
            e.printStackTrace();
        };

        /**
         * Specific handler for InterruptedException. By default, fallback to
         * {@link #handle(Exception)}.
         *
         * @param ie The InterruptedException to handle.
         */
        default void handleInterruption(InterruptedException ie) {
            handle(ie);
        }

        /**
         * Specific handler for ExecutionException. By default, fallback to
         * {@link #handle(Exception)}.
         *
         * @param ee The ExecutionException to handle.
         */
        default void handleExecution(ExecutionException ee) {
            handle(ee);
        }

        /**
         * Specific handler for TimeoutException. By default, fallback to
         * {@link #handle(Exception)}.
         *
         * @param te The TimeoutException to handle.
         */
        default void handleTimeout(TimeoutException te) {
            handle(te);
        }
    }
}
