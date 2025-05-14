package com.pjava.src.UI.components;

import java.io.IOException;

import com.pjava.src.errors.BusSizeException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;

/**
 * Create a pins that can resize if needed, and flip side depending on
 * {@link #isInput()}.
 */
public class Pin extends VBox {
    /**
     * True if the pins is used as input, false otherwise.
     */
    private boolean isInput = true;
    /**
     * The current busSize of the pins. Greater bus size means longer pins.
     */
    private int busSize = 1;

    @FXML
    private Rectangle pinCenter;
    @FXML
    private Arc pinArc1;
    @FXML
    private Arc pinArc2;

    /**
     * Create a new pins of said size.
     */
    public Pin(int busSize) {
        this(busSize, true);
    }

    /**
     * Create a new pins of said size and if it's an input or not.
     */
    public Pin(int busSize, boolean isInput) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/components/Pin.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            setBusSize(busSize);
            setAsInput(isInput);
        } catch (IOException | BusSizeException e) {
            throw new Error(e);
        }
    }

    /**
     * Change color of the pin.
     *
     * @param color The new color.
     * @throws NullPointerException Throws if color is null.
     */
    public void setColor(Color color) throws NullPointerException {
        if (color == null) {
            throw new NullPointerException("Expected color to be an instance of Color, received null");
        }
        pinCenter.setFill(color);
        pinArc1.setFill(color);
        pinArc2.setFill(color);
    }

    /**
     * Getter for {@link #busSize}.
     *
     * @return The bus size of the pin.
     */
    public int getBusSize() {
        return busSize;
    }

    /**
     * Setter for {@link #busSize}. Also change the height of the pin.
     *
     * @param busSize The new bus size.
     * @throws BusSizeException Throws if bus size if not between 1 and 32 included.
     */
    public void setBusSize(int busSize) throws BusSizeException {
        if (BusSizeException.isBusSizeException(busSize)) {
            throw BusSizeException.fromName("busSize", busSize);
        }

        this.busSize = busSize;
        pinCenter.setHeight((busSize - 1) * 50);
    }

    /**
     * Getter for {@link #isInput}
     *
     * @return True if this pin is used as an input. False otherwise.
     */
    public boolean isInput() {
        return isInput;
    }

    /**
     * Getter for the opposite of {@link #isInput}.
     *
     * @return True if this pin is used as an output. False otherwise.
     */
    public boolean isOutput() {
        return !isInput;
    }

    /**
     * Setter for {@link #isInput}.
     *
     * @param isInput True if this pin is used as input, false if as output.
     */
    public void setAsInput(boolean isInput) {
        this.isInput = isInput;
        this.setRotate(isInput ? 0 : 180);
    }
}
