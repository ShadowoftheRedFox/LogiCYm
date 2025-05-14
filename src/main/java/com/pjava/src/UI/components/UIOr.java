package com.pjava.src.UI.components;

import com.pjava.src.components.gates.Or;

public class UIOr extends UIGate {
    public UIOr() {
        super("UIOr");
        setLogic(new Or());
    }

    @Override
    public Or getLogic() {
        return (Or) super.getLogic();
    }

    public void setLogic(Or or) {
        super.setLogic(or);
    }
}
