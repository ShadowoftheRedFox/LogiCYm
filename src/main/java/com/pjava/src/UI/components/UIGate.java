package com.pjava.src.UI.components;

import java.util.ArrayList;
import java.util.List;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Element;

import javafx.geometry.Point2D;

public abstract class UIGate extends UIElement {
    /**
     * width of the gate
     */
    private int width = 0;
    /** height of the gate */
    private int height = 0;
    /** in the very end, there will be a list of input pins and output pins */
    protected List<Pin> inputPins = new ArrayList<>();
    protected List<Pin> outputPins = new ArrayList<>();
    /** as well as the pins, there will be a list for the cables */
    private List<UICable> connectedCables = new ArrayList<>();

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

    // #endregion
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
     * its in the name
     *
     * @param cable (a UICable)
     */
    public void addConnectedCable(UICable cable) {
        if (!connectedCables.contains(cable)) {
            connectedCables.add(cable);
        }
    }

    /**
     * in the name
     *
     * @param cable an UICable
     */
    public void removeConnectedCable(UICable cable) {
        connectedCables.remove(cable);
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
    public void setPosition(Point2D position) {
        super.setPosition(position);
    }

    @Override
    public Element getLogic() {
        return (Element) super.getLogic();
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
}
