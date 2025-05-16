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

    private Pin sourcePin;
    private Pin targetPin;
    private UIElement sourceElement;
    private UIElement targetElement;

    public static Node create(Class<?> getClass) {
        return UIElement.create(getClass, "UICable");
    }

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
            setLogic(new Cable(1)); //busSize can be changed
        } catch (BusSizeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cable getLogic() {
        return (Cable) super.getLogic();
    }

    public void connect(Pin source, Pin target, UIElement sourceElem, UIElement targetElem) {
        if (source == null || target == null || sourceElem == null || targetElem == null) {
            return;
        }

        // 1 input to 1 output
        if (source.isInput() && target.isInput() || !source.isInput() && !target.isInput()) {
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

    public void updateCablePosition() {
        if (sourcePin == null || targetPin == null) {
            return;
        }

        // calculating position
        Point2D sourcePos = calculatePinPosition(sourcePin);
        Point2D targetPos = calculatePinPosition(targetPin);

        // updating
        cableLine.setStartX(sourcePos.getX());
        cableLine.setStartY(sourcePos.getY());
        cableLine.setEndX(targetPos.getX());
        cableLine.setEndY(targetPos.getY());
    }

    private Point2D calculatePinPosition(Pin pin) {
        Node pinNode = pin;
        double x = pinNode.getLayoutX();
        double y = pinNode.getLayoutY();

        // chekcing the roots
        Node parent = pinNode.getParent();
        while (parent != null && !(parent instanceof AnchorPane)) {
            x += parent.getLayoutX();
            y += parent.getLayoutY();
            parent = parent.getParent();
        }

        // ajusting the center
        x += pin.getWidth() / 2;
        y += pin.getHeight() / 2;

        return new Point2D(x, y);
    }

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

    public Pin getSourcePin() {
        return sourcePin;
    }

    public Pin getTargetPin() {
        return targetPin;
    }

    public UIElement getSourceElement() {
        return sourceElement;
    }

    public UIElement getTargetElement() {
        return targetElement;
    }
}
