package com.pjava.src.UI.components;

import com.pjava.src.components.gates.And;

import javafx.fxml.FXML;

public class UIAnd extends UIGate {

    @FXML
    private Pin input1;
    @FXML
    private Pin input2;
    @FXML
    private Pin output1;

    public UIAnd() {
        super("UIAnd");
        setLogic(new And());
        output1.setAsInput(false);
    }

    @Override
    public And getLogic() {
        return (And) super.getLogic();
    }

    public void setLogic(And and) {
        super.setLogic(and);
    }
}
