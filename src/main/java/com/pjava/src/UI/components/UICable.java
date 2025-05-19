package com.pjava.src.UI.components;

import com.pjava.src.components.Cable;
import com.pjava.src.errors.BusSizeException;

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
     * The source pin controller.
     */
    private Pin sourcePin = null;
    /**
     * The target pin controller.
     */
    private Pin targetPin = null;
    /**
     * The gate controller that owns the {@link #sourcePin}.
     */
    private UIGate sourceElement = null;
    /**
     * The gate controller that owns the {@link #targetPin}.
     */
    private UIGate targetElement = null;

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
        try {
            setLogic(new Cable(1)); // busSize can be changed
        } catch (BusSizeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cable getLogic() {
        return (Cable) super.getLogic();
    }

    /**
     * It creates a connection between the two pin as well as putting the target pin
     * green.
     *
     * @param source     A source pin.
     * @param target     A target pin.
     * @param sourceElem The controller that owns the source pin.
     * @param targetElem The controller that owns the target pin.
     * @throws NullPointerException Throws if any of the parameters is null.
     */
    public void connect(Pin source, Pin target, UIGate sourceElem, UIGate targetElem) {
        if (source == null) {
            throw new NullPointerException("Expected source to be an instance of Pin, received null");
        }
        if (target == null) {
            throw new NullPointerException("Expected target to be an instance of Pin, received null");
        }
        if (sourceElem == null) {
            throw new NullPointerException("Expected sourceElem to be an instance of UIGate, received null");
        }
        if (targetElem == null) {
            throw new NullPointerException("Expected targetElem to be an instance of UIGate, received null");
        }

        // fails if we connect an input to input or an output to an output
        if ((source.isInput() && target.isInput()) || (!source.isInput() && !target.isInput())) {
            return;
        }

        // swapping depending on the pin role
        if (source.isInput()) {
            Pin temp = source;
            source = target;
            target = temp;

            UIGate tempElem = sourceElem;
            sourceElem = targetElem;
            targetElem = tempElem;
        }

        this.sourcePin = source;
        this.targetPin = target;
        this.sourceElement = sourceElem;
        this.targetElement = targetElem;

        updateCablePosition();

        // if its connected it becomes green
        target.setColor(Color.GREEN);
        source.setColor(Color.GREEN);
    }

    /**
     * check the new position then take the center to determine the new cablelines
     * ends and starts
     */
    public void updateCablePosition() {
        // calculating position then update position
        if (sourcePin != null) {
            Point2D sourcePos = sourcePin.getCenter();
            cableLine.setStartX(sourcePos.getX());
            cableLine.setStartY(sourcePos.getY());
        }
        if (targetPin != null) {
            Point2D targetPos = targetPin.getCenter();
            cableLine.setEndX(targetPos.getX());
            cableLine.setEndY(targetPos.getY());
        }
    }

    /**
     * reset the cable
     */
    public void disconnect() {
        sourcePin = null;
        targetPin = null;
        sourceElement = null;
        targetElement = null;

        // hide the cable
        cableLine.setVisible(false);
    }

    /**
     * Getter for {@link #sourcePin}.
     *
     * @return The starting pin.
     */
    public Pin getSourcePin() {
        return sourcePin;
    }

    /**
     * Getter for {@link #targetPin}.
     *
     * @return The target pin.
     */
    public Pin getTargetPin() {
        return targetPin;
    }

    /**
     * Getter for {@link #sourceElement}, which is the controller where the
     * {@link #sourcePin} is attached to.
     *
     * @return The gate controller.
     */
    public UIGate getSourceElement() {
        return sourceElement;
    }

    /**
     * Getter for {@link #targetElement}, which is the controller where the
     * {@link #targetPin} is attached to.
     *
     * @return The gate controller.
     */
    public UIGate getTargetElement() {
        return targetElement;
    }

}
