package com.pjava.components.input;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.pjava.src.components.input.Button;

public class ButtonTest {
    @Test
    void buttonConstructor() {
        assertThrows(Exception.class, () -> {
            new Button(0l);
        });

        assertThrows(Exception.class, () -> {
            new Button(0l);
        });

        assertDoesNotThrow(() -> {
            new Button();
            new Button(10l);
            new Button(10l);
        });
    }

    @Test
    void press() throws Exception {
        Button button = new Button();
        assertFalse(button.getState(0));
        button.press();
        assertTrue(button.getState(0));
    }

    @Test
    void release() throws Exception {
        final long delay = 10l;
        Button button = new Button(delay);
        button.press();

        final long now = System.currentTimeMillis();
        while (System.currentTimeMillis() - now <= delay) {
        }

        button.release();
        assertFalse(button.getState(0));
    }
}
