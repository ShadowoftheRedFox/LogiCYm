package com.pjava.components.cables;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.cables.BusSplitter;
import com.pjava.src.components.input.Power;

public class SplitterTest {
    @Test
    void splitter() throws Exception {
        Power power1 = new Power(1);
        BusSplitter splitter1 = new BusSplitter(1);
        power1.connect(splitter1);
        assertEquals(1, splitter1.getOutputNumber());

        Power power4 = new Power(4);
        BusSplitter splitter4 = new BusSplitter(4);
        power4.connect(splitter4);
        assertEquals(4, splitter4.getOutputNumber());
    }
}
