package com.pjava.src.UI.components;

import java.util.Objects;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;
import com.pjava.src.utils.UIUtils;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class UICable extends UIElement {
    @FXML
    private Line cableLine;

    @FXML
    private AnchorPane self;

    /**
     * The input pin controller.
     */
    private Pin inputPin = null;
    /**
     * The output pin controller.
     */
    private Pin outputPin = null;
    /**
     * The gate controller that owns the {@link #inputPin}.
     */
    private UIGate inputGate = null;
    /**
     * The gate controller that owns the {@link #outputPin}.
     */
    private UIGate outputGate = null;

    /**
     * Create a new cable instance.
     *
     * @return The new cable instance.
     */
    public static UICable create() {
        return (UICable) UIElement.create("UICable");
    }

    @FXML
    private void initialize() {
        // all cables anchor pane element are snapped to (0,0) to match other elements
        // position without having to make obscure calculations
        self.setLayoutX(0);
        self.setLayoutY(0);
        cableLine.setLayoutX(0);
        cableLine.setLayoutY(0);
        cableLine.setStroke(Color.BLACK);
        cableLine.setFill(Color.BLACK);

        cableLine.setOnMousePressed(event -> pressed(event));
        cableLine.setOnMouseReleased(event -> released(event));
        cableLine.setOnMouseDragged(event -> dragged(event));

        // since on the cable only the line is selected, move the line a bit on spawn
        cableLine.setStartX(0);
        cableLine.setStartY(0);
        cableLine.setEndX(UIElement.baseSize);
        cableLine.setEndY(UIElement.baseSize);
        self.setPrefWidth(UIElement.baseSize);
        self.setPrefHeight(UIElement.baseSize);

        // TODO if dragged, disconnect
        // TODO when pressed then dragged, if starting pos is near the start/end of the
        // cable, change the cable start/end pos to the mous

        updateCableColor();
    }

    @Override
    public Cable getLogic() {
        return (Cable) super.getLogic();
    }

    /**
     * It creates a connection between the two pin as well as putting the output pin
     * green.
     *
     * @param input      A input pin.
     * @param output     A output pin.
     * @param inputGate  The controller that owns the input pin.
     * @param outputGate The controller that owns the output pin.
     * @throws NullPointerException Throws if any of the parameters is null.
     */
    public void connect(Pin input, Pin output, UIGate inputGate, UIGate outputGate) {
        if (input == null) {
            throw new NullPointerException("Expected input to be an instance of Pin, received null");
        }
        if (output == null) {
            throw new NullPointerException("Expected output to be an instance of Pin, received null");
        }
        // check pins are linked to a gate
        if (input.originController == null || output.originController == null) {
            throw new Error("Stand alone pin");
        }
        // fails if we connect an input to input or an output to an output
        if ((input.isInput() && output.isInput()) || (!input.isInput() && !output.isInput())) {
            return;
        }
        if (inputGate == null) {
            throw new NullPointerException("Expected inputElem to be an instance of UIGate, received null");
        }
        if (outputGate == null) {
            throw new NullPointerException("Expected outputElem to be an instance of UIGate, received null");
        }

        // swapping depending on the pin role
        if (input.isInput()) {
            Pin temp = input;
            input = output;
            output = temp;

            UIGate tempElem = inputGate;
            inputGate = outputGate;
            outputGate = tempElem;
        }

        // link front
        this.inputPin = input;
        this.outputPin = output;
        this.inputGate = inputGate;
        this.outputGate = outputGate;

        // tell gate that this is the cable controller
        inputGate.addConnectedCable(this);
        outputGate.addConnectedCable(this);

        // link to back
        try {
            Cable cable = ((Gate) inputGate.getLogic()).connect((Gate) outputGate.getLogic());
            if (!Objects.equals(cable, getLogic())) {
                setLogic(cable);
            }
        } catch (Exception e) {
            UIUtils.errorPopup(e.getMessage());
            e.printStackTrace();
        }

        updateCablePosition();

        // if its connected it becomes green
        input.setColor(Color.GREEN);
        output.setColor(Color.GREEN);

        // connect, so cable should be visible

        // call update on both gates
        inputGate.updateVisuals();
    }

    @Override
    public void updateVisuals() {
        // TODO update own style with multiple cable size
        if (outputGate != null && updateCableColor()) {
            outputGate.updateVisuals();
        }
    }

    /**
     * check the new position then take the center to determine the new cablelines
     * ends and starts
     */
    private void updateCablePosition() {
        Point2D inputPos = new Point2D(0, 0);
        Point2D outputPos = new Point2D(0, 0);
        // calculating position then update position
        if (inputPin != null) {
            inputPos = inputPin.getCenter();
            cableLine.setStartX(inputPos.getX());
            cableLine.setStartY(inputPos.getY());
        }
        if (outputPin != null) {
            outputPos = outputPin.getCenter();
            cableLine.setEndX(outputPos.getX());
            cableLine.setEndY(outputPos.getY());
        }
        // stretch the width and heigth
        // self.setMinWidth(Math.max(inputPos.getX(), outputPos.getX()));
        // self.setMinHeight(Math.max(inputPos.getY(), outputPos.getY()));
        self.setPrefWidth(Math.max(inputPos.getX(), outputPos.getX()));
        self.setPrefHeight(Math.max(inputPos.getY(), outputPos.getY()));

        System.out.println("cabling from "
                + cableLine.getStartX() + ":" + cableLine.getStartY() + " to "
                + cableLine.getEndX() + ":" + cableLine.getEndY());
    }

    /**
     * Update the color of the lines according to the logic cable.
     *
     * @return True if colors changed, false otherwise.
     */
    private boolean updateCableColor() {
        cableLine.strokeWidthProperty().set(5);
        Color color = Color.PURPLE;

        if (getLogic() != null) {
            // TODO check for multiple lines
            if (!getLogic().getPowered()) {
                color = Color.RED;
            } else if (getLogic().getState(0)) {
                color = Color.LIGHTGREEN;
            } else {
                color = Color.DARKGREEN;
            }
        }

        // check if color changed, if yes, then update must be propagated
        // kind of a old state, but saved in the color
        boolean res = cableLine.getFill() != color;

        cableLine.setStroke(color);
        cableLine.setFill(color);

        return res;
    }

    /**
     * reset the cable
     */
    public void disconnect() {
        inputPin = null;
        outputPin = null;
        inputGate = null;
        outputGate = null;
    }

    /**
     * Disconnect the given gate from this cable.
     * If you want to disconnect both side, calling disconnect on the cable is
     * enough.
     *
     * @param gate The gate to disconnect.
     */
    public void disconnect(UIGate gate) {
        if (gate == null) {
            return;
        }

        if (gate.equals(inputGate)) {
            inputGate = null;
            inputPin = null;
            gate.getLogic().disconnect(getLogic());
            updateVisuals();
        }

        if (gate.equals(outputGate)) {
            outputGate = null;
            outputPin = null;
            gate.getLogic().disconnect(getLogic());
            gate.updateVisuals();
        }
    }

    /**
     * Getter for {@link #inputPin}.
     *
     * @return The starting pin.
     */
    public Pin getInputPin() {
        return inputPin;
    }

    /**
     * Getter for {@link #outputPin}.
     *
     * @return The output pin.
     */
    public Pin getOutputPin() {
        return outputPin;
    }

    /**
     * Getter for {@link #inputGate}, which is the controller where the
     * {@link #inputPin} is attached to.
     *
     * @return The gate controller.
     */
    public UIGate getInputGate() {
        return inputGate;
    }

    /**
     * Getter for {@link #outputGate}, which is the controller where the
     * {@link #outputPin} is attached to.
     *
     * @return The gate controller.
     */
    public UIGate getOutputGate() {
        return outputGate;
    }

    public Line getLine() {
        return cableLine;
    }

    protected void setLogic(Cable logic) throws NullPointerException {
        super.setLogic(logic);
    }
}
