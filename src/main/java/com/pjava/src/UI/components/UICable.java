package com.pjava.src.UI.components;

import java.util.Objects;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;
import com.pjava.src.utils.UIUtlis;

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
    private UIGate sourceGate = null;
    /**
     * The gate controller that owns the {@link #targetPin}.
     */
    private UIGate targetGate = null;

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
        cableLine.setVisible(false);
        // all cables anchor pane element are snapped to (0,0) to match other elements
        // position without having to make obscure calculations
        self.setLayoutX(0);
        self.setLayoutY(0);
        cableLine.setLayoutX(0);
        cableLine.setLayoutY(0);
        cableLine.setStroke(Color.BLACK);
        cableLine.setFill(Color.BLACK);
        updateCableColor();
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
     * @param sourceGate The controller that owns the source pin.
     * @param targetGate The controller that owns the target pin.
     * @throws NullPointerException Throws if any of the parameters is null.
     */
    public void connect(Pin source, Pin target, UIGate sourceGate, UIGate targetGate) {
        if (source == null) {
            throw new NullPointerException("Expected source to be an instance of Pin, received null");
        }
        if (target == null) {
            throw new NullPointerException("Expected target to be an instance of Pin, received null");
        }
        // check pins are linked to a gate
        if (source.originController == null || target.originController == null) {
            throw new Error("Stand alone pin");
        }
        // fails if we connect an input to input or an output to an output
        if ((source.isInput() && target.isInput()) || (!source.isInput() && !target.isInput())) {
            return;
        }
        if (sourceGate == null) {
            throw new NullPointerException("Expected sourceElem to be an instance of UIGate, received null");
        }
        if (targetGate == null) {
            throw new NullPointerException("Expected targetElem to be an instance of UIGate, received null");
        }

        // swapping depending on the pin role
        if (source.isInput()) {
            Pin temp = source;
            source = target;
            target = temp;

            UIGate tempElem = sourceGate;
            sourceGate = targetGate;
            targetGate = tempElem;
        }

        // link front
        this.sourcePin = source;
        this.targetPin = target;
        this.sourceGate = sourceGate;
        this.targetGate = targetGate;

        // tell gate that this is the cable controller
        sourceGate.addConnectedCable(this);
        targetGate.addConnectedCable(this);

        // link to back
        try {
            Cable cable = ((Gate) sourceGate.getLogic()).connect((Gate) targetGate.getLogic());
            if (!Objects.equals(cable, getLogic())) {
                setLogic(cable);
            }
        } catch (Exception e) {
            UIUtlis.errorPopup(e.getMessage());
            e.printStackTrace();
        }

        updateCablePosition();

        // if its connected it becomes green
        target.setColor(Color.GREEN);
        source.setColor(Color.GREEN);

        // connect, so cable should be visible
        cableLine.setVisible(true);

        // call update on both gates
        sourceGate.updateVisuals();
        targetGate.updateVisuals();
    }

    @Override
    public void updateVisuals() {
        // TODO update own style with multiple cable size
        if (targetGate != null && updateCableColor()) {
            targetGate.updateVisuals();
        }
    }

    /**
     * check the new position then take the center to determine the new cablelines
     * ends and starts
     */
    private void updateCablePosition() {
        Point2D sourcePos = new Point2D(0, 0);
        Point2D targetPos = new Point2D(0, 0);
        // calculating position then update position
        if (sourcePin != null) {
            sourcePos = sourcePin.getCenter();
            cableLine.setStartX(sourcePos.getX());
            cableLine.setStartY(sourcePos.getY());
        }
        if (targetPin != null) {
            targetPos = targetPin.getCenter();
            cableLine.setEndX(targetPos.getX());
            cableLine.setEndY(targetPos.getY());
        }
        // stretch the width and heigth
        // self.setMinWidth(Math.max(sourcePos.getX(), targetPos.getX()));
        // self.setMinHeight(Math.max(sourcePos.getY(), targetPos.getY()));
        self.setPrefWidth(Math.max(sourcePos.getX(), targetPos.getX()));
        self.setPrefHeight(Math.max(sourcePos.getY(), targetPos.getY()));

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
        Color color = Color.RED;

        if (getLogic() != null) {
            // TODO check for multiple lines
            if (getLogic().getState(0)) {
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
        sourcePin = null;
        targetPin = null;
        sourceGate = null;
        targetGate = null;

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
     * Getter for {@link #sourceGate}, which is the controller where the
     * {@link #sourcePin} is attached to.
     *
     * @return The gate controller.
     */
    public UIGate getSourceGate() {
        return sourceGate;
    }

    /**
     * Getter for {@link #targetGate}, which is the controller where the
     * {@link #targetPin} is attached to.
     *
     * @return The gate controller.
     */
    public UIGate getTargetGate() {
        return targetGate;
    }

    public Line getLine() {
        return cableLine;
    }

    protected void setLogic(Cable logic) throws NullPointerException {
        super.setLogic(logic);
    }
}
