package com.pjava.src.UI.components;

import java.util.ArrayList;
import java.util.List;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Element;

import javafx.geometry.Point2D;

public abstract class UIGate extends UIElement {
    private int width = 0;
    private int height = 0;
    private List<UICable> connectedCables = new ArrayList<>();

    // #region Getters
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    // #endregion

    // #region Setters
    protected void setWidth(int width) {
        this.width = width;
    }

    protected void setHeight(int height) {
        this.height = height;
    }
    // #endregion

    public void addConnectedCable(UICable cable) {
        if (!connectedCables.contains(cable)) {
            connectedCables.add(cable);
        }
    }

    public void removeConnectedCable(UICable cable) {
        connectedCables.remove(cable);
    }

    public List<UICable> getConnectedCables() {
        return connectedCables;
    }

    /**
     * Met à jour la position de tous les câbles connectés
     */
    public void updateConnectedCables() {
        for (UICable cable : connectedCables) {
            cable.updateCablePosition();
        }
    }

    public List<Cable> getConnectedLogicCables() {
        List<Cable> cablesLogic = new ArrayList<>();
        for (UICable cable : connectedCables) {
            cablesLogic.add(cable.getLogic());
        }
        return cablesLogic;
    }

    @Override
    public void setPosition(Point2D position) {
        super.setPosition(position);
        updateConnectedCables();
    }

    @Override
    public Element getLogic() {
        return (Element) super.getLogic();
    }
}
