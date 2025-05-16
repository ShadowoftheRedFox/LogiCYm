package com.pjava.components.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.BitSet;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;
import com.pjava.src.components.output.Display;

public class DisplayTest {

    class TestDisplay extends Display {
        public TestDisplay(int busSize, int base) throws Exception {
            super(busSize, base);
        }

        public void setInputCable(Cable cable) {
            if (getInputCable().size() == 0)
                getInputCable().add(cable);
            else
                getInputCable().set(0, cable);
        }
    }

    class CableTest extends Cable {
        public CableTest(int busSize) throws Exception {
            super(busSize);
        }

        public void setState(BitSet bit) {
            state = bit;
        }

        public void setOutputGate(Gate gate) {
            outputGate = gate;
        }
    }

    @Test
    void displays() throws Exception {
        TestDisplay i = new TestDisplay(5, 2);
        BitSet bit = new BitSet(5);
        bit.flip(0, 3);

        CableTest cable = new CableTest(5);
        cable.setState(bit);

        cable.setOutputGate(i);
        i.setInputCable(cable);
        assertEquals("00111", i.getOutput());

        i.setBaseOutput(8);
        assertEquals("07", i.getOutput());

        i.setBaseOutput(10);
        assertEquals("07", i.getOutput());

        i.setBaseOutput(16);
        assertEquals("07", i.getOutput());
    }
}
