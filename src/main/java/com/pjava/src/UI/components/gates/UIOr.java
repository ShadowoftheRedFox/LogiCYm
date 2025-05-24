package com.pjava.src.UI.components.gates;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.Element;
import com.pjava.src.components.Element.ElementEvent;
import com.pjava.src.components.gates.Or;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class UIOr extends UIGate {
    @FXML
    private Pin input1Controller;
    @FXML
    private Pin input2Controller;
    @FXML
    private Pin output1Controller;

    @FXML
    private VBox input1;
    @FXML
    private VBox input2;
    @FXML
    private VBox output1;
    @FXML
    private ImageView body1;

    /**
     * creates an Or gate in fxml
     *
     * @return an UIElement of an OR gate
     */
    public static UIOr create() {
        return (UIOr) UIElement.create("UIOr");
    }

    @FXML
    private void initialize() {
        System.out.println("Initialisation!");
        setLogic(new Or());
        output1Controller.setAsInput(false);

        body1.setOnMousePressed(event -> pressed(event));
        body1.setOnMouseReleased(event -> released(event));
        body1.setOnMouseDragged(event -> dragged(event));

        input1Controller.originController = this;
        input2Controller.originController = this;
        output1Controller.originController = this;

        inputPins.add(input1Controller);
        inputPins.add(input2Controller);
        outputPins.add(output1Controller);


        getLogic().getUpdateEvents().add(new ElementEvent() {
            public void updateState(Element element) {
                updateVisuals();
            };

            public void updatePower(Element element) {
                updateVisuals();
            };
        });
    }

    @Override
    public Or getLogic() {
        return (Or) super.getLogic();
    }

    /**
     * set the Logic of a Or Gate
     *
     * @param or a Or gate
     */
    public void setLogic(Or or) {
        super.setLogic(or);
    }
}
