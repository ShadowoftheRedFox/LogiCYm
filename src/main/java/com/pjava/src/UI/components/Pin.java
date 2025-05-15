package com.pjava.src.UI.components;

import com.pjava.src.errors.BusSizeException;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
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

    private Color color = Color.BLUE;

    @FXML
    private Rectangle pinCenter;
    @FXML
    private Arc pinArc1;
    @FXML
    private Arc pinArc2;

    @FXML
    private VBox container;

    @FXML
    private void initialize() {
        setColor(color);
        container.setOnMousePressed(event -> pressed(event));
        container.setOnMouseReleased(event -> released(event));
    }

    private void pressed(MouseEvent event) {
        changeColor(color.desaturate());
    }

    private void released(MouseEvent event) {
        changeColor(color.saturate());
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
        this.color = color;
        changeColor(color);
    }

    private void changeColor(Color color) {
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
        setColor(isInput ? Color.BLUE : Color.RED);
    }
}
