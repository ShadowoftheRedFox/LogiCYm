package com.pjava.components.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.BitSet;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.output.Output;

public class OutputTest {
    class TestOutput extends Output {
        TestOutput() {
            super(new int[] { 1 });
        }

        @Override
        public BitSet getState() {
            return null;
        }

        public boolean setOutputBusTest1() {
            return setOutputBus(new int[] {});
        }

        public boolean setOutputBusTest2() {
            return setOutputBus(new int[] {});
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
}
