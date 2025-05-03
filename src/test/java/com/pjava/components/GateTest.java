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
    class testGate extends Gate {
        testGate() {
            super();
        }

        testGate(int[] busInput, int[] busOutput) {
            super(busInput, busOutput);
        }

        @Override
        public BitSet getState() {
            return null;
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
            new testGate();
        });
        assertThrows(Error.class, () -> {
            new testGate(new int[] { -1 }, new int[] { 0 });
        });
        assertThrows(Error.class, () -> {
            new testGate(null, null);
        });
    }

    @Test
    void getState() {
        testGate g = new testGate();
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
        testGate g = new testGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        testGate h = new testGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        testGate i = new testGate(new int[] { 2 }, new int[] { 2 });
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
        testGate g = new testGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        testGate h = new testGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        testGate i = new testGate(new int[] { 2 }, new int[] { 2 });
        Cable c = g.connect(h);
        assertEquals(c, g.getCableWith(h));
        assertEquals(c, g.getCableWith(h, 1));
        assertEquals(null, g.getCableWith(null));
        assertEquals(null, g.getCableWith(i));
        assertEquals(null, g.getCableWith(i, 1));
    }

    @Test
    void addInputBus() throws BusSizeException {
        testGate g = new testGate(new int[] { 1, 1 }, new int[] { 1, 1 });
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
        testGate g = new testGate(new int[] { 1, 1 }, new int[] { 1, 1 });
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
        testGate g = new testGate(new int[] { 1, 1 }, new int[] { 1, 1 });
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
        testGate g = new testGate(new int[] { 1, 1 }, new int[] { 1, 1 });
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
        testGate g = new testGate();
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
        testGate g = new testGate();
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
        testGate g = new testGate();
        g.setPowered(false);
        assertEquals(false, g.getPowered());
        g.setPowered(true);
        assertEquals(true, g.getPowered());
    }

    @Test
    void getInputNumberAndBus() {
        testGate g = new testGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        assertEquals(2, g.getInputNumber());
        assertEquals(2, g.getOutputBus().length);
    }

    @Test
    void getOutputNumberAndBus() {
        testGate g = new testGate(new int[] { 1, 1 }, new int[] { 1, 1 });
        assertEquals(2, g.getOutputNumber());
        assertEquals(2, g.getOutputBus().length);
    }

    @Test
    void gateEquals() {
        testGate g = new testGate();
        testGate h = new testGate();
        assertEquals(g, g);
        assertNotEquals(h, g);
        assertNotEquals(null, g);
    }

    @Test
    void gateHashCode() {
        testGate g = new testGate();
        testGate h = new testGate();
        assertEquals(g.hashCode(), g.hashCode());
        assertEquals(g.hashCode(), g.uuid());
        assertNotEquals(h, g);
    }
}
