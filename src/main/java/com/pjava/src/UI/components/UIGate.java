package com.pjava.src.UI.components;

public abstract class UIGate extends UIElement {
    private int width = 0;
    private int height = 0;

    protected UIGate(String fxml) {
        super(fxml);
    }

    // #region Getters
    public int getGateWidth() {
        return width;
    }

    public int getGateHeight() {
        return height;
    }
    // #endregion

    // #region Setters
    protected void setGateWiwidth(int width) {
        this.width = width;
    }

    protected void setGateHeight(int height) {
        this.height = height;
    }
    // #endregion
}
