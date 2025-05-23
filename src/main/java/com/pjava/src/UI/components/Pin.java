package com.pjava.src.UI.components;

import java.util.ArrayList;
import java.util.Iterator;

import com.pjava.src.errors.BusSizeException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
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

    // Référence au mode de câblage actuel
    private static boolean cablingMode = false;

    @FXML
    private Rectangle pinCenter;
    @FXML
    private Arc pinArc1;
    @FXML
    private Arc pinArc2;

    @FXML
    private VBox container;

    public UIElement originController = null;

    private ArrayList<EventHandler<? super MouseEvent>> pressedListener = new ArrayList<EventHandler<? super MouseEvent>>();

    @FXML
    private void initialize() {
        setColor(color);
        container.setOnMousePressed(event -> pressed(event));
        container.setOnMouseReleased(event -> released(event));
        container.setOnMouseDragged(event -> dragged(event));
    }

    /**
     * if the user is in cabling mode (by pressing the "cable" button),
     * the two next input and output pins will be connected
     *
     * @param event On click
     */
    private void pressed(MouseEvent event) {
        changeColor(color.desaturate());

        // call all listener
        pressedListener.forEach(callback -> {
            callback.handle(event);
        });
    }

    /**
     * when the pins is released, it changes color
     *
     * @param event released events
     */
    private void released(MouseEvent event) {
        changeColor(color.saturate());
        setCablingMode(false);
    }

    /**
     * when a pin is dragges, it enable the cabling mode
     *
     * @param event the dragged mouse event
     */
    private void dragged(MouseEvent event) {
        setCablingMode(true);
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

    /**
     * changes the color of the pins
     *
     * @param color the color you want to put
     */
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

    /**
     * Activate the cablingmode
     */
    public void setCablingMode(boolean enabled) {
        cablingMode = enabled;
    }

    public boolean getCablingMode() {
        return cablingMode;
    }

    /**
     * give the center of a 2d point
     *
     * @return point2D center (x,y)
     */
    public Point2D getCenter() {
        double x = container.getLayoutX() + originController.getNode().getLayoutX();
        double y = container.getLayoutY() + originController.getNode().getLayoutY()
                + (container.getHeight() + pinArc1.getRadiusY()) * container.getScaleY() + 10;

        x += pinCenter.getWidth() / 2;
        y += pinCenter.getHeight() / 2;

        return new Point2D(x, y);
    }

    public void setOnPressed(EventHandler<? super MouseEvent> value) {
        if (!pressedListener.contains(value)) {
            pressedListener.add(value);
        }
    }

    public boolean removeOnPressed(EventHandler<? super MouseEvent> value) {
        return pressedListener.remove(value);
    }

    public Iterator<EventHandler<? super MouseEvent>> onPressed() {
        return pressedListener.iterator();
    }

    public Color getColor() {
        return color;
    }

    // Dans la classe Pin, ajoutez cette méthode:
    public static Pin create() {
        return (Pin) UIElement.create("Pin");
    }
}
