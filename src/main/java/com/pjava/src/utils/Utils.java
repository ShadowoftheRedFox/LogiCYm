package com.pjava.src.utils;

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
    static int id = 0;

    /**
     * Create a unique id, though it's garanted to be unique during the same run.
     * Also can overflow if the id gets bigger than the {@code Integer.MAX_VALUE}
     *
     * @return The unnique ID, starting from 0.
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
}