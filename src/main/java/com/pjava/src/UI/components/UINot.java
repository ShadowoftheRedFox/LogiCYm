package com.pjava.src.UI.components;

import com.pjava.src.components.gates.Not;

public class UINot extends UIGate {
    public UINot() {
        super("UINot");
        setLogic(new Not());
    }

    @Override
    public Not getLogic() {
        return (Not) super.getLogic();
    }

    public void setLogic(Not not) {
        super.setLogic(not);
    }
}
