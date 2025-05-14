package com.pjava.components.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pjava.src.components.input.Clock;

public class ClockTest {
    @Test
    void clockConstructor() {
        Clock clock = new Clock(0l);
        assertEquals(100l, clock.getCycleSpeed());
    }

    @Test
    void instantCycle() {
        Clock clock = new Clock();
        assertFalse(clock.getState(0));
        clock.instantCycle();
        assertTrue(clock.getState(0));
    }

    @Test
    void getsetEnabled() {
        Clock clock = new Clock();
        assertFalse(clock.getEnabled());
        clock.setEnabled(true);
        assertTrue(clock.getEnabled());
        clock.setEnabled(false);
        assertFalse(clock.getEnabled());
    }

    @Test
    void timeCycle() {
        Clock clock = new Clock();
        final long delay = clock.getCycleSpeed();

        clock.timeCycle();
        assertFalse(clock.getState(0));

        clock.setEnabled(true);
        clock.timeCycle();
        assertTrue(clock.getState(0));

        final long now = System.currentTimeMillis();
        while (System.currentTimeMillis() - now <= delay - 20) {
        }

        clock.timeCycle();
        assertTrue(clock.getState(0));

        while (System.currentTimeMillis() - now <= delay) {
        }

        clock.timeCycle();
        assertFalse(clock.getState(0));
    }
}
