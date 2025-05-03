package com.pjava.components;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.Cable;
import com.pjava.src.components.gates.NOT;
import com.pjava.src.errors.BusSizeException;

public class CableTest {
    class testCable extends Cable {
        testCable(int size) throws BusSizeException {
            super(size);
        }

        @Override
        protected void setBusSize(int busSize) throws BusSizeException {
            super.setBusSize(busSize);
        }
    }

    @Test
    void cableConstructor() {
        assertThrows(BusSizeException.class, () -> {
            new Cable(0);
        });
        assertDoesNotThrow(() -> {
            new Cable(1);
        });
    }

    @Test
    void getsetPowered() throws BusSizeException {
        Cable c = new Cable(1);
        c.setPowered(false);
        assertEquals(false, c.getPowered());
        c.setPowered(true);
        assertEquals(true, c.getPowered());
    }

    @Test
    void getsetBusSize() throws BusSizeException {
        testCable c = new testCable(1);
        assertEquals(1, c.getBusSize());
        c.setBusSize(2);
        assertEquals(2, c.getBusSize());
    }

    @Test
    void getInputNumberAndBus() throws BusSizeException {
        Cable c = new Cable(2);
        assertEquals(0, c.getInputNumber());
        assertEquals(0, c.getInputGate().size());
        NOT n1 = new NOT();
        NOT n2 = new NOT();
        Cable d = n1.connect(n2);
        assertEquals(1, d.getInputNumber());
        assertEquals(1, d.getInputGate().size());
    }

    @Test
    void getOutputNumberAndBus() throws BusSizeException {
        Cable c = new Cable(2);
        assertEquals(0, c.getOutputNumber());
        assertEquals(0, c.getOutputGate().size());
        NOT n1 = new NOT();
        NOT n2 = new NOT();
        Cable d = n1.connect(n2);
        assertEquals(1, d.getOutputNumber());
        assertEquals(1, d.getOutputGate().size());
    }

    @Test
    void gateEquals() throws BusSizeException {
        Cable c = new Cable(1);
        Cable d = new Cable(1);
        assertEquals(c, c);
        assertNotEquals(d, c);
        assertNotEquals(null, c);
    }

    @Test
    void gateHashCode() throws BusSizeException {
        Cable c = new Cable(1);
        Cable d = new Cable(1);
        assertEquals(c.hashCode(), c.hashCode());
        assertEquals(c.hashCode(), c.uuid());
        assertNotEquals(d, c);
    }
}
