package com.pjava.components.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.output.Output;

public class OutputTest {
    class TestOutput extends Output {
        TestOutput() {
            super(new int[] { 1 });
        }

        public boolean setOutputBusTest1() {
            return setOutputBus(new int[] {});
        }

        public boolean setOutputBusTest2() {
            return setOutputBus(new int[] {});
        }

        @Override
        public TestOutput clone() {
            return null;
        }
    }

    @Test
    void outputs() {
        TestOutput i = new TestOutput();

        assertEquals(0, i.getOutputNumber());
        assertEquals(0, i.getOutputCable().size());

        assertFalse(i.setOutputBusTest1());
        assertFalse(i.setOutputBusTest2());
    }

    @Test
    void getState() {
        TestOutput i = new TestOutput();
        assertNull(i.getState());
    }
}
