package com.pjava.components.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.BitSet;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.input.Input;

public class InputTest {
    class TestInput extends Input {
        TestInput() {
            super(new int[] { 1 });
        }

        @Override
        public BitSet getState() {
            return null;
        }

        public boolean setInputBusTest1() {
            return setInputBus(new int[] {});
        }

        public boolean setInputBusTest2() {
            return setInputBus(new int[] {});
        }

        @Override
        public TestInput clone() {
            return null;
        }
    }

    @Test
    void inputs() {
        TestInput i = new TestInput();

        assertEquals(0, i.getInputNumber());
        assertEquals(0, i.getInputCable().size());

        assertFalse(i.setInputBusTest1());
        assertFalse(i.setInputBusTest2());
    }
}
