package com.pjava.src.UI.components.input;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.Element;
import com.pjava.src.components.Element.ElementEvent;
import com.pjava.src.components.input.Clock;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class UIClock extends UIGate {
    @FXML
    private Pin outputController;
    @FXML
    private Rectangle body1;
    @FXML
    private VBox output;
    @FXML
    private ImageView body2;
    @FXML
    private AnchorPane self;

    @FXML
    private void initialize() {
        System.out.println("Initialisation!");
        try {
            setLogic(new Clock());
        } catch (Exception e) {
        }
        outputController.setAsInput(false);
        body1.setOnMousePressed(event -> pressed(event));
        body1.setOnMouseReleased(event -> released(event));
        body1.setOnMouseDragged(event -> dragged(event));
        body2.setOnMousePressed(event -> pressed(event));
        body2.setOnMouseReleased(event -> released(event));
        body2.setOnMouseDragged(event -> dragged(event));

        outputController.originController = this;

        outputPins.add(outputController);


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
    public Clock getLogic() {
        return (Clock) super.getLogic();
    }
    /**
     * set the logic of a Clock Gate
     *
     * @param clock a clock gate
     */
    private void setLogic(Clock clock) {
        super.setLogic(clock);
    }

    @Override
    public Pin getPinInput(int index) {
        return null;
    }

    @Override
    public Pin getPinOutput(int index) {
        switch (index) {
            case 0:
                return outputController;
            default:
                return null;
        }
    }

    @Override
    protected void pressed(MouseEvent event) {
        getLogic().instantCycle();
        updateVisuals();
        super.pressed(event);
    }
}
