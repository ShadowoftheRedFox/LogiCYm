package com.pjava.src.UI.components;

import java.util.ArrayList;
import java.util.List;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;

import javafx.geometry.Point2D;

public abstract class UIGate extends UIElement {
    /**
     * width of the gate
     */
    private int width = 0;
    /** height of the gate */
    private int height = 0;
    /**
     * List of pins that are the inputs of this gate
     */
    protected List<Pin> inputPins = new ArrayList<>();
    /**
     * List of pins that are the outputs of this gate
     */
    protected List<Pin> outputPins = new ArrayList<>();
    /**
     * List of cables connect to this gate
     */
    private List<UICable> connectedCables = new ArrayList<>();

    @Override
    public void updateVisuals() {
        for (UICable connectedCables : getConnectedCables()) {
            connectedCables.updateVisuals();
        }
    }

    /**
     * add the given cable to the connected list. it does not connect the cable to
     * this gate, only this gate has a reference of this cable.
     *
     * @param cable the cable to add
     */
    public void addConnectedCable(UICable cable) {
        if (!connectedCables.contains(cable)) {
            connectedCables.add(cable);
        }
    }

    /**
     * disconnect the cable from this gate, and the gate from the cable.
     *
     * @param cable the cable to disconnect from.
     */
    public void disconnect(UICable cable) {
        if (cable == null) {
            return;
        }
        cable.disconnect(this);
        connectedCables.remove(cable);
    }

    /**
     * disconnect all cables from this gate, and the gate from every cable
     */
    public void disconnect() {
        for (UICable connectedCables : connectedCables) {
            disconnect(connectedCables);
        }
    }

    // #region Getters
    /**
     * @return return the width of the gate
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return return the height of the gate
     */
    public int getHeight() {
        return height;
    }

    /**
     * returns the list of pins input of the gate
     *
     * @return (list of input pins)
     */
    public List<Pin> getInputPins() {
        return inputPins;
    }

    /**
     * returns the list of pins output
     *
     * @return ( list of output pins)
     */
    public List<Pin> getOutputPins() {
        return outputPins;
    }

    /**
     * to get the UICable connected
     *
     * @return a UICable
     */
    public List<UICable> getConnectedCables() {
        return connectedCables;
    }

    /**
     * used to get all the cable connecte to the gate sended
     *
     * @return List of Cable connected to a Gate
     */
    public List<Cable> getConnectedLogicCables() {
        List<Cable> cablesLogic = new ArrayList<>();
        for (UICable cable : connectedCables) {
            cablesLogic.add((Cable) cable.getLogic());
        }
        return cablesLogic;
    }

    @Override
    public Gate getLogic() {
        return (Gate) super.getLogic();
    }

    /**
     * to get a single pinInput
     *
     * @param index which one do you want (0 to N)
     * @return the pin selected
     */
    public Pin getPinInput(int index) {
        return inputPins.get(index);
    }

    /**
     * to get a single pinOutput
     *
     * @param index which one do you want (from 0 to N)
     * @return the Output pin selected
     */
    public Pin getPinOutput(int index) {
        return outputPins.get(index);
    }
    // #endregion

    // #region Setters
    /**
     * to modifie the width
     *
     * @param width (width of the gate you want to put)
     */
    protected void setWidth(int width) {
        this.width = width;
    }

    /**
     * to modifie the height
     *
     * @param height (height of the gate you want to put)
     */
    protected void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setPosition(Point2D position) {
        super.setPosition(position);
    }
    // #endregion
}
