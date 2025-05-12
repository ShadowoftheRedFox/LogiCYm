package com.pjava.components;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.BitSet;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;
import com.pjava.src.errors.BusSizeException;

public class GateTest {
    class TestGate extends Gate {
        TestGate() {
            super();
        }

        TestGate(int[] busInput, int[] busOutput) {
            super(busInput, busOutput);
        }

        @Override
        public BitSet getState() {
            return null;
        }

        @Override
        protected void setPowered(boolean powered) {
            super.setPowered(powered);
        }

        @Override
        public void addInputBus(int size) throws BusSizeException {
            super.addInputBus(size);
        }

        @Override
        public void removeInputBus(int index) throws IllegalArgumentException {
            super.removeInputBus(index);
        }

        @Override
        public void addOutputBus(int size) throws BusSizeException {
            super.addOutputBus(size);
        }

        @Override
        public void removeOutputBus(int index) throws IllegalArgumentException {
            super.removeOutputBus(index);
        }

        @Override
        protected boolean setInputBus(int[] busSizes) throws BusSizeException, NullPointerException {
            return super.setInputBus(busSizes);
        }

        @Override
        protected boolean setInputBus(int busSize, int index)
                throws BusSizeException, IndexOutOfBoundsException, NullPointerException {
            return super.setInputBus(busSize, index);
        }

        @Override
        protected boolean setOutputBus(int[] busSizes) throws BusSizeException, NullPointerException {
            return super.setOutputBus(busSizes);
        }

        @Override
        protected boolean setOutputBus(int busSize, int index)
                throws BusSizeException, IndexOutOfBoundsException, NullPointerException {
            return super.setOutputBus(busSize, index);
        }
    }

    @Test
    void gateContructor() {
        assertDoesNotThrow(() -> {
            new TestGate();
        });
        assertThrows(Error.class, () -> {
            new TestGate(new int[] { -1 }, new int[] { 0 });
        });
        assertThrows(Error.class, () -> {
            new TestGate(null, null);
        });
    }

    @Test
    void getState() {
        TestGate g = new TestGate();
        assertNull(g.getState());
        assertThrows(IllegalArgumentException.class, () -> {
            g.getState(-1);
        });
        assertThrows(Error.class, () -> {
            g.getState(0);
        });
    }

    @Test
    void connect() throws NullPointerException, IndexOutOfBoundsException, BusSizeException, Error {
        TestGate g = new TestGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        TestGate h = new TestGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        TestGate i = new TestGate(new int[] { 2 }, new int[] { 2 });
        assertThrows(NullPointerException.class, () -> {
            g.connect(null);
        });
        assertThrows(NullPointerException.class, () -> {
            g.connect(null, 0, 0);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            g.connect(h, -1, -1);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            g.connect(h, 2, 2);
        });
        assertThrows(BusSizeException.class, () -> {
            g.connect(i, 0, 0);
        });
        Cable c = g.connect(h);
        Cable d = g.connect(h, 1, 1);
        assertInstanceOf(Cable.class, c);
        assertEquals(c, g.connect(h));
        assertEquals(c, g.connect(h, 0, 0));
        assertEquals(d, g.connect(h, 1, 1));
    }

    @Test
    void getCableWith() {
        TestGate g = new TestGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        TestGate h = new TestGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        TestGate i = new TestGate(new int[] { 2 }, new int[] { 2 });
        Cable c = g.connect(h);
        assertEquals(c, g.getCableWith(h));
        assertEquals(c, g.getCableWith(h, 1));
        assertEquals(null, g.getCableWith(null));
        assertEquals(null, g.getCableWith(i));
        assertEquals(null, g.getCableWith(i, 1));
    }

    @Test
    void addInputBus() throws BusSizeException {
        TestGate g = new TestGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        assertThrows(BusSizeException.class, () -> {
            g.addInputBus(0);
        });
        assertDoesNotThrow(() -> {
            g.addInputBus(1);
        });
        assertEquals(3, g.getInputNumber());
    }

    @Test
    void addOutputBus() throws BusSizeException {
        TestGate g = new TestGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        assertThrows(BusSizeException.class, () -> {
            g.addOutputBus(0);
        });
        assertDoesNotThrow(() -> {
            g.addOutputBus(1);
        });
        assertEquals(3, g.getOutputNumber());
    }

    @Test
    void removeInputBus() {
        TestGate g = new TestGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        assertThrows(IllegalArgumentException.class, () -> {
            g.removeInputBus(-1);
        });
        assertDoesNotThrow(() -> {
            g.removeInputBus(1);
        });
        assertEquals(1, g.getInputNumber());
    }

    @Test
    void removeOutputBus() {
        TestGate g = new TestGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        assertThrows(IllegalArgumentException.class, () -> {
            g.removeOutputBus(-1);
        });
        assertDoesNotThrow(() -> {
            g.removeOutputBus(1);
        });
        assertEquals(1, g.getOutputNumber());
    }

    @Test
    void setInputBus() {
        TestGate g = new TestGate();
        assertThrows(NullPointerException.class, () -> {
            g.setInputBus(null);
        });
        assertThrows(BusSizeException.class, () -> {
            g.setInputBus(new int[] { 0, 3 });
        });
        assertDoesNotThrow(() -> {
            g.setInputBus(new int[] { 2 });
        });
        assertThrows(BusSizeException.class, () -> {
            g.setInputBus(0, 0);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            g.setInputBus(1, -1);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            g.setInputBus(1, 2);
        });
        assertDoesNotThrow(() -> {
            g.setInputBus(1, 0);
        });
    }

    @Test
    void setOutputBus() {
        TestGate g = new TestGate();
        assertThrows(NullPointerException.class, () -> {
            g.setOutputBus(null);
        });
        assertThrows(BusSizeException.class, () -> {
            g.setOutputBus(new int[] { 0, 3 });
        });
        assertDoesNotThrow(() -> {
            g.setOutputBus(new int[] { 2 });
        });
        assertThrows(BusSizeException.class, () -> {
            g.setOutputBus(0, 0);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            g.setOutputBus(1, -1);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            g.setOutputBus(1, 2);
        });
        assertDoesNotThrow(() -> {
            g.setOutputBus(1, 0);
        });
    }

    @Test
    void getsetPowered() {
        TestGate g = new TestGate();
        g.setPowered(false);
        assertEquals(false, g.getPowered());
        g.setPowered(true);
        assertEquals(true, g.getPowered());
    }

    @Test
    void getInputNumberAndBus() {
        TestGate g = new TestGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        assertEquals(2, g.getInputNumber());
        assertEquals(2, g.getOutputBus().length);
    }

    @Test
    void getOutputNumberAndBus() {
        TestGate g = new TestGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        assertEquals(2, g.getOutputNumber());
        assertEquals(2, g.getOutputBus().length);
    }

    @Test
    void gateEquals() {
        TestGate g = new TestGate();
        TestGate h = new TestGate();
        assertEquals(g, g);
        assertNotEquals(h, g);
        assertNotEquals(null, g);
    }

    @Test
    void gateHashCode() {
        TestGate g = new TestGate();
        TestGate h = new TestGate();
        assertNotEquals(h, g);
    }
}
