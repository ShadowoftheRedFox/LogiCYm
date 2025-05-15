package com.pjava.src.UI.components;

public abstract class UIGate extends UIElement {
    private int width = 0;
    private int height = 0;

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

    protected void setHeht(int height) {
        this.height = height;
    }
    // #endregion
}
