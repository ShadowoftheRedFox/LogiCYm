package com.pjava.components;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.Cable;
import com.pjava.src.errors.BusSizeException;

public class CableTest {
    class TestCable extends Cable {
        TestCable(int size) throws BusSizeException {
            super(size);
        }

        @Override
        public void setPowered(boolean powered) {
            super.setPowered(powered);
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
        TestCable c = new TestCable(1);
        c.setPowered(false);
        assertEquals(false, c.getPowered());
        c.setPowered(true);
        assertEquals(true, c.getPowered());
    }

    @Test
    void getsetBusSize() throws BusSizeException {
        TestCable c = new TestCable(1);
        assertEquals(1, c.getBusSize());
        c.setBusSize(2);
        assertEquals(2, c.getBusSize());
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
        assertNotEquals(d, c);
    }
}
