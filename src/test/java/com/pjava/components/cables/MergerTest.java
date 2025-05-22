package com.pjava.components.cables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.pjava.src.components.cables.Merger;
import com.pjava.src.components.input.Power;

public class MergerTest {
    @Test
    void merger() throws Exception {
        Power power1 = new Power();
        Power power2 = new Power();

        Merger merger2 = new Merger(new int[] { 1, 1 });

        power1.connect(merger2);
        power2.connect(merger2);

        assertTrue(merger2.getState(0));
        assertTrue(merger2.getState(1));
    }
}
