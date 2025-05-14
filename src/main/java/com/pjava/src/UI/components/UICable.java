package com.pjava.src.UI.components;

import com.pjava.src.components.Cable;

public class UICable extends UIElement {
    private Integer lenght;

    public UICable() {
        super("UICable");
    }

    @Override
    public Cable getLogic() {
        return (Cable) super.getLogic();
    }

    // #region Getters
    public Integer getLenght() {
        return this.lenght;
    }

    // #endregion

    // #region Setters
    public void setLenght(Integer lenght) {
        this.lenght = lenght;
    }
    // #endregion
}
