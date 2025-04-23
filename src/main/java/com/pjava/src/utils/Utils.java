package com.pjava.src.utils;

public abstract class Utils {
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
}