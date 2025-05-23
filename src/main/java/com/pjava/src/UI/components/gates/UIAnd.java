package com.pjava.src.UI.components.gates;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.Element;
import com.pjava.src.components.gates.And;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;

public class UIAnd extends UIGate {
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
    private Arc body1;
    @FXML
    private Rectangle body2;

    @FXML
    private AnchorPane self;

    /**
     * creates the And Gate in Fxml
     *
     * @return fmxl And Gate
     */
    public static UIAnd create() {
        return (UIAnd) UIElement.create("UIAnd");
    }

    @FXML
    private void initialize() {
        System.out.println("Initialisation!");
        setLogic(new And());
        output1Controller.setAsInput(false);

        body1.setOnMousePressed(event -> pressed(event));
        body1.setOnMouseReleased(event -> released(event));
        body1.setOnMouseDragged(event -> dragged(event));
        body2.setOnMousePressed(event -> pressed(event));
        body2.setOnMouseReleased(event -> released(event));
        body2.setOnMouseDragged(event -> dragged(event));

        input1Controller.originController = this;
        input2Controller.originController = this;
        output1Controller.originController = this;

        inputPins.add(input1Controller);
        inputPins.add(input2Controller);
        outputPins.add(output1Controller);

        // getLogic().getUpdateEvents().add(new ElementEvent() {
        //     void updateState(Element element) {
        //     }
        // });
        // TODO Listen to updateEvent in Element and call updateVisuals for both
    }

    @Override
    public And getLogic() {
        return (And) super.getLogic();
    }

    /**
     * set the logic to the and gate its supposed to have
     *
     * @param and a And gate(FXML)
     */
    private void setLogic(And and) {
        super.setLogic(and);
    }
}
