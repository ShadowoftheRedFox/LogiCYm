package com.pjava.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.pjava.src.utils.Utils;

public class UtilsTest {
    @Test
    void isOdd() {
        assertEquals(true, Utils.isOdd(1));
        assertEquals(false, Utils.isOdd(2));
    }

    @Test
    void isEven() {
        assertEquals(false, Utils.isEven(1));
        assertEquals(true, Utils.isEven(2));
    }

    @ParameterizedTest(name = "isPower2({2}) = {1}")
    @CsvSource({
            "true, 1",
            "true, 2",
            "true, 4",
            "false, 0",
            "false, -1",
            "false, 3",
            "false, 33",
    })
    void isPower2(boolean expectedResult, int value) {
        assertEquals(expectedResult, Utils.isPower2(value),
                () -> "Utils.isPower2(" + value + ") should equal " + expectedResult + ", received "
                        + !expectedResult);
    }

    @ParameterizedTest(name = "pow2({2}) = {1}")
    @CsvSource({
            "1, -1",
            "1, 0",
            "2, 1",
            "4, 2",
    })
    void pow2(int expectedResult, int value) {
        assertEquals(expectedResult, Utils.pow2(value),
                () -> "Utils.pow2(" + value + ") should not equal " + expectedResult);
    }

    @Test
    void intIncludes() {
        int[] arr = new int[] { 0, 1, 2 };
        assertEquals(true, Utils.includes(arr, 1),
                () -> "Utils.includes(" + arr + ", 1) should equal true");
        assertEquals(false, Utils.includes(arr, 8),
                () -> "Utils.includes(" + arr + ", 8) should equal false");
    }

    @Test
    void IntegerIncludes() {
        int[] arr = new int[] { 0, 1, 2 };
        assertEquals(true, Utils.includes(arr, 1),
                () -> "Utils.includes(" + arr + ", 1) should equal true");
        assertEquals(false, Utils.includes(arr, 8),
                () -> "Utils.includes(" + arr + ", 8) should equal false");
    }
}
