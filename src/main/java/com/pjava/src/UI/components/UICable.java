package com.pjava.src.UI.components;

import com.pjava.src.components.Cable;
import com.pjava.src.errors.BusSizeException;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class UICable extends UIElement {

    @FXML
    private Line cableLine;

    @FXML
    private AnchorPane self;
    /*
     * there will be two Pins, the source one from the sourceElement and the second one the targer Pin from the targetElement
     */
    private Pin sourcePin;
    private Pin targetPin;
    private UIElement sourceElement;
    private UIElement targetElement;
    /**
     * create an cable in FXML
     * @return an cable
     */
    public static UICable create() {
        return (UICable) UIElement.create("UICable");
    }

    /**
     *
     * @param node (a cable)
     * @return the controller controlling the cable
     */
    public static UICable getController(Node node) {
        Object controller = null;
        do {
            controller = node.getProperties().get("controller");
            node = node.getParent();
        } while (controller == null && node != null);
        return (UICable) controller;
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
     *  it creates a connection between the two pin as well as putting the target pin green.
     * @param source (pin source)
     * @param target (pin target)
     * @param sourceElem (gate source)
     * @param targetElem (gate target)
     */
    public void connect(Pin source, Pin target, UIElement sourceElem, UIElement targetElem) {
        if (source == null || target == null || sourceElem == null || targetElem == null) {
            return;
        }

        // 1 input to 1 output
        if ((source.isInput() && target.isInput()) || (!source.isInput() && !target.isInput())) {
            return;
        }

        // checking if its an output
        if (source.isInput()) {
            Pin temp = source;
            source = target;
            target = temp;

            UIElement tempElem = sourceElem;
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
    }

    /**
     * check the new position then take the center to determine the new cablelines ends and starts
     */
    public void updateCablePosition() {
        if (sourcePin == null || targetPin == null) {
            return;
        }

        // calculating position
        Point2D sourcePos = sourcePin.getCenter();
        Point2D targetPos = targetPin.getCenter();

        // updating
        cableLine.setStartX(sourcePos.getX());
        cableLine.setStartY(sourcePos.getY());
        cableLine.setEndX(targetPos.getX());
        cableLine.setEndY(targetPos.getY());
    }
    /**
     * reset the cable
     */
    public void disconnect() {
        if (targetPin != null) {
            // if disconnected => black again
            targetPin.setAsInput(true);
        }

        sourcePin = null;
        targetPin = null;
        sourceElement = null;
        targetElement = null;

        // let the cable go
        cableLine.setStartX(0);
        cableLine.setStartY(0);
        cableLine.setEndX(0);
        cableLine.setEndY(0);
    }
/**
 * gets the pin source
 * @return pin source
 */
    public Pin getSourcePin() {
        return sourcePin;
    }
/**
 * gets the target pin
 * @return pin targer
 */
    public Pin getTargetPin() {
        return targetPin;
    }
/**
 * gets the source element
 * @return source element
 */
    public UIElement getSourceElement() {
        return sourceElement;
    }
/**
 * gets the target element
 * @return target element
 */
    public UIElement getTargetElement() {
        return targetElement;
    }

}
